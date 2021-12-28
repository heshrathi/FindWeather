package com.example.findweather;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText cityNameEditText;
    TextView cityNameText;
    TextView tempText;
    TextView minTempText;
    TextView maxTempText;
    TextView pressureText;
    TextView humidityText;
    ImageButton searchTempButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityNameEditText = findViewById(R.id.cityName_et);
        cityNameText = findViewById(R.id.cityName_tv);
        tempText = findViewById(R.id.temp_tv);
        minTempText = findViewById(R.id.minTemp_tv);
        maxTempText = findViewById(R.id.maxTemp_tv);
        pressureText = findViewById(R.id.pressure_tv);
        humidityText = findViewById(R.id.humidity_tv);
    }

    public void SearchFun(View view) {
        String result = null;

//        String cityNameTemp = cityNameEditText.getText().toString();
//        cityNameText.setText(cityNameTemp);
        String city = cityNameEditText.getText().toString();
        DownloadWeather downloadWeather = new DownloadWeather();
        try {
            result = downloadWeather.execute("https://api.openweathermap.org/data/2.5/weather?q="+ city +"&appid=e7f1d25e003762a9c93bbcfa9f201a79").get();
            //Log.i("Result: ", result);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public class DownloadWeather extends AsyncTask<String, Void, String> {

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

            try {
                //Json object for the result string of json data
                JSONObject jsonObject = new JSONObject(s);

                //weather and main of json data
                String weatherInfo = jsonObject.getString("weather");
                String mainInfo = jsonObject.getString("main");

                //result log for the weather and main of the result string of the json data
                Log.i("Weather Info", weatherInfo);
                Log.i("Main Info", mainInfo);

                //json data extract from the json result using each json object for the individual category
                JSONObject mainTemp = new JSONObject(mainInfo);
                String mainTempString = mainTemp.getString("temp");
                String mainMinTempString = mainTemp.getString("temp_min");
                String mainMaxTempString = mainTemp.getString("temp_max");
                String mainPressureString = mainTemp.getString("pressure");
                String mainHumidityString = mainTemp.getString("humidity");

                DecimalFormat decimalFormat = new DecimalFormat("##.00");

                double mainTempInC = Double.parseDouble(mainTempString) - 273.15;
                String mainTempThis = Double.toString(Double.parseDouble(decimalFormat.format(mainTempInC)));
                Log.i("Temperature", mainTempThis);
                Log.i("Temperature", mainTempString);

                double mainMinTempInC = Double.parseDouble(mainMinTempString) - 273.15;
                String mainMinTempThis = Double.toString(Double.parseDouble(decimalFormat.format(mainMinTempInC)));

                double mainMaxTempInC = Double.parseDouble(mainMaxTempString) - 273.15;
                String mainMaxTempThis = Double.toString(Double.parseDouble(decimalFormat.format(mainMaxTempInC)));

                tempText.setText(mainTempThis + "°");
                minTempText.setText(mainMinTempThis + "°");
                maxTempText.setText(mainMaxTempThis + "°");
                pressureText.setText(mainPressureString + "P");
                pressureText.setText(mainHumidityString);
                cityNameText.setText(cityNameEditText.getText().toString());

                Log.i("Min Temperature", mainTemp.getString("temp_min"));
                Log.i("Max Temperature", mainTemp.getString("temp_max"));
                Log.i("Pressure", mainTemp.getString("pressure"));
                Log.i("Humidity", mainTemp.getString("humidity"));

                JSONArray jsonArray = new JSONArray(weatherInfo);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String main = object.getString("main");
                    Log.i("Weather Main", main);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}