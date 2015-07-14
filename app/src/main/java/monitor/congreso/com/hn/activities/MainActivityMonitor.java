package monitor.congreso.com.hn.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import monitor.congreso.com.hn.Constants.MonitorConstants;
import monitor.congreso.com.hn.adapters.AdapterMenuGridView;
import monitor.congreso.com.hn.congresomonitor.R;
import monitor.congreso.com.hn.dto.MenuItem;


public class MainActivityMonitor extends Activity implements View.OnClickListener {

    private ArrayList<MenuItem> gridArray = new ArrayList<MenuItem>();
    private AdapterMenuGridView customGridAdapter;
    private GridView gridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_monitor);


        ActivityLogin.activityLogin.finish();

        SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String usuario = GetPrefs.getString(MonitorConstants.LOGIN_USUARIO, "");
        getActionBar().setTitle("Menu Principal");

        //*******************************************************************************************************
        // Cargando Menu Principal
        //*******************************************************************************************************
        Bitmap Proyectos = BitmapFactory.decodeResource(this.getResources(), R.drawable.ico_reportes);
        Bitmap Sesiones = BitmapFactory.decodeResource(this.getResources(), R.drawable.ico_sesiones);
        Bitmap Cosmisiones = BitmapFactory.decodeResource(this.getResources(), R.drawable.ico_comisiones);
        Bitmap Partidos = BitmapFactory.decodeResource(this.getResources(), R.drawable.ico_partidos);
        Bitmap Diputados = BitmapFactory.decodeResource(this.getResources(), R.drawable.ico_diputados);
        Bitmap Monitor = BitmapFactory.decodeResource(this.getResources(), R.drawable.ico_notificaciones);

        gridArray.add(new MenuItem(Proyectos, "Reportes"));
        gridArray.add(new MenuItem(Sesiones, "Sesiones"));
        gridArray.add(new MenuItem(Cosmisiones, "Comisiones"));

        gridArray.add(new MenuItem(Partidos, "Partidos"));
        gridArray.add(new MenuItem(Diputados, "Diputados"));
        gridArray.add(new MenuItem(Monitor, "Notificaciones"));

        gridView = (GridView) findViewById(R.id.gridView1);
        customGridAdapter = new AdapterMenuGridView(this, R.layout.activity_main_activity_monitor_item, gridArray);
        gridView.setAdapter(customGridAdapter);


        //*******************************************************************************************************
        // Evento Click del Boton
        //*******************************************************************************************************
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Class ourclass = null;

                switch (position) {
                    case 0:
                        try {
                            ourclass = Class.forName("monitor.congreso.com.hn.activities.ActivityProyectosFiltro");
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        Intent ourintent0 = new Intent(MainActivityMonitor.this, ourclass);
                        startActivity(ourintent0);
                        break;

                    case 1:
                        try {
                            ourclass = Class.forName("monitor.congreso.com.hn.activities.ActivitySesiones");
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        Intent ourintent1 = new Intent(MainActivityMonitor.this, ourclass);
                        startActivity(ourintent1);
                        break;

                    case 2:
                        try {
                            ourclass = Class.forName("monitor.congreso.com.hn.activities.ActivityComisiones");
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        Intent ourintent2 = new Intent(MainActivityMonitor.this, ourclass);
                        startActivity(ourintent2);
                        break;

                    case 3:
                        try {
                            ourclass = Class.forName("monitor.congreso.com.hn.activities.ActivityPartidos");
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        Intent ourintent3 = new Intent(MainActivityMonitor.this, ourclass);
                        startActivity(ourintent3);
                        break;

                    case 4:
                        Bundle bundle = new Bundle();
                        bundle.putString(MonitorConstants.TABLE_XML_PROPONENTES_XPATH, "/NewDataSet/" + MonitorConstants.TABLE_XML_PROPONENTES);

                        try {
                            ourclass = Class.forName("monitor.congreso.com.hn.activities.ActivityDiputados");
                        } catch (ClassNotFoundException e) {
                        }
                        Intent ourintent4 = new Intent(MainActivityMonitor.this, ourclass);
                        ourintent4.putExtras(bundle);
                        startActivity(ourintent4);
                        break;

                    case 5:
                        SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        SharedPreferences.Editor editor = GetPrefs.edit();
                        editor.putString(MonitorConstants.PROYECTO_DEBATE_ACTUAL, "");
                        editor.putString(MonitorConstants.PROYECTO_NOMBRE_ACTUAL, "");
                        editor.putString(MonitorConstants.ID_SESION_PROYECTO, "");
                        editor.putString(MonitorConstants.TABLE_XML_INTERVENCIONS_POR_USUARIO_XPATH, "/NewDataSet/" + MonitorConstants.TABLE_XML_INTERVENCIONES_POR_USUARIO);
                        editor.commit();

                        Bundle bundleProyecto = new Bundle();
                        bundleProyecto.putString(MonitorConstants.TABLE_XML_INTERVENCIONS_POR_USUARIO_XPATH, "/NewDataSet/" + MonitorConstants.TABLE_XML_INTERVENCIONES_POR_USUARIO);
                        try {
                            ourclass = Class.forName("monitor.congreso.com.hn.activities.ActivityIntervencionesPorUsuario");
                            Intent ourintent = new Intent(MainActivityMonitor.this, ourclass);
                            ourintent.putExtras(bundleProyecto);
                            startActivity(ourintent);
                            //stopService(arg0);
                        } catch (Exception e) {
                        }
                        break;

                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity_monitor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_general_logout) {
            try {
                SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor editor = GetPrefs.edit();
                editor.putString(MonitorConstants.LOGIN_USUARIO, "");
                editor.putString(MonitorConstants.LOGIN_PASSWORD, "");
                editor.putString(MonitorConstants.LOGIN_SESION_ACTIVA, "0");
                editor.commit();
                Class ourclass = Class.forName("monitor.congreso.com.hn.activities.ActivityLogin");
                Intent ourintent = new Intent(MainActivityMonitor.this, ourclass);
                startActivity(ourintent);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {

    }
}
