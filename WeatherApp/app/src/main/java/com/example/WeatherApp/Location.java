package com.example.WeatherApp;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.HashMap;

//this will be the location class; NetworkUtils will become FetchWeather
//this is a separate class and extends AsynchTask so that NetoworkUtils (or connection to the internet and the API) can be executed on a different thread
public class Location extends AsyncTask<String, Void, String> {
    private String locationName;
    private String actualTemp;
    private String feelLikeTemp;
    private String windSpeed;
    private String actualTempF;
    private String actualTempC;
    private String feelLikeTempF;
    private String feelLikeTempC;
    private String windSpeedMph;
    private String windSpeedKph;
    private String windDirection;
    private String condition;
    private String conditionImage = "https:";
    private String dayOfWeek;
    private HashMap<Integer,String> daysOfWeekMap = new HashMap<>();
    private String status = "Loading...";
    private int color = Color.BLACK;
    private FetchWeather fwObject;
    private WeakReference<TextView> statusTextview;
    private String[] fetchWeatherVars = new String[10];
    private String[] unitsUS = new String[3];
    private String[] unitsImp = new String[3];

    public Location(Context context, TextView statusText){
        fwObject = new FetchWeather(context);
        this.statusTextview = new WeakReference<>(statusText);
    }

    @Override
    //calls the query method getWeatherInfo from NetworkUtils
    //starts when FetchWeather.execute(String s) is called
    protected String doInBackground(String... strings) {
        return FetchWeather.getWeatherInfo(strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //s is the response
        //if it's invalid set the status and color variables to "Please enter a valid location" and red\
        if(s.contains("null")){
            statusTextview.get().setText("This location couldn't be found");
            statusTextview.get().setTextColor(Color.RED);
        }

        //if this location (location's name) already exists set the status and color variables to "This location already exists" and red
        //else do this stuff
        else{
            //if we do it here, we don't have to check for this stuff in the addLocation() method; it can also legitimately run without affecting the Main/UI thread
            //String s is the result form the FetchWeather class; it's the FetchWeatherVars array but as a string
            //removing the brackets from String s leaves it as a list of the variables separated by commas
            s = s.replace("[", "");
            s = s.replace("]", "");
            fetchWeatherVars = s.split(",");
            locationName = fetchWeatherVars[0];
            actualTempF = fetchWeatherVars[1];
            actualTempC = fetchWeatherVars[2];
            feelLikeTempF = fetchWeatherVars[3];
            feelLikeTempC = fetchWeatherVars[4];
            windSpeedMph = fetchWeatherVars[5];
            windSpeedKph = fetchWeatherVars[6];
            windDirection = fetchWeatherVars[7];
            condition = fetchWeatherVars[8];
            conditionImage += fetchWeatherVars[9].replace(" ", "");
            setupUnits();

            setDaysOfWeek();
            Calendar calendar = Calendar.getInstance();
            //returns an integer value between 1-7 that corresponds to the current day of the week
            int dayNum = calendar.get(Calendar.DAY_OF_WEEK);
            dayOfWeek = daysOfWeekMap.get(dayNum);

            //check if the location already exists
            //if it does then set the text to "this location already exists."
            if (MainActivity.exists(locationName)) {
                statusTextview.get().setText("This location already exists");
                statusTextview.get().setTextColor(Color.RED);
            }
            else {
                MainActivity.addLocation(this);
                fwObject.startNewScreen();
            }

        }
    }

    public void setupUnits(){
        unitsUS[0] = actualTempF;
        unitsUS[1] = feelLikeTempF;
        unitsUS[2] = windSpeedMph;
        unitsImp[0] = actualTempC;
        unitsImp[1] = feelLikeTempC;
        unitsImp[2] = windSpeedKph;

        actualTemp = unitsUS[0];
        feelLikeTemp = unitsUS[1];
        windSpeed = unitsUS[2];
    }

    public void setDaysOfWeek(){
        //map each int value to it's respective day of the week
        daysOfWeekMap.put(1,"Sunday");
        daysOfWeekMap.put(2,"Monday");
        daysOfWeekMap.put(3,"Tuesday");
        daysOfWeekMap.put(4,"Wednesday");
        daysOfWeekMap.put(5,"Thursday");
        daysOfWeekMap.put(6,"Friday");
        daysOfWeekMap.put(7,"Saturday");

    }

    public String getName(){
        return locationName;
    }

    public String getTemp() {
        return actualTemp;
    }

    public String getConditions() {
        return condition;
    }

    public String getConditionImage() {
        return conditionImage;
    }

    public String getDayOfWeek(){
        return dayOfWeek;
    }

    public String getStatusText(){
        return status;
    }

    public int getTextColor(){
        return color;
    }

    public void switchToUnitsImp(){
        actualTemp = unitsImp[0];
        feelLikeTemp = unitsImp[1];
        windSpeed = unitsImp[2];
    }
//
    public void switchToUnitsUS(){
        actualTemp = unitsUS[0];
        feelLikeTemp = unitsUS[1];
        windSpeed = unitsUS[2];
    }
}
