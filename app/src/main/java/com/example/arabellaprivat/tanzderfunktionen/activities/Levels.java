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

import com.example.arabellaprivat.tanzderfunktionen.checkAndDraw.TouchViewDots;
import com.example.arabellaprivat.tanzderfunktionen.checkAndDraw.Check;
import com.example.arabellaprivat.tanzderfunktionen.R;
import com.example.arabellaprivat.tanzderfunktionen.checkAndDraw.TouchViewGraph;
import com.example.arabellaprivat.tanzderfunktionen.database.Datasource;


/**
 * Created by Arabella und Kathi
 * hier findet das Spiel an sich, sowie das Zeichnen und Überprüfen statt
 * Quellen:
 * DoubleClick: http://stackoverflow.com/questions/5608720/android-preventing-double-click-on-a-button
 * Menü: https://developer.android.com/guide/topics/ui/menus.html
 */
public class Levels extends AppCompatActivity {

    // IV
    /** zeigt das aktuelle Level an */
    private int level;
    /** speichert Infos über das aktulle Spiel
     * Index 0:     aktuelles Level
     * Index 1-5:   Punkte des jeweiligen Levels */
    private ArrayList<Integer> levelinfo;

    // Buttons
    /** Info-Button */
    private Button b_info;
    /** Button zum Löschen der View */
    private Button b_delete;
    /** Button zum prüfen der gemalten Funktion */
    private Button b_check;
    /** führt zum nächsten Level oder zur Endbewertung */
    private Button b_next;
    /** Button um nach dem einzeichnen der Hilfspunkte die Funktion zu zeichnen*/
    private Button b_draw;

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

    /** TouchView, auf dem die Funktionen gezeichnet werden */
    private TouchViewGraph tvg;
    /** TouchVie für Hilfspunkte */
    private TouchViewDots tvd;

    /** Prüfung  */
    private Check check;

    /**Instanz vonn Datasource */
    Datasource datasource = MainActivity.dataSource;
    /* Instanzen für das Speichern der Aktuellen Punktestände */
    private int levelpoint1; //= levelinfo.get(1);
    private int levelpoint2; //= levelinfo.get(2);
    private int levelpoint3; //= levelinfo.get(3);
    private int levelpoint4; //= levelinfo.get(4);
    private int levelpoint5; //= 3; //levelinfo.get(5)


    // Listen zum Auslesen aus der Datenbank
    /** float_list enthält alle Parameter, Nullstellen und Achsenabschnitte der Funktionenen */
    private ArrayList <Float> float_list;
    /** string_list enthält alle Text, also Funktion als Ganzes und Tipps */
    private ArrayList <String> string_list;
    /** temporäres Array, in dem die Texte des jeweiligen Levels gespeichert werden */
    private String [] text;
    /** temporäres Array, in dem alle Werte des jeweiligen Levels gespeichert werden */
    private double [] parameters;

    /** Zeitstempel für das Abfangen von DoppelKlick */
    private long lastClick=0;

    /** Media Player für Musik */
    private MediaPlayer mediaPlayer;

    /** Variable für den Sound */
    private boolean soundIsOn;

    /** Popup Window informiert falls nichts gezeichnet wurde   */
    private PopupWindow pw_nothingIsDrawn;
    /** Layout dieses Popup Windows*/
    private View popupLayout2;
    /** Ok Button deses PopupWindows */
    private Button b_ok2;

    /** Popup Window informiert, falls Graph zu kuzr gezeichnet wurde */
    private PopupWindow pw_pathTooShort;
    /** Layout dieses Popup Windows*/
    private View popupLayout3;
    /** Ok Button deses PopupWindows */
    private Button b_ok3;

    /** Popup Window informiert, wenn das angeklickte Level nicht ausgewählt werden darf */
    private PopupWindow pw_forbiddenChoice;
    /** Layout dieses Popup Windows*/
    private View layout;
    /** Ok Button deses PopupWindows */
    private Button b_ok;

