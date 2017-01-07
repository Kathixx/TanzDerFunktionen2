package com.example.arabellaprivat.tanzderfunktionen.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasse Datasource
 * ->Datenquelle
 * hält die Verbindung zur Datenbank aufrecht
 * fragt Referenz zu dem Datenbankobjekt an
 * startet Erstellungsprozess der Tabelle
 * erstellt Listen mit Datensätzen
 */
public class Datasource {

    private static final String LOG_TAG = Datasource.class.getSimpleName();

    private SQLiteDatabase database;
    private DatabaseHelper databaseHelper;
    //Listen in die die Daten aus der Datenbank geschrieben werden
    private ArrayList<String> string_list = new ArrayList<String>();
    private ArrayList<Float> float_list = new ArrayList<Float>();
    //Liste für die Daten aus dem Zwischenspeicher
    private ArrayList<Integer> integer_list = new ArrayList<>();

    /** Konstruktor */
    public Datasource(Context context) {
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den DatabaseHelper.");
        databaseHelper = new DatabaseHelper(context);
    }

    /** Callback-Methoden open() und close()*/
    public void open() {
        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        //Verbindung zur DB herstellen
        database = databaseHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    public void close() {
        //Verbindung zur DB schließen
        databaseHelper.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }

    /** Methode Eintraege_string
     * selektiert alle Strings und das Level und schreibt sie in eine Array liste
     * @return      Arrayliste mit Level, Funktion und Tipp drinnen
     */
    public ArrayList<String> Eintraege_String () {
        //Cursor der alle Level durchläuft und level,funktion und tipp selektiert
        Cursor c = database.rawQuery("SELECT level_level, funktion, tipp FROM " + DatabaseHelper.DB_TABLE_1, null);
        //auslesen der Indices
        int level = c.getColumnIndex(DatabaseHelper.LEVEL_LEVEL);
        int funktion = c.getColumnIndex(DatabaseHelper.FUNKTION);
        int tipp = c.getColumnIndex(DatabaseHelper.TIPP);
        //Cursor an erste Stelle setzen
        c.moveToFirst();

        while(!c.isAfterLast()) {
            //Suchanfragen ausgeben lassen und zu Liste hinzufuegen
            //solange es noch eintraege gibt
            String l = c.getString(level);
            String f = c.getString(funktion);
            String t = c.getString(tipp);

            string_list.add(l + ";" + f + ";" + t);
            //Cursor weiterlaufen lassen
            c.moveToNext();
        }
        c.close();
        Log.d(LOG_TAG, "String-Liste erstellt");
        return string_list;
    }

    /** Methode Eintraege_Float
     * selektiert alle Float-Werte und schreibt sie in eine ArrayList<Float>
     * @return      alle Parameter,Nullstellen und Achsenabschnitt in einer Arrayliste
     */
    public ArrayList<Float> Eintraege_Float () {
        //mit where-clausel, falls man nach Level filtern will
        //Cursor c = database.rawQuery("SELECT a, b, c, d FROM " + DatabaseHelper.DB_TABLE_1+" WHERE "+ "level_level" + " =1", null);
        //alle level werden durchspielt -> keine Where-Clausel
        Cursor c = database.rawQuery("SELECT a, b, c, d, x1, x2, y FROM " + DatabaseHelper.DB_TABLE_1, null);
        //auslesen der Indices
        int parameter_1 = c.getColumnIndex(DatabaseHelper.PARAMETER_1);
        int parameter_2 = c.getColumnIndex(DatabaseHelper.PARAMETER_2);
        int parameter_3 = c.getColumnIndex(DatabaseHelper.PARAMETER_3);
        int parameter_4 = c.getColumnIndex(DatabaseHelper.PARAMETER_4);
        int nullstelle_1 = c.getColumnIndex(DatabaseHelper.NULLSTELLE_1);
        int nullstelle_2 = c.getColumnIndex(DatabaseHelper.NULLSTELLE_2);
        int achsenabschnitt = c.getColumnIndex(DatabaseHelper.ACHSENABSCHNITT);
        //Cursor an erste Stelle setzen
        c.moveToFirst();

        while(!c.isAfterLast()) {
            //Suchanfragen ausgeben lassen und in Liste schreiben (pro Float Wert neue Spalte)
            //solange noch Werte vorhanden sind
            Float p_1 = c.getFloat(parameter_1);
            Float p_2 = c.getFloat(parameter_2);
            Float p_3 = c.getFloat(parameter_3);
            Float p_4 = c.getFloat(parameter_4);
            Float ns_1 = c.getFloat(nullstelle_1);
            Float ns_2 = c.getFloat(nullstelle_2);
            Float a = c.getFloat(achsenabschnitt);

            float_list.add(p_1);
            float_list.add(p_2);
            float_list.add(p_3);
            float_list.add(p_4);
            float_list.add(ns_1);
            float_list.add(ns_2);
            float_list.add(a);

            //Cursor läuft weiter
            c.moveToNext();
        }
        c.close();
        Log.d(LOG_TAG, "Float-Liste erstellt");
        return float_list;
    }

    /**Methode insert
     * schreibt aktuelles Level und die Punkte für jedes Level in die Tabelle Zwischenspeicher in der Datenbank
      * @param level        aktuelles Level
     * @param punkte1       Punktestand aus Level 1
     * @param punkte2       Punktestand aus Level 2
     * @param punkte3       Punktestand aus Level 3
     * @param punkte4       Punktestand aus Level 4
     * @param punkte5       Punktestand aus Level 5
     */
    public void insert_table2 (int level, int punkte1, int punkte2, int punkte3, int punkte4, int punkte5) {
        //wenn nichts eingefuegt wurde -1 ausgeben
        long rowId = -1;
        try {
            //Die zu speichernden Werte
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.LEVEL_SPEICHER, level);
            values.put(DatabaseHelper.PUNKTE_SPEICHER1, punkte1);
            values.put(DatabaseHelper.PUNKTE_SPEICHER2, punkte2);
            values.put(DatabaseHelper.PUNKTE_SPEICHER3, punkte3);
            values.put(DatabaseHelper.PUNKTE_SPEICHER4, punkte4);
            values.put(DatabaseHelper.PUNKTE_SPEICHER5, punkte5);
            //in die Tabelle Zwischenspeicehr einfügen
            rowId = database.insert(DatabaseHelper.DB_TABLE_2,null,values);
        } catch (SQLiteException e) {
            Log.e(LOG_TAG, "insert(): rowId="+rowId);
        }
    }

