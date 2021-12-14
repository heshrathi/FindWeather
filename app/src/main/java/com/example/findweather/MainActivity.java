package com.example.findweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

    public class DownloadWeather extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection connection;

            String result = null;

            try {

                url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while (data != -1) {
                    result += (char) data;
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

            Log.i("JSON ", s);

            try {
                JSONObject jsonObject = new JSONObject(s);

//                JSONArray weather = jsonObject.getJSONArray("weather");
//                for(int i = 0;i < weather.length(); i++){
//                    JSONObject w = weather.getJSONObject(i);
//                    String main = w.getString("main");
//                    String description = w.getString("wind");
//                    //...
//                    Log.i("Weather Content: ", main);
//                    Log.i("Weather Content: ", description);
//                }

                JSONArray jsonArray = jsonObject.getJSONArray("weather");

                for(int i=0;i<jsonArray.length();i++){
                    JSONObject object=jsonArray.getJSONObject(i);
                    String main =object.getString("main");
                    Log.i("Weather: ", main);
                }


                //String weatherInfo = jsonObject.getString("main");
                //Log.i("Weather Content: ", weatherInfo);
            } catch (JSONException e) {
                Log.i("Exception:", String.valueOf(e));
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadWeather downloadWeather = new DownloadWeather();
        String result = null;

        try {
            result = downloadWeather.execute("https://api.openweathermap.org/data/2.5/weather?q=London&appid=e7f1d25e003762a9c93bbcfa9f201a79").get();
            Log.i("Result: ", "" + result);
            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        } catch (ExecutionException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }
}