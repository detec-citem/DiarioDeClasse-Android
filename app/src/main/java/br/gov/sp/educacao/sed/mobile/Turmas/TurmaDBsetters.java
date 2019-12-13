package br.gov.sp.educacao.sed.mobile.Turmas;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.text.SimpleDateFormat;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteStatement;
import android.database.sqlite.SQLiteDoneException;

import br.gov.sp.educacao.sed.mobile.Login.UsuarioTO;

import br.gov.sp.educacao.sed.mobile.util.GenericsTable;

import br.gov.sp.educacao.sed.mobile.Escola.AulasTO;
import br.gov.sp.educacao.sed.mobile.Escola.BimestreTO;
import br.gov.sp.educacao.sed.mobile.Escola.DisciplinaTO;
import br.gov.sp.educacao.sed.mobile.Escola.DiasLetivosTO;

import br.gov.sp.educacao.sed.mobile.Avaliacao.Avaliacao;
import br.gov.sp.educacao.sed.mobile.Avaliacao.AvaliacoesTO;
import br.gov.sp.educacao.sed.mobile.Avaliacao.NotasAlunoTO;
import br.gov.sp.educacao.sed.mobile.Avaliacao.AvaliacaoDBgetters;
import br.gov.sp.educacao.sed.mobile.Avaliacao.AvaliacaoDBsetters;

import br.gov.sp.educacao.sed.mobile.Fechamento.TipoFechamentoTO;
import br.gov.sp.educacao.sed.mobile.Fechamento.FechamentoAlunoTO;
import br.gov.sp.educacao.sed.mobile.Fechamento.FechamentoTurmaTO;

import br.gov.sp.educacao.sed.mobile.Frequencia.TotalFaltasAlunosTO;

import br.gov.sp.educacao.sed.mobile.RegistroDeAula.Conteudo;
import br.gov.sp.educacao.sed.mobile.RegistroDeAula.Registro;
import br.gov.sp.educacao.sed.mobile.RegistroDeAula.ObjetoConteudo;
import br.gov.sp.educacao.sed.mobile.RegistroDeAula.RegistroDBcrud;
import br.gov.sp.educacao.sed.mobile.RegistroDeAula.HabilidadeConteudo;

import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.Utils;
import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;

public class TurmaDBsetters {

    private final String TAG = TurmaDBsetters.class.getSimpleName() ;

    private Banco banco;

    private RegistroDBcrud registroDBcrud;

    private AvaliacaoDBsetters avaliacaoDBsetters;

    private AvaliacaoDBgetters avaliacaoDBgetters;

    //Atualizar Faltas

    private TotalFaltasAlunosTO totalFaltasAlunosTO;

    private JSONArray jsonarrayFaltasAlunos;

    private int numeroFaltasAlunos;

    private int disciplina_id;

    private JSONObject jsonObjectFaltasAlunos;

    private String json;

    private int charsLength;

    private Integer aluno_id;

    private String queryTotalFaltasAlunos;

    private SQLiteStatement statementTotalFaltasAlunos;

    private String queryDiasComFrequencia;

    private SQLiteStatement statementDiasComFrequencia;

    private String queryUpdateDiasComFrequencia;

    private SQLiteStatement statementUpdateDiasComFrequencia;

    private String dataFormatada;

    private String [] dataSeparada;

    private String dia;

    private String mes;

    //

    //
    // Convert Disciplina
    //

    private int disciplina_id_disc;

    private JSONArray jsonarrayAula;

    private int numeroAulas;

    private JSONObject jsonObjectAulas;

    private DisciplinaTO disciplinaTO;

    private AulasTO aulasTO;

    private String queryInsertDisciplina;

    private SQLiteStatement statementInsertDisciplina;

    private String queryInsertAulas;

    private SQLiteStatement statementInsertAulas;

    //

    //
    //Convert Turmas Avaliacoes
    //

    private int numeroAvaliacoes;

    private ArrayList<Integer> listaAvaliacaoJSON;

    private ArrayList<Avaliacao> listaAvaliacaoSQLite;

    private List<Integer> listaIds1;

    private int usuarioId;

    private JSONObject jsonObjectAvaliacoes;

    private int codigoTurma1;

    private int turmasFrequenciaId;

    private int turma_id;

    private int codigoDisciplinaJson;

    private int disciplina_id_avaliacao;

    private AvaliacoesTO avaliacoesTO;

    private int avaliacao_id;

    private String queryInsertAvaliacao;

    private SQLiteStatement statementInsertAvaliacao;

    private JSONArray jsonArrayNotas;

    private int numeroNotas;

    private JSONObject jsonObjNota;

    private String jsonAvaliacao;

    private NotasAlunoTO notasAlunoTO;

    private String queryInsertNotas;

    private SQLiteStatement statementInsertNotas;

    private int idColumnIndex;

    private int codigoMatriculaColumnIndex;

    //

    //
    //Bimestres Calendarios
    //

    private BimestreTO bimestreTO;

    private BimestreTO bimestreTO2;

    private BimestreTO bimestreTO3;

    private String queryBimestresCalendario;

    private SQLiteStatement statementBimestresCalendario;

    private String queryTemBimestreBanco;

    private SQLiteStatement statementTemBimestreBanco;

    private String queryBimestreId;

    private SQLiteStatement statementBimestreId;

    //

    //
    //Bimestres 2
    //

    private String queryAtualizarBimestreAtual;

    private SQLiteStatement statementAtualizarBimestreAtual;

    private String queryInsertBimestre;

    private SQLiteStatement statementInsertBimestre;

    private String queryAtualizarBimestre;

    private SQLiteStatement statementAtualizarBimestre;

    //

    //
    //Turmas Calendario
    //

    private int numeroElementosCalendario;

    private JSONObject jsonObjectCalendario;

    private JSONArray jsonArrayDias;

    private int numeroDiasLetivos;

    private DiasLetivosTO diasLetivosTO;

    private String queryDiasLetivos;

    private SQLiteStatement statementDiasLetivos;

    //

    //
    //GetTurmasFrequenciaId
    //

    private String queryTurmasFrequenciaId;

    private SQLiteStatement statementTurmasFrequenciaId;

    //

    //
    //BuscarDisciplinaCurriculo
    //

    private String queryDisciplinaCurriculo;

    private SQLiteStatement statementDisciplinaCurriculo;

    //

    //
    //GetDisciplinaId
    //

    private String queryDisciplinaId;

    private SQLiteStatement statementDisciplinaId;

    //

    public TurmaDBsetters(Banco banco) {

        this.banco = banco;

        this.registroDBcrud = new RegistroDBcrud(banco);

        this.avaliacaoDBsetters = new AvaliacaoDBsetters(banco);

        this.avaliacaoDBgetters = new AvaliacaoDBgetters(banco);
    }

    public UsuarioTO getUsuarioAtivo() {

        StringBuffer query = new StringBuffer();

        query.append("SELECT " +
                "us.id, " +
                "us.usuario, " +
                "us.senha, " +
                "us.nome, " +
                "us.cpf, " +
                "us.rg, " +
                "us.digitoRG, " +
                "us.dataUltimoAcesso, " +
                "us.ativo, " +
                "us.token  ");

        query.append(" FROM USUARIO as us ");
        query.append(" WHERE us.ativo = 1 ");

        Cursor cursor = null;

        UsuarioTO usuarioTO = null;

        try {

            cursor = banco.get()
                    .rawQuery(query.toString(),
                            null);

            if (cursor.moveToFirst()) {

                usuarioTO = new UsuarioTO(cursor);
            }
        }
        catch (Exception e) {

            e.printStackTrace();

            CrashAnalytics.e(TAG, e);
        }
        finally {

            if(cursor != null) {

                cursor.close();
            }
        }
        return usuarioTO;
    }

    private Cursor getSelect(StringBuffer query) {

        Cursor cursor = null;

        cursor = banco.get().rawQuery(query.toString(),null);

        if(!cursor.moveToFirst()) {

            cursor.close();

            return null;
        }
        return cursor;
    }

