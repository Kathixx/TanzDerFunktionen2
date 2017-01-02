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
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kathi on 03.12.2016.
 */

public class Hilfspunkte extends View {
    private Paint paint =new Paint();
    private Path path= new Path();
    private Path tempPath= new Path();
    private ArrayList<Path> pathList= new ArrayList<>();


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
    public Hilfspunkte (Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(10f);
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
                if (!path.isEmpty()){
                    tempPath.set(path);
                    //pathList.add(path);
                }
                path.addCircle(xPos,yPos,2f, Path.Direction.CCW);
                return true;
            case MotionEvent.ACTION_MOVE:
                //path.addCircle(xPos,yPos,2f, Path.Direction.CCW);
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:return false;
        }
        invalidate();
        return true;
    }




    public void deleteView(){
        path.reset();
        invalidate();
        index=0;
    }

    public void deleteLast(int counter, TextView t){
        //int index= pathList.size()-(counter);
        //if(index >=1) tempPath=pathList.get(index-1);
        tempPath= pathList.get(0);
        path.set(tempPath);
        t.setText("Counter: "+counter+" Länge: "+pathList.size()+" index: "+index);
        invalidate();
    }

    public void deleteLast(){
        path.set(tempPath);
        invalidate();
    }





}
