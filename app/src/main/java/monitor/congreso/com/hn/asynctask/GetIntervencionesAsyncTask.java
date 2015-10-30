package monitor.congreso.com.hn.asynctask;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import monitor.congreso.com.hn.Constants.MonitorConstants;

/**
 * Created by CortesMoncada on 24/02/2015.
 */
public class GetIntervencionesAsyncTask extends AsyncTask<String, Void, String> {
    private Context context;

    private  String SOAP_ACTION1 = "http://tempuri.org/retornarIntervenciones";
    private  String NAMESPACE = "http://tempuri.org/";
    private  String METHOD_NAME1 = "retornarIntervenciones";
    private  String URLWS = "http://miradorlegislativo.com/monitorservice.asmx?wsdl";



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    public GetIntervencionesAsyncTask(Context context) {
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



        return xmlResult;
    }

    @Override
    protected void onPostExecute(String xml) {

        SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(this.context);
        SharedPreferences.Editor editor = GetPrefs.edit();
        editor.putString(MonitorConstants.TABLE_XML_INTERVENCIONES, xml);
        editor.commit();

    }
}
