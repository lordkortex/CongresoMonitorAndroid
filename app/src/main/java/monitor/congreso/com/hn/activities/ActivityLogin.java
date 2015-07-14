package monitor.congreso.com.hn.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import hn.com.congreso.monitor.google.cloud.backend.registration.Registration;
import monitor.congreso.com.hn.Constants.MonitorConstants;
import monitor.congreso.com.hn.asynctask.GcmRegistrationAsyncTask;
import monitor.congreso.com.hn.congresomonitor.R;

/**
 * Created by CortesMoncada on 01/02/2015.
 */
public class ActivityLogin extends Activity {

    private String usuario = "";
    private String password = "";
    private String sesionActiva = "";
    private String perfil = "";
    private String usuarioGoogleId = "";
    private String android_id="";

    private Button loginButton, prefsButton;
    private EditText etUsuario, etPassword;


    public static Activity activityLogin;

    private static Registration regService = null;
    private GoogleCloudMessaging gcm;
    private Context context;
    private static final String SENDER_ID = "1092859338991";


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activityLogin = this;

        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));


        etUsuario = (EditText) findViewById(R.id.tvUsuario);
        etPassword = (EditText) findViewById(R.id.tvPassword);
        android_id = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);

        SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if (GetPrefs.contains(MonitorConstants.LOGIN_USUARIO) &&
                GetPrefs.contains(MonitorConstants.LOGIN_PASSWORD) &&
                GetPrefs.contains(MonitorConstants.LOGIN_SESION_ACTIVA) ) {
            usuario = GetPrefs.getString(MonitorConstants.LOGIN_USUARIO, "");
            password = GetPrefs.getString(MonitorConstants.LOGIN_PASSWORD, "");
            sesionActiva= GetPrefs.getString(MonitorConstants.LOGIN_SESION_ACTIVA, "");
        }

        etUsuario.setText(usuario);
        etPassword.setText(password);


        if("1".equals(sesionActiva)){
            Class ourclass = null;
            try {
                ourclass = Class.forName("monitor.congreso.com.hn.activities.MainActivityMonitor");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        loginButton = (Button) findViewById(R.id.loginButton);
        //loginButton.setBackgroundDrawable(getResources().getDrawable(com.insurance.broker.R.drawable.apptheme_btn_default_normal_holo_light));
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                if (GetPrefs.contains(MonitorConstants.LOGIN_USUARIO) &&
                        GetPrefs.contains(MonitorConstants.LOGIN_PASSWORD) &&
                        GetPrefs.contains(MonitorConstants.LOGIN_SESION_ACTIVA) ) {
                    usuario = GetPrefs.getString(MonitorConstants.LOGIN_USUARIO, "");
                    password = GetPrefs.getString(MonitorConstants.LOGIN_PASSWORD, "");
                    sesionActiva= GetPrefs.getString(MonitorConstants.LOGIN_SESION_ACTIVA, "");
                    perfil= GetPrefs.getString(MonitorConstants.VALOR_PERFIL, "");
                }

                if(usuario.equals(etUsuario.getText().toString()) && password.equals(etPassword.getText().toString())){
                    if("1".equals(sesionActiva)){
                        Class ourclass = null;
                        try {
                            if("monitor".equals(perfil)){
                                ourclass = Class.forName("monitor.congreso.com.hn.activities.ActivitySesiones" );
                            }else{
                                ourclass = Class.forName("monitor.congreso.com.hn.activities.MainActivityMonitor");
                            }
                            Intent ourintent = new Intent(activityLogin,ourclass);
                            activityLogin.startActivity(ourintent);
                        } catch (ClassNotFoundException e) {
                        }
                    }else{
                        login();
                    }
                }else{
                    login();
                }

            }
        });


    }

    private void login(){

        SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        usuario = etUsuario.getText().toString();
        password = etPassword.getText().toString();
        SharedPreferences.Editor editor = GetPrefs.edit();
        editor.putString(MonitorConstants.LOGIN_USUARIO, usuario);
        editor.putString(MonitorConstants.LOGIN_PASSWORD, password);
        editor.putString(MonitorConstants.LOGIN_SESION_ACTIVA, "");


        editor.commit();
        new GcmRegistrationAsyncTask(activityLogin).execute(usuario.toString() + ";" + password.toString() + ";" + usuarioGoogleId + ";" + android_id);
        //finish();

    }

}
