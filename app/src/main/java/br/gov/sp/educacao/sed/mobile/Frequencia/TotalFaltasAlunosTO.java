package br.gov.sp.educacao.sed.mobile.Frequencia;

import android.content.ContentValues;

import org.json.JSONException;
import org.json.JSONObject;

import br.gov.sp.educacao.sed.mobile.util.GenericsTable;

public class TotalFaltasAlunosTO
        implements GenericsTable {

    public static final String nomeTabela = "TOTALFALTASALUNOS";

    private int faltasAnuais;
    private int faltasBimestre;
    private int faltasSequenciais;
    private int faltasBimestreAnterior;
    private int aluno_id;
    private int disciplina_id;
    private String codigoMatricula;

    public TotalFaltasAlunosTO() {

    }

    public void setJSON(JSONObject retorno, int disciplina_id, int aluno_id) throws  JSONException {

        faltasAnuais = retorno.getInt("FaltasAnuais");
        faltasBimestre = retorno.getInt("FaltasBimestre");
        faltasBimestreAnterior = retorno.getInt("FaltasBimestreAnterior");
        faltasSequenciais = retorno.getInt("FaltasSequenciais");

        this.aluno_id = aluno_id;
        this.disciplina_id = disciplina_id;

        String json = retorno.toString();
        char[] chars = json.toCharArray();
        int charsLength = chars.length;

        StringBuilder matriculaStringBuilder = new StringBuilder();

        for (int i = json.indexOf("\"CodigoMatricula\":") + 18; i < charsLength; i++) {

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

    public TotalFaltasAlunosTO(JSONObject retorno, int disciplina_id, int aluno_id) throws JSONException {

        faltasAnuais = retorno.getInt("FaltasAnuais");
        faltasBimestre = retorno.getInt("FaltasBimestre");
        faltasBimestreAnterior = retorno.getInt("FaltasBimestreAnterior");
        faltasSequenciais = retorno.getInt("FaltasSequenciais");

        this.aluno_id = aluno_id;
        this.disciplina_id = disciplina_id;

        String json = retorno.toString();
        char[] chars = json.toCharArray();
        int charsLength = chars.length;

        StringBuilder matriculaStringBuilder = new StringBuilder();

        for (int i = json.indexOf("\"CodigoMatricula\":") + 18; i < charsLength; i++) {

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

    @Override
    public ContentValues getContentValues() {

        ContentValues values = new ContentValues();

        values.put("faltasAnuais", faltasAnuais);
        values.put("faltasBimestre", faltasBimestre);
        values.put("faltasBimestreAnterior", faltasBimestreAnterior);
        values.put("faltasSequenciais", faltasSequenciais);
        values.put("aluno_id", aluno_id);
        values.put("disciplina_id", disciplina_id);
        values.put("codigoMatricula", codigoMatricula);

        return values;
    }

    @Override
    public String getCodigoUnico() {

        return "disciplina_id = " + disciplina_id + " and aluno_id = " + aluno_id;
    }
}