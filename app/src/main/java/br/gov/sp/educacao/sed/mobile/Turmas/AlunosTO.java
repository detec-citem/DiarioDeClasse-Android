package br.gov.sp.educacao.sed.mobile.Turmas;

import android.content.ContentValues;

import org.json.JSONException;
import org.json.JSONObject;

import br.gov.sp.educacao.sed.mobile.util.GenericsTable;

public class AlunosTO
        implements GenericsTable {

    public static final String nomeTabela = "ALUNOS";

    private boolean alunoAtivo;
    private byte numeroChamada;
    private int codigoAluno;
    private int numeroRa;
    private int turma_id;
    private String codigoMatricula;
    private String digitoRa;
    private String ufRa;
    private String nomeAluno;
    private String pai;
    private String mae;
    private String nascimento;
    private String necessidadesEspeciais;

    public AlunosTO() {

    }

    public void setJSON(JSONObject retorno, int turma_id) throws JSONException {

        this.turma_id = turma_id;

        numeroChamada = (byte) retorno.getInt("NumeroChamada");
        codigoAluno = retorno.getInt("CodigoAluno");
        numeroRa = retorno.getInt("NumeroRa");
        digitoRa = retorno.getString("DigitoRa");
        ufRa = retorno.getString("UfRa");
        nomeAluno = retorno.getString("NomeAluno").trim();
        nascimento = retorno.getString("DtNascimento").trim();
        necessidadesEspeciais = retorno.getString("PossuiDeficiencia").trim();

        JSONObject pais = retorno.getJSONObject("Pais");

        pai = pais.getString("Pai").trim();
        mae = pais.getString("Mae").trim();

        if (retorno.getString("AlunoAtivo").trim().equals("Ativo")) {

            alunoAtivo = true;
        }
        else {

            alunoAtivo = false;
        }

        String json = retorno.toString();

        char[] chars = json.toCharArray();

        int charsLength = chars.length;

        StringBuilder matriculaStringBuilder = new StringBuilder();

        for(int i = json.indexOf("\"CodigoMatricula\":") + 18; i < charsLength; i++) {

            char c = chars[i];

            if (c != ','
                    && c != '}') {

                matriculaStringBuilder.append(c);
            }
            else {

                break;
            }
        }
        codigoMatricula = matriculaStringBuilder.toString();
    }

    public AlunosTO(JSONObject retorno, int turma_id) throws JSONException {

        this.turma_id = turma_id;

        numeroChamada = (byte) retorno.getInt("NumeroChamada");
        codigoAluno = retorno.getInt("CodigoAluno");
        numeroRa = retorno.getInt("NumeroRa");
        digitoRa = retorno.getString("DigitoRa");
        ufRa = retorno.getString("UfRa");
        nomeAluno = retorno.getString("NomeAluno").trim();
        nascimento = retorno.getString("DtNascimento").trim();
        necessidadesEspeciais = retorno.getString("PossuiDeficiencia").trim();

        JSONObject pais = retorno.getJSONObject("Pais");

        pai = pais.getString("Pai").trim();
        mae = pais.getString("Mae").trim();

        if (retorno.getString("AlunoAtivo").trim().equals("Ativo")) {

            alunoAtivo = true;
        }
        else {

            alunoAtivo = false;
        }

        String json = retorno.toString();

        char[] chars = json.toCharArray();

        int charsLength = chars.length;

        StringBuilder matriculaStringBuilder = new StringBuilder();

        for(int i = json.indexOf("\"CodigoMatricula\":") + 18; i < charsLength; i++) {

            char c = chars[i];

            if (c != ','
                    && c != '}') {

                matriculaStringBuilder.append(c);
            }
            else {

                break;
            }
        }
        codigoMatricula = matriculaStringBuilder.toString();
    }

    public byte getNumeroChamada() {

        return numeroChamada;
    }

    public int getTurma_id() {

        return turma_id;
    }

    public void setTurma_id(int turma_id) {

        this.turma_id = turma_id;
    }

    public String getCodigoMatricula() {

        return codigoMatricula;
    }

    public void setCodigoMatricula(String codigoMatricula) {

        this.codigoMatricula = codigoMatricula;
    }

    public String getNomeAluno() {

        return nomeAluno;
    }

    @Override
    public ContentValues getContentValues() {

        ContentValues values = new ContentValues();

        values.put("alunoAtivo", alunoAtivo);
        values.put("numeroChamada", numeroChamada);
        values.put("codigoAluno", codigoAluno);
        values.put("numeroRa", numeroRa);
        values.put("turma_id", turma_id);
        values.put("codigoMatricula", codigoMatricula);
        values.put("digitoRa", digitoRa);
        values.put("ufRa", ufRa);
        values.put("nomeAluno", nomeAluno);
        values.put("pai", pai);
        values.put("mae", mae);
        values.put("nascimento", nascimento);
        values.put("necessidadesEspeciais", necessidadesEspeciais);

        return values;
    }

    @Override
    public String getCodigoUnico() {

        return "codigoMatricula = " + codigoMatricula;
    }
}