package br.gov.sp.educacao.sed.mobile.Menu;

import android.os.AsyncTask;

import java.util.List;

class DeletarAvaliacoesTask
        extends AsyncTask<List<Integer>, Void, Void> {

    private String token;

    HomeViewMvcImpl delegate = null;

    DeletarAvaliacoesTask(String token) {

        this.token = token;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    @SafeVarargs
    public final Void doInBackground(List<Integer>... params) {

        int avaliacoesParaDeletarSize = params[0].size();

        DeletarAvaliacaoRequest deletarAvaliacaoRequest = new DeletarAvaliacaoRequest();

        if(avaliacoesParaDeletarSize > 0) {

            for(int j = 0; j < avaliacoesParaDeletarSize; j++) {

                if(params[0].get(j) == 0) {

                    delegate.deletarAvaliacaoNoBancoLocal(params[0].get(j));
                }
                else {

                    if(deletarAvaliacaoRequest.executeRequest(params[0].get(j), token)) {

                        delegate.deletarAvaliacaoNoBancoLocal(params[0].get(j));
                    }
                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void a) {

        delegate = null;
    }
}
