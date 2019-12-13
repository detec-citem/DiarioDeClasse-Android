package br.gov.sp.educacao.sed.mobile.Fechamento;

public class FechamentoAluno {

    private boolean confirmado;
    private int codigoFechamento;
    private int codigoTurma;
    private int codigoDisciplina;
    private int codigoTipoFechamento;
    private int ausenciasCompensadas;
    private int faltas;
    private int faltasAcumuladas;
    private int nota;
    private String codigoMatricula;
    private String nomeAluno;

    public String getNomeAluno() {
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public int getCodigoFechamento() {
        return codigoFechamento;
    }

    public void setCodigoFechamento(int codigoFechamento) {
        this.codigoFechamento = codigoFechamento;
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public void setConfirmado(boolean confirmado) {
        this.confirmado = confirmado;
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

    public int getAusenciasCompensadas() {
        return ausenciasCompensadas;
    }

    public void setAusenciasCompensadas(int ausenciasCompensadas) {
        this.ausenciasCompensadas = ausenciasCompensadas;
    }

    public int getFaltas() {
        return faltas;
    }

    public void setFaltas(int faltas) {
        this.faltas = faltas;
    }

    public int getFaltasAcumuladas() {
        return faltasAcumuladas;
    }

    public void setFaltasAcumuladas(int faltasAcumuladas) {
        this.faltasAcumuladas = faltasAcumuladas;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public String getCodigoMatricula() {
        return codigoMatricula;
    }

    public void setCodigoMatricula(String codigoMatricula) {
        this.codigoMatricula = codigoMatricula;
    }
}
