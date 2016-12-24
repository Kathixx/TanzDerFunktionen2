package com.example.arabellaprivat.tanzderfunktionen;

/**
 * Created by Vicky on 12.12.2016.
 */


/** Klasse der DB_TABLE 1
 */

public class DatabaseTable_1 {

    private int level_level;
    private String funktion;
    private float a;
    private float b;
    private float c;
    private int min;
    private int max;
    private String tipp;

    //Konstruktor der die Daten übergibt
    public DatabaseTable_1(int level_level, String funktion, float a, float b, float c, int min, int max, String tipp){
        this.level_level = level_level;
        this.funktion = funktion;
        this.a = a;
        this.b = b;
        this.c = c;
        this.min = min;
        this.max = max;
        this.tipp = tipp;
    }

    public int getLevel_l() {
        return level_level;
    }

    public void setLevel_l(int level_l) {
        this.level_level = level_level;
    }


    public String getFunktion() {
        return funktion;
    }

    public void setFunktion(String funktion) {
        this.funktion = funktion;
    }


    public float getA() {
        return a;
    }

    public void setA(float a) { this.a = a; }


    public float getB() {
        return b;
    }

    public void setB(float b) { this.b = b; }


    public float getC() {
        return c;
    }

    public void setC(float c) { this.c = c; }


    public String getTipp() {
        return tipp;
    }

    public void setTipp(String tipp) {
        this.tipp = tipp;
    }

    @Override
    public String toString() {
        String output = "Level " + level_level+" : "+funktion+". Parameter:"+a+", "+b+", "+c+". Intervall: "+min+" < x > "+max+". Tipps: "+tipp;

        return output;
    }
}

