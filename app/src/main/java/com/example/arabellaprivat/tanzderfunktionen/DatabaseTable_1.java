package com.example.arabellaprivat.tanzderfunktionen;

/** Klasse der DB_TABLE 1
 */

public class DatabaseTable_1 {

    private int level_level;
    private String funktion;
    private String tipp;
    private float a;
    private float b;
    private float c;
    private float d;
    private float x1;
    private float x2;
    private float y;
    private int min;
    private int max;


    //Konstruktor der die Daten übergibt
    public DatabaseTable_1(int level_level, String funktion, String tipp, float a, float b, float c, float d, float x1, float x2, float y, int min, int max){
        this.level_level = level_level;
        this.funktion = funktion;
        this.tipp = tipp;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.x1 = x1;
        this.x2 = x2;
        this.y = y;
        this.min = min;
        this.max = max;

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


    public String getTipp() {return tipp;}
    public void setTipp(String tipp) {this.tipp = tipp;}


    public float getA() {
        return a;
    }
    public void setA(float a) { this.a = a; }


    public float getB() {
        return b;
    }
    public void setB(float b) { this.b = b; }


    public float getC() {return c;}
    public void setC(float c) { this.c = c; }


    public float getD() {return d;}
    public void setD(float d) { this.d = d; }


    public float getX1() {return x1;}
    public void setX1(float x1) { this.x1 = x1; }

    public float getX2() {return x2;}
    public void setX2(float x2) { this.x2 = x2; }

    public float getY() {return y;}
    public void setY(float y) { this.y = y; }

    public float getMIN() {return min;}
    public void setMIN(int min) { this.min = min; }

    public float getMAX() {return max;}
    public void setMAX(int max) { this.max = max; }

    @Override
    public String toString() {
        String output =  level_level+","+funktion+","+tipp+","+a+","+b+","+c+","+d+","+x1+","+x2+","+y+","+min+","+max;

        return output;
    }
}
