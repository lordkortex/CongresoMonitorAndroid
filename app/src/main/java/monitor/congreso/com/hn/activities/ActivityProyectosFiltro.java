package monitor.congreso.com.hn.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import monitor.congreso.com.hn.Constants.MonitorConstants;
import monitor.congreso.com.hn.congresomonitor.R;
import monitor.congreso.com.hn.interfaces.OnItemClickListener;
import monitor.congreso.com.hn.util.XpathUtil;

/**
 * Created by CortesMoncada on 07/02/2015.
 */


public class ActivityProyectosFiltro extends Activity implements OnItemClickListener, AdapterView.OnItemSelectedListener, View.OnClickListener {

    private String SspSpinnerSesion, SspSpinnerTipoActo, SspSpinnerDebate, SspSpinnerStatus, SspSpinnerProponente, SspSpinnerPartido, SspSpinnerCosmision;
    private Spinner spSpinnerSesion, spSpinnerTipoActo, spSpinnerDebate,
            spSpinnerStatus, spSpinnerProponente, spSpinnerPartido, spSpinnerCosmision;

    private AutoCompleteTextView actv;
    private Button btnMensajes;
    private Boolean firstLoadComision,firstLoadPartido = Boolean.TRUE;
    private Activity activity;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_proyectos_filtro);
        activity = this;


        SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String usuario = GetPrefs.getString(MonitorConstants.LOGIN_USUARIO, "");
        getActionBar().setTitle("Reportes");

        ArrayAdapter adapter = new ArrayAdapter
                (this, android.R.layout.simple_list_item_1, XpathUtil.getListObjects(MonitorConstants.TABLE_XML_PROYECTOS, 1,"",Boolean.TRUE,getBaseContext()));
        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        actv.setAdapter(adapter);
        findSpinners();
        populateSpinners();


    }

    private void findSpinners() {

        spSpinnerSesion = (Spinner) findViewById(R.id.spinnerSesion);
        spSpinnerSesion.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        spSpinnerTipoActo = (Spinner) findViewById(R.id.spinnerTipoActo);
        spSpinnerTipoActo.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        spSpinnerDebate = (Spinner) findViewById(R.id.spinnerDebate);
        spSpinnerDebate.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        spSpinnerStatus = (Spinner) findViewById(R.id.spinnerStatus);
        spSpinnerStatus.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        spSpinnerProponente = (Spinner) findViewById(R.id.spinnerProponente);
        spSpinnerProponente.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        spSpinnerPartido = (Spinner) findViewById(R.id.spinnerPartido);
        spSpinnerPartido.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        spSpinnerCosmision = (Spinner) findViewById(R.id.spinnerComision);
        spSpinnerCosmision.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        btnMensajes = (Button) findViewById(R.id.searchButton);


        btnMensajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                StringBuilder txtXpath = new StringBuilder();
                if (! "".equals(actv.getText().toString())){
                    String proyectoNombreFiltro  = " proyecto_nombre = '" + actv.getText().toString() + "'";
                    txtXpath.append(proyectoNombreFiltro);
                }
                txtXpath.append("".equals(txtXpath.toString()) ? SspSpinnerSesion : "".equals(SspSpinnerSesion) ? "" :  " and " + SspSpinnerSesion );
                txtXpath.append("".equals(txtXpath.toString()) ? SspSpinnerTipoActo : "".equals(SspSpinnerTipoActo) ? "" :  " and " + SspSpinnerTipoActo );
                txtXpath.append("".equals(txtXpath.toString()) ? SspSpinnerDebate : "".equals(SspSpinnerDebate) ? "" :  " and " + SspSpinnerDebate );
                txtXpath.append("".equals(txtXpath.toString()) ? SspSpinnerStatus : "".equals(SspSpinnerStatus) ? "" :  " and " + SspSpinnerStatus );
                txtXpath.append("".equals(txtXpath.toString()) ? SspSpinnerProponente : "".equals(SspSpinnerProponente) ? "" :  " and " + SspSpinnerProponente );
                txtXpath.append("".equals(txtXpath.toString()) ? SspSpinnerPartido : "".equals(SspSpinnerPartido) ? "" :  " and " + SspSpinnerPartido );
                txtXpath.append("".equals(txtXpath.toString()) ? SspSpinnerCosmision : "".equals(SspSpinnerCosmision) ? "" :  " and " + SspSpinnerCosmision );

                String txtPathQuery = "".equals(txtXpath.toString()) ? "" : "[" + txtXpath.toString() + "]";
                String txtPathFinal = "/NewDataSet/" + MonitorConstants.TABLE_XML_PROYECTOS + txtPathQuery ;

                SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
                SharedPreferences.Editor editor = GetPrefs.edit();
                editor.putString(MonitorConstants.SESION_NOMBRE_ACTUAL , "");
                editor.commit();


                Bundle bundleProyecto = new Bundle();
                bundleProyecto.putString(MonitorConstants.TABLE_XML_PROYECTOS_XPATH, txtPathFinal);
                try {
                    Class ourclass = Class.forName("monitor.congreso.com.hn.activities.ActivityProyectos");
                    Intent ourintent = new Intent(ActivityProyectosFiltro.this, ourclass);
                    ourintent.putExtras(bundleProyecto);
                    startActivity(ourintent);
                    //stopService(arg0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void populateSpinners() {

        List<String> planlistSesion = XpathUtil.getListObjects(MonitorConstants.TABLE_XML_SESIONES_FILTRO, 1,"",Boolean.TRUE,getBaseContext());
        spSpinnerSesion.setAdapter(createAdapter(planlistSesion));

        List<String> planlistTipoActo = XpathUtil.getListObjects(MonitorConstants.TABLE_XML_TIPO_ACTOS, 1,"",Boolean.TRUE,getBaseContext());
        spSpinnerTipoActo.setAdapter(createAdapter(planlistTipoActo));

        List<String> planlistDebate = XpathUtil.getListObjects(MonitorConstants.TABLE_XML_DEBATES, 1,"",Boolean.TRUE,getBaseContext());
        spSpinnerDebate.setAdapter(createAdapter(planlistDebate));

        List<String> planlistStatus = XpathUtil.getListObjects(MonitorConstants.TABLE_XML_STATUS, 1,"",Boolean.TRUE,getBaseContext());
        spSpinnerStatus.setAdapter(createAdapter(planlistStatus));

        List<String> planlistProponente = XpathUtil.getListObjects(MonitorConstants.TABLE_XML_PROPONENTES, 1,"",Boolean.TRUE,getBaseContext());
        spSpinnerProponente.setAdapter(createAdapter(planlistProponente));

        List<String> planlistPartido = XpathUtil.getListObjects(MonitorConstants.TABLE_XML_PARTIDOS, 1,"",Boolean.TRUE,getBaseContext());
        spSpinnerPartido.setAdapter(createAdapter(planlistPartido));

        List<String> planlistComision = XpathUtil.getListObjects(MonitorConstants.TABLE_XML_COMISIONES_DISTINCT, 1,"",Boolean.TRUE,getBaseContext());
        spSpinnerCosmision.setAdapter(createAdapter(planlistComision));


    }




    private ArrayAdapter<String> createAdapter(List<String> planlistGenerico) {

        ArrayAdapter<String> planAdaptergenerico = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, planlistGenerico);
        planAdaptergenerico.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return planAdaptergenerico;

    }

    private NodeList xptathResult(final String xml, final String expression) {
        NodeList nodes = null;
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;
            builder = builderFactory.newDocumentBuilder();
            Document xmlDocument = builder.parse(new ByteArrayInputStream(xml.getBytes()));
            XPath xPath = XPathFactory.newInstance().newXPath();
            Object result = xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
            nodes = (NodeList) result;
        } catch (SAXException e) {
        } catch (IOException e) {
        } catch (ParserConfigurationException e) {
        } catch (XPathExpressionException e) {
        }

        return nodes;

    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

        switch (arg0.getId()) {
            case R.id.spinnerSesion:
                SspSpinnerSesion = "".equals(spSpinnerSesion.getItemAtPosition(arg2).toString()) ? "" : "fecha_creacion = '" + spSpinnerSesion.getItemAtPosition(arg2).toString() + "'";
                break;
            case R.id.spinnerTipoActo:
                SspSpinnerTipoActo = "".equals(spSpinnerTipoActo.getItemAtPosition(arg2).toString()) ? "" : "legislacion_nombre = '" + spSpinnerTipoActo.getItemAtPosition(arg2).toString() + "'";
                break;
            case R.id.spinnerDebate:
                SspSpinnerDebate = "".equals(spSpinnerDebate.getItemAtPosition(arg2).toString()) ? "" : "tipo_debate_nombre  = '" + spSpinnerDebate.getItemAtPosition(arg2).toString() + "'";
                break;
            case R.id.spinnerStatus:
                SspSpinnerStatus = "".equals(spSpinnerStatus.getItemAtPosition(arg2).toString()) ? "" : "status_nombre = '" + spSpinnerStatus.getItemAtPosition(arg2).toString() + "'";
                break;
            case R.id.spinnerProponente:
                SspSpinnerProponente = "".equals(spSpinnerProponente.getItemAtPosition(arg2).toString()) ? "" : "diputado_nombre = '" + spSpinnerProponente.getItemAtPosition(arg2).toString() + "'";
                break;
            case R.id.spinnerPartido:
                SspSpinnerPartido = "".equals(spSpinnerPartido.getItemAtPosition(arg2).toString()) ? "" : "partido_nombre = '" + spSpinnerPartido.getItemAtPosition(arg2).toString() + "'";

                if (firstLoadPartido == Boolean.FALSE){
                    if("".equals(SspSpinnerCosmision)){
                        if ("".equals(spSpinnerPartido.getItemAtPosition(arg2).toString())){
                            List<String> planlistProponente = XpathUtil.getListObjects(MonitorConstants.TABLE_XML_PROPONENTES, 1,"",Boolean.TRUE,getBaseContext());
                            spSpinnerProponente.setAdapter(createAdapter(planlistProponente));

                        }else{
                            List<String> planlistProponente = XpathUtil.getListObjects(MonitorConstants.TABLE_XML_PROPONENTES, 1,"[" + SspSpinnerPartido +"]",Boolean.FALSE,getBaseContext());
                            spSpinnerProponente.setAdapter(createAdapter(planlistProponente));
                        }
                    }else{
                        if ("".equals(spSpinnerPartido.getItemAtPosition(arg2).toString())){
                            List<String> planlistProponente = XpathUtil.getListObjects(MonitorConstants.TABLE_XML_COMISIONES, 2,"[" + SspSpinnerCosmision + "]",Boolean.FALSE,getBaseContext());
                            spSpinnerProponente.setAdapter(createAdapter(planlistProponente));

                        }else{
                            List<String> planlistProponente = XpathUtil.getListObjects(MonitorConstants.TABLE_XML_COMISIONES, 2,"[" + SspSpinnerCosmision + " and " + SspSpinnerPartido +"]",Boolean.FALSE,getBaseContext());
                            spSpinnerProponente.setAdapter(createAdapter(planlistProponente));
                        }

                    }

                }

                firstLoadPartido = Boolean.FALSE;

                break;
            case R.id.spinnerComision:
                SspSpinnerCosmision = "".equals(spSpinnerCosmision.getItemAtPosition(arg2).toString()) ? "" : "comision_dictaminadora_nombre = '" + spSpinnerCosmision.getItemAtPosition(arg2).toString() + "'";

                if (firstLoadComision == Boolean.FALSE){
                    //CARGAR PARTIDOS Y DIPUTADOS
                    if ("".equals(spSpinnerCosmision.getItemAtPosition(arg2).toString())){
                        List<String> planlistPartido = XpathUtil.getListObjects(MonitorConstants.TABLE_XML_PARTIDOS, 1,"",Boolean.TRUE,getBaseContext());
                        spSpinnerPartido.setAdapter(createAdapter(planlistPartido));

                        List<String> planlistProponente = XpathUtil.getListObjects(MonitorConstants.TABLE_XML_PROPONENTES, 1,"",Boolean.TRUE,getBaseContext());
                        spSpinnerProponente.setAdapter(createAdapter(planlistProponente));

                    }else{
                        List<String> planlistPartido = XpathUtil.getListObjects(MonitorConstants.TABLE_XML_COMISIONES, 3,"[" + SspSpinnerCosmision +"]",Boolean.FALSE,getBaseContext());
                        spSpinnerPartido.setAdapter(createAdapter(planlistPartido));

                        List<String> planlistProponente = XpathUtil.getListObjects(MonitorConstants.TABLE_XML_COMISIONES, 2,"[" + SspSpinnerCosmision + "]",Boolean.FALSE,getBaseContext());
                        spSpinnerProponente.setAdapter(createAdapter(planlistProponente));

                    }
                }

                firstLoadComision = Boolean.FALSE;

                break;
            default:
                break;
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
