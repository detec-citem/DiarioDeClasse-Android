package br.gov.sp.educacao.sed.mobile.Escola;

import android.content.ContentValues;

import org.json.JSONException;
import org.json.JSONObject;

import br.gov.sp.educacao.sed.mobile.util.GenericsTable;

public class DisciplinaTO
        implements GenericsTable {

    public static final String nomeTabela = "DISCIPLINA";

    private Integer codigoDisciplina;
    private String nomeDisciplina;
    private Integer turmasFrequencia_id;

    public DisciplinaTO() {

    }

    public void setJSON(JSONObject retorno, int turmasFrequencia_id) throws JSONException {

        setCodigoDisciplina(retorno.getInt("CodigoDisciplina"));
        setNomeDisciplina(retorno.getString("NomeDisciplina").trim());

        setTurmasFrequencia_id(turmasFrequencia_id);
    }

    public DisciplinaTO(JSONObject retorno, Integer turmasFrequencia_id) throws JSONException {

        setCodigoDisciplina(retorno.getInt("CodigoDisciplina"));
        setNomeDisciplina(retorno.getString("NomeDisciplina").trim());

        setTurmasFrequencia_id(turmasFrequencia_id);
    }

    public Integer getCodigoDisciplina() {
        return codigoDisciplina;
    }

    public void setCodigoDisciplina(Integer codigoDisciplina) {
        this.codigoDisciplina = codigoDisciplina;
    }

    public String getNomeDisciplina() {
        return nomeDisciplina;
    }

    public void setNomeDisciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }

    public Integer getTurmasFrequencia_id() {
        return turmasFrequencia_id;
    }

    public void setTurmasFrequencia_id(Integer turma_id) {
        this.turmasFrequencia_id = turma_id;
    }

    @Override
    public ContentValues getContentValues() {

        ContentValues values = new ContentValues();

        values.put("codigoDisciplina", getCodigoDisciplina());
        values.put("nomeDisciplina", getNomeDisciplina());
        values.put("turmasFrequencia_id", getTurmasFrequencia_id());

        return values;
    }

    @Override
    public String getCodigoUnico() {

        return "codigoDisciplina = " + getCodigoDisciplina() + " AND turmasFrequencia_id = " + getTurmasFrequencia_id();
    }
}