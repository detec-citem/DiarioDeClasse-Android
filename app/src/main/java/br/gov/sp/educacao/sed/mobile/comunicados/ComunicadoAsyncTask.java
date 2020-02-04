package br.gov.sp.educacao.sed.mobile.comunicados;

import android.os.AsyncTask;

import org.json.JSONArray;

import br.gov.sp.educacao.sed.mobile.Menu.HomeActivity;

public class ComunicadoAsyncTask extends AsyncTask<String, Void, JSONArray> {

    public HomeActivity delegate = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONArray doInBackground(String... strings) {

        String token = strings[0];

        ComunicadoRequest comunicadoRequest = new ComunicadoRequest();

        return comunicadoRequest.executeRequest(token, delegate.getApplicationContext());
    }

    @Override
    protected void onPostExecute(JSONArray jsonObject) {
        super.onPostExecute(jsonObject);

        delegate.terminouRequisicaoRecado(jsonObject);

        delegate = null;
    }
}
