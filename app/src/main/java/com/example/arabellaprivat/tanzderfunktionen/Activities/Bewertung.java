package com.example.arabellaprivat.tanzderfunktionen.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arabellaprivat.tanzderfunktionen.R;

import java.util.ArrayList;

/**
 * zur Bewertung des Spiels
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
    private ArrayList<Integer> levelinfo;
    private int score = 0;
    /** Bewertungskategorie */
    private int category;
    /** ist der Sound eingeschaltet? */
    private boolean soundIsOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bewertung);

        // Intent, das diese Activity geöffnet hat
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        // daraus die übergebenen Daten holen
        this.levelinfo = bundle.getIntegerArrayList("Infos");
        soundIsOn=bundle.getBoolean("Sound");

        // Score berechnen
        for(int i=1; i<=5; i++){
            if(levelinfo.get(i) != 100)
                score += levelinfo.get(i);
        }

        // Variablen belegen
        t_review = (TextView) findViewById(R.id.review);
        i_points = (ImageView) findViewById(R.id.points);
        t_score = (TextView) findViewById(R.id.score);
        b_restart = (Button) findViewById(R.id.restart);

        // Bewertungstext
        // je nach Punktezahl gibt es einen anderen Text
        // bei 0 Punkten
        if(score <= 100){
            t_review.setText("Leider hast du keine Funktion richtig gezeichnet. Vielleicht solltest Du Dich nochmal in das Thema einarbeiten.");
            category = 1;
        } else if(score <= 200){
            t_review.setText("Das war noch nicht ganz überzeugend. Übe weiter, um Dich zu verbessern.");
            category = 2;
        } else if(score <= 300){
            t_review.setText("Das war schon ein guter Anfang. Übe weiter, um Dich zu verbessern.");
            category = 3;
        } else if(score <= 400) {
            t_review.setText("Gut gemacht. Übe weiter, um Dein Wissen zu festigen.");
            category = 4;
        } else {
            t_review.setText("Super! Du hast alles richtig gezeichnet. Du bist bereit für die nächste Prüfung.");
            category = 5;
        }

        // Visualisierung der Punkte
        visualizeScore();

        // Wie viele Punkte wurden erreicht?
        t_score.setText("Du erhälst " + score + " von 500 Punkten.");

        b_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Möglichkeit dann weiterzuspielen "ausschalten"
                MainActivity.firstTime = true;
                sendMessage(v);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions, menu);
        if (!soundIsOn) changeIcon(menu);
        return true;

    }


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
     * hier werden Zeichen-Eigenschaften gesetzt
     */
    private void visualizeScore(){
        Bitmap bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);

        // der Abstand vom linken Bildschirmrand wird um jew. 15 px erhöht
        float abstand_x = bitmap.getWidth()/16;
        // Style und Farbe hängen von der Bewertung der Level ab
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        // mit einer Schleife gehen wir durch die Liste zu den verschiedenen Levels
        for (int i=1; i<=5; i++) {
            // alle nicht gemachten Level sind schwarz umrandet
            if(levelinfo.get(i) == 200) {
                paint.setColor(Color.BLACK);
                paint.setStyle(Paint.Style.STROKE);
            }
            // 5. Stufe 0-9: rot
            else if(levelinfo.get(i) <= 20){
                // sei der Kreis rot ausgemalt
                paint.setColor(Color.rgb(153,2,14));
            }
            // 4. Stufe 10-19: orange
            else if(levelinfo.get(i) <= 40){
                paint.setColor(Color.rgb(255, 127, 39));
            }
            // 3. Stufe 20-29: gelb
            else if(levelinfo.get(i) <= 60){
                paint.setColor(Color.rgb(255, 201, 14));
            }
            // 2. Stufe 30-39: hellgrün
            else if(levelinfo.get(i) <= 80){
                paint.setColor(Color.rgb(181, 230, 29));
            }
            // 1. Stufe 10-19: grün
            else if(levelinfo.get(i) <= 100){
                paint.setColor(Color.rgb(34, 177, 76));
            }

            // Abstand zum linken Nachbarkreis vergrößern
            abstand_x += bitmap.getWidth()/8;

            // mit diesen Einstellungen den Kreis malen
            this.paintStar(bitmap, paint, abstand_x);
        }
    }

    /**
     * zeichnet einen Punkt
     * @param bitmap        Hier wird gezeichnet
     * @param paint         Zeichen-Eigenschaften
     * @param leftBorder     Abstand zum linken Bildschirmrand, bzw. zum linken Nachbar-Kreis
     */
    private void paintStar(Bitmap bitmap, Paint paint, float leftBorder){
        Canvas canvas = new Canvas(bitmap);
        // Kreis zeichnen
        // linker Abstand vergrößert sich nach jedem Kreis
        // auf Höhe der Hälfte der Bitmap
        // Radius 50 px
        // mit den "Stift"-Eigenschaften, die je nach Level verändert wurden
        // canvas.drawCircle(leftBorder, bitmap.getHeight()/2, 45, paint);

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

    public void sendMessage(View view){
        if(view.getId() == R.id.restart)
            startActivity(new Intent(this, MainActivity.class));
    }

    // disable Back-Button
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
