package br.gov.sp.educacao.sed.mobile.Menu;

public class CurriculoFundamental {
    //Variáveis
    private int codigoCurriculo;
    private int ano;
    private int serie;
    private int disciplina;

    //Construtor
    public CurriculoFundamental(int codigoCurriculo, int ano, int serie, int disciplina) {
        this.codigoCurriculo = codigoCurriculo;
        this.ano = ano;
        this.serie = serie;
        this.disciplina = disciplina;
    }

    //Métodos
    public int getCodigoCurriculo() {
        return codigoCurriculo;
    }

    public int getAno() {
        return ano;
    }

    public int getSerie() {
        return serie;
    }


    public int getDisciplina() {
        return disciplina;
    }
}