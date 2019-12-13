package br.gov.sp.educacao.sed.mobile.Menu;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

class EnviarFrequenciasAsyncTask
        extends AsyncTask<List<JSONObject>, Void, List<JSONObject>> {

    private String token;

    HomeViewMvcImpl delegate;

    EnviarFrequenciasAsyncTask(String token) {

        this.token = token;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    public List<JSONObject> doInBackground(List<JSONObject>... params) {

        List<JSONObject> listaRespostasFrequencia = new ArrayList<>();

        EnviarFrequenciasRequest enviarFrequenciasRequest = new EnviarFrequenciasRequest();

        List<JSONObject> listaDeFrequenciasPorTurma = params[0];

        for(int i = 0; i < listaDeFrequenciasPorTurma.size(); i++) {

            JSONObject jsonObject = null;

            try {

                jsonObject = enviarFrequenciasRequest.executeRequest(listaDeFrequenciasPorTurma.get(i), token);

                jsonObject.put("Turma", listaDeFrequenciasPorTurma.get(i).get("CodigoTurma"));

                jsonObject.put("Disciplina", listaDeFrequenciasPorTurma.get(i).get("CodigoDisciplina"));

            }
            catch(Exception e) {

                e.printStackTrace();
            }

            if(jsonObject != null) {

                listaRespostasFrequencia.add(jsonObject);
            }
        }

        return listaRespostasFrequencia;
    }

    @Override
    protected void onPostExecute(List<JSONObject> listaRespostasFrequencia) {

        if(listaRespostasFrequencia.size() > 0) {

            delegate.frequenciasResultadoSincronizacao(listaRespostasFrequencia);
        }

        delegate.completouEtapaSincronizacao();

        delegate = null;
    }
}
