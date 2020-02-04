package br.gov.sp.educacao.sed.mobile.Menu;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

public class VersaoAppAsyncTask extends AsyncTask<String, Void, Integer> {
    //Variáveis
    private WeakReference<Context> contextWeakRef;
    private WeakReference<HomeViewMvc.Listener> delegateWeakRef;

    //Constructor
    public VersaoAppAsyncTask(HomeViewMvc.Listener delegate, Context context) {
        contextWeakRef = new WeakReference<>(context);
        delegateWeakRef = new WeakReference<>(delegate);
    }

    //AsyncTask
    @Override
    protected Integer doInBackground(String... strings) {
        String token = strings[0];
        Context context = contextWeakRef.get();
        return VersaoAppRequest.buscarVersaoDoApp(token, context);
    }

    @Override
    protected void onPostExecute(Integer versaoApp) {
        super.onPostExecute(versaoApp);
        HomeViewMvc.Listener listener = delegateWeakRef.get();
        if (listener != null) {
            listener.terminouRequisicaoVersaoDoApp(versaoApp);
        }
    }
}