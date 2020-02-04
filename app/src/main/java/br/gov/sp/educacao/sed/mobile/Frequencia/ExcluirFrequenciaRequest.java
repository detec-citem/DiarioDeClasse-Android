package br.gov.sp.educacao.sed.mobile.Frequencia;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.net.ssl.HttpsURLConnection;

import br.gov.sp.educacao.sed.mobile.util.ConexaoHttps.HttpsUrlConnectionFactory;
import br.gov.sp.educacao.sed.mobile.util.Servidor.UrlServidor;

public class ExcluirFrequenciaRequest {

    private boolean frequenciaExcluida;

    private HttpsURLConnection httpsURLConnection;

    private String TAG = ExcluirFrequenciaRequest.class.getSimpleName();

    ExcluirFrequenciaRequest() {

        this.frequenciaExcluida = false;
    }

    public boolean executeRequest(FrequenciaEnvio frequenciaEnvio, String token) {

        try {

            JSONObject excluirFrequenciaJson = new JSONObject();

            excluirFrequenciaJson.put("DataDaAula", frequenciaEnvio.getDataDaAula());

            excluirFrequenciaJson.put("CodigoTurma", frequenciaEnvio.codigoTurma);

            excluirFrequenciaJson.put("HorarioInicioAula", frequenciaEnvio.getHorarioInicioAula());

            excluirFrequenciaJson.put("HorarioFimAula", frequenciaEnvio.getHorarioFimAula());

            excluirFrequenciaJson.put("CodigoEscola", frequenciaEnvio.getCodigoEscola());

            excluirFrequenciaJson.put("CodigoDisciplina", frequenciaEnvio.getCodigoDisciplina());

            httpsURLConnection = HttpsUrlConnectionFactory.createHttpsUrlConnection (

                    "POST", UrlServidor.URL_FREQUENCIA_EXCLUIR, token
            );

            byte[] excluirFrequenciaJsonBytes = excluirFrequenciaJson.toString().getBytes("UTF-8");

            enviarFrequenciaParaExcluirJsonBytes(excluirFrequenciaJsonBytes, httpsURLConnection);

            if(httpsURLConnection.getResponseCode() == 200
                    || httpsURLConnection.getResponseCode() == 201) {

                frequenciaExcluida = true;
            }
        }
        catch(Exception e) {

            e.printStackTrace();
        }

        return frequenciaExcluida;
    }

    private void enviarFrequenciaParaExcluirJsonBytes(byte[] excluirFrequenciaJsonBytes,
                                                   HttpsURLConnection httpsURLConnection)
            throws IOException {

        httpsURLConnection.setFixedLengthStreamingMode(excluirFrequenciaJsonBytes.length);

        OutputStream outputStream = httpsURLConnection.getOutputStream();

        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

        bufferedOutputStream.write(excluirFrequenciaJsonBytes);

        bufferedOutputStream.flush();

        outputStream.flush();

        httpsURLConnection.connect();
    }
}
