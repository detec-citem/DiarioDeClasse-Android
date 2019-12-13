package br.gov.sp.educacao.sed.mobile.util.Servidor;

import android.content.Context;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class ParametroJson {

    public static final String JSON_OBJECT_VAZIO = "{}";
    public static final String JSON_ARRAY_VAZIO = "[]";

    private String requestType;
    private String urlServico;
    private String jsonObject;
    private Context context;

    private final List<Pair<String, String>> headers = new ArrayList<>();

    public ParametroJson(String requestType) {

        this.requestType = requestType;
    }

    public String getUrlServico() {

        return urlServico;
    }

    public void setUrlServico(String urlServico) {

        this.urlServico = urlServico;
    }

    public Context getContext() {

        return context;
    }

    public void setContext(Context context) {

        this.context = context;
    }

    public String getJsonObject() {

        return jsonObject;
    }

    public void setJsonObject(String jsonObject) {

        this.jsonObject = jsonObject;
    }

    public String getRequestType() {

        return requestType;
    }

    public void addHeader(String key,
                          String value) {

        headers.add(new Pair<String, String>(key, value));
    }

    public List<Pair<String,
                     String>> getHeaders() {

        return headers;
    }
}