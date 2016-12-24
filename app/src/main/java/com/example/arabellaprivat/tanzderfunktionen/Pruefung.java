package com.example.arabellaprivat.tanzderfunktionen;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Kathi on 09.12.2016.
 * enthält alle Methoden, die für die Prüfung der gezeichneten Funktion mit der original Funktion notwendig sind
 * dadurch wird v.a. die KLasse "Spiel" übersichtlicher
 */

public class Pruefung {
     /** Instanzvariablen */
    /** Liste, die die übergebenen x-Werte des Pfades abspeichert */
    private Liste listeX;
    /** Liste, die die übergebenen y-Werte des Pfades abspeichert */
    private Liste listeY;
    /** Zeichenfläche */
    private Zeichenfläche z;
    /** Hilfspunkte */
    private Hilfspunkte h;
    /** Levelanzeige */
    private int x;


    /**
     *Constructor
     */
    Pruefung (Zeichenfläche zf, Hilfspunkte hp){
        z=zf;
        h=hp;
        listeX=z.getListX();
        listeY=z.getListY();
    }


    /** Methoden *************************************************** */

    /** check ()
     * vergleicht den gezeichneten Pfad mit der jeweils vorgegebenen Funktion
     * @param level gibt das aktuelle level an
     * @return true falls gezeichnete Funktion der Original-Funktion entspricht
    */


    int check(int level) {
        double xWert;
        double yWert;
        // index i wird fortlaufend hochgezählt, bis zum Ende der Liste
        int index=0;
        int listLength=listeX.size();
        String s="";
        // Zähler zählt richtige Vergleiche des gezeichneten und berechneten Wert
        int counter=0;

        //TODO nur wenn Extremstellen-Check positiv ist, dann wird weiter geprüft
        // TODO hier harcoding: Nullstellen aus Level 1: (-4/0)
        if (comparePoints(convertViewToBitmap(z),-4,0)) {
            while (index < listLength) {
                // x-Wert aus der Liste auslesen
                xWert = (listeX.get(index));
                // in x-Koordinaten-Werte umwandeln
                xWert = pixelToCoordinate(xWert, z, 10);
                // entsprechenden y-Wert mit der Funktion berechnen , Level x zeigt an welche Funktion aufgerufen werden soll
                // TODO VICKY + ARABELLA: wie liest man das Level aus??
                switch (level) {
                    case 1:
                        yWert = linearFunction(xWert);
                        break;
                    case 2:
                        yWert = quadratFunction(xWert);
                        break;
                    case 3:
                        yWert = rationalFunction(xWert);
                        break;
                    case 4:
                        yWert= trigonometricFunction(xWert);
                        break;
                    case 5:
                        yWert= logarithmicFunction(xWert);
                        break;
                    default:
                        yWert = 0;
                }
                // y-Koordinaten-Wert in Pixel umrechnen
                yWert = coordinateToPixel(yWert, z, 6);
                // berechneten y-Wert mit y-Wert aus der Liste vergleichen
                boolean comparison = compare(yWert, listeY, index);
                // counter hochzählen
                if (comparison) counter++;
                // index hochzählen
                index++;
            }
            // je nach dem wie viele Vergleiche richtig sind wird der gezeichnete Pfad akzeptiert
            // falls richtig wird 1 zurückgegeben
            if (counter > 8)return 1;
            // sind die Nullstellen, Extremas, Achsenabschnitt ect richtig und ist nur ungenau gezeichnet worden
            // dann ist es leider trotzdem falsch und es wird -2 zurückgegeben
            else return -2;

        }
        // wenn die Nullstellen, Extremas und Achsenabschnitt nicht richtig gezeichnet wurden wird -1 zurückgegeben
        else return -1;

    }


    /**
     * lineare Funktion: f(x)=0,5x+2
     * hier könnte man eventuell vorgegeben Werte auch aus Datenbank auslesen?
     * @param x zu berechneter x-Wert in Koordinaten-Angaben
     * @return yWert berechneter y-Wert
     */
    private double linearFunction (double x){
        // TODO VICKY auslesen aus der Liste
        // Daten evtl. aus Datenbank auslesen
        double yWert=(0.5*x)+2;
        return  round(yWert);
    }// Ende linearFunction()

    /**
     * quadratische Funktion: 0.25*x^2+1x-4
     * Todo Vicky Datenbank auslesen
     */
    private double quadratFunction (double x){
        double yWert=(0.25*x*x)+x-4;
        return round(yWert);
    }

    /**
     * gebrochenrationale Funktion: (3)/(x-2)+1
     * TODO VICKY Datenbank auslesen
     */
    private double rationalFunction (double x) {
        double yWert = 3/(x-2)+1;
        return round(yWert);
    }