    /** Popup Window, informiert über den erreichten Punktestand nach der Überprüfung */
    private PopupWindow pw_scoreInThisLevel;
    /** Layout dieses Popup Windows*/
    private View popupLayout4;
    /** Text View zur Ergebnisanzeige in diesem Popup Window*/
    private TextView t_result;
    /** Text View zur Punkteanzeige in diesem Popup Window*/
    private TextView t_points;
    /** Text View zur Erklärung des Ergebnisses in diesem Popup Window*/
    private TextView t_conclusion;

    /** Popup Window zeigt Tipps zum zeichnen */
    private PopupWindow pw_tipps;
    /** Layout dieses Popup Windows*/
    private View popuplayout5;
    /** Schließen button des PopUp Windows*/
    private Button b_close;
    /** Text View für die Tipps in diesem Popup Window*/
    private TextView t_tipps;



    /** Methode onCreate()
     * erstellt die Activity bei dessen Aufruf
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        // Schriftart für die ganze Activity ändern mithilfe des FontChanger
        FontChanger fontChanger = new FontChanger(getAssets(), "fonts/Brandon_reg.otf");
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));
        // Schriftart für Popups extra überschreiben (wird nicht vom FontChanger gemacht)
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
        tvg           = (TouchViewGraph) findViewById(R.id.drawing);
        tvd           = (TouchViewDots) findViewById (R.id.dots);
        b_draw      = (Button) findViewById(R.id.draw);
        // Instanz der Prüfung (check) erstellen, Zeichenfläche und Hilfspunkte-Zeichenfläche werden mitübergeben
        check          = new Check(tvg, tvd);


        // LayoutInflater für alle PopUpWindows
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // PopupWindow 1: falls ein Level schon gespielt aber nochmal aufgerufen wurde
        layout = inflater.inflate(R.layout.popup_forbidden_choice, (ViewGroup) findViewById(R.id.popup_forbiddenChoice));
        pw_forbiddenChoice = new PopupWindow(layout, 300, 200, true);
        b_ok = (Button) layout.findViewById(R.id.ok);
        b_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw_forbiddenChoice.dismiss();
            }
        });
        TextView t_popupText= (TextView)layout.findViewById(R.id.content);
        // Schriftarten werden nicht vom FontChanger verändert und müssen hier extra überschrieben werden
        b_ok.setTypeface(fontRegular);
        t_popupText.setTypeface(fontRegular);


        // Popup Window 2: Falls nichts gezeichnet wurde und auf überprüfen geklickt wurde
        popupLayout2=inflater.inflate(R.layout.popup_nothing_is_drawn, (ViewGroup)findViewById(R.id.popup_nothingIsDrawn));
        pw_nothingIsDrawn= new PopupWindow(popupLayout2, 300,200, true);
        b_ok2=(Button) popupLayout2.findViewById(R.id.ok);
        b_ok2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw_nothingIsDrawn.dismiss();
            }
        });
        t_popupText= (TextView)popupLayout2.findViewById(R.id.content);
        // Schriftarten werden nicht vom FontChanger verändert und müssen hier extra überschrieben werden
        b_ok2.setTypeface(fontRegular);
        t_popupText.setTypeface(fontRegular);

        // Popup Window 3: Falls der Pfad zu kurz also in einem zu kleinen Intervall gezeichnet wurde
        popupLayout3=inflater.inflate(R.layout.popup_path_too_short, (ViewGroup)findViewById(R.id.popup_pathTooShort));
        pw_pathTooShort= new PopupWindow(popupLayout3,300,200, true);
        b_ok3= (Button) popupLayout3.findViewById(R.id.ok);
        b_ok3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {pw_pathTooShort.dismiss();tvg.deleteView();
            }
        });
        t_popupText= (TextView)popupLayout3.findViewById(R.id.content);
        // Schriftarten werden nicht vom FontChanger verändert und müssen hier extra überschrieben werden
        b_ok3.setTypeface(fontRegular);
        t_popupText.setTypeface(fontRegular);

        // Popup Window 4: Zeigt das Ergebnis und die Punkte des gezeichneten Graphen an
        popupLayout4=inflater.inflate(R.layout.popup_score_in_this_level, (ViewGroup)findViewById(R.id.popup_scoreInThisLevel));
        pw_scoreInThisLevel= new PopupWindow(popupLayout4,400,350, true);
        t_result=(TextView) popupLayout4.findViewById(R.id.Ergebnis);
        t_points=(TextView) popupLayout4.findViewById(R.id.Punkte);
        t_conclusion=(TextView) popupLayout4.findViewById(R.id.Erklärung);
        // Eigenschaften dieses Popup Windows festlegen: Bei Klick auserhalb des Popup soll sich dieses schließen
        pw_scoreInThisLevel.setBackgroundDrawable(new BitmapDrawable());
        // Schriftarten werden nicht vom FontChanger verändert und müssen hier extra überschrieben werden
        t_result.setTypeface(fontRegular);
        t_conclusion.setTypeface(fontRegular);
        t_points.setTypeface(fontBold);

        // Popup Window 5: Tipps die beim Zeichnen der Funktion helfen
        popuplayout5 = inflater.inflate(R.layout.popup_info, (ViewGroup) findViewById(R.id.popup_info));
        pw_tipps = new PopupWindow(popuplayout5, 400, 400, true);
        b_close = (Button) popuplayout5.findViewById(R.id.close);
        b_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw_tipps.dismiss();
            }
        });
        t_tipps = (TextView) popuplayout5.findViewById(R.id.help);
        // Eigenschaften des Popup Windows festlegen: Bei Klick auserhalb des Popup soll sich dieses schließen
        pw_tipps.setBackgroundDrawable(new BitmapDrawable());
        // Schriftarten werden nicht vom FontChanger verändert und müssen hier extra überschrieben werden
        b_close.setTypeface(fontRegular);
        t_tipps.setTypeface(fontRegular);

        // MediaPlayer für Musik erstellen
        mediaPlayer= MediaPlayer.create(this, R.raw.confirm);

        // Informationen aus der MainActivity holen und verarbeiten
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        // Liste mit den Infos über Level und Punkte
        levelinfo = bundle.getIntegerArrayList("Infos");
        // Das aktuelle Level steht am Index 0
        level = levelinfo.get(0);
        // Variable ob Sound on oder off ist aus zuvorigen Activity
        soundIsOn=bundle.getBoolean("Sound");

        // Listen aus der datasource holen (static, da datasource nicht mittels Bunde übergeben werden kann und keine neue Instanz von Datasource erstellt werden soll
        float_list= datasource.Float_Entries();
        string_list= datasource.String_Entries();

        // Text Array mit 3 möglichen Einträgen erstellen: 1. für das Level, 2. für die Funktion (als String), 3. Tipp
        text= new String [3];
        // einzelne Strings des aktuellen Levels in dem Text Array abspeichern
        splitArray(string_list.get(level-1));

        // Parameter Array mit 9 möglichen Einträgen erstellen:
        // 1.-4. Parameter der Funktion, 5.-6. Nullstellen, 7. Achsenabschnitt, 8.-9. Intervall
        parameters= new double [9];
        // Parameter Array mit aktuellen Parametern des Levels füllen
        insertParameters();


        /* ************ Layout dynamisch anpassen */
        // "Weiter"-Button, TouchView zum Zeichnen sowie "Check" Button sind unsichtber
        b_next.setVisibility(View.INVISIBLE);
        tvg.setVisibility(View.INVISIBLE);
        b_check.setVisibility(View.INVISIBLE);

