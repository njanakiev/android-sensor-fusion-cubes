package com.janakiev.sensorfusioncube;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

public class SensorFuisonCubeActivity extends ActionBarActivity implements SensorEventListener {

    private static final String TAG = "OpenGLCubeMainActivity";
    private GLSurfaceView glSurfaceView;
    private SensorManager sensorManager;
    private CubeRenderer cubeRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);
        cubeRenderer = new CubeRenderer(this);
        glSurfaceView.setRenderer(cubeRenderer);
        setContentView(glSurfaceView);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null){
            Toast.makeText(getApplicationContext(), "TYPE_ACCELEROMETER not available",
                    Toast.LENGTH_SHORT).show();
        } else if(sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) == null){
            Toast.makeText(getApplicationContext(), "TYPE_GYROSCOPE not available",
                    Toast.LENGTH_SHORT).show();
        } else if(sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null){
            Toast.makeText(getApplicationContext(), "TYPE_MAGNETIC_FIELD not available",
                    Toast.LENGTH_SHORT).show();
        }
        initSensorEventListeners();
    }

    private void initSensorEventListeners() {
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
        initSensorEventListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sensor_fuison_cube, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float[] values = event.values;
        final float[] rotationMatrix = new float[16];
        final float[] orientationVector = new float[3];
        switch (event.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:
                break;
            case Sensor.TYPE_GYROSCOPE:
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                if(cubeRenderer != null){
                    try {
                        SensorManager.getRotationMatrixFromVector(rotationMatrix, values);
                        // azimuth  values[0]
                        // pitch    values[1]
                        // rol      values[2]
                        SensorManager.getOrientation(rotationMatrix, orientationVector);
                        glSurfaceView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                cubeRenderer.setCameraOrientation(orientationVector);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
