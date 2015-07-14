package monitor.congreso.com.hn.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import monitor.congreso.com.hn.Constants.MonitorConstants;

public class LtSqliteHelper extends SQLiteOpenHelper {



    String sqlCreate = "CREATE TABLE  " + MonitorConstants.TABLA1 +
            " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " sesion TEXT, " +
            " nombre TEXT, " +
            " tipoActo TEXT, " +
            " debate TEXT, " +
            " status TEXT, " +
            " comision TEXT, " +
            " partido TEXT, " +
            " proponente TEXT, " +
            " fecha TEXT,descripcion TEXT) " ;

    String sqlCreate1 = "CREATE TABLE  " + MonitorConstants.TABLA2 +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " nombre TEXT, " +
            " debate TEXT, " +
            " diputado TEXT, " +
            " intervencion TEXT, " +
            " fecha TEXT)";


    public LtSqliteHelper(Context contexto, String nombre, CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //EJECUCION DE LA CREACION DE TABLA DE CONTACTOS IMAGINAMOS
        db.execSQL(sqlCreate);
        db.execSQL(sqlCreate1);

        db.execSQL("DELETE FROM " + MonitorConstants.TABLA1);
        db.execSQL("DELETE FROM " +  MonitorConstants.TABLA2);

        /*
        //INSERTO 3 REGISTROS DE PRUEBA
        for(int i=1; i<=3; i++)
        {
            String latitud = "17.8565455" + i;
            String longitud = "-87.59855455" + i;
            String usuario = "jcortes";
            String fecha = "10/07/1988 10:30:00";

            //Insertamos los datos en la tabla Clientes
            db.execSQL("INSERT INTO Tracking (latitud, longitud, fecha,usuario) " +
                       "VALUES ('" + latitud + "', '" + longitud +"', '" + fecha + "', '" + usuario + "')");
        }*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        //NOTA: Por simplicidad del ejemplo aquí utilizamos directamente la opción de
        //      eliminar la tabla anterior y crearla de nuevo vacía con el nuevo formato.
        //      Sin embargo lo normal será que haya que migrar datos de la tabla antigua
        //      a la nueva, por lo que este método debería ser más elaborado.

        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS " +  MonitorConstants.TABLA1);
        db.execSQL("DROP TABLE IF EXISTS " +  MonitorConstants.TABLA2);

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
        db.execSQL(sqlCreate1);
    }
}
