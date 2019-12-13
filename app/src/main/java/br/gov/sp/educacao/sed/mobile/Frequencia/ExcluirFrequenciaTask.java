package br.gov.sp.educacao.sed.mobile.Frequencia;

import java.util.List;

import android.os.AsyncTask;

class ExcluirFrequenciaTask
        extends AsyncTask<FrequenciaEnvio, Void, Boolean> {

    private String token;

    FrequenciaLancamentoViewMvcImpl delegate = null;

    ExcluirFrequenciaTask(String token) {

        this.token = token;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    @SafeVarargs
    public final Boolean doInBackground(FrequenciaEnvio... params) {

        boolean frequenciaExcluida =  false;

        ExcluirFrequenciaRequest excluirFrequenciaRequest = new ExcluirFrequenciaRequest();

        frequenciaExcluida = excluirFrequenciaRequest.executeRequest(params[0], token);

        return frequenciaExcluida;
    }

    @Override
    protected void onPostExecute(Boolean frequenciaExcluida) {

        if(frequenciaExcluida) {

            delegate.excluirFrequenciaNoBancoLocal();
        }
        else{

            delegate.usuarioAvisoFalha();
        }

        delegate = null;
    }
}
