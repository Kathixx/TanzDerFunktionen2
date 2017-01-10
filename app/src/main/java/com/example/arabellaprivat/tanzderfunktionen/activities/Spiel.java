package com.example.arabellaprivat.tanzderfunktionen.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import com.example.arabellaprivat.tanzderfunktionen.checkAndDraw.Hilfspunkte;
import com.example.arabellaprivat.tanzderfunktionen.checkAndDraw.Pruefung;
import com.example.arabellaprivat.tanzderfunktionen.R;
import com.example.arabellaprivat.tanzderfunktionen.checkAndDraw.Zeichenfläche;
import com.example.arabellaprivat.tanzderfunktionen.database.Datasource;


/**
 * hier fidet das Spiel / das Zeichnen statt
 *
 * Quellen:
 * Bundle   https://www.youtube.com/watch?v=KRN7EYvorZY
 * DoubleClick: http://stackoverflow.com/questions/5608720/android-preventing-double-click-on-a-button
 * Menü: https://developer.android.com/guide/topics/ui/menus.html
 */
public class Spiel extends AppCompatActivity {

    // IV
    /** Info-Button */
    private Button b_info;
    /** Button zum Löschen der View */
    private Button b_delete;
    /** Button zum prüfen der gemalten Funktion */
    private Button b_check;
    /** führt zum nächsten Level oder zur Endbewertung */
    private Button b_next;
    /** sagt, in welchem Intervall gezeichnet werden muss */
    private TextView t_intervall;
    /** zeigt die zu malende Funktion an */
    private TextView t_function;
    /** diese TextView zeigt das aktuelle Level an */
    private TextView t_level;
    /** gibt die Punkteanzahl aus */
    private TextView t_number;
    /** Punkte, die zeigen, welche Level wie abgeschlossen wurden */
    private ImageView i_score;
    /** zeigt das aktuelle Level an */
    private int level;
    /** speichert Infos über das aktulle Spiel
     * Index 0:     aktuelles Level
     * Index 1-5:   Punkte des jeweiligen Levels */
    private ArrayList<Integer> levelinfo;
    /** Zeichenfläche, auf der die Funktionen gezeichnet werden */
    private Zeichenfläche z;
    /** Touchfläche für Hilfspunkte */
    private Hilfspunkte h;
    /** Button um nach dem einzeichnen der Hilfspunkte die Funktion zu zeichnen*/
    private Button b_draw;


    //Instanz vonn Datasource
    Datasource datasource = MainActivity.dataSource;
    /* Instanzen für das Speichern der Aktuellen Punktestände */
    private int levelpoint1; //= levelinfo.get(1);
    private int levelpoint2; //= levelinfo.get(2);
    private int levelpoint3; //= levelinfo.get(3);
    private int levelpoint4; //= levelinfo.get(4);
    private int levelpoint5; //= 3; //levelinfo.get(5)
    /** Listen zum Auslesen aus der Datenbank */
    // float_list enthält alle Parameter, Nullstellen und Achsenabschnitte der Funktionen
    private ArrayList <Float> float_list;
    // string_list enthält alle Text, also Funktion als Ganzes und Tipps
    // Texte eines Levels in einem Array mit Komma getrennt
    private ArrayList <String> string_list;
    // temporäres Array, in dem die Texte des jeweiligen Levels gespeichert werden
    static String [] text;
    // temporäres Array, in dem alle WErte des jeweiligen Levels gespeichert werden
    private double [] parameters;
    static double [] para;

    /** Zeitstempel für das Abfangen von DoppelKlick */
    private long lastClick=0;

    /** Media Player für Musik */
    private MediaPlayer mp;
    /** Variable für Sound */
    private boolean soundIsOn;

    /** Popup Window informiert falls nichts gezeichnet wurde   */
    private PopupWindow nothingIsDrawn;
    private View popupLayout2;
    private Button b_ok2;
    private Pruefung p;
    /** Popup Window informiert, falls Graph zu kuzr gezeichnet wurde */
    private PopupWindow pathTooShort;
    private View popupLayout3;
    private Button b_ok3;
    /** Popup Window informiert, wenn das angeklickte Level nicht ausgewählt werden darf */
    private PopupWindow w_forbidden_choice;
    private View layout;
    private Button b_ok;

