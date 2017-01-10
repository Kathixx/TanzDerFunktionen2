package com.example.arabellaprivat.tanzderfunktionen.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

import com.example.arabellaprivat.tanzderfunktionen.R;


/**
 * zur Bewertung des Spiels
 * Quelle des Algorithmus zum Zeichnen eines Sterns:   http://stackoverflow.com/questions/7007429/android-how-to-draw-triangle-star-square-heart-on-the-canvas
 */
public class Bewertung extends AppCompatActivity {

    /** Bewertungssatz */
    private TextView t_review;
    /** visualisiert, wie viele Punkte erreicht wurden */
    private ImageView i_points;
    /** gibt an, wie viele Punkte erreicht wurden */
    private TextView t_score;
    /** Button startet das Spiel von vorne */
    private Button b_restart;
    /** enthält alle wichtigen Infos über Level und Punkte */
    private ArrayList<Integer> levelinfo;
    /** Anzahl der Punkte insgesamt */
    private int score = 0;
    /** ist der Sound eingeschaltet? */
    private boolean soundIsOn;

    /**
     * erstellt die Activity bei dessen Aufruf
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bewertung);


        //// Schriftart für die ganze Activity ändern mithilfe des FontChangeCrawlers
        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "fonts/Brandon_reg.otf");
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));
        // Schriftart für Popups extra holen
        Typeface fontBold = Typeface.createFromAsset(getAssets(),  "fonts/BAUHS93.TTF");


        // Möglichkeit weiterzuspielen "ausschalten"
        MainActivity.firstTime = false;

        // Intent, das diese Activity geöffnet hat
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        // daraus die übergebenen Daten holen
        this.levelinfo = bundle.getIntegerArrayList("Infos");
        soundIsOn=bundle.getBoolean("Sound");

        // Score berechnen
        for(int i=1; i<=5; i++){
            score += levelinfo.get(i);
        }

        // Variablen belegen
        t_review = (TextView) findViewById(R.id.review);
        i_points = (ImageView) findViewById(R.id.points);
        t_score = (TextView) findViewById(R.id.score);
        b_restart = (Button) findViewById(R.id.restart);

        // Schriftart für die Punte verändern
        t_score.setTypeface(fontBold);


        // Bewertungstext
        // je nach Punktezahl gibt es einen anderen Text
        // bei 0 Punkten
        if(score <= 100) {
            t_review.setText("Leider hast du keine Funktion richtig gezeichnet. Vielleicht solltest Du Dich nochmal in das Thema einarbeiten.");
            t_score.setTextColor(Color.rgb(153, 2, 14));
        }
        else {
            if(score <= 200){
                t_review.setText("Das war noch nicht ganz überzeugend. Übe weiter, um Dich zu verbessern.");
                t_score.setTextColor(Color.rgb(255, 127, 39));
            }
            else {
                if(score <= 300){
                    t_review.setText("Das war schon ein guter Anfang. Übe weiter, um Dich zu verbessern.");
                    t_score.setTextColor(Color.rgb(255, 201, 14));}
                else {
                    if(score <= 400){
                        t_review.setText("Gut gemacht. Übe weiter, um Dein Wissen zu festigen.");
                        t_score.setTextColor(Color.rgb(181, 230, 29));}
                    else {
                        t_review.setText("Super! Du hast alles richtig gezeichnet. Du bist bereit für die nächste Prüfung.");
                        t_score.setTextColor(Color.rgb(34, 177, 76));
                    }
                }
            }
        }

        // Visualisierung der Punkte
        visualizeScore();

        // Wie viele Punkte wurden erreicht?
        t_score.setText(String.valueOf(score));

        b_restart.setOnClickListener(new View.OnClickListener() {
            /**
             * ermöglicht eine Aktoin beim Klick auf den Button
             * @param v View, auf die geklickt wurde
             */
            @Override
            public void onClick(View v) {
                MainActivity.firstTime = false;
                sendMessage(v);
            }
        });
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
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * koordiniert das Zeichnen der Punkteanzeige
     */
    private void visualizeScore() {

        Bitmap bitmap = Bitmap.createBitmap(500, 100, Bitmap.Config.ARGB_8888);


        // der Abstand vom linken Bildschirmrand wird um jew. 15 px erhöht
        float abstand_x = bitmap.getWidth() / 16;

        // nach und nach jeden Stern zeichnen
        for (int i = 1; i <= 5; i++) {
            // Farben festlegen
            Paint paint = setPaint(i);
            // Abstand zum linken Nachbarstern vergrößern
            abstand_x += bitmap.getWidth() / 8;
            // mit diesen Einstellungen den Stern malen
            this.paintStar(bitmap, paint, abstand_x);
        }
    }


    /**
     * legt Farbe des Sterns fest
     * je nach dem wie das Level abgeschlossen wurde
     * @param starNumber    Stern, der gezeichnet werden soll
     * @return              Farbeigenschaften des Sterns
     */
    private Paint setPaint(int starNumber){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        // alle nicht gemachten Level sind schwarz umrandet
        if(levelinfo.get(starNumber) == 200) {
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
        } // 5. Stufe: rot
        else if(levelinfo.get(starNumber) <= 40)
            paint.setColor(Color.rgb(153,2,14));
        // 4. Stufe: orange
        else if(levelinfo.get(starNumber) <= 50)
            paint.setColor(Color.rgb(255, 127, 39));
        // 3. Stufe: gelb
        else if(levelinfo.get(starNumber) <= 70)
            paint.setColor(Color.rgb(255, 201, 14));
        // 2. Stufe: hellgrün
        else if(levelinfo.get(starNumber) <= 90)
            paint.setColor(Color.rgb(181, 230, 29));
        // 1. Stufe 10-19: grün
        else if(levelinfo.get(starNumber) <= 100)
            paint.setColor(Color.rgb(34, 177, 76));

        return paint;
    }

    /**
     * zeichnet einen Stern
     * Quelle: http://stackoverflow.com/questions/7007429/android-how-to-draw-triangle-star-square-heart-on-the-canvas
     * @param bitmap        Hier wird gezeichnet
     * @param paint         Zeichen-Eigenschaften
     * @param leftBorder    Abstand zum linken Bildschirmrand, bzw. zum linken Nachbar-Stern
     */
    private void paintStar(Bitmap bitmap, Paint paint, float leftBorder){
        Canvas canvas = new Canvas(bitmap);

        // Stern zeichnen
        // Eckpunkte
        // Point besteht aus (x, y)-Werten
        // x und y sind die obere, linke Ecke in dem Bereich, in dem der Stern gezeichnet wird
        int x = (int) leftBorder;
        int y = bitmap.getHeight()/3;
        // size ist die Größe und Höhe des (den Stern umgebenden) Rechtecks
        int size = bitmap.getWidth()/8;
        int hMargin = size/9;
        int vMargin = size/4;

        // Eckpunkte des Sterns festlegen
        Point a = new Point((x + size/2),               y);
        Point b = new Point((x + size/2 + hMargin),     (y + vMargin));
        Point c = new Point((x + size),                 (y + vMargin));
        Point d = new Point((x + size/2 + 2*hMargin),   (y + size/2 + vMargin/2));
        Point e = new Point((x + size/2 + 3*hMargin),   (y + size));
        Point f = new Point((x + size/2),               (y + size - vMargin));
        Point g = new Point((x + size/2 - 3*hMargin),   (y + size));
        Point h = new Point((x + size/2 - 2*hMargin),   (y + size/2 + vMargin/2));
        Point i = new Point(x,                          (y + vMargin));
        Point j = new Point((x + size/2 - hMargin),     (y + vMargin));

        Path path = new Path();
        path.moveTo(a.x, a.y);

        // Eckpunkte verbinden
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.lineTo(d.x, d.y);
        path.lineTo(e.x, e.y);
        path.lineTo(f.x, f.y);
        path.lineTo(g.x, g.y);
        path.lineTo(h.x, h.y);
        path.lineTo(i.x, i.y);
        path.lineTo(j.x, j.y);
        path.lineTo(a.x, a.y);

        path.close();
        canvas.drawPath(path, paint);

        // in die ImageView einfügen
        i_points.setImageBitmap(bitmap);
    }

    /**
     * startet eine neue Activity beim Klick auf einen Button
     * @param view  Button, auf den geklickt wurde
     */
    public void sendMessage(View view){
        if(view.getId() == R.id.restart)
            startActivity(new Intent(this, MainActivity.class));
    }

    /**
     * verhindert, dass über den "Zurück"-Button des Tablets ein Level wiederholt werden könnte
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

    private void changeIcon(android.view.Menu m){
        MenuItem item =m.findItem(R.id.sound);
        item.setIcon(R.mipmap.sound_off);
    }


}
