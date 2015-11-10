package com.example.jonas.myapplication;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static String urlFirst = "http://api.openweathermap.org/data/2.5/find?q=";
    private static String urlLast = "&type=like&cnt=10&units=metric&APPID=0edef5f231f8ba1d2b2f8a19ef2e882b";

    private TextView txt1;
    private Button searchButton;
    private EditText cityEdtText;
    private static final String TAG_LIST = "list";
    private static final String TAG_CITY_ID = "id";
    private static final String TAG_CITY_NAME = "name";
    private static final String TAG_SYS = "sys";
    private static final String TAG_MAIN = "main";
    private static final String TAG_TEMP = "temp";
    private static final String TAG_COUNTRY = "country";
    private static final String TAG_CITYID = "cityId";

    private String citySearch;

    private StableArrayAdapter adapter;
    private ArrayList<City> cityList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView listview = (ListView) findViewById(R.id.listview);

        cityList = new ArrayList<City>();
        adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, cityList);
        listview.setAdapter(adapter);

        txt1 = (TextView)findViewById(R.id.txt1);
        cityEdtText = (EditText)findViewById(R.id.cityEdtText);
        searchButton = (Button)findViewById(R.id.searchButton);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, final View view,
        int position, long id) {
            Intent intent = new Intent(getApplicationContext(), Detail_Activity.class);
            intent.putExtra(TAG_CITYID, String.valueOf(adapter.getItem(position).getId()));
            startActivity(intent);
        }
    });

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cityList.clear();
                adapter.clear();
                citySearch = cityEdtText.getText().toString();
                new JSONParse().execute();
            }
        });
    }

    private class StableArrayAdapter extends ArrayAdapter<City> {

        HashMap<City, Integer> mIdMap = new HashMap<City, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<City> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
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
                JSONArray list = json.getJSONArray(TAG_LIST);
                for(int i = 0; i< list.length();i++){
                    JSONObject jsonName = list.getJSONObject(i);
                    String cityName = jsonName.getString(TAG_CITY_NAME);
                    int cityId = jsonName.getInt(TAG_CITY_ID);
                    JSONObject jsonSys = jsonName.getJSONObject(TAG_SYS);
                    JSONObject jsonMain = jsonName.getJSONObject(TAG_MAIN);
                    Double temp = jsonMain.getDouble(TAG_TEMP);
                   String country = jsonSys.getString(TAG_COUNTRY);

                    City city = new City();
                    city.setId(cityId);
                    city.setName(cityName);
                    city.setCountry(country);
                    city.setTemp(temp);

                    adapter.add(city);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
