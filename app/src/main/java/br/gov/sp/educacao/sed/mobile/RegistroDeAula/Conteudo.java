package br.gov.sp.educacao.sed.mobile.RegistroDeAula;

public class Conteudo {

    private int codigo;
    private int codigoCurriculo;
    private String descricao;
    private boolean habilidadeCheck;

    public Conteudo(int codigo, int codigoCurriculo, String descricao) {

        this.codigo = codigo;
        this.codigoCurriculo = codigoCurriculo;
        this.descricao = descricao;

        habilidadeCheck = false;
    }

    public Conteudo() {

        habilidadeCheck = false;
    }

    public void setConteudo(int codigo, int codigoCurriculo, String descricao) {

        this.codigo = codigo;
        this.codigoCurriculo = codigoCurriculo;
        this.descricao = descricao;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigoCurriculo() {
        return codigoCurriculo;
    }

    public void setCodigoCurriculo(int codigoCurriculo) {
        this.codigoCurriculo = codigoCurriculo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isHabilidadeCheck() {
        return habilidadeCheck;
    }

    public void setHabilidadeCheck(boolean habilidadeCheck) {
        this.habilidadeCheck = habilidadeCheck;
    }
}
