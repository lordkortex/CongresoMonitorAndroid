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
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import monitor.congreso.com.hn.Constants.MonitorConstants;
import monitor.congreso.com.hn.adapters.AdapterProyectos;
import monitor.congreso.com.hn.asynctask.EnviarReportesProyectoAsyncTask;
import monitor.congreso.com.hn.congresomonitor.R;
import monitor.congreso.com.hn.interfaces.OnItemClickListener;
import monitor.congreso.com.hn.util.XpathUtil;

/**
 * Created by CortesMoncada on 25/01/2015.
 */
public class ActivityProyectos extends Activity {

    private RecyclerView mRecyclerView;
    private AdapterProyectos mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String xpath ="";
    private String  xmlProyectos = "";
    private Activity activity;
    private String usuario = "";
    private ProgressDialog Brockerdialog;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_proyectos);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        activity=this;

        Bundle bundleProyecto = new Bundle();
        bundleProyecto = getIntent().getExtras();
        xpath = bundleProyecto.getString(MonitorConstants.TABLE_XML_PROYECTOS_XPATH);

        SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        if (GetPrefs.contains(MonitorConstants.TABLE_XML_PROYECTOS) ) {
            xmlProyectos = GetPrefs.getString(MonitorConstants.TABLE_XML_PROYECTOS, MonitorConstants.TABLE_XML_PROYECTOS);
            String usuario = GetPrefs.getString(MonitorConstants.LOGIN_USUARIO, "");
            getActionBar().setTitle("Actos Legislativos");
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
        // specify an adapter (see also next example)

        mAdapter = new AdapterProyectos(values);
        mAdapter.SetOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                final String proyecto_id_sesion_proyecto = ((TextView) view.findViewById(R.id.proyecto_id_sesion_proyecto)).getText().toString();
                final String proyecto_debate = ((TextView) view.findViewById(R.id.proyecto_debate)).getText().toString();
                final String proyecto_nombre = ((TextView) view.findViewById(R.id.proyecto_nombre)).getText().toString();

                final String txtXpath = "id_sesion_proyecto ='" + proyecto_id_sesion_proyecto + "'";
                final String txtPathQuery ="[" + txtXpath.toString() + "]";;
                final String txtPathFinal = "/NewDataSet/" + MonitorConstants.TABLE_XML_INTERVENCIONES + txtPathQuery ;

                //Debo instanciar el debate actual , en caso de que en la siguiente pantalle se quiera crear una nueva intervencion.
                SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor editor = GetPrefs.edit();
                editor.putString(MonitorConstants.PROYECTO_DEBATE_ACTUAL, proyecto_debate);
                editor.putString(MonitorConstants.PROYECTO_NOMBRE_ACTUAL, proyecto_nombre);
                editor.putString(MonitorConstants.ID_SESION_PROYECTO, proyecto_id_sesion_proyecto);


                editor.commit();


                Bundle bundleProyecto = new Bundle();
                bundleProyecto.putString(MonitorConstants.TABLE_XML_INTERVENCIONS_XPATH, txtPathFinal);
                try {
                    Class ourclass = Class.forName("monitor.congreso.com.hn.activities.ActivityIntervenciones");
                    Intent ourintent = new Intent(ActivityProyectos.this, ourclass);
                    ourintent.putExtras(bundleProyecto);
                    startActivity(ourintent);
                    //stopService(arg0);
                } catch (Exception e) {
                }

            }
        });
        mRecyclerView.setAdapter(mAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if (GetPrefs.contains(MonitorConstants.SESION_NOMBRE_ACTUAL) &&  GetPrefs.contains(MonitorConstants.VALOR_PERFIL)) {
            if ((! "".equals(GetPrefs.getString(MonitorConstants.SESION_NOMBRE_ACTUAL,"")) ) && ( !"cliente".equals(GetPrefs.getString(MonitorConstants.VALOR_PERFIL,"")) ) ){
                getMenuInflater().inflate(R.menu.menu_activity_proyecto, menu);
                usuario ="all";
            }else{
                getMenuInflater().inflate(R.menu.menu_activity_proyecto_cliente, menu);
            };
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
        if (id == R.id.action_proyecto_crear) {
            try {

                Class ourclass = Class.forName("monitor.congreso.com.hn.activities.ActivityProyectosInsercion");
                Intent ourintent = new Intent(ActivityProyectos.this, ourclass);
                startActivity(ourintent);
            } catch (Exception e) {
            }

        }

        if (id == R.id.action_proyecto_reporte ) {
            try {
                SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                if (GetPrefs.contains(MonitorConstants.LOGIN_USUARIO ) && !"all".equals(usuario) ) {
                    usuario = GetPrefs.getString(MonitorConstants.LOGIN_USUARIO, "");
                }
                new EnviarReportesProyectoAsyncTask(activity).execute(xpath+";"+usuario);
            } catch (Exception e) {
            }

        }



        return super.onOptionsItemSelected(item);
    }

    class IntervencionesAsyncTask extends AsyncTask<String, Void, String> {
        private Context context;

        private  String SOAP_ACTION1 = "http://tempuri.org/retornarProyectos";
        private  String NAMESPACE = "http://tempuri.org/";
        private  String METHOD_NAME1 = "retornarProyectos";
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

            if(xml.contains(MonitorConstants.TABLE_XML_PROYECTOS)){
                SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(this.context);
                SharedPreferences.Editor editor = GetPrefs.edit();
                editor.putString(MonitorConstants.TABLE_XML_PROYECTOS, xml);
                editor.commit();

                xmlProyectos= xml;
                NodeList nodeList = XpathUtil.getXptathResult(xmlProyectos, xpath);
                setListData(nodeList);
            }
            swipeRefreshLayout.setRefreshing(Boolean.FALSE);

            Brockerdialog.setCancelable(true);
            Brockerdialog.dismiss();
        }
    }

   /* @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this,ActivityLogin.class);
        startActivity(intent);
    }*/


}
