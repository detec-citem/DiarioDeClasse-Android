package br.gov.sp.educacao.sed.mobile.util.Servidor;

import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

class UrlIO {

    static HttpsURLConnection open(String url) throws IOException {

        HttpsURLConnection result = (HttpsURLConnection) new URL(url).openConnection();

        result.setConnectTimeout(50000000);

        result.setReadTimeout(50000000);

        return result;
    }
}
