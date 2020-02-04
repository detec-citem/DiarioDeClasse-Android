package br.gov.sp.educacao.sed.mobile.Turmas;

import android.content.ContentValues;

import org.json.JSONException;
import org.json.JSONObject;

import br.gov.sp.educacao.sed.mobile.util.GenericsTable;

public class TurmasTO implements GenericsTable {

    //Constantes
    public static final String nomeTabela = "TURMAS";

    //Variáveis
    private Integer codigoTurma;
    private String nomeTurma;
    private Integer serieTurma;
    private Integer codigoDiretoria;
    private String nomeDiretoria;
    private Integer codigoEscola;
    private String nomeEscola;
    private Integer codigoTipoEnsino;
    private String nomeTipoEnsino;
    private Integer anoLetivo;

    //Métodos
    public void setJSON(JSONObject jsonObject, int ano) throws JSONException {
        anoLetivo = ano;
        codigoTurma = jsonObject.getInt("CodigoTurma");
        nomeTurma = jsonObject.getString("NomeTurma").trim();
        serieTurma = jsonObject.getInt("Serie");
        codigoDiretoria = jsonObject.getInt("CodigoDiretoria");
        nomeDiretoria = jsonObject.getString("NomeDiretoria").trim();
        codigoEscola = jsonObject.getInt("CodigoEscola");
        nomeEscola = jsonObject.getString("NomeEscola").trim();
        codigoTipoEnsino = jsonObject.getInt("CodigoTipoEnsino");
        nomeTipoEnsino = jsonObject.getString("NomeTipoEnsino").trim();
    }

    public Integer getCodigoTurma() {
        return codigoTurma;
    }

    public void setCodigoTurma(Integer codigoTurma) {
        this.codigoTurma = codigoTurma;
    }

    public String getNomeTurma() {
        return nomeTurma;
    }

    public void setNomeTurma(String nomeTurma) {
        this.nomeTurma = nomeTurma;
    }

    public Integer getCodigoTipoEnsino() {
        return codigoTipoEnsino;
    }

    public void setCodigoTipoEnsino(Integer codigoTipoEnsino) {
        this.codigoTipoEnsino = codigoTipoEnsino;
    }

    //GenericsTable
    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put("codigoTurma", codigoTurma);
        values.put("nomeTurma", nomeTurma);
        values.put("serieTurma", serieTurma);
        values.put("codigoDiretoria", codigoDiretoria);
        values.put("nomeDiretoria", nomeDiretoria);
        values.put("codigoEscola", codigoEscola);
        values.put("nomeEscola", nomeEscola);
        values.put("codigoTipoEnsino", codigoTipoEnsino);
        values.put("nomeTipoEnsino", nomeTipoEnsino);
        values.put("anoLetivo", anoLetivo);
        return values;
    }

    @Override
    public String getCodigoUnico() {
        return "codigoTurma = " + codigoTurma;
    }
}