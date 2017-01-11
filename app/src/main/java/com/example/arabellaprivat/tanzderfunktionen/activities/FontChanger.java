package com.example.arabellaprivat.tanzderfunktionen.activities;

/**
 * Created by Kathi on 09.01.2017.
 * Quelle: https://coderwall.com/p/qxxmaa/android-use-a-cust
 */

        import android.content.res.AssetManager;
        import android.graphics.Typeface;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;


public class FontChanger {
    private Typeface typeface;

    public FontChanger(Typeface typeface) {
        this.typeface = typeface;
    }

    public FontChanger(AssetManager assets, String assetsFontFileName) {
        typeface = Typeface.createFromAsset(assets, assetsFontFileName);
    }

    public void replaceFonts(ViewGroup viewTree) {
        View child;
        for(int i = 0; i < viewTree.getChildCount(); ++i) {
            child = viewTree.getChildAt(i);
            if(child instanceof ViewGroup) {
                // recursive call
                replaceFonts((ViewGroup)child);
            }
            else if(child instanceof TextView) {
                // base case
                ((TextView) child).setTypeface(typeface);
            }
        }
    }
}