package br.gov.sp.educacao.sed.mobile.Menu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import br.gov.sp.educacao.sed.mobile.RegistroDeAula.HabilidadeRegistroFundamentalDao;
import br.gov.sp.educacao.sed.mobile.RegistroDeAula.Registro;
import br.gov.sp.educacao.sed.mobile.RegistroDeAula.RegistroAulaFundamental;
import br.gov.sp.educacao.sed.mobile.RegistroDeAula.RegistroAulaFundamentalDao;
import br.gov.sp.educacao.sed.mobile.RegistroDeAula.RegistroDBcrud;
import br.gov.sp.educacao.sed.mobile.RegistroDeAula.RegistroDBgetters;
import br.gov.sp.educacao.sed.mobile.constants.EtapasSincronizacao;

public class EnviarRegistrosAsyncTask extends AsyncTask<String, Void, Boolean> {
    //Variáveis
    private Context context;
    HomeViewMvc.Listener delegate = null;
    private RegistroDBgetters registroDBgetters;
    private RegistroDBcrud registroDBcrud;
    private SQLiteDatabase database;

    //Construtor
    EnviarRegistrosAsyncTask(Context context, RegistroDBgetters registroDBgetters, RegistroDBcrud registroDBcrud, SQLiteDatabase database) {
        this.context = context;
        this.registroDBgetters = registroDBgetters;
        this.registroDBcrud = registroDBcrud;
        this.database = database;
    }

    //AsyncTask
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        boolean sucesso = true;
        int i;
        String token = params[0];
        List<Registro> registros = registroDBgetters.buscarRegistrosNaoSincronizados();
        if (registros != null) {
            int numeroRegistros = registros.size();
            database.beginTransaction();
            for(i = 0; i < numeroRegistros; i++) {
                Registro registro = registros.get(i);
                JSONObject registroAulaJson = registroDBgetters.montarJSONEnvio(registro);
                EnviarRegistrosRequest enviarRegistrosRequest = new EnviarRegistrosRequest();
                int codigo = enviarRegistrosRequest.executeRequest(registroAulaJson, token);
                if (codigo == -1) {
                    sucesso = false;
                }
                registro.setCodNovoRegistro(codigo);
                Registro registroAntigo = registroDBcrud.existeRegistro(registro.getDataCriacao(), Integer.parseInt(registro.getCodigoTurma()));
                if (registroAntigo == null) {
                    registroDBcrud.salvarRegistroBanco(registro);
                }
                else {
                    registroDBcrud.atualizarRegistroBanco(registro, registroAntigo);
                }
            }
        }
        List<RegistroAulaFundamental> registroAulaFundamental = RegistroAulaFundamentalDao.buscarRegistrosAulaNaoSincronizados(database);
        if (registroAulaFundamental != null) {
            int numeroRegistros = registroAulaFundamental.size();
            if (!database.inTransaction()) {
                database.beginTransaction();
            }
            for (i = 0; i < numeroRegistros; i++) {
                RegistroAulaFundamental registroAula = registroAulaFundamental.get(i);
                int codigoTurma = TurmaDao.buscarCodigoTurma(registroAula.getTurma(), database);
                EnviarRegistroFundamentalRequest enviarRegistroFundamentalRequest = new EnviarRegistroFundamentalRequest();
                try {
                    List<Integer> habilidadesRegistro = HabilidadeRegistroFundamentalDao.buscarHabilidadesRegistroFundamental(registroAula.getCodigoRegistroAula(), database);
                    int codigoNovo = enviarRegistroFundamentalRequest.enviarRegistroAula(codigoTurma, token, habilidadesRegistro, registroAula, context);
                    if (codigoNovo == -1) {
                        sucesso = false;
                    }
                    else {
                        int codigoAntigo = registroAula.getCodigoRegistroAula();
                        registroAula.setCodigoRegistroAula(codigoNovo);
                        RegistroAulaFundamentalDao.atualizarRegistro(codigoAntigo, registroAula, database);
                        HabilidadeRegistroFundamentalDao.atualizarHabilidadesRegistro(codigoAntigo, codigoNovo, database);
                    }
                }
                catch (IOException | JSONException e) {
                    sucesso = false;
                    break;
                }
            }
        }
        if (database.inTransaction()) {
            database.setTransactionSuccessful();
            database.endTransaction();
        }
        return sucesso;
    }

    @Override
    protected void onPostExecute(Boolean sucesso) {
        super.onPostExecute(sucesso);
        delegate.completouEtapaSincronizacao(EtapasSincronizacao.ETAPA_REGISTRO, sucesso);
        delegate = null;
    }
}