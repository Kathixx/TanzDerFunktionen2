package com.example.arabellaprivat.tanzderfunktionen.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arabellaprivat.tanzderfunktionen.checkAndDraw.Hilfspunkte;
import com.example.arabellaprivat.tanzderfunktionen.checkAndDraw.Pruefung;
import com.example.arabellaprivat.tanzderfunktionen.R;
import com.example.arabellaprivat.tanzderfunktionen.checkAndDraw.Zeichenfläche;
import com.example.arabellaprivat.tanzderfunktionen.database.Datasource;

import java.util.ArrayList;

/**
 * hier fidet das Spiel / das Zeichnen statt
 *
 * Quellen:
 * Bundle   https://www.youtube.com/watch?v=KRN7EYvorZY
 * DoubleClick: http://stackoverflow.com/questions/5608720/android-preventing-double-click-on-a-button
 */
public class Spiel extends AppCompatActivity {

    // IV
    /** gibt es einen Zwischenstand?
     * wird auf true gesetzt, wenn der Zwischenstand gespeichert wird
     */
    static boolean isBuffered = false;
    /** Info-Button */
    private Button b_info;
    /** schließt das Popup Window im Menü */
    private Button b_ok;
    /** zeigt den aktuellen Punktestand an */
    private TextView t_temp_score;
    /** Button zum Löschen der View */
    private Button b_delete;
    /** Button zum prüfen der gemalten Funktion */
    private Button b_check;
    /** führt zum nächsten Level oder zur Endbewertung */
    private Button b_next;
    /** sagt, ob richtig oder falsch gezeichnet wurde */
    private TextView t_result;
    /** zeigt die zu malende Funktion an */
    private TextView t_function;
    /** diese TextView zeigt das aktuelle Level an */
    private TextView t_level;
    /** zeigt, wie viele Level absolviert wurden */
    private ImageView i_score;
    /** zeigt das aktuelle Level an */
    private int level;
    /** speichert die Punkte jedes Levels */
    private ArrayList<Integer> levelinfo;
    /** informiert, wenn das angeklicckte Level nicht ausgewählt werden darf */
    private PopupWindow w_unallowed_choice;
    /** Zeichenfläche, auf der die Funktionen gezeichnet werden */
    private Zeichenfläche z;
    /** hier wird das Popup Window eingefügt */
    private View layout;
    /** Touchfläche für Hilfspunkt */
    private Hilfspunkte h;
    /** Button um nach dem einzeichnen der HIlfspunkte die Funktion zu zeichnen*/
    private Button b_draw;
    /** zeigt an, ob das Level richtig oder Falsch ist*/
    private TextView t_result2;
    /** zeigt, wie viele Punkte man gesammelt hat */
    private TextView t_points;
    /** zeigt, die Erklärung an */
    private TextView t_conclusion;
    private TextView t_number;

    //Instanz vonn Datasource
    Datasource datasource = MainActivity.dataSource;

