package com.example.arabellaprivat.tanzderfunktionen.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.arabellaprivat.tanzderfunktionen.R;

/**
 * hier werden Tipps zum Lösen der Aufgaben gegeben
 */
public class Info extends Activity {


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


        // Größe beeinflussen
        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width =dm.widthPixels;
        int heigth= dm.heightPixels;

        getWindow().setLayout((int)(width*0.8),(int)(heigth*0.5));


        // entsprechende Info zum jeweiligen Level wird aus der Klasse Spiel übergeben
        // TODO static Instanzen und Methoden non-static machen!!
        t_help.setText(Spiel.getInfo());

        b_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // schließe diese Activity
                finish();
            }
        });
    }
}
