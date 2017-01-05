package com.example.arabellaprivat.tanzderfunktionen.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.arabellaprivat.tanzderfunktionen.R;

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
    /** informiert, wenn das angeklicckte Level nicht ausgewählt werden darf */
    PopupWindow w_unallowed_choice;
    private Button b_ok;
    /** schließt die Activity */
    private Button b_close;
    /** Button um zur MainActivity zu navigieren */
    private Button b_home;
    /** übergibt dasausgewählte Level der Spiel Activity */
    private Bundle b_new = new Bundle();
    private ArrayList<Integer> levelpoints;
    View layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        /*TODO falls du magst könnten wir das Menü auch als Pop up gestalten
        *
        //Größe beeinflussen
        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width =dm.widthPixels;
        int heigth= dm.heightPixels;

        getWindow().setLayout((int)(width*0.8),(int)(heigth*0.8));
        */



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
        b_home = (Button) findViewById(R.id.home);




        b_level_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // wenn das Level ausgewählt werden darf
                if(choicePermitted(1)){
                    // speicher das Level in einem Bundle
                    b_new.putInt("Level", 1);
                    sendMessage(v);
                } else {
                    w_unallowed_choice.showAtLocation(layout, Gravity.CENTER, 0, 0);
                }
            }
        }); // Ende onClickListener

        b_level_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // wenn das Level ausgewählt werden darf
                if(choicePermitted(2)){
                    // speicher das Level in einem Bundle
                    b_new.putInt("Level", 2);
                    sendMessage(v);
                } else {
                    w_unallowed_choice.showAtLocation(layout, Gravity.CENTER, 0, 0);
                }
            }
        });

        b_level_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // wenn das Level ausgewählt werden darf
                if(choicePermitted(3)){
                    // speicher das Level in einem Bundle
                    b_new.putInt("Level", 3);
                    sendMessage(v);
                } else {
                    w_unallowed_choice.showAtLocation(layout, Gravity.CENTER, 0, 0);
                }
            }
        });

        b_level_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // wenn das Level ausgewählt werden darf
                if(choicePermitted(4)){
                    // speicher das Level in einem Bundle
                    b_new.putInt("Level", 4);
                    sendMessage(v);
                } else {
                    w_unallowed_choice.showAtLocation(layout, Gravity.CENTER, 0, 0);
                }
            }
        });

        b_level_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // wenn das Level ausgewählt werden darf
                if(choicePermitted(5)){
                    // speicher das Level in einem Bundle
                    b_new.putInt("Level", 5);
                    sendMessage(v);
                } else {
                    w_unallowed_choice.showAtLocation(layout, Gravity.CENTER, 0, 0);
                }
            }
        });
        b_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        b_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(v);
            }
        });
    }

    /*@Override
    public void onPause(){
        super.onPause();
        finish();
    }*/

    /** prüft ob das angeklickte Leel ausgewählt werden darf
     * es darf ausgewählt werden, wenn es noch nicht absolviert wurde
     * @return
     */
    public boolean choicePermitted(int chosenLevel){
        return (levelpoints.get(chosenLevel) == null);
    }

    public void sendMessage(View view) {
        Intent i_new;
        // Wenn der Home Button geklickt wurde
        if(view.getId() == R.id.home)
            i_new = new Intent(this, MainActivity.class);
        else {
            b_new.putIntegerArrayList("Punkte", levelpoints);
            // neues Intent
            i_new = new Intent(this, Spiel.class);
            i_new.putExtras(b_new);
        }
        // Activity starten
        startActivity(i_new);
    }

}
