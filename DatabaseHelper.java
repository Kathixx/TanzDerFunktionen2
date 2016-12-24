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
    public static final String DB_TABLE_3 = "Endbewertung";

    //Tabellenspalten_LEVEL
    public static final String LEVEL_LEVEL = "level_level";
    public static final String FUNKTION = "funktion";
    public static final String PARAMETER_1 = "a";
    public static final String PARAMETER_2 = "b";
    public static final String PARAMETER_3 = "c";
    public static final String MIN = "min";
    public static final String MAX = "max";
    public static final String TIPP = "tipp";

    //Tabellenspalten_ENDBEWERTUNG
    public static final String PUNKTE_GESAMT = "punkte_gesamt";
    public static final String AUSGABE_TEXT = "text";

    //String der die Tabelle 1: LEVEL erstellt
    public static final String SQL_CREATEDB_1 =
            "CREATE TABLE " + DB_TABLE_1 +
                    " (" + LEVEL_LEVEL + " INTEGER PRIMARY KEY, " +
                    FUNKTION + " TEXT NOT NULL, " +
                    PARAMETER_1 + " REAL, " +
                    PARAMETER_2 + " REAL, " +
                    PARAMETER_3 + " REAL, " +
                    MIN + " INTEGER, " +
                    MAX + " INTEGER, " +
                    TIPP + " TEXT);";

    //String der die Tabelle 3: ENDBEWERTUNG erstellt
    public static final String SQL_CREATEDB_3 = "CREATE TABLE " +DB_TABLE_3+" ("+
            PUNKTE_GESAMT + " INTEGER PRIMARY KEY, "+
            AUSGABE_TEXT + " TEXT);";

    //Werte in die Tabelle schreiben
    //TABELLE 1
    public static final String SQL_INSERTDB1_1 =
            "INSERT INTO Level"+
                    "(level_level,funktion,a,b,c,min,max,tipp) " +
                    "VALUES " +
                    "(1, '0.5x+2', 0.5, 2, NULL, -8, 8, 'Erinnerst Du Dich an die Funktion der Parameter m und b in f(x)=mx+b? m steht für die Steigung und b für den Schnittpunkt mit der y-Achse');";
    public static final String SQL_INSERTDB1_2 =
            "INSERT INTO Level"+
                    "(level_level,funktion,a,b,c,min,max,tipp) " +
                    "VALUES " +
                    "(2, '0.25x^2+1x-4', 0.25, 1, -4, -8, 4, 'Alles, was Du tun musst ist, den Scheitelpunkt und die Verschiebung abzulesen');";
    public static final String SQL_INSERTDB1_3 =
            "INSERT INTO Level"+
                    "(level_level,funktion,a,b,c,min,max,tipp) " +
                    "VALUES " +
                    "(3, '3/(x-2)+1', 3, 1, -2, -10, -10, 'Wie verhält sich der Graph für lim->0 ?');";
    public static final String SQL_INSERTDB1_4 =
            "INSERT INTO Level"+
                    "(level_level,funktion,a,b,c,min,max,tipp) " +
                    "VALUES " +
                    "(4, '3cos(x+1)', 3, 1, 1, -4, 4, 'Die allgemeine Form dieser Funktion lautet f(x)=a * sin(b*(x+c))+d. Was geben die Parameter an ? a: Vergrößerung/Verkleinerung der Amplitude b: Streckung/Stauchung/Spiegelung an der x-Achse c: Verschiebung nach links oder rechts d: Verschiebung auf der y-Achse');";
    public static final String SQL_INSERTDB1_5 =
            "INSERT INTO Level"+
                    "(level_level,funktion,a,b,c,min,max,tipp) " +
                    "VALUES " +
                    "(5, 'ln(x+5)', 1, 5, NULL, -5, 2, 'Weisst du noch in welchem Punkt sich alle logarithmischen Funktionen schneiden ?');";
    //TABELLE 3
    public static final String SQL_INSERTDB3_1 =
            "INSERT INTO Endbewertung"+
                    "(punkte_gesamt,text) " +
                    "VALUES " +
                    "(0,'Leider hast du keine Funktion richtig gezeichnet. Vielleicht solltest Du Dich nochmal in das Thema einarbeiten.');";

    public static final String SQL_INSERTDB3_2 =
            "INSERT INTO Endbewertung"+
                    "(punkte_gesamt,text) " +
                    "VALUES " +
                    "(1,'Das war schon ein guter Anfang. Übe weiter, um Dich zu verbessern.');";

    public static final String SQL_INSERTDB3_3 =
            "INSERT INTO Endbewertung"+
                    "(punkte_gesamt,text) " +
                    "VALUES " +
                    "(2,'Das war schon ein guter Anfang. Übe weiter, um Dich zu verbessern.');";

    public static final String SQL_INSERTDB3_4 =
            "INSERT INTO Endbewertung"+
                    "(punkte_gesamt,text) " +
                    "VALUES " +
                    "(3,'Gut gemacht. Übe weiter, um Dein Wissen zu festigen.');";

    public static final String SQL_INSERTDB3_5 =
            "INSERT INTO Endbewertung"+
                    "(punkte_gesamt,text) " +
                    "VALUES " +
                    "(4,'Gut gemacht. Übe weiter, um Dein Wissen zu festigen.');";

    public static final String SQL_INSERTDB3_6 =
            "INSERT INTO Endbewertung"+
                    "(punkte_gesamt,text) " +
                    "VALUES " +
                    "(5,'Perfekt! Du hast alles richtig gemacht. Weiter so!');";

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

            //Tabelle in DB erstellen
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATEDB_3 + " angelegt.");
            db.execSQL(SQL_CREATEDB_3);

            //Daten einfügen
            Log.d(LOG_TAG, "Die Daten werden mit SQL-Befehl: " + SQL_INSERTDB3_1 + " in die Tabelle eingefuegt.");
            db.execSQL(SQL_INSERTDB3_1);
            Log.d(LOG_TAG, "Die Daten werden mit SQL-Befehl: " + SQL_INSERTDB3_2 + " in die Tabelle eingefuegt.");
            db.execSQL(SQL_INSERTDB3_2);
            Log.d(LOG_TAG, "Die Daten werden mit SQL-Befehl: " + SQL_INSERTDB3_3 + " in die Tabelle eingefuegt.");
            db.execSQL(SQL_INSERTDB3_3);
            Log.d(LOG_TAG, "Die Daten werden mit SQL-Befehl: " + SQL_INSERTDB3_4 + " in die Tabelle eingefuegt.");
            db.execSQL(SQL_INSERTDB3_4);
            Log.d(LOG_TAG, "Die Daten werden mit SQL-Befehl: " + SQL_INSERTDB3_5 + " in die Tabelle eingefuegt.");
            db.execSQL(SQL_INSERTDB3_5);
            Log.d(LOG_TAG, "Die Daten werden mit SQL-Befehl: " + SQL_INSERTDB3_6 + " in die Tabelle eingefuegt.");
            db.execSQL(SQL_INSERTDB3_6);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabellen: " + ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_1);
        db.execSQL("DROP TABLE IF EXISTS "+DB_TABLE_3);

        onCreate(db);

    }
}
