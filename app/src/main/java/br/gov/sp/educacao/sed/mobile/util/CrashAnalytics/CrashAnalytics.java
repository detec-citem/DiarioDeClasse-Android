package br.gov.sp.educacao.sed.mobile.util.CrashAnalytics;

import android.util.Log;

public class CrashAnalytics {

    public static void e(String tag, Throwable e) {

        Log.e(tag, e.toString(), e);
    }
}