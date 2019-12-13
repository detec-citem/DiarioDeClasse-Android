package br.gov.sp.educacao.sed.mobile.Turmas;

import android.content.Context;
import android.util.Log;
import android.os.AsyncTask;

import org.json.JSONObject;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import br.gov.sp.educacao.sed.mobile.Menu.HomeViewMvcImpl;

import br.gov.sp.educacao.sed.mobile.R;
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

        String json;

        if(token[0].equals("aaa")){

            json = lerArquivoEstatico(R.raw.offline3_test).toString();
        }
        else{

            json = resgatarTurmasRequest.executeRequest(token[0]);
        }

        try {

            Log.e(TAG, "JSON: Processamento Iniciado" );

            JSONObject jsonObject = new JSONObject(json);

            turmaDBsetters.updateTurmas(jsonObject);

            sincronizacaoComSucesso = true;
        }
        catch (JSONException e) {

            CrashAnalytics.e(TAG, e);
            e.printStackTrace();
        }
        return sincronizacaoComSucesso;
    }

    @Override
    protected void onPostExecute(Boolean result) {

        super.onPostExecute(result);

        delegate.terminouSincronizacao(result);

        delegate = null;
    }

    protected StringBuilder lerArquivoEstatico(int fileID) {

        StringBuilder jsonString = new StringBuilder();

        BufferedReader br = null;
        try {
            br = new BufferedReader(

                    new InputStreamReader(delegate.getRootView().getResources().openRawResource(fileID), "UTF-8")
            );

            String line;

            while ((line = br.readLine()) != null) {

                line = line.trim();

                if (line.length() > 0) {

                    jsonString.append(line);
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        return jsonString;
    }
}
