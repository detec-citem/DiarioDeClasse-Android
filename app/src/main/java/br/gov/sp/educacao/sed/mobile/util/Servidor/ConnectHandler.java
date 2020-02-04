package br.gov.sp.educacao.sed.mobile.util.Servidor;

import android.os.AsyncTask;
import android.util.Pair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import br.gov.sp.educacao.sed.mobile.Login.LoginDBcrud;
import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;

public class ConnectHandler
        extends AsyncTask<Object,
                          Integer,
                          RetornoJson> {

    private final String TAG = "ConnectHandler";

    private ParametroJson request;

    private LoginDBcrud loginDBcrud;

    @Override
    protected RetornoJson doInBackground(Object... params) {

        request = (ParametroJson) params[0];

        loginDBcrud = (LoginDBcrud) params[1];

        RetornoJson retornoJson;

        try {

            String tokenUsuario = loginDBcrud.getTokenUsuario();

            if(!UrlServidor.URL_LOGIN.equals(request.getUrlServico()) && !tokenUsuario.equals("")) {

                request.addHeader("Authorization", tokenUsuario);
            }
            HttpsURLConnection httpsURLConnection = UrlIO.open(request.getUrlServico());

            String requestType = request.getRequestType();

            httpsURLConnection.setRequestMethod(requestType);

            if(requestType.equals("POST")) {

                httpsURLConnection.setDoOutput(true);

                httpsURLConnection.setRequestProperty("Content-type", "application/json");
            }
            List<Pair<String, String>> requestHeaders = request.getHeaders();

            int i;

            int requestHeadersSize = requestHeaders.size();

            for(i = 0; i < requestHeadersSize; i++) {

                Pair<String, String> header = requestHeaders.get(i);

                httpsURLConnection.setRequestProperty(header.first, header.second);
            }
            httpsURLConnection.connect();

            if(requestType.equals("POST")) {

                OutputStream out = httpsURLConnection.getOutputStream();

                out.write(request.getJsonObject().getBytes("UTF-8"));

                out.close();
            }
            String result = "";

            if(httpsURLConnection.getResponseCode() != 204) {

                InputStream inputStream = httpsURLConnection.getInputStream();

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                int character = bufferedReader.read();

                while(character != -1) {

                    result += (char)character;

                    character = bufferedReader.read();
                }
                bufferedReader.close();

                inputStreamReader.close();

                inputStream.close();
            }
            retornoJson = new RetornoJson(httpsURLConnection.getResponseCode(), result);

            httpsURLConnection.disconnect();
        }
        catch(Throwable e) {

            retornoJson = new RetornoJson(999, e.getMessage());

            e.printStackTrace();

            CrashAnalytics.e(TAG, e);
        }
        return retornoJson;
    }
}