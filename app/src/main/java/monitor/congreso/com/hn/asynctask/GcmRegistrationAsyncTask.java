package monitor.congreso.com.hn.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.HttpsTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import hn.com.congreso.monitor.google.cloud.backend.registration.Registration;
import monitor.congreso.com.hn.Constants.MonitorConstants;

/**
 * Created by CortesMoncada on 01/02/2015.
 */
public class GcmRegistrationAsyncTask extends AsyncTask<String , Void, String> {
    private static Registration regService = null;
    private GoogleCloudMessaging gcm;
    private Context context;
    private static final String SENDER_ID = "1092859338991";

    private static String SOAP_ACTION1 = "http://tempuri.org/monitorLogin";
    private static String NAMESPACE = "http://tempuri.org/";
    private static String METHOD_NAME1 = "monitorLogin";
    private static String URLWS = "http://miradorlegislativo.com/monitorservice.asmx?wsdl";

    private ProgressDialog Brockerdialog;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Brockerdialog = new ProgressDialog(context);
        Brockerdialog.setMessage("Autenticando Usuario ...");
        Brockerdialog.setCancelable(false);
        Brockerdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Brockerdialog.show();
        //Brockerdialog.setIcon(com.insurance.broker.R.drawable.ic_launcher);

    }

    public GcmRegistrationAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        if (regService == null) {
            Registration.Builder builder = new Registration.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setRootUrl("https://iron-arbor-843.appspot.com/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest)
                                throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            regService = builder.build();
        }

        String msg = "";
        String regId ="";
        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(context);
            }
            regId = gcm.register(SENDER_ID);
            msg = "Device registered, registration ID=" + regId;

            regService.register(regId).execute();

        } catch (IOException ex) {
            msg = "Error: " + ex.getMessage();
            Logger.getLogger("REGISTRATION").log(Level.INFO, msg);
        }


        String xml =params[0].toString();
        String[] listElements = {};
        listElements = xml.split(";",4);

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
            request.addProperty("monitorUsuarioNombre",listElements[0].toString());
            request.addProperty("monitorUsuarioContrasena",listElements[1].toString());
            request.addProperty("monitorGooleID",regId);
            request.addProperty("monitorDeviceID",listElements[3].toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URLWS,20000);
            //HttpsTransportSE androidHttpTransport = new HttpsTransportSE(URLWS,443,"",20000);
            androidHttpTransport.call(SOAP_ACTION1, envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;
            xml=result.getProperty(0).toString();
        } catch (IOException e) {
            xml = "Tiempo de Espera agotado.";//e.getMessage().toString();

        } catch (XmlPullParserException e) {
            xml = "Tiempo de Espera agotado.";//e.getMessage().toString();
        } catch (Exception e) {
            xml = "Tiempo de Espera agotado.";//e.getMessage().toString();
        }


        return xml;
    }

    @Override
    protected void onPostExecute(String xml) {

        if(!xml.contains("xmlSesiones")){
            Toast.makeText(context, "Autenticación incorrecta.", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(context, "Bienvenido.", Toast.LENGTH_LONG).show();

            ArrayList listXml = new ArrayList<String>(Arrays.asList(xml.split(";")));
            String resultSesiones=listXml.get(0).toString();
            String resultProyectos=listXml.get(1).toString();
            String resultIntervenciones=listXml.get(2).toString();
            String resultTipoActos=listXml.get(3).toString();
            String resultDebates=listXml.get(4).toString();
            String resultStatus=listXml.get(5).toString();
            String resultProponentes=listXml.get(6).toString();
            String resultPartidos=listXml.get(7).toString();
            String resultComisiones=listXml.get(8).toString();
            String resultComisionesDistinct=listXml.get(9).toString();
            String resultProponentesPorComision=listXml.get(10).toString();
            String resultPerfil=listXml.get(11).toString();
            String resultIntervencionesPorUsuario=listXml.get(12).toString();
            String resultSesionesFiltro=listXml.get(13).toString();


            SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(this.context);
            SharedPreferences.Editor editor = GetPrefs.edit();

            editor.putString(MonitorConstants.TABLE_XML_SESIONES, resultSesiones);
            editor.putString(MonitorConstants.TABLE_XML_PROYECTOS, resultProyectos);
            editor.putString(MonitorConstants.TABLE_XML_INTERVENCIONES, resultIntervenciones);
            editor.putString(MonitorConstants.TABLE_XML_TIPO_ACTOS, resultTipoActos);
            editor.putString(MonitorConstants.TABLE_XML_DEBATES, resultDebates);
            editor.putString(MonitorConstants.TABLE_XML_STATUS, resultStatus);
            editor.putString(MonitorConstants.TABLE_XML_PROPONENTES, resultProponentes);
            editor.putString(MonitorConstants.TABLE_XML_PARTIDOS, resultPartidos);
            editor.putString(MonitorConstants.TABLE_XML_COMISIONES, resultComisiones);
            editor.putString(MonitorConstants.TABLE_XML_COMISIONES_DISTINCT, resultComisionesDistinct);
            editor.putString(MonitorConstants.TABLE_XML_PROPONENTES_POR_COMISION, resultProponentesPorComision);
            editor.putString(MonitorConstants.TABLE_XML_INTERVENCIONES_POR_USUARIO, resultIntervencionesPorUsuario);
            editor.putString(MonitorConstants.VALOR_PERFIL, resultPerfil);
            editor.putString(MonitorConstants.TABLE_XML_SESIONES_FILTRO, resultSesionesFiltro);


            editor.commit();

            Class ourclass = null;
            try {
                editor.putString(MonitorConstants.LOGIN_SESION_ACTIVA, "1");
                editor.commit();
                if("monitor".equals(resultPerfil)){
                    ourclass = Class.forName("monitor.congreso.com.hn.activities.ActivitySesiones" );
                }else{
                    ourclass = Class.forName("monitor.congreso.com.hn.activities.MainActivityMonitor");
                }
            } catch (ClassNotFoundException e) {
            }
            Intent ourintent = new Intent(this.context,ourclass);
            this.context.startActivity(ourintent);
        }


        Brockerdialog.setCancelable(true);
        Brockerdialog.dismiss();

    }



}