package aarnav100.developer.indiragandhisafety;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;

public class MyService extends Service implements SensorEventListener {
    int i=0;
    public MyService() {
    }
    SensorManager sm;
    Sensor accSensor;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sm=(SensorManager)getSystemService(SENSOR_SERVICE);
        accSensor=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this,accSensor, 1000);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER&&(event.values[i]>15||event.values[i]<-15)) {
            work();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sm.unregisterListener(this,accSensor);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE)
            i=1;
        else
            i=0;
    }
    void work() {
        final MediaPlayer mp;
        mp = MediaPlayer.create(this, R.raw.siren);
        mp.start();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mp.stop();
            }
        }, 2000);
    }
}