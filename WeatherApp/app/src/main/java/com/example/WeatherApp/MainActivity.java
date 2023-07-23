package com.example.WeatherApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
//have the empty list version be a completely different view

//the main or homescreen of the app
public class MainActivity extends AppCompatActivity {
    //stores the user's locations and is used to display them on the MainActivity
    private static ArrayList<Location> locations = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private MainActivtyAdapter mAdapater;
    private static boolean isDefaultUnit = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(locations.isEmpty()){
            setContentView(R.layout.empty_list_activity_main);
        }
        else{
            setContentView(R.layout.activity_main);
            mRecyclerView = findViewById(R.id.recyclerView);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mAdapater = new MainActivtyAdapter(this,locations);
            mRecyclerView.setAdapter(mAdapater);
            ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper
                    .SimpleCallback(
                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT |
                            ItemTouchHelper.DOWN | ItemTouchHelper.UP,
                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                @Override
                public boolean onMove(RecyclerView recyclerView,
                                      RecyclerView.ViewHolder viewHolder,
                                      RecyclerView.ViewHolder target) {
                    // Get the from and to positions.
                    int from = viewHolder.getAdapterPosition();
                    int to = target.getAdapterPosition();

                    // Swap the items and notify the adapter.
                    Collections.swap(locations, from, to);
                    mAdapater.notifyItemMoved(from, to);
                    return true;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder,
                                     int direction) {
                    // Remove the item from the dataset.
                    locations.remove(viewHolder.getAdapterPosition());
                    // Notify the adapter.
                    mAdapater.notifyItemRemoved(viewHolder.getAdapterPosition());
                    if(locations.isEmpty()){
                        setContentView(R.layout.empty_list_activity_main);
                    }
                }
            });
            // Attach the helper to the RecyclerView.
            helper.attachToRecyclerView(mRecyclerView);
            if(isDefaultUnit == false){
                findViewById(R.id.unitsUSButton).setVisibility(View.INVISIBLE);
                findViewById(R.id.unitsImpButton).setVisibility(View.VISIBLE);
            }
        }
    }

    public void launchAddCityActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), AddLocationActivity.class);
        startActivity(intent);
    }

    public static void addLocation(Location location){
        if(isDefaultUnit == false){
            location.switchToUnitsImp();
        }
        locations.add(location);
    }

    public void switchToUnitsImp(View view){
        for(int i = 0; i < locations.size(); i++){
            locations.get(i).switchToUnitsImp();
        }
        isDefaultUnit = false;
        mAdapater.notifyDataSetChanged();
        findViewById(R.id.unitsUSButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.unitsImpButton).setVisibility(View.VISIBLE);
    }

    public void switchToUnitsUS(View view){
        for(int i = 0; i < locations.size(); i++){
            locations.get(i).switchToUnitsUS();
        }
        isDefaultUnit = true;
        mAdapater.notifyDataSetChanged();
        findViewById(R.id.unitsImpButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.unitsUSButton).setVisibility(View.VISIBLE);
    }

    //checks if the location already exists in the locations arraylist
    public static boolean exists(String locationName){
        System.out.println(locations.size() + "");
        if(locations.isEmpty()){
            return false;
        }
        for(int i = 0; i < locations.size(); i++){
            if(locationName.equals(locations.get(i).getName())){
                return true;
            }
        }
        return false;
    }

    public static ArrayList<Location> getLocations(){
        return locations;
    }
}