package br.gov.sp.educacao.sed.mobile.util;

import java.util.HashMap;

import android.app.Application;

import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.GoogleAnalytics;

public class AnalyticsApplication
        extends Application {

    private static final String PROPERTY_ID = "UA-111388867-2";

    public enum TrackerName {

        APP_TRACKER,
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public AnalyticsApplication() {

        super();
    }

    public synchronized Tracker getTracker(TrackerName trackerId) {

        if(!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);

            if(trackerId == TrackerName.APP_TRACKER) {

                Tracker t = analytics.newTracker(PROPERTY_ID);

                mTrackers.put(trackerId, t);
            }
        }
        return mTrackers.get(trackerId);
    }
}
