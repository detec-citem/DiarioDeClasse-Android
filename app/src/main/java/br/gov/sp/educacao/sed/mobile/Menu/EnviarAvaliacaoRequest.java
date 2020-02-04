package br.gov.sp.educacao.sed.mobile.Menu;

//import com.google.firebase.perf.FirebasePerformance;
//import com.google.firebase.perf.metrics.HttpMetric;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import br.gov.sp.educacao.sed.mobile.util.ConexaoHttps.HttpsUrlConnectionFactory;
import br.gov.sp.educacao.sed.mobile.util.ConexaoHttps.HttpsUrlConnectionUtil;
import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;
import br.gov.sp.educacao.sed.mobile.util.Servidor.UrlServidor;

class EnviarAvaliacaoRequest {

    private String TAG = EnviarAvaliacaoRequest.class.getSimpleName();

    private JSONObject resultJson;

    private HttpsURLConnection httpsURLConnection;

    public JSONObject executeRequest(JSONObject avaliacao, String token) {

        try {

            httpsURLConnection = HttpsUrlConnectionFactory.createHttpsUrlConnection (
                    "POST",
                     UrlServidor.URL_AVALIACAO,
                     token
            );

            //region Métricas Google
            /*HttpMetric metric =
                    FirebasePerformance.getInstance().newHttpMetric(UrlServidor.URL_AVALIACAO,
                            FirebasePerformance.HttpMethod.POST);*/
            //endregion

            byte[] avaliacaoJsonBytes = avaliacao.toString().getBytes("UTF-8");

            sendAvaliacaoJsonBytes(avaliacaoJsonBytes, httpsURLConnection);

            //region Métricas Google

            /*metric.start();

            metric.setResponsePayloadSize(avaliacaoJsonBytes.length);

            metric.setResponseContentType("application/json");

            metric.setHttpResponseCode(httpsURLConnection.getResponseCode());*/
            //endregion Métricas Google

            if (httpsURLConnection.getResponseCode() == 201
                    || httpsURLConnection.getResponseCode() == 200) {

                String result = HttpsUrlConnectionUtil.readStringFromHttpsURLConnection(true,
                        httpsURLConnection);

                resultJson = new JSONObject(result);

                resultJson.put("Id", avaliacao.getString("Id"));

                resultJson.put("DataServidor", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
            }
            else{

                resultJson.put("Erro", httpsURLConnection.getResponseCode());
            }
        }
        catch (IOException | JSONException e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            httpsURLConnection.disconnect();
        }
        return resultJson;
    }

    private void sendAvaliacaoJsonBytes(byte[] avaliacaoJsonBytes,
                                               HttpsURLConnection httpsURLConnection)
            throws IOException {

        httpsURLConnection.setFixedLengthStreamingMode(avaliacaoJsonBytes.length);

        OutputStream outputStream = httpsURLConnection.getOutputStream();

        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

        bufferedOutputStream.write(avaliacaoJsonBytes);

        bufferedOutputStream.flush();

        outputStream.flush();

        httpsURLConnection.connect();
    }
}
