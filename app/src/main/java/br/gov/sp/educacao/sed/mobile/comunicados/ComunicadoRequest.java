package br.gov.sp.educacao.sed.mobile.comunicados;

import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;

import br.gov.sp.educacao.sed.mobile.Login.UsuarioTO;
import br.gov.sp.educacao.sed.mobile.Menu.MenuDBcrud;
import br.gov.sp.educacao.sed.mobile.Menu.MenuDBgetters;
import br.gov.sp.educacao.sed.mobile.Menu.RevalidarToken2Request;
import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.ConexaoHttps.HttpsUrlConnectionFactory;
import br.gov.sp.educacao.sed.mobile.util.ConexaoHttps.HttpsUrlConnectionUtil;
import br.gov.sp.educacao.sed.mobile.util.CriarAcessoBanco;
import br.gov.sp.educacao.sed.mobile.util.Servidor.UrlServidor;

public class ComunicadoRequest {
    //Constantes
    private final String TAG = "ComunicadoRequest";

    //Variáveis
    private int tentativas = 0;

    //Métodos
    public JSONArray executeRequest(String token, Context context) {
        JSONArray jsonRetorno = null;
        try {
            tentativas++;
            String url = UrlServidor.URL_COMUNICADOS + "?codigoApp=1";
            HttpsURLConnection conexao = HttpsUrlConnectionFactory.createHttpsUrlConnection("GET", url, token);
            conexao.connect();
            if(conexao.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String retorno = HttpsUrlConnectionUtil.readStringFromHttpsURLConnection(true, conexao);
                conexao.disconnect();
                if (retorno.contains("Erro")) {
                    return null;
                }
                return new JSONArray(retorno);
            }
            else if (conexao.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
                if(tentativas == 1) {
                    Banco banco = CriarAcessoBanco.gerarBanco(context);
                    MenuDBgetters menuDBgetters = new MenuDBgetters(banco);
                    MenuDBcrud menuDBcrud = new MenuDBcrud(banco);
                    UsuarioTO usuario = menuDBgetters.getUsuarioAtivo();
                    RevalidarToken2Request revalidarToken2Request = new RevalidarToken2Request();
                    String novoToken = revalidarToken2Request.executeRequest(usuario.getUsuario(), usuario.getSenha());
                    menuDBcrud.atualizarTokenUsuario(novoToken);
                    return executeRequest(novoToken, context);
                }
                else {
                    jsonRetorno = new JSONArray().put(new JSONObject().put("Erro", "Sem permissão ou token expirado."));
                }
            }
        }
        catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(TAG, "executeRequest:" , e);
        }
        return jsonRetorno;
    }
}