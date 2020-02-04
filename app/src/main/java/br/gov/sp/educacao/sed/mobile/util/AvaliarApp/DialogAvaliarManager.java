package br.gov.sp.educacao.sed.mobile.util.AvaliarApp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class DialogAvaliarManager {

    public static void showRateDialog(Context context, Bundle savedInstanceState) {

        GerenciadorAvaliarApp.atualizarVezesInicioApp(context, savedInstanceState);

        FragmentManager fragmentManager = getFragManager(context);

        if(GerenciadorAvaliarApp.podeMostrarDialog(context) && fragmentManager.findFragmentByTag(DialogAvaliarApp.KEY) == null) {

            DialogAvaliarApp dialog = new DialogAvaliarApp();

            dialog.setCancelable(false);

            dialog.show(fragmentManager, DialogAvaliarApp.KEY);
        }
    }

    static void showRateDialogPlayStore(Context context) {

        FragmentManager fragmentManager = getFragManager(context);

        DialogAvaliarApp dialog = new DialogAvaliarNaPlayStore();

        dialog.setCancelable(false);

        dialog.show(fragmentManager, DialogAvaliarApp.KEY);
    }

    static void showRateDialogFeedback(Context context, float nota) {

        FragmentManager fragmentManager = getFragManager(context);

        DialogEscreverFeedback dialog = new DialogEscreverFeedback();

        dialog.darNota(nota);

        dialog.setCancelable(false);

        dialog.show(fragmentManager, DialogAvaliarApp.KEY);
    }

    private static FragmentManager getFragManager(Context context){

        AppCompatActivity activity = (AppCompatActivity) context;

        return activity.getSupportFragmentManager();
    }
}