package com.example.arabellaprivat.tanzderfunktionen.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
                // MainActivity.firstTime = true;
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
        Bitmap bitmap = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888);

        // Start bei 1/16 der Bitmap
        float leftBorder = bitmap.getWidth()/16;
        // Style und Farbe
        Paint paint = new Paint();
        // insgesamt gibt es 5 Kategorien
        // je nach dem wie hoch die Anzahl der erreichten Punkte sind, werden verschiedene Farben verwendet
        for(int i=1; i<=category; i++){
            if(category == 1)
                // diese Punkte werden rot gezeichnet
                paint.setColor(Color.RED);
            else if(category == 2)
                paint.setColor(Color.rgb(255, 127, 39));
            else if(category == 3)
                paint.setColor(Color.YELLOW);
            else if(category == 4)
                paint.setColor(Color.rgb(181, 230, 29));
            else
                paint.setColor(Color.GREEN);
            paint.setStyle(Paint.Style.FILL);

            // der Abstand zwischen den Kreisen beträgt 1/8 der gesamten Breite der Bitmap
            leftBorder += bitmap.getWidth()/8;

            // mit diesen Einstellungen den Kreis zeichnen
            this.paintCircle(bitmap, paint, leftBorder);
        }
        // restliche nicht erreichte Punkte zeichnen
        for(int i=category; i<=4; i++){
            // diese Punkte werden schwarz umrandet
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);

            // der Abstand zwischen den Kreisen beträgt 1/8 der gesamten Breite der Bitmap
            leftBorder += bitmap.getWidth()/8;

            // mit diesen Einstellungen den Kreis zeichnen
            this.paintCircle(bitmap, paint, leftBorder);
        }
    }

    /**
     * zeichnet einen Punkt
     * @param bitmap        Hier wird gezeichnet
     * @param paint         Zeichen-Eigenschaften
     * @param linkerAbstand     Abstand zum linken Bildschirmrand, bzw. zum linken Nachbar-Kreis
     */
    private void paintCircle(Bitmap bitmap, Paint paint, float linkerAbstand){
        Canvas canvas = new Canvas(bitmap);
        // Kreis zeichnen
        // linker Abstand vergrößert sich nach jedem Kreis
        // auf Höhe der Hälfte der Bitmap
        // Radius 50 px
        // mit den "Stift"-Eigenschaften, die je nach Level verändert wurden
        canvas.drawCircle(linkerAbstand, bitmap.getHeight()/2, 45, paint);

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
