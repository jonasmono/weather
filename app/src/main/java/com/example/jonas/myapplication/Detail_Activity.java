package com.example.jonas.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class Detail_Activity extends AppCompatActivity {

    private static String urlFirst = "http://api.openweathermap.org/data/2.5/forecast/daily?id=";
    private static String urlLast = "&cnt=3&units=metric&APPID=0edef5f231f8ba1d2b2f8a19ef2e882b";
    private String citySearch;
    private TextView cityText, condDescr, tempText, hum, press, windSpeedText, windDegText;
    private static final String TAG_LIST = "list";
    private static final String TAG_CITY = "city";
    private static final String TAG_CITY_NAME = "name";
    private static final String TAG_CITY_PRESSURE = "pressure";
    private static final String TAG_CITY_HUMIDITY = "humidity";
    private static final String TAG_CITY_TEMP = "temp";
    private static final String TAG_CITY_DAY = "day";
    private static final String TAG_CITY_WEATHER = "weather";
    private static final String TAG_CITY_DESCRIPTION = "description";
    private static final String TAG_CITY_SPEED = "speed";
    private static final String TAG_CITY_DEG = "deg";

    private static final String TAG_CITYID = "cityId";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_);
        cityText = (TextView) findViewById(R.id.cityText);
        condDescr = (TextView) findViewById(R.id.condDescr);
        tempText = (TextView) findViewById(R.id.temp);
        hum = (TextView) findViewById(R.id.hum);
        press = (TextView) findViewById(R.id.press);
        windSpeedText = (TextView) findViewById(R.id.windSpeed);
        windDegText = (TextView) findViewById(R.id.windDeg);
        Intent intent = getIntent();
        citySearch = intent.getStringExtra(TAG_CITYID);
        new JSONParse().execute();
}

private class JSONParse extends AsyncTask<String, String, JSONObject> {
    private ProgressDialog pd;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(Detail_Activity.this);
        pd.setMessage("Wait for data to load");
        pd.setIndeterminate(false);
        pd.setCancelable(true);
        pd.show();
    }

    @Override
    protected JSONObject doInBackground(String... args) {
        JSONParser jParser = new JSONParser();

        JSONObject json = jParser.getJSONFromUrl(urlFirst + citySearch + urlLast);
        return json;
    }
    @Override
    protected void onPostExecute(JSONObject json) {
        pd.dismiss();
        try {
            JSONObject jsonCity = json.getJSONObject(TAG_CITY);
            String cityName = jsonCity.getString(TAG_CITY_NAME);
            cityText.setText(cityName);

            JSONArray jsonList = json.getJSONArray(TAG_LIST);
            JSONObject temp = jsonList.getJSONObject(0);
            double pressure = temp.getDouble(TAG_CITY_PRESSURE);
            press.setText(" "+ String.valueOf(pressure));
            double humidity = temp.getDouble(TAG_CITY_HUMIDITY);
            hum.setText(" " + String.valueOf(humidity));

            JSONObject test = temp.getJSONObject(TAG_CITY_TEMP);
            String day = test.getString(TAG_CITY_DAY);
            tempText.setText(day);

            JSONArray weather = temp.getJSONArray(TAG_CITY_WEATHER);
            JSONObject firstWeather = weather.getJSONObject(0);
            String desc = firstWeather.getString(TAG_CITY_DESCRIPTION);
            condDescr.setText(desc);

            double windSpeed = temp.getDouble(TAG_CITY_SPEED);
            windSpeedText.setText(String.valueOf(windSpeed));
            double windDeg = temp.getDouble(TAG_CITY_DEG);
            windDegText.setText(String.valueOf(windDeg));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
}

