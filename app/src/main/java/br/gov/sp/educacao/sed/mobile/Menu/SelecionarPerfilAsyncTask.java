package br.gov.sp.educacao.sed.mobile.Menu;

import android.os.AsyncTask;

import br.gov.sp.educacao.sed.mobile.Carteirinha.RevalidarTokenInterface;

public class SelecionarPerfilAsyncTask
        extends AsyncTask<String, Void, Boolean> {

    public RevalidarTokenInterface delegate = null;

    @Override
    protected void onPreExecute() {


    }

    @Override
    public Boolean doInBackground(String... params) {

        return new SelecionarPerfilRequest().executeRequest(params[0]);
    }

    @Override
    protected void onPostExecute(Boolean perfilOk) {

        super.onPostExecute(perfilOk);

        delegate.perfilSelecionado(perfilOk);

        delegate = null;
    }
}
