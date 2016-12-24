package com.example.arabellaprivat.tanzderfunktionen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;



/**
 * Created by Kathi on 29.11.2016.
 */

public class Zeichenfläche extends View {

    private Paint paint =new Paint();
    private Path path= new Path();

    PathMeasure pm;
    float pos []=  new float [2];
    float distance;
    float length;
    float step;

    /* neue Liste wird hier erstellt
     während dem Zeichnen werden die y-Wert hier eingetragen
     */
    Liste listX = new Liste();
    Liste listY= new Liste();

    int index =0;
    /** Constructor
     * legt Werte des Pinsels fest
     * @param ctx  l
     * @param attrs xy
     */
    public Zeichenfläche (Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8f);
        distance =0;

    }

    @Override
    protected void onDraw (Canvas canvas) {

        canvas.drawPath(path, paint);
    }


    @Override
    public boolean onTouchEvent (MotionEvent event){
        float xPos=event.getX();
        float yPos=event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                // löscht den alten pfad, sobald man neu aufsetzt, d.h. man nochmal zeichnen möchte --> warum löschen button noch?
                //path.reset();
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
        einfuegen (path);
        return true;
    }

    /** einfugen ()
     * fügt mitgegeben y WErt an nächste IndexStelle
     * @param p einzutragender yWert */
    public void einfuegen (Path p){
        // alte Inhalte der Liste löschen
        listX.clear();
        listY.clear();
        //Startwert bzw. laufenden WErt auf 0 setzten
        distance=0;
        // Eintrag in die Liste sollen bei 0 beginnen
        index=0;
        // Der übergebene Path wir dem Pathmeasure übergeben
        pm=new PathMeasure(p,false);
        //liest die Länge des Pfades aus
        length =pm.getLength();
        //Step in dem die WErte ausgelesen werden sollen, geht hier 100f auf dem pfad entlang
        step=length/10;
        while (distance <= length){
            // speichert die x-Koordinate an STelle pos[0]
            // und die y-Koordinate an Stelle pos[1]
            pm.getPosTan(distance,pos,null);
            //speichert nur den y-WErt in die Liste
            // x-WErt wird durch step vorgegeben
            //FRAGE: Path beginnt nciht immer gleich, x-Wert richtet sich an gemalten path nicht am koordniatensystem!?
            // xPixel einspeicher
            listX.add (index, pos [0]);
            //yPixel einspeichern
            listY.add(index, pos[1]);
            index++;
            distance +=step;
        }
    }

    /** gibt aktelle Liste zurück
     * @return Liste
     */
    public Liste getListX (){
        return listX;
    }
    public Liste getListY(){return listY;}





    public void loescheView(){
        path.reset();
        invalidate();
        index=0;



    }


    public void changeBackground(int x){
        switch (x){
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
    }


}
