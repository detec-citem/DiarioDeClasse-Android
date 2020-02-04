package br.gov.sp.educacao.sed.mobile.Menu;

public class ConteudoFundamental {
    //Variáveis
    private boolean checado;
    private int codigoConteudo;
    private int codigoCurriculo;
    private int bimestre;
    private String campoDeAtuacao;
    private String descricao;
    private String praticaLinguagem;
    private String objetosConhecimento;

    //Construtor
    public ConteudoFundamental(boolean checado, int codigoConteudo, int codigoCurriculo, int bimestre, String campoDeAtuacao, String descricao, String praticaLinguagem, String objetosConhecimento) {
        this.checado = checado;
        this.codigoConteudo = codigoConteudo;
        this.codigoCurriculo = codigoCurriculo;
        this.bimestre = bimestre;
        this.campoDeAtuacao = campoDeAtuacao;
        this.descricao = descricao;
        this.praticaLinguagem = praticaLinguagem;
        this.objetosConhecimento = objetosConhecimento;
    }

    //Métodos
    public boolean getChecado() {
        return checado;
    }

    public void setChecado(boolean checado) {
        this.checado = checado;
    }

    public int getCodigoConteudo() {
        return codigoConteudo;
    }

    public void setCodigoConteudo(int codigoConteudo) {
        this.codigoConteudo = codigoConteudo;
    }

    public int getCodigoCurriculo() {
        return codigoCurriculo;
    }

    public void setCodigoCurriculo(int codigoCurriculo) {
        this.codigoCurriculo = codigoCurriculo;
    }

    public int getBimestre() {
        return bimestre;
    }

    public void setBimestre(int bimestre) {
        this.bimestre = bimestre;
    }

    public String getCampoDeAtuacao() {
        return campoDeAtuacao;
    }

    public void setCampoDeAtuacao(String campoDeAtuacao) {
        this.campoDeAtuacao = campoDeAtuacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPraticaLinguagem() {
        return praticaLinguagem;
    }

    public void setPraticaLinguagem(String praticaLinguagem) {
        this.praticaLinguagem = praticaLinguagem;
    }

    public String getObjetosConhecimento() {
        return objetosConhecimento;
    }

    public void setObjetosConhecimento(String objetosConhecimento) {
        this.objetosConhecimento = objetosConhecimento;
    }
}