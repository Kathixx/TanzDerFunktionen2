package com.example.arabellaprivat.tanzderfunktionen.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.arabellaprivat.tanzderfunktionen.R;

/* diese Klasse erklärt die Regeln der App */
public class Instruction extends AppCompatActivity {

    /**
     * erstellt die Activity bei dessen Aufruf
     * @param savedInstanceState    Siehe Super Klasse
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

        // Schriftart für die ganze Activity ändern mithilfe des FontChangeCrawlers
        FontChanger fontChanger = new FontChanger(getAssets(), "fonts/Brandon_reg.otf");
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));

        // Variablen belegen
        TextView t_instructions = (TextView) findViewById(R.id.instructions);
        Button b_close = (Button) findViewById(R.id.close);

        // richtigen Text ausgeben
        t_instructions.setText("Diese App hilft Dir dabei, deine Kenntnisse über mathematische Funktionen auszubauen. Freu dich auf 5 Level, in denen Du Funktionen untersuchst und zeichnest. Sammle bis zu 500 Punkte und werde so nicht nur zum Mathe-King, sondern bereite Dich egal wo du gerade bist, auf die nächste Klausur vor!\n\n" +
                "Und so geht’s: Oben im Bildschirm siehst du die Funktion, die Du in das Zeichenfeld darunter zeichnen sollst. Mach Dir ruhig Notizen oder Berechnungen auf einem Zettel, denn nicht immer ist die Lage der Funktion offensichtlich.\n\n" +
                "Wenn du mal nicht weiter kommst, tippe auf den Info-Button unten rechts und erhalte hilfreiche Tipps.\n\n" +
                "Und jetzt viel Spaß beim Zeichnen!\n\n");

        b_close.setOnClickListener(new View.OnClickListener() {
            /**
             * ermöglicht eine Aktoin beim Klick auf den Button
             * @param v View, auf die geklickt wurde
             */
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
