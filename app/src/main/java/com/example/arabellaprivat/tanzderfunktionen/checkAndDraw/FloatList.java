package com.example.arabellaprivat.tanzderfunktionen.checkAndDraw;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Kathi on 10.11.2016.
 */

class FloatList extends ArrayList <Double> implements Serializable{


    /** Constructor */
    FloatList(){
    };

    /*
    Methoden void add (int index, E element) und E get (int index) sind schon vorgeben
    und werden so wie vorgegeben verwendet und nicht überschrieben
     */


    /**
     * gibt den Wert an der entsprechenden Stelle in der Liste als double zurück
     */

    /** getList()
     * gibt Liste zurück
     * @return Liste
     */
    public FloatList getList (){
        return this;
    }
}