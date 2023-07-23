package com.example.WeatherApp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//a class to connect to the internet and make queries to the API
public class FetchWeather extends AppCompatActivity{
    private static String locationName; //{location} name
    private static String actualTempF; //{current} temp_f
    private static String actualTempC; //{current} temp_c
    private static String feelLikeTempF; //{current} feelslike_f
    private static String feelLikeTempC; //{current} feelslike_c
    private static String windSpeedMph; //{current} wind_mph
    private static String windSpeedKph; //{current} wind_kph
    private static String windDirection; //{current} wind_dir
    private static String condition; //{current: {condition}} text
    private static String conditionImage; //{current}: {condition} icon
    private Context addLocationActivityContext;
    private static String[] fetchWeatherVars = new String[10];
    //create the intent here and have a method that lets me start the activity from the Location class
    private Intent intent;

    public FetchWeather(Context context){
        addLocationActivityContext = context;
        intent = new Intent(addLocationActivityContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    }
    public static String getWeatherInfo(String queryString) {
        String weatherJSONString = null;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(prepareURL(queryString))
                    .get()
                    .addHeader("X-RapidAPI-Key", "cc6408ba79mshf300b9023c17db1p15af17jsn52842b5194c1")
                    .addHeader("X-RapidAPI-Host", "weatherapi-com.p.rapidapi.com")
                    .build();

            Response response = client.newCall(request).execute();
            weatherJSONString = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        parseJSON(weatherJSONString);
        return Arrays.toString(fetchWeatherVars);
    }

    public static String prepareURL(String queryString){
        queryString.replace(" ", "");
        String[] queryStringArr = queryString.split("");
        String url;

        for(int i = 0; i < queryStringArr.length; i++){
            if(queryStringArr[i].equals(",")){
                queryStringArr[i] = "%2C";
            }
            if(queryStringArr[i].equals(":")){
                queryStringArr[i] = "%3A";
            }
        }
        queryString = String.join("",queryStringArr);
        url = "https://weatherapi-com.p.rapidapi.com/forecast.json?q=" + queryString + "&days=3";

        return url;
    }

    public static void parseJSON(String JSONString){
        try {
            Log.d("jsonstr", JSONString);
            JSONObject jsonObject = new JSONObject(JSONString);
            //make sure it's reset for each query
            fetchWeatherVars = new String[10];
            //if the query was successful initialize fetchWeatherVars' values
            if(!(jsonObject.has("error"))){
                //set each index in fetchWeatherVars to a value from the resulting JSON object
                //the location object in the JSON result
                JSONObject locationJSONObject = jsonObject.getJSONObject("location");
                locationName = locationJSONObject.getString("name");
                fetchWeatherVars[0] = locationName;

                //the current object in the JSON result
                JSONObject currentJSONObject = jsonObject.getJSONObject("current");
                actualTempF = currentJSONObject.getString("temp_f");
                actualTempC = currentJSONObject.getString("temp_c");
                feelLikeTempF = currentJSONObject.getString("feelslike_f");
                feelLikeTempC = currentJSONObject.getString("feelslike_c");
                windSpeedMph = currentJSONObject.getString("wind_mph");
                windSpeedKph = currentJSONObject.getString("wind_kph");
                windDirection = currentJSONObject.getString("wind_dir");
                //round all of the double values to integers
                actualTempF = (int)(Math.floor(Double.valueOf(actualTempF))) + "\u00B0" + " F";
                actualTempC = (int)(Math.floor(Double.valueOf(actualTempC))) + "\u00B0" + " C";
                feelLikeTempF = (int)(Math.floor(Double.valueOf(feelLikeTempF))) + "\u00B0" + " F";
                feelLikeTempC =  (int)(Math.floor(Double.valueOf(feelLikeTempC))) + "\u00B0" + " C";
                windSpeedMph = (int)(Math.floor(Double.valueOf(windSpeedMph))) + " MPH";
                windSpeedKph = (int)(Math.floor(Double.valueOf(windSpeedKph))) + " KPH";
                fetchWeatherVars[1] = actualTempF;
                fetchWeatherVars[2] = actualTempC;
                fetchWeatherVars[3] = feelLikeTempF;
                fetchWeatherVars[4] = feelLikeTempC;
                fetchWeatherVars[5] = windSpeedMph;
                fetchWeatherVars[6] = windSpeedKph;
                fetchWeatherVars[7] = windDirection;

                //the condition object in the JSON result
                JSONObject conditionJSONObject = currentJSONObject.getJSONObject("condition");
                condition = conditionJSONObject.getString("text");
                conditionImage = conditionJSONObject.getString("icon");
                fetchWeatherVars[8] = condition;
                fetchWeatherVars[9] = conditionImage;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startNewScreen(){
        addLocationActivityContext.startActivity(intent);
    }




}
