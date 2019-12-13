package br.gov.sp.educacao.sed.mobile.Fechamento;

public class FechamentoData {

    private String ano;
    private String inicio;
    private String fim;
    private String nome;
    private String codigoTipoFechamento;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public FechamentoData(){}

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFim() {
        return fim;
    }

    public void setFim(String fim) {
        this.fim = fim;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigoTipoFechamento() {
        return codigoTipoFechamento;
    }

    public void setCodigoTipoFechamento(String codigoTipoFechamento) {
        this.codigoTipoFechamento = codigoTipoFechamento;
    }
}
