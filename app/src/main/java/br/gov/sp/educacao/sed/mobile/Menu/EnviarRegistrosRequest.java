package br.gov.sp.educacao.sed.mobile.Menu;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.net.ssl.HttpsURLConnection;

import br.gov.sp.educacao.sed.mobile.util.ConexaoHttps.HttpsUrlConnectionUtil;
import br.gov.sp.educacao.sed.mobile.util.ConexaoHttps.HttpsUrlConnectionFactory;
import br.gov.sp.educacao.sed.mobile.util.Servidor.UrlServidor;
import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;

class EnviarRegistrosRequest {

    private String TAG = EnviarRegistrosRequest.class.getSimpleName();

    private HttpsURLConnection httpsURLConnection;

    private int codigo;

    public int executeRequest(JSONObject registros, String token) {

        try {

            httpsURLConnection = HttpsUrlConnectionFactory.createHttpsUrlConnection (
                    "POST",
                    UrlServidor.URL_REGISTRO_AULAS,
                    token
            );

            codigo = -1;

            byte[] registrosJsonBytes = registros.toString().getBytes("UTF-8");

            sendRegistrosJsonBytes(registrosJsonBytes, httpsURLConnection);

            if(httpsURLConnection.getResponseCode() == 200
                    || httpsURLConnection.getResponseCode() == 201) {

                String result = HttpsUrlConnectionUtil.readStringFromHttpsURLConnection(true,
                        httpsURLConnection);

                JSONObject resultado = new JSONObject(result);

                codigo = resultado.getInt("Codigo");
            }
        }
        catch (IOException |JSONException e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            httpsURLConnection.disconnect();
        }
        return codigo;
    }

    private void sendRegistrosJsonBytes(byte[] registrosJsonBytes,
                                        HttpsURLConnection httpsURLConnection)
            throws IOException {

        httpsURLConnection.setFixedLengthStreamingMode(registrosJsonBytes.length);

        OutputStream outputStream = httpsURLConnection.getOutputStream();

        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

        bufferedOutputStream.write(registrosJsonBytes);

        bufferedOutputStream.flush();

        outputStream.flush();

        httpsURLConnection.connect();
    }
}
