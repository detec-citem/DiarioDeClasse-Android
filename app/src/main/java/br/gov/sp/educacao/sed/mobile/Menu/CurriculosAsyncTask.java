package br.gov.sp.educacao.sed.mobile.Menu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;
import br.gov.sp.educacao.sed.mobile.constants.EtapasSincronizacao;
import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;

public class CurriculosAsyncTask extends AsyncTask<String, Void, Boolean> {
    //Constantes
    private final String TAG = "CurriculosAsyncTask";

    //Variáveis
    private Banco banco;
    private List<TurmaGrupo> turmas;
    private WeakReference<Context> contextWeakRef;
    private WeakReference<HomeViewMvc.Listener> delegateWeakRef;

    //Construtor
    public CurriculosAsyncTask(Banco banco, List<TurmaGrupo> turmas, Context context, HomeViewMvc.Listener delegate) {
        this.banco = banco;
        this.turmas = turmas;
        this.contextWeakRef = new WeakReference<>(context);
        this.delegateWeakRef = new WeakReference<>(delegate);
    }

    //AsyncTask
    @Override
    protected Boolean doInBackground(String... strings) {
        int i;
        int j;
        String token = strings[0];
        Context context = contextWeakRef.get();
        try {
            JSONObject curriculosJson = CurriculosRequest.buscarCurriculos(token, turmas, context);
            SQLiteDatabase database = banco.get();
            database.beginTransaction();
            if (curriculosJson.has("CurriculosEnsinoMedio") && !curriculosJson.isNull("CurriculosEnsinoMedio")) {
                JSONArray curriculosEnsinoMedioJsonArray = curriculosJson.getJSONArray("CurriculosEnsinoMedio");
                if (curriculosEnsinoMedioJsonArray.length() > 0) {
                    int k;
                    int l;
                    int numeroCurriculosEnsinoMedio = curriculosEnsinoMedioJsonArray.length();
                    for(i = 0; i < numeroCurriculosEnsinoMedio; i++) {
                        JSONObject curriculoEnsinoMedioJson = curriculosEnsinoMedioJsonArray.getJSONObject(i);
                        if (curriculoEnsinoMedioJson.has("Grupo") && !curriculoEnsinoMedioJson.isNull("Grupo")) {
                            JSONObject grupoJson = curriculoEnsinoMedioJson.getJSONObject("Grupo");
                            if (grupoJson.has("Codigo") && !grupoJson.isNull("Codigo")) {
                                int codigoGrupo = grupoJson.getInt("Codigo");
                                GrupoDao.inserirGrupo(codigoGrupo, grupoJson, database);
                                if (grupoJson.has("Curriculos") && !grupoJson.isNull("Curriculos")) {
                                    JSONArray grupoCurriculosJsonArray = grupoJson.getJSONArray("Curriculos");
                                    int numeroGrupoCurriculos = grupoCurriculosJsonArray.length();
                                    if (numeroGrupoCurriculos > 0) {
                                        for (j = 0; j < numeroGrupoCurriculos; j++) {
                                            JSONObject grupoCurriculoJson = grupoCurriculosJsonArray.getJSONObject(j);
                                            if (grupoCurriculoJson.has("Codigo") && !grupoCurriculoJson.isNull("Codigo")) {
                                                int codigoCurriculo = grupoCurriculoJson.getInt("Codigo");
                                                CurriculoDao.inserirCurriculo(codigoCurriculo, codigoGrupo, grupoCurriculoJson, database);
                                                if (grupoCurriculoJson.has("Conteudos") && !grupoCurriculoJson.isNull("Conteudos")) {
                                                    JSONArray conteudosJsonArray = grupoCurriculoJson.getJSONArray("Conteudos");
                                                    int numeroConteudos = conteudosJsonArray.length();
                                                    if (numeroConteudos > 0) {
                                                        for (k = 0; k < numeroConteudos; k++) {
                                                            JSONObject conteudoJson = conteudosJsonArray.getJSONObject(k);
                                                            if (conteudoJson.has("Codigo")&& !conteudoJson.isNull("Codigo")) {
                                                                int codigoConteudo = conteudoJson.getInt("Codigo");
                                                                ConteudoDao.inserirConteudo(codigoConteudo, codigoCurriculo, conteudoJson, database);
                                                                if (conteudoJson.has("Habilidades") && !conteudoJson.isNull("Habilidades")) {
                                                                    JSONArray habilidadesJsonArray = conteudoJson.getJSONArray("Habilidades");
                                                                    int numeroHabilidades = habilidadesJsonArray.length();
                                                                    if (numeroHabilidades > 0) {
                                                                        for (l = 0; l < numeroHabilidades; l++) {
                                                                            JSONObject habilidadeJson = habilidadesJsonArray.getJSONObject(l);
                                                                            HabilidadeDao.inserirHabilidade(codigoConteudo, habilidadeJson, database);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (curriculosJson.has("CurriculoEnsinoFundamental") && !curriculosJson.isNull("CurriculoEnsinoFundamental")) {
                JSONArray curriculosEnsinoFundamentalJsonArray = curriculosJson.getJSONArray("CurriculoEnsinoFundamental");
                int numeroCurriculosEnsinoFundamental = curriculosEnsinoFundamentalJsonArray.length();
                if (numeroCurriculosEnsinoFundamental > 0) {
                    for (i = 0; i < numeroCurriculosEnsinoFundamental; i++) {
                        JSONObject curriculoEnsinoFundamentalJson = curriculosEnsinoFundamentalJsonArray.getJSONObject(i);
                        int curriculoId = CurriculoFundamentalDao.inserirCurriculoFundamental(curriculoEnsinoFundamentalJson, database);
                        if (curriculoId != -1 && curriculoEnsinoFundamentalJson.has("Conteudos") && !curriculoEnsinoFundamentalJson.isNull("Conteudos")) {
                            JSONArray conteudosEnsinoFundamentalJsonArray = curriculoEnsinoFundamentalJson.getJSONArray("Conteudos");
                            int numeroConteudosEnsinoFundamental = conteudosEnsinoFundamentalJsonArray.length();
                            if (numeroConteudosEnsinoFundamental > 0) {
                                for (j = 0; j < numeroConteudosEnsinoFundamental; j++) {
                                    JSONObject conteudoEnsinoFundamentalJson = conteudosEnsinoFundamentalJsonArray.getJSONObject(j);
                                    ConteudoFundamentalDao.inserirConteudoFundamental(curriculoId, conteudoEnsinoFundamentalJson, database);
                                }
                            }
                        }
                    }
                }
            }
            database.setTransactionSuccessful();
            database.endTransaction();
            ConteudoDao.fecharStatement();
            ConteudoFundamentalDao.fecharStatement();
            CurriculoDao.fecharStatement();
            CurriculoFundamentalDao.fecharStatement();
            GrupoDao.fecharStatement();
            HabilidadeDao.fecharStatements();
            return true;
        }
        catch (IOException | JSONException e) {
            CrashAnalytics.e(TAG, e);
            Crashlytics.logException(e);
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        HomeViewMvc.Listener delegate = delegateWeakRef.get();
        if (delegate != null) {
            delegate.completouEtapaSincronizacao(EtapasSincronizacao.ETAPA_DADOS, result);
            delegateWeakRef.clear();
        }
    }
}