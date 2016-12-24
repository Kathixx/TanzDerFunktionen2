package com.example.arabellaprivat.tanzderfunktionen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Anleitung extends AppCompatActivity {

    /** Text für die Anleitung */
    private TextView t_instructions;
    /** schließt die Activity */
    private Button b_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anleitung);

        // Variablen belegen
        t_instructions = (TextView) findViewById(R.id.instructions);
        b_close = (Button) findViewById(R.id.close);

        // richtigen Text ausgeben
        t_instructions.setText("Diese App hilft Dir dabei, deine Kenntnisse über mathematische Funktionen auszubauen. Freu dich auf 5 Level, in denen Du Funktionen untersuchst und zeichnest. Sammle bis zu 5 Punkten und werde so nicht nur zum Mathe-King, sondern bereite Dich egal wo du gerade bist, auf die nächste Klausur vor!\n" +
                "Und so geht’s: Oben im Bildschirm siehst du die Funktion, die Du in das Zeichenfeld darunter zeichnen sollst. Mach Dir ruhig Notizen oder Berechnungen auf einem Zettel, denn nicht immer ist die Lage der Funktion offensichtlich.\n" +
                "Wenn du mal nicht weiter kommst, tippe auf den Info-Button unten links und erhalte hilfreiche Tipps.\n" +
                "Und jetzt viel Spaß beim Zeichnen!\n");

        b_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
