package com.example.arabellaprivat.tanzderfunktionen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

/**
 * in dieser Klasse wird das Level ausgweählt
 */
public class Menu extends AppCompatActivity {

    // IV
    /** diese Buttons wählen aus, welches Level gespielt werden soll */
    private Button b_level_1;
    private Button b_level_2;
    private Button b_level_3;
    private Button b_level_4;
    private Button b_level_5;
    /** schließt die Activity */
    private Button b_close;
    /** übergibt dasausgewählte Level der Spiel Activity */
    private Bundle b_new = new Bundle();
    private ArrayList<Integer> levelpoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Intent, das diese Activity geöffnet hat holen
        Intent i_old = getIntent();
        Bundle b_old = i_old.getExtras();
        // daraus die übergebenen Daten holen
        this.levelpoints = b_old.getIntegerArrayList("Punkte");

        // Variablen belegen
        b_level_1 = (Button) findViewById(R.id.level_1);
        b_level_2 = (Button) findViewById(R.id.level_2);
        b_level_3 = (Button) findViewById(R.id.level_3);
        b_level_4 = (Button) findViewById(R.id.level_4);
        b_level_5 = (Button) findViewById(R.id.level_5);
        b_close = (Button) findViewById(R.id.close);

        b_level_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // speicher das Level in einem Bundle
                b_new.putInt("Level", 1);
                sendMessage(v);
            }
        }); // Ende onClickListener

        b_level_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // speicher das Level in einem Bundle
                b_new.putInt("Level", 2);
                sendMessage(v);
            }
        });

        b_level_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b_new.putInt("Level", 3);
                sendMessage(v);
            }
        });

        b_level_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b_new.putInt("Level", 4);
                sendMessage(v);
            }
        });

        b_level_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b_new.putInt("Level", 5);
                sendMessage(v);
            }
        });
        b_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void sendMessage(View view) {
        b_new.putIntegerArrayList("Punkte", levelpoints);
        // neues Intent
        Intent i_new = new Intent(this, Spiel.class);
        i_new.putExtras(b_new);
        // Activity starten
        startActivity(i_new);
    }

}
