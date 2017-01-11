package com.example.arabellaprivat.tanzderfunktionen.checkAndDraw;

import android.graphics.Bitmap;
import android.view.View;

/**
 * Created by Kathi on 09.12.2016.
 * enthält alle Methoden, die für die Prüfung der gezeichneten Funktion mit der original Funktion notwendig sind
 * dadurch wird v.a. die KLasse "Levels" übersichtlicher
 */

public class Check {
     // IV

    /** Liste, die die übergebenen x-Werte des Pfades abspeichert */
    private FloatList listX;
    /** Liste, die die übergebenen y-Werte des Pfades abspeichert */
    private FloatList listY;
    /** Zeichenfläche */
    private TouchViewGraph tvg;
    /** Hilfspunkte */
    private TouchViewDots tvd;




    /**
     *Constructor erstellt eine neue Prüfung
     * weißt den Instanzvariablen den übergeben Parametern zu
     */
    public Check(TouchViewGraph tvg, TouchViewDots tvd){
        this.tvg=tvg;
        this.tvd=tvd;
        // von der KLasse TouchVieGraph die Listen mit den gespeicherten x- und y-Werten holen
        listX=tvg.getListX();
        listY=tvg.getListY();
    }

    // Methoden
    /**
     * vergleicht den gezeichneten Pfad mit der jeweils vorgegebenen Funktion
     * @param level gibt das aktuelle level an
     * @param parameters wird von der Klasse Levels übergeben, beinhalten alle Parameter die aus der Datenbank gelesen wurden und für die Berechnung notwendig sind
     * @return true falls gezeichnete Funktion der Original-Funktion entspricht
    */

    public int checkFunction(int level, double [] parameters) {
        // xWert der Funktion
        double xValue;
        // yWert der Funktion
        double yValue;
        // index i wird fortlaufend hochgezählt, bis zum Ende der Liste
        int index=0;
        int listLength=listX.size();

        //Parameter zuweisen
        double a=parameters[0];
        double b=parameters[1];
        double c= parameters[2];
        double d=parameters [3];
        // Nullstellen zuweisen
        double n1= parameters[4];
        double n2= parameters[5];
        // Achsenabschnitt zuweisen
        double t= parameters[6];

        // Variable als anzeige für die maximale Punktzahl, da diese je nach Anzahl der Nullstellen variiert
        int maxPoints=41;

        // Variable für die erreichten Punkte bei dieser Funktion
        int points=0;

        /*Nullstellen und Achsenabschnitt überprüfen
         * pro richtig gezeichnete Nullstelle bzw. Achsenabschnitt gibt es 4 Punkte
         * eine Abstufung gibt es hier nicht, entweder richtig oder falsch */
        points+=compareSpecialPoints(n1,0)+compareSpecialPoints(0,t);
        // nur wenn die Funktion einen zweiten Nullpunkt hat, soll dieser überprüft werden
        if (n2!=99) {
            // die maximal zu erreichende Punktzahl in diesem Level erhöht sich dann auf 45
            maxPoints=45;
            points+=compareSpecialPoints(n2,0);
        }

        /* Punkte für die "restlichen" Punkte berechnen
         * hängt von der Genauigkeit (Toleranzbereich) ab
         * es gibt drei Abstufungen
         * ist der PUnkt exakt gezeichnet gibt es höchstens 3 Punkte*/
        while (index < listLength) {
            // x-Wert aus der Liste auslesen
            xValue = (listX.get(index));
            // in x-Koordinaten-Werte umwandeln
            xValue = pixelToCoordinate(xValue, 10);
            // Berechnet den y-Wert, Funktionstyp ist abhängig vom Level
            yValue = calculateYValue(level, xValue, a, b, c, d);
            // y-Koordinaten-Wert in Pixel umrechnen
            yValue = yCoordinateToPixel(yValue, 6);
            // berechneten y-Wert mit y-Wert aus der Liste vergleichen
            // 3 Abstufungen erzeugen in dem Toleranzbereich verändert wird, je ganuer gezeichnet wurde desto mehr PUnkte gibt es
            if (compare(yValue, listY, index, 10)) points += 3;
            else {
                if (compare(yValue, listY, index, 20)) points += 2;
                else {
                    if (compare(yValue, listY, index, 30)) points += 1;
                    else points += 0;
                }
            }
            index++;
        }
        return  pointsInPercent(maxPoints,points);
    }