    private ArrayList turmaDisciplina(int codTurma) {

        String[] cod = new String[] {String.valueOf(codTurma)};

        Cursor cursor = null;

        ArrayList<Integer> list = new ArrayList();

        try {

            cursor = banco.get()
                    .rawQuery("SELECT c.codigoDisciplina " +
                            "FROM TURMASFREQUENCIA a " +
                            "INNER JOIN TURMAS b " +
                            "ON a.turma_id = b.id " +
                            "INNER JOIN DISCIPLINA c " +
                            "ON c.turmasFrequencia_id = a.id " +
                            "AND a.codigoTurma = ?;", cod);

            cursor.moveToFirst();

            while(!cursor.isAfterLast()) {

                int disciplina = cursor.getInt(0);

                list.add(disciplina);

                cursor.moveToNext();
            }

            cursor.close();
        }
        catch (Exception e) {

        }
        finally {

            if(cursor != null) {

                cursor.close();
            }
        }
        return list;
    }

    private int getTurmasFrequenciaId(int turma_id, SQLiteStatement statementTurmasFrequenciaId) {

        int id = -1;

        statementTurmasFrequenciaId.bindLong(1, turma_id);

        try {

            id = (int) statementTurmasFrequenciaId.simpleQueryForLong();
        }
        catch (Exception e) {

            id = -1;
        }
        finally {

            statementTurmasFrequenciaId.clearBindings();
        }

        return id;
    }

    private void atualizarCurriculo(JSONArray jsonArray) {

        String queryInserirGrupo =

                "INSERT OR IGNORE INTO NOVO_GRUPO (codigo, anoLetivo, codigoTipoEnsino, serie, codigoDisciplina) VALUES (?, ?, ?, ?, ?);";

        SQLiteStatement statementInserirGrupo = banco.get().compileStatement(queryInserirGrupo);

        String queryInserirCurriculo =

                "INSERT OR IGNORE INTO NOVO_CURRICULO (codigo, codigoGrupo, bimestre) VALUES (?, ?, ?);";

        SQLiteStatement statementInserirCurriculo = banco.get().compileStatement(queryInserirCurriculo);

        String queryInserirConteudo =

                "INSERT OR IGNORE INTO NOVO_CONTEUDO (codigoConteudo, codigoCurriculo, descricao) VALUES (?, ?, ?);";

        SQLiteStatement statementInserirConteudo = banco.get().compileStatement(queryInserirConteudo);

        String queryInserirHabilidade =

                "INSERT INTO NOVO_HABILIDADE (codigoHabilidade, codigoConteudo, descricao) VALUES (?, ?, ?);";

        SQLiteStatement statementInserirHabilidade = banco.get().compileStatement(queryInserirHabilidade);

        String queryInserirHabilidadeConteudo =

                "INSERT OR IGNORE INTO NOVO_CONTEUDO_HABILIDADE (conteudo, habilidade) VALUES (?, ?);";

        SQLiteStatement statementInserirHabilidadeConteudo = banco.get().compileStatement(queryInserirHabilidadeConteudo);

        String queryTemHabilidade = "SELECT descricao FROM NOVO_HABILIDADE WHERE codigoHabilidade = ? AND codigoConteudo = ?;";

        SQLiteStatement statementTemHabilidade = banco.get().compileStatement(queryTemHabilidade);

        try {

            JSONArray curriculosJsonArray = jsonArray;

            int numeroCurriculos = curriculosJsonArray.length();

            JSONObject curriculoJson;

            JSONObject grupoJson;

            int serie;

            int anoLetivo;

            int codigoGrupo;

            int codigoConteudo;

            int numeroConteudos;

            int codigoTipoEnsino;

            int codigoDisciplina;

            int codigoHabilidade;

            int numeroHabilidades;

            int numeroGrupoCurriculos;

            Conteudo conteudo = new Conteudo();

            JSONObject conteudoJson;

            JSONObject habilidadeJson;

            JSONArray conteudosJsonArray;

            JSONArray habilidadesJsonArray;

            JSONArray grupoCurriculosJsonArray;

            JSONObject grupoCurriculoJsonObject;

            banco.get().beginTransaction();

            for(int i = 0; i < numeroCurriculos; i++) {

                curriculoJson = curriculosJsonArray.getJSONObject(i);

                grupoJson = curriculoJson.getJSONObject("Grupo");

                serie = grupoJson.getInt("Serie");
                codigoGrupo = grupoJson.getInt("Codigo");
                anoLetivo = grupoJson.getInt("AnoLetivo");
                codigoTipoEnsino = grupoJson.getInt("CodigoTipoEnsino");
                codigoDisciplina = grupoJson.getInt("CodigoDisciplina");

                statementInserirGrupo.bindLong(1, codigoGrupo);
                statementInserirGrupo.bindLong(2, anoLetivo);
                statementInserirGrupo.bindLong(3, codigoTipoEnsino);
                statementInserirGrupo.bindLong(4, serie);
                statementInserirGrupo.bindLong(5, codigoDisciplina);

                statementInserirGrupo.executeInsert();

                grupoCurriculosJsonArray = grupoJson.getJSONArray("Curriculos");

                numeroGrupoCurriculos = grupoCurriculosJsonArray.length();

                for(int j = 0; j < numeroGrupoCurriculos; j++) {

                    grupoCurriculoJsonObject = grupoCurriculosJsonArray.getJSONObject(j);

                    statementInserirCurriculo.bindLong(1, grupoCurriculoJsonObject.getInt("Codigo"));
                    statementInserirCurriculo.bindLong(2, codigoGrupo);
                    statementInserirCurriculo.bindLong(3, grupoCurriculoJsonObject.getInt("Bimestre"));

                    statementInserirCurriculo.executeInsert();

                    conteudosJsonArray = grupoCurriculoJsonObject.getJSONArray("Conteudos");

                    numeroConteudos = conteudosJsonArray.length();

                    for(int k = 0; k < numeroConteudos; k++) {

                        conteudoJson = conteudosJsonArray.getJSONObject(k);

                        codigoConteudo = conteudoJson.getInt("Codigo");

                        conteudo.setConteudo(codigoConteudo, grupoCurriculoJsonObject.getInt("Codigo"), conteudoJson.getString("Descricao"));

                        statementInserirConteudo.bindLong(1, codigoConteudo);
                        statementInserirConteudo.bindLong(2, grupoCurriculoJsonObject.getInt("Codigo"));
                        statementInserirConteudo.bindString(3, conteudoJson.getString("Descricao"));

                        statementInserirConteudo.executeInsert();

                        habilidadesJsonArray = conteudoJson.getJSONArray("Habilidades");

                        numeroHabilidades = habilidadesJsonArray.length();

                        for(int l = 0; l < numeroHabilidades; l++) {

                            habilidadeJson = habilidadesJsonArray.getJSONObject(l);//

                            codigoHabilidade = habilidadeJson.getInt("Codigo");//

                            statementTemHabilidade.bindLong(1, codigoHabilidade);//
                            statementTemHabilidade.bindLong(2, codigoConteudo);//

                            String a = "";

                            try {

                                statementTemHabilidade.simpleQueryForString();
                            }
                            catch(SQLiteDoneException e) {

                                statementInserirHabilidade.bindLong(1, codigoHabilidade);
                                statementInserirHabilidade.bindLong(2, codigoConteudo);
                                statementInserirHabilidade.bindString(3, habilidadeJson.getString("Descricao"));

                                statementInserirHabilidade.executeInsert();
                            }

                            statementInserirHabilidadeConteudo.bindLong(1, codigoConteudo);
                            statementInserirHabilidadeConteudo.bindLong(2, codigoHabilidade);

                            statementInserirHabilidadeConteudo.executeInsert();
                        }
                    }
                }
            }
        }
        catch(JSONException e) {

            e.printStackTrace();
        }
        finally {

            statementInserirGrupo.close();

            statementInserirCurriculo.close();

            statementInserirConteudo.close();

            statementInserirHabilidade.close();

            statementInserirHabilidadeConteudo.close();

            statementTemHabilidade.close();

            banco.get().setTransactionSuccessful();

            banco.get().endTransaction();
        }
    }

