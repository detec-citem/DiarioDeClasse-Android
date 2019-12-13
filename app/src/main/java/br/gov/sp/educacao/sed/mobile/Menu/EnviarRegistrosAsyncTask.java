package br.gov.sp.educacao.sed.mobile.Menu;

import android.os.AsyncTask;

//import com.google.firebase.perf.FirebasePerformance;
//import com.google.firebase.perf.metrics.HttpMetric;

import org.json.JSONObject;

import java.util.List;

import br.gov.sp.educacao.sed.mobile.RegistroDeAula.Registro;
import br.gov.sp.educacao.sed.mobile.RegistroDeAula.RegistroDBcrud;
import br.gov.sp.educacao.sed.mobile.RegistroDeAula.RegistroDBgetters;

public class EnviarRegistrosAsyncTask
        extends AsyncTask<String,
                          Void,
                          Void> {

    HomeViewMvcImpl delegate = null;

    private RegistroDBgetters registroDBgetters;

    private RegistroDBcrud registroDBcrud;

    EnviarRegistrosAsyncTask(RegistroDBgetters registroDBgetters, RegistroDBcrud registroDBcrud) {

        this.registroDBgetters = registroDBgetters;

        this.registroDBcrud = registroDBcrud;
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... params) {

        try {

            List<Registro> registros = registroDBgetters.buscarRegistrosNaoSincronizados();

            if(registros != null) {

                for(int i = 0; i < registros.size(); i++){

                    JSONObject jsonObject = registroDBgetters.montarJSONEnvio(registros.get(i));

                    Registro registroEnvio;

                    registroEnvio = registros.get(i);

                    EnviarRegistrosRequest enviarRegistrosRequest = new EnviarRegistrosRequest();

                    int codigo = enviarRegistrosRequest.executeRequest(jsonObject, params[0]);

                    registroEnvio.setCodNovoRegistro(codigo);

                    Registro registroAntigo = registroDBcrud.existeRegistro(

                            registroEnvio.getDataCriacao(), Integer.parseInt(registroEnvio.getCodigoTurma())
                    );

                    if (registroAntigo == null) {

                        registroDBcrud.salvarRegistroBanco(registroEnvio);
                    }
                    else {

                        registroDBcrud.atualizarRegistroBanco(registroEnvio, registroAntigo);
                    }
                }
            }
        }
        catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void n) {

        super.onPostExecute(n);

        delegate.completouEtapaSincronizacao();

        delegate = null;
    }
}
