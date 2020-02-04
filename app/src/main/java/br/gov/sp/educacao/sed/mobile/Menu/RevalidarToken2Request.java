package br.gov.sp.educacao.sed.mobile.Menu;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.net.ssl.HttpsURLConnection;

import br.gov.sp.educacao.sed.mobile.util.ConexaoHttps.HttpsUrlConnectionFactory;
import br.gov.sp.educacao.sed.mobile.util.ConexaoHttps.HttpsUrlConnectionUtil;
import br.gov.sp.educacao.sed.mobile.util.Servidor.UrlServidor;

public class RevalidarToken2Request {

    public String executeRequest(String rg, String senha) {

        String token = "";

        try {

            HttpsURLConnection httpsURLConnection =
                    HttpsUrlConnectionFactory.createHttpsUrlConnection("POST", UrlServidor.URL_LOGIN, null);

            JSONObject revalidarTokenJson = new JSONObject().put("user", rg).put("senha", senha);

            byte[] revalidarTokenJsonBytes = revalidarTokenJson.toString().getBytes("UTF-8");

            httpsURLConnection.setFixedLengthStreamingMode(revalidarTokenJsonBytes.length);

            OutputStream outputStream = httpsURLConnection.getOutputStream();

            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

            bufferedOutputStream.write(revalidarTokenJsonBytes);

            bufferedOutputStream.flush();

            outputStream.flush();

            bufferedOutputStream.close();

            outputStream.close();

            httpsURLConnection.connect();

            int statusCode = httpsURLConnection.getResponseCode();

            if(statusCode == 200) {

                String revalidarTokenResponse = HttpsUrlConnectionUtil.readStringFromHttpsURLConnection(true, httpsURLConnection);

                httpsURLConnection.disconnect();

                JSONObject revalidarTokenJsonResponse = new JSONObject(revalidarTokenResponse);

                if(!revalidarTokenJsonResponse.has("Erro")) {

                    revalidarTokenJsonResponse.put("Usuario", rg).put("Senha", senha);

                    token = revalidarTokenJsonResponse.getString("Token");

                    httpsURLConnection =
                            HttpsUrlConnectionFactory.createHttpsUrlConnection("GET",
                                    UrlServidor.URL_SELECIONAR_PERFIL + "?perfilSelecionado=4&token=" + token, null);

                    httpsURLConnection.connect();

                    Log.e("SELECIONAR PERFIL", httpsURLConnection.getResponseMessage());

                    httpsURLConnection.disconnect();
                }
            }
        }
        catch (IOException | JSONException e) {

            e.printStackTrace();
        }

        return token;
    }
}
