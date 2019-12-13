package br.gov.sp.educacao.sed.mobile.Avaliacao;

import android.content.ContentValues;

import java.text.SimpleDateFormat;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import br.gov.sp.educacao.sed.mobile.util.GenericsTable;

public class NotasAlunoTO
        implements GenericsTable {

    public static final String nomeTabela = "NOTASALUNO";
    private String codigoMatricula;
    private String dataCadastro;
    private String dataServidor;
    private String nota;
    private int aluno_id;
    private int avaliacao_id;
    private int usuario_id;

    public NotasAlunoTO() {

    }

    public void setJSON(JSONObject retorno, int aluno_id, int usuario_id, int avaliacao_id) throws JSONException {

        nota = retorno.getString("Nota").trim();

        if(nota.length() == 1) {

            nota = nota + ".0";
        }

        if(nota.length() == 3
                || nota.indexOf(".") == 2) {

            nota = nota + "0";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

        String dataImportacao = dateFormat.format(new Date());

        dataCadastro = dataImportacao;
        dataServidor = dataImportacao;

        this.aluno_id = aluno_id;
        this.avaliacao_id = avaliacao_id;
        this.usuario_id = usuario_id;

        String json = retorno.toString();

        char[] chars = json.toCharArray();

        int charsLength = chars.length;

        StringBuilder matriculaStringBuilder = new StringBuilder();

        for (int i = json.indexOf("\"CodigoMatriculaAluno\":") + 23; i < charsLength; i++) {

            char c = chars[i];

            if (c != ',' && c != '}') {

                matriculaStringBuilder.append(c);
            }
            else {

                break;
            }
        }
        codigoMatricula = matriculaStringBuilder.toString();
    }

    public NotasAlunoTO(JSONObject retorno,
                        int aluno_id,
                        int usuario_id,
                        int avaliacao_id) throws JSONException {

        nota = retorno.getString("Nota").trim();

        if(nota.length() == 1) {

            nota = nota + ".0";
        }

        if(nota.length() == 3
                || nota.indexOf(".") == 2) {

            nota = nota + "0";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

        String dataImportacao = dateFormat.format(new Date());

        dataCadastro = dataImportacao;
        dataServidor = dataImportacao;

        this.aluno_id = aluno_id;
        this.avaliacao_id = avaliacao_id;
        this.usuario_id = usuario_id;

        String json = retorno.toString();

        char[] chars = json.toCharArray();

        int charsLength = chars.length;

        StringBuilder matriculaStringBuilder = new StringBuilder();

        for (int i = json.indexOf("\"CodigoMatriculaAluno\":") + 23; i < charsLength; i++) {

            char c = chars[i];

            if (c != ',' && c != '}') {

                matriculaStringBuilder.append(c);
            }
            else {

                break;
            }
        }
        codigoMatricula = matriculaStringBuilder.toString();
    }

    public String getCodigoMatricula() {
        return codigoMatricula;
    }

    public void setCodigoMatricula(String codigoMatricula) {

        this.codigoMatricula = codigoMatricula;
    }

    public String getDataServidor() {
        return dataServidor;
    }

    public void setDataServidor(String dataServidor) {
        this.dataServidor = dataServidor;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    @Override
    public ContentValues getContentValues() {

        ContentValues values = new ContentValues();

        values.put("codigoMatricula", codigoMatricula);
        values.put("dataCadastro", dataCadastro);
        values.put("dataServidor", dataServidor);
        values.put("nota", nota);
        values.put("aluno_id", aluno_id);
        values.put("usuario_id", usuario_id);
        values.put("avaliacao_id", avaliacao_id);

        return values;
    }

    @Override
    public String getCodigoUnico() {

        return "aluno_id = " + aluno_id + " and avaliacao_id = " + avaliacao_id;
    }
}