    /** Methode die Nullstellen und Achsenabschnitt überprüft
     * @param x  x-Wert in Koordinatenangaben der verglichen werden soll
     * @param y  y-Wert in Koordinatenangaben der verglichen werden soll
     * @return true, falls Vergleich richtig ist
     */
    private int compareSpecialPoints(double x, double y) {
        if (compareBitmapPoints(convertViewToBitmap(tvg), x, y)) return 4;
        else return 0;
    }// Ende compareSpecialPoints




    /** Methode die zwei Punkte zwischen dem gezichneten Pfad als Bitmap und dem original Pfad vergleicht
     * gezeichneter Pfad wird dabei als Bitmap umgewandelt
     * @param map Bitmap die aus dem gezeichneten Pfad entstanden ist
     * @param x  x-Wert in Koordinatenangaben an dem überpüft werden soll
     * @param y  y-Wert in Koordinatenangaben an dem überpüft werden soll
     * @return blackPixel true wenn Pixel an gewünschter STelle schwarz ist, also der gezeichnete Graph über gewünschte Stelle verläuft
     */
    private boolean compareBitmapPoints (Bitmap map, double x, double y){
        boolean blackPixel=false;
        double tolerance=5;
        // berechnete bzw. in der Datenbank stehende Koordinatenwerte werden zum weiteren Vergleich in Pixelwerte umgerechnet
        double  xCompareValue=  xCoordinateToPixel(x, 10);
        double yCompareValue=  yCoordinateToPixel(y,  6);
        /* Toleranzbereich für Kontrollzone
         * d.h. nicht nur an exakt berechneten Pixelwert wird kontrolliert ob der Pfad gezeichnet wurde, also die Pixel schwarz sind
         * sondern auch im nahen Bereich um den genauen Pixelwert herum */
        for (double i=xCompareValue-tolerance; i<=xCompareValue+tolerance; i++){
            for (double j= yCompareValue-tolerance; j<=yCompareValue+tolerance; j++){
                // der Farbwert, der die Bitmap an den zuvor berechneten Pixelwerten hat, wird ausgelesen
                int pixel=map.getPixel((int)i, (int)j);
                // Überprüfung, ob an der gewünschten Stelle die Pixel schwarz sind, d.h. dort wurde gezeichnet
                //Wert 0xff000000 entspricht der Farbe Schwarz -16777216
                if (pixel==-16777216) blackPixel=true;
                //Kontrolle
                //s+=blackPixel;
            }
        }
        return  blackPixel;
    }//End compareBitmapPoints


    //berechnet x Koordinate
    private double xCoordinateToPixel (double coordinate,  double xMax){
        double xValue;
        double widthView=tvg.getRight()-tvg.getLeft();
        // nach wie vielen Pixeln kommt 1 x Wert
        double stepPixels=widthView/(2*xMax);
        coordinate=xMax+coordinate;
        xValue=coordinate*stepPixels;
        return xValue;
    } // Ende coordinateToPixel


