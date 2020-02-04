package br.gov.sp.educacao.sed.mobile.util;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

public class AnalyticsApplication extends Application {
    //Constantes
    private static final String PROPERTY_ID = "UA-111388867-2";

    public enum TrackerName {

        APP_TRACKER,
    }

    //Variáveis
    private HashMap<TrackerName, Tracker> trackers = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public synchronized Tracker getTracker(TrackerName trackerId) {
        if(!trackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);

            if(trackerId == TrackerName.APP_TRACKER) {

                Tracker t = analytics.newTracker(PROPERTY_ID);

                trackers.put(trackerId, t);
            }
        }
        return trackers.get(trackerId);
    }
}
