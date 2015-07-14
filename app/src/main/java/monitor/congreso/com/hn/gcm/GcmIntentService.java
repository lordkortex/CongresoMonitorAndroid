package monitor.congreso.com.hn.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.logging.Level;
import java.util.logging.Logger;

import monitor.congreso.com.hn.Constants.MonitorConstants;
import monitor.congreso.com.hn.activities.ActivityIntervencionesPorUsuario;
import monitor.congreso.com.hn.activities.ActivityIntervencionesRefresh;
import monitor.congreso.com.hn.asynctask.GetIntervencionesAsyncTask;
import monitor.congreso.com.hn.congresomonitor.R;

/**
 * Created by CortesMoncada on 01/02/2015.
 */
public class GcmIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1;

    private NotificationManager mNotificationManager;


    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (extras != null && !extras.isEmpty()) {  // has effect of unparcelling Bundle
            // Since we're not using two way messaging, this is all we really to check for
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Logger.getLogger("GCM_RECEIVED").log(Level.INFO, extras.toString());
                sendNotification(extras.getString("payload").toString());
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    protected void showToast(final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        //new GetIntervencionesAsyncTask(getApplicationContext()).execute("");

        String pString = msg.toString();
        String[] listElements = {};
        listElements = pString.split(";", 3);

        final String txtXpath = "id_detalle_intervencion ='" + listElements[2].toString() + "'";
        final String txtPathQuery = "[" + txtXpath.toString() + "]";
        //final String txtPathFinal = "/NewDataSet/" + MonitorConstants.TABLE_XML_INTERVENCIONES + txtPathQuery;
        final String txtPathFinal = "/NewDataSet/" + MonitorConstants.TABLE_XML_INTERVENCIONES_POR_USUARIO + txtPathQuery;

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = GetPrefs.edit();
        //editor.putString(MonitorConstants.TABLE_XML_INTERVENCIONS_XPATH, txtPathFinal);
        editor.putString(MonitorConstants.TABLE_XML_INTERVENCIONS_POR_USUARIO_XPATH, txtPathFinal);
        editor.commit();

        Bundle bundleProyecto = new Bundle();
        bundleProyecto.putString(MonitorConstants.TABLE_XML_INTERVENCIONS_POR_USUARIO_XPATH, txtPathFinal);
        PendingIntent pendingIntent;
        Intent intent = new Intent();
        //intent.setClass(this, ActivityIntervencionesRefresh.class);
        intent.setClass(this, ActivityIntervencionesPorUsuario.class);
        intent.putExtras(bundleProyecto);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSound(alarmSound)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(listElements[0].toString())
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(listElements[1].toString()))
                        .setAutoCancel(Boolean.TRUE)
                        .setContentText(listElements[1].toString());


        mBuilder.setContentIntent(pendingIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }


}