    /** Methode Eintraege_Int
     * prueft ob level und punkte in db eingetragen wurden und schreibt sie in eine arrayliste
     * @return      Arrayliste mit Level und punkten
     */
    public ArrayList<Integer> Eintraege_Int () {
        //Cursor der level und punkte der tabelle zwischenspeicher durchläuft
        Cursor c = database.rawQuery("SELECT level, punkte1, punkte2, punkte3, punkte4, punkte5 FROM " + DatabaseHelper.DB_TABLE_2, null);
        //auslesen der Indices
        int akt_level = c.getColumnIndex(DatabaseHelper.LEVEL_SPEICHER);
        int akt_punkte_1 = c.getColumnIndex(DatabaseHelper.PUNKTE_SPEICHER1);
        int akt_punkte_2 = c.getColumnIndex(DatabaseHelper.PUNKTE_SPEICHER2);
        int akt_punkte_3 = c.getColumnIndex(DatabaseHelper.PUNKTE_SPEICHER3);
        int akt_punkte_4 = c.getColumnIndex(DatabaseHelper.PUNKTE_SPEICHER4);
        int akt_punkte_5 = c.getColumnIndex(DatabaseHelper.PUNKTE_SPEICHER5);

        //Cursor an erste Stelle setzen
        c.moveToFirst();

        while(!c.isAfterLast()) {
            //Suchanfragen ausgeben lassen und zu Liste hinzufuegen
            //solange noch eintaege vorhanden
            int l_stand = c.getInt(akt_level);
            int p1_stand = c.getInt(akt_punkte_1);
            int p2_stand = c.getInt(akt_punkte_2);
            int p3_stand = c.getInt(akt_punkte_3);
            int p4_stand = c.getInt(akt_punkte_4);
            int p5_stand = c.getInt(akt_punkte_5);

            integer_list.add(l_stand);
            integer_list.add(p1_stand);
            integer_list.add(p2_stand);
            integer_list.add(p3_stand);
            integer_list.add(p4_stand);
            integer_list.add(p5_stand);

            //Cursor weiterlaufen lassen
            c.moveToNext();
        }
        c.close();
        return integer_list;
    }

}