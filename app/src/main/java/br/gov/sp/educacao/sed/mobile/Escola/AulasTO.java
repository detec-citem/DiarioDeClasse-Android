package br.gov.sp.educacao.sed.mobile.Escola;

import android.content.ContentValues;

import org.json.JSONException;
import org.json.JSONObject;

import br.gov.sp.educacao.sed.mobile.util.GenericsTable;

public class AulasTO implements GenericsTable {
    public static final String nomeTabela = "AULAS";

    //Variáveis
    private String inicioHora;
    private String fimHora;
    private Integer disciplina_id;

    //Métodos
    public void setJSON(JSONObject retorno, int disciplina_id) throws JSONException {
        this.disciplina_id = disciplina_id;
        inicioHora = retorno.getString("Inicio").trim();
        fimHora = retorno.getString("Fim").trim();
    }

    public Integer getDisciplina_id() {
        return disciplina_id;
    }

    public void setDisciplina_id(Integer disciplina_id) {
        this.disciplina_id = disciplina_id;
    }

    //GenericsTable
    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put("inicioHora", inicioHora);
        values.put("fimHora", fimHora);
        values.put("diaSemana", 0);
        values.put("codigoAula", 0);
        values.put("disciplina_id", disciplina_id);
        return values;
    }

    @Override
    public String getCodigoUnico() {
        return "inicioHora = '" + inicioHora + "' and fimHora = '" + fimHora + "' AND disciplina_id = " + disciplina_id;
    }
}