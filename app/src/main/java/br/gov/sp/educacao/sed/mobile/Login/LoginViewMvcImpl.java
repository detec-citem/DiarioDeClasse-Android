package br.gov.sp.educacao.sed.mobile.Login;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import br.gov.sp.educacao.sed.mobile.R;
import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;
import br.gov.sp.educacao.sed.mobile.util.NetworkUtils;

public class LoginViewMvcImpl
        implements LoginViewMvc {

    private String strSenha;

    private EditText etSenha;

    private Listener listener;

    private String strUsuario;

    private EditText etUsuario;

    private final View mRootView;

    private AlertDialog dialogLogin;

    private LayoutInflater layoutInflater;

    LoginViewMvcImpl(LayoutInflater layoutInflater, ViewGroup parent) {

        mRootView = layoutInflater.inflate(R.layout.activity_login, parent, false);

        this.layoutInflater = layoutInflater;

        etUsuario = findViewById(R.id.et_usuario);

        etSenha = findViewById(R.id.et_senha);
    }

    @Override
    public View getRootView() {

        return mRootView;
    }

    @Override
    public void efetuarLogin() {

        strUsuario = etUsuario.getText().toString();

        strSenha = etSenha.getText().toString();

        if(!strUsuario.equals("")
                && !strSenha.equals("")) {

            inicializaProgress();

            listener.executaLogin(strUsuario, strSenha);
        }
        else {

            usuarioAvisoCamposVazios();
        }
    }

    private Context getContext() {

        return getRootView().getContext();
    }

    @Override
    public void conferirConexao() {

        if(!NetworkUtils.isWifi(getContext())) {

            AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                    .setTitle("Acesso à Internet")
                    .setMessage("O aparelho não está conectado a uma rede sem fio (Wi-Fi). " +
                            "Aceita utilizar sua internet móvel (3G, 4G)? \n" +
                            "Importante: ao clicar em SIM, ACEITO, declara estar ciente " +
                            "de que a utilização poderá acarretar em cobranças adicionais de sua operadora de telefonia móvel.")

                    .setPositiveButton("SIM, ACEITO", new DialogInterface.OnClickListener() {

                        @Override

                        public void onClick(DialogInterface dialog, int which) {

                            efetuarLogin();
                        }
                    })
                    .setNeutralButton("NÃO, VOLTAR", new DialogInterface.OnClickListener() {

                        @Override

                        public void onClick(DialogInterface dialog, int which) {

                            Toast.makeText(getContext(), (R.string.conecte_wifi), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setCancelable(false)
                    .create();

            alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);

            alertDialog.show();
        }
        else {

            efetuarLogin();
        }
    }

    @Override
    public void finalizaProgress() {
        try {
            dialogLogin.dismiss();
        }
        catch (Exception e) {
            Crashlytics.logException(e);
        }
    }

    @Override
    public void usuarioAvisoFalha() {

        finalizaProgress();

        Toast.makeText(getContext(), "Não foi possível fazer o login", Toast.LENGTH_LONG).show();
    }

    @Override
    public void unregisterListener() {

        this.listener = null;
    }

    private void inicializaProgress() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(

                getContext(), R.style.ThemeOverlay_AppCompat_Dialog
        );

        View view = layoutInflater.inflate(R.layout.dialoglogin, null, false);

        builder.setView(view);

        dialogLogin = builder.create();

        dialogLogin.setCancelable(false);

        dialogLogin.setCanceledOnTouchOutside(false);

        dialogLogin.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);

        dialogLogin.show();
    }

    private void usuarioAvisoCamposVazios() {

        if(strUsuario.equals("")) {

            etUsuario.setError(getContext().getResources().getString(R.string.hintusuario));
        }
        else if (strSenha.equals("")){

            etSenha.setError(getContext().getResources().getString(R.string.hintsenha));
        }
    }

    @Override
    public void resultadoLogin(UsuarioTO usuarioTO) {

        finalizaProgress();

        listener.resultadoLogin((usuarioTO));
    }

    private <T extends View> T findViewById(int id) {

        return getRootView().findViewById(id);
    }

    @Override
    public void registerListener(Listener listener) {

        this.listener = listener;
    }
}