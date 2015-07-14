package monitor.congreso.com.hn.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import monitor.congreso.com.hn.Constants.MonitorConstants;
import monitor.congreso.com.hn.asynctask.InsertProyectoAsyncTask;
import monitor.congreso.com.hn.congresomonitor.R;
import monitor.congreso.com.hn.interfaces.OnItemClickListener;
import monitor.congreso.com.hn.util.XpathUtil;

/**
 * Created by CortesMoncada on 14/02/2015.
 */
public class ActivityProyectosInsercion extends Activity implements OnItemClickListener, View.OnClickListener {

    private AutoCompleteTextView actvNombreProyecto, actvTipoActo, actvDebate, actvStatus, actvProponente, actvPartido, actvComision,actvDescripcionProyecto;
    private Button btnCrear;
    public static  Activity activityProyectosInsercion;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_proyectos_insercion);
        getActionBar().setTitle("Creacion de Proyectos");
        initializeElements();
        Context context = getBaseContext();
        activityProyectosInsercion= this;

        btnCrear = (Button) findViewById(R.id.ButtonCrearProyecto);
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });


        ArrayAdapter adapterNombre = new ArrayAdapter
                (this, android.R.layout.simple_list_item_1, XpathUtil.getListObjects(MonitorConstants.TABLE_XML_PROYECTOS, 1, "", Boolean.TRUE, context));
        actvNombreProyecto = (AutoCompleteTextView) findViewById(R.id.autoCompleteNombreProyecto);
        actvNombreProyecto.setAdapter(adapterNombre);

        ArrayAdapter adapterTipoActo = new ArrayAdapter
                (this, android.R.layout.simple_list_item_1, XpathUtil.getListObjects(MonitorConstants.TABLE_XML_TIPO_ACTOS, 1, "", Boolean.TRUE, context));
        actvTipoActo = (AutoCompleteTextView) findViewById(R.id.autoCompleteTipoActo);
        actvTipoActo.setAdapter(adapterTipoActo);

        ArrayAdapter adapterDebate = new ArrayAdapter
                (this, android.R.layout.simple_list_item_1, XpathUtil.getListObjects(MonitorConstants.TABLE_XML_DEBATES, 1, "", Boolean.TRUE, context));
        actvDebate = (AutoCompleteTextView) findViewById(R.id.autoCompleteDebate);
        actvDebate.setAdapter(adapterDebate);

        ArrayAdapter adapterStatus = new ArrayAdapter
                (this, android.R.layout.simple_list_item_1, XpathUtil.getListObjects(MonitorConstants.TABLE_XML_STATUS, 1, "", Boolean.TRUE, context));
        actvStatus = (AutoCompleteTextView) findViewById(R.id.autoCompleteStatus);
        actvStatus.setAdapter(adapterStatus);

        ArrayAdapter adapterProponente = new ArrayAdapter
                (this, android.R.layout.simple_list_item_1, XpathUtil.getListObjects(MonitorConstants.TABLE_XML_PROPONENTES, 1, "", Boolean.TRUE, context));
        actvProponente = (AutoCompleteTextView) findViewById(R.id.autoCompleteProponente);
        actvProponente.setAdapter(adapterProponente);

        ArrayAdapter adapterPartido = new ArrayAdapter
                (this, android.R.layout.simple_list_item_1, XpathUtil.getListObjects(MonitorConstants.TABLE_XML_PARTIDOS, 1, "", Boolean.TRUE, context));
        actvPartido = (AutoCompleteTextView) findViewById(R.id.autoCompletePartido);
        actvPartido.setAdapter(adapterPartido);

        ArrayAdapter adapterComision = new ArrayAdapter
                (this, android.R.layout.simple_list_item_1, XpathUtil.getListObjects(MonitorConstants.TABLE_XML_COMISIONES_DISTINCT, 1, "", Boolean.TRUE, context));
        actvComision = (AutoCompleteTextView) findViewById(R.id.autoCompleteComision);
        actvComision.setAdapter(adapterComision);

    }


    private ArrayAdapter<String> createAdapter(List<String> planlistGenerico) {

        ArrayAdapter<String> planAdaptergenerico = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, planlistGenerico);
        planAdaptergenerico.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return planAdaptergenerico;

    }

    private void initializeElements() {

        actvNombreProyecto = (AutoCompleteTextView) findViewById(R.id.autoCompleteNombreProyecto);
        actvTipoActo = (AutoCompleteTextView) findViewById(R.id.autoCompleteTipoActo);
        actvDebate = (AutoCompleteTextView) findViewById(R.id.autoCompleteDebate);
        actvStatus = (AutoCompleteTextView) findViewById(R.id.autoCompleteStatus);
        actvComision = (AutoCompleteTextView) findViewById(R.id.autoCompleteComision);
        actvPartido = (AutoCompleteTextView) findViewById(R.id.autoCompletePartido);
        actvProponente = (AutoCompleteTextView) findViewById(R.id.autoCompleteProponente);
        actvDescripcionProyecto = (AutoCompleteTextView) findViewById(R.id.autoCompleteDescripcionProyecto);


    }

    private void insertData() {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
        String fecha = df.format(new Date());

        SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String actvSesionNombre ="";
        if (GetPrefs.contains(MonitorConstants.SESION_NOMBRE_ACTUAL)) {
            actvSesionNombre = GetPrefs.getString(MonitorConstants.SESION_NOMBRE_ACTUAL, "");
        }

        //TODO : Estos prefernec deben de ir vacios cuando se llame a la pantalla de listas de intervenciones
        SharedPreferences.Editor editor = GetPrefs.edit();
        editor.putString(MonitorConstants.PROYECTO_DEBATE_ACTUAL, actvDebate.getText().toString());
        editor.putString(MonitorConstants.PROYECTO_NOMBRE_ACTUAL, actvNombreProyecto.getText().toString());
        editor.commit();

        String xmlRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        xmlRequest += " <proyectos>";
        xmlRequest += XpathUtil.buildXmlProyectos("0",actvSesionNombre,
                actvNombreProyecto.getText().toString(),
                actvTipoActo.getText().toString(),
                actvDebate.getText().toString(),
                actvStatus.getText().toString(),
                actvComision.getText().toString(),
                actvPartido.getText().toString(),
                actvProponente.getText().toString(),
                fecha,actvDescripcionProyecto.getText().toString());
        xmlRequest += " </proyectos>";

        new InsertProyectoAsyncTask(activityProyectosInsercion).execute(xmlRequest.toString());

        //finish();
    }


    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main_activity_monitor, menu);
        return true;
    }


    @Override
    public void onClick(View v) {

    }
}

