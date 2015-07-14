package monitor.congreso.com.hn.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import monitor.congreso.com.hn.Constants.MonitorConstants;
import monitor.congreso.com.hn.asynctask.InsertIntervencionAsyncTask;
import monitor.congreso.com.hn.congresomonitor.R;
import monitor.congreso.com.hn.util.XpathUtil;

/**
 * Created by CortesMoncada on 25/01/2015.
 */
public class ActivityIntervencionesInsercion extends Activity {

    private AutoCompleteTextView actvProponente;
    private EditText etIntervencion;
    private Button btnCrear;
    private String proyectoNombre, proyectoDebate;
    private Activity activity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_intervenciones_insercion);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        initializeElements();
        Context context = getBaseContext();
        activity= this;

        btnCrear = (Button) findViewById(R.id.ButtonCrearIntervencion);
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });

        SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if (GetPrefs.contains(MonitorConstants.PROYECTO_NOMBRE_ACTUAL)) {
            proyectoNombre = GetPrefs.getString(MonitorConstants.PROYECTO_NOMBRE_ACTUAL, "");
            proyectoDebate = GetPrefs.getString(MonitorConstants.PROYECTO_DEBATE_ACTUAL, "");
            String usuario = GetPrefs.getString(MonitorConstants.LOGIN_USUARIO, "");
            getActionBar().setTitle("Nueva Intervención");
        }

        // LLENA EL AUTOCOMPLETE TEXTBOX DE DIPUTADOS
        ArrayAdapter adapterProponente = new ArrayAdapter
                (this, android.R.layout.simple_list_item_1, XpathUtil.getListObjects(MonitorConstants.TABLE_XML_PROPONENTES, 1, "", Boolean.TRUE, getBaseContext()));
        actvProponente = (AutoCompleteTextView) findViewById(R.id.autoCompleteProponente);
        actvProponente.setAdapter(adapterProponente);


    }


    private void initializeElements() {
        actvProponente = (AutoCompleteTextView) findViewById(R.id.autoCompleteProponente);
        etIntervencion = (EditText) findViewById(R.id.EditTextIntervencion);

        etIntervencion.setMovementMethod(new ScrollingMovementMethod());
        etIntervencion.setSelection(etIntervencion.length());
    }

    private void insertData() {

        if("".equals(actvProponente.getText().toString()) || "".equals(etIntervencion.getText().toString())){
            Toast.makeText(activity, "El proponente y la intervención no pueden estar vacias.", Toast.LENGTH_LONG).show();

        }else{
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
            String fecha = df.format(new Date());

            SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            String xmlRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
            xmlRequest += " <intervenciones>";
            xmlRequest += XpathUtil.buildXmlIntervenciones("0",proyectoNombre,
                    proyectoDebate,actvProponente.getText().toString(),etIntervencion.getText().toString(),fecha);
            xmlRequest += " </intervenciones>";
            new InsertIntervencionAsyncTask(activity).execute(xmlRequest.toString());

            actvProponente.setText("");
            etIntervencion.setText("");
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_intervencion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

        if (id== android.R.id.home){
            //NavUtils.getParentActivityIntent(this);
            finish();
            return true;
        }


        if (id == R.id.action_proyecto_crear) {

            try {
                Class ourclass = Class.forName("monitor.congreso.com.hn.activities.ActivityProyectosInsercion");
                Intent ourintent = new Intent(ActivityIntervencionesInsercion.this, ourclass);
                startActivity(ourintent);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return super.onOptionsItemSelected(item);
    }


}
