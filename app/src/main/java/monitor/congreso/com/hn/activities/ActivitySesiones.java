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
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import monitor.congreso.com.hn.Constants.MonitorConstants;
import monitor.congreso.com.hn.adapters.AdapterGenerico;
import monitor.congreso.com.hn.asynctask.BackUpAsyncTask;
import monitor.congreso.com.hn.congresomonitor.R;
import monitor.congreso.com.hn.interfaces.OnItemClickListener;
import monitor.congreso.com.hn.util.SearchDbData;
import monitor.congreso.com.hn.util.XpathUtil;

/**
 * Created by CortesMoncada on 25/01/2015.
 */
public class ActivitySesiones extends Activity {

    private RecyclerView mRecyclerView;
    private AdapterGenerico mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Activity activity;
    private ProgressDialog Brockerdialog;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_generico);

        activity = this;

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new IntervencionesAsyncTask(getApplicationContext()).execute("");
            }
        });

        new IntervencionesAsyncTask(getApplicationContext()).execute("");

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        /*Bundle bundleProyecto = new Bundle();
        bundleProyecto = getIntent().getExtras();
        String xpath = bundleProyecto.getString(MonitorConstants.TABLE_XML_PROYECTOS_XPATH);*/

        SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String xmlComisiones = "";
        if (GetPrefs.contains(MonitorConstants.TABLE_XML_SESIONES)) {
            xmlComisiones = GetPrefs.getString(MonitorConstants.TABLE_XML_SESIONES, MonitorConstants.TABLE_XML_SESIONES);
            String usuario = GetPrefs.getString(MonitorConstants.LOGIN_USUARIO, "");
            getActionBar().setTitle("Sesiones");
        }
        NodeList nodeList = XpathUtil.getXptathResult(xmlComisiones, "/NewDataSet/" + MonitorConstants.TABLE_XML_SESIONES);
        setListData(nodeList);
    }

    public void setListData(NodeList values){
        mAdapter = new AdapterGenerico(values);
        mAdapter.SetOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                //final String sesion_id = ((TextView) view.findViewById(R.id.generic_field_id)).getText().toString();
                final String sesion_nombre = ((TextView) view.findViewById(R.id.generic_field_id)).getText().toString();
                //final String sesion_nombre = ((TextView) view.findViewById(R.id.generic_field_1)).getText().toString();

                final String txtXpath = "sesion_nombre ='" + sesion_nombre + "'";
                final String txtPathQuery ="[" + txtXpath.toString() + "]";;
                final String txtPathFinal = "/NewDataSet/" + MonitorConstants.TABLE_XML_PROYECTOS + txtPathQuery ;


                SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
                SharedPreferences.Editor editor = GetPrefs.edit();
                editor.putString(MonitorConstants.SESION_NOMBRE_ACTUAL , sesion_nombre);
                editor.commit();


                Bundle bundleProyecto = new Bundle();
                bundleProyecto.putString(MonitorConstants.TABLE_XML_PROYECTOS_XPATH, txtPathFinal);
                try {
                    Class ourclass = Class.forName("monitor.congreso.com.hn.activities.ActivityProyectos");
                    Intent ourintent = new Intent(ActivitySesiones.this, ourclass);
                    ourintent.putExtras(bundleProyecto);
                    startActivity(ourintent);
                    //stopService(arg0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        mRecyclerView.setAdapter(mAdapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if (GetPrefs.contains(MonitorConstants.VALOR_PERFIL) ) {
            if ( !"cliente".equals(GetPrefs.getString(MonitorConstants.VALOR_PERFIL,"") )){
                getMenuInflater().inflate(R.menu.menu_activity_sesiones, menu);
            };
        }


        /*View v = (View) menu.findItem(R.id.search).getActionView();
        ImageButton txtSearch = (ImageButton) v.findViewById(R.id.imageButton);
        txtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(ActivitySesiones.this,
                        "ImageButton is clicked!", Toast.LENGTH_SHORT).show();
            }
        });*/
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sesiones_crear) {

            try {
                SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
                SharedPreferences.Editor editor = GetPrefs.edit();
                //GENERAR UN NOMBRE

                Date date = new Date(); // your date
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                Random rand = new Random();
                String sesionNombreGenerator = String.valueOf(year) + String.valueOf(month) + String.valueOf(day) + String.valueOf(rand.nextInt());
                editor.putString(MonitorConstants.SESION_NOMBRE_ACTUAL , sesionNombreGenerator);
                editor.commit();

                Class ourclass = Class.forName("monitor.congreso.com.hn.activities.ActivityProyectosInsercion");
                Intent ourintent = new Intent(ActivitySesiones.this, ourclass);
                startActivity(ourintent);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (id == R.id.action_sesiones_sincronizar) {
            String xmlIntervenciones = SearchDbData.searchDataIntervenciones(getBaseContext());
            String xmlProyectos = SearchDbData.searchDataProyectos(getBaseContext());
            String xmlRequest = xmlProyectos.concat(";").concat(xmlIntervenciones);
            new BackUpAsyncTask(activity).execute(xmlRequest.toString());
            Toast.makeText(this, "Sincronizando ...", Toast.LENGTH_LONG).show();
        }

        if (id == R.id.action_general_logout) {
            try {
                SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor editor = GetPrefs.edit();
                editor.putString(MonitorConstants.LOGIN_USUARIO, "");
                editor.putString(MonitorConstants.LOGIN_PASSWORD, "");
                editor.putString(MonitorConstants.LOGIN_SESION_ACTIVA, "0");
                editor.commit();
                Class ourclass = Class.forName("monitor.congreso.com.hn.activities.ActivityLogin");
                Intent ourintent = new Intent(ActivitySesiones.this, ourclass);
                startActivity(ourintent);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        return super.onOptionsItemSelected(item);
    }

    class IntervencionesAsyncTask extends AsyncTask<String, Void, String> {
        private Context context;

        private  String SOAP_ACTION1 = "http://tempuri.org/retornarSesiones";
        private  String NAMESPACE = "http://tempuri.org/";
        private  String METHOD_NAME1 = "retornarSesiones";
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

            if(xml.contains(MonitorConstants.TABLE_XML_SESIONES)){
                SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(this.context);
                SharedPreferences.Editor editor = GetPrefs.edit();
                editor.putString(MonitorConstants.TABLE_XML_SESIONES, xml);
                editor.commit();

                NodeList nodeList = XpathUtil.getXptathResult(xml, "/NewDataSet/" + MonitorConstants.TABLE_XML_SESIONES);
                setListData(nodeList);
           }
            swipeRefreshLayout.setRefreshing(Boolean.FALSE);

            Brockerdialog.setCancelable(true);
            Brockerdialog.dismiss();
        }
    }


}
