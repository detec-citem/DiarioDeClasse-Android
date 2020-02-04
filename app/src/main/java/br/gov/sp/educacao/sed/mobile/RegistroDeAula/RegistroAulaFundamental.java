package br.gov.sp.educacao.sed.mobile.RegistroDeAula;

public class RegistroAulaFundamental {
    //Variáveis
    private int codigoRegistroAula;
    private boolean sincronizado;
    private String dataCriacao;
    private String observacoes;
    private String horarios;
    private int bimestre;
    private int disciplina;
    private int turma;

    //Construtor
    public RegistroAulaFundamental(int codigoRegistroAula, boolean sincronizado, String dataCriacao, String observacoes, String horarios, int bimestre, int disciplina, int turma) {
        this.codigoRegistroAula = codigoRegistroAula;
        this.sincronizado = sincronizado;
        this.dataCriacao = dataCriacao;
        this.observacoes = observacoes;
        this.horarios = horarios;
        this.bimestre = bimestre;
        this.disciplina = disciplina;
        this.turma = turma;
    }

    public RegistroAulaFundamental(int codigoRegistroAula, boolean sincronizado, String dataCriacao, String observacoes, int bimestre, int disciplina, int turma) {
        this.codigoRegistroAula = codigoRegistroAula;
        this.sincronizado = sincronizado;
        this.dataCriacao = dataCriacao;
        this.observacoes = observacoes;
        this.bimestre = bimestre;
        this.disciplina = disciplina;
        this.turma = turma;
    }

    //Métodos

    public int getCodigoRegistroAula() {
        return codigoRegistroAula;
    }

    public void setCodigoRegistroAula(int codigoRegistroAula) {
        this.codigoRegistroAula = codigoRegistroAula;
    }

    public boolean getSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(boolean sincronizado) {
        this.sincronizado = sincronizado;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getHorarios() {
        return horarios;
    }

    public void setHorarios(String horarios) {
        this.horarios = horarios;
    }

    public int getBimestre() {
        return bimestre;
    }

    public void setBimestre(int bimestre) {
        this.bimestre = bimestre;
    }

    public int getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(int disciplina) {
        this.disciplina = disciplina;
    }

    public int getTurma() {
        return turma;
    }

    public void setTurma(int turma) {
        this.turma = turma;
    }
}