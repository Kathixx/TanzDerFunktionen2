package com.example.arabellaprivat.tanzderfunktionen.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/** Klasse DatabaseHelper
 * erstellt die Datenbank mit den zugehörigen Tabellen
 * fügt vordefinierte Werte in die Tabelle "Level" ein
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    //Zur Hilfe bei Ausgaben auf die Konsole
    public static final String LOG_TAG = DatabaseHelper.class.getSimpleName();

    //Konstanten
    //Dateiname und Version
    private static final String DB_NAME = "matheapp.db";
    private static final int DB_VERSION = 1; //muss bei Änderung incrementiert werden
    //Tabellennamen
    public static final String DB_TABLE_1 = "Level"; //enthält alle Informationen zu einem Level
    public static final String DB_TABLE_2 = "Temp_Storage"; //speichert Zwischenstände ab

    //Tabellenspalten von der Tabelle Level
    public static final String LEVEL_LEVEL = "level_level";
    public static final String FUNCTION = "function";
    public static final String HINT = "hint"; //Tipp für die entsprechende Funktion
    public static final String PARAMETER_1 = "a";
    public static final String PARAMETER_2 = "b";
    public static final String PARAMETER_3 = "c";
    public static final String PARAMETER_4 = "d";
    public static final String NULL_1 = "x1"; //erste Nullstelle
    public static final String NULL_2 = "x2"; //zweite Nullstelle
    public static final String INTERCEPT = "y"; //Achsenabschnitt
    public static final String INTERVAL_1 = "min"; //minimales Intervall
    public static final String INTERVAL_2 = "max"; //maximales Intervall

    //Tabellenspalten von der Tabelle temp_Storage
    public static final String STORE_LEVEL = "level"; //aktuelles Level
    public static final String STORE_POINTS_1 = "points1"; //Punktestand Level 1
    public static final String STORE_POINTS_2 = "points2"; //Punktestand Level 2
    public static final String STORE_POINTS_3 = "points3"; //Punktestand Level 3
    public static final String STORE_POINTS_4 = "points4"; //Punktestand Level 4
    public static final String STORE_POINTS_5 = "points5"; //Punktestand Level 5

    //String der die Tabelle 1: Level erstellt
    public static final String SQL_CREATEDB_1 =
            "CREATE TABLE " + DB_TABLE_1 +
                    " (" + LEVEL_LEVEL + " INTEGER PRIMARY KEY, " +
                    FUNCTION + " TEXT NOT NULL, " +
                    HINT + " TEXT, " +
                    PARAMETER_1 + " REAL, " +
                    PARAMETER_2 + " REAL, " +
                    PARAMETER_3 + " REAL, " +
                    PARAMETER_4 + " REAL, " +
                    NULL_1 + " REAL, " +
                    NULL_2 + " REAL, " +
                    INTERCEPT + " REAL, "+
                    INTERVAL_1 + " REAL, "+
                    INTERVAL_2 + " REAL);";

    //String der die Tabelle 2: Temp_Storage erstellt
    public static final String SQL_CREATEDB_2 = "CREATE TABLE " +DB_TABLE_2+" ("+
            STORE_LEVEL + " INTEGER, " +
            STORE_POINTS_1 + " INTEGER, " +
            STORE_POINTS_2+ " INTEGER, " +
            STORE_POINTS_3 + " INTEGER, " +
            STORE_POINTS_4 + " INTEGER, " +
            STORE_POINTS_5 + " INTEGER);";

    //vordefinierte Werte in die Tabelle 1 schreiben
    //Für alle 0-Werte haben wir 99 geschrieben, da dies besser herauszufiltern ist
    public static final String SQL_INSERTDB1_1 =
            "INSERT INTO Level"+
                    "(level_level,function,hint,a,b,c,d,x1,x2,y,min,max) " +
                    "VALUES " +
                    "(1, '0.5x+2', 'Erinnerst Du Dich an die Funktion der Parameter m und b in f(x)=mx+b? m steht für die Steigung und b für den Schnittpunkt mit der y-Achse', 0.5, 2, 99, 99, -4, 99, 2,-8,6);";
    public static final String SQL_INSERTDB1_2 =
            "INSERT INTO Level"+
                    "(level_level,function,hint,a,b,c,d,x1,x2,y,min,max) " +
                    "VALUES " +
                    "(2, '0.25x^2+1x-4', 'Alles, was Du tun musst ist, den Scheitelpunkt und die Verschiebung abzulesen', 0.25, 1, -4, 99, -6.5, 2.5, -4,-8,4);";
    public static final String SQL_INSERTDB1_3 =
            "INSERT INTO Level"+
                    "(level_level,function,hint,a,b,c,d,x1,x2,y,min,max) " +
                    "VALUES " +
                    "(3, '3/(x-2)+1', 'Wie verhält sich der Graph für lim->0 ?', 3, 1, -2, 1, -1, 99, -0.5, -8,2);";
    public static final String SQL_INSERTDB1_4 =
            "INSERT INTO Level"+
                    "(level_level,function,hint,a,b,c,d,x1,x2,y,min,max) " +
                    "VALUES " +
                    "(4, '3cos(x+1)', 'Die allgemeine Form dieser Funktion lautet f(x)=a * sin(b*x+c)+d. Was geben die Parameter an ? a: Vergrößerung/Verkleinerung der Amplitude b: Streckung/Stauchung/Spiegelung an der x-Achse c: Verschiebung nach links oder rechts d: Verschiebung auf der y-Achse', 3, 1, 1, 0, -2.571, 0.571, 1.62, -4, 4);";

    public static final String SQL_INSERTDB1_5 =
            "INSERT INTO Level"+
                    "(level_level,function,hint,a,b,c,d,x1,x2,y,min,max) " +
                    "VALUES " +
                    "(5, 'ln(x+5)', 'Weisst du noch in welchem Punkt sich alle logarithmischen Funktionen schneiden ?', 1, 1, 5, 0, -4, 99, 1.5, -5, 5);";

    //Constructor
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "DatabaseHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            //Tabelle 1 in DB erstellen
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

            //Tabelle 2 in DB erstellen
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATEDB_2 + " angelegt.");
            db.execSQL(SQL_CREATEDB_2);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabellen: " + ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_1);
        db.execSQL("DROP TABLE IF EXISTS "+ DB_TABLE_2);

        onCreate(db);
    }

}

