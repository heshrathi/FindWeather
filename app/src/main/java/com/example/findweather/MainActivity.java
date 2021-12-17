package com.example.findweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = findViewById(R.id.cityName_et);
    }

    public void SearchFun(View view) {
        String result = null;

        String cityNameText = cityName.getText().toString();

        DownloadWeather downloadWeather = new DownloadWeather();
        try {
            result = downloadWeather.execute("https://api.openweathermap.org/data/2.5/weather?q=" + cityNameText + "&appid=e7f1d25e003762a9c93bbcfa9f201a79").get();
            Log.i("Result: ", result);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public class DownloadWeather extends AsyncTask <String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection connection;
            String result = "";

            try {
                url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while (data != -1) {
                    result += (char) data;;
                    data = inputStreamReader.read();
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                String mainInfo = jsonObject.getString("main");

                Log.i("Weather Info", weatherInfo);
                Log.i("Main Info", mainInfo);

                JSONArray array1 = new JSONArray(weatherInfo);
                //JSONArray array2 = new JSONArray(mainInfo);

                for(int i=0; i<array1.length(); i++) {
                    JSONObject object = array1.getJSONObject(i);

                    Log.i("Main :", object.getString("main"));
                    Log.i("Description :", object.getString("description"));
                }
//                for(int j=0; j<array2.length(); j++) {
//                    JSONObject object = array2.getJSONObject(j);
//
//                    Log.i("temp_min :", object.getString("temp_min"));
//                    Log.i("temp_max :", object.getString("temp_max"));
//                    Log.i("pressure :", object.getString("pressure"));
//                    Log.i("humidity :", object.getString("humidity"));
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}