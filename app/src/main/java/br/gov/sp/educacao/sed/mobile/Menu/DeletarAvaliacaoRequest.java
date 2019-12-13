package br.gov.sp.educacao.sed.mobile.Menu;

import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedOutputStream;

import javax.net.ssl.HttpsURLConnection;

import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;
import br.gov.sp.educacao.sed.mobile.util.Servidor.UrlServidor;
import br.gov.sp.educacao.sed.mobile.util.ConexaoHttps.HttpsUrlConnectionFactory;

public class DeletarAvaliacaoRequest {

    private boolean avaliacaoDeletada;

    private HttpsURLConnection httpsURLConnection;

    private String TAG = DeletarAvaliacaoRequest.class.getSimpleName();

    DeletarAvaliacaoRequest() {

        this.avaliacaoDeletada = false;
    }

    public boolean executeRequest(int codigoAvaliacao, String token) {

        try {

            JSONObject deletarAvaliacaoJson = new JSONObject();

            deletarAvaliacaoJson.put("Codigo", codigoAvaliacao);

            httpsURLConnection = HttpsUrlConnectionFactory.createHttpsUrlConnection (
                    "POST",
                     UrlServidor.URL_AVALIACAO_DELETAR,
                     token
            );

            byte[] deletarAvaliacaoJsonBytes = deletarAvaliacaoJson.toString().getBytes("UTF-8");

            sendAvaliacaoParaDeletarJsonBytes(deletarAvaliacaoJsonBytes, httpsURLConnection);

            if(httpsURLConnection.getResponseCode() == 200
                    || httpsURLConnection.getResponseCode() == 201) {

                avaliacaoDeletada = true;
            }
        }
        catch (IOException | JSONException e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            httpsURLConnection.disconnect();
        }
        return avaliacaoDeletada;
    }

    private void sendAvaliacaoParaDeletarJsonBytes(byte[] deletarAvaliacaoJsonBytes,
                                                          HttpsURLConnection httpsURLConnection)
            throws IOException{

        httpsURLConnection.setFixedLengthStreamingMode(deletarAvaliacaoJsonBytes.length);

        OutputStream outputStream = httpsURLConnection.getOutputStream();

        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

        bufferedOutputStream.write(deletarAvaliacaoJsonBytes);

        bufferedOutputStream.flush();

        outputStream.flush();

        httpsURLConnection.connect();
    }
}
