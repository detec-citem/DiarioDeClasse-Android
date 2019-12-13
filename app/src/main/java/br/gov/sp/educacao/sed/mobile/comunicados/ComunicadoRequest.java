package br.gov.sp.educacao.sed.mobile.comunicados;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

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

    int qtdTentativas = 0;

    public JSONArray executeRequest(String token, Context context){

        JSONArray jsonRetorno = null;

        try{

            qtdTentativas ++;

            String url = UrlServidor.URL_COMUNICADOS + "?codigoApp=1";

            HttpsURLConnection httpsURLConnection =
                    HttpsUrlConnectionFactory.createHttpsUrlConnection("GET", url, token);

            httpsURLConnection.connect();

            if(httpsURLConnection.getResponseCode() == 200) {

                String retorno = HttpsUrlConnectionUtil.readStringFromHttpsURLConnection(true, httpsURLConnection);

                httpsURLConnection.disconnect();

                return new JSONArray(retorno);

            }
            else if(httpsURLConnection.getResponseCode() == 400){

                if(qtdTentativas == 1){

                    Banco banco = new CriarAcessoBanco().gerarBanco(context);

                    MenuDBgetters menuDBgetters = new MenuDBgetters(banco);

                    MenuDBcrud menuDBcrud = new MenuDBcrud(banco);

                    UsuarioTO usuario = menuDBgetters.getUsuarioAtivo();

                    RevalidarToken2Request revalidarToken2Request = new RevalidarToken2Request();

                    String novoToken = revalidarToken2Request.executeRequest(usuario.getUsuario(), usuario.getSenha());

                    menuDBcrud.atualizarTokenUsuario(novoToken);

                    return executeRequest(novoToken, context);
                }
                else{

                    jsonRetorno = new JSONArray().put(new JSONObject().put("Erro", "Sem permissão ou token expirado."));
                }
            }

        }
        catch (Exception e){

            e.printStackTrace();
        }

        return jsonRetorno;
    }
}
