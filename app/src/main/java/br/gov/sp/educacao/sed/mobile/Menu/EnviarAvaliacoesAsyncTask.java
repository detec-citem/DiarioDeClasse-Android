package br.gov.sp.educacao.sed.mobile.Menu;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EnviarAvaliacoesAsyncTask
        extends AsyncTask<List<JSONObject>,
                          Void,
                          List<JSONObject>> {

    private String token;

    HomeViewMvcImpl delegate = null;

    EnviarAvaliacoesAsyncTask(String token) {

        this.token = token;
    }

    @Override
    @SafeVarargs
    final public List<JSONObject> doInBackground(List<JSONObject>... params) {

        int avaliacoesSize = params[0].size();

        List<JSONObject> results = new ArrayList<>(avaliacoesSize);

        for(int i = 0; i < avaliacoesSize; i++) {

            JSONObject result = new EnviarAvaliacaoRequest().executeRequest(params[0].get(i), token);

            if(result != null) {

                results.add(result);
            }
        }
        return results;
    }

    @Override
    protected void onPostExecute(List<JSONObject> strings) {

        super.onPostExecute(strings);

        delegate.avaliacoesResultadoSincronizacao(strings);

        delegate.completouEtapaSincronizacao();

        delegate = null;
    }
}
