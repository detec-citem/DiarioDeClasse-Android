package br.gov.sp.educacao.sed.mobile.Menu;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedOutputStream;

import javax.net.ssl.HttpsURLConnection;

import br.gov.sp.educacao.sed.mobile.util.ConexaoHttps.HttpsUrlConnectionUtil;
import br.gov.sp.educacao.sed.mobile.util.Servidor.UrlServidor;
import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;
import br.gov.sp.educacao.sed.mobile.util.ConexaoHttps.HttpsUrlConnectionFactory;

class EnviarFrequenciasRequest {

    private String TAG = EnviarFrequenciasRequest.class.getSimpleName();

    private HttpsURLConnection httpsURLConnection;

    private boolean enviouFrequencia;

    EnviarFrequenciasRequest() {

        this.enviouFrequencia = false;
    }

    public JSONObject executeRequest(JSONObject frequencias, String token) {

        JSONObject respostaFrequencia = null;

        try {

            httpsURLConnection = HttpsUrlConnectionFactory.createHttpsUrlConnection (

                    "POST", UrlServidor.URL_FREQUENCIA, token
            );

            byte[] frequenciasJsonBytes = frequencias.toString().getBytes("UTF-8");

            sendFrequenciasJsonBytes(frequenciasJsonBytes, httpsURLConnection);

            if(httpsURLConnection.getResponseCode() == 200
                    || httpsURLConnection.getResponseCode() == 201) {

                String jsonString = HttpsUrlConnectionUtil.readStringFromHttpsURLConnection(true, httpsURLConnection);

                respostaFrequencia = new JSONObject(jsonString);
            }
        }
        catch (Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            httpsURLConnection.disconnect();
        }
        return respostaFrequencia;
    }

    private void sendFrequenciasJsonBytes(byte[] frequenciasJsonBytes,
                                          HttpsURLConnection httpsURLConnection)
            throws IOException {

        httpsURLConnection.setFixedLengthStreamingMode(frequenciasJsonBytes.length);

        OutputStream outputStream = httpsURLConnection.getOutputStream();

        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

        bufferedOutputStream.write(frequenciasJsonBytes);

        bufferedOutputStream.flush();

        outputStream.flush();

        bufferedOutputStream.close();

        outputStream.close();

        httpsURLConnection.connect();
    }
}
