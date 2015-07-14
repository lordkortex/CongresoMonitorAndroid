package monitor.congreso.com.hn.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import monitor.congreso.com.hn.Constants.MonitorConstants;
import monitor.congreso.com.hn.adapters.AdapterGenerico;
import monitor.congreso.com.hn.congresomonitor.R;
import monitor.congreso.com.hn.interfaces.OnItemClickListener;
import monitor.congreso.com.hn.util.XpathUtil;

/**
 * Created by CortesMoncada on 08/02/2015.
 */
public class ActivityDiputados extends Activity {

    private RecyclerView mRecyclerView;
    private AdapterGenerico mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String xpath = "";
    private String xmlProponentes = "";
    private NodeList nodeList ;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_generico);
        handleIntent(getIntent());


        getActionBar().setTitle("Diputados");
        getActionBar().setDisplayHomeAsUpEnabled(true);


        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Bundle bundleProyecto = new Bundle();
        bundleProyecto = getIntent().getExtras();
        if (bundleProyecto.getString(MonitorConstants.TABLE_XML_PROPONENTES_XPATH) == null) {
            xpath = "/NewDataSet/" + MonitorConstants.TABLE_XML_PROPONENTES;
        } else {
            xpath = bundleProyecto.getString(MonitorConstants.TABLE_XML_PROPONENTES_XPATH);
        }


        SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String usuario = GetPrefs.getString(MonitorConstants.LOGIN_USUARIO, "");
        getActionBar().setTitle("Diputados");

        if (xpath.contains("comision_dictaminadora_nombre")) {
            if (GetPrefs.contains(MonitorConstants.TABLE_XML_PROPONENTES_POR_COMISION)) {
                xmlProponentes = GetPrefs.getString(MonitorConstants.TABLE_XML_PROPONENTES_POR_COMISION, "");
            }
        } else {
            if (GetPrefs.contains(MonitorConstants.TABLE_XML_PROPONENTES)) {
                xmlProponentes = GetPrefs.getString(MonitorConstants.TABLE_XML_PROPONENTES, "");
            }
        }

        nodeList = XpathUtil.getXptathResult(xmlProponentes, xpath);
        setListData(nodeList);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new DiputadosAsyncTask(getApplicationContext()).execute("");
            }
        });


    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY).toLowerCase();
            String queryXpath = "/NewDataSet/" + MonitorConstants.TABLE_XML_PROPONENTES + "[contains(diputado_nombre_lower ,'" + query + "')]";
            NodeList nodeList = XpathUtil.getXptathResult(xmlProponentes, queryXpath);
            mAdapter.getFirstFilter(nodeList);
       }
    }


    public void setListData(NodeList values) {
        // specify an adapter (see also next example)

        mAdapter = new AdapterGenerico(values);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                final String generic_field_1 = ((TextView) view.findViewById(R.id.generic_field_1)).getText().toString();
                final String generic_field_2 = ((TextView) view.findViewById(R.id.generic_field_2)).getText().toString();
                final String generic_field_3 = ((TextView) view.findViewById(R.id.generic_field_3)).getText().toString();
                final String generic_field_4 = ((TextView) view.findViewById(R.id.generic_field_4)).getText().toString();
                final String generic_field_5 = ((TextView) view.findViewById(R.id.generic_field_5)).getText().toString();

                Bundle bundleProyecto = new Bundle();
                bundleProyecto.putString(MonitorConstants.FIELD_DIPUTADO_NOMBRE, generic_field_1);
                bundleProyecto.putString(MonitorConstants.FIELD_PARTIDO_NOMBRE, generic_field_2);
                bundleProyecto.putString(MonitorConstants.FIELD_DESC_CORTA, generic_field_3);
                bundleProyecto.putString(MonitorConstants.FIELD_DESC_LARGA, generic_field_4);
                bundleProyecto.putString(MonitorConstants.FIELD_IMAGEN, generic_field_5);
                try {
                    Class ourclass = Class.forName("monitor.congreso.com.hn.activities.ActivityDashBoardDiputados");
                    Intent ourintent = new Intent(ActivityDiputados.this, ourclass);
                    ourintent.putExtras(bundleProyecto);
                    startActivity(ourintent);
                } catch (Exception e) {
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setSubmitButtonEnabled(true);
        //searchView.setOnQueryTextListener(this);

        MenuItem searchMenuItem = menu.findItem(R.id.search);
        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do whatever you need
                return true; // KEEP IT TO TRUE OR IT DOESN'T OPEN !!
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                NodeList nodeList = XpathUtil.getXptathResult(xmlProponentes, xpath);
                mAdapter.getFirstFilter(nodeList);
                return true; // OR FALSE IF YOU DIDN'T WANT IT TO CLOSE!
            }
        });

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

            int id = item.getItemId();



        if (id == android.R.id.home) {
            //NavUtils.getParentActivityIntent(this);
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);

    }

    class DiputadosAsyncTask extends AsyncTask<String, Void, String> {
        private Context context;

        private String SOAP_ACTION1 = "http://tempuri.org/retornarDiputados";
        private String NAMESPACE = "http://tempuri.org/";
        private String METHOD_NAME1 = "retornarDiputados";
        private String URLWS = "http://miradorlegislativo.com/monitorservice.asmx?wsdl";

        private ProgressDialog Brockerdialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        public DiputadosAsyncTask(Context context) {
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
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URLWS, 70000);
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

            if (xml.contains("diputado")) {
                SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(this.context);
                SharedPreferences.Editor editor = GetPrefs.edit();
                editor.putString(MonitorConstants.TABLE_XML_PROPONENTES, xml);
                editor.commit();

                NodeList nodeList = XpathUtil.getXptathResult(xml, xpath);
                setListData(nodeList);
            }

            swipeRefreshLayout.setRefreshing(Boolean.FALSE);
        }
    }

}

