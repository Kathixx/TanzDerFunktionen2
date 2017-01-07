package com.example.arabellaprivat.tanzderfunktionen.checkAndDraw;

import android.content.res.ColorStateList;
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

    /** Array mit den aktuellen Werten der Funktion */
    private double[] parameters;


    /**
     *Constructor
     */
    public Pruefung(Zeichenfläche zf, Hilfspunkte hp){
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


    public int check(int level, double [] fl) {
        double xWert;
        double yWert;
        // index i wird fortlaufend hochgezählt, bis zum Ende der Liste
        int index=0;
        int listLength=listeX.size();
        String s="";
        // Zähler zählt richtige Vergleiche des gezeichneten und berechneten Wert
        int counter=0;

        // aktuelle Werte aus der Klasse Spiele holen
        parameters=fl;
        //Parameter zuweisen
        double a=parameters[0];
        double b=parameters[1];
        double c= parameters[2];
        double d=parameters [3];
        double n1= parameters[4];
        double  n2= parameters[5];
        double t= parameters[6];

        int points=0;
        // Punkte für Nullstellen und Achsenabschnitte berechnen und zum Punktestand dazu zählen
        // pro richtig gezeichnete Nullstelle bzw. Achsenabschnitt gibt es 4 Punkte
        // eine Abstufung gibt es hier nicht, entweder richtig oder falsch
        points+=compareSpecialPoints(n1,0)+compareSpecialPoints(n2,0)+compareSpecialPoints(0,t);

        // Punkte für die "restlichen" Punkte berechnen
        // hängt von der Genauigkeit (Toleranzbereich) ab
        // es gibt drei Abstufungen
        // ist der PUnkt exakt gezeichnet gibt es höchstens 3 Punkte
        while (index < listLength) {
            // x-Wert aus der Liste auslesen
            xWert = (listeX.get(index));
            // in x-Koordinaten-Werte umwandeln
            xWert = pixelToCoordinate(xWert, z, 10);
            // Berechnet den y-Wert, Funktionstyp ist abhängig vom Level
            yWert = calculateYValue(level, xWert, a, b, c, d);
            // y-Koordinaten-Wert in Pixel umrechnen
            yWert = coordinateToPixel(yWert, z, 6);
            // berechneten y-Wert mit y-Wert aus der Liste vergleichen
            // 3 Abstufungen erzeugen in dem Toleranzbereich verändert wird, je ganuer gezeichnet wurde desto mehr PUnkte gibt es
            if (compare(yWert, listeY, index, 10)) points += 3;
            else {
                if (compare(yWert, listeY, index, 20)) points += 2;
                else {
                    if (compare(yWert, listeY, index, 30)) points += 1;
                    else points += 0;
                }
            }
            index++;
        }
        return  points;
    }


    private double calculateYValue (int level,double xWert, double a, double b, double c, double d) {
        double yWert;
        // entsprechenden y-Wert mit der Funktion berechnen , Level x zeigt an welche Funktion aufgerufen werden soll
        switch (level) {
            case 1:
                // allgemeine Funktion a*x+b also zwei Parameter übergeben
                yWert = linearFunction(xWert, a, b);
                break;
            case 2:
                //allgemeine Funktion ax^2+bx+c also drei Parameter übergeben
                yWert = quadratFunction(xWert, a, b, c);
                break;
            case 3:
                // hier nicht allgemeine Funktion, sondern angepasst an unsere Funktion: (3)/(x-2)+1
                // da allgemeine Funktion: rationale Funktion/rationale Funktion
                yWert = rationalFunction(xWert, a, b, c, d);
                break;
            case 4:
                yWert = trigonometricFunction(xWert, a, b, c, d);
                break;
            case 5:
                yWert = logarithmicFunction(xWert, a, b, c, d);
                break;
            default:
                yWert = 0;
        }
        return yWert;
    }//End calculateYValue


    /**
     * lineare Fuktion allgemein: f(x)= m*x+t= a*x+b
     * lineare Funktion: f(x)=0,5x+2
     * hier könnte man eventuell vorgegeben Werte auch aus Datenbank auslesen?
     * @param x zu berechneter x-Wert in Koordinaten-Angaben
     * @return yWert berechneter y-Wert
     */
    private double linearFunction (double x, double  a, double b){
        double yWert=(a*x)+b;
        return  round(yWert);
    }// Ende linearFunction()

    /**
     * allgemein: a*x^2+bx+c
     * quadratische Funktion: 0.25*x^2+1x-4
     */
    private double quadratFunction (double x, double a, double b, double c){
        double yWert=(a*x*x)+b*x+c;
        return round (yWert);
    }

    /**
     * allgemein: rationale Funktion /rationale funktion
     * hier auf unsere Funktion angepasst
     * gebrochenrationale Funktion: (3)/(x-2)+1
     *
     */
    private double rationalFunction (double x, double a, double b, double c, double d) {
        double yWert = a/(b*x+c)+d;
        return round(yWert);
    }

    /** allgemeine Funktion: a*sin(bx+x)+d
     * trigonometrische Funktion: 3cos(x+1)
     * Cosinus wird erreicht, indem in die Datenbank der Wert des sinus um 90 reduziert wird
     */
    private double trigonometricFunction (double x, double a, double b, double c, double d){
        double yWert = a*Math.sin(b*x+c+0.5*3.14)+d;
        return round (yWert);
    }

    /**
     * allgemeine Funktion: a*ln(bx+c)+d
     * logharitmische Funktionen: ln(x+5)
     * TODO welche anderen logharitmischen FUnktion gibt es noch? wie könnte man die miteinander vereinbaren?
     */
    private double logarithmicFunction (double x, double a, double b, double c, double d){
        double yWert= a*Math.log(b*x+c)+d;

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
        double widthView=z.getRight()-z.getLeft();
        // Länge von x=0 bis x=xMax
        double halfView=widthView/2;
        // LängenMaß einer Koordinate
        double stepPixels= widthView/(2*xMax);
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
    private double coordinateToPixel (double coordinate, Zeichenfläche z, double yMax){
        double yWert;
        double widthView=z.getBottom()-z.getTop();
        double stepPixels=widthView/(2*yMax);
        coordinate=yMax-coordinate;
        yWert=coordinate*stepPixels;
        return yWert;
    } // Ende coordinateToPixel


    //berechnet x Koordinate
    private double xCoordinateToPixel (double coordinate, Zeichenfläche z, double xMax){
        double xWert;
        double widthView=z.getRight()-z.getLeft();
        // nach wie vielen Pixeln kommt 1 x Wert
        double stepPixels=widthView/(2*xMax);
        coordinate=xMax+coordinate;
        xWert=coordinate*stepPixels;
        return xWert;
    } // Ende coordinateToPixel

    /** Methode compare()
     * vergleicht den mitgegebenen Wert, mit dem Wert an ensprechender Stelle in der Liste
     * in Pixel-Werten
     * @param f  zu vergleichender Wert (berechnet)
     * @param l  Liste, in der Vergleichswerte gespeichert sind
     * @param index Stelle in der Liste, an der Vergleichswert steht
     * @return true falls Vergleich der zwei Werte übereinstimmt
     */
    private boolean compare (double f, Liste l, int index, double tolerance){
        double compareValue=l.get(index);
        return (f>=compareValue-tolerance && f<=compareValue+tolerance );
    }//Ende compare


    /** Methode
     * wandelt View inklusive gezeichneten Graph in eine Bitmap um
     * Koordinatensystem ist dabei nicht enthalten, da Koordinatensystem als Hintergrund des Hilfspunkt-Views hinterlegt ist!
     * d.h. Bitmap besteht aus lediglich weißen/transparenten Hintergrund und einer/mehrerer schwarzer Linien
     * @param v View die umgewandelt werden soll
     * @return Bitmap
     */
     Bitmap convertViewToBitmap (View v){
        Bitmap map;
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        return map=v.getDrawingCache();
    }

    /** Methode die zwei Punkte zwischen dem gezichneten Pfad als Bitmap und dem original Pfad vergleicht
     * gezeichneter Pfad wird dabei als Bitmap umgewandelt
     * @param map Bitmap die aus dem gezeichneten Pfad entstanden ist
     * @param x  x-Wert in Koordinatenangaben an dem überpüft werden soll
     * @param y  y-Wert in Koordinatenangaben an dem überpüft werden soll
     * @return blackPixel true wenn Pixel an gewünschter STelle schwarz ist, also der gezeichnete Graph über gewünschte Stelle verläuft
     */
    boolean compareBitmapPoints (Bitmap map, double x, double y){
        boolean blackPixel=false;
        double tolerance=5;
        // berechnete bzw. in der Datenbank stehende Koordinatenwerte werden zum weiteren Vergleich in Pixelwerte umgerechnet
        double  xVergleichswert=  xCoordinateToPixel(x, z, 10);
        double yVergleichswert=  coordinateToPixel(y, z, 6);

        //Kontrolle
        //String s="X-WErt: "+x+" Y-Wert: "+y+" ausgerechnete Pixel x: "+ xVergleichswert+" ausgerechnete Pixel y: "+yVergleichswert;

        // Toleranzbereich für Kontrollzone
        // d.h. nicht nur an exakt berechneten Pixelwert wird kontrolliert ob der Pfad gezeichnet wurde, also die Pixel schwarz sind
        // sondern auch im nahen Bereich um den genauen Pixelwert herum
        for (double i=xVergleichswert-tolerance; i<=xVergleichswert+tolerance; i++){
            for (double j= yVergleichswert-tolerance; j<=yVergleichswert+tolerance; j++){
                // der Farbwert, der die Bitmap an den zuvor berechneten Pixelwerten hat, wird ausgelesen
                int pixel=map.getPixel((int)i, (int)j);
                // Überprüfung, ob an der gewünschten Stelle die Pixel schwarz sind, d.h. dort wurde gezeichnet
                //Wert 0xff000000 entspricht der Farbe Schwarz -16777216
                if (pixel==-16777216) blackPixel=true;
                //Kontrolle
                //s+=blackPixel;
            }
        }
        // Kontrolle
         //t.setText(s);
       return  blackPixel;
    }//End compareBitmapPoints


    /** Methode die Nullstellen und Achsenabschnitt überprüft
     *
     * @param x  x-Wert in Koordinatenangaben der verglichen werden soll
     * @param y  y-Wert in Koordinatenangaben der verglichen werden soll
     * @return true, falls Vergleich richtig ist
     */
    private int compareSpecialPoints(double x, double y) {
        // wenn es beispielsweise nur eine Nullstelle gibt, steht in der Liste der Wert 99 drin, dann soll dies automatisch auf true also ungewertet bleiben
        if (x == 99 || y == 99) return 4;
            // übergeben wird die Zeichenfläche, die zuvor noch zu einer Bitmap umgewandelt wird und die zwei Koordinatenwerte
        else {
            if (compareBitmapPoints(convertViewToBitmap(z), x, y)) return 4;
            else return 0;
        }
    }// Ende compareSpecialPoints
}
