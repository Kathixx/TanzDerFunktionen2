package com.example.arabellaprivat.tanzderfunktionen.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import java.util.ArrayList;

/**
 * Klasse Datasource
 * ->Datenquelle
 * hält die Verbindung zur Datenbank aufrecht
 * fragt Referenz zu dem Datenbankobjekt an
 * startet Erstellungsprozess der Tabelle
 * erstellt Listen mit Datensätzen
 */
public class Datasource  {
    

    //Zur Hilfe beim Ausgeben auf die Konsole
    private static final String LOG_TAG = Datasource.class.getSimpleName();

    private SQLiteDatabase database;
    private DatabaseHelper databaseHelper;
    //Arrayliste mit dem Level, der Funktion und dem Tipp aus Tabelle 1
    private ArrayList<String> string_list = new ArrayList<String>();
    //Arrayliste mit allen Parametern, den Nullstellen, dem Achsenabschnitt und den Intervallen aus Tabelle 1
    private ArrayList<Float> float_list = new ArrayList<Float>();
    //Liste für die Daten aus dem Zwischenspeicher
    private ArrayList<Integer> integer_list = new ArrayList<>();


    /** Konstruktor */
    public Datasource(Context context) {
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den DatabaseHelper.");
        databaseHelper = new DatabaseHelper(context);
    }

