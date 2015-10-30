package monitor.congreso.com.hn.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import monitor.congreso.com.hn.Constants.MonitorConstants;
import monitor.congreso.com.hn.adapters.AdapterIntervenciones;
import monitor.congreso.com.hn.asynctask.EnviarReportesIntervencionAsyncTask;
import monitor.congreso.com.hn.congresomonitor.R;
import monitor.congreso.com.hn.interfaces.OnItemClickListener;
import monitor.congreso.com.hn.util.XpathUtil;

/**
 * Created by CortesMoncada on 25/01/2015.
 */
public class ActivityIntervenciones extends Activity {

    private RecyclerView mRecyclerView;
    private AdapterIntervenciones mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String xpath ="";
    private Activity activity;
    private ProgressDialog Brockerdialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_intervenciones);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        activity = this;


        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Bundle bundleProyecto = new Bundle();
        bundleProyecto = getIntent().getExtras();
        xpath = bundleProyecto.getString(MonitorConstants.TABLE_XML_INTERVENCIONS_XPATH);

        SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String xmlProyectos = "";
        if (GetPrefs.contains(MonitorConstants.TABLE_XML_INTERVENCIONES) ) {
            xmlProyectos = GetPrefs.getString(MonitorConstants.TABLE_XML_INTERVENCIONES, MonitorConstants.TABLE_XML_INTERVENCIONES);
            String usuario = GetPrefs.getString(MonitorConstants.LOGIN_USUARIO, "");
            getActionBar().setTitle("Intervenciones");
        }
        NodeList nodeList = XpathUtil.getXptathResult(xmlProyectos, xpath);
        setListData(nodeList);



        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new IntervencionesAsyncTask(getApplicationContext()).execute("");
            }
        });




        new IntervencionesAsyncTask(getApplicationContext()).execute("");
    }

    public void setListData(NodeList values){


        mAdapter = new AdapterIntervenciones(values);
        mAdapter.SetOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        mRecyclerView.setAdapter(mAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // Inflate the menu; this adds items to the action bar if it is present.
        SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if (GetPrefs.contains(MonitorConstants.SESION_NOMBRE_ACTUAL) &&  GetPrefs.contains(MonitorConstants.VALOR_PERFIL)) {
            if ((! "".equals(GetPrefs.getString(MonitorConstants.SESION_NOMBRE_ACTUAL,"")) ) && ( !"cliente".equals(GetPrefs.getString(MonitorConstants.VALOR_PERFIL,"")) ) ){
                getMenuInflater().inflate(R.menu.menu_activity_intervencion_listado, menu);
            }else{
                getMenuInflater().inflate(R.menu.menu_activity_intervencion_cliente, menu);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id== android.R.id.home){
            //NavUtils.getParentActivityIntent(this);
            finish();
            return true;
        }



        //noinspection SimplifiableIfStatement
        if (id == R.id.action_intervencion_crear) {
            try {
                Class ourclass = Class.forName("monitor.congreso.com.hn.activities.ActivityIntervencionesInsercion");
                Intent ourintent = new Intent(ActivityIntervenciones.this, ourclass);
                startActivity(ourintent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (id == R.id.action_intervencion_reporte ) {
            try {
                String usuario = "";
                SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                if (GetPrefs.contains(MonitorConstants.LOGIN_USUARIO) ) {
                    usuario = GetPrefs.getString(MonitorConstants.LOGIN_USUARIO, "");
                }
                new EnviarReportesIntervencionAsyncTask(activity).execute(xpath+";"+usuario);
            } catch (Exception e) {
            }

        }

        return super.onOptionsItemSelected(item);
    }

     class IntervencionesAsyncTask extends AsyncTask<String, Void, String> {
        private Context context;

        private  String SOAP_ACTION1 = "http://tempuri.org/retornarIntervenciones";
        private  String NAMESPACE = "http://tempuri.org/";
        private  String METHOD_NAME1 = "retornarIntervenciones";
        private  String URLWS = "http://miradorlegislativo.com/monitorservice.asmx?wsdl";

        private ProgressDialog Brockerdialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Brockerdialog = new ProgressDialog(activity);
            Brockerdialog.setMessage("Cargando ...");
            Brockerdialog.setCancelable(false);
            Brockerdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            Brockerdialog.show();

        }

        public IntervencionesAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String xmlResult = "";

            try {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                envelope.setOutputSoapObject(request);
                envelope.dotNet = true;
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URLWS, 10000);
                androidHttpTransport.call(SOAP_ACTION1, envelope);
                SoapObject result = (SoapObject) envelope.bodyIn;
                xmlResult = result.getProperty(0).toString();
            } catch (IOException e) {
                xmlResult = "Tiempo de Espera agotado.";//e.getMessage().toString();
            } catch (XmlPullParserException e) {
                xmlResult = "Tiempo de Espera agotado.";//e.getMessage().toString();
            } catch (Exception e) {
                xmlResult = "Tiempo de Espera agotado.";//e.getMessage().toString();
            }



            return xmlResult;
        }

        @Override
        protected void onPostExecute(String xml) {

            if(xml.contains("ntervencion")){
                SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(this.context);
                SharedPreferences.Editor editor = GetPrefs.edit();
                editor.putString(MonitorConstants.TABLE_XML_INTERVENCIONES, xml);
                editor.commit();

                NodeList nodeList = XpathUtil.getXptathResult(xml, xpath);
                setListData(nodeList);
            }

            swipeRefreshLayout.setRefreshing(Boolean.FALSE);

            Brockerdialog.setCancelable(true);
            Brockerdialog.dismiss();
        }
    }

}
