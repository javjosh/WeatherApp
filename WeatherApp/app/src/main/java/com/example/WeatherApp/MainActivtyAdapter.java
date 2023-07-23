package com.example.WeatherApp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivtyAdapter extends RecyclerView.Adapter<MainActivtyAdapter.ViewHolder>{
    private ArrayList<Location> locations;
    private Context mContext;
    MainActivtyAdapter(Context context, ArrayList<Location> locations) {
        this.locations = locations;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MainActivtyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //create another "list_item"-like xml file with the correct textviews
        //create a LocationAdapter class
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MainActivtyAdapter.ViewHolder holder, int position) {
        Location currLocation = locations.get(position);

        // Populate the textviews with data.
        holder.bindTo(currLocation);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView locationText;
        private TextView tempText;
        private TextView condsText;
        private TextView currDayText;
        private ImageView condsImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            locationText = itemView.findViewById(R.id.locationNameText);
            tempText = itemView.findViewById(R.id.temperatureText);
            condsText = itemView.findViewById(R.id.conditionsText);
            currDayText = itemView.findViewById(R.id.currentDayText);
            condsImage = itemView.findViewById(R.id.conditionImage);
            itemView.setOnClickListener(this);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        void bindTo(Location currLocation){
            // Populate the textviews with data.
            locationText.setText(currLocation.getName());
            tempText.setText(currLocation.getTemp());
            condsText.setText(currLocation.getConditions());
            currDayText.setText(currLocation.getDayOfWeek());
            Picasso.get().load(currLocation.getConditionImage()).into(condsImage);
        }

        @Override
        public void onClick(View view) {

        }
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }
}
