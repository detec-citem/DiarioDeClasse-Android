package br.gov.sp.educacao.sed.mobile.Login;

import android.os.Bundle;

import android.content.Intent;

import android.view.View;
import android.view.LayoutInflater;

import android.support.v7.app.AppCompatActivity;

import org.json.JSONObject;

import br.gov.sp.educacao.sed.mobile.Menu.HomeActivity;

import br.gov.sp.educacao.sed.mobile.R;
import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.Analytics;
import br.gov.sp.educacao.sed.mobile.util.CriarAcessoBanco;
import br.gov.sp.educacao.sed.mobile.util.MyPreferences;

import static br.gov.sp.educacao.sed.mobile.util.Utils.lerArquivoEstatico;

public class LoginActivity
        extends AppCompatActivity
         implements LoginViewMvcImpl.Listener {

    private Banco banco;

    private LoginDBcrud loginDBcrud;

    private LoginViewMvc loginViewMvcImpl;

    private LoginAsyncTask loginAsyncTask;

    private MyPreferences myPreferences;

    private CriarAcessoBanco criarAcessoBanco;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Analytics.setTela(this, getClass().toString());

        loginViewMvcImpl = new LoginViewMvcImpl(LayoutInflater.from(this), null);

        criarAcessoBanco = new CriarAcessoBanco();

        banco = criarAcessoBanco.gerarBanco(getApplicationContext());

        loginDBcrud = new LoginDBcrud(banco);

        myPreferences = new MyPreferences(this);

        setContentView(loginViewMvcImpl.getRootView());
    }

    @Override
    protected void onStart() {

        super.onStart();

        loginViewMvcImpl.registerListener(this);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        banco = null;

        loginDBcrud = null;

        criarAcessoBanco = null;

        if(loginAsyncTask != null) {

            loginAsyncTask = null;
        }

        loginViewMvcImpl.unregisterListener();

        loginViewMvcImpl = null;
    }

    public void conferirConexao(View view) {

        loginViewMvcImpl.conferirConexao();
    }

    @Override
    public void resultadoLogin(UsuarioTO usuarioTO) {

        try{

            loginAsyncTask.delegate = null;
        }
        catch (Exception e){

            e.printStackTrace();
        }

        if(usuarioTO != null) {

            loginDBcrud.inserirUsuarioNoBanco(usuarioTO);

            this.startActivity(new Intent(this, HomeActivity.class));

            finish();
        }
        else {

            loginViewMvcImpl.usuarioAvisoFalha();
        }
    }

    @Override
    public void executaLogin(String login, String senha) {

        if(login.equals("rg222222222sp") && senha.equals("testelogin")){

            myPreferences.setLoginTeste();

            loginViewMvcImpl.finalizaProgress();

            try{

                JSONObject jsonObject =
                        new JSONObject(lerArquivoEstatico(R.raw.login_test, this).toString())
                                .put("Usuario", login)
                                .put("Senha", senha);

                UsuarioTO usuarioTO = new UsuarioTO(jsonObject);

                resultadoLogin(usuarioTO);
            }
            catch (Exception e){

                e.printStackTrace();
            }
        }
        else{


            loginAsyncTask = new LoginAsyncTask();

            loginAsyncTask.delegate = loginViewMvcImpl;

            loginAsyncTask.execute(login, senha);
        }

    }
}