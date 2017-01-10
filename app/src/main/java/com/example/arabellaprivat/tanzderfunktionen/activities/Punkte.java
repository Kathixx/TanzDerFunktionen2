package com.example.arabellaprivat.tanzderfunktionen.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.example.arabellaprivat.tanzderfunktionen.R;

import java.util.Locale;

/**
 * Created by Kathi on 05.01.2017.
 *
 * Quellen:
 * Umrahmung:  http://stackoverflow.com/questions/3496269/how-do-i-put-a-border-around-an-android-textview
 * Schrift: http://stackoverflow.com/questions/27588965/how-to-use-custom-font-in-android-studio
 * Größe und PopUpWindow im Allgemeinen: https://www.youtube.com/watch?v=fn5OlqQuOCk
 *
 */

public class Punkte extends Activity {

    TextView t_points;
     TextView t_result2;
     TextView t_conclusion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punkte);
        t_points= (TextView) findViewById(R.id.Punkte);
        t_result2= (TextView)findViewById(R.id.Ergebnis);
        t_conclusion=(TextView)findViewById(R.id.Erklärung);

        // Datenübergabe mittels Bundle
        Intent intent=getIntent();
        Bundle bundle= intent.getExtras();
        String c= bundle.getString("conclusion");
        String r= bundle.getString("result2");
        int co=bundle.getInt("color");
        int p=bundle.getInt("points");

        // TExte individuell anzeigen
        t_points.setText(String.valueOf(p));
        t_result2.setText(r);
        t_conclusion.setText(c);


        // Größe beeinflussen
        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width =dm.widthPixels;
        int heigth= dm.heightPixels;

        getWindow().setLayout(500,350);

        // Schrift beeinflussen
        Typeface fontNumber = Typeface.createFromAsset(getAssets(),  "fonts/BAUHS93.TTF");
        Typeface fontText=Typeface.createFromAsset(getAssets(),  "fonts/Brandon_reg.otf");
        t_points.setTypeface(fontNumber);
        t_result2.setTypeface(fontText);
        t_points.setTextColor(co);
        t_conclusion.setTypeface(fontText);
    }



}
