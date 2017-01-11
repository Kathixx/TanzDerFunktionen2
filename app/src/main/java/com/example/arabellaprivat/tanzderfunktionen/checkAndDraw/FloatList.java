package com.example.arabellaprivat.tanzderfunktionen.checkAndDraw;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Kathi on 10.11.2016.
 * Klasse zum einfachen Zurückgeben von Floatlisten
 */

class FloatList extends ArrayList <Float> {


    /** Constructor */
    FloatList(){};

    /**
     * gibt Liste zurück
     * @return Liste
     */
    public FloatList getList (){
        return this;
    }
}