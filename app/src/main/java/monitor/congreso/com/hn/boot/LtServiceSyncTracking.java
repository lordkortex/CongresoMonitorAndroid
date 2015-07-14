package monitor.congreso.com.hn.boot;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by CortesMoncada on 15/02/2015.
 */
public class LtServiceSyncTracking extends Service {

    private static Activity activity;

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        //Log.i("Class Personalizada", "Intent Service Destroyed");
    }


}