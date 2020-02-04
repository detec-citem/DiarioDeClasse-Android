package br.gov.sp.educacao.sed.mobile.Menu;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import br.gov.sp.educacao.sed.mobile.Login.UsuarioTO;
import br.gov.sp.educacao.sed.mobile.Turmas.Turma;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;
import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.ConexaoHttps.HttpsUrlConnectionFactory;
import br.gov.sp.educacao.sed.mobile.util.ConexaoHttps.HttpsUrlConnectionUtil;
import br.gov.sp.educacao.sed.mobile.util.CriarAcessoBanco;
import br.gov.sp.educacao.sed.mobile.util.Servidor.UrlServidor;

public class CurriculosRequest {
    //Métodos
    public static JSONObject buscarCurriculos(String token, List<TurmaGrupo> turmas, Context context) throws IOException, JSONException {
        int i;
        int numeroTurmas = turmas.size();
        JSONArray turmasJsonArray = new JSONArray();
        for (i = 0; i < numeroTurmas; i++) {
            TurmaGrupo turmaGrupo = turmas.get(i);
            Turma turma = turmaGrupo.getTurma();
            JSONObject turmaJson = new JSONObject();
            turmaJson.put("Ano", turma.getAno());
            turmaJson.put("Serie", turma.getSerie());
            turmaJson.put("CodigoTipoEnsino", turma.getCodigoTipoEnsino());
            turmaJson.put("CodigoDisciplina", turmaGrupo.getDisciplina().getCodigoDisciplina());
            turmasJsonArray.put(turmaJson);
        }
        JSONObject turmasJson = new JSONObject().put("Turmas", turmasJsonArray);
        byte[] turmasJsonBytes = turmasJson.toString().getBytes("UTF-8");
        HttpsURLConnection connection = HttpsUrlConnectionFactory.createHttpsUrlConnection("POST", UrlServidor.URL_CURRICULOS, token);
        connection.setFixedLengthStreamingMode(turmasJsonBytes.length);
        OutputStream outputStream = connection.getOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        bufferedOutputStream.write(turmasJsonBytes);
        bufferedOutputStream.flush();
        outputStream.flush();
        bufferedOutputStream.close();
        outputStream.close();
        connection.connect();
        int statusCode = connection.getResponseCode();
        if(statusCode == HttpsURLConnection.HTTP_OK) {
            String curriculosResponse = HttpsUrlConnectionUtil.readStringFromHttpsURLConnection(true, connection);
            connection.disconnect();
            JSONObject curriculosJson = new JSONObject(curriculosResponse);
            return curriculosJson;
        }
        if (statusCode == HttpURLConnection.HTTP_BAD_REQUEST) {
            Banco banco = CriarAcessoBanco.gerarBanco(context);
            MenuDBgetters menuDBgetters = new MenuDBgetters(banco);
            MenuDBcrud menuDBcrud = new MenuDBcrud(banco);
            UsuarioTO usuario = menuDBgetters.getUsuarioAtivo();
            RevalidarToken2Request revalidarToken2Request = new RevalidarToken2Request();
            String novoToken = revalidarToken2Request.executeRequest(usuario.getUsuario(), usuario.getSenha());
            menuDBcrud.atualizarTokenUsuario(novoToken);
            return buscarCurriculos(novoToken, turmas, context);
        }
        return null;
    }
}