    private void atualizaBimestre(BimestreTO bimestreTO, SQLiteStatement statementAtualizarBimestreAtual,
                                  SQLiteStatement statementInsertBimestre, SQLiteStatement statementAtualizarBimestre) {

        int rowsUpdate = 0;

        try {

            statementAtualizarBimestreAtual.bindLong(   1, bimestreTO.getTurmasFrequencia_id());
            statementAtualizarBimestreAtual.bindLong(   2, bimestreTO.getNumero());
            statementAtualizarBimestreAtual.bindString( 3, bimestreTO.getInicioBimestre());
            statementAtualizarBimestreAtual.bindString( 4, bimestreTO.getFimBimestre());

            rowsUpdate = statementAtualizarBimestreAtual.executeUpdateDelete();

            if(rowsUpdate == 0) {

                statementInsertBimestre.bindLong(   1, 1);
                statementInsertBimestre.bindLong(   2, bimestreTO.getNumero());
                statementInsertBimestre.bindString( 3, bimestreTO.getInicioBimestre());
                statementInsertBimestre.bindString( 4, bimestreTO.getFimBimestre());
                statementInsertBimestre.bindLong(   5, bimestreTO.getTurmasFrequencia_id());

                statementInsertBimestre.executeInsert();
            }
            else {

                statementAtualizarBimestre.bindLong(1, bimestreTO.getTurmasFrequencia_id());
                statementAtualizarBimestre.bindLong(2, bimestreTO.getNumero() == 1 ? 4 : (bimestreTO.getNumero() - 1));

                statementAtualizarBimestre.executeUpdateDelete();
            }
        }
        catch (Exception e) {

            e.printStackTrace();

            CrashAnalytics.e(TAG, e);
        }
        finally {

            statementAtualizarBimestreAtual.clearBindings();

            statementAtualizarBimestre.clearBindings();

            statementInsertBimestre.clearBindings();;
        }
    }

    private Integer getBimestreId(BimestreTO bimestreTO, SQLiteStatement statementBimestreId) {

        int id = 0;

        try {

            statementBimestreId.bindLong(1, bimestreTO.getNumero());

            statementBimestreId.bindLong(2, bimestreTO.getTurmasFrequencia_id());

            id = (int) statementBimestreId.simpleQueryForLong();
        }
        catch(Exception e) {


        }
        finally {

            statementBimestreId.clearBindings();
        }

        return id;
    }

    private boolean getBimestreBanco(BimestreTO bimestreTO, SQLiteStatement statement) {

        boolean bimestreNoBanco = true;

        try {

            statement.bindLong(1, bimestreTO.getNumero());

            statement.bindLong(2, bimestreTO.getTurmasFrequencia_id());

            statement.simpleQueryForLong();
        }
        catch(Exception e) {

            bimestreNoBanco = false;
        }
        finally {

            statement.clearBindings();
        }
        return bimestreNoBanco;
    }

    private void atualizarRegistroJson(JSONArray jsonArray) {

        queryDisciplinaCurriculo =

                "SELECT codigoDisciplina FROM NOVO_GRUPO WHERE codigo = ?;";

        statementDisciplinaCurriculo = banco.get().compileStatement(queryDisciplinaCurriculo);

        try {

            JSONArray registroJsonArray = jsonArray;

            int numeroRegistro = registroJsonArray.length();

            Registro registro = new Registro();

            JSONObject registroJson;

            int codNovoRegistro;

            int bimestre;

            int codigoGrupoCurriculo;

            String codigoTurma;

            String observacoes;

            String dataCriacao;

            JSONArray habilidades;

            int numeroHabilidades;

            List<HabilidadeConteudo> listaHabilidadeConteudo = new ArrayList<>();

            List<Integer> habilidadess = new ArrayList<>();

            JSONObject habilidadeConteudo;

            int codHabilidade;

            int codConteudo;

            boolean n = true;

            //HabilidadeConteudo habcon = new HabilidadeConteudo();

            ObjetoConteudo objcont = new ObjetoConteudo();

            for(int i = 0; i < numeroRegistro; i++) {

                registroJson = registroJsonArray.getJSONObject(i);

                codNovoRegistro = registroJson.getInt("Codigo");
                bimestre = registroJson.getInt("Bimestre");
                codigoGrupoCurriculo = registroJson.getInt("CodigoGrupoCurriculo");

                codigoTurma = registroJson.getString("CodigoTurma");
                observacoes = registroJson.getString("Observacoes");
                dataCriacao = registroJson.getString("DataCriacao");

                registro.setCodNovoRegistro(codNovoRegistro);
                registro.setBimestre(bimestre);
                registro.setCodigoTurma(codigoTurma);
                registro.setObservacoes(observacoes);
                registro.setCodigoGrupoCurriculo(String.valueOf(codigoGrupoCurriculo));
                registro.setDataCriacao(dataCriacao.substring(0, 10));
                registro.setCodigoDisciplina(String.valueOf(buscarDisciplinaCurriculo(codigoGrupoCurriculo, statementDisciplinaCurriculo)));

                habilidades = registroJson.getJSONArray("Habilidades");

                numeroHabilidades = habilidades.length();

                listaHabilidadeConteudo.clear();

                for(int o = 0; o < numeroHabilidades; o++) {

                    HabilidadeConteudo habcon = new HabilidadeConteudo();

                    habilidadeConteudo = habilidades.getJSONObject(o);

                    codHabilidade = habilidadeConteudo.getInt("CodigoHabilidade");
                    codConteudo = habilidadeConteudo.getInt("CodigoConteudo");

                    habcon.setCodigoConteudo(codConteudo);

                    habcon.setCodigoHabilidade(codHabilidade);

                    listaHabilidadeConteudo.add(habcon);
                }

                List<ObjetoConteudo> listaObjConteudo = new ArrayList<>();

                for(int z = 0; z < listaHabilidadeConteudo.size(); z++) {

                    HabilidadeConteudo habcon = new HabilidadeConteudo();

                    habcon = listaHabilidadeConteudo.get(z);

                    codConteudo = habcon.getCodigoConteudo();

                    habilidadess.clear();

                    objcont.setCodigoConteudo(codConteudo);

                    for(int c = 0; c < listaHabilidadeConteudo.size(); c++) {

                        if(listaHabilidadeConteudo.get(c).getCodigoConteudo() == codConteudo) {

                            habilidadess.add(listaHabilidadeConteudo.get(c).getCodigoHabilidade());
                        }
                    }
                    objcont.setCodigosHabilidades(habilidadess);

                    objcont.setCodigoConteudo(codConteudo);

                    //for(int m = 0; m < listaObjConteudo.size(); m++) {

                        //if(listaObjConteudo.get(m).getCodigoConteudo() == objcont.getCodigoConteudo()) {

                            //n = false;
                        //}
                    //}
                    //if(n) {

                        listaObjConteudo.add(objcont);
                    //}
                }
                registro.setConteudos(listaObjConteudo);

                registroDBcrud.salvarRegistroBanco(registro);
            }
        }
        catch(Exception e) {

            e.printStackTrace();
        }
        finally {

            statementDisciplinaCurriculo.close();
        }
    }

    private List<Integer> getTurmasFrequenciaIds(int codigoTurma) {

        Cursor cursor = null;

        List<Integer> listaIds = new ArrayList<>();

        try {

            cursor = banco.get().rawQuery("SELECT id FROM TURMASFREQUENCIA WHERE codigoTurma = " + codigoTurma, null);

            cursor.moveToFirst();

            while(!cursor.isAfterLast()) {

                listaIds.add(cursor.getInt(0));

                cursor.moveToNext();
            }
        }
        catch(Exception e) {

            e.printStackTrace();
        }
        finally {

            if(cursor != null) {

                cursor.close();
            }
        }
        return listaIds;
    }

