package br.gov.sp.educacao.sed.mobile.Fechamento;

import android.content.ContentValues;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

import br.gov.sp.educacao.sed.mobile.util.GenericsTable;
import br.gov.sp.educacao.sed.mobile.util.DateUtils;

public class TipoFechamentoTO
        implements GenericsTable {

    public static final String nomeTabela = "TIPO_FECHAMENTO";

    private Integer codigoTipoFechamento;
    private Integer ano;
    private String nome;
    private String inicio;
    private String fim;

    public TipoFechamentoTO() {

    }

    public TipoFechamentoTO(JSONObject retorno) throws JSONException {

        setAno(retorno.getInt("Ano"));
        setCodigoTipoFechamento(retorno.getInt("CodigoTipoFechamento"));
        setNome(retorno.getString("Nome"));
        setInicio(retorno.getString("Inicio"));
        setFim(retorno.getString("Fim"));
    }

    public void setJSON(JSONObject retorno) throws JSONException {

        setAno(retorno.getInt("Ano"));
        setCodigoTipoFechamento(retorno.getInt("CodigoTipoFechamento"));
        setNome(retorno.getString("Nome"));
        setInicio(retorno.getString("Inicio"));
        setFim(retorno.getString("Fim"));
    }

    public Integer getCodigoTipoFechamento() {
        return codigoTipoFechamento;
    }

    public void setCodigoTipoFechamento(Integer codigoTipoFechamento) {
        this.codigoTipoFechamento = codigoTipoFechamento;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    @Override
    public ContentValues getContentValues() {

        ContentValues values = new ContentValues();

        values.put("codigoTipoFechamento", getCodigoTipoFechamento());
        values.put("nome", getNome());
        values.put("ano", getAno());
        try {

            values.put("inicio", DateUtils.convertBarraParaTraco(getInicio()));

            values.put("fim", DateUtils.convertBarraParaTraco(getFim()));
        }
        catch (ParseException e) {

            e.printStackTrace();
        }
        return values;
    }

    @Override
    public String getCodigoUnico() {
        return "codigoTipoFechamento = " + getCodigoTipoFechamento();
    }
}