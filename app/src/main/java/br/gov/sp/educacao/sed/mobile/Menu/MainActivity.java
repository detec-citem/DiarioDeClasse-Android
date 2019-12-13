package br.gov.sp.educacao.sed.mobile.Menu;

import android.os.Bundle;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.Intent;
import android.content.Context;
import android.content.DialogInterface;

import java.util.concurrent.TimeUnit;

import android.database.sqlite.SQLiteException;

import br.gov.sp.educacao.sed.mobile.Frequencia.Calendario;
import br.gov.sp.educacao.sed.mobile.Login.UsuarioTO;
import br.gov.sp.educacao.sed.mobile.Login.LoginActivity;

import br.gov.sp.educacao.sed.mobile.R;
import br.gov.sp.educacao.sed.mobile.util.Analytics;
import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.CriarAcessoBanco;

public class MainActivity
        extends Activity {

    private Banco banco;

    private MenuDBgetters menuDBgetters;

    private CriarAcessoBanco criarAcessoBanco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Analytics.setTela(this, this.getClass().toString());

        final Intent intent;

        UsuarioTO usuario = null;

        criarAcessoBanco = new CriarAcessoBanco();

        banco = criarAcessoBanco.gerarBanco(getApplicationContext());

        menuDBgetters = new MenuDBgetters(banco);

        try {

            usuario = menuDBgetters.getUsuarioAtivo();
        }
        catch(SQLiteException e) {

            e.printStackTrace();

            String noSuchColumn = getString(R.string.no_such_column);

            if(e.getMessage().contains(noSuchColumn)) {

                showErroDialog(this, getString(R.string.problemas_inicializacao, getString(R.string.app_name)));
            }
        }
        if(usuario != null) {

            intent = new Intent(MainActivity.this, HomeActivity.class);

            intent.putExtra("usuario", usuario);
        }
        else {

            intent = new Intent(MainActivity.this, LoginActivity.class);
        }

        new Thread(new Runnable() {

            @Override
            public void run() {

                try {

                    TimeUnit.SECONDS.sleep(1);

                    finish();

                    startActivity(intent);
                }
                catch(InterruptedException e) {

                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showErroDialog(Context context, String mensagem) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(mensagem)
               .setIcon(R.drawable.ic_launcher)
               .setTitle(R.string.app_name);

        builder.setPositiveButton(R.string.sair, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                finish();
            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        banco = null;

        criarAcessoBanco = null;

        menuDBgetters = null;
    }
}