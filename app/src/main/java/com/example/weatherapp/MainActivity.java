package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ImageView search ;
    TextView tempTextView , showCityTextView , minTempTextView , maxTempTextView;
    TextView discriptionTextView , latiTextView , longiTextView , dateTextView , dayTextView;
    EditText cityNameEditTextView;
    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = (ImageView)findViewById(R.id.imageView);
        tempTextView = (TextView)findViewById(R.id.tempTextView);
        cityNameEditTextView = (EditText)findViewById(R.id.cityName);
        showCityTextView = (TextView)findViewById(R.id.cityNameTextView);
        minTempTextView = (TextView)findViewById(R.id.minTempTextView);
        maxTempTextView = (TextView)findViewById(R.id.maxTempTextView);
        discriptionTextView = (TextView)findViewById(R.id.discriptionTextView);
        latiTextView = (TextView)findViewById(R.id.latitudeTextView);
        longiTextView = (TextView)findViewById(R.id.longitudeTextView);
        dateTextView = (TextView)findViewById(R.id.dateTextView);
        dayTextView = (TextView)findViewById(R.id.dayTextView);

        queue = Volley.newRequestQueue(this);

        display_data();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                display_data();
            }
        });
    }

    public double convertInCelcius(double k)
    {
        k = k - 273.15;
        return k;
    }

    public void display_data()
    {
        String api = "a84f357566e2129b287d25ecd206c3a8";
        String city = cityNameEditTextView.getText().toString();
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=a84f357566e2129b287d25ecd206c3a8";


        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    JSONObject main_obj = response.getJSONObject("main");
                    JSONArray array = response.getJSONArray("weather");
                    JSONObject obj = array.getJSONObject(0);
                    JSONObject corr_obj = response.getJSONObject("coord");

                    double temp  = convertInCelcius(Double.valueOf(main_obj.getDouble("temp")));
                    String temperature = String.format("%.2f", temp) + "°C";
                    double minTemp = convertInCelcius(Double.valueOf(main_obj.getDouble("temp_min")));
                    String minTemperature = String.format("%.2f", minTemp) + "°C";
                    double maxTemp = convertInCelcius(Double.valueOf(main_obj.getDouble("temp_max")));
                    String maxTemperature = String.format("%.2f", maxTemp) + "°C";

                    String description = obj.getString("description");
                    String city = response.getString("name");

                    double latD  = Double.valueOf(corr_obj.getDouble("lat"));
                    String lat = String.format("%.2f", latD);

                    double longD  = Double.valueOf(corr_obj.getDouble("lon"));
                    String lon = String.format("%.2f", longD);

                    tempTextView.setText(temperature);
                    discriptionTextView.setText(description);
                    showCityTextView.setText(city);
                    minTempTextView.setText(minTemperature);
                    maxTempTextView.setText(maxTemperature);
                    latiTextView.setText(lat);
                    longiTextView.setText(lon);

                    //calendar

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                    String date = sdf.format(calendar.getTime());
                    sdf = new SimpleDateFormat("EEEE");
                    String day = sdf.format(calendar.getTime());

                    dateTextView.setText(date);
                    dayTextView.setText(day);

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }
        );

        queue.add(jor);
    }
}
