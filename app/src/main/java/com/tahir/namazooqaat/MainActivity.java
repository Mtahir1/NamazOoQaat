package com.tahir.namazooqaat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView islamicDate;
    TextView fajrTime;
    TextView shurooq;
    TextView shurooqTime;
    TextView dhuhrTime;
    TextView asrTime;
    TextView maghrebTime;
    TextView ishaTime;
    TextView location;

    String cityName="";
    // Getting Current City

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                if (addresses.size() > 0) {
                    System.out.println(addresses.get(0).getLocality());
                    cityName = addresses.get(0).getLocality();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            location.setText(cityName);
        }
        @Override
        public void onProviderDisabled(String provider) {
        }
        @Override
        public void onProviderEnabled(String provider) {
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    // Getting JSON data from API

    public class NamazTime extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream is = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(is);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    //Log.i("Data received is ", result);
                    data = reader.read();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                String namazTime = jsonObject.getString("items");

                JSONArray arr = new JSONArray(namazTime);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonPart = arr.getJSONObject(i);

                    Log.i("Data of Items is ", jsonPart.getString("date_for"));
                    islamicDate.setText(jsonPart.getString("date_for"));
                    fajrTime.setText(jsonPart.getString("fajr"));
                    shurooqTime.setText(jsonPart.getString("shurooq"));
                    dhuhrTime.setText(jsonPart.getString("dhuhr"));
                    asrTime.setText(jsonPart.getString("asr"));
                    maghrebTime.setText(jsonPart.getString("maghrib"));
                    ishaTime.setText(jsonPart.getString("isha"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

        islamicDate = (TextView) findViewById(R.id.islamicDate);
        fajrTime = (TextView) findViewById(R.id.fajrTime);
        shurooq = (TextView) findViewById(R.id.shurooq);
        shurooqTime = (TextView) findViewById(R.id.shurooqTime);
        dhuhrTime = (TextView) findViewById(R.id.dhuhrTime);
        asrTime = (TextView) findViewById(R.id.asrTime);
        maghrebTime = (TextView) findViewById(R.id.maghrebTime);
        ishaTime = (TextView) findViewById(R.id.ishaTime);
        location = (TextView) findViewById(R.id.location);

        NamazTime namazTime = new NamazTime();
        String apiLink = "https://muslimsalat.com/"+cityName+".json?key=ed3d60643f756b0d26c2be4000ad0a84";
        namazTime.execute(apiLink);

    }
}