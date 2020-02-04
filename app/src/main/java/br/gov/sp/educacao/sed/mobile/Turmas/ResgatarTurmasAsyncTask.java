package br.gov.sp.educacao.sed.mobile.Turmas;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import br.gov.sp.educacao.sed.mobile.Menu.HomeViewMvcImpl;
import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;

public class ResgatarTurmasAsyncTask
        extends AsyncTask<String,
                          Integer,
                          Boolean> {

    private TurmaDBsetters turmaDBsetters;

    public HomeViewMvcImpl delegate = null;

    public String TAG = ResgatarTurmasAsyncTask.class.getSimpleName();

    public ResgatarTurmasAsyncTask(TurmaDBsetters turmaDBsetters) {

        this.turmaDBsetters = turmaDBsetters;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Boolean doInBackground(String... token) {

        ResgatarTurmasRequest resgatarTurmasRequest = new ResgatarTurmasRequest();

        boolean sincronizacaoComSucesso = false;

        String json = resgatarTurmasRequest.executeRequest(token[0]);

        if(json.equals("")){

            return false;
        }

        try {

            Log.e(TAG, "JSON: Processamento Iniciado" );

            JSONObject jsonObject = new JSONObject(json);

            if(!jsonObject.getString("Mensagem").equals("null")){

                return false;
            }

            turmaDBsetters.updateTurmas(jsonObject);

            sincronizacaoComSucesso = true;
        }
        catch (JSONException e) {

            CrashAnalytics.e(TAG, e);
        }
        return sincronizacaoComSucesso;
    }

    @Override
    protected void onPostExecute(Boolean result) {

        super.onPostExecute(result);

        delegate.terminouSincronizacaoTurmas(result);

        delegate = null;
    }
}
