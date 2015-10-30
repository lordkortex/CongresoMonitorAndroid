package monitor.congreso.com.hn.asynctask;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import monitor.congreso.com.hn.activities.ActivityProyectosInsercion;
import monitor.congreso.com.hn.database.LtProviderTracking;
import monitor.congreso.com.hn.util.XpathUtil;

/**
 * Created by CortesMoncada on 14/02/2015.
 */
public class InsertProyectoAsyncTask extends AsyncTask<String, Void, String> {
    private Context context;

    private static String SOAP_ACTION1 = "http://tempuri.org/insertProyecto";
    private static String NAMESPACE = "http://tempuri.org/";
    private static String METHOD_NAME1 = "insertProyecto";
    private static String URLWS = "http://miradorlegislativo.com/monitorservice.asmx?wsdl";

    private ProgressDialog Brockerdialog;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Brockerdialog = new ProgressDialog(context);
        Brockerdialog.setMessage("Guardando Proyecto...");
        Brockerdialog.setCancelable(false);
        Brockerdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Brockerdialog.show();

    }

    public InsertProyectoAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {

        String xmlRequest = params[0].toString();
        String xmlResult = "";

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
            request.addProperty("myXMl", xmlRequest);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URLWS, 30000);
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

        if (!"SUCCESS".equals(xmlResult)) {

            NodeList nodeList = XpathUtil.getXptathResult(xmlRequest, "proyectos/proyecto");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                NodeList venueChildNodes = node.getChildNodes();
                String sesionNombre = venueChildNodes.item(1).getChildNodes().item(0).getNodeValue();
                String actvNombreProyecto = venueChildNodes.item(2).getChildNodes().item(0).getNodeValue();
                String actvTipoActo = venueChildNodes.item(3).getChildNodes().item(0).getNodeValue();
                String actvDebate = venueChildNodes.item(4).getChildNodes().item(0).getNodeValue();
                String actvStatus = venueChildNodes.item(5).getChildNodes().item(0).getNodeValue();
                String actvComision = venueChildNodes.item(6).getChildNodes().item(0).getNodeValue();
                String actvPartido = venueChildNodes.item(7).getChildNodes().item(0).getNodeValue();
                String actvProponente = venueChildNodes.item(8).getChildNodes().item(0).getNodeValue();
                String fecha = venueChildNodes.item(9).getChildNodes().item(0).getNodeValue();
                String descripcion = venueChildNodes.item(10).getChildNodes().item(0).getNodeValue();

                ContentValues values = new ContentValues();
                values.put(LtProviderTracking.Proyecto.COL_PROYECTO_SESION, sesionNombre);
                values.put(LtProviderTracking.Proyecto.COL_PROYECTO_NOMBRE, actvNombreProyecto);
                values.put(LtProviderTracking.Proyecto.COL_PROYECTO_TIPO_ACTO, actvTipoActo);
                values.put(LtProviderTracking.Proyecto.COL_PROYECTO_DEBATE,actvDebate );
                values.put(LtProviderTracking.Proyecto.COL_PROYECTO_STATUS, actvStatus);
                values.put(LtProviderTracking.Proyecto.COL_PROYECTO_COMISION,actvComision );
                values.put(LtProviderTracking.Proyecto.COL_PROYECTO_PARTIDO,actvPartido );
                values.put(LtProviderTracking.Proyecto.COL_PROYECTO_PROPONENTE,actvProponente );
                values.put(LtProviderTracking.Proyecto.COL_PROYECTO_FECHA,fecha );
                values.put(LtProviderTracking.Proyecto.COL_PROYECTO_DESCRIPCION,descripcion );
                ContentResolver cr = this.context.getContentResolver();
                cr.insert(LtProviderTracking.CONTENT_URI_PROYECTO, values);
            }
        } else {
        }

        try {
            Class ourclass = Class.forName("monitor.congreso.com.hn.activities.ActivityIntervencionesInsercion");
            Intent ourintent = new Intent(this.context, ourclass);
            this.context.startActivity(ourintent);
            ActivityProyectosInsercion.activityProyectosInsercion.finish();
        } catch (Exception e) {

        }
        return xmlResult;
    }

    @Override
    protected void onPostExecute(String xml) {
        Toast.makeText(context, xml, Toast.LENGTH_LONG).show();
        Brockerdialog.setCancelable(true);
        Brockerdialog.dismiss();
    }
}