package monitor.congreso.com.hn.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import monitor.congreso.com.hn.database.LtProviderTracking;

/**
 * Created by CortesMoncada on 15/02/2015.
 */
public class SearchDbData {

    public static String searchDataProyectos(Context context) {

        //*******************************************************************************************************
        // Busca si Existen Datos Pendientes de Subir
        //*******************************************************************************************************
        String[] projection = new String[]{
                LtProviderTracking.Proyecto._ID,
                LtProviderTracking.Proyecto.COL_PROYECTO_SESION,
                LtProviderTracking.Proyecto.COL_PROYECTO_NOMBRE,
                LtProviderTracking.Proyecto.COL_PROYECTO_TIPO_ACTO,
                LtProviderTracking.Proyecto.COL_PROYECTO_DEBATE,
                LtProviderTracking.Proyecto.COL_PROYECTO_STATUS,
                LtProviderTracking.Proyecto.COL_PROYECTO_COMISION,
                LtProviderTracking.Proyecto.COL_PROYECTO_PARTIDO,
                LtProviderTracking.Proyecto.COL_PROYECTO_PROPONENTE,
                LtProviderTracking.Proyecto.COL_PROYECTO_FECHA,
                LtProviderTracking.Proyecto.COL_PROYECTO_DESCRIPCION
        };

        Uri clientesUri = LtProviderTracking.CONTENT_URI_PROYECTO;

        ContentResolver cr = context.getContentResolver();

        //Hacemos la consulta
        Cursor cur = cr.query(clientesUri,
                projection, //Columnas a devolver
                null,       //Condición de la query
                null,       //Argumentos variables de la query
                null);      //Orden de los resultados

        int ContadorRegistrosPendientesTracking = 0;
        int ContadorRegistrosPendientesItinerario = 0;

        String CadenaPendiente = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        CadenaPendiente += " <proyectos>";
        if (cur != null) {
            if (cur.moveToFirst()) {
                int colid = cur.getColumnIndex(LtProviderTracking.Proyecto._ID);
                int colSesion = cur.getColumnIndex(LtProviderTracking.Proyecto.COL_PROYECTO_SESION);
                int colNombre = cur.getColumnIndex(LtProviderTracking.Proyecto.COL_PROYECTO_NOMBRE);
                int colTipoActo = cur.getColumnIndex(LtProviderTracking.Proyecto.COL_PROYECTO_TIPO_ACTO);
                int colDebate = cur.getColumnIndex(LtProviderTracking.Proyecto.COL_PROYECTO_DEBATE);
                int colStatus = cur.getColumnIndex(LtProviderTracking.Proyecto.COL_PROYECTO_STATUS);
                int colComision = cur.getColumnIndex(LtProviderTracking.Proyecto.COL_PROYECTO_COMISION);
                int colPartido = cur.getColumnIndex(LtProviderTracking.Proyecto.COL_PROYECTO_PARTIDO);
                int colProponente = cur.getColumnIndex(LtProviderTracking.Proyecto.COL_PROYECTO_PROPONENTE);
                int colFecha = cur.getColumnIndex(LtProviderTracking.Proyecto.COL_PROYECTO_FECHA);
                int colDescripcion = cur.getColumnIndex(LtProviderTracking.Proyecto.COL_PROYECTO_DESCRIPCION);

                do {
                    ContadorRegistrosPendientesTracking += 1;
                    CadenaPendiente += XpathUtil.buildXmlProyectos(cur.getString(colid), cur.getString(colSesion),
                            cur.getString(colNombre), cur.getString(colTipoActo),
                            cur.getString(colDebate),cur.getString(colStatus),
                            cur.getString(colComision), cur.getString(colPartido),
                            cur.getString(colProponente),cur.getString(colFecha),cur.getString(colDescripcion));
                } while (cur.moveToNext());
            }

            cur.close();
            CadenaPendiente += " </proyectos>";

        }

        return CadenaPendiente;

    }


    public static String searchDataIntervenciones(Context context) {

        //*******************************************************************************************************
        // Busca si Existen Datos Pendientes de Subir
        //*******************************************************************************************************
        String CadenaPendiente = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String[] projection = new String[]{
                LtProviderTracking.Intervencion._ID,
                LtProviderTracking.Intervencion.COL_IT_NOMBRE,
                LtProviderTracking.Intervencion.COL_IT_DEBATE,
                LtProviderTracking.Intervencion.COL_IT_DIPUTADO,
                LtProviderTracking.Intervencion.COL_IT_INTERVENCION,
                LtProviderTracking.Intervencion.COL_IT_FECHA
        };

        Uri clientesUri = LtProviderTracking.CONTENT_URI_INTERVENCION;

        ContentResolver cr = context.getContentResolver();

        //Hacemos la consulta
        Cursor cur = cr.query(clientesUri,
                projection, //Columnas a devolver
                null,       //Condición de la query
                null,       //Argumentos variables de la query
                null);      //Orden de los resultados

        int ContadorRegistrosPendientesTracking = 0;
        int ContadorRegistrosPendientesItinerario = 0;


        CadenaPendiente += " <intervenciones>";
        if (cur != null) {
            if (cur.moveToFirst()) {
                int colId = cur.getColumnIndex(LtProviderTracking.Intervencion._ID);
                int colNombre = cur.getColumnIndex(LtProviderTracking.Intervencion.COL_IT_NOMBRE);
                int colDebate = cur.getColumnIndex(LtProviderTracking.Intervencion.COL_IT_DEBATE);
                int colDiputado = cur.getColumnIndex(LtProviderTracking.Intervencion.COL_IT_DIPUTADO);
                int colIntervencion = cur.getColumnIndex(LtProviderTracking.Intervencion.COL_IT_INTERVENCION);
                int colFecha = cur.getColumnIndex(LtProviderTracking.Intervencion.COL_IT_FECHA);

                do {
                    ContadorRegistrosPendientesTracking += 1;
                    CadenaPendiente += XpathUtil.buildXmlIntervenciones(cur.getString(colId), cur.getString(colNombre), cur.getString(colDebate), cur.getString(colDiputado), cur.getString(colIntervencion), cur.getString(colFecha));
                } while (cur.moveToNext());
            }

            cur.close();
            CadenaPendiente += " </intervenciones>";
        }

        return CadenaPendiente;

    }

}
