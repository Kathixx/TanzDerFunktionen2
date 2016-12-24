package com.example.arabellaprivat.tanzderfunktionen;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**Klasse DatabaseHelper
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = DatabaseHelper.class.getSimpleName();

    //Konstanten
    //Dateiname und Version
    private static final String DB_NAME = "matheapp.db";
    private static final int DB_VERSION = 1; //muss bei Änderung incrementiert werden
    //Tabellennamen
    public static final String DB_TABLE_1 = "Level";

    //Tabellenspalten_LEVEL
    public static final String LEVEL_LEVEL = "level_level";
    public static final String FUNKTION = "funktion";
    public static final String TIPP = "tipp";
    public static final String PARAMETER_1 = "a";
    public static final String PARAMETER_2 = "b";
    public static final String PARAMETER_3 = "c";
    public static final String PARAMETER_4 = "d";
    public static final String NULLSTELLE_1 = "x1";
    public static final String NULLSTELLE_2 = "x2";
    public static final String ACHSENABSCHNITT = "y";
    public static final String MIN = "min";
    public static final String MAX = "max";

    //String der die Tabelle 1: LEVEL erstellt
    public static final String SQL_CREATEDB_1 =
            "CREATE TABLE " + DB_TABLE_1 +
                    " (" + LEVEL_LEVEL + " INTEGER PRIMARY KEY, " +
                    FUNKTION + " TEXT NOT NULL, " +
                    TIPP + " TEXT, " +
                    PARAMETER_1 + " REAL, " +
                    PARAMETER_2 + " REAL, " +
                    PARAMETER_3 + " REAL, " +
                    PARAMETER_4 + " REAL, " +
                    NULLSTELLE_1 + " REAL, " +
                    NULLSTELLE_2 + " REAL, " +
                    ACHSENABSCHNITT + " REAL, " +
                    MIN + " INTEGER, " +
                    MAX + " INTEGER);";

    //Werte in die Tabelle schreiben
    //TABELLE 1
    public static final String SQL_INSERTDB1_1 =
            "INSERT INTO Level"+
                    "(level_level,funktion,tipp,a,b,c,d,x1,x2,y,min,max) " +
                    "VALUES " +
                    "(1, '0.5x+2', 'Erinnerst Du Dich an die Funktion der Parameter m und b in f(x)=mx+b? m steht für die Steigung und b für den Schnittpunkt mit der y-Achse', 0.5, 2, NULL, NULL, -4, NULL, 2, -8, 8);";
    public static final String SQL_INSERTDB1_2 =
            "INSERT INTO Level"+
                    "(level_level,funktion,tipp,a,b,c,d,x1,x2,y,min,max) " +
                    "VALUES " +
                    "(2, '0.25x^2+1x-4', 'Alles, was Du tun musst ist, den Scheitelpunkt und die Verschiebung abzulesen', 0.25, 1, -4, NULL, -6.5, 2.5, -4, -8, 4);";
    public static final String SQL_INSERTDB1_3 =
            "INSERT INTO Level"+
                    "(level_level,funktion,tipp,a,b,c,d,x1,x2,y,min,max) " +
                    "VALUES " +
                    "(3, '3/(x-2)+1', 'Wie verhält sich der Graph für lim->0 ?', 3, 1, -2, 1, -1, NULL, -0.5, -10, 10);";
    public static final String SQL_INSERTDB1_4 =
            "INSERT INTO Level"+
                    "(level_level,funktion,tipp,a,b,c,d,x1,x2,y,min,max) " +
                    "VALUES " +
                    "(4, '3cos(x+1)', 'Die allgemeine Form dieser Funktion lautet f(x)=a * sin(b*(x+c))+d. Was geben die Parameter an ? a: Vergrößerung/Verkleinerung der Amplitude b: Streckung/Stauchung/Spiegelung an der x-Achse c: Verschiebung nach links oder rechts d: Verschiebung auf der y-Achse', 3, 1, 1, NULL, -2.571, 0.571, 1.62, -4, 4);";
    public static final String SQL_INSERTDB1_5 =
            "INSERT INTO Level"+
                    "(level_level,funktion,tipp,a,b,c,d,x1,x2,y,min,max) " +
                    "VALUES " +
                    "(5, 'ln(x+5)', 'Weisst du noch in welchem Punkt sich alle logarithmischen Funktionen schneiden ?', 1, 1, 5, NULL, -4, NULL, 1.5, -5, 2);";

    //Constructor
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "DatabaseHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            //Tabelle in DB erstellen
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATEDB_1 + " angelegt.");
            db.execSQL(SQL_CREATEDB_1);

            //Daten einfügen
            Log.d(LOG_TAG, "Die Daten werden mit SQL-Befehl: " + SQL_INSERTDB1_1 + " in die Tabelle eingefuegt.");
            db.execSQL(SQL_INSERTDB1_1);
            Log.d(LOG_TAG, "Die Daten werden mit SQL-Befehl: " + SQL_INSERTDB1_2 + " in die Tabelle eingefuegt.");
            db.execSQL(SQL_INSERTDB1_2);
            Log.d(LOG_TAG, "Die Daten werden mit SQL-Befehl: " + SQL_INSERTDB1_3 + " in die Tabelle eingefuegt.");
            db.execSQL(SQL_INSERTDB1_3);
            Log.d(LOG_TAG, "Die Daten werden mit SQL-Befehl: " + SQL_INSERTDB1_4 + " in die Tabelle eingefuegt.");
            db.execSQL(SQL_INSERTDB1_4);
            Log.d(LOG_TAG, "Die Daten werden mit SQL-Befehl: " + SQL_INSERTDB1_5 + " in die Tabelle eingefuegt.");
            db.execSQL(SQL_INSERTDB1_5);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabellen: " + ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_1);

        onCreate(db);
    }

}

