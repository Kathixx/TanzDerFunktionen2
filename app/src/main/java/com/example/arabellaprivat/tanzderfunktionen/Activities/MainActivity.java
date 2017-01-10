package com.example.arabellaprivat.tanzderfunktionen.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.util.Log;
import java.util.ArrayList;

import com.example.arabellaprivat.tanzderfunktionen.database.Datasource;
import com.example.arabellaprivat.tanzderfunktionen.R;


/**
 * Startbildschirm
 * wird beim Start der App automatisch aufgerufen
 * Quelle der Methode firstTime(): http://www.andreabaccega.com/blog/2012/04/12/android-how-to-execute-some-code-only-on-first-time-the-application-is-launched/
 */
public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    // IV
    /** Begrüßungstext beim Start der App */
    private TextView t_willkommen;
    /** Button, mit dem das Spiel gestartet wird */
    private Button b_start;
    /** Button navigiert zur Anleitung */
    private Button b_anleitung;
    /** setzt das Spiel an dem Punkt fort, wo man aufgehört hat */
    private Button b_weiterspielen;
    /** hier werden die Punkte der einzelnen Level gespeichert und das Level selbst */
    private ArrayList<Integer> levelinfo;
    /** legt ein Datenquellen-Objekt an */
    public static Datasource dataSource;
    /** Listen mit Datena aus der Datenbank */
    static ArrayList <Float> float_list;
    static ArrayList <String> string_list;
    static ArrayList <Integer> integer_list;
    /** Variable ob Sound an oder off ist */
    boolean soundIsOn= true;
    /** gibt an, ob die App zum Ersten Mal nach Installation geöffnet wird */
    protected static Boolean firstTime = null;
    /**Popup Window informiert, dass zuerst auf einem BlattPapier gerechnet werden muss */
    private PopupWindow mainInfo;
    private View popupLayout4;
    private Button b_ok4;



    /**
     * wird beim Start der App aufgerufen
     * setzt Layout und Interaktionen fest
     * legt Datenquellenobjekt an
     * öffnen und schließen der DBverbindung in lifecyle-callbacks ausgelagert
     *
     * @param savedInstanceState    Siehe Super-Klasse
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Schriftart setzen
        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "fonts/Brandon_reg.otf");
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));
        // Schriftart für Popups extra holen
        Typeface fontBold = Typeface.createFromAsset(getAssets(),  "fonts/BAUHS93.TTF");
        Typeface fontRegular= Typeface.createFromAsset(getAssets(), "fonts/Brandon_reg.otf");

        Log.d(LOG_TAG, "Das Datenquellen-Objekt wird angelegt.");
        dataSource = new Datasource(this);


        // Variablen belegen
        t_willkommen = (TextView) findViewById(R.id.willkommen);
        b_start = (Button) findViewById(R.id.start);
        b_anleitung = (Button) findViewById(R.id.anleitung);
        b_weiterspielen = (Button) findViewById(R.id.weiterspielen);
        levelinfo = new ArrayList<>(6);


        // Begrüßungstext anzeigen
        t_willkommen.setText("Herzlich Willkommen beim Tanz der Funktionen");


        // LayoutInflater für alle PopUpWindows
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // Popup Window 4: Info, dass zuerst berechnet werden muss
        popupLayout4=inflater.inflate(R.layout.popup_maininfo, (ViewGroup)findViewById(R.id.popup_element_4));
        mainInfo= new PopupWindow(popupLayout4,300,370, true);
        b_ok4=(Button)popupLayout4.findViewById(R.id.ok);
        b_ok4.setOnClickListener(new View.OnClickListener() {
            /**
             * ermöglicht eine Aktoin beim Klick auf den Button
             * @param v View, auf die geklickt wurde
             */
            @Override
            public void onClick(View v) {
                mainInfo.dismiss();
                sendMessage(v);
            }
        });
        TextView popupText= (TextView)popupLayout4.findViewById(R.id.content);
        b_ok4.setTypeface(fontRegular);
        popupText.setTypeface(fontRegular);

        // wenn es das erste Mal ist, dass die App nach Installation geöffnet wird, heißt das, es gibt keinen Zwischenspeicher
        if(isFirstTime()){
            // blende die Möglichkeit weiterzuspielen aus
            b_weiterspielen.setVisibility(View.INVISIBLE);
            b_start.setText("Start");
        }


        // Buttonfunktion die das Spiel neu startet erstellen
        b_start.setOnClickListener(new View.OnClickListener() {
            /**
             * ermöglicht eine Aktoin beim Klick auf den Button
             * @param v View, auf die geklickt wurde
             */
            @Override
            public void onClick(View v) {
                // Listen aus der Klasse Datasource holen
                float_list= dataSource.Float_Entries();
                string_list= dataSource.String_Entries();

                // beim Neustart neue Liste erstellen, in der alle Infos stehen
                // Liste mit den richtigen Werten füllen
                // als erstes kommt das Level
                levelinfo.add(1);
                // dann folgen die Punkte der Level 1-5
                // default 200
                for(int i=1; i<=5; i++){
                    levelinfo.add(i, 200);
                }
                mainInfo.showAtLocation(popupLayout4, Gravity.CENTER, 0, 0);

            }
        });

        b_weiterspielen.setOnClickListener(new View.OnClickListener() {
            /**
             * ermöglicht eine Aktoin beim Klick auf den Button
             * @param v View, auf die geklickt wurde
             */
            @Override
            public void onClick(View v) {
                // Listen aus der Klasse Datasource holen
                float_list= dataSource.Float_Entries();
                string_list= dataSource.String_Entries();
                // wenn weiter gespielt werden soll, brauchen wir den letzten Zwischenstand
                integer_list = dataSource.Int_Entries();

                // Level am Index 0 speichern
                levelinfo.add(integer_list.get(integer_list.size()-6));
                // levelinfo füllen
                // am Index 1-5 sollen die letzten 5 Einträge der Liste stehen
                // im 5. Level steht der letzte Wert der Liste
                int index = (integer_list.size()-5);
                for(int i=1; i<=5; i++) {
                    levelinfo.add(integer_list.get(index));
                    index++;
                }
                mainInfo.showAtLocation(popupLayout4, Gravity.CENTER, 0, 0);
            }
        });

        b_anleitung.setOnClickListener(new View.OnClickListener() {
            /**
             * ermöglicht eine Aktoin beim Klick auf den Button
             * @param v View, auf die geklickt wurde
             */
            @Override
            public void onClick(View v) {
                // in sendMessage wird die neue Activity gestartet
                sendMessage(v);
            }
        });
    }//Ende OnCreate

    /**
     * Checks if the user is opening the app for the first time.
     * Note that this method should be placed inside an activity and it can be called multiple times.
     * Source: http://www.andreabaccega.com/blog/2012/04/12/android-how-to-execute-some-code-only-on-first-time-the-application-is-launched/
     * @return boolean      edited variable firstTime
     */
    private boolean isFirstTime() {
        if (firstTime == null) {
            SharedPreferences mPreferences = this.getSharedPreferences("first_time", Context.MODE_PRIVATE);
            firstTime = mPreferences.getBoolean("firstTime", true);
            if (firstTime) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean("firstTime", false);
                editor.commit();
            }
        }
        if(!firstTime)
            firstTime = true;
        return firstTime;
    }

    /**
     * erstellt die Icons in der ActionBar
     * @param menu  Menü in der ActionBar
     * @return      true wenn das Menü erstellt wurde
     */
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions, menu);
        int i=R.id.sound;

        return true;
    }

    /**
     * ermöglicht den Klick auf ein Item des Menüs
     * @param item  Element, das angeklickt wurde
     * @return      siehe Super-Klasse
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sound:
                changeSound(item);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * wird beim Klick auf einen Button aufgerufen
     * und startet abhängig vom Button die passende Activity
     * @param view View, die geklickt wurde
     */
    public void sendMessage(View view) {
        // Spiel neu starten oder weiterspielen
        if (view.getId()==R.id.ok) {
            // Bundle füllen
            // Sound
            // je nach dem ob Sound on oder off ist
            Bundle b = new Bundle();
            if (soundIsOn) b.putBoolean("Sound",true);
            else b.putBoolean("Sound", false);

            // Liste mit Infos
            b.putIntegerArrayList("Infos", levelinfo);

            Intent intent = new Intent(this, Spiel.class);
            intent.putExtras(b);
            startActivity(intent);
        }
        // Anleitung anzeigen lassen
        if (view.getId() == R.id.anleitung) {
            startActivity(new Intent(this, Anleitung.class));
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
    }

    static ArrayList returnFloatList (){
        return float_list;
    }

    static ArrayList returnStringList (){
        return string_list;
    }


    /**
     * verbietet das zurück-gehen mit dem "Zurück"-Button des Tablets
     */
    @Override
    public void onBackPressed(){}


    private void changeSound(MenuItem item){
        if (soundIsOn) {
            soundIsOn=false;
            // Icon ändern
            item.setIcon(R.mipmap.sound_off);

        }
        else {
            soundIsOn=true;
            item.setIcon(R.mipmap.sound_on_white);
        }
    }



}