    /** Popup Window, informiert über den erreichten Punktestand nach der Überprüfung */
    private PopupWindow scoreInThisLevel;
    private View popupLayout4;
    private TextView t_result2;
    private TextView t_points;
    private TextView t_conclusion;

    /** Popup Window zeigt Tipps zum zeichnen */
    private PopupWindow tipps;
    private View popuplayout5;
    private Button b_ok5;
    private TextView t_tipps;

    /**
     * erstellt die Activity bei dessen Aufruf
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spiel);

        // Schriftart für die ganze Activity ändern mithilfe des FontChangeCrawlers
        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "fonts/Brandon_reg.otf");
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));
        // Schriftart für Popups extra holen
        Typeface fontBold = Typeface.createFromAsset(getAssets(),  "fonts/BAUHS93.TTF");
        Typeface fontRegular= Typeface.createFromAsset(getAssets(), "fonts/Brandon_reg.otf");

        // Variablen belegen
        b_info      = (Button) findViewById(R.id.info);
        b_delete    = (Button) findViewById(R.id.delete);
        b_check     = (Button) findViewById(R.id.check);
        b_next      = (Button) findViewById(R.id.next);
        t_intervall = (TextView) findViewById(R.id.review);
        t_function  = (TextView) findViewById(R.id.functionText);
        t_level     = (TextView) findViewById(R.id.level);
        t_number    = (TextView) findViewById(R.id.number);
        i_score     = (ImageView) findViewById(R.id.score);
        z           = (Zeichenfläche) findViewById(R.id.zeichenfläche);
        h           = (Hilfspunkte) findViewById (R.id.hilfspunkte);
        b_draw      = (Button) findViewById(R.id.draw);
        p           = new Pruefung(z, h);


        // LayoutInflater für alle PopUpWindows
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // PopupWindow 1: falls ein Level schon gespielt aber nochmal aufgerufen wurde
        layout = inflater.inflate(R.layout.popupwindow, (ViewGroup) findViewById(R.id.popup_element));
        w_forbidden_choice = new PopupWindow(layout, 300, 370, true);
        b_ok = (Button) layout.findViewById(R.id.ok);
        b_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                w_forbidden_choice.dismiss();
            }
        });
        TextView popupText= (TextView)layout.findViewById(R.id.content);
        b_ok.setTypeface(fontRegular);
        popupText.setTypeface(fontRegular);


        // Popup Window 2: Falls nichts gezeichnet wurde und auf überprüfen geklickt wurde
        popupLayout2=inflater.inflate(R.layout.popupwindow2, (ViewGroup)findViewById(R.id.popup_element_2));
        nothingIsDrawn= new PopupWindow(popupLayout2, 300,200, true);
        b_ok2=(Button) popupLayout2.findViewById(R.id.ok);
        b_ok2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nothingIsDrawn.dismiss();
            }
        });
        popupText= (TextView)popupLayout2.findViewById(R.id.content);
        b_ok2.setTypeface(fontRegular);
        popupText.setTypeface(fontRegular);


        // Popup Window 3: Falls der Pfad zu kurz also in einem zu kleinen Intervall gezeichnet wurde
        popupLayout3=inflater.inflate(R.layout.popup_pathtooshort, (ViewGroup)findViewById(R.id.popup_element_3));
        pathTooShort= new PopupWindow(popupLayout3,300,200, true);
        b_ok3= (Button) popupLayout3.findViewById(R.id.ok);
        b_ok3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {pathTooShort.dismiss();z.deleteView();
            }
        });
        popupText= (TextView)popupLayout3.findViewById(R.id.content);
        b_ok3.setTypeface(fontRegular);
        popupText.setTypeface(fontRegular);

        // Popup Window 4: Zeigt das Ergebnis und die Punkte des gezeichneten Graphen an
        popupLayout4=inflater.inflate(R.layout.activity_punkte, (ViewGroup)findViewById(R.id.popup_element_4));
        scoreInThisLevel= new PopupWindow(popupLayout4,400,350, true);
        t_result2=(TextView) popupLayout4.findViewById(R.id.Ergebnis);
        t_points=(TextView) popupLayout4.findViewById(R.id.Punkte);
        t_conclusion=(TextView) popupLayout4.findViewById(R.id.Erklärung);
        // Eigenschaften des Popup Windows festlegen: Bei Klick auserhalb des Popup soll sich dieses schließen
        scoreInThisLevel.setBackgroundDrawable(new BitmapDrawable());
        t_result2.setTypeface(fontRegular);
        t_conclusion.setTypeface(fontRegular);
        t_points.setTypeface(fontBold
        );

        // Popup Window 5: Tipps die beim Zeichnen der Funktion helfen
        popuplayout5 = inflater.inflate(R.layout.popup_tipps, (ViewGroup) findViewById(R.id.popup_tipps));
        tipps = new PopupWindow(popuplayout5, 300, 200, true);
        b_ok5 = (Button) popuplayout5.findViewById(R.id.ok);
        b_ok5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipps.dismiss();
            }
        });
        t_tipps = (TextView) popuplayout5.findViewById(R.id.content);
        b_ok5.setTypeface(fontRegular);
        t_tipps.setTypeface(fontRegular);

        // Informationen aus der MainActivity holen und verarbeiten
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        // Liste mit den Infos über Level und Punkte
        levelinfo = bundle.getIntegerArrayList("Infos");
        // Das aktuelle Level steht am Index 0
        level = levelinfo.get(0);

        // Listen aus der Main Activity holen
        float_list= MainActivity.returnFloatList();
        string_list= MainActivity.returnStringList();

        text= new String [3];


        parameters= new double [9];
        para= new double [9];

        // MediaPlayer für Musik
        mp= MediaPlayer.create(this, R.raw.confirm);
        // Variable ob Sound on oder of ist
        soundIsOn=bundle.getBoolean("Sound");



        //  Weiter Button ist erstmal unsichtbar
        b_next.setVisibility(View.INVISIBLE);
        // auch eigentliche View zum Zeichnen der Funktion sowie der Überprüfungsbutton sind unsichtbar
        z.setVisibility(View.INVISIBLE);
        b_check.setVisibility(View.INVISIBLE);

        // zeigen, in welchem Level wir sind
        t_level.setText("Level " + level);

        // Kreise zur Anzeige, wie die Level absolviert wurden
        this.visualizeScore();

        // Zwischenstand der Punkte anzeigen
        int score = 0;
        for(int i=1; i<=5; i++) {
            if (levelinfo.get(i) != 200)
                score += levelinfo.get(i);
        }
        // Punktestand anzeigen
        t_number.setText(String.valueOf(score));
        t_number.setTypeface(fontBold);


        // einzelne Strings des aktuellen Levels in dem TextARRAY abspeichern
        splitArray(string_list.get(level-1));
        // liest die jeweilige Funktion aus
        t_function.setText(getFunction());
        // Array mit aktuellen Parametern des Levels füllen
        // parameters= getParameters(level, float_list);
        insertParameters(level, float_list);
        //TODO
        double iMin= float_list.get(getiMin(level, float_list));
        double iMax= float_list.get(getiMax(level, float_list));
        t_intervall.setText("Bitte zeichne den Funktionsgraphen im Intervall von "+ String.valueOf(iMin)+" und "+String.valueOf(iMax));




        b_info.setOnClickListener(new View.OnClickListener() {
            /**
             * ermöglicht eine Aktoin beim Klick auf den Button
             * @param v View, auf die geklickt wurde
             */
            @Override
            public void onClick(View v) {
                sendMessage(v);
            }
        });

        b_delete.setOnClickListener(new View.OnClickListener() {
            /**
             * ermöglicht eine Aktoin beim Klick auf den Button
             * @param v View, auf die geklickt wurde
             */
            @Override
            public void onClick(View v) {
                // Je nach dem ob gerade Hilfspunkte eingezeichnet werden oder schon die eigentliche Funktion gezeichnet wird
                // werden unterschiedliche Aktionen von diesem Button hervorgerufen
                // die Zeichenfläche die Sichtbar ist wird geleert
                if (z.getVisibility() == View.INVISIBLE) {
                    // bei DoubleKlick werden alle Hilfspunkte gelöscht
                    // elapsedRealtime() gibt die Sekunden (nano!) zurück, seid dem letzten boot
                    // 500 gibt die Sekunden an inherhalb denen man doppelt geklickt haben muss
                    if (SystemClock.elapsedRealtime() - lastClick < 500) {
                        h.deleteView();
                    }
                    // bei einfachen Klick nur den letzten Hilfspunkt löschen
                    else h.deleteLast();
                }
                // wenn man bereits beim Zeichnen ist,
                // sollen die Hilfspunkte nicht mehr verändert werden
                // nur der Graph soll gelöscht werden
                // hier nur einfacher Klick möglich
                else z.deleteView();
                lastClick = SystemClock.elapsedRealtime();
            }
        });

        b_draw.setOnClickListener(new View.OnClickListener() {
            /**
             * ermöglicht eine Aktoin beim Klick auf den Button
             * @param v View, auf die geklickt wurde
             */
            @Override
            public void onClick(View v) {
                //Zeichenfläche zum Zeichnen der Funktion sichtbar machen ("einschalten")
                z.setVisibility(View.VISIBLE);
                // Button für Beenden der Hilfspunkte-setzen ausschalten
                b_draw.setVisibility(View.INVISIBLE);

                // Button zum Überprüfen der Funktion einschalten
                b_check.setVisibility(View.VISIBLE);


                //Toast als Hinweis, dass nun der Funktionsgraph eingezeichnet werden soll
                Context context = getApplicationContext();
                CharSequence text = "Zeichne jetzt den Funktionsgraph ein.";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        b_check.setOnClickListener(new View.OnClickListener() {
            /**
             * ermöglicht eine Aktoin beim Klick auf den Button
             * @param v View, auf die geklickt wurde
             */
            @Override
            public void onClick(View v) {
                // Intervall soll angezeigt werden
                t_intervall.setVisibility(View.VISIBLE);
                // die Funktion zum Prüfen der Funktion wird aufgerufen
                // zunächst wird überprüft ob überhaupt ein Graph gezeichnet wurde und ein entsprechender dialog angezeigt
                if (p.pathIsEmpty()){
                    nothingIsDrawn.showAtLocation(popupLayout2, Gravity.TOP, 0, 358);
                }
                else {

                   if (!p.pathIsInIntervall(para)) {
                        pathTooShort.showAtLocation(popupLayout3, Gravity.TOP, 0, 380);
                   }
                    else {
                    check(p);
                     }
                }

            }
        });

        b_next.setOnClickListener(new View.OnClickListener() {
            /**
             * ermöglicht eine Aktoin beim Klick auf den Button
             * @param v View, auf die geklickt wurde
             */
            @Override
            public void onClick(View v) {
                sendMessage(v);
            }
        });

    }// Ende onCreate


    /**
     * erstellt die Icons in der ActionBar
     * @param menu  Menü in der ActionBar
     * @return      true wenn das Menü erstellt wurde
     */
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sound_level, menu);
        if (!soundIsOn) changeIcon(menu);
        return true;

    }

    /**
     * ermöglicht den Klick auf ein Item des Menüs
     * @param item  Element, das angeklickt wurde
     * @return      siehe Super-Klasse
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.sound:
                changeSound(item);
                break;
            case R.id.level1:
                chooseLevel(1);
                break;
            case R.id.level2:
                chooseLevel(2);
                break;
            case R.id.level3:
                chooseLevel(3);
                break;
            case R.id.level4:
                chooseLevel(4);
                break;
            case R.id.level5:
                chooseLevel(5);
                break;
            case R.id.home:
                MainActivity.firstTime = false;
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.anleitung:
                startActivity(new Intent(this, Anleitung.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * navigiert zum ausgewähltem Level
     * @param chosenLevel   Level, auf das geklickt wurde
     */
    public void chooseLevel(int chosenLevel){
        // wenn das Level ausgewählt werden darf
        if(levelinfo.get(chosenLevel) == 200){
            // speicher das Level in einem Bundle
            Bundle bundle = new Bundle();
            // Level aktualisieren
            levelinfo.set(0, chosenLevel);
            bundle.putIntegerArrayList("Infos", levelinfo);
            bundle.putBoolean("Sound", soundIsOn);
            // neues Intent
            Intent i_new = new Intent(this, Spiel.class);
            i_new.putExtras(bundle);
            // Activity starten
            startActivity(i_new);
        } else {
            w_forbidden_choice.showAtLocation(layout, Gravity.CENTER, 0, 0);
        }
    }

    /**
     * koordiniert das Zeichnen der Punkteanzeige
     */
    private void visualizeScore() {
        Bitmap bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
        // der Abstand vom linken Bildschirmrand wird um jew. 15 px erhöht
        int abstand_x = 0;

        for (int i = 1; i <= 5; i++) {
            // Farb-Eigenschaften festlegen
            Paint paint = setPaint(i);
            // Abstand zum linken Nachbarn vergrößern
            abstand_x += 15;
            // damit den Kreis zeichnen
            this.paintCircle(bitmap, paint, abstand_x);
        }
    }

    /**
     * setzt die Farbe des zu zeichnenden Kreises fest
     * @param circleNumber  Kreis, der gezeichnet werden soll
     * @return              Farbe und Style des Kreises
     */
    private Paint setPaint(int circleNumber){
        // Style und Farbe hängen von der Bewertung der Level ab
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        // alle nicht gemachten Level sind schwarz umrandet
        if(levelinfo.get(circleNumber) == 200) {
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
        }
        // 5. Stufe: rot
        else if(levelinfo.get(circleNumber) <= 40)
            paint.setColor(Color.rgb(153,2,14));
        // 4. Stufe: orange
        else if(levelinfo.get(circleNumber) <= 50)
            paint.setColor(Color.rgb(255, 127, 39));
        // 3. Stufe: gelb
        else if(levelinfo.get(circleNumber) <= 70)
            paint.setColor(Color.rgb(255, 201, 14));
        // 2. Stufe: hellgrün
        else if(levelinfo.get(circleNumber) <= 90)
            paint.setColor(Color.rgb(181, 230, 29));
        // 1. Stufe: grün
        else if(levelinfo.get(circleNumber) <= 100)
            paint.setColor(Color.rgb(34, 177, 76));
        return paint;
    }

    /**
     * zeichnet einen Kreis
     * @param bitmap        Hier wird gezeichnet
     * @param paint         Zeichen-Eigenschaften
     * @param abstand_x     Abstand zum linken Bildschirmrand, bzw. zum linken Nachbar-Kreis
     */
    private void paintCircle(Bitmap bitmap, Paint paint, int abstand_x){
        Canvas canvas = new Canvas(bitmap);
        // Kreis zeichnen
        // mit dem sich mit jedem Kreis vergrößernden linken Abstand
        // Abstand nach oben 5 px
        // Radius 5 px
        // mit den "Stift"-Eigenschaften, die je nach Level verändert wurden
        canvas.drawCircle(abstand_x, 5, 5, paint);

        // in die ImageView einfügen
        i_score.setImageBitmap(bitmap);
    }

    /**
     * wird beim Click auf einen Button aufgerufen
     * und startet abhängig vom Button die passende Activity
     * @param view  Button, der geklickt wurde
     */
    public void sendMessage(View view){
        // Infos im Bundle speichern
        Bundle bundle = new Bundle();

        if(view.getId() == R.id.info) {
            tipps.showAtLocation(popuplayout5, Gravity.BOTTOM, 0, 0);
            t_tipps.setText(getInfo());
        }

        else if(view.getId() == R.id.next) {
            // wenn alle 5 Level gespielt wurden
            // Level die noch nicht gemacht wurden haben von Beginn an den Wert 200
            if(levelinfo.get(1) != 200 && levelinfo.get(2) != 200 && levelinfo.get(3) != 200 && levelinfo.get(4) != 200 && levelinfo.get(5) != 200){
                // gehe zur Endbewertung
                bundle.putIntegerArrayList("Infos", levelinfo);
                bundle.putBoolean("Sound", soundIsOn);
                Intent intent = new Intent(this, Bewertung.class);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                // gehe in das Level, das noch nicht gespielt wurde
                int counter = 1;
                while(levelinfo.get(counter) != 200)
                    counter++;
                // in dieses Level gehen
                levelinfo.set(0, counter);
                bundle.putIntegerArrayList("Infos", levelinfo);
                bundle.putBoolean("Sound",soundIsOn);
                Intent intent = new Intent(this, Spiel.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }

    /**
     * Teilt die Texte des ersten Levels, die nur mit einem Komma getrennt sind und gibt sie als einzelnes Array zurück
     * an stelle 0: Level
     * an Stelle 1: Funktion
     * an Stelle 2: Tipp
     * TODO Komma kommen auch im Info-Satz vor, sodass der Satz auch getrennt wird :(
     * @param s
     */
    public void splitArray (String s){
        text = s.split(";");
    }

    /**
     * Gibt die aktuelle Funktion als String zurück
     * @return  aktuelle Funktion
     */

    public String getFunction (){
        return text[1];
    }

    /**
     * Gibt die Info zu der aktuellen Funktion als String zurück
     * TODO Methode non-static machen!
     * @return aktuelle Info
     */
    static String getInfo (){
        return text[2];
    }

    /**
     * liest die aktuell  Parameter, Nullstellen un Achsenabschnitte eines Levels aus der float_list aus und gibt sie zurück
     * @param l aktuelles Level
     * @return Array mit allen relevanten Werten des Levels

    static float[] getParameters(int l, ArrayList <Float> fl){
    // Float-Array in dem alle Parameter, Nullstellen und Achsenabschnitt der jeweiligen Funktion und damit Level gespeichert wird
    // eine Funktion hat max. 7 Werte: 4 Parameter, 2 Nullstellen, 1 Achsenabschnitt; deshalb 7-stelliges Array;
    float [] paramters = new float[7];
    // Startwert, bei dem in der float_liste begonnen werden soll, die WErte auszulesen
    // und Endwert bei dem dann ein neues Level/Funktion beginnt und gestoppt werden soll
    //Bsp: wir sind bei Funktion 2 also Level 2:
    // wir müssen an Stelle 7 in der float_list beginnen: (2-1)*7=7
    // dann folgen 7 Werte die wir auslesen müssen, also bis stelle 13
    // der Endwert ist somit 14: 2+7;
    int start = (l-1)*7;
    int end= l*2;
    int index=0;
    for (int i=start; i<end; i++){
    paramters[index]=fl.get(i);
    index++;
    };
    return paramters;

    } */


    static void insertParameters (int l,ArrayList <Float> fl){
        int paramertsPerFunction =9;
        int level =l;
        int start = (level-1)*paramertsPerFunction;
        int end= (level*paramertsPerFunction);
        int index=0;
        for (int i=start; i<end; i++){
            para[index]=fl.get(i);
            index++;
        };
    }


    /**
     * verhindert, dass über den "Zurück"-Button des Tablets ein Level wiederholt werden könnte
     */
    @Override
    public void onBackPressed(){}

    /**
     * Callback Methode
     * speichert Zwischenstand ab
     */
    @Override
    protected void onPause() {
        super.onPause();
        levelpoint1 = levelinfo.get(1);
        levelpoint2 = levelinfo.get(2);
        levelpoint3 = levelinfo.get(3);
        levelpoint4 = levelinfo.get(4);
        levelpoint5 = levelinfo.get(5);
        //levelpoint5 = 5;
        datasource.insert_table2(level,levelpoint1, levelpoint2, levelpoint3, levelpoint4, levelpoint5);
        //"Gespeichert"-Toast anzeigen zum überprüfen ob es klappt
        Toast.makeText(this, "Deine Daten wurden gespeichert",Toast.LENGTH_SHORT).show();
    }

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

    private void changeIcon(android.view.Menu m){
        MenuItem item =m.findItem(R.id.sound);
        item.setIcon(R.mipmap.sound_off);
    }


    // TODO umbennen in Bewertungs-Textausgeben
    private void check (Pruefung p){
        // je nach Ergebnis wird das Ergebnis ausgegeben
        int points=p.check(level,para);
        // Variablen für die visuelle und textuelle Ergebnisanzeige
        String result2;
        String conclusion;
        int color;

        // Ergebnistext je nach PUnktanzahl verändern
        // macimal erreichte PUnktzahl in einem Level sind 45
        // falsch gezeichnet
        if (points<=40){
            points=0;
            result2="Falsch!";
            conclusion="Leider hast du nicht gut genug gezeichnet. \n " +
                    "Schau doch das nächste Mal in die Infos, vielleicht bekommst du da ein paar Tipps wie du die Funktion richtig zeichnen kannst. "+
                    "Probier dein Glück im nächsten Level.";
            // Rot
            color=Color.rgb(153,2,14);
        }
        else {

            if (points <= 50) {
                result2 = "Gerade nochmal gut gegangen!";
                conclusion = " Puh da hast du ja nochmal Glück gehabt. \n" +
                        "Versuche das nächste Mal genauer zu zeichnen,\n" +
                        " vielleicht helfen dir mehr Hilfspunkte am Anfang?" +
                        " In diesem Level hast du " + String.valueOf(points) + " Punkte geschafft. \n " +
                        "Auf ins nächste Level!";
                // Orange
                color = Color.rgb(255, 127, 39);
            } else {
                if (points <=70) {
                    result2 = "Ganz ok";
                    conclusion = " Das war doch gar nicht mal so schlecht.\n "+
                            "Aber Übung macht den Meister!"+
                            "Du bekommst es das nächste Mal" +
                            " bestimmt noch etwas besser hin!" +
                            " In diesem Level hast du " + String.valueOf(points) + " Punkte geschafft. \n ";

                    // Gelb
                    color = Color.rgb(255, 201, 14);
                } else {
                    if (points <= 90) {
                        result2 = "Gut gemacht!";
                        conclusion = " Das war schon ziemlich gut! \n" +
                                "Glückwunsch, du konntest schon " + String.valueOf(points) + " Punkte sammeln. \n " +
                                "Schaffst du es im nächsten Level noch besser?";
                        // hellgrün
                        color = Color.rgb(181, 230, 29);
                    } else {
                        result2 = "Sehr gut";
                        conclusion = " Bravo, du hast den Funktionsgraph ziemlich gut gezeichnet \n" +
                                "und konntest in diesem Level fabelhaft " + String.valueOf(points) + " Punkte erreichen. \n " +
                                "Beweise dich im neuen Level!";
                        // Grün
                        color = Color.rgb(34, 177, 76);

                    }
                }
            }
            // Sound ertön sobal Funktionsgraph richtig gezeichnet wurde
            if(soundIsOn) mp.start();
        }// Ende if-else
        // ändere die Anzeige des Buttons
        // Löschen und Prüfen Button verschwinden
        b_delete.setVisibility(View.INVISIBLE);
        b_check.setVisibility(View.INVISIBLE);
        // Button, der zum nächsten Level führt wird sichtbar
        b_next.setVisibility(View.VISIBLE);
        //  Korrekturbild soll über die Zeichnung gelegt werden
        z.changeBackground(level);
        z.redrawInColor(color);
        levelinfo.set(level, points);
        //Pop-Up Window mit Texten füllen
        t_result2.setText(result2);
        t_points.setText(String.valueOf(points));
        t_points.setTextColor(color);
        t_conclusion.setText(conclusion);
        scoreInThisLevel.showAtLocation(popupLayout4, Gravity.TOP, 0, 280);

        /*Intent intent = new Intent(Spiel.this, Punkte.class);
        Bundle bundle = new Bundle();
        bundle.putString("result2", result2);
        bundle.putString("conclusion",conclusion);
        bundle.putInt("color",color);
        bundle.putInt("points",points);
        intent.putExtras(bundle);
        startActivity (intent); */
    }

    public int getiMin(int level, ArrayList<Float> fl){
        return 7+((level-1)*9);
    }

    public int getiMax(int level, ArrayList<Float> fl){
        return 8+((level-1)*9);
    }
}