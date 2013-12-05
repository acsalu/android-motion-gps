package com.acsalu.motiongps;

import com.acsalu.motiongps.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class FullscreenActivity extends Activity implements LocationListener, SensorEventListener {

    private LocationManager mLocationManager;
    private String mLocationProvider;
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;

    private SensorManager mSensorManager;
    private TextView mGyroxTextView;
    private TextView mGyroyTextView;
    private TextView mGyrozTextView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        findViews();

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        mLocationProvider = mLocationManager.getBestProvider(criteria, false);
        Location location = mLocationManager.getLastKnownLocation(mLocationProvider);

        if (location != null) {
            Log.d("Location", "Provider " + mLocationProvider + " has been selected");
            onLocationChanged(location);

        } else {
            mLatitudeTextView.setText("Location not available.");
            mLongitudeTextView.setText("Location not available");
        }

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mLocationManager.requestLocationUpdates(mLocationProvider, 400, 1, this);

        List<Sensor> typedSensors = mSensorManager.getSensorList(Sensor.TYPE_GYROSCOPE);
        mSensorManager.registerListener(this, typedSensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onStop() {


        super.onStop();
    }


    /*
     * LocationListener methods
     */

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        mLatitudeTextView.setText(String.valueOf(latitude));
        mLongitudeTextView.setText(String.valueOf(longitude));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled new provider " + provider, Toast.LENGTH_SHORT).show();
    }

    /*
     * SensorEventListener methods
     */

    @Override
    public void onSensorChanged(SensorEvent event) {
        mGyroxTextView.setText(String.valueOf(event.values[0] * 180));
        mGyroyTextView.setText(String.valueOf(event.values[1] * 180));
        mGyrozTextView.setText(String.valueOf(event.values[2] * 180));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /*
     * UI methods
     */

    protected void findViews() {
        mLatitudeTextView = (TextView) findViewById(R.id.latitudeText);
        mLongitudeTextView = (TextView) findViewById(R.id.longitudeText);

        mGyroxTextView = (TextView) findViewById(R.id.gyroxText);
        mGyroyTextView = (TextView) findViewById(R.id.gyroyText);
        mGyrozTextView = (TextView) findViewById(R.id.gyrozText);
    }
}
