package br.gov.sp.educacao.sed.mobile.util;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class Analytics {
    //Método
    public static void setTela(Context context, String nomeTela) {
        GoogleAnalytics instance = GoogleAnalytics.getInstance(context);
        Tracker tracker = instance.newTracker("UA-102063893-1");
        tracker.setScreenName(nomeTela);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}