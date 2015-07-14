package monitor.congreso.com.hn.asynctask;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
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
 * Created by CortesMoncada on 14/02/2015.
 */
public class InsertIntervencionAsyncTask extends AsyncTask<String, Void, String> {
    private Context context;

    private static String SOAP_ACTION1 = "http://tempuri.org/insertIntervencion";
    private static String NAMESPACE = "http://tempuri.org/";
    private static String METHOD_NAME1 = "insertIntervencion";
    private static String URLWS = "http://miradorlegislativo.com/monitorservice.asmx?wsdl";

    private ProgressDialog Brockerdialog;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Brockerdialog = new ProgressDialog(context);
        Brockerdialog.setMessage("Guardando Intervención ...");
        Brockerdialog.setCancelable(false);
        Brockerdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Brockerdialog.show();

    }

    public InsertIntervencionAsyncTask(Context context) {
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
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URLWS, 20000);
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

            NodeList nodeList = XpathUtil.getXptathResult(xmlRequest, "intervenciones/intervencion");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                NodeList venueChildNodes = node.getChildNodes();
                String proyectoNombre = venueChildNodes.item(1).getChildNodes().item(0).getNodeValue();
                String sesionDebate = venueChildNodes.item(2).getChildNodes().item(0).getNodeValue();
                String diputado = venueChildNodes.item(3).getChildNodes().item(0).getNodeValue();
                String intervencion = venueChildNodes.item(4).getChildNodes().item(0).getNodeValue();
                String fecha = venueChildNodes.item(5).getChildNodes().item(0).getNodeValue();


                ContentValues values = new ContentValues();
                values.put(LtProviderTracking.Intervencion.COL_IT_NOMBRE, proyectoNombre);
                values.put(LtProviderTracking.Intervencion.COL_IT_DEBATE, sesionDebate);
                values.put(LtProviderTracking.Intervencion.COL_IT_DIPUTADO,diputado);
                values.put(LtProviderTracking.Intervencion.COL_IT_INTERVENCION, intervencion);
                values.put(LtProviderTracking.Intervencion.COL_IT_FECHA, fecha);
                ContentResolver cr = this.context.getContentResolver();
                cr.insert(LtProviderTracking.CONTENT_URI_INTERVENCION, values);
            }
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