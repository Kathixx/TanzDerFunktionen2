package com.example.arabellaprivat.tanzderfunktionen.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;

import com.example.arabellaprivat.tanzderfunktionen.database.Datasource;
import com.example.arabellaprivat.tanzderfunktionen.R;

import java.util.ArrayList;

/**
 * Startbildschirm
 * wird beim Start der App automatisch aufgerufen
 */
public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    // IV
    /**
     * Begrüßungstext beim Start der App
     */
    private TextView t_willkommen;
    /**
     * Button, mit dem das Spiel gestartet wird
     */
    private Button b_start;
    /**
     * Button navigiert zur Anleitung
     */
    private Button b_anleitung;
    /**
     * setzt das Spiel an dem Punkt fort, wo man aufgehört hat
     */
    private Button b_weiterspielen;
    /**
     * legt ein Datenquellen-Objekt an
     */
    public static Datasource dataSource;

    /** Listen mit Datena aus der Datenbank */
    static ArrayList <Float> float_list;
    static ArrayList <String> string_list;


    /**
     * wird beim Start der App aufgerufen
     * setzt Layout und Interaktionen fest
     * legt Datenquellenobjekt an
     * öffnen und schließen der DBverbindung in lifecyle-callbacks ausgelagert
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(LOG_TAG, "Das Datenquellen-Objekt wird angelegt.");
        dataSource = new Datasource(this);


        // Variablen belegen
        t_willkommen = (TextView) findViewById(R.id.willkommen);
        b_start = (Button) findViewById(R.id.start);
        b_anleitung = (Button) findViewById(R.id.anleitung);
        b_weiterspielen = (Button) findViewById(R.id.weiterspielen);



        // man kann nur weiter spielen, wenn schonmal gespielt wurde
        // d. h. wenn in der Datenbank keine Bewertungen stehen kann man nur neu starten
        // TODO Abfrage: stehen in der Datenbank irgendwo Werte?
        // if(...){
        b_weiterspielen.setVisibility(View.INVISIBLE);
        // b_start.setText("Neustart");


        // Begrüßungstext anzeigen
        t_willkommen.setText("Herzlich Willkommen beim Tanz der Funktionen!");

        // Buttonfunktion die das Spiel neu startet erstellen
        b_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Datenbank zurücksetzen
                // in sendMessage wird die neue Activity gestartet
                sendMessage(v);
                // Listen aus der Klasse Datasource holen
                float_list= dataSource.Eintraege_Float();
                string_list= dataSource.Eintraege_String();
            }
        });

        b_weiterspielen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Datenbank nicht zurücksetzen??
                // in sendMessage wird die neue Activity gestartet
                sendMessage(v);
            }
        });

        // Buttonfunktion die zur Anleitung führt erstellen
        b_anleitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // in sendMessage wird die neue Activity gestartet
                sendMessage(v);
            }
        });
    }

    /**
     * wird beim Click auf einen Button aufgerufen
     * und startet abhängig vom Button die passende Activity
     *
     * @param view View, die geklickt wurde
     */
    public void sendMessage(View view) {
        // Spiel neu starten
        if (view.getId() == R.id.start) {
            Bundle b = new Bundle();

            // starte in Level 1
            b.putInt("Level", 1);

            // erstelle eine neue Liste für die Punkte
            ArrayList<Integer> levelpoints = new ArrayList<>(5);
            // levelpoints.trimToSize();
            // Liste mit null-Werten füllen
            for (int i = 0; i <= 5; i++) {
                levelpoints.add(i, 100);
            }
            b.putIntegerArrayList("Punkte", levelpoints);
            Intent i = new Intent(this, Spiel.class);
            i.putExtras(b);
            startActivity(i);
        }
        // Spiel weiter spielen
        if (view.getId() == R.id.weiterspielen) {
            Bundle b = new Bundle();
            // TODO letzten Stand aus der Datenbank holen
            // Level und die ArrayList mit den Punkten
            // b.putInt("Level", ...);
            // b.putIntegerArrayList("Punkte pro Level", ...);
            Intent i = new Intent(this, Spiel.class);
            i.putExtras(b);
            startActivity(i);
        }
        // Anleitung anzeigen lassen
        if (view.getId() == R.id.anleitung) {
            Intent i = new Intent(this, Anleitung.class);
            startActivity(i);
        }

    }

    /**
     * Callback Methode
     * stellt die Verbindung zur DB her
     */
    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "Die Datenquelle wird geöffnet.");
        dataSource.open();
    }

    /**
     * Callback Methode
     * schließt die Verbindung zur Datenbank
     */
    @Override
    protected void onPause() {
        super.onPause();

        Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
        //dataSource.close();
        //finish();
    }

   static ArrayList returnFloatList (){
        return float_list;
    }

    static ArrayList returnStringList (){
        return string_list;
    }


    // disable Back-Button
    @Override
    public void onBackPressed(){}
}