    /** Methode
     * wandelt View inklusive gezeichneten Graph in eine Bitmap um
     * Koordinatensystem ist dabei nicht enthalten, da Koordinatensystem als Hintergrund des Hilfspunkt-Views hinterlegt ist!
     * d.h. Bitmap besteht aus lediglich weißen/transparenten Hintergrund und einer/mehrerer schwarzer Linien
     * @param v View die umgewandelt werden soll
     * @return Bitmap
     */
    private Bitmap convertViewToBitmap (View v){
        Bitmap map;
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        return map=v.getDrawingCache();
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
    private double pixelToCoordinate (double pixel, int xMax){
        double xWert;
        // Liest die maximalen Pixelwerte des Views zurück
        //float maxXPixel = v. getRight-getLeft??
        double widthView=tvg.getRight()-tvg.getLeft();
        // Länge von x=0 bis x=xMax
        double halfView=widthView/2;
        // LängenMaß einer Koordinate
        double stepPixels= widthView/(2*xMax);
        xWert=(pixel-halfView)/stepPixels;
        return  round(xWert);
    }// Ende pixelToCoordinate


    private double calculateYValue (int level,double xValue, double a, double b, double c, double d) {
        double yValue;
        // entsprechenden y-Wert mit der Funktion berechnen , Level x zeigt an welche Funktion aufgerufen werden soll
        switch (level) {
            case 1:
                // allgemeine Funktion a*x+b also zwei Parameter übergeben
                yValue = linearFunction(xValue, a, b);
                break;
            case 2:
                //allgemeine Funktion ax^2+bx+c also drei Parameter übergeben
                yValue = quadratFunction(xValue, a, b, c);
                break;
            case 3:
                // hier nicht allgemeine Funktion, sondern angepasst an unsere Funktion: (3)/(x-2)+1
                // da allgemeine Funktion: rationale Funktion/rationale Funktion
                yValue = rationalFunction(xValue, a, b, c, d);
                break;
            case 4:
                yValue = trigonometricFunction(xValue, a, b, c, d);
                break;
            case 5:
                yValue = logarithmicFunction(xValue, a, b, c, d);
                break;
            default:
                yValue = 0;
        }
        return yValue;
    }//End calculateYValue

    /**
     * rechnet einen Koordinaten-Wert in Pixelwert um
     * v.a. für y-Werte
     * abhängig von den maximalen y-Werten
     * Vorraussetzung: Koordinatensystem ist zentriert
     * @param coordinate  berechnete y-Koordinaten
     * @param yMax
     * @return
     */
    private double yCoordinateToPixel (double coordinate, double yMax){
        double yValue;
        double widthView=tvg.getBottom()-tvg.getTop();
        double stepPixels=widthView/(2*yMax);
        coordinate=yMax-coordinate;
        yValue=coordinate*stepPixels;
        return yValue;
    } // Ende coordinateToPixel


    /**
     * lineare Fuktion allgemein: f(x)= m*x+t= a*x+b
     * lineare Funktion: f(x)=0,5x+2
     * hier könnte man eventuell vorgegeben Werte auch aus Datenbank auslesen?
     * @param x  aktueller xWert der überprüft werden soll
     * @param a  Parameter a
     * @param b  Parameter b
     * @return berechnten yWert
     */
    private double linearFunction (double x, double  a, double b){
        double yWert=(a*x)+b;
        return  round(yWert);
    }// Ende linearFunction()

    /**
     * allgemein: a*x^2+bx+c
     * quadratische Funktion: 0.25*x^2+1x-4
     * @param x  aktueller xWert der überprüft werden soll
     * @param a  Parameter a
     * @param b  Parameter b
     * @param c  Paramber c
     * @return berechnten yWert
     */
    private double quadratFunction (double x, double a, double b, double c) {
        double yWert = (a * x * x) + b * x + c;
        return round(yWert);
    }// Ende quadratFunction

    /**
     * allgemein: rationale Funktion /rationale funktion
     * hier auf unsere Funktion angepasst
     * gebrochenrationale Funktion: (3)/(x-2)+1
     * @param x  aktueller xWert der überprüft werden soll
     * @param a  Parameter a
     * @param b  Parameter b
     * @param c  Paramber c
     * @param d  Paramber d
     * @return berechnten yWert
     */
    private double rationalFunction (double x, double a, double b, double c, double d) {
        double yWert = a/(b*x+c)+d;
        return round(yWert);
    }// Ende rationalFunction

    /** allgemeine Funktion: a*sin(bx+x)+d
     * trigonometrische Funktion: 3cos(x+1)
     * Cosinus wird erreicht, indem in die Datenbank der Wert des sinus um 90 reduziert wird
     * @param x  aktueller xWert der überprüft werden soll
     * @param a  Parameter a
     * @param b  Parameter b
     * @param c  Paramber c
     * @param d  Paramber d
     * @return berechnten yWert
     */
    private double trigonometricFunction (double x, double a, double b, double c, double d){
        double yWert = a*Math.sin(b*x+c+0.5*3.14)+d;
        return round (yWert);
    }// Ende trigonometricFunction

    /**
     * allgemeine Funktion: a*ln(bx+c)+d
     * natürliche Logarithmusfunktionen: ln(x+5)
     * @param x  aktueller xWert der überprüft werden soll
     * @param a  Parameter a
     * @param b  Parameter b
     * @param c  Paramber c
     * @param d  Paramber d
     * @return berechnten yWert
     */
    private double logarithmicFunction (double x, double a, double b, double c, double d){
        double yWert= a*Math.log(b*x+c)+d;
        return round (yWert);
    }// Ende logarithmicFunction



    /** Methode compare()
     * vergleicht den mitgegebenen Wert, mit dem Wert an ensprechender Stelle in der Liste
     * in Pixel-Werten
     * @param f  zu vergleichender Wert (berechnet)
     * @param l  Liste, in der Vergleichswerte gespeichert sind
     * @param index Stelle in der Liste, an der Vergleichswert steht
     * @return true falls Vergleich der zwei Werte übereinstimmt
     */
    private boolean compare (double f, FloatList l, int index, double tolerance){
        double compareValue=l.get(index);
        return (f>=compareValue-tolerance && f<=compareValue+tolerance );
    }//Ende compare


    /** berechnet die Porzentpunkte aus erreichten Punkten und maximal zu erreichender Punktzahl
     * @param maxPoints maximal mögliche Punktzahl
     * @param points  erreichte Punkte
     * @return gerundete Prozentpunkte
     */
    private int pointsInPercent (int maxPoints, int points){
        return Math.round((points*100/maxPoints));
    }// Ende pointsInPercent


    /** Methode round
     * rundet einen Double-Wert auf 2 Dezimalstellen ab
     * @param d zu rundender Double-Wert
     * @return gerundeter Double-Wert
     */
    private double round (double d){
        double rounded=Math.round(d*100);
        return rounded/100;
    }//Ende round


    /** Überprüfung ob Graph innerhalb des vorgegebenen Intervalls gezeichnet wurde
     * wichtig für das entsprechende Pop-Up Window
     * @param parameters  wird von der Klasse Levels übergeben, beinhalten alle Parameter die aus der Datenbank gelesen wurden und für die Berechnung notwendig sind
     * @return true falls Start- und Endwert auserhalb des Intervalls bzw. auf der Intervallgrenze liegen, ist die gesamte Funktion innerhalb des Intervalls gezeichnet worden
     */
    public boolean pathIsInIntervall (double [] parameters){
        // Das Intervall ist in der parameterliste in den letzten Zwei Stellen gespeichert
        // untere und obere Intervallgrenze auslesen
        double iMin =parameters[7];
        double iMax= parameters[8];
        // Start- und Endwert des gezeichneten Pfades auslesen und in Koordinaten umwandeln
        double start= pixelToCoordinate(listX.get(0), 10);
        double end= pixelToCoordinate(listX.get(listX.size()-1),10);
        return (start<=iMin && iMax<=end);
    }// Ende pathIsInIntervall


    /** Überprüft ob Zeichenfläche leer ist,
     * indem kontrolliert wird ob Werte in den Speicherlisten stehen
     * @return true falls nichts gezeichnet wurde
     */
    public boolean pathIsEmpty(){
        return listX.isEmpty()&& listY.isEmpty();
    }
}
