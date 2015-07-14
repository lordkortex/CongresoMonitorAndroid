package monitor.congreso.com.hn.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by CortesMoncada on 22/02/2015.
 */
public class EnviarReportesProyectoAsyncTask extends AsyncTask<String, Void, String> {
    private Context context;

    private static String SOAP_ACTION1 = "http://tempuri.org/enviar_email";
    private static String NAMESPACE = "http://tempuri.org/";
    private static String METHOD_NAME1 = "enviar_email";
    private static String URLWS = "http://miradorlegislativo.com/monitorservice.asmx?wsdl";

    private ProgressDialog Brockerdialog;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Brockerdialog = new ProgressDialog(context);
        Brockerdialog.setMessage("Generando reporte ...");
        Brockerdialog.setCancelable(false);
        Brockerdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Brockerdialog.show();

    }

    public EnviarReportesProyectoAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {



        String xmlRequest =params[0].toString();
        String xmlResult = "";
        String[] listElements = {};
        listElements = xmlRequest.split(";",2);

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
            request.addProperty("filtro", listElements[0].toString());
            request.addProperty("usuario", listElements[1].toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URLWS, 80000);
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
        Toast.makeText(context, xml, Toast.LENGTH_LONG).show();
        Brockerdialog.setCancelable(true);
        Brockerdialog.dismiss();
    }
}