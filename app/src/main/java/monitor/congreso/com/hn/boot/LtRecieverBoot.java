package monitor.congreso.com.hn.boot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by CortesMoncada on 15/02/2015.
 */
public class LtRecieverBoot extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();
        Log.i("MyActivity","************* Ingreso "  );
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()))
        {
            Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();
            Log.i("MyActivity","************* Ingreso "  );
            int Segundos_Tracking=30;

           /*Calendar cal1 = Calendar.getInstance();
            Intent intent1 = new Intent(context, LtServiceSyncTracking.class);
            PendingIntent pintent1 = PendingIntent.getService(context, 0, intent1, 0);
            AlarmManager alarm1 = (AlarmManager)context.getApplicationContext().getSystemService(context.ALARM_SERVICE);
            alarm1.setRepeating(AlarmManager.RTC_WAKEUP, cal1.getTimeInMillis(), Segundos_Tracking * 1000, pintent1);*/
        }
    }


}

