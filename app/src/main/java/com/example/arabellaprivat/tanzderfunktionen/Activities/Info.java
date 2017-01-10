package com.example.arabellaprivat.tanzderfunktionen.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "fonts/Brandon_reg.otf");
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));

        // Variablen belegen
        t_help = (TextView) findViewById(R.id.help);
        b_close = (Button) findViewById(R.id.close);


        // Größe beeinflussen
        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        int width =dm.widthPixels;
        int heigth= dm.heightPixels;

        getWindow().setLayout((int)(width*0.8),(int)(heigth*0.65));
        getWindow().setGravity(Gravity.BOTTOM|Gravity.RIGHT);


        // entsprechende Info zum jeweiligen Level wird aus der Klasse Spiel übergeben
        // TODO static Instanzen und Methoden non-static machen!!
        t_help.setText(Spiel.getInfo());

        b_close.setOnClickListener(new View.OnClickListener() {
            /**
             * ermöglicht eine Aktoin beim Klick auf den Button
             * @param v View, auf die geklickt wurde
             */
            @Override
            public void onClick(View v) {
                // schließe diese Activity
                finish();
            }
        });
    }
}
