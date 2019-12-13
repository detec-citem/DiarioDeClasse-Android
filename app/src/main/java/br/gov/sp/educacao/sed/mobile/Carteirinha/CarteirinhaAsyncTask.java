package br.gov.sp.educacao.sed.mobile.Carteirinha;

import android.os.AsyncTask;

public class CarteirinhaAsyncTask
        extends AsyncTask<String, Void, String> {

    public ListaCarteirinhasViewMvcImpl delegate = null;

    @Override
    protected void onPreExecute() {

    }

    @Override
    public String doInBackground(String... params) {

        return new CarteirinhaRequest().executeRequest(params[0], params[1]); //CPF e Token
    }

    @Override
    protected void onPostExecute(String respostaJsonCarteirinha) {

        super.onPostExecute(respostaJsonCarteirinha);

        delegate.dadosRecebidosCarteirinhas(respostaJsonCarteirinha);

        delegate = null;
    }
}
