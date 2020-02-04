package br.gov.sp.educacao.sed.mobile.util.ConexaoHttps;

import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpsUrlConnectionFactory {

    public static HttpsURLConnection createHttpsUrlConnection(String httpMethod,
                                                              String url,
                                                              String token)
                                                              throws IOException {

        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) new URL(url).openConnection();

        httpsURLConnection.setConnectTimeout(0);

        httpsURLConnection.setReadTimeout(0);

        httpsURLConnection.setRequestMethod(httpMethod);



        if (httpMethod.equals("POST")) {
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setRequestProperty("Content-Type", "application/json");
        }
        if (token != null) {
            httpsURLConnection.setRequestProperty("Authorization", token);
        }
        return httpsURLConnection;
    }
}