package br.gov.sp.educacao.sed.mobile.util.Servidor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class RetornoJson {

    public static final int TOKEN_EXPIRADO = 999;

    public static final int RETORNO_COM_SUCESSO = 200;

    public static final int RETORNO_COM_SUCESSO_SEM_RESPOSTA = 201;

    public static final int ERROR = 500;

    public static final int NAO_ENCONTRADO = 404;

    public static final int NAO_DISPONIVEL = 503;

    public static final int EMPTY = 100;

    private int statusRetorno;

    private String resultString;

    public RetornoJson(int statusRetorno,
                       String resultString) {

        this.statusRetorno = statusRetorno;

        this.resultString = resultString;
    }

    public String getResultString() {
        return resultString;
    }

    public int getStatusRetorno() {
        return statusRetorno;
    }

    public JSONObject getResultJson() {

        JSONObject jsonObject = new JSONObject();

        try {
            if (resultString != null
                    && !"".equals(resultString)) {

                Object json = null;

                json = new JSONTokener(resultString).nextValue();

                if (!(json instanceof JSONObject)) {

                    jsonObject = new JSONObject();

                    jsonObject.put("Lista", new JSONArray(resultString));
                }
                else {

                    jsonObject = new JSONObject(resultString);
                }
            }
        }
        catch(JSONException e ) {

            jsonObject = new JSONObject();

            try {

                jsonObject.put("Message", "Erro de servidor, tente novamente mais tarde!");
            }
            catch (JSONException e1) {

                e1.printStackTrace();
            }
        }
        return jsonObject;
    }
}