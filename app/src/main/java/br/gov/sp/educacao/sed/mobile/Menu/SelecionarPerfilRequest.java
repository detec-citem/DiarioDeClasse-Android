package br.gov.sp.educacao.sed.mobile.Menu;

import javax.net.ssl.HttpsURLConnection;

import br.gov.sp.educacao.sed.mobile.util.Servidor.UrlServidor;
import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;
import br.gov.sp.educacao.sed.mobile.util.ConexaoHttps.HttpsUrlConnectionFactory;

public class SelecionarPerfilRequest {

    private String TAG = SelecionarPerfilRequest.class.getSimpleName();

    private boolean perfilOk;

    private HttpsURLConnection httpsURLConnection;

    public boolean executeRequest(String token) {

        try {

            httpsURLConnection = HttpsUrlConnectionFactory.createHttpsUrlConnection(

                    "GET", UrlServidor.URL_SELECIONAR_PERFIL + "?perfilSelecionado=4&token=" + token, null
            );

            httpsURLConnection.connect();

            if(httpsURLConnection.getResponseMessage().equals("OK")) {

                perfilOk = true;
            }
        }
        catch(Exception e) {

            e.printStackTrace();

            CrashAnalytics.e(TAG, e);
        }
        finally {

            httpsURLConnection.disconnect();
        }
        return perfilOk;
    }
}
