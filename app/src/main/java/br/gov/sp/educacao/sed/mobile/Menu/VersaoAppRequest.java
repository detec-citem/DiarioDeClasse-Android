package br.gov.sp.educacao.sed.mobile.Menu;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.json.JSONObject;

import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;

import br.gov.sp.educacao.sed.mobile.BuildConfig;
import br.gov.sp.educacao.sed.mobile.Login.UsuarioTO;
import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.ConexaoHttps.HttpsUrlConnectionFactory;
import br.gov.sp.educacao.sed.mobile.util.ConexaoHttps.HttpsUrlConnectionUtil;
import br.gov.sp.educacao.sed.mobile.util.CriarAcessoBanco;
import br.gov.sp.educacao.sed.mobile.util.Servidor.UrlServidor;

public class VersaoAppRequest {
    //Constantes
    private static final String TAG = "ComunicadoRequest";
    private static final String campoVersaoApp = "VersaoApp";

    //Variáveis
    private static int tentativas = 0;

    //Métodos
    public static int buscarVersaoDoApp(String token, Context context) {
        if (true) {
            return BuildConfig.VERSION_CODE;
        }

        try {
            tentativas++;
            HttpsURLConnection conexao = HttpsUrlConnectionFactory.createHttpsUrlConnection("GET", UrlServidor.URL_VERSAO_APP, token);
            conexao.connect();
            if(conexao.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String retorno = HttpsUrlConnectionUtil.readStringFromHttpsURLConnection(true, conexao);
                conexao.disconnect();
                JSONObject versaoAppJson = new JSONObject(retorno);
                return versaoAppJson.getInt(campoVersaoApp);
            }
            else if (conexao.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
                if (tentativas == 1) {
                    Banco banco = CriarAcessoBanco.gerarBanco(context);
                    MenuDBgetters menuDBgetters = new MenuDBgetters(banco);
                    MenuDBcrud menuDBcrud = new MenuDBcrud(banco);
                    UsuarioTO usuario = menuDBgetters.getUsuarioAtivo();
                    RevalidarToken2Request revalidarToken2Request = new RevalidarToken2Request();
                    String novoToken = revalidarToken2Request.executeRequest(usuario.getUsuario(), usuario.getSenha());
                    menuDBcrud.atualizarTokenUsuario(novoToken);
                    return buscarVersaoDoApp(novoToken, context);
                }
                else {
                    return -1;
                }
            }
            else{

                return -1;
            }
        }
        catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(TAG, "buscarVersaoDoApp:", e);
        }
        return -1;
    }
}