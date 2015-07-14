package monitor.congreso.com.hn.database;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import monitor.congreso.com.hn.Constants.MonitorConstants;


public class LtProviderTracking extends ContentProvider {

    //Definición del CONTENT_URI
    //private static final String uri ="content://monitor.congreso.com.hn/proyecto";
    private static final String uriTracking = "content://monitor.congreso.com.hn/proyecto";
    private static final String uriItinerario = "content://monitor.congreso.com.hn/intervencion";

    private static final String uriTrackingAll = "content://monitor.congreso.com.hn/AllTrack";
    private static final String uriItinerarioAll = "content://monitor.congreso.com.hn/AllItini";


    public static final Uri CONTENT_URI_PROYECTO = Uri.parse(uriTracking);
    public static final Uri CONTENT_URI_INTERVENCION = Uri.parse(uriItinerario);

    public static final Uri CONTENT_URI_PROYECTO_ALL = Uri.parse(uriTrackingAll);
    public static final Uri CONTENT_URI_INTERVENCION_ALL = Uri.parse(uriItinerarioAll);

    //Necesario para UriMatcher
    private static final int PROYECTO = 1;
    private static final int PROYECTO_ID = 2;
    private static final UriMatcher uriMatcher;

    //Clase interna para declarar las constantes de columna
    public static final class Proyecto implements BaseColumns {
        private Proyecto() {
        }

        //Nombres de columnas
        public static final String COL_PROYECTO_ID = "_id";
        public static final String COL_PROYECTO_SESION = "sesion";
        public static final String COL_PROYECTO_NOMBRE = "nombre";
        public static final String COL_PROYECTO_TIPO_ACTO = "tipoActo";
        public static final String COL_PROYECTO_DEBATE = "debate";
        public static final String COL_PROYECTO_STATUS = "status";
        public static final String COL_PROYECTO_COMISION = "comision";
        public static final String COL_PROYECTO_PARTIDO = "partido";
        public static final String COL_PROYECTO_PROPONENTE = "proponente";
        public static final String COL_PROYECTO_FECHA = "fecha";
        public static final String COL_PROYECTO_DESCRIPCION = "descripcion";

    }

    public static final class Intervencion implements BaseColumns {
        private Intervencion() {
        }

        //Nombres de columnas
        public static final String COL_IT_ID = "_id";
        public static final String COL_IT_NOMBRE = "nombre";
        public static final String COL_IT_DEBATE= "debate";
        public static final String COL_IT_DIPUTADO = "diputado";
        public static final String COL_IT_INTERVENCION = "intervencion";
        public static final String COL_IT_FECHA = "fecha";
    }


    //Base de datos
    private LtSqliteHelper clidbh;
    private static final String BD_NOMBRE = "DBContactos";
    private static final int BD_VERSION = 1;


    //Inicializamos el UriMatcher
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("monitor.congreso.com.hn", "proyecto", PROYECTO);
        uriMatcher.addURI("monitor.congreso.com.hn", "proyecto/#", PROYECTO_ID);

    }

    @Override
    public boolean onCreate() {

        clidbh = new LtSqliteHelper(getContext(), BD_NOMBRE, null, BD_VERSION);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection, String[] selectionArgs, String sortOrder) {

        String stringUri;
        stringUri = uri.toString();

        //Si es una consulta a un ID concreto construimos el WHERE
        String where = selection;
        if (uriMatcher.match(uri) == PROYECTO_ID) {
            where = "_id=" + uri.getLastPathSegment();
        }

        SQLiteDatabase db = clidbh.getWritableDatabase();

        Cursor c = null;

        if (stringUri.contains("AllTrack")) {
            c = db.query(MonitorConstants.TABLA1, projection, where, selectionArgs, null, null, sortOrder);
        }
        if (stringUri.contains("AllItini")) {
            c = db.query(MonitorConstants.TABLA2, projection, where, selectionArgs, null, null, sortOrder);
        }

        if (stringUri.contains("proyecto")) {
            c = db.query(MonitorConstants.TABLA1, projection, where, selectionArgs, null, null, sortOrder, "120");
            //c = db.query( TABLA_PROYECTO, projection, where,selectionArgs, null, null, sortOrder);
        }
        if (stringUri.contains("intervencion")) {
            c = db.query(MonitorConstants.TABLA2, projection, where, selectionArgs, null, null, sortOrder, "10");
        }

        return c;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long regId = 1;
        Uri newUri = null;

        String stringUri;
        stringUri = uri.toString();

        SQLiteDatabase db = clidbh.getWritableDatabase();

        if (stringUri.contains("proyecto")) {
            regId = db.insert(MonitorConstants.TABLA1, null, values);
            newUri = ContentUris.withAppendedId(CONTENT_URI_PROYECTO, regId);
        }
        if (stringUri.contains("intervencion")) {
            regId = db.insert(MonitorConstants.TABLA2, null, values);
            newUri = ContentUris.withAppendedId(CONTENT_URI_INTERVENCION, regId);
        }


        return newUri;
    }


    @Override
    public int update(Uri uri, ContentValues values,
                      String selection, String[] selectionArgs) {

        int cont;

        //Si es una consulta a un ID concreto construimos el WHERE
        String where = selection;
        if (uriMatcher.match(uri) == PROYECTO_ID) {
            where = "_id=" + uri.getLastPathSegment();
        }

        SQLiteDatabase db = clidbh.getWritableDatabase();

        cont = db.update(MonitorConstants.TABLA1, values, where, selectionArgs);

        return cont;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int cont = 0;

        //Si es una consulta a un ID concreto construimos el WHERE
        String where = selection;
        /*if(uriMatcher.match(uri) == PROYECTO_ID){
            where = "_id=" + uri.getLastPathSegment();
        }*/

        if (selection != null) {
            where = "_id=" + selection;
        }

        String stringUri;
        stringUri = uri.toString();

        SQLiteDatabase db = clidbh.getWritableDatabase();

        if (stringUri.contains("proyecto")) {
            cont = db.delete(MonitorConstants.TABLA1, where, selectionArgs);
        }
        if (stringUri.contains("intervencion")) {
            cont = db.delete(MonitorConstants.TABLA2, where, selectionArgs);
        }


        return cont;
    }

    @Override

    public String getType(Uri uri) {

        int match = uriMatcher.match(uri);

        switch (match) {
            case PROYECTO:
                return "vnd.android.cursor.dir/vnd.monitor.proyecto";
            case PROYECTO_ID:
                return "vnd.android.cursor.item/vnd.monitor.proyecto";
            default:
                return null;
        }

    }
}

