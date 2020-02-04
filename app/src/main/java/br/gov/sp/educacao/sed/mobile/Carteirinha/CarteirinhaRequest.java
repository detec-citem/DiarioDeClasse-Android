package br.gov.sp.educacao.sed.mobile.Carteirinha;

import javax.net.ssl.HttpsURLConnection;

import br.gov.sp.educacao.sed.mobile.util.ConexaoHttps.HttpsUrlConnectionFactory;
import br.gov.sp.educacao.sed.mobile.util.ConexaoHttps.HttpsUrlConnectionUtil;
import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;
import br.gov.sp.educacao.sed.mobile.util.Servidor.UrlServidor;

public class CarteirinhaRequest {

    private String TAG = CarteirinhaRequest.class.getSimpleName();

    private String loginResponse;

    private HttpsURLConnection httpsURLConnection;

    public String executeRequest(String cpf, String token) {

        loginResponse = "";

        try {

            httpsURLConnection = HttpsUrlConnectionFactory.createHttpsUrlConnection(

                    "GET", UrlServidor.URL_CARTEIRINHA + "?cpf=" + cpf + "&codigoCargo=0", token
            );

            httpsURLConnection.connect();

            if(httpsURLConnection.getResponseCode() == 201 || httpsURLConnection.getResponseCode() == 200) {

                loginResponse = HttpsUrlConnectionUtil.readStringFromHttpsURLConnection(true, httpsURLConnection);
            }
        }
        catch(Exception e) {

            e.printStackTrace();

            CrashAnalytics.e(TAG, e);
        }
        finally {

            httpsURLConnection.disconnect();
        }
        return loginResponse;
    }
}
