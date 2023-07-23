package com.example.WeatherApp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;


//the view to add a new location
public class AddLocationActivity extends AppCompatActivity {
    private EditText locationInput;
    private String queryString = "";
    //the user's input is used to make the query to the API
    //we should have a method for the "Next" button that makes the query to the API with the user's input,
    //we then use the information that we get from the API to create a new Location object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_location);
        locationInput = findViewById(R.id.locationInput);
    }

    //the onClick for the Next Button
    public void doQuery(View view){
        queryString = locationInput.getText().toString();
        TextView statusText = findViewById(R.id.statusText);
        Location location = new Location(getApplicationContext(), statusText);
        //we have to use weak references
        //set the statusText Textview's color to the location object's color variable
        statusText.setTextColor(location.getTextColor());
        //set the statusText Textview's text to the location object's status variable
        statusText.setText(location.getStatusText());
        location.execute(queryString);
    }
}
