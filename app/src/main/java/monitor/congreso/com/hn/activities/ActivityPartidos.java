package monitor.congreso.com.hn.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.NodeList;

import monitor.congreso.com.hn.Constants.MonitorConstants;
import monitor.congreso.com.hn.adapters.AdapterGenerico;
import monitor.congreso.com.hn.congresomonitor.R;
import monitor.congreso.com.hn.interfaces.OnItemClickListener;
import monitor.congreso.com.hn.util.XpathUtil;

/**
 * Created by CortesMoncada on 08/02/2015.
 */
public class ActivityPartidos extends Activity {

    private RecyclerView mRecyclerView;
    private AdapterGenerico mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_generico);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String xmlComisiones = "";
        if (GetPrefs.contains(MonitorConstants.TABLE_XML_PARTIDOS)) {
            xmlComisiones = GetPrefs.getString(MonitorConstants.TABLE_XML_PARTIDOS, MonitorConstants.TABLE_XML_PARTIDOS);
            String usuario = GetPrefs.getString(MonitorConstants.LOGIN_USUARIO, "");
            getActionBar().setTitle("Partidos");
        }
        NodeList nodeList = XpathUtil.getXptathResult(xmlComisiones, "/NewDataSet/" + MonitorConstants.TABLE_XML_PARTIDOS);
        setListData(nodeList);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(Boolean.FALSE);
            }
        });

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
                bundleProyecto.putString(MonitorConstants.FIELD_PARTIDO_NOMBRE, generic_field_1);
                bundleProyecto.putString(MonitorConstants.FIELD_PARTIDO_DESC, generic_field_2);
                bundleProyecto.putString(MonitorConstants.FIELD_DESC_CORTA, generic_field_3);
                bundleProyecto.putString(MonitorConstants.FIELD_DESC_LARGA, generic_field_4);
                bundleProyecto.putString(MonitorConstants.FIELD_IMAGEN, generic_field_5);

                try {
                    Class ourclass = Class.forName("monitor.congreso.com.hn.activities.ActivityDashBoardPartidos");
                    Intent ourintent = new Intent(ActivityPartidos.this, ourclass);
                    ourintent.putExtras(bundleProyecto);
                    startActivity(ourintent);
                } catch (Exception e) {
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_main_activity_monitor, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);

    }
}

