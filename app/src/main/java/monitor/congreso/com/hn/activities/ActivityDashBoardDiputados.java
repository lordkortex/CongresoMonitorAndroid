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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import monitor.congreso.com.hn.Constants.MonitorConstants;
import monitor.congreso.com.hn.adapters.AdapterMenuGridView;
import monitor.congreso.com.hn.congresomonitor.R;
import monitor.congreso.com.hn.dto.MenuItem;

/**
 * Created by CortesMoncada on 08/02/2015.
 */
public class ActivityDashBoardDiputados extends Activity implements View.OnClickListener {

    private ArrayList<MenuItem> gridArray = new ArrayList<MenuItem>();
    private AdapterMenuGridView customGridAdapter;
    private GridView gridView;
    private Activity activity;
    private TextView generic_field_id, generic_field_1, generic_field_2, generic_field_3, generic_field_4;
    private ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getActionBar().setTitle("Diputados");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        activity = this;

        //*******************************************************************************************************
        Bundle bundle = getIntent().getExtras();

        generic_field_1 = (TextView) findViewById(R.id.generic_field_1);
        generic_field_2 = (TextView) findViewById(R.id.generic_field_2);
        generic_field_3 = (TextView) findViewById(R.id.generic_field_3);
        generic_field_4 = (TextView) findViewById(R.id.generic_field_4);

        imageView = (ImageView) findViewById(R.id.generic_image);

        generic_field_1.setText( bundle.getString(MonitorConstants.FIELD_DIPUTADO_NOMBRE));
        generic_field_2.setText( bundle.getString(MonitorConstants.FIELD_PARTIDO_NOMBRE));
        generic_field_3.setText( bundle.getString(MonitorConstants.FIELD_DESC_CORTA));
        generic_field_4.setText( bundle.getString(MonitorConstants.FIELD_DESC_LARGA));

        if(!"".equals(bundle.getString(MonitorConstants.FIELD_IMAGEN))){
            Picasso.with(this)
                    .load(bundle.getString(MonitorConstants.FIELD_IMAGEN))
                    .resize(100, 100)
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .into(imageView);

        }



        //*******************************************************************************************************
        // Cargando Menu Principal
        //*******************************************************************************************************
        Bitmap Proyectos = BitmapFactory.decodeResource(this.getResources(), R.drawable.ico_actos);
        Bitmap Intervenciones = BitmapFactory.decodeResource(this.getResources(), R.drawable.ico_intervenciones);

        gridArray.add(new MenuItem(Proyectos, "Actos Legislativos"));
        gridArray.add(new MenuItem(Intervenciones, "Intervenciones"));

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
                Bundle bundle = getIntent().getExtras();
                String diputado_nombre = bundle.getString(MonitorConstants.FIELD_DIPUTADO_NOMBRE);
                StringBuilder txtXpath = new StringBuilder();
                final String txtPathQuery;
                final String txtPathFinal;
                SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor editor = GetPrefs.edit();
                Bundle bundleProyecto = new Bundle();

                switch (position) {
                    case 0:
                        txtXpath.append("diputado_nombre  = '" + diputado_nombre + "'");
                        txtPathQuery = "".equals(txtXpath.toString()) ? "" : "[" + txtXpath.toString() + "]";
                        txtPathFinal = "/NewDataSet/" + MonitorConstants.TABLE_XML_PROYECTOS + txtPathQuery;

                        editor.putString(MonitorConstants.SESION_NOMBRE_ACTUAL, "");
                        editor.commit();

                        bundleProyecto.putString(MonitorConstants.TABLE_XML_PROYECTOS_XPATH, txtPathFinal);
                        try {
                            ourclass = Class.forName("monitor.congreso.com.hn.activities.ActivityProyectos");
                            Intent ourintent = new Intent(ActivityDashBoardDiputados.this, ourclass);
                            ourintent.putExtras(bundleProyecto);
                            startActivity(ourintent);
                        } catch (Exception e) {
                        }
                        break;

                    case 1:
                        txtXpath.append("diputado_nombre  = '" + diputado_nombre + "'");
                        txtPathQuery = "[" + txtXpath.toString() + "]";
                        txtPathFinal = "/NewDataSet/" + MonitorConstants.TABLE_XML_INTERVENCIONES + txtPathQuery;

                        //Debo instanciar el debate actual , en caso de que en la siguiente pantalle se quiera crear una nueva intervencion.
                        editor.putString(MonitorConstants.PROYECTO_DEBATE_ACTUAL, "");
                        editor.putString(MonitorConstants.PROYECTO_NOMBRE_ACTUAL, "");
                        editor.commit();

                        bundleProyecto.putString(MonitorConstants.TABLE_XML_INTERVENCIONS_XPATH, txtPathFinal);
                        try {
                            ourclass = Class.forName("monitor.congreso.com.hn.activities.ActivityIntervenciones");
                            Intent ourintent = new Intent(ActivityDashBoardDiputados.this, ourclass);
                            ourintent.putExtras(bundleProyecto);
                            startActivity(ourintent);
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
        //getMenuInflater().inflate(R.menu.menu_main_activity_monitor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        if (id== android.R.id.home){
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View v) {

    }
}
