package com.example.arabellaprivat.tanzderfunktionen;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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

    //Alle Spalten, die wir uns ausgeben lassen
    private String[] columns_complete = {
            DatabaseHelper.LEVEL_LEVEL,
            DatabaseHelper.FUNKTION,
            DatabaseHelper.PARAMETER_1,
            DatabaseHelper.PARAMETER_2,
            DatabaseHelper.PARAMETER_3,
            DatabaseHelper.MIN,
            DatabaseHelper.MAX,
            DatabaseHelper.TIPP
    };

    //Selection und SelectionArgs ( WHERE BEDINGUNG )
    String selection = DatabaseHelper.LEVEL_LEVEL + " = ?";
    String[] selectionArgs_1 = {"1"};
    String[] selectionArgs_2 = {"2"};
    String[] selectionArgs_3 = {"3"};
    String[] selectionArgs_4 = {"4"};
    String[] selectionArgs_5 = {"5"};

    public Datasource(Context context) {
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den DatabaseHelper.");
        databaseHelper = new DatabaseHelper(context);
    }
    /* Callback-Methoden open() und close() */
    public void open() {
        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        //Verbindung zur DB herstellen
        database = databaseHelper.getWritableDatabase();
        //database = databaseHelper.getReadableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    public void close() {
        //Verbindung zur DB schließen
        databaseHelper.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }

    /**Methode
     * wandelt ein Cursor-Objekt in ein DatabaseTable_1-Objekt um
     * @param cursor
     * @return
     */
    private DatabaseTable_1 cursorToDatabaseTable_1(Cursor cursor) {
        //auslesen der Indizes
        int idLevel = cursor.getColumnIndex(DatabaseHelper.LEVEL_LEVEL);
        int idFunktion = cursor.getColumnIndex(DatabaseHelper.FUNKTION);
        int idP_1 = cursor.getColumnIndex(DatabaseHelper.PARAMETER_1);
        int idP_2 = cursor.getColumnIndex(DatabaseHelper.PARAMETER_2);
        int idP_3 = cursor.getColumnIndex(DatabaseHelper.PARAMETER_3);
        int id_min = cursor.getColumnIndex(DatabaseHelper.MIN);
        int id_max = cursor.getColumnIndex(DatabaseHelper.MAX);
        int id_tipp = cursor.getColumnIndex(DatabaseHelper.TIPP);
        //Suchanfragen ausgeben lassen
        int level = cursor.getInt(idLevel);
        String funktion = cursor.getString(idFunktion);
        float p_1 = cursor.getFloat(idP_1);
        float p_2 = cursor.getInt(idP_2);
        float p_3 = cursor.getInt(idP_3);
        int min = cursor.getInt(id_min);
        int max = cursor.getInt(id_max);
        String tipp = cursor.getString(id_tipp);
        //DatabaseTable_1 Objekt erzeugen mitHilfe der ausgelesenen Daten
        DatabaseTable_1 tabelle_1 = new DatabaseTable_1(level,funktion,p_1,p_2,p_3,min,max,tipp);

        return tabelle_1;
    }

    /**Methode
     * liest alle vorhandenen Datensätze aus Tabelle aus
     * @return
     */
    public List<DatabaseTable_1> getAllEntries() {
        //Liste, die DatabaseTable_1-Objekt aufnehmen kann
        List<DatabaseTable_1> db_all_List = new ArrayList<>();
        //Suchanfrage, dritte stelle null, also alle Datensätze auslesen
        //query(db,spalten,selection,selectionsArgs,groupy,having,orderBy
        Cursor cursor_alle = database.query(DatabaseHelper.DB_TABLE_1,
                columns_complete, null, null, null, null, null);

        cursor_alle.moveToFirst();
        DatabaseTable_1 databaseTable_1;
        //alle Datensätze auslesen und in DatabaseTable_1-Objekte umwandeln
        //und der Liste hinzufuegen
        while(!cursor_alle.isAfterLast()) {
            databaseTable_1 = cursorToDatabaseTable_1(cursor_alle);
            db_all_List.add(databaseTable_1);
            Log.d(LOG_TAG, " Inhalt der Tabelle 1: " + databaseTable_1.toString());
            cursor_alle.moveToNext();
        }
        //Suchanfrage schließen
        cursor_alle.close();
        //erzeugte DatabaseTable_1-Liste an die aufrufende Methode zurückgeben
        return db_all_List;
    }

    /**Methode
     * liest Spalte 1 aus Tabelle aus
     * @return
     */
    public List<DatabaseTable_1> getColumnOne() {
        //Liste, die DatabaseTable_1-Objekt aufnehmen kann
        List<DatabaseTable_1> db_1_List = new ArrayList<>();
        //Suchanfrage, dritte stelle null, also alle Datensätze auslesen
        //query(db,spalten,selection,selectionsArgs,groupy,having,orderBy
        Cursor cursor_1 = database.query(DatabaseHelper.DB_TABLE_1,
                columns_complete, selection, selectionArgs_1, null, null, null);

        cursor_1.moveToFirst();
        DatabaseTable_1 databaseTable_1;
        //alle Datensätze auslesen und in DatabaseTable_1-Objekte umwandeln
        //und der Liste hinzufuegen
        while(!cursor_1.isAfterLast()) {
            databaseTable_1 = cursorToDatabaseTable_1(cursor_1);
            db_1_List.add(databaseTable_1);
            Log.d(LOG_TAG, " Inhalt der Tabelle 1: " + databaseTable_1.toString());
            cursor_1.moveToNext();
        }
        //Suchanfrage schließen
        cursor_1.close();
        //erzeugte DatabaseTable_1-Liste an die aufrufende Methode zurückgeben
        return db_1_List;
    }

    /**Methode
     * liest Spalte 2 aus Tabelle aus
     * @return
     */
    public List<DatabaseTable_1> getColumnTwo() {
        //Liste, die DatabaseTable_1-Objekt aufnehmen kann
        List<DatabaseTable_1> db_2_List = new ArrayList<>();
        //Suchanfrage, dritte stelle null, also alle Datensätze auslesen
        //query(db,spalten,selection,selectionsArgs,groupy,having,orderBy
        Cursor cursor_2 = database.query(DatabaseHelper.DB_TABLE_1,
                columns_complete, selection, selectionArgs_2, null, null, null);

        cursor_2.moveToFirst();
        DatabaseTable_1 databaseTable_1;
        //alle Datensätze auslesen und in DatabaseTable_1-Objekte umwandeln
        //und der Liste hinzufuegen
        while(!cursor_2.isAfterLast()) {
            databaseTable_1 = cursorToDatabaseTable_1(cursor_2);
            db_2_List.add(databaseTable_1);
            Log.d(LOG_TAG, " Inhalt der Tabelle 1: " + databaseTable_1.toString());
            cursor_2.moveToNext();
        }
        //Suchanfrage schließen
        cursor_2.close();
        //erzeugte DatabaseTable_1-Liste an die aufrufende Methode zurückgeben
        return db_2_List;
    }

    /**Methode
     * liest alle Spalte 3 aus Tabelle aus
     * @return
     */
    public List<DatabaseTable_1> getColumnThree() {
        //Liste, die DatabaseTable_1-Objekt aufnehmen kann
        List<DatabaseTable_1> db_3_List = new ArrayList<>();
        //Suchanfrage, dritte stelle null, also alle Datensätze auslesen
        //query(db,spalten,selection,selectionsArgs,groupy,having,orderBy
        Cursor cursor_3 = database.query(DatabaseHelper.DB_TABLE_1,
                columns_complete, selection, selectionArgs_3, null, null, null);

        cursor_3.moveToFirst();
        DatabaseTable_1 databaseTable_1;
        //alle Datensätze auslesen und in DatabaseTable_1-Objekte umwandeln
        //und der Liste hinzufuegen
        while(!cursor_3.isAfterLast()) {
            databaseTable_1 = cursorToDatabaseTable_1(cursor_3);
            db_3_List.add(databaseTable_1);
            Log.d(LOG_TAG, " Inhalt der Tabelle 1: " + databaseTable_1.toString());
            cursor_3.moveToNext();
        }
        //Suchanfrage schließen
        cursor_3.close();
        //erzeugte DatabaseTable_1-Liste an die aufrufende Methode zurückgeben
        return db_3_List;
    }

    /**Methode
     * liest alle Spalte 4 aus Tabelle aus
     * @return
     */
    public List<DatabaseTable_1> getColumnFour() {
        //Liste, die DatabaseTable_1-Objekt aufnehmen kann
        List<DatabaseTable_1> db_4_List = new ArrayList<>();
        //Suchanfrage, dritte stelle null, also alle Datensätze auslesen
        //query(db,spalten,selection,selectionsArgs,groupy,having,orderBy
        Cursor cursor_4 = database.query(DatabaseHelper.DB_TABLE_1,
                columns_complete, selection, selectionArgs_4, null, null, null);

        cursor_4.moveToFirst();
        DatabaseTable_1 databaseTable_1;
        //alle Datensätze auslesen und in DatabaseTable_1-Objekte umwandeln
        //und der Liste hinzufuegen
        while(!cursor_4.isAfterLast()) {
            databaseTable_1 = cursorToDatabaseTable_1(cursor_4);
            db_4_List.add(databaseTable_1);
            Log.d(LOG_TAG, " Inhalt der Tabelle 1: " + databaseTable_1.toString());
            cursor_4.moveToNext();
        }
        //Suchanfrage schließen
        cursor_4.close();
        //erzeugte DatabaseTable_1-Liste an die aufrufende Methode zurückgeben
        return db_4_List;
    }

    /**Methode
     * liest alle Spalte 5 aus Tabelle aus
     * @return
     */
    public List<DatabaseTable_1> getColumnFive() {
        //Liste, die DatabaseTable_1-Objekt aufnehmen kann
        List<DatabaseTable_1> db_5_List = new ArrayList<>();
        //Suchanfrage, dritte stelle null, also alle Datensätze auslesen
        //query(db,spalten,selection,selectionsArgs,groupy,having,orderBy
        Cursor cursor_5 = database.query(DatabaseHelper.DB_TABLE_1,
                columns_complete, selection, selectionArgs_5, null, null, null);

        cursor_5.moveToFirst();
        DatabaseTable_1 databaseTable_1;
        //alle Datensätze auslesen und in DatabaseTable_1-Objekte umwandeln
        //und der Liste hinzufuegen
        while(!cursor_5.isAfterLast()) {
            databaseTable_1 = cursorToDatabaseTable_1(cursor_5);
            db_5_List.add(databaseTable_1);
            Log.d(LOG_TAG, " Inhalt der Tabelle 1: " + databaseTable_1.toString());
            cursor_5.moveToNext();
        }
        //Suchanfrage schließen
        cursor_5.close();
        //erzeugte DatabaseTable_1-Liste an die aufrufende Methode zurückgeben
        return db_5_List;
    }


}