    /**
     * trigonometrische Funktion: 3cos(x+1)
     * TODO VICKY Datenbank auslesen
     * TODO bei cosinus muss in der Tabelle bereits ein Parameter mit -90 sein, sodass man mit Sinus rechnen kann!
     */
    private double trigonometricFunction (double x){
        //double yWert = 3*Math.sin(x+1);
        double yWert = 3*Math.cos(x+1);
        return round (yWert);
    }

    /**
     * logharitmische Funktionen: ln(x+5)
     * TODO VICKY Datenbank auslesen
     * TODO welche anderen logharitmischen FUnktion gibt es noch? wie könnte man die miteinander vereinbaren?
     */
    private double logarithmicFunction (double x){
        double yWert= Math.log(x+5);
        return round (yWert);
    }




    /** Methode pixelToCoordinate
     *  rechnet den ausgelesen xWert in Koordinaten-Werte um
     *  abhängig von den maximalen x-WErten und y-WErten!
     *  Vorrausgesetzt dass der Koordinatenursprung in der Mitte des Views liegt
     *
     *  @param pixel  ausgelesener x-WErt in Pixelangaben
     *  @param xMax   maximaler Wert der x-Achse
     *  @return xWert dieser Wert wird in die Funktion eingesetzt
     */
    private double pixelToCoordinate ( double pixel, Zeichenfläche z, int xMax){
        double xWert;
        // Liest die maximalen Pixelwerte des Views zurück
        //float maxXPixel = v. getRight-getLeft??
        float widthView=z.getRight()-z.getLeft();
        // Länge von x=0 bis x=xMax
        float halfView=widthView/2;
        // LängenMaß einer Koordinate
        float stepPixels= widthView/(2*xMax);
        xWert=(pixel-halfView)/stepPixels;
        return  round(xWert);
    }// Ende pixelToCoordinate



    /** Methode round
     * rundet einen Double-Wert auf 2 Dezimalstellen ab
     * @param d zu rundender Double-Wert
     * @return gerundeter Double-Wert
     */
    private double round (double d){
        double rounded=Math.round(d*100);
        return rounded/100;
    }//Ende round

    /** Methode coordinateToPixel
     * rechnet einen Koordinaten-Wert in Pixelwert um
     * v.a. für y-Werte
     * abhängig von den maximalen y-Werten
     * Vorraussetzung: Koordinatensystem ist zentriert
     *
     * @param coordinate  berechnete y-Koordinaten
     * @param yMax
     * @return
     */
    private double coordinateToPixel (double coordinate, Zeichenfläche z, float yMax){
        double yWert;
        float widthView=z.getBottom()-z.getTop();
        float stepPixels=widthView/(2*yMax);
        coordinate=yMax-coordinate;
        yWert=coordinate*stepPixels;
        return yWert;
    } // Ende coordinateToPixel


    //berechnet x Koordinate
    private double xCoordinateToPixel (double coordinate, Zeichenfläche z, float xMax){
        double xWert;
        float widthView=z.getRight()-z.getLeft();
        // nach wie vielen Pixeln kommt 1 x Wert
        float stepPixels=widthView/(2*xMax);
        coordinate=xMax+coordinate;
        xWert=coordinate*stepPixels;
        return xWert;
    } // Ende coordinateToPixel

    /** Methode compare()
     * vergleicht den mitgegebenen Wert, mit dem Wert an ensprechender Stelle in der Liste
     * in Pixel-Werten
     * @param d  zu vergleichender Wert (berechnet)
     * @param l  Liste, in der Vergleichswerte gespeichert sind
     * @param index Stelle in der Liste, an der Vergleichswert steht
     * @return true falls Vergleich der zwei Werte übereinstimmt
     */
    private boolean compare (double d, Liste l, int index){
        double tolerance=15;
        float compareValue=l.get(index);
        if (d>=compareValue-tolerance && d<=compareValue+tolerance ) return true;
        else return false;
    }//Ende compare

//TODO  Umwandlung von View zu Bitmap

    /** Methode
     * wandelt View in eine Bitmap um
     * @param v View die umgewandelt werden soll
     * @return Bitmap
     */
     Bitmap convertViewToBitmap (View v){
        Bitmap map;
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        return map=v.getDrawingCache();
    }

    boolean comparePoints (Bitmap map, int x, int y){
        int  xVergleichswert=  (int)xCoordinateToPixel(x, z, 10);
        int yVergleichswert=  (int)coordinateToPixel(y, z, 6);
        int pixel=map.getPixel(xVergleichswert, yVergleichswert);
        // Kontrolle
        // t.setText("X-WErt: "+x+" Y-Wert: "+y+" ausgerechnete Pixel x: "+ xVergleichswert+" ausgerechnete Pixel y: "+yVergleichswert+" Farbwert: "+pixel);
        //Wert 0xff000000 entspricht der Farbe Schwarz -16777216
         return  (pixel==-16777216);
    }

}
