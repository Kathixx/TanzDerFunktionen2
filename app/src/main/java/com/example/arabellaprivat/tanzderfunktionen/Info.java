package com.example.arabellaprivat.tanzderfunktionen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * hier werden Tipps zum Lösen der Aufgaben gegeben
 */
public class Info extends AppCompatActivity {

    /** zeigt den Hilfetext an */
    private TextView t_help;
    /** schließt die Activity */
    private Button b_close;
    private int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        // daraus die übergebenen Daten holen
        this.level = bundle.getInt("Level");

        // Variablen belegen
        t_help = (TextView) findViewById(R.id.help);
        b_close = (Button) findViewById(R.id.close);

        // Intent, das diese Activity geöffnet hat holen
        Intent i = getIntent();
        Bundle b = i.getExtras();

        // Hilfetext ausgeben
        if(level == 1)
            t_help.setText("Erinnerst Du Dich an die Funktion der Parameter m und b in f(x)=mx+b? m steht für die Steigung und b für den Schnittpunkt mit der y-Achse.");
        else if(level == 2)
            t_help.setText("Alles, was Du tun musst ist, den Scheitelpunkt und die Verschiebung abzulesen.");
        else if(level == 3)
            t_help.setText("Wie verhält sich der Graph für lim x->0?");
        else if(level == 4)
            t_help.setText("Die allgemeine Form dieser Funktion lautet: f(x) = a * sin(b * (x+c)) + d Was geben die Parameter an? a: Vergrößerung bzw. Verkleinerung der Amplitude b: Steckung / Stauchung / Spiegelung an der x-Achse c: Verschiebung nach links oder rechts d: Verschiebung auf der y-Achse");
        else if(level == 5)
            t_help.setText("Weißt du noch in welchem Punkt sich alle log-Funktionen schneiden?");


        b_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // schließe diese Activity
                finish();
            }
        });
    }
}
