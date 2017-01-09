package com.example.arabellaprivat.tanzderfunktionen.activities;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Arabella.Privat on 08.01.2017.
 * Quelle: https://coderwall.com/p/qxxmaa/android-use-a-custom-font-everywhere
 */

public class FontChangeCrawler {
    private Typeface typeface;

    public FontChangeCrawler(Typeface typeface) {
        this.typeface = typeface;
    }

    public FontChangeCrawler(AssetManager assets, String assetsFontFileName) {
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
