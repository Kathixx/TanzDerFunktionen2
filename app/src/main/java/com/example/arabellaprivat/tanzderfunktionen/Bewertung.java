package com.example.arabellaprivat.tanzderfunktionen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
    private ArrayList<Integer> levelpoints;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bewertung);

        // Intent, das diese Activity geöffnet hat
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        // daraus die übergebenen Daten holen
        this.levelpoints = bundle.getIntegerArrayList("Punkte");

        // Score berechnen
        for(int i=1; i<=5; i++){
            if(levelpoints.get(i) != null)
                score += levelpoints.get(i);
        }

        // Variablen belegen
        t_review = (TextView) findViewById(R.id.review);
        i_points = (ImageView) findViewById(R.id.points);
        t_score = (TextView) findViewById(R.id.score);
        b_restart = (Button) findViewById(R.id.restart);

        // Bewertungstext
        // je nach Punktezahl gibt es einen anderen Text
        // bei 0 Punkten
        if(score == 0){
            t_review.setText("Leider hast du keine Funktion richtig gezeichnet. Vielleicht solltest Du Dich nochmal in das Thema einarbeiten.");
        } else if(score == 1 || score == 2){
            t_review.setText("Das war schon ein guter Anfang. Übe weiter, um Dich zu verbessern.");
        } else if(score == 3 || score == 4){
            t_review.setText("Gut gemacht. Übe weiter, um Dein Wissen zu festigen.");
        } else {
            t_review.setText("Perfekt! Du hast alles richtig gemacht. Weiter so!");
        }

        // Visualisierung der Punkte
        visualizeScore();

        // Wie viele Punkte wurden erreicht?
        t_score.setText("Du erhälst " + score + " von 5 Punkten.");

        b_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Datenbank zurücksetzen
                sendMessage(v);
            }
        });
    }
    // Visualisierung der Punkte wie in der Activity Spiel
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
        // insgesamt werden 5 Punkte gezeichnet
        // je nach dem wie hoch die Anzahl der erreichten Punkte sind, werden verschiedene Farben verwendet
        for(int i=1; i<=score; i++){
            // diese Punkte werden hellblau gezeichnet
            paint.setColor(Color.rgb(158, 174, 202));
            paint.setStyle(Paint.Style.FILL);

            // der Abstand zwischen den Kreisen beträgt 1/8 der gesamten Breite der Bitmap
            leftBorder += bitmap.getWidth()/8;

            // mit diesen Einstellungen den Kreis zeichnen
            this.paintCircle(bitmap, paint, leftBorder);
        }
        // restliche nicht erreichte Punkte zeichnen
        for(int i=score; i<=4; i++){
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
        if(view.getId() == R.id.restart) {
            startActivity(new Intent(this, MainActivity.class));
        }
}
}
