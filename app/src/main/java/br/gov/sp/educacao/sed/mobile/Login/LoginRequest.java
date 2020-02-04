package br.gov.sp.educacao.sed.mobile.Login;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.net.ssl.HttpsURLConnection;

import br.gov.sp.educacao.sed.mobile.util.ConexaoHttps.HttpsUrlConnectionFactory;
import br.gov.sp.educacao.sed.mobile.util.ConexaoHttps.HttpsUrlConnectionUtil;
import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;
import br.gov.sp.educacao.sed.mobile.util.Servidor.UrlServidor;

public class LoginRequest {

    private final String TAG = LoginRequest.class.getSimpleName();

    private UsuarioTO usuarioTO;

    public UsuarioTO executeRequest(String rg, String senha) {

        try {

            HttpsURLConnection httpsURLConnection = HttpsUrlConnectionFactory.createHttpsUrlConnection(

                    "POST", UrlServidor.URL_LOGIN, null
            );

            JSONObject loginJson = new JSONObject().put("user", rg)
                    .put("senha", senha)
                    .put("RefLogin","App Di@rio de Classe");

            byte[] loginJsonBytes = loginJson.toString().getBytes("UTF-8");

            httpsURLConnection.setFixedLengthStreamingMode(loginJsonBytes.length);

            OutputStream outputStream = httpsURLConnection.getOutputStream();

            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

            bufferedOutputStream.write(loginJsonBytes);

            bufferedOutputStream.flush();

            outputStream.flush();

            bufferedOutputStream.close();

            outputStream.close();

            httpsURLConnection.connect();

            int statusCode = httpsURLConnection.getResponseCode();

            if(statusCode == 201 || statusCode == 200) {

                String loginResponse = HttpsUrlConnectionUtil.readStringFromHttpsURLConnection(true, httpsURLConnection);

                httpsURLConnection.disconnect();

                JSONObject loginJsonResponse = new JSONObject(loginResponse);

                if(!loginJsonResponse.has("Erro")) {

                    loginJsonResponse.put("Usuario", rg).put("Senha", senha);

                    String token = loginJsonResponse.getString("Token");

                    usuarioTO = new UsuarioTO(loginJsonResponse);

                    httpsURLConnection = HttpsUrlConnectionFactory.createHttpsUrlConnection(

                            "GET", UrlServidor.URL_SELECIONAR_PERFIL + "?perfilSelecionado=4&token=" + token, null
                    );

                    httpsURLConnection.connect();
                }
            }
            else {

                httpsURLConnection.disconnect();
            }
        }
        catch(IOException | JSONException e) {

            CrashAnalytics.e(TAG, e);
        }
        return usuarioTO;
    }
}
