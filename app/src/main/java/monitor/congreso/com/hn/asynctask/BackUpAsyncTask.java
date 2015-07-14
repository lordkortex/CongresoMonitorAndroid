package monitor.congreso.com.hn.asynctask;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
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

import monitor.congreso.com.hn.database.LtProviderTracking;
import monitor.congreso.com.hn.util.XpathUtil;

/**
 * Created by CortesMoncada on 15/02/2015.
 */
public class BackUpAsyncTask extends AsyncTask<String, Void, String> {
    private Context context;

    private static String SOAP_ACTION1 = "http://tempuri.org/backUpProyecto";
    private static String NAMESPACE = "http://tempuri.org/";
    private static String METHOD_NAME1 = "backUpProyecto";
    private static String URLWS = "http://miradorlegislativo.com/monitorservice.asmx?wsdl";

    private ProgressDialog Brockerdialog;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Brockerdialog = new ProgressDialog(context);
        Brockerdialog.setMessage("Sincronizando información ...");
        Brockerdialog.setCancelable(false);
        Brockerdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Brockerdialog.show();

    }

    public BackUpAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {


        String[] xmlRequest = params[0].toString().split(";");

        String xmlProyectos = xmlRequest[0].toString();
        String xmlIntervenciones = xmlRequest[1].toString();

        String xmlResult = "";

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
            request.addProperty("xmlProyecto", xmlProyectos);
            request.addProperty("xmlIntervencion", xmlIntervenciones);
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


        if ("SUCCESS".equals(xmlResult)) {
            //ELIMINAR LA DATA
            ContentResolver cr = this.context.getContentResolver();

            NodeList nodeListProyectos = XpathUtil.getXptathResult(xmlProyectos, "proyectos/proyecto");
            for (int i = 0; i < nodeListProyectos.getLength(); i++) {
                Node node = nodeListProyectos.item(i);
                NodeList venueChildNodes = node.getChildNodes();
                String id = venueChildNodes.item(0).getChildNodes().item(0).getNodeValue();
                cr.delete(LtProviderTracking.CONTENT_URI_PROYECTO, id, null);
            }

            NodeList nodeListIntervencion = XpathUtil.getXptathResult(xmlIntervenciones, "intervenciones/intervencion");
            for (int i = 0; i < nodeListIntervencion.getLength(); i++) {
                Node node = nodeListIntervencion.item(i);
                NodeList venueChildNodes = node.getChildNodes();
                String id = venueChildNodes.item(0).getChildNodes().item(0).getNodeValue();
                cr.delete(LtProviderTracking.CONTENT_URI_INTERVENCION, id, null);
            }
        }

        return xmlResult;
    }

    @Override
    protected void onPostExecute(String xml) {

        Toast.makeText(context, xml, Toast.LENGTH_LONG).show();

        if (xml.contains("Success")) {
            //ELIMINAR DATA DE LA BASE DE DATOS
        }
        Brockerdialog.setCancelable(true);
        Brockerdialog.dismiss();
    }
}