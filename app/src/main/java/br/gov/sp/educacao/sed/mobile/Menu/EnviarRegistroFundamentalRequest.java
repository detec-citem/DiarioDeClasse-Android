package br.gov.sp.educacao.sed.mobile.Menu;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import br.gov.sp.educacao.sed.mobile.Login.UsuarioTO;
import br.gov.sp.educacao.sed.mobile.RegistroDeAula.RegistroAulaFundamental;
import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.ConexaoHttps.HttpsUrlConnectionFactory;
import br.gov.sp.educacao.sed.mobile.util.ConexaoHttps.HttpsUrlConnectionUtil;
import br.gov.sp.educacao.sed.mobile.util.CriarAcessoBanco;
import br.gov.sp.educacao.sed.mobile.util.Servidor.UrlServidor;

public class EnviarRegistroFundamentalRequest {
    //Variáveis
    private static int tentativas = 0;

    //Métodos
    public int enviarRegistroAula(int codigoTurma, String token, List<Integer> habilidadesRegistro, RegistroAulaFundamental registroAula, Context context) throws IOException, JSONException {
        int i;
        tentativas++;
        HttpsURLConnection conexao = HttpsUrlConnectionFactory.createHttpsUrlConnection("POST", UrlServidor.URL_REGISTRO_AULAS, token);
        int codigo = -1;
        int codigoRegistroAula = registroAula.getCodigoRegistroAula();
        if (codigoRegistroAula > -1) {
            codigo = codigoRegistroAula;
        }
        JSONArray horariosJson = new JSONArray();
        String horarios = registroAula.getHorarios();
        String[] horariosArray = horarios.split("-");
        int numeroHorarios = horariosArray.length;
        for (i = 0; i < numeroHorarios; i++) {
            String horario = horariosArray[i].replace("/", " às ");
            horariosJson.put(horario);
        }
        JSONArray conteudosJson = new JSONArray();
        if (habilidadesRegistro != null) {
            int numeroHabilidades = habilidadesRegistro.size();
            for (i = 0; i < numeroHabilidades; i++) {
                int codigoHabilidade = habilidadesRegistro.get(i);
                JSONArray habilidadesJson = new JSONArray().put(codigoHabilidade);
                JSONObject conteudoJson = new JSONObject().put("CodigoConteudo", codigoHabilidade).put("Habilidades", habilidadesJson);
                conteudosJson.put(conteudoJson);
            }
        }
        JSONObject registroAulaJson = new JSONObject().put("Ocorrencias", "").put("Codigo", codigo).put("CodigoDisciplina", registroAula.getDisciplina()).put("CodigoTurma", codigoTurma).put("Bimestre", registroAula.getBimestre()).put("DataCriacao", registroAula.getDataCriacao()).put("Observacoes", registroAula.getObservacoes()).put("Horarios", horariosJson).put("Conteudos", conteudosJson);
        Log.e("Teste", registroAulaJson.toString());
        byte[] registroJsonBytes = registroAulaJson.toString().getBytes("UTF-8");
        conexao.setFixedLengthStreamingMode(registroJsonBytes.length);
        OutputStream outputStream = conexao.getOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        bufferedOutputStream.write(registroJsonBytes);
        bufferedOutputStream.flush();
        outputStream.flush();
        conexao.connect();
        int statusCode = conexao.getResponseCode();
        if (statusCode == HttpsURLConnection.HTTP_BAD_REQUEST) {
            conexao.disconnect();
            if (tentativas == 1) {
                Banco banco = CriarAcessoBanco.gerarBanco(context);
                MenuDBgetters menuDBgetters = new MenuDBgetters(banco);
                MenuDBcrud menuDBcrud = new MenuDBcrud(banco);
                UsuarioTO usuario = menuDBgetters.getUsuarioAtivo();
                RevalidarToken2Request revalidarToken2Request = new RevalidarToken2Request();
                String novoToken = revalidarToken2Request.executeRequest(usuario.getUsuario(), usuario.getSenha());
                menuDBcrud.atualizarTokenUsuario(novoToken);
                return enviarRegistroAula(codigoTurma, token, habilidadesRegistro, registroAula, context);
            }
            else {
                return -1;
            }
        }
        else {
            String resultado = HttpsUrlConnectionUtil.readStringFromHttpsURLConnection(true, conexao);
            conexao.disconnect();
            JSONObject resultadoJson = new JSONObject(resultado);
            if (resultadoJson.has("Codigo") && !resultadoJson.isNull("Codigo")) {
                codigo = resultadoJson.getInt("Codigo");
            }
        }
        return codigo;
    }
}