    /** Callback-Methode open()
     * stellt eine Verbindung zur Datenbank her
     * */
    public void open() {
        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        database = databaseHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    /** Callback-Methode close()
     * schließt die Verbindung zur Datenbank
     */
    public void close() {
        databaseHelper.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }

    /** Methode String_Entries
     * selektiert die Funktion, den Tipp und das Level  aus Tabelle 1
     * und schreibt sie in eine Array liste
     * @return      Arrayliste von Strings mit Level, Funktion und Tipp drinnen
     */
    public ArrayList<String> String_Entries () {
        //Cursor der alle Level nacheinander durchläuft und level,funktion und tipp selektiert
        Cursor c = database.rawQuery("SELECT level_level, function, hint FROM " + DatabaseHelper.DB_TABLE_1, null);
        //auslesen der Indices
        int level = c.getColumnIndex(DatabaseHelper.LEVEL_LEVEL);
        int function = c.getColumnIndex(DatabaseHelper.FUNCTION);
        int hint = c.getColumnIndex(DatabaseHelper.HINT);
        //Cursor an erste Stelle setzen
        c.moveToFirst();

        while(!c.isAfterLast()) {
            //Suchanfragen ausgeben lassen und zur Liste hinzufuegen
            //solange es noch Datensätze gibt
            String level_l = c.getString(level);
            String function_f = c.getString(function);
            String hint_h = c.getString(hint);

            string_list.add(level_l + ";" + function_f + ";" + hint_h);
            //Cursor weiterlaufen lassen
            c.moveToNext();
        }
        //cursor muss geschlossen werden bzw wieder freigegeben werden
        c.close();
        Log.d(LOG_TAG, "String-Liste erstellt");
        return string_list;
    }

    /** Methode Float_Entries
     * selektiert alle Parameter, Nullstellen, Intervalle und den Achsenabschnitt
     * und schreibt sie in eine ArrayListe von Float
     * @return      alle Parameter,Nullstellen, Intervalle und den Achsenabschnitt in einer Arrayliste
     */
    public ArrayList<Float> Float_Entries () {
        //letzter Wert der query null, da wir alle level durchspielen -> ansonsten Where-Clausel
        Cursor c = database.rawQuery("SELECT a, b, c, d, x1, x2, y, min, max FROM " + DatabaseHelper.DB_TABLE_1, null);
        //auslesen der Indices
        int parameter_1 = c.getColumnIndex(DatabaseHelper.PARAMETER_1);
        int parameter_2 = c.getColumnIndex(DatabaseHelper.PARAMETER_2);
        int parameter_3 = c.getColumnIndex(DatabaseHelper.PARAMETER_3);
        int parameter_4 = c.getColumnIndex(DatabaseHelper.PARAMETER_4);
        int null_1 = c.getColumnIndex(DatabaseHelper.NULL_1);
        int null_2 = c.getColumnIndex(DatabaseHelper.NULL_2);
        int intercept = c.getColumnIndex(DatabaseHelper.INTERCEPT);
        int interval_1 = c.getColumnIndex(DatabaseHelper.INTERVAL_1);
        int interval_2 = c.getColumnIndex(DatabaseHelper.INTERVAL_2);
        //Cursor an erste Stelle setzen
        c.moveToFirst();

        while(!c.isAfterLast()) {
            //Suchanfragen ausgeben lassen und in Liste schreiben (pro Float Wert neue Spalte)
            //solange noch Werte vorhanden sind
            Float p_1 = c.getFloat(parameter_1);
            Float p_2 = c.getFloat(parameter_2);
            Float p_3 = c.getFloat(parameter_3);
            Float p_4 = c.getFloat(parameter_4);
            Float n_1 = c.getFloat(null_1);
            Float n_2 = c.getFloat(null_2);
            Float i = c.getFloat(intercept);
            Float min = c.getFloat(interval_1);
            Float max = c.getFloat(interval_2);

            float_list.add(p_1);
            float_list.add(p_2);
            float_list.add(p_3);
            float_list.add(p_4);
            float_list.add(n_1);
            float_list.add(n_2);
            float_list.add(i);
            float_list.add(min);
            float_list.add(max);

            //Cursor läuft weiter
            c.moveToNext();
        }
        //cursor schließen / freigeben
        c.close();
        Log.d(LOG_TAG, "Float-Liste erstellt");
        return float_list;
    }

    /**Methode insert_table2
     * schreibt das aktuelle Level und den aktuellen Punktestand für jedes Level in die Tabelle Temp_Storage in der Datenbank
     * @param level         aktuelles Level
     * @param points1       Punktestand aus Level 1
     * @param points2       Punktestand aus Level 2
     * @param points3       Punktestand aus Level 3
     * @param points4       Punktestand aus Level 4
     * @param points5       Punktestand aus Level 5
     */
    public void insert_table2 (int level, int points1, int points2, int points3, int points4, int points5) {
        //wenn nichts eingefuegt wurde -1 ausgeben
        long rowId = -1;
        try {
            //Die zu speichernden Werte
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.STORE_LEVEL, level);
            values.put(DatabaseHelper.STORE_POINTS_1, points1);
            values.put(DatabaseHelper.STORE_POINTS_2, points2);
            values.put(DatabaseHelper.STORE_POINTS_3, points3);
            values.put(DatabaseHelper.STORE_POINTS_4, points4);
            values.put(DatabaseHelper.STORE_POINTS_5, points5);
            //in die Tabelle Temp_Storage einfügen
            rowId = database.insert(DatabaseHelper.DB_TABLE_2,null,values);
        } catch (SQLiteException e) {
            Log.e(LOG_TAG, "insert(): rowId="+rowId);
        }
    }

    /** Methode Int_Entries
     * prueft ob level und punkte in db eingetragen wurden und schreibt sie in eine arrayliste
     * @return     Arrayliste mit Level und punkten
     */
    public ArrayList<Integer> Int_Entries () {
        //Cursor der level und punkte der tabelle zwischenspeicher durchläuft
        Cursor c = database.rawQuery("SELECT level, points1, points2, points3, points4, points5 FROM " + DatabaseHelper.DB_TABLE_2, null);
        //auslesen der Indices
        int akt_level = c.getColumnIndex(DatabaseHelper.STORE_LEVEL);
        int akt_punkte_1 = c.getColumnIndex(DatabaseHelper.STORE_POINTS_1);
        int akt_punkte_2 = c.getColumnIndex(DatabaseHelper.STORE_POINTS_2);
        int akt_punkte_3 = c.getColumnIndex(DatabaseHelper.STORE_POINTS_3);
        int akt_punkte_4 = c.getColumnIndex(DatabaseHelper.STORE_POINTS_4);
        int akt_punkte_5 = c.getColumnIndex(DatabaseHelper.STORE_POINTS_5);

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
        Log.d(LOG_TAG, "Integer-Liste erstellt");
        return integer_list;
    }

}