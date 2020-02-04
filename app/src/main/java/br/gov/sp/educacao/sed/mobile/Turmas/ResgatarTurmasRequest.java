package br.gov.sp.educacao.sed.mobile.Turmas;

import android.util.Log;

import java.io.IOException;

import javax.net.ssl.HttpsURLConnection;

import br.gov.sp.educacao.sed.mobile.util.ConexaoHttps.HttpsUrlConnectionFactory;
import br.gov.sp.educacao.sed.mobile.util.ConexaoHttps.HttpsUrlConnectionUtil;
import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;
import br.gov.sp.educacao.sed.mobile.util.Servidor.UrlServidor;

public class ResgatarTurmasRequest {

    private String resultado;

    private HttpsURLConnection httpsURLConnection;

    private String TAG = ResgatarTurmasRequest.class.getSimpleName();

    ResgatarTurmasRequest() {

        this.resultado = "";
    }

    public String executeRequest(String token) {

        try {

            Log.e(TAG, "JSON: Request iniciada");

            httpsURLConnection = HttpsUrlConnectionFactory.createHttpsUrlConnection(

                    "GET", UrlServidor.URL_DADOS_OFF_LINE, token
            );

            if(httpsURLConnection.getResponseCode() == 200) {

                resultado = HttpsUrlConnectionUtil.readStringFromHttpsURLConnection(true, httpsURLConnection);
            }


            return resultado;
        }
        catch (IOException e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            Log.e(TAG, "JSON: Request finalizada:");

            httpsURLConnection.disconnect();
        }
        return resultado;
    }
}
