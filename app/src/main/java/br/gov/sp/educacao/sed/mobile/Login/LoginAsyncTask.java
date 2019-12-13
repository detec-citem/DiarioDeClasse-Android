package br.gov.sp.educacao.sed.mobile.Login;

import android.os.AsyncTask;

public class LoginAsyncTask
        extends AsyncTask<String,
                          Void,
                          UsuarioTO> {

    LoginViewMvc delegate = null;

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
    }

    @Override
    protected UsuarioTO doInBackground(String... params) {

        return new LoginRequest().executeRequest(params[0], //RG
                                           params[1]); //Senha
    }

    @Override
    protected void onPostExecute(UsuarioTO result) {

        super.onPostExecute(result);

        delegate.resultadoLogin(result);
    }
}
