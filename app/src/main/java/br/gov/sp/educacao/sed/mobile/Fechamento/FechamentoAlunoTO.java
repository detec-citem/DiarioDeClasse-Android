package br.gov.sp.educacao.sed.mobile.Fechamento;

import android.content.ContentValues;

import br.gov.sp.educacao.sed.mobile.util.GenericsTable;

public class FechamentoAlunoTO
        implements GenericsTable {

    public static final String nomeTabela = "FECHAMENTO_ALUNO";

    private boolean confirmado;
    private int codigoFechamento;
    private int codigoTurma;
    private int codigoDisciplina;
    private int codigoTipoFechamento;
    private int nota;
    private int faltas;
    private int ausenciasCompensadas;
    private int faltasAcumuladas;
    private String codigoMatricula;
    private String justificativa;
    private String dataServidor;

    public String getDataServidor() {
        return dataServidor;
    }

    public void setDataServidor(String dataServidor) {
        this.dataServidor = dataServidor;
    }

    public void setConfirmado(boolean confirmado) {
        this.confirmado = confirmado;
    }

    public void setCodigoFechamento(int codigoFechamento) {
        this.codigoFechamento = codigoFechamento;
    }

    public int getCodigoTurma() {
        return codigoTurma;
    }

    public void setCodigoTurma(int codigoTurma) {
        this.codigoTurma = codigoTurma;
    }

    public int getCodigoDisciplina() {
        return codigoDisciplina;
    }

    public void setCodigoDisciplina(int codigoDisciplina) {
        this.codigoDisciplina = codigoDisciplina;
    }

    public int getCodigoTipoFechamento() {
        return codigoTipoFechamento;
    }

    public void setCodigoTipoFechamento(int codigoTipoFechamento) {
        this.codigoTipoFechamento = codigoTipoFechamento;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public int getFaltas() {
        return faltas;
    }

    public void setFaltas(int faltas) {
        this.faltas = faltas;
    }

    public void setAusenciasCompensadas(int ausenciasCompensadas) {
        this.ausenciasCompensadas = ausenciasCompensadas;
    }

    public void setFaltasAcumuladas(int faltasAcumuladas) {
        this.faltasAcumuladas = faltasAcumuladas;
    }

    public String getCodigoMatricula() {
        return codigoMatricula;
    }

    public void setCodigoMatricula(String codigoMatricula) {
        this.codigoMatricula = codigoMatricula;
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

        values.put("codigoFechamento", codigoFechamento);
        values.put("codigoTurma", codigoTurma);
        values.put("codigoMatricula", codigoMatricula);
        values.put("codigoDisciplina", codigoDisciplina);
        values.put("codigoTipoFechamento", codigoTipoFechamento);
        values.put("nota", nota);
        values.put("faltas", faltas);
        values.put("ausenciasCompensadas", ausenciasCompensadas);
        values.put("faltasAcumuladas", faltasAcumuladas);
        values.put("justificativa", justificativa);
        values.put("confirmado", confirmado);
        values.put("dataServidor", dataServidor);

        return values;
    }

    @Override
    public String getCodigoUnico() {
        return "codigoTurma = " + codigoTurma + " AND codigoDisciplina = " + codigoDisciplina + " AND codigoMatricula = " + codigoMatricula + " AND codigoTipoFechamento = " + codigoTipoFechamento;
    }
}