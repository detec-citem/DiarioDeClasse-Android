package br.gov.sp.educacao.sed.mobile.Fechamento;

import android.content.ContentValues;

import br.gov.sp.educacao.sed.mobile.util.GenericsTable;

public class FechamentoTurmaTO
        implements GenericsTable {

    public static final String nomeTabela = "FECHAMENTO_TURMA";

    private Integer codigoTurma;
    private Integer codigoDisciplina;
    private Integer codigoTipoFechamento;
    private Integer aulasRealizadas;
    private Integer aulasPlanejadas;
    private String justificativa;
    private String dataServidor;

    public Integer getCodigoTurma() {
        return codigoTurma;
    }

    public void setCodigoTurma(Integer codigoTurma) {
        this.codigoTurma = codigoTurma;
    }

    public Integer getCodigoDisciplina() {
        return codigoDisciplina;
    }

    public String getDataServidor() {
        return dataServidor;
    }

    public void setDataServidor(String dataServidor) {
        this.dataServidor = dataServidor;
    }

    public void setCodigoDisciplina(Integer codigoDisciplina) {
        this.codigoDisciplina = codigoDisciplina;
    }

    public Integer getCodigoTipoFechamento() {
        return codigoTipoFechamento;
    }

    public void setCodigoTipoFechamento(Integer codigoTipoFechamento) {
        this.codigoTipoFechamento = codigoTipoFechamento;
    }

    public Integer getAulasRealizadas() {
        return aulasRealizadas;
    }

    public void setAulasRealizadas(Integer aulasRealizadas) {
        this.aulasRealizadas = aulasRealizadas;
    }

    public Integer getAulasPlanejadas() {
        return aulasPlanejadas;
    }

    public void setAulasPlanejadas(Integer aulasPlanejadas) {
        this.aulasPlanejadas = aulasPlanejadas;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    @Override
    public ContentValues getContentValues() {

        ContentValues values = new ContentValues();

        values.put("codigoTurma", getCodigoTurma());
        values.put("codigoDisciplina", getCodigoDisciplina());
        values.put("codigoTipoFechamento", getCodigoTipoFechamento());
        values.put("aulasRealizadas", getAulasRealizadas());
        values.put("aulasPlanejadas", getAulasPlanejadas());
        values.put("justificativa", getJustificativa());
        values.put("dataServidor", getDataServidor());

        return values;
    }

    @Override
    public String getCodigoUnico() {
        return "codigoTurma = " + getCodigoTurma() + " AND codigoDisciplina = " + getCodigoDisciplina() + " AND codigoTipoFechamento = " + getCodigoTipoFechamento();
    }
}