package br.gov.sp.educacao.sed.mobile.Avaliacao;

import android.content.ContentValues;

import java.text.SimpleDateFormat;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import br.gov.sp.educacao.sed.mobile.util.GenericsTable;

public class AvaliacoesTO
        implements GenericsTable {

    public static final String nomeTabela = "AVALIACOES";

    private Integer codigoAvaliacao;
    private Integer codigoTurma;
    private Integer codigoDisciplina;
    private Integer codigoTipoAtividade;
    private String nome;
    private String data;
    private Integer bimestre;
    private Integer valeNota;
    private Integer mobileId;
    private Integer turma_id;
    private Integer disciplina_id;
    private String dataServidor;

    public AvaliacoesTO() {

    }

    public void setJSON(JSONObject retorno, int turma_id, int disciplina_id) throws JSONException {

        setCodigoAvaliacao(retorno.getInt("Codigo"));
        setCodigoTurma(retorno.getInt("CodigoTurma"));
        setCodigoDisciplina(retorno.getInt("CodigoDisciplina"));
        setCodigoTipoAtividade(retorno.getInt("CodigoTipoAtividade"));
        setNome(retorno.getString("Nome").trim());
        setData(retorno.getString("Data").trim());
        setBimestre(retorno.getInt("Bimestre"));
        setValeNota(retorno.getBoolean("ValeNota") ? 1 : 0);
        setMobileId(retorno.getInt("MobileId"));
        setTurma_id(turma_id);
        setDisciplina_id(disciplina_id);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dataServidor = dateFormat.format(new Date());
    }

    public AvaliacoesTO(JSONObject retorno, int turma_id, int disciplina_id) throws JSONException {

        setCodigoAvaliacao(retorno.getInt("Codigo"));
        setCodigoTurma(retorno.getInt("CodigoTurma"));
        setCodigoDisciplina(retorno.getInt("CodigoDisciplina"));
        setCodigoTipoAtividade(retorno.getInt("CodigoTipoAtividade"));
        setNome(retorno.getString("Nome").trim());
        setData(retorno.getString("Data").trim());
        setBimestre(retorno.getInt("Bimestre"));
        setValeNota(retorno.getBoolean("ValeNota") ? 1 : 0);
        setMobileId(retorno.getInt("MobileId"));
        setTurma_id(turma_id);
        setDisciplina_id(disciplina_id);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dataServidor = dateFormat.format(new Date());
    }

    public Integer getCodigoAvaliacao() {
        return codigoAvaliacao;
    }

    public void setCodigoAvaliacao(Integer codigoAvaliacao) {
        this.codigoAvaliacao = codigoAvaliacao;
    }

    public Integer getCodigoTurma() {
        return codigoTurma;
    }

    public void setCodigoTurma(Integer codigoTurma) {
        this.codigoTurma = codigoTurma;
    }

    public Integer getCodigoDisciplina() {
        return codigoDisciplina;
    }

    public void setCodigoDisciplina(Integer codigoDisciplina) {
        this.codigoDisciplina = codigoDisciplina;
    }

    public Integer getCodigoTipoAtividade() {
        return codigoTipoAtividade;
    }

    public void setCodigoTipoAtividade(Integer codigoTipoAtividade) {
        this.codigoTipoAtividade = codigoTipoAtividade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getBimestre() {
        return bimestre;
    }

    public void setBimestre(Integer bimestre) {
        this.bimestre = bimestre;
    }

    public Integer getValeNota() {
        return valeNota;
    }

    public void setValeNota(Integer valeNota) {
        this.valeNota = valeNota;
    }

    public Integer getMobileId() {
        return mobileId;
    }

    public void setMobileId(Integer mobileId) {
        this.mobileId = mobileId;
    }

    public Integer getTurma_id() {
        return turma_id;
    }

    public void setTurma_id(Integer turma_id) {
        this.turma_id = turma_id;
    }

    public Integer getDisciplina_id() {
        return disciplina_id;
    }

    public void setDisciplina_id(Integer disciplina_id) {
        this.disciplina_id = disciplina_id;
    }

    public String getDataServidor() {
        return dataServidor;
    }

    public void setDataServidor(String dataServidor) {
        this.dataServidor = dataServidor;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();

        values.put("codigoAvaliacao", codigoAvaliacao);
        values.put("codigoTurma", codigoTurma);
        values.put("codigoDisciplina", codigoDisciplina);
        values.put("codigoTipoAtividade", codigoTipoAtividade);
        values.put("nome", nome);
        values.put("data", data);
        values.put("bimestre", bimestre);
        values.put("valeNota", valeNota);
        values.put("mobileId", mobileId);
        values.put("turma_id", turma_id);
        values.put("disciplina_id", disciplina_id);
        values.put("dataServidor", dataServidor);

        return values;
    }

    @Override
    public String getCodigoUnico() {

        return " codigoAvaliacao = " + codigoAvaliacao + " AND disciplina_id = " + disciplina_id + ";";
    }
}