    void updateTurmas(final JSONObject retorno)throws JSONException {

        final int ano = retorno.getInt("Ano");

        convertJSONArrayForTurmas(ano, retorno.getJSONArray("TurmasTurmas"));

        convertJSONArrayForTurmasFrequencia(ano, retorno.getJSONArray("TurmasFrequencia"));

        if(retorno.getJSONArray("Curriculos").length() > 0) {

            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {

                    try {

                        atualizarCurriculo(retorno.getJSONArray("Curriculos"));
                    }
                    catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }

        /*Thread thread2 = new Thread(new Runnable() {

                @Override
                public void run() {

                    try {*/

                        convertJSONArrayForTurmasFechamentos(retorno.getJSONArray("Fechamentos"));

                        convertJSONArrayForTurmasTiposFechamentos(retorno.getJSONArray("FechamentoParametrizacao"));
                    /*}
                    catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            });
        thread2.start();*/

        if(retorno.getJSONArray("RegistrosAula").length() > 0) {

            Thread thread3 = new Thread(new Runnable() {

                @Override
                public void run() {

                    try {

                        atualizarRegistroJson(retorno.getJSONArray("RegistrosAula"));
                    }
                    catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            });
            thread3.start();
        }
    }

    private Integer buscarDisciplinaCurriculo(int codGrupoCurriculo, SQLiteStatement statementDisciplinaCurriculo) {

        int codDisciplina = 0;

        statementDisciplinaCurriculo.bindLong(1, codGrupoCurriculo);

        try {

            codDisciplina = (int) statementDisciplinaCurriculo.simpleQueryForLong();
        }
        catch(Exception e) {

            codDisciplina = 0;
        }
        finally {

            statementDisciplinaCurriculo.clearBindings();
        }

        return codDisciplina;
    }

    private void setDiasComFrequencia(String data, int turmaFrequenciaId, int disciplina_id) {

        dataFormatada = data.split("T")[0];

        String horario = data.split("T")[1];//.substring(0, 5);

        dataSeparada = dataFormatada.split("[-]");

        dia = Integer.valueOf(dataSeparada[2]) < 10 ? dataSeparada[2].substring(1) : dataSeparada[2];

        mes = Integer.valueOf(dataSeparada[1]) < 10 ? dataSeparada[1].substring(1) : dataSeparada[1];

        statementUpdateDiasComFrequencia.bindString(1, dia);
        statementUpdateDiasComFrequencia.bindString(2, mes);
        statementUpdateDiasComFrequencia.bindString(3, dataSeparada[0]);
        statementUpdateDiasComFrequencia.bindLong(  4, turmaFrequenciaId);
        statementUpdateDiasComFrequencia.bindString(5, horario);
        statementUpdateDiasComFrequencia.bindString(6, dia);
        statementUpdateDiasComFrequencia.bindString(7, mes);
        statementUpdateDiasComFrequencia.bindString(8, dataSeparada[0]);
        statementUpdateDiasComFrequencia.bindLong(  9, turmaFrequenciaId);
        statementUpdateDiasComFrequencia.bindString(10, horario);

        try {

            int colunasAtualizadas = statementUpdateDiasComFrequencia.executeUpdateDelete();

            if(colunasAtualizadas == 0) {

                statementDiasComFrequencia.bindString(1, dia);
                statementDiasComFrequencia.bindString(2, mes);
                statementDiasComFrequencia.bindString(3, dataSeparada[0]);
                statementDiasComFrequencia.bindLong(  4, turmaFrequenciaId);
                statementDiasComFrequencia.bindString(5, horario);

                statementDiasComFrequencia.executeInsert();
            }
        }
        catch(Exception e) {

        }
        finally {

            statementUpdateDiasComFrequencia.clearBindings();

            statementDiasComFrequencia.clearBindings();
        }
    }

    private Integer setDadosDataBase2(String nameTable, GenericsTable genericsTable, SQLiteStatement statement) {

        long id = -1;

        try {

            if(genericsTable.getCodigoUnico() != null) {

                int rowsUpdate = banco.get().update(

                        nameTable, genericsTable.getContentValues(), genericsTable.getCodigoUnico(), null
                );

                if(rowsUpdate == 0) {

                    id = banco.get().insertWithOnConflict(

                            nameTable, null, genericsTable.getContentValues(), SQLiteDatabase.CONFLICT_REPLACE
                    );
                }
                else {

                        id = statement.simpleQueryForLong();
                }
            }
            else {

                id = banco.get().insert(

                        nameTable, null, genericsTable.getContentValues()
                );
            }
        }
        catch (Exception e) {

            e.printStackTrace();

            CrashAnalytics.e(TAG, e);
        }
        finally {

        }
        return Integer.valueOf(String.valueOf(id));
    }

    private int getDisciplinaId(Integer codDisciplina, Integer turmasFrequencia_id, SQLiteStatement statementDisciplinaId) {

        Cursor cursor = null;

        int disciplina_id = 0;

        try {

            cursor = banco.get().rawQuery(

                    "SELECT id FROM DISCIPLINA WHERE codigoDisciplina = " + codDisciplina
                            + " AND turmasFrequencia_id = " + turmasFrequencia_id , null
            );

            //statementDisciplinaId.bindLong(1, codDisciplina);
            //statementDisciplinaId.bindLong(2, turmasFrequencia_id);

            //disciplina_id = (int) statementDisciplinaId.simpleQueryForLong();

            if(cursor.getCount() > 0) {

                cursor.moveToFirst();

                cursor.getInt(0);

                disciplina_id = cursor.getInt(0);
            }
        }
        catch (Exception e) {

            e.printStackTrace();

            disciplina_id = 0;
        }
        finally {

            if(cursor != null) {

                cursor.close();
            }

            //statementDisciplinaId.clearBindings();
        }
        return disciplina_id;
    }

    private void convertJSONArrayForTurmas(int ano, JSONArray jsonArrayTurmas)throws JSONException {

        int numeroTurmas = jsonArrayTurmas.length();

        String queryTurmas = "SELECT id FROM " + TurmasTO.nomeTabela + " WHERE codigoTurma = ?;";

        SQLiteStatement turmasStatement = banco.get().compileStatement(queryTurmas);

        String queryAlunos = "SELECT id FROM " + AlunosTO.nomeTabela + " WHERE codigoMatricula = ?";

        SQLiteStatement alunosStatement = banco.get().compileStatement(queryAlunos);

        JSONObject jsonTurma;

        TurmasTO turmasTO = new TurmasTO();

        JSONObject jsonAluno;

        AlunosTO alunosTO = new AlunosTO();

        banco.get().beginTransaction();

        for(int i = 0; i < numeroTurmas; i++) {

            jsonTurma = jsonArrayTurmas.getJSONObject(i);

            turmasTO.setJSON(jsonTurma, ano);

            turmasStatement.bindLong(1, turmasTO.getCodigoTurma());

            Integer turma_id = setDadosDataBase2(TurmasTO.nomeTabela, turmasTO, turmasStatement);

            JSONArray jsonArrayAlunos = jsonTurma.getJSONArray("Alunos");

            int numeroAlunos = jsonArrayAlunos.length();

            for(int j = 0; j < numeroAlunos; j++) {

                jsonAluno = jsonArrayAlunos.getJSONObject(j);

                alunosTO.setJSON(jsonAluno, turma_id);

                alunosStatement.bindString(1, alunosTO.getCodigoMatricula());

                setDadosDataBase2(AlunosTO.nomeTabela, alunosTO, alunosStatement);////////
            }
        }

        banco.get().setTransactionSuccessful();
        banco.get().endTransaction();

        turmasStatement.close();

        alunosStatement.close();
    }

    private void convertJSONArrayForTurmasFechamentos(JSONArray jsonArrayFechamentos) throws JSONException {

        int numeroFechamentos = jsonArrayFechamentos.length();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

        final String data = dateFormat.format(new Date());

        String queryTurmas = "SELECT id FROM " + FechamentoTurmaTO.nomeTabela + " WHERE ?";

        SQLiteStatement turmasStatement = banco.get().compileStatement(queryTurmas);

        String queryAlunos = "SELECT id FROM " + FechamentoAlunoTO.nomeTabela + " WHERE ?";

        SQLiteStatement alunosStatement = banco.get().compileStatement(queryAlunos);

        JSONObject jsonObjFechamento;

        JSONObject jsonFechamento;

        FechamentoTurmaTO fechamentoTurmaTO = new FechamentoTurmaTO();

        FechamentoAlunoTO fechamentoAlunoTO = new FechamentoAlunoTO();

        banco.get().beginTransaction();

        for(int i = 0; i < numeroFechamentos; i++) {

            jsonObjFechamento = jsonArrayFechamentos.getJSONObject(i);

                try {

                    fechamentoTurmaTO.setCodigoTurma(jsonObjFechamento.getInt("CodigoTurma"));

                    fechamentoTurmaTO.setCodigoTipoFechamento(jsonObjFechamento.getInt("CodigoTipoFechamento"));

                    fechamentoTurmaTO.setCodigoDisciplina(jsonObjFechamento.getInt("CodigoDisciplina"));

                    fechamentoTurmaTO.setAulasRealizadas(jsonObjFechamento.getInt("AulasRealizadas"));

                    fechamentoTurmaTO.setAulasPlanejadas(jsonObjFechamento.getInt("AulasPlanejadas"));

                    fechamentoTurmaTO.setJustificativa(jsonObjFechamento.getString("Justificativa"));

                    fechamentoTurmaTO.setDataServidor(data);

                    turmasStatement.bindString(1, fechamentoTurmaTO.getCodigoUnico());

                    setDadosDataBase2(FechamentoTurmaTO.nomeTabela, fechamentoTurmaTO, turmasStatement);

                    JSONArray arrayFechamentosAlunos = jsonObjFechamento.getJSONArray("Fechamentos");

                    for(int j = 0; j < arrayFechamentosAlunos.length(); j++) {

                        jsonFechamento = arrayFechamentosAlunos.getJSONObject(j);

                        fechamentoAlunoTO.setCodigoTurma(fechamentoTurmaTO.getCodigoTurma());

                        fechamentoAlunoTO.setCodigoDisciplina(fechamentoTurmaTO.getCodigoDisciplina());

                        fechamentoAlunoTO.setCodigoFechamento(jsonFechamento.getInt("Codigo"));

                        fechamentoAlunoTO.setCodigoTipoFechamento(fechamentoTurmaTO.getCodigoTipoFechamento());

                        if ((fechamentoTurmaTO.getAulasPlanejadas() != 0
                                && fechamentoTurmaTO.getAulasRealizadas() != 0
                                && fechamentoTurmaTO.getJustificativa() != null) && !jsonFechamento.isNull("Nota")) {

                            fechamentoAlunoTO.setDataServidor(data);
                        }
                        else {

                            fechamentoAlunoTO.setConfirmado(Utils.ALUNO_NAO_CONFIRMADO_FECHAMENTO);
                        }
                        String jsonFechamentoString = jsonFechamento.toString();

                        char[] chars = jsonFechamentoString.toCharArray();

                        int charsLength = chars.length;

                        StringBuilder matriculaStringBuilder = new StringBuilder();

                        for(int k = jsonFechamentoString.indexOf("\"Aluno\":") + 8; k < charsLength; k++) {

                            char c = chars[k];

                            if (c != ','
                                    && c != '}') {

                                matriculaStringBuilder.append(c);
                            }
                            else {

                                break;
                            }
                        }

                        fechamentoAlunoTO.setCodigoMatricula(matriculaStringBuilder.toString());

                        if(!jsonFechamento.isNull("Nota")) {

                            fechamentoAlunoTO.setNota(jsonFechamento.getInt("Nota"));
                        }
                        else {

                            StringBuffer sb = new StringBuffer();

                            sb.append("SELECT * FROM alunos WHERE codigoMatricula = ");

                            sb.append(jsonFechamento.getInt("Aluno"));

                            Cursor cursorAluno = getSelect(sb);

                            if(cursorAluno != null
                                    && cursorAluno.getCount() > 0 && cursorAluno.getInt(cursorAluno.getColumnIndex("alunoAtivo")) == 1) {

                                fechamentoAlunoTO.setNota(Utils.ALUNO_ATIVO_SEM_NOTA);

                                cursorAluno.close();
                            }
                            else {

                                fechamentoAlunoTO.setNota(Utils.ALUNO_ATIVO_SEM_NOTA);
                            }
                        }
                        fechamentoAlunoTO.setFaltas(jsonFechamento.getInt("Faltas"));

                        fechamentoAlunoTO.setAusenciasCompensadas(jsonFechamento.getInt("FaltasCompensadas"));

                        fechamentoAlunoTO.setFaltasAcumuladas(jsonFechamento.getInt("FaltasAcumuladas"));

                        if(!jsonFechamento.isNull("Justificativa")) {

                            fechamentoAlunoTO.setJustificativa(jsonFechamento.getString("Justificativa"));
                        }
                        else {

                            fechamentoAlunoTO.setJustificativa("");
                        }

                        alunosStatement.bindString(1, fechamentoAlunoTO.getCodigoUnico());

                        setDadosDataBase2(FechamentoAlunoTO.nomeTabela, fechamentoAlunoTO, alunosStatement);
                    }
                }
                catch (JSONException e) {

                }
        }

        turmasStatement.close();

        alunosStatement.close();

        banco.get().setTransactionSuccessful();

        banco.get().endTransaction();

        Log.e(TAG, "JSON: Fechou Statement");
    }

    private void convertJSONArrayForTurmasTiposFechamentos(JSONArray jsonArrayTiposFechamentos) throws JSONException {

        TipoFechamentoTO tipoFechamentoTO = new TipoFechamentoTO();

        JSONObject jsonTipoFechamento;

        String queryTipoFechamento = "SELECT id FROM " + TipoFechamentoTO.nomeTabela + " WHERE ?";

        SQLiteStatement tipoFechamento = banco.get().compileStatement(queryTipoFechamento);

        banco.get().beginTransaction();

        for (int i = 0; i < 4; i++) {

            jsonTipoFechamento = jsonArrayTiposFechamentos.getJSONObject(i);

            tipoFechamentoTO.setJSON(jsonTipoFechamento);

            tipoFechamento.bindString(1, tipoFechamentoTO.getCodigoUnico());

            setDadosDataBase2(TipoFechamentoTO.nomeTabela, tipoFechamentoTO, tipoFechamento);
        }

        tipoFechamento.close();

        banco.get().setTransactionSuccessful();

        banco.get().endTransaction();
    }

    private void convertJSONArrayForTurmasFrequencia(final Integer anoLetivo, JSONArray jsonArrayFrequencia) throws JSONException {

        int numeroFrequencias = jsonArrayFrequencia.length();

        int codTurma;

        List<Integer> list = new ArrayList();

        List<Integer> listaIds = new ArrayList<>();

        int codDisciplina;

        int numeroDatas;

        int turmaFrequenciaId;

        int turmasFrequencia_id;

        TurmasFrequenciaTO turmasFrequenciaTO = new TurmasFrequenciaTO();

        String queryTurmas =

                "SELECT id FROM TURMAS WHERE codigoTurma = ?";

        SQLiteStatement statement = banco.get().compileStatement(queryTurmas);

        String insert =

                "INSERT INTO " + TurmasFrequenciaTO.nomeTabela +
                "(codigoTurma, codigoDiretoria, codigoEscola, codigoTipoEnsino, aulasBimestre, aulasAno, turma_id, aulasSemana)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        SQLiteStatement statement1 = banco.get().compileStatement(insert);

        //
        //Atualizar Faltas
        //

        totalFaltasAlunosTO = new TotalFaltasAlunosTO();

        queryTotalFaltasAlunos =

                "SELECT id FROM " + TotalFaltasAlunosTO.nomeTabela + " WHERE ?";

        statementTotalFaltasAlunos = banco.get().compileStatement(queryTotalFaltasAlunos);

        queryDiasComFrequencia =

                "INSERT INTO DIASCOMFREQUENCIA (dataFrequenciaDia, dataFrequenciaMes, dataFrequenciaAno, turmasFrequencia_id, horario) " +
                    "VALUES (?, ? , ? , ?, ?);";

        statementDiasComFrequencia = banco.get().compileStatement(queryDiasComFrequencia);

        queryUpdateDiasComFrequencia =

                "UPDATE DIASCOMFREQUENCIA SET dataFrequenciaDia = ?, dataFrequenciaMes = ?, dataFrequenciaAno = ?, turmasFrequencia_id = ?, horario = ?" +
                " WHERE dataFrequenciaDia = ? AND dataFrequenciaMes = ? AND dataFrequenciaAno = ? AND turmasFrequencia_id = ? AND horario = ?;";

        statementUpdateDiasComFrequencia = banco.get().compileStatement(queryUpdateDiasComFrequencia);

        //

        //
        //Convert Disciplinas
        //

        aulasTO = new AulasTO();

        disciplinaTO = new DisciplinaTO();

        queryInsertDisciplina =

                "SELECT id FROM " + DisciplinaTO.nomeTabela + " WHERE ?";

        statementInsertDisciplina = banco.get().compileStatement(queryInsertDisciplina);

        queryInsertAulas =

                "SELECT id FROM " + AulasTO.nomeTabela + " WHERE ?";

        statementInsertAulas = banco.get().compileStatement(queryInsertAulas);

        //

        //
        //Convert Turmas Avaliações
        //

        listaAvaliacaoJSON = new ArrayList<>();

        listaAvaliacaoSQLite = new ArrayList<>();

        listaIds1 = new ArrayList<>();

        avaliacoesTO = new AvaliacoesTO();

        queryInsertAvaliacao =

                "SELECT id FROM AVALIACOES WHERE codigoAvaliacao = ? AND disciplina_id = ?";

        statementInsertAvaliacao = banco.get().compileStatement(queryInsertAvaliacao);

        notasAlunoTO = new NotasAlunoTO();

        queryInsertNotas =

                "SELECT id FROM " + NotasAlunoTO.nomeTabela + " WHERE ?";

        statementInsertNotas = banco.get().compileStatement(queryInsertNotas);

        //

        //
        //Bimestres Calendario
        //

        bimestreTO = new BimestreTO();

        bimestreTO2 = new BimestreTO();

        bimestreTO3 = new BimestreTO();

        queryBimestresCalendario =

                "SELECT id FROM " + BimestreTO.nomeTabela + " WHERE ?";

        statementBimestresCalendario = banco.get().compileStatement(queryBimestresCalendario);

        queryTemBimestreBanco =

                "SELECT id FROM BIMESTRE WHERE numero = ? AND turmasFrequencia_id = ?;";

        statementTemBimestreBanco = banco.get().compileStatement(queryTemBimestreBanco);

        queryBimestreId =

                "SELECT id FROM BIMESTRE WHERE numero = ? AND turmasFrequencia_id = ?;";

        statementBimestreId = banco.get().compileStatement(queryBimestreId);

        //

        //
        //Bimestres 2
        //

        queryAtualizarBimestreAtual =

                "UPDATE BIMESTRE SET bimestreAtual = 1 WHERE turmasFrequencia_id = ? AND numero = ? AND inicioBimestre = ? AND fimBimestre = ?;";

        statementAtualizarBimestreAtual = banco.get().compileStatement(queryAtualizarBimestreAtual);

        queryInsertBimestre =

                "INSERT INTO BIMESTRE (bimestreAtual, numero, inicioBimestre, fimBimestre, turmasFrequencia_id) VALUES (?, ?, ?, ?, ?);";

        statementInsertBimestre = banco.get().compileStatement(queryInsertBimestre);

        queryAtualizarBimestre =

                "UPDATE BIMESTRE SET bimestreAtual = 0 WHERE turmasFrequencia_id = ? AND numero = ?;";

        statementAtualizarBimestre = banco.get().compileStatement(queryAtualizarBimestre);

        //

        //
        //Turmas Calendario
        //

        diasLetivosTO = new DiasLetivosTO();

        queryDiasLetivos =

                "SELECT id FROM " + DiasLetivosTO.nomeTabela + " WHERE ?";

        statementDiasLetivos = banco.get().compileStatement(queryDiasLetivos);

        //

        //
        //GetTurmasFrequenciaId
        //

        queryTurmasFrequenciaId =

                "SELECT id FROM TURMASFREQUENCIA WHERE turma_id = ?;";

        statementTurmasFrequenciaId = banco.get().compileStatement(queryTurmasFrequenciaId);

        //

        //
        //GetDisciplinaId
        //

        queryDisciplinaId =

                "SELECT id FROM DISCIPLINA WHERE codigoDisciplina = ? AND turmasFrequencia_id = ?";

        statementDisciplinaId = banco.get().compileStatement(queryDisciplinaId);

        //

        banco.get().beginTransaction();

        for(int i = 0; i < numeroFrequencias; i++) {

            final JSONObject jsonObjectFrequencia = jsonArrayFrequencia.getJSONObject(i);

            codTurma = jsonObjectFrequencia.getInt("CodigoTurma");

            statement.bindLong(1, codTurma);

            int turma_id = -1;

            try {

                turma_id = (int) statement.simpleQueryForLong();
            }
            catch (IndexOutOfBoundsException | SQLiteDoneException e) {

                e.printStackTrace();
            }
            finally {

            }

            if(turma_id != -1) {

                turmasFrequenciaTO.setJSON(jsonObjectFrequencia, turma_id);

                list.clear();

                list = turmaDisciplina(turmasFrequenciaTO.getCodigoTurma());

                codDisciplina = Integer.parseInt(jsonObjectFrequencia.getJSONObject("Disciplina").getString("CodigoDisciplina"));

                numeroDatas = jsonObjectFrequencia.getJSONArray("DiasComFrequencia").length();

                turmaFrequenciaId = getTurmasFrequenciaId(turma_id, statementTurmasFrequenciaId);

                /////////////////////////////////

                listaIds.clear();

                listaIds = getTurmasFrequenciaIds(codTurma);

                if(turmaFrequenciaId == -1 || !list.contains(codDisciplina)) {

                    statement1.bindLong(1, turmasFrequenciaTO.getCodigoTurma());
                    statement1.bindLong(2, turmasFrequenciaTO.getCodigoDiretoria());
                    statement1.bindLong(3, turmasFrequenciaTO.getCodigoEscola());
                    statement1.bindLong(4, turmasFrequenciaTO.getCodigoTipoEnsino());
                    statement1.bindLong(5, turmasFrequenciaTO.getAulasBimestre());
                    statement1.bindLong(6, turmasFrequenciaTO.getAulasAno());
                    statement1.bindLong(7, turmasFrequenciaTO.getTurma_id());
                    statement1.bindLong(8, turmasFrequenciaTO.getAulasSemana());

                    turmasFrequencia_id = (int) statement1.executeInsert();

                    if(!list.contains(codDisciplina)) {

                        convertJSONArrayForTurmasDisciplina(

                                jsonObjectFrequencia.getJSONObject("Disciplina"), turmasFrequencia_id, codDisciplina
                        );
                    }

                    turmaFrequenciaId = turmasFrequencia_id;
                }

                if(turmaFrequenciaId == -1) {

                    return;
                }
                else if(list.contains(codDisciplina)) {

                    if(listaIds.size() > 1) {

                        for(int j = 0; j < listaIds.size(); j++) {

                            atualizarFaltas(jsonObjectFrequencia.getJSONObject("Disciplina"), codDisciplina, listaIds.get(j));
                        }
                    }
                    else {

                        atualizarFaltas(jsonObjectFrequencia.getJSONObject("Disciplina"), codDisciplina, turmaFrequenciaId);
                    }
                }

                /////////////////////////////////////////////////

                List<String> listaDiasFrequenciaJson = new ArrayList<>();

                Cursor cursor = banco.get().rawQuery("SELECT COUNT(id) FROM DIASCOMFREQUENCIA WHERE turmasFrequencia_id = " + turmaFrequenciaId, null);

                for(int data = 0; data < numeroDatas; data++) {

                    String dataFormatada = jsonObjectFrequencia.getJSONArray("DiasComFrequencia").get(data).toString().split("T")[0];

                    String horario = jsonObjectFrequencia.getJSONArray("DiasComFrequencia").get(data).toString().split("T")[1];//.substring(0, 5);

                    String[] dataSeparada = dataFormatada.split("[-]");

                    String dia = Integer.valueOf(dataSeparada[2]) < 10 ? dataSeparada[2].substring(1) : dataSeparada[2];

                    String mes = Integer.valueOf(dataSeparada[1]) < 10 ? dataSeparada[1].substring(1) : dataSeparada[1];

                    String diaComFrequencia = anoLetivo + "-" + mes + "-" + dia + "T" + horario;// +":00";

                    listaDiasFrequenciaJson.add(diaComFrequencia);

                    setDiasComFrequencia(jsonObjectFrequencia.getJSONArray("DiasComFrequencia").get(data).toString(),turmaFrequenciaId, disciplina_id);
                }

                int totalNumeroDatasNoBanco = 0;

                List<String> listaDiasFrequenciaBanco = new ArrayList<>();

                if(cursor != null) {

                    cursor.moveToFirst();

                    totalNumeroDatasNoBanco = cursor.getInt(0);

                    if(totalNumeroDatasNoBanco > numeroDatas) {

                        Cursor cursor1 = banco.get().rawQuery(

                                "SELECT dataFrequenciaAno, dataFrequenciaMes, dataFrequenciaDia, horario " +
                                        "FROM DIASCOMFREQUENCIA WHERE turmasFrequencia_id = " + turmaFrequenciaId, null);

                        if(cursor1 != null) {

                            while(cursor1.moveToNext()) {

                                String diaBanco = cursor1.getString(cursor1.getColumnIndex("dataFrequenciaAno")) + "-"
                                        + cursor1.getString(cursor1.getColumnIndex("dataFrequenciaMes")) + "-"
                                        + cursor1.getString(cursor1.getColumnIndex("dataFrequenciaDia")) + "T"
                                        + cursor1.getString(cursor1.getColumnIndex("horario"));// + ":00";

                                listaDiasFrequenciaBanco.add(diaBanco);
                            }

                            cursor1.close();
                        }
                    }
                    cursor.close();
                }

                for(int k = 0; k < listaDiasFrequenciaBanco.size(); k++) {

                    if(!listaDiasFrequenciaJson.contains(listaDiasFrequenciaBanco.get(k))) {

                        deletarDiasComFrequenciaBancoLocal(listaDiasFrequenciaBanco.get(k), turmaFrequenciaId);
                    }
                }

                ////////////////////////////////

                convertJSONArrayForTurmasAvaliacoes(jsonObjectFrequencia.getJSONArray("Avaliacoes"), codTurma, codDisciplina);

                convertJSONArrayForBimestresCalendario(jsonObjectFrequencia.getJSONArray("BimestreCalendario"), turmaFrequenciaId);

                final Integer bimestre_id1 = convertJSONArrayForTurmasBimestres(jsonObjectFrequencia.getJSONObject("BimestreAtual"), turmaFrequenciaId);

                if(bimestre_id1 != 0) {

                    final int turmaFreqId = turmaFrequenciaId;

                    try {

                        convertJSONArrayForTurmasBimestres2(

                                jsonObjectFrequencia.getJSONObject("BimestreAtual"), turmaFreqId
                        );

                        convertJSONArrayForTurmasCalendario(

                                jsonObjectFrequencia.getJSONArray("CalendarioBimestreAtual"), bimestre_id1, anoLetivo
                        );
                    }
                    catch (JSONException e) {

                    }

                    try {

                        Integer bimestre_id2 = convertJSONArrayForTurmasBimestres(

                                jsonObjectFrequencia.getJSONObject("BimestreAnterior"), turmaFreqId
                        );

                        convertJSONArrayForTurmasCalendario(

                                jsonObjectFrequencia.getJSONArray("CalendarioBimestreAnterior"), bimestre_id2, anoLetivo
                        );
                    }
                    catch (JSONException e) {

                    }
                }
            }
        }
        statement.close();

        statement1.close();

        statementTotalFaltasAlunos.close();

        statementDiasComFrequencia.close();

        statementInsertDisciplina.close();

        statementInsertAulas.close();

        statementInsertAvaliacao.close();

        statementInsertNotas.close();

        statementBimestresCalendario.close();

        statementTemBimestreBanco.close();

        statementBimestreId.close();

        statementAtualizarBimestreAtual.close();

        statementInsertBimestre.close();

        statementAtualizarBimestre.close();

        statementDiasLetivos.close();

        statementTurmasFrequenciaId.close();

        statementDisciplinaId.close();

        statementUpdateDiasComFrequencia.close();

        banco.get().setTransactionSuccessful();

        banco.get().endTransaction();
    }

    private void deletarDiasComFrequenciaBancoLocal(String s, int turmaFrequenciaId) {

        String diaFrequencia = s.split("-")[2].split("T")[0];

        String mesFrequencia = s.split("-")[1];

        String anoFrequencia = s.split("-")[0];

        String horario = s.split("T")[1].substring(0, 5);

        Cursor cursor = banco.get().rawQuery("DELETE FROM DIASCOMFREQUENCIA " +
                "WHERE dataFrequenciaDia = " + diaFrequencia + " AND dataFrequenciaMes = " + mesFrequencia + " AND dataFrequenciaAno = " + anoFrequencia +
                " AND horario = '" + horario + "' AND turmasFrequencia_id = " + turmaFrequenciaId, null);

        if(cursor != null) {

            cursor.moveToFirst();

            cursor.close();
        }
    }

    private void convertJSONArrayForTurmasBimestres2(JSONObject bimestreAtualJson, Integer turmasFrequencia_id)throws JSONException {

        bimestreTO3.setJSON(bimestreAtualJson, turmasFrequencia_id);

        atualizaBimestre(bimestreTO3, statementAtualizarBimestreAtual, statementInsertBimestre, statementAtualizarBimestre);
    }

    private Integer convertJSONArrayForTurmasBimestres(JSONObject bimestreAtualJson, Integer turmasFrequencia_id)throws JSONException {

        bimestreTO2.setJSON(bimestreAtualJson, turmasFrequencia_id);

        return getBimestreId(bimestreTO2, statementBimestreId);
    }

    private void convertJSONArrayForTurmasDisciplina(JSONObject jsonObjectDisciplina, Integer turmasFrequencia_id, int codDisciplina)throws JSONException {

        disciplinaTO.setJSON(jsonObjectDisciplina, turmasFrequencia_id);

        statementInsertDisciplina.bindString(1, disciplinaTO.getCodigoUnico());

        disciplina_id_disc = setDadosDataBase2(DisciplinaTO.nomeTabela, disciplinaTO, statementInsertDisciplina);

        jsonarrayAula = jsonObjectDisciplina.getJSONArray("Aulas");

        numeroAulas = jsonarrayAula.length();

        for(int k = 0; k < numeroAulas; k++) {

            jsonObjectAulas = jsonarrayAula.getJSONObject(k);

            aulasTO.setJSON(jsonObjectAulas, disciplina_id_disc);

            statementInsertAulas.bindString(1, aulasTO.getCodigoUnico());

            setDadosDataBase2(AulasTO.nomeTabela, aulasTO, statementInsertAulas);
        }

        atualizarFaltas(jsonObjectDisciplina, codDisciplina, turmasFrequencia_id);
    }

    private void convertJSONArrayForTurmasAvaliacoes(JSONArray jsonArrayAvaliacoes, int codTurma, int codDisciplina)throws JSONException {

        numeroAvaliacoes = jsonArrayAvaliacoes.length();

        listaAvaliacaoSQLite.clear();

        listaAvaliacaoJSON.clear();

        listaIds1.clear();

        for (int i = 0; i < numeroAvaliacoes; i++) {

            listaAvaliacaoJSON.add(jsonArrayAvaliacoes.getJSONObject(i).getInt("Codigo"));
        }

        listaAvaliacaoSQLite = avaliacaoDBgetters.getListaTotalAvaliacoes(codTurma, String.valueOf(codDisciplina));

        for (int k = 0; k < listaAvaliacaoSQLite.size(); k++) {

            if (!listaAvaliacaoJSON.contains(listaAvaliacaoSQLite.get(k).getCodigo())) {

                avaliacaoDBsetters.setAvaliacaoParaDeletar(listaAvaliacaoSQLite.get(k).getId());
            }
        }

        usuarioId = getUsuarioAtivo().getId();

        for(int i = 0; i < numeroAvaliacoes; i++) {

            jsonObjectAvaliacoes = jsonArrayAvaliacoes.getJSONObject(i);

            codigoTurma1 = jsonObjectAvaliacoes.getInt("CodigoTurma");

            listaIds1 = avaliacaoDBgetters.getTurmasFrequenciaIds(codigoTurma1);

            for(int ids = 0; ids < listaIds1.size(); ids++) {

                turmasFrequenciaId = listaIds1.get(ids);

                StringBuffer queryTurma = new StringBuffer();

                queryTurma.append(" SELECT id FROM TURMAS  as t ");

                queryTurma.append(" WHERE t.codigoTurma = ").append(codigoTurma1);

                Cursor cursorTurma = getSelect(queryTurma);

                turma_id = -1;

                if(cursorTurma != null) {

                    turma_id = cursorTurma.getInt(cursorTurma.getColumnIndex("id"));

                    cursorTurma.close();
                }

                codigoDisciplinaJson = jsonObjectAvaliacoes.getInt("CodigoDisciplina");

                if(codDisciplina == 1000) {

                    codigoDisciplinaJson = codDisciplina;
                }

                StringBuffer queryDisciplina = new StringBuffer();

                queryDisciplina.append(" SELECT id FROM DISCIPLINA  as t ");

                queryDisciplina.append(" WHERE t.codigoDisciplina = ").append(codigoDisciplinaJson );

                queryDisciplina.append(" AND t.turmasFrequencia_id = ").append(turmasFrequenciaId);

                Cursor cursorDisciplina = getSelect(queryDisciplina);

                disciplina_id_avaliacao = -1;

                if(cursorDisciplina != null) {

                    disciplina_id_avaliacao = cursorDisciplina.getInt(cursorDisciplina.getColumnIndex("id"));

                    cursorDisciplina.close();
                }

                if(disciplina_id_avaliacao != -1) {

                    avaliacoesTO.setJSON(jsonObjectAvaliacoes, turma_id, disciplina_id_avaliacao);

                    statementInsertAvaliacao.bindLong(1, avaliacoesTO.getCodigoAvaliacao());

                    statementInsertAvaliacao.bindLong(2, avaliacoesTO.getDisciplina_id());

                    avaliacao_id = setDadosDataBase2(AvaliacoesTO.nomeTabela, avaliacoesTO, statementInsertAvaliacao);

                    if(jsonObjectAvaliacoes.has("Notas")) {

                        jsonArrayNotas = jsonObjectAvaliacoes.getJSONArray("Notas");

                        numeroNotas = jsonArrayNotas.length();

                        if(numeroNotas > 0) {

                            for(int l = 0; l < numeroNotas; l++) {

                                jsonObjNota = jsonArrayNotas.getJSONObject(l);

                                jsonAvaliacao = jsonObjNota.toString();

                                char[] chars = jsonAvaliacao.toCharArray();

                                int charsLength = chars.length;

                                StringBuilder matriculaStringBuilder = new StringBuilder();

                                for(int j = jsonAvaliacao.indexOf("\"CodigoMatriculaAluno\":") + 23; j < charsLength; j++) {

                                    char c = chars[j];

                                    if (c != ','
                                            && c != '}') {

                                        matriculaStringBuilder.append(c);
                                    }
                                    else {

                                        break;
                                    }
                                }
                                Cursor cursorAlunos = getSelect(new StringBuffer("SELECT id " +
                                        "FROM ALUNOS " +
                                        "WHERE codigoMatricula = ").append(matriculaStringBuilder.toString()));

                                Integer aluno_id = -1;

                                if(cursorAlunos != null) {

                                    aluno_id = cursorAlunos.getInt(cursorAlunos.getColumnIndex("id"));

                                    cursorAlunos.close();
                                }

                                notasAlunoTO.setJSON(jsonObjNota, aluno_id, usuarioId, avaliacao_id);

                                statementInsertNotas.bindString(1, notasAlunoTO.getCodigoUnico());

                                setDadosDataBase2(NotasAlunoTO.nomeTabela, notasAlunoTO, statementInsertNotas);
                            }
                            Cursor cursorAlunos = getSelect(new StringBuffer("SELECT ALUNOS.id, " +
                                    "ALUNOS.codigoMatricula " +
                                    "FROM ALUNOS " +
                                    "LEFT JOIN NOTASALUNO " +
                                    "ON ALUNOS.id = NOTASALUNO.aluno_id " +
                                    "LEFT JOIN AVALIACOES " +
                                    "ON AVALIACOES.turma_id = ALUNOS.turma_id " +
                                    "WHERE NOTASALUNO.id IS NULL " +
                                    "AND ALUNOS.turma_id = " + turma_id));

                            if(cursorAlunos != null) {

                                idColumnIndex = cursorAlunos.getColumnIndex("id");

                                codigoMatriculaColumnIndex = cursorAlunos.getColumnIndex("codigoMatricula");

                                do {

                                    JSONObject jsonObjNota = new JSONObject("{\"CodigoMatriculaAluno\":" +
                                            cursorAlunos.getString(codigoMatriculaColumnIndex) +
                                            ",\"Nota\":\"11.0\"}");

                                    notasAlunoTO.setJSON(jsonObjNota, cursorAlunos.getInt(idColumnIndex), usuarioId, avaliacao_id);

                                    statementInsertNotas.bindString(1, notasAlunoTO.getCodigoUnico());

                                    setDadosDataBase2(NotasAlunoTO.nomeTabela, notasAlunoTO, statementInsertNotas);
                                }
                                while (cursorAlunos.moveToNext());

                                cursorAlunos.close();
                            }
                        }
                    }
                }
            }
        }
    }

    private void atualizarFaltas(JSONObject jsonObjectDisciplina, Integer codDisciplina, Integer turmasFrequencia_id)throws JSONException {

        jsonarrayFaltasAlunos = jsonObjectDisciplina.getJSONArray("FaltasAlunos");

        numeroFaltasAlunos = jsonarrayFaltasAlunos.length();

        disciplina_id = getDisciplinaId(codDisciplina, turmasFrequencia_id, statementDisciplinaId);

        for(int k = 0; k < numeroFaltasAlunos; k++) {

            jsonObjectFaltasAlunos = jsonarrayFaltasAlunos.getJSONObject(k);

            json = jsonObjectFaltasAlunos.toString();

            char[] chars = json.toCharArray();

            charsLength = chars.length;

            StringBuilder matriculaStringBuilder = new StringBuilder();

            for(int j = json.indexOf("\"CodigoMatricula\":") + 18; j < charsLength; j++) {

                char c = chars[j];

                if (c != ','
                        && c != '}') {

                    matriculaStringBuilder.append(c);
                }
                else {

                    break;
                }
            }

            StringBuffer query = new StringBuffer();

            query.append(" SELECT id FROM ALUNOS as t ");

            query.append(" WHERE t.codigoMatricula = ").append(matriculaStringBuilder.toString());

            Cursor cursor = getSelect(query);

            aluno_id = -1;

            if(cursor != null) {

                aluno_id = cursor.getInt(cursor.getColumnIndex("id"));

                cursor.close();
            }

            totalFaltasAlunosTO.setJSON(jsonObjectFaltasAlunos, disciplina_id, aluno_id);

            statementTotalFaltasAlunos.bindString(1, totalFaltasAlunosTO.getCodigoUnico());

            setDadosDataBase2(TotalFaltasAlunosTO.nomeTabela, totalFaltasAlunosTO, statementTotalFaltasAlunos);
        }
    }

    private void convertJSONArrayForBimestresCalendario(JSONArray bimestreCalendarioJson, Integer turmasFrequencia_id)throws JSONException {

        int bimestres = bimestreCalendarioJson.length();

        for(int i = 0; i < bimestres; i++) {

            bimestreTO.setJSON(bimestreCalendarioJson.getJSONObject(i), turmasFrequencia_id);

            if(!getBimestreBanco(bimestreTO, statementTemBimestreBanco)) {

                setDadosDataBase2(BimestreTO.nomeTabela, bimestreTO, statementBimestresCalendario);
            }
        }
    }

    private void convertJSONArrayForTurmasCalendario(JSONArray jsonarrayCalendario, Integer bimestre_id, Integer anoLetivo)throws JSONException {

        numeroElementosCalendario = jsonarrayCalendario.length();

        Calendar calendar = Calendar.getInstance();

        for(int i = 0; i < numeroElementosCalendario; i++) {

            jsonObjectCalendario = jsonarrayCalendario.getJSONObject(i);

            jsonArrayDias = jsonObjectCalendario.getJSONArray("DiasLetivos");

            numeroDiasLetivos = jsonArrayDias.length();

            for(int l = 0; l < numeroDiasLetivos; l++) {

                int dia = jsonArrayDias.getInt(l);

                int mes = jsonObjectCalendario.getInt("Mes");

                calendar.set(anoLetivo, mes - 1, dia);

                int diaSemana = calendar.get(Calendar.DAY_OF_WEEK);

                int semanaMes = calendar.get(Calendar.WEEK_OF_MONTH);

                int mes1 = calendar.get(Calendar.MONTH);

                diasLetivosTO.setJSON(bimestre_id, dia, mes, anoLetivo, diaSemana, semanaMes, mes1);

                setDadosDataBase2(DiasLetivosTO.nomeTabela, diasLetivosTO, statementDiasLetivos);
            }
        }
    }
}
