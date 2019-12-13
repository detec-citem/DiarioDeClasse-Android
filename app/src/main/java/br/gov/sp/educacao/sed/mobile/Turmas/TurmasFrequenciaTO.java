package br.gov.sp.educacao.sed.mobile.Turmas;

import android.content.ContentValues;

import org.json.JSONException;
import org.json.JSONObject;

import br.gov.sp.educacao.sed.mobile.util.GenericsTable;

public class TurmasFrequenciaTO implements GenericsTable {

    public static final String nomeTabela = "TURMASFREQUENCIA";

    private Integer codigoTurma;
    private Integer codigoDiretoria;
    private Integer codigoEscola;
    private Integer codigoTipoEnsino;
    private Integer aulasBimestre;
    private Integer aulasAno;
    private Integer aulasSemana;
    private Integer turma_id;

    public TurmasFrequenciaTO() {

    }

    public TurmasFrequenciaTO(JSONObject retorno, Integer turma_id) throws JSONException {

        setCodigoTurma(retorno.getInt("CodigoTurma"));
        setCodigoDiretoria(retorno.getInt("CodigoDiretoria"));
        setCodigoEscola(retorno.getInt("CodigoEscola"));
        setCodigoTipoEnsino(retorno.getInt("CodigoTipoEnsino"));
        setAulasBimestre(retorno.getInt("AulasBimestre"));
        setAulasAno(retorno.getInt("AulasAno"));

        setTurma_id(turma_id);
    }

    public void setJSON(JSONObject retorno, int turma_id) throws JSONException {

        setCodigoTurma(retorno.getInt("CodigoTurma"));
        setCodigoDiretoria(retorno.getInt("CodigoDiretoria"));
        setCodigoEscola(retorno.getInt("CodigoEscola"));
        setCodigoTipoEnsino(retorno.getInt("CodigoTipoEnsino"));
        setAulasBimestre(retorno.getInt("AulasBimestre"));
        setAulasAno(retorno.getInt("AulasAno"));
        setAulasSemana(retorno.getInt("NumeroAulasPorSemana"));

        setTurma_id(turma_id);
    }

    public Integer getAulasSemana() {
        return aulasSemana;
    }

    public void setAulasSemana(Integer aulasSemana) {
        this.aulasSemana = aulasSemana;
    }

    public Integer getCodigoTurma() {
        return codigoTurma;
    }

    public void setCodigoTurma(Integer codigoTurma) {
        this.codigoTurma = codigoTurma;
    }

    public Integer getCodigoDiretoria() {
        return codigoDiretoria;
    }

    public void setCodigoDiretoria(Integer codigoDiretoria) {
        this.codigoDiretoria = codigoDiretoria;
    }

    public Integer getCodigoEscola() {
        return codigoEscola;
    }

    public void setCodigoEscola(Integer codigoEscola) {
        this.codigoEscola = codigoEscola;
    }

    public Integer getCodigoTipoEnsino() {
        return codigoTipoEnsino;
    }

    public void setCodigoTipoEnsino(Integer codigoTipoEnsino) {
        this.codigoTipoEnsino = codigoTipoEnsino;
    }

    public Integer getAulasBimestre() {
        return aulasBimestre;
    }

    public void setAulasBimestre(Integer aulasBimestre) {
        this.aulasBimestre = aulasBimestre;
    }

    public Integer getAulasAno() {
        return aulasAno;
    }

    public void setAulasAno(Integer aulasAno) {
        this.aulasAno = aulasAno;
    }

    public Integer getTurma_id() {
        return turma_id;
    }

    public void setTurma_id(Integer turma_id) {
        this.turma_id = turma_id;
    }

    @Override
    public ContentValues getContentValues() {

        ContentValues values = new ContentValues();

        values.put("codigoTurma", getCodigoTurma());
        values.put("codigoDiretoria", getCodigoDiretoria());
        values.put("codigoEscola", getCodigoEscola());
        values.put("codigoTipoEnsino", getCodigoTipoEnsino());
        values.put("aulasBimestre", getAulasBimestre());
        values.put("aulasAno", getAulasAno());
        values.put("aulasSemana", getAulasSemana());
        values.put("turma_id", getTurma_id());

        return values;
    }

    @Override
    public String getCodigoUnico() {
        return "turma_id = " + getTurma_id();
    }
}