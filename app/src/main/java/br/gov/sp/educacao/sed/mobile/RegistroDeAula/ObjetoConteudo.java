package br.gov.sp.educacao.sed.mobile.RegistroDeAula;

import java.util.ArrayList;
import java.util.List;

public class ObjetoConteudo {

    private int codigoConteudo;
    private List<Integer> codigosHabilidades = new ArrayList<>();

    public List<Integer> getCodigosHabilidades() {
        return codigosHabilidades;
    }

    public void setCodigosHabilidades(List<Integer> codigosHabilidades) {

        this.codigosHabilidades = codigosHabilidades;
    }

    public int getCodigoConteudo() {
        return codigoConteudo;
    }

    public void setCodigoConteudo(int codigoConteudo) {
        this.codigoConteudo = codigoConteudo;
    }

    public void adicionarCodigoHabilidade(Integer codigoHabilidade) {

        if(!codigosHabilidades.contains(codigoHabilidade)){

            codigosHabilidades.add(codigoHabilidade);
        }
    }

    public int checarHabilidades()
    {
        return codigosHabilidades.size();
    }

    public int getHabSelecionadas(Integer position)
    {
        return codigosHabilidades.get(position);
    }
}