    //Instanzen für das Speichern der Aktuellen Punktestände
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
    private ArrayList<Integer> integer_list;
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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spiel);
        // Schriftart
        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "fonts/Brandon_reg.otf");
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));

        // Variablen belegen
        b_info = (Button) findViewById(R.id.info);
        b_delete = (Button) findViewById(R.id.delete);
        b_check = (Button) findViewById(R.id.check);
        b_next = (Button) findViewById(R.id.next);
        t_temp_score = (TextView) findViewById(R.id.temp_score);
        t_number = (TextView) findViewById(R.id.number);
        t_result = (TextView) findViewById(R.id.review);
        t_function = (TextView) findViewById(R.id.functionText);
        t_level = (TextView) findViewById(R.id.level);
        i_score = (ImageView) findViewById(R.id.score);
        z = (Zeichenfläche) findViewById(R.id.zeichenfläche);
        h= (Hilfspunkte) findViewById (R.id.hilfspunkte);
        b_draw=(Button) findViewById(R.id.draw);
        t_result2= (TextView)findViewById(R.id.Ergebnis);
        t_points=(TextView)findViewById(R.id.text);
        t_conclusion=(TextView)findViewById(R.id.Erklärung);
        p = new Pruefung(z, h);


        // LayoutInflater für alle PopUpWindows
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // PopupWindow 1: falls ein Level schon gespielt aber nochmal aufgerufen wurde
        layout = inflater.inflate(R.layout.popupwindow, (ViewGroup) findViewById(R.id.popup_element));
        w_unallowed_choice = new PopupWindow(layout, 300, 370, true);
        b_ok = (Button) layout.findViewById(R.id.ok);
        b_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                w_unallowed_choice.dismiss();
            }
        });

        // Popup Window 2: Falls nichts gezeichnet wurde und auf überprüfen geklickt wurde
        popupLayout2=inflater.inflate(R.layout.popupwindow2, (ViewGroup)findViewById(R.id.popup_element_2));
        nothingIsDrawn= new PopupWindow(popupLayout2, 300,370, true);
        b_ok2=(Button) popupLayout2.findViewById(R.id.ok);
        b_ok2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nothingIsDrawn.dismiss();
            }
        });


        // Popup Window 3: Falls der Pfad zu kurz also in einem zu kleinen Intervall gezeichnet wurde
        popupLayout3=inflater.inflate(R.layout.popup_pathtooshort, (ViewGroup)findViewById(R.id.popup_element_3));
        pathTooShort= new PopupWindow(popupLayout3,300,370, true);
        b_ok3= (Button) popupLayout3.findViewById(R.id.ok);
        b_ok3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {pathTooShort.dismiss();z.deleteView();
            }
        });





        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        levelinfo = bundle.getIntegerArrayList("Infos");
        level = levelinfo.get(0);

        // Listen aus der Main Activity holen
        float_list= MainActivity.returnFloatList();
        string_list= MainActivity.returnStringList();

        text= new String [3];

        // TODO parameter auf 9 erhöhen
        parameters= new double [7];
        para= new double [7];

        // MediaPlayer für Musik
        mp= MediaPlayer.create(this, R.raw.pad_confirm);
        // Variable ob Sound on oder of ist
        soundIsOn=bundle.getBoolean("Sound");

        // Text reinschreiben
        t_result.setText("Berechne Schnittpunkte mit den Achsen und ggf. Extremstellen auf einem Blatt Papier, denn darauf wird bei der Überprüfung besonders Wert gelegt. Zeichne dann Deine Hilfspunkte ein."+String.valueOf(soundIsOn));


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
        t_number.setText(String.valueOf(score));
        Typeface fontNumber = Typeface.createFromAsset(getAssets(),  "fonts/BAUHS93.TTF");
        t_number.setTypeface(fontNumber);
        // Je nach Punktestand Farbe setzen
        int c;
        if(score <= 9){
            c = Color.rgb(153,2,14);
        } else if(score <= 19){
            c = Color.rgb(255, 127, 39);
        } else if(score <= 29){
            c = Color.rgb(255, 201, 14);
        } else if(score <= 39) {
            c = Color.rgb(181, 230, 29);
        } else {
            c = Color.rgb(34, 177, 76);
        }
        t_number.setTextColor(c);
        t_function.setTextColor(c);


        // einzelne Strings des aktuellen Levels in dem TextARRAY abspeichern
        splitArray(string_list.get(level-1));
        // liest die jeweilige Funktion aus
        t_function.setText(getFunction());
        // Array mit aktuellen Parametern des Levels füllen
        // parameters= getParameters(level, float_list);
        insertParameters(level, float_list);


        // Buttons mit Funktion belegen
        b_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(v);
            }
        });

        b_delete.setOnClickListener(new View.OnClickListener() {
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
            @Override
            public void onClick(View v) {
                //Zeichenfläche zum Zeichnen der Funktion sichtbar machen ("einschalten")
                z.setVisibility(View.VISIBLE);
                // Button für Beenden der Hilfspunkte-setzen ausschalten
                b_draw.setVisibility(View.INVISIBLE);

                // Button zum Überprüfen der Funktion einschalten
                b_check.setVisibility(View.VISIBLE);
                // Text ausblenden
                t_result.setVisibility(View.INVISIBLE);

                //Toast als Hinweis, dass nun der Funktionsgraph eingezeichnet werden soll
                Context context = getApplicationContext();
                CharSequence text = "Zeichne jetzt den Funktionsgraph ein.";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });


        b_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // RICHITG oder FALSCH soll angezeigt werden
                t_result.setVisibility(View.VISIBLE);
                // TODO Level auslesen? WO??
                // die Funktion zum Prüfen der Funktion wird aufgerufen
                // zunächst wird überprüft ob überhaupt ein Graph gezeichnet wurde und ein entsprechender dialog angezeigt
                if (p.pathIsEmpty()){
                    nothingIsDrawn.showAtLocation(popupLayout2, Gravity.CENTER, 0, 0);
                }
                else {

                    if (points <= 20) {
                        result2 = "Gerade nochmal gut gegangen!";
                        conclusion = " Puh da hast du ja nochmal Glück gehabt. \n" +
                                "Versuche das nächste Mal genauer zu zeichnen,\n" +
                                " vielleicht helfen dir mehr Hilfspunkte am Anfang?" +
                                " In diesem Level hast du " + String.valueOf(points) + " Punkte geschafft. \n " +
                                "Auf ins nächste Level, dann kannst du noch mehr Punkte sammeln";
                        // Orange
                        color = Color.rgb(255, 127, 39);
                    } else {
                        if (points <= 30) {
                            result2 = "Ganz ok";
                            conclusion = " Das war doch gar nicht mal so schlecht \n" +
                                    "Aber Übung macht den Meister! \n" +
                                    "Du bekommst es das nächste Mal bestimmt noch etwas besser hin!" +
                                    " In diesem Level hast du " + String.valueOf(points) + " Punkte geschafft. \n " +
                                    "Ab ins nächste Level";
                            // Gelb
                            color = Color.rgb(255, 201, 14);
                        } else {
                            if (points <= 30) {
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
                                if(soundIsOn) mp.start();
                            }
                        }
                    }
                }// Ende if-else
                z.redrawInColor(color);
                levelinfo.set(level, points);




                // Button, der zum nächsten Level führt wird sichtbar
                b_next.setVisibility(View.VISIBLE);

                //  Korrekturbild soll über die Zeichnung gelegt werden
                z.changeBackground(level);

                // Pop-Up Window
                Intent intent = new Intent(Spiel.this, Punkte.class);
                Bundle bundle = new Bundle();
                bundle.putString("result2", result2);
                bundle.putString("conclusion",conclusion);
                bundle.putInt("color",color);
                bundle.putString("points",String.valueOf(points));
                intent.putExtras(bundle);
                startActivity (intent);

                    /*TODO
                   if (p.pathIsInIntervall(para)) {
                        pathTooShort.showAtLocation(popupLayout3, Gravity.CENTER, 0, 0);
                   }
                    else { */
                    check(p);
                    // }
                }

            }
        });

        b_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(v);
            }
        });

    }// Ende onCreate


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sound_level, menu);
        if (!soundIsOn) changeIcon(menu);
        return true;

    }

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
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.anleitung:
                Intent in = new Intent(this, Anleitung.class);
                startActivity(in);
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
            bundle.putInt("Level", chosenLevel);
            bundle.putIntegerArrayList("Infos", levelinfo);
            bundle.putBoolean("Sound", soundIsOn);
            // neues Intent
            Intent i_new = new Intent(this, Spiel.class);
            i_new.putExtras(bundle);
            // Activity starten
            startActivity(i_new);
        } else {
            // Pop-Up Window
            Intent intent = new Intent(Spiel.this, Punkte.class);
            Bundle bundle = new Bundle();
            bundle.putString("result2", "Halt!");
            bundle.putString("conclusion","Das Level hast Du schon gemacht. Bitte such Dir ein anderes Level aus.");
            bundle.putInt("color", Color.RED);
            bundle.putString("points", "X");
            intent.putExtras(bundle);
            startActivity (intent);
        }
    }

    /**
     * koordiniert das Zeichnen der Punkteanzeige
     * hier werden Zeichen-Eigenschaften gesetzt
     */
    private void visualizeScore(){
        Bitmap bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
        // der Abstand vom linken Bildschirmrand wird um jew. 15 px erhöht
        int abstand_x = 0;
        // Style und Farbe hängen von der Bewertung der Level ab
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        // mit einer Schleife gehen wir durch die Liste zu den verschiedenen Levels
        for (int i=1; i<=5; i++) {
            // 5. Stufe 0-9: rot
            if(levelinfo.get(i) >= 0 && levelinfo.get(i) <= 9){
                // sei der Kreis rot ausgemalt
                paint.setColor(Color.rgb(153,2,14));
            }
            // 4. Stufe 10-19: orange
            else if(levelinfo.get(i) >= 10 && levelinfo.get(i) <= 19){
                paint.setColor(Color.rgb(255, 127, 39));
            }
            // 3. Stufe 20-29: gelb
            else if(levelinfo.get(i) >= 20 && levelinfo.get(i) <= 29){
                paint.setColor(Color.rgb(255, 201, 14));
            }
            // 2. Stufe 30-39: hellgrün
            else if(levelinfo.get(i) >= 30 && levelinfo.get(i) <= 39){
                paint.setColor(Color.rgb(181, 230, 29));
            }
            // 1. Stufe 10-19: grün
            else if(levelinfo.get(i) >= 40 && levelinfo.get(i) <= 45){
                paint.setColor(Color.rgb(34, 177, 76));
            }
            // wenn das Level noch nicht gemacht wurde
            else if(levelinfo.get(i)== 200){
                // grundsätzlich sind alle Kreise leer mit schwarzer Umrandung
                paint.setColor(Color.BLACK);
                paint.setStyle(Paint.Style.STROKE);
            }

            // Abstand zum linken Nachbarkreis vergrößern
            abstand_x += 15;

            // mit diesen Einstellungen den Kreis malen
            this.einenKreisZeichnen(bitmap, paint, abstand_x);
        }
    }

    /**
     * zeichnet einen Punkt
     * @param bitmap        Hier wird gezeichnet
     * @param paint         Zeichen-Eigenschaften
     * @param abstand_x     Abstand zum linken Bildschirmrand, bzw. zum linken Nachbar-Kreis
     */
    private void einenKreisZeichnen(Bitmap bitmap, Paint paint, int abstand_x){
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
        Bundle bundle = new Bundle();
        if(view.getId() == R.id.info) {
            // die Klasse Info braucht nur das aktuelle Level
            bundle.putInt("Level", level);
            Intent intent = new Intent(this, Info.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if(view.getId() == R.id.next) {
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
        // TODO von 7 auf 9 erhöhen
        int paramertsPerFunction = 7; //9;
        int level =l;
        int start = (level-1)*paramertsPerFunction;
        int end= (level*paramertsPerFunction);
        int index=0;
        for (int i=start; i<end; i++){
            para[index]=fl.get(i);
            index++;
        };
    }

    // disable Back-Button
    @Override
    public void onBackPressed(){}

    /**
     * Callback Methode
     * speichert Zwischenstand ab
     */
    @Override
    protected void onPause() {
        super.onPause();
        isBuffered = true;
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
                        "Auf ins nächste Level, dann kannst du noch mehr Punkte sammeln";
                // Orange
                color = Color.rgb(255, 127, 39);
            } else {
                if (points <=70) {
                    result2 = "Ganz ok";
                    conclusion = " Das war doch gar nicht mal so schlecht \n" +
                            "Aber Übung macht den Meister! \n" +
                            "Du bekommst es das nächste Mal bestimmt noch etwas besser hin!" +
                            " In diesem Level hast du " + String.valueOf(points) + " Punkte geschafft. \n " +
                            "Ab ins nächste Level";
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
                        if(soundIsOn) mp.start();
                    }
                }
            }
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
        // Pop-Up Window
        Intent intent = new Intent(Spiel.this, Punkte.class);
        Bundle bundle = new Bundle();
        bundle.putString("result2", result2);
        bundle.putString("conclusion",conclusion);
        bundle.putInt("color",color);
        bundle.putInt("points",points);
        intent.putExtras(bundle);
        startActivity (intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}

