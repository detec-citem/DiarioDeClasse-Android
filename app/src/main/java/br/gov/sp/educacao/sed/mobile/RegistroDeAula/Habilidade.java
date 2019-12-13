package br.gov.sp.educacao.sed.mobile.RegistroDeAula;

public class Habilidade {

    private int codigo;
    private int codigoConteudo;
    private String descricao;
    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Habilidade(int codigo,
                      int codigoConteudo,
                      String descricao) {

        this.codigo = codigo;

        this.codigoConteudo = codigoConteudo;

        Character c = '\u00BF';

        this.descricao = descricao.replace(c, ' ').trim();
    }

    public Habilidade() {}

    public void setHabilidade(int codigo, int codigoConteudo, String descricao) {

        this.codigo = codigo;

        this.codigoConteudo = codigoConteudo;

        Character c = '\u00BF';

        this.descricao = descricao.replace(c, ' ').trim();
    }

    public int getCodigo() {
        return codigo;
    }
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    public int getCodigoConteudo() {
        return codigoConteudo;
    }
    public void setCodigoConteudo(int codigoConteudo) {
        this.codigoConteudo = codigoConteudo;
    }
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {

        this.descricao = descricao;
    }
}