        // Anzeige setzen, in welchem Level wir sind
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
        // Farbe je nach Punktestand setzen
        if (level!=1) t_number.setTextColor(calculateColor(score));
        // Schriftart für den Punktestand verändern
        t_number.setTypeface(fontBold);


        // liest die jeweilige Funktion aus
        t_function.setText(getFunction());

        // Intervall auslesen und ausgeben
        t_intervall.setText("Bitte zeichne den Funktionsgraphen im Intervall von "+ getiMin()+" und "+getiMax());


        /*********** Click Listener der Buttons ***/
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
                /* Je nach dem ob gerade Hilfspunkte eingezeichnet werden oder schon die eigentliche Funktion gezeichnet wird
                 * werden unterschiedliche Aktionen von diesem Button hervorgerufen
                 * die Zeichenfläche die Sichtbar ist wird geleert */
                if (tvg.getVisibility() == View.INVISIBLE) {
                    /* bei DoubleKlick werden alle Hilfspunkte gelöscht
                     * elapsedRealtime() gibt die Sekunden (nano!) zurück, seid dem letzten boot
                     * 500 gibt die Sekunden an inherhalb denen man doppelt geklickt haben muss */
                    if (SystemClock.elapsedRealtime() - lastClick < 500) {
                        tvd.deleteView();
                    }
                    // bei einfachen Klick nur den letzten Hilfspunkt löschen
                    else tvd.deleteLast();
                }
                /* wenn man bereits beim Zeichnen ist,
                 * sollen die Hilfspunkte nicht mehr verändert werden
                 * nur der Graph soll gelöscht werden
                 * hier nur einfacher Klick möglich */
                else tvg.deleteView();
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
                tvg.setVisibility(View.VISIBLE);
                // Button für Beenden der Hilfspunkte-setzen ausblenden
                b_draw.setVisibility(View.INVISIBLE);
                // Button zum Überprüfen der Funktion anzeigen
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
                if (check.pathIsEmpty()){
                    pw_nothingIsDrawn.showAtLocation(popupLayout2, Gravity.TOP, 0, 358);
                }
                else {
                   if (!check.pathIsInIntervall(parameters)) {
                       pw_pathTooShort.showAtLocation(popupLayout3, Gravity.TOP, 0,358);
                   }
                   else {
                       // ruft die eigentliche checkFunction() Methode auf und gibt das Ergebnis als PopUp aus
                       showResult(check);
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
    }// Ende onCreateOptionsMenu

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
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.anleitung:
                startActivity(new Intent(this, Instruction.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }// Ende onOptionsItemSelected

    /**
     * Teilt die Texte des ersten Levels, die nur mit einem Komma getrennt sind und gibt sie als einzelnes Array zurück
     * an stelle 0: Level
     * an Stelle 1: Funktion
     * an Stelle 2: Tipp
     * @param s String der kompletten Texte, also Level, Funktion und Tipp der Funktion
     */
    private void splitArray (String s){
        text = s.split(";");
    }//Ende splitArray


    /** speichert in das Parameter-Array alle Parameter die für die aktuelle Funktion notwendig werden
     * da in der float_list alle Parameter aller Funktionen gespeichert sind
     */
    private void insertParameters (){
        int paramertsPerFunction =9;
        int start = (level-1)*paramertsPerFunction;
        int end= (level*paramertsPerFunction);
        int index=0;
        for (int i=start; i<end; i++){
            parameters[index]=float_list.get(i);
            index++;
        }
    } // Ende insertParameters


    /* berechnet die Farbe des aktuellen Punktestandes runtergerechnet auf ein Level
     */
    private int calculateColor (int score){
        int color;
        int relativScore=score/(level-1);
        if(relativScore <=40){
            color = Color.rgb(153,2,14);
        } else if(relativScore <= 50){
            color = Color.rgb(255, 127, 39);
        } else if(relativScore <= 70){
            color = Color.rgb(255, 201, 14);
        } else if(relativScore <= 90) {
            color = Color.rgb(181, 230, 29);
        } else {
            color = Color.rgb(34, 177, 76);
        }
        return color;
    }// Ende calculateColor


    /**
     * koordiniert das Zeichnen der Punkteanzeige
     */
    private void visualizeScore() {
        Bitmap bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
        // der Abstand vom linken Bildschirmrand wird jeweils erhöht
        int abstand_x = 0;
        for (int i = 1; i <= 5; i++) {
            // Farb-Eigenschaften festlegen
            Paint paint = setPaint(i);
            // Abstand zum linken Nachbarn vergrößern
            abstand_x += 15;
            // damit den Kreis zeichnen
            this.paintCircle(bitmap, paint, abstand_x);
        }
    }// Ende visualizeScore


    /**
     * setzt die Farbe des zu zeichnenden Kreises fest
     * @param circleNumber  Kreis, der gezeichnet werden soll
     * @return              Farbe und Style des Kreises
     */
    private Paint setPaint(int circleNumber){
        // Style und Farbe hängen von der Rating der Level ab
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
    }//Ende setPaint



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
    }// paintCircle


    /**
     * Gibt die aktuelle Funktion als String zurück
     * @return  aktuelle Funktion
     */
    private String getFunction (){
        return text[1];
    }// Ende getFunction


    /** gibt die untere Intervallgrenze zurück */
    private float getiMin(){
        return float_list.get(7+((level-1)*9));
    }//Ende getiMin

    /** gibt die obere Intervallgrenze zurück */
    private float getiMax(){
        return float_list.get(8+((level-1)*9));
    }// Ende getiMax


    /**
     * wird beim Click auf einen Button aufgerufen
     * und startet abhängig vom Button die passende Activity
     * @param view  Button, der geklickt wurde
     */
    private void sendMessage(View view){
        // Infos im Bundle speichern
        Bundle bundle = new Bundle();

        if(view.getId() == R.id.info) {
            // zuerst x dann y
            pw_tipps.showAtLocation(popuplayout5, Gravity.CENTER,85, 245);
            t_tipps.setText(getInfo());
        }

        else if(view.getId() == R.id.next) {
            // wenn alle 5 Level gespielt wurden
            // Level die noch nicht gemacht wurden haben von Beginn an den Wert 200
            if(levelinfo.get(1) != 200 && levelinfo.get(2) != 200 && levelinfo.get(3) != 200 && levelinfo.get(4) != 200 && levelinfo.get(5) != 200){
                // gehe zur Endbewertung
                bundle.putIntegerArrayList("Infos", levelinfo);
                bundle.putBoolean("Sound", soundIsOn);
                Intent intent = new Intent(this, Rating.class);
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
                Intent intent = new Intent(this, Levels.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }// Ende sendMessage


    /**
     * Gibt die Info zu der aktuellen Funktion als String zurück
     * @return aktuelle Info
     */
    private String getInfo (){
        return text[2];
    }// Ende getInfo


    /** eigentliche Prüffunktion
     *
     * @param check Instanz der Prüfung
     */
    private void showResult (Check check){
        // je nach Ergebnis wird das Ergebnis ausgegeben
        int points=check.checkFunction(level, parameters);
        // Variablen für die visuelle und textuelle Ergebnisanzeige
        String result;
        String conclusion;
        int color;
        // Ergebnistext je nach Pnktanzahl verändern
        // maximal erreichte Punktzahl in einem Level sind 100
        // bei falsch gezeichneten Graphen
        if (points<=40){
            points=0;
            result="Falsch!";
            conclusion="Leider hast Du nicht gut genug gezeichnet. \n " +
                    "Schau doch das nächste Mal in die Infos, vielleicht bekommst Du da ein paar Tipps wie Du die Funktion richtig zeichnen kannst. "+
                    "Probier Dein Glück im nächsten Level.";
            // Rot
            color=Color.rgb(153,2,14);
        }
        else {
            // bei schlecht gezeichneten Graphen
            if (points <= 50) {
                result = "Gerade nochmal gut gegangen!";
                conclusion = " Puh da hast Du ja nochmal Glück gehabt. \n" +
                        "Versuche das nächste Mal genauer zu zeichnen,\n" +
                        " vielleicht helfen Dir mehr Hilfspunkte am Anfang?" +
                        " In diesem Level hast Du " + String.valueOf(points) + " Punkte geschafft. \n " +
                        "Auf ins nächste Level!";
                // Orange
                color = Color.rgb(255, 127, 39);
            } else {
                // bei mittemmäßig gezeichneten Graphen
                if (points <=70) {
                    result = "Ganz ok";
                    conclusion = " Das war doch gar nicht mal so schlecht.\n "+
                            "Aber Übung macht den Meister! "+
                            "Das bekommst Du das nächste Mal" +
                            " bestimmt noch besser hin!\n" +
                            "In diesem Level hast Du " + String.valueOf(points) + " Punkte geschafft. \n ";

                    // Gelb
                    color = Color.rgb(255, 201, 14);
                } else {
                    // bei gut gezeichneten Graphen
                    if (points <= 90) {
                        result = "Gut gemacht!";
                        conclusion = " Das war schon ziemlich gut! \n" +
                                "Glückwunsch, Du konntest weitere \n" + String.valueOf(points) + " Punkte sammeln. \n " +
                                "Schaffst Du es im nächsten Level noch besser?";
                        // hellgrün
                        color = Color.rgb(181, 230, 29);
                    } else {
                        // bei sehr gut gezeichneten Graphen
                        result = "Sehr gut";
                        conclusion = " Bravo, Du hast den Funktionsgraph hervorragend gezeichnet " +
                                "und konntest in diesem Level fabelhaft " + String.valueOf(points) + " Punkte erreichen. \n " +
                                "Beweise Dich im neuen Level!";
                        // Grün
                        color = Color.rgb(34, 177, 76);
                    }
                }
            }
            // Sound ertön sobald Funktionsgraph richtig gezeichnet wurde
            if(soundIsOn) mediaPlayer.start();
        }// Ende if-else
        // Ändern der Buttons: Löschen und Prüfen Button verschwinden, Button, der zum nächsten Level führt wird sichtbar
        b_delete.setVisibility(View.INVISIBLE);
        b_check.setVisibility(View.INVISIBLE);
        b_next.setVisibility(View.VISIBLE);
        //  Korrekturbild soll über die Zeichnung gelegt werden
        tvg.changeBackground(level);
        // gezeichneter Funktionsgraph wird in entsprechender Farbe ersetzt
        tvg.redrawInColor(color);
        // Punkte in die dafür vorgesehen Liste eintragen
        levelinfo.set(level, points);
        //Pop-Up Window für die Ausgabe der Bewertung mit Texten füllen
        t_result.setText(result);
        t_points.setText(String.valueOf(points));
        t_points.setTextColor(color);
        t_conclusion.setText(conclusion);
        // Pop-Up Window anzeigen lassen
        pw_scoreInThisLevel.showAtLocation(popupLayout4, Gravity.TOP, 0, 280);
    } // Ende showResult



    /** Icon ändern
     * @param m ActionBar Icon das verändert werden soll
     */
    private void changeIcon(android.view.Menu m){
        MenuItem item =m.findItem(R.id.sound);
        item.setIcon(R.mipmap.sound_off);
    } // Ende changeIcon


    /** verändert den Sound
     * @param item Item in der Actionbar der dieses Funktion auslöst
     */
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
    }// Ende changeSound



    /**
     * navigiert zum ausgewähltem Level
     * @param chosenLevel   Level, auf das geklickt wurde
     */
    private void chooseLevel(int chosenLevel){
        // wenn das Level ausgewählt werden darf
        if(levelinfo.get(chosenLevel) == 200){
            // speicher das Level in einem Bundle
            Bundle bundle = new Bundle();
            // Level aktualisieren
            levelinfo.set(0, chosenLevel);
            bundle.putIntegerArrayList("Infos", levelinfo);
            bundle.putBoolean("Sound", soundIsOn);
            // neues Intent
            Intent i_new = new Intent(this, Levels.class);
            i_new.putExtras(bundle);
            // Activity starten
            startActivity(i_new);
        }
        else {
            pw_forbiddenChoice.showAtLocation(layout, Gravity.TOP, 0, 358);
        }
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
        levelpoint2 = levelinfo.get(2);
        levelpoint3 = levelinfo.get(3);
        levelpoint4 = levelinfo.get(4);
        levelpoint5 = levelinfo.get(5);
        //levelpoint5 = 5;
        datasource.insert_table2(level,levelpoint1, levelpoint2, levelpoint3, levelpoint4, levelpoint5);
        //"Gespeichert"-Toast anzeigen zum überprüfen ob es klappt
        Toast.makeText(this, "Deine Daten wurden gespeichert",Toast.LENGTH_SHORT).show();
    }// Ende onPause


}