package br.gov.sp.educacao.sed.mobile.util.AvaliarApp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

class GerenciadorAvaliarApp {

    private static final int INICIO_APP = 7;

    private static final String TEMPO_KEY = "time_key";

    private static final String NAO_PERGUNTAR_NOVAMENTE_KEY = "never_ask_key";

    private static final int ESPERA_DIAS = 7 * (24 * 60 * 60 * 1000);

    private static final String INICIO_APP_KEY = "launch_times_key";

    private static SharedPreferences getSP(Context context) {

        return context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
    }

    static void naoPerguntarNovamente(Context context) {

        SharedPreferences sharedPreferences = getSP(context);

        sharedPreferences.edit().putBoolean(NAO_PERGUNTAR_NOVAMENTE_KEY, true).apply();
    }

    private static boolean isSelecionadoNaoPerguntarNovamente(Context context) {

        SharedPreferences sharedPreferences = getSP(context);

        return sharedPreferences.getBoolean(NAO_PERGUNTAR_NOVAMENTE_KEY, false);
    }

    static void atualizarTempo(Context context) {

        SharedPreferences sharedPreferences = getSP(context);

        sharedPreferences.edit().putLong(TEMPO_KEY, System.currentTimeMillis() + ESPERA_DIAS).apply();
    }

    private static boolean isTempoValido(Context context) {

        SharedPreferences sharedPreferences = getSP(context);

        Long tempo = sharedPreferences.getLong(TEMPO_KEY, 0);

        if(tempo == 0) {

            atualizarTempo(context);

            tempo = sharedPreferences.getLong(TEMPO_KEY, 0 );
        }

        return tempo < System.currentTimeMillis();
    }

    static void atualizarVezesInicioApp(Context c) {

        SharedPreferences sp = getSP(c);

        sp.edit().putInt(INICIO_APP_KEY, 0).apply();
    }

    static void atualizarVezesInicioApp(Context context, Bundle savedInstanceState ) {

        if(savedInstanceState != null) {

            return;
        }

        SharedPreferences sharedPreferences = getSP(context);

        int vezesInicioApp = sharedPreferences.getInt(INICIO_APP_KEY, 0);

        sharedPreferences.edit().putInt(INICIO_APP_KEY, vezesInicioApp + 1).apply();
    }

    private static boolean isVezesDeInicioAppValido(Context context) {

        SharedPreferences sharedPreferences = getSP(context);

        int vezesInicioApp = sharedPreferences.getInt(INICIO_APP_KEY, 0);

        return vezesInicioApp > 0 && vezesInicioApp % INICIO_APP == 0;
    }

    static boolean podeMostrarDialog(Context context) {

        return !isSelecionadoNaoPerguntarNovamente(context) && (isTempoValido(context) || isVezesDeInicioAppValido(context));
    }
}
