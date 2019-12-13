package br.gov.sp.educacao.sed.mobile.RegistroDeAula;

import java.util.ArrayList;
import java.util.List;

public class Registro {

    private int codNovoRegistro;
    private int bimestre;
    private String codigoDisciplina;
    private String codigoTurma;
    private String ocorrencias;
    private String observacoes;
    private String codigoGrupoCurriculo;
    private String dataCriacao;
    private List<ObjetoConteudo> conteudos = new ArrayList<>();

    private List<String> horarios = new ArrayList<>();

    public Registro() {

    }

    public String getHorariosInsert() {

        String horariosString = "";

        for(int i = 0; i < horarios.size(); i++) {

            horariosString = horariosString + horarios.get(i) + "-";
        }

        return horariosString;
    }

    public List<String> getHorarios() {

        return horarios;
    }

    public void setHorariosFormatando(String horario) {

        String[] horarioSeparado = horario.split("/");

        String horarioFormatado = horarioSeparado[0] + " às " + horarioSeparado[1];

        if(!horarios.contains(horarioFormatado)) {

            horarios.add(horarioFormatado);
        }
    }

    public void setHorarios(String horario) {

        if(!horarios.contains(horario)) {

            horarios.add(horario);
        }
    }

    public List<ObjetoConteudo> getConteudos() {
        return conteudos;
    }

    public void setConteudos(List<ObjetoConteudo> conteudos) {
        this.conteudos = conteudos;
    }

    public int getCodNovoRegistro() {
        return codNovoRegistro;
    }

    public void setCodNovoRegistro(int codNovoRegistro) {
        this.codNovoRegistro = codNovoRegistro;
    }

    public void adicionaConteudos (ObjetoConteudo objetoConteudo)
    {
        conteudos.add(objetoConteudo);
    }

    public int checarConteudos()
    {
        return conteudos.size();
    }

    public ObjetoConteudo getConteudo(Integer position)
    {
        return conteudos.get(position);
    }

    public void limpaLista () {conteudos.clear();}

    public int getBimestre() {
        return bimestre;
    }

    public void setBimestre(int bimestre) {
        this.bimestre = bimestre;
    }

    public String getCodigoDisciplina() {
        return codigoDisciplina;
    }

    public void setCodigoDisciplina(String codigoDisciplina) {
        this.codigoDisciplina = codigoDisciplina;
    }

    public String getCodigoTurma() {
        return codigoTurma;
    }

    public void setCodigoTurma(String codigoTurma) {
        this.codigoTurma = codigoTurma;
    }

    public String getOcorrencias() {
        return ocorrencias;
    }

    public void setOcorrencias(String ocorrencias) {
        this.ocorrencias = ocorrencias;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getCodigoGrupoCurriculo() {
        return codigoGrupoCurriculo;
    }

    public void setCodigoGrupoCurriculo(String codigoGrupoCurriculo) {
        this.codigoGrupoCurriculo = codigoGrupoCurriculo;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}


