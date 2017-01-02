package com.example.arabellaprivat.tanzderfunktionen.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**Klasse Datasource
 *->Datenquelle
 *hält die Verbindung zur Datenbank aufrecht
 *fragt Referenz zu dem Datenbankobjekt an
 *startet Erstellungsprozess der Tabelle
 * erstellt Listen mit Datensätzen
 */
public class Datasource {

    private static final String LOG_TAG = Datasource.class.getSimpleName();

    private SQLiteDatabase database;
    private DatabaseHelper databaseHelper;
    private ArrayList<String> string_list = new ArrayList<String>();
    private ArrayList<Float> float_list = new ArrayList<Float>();

    /* Konstruktor */
    public Datasource(Context context) {
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den DatabaseHelper.");
        databaseHelper = new DatabaseHelper(context);
    }

    /* Callback-Methoden open() und close()*/
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
     * selektiert alle Strings und das Level und schreibt sie in eine array liste
     * @return      Arrayliste mit Level, Funktion und Tipp drinnen
     */
    public ArrayList<String> Eintraege_String () {
        //Cursor der alle Level durchläuft und level,funktion und tipp selektiert
        Cursor c = database.rawQuery("SELECT level_level, funktion, tipp FROM " + DatabaseHelper.DB_TABLE_1 + ";", null);
        //auslesen der Indices
        int level = c.getColumnIndex(DatabaseHelper.LEVEL_LEVEL);
        int funktion = c.getColumnIndex(DatabaseHelper.FUNKTION);
        int tipp = c.getColumnIndex(DatabaseHelper.TIPP);
        //Cursor an erste Stelle setzen
        c.moveToFirst();

        while(!c.isAfterLast()) {
            //Suchanfragen ausgeben lassen und zu Liste hinzufuegen
            String l = c.getString(level);
            String f = c.getString(funktion);
            String t = c.getString(tipp);
            string_list.add(l + ";" + f + ";" + t);
            //Cursor weiterlaufen lassen
            c.moveToNext();
        }
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


        return float_list;
    }

}