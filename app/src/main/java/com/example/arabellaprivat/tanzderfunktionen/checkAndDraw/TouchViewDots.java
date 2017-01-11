package com.example.arabellaprivat.tanzderfunktionen.checkAndDraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kathi on 03.12.2016.
 * Zeichenfläche zum malen von Hilfspunkten
 */

public class TouchViewDots extends View {
    /** "Pinsel"  */
    private Paint paint =new Paint();
    /** Pfad */
    private Path path= new Path();
    /**temporärer Pfad, wird zum Löschen des letzten Hilfspunktes benötigt */
    private Path tempPath= new Path();

    /** Constructor
     * legt Werte des Pinsels fest
     * @param ctx
     * @param attrs
     */
    public TouchViewDots(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        // Eigenschaften des "Pinsels" festlegen
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(10f);
    }

    /*
    * zeichnet den Pfad mit entsprechenden "Pinsel"
     */
    @Override
    protected void onDraw (Canvas canvas) {
        canvas.drawPath(path, paint);
    }// Ende onDraw


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
                // letzten Stand des Pfades speichern
                if (!path.isEmpty()){
                    tempPath.set(path);
                }
                // Hilfspunkt einzeichnen
                path.addCircle(xPos,yPos,2f, Path.Direction.CCW);
                return true;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:return false;
        }
        invalidate();
        return true;
    }// Ende onTouchEvent


    /**
     * löscht die gesamte TouchView
     * wird bie Doppelklick des Buttons löschen aufgerufen
     */
    public void deleteView(){
        path.reset();
        invalidate();
    }// Ende deleteView


    /**
     * löscht den letzten Hilfspunkt
     * wird bei einmaligen Klicken des Buttons löschen aufgerufen
     */
    public void deleteLast(){
        path.set(tempPath);
        invalidate();
    }//Ende deleteLast

}
