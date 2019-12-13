package br.gov.sp.educacao.sed.mobile.Fechamento;

public class FechamentoTurma {

    private int codigoTurma;
    private int codigoDisciplina;
    private int codigoTipoFechamento;
    private int aulasRealizadas;
    private int aulasPlanejadas;
    private String justificativa;
    private String dataServidor;

    public String getDataServidor() {
        return dataServidor;
    }

    public void setDataServidor(String dataServidor) {
        this.dataServidor = dataServidor;
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

    public int getAulasRealizadas() {
        return aulasRealizadas;
    }

    public void setAulasRealizadas(int aulasRealizadas) {
        this.aulasRealizadas = aulasRealizadas;
    }

    public int getAulasPlanejadas() {
        return aulasPlanejadas;
    }

    public void setAulasPlanejadas(int aulasPlanejadas) {
        this.aulasPlanejadas = aulasPlanejadas;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }
}