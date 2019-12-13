package br.gov.sp.educacao.sed.mobile.RegistroDeAula;

public class HabilidadeConteudo {

    private int CodigoConteudo;
    private int CodigoHabilidade;

    public HabilidadeConteudo() {

    }

    public HabilidadeConteudo(int codigoConteudo, int codigoHabilidade) {

        CodigoConteudo = codigoConteudo;
        CodigoHabilidade = codigoHabilidade;
    }

    public int getCodigoConteudo() {
        return CodigoConteudo;
    }
    public void setCodigoConteudo(int codigoConteudo) {
        CodigoConteudo = codigoConteudo;
    }
    public int getCodigoHabilidade() {
        return CodigoHabilidade;
    }
    public void setCodigoHabilidade(int codigoHabilidade) {
        CodigoHabilidade = codigoHabilidade;
    }

}
