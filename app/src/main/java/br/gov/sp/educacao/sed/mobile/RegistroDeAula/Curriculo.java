package br.gov.sp.educacao.sed.mobile.RegistroDeAula;

public class Curriculo {

    private int codigoCurriculo;
    private int codigoGrupo;
    private int bimestre;

    public Curriculo(int codigoCurriculo, int codigoGrupo, int bimestre) {

        this.codigoCurriculo = codigoCurriculo;
        this.codigoGrupo = codigoGrupo;
        this.bimestre = bimestre;
    }

    public Curriculo() {}

    public void setCurriculo(int codigoCurriculo, int codigoGrupo, int bimestre) {

        this.codigoCurriculo = codigoCurriculo;
        this.codigoGrupo = codigoGrupo;
        this.bimestre = bimestre;
    }

    public int getCodigoCurriculo() {
        return codigoCurriculo;
    }

    public void setCodigoCurriculo(int codigoCurriculo) {
        this.codigoCurriculo = codigoCurriculo;
    }

    public int getCodigoGrupo() {
        return codigoGrupo;
    }

    public void setCodigoGrupo(int codigoGrupo) {
        this.codigoGrupo = codigoGrupo;
    }

    public int getBimestre() {
        return bimestre;
    }

    public void setBimestre(int bimestre) {
        this.bimestre = bimestre;
    }
}
