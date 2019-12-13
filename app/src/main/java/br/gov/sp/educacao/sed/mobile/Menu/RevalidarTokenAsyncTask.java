package br.gov.sp.educacao.sed.mobile.Menu;

import android.os.AsyncTask;

import br.gov.sp.educacao.sed.mobile.Carteirinha.RevalidarTokenInterface;

public class RevalidarTokenAsyncTask
        extends AsyncTask<String, Void, String> {

    public RevalidarTokenInterface delegate = null;

    @Override
    protected void onPreExecute() {


    }

    @Override
    public String doInBackground(String... params) {

        return new RevalidarTokenRequest().executeRequest(params[0], //RG
                                                          params[1]); //Senha
    }

    @Override
    protected void onPostExecute(String token) {

        super.onPostExecute(token);

        delegate.revalidacaoDeToken(token);

        delegate = null;
    }
}
