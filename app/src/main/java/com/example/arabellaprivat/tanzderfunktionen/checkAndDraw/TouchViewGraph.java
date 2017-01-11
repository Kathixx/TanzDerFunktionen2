package com.example.arabellaprivat.tanzderfunktionen.checkAndDraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.arabellaprivat.tanzderfunktionen.R;


/**
 * Created by Kathi on 29.11.2016.
 * Zeichenfläche zum zeichnen des Funktionsgraphen
 */

public class TouchViewGraph extends View {
    /* Pfad */
    private Paint paint =new Paint();
    /** Pinsel */
    private Path path= new Path();
    /**Liste zum speichern der xWerte */
    FloatList listX = new FloatList();
    /** Liste zum speichern der yWerte */
    FloatList listY= new FloatList();


    /** Constructor
     * legt Werte des Pinsels fest
     * @param ctx  l
     * @param attrs xy
     */
    public TouchViewGraph(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        // Eigenschaften des "Pinsels" festlegen
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);

    }

    /*
    * zeichnet den Pfad mit entsprechenden "Pinsel"
     */
    @Override
    protected void onDraw (Canvas canvas) {
        canvas.drawPath(path, paint);
    }


    /*
     * Methode fängt mögliche Events ab und reagiert dementsprechend
     * nur bei einfacher Berührung wird ein Hilfspunkt gezeichnet
     */
    @Override
    public boolean onTouchEvent (MotionEvent event){
        float xPos=event.getX();
        float yPos=event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                // löscht den alten pfad, sobald man neu aufsetzt, d.h. man nochmal zeichnen möchte --> warum löschen button noch?
                path.reset();
                path.moveTo(xPos,yPos);
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(xPos,yPos);
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:return false;
        }
        invalidate();
        insertInList (path);
        return true;
    }

    /**
     * fügt mitgegeben y WErt an nächste IndexStelle
     * @param p einzutragender yWert */
    public void insertInList (Path p){

        // Der übergebene Path wir dem Pathmeasure übergeben
        PathMeasure pm=new PathMeasure(p,false);
        //Startwert bzw. laufenden Wert auf 0 setzten
        float distance=0;

        //liest die Länge des Pfades aus
        float length =pm.getLength();
        //Step in dem die WErte ausgelesen werden sollen, geht hier 100f auf dem pfad entlang
        float step=length/10;
        //temporäres, zweistelliges Array zum vorläufigen Abspeichern der x- und y-Werte
        float [] pos = new float [2];
        // Eintrag in die Liste sollen bei 0 beginnen
        int index=0;
        // alte Inhalte der Liste löschen
        listX.clear();
        listY.clear();

        while (distance <= length){
            // speichert die x-Koordinate an STelle pos[0]
            // und die y-Koordinate an Stelle pos[1]
            pm.getPosTan(distance,pos,null);
            //speichert nur den y-WErt in die Liste
            // x-WErt wird durch step vorgegeben
            //FRAGE: Path beginnt nciht immer gleich, x-Wert richtet sich an gemalten path nicht am koordniatensystem!?
            // xPixel einspeicher
            // TODO casten weg
            listX.add (index, pos [0]);
            //yPixel einspeichern
            listY.add(index, pos[1]);
            index++;
            distance +=step;
        }
    }// Ende insertInList

    /** gibt aktelle Liste  mit den x-Werten zurück
     * @return Liste
     */
    public FloatList getListX (){
        return listX;
    }// Ende getListX

    /** gibt aktelle Liste  mit den x-Werten zurück
     * @return Liste
     */
    public FloatList getListY(){return listY;}// Ende getListY

    /** Leert die Zeichenfläche indem der Pfad gelöscht wird
     *
     */
    public void deleteView(){
        path.reset();
        invalidate();
    }// Ende deleteView

    /**
     * ersetzt den Pfad durch übergebene Farbe in dem er ihn neu malt
     * @param color Farbe in der der Pfad erneut gezeichnet werden soll
     */
    public void redrawInColor (int color){
        paint.setColor(color);
        invalidate();
    }// ENde redrawInColor


    /**
     * ändert das Hintergrundbild der Zeichenfläche
     * Methode wird bei der checkFunction Funktion aufgerufen
     * Level zeigt an welche Funktion gerade gezeichnet werden musste
     * @param level
     */
    public void changeBackground(int level){
        switch (level){
            case 1: this.setBackgroundResource(R.drawable.linearfunction);
                    break;
            case 2: this.setBackgroundResource(R.drawable.quadratfunction);
                    break;
            case 3: this.setBackgroundResource(R.drawable.rationalfunction);
                    break;
            case 4: this.setBackgroundResource(R.drawable.trigfunction);
                    break;
            case 5: this.setBackgroundResource(R.drawable.logfunction);
                    break;
            default: break;
        }
    }// ende changeBackground



}
