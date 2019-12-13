package br.gov.sp.educacao.sed.mobile.Frequencia;

import java.util.Date;
import java.util.List;
import java.util.Calendar;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;

import br.gov.sp.educacao.sed.mobile.Login.UsuarioTO;

import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;

import br.gov.sp.educacao.sed.mobile.Escola.Aula;
import br.gov.sp.educacao.sed.mobile.Escola.Bimestre;
import br.gov.sp.educacao.sed.mobile.Escola.Disciplina;
import br.gov.sp.educacao.sed.mobile.Escola.DiasLetivos;

import br.gov.sp.educacao.sed.mobile.Fechamento.FechamentoData;

import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;

public class FrequenciaDBgetters {

    private Banco banco;

    private Cursor cursor;

    private String queryIdAula;

    @SuppressWarnings("FieldCanBeLocal")
    private SQLiteStatement statementIdAula;

    private String queryIdDiaLetivo;

    @SuppressWarnings("FieldCanBeLocal")
    private SQLiteStatement statementIdDiasLetivos;

    private String queryIdDiaLetivoPelosBimestres;

    @SuppressWarnings("FieldCanBeLocal")
    private SQLiteStatement statementIdDiaLetivoPelosBimestres;

    private String queryNumeroFaltas;

    @SuppressWarnings("FieldCanBeLocal")
    private SQLiteStatement statementNumeroFaltas;

    private String queryTotalDiasComConflito;

    private SQLiteStatement statementTotalDiasComConflito;

    private String queryInsertDiasComConflito;

    private SQLiteStatement statementInsertDiasComConflito;

    private String queryUpdateFaltasAlunos;

    private SQLiteStatement statementUpdateFaltasAlunos;

    private String queryGetAulaIdPeloHorario;

    private SQLiteStatement statementGetAulaIdPeloHorario;

    private String queryGetDisciplinaId;

    private SQLiteStatement statementGetDisciplinaId;

    private String queryDeleteConflitosResolvidos;

    private SQLiteStatement statementDeleteConflitosResolvidos;

    private String queryNumeroFaltasSincronizadas;

    @SuppressWarnings("FieldCanBeLocal")
    private SQLiteStatement statementFaltasSincronizadas;

    private String querySiglaComparecimento;

    @SuppressWarnings("FieldCanBeLocal")
    private SQLiteStatement statementSiglaComparecimento;

    private final String TAG = FrequenciaDBgetters.class.getSimpleName();

    public FrequenciaDBgetters(Banco banco) {

        this.banco = banco;

        queryIdDiaLetivo =

                "SELECT id FROM DIASLETIVOS WHERE dataAula = ?";

        queryIdDiaLetivoPelosBimestres =

                "SELECT id FROM DIASLETIVOS WHERE (bimestre_id = ? OR bimestre_id = ?) AND dataAula = ?";

        queryNumeroFaltas =

                "SELECT COUNT(id) FROM FALTASALUNOS WHERE diasLetivos_id = ? AND aula_id = ?";

        queryNumeroFaltasSincronizadas =

                "SELECT COUNT(id) FROM FALTASALUNOS WHERE diasLetivos_id = ? AND aula_id = ? AND dataServidor IS NOT NULL";

        queryTotalDiasComConflito =

                "SELECT COUNT(id) FROM DIASCONFLITO WHERE diaLetivo_id = ? AND aula_id = ? AND turmasFrequencia_id = ? AND disciplina_id = ?";

        queryInsertDiasComConflito =

                "INSERT INTO DIASCONFLITO (diaLetivo_id, aula_id, turmasFrequencia_id, disciplina_id) VALUES (?, ?, ?, ?)";

        queryUpdateFaltasAlunos =

                "UPDATE FALTASALUNOS SET dataServidor = NULL WHERE diasLetivos_id = ? AND aula_id = ?";

        queryGetAulaIdPeloHorario =

                "SELECT id FROM AULAS WHERE inicioHora = ? AND disciplina_id = ?";

        queryGetDisciplinaId =

                "SELECT id FROM DISCIPLINA WHERE codigoDisciplina = ? AND turmasFrequencia_id = ?";

        queryDeleteConflitosResolvidos =

                "DELETE FROM DIASCONFLITO WHERE id = ?";

        querySiglaComparecimento =

                "SELECT F.tipoFalta FROM FALTASALUNOS AS F WHERE F.aluno_id = ?" +
                        "AND F.diasLetivos_id = ? AND F.aula_id = ?";

        queryIdAula =

                "SELECT * FROM AULAS WHERE inicioHora = ? AND fimHora = ? AND diaSemana = ? AND disciplina_id = ?";
    }
    ///OK
    UsuarioTO getUsuarioAtivo() {

        String query =

                "SELECT id, usuario, senha, nome, cpf, rg, digitoRG, dataUltimoAcesso, ativo, token FROM USUARIO WHERE ativo = 1;";

        cursor = banco.get().rawQuery(query, null);

        try {

            if (cursor.moveToFirst()) {

                return new UsuarioTO(cursor);
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
        return null;
    }
    ///OK
    int getDiaLetivoIdPelosBimestres(int bimestreAtualId, int bimestreAnteriorId, String data) {

        statementIdDiaLetivoPelosBimestres = banco.get().compileStatement(queryIdDiaLetivoPelosBimestres);

        int diaLetivoId = 0;

        try {

            banco.get().beginTransaction();

            statementIdDiaLetivoPelosBimestres.bindLong(  1, bimestreAtualId);

            statementIdDiaLetivoPelosBimestres.bindLong(  2, bimestreAnteriorId);

            statementIdDiaLetivoPelosBimestres.bindString(3, data);

            diaLetivoId = (int) statementIdDiaLetivoPelosBimestres.simpleQueryForLong();
        }
        catch(Exception e) {

            e.printStackTrace();
        }
        finally {

            statementIdDiaLetivoPelosBimestres.clearBindings();

            statementIdDiaLetivoPelosBimestres.close();

            banco.get().setTransactionSuccessful();

            banco.get().endTransaction();
        }

        return diaLetivoId;
    }
    ///OK
    public void inserirFrequenciaConflito(String dia, String horario, int turmaFrequenciaId, String disciplina) {

        try {

            banco.get().beginTransaction();

            int totalDiasComConflito = 0;

            int bimestreId = getBimestre(turmaFrequenciaId).getId();

            int bimestreAnteriorId = getBimestreAnterior(turmaFrequenciaId).getId();

            int diaLetivoId = getDiaLetivoIdPelosBimestres(bimestreId, bimestreAnteriorId, dia);

            int disciplinaId = getDisciplinaId(disciplina, turmaFrequenciaId);

            int aulaId = getAulaIdPeloHorario(horario, disciplinaId);

            statementTotalDiasComConflito = banco.get().compileStatement(queryTotalDiasComConflito);

            statementTotalDiasComConflito.bindLong(1, diaLetivoId);

            statementTotalDiasComConflito.bindLong(2, aulaId);

            statementTotalDiasComConflito.bindLong(3, turmaFrequenciaId);

            statementTotalDiasComConflito.bindLong(4, disciplinaId);

            totalDiasComConflito = (int) statementTotalDiasComConflito.simpleQueryForLong();

            if(totalDiasComConflito < 1) {

                statementInsertDiasComConflito = banco.get().compileStatement(queryInsertDiasComConflito);

                statementInsertDiasComConflito.bindLong(1, diaLetivoId);

                statementInsertDiasComConflito.bindLong(2, aulaId);

                statementInsertDiasComConflito.bindLong(3, turmaFrequenciaId);

                statementInsertDiasComConflito.bindLong(4, disciplinaId);

                statementInsertDiasComConflito.executeInsert();
            }

            statementUpdateFaltasAlunos = banco.get().compileStatement(queryUpdateFaltasAlunos);

            statementUpdateFaltasAlunos.bindLong(1, diaLetivoId);

            statementUpdateFaltasAlunos.bindLong(2, aulaId);

            statementUpdateFaltasAlunos.executeUpdateDelete();
        }
        catch(Exception e) {

            e.printStackTrace();
        }
        finally {

            statementTotalDiasComConflito.clearBindings();

            statementTotalDiasComConflito.close();

            statementInsertDiasComConflito.clearBindings();

            statementInsertDiasComConflito.close();

            statementUpdateFaltasAlunos.clearBindings();

            statementUpdateFaltasAlunos.close();

            banco.get().setTransactionSuccessful();

            banco.get().endTransaction();
        }
    }
    ///OK
    public List<String> getHorariosComConflito() {

        Cursor cursor = null;

        List<String> listaMensagens = new ArrayList<>();

        try {

            banco.get().beginTransaction();

            cursor = banco.get().rawQuery(

                    "SELECT DL.dataAula, AU.inicioHora, AU.fimHora, TU.nomeTurma, DP.nomeDisciplina " +
                    "FROM DIASCONFLITO AS DC JOIN DIASLETIVOS AS DL, AULAS AS AU, TURMASFREQUENCIA AS TF, TURMAS AS TU, DISCIPLINA AS DP " +
                    "ON DC.diaLetivo_id = DL.id " +
                    "AND DC.aula_id = AU.id " +
                    "AND DC.turmasFrequencia_id = TF.id " +
                    "AND DC.disciplina_id = DP.id " +
                    "AND TF.turma_id = TU.id;", null

            );

            if(cursor != null) {

                while(cursor.moveToNext()) {

                    String dataAula = cursor.getString(cursor.getColumnIndex("dataAula"));

                    String horaInicio = cursor.getString(cursor.getColumnIndex("inicioHora"));

                    String horaFim = cursor.getString(cursor.getColumnIndex("fimHora"));

                    String nomeTurma = cursor.getString(cursor.getColumnIndex("nomeTurma"));

                    String nomeDisciplina = cursor.getString(cursor.getColumnIndex("nomeDisciplina"));

                    String mensagem = "Conflito de lançamento para Turma: " + nomeTurma +
                            " Disciplina: " + nomeDisciplina + " Horário: " + horaInicio + "/" + horaFim + " Data: " + dataAula;

                    listaMensagens.add(mensagem);
                }
            }
        }
        catch(Exception e) {

            e.printStackTrace();
        }
        finally {

            if(cursor != null) {

                cursor.close();
            }

            banco.get().setTransactionSuccessful();

            banco.get().endTransaction();
        }

        return listaMensagens;
    }
    ///OK
    int getDiaLetivo(String data) {

        int diasLetivos = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");

        statementIdDiasLetivos = banco.get().compileStatement(queryIdDiaLetivo);

        try {

            String strDate = sdf.format(sdf.parse(data));

            statementIdDiasLetivos.bindString(1, strDate);

            diasLetivos = (int) statementIdDiasLetivos.simpleQueryForLong();
        }
        catch (Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            statementIdDiasLetivos.clearBindings();

            statementIdDiasLetivos.close();
        }
        return diasLetivos;
    }
    ///OK
    int getAulaIdPeloHorario(String horarioInicio, int disciplinaId) {

        int aulaId = 0;

        try {

            banco.get().beginTransaction();

            statementGetAulaIdPeloHorario = banco.get().compileStatement(queryGetAulaIdPeloHorario);

            statementGetAulaIdPeloHorario.bindString(1, horarioInicio);

            statementGetAulaIdPeloHorario.bindLong(  2, disciplinaId);

            aulaId = (int) statementGetAulaIdPeloHorario.simpleQueryForLong();
        }
        catch(Exception e) {

            e.printStackTrace();
        }
        finally {

            statementGetAulaIdPeloHorario.clearBindings();

            statementGetAulaIdPeloHorario.close();

            banco.get().setTransactionSuccessful();

            banco.get().endTransaction();
        }

        return aulaId;
    }
    ///OK
    public List<JSONObject> montarJSONEnvio(List<TurmaGrupo> listaTurmas) {

        List<JSONObject> listaDeFrequenciasPorTurma = new ArrayList<>();

        try {

            banco.get().beginTransaction();

            for(int i = 0; i < listaTurmas.size(); i++) {

                boolean temFrequencia = false;

                JSONObject frequenciaLancamento = new JSONObject();

                Bimestre bimestreAtual = getBimestre(listaTurmas.get(i).getTurmasFrequencia().getId());

                Bimestre bimestreAnterior = getBimestreAnterior(listaTurmas.get(i).getTurmasFrequencia().getId());

                List<DiasLetivos> listaDiasLetivos = getDiasLetivosTurma(bimestreAtual.getId(), bimestreAnterior.getId());

                List<Aula> listaAulas = getAulaId(listaTurmas.get(i).getDisciplina().getId(), listaTurmas.get(i).getTurmasFrequencia().getId());

                frequenciaLancamento.put("CodigoTurma", listaTurmas.get(i).getTurma().getCodigoTurma());

                frequenciaLancamento.put("CodigoDisciplina", listaTurmas.get(i).getDisciplina().getCodigoDisciplina());

                JSONArray dias = new JSONArray();

                for(int j = 0; j < listaDiasLetivos.size(); j++) {

                    JSONObject dia = new JSONObject();

                    dia.put("Dia", listaDiasLetivos.get(j).getDataAula());

                    JSONArray horarios = new JSONArray();

                    for(int k = 0; k < listaAulas.size(); k++) {

                        JSONObject horario = new JSONObject();

                        horario.put("Inicio", listaAulas.get(k).getInicio());

                        horario.put("Fim", listaAulas.get(k).getFim());

                        JSONArray frequencias = new JSONArray();

                        horario.put("Frequencias", frequencias);

                        List<Faltas> listaFaltasAlunos = getIdAlunos(

                                listaTurmas.get(i).getTurma().getId(), listaDiasLetivos.get(j).getId(), listaAulas.get(k).getId()

                        );

                        if(listaFaltasAlunos.size() > 0){

                            temFrequencia = true;
                        }

                        for(int l = 0; l < listaFaltasAlunos.size(); l++) {

                            JSONObject frequencia = new JSONObject();

                            frequencia.put("CodigoDisciplina", listaTurmas.get(i).getDisciplina().getCodigoDisciplina());

                            frequencia.put("CodigoTurma", listaTurmas.get(i).getTurma().getCodigoTurma());

                            frequencia.put("CodigoDaAula", 0);

                            frequencia.put("CodigoTipoFrequencia", listaFaltasAlunos.get(l).getTipoFalta());

                            frequencia.put("DataDaAula", listaDiasLetivos.get(j).getDataAula());

                            frequencia.put("HorarioInicioAula", listaAulas.get(k).getInicio());

                            frequencia.put("HorarioFimAula", listaAulas.get(k).getFim());

                            frequencia.put("CodigoMotivoFrequencia", 0);

                            frequencia.put("Justificativa", "");

                            if(frequencia.get("CodigoTipoFrequencia").equals("F")) {

                                frequencia.put("Presenca", false);
                            }
                            else if(frequencia.get("CodigoTipoFrequencia").equals("C")) {

                                frequencia.put("Presenca", true);
                            }

                            frequencia.put("CodigoMatriculaAluno", listaFaltasAlunos.get(l).getCodigoMatricula());

                            frequencias.put(frequencia);
                        }

                        horarios.put(horario);

                        dia.put("Horarios", horarios);
                    }
                    dias.put(dia);

                    frequenciaLancamento.put("Dias", dias);
                }

                if(listaDiasLetivos.size() > 0 && frequenciaLancamento.getJSONArray("Dias").length() > 0) {

                    if(temFrequencia){

                        listaDeFrequenciasPorTurma.add(frequenciaLancamento);
                    }
                }
            }
        }
        catch(Exception e) {

            e.printStackTrace();
        }
        finally {

            banco.get().setTransactionSuccessful();

            banco.get().endTransaction();
        }

        return listaDeFrequenciasPorTurma;
    }
    ///OK
    FechamentoData getFechamentoAberto() {

        FechamentoData fechamentoData = new FechamentoData();

        try {

            cursor = banco.get().rawQuery(

                    "SELECT codigoTipoFechamento, nome, ano, inicio, fim " +
                            "FROM TIPO_FECHAMENTO " +
                            "WHERE date('now') >= inicio " +
                            "AND date('now') <= fim;", null
            );

            if(cursor != null && cursor.moveToNext()) {

                fechamentoData.setFim(cursor.getString(cursor.getColumnIndex("fim")));
                fechamentoData.setAno(cursor.getString(cursor.getColumnIndex("ano")));
                fechamentoData.setNome(cursor.getString(cursor.getColumnIndex("nome")));
                fechamentoData.setInicio(cursor.getString(cursor.getColumnIndex("inicio")));
                fechamentoData.setCodigoTipoFechamento(cursor.getString(cursor.getColumnIndex("codigoTipoFechamento")));
            }
            else {

                fechamentoData = null;
            }
        }
        catch(Exception e) {

            CrashAnalytics.e(TAG, e);

            fechamentoData = null;
        }
        finally {

            if(cursor != null) {

                cursor.close();
            }
        }
        return fechamentoData;
    }
    ///OK
    int getFaltas(int diasLetivos, int aula) {

        int numeroFaltas = 0;

        statementNumeroFaltas = banco.get().compileStatement(queryNumeroFaltas);

        try {

            statementNumeroFaltas.bindLong(1, diasLetivos);

            statementNumeroFaltas.bindLong(2, aula);

            numeroFaltas = (int) statementNumeroFaltas.simpleQueryForLong();
        }
        catch (Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            statementNumeroFaltas.clearBindings();

            statementNumeroFaltas.close();
        }
        return numeroFaltas;
    }
    ///OK
    int getFaltasSincronizadas(int diasLetivos, int aula) {

        int numeroFaltasSincronizadas = 0;

        statementFaltasSincronizadas = banco.get().compileStatement(queryNumeroFaltasSincronizadas);

        try {

            statementFaltasSincronizadas.bindLong(1, diasLetivos);

            statementFaltasSincronizadas.bindLong(2, aula);

            numeroFaltasSincronizadas = (int) statementFaltasSincronizadas.simpleQueryForLong();
        }
        catch (Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            statementFaltasSincronizadas.clearBindings();

            statementFaltasSincronizadas.close();
        }
        return numeroFaltasSincronizadas;
    }
    ///OK
    Bimestre getBimestre(int turmasFrequenciaId) {

        Bimestre bimestre = new Bimestre();

        try {

            cursor = banco.get().rawQuery(

                    "SELECT * FROM BIMESTRE WHERE turmasFrequencia_id = " + turmasFrequenciaId +" AND bimestreAtual = 1;", null
            );

            if(cursor.moveToNext()) {

                bimestre.setId(cursor.getInt(cursor.getColumnIndex("id")));

                bimestre.setNumero(cursor.getInt(cursor.getColumnIndex("numero")));

                bimestre.setInicio(cursor.getString(cursor.getColumnIndex("inicioBimestre")));

                bimestre.setFim(cursor.getString(cursor.getColumnIndex("fimBimestre")));
            }
        }
        catch(Exception e) {

            e.printStackTrace();

            CrashAnalytics.e(TAG, e);
        }
        finally {

            if(cursor != null) {

                cursor.close();
            }
        }
        return bimestre;
    }
    ///OK
    List<Aluno> getAlunosAtivos(List<Aluno> alunos){

        List<Aluno> alunosAtivos = new ArrayList<>();

        try {

            for(int i = 0; i < alunos.size(); i++) {

                try {

                    cursor = banco.get().rawQuery(

                            "SELECT * FROM ALUNOS WHERE alunoAtivo = 1 AND id = " + alunos.get(i).getId(), null
                    );

                    if(cursor.getCount() > 0) {

                        alunosAtivos.add(alunos.get(i));
                    }
                }
                finally {

                    if(cursor != null) {

                        cursor.close();
                    }
                }
            }
        }
        catch (Exception e) {

            e.printStackTrace();

            CrashAnalytics.e(TAG, e);
        }

        return alunosAtivos;
    }
    ///OK
    ArrayList<Aula> getAula(Disciplina disciplina) {

        ArrayList<Aula> listaAula = new ArrayList<>();

        try {

            banco.get().beginTransaction();

            cursor = banco.get().rawQuery(

                    "SELECT * FROM AULAS WHERE disciplina_id = " + disciplina.getId(), null
            );

            while(cursor.moveToNext()) {

                for(int i = 1; i <= 5; i++) {

                    final Aula aula = new Aula();

                    aula.setInicio(cursor.getString(cursor.getColumnIndex("inicioHora")));
                    aula.setFim(cursor.getString(cursor.getColumnIndex("fimHora")));
                    aula.setDiaSemana(i);

                    listaAula.add(aula);
                }
            }
        }
        catch(Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            if(cursor != null) {

                cursor.close();
            }

            banco.get().setTransactionSuccessful();

            banco.get().endTransaction();
        }
        return listaAula;
    }
    ///OK
    private int getDisciplinaId(String codigoDisciplina, int turmaFrequenciaId) {

        int disciplinaId = 0;

        try {

            banco.get().beginTransaction();

            statementGetDisciplinaId = banco.get().compileStatement(queryGetDisciplinaId);

            statementGetDisciplinaId.bindString(1, codigoDisciplina);

            statementGetDisciplinaId.bindLong(  2, turmaFrequenciaId);

            disciplinaId = (int) statementGetDisciplinaId.simpleQueryForLong();
        }
        catch(Exception e) {

            e.printStackTrace();
        }
        finally {

            statementGetDisciplinaId.clearBindings();

            statementGetDisciplinaId.close();

            banco.get().setTransactionSuccessful();

            banco.get().endTransaction();
        }

        return disciplinaId;
    }
    ///OK
    private ArrayList<Aula> getAulaId(int disciplina_id, int turmasFrequencia_id) {

        ArrayList<Aula> listaAula = new ArrayList<>();

        Cursor cursor = null;

        try {

            cursor = banco.get().rawQuery(

                    "SELECT AU.inicioHora, AU.fimHora, AU.id " +
                    "FROM AULAS AS AU JOIN DISCIPLINA AS DI " +
                    "WHERE AU.disciplina_id = " + disciplina_id +
                    " AND DI.turmasFrequencia_id = " + turmasFrequencia_id, null
            );

            while(cursor.moveToNext()) {

                    final Aula aula = new Aula();

                    aula.setInicio(cursor.getString(cursor.getColumnIndex("inicioHora")));
                    aula.setFim(cursor.getString(cursor.getColumnIndex("fimHora")));
                    //aula.setDiaSemana(i);
                    aula.setId(cursor.getInt(cursor.getColumnIndex("id")));

                    listaAula.add(aula);
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

        return listaAula;
    }
    ///OK
    private List<DiasLetivos> getDiasLetivosTurma(int bimestraAtual_id, int bimestreAnterior_id) {

        Cursor cursor = banco.get().rawQuery(

                "SELECT DISTINCT DL.id, DL.dataAula " +
                "FROM DIASLETIVOS AS DL " +
                "JOIN FALTASALUNOS AS FA " +
                "ON DL.id = FA.diasLetivos_id WHERE DL.bimestre_id = " + bimestraAtual_id +
                " OR DL.bimestre_id = " + bimestreAnterior_id, null

        );

        List<DiasLetivos> listaDiasLetivos = new ArrayList<>();

        if(cursor != null) {

            while(cursor.moveToNext()) {

                DiasLetivos diasLetivos = new DiasLetivos();

                diasLetivos.setId(cursor.getInt(cursor.getColumnIndex("id")));

                diasLetivos.setDataAula(cursor.getString(cursor.getColumnIndex("dataAula")));

                listaDiasLetivos.add(diasLetivos);
            }
            cursor.close();
        }
        return listaDiasLetivos;
    }
    ///OK
    private List<Faltas> getIdAlunos(int turmaId, int diasLetivos_id, int aula_id) {

        Cursor cursor = banco.get().rawQuery(

                "SELECT FA.tipoFalta, AL.codigoMatricula " +
                "FROM ALUNOS AS AL " +
                "JOIN FALTASALUNOS AS FA " +
                "ON AL.id = FA.aluno_id " +
                "WHERE AL.turma_id = " + turmaId +
                " AND FA.diasLetivos_id = " + diasLetivos_id +
                " AND FA.aula_id = " + aula_id +
                " AND FA.dataServidor IS NULL ", null

        );

        List<Faltas> listaIdAlunos = new ArrayList<>();

        if(cursor != null) {

            while(cursor.moveToNext()) {

                Faltas faltas = new Faltas();

                faltas.setTipoFalta(cursor.getString(cursor.getColumnIndex("tipoFalta")));

                faltas.setCodigoMatricula(cursor.getString(cursor.getColumnIndex("codigoMatricula")));

                listaIdAlunos.add(faltas);
            }
            cursor.close();
        }
        return listaIdAlunos;
    }
    ///OK
    Aluno getTotalFaltas(Aluno aluno, int idDisciplina) {

        try {

            cursor = banco.get().rawQuery(

                    "SELECT faltasAnuais, faltasBimestre, faltasSequenciais " +
                    "FROM TOTALFALTASALUNOS " +
                    "WHERE aluno_id = " + aluno.getId() +
                    " AND disciplina_id = " + idDisciplina,
                    null
            );

            if(cursor.getCount() > 0) {

                cursor.moveToNext();

                aluno.setFaltasAnuais(cursor.getInt(cursor.getColumnIndex("faltasAnuais")));

                aluno.setFaltasBimestre(cursor.getInt(cursor.getColumnIndex("faltasBimestre")));

                aluno.setFaltasSequenciais(cursor.getInt(cursor.getColumnIndex("faltasSequenciais")));
            }
        }
        catch(Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            if(cursor != null) {

                cursor.close();
            }
        }
        return aluno;
    }
    ///OK
    Bimestre getBimestreAnterior(int turmasFrequencia_id) {

        Bimestre bimestre = new Bimestre();

        cursor = banco.get().rawQuery(

                "SELECT numero, inicioBimestre, fimBimestre, id" +
                " FROM BIMESTRE" +
                " WHERE numero = ((SELECT numero FROM BIMESTRE WHERE turmasFrequencia_id = ? " +
                " AND bimestreAtual = 1) - 1) AND turmasFrequencia_id = ? ;",
                new String [] {String.valueOf(turmasFrequencia_id),
                        String.valueOf(turmasFrequencia_id)}
        );

        if(cursor.moveToFirst()) {

            bimestre.setNumero(cursor.getInt(0));
            bimestre.setInicio(cursor.getString(1));
            bimestre.setFim(cursor.getString(2));
            bimestre.setId(cursor.getInt(3));

            cursor.close();
        }

        return bimestre;
    }
    ///OK
    String getSiglaComparecimento(int alunoId, int aula, int diaLetivoId) {

        String siglaComparecimento = "";

        statementSiglaComparecimento = banco.get().compileStatement(querySiglaComparecimento);

        try {

            statementSiglaComparecimento.bindLong(   1, alunoId);

            statementSiglaComparecimento.bindLong(   2, diaLetivoId);

            statementSiglaComparecimento.bindLong(   3, aula);

            siglaComparecimento = statementSiglaComparecimento.simpleQueryForString();
        }
        catch (Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            statementSiglaComparecimento.clearBindings();

            statementSiglaComparecimento.close();
        }
        return siglaComparecimento;
    }
    ///OK
    int getAula(String inicioHora, String fimHora, int diaSemana, int disciplinaId) {

        int aula = 0;

        statementIdAula = banco.get().compileStatement(queryIdAula);

        try {

            statementIdAula.bindString(1, inicioHora);
            statementIdAula.bindString(2, fimHora);
            statementIdAula.bindLong(  3, diaSemana);
            statementIdAula.bindLong(  4, disciplinaId);

            aula = (int) statementIdAula.simpleQueryForLong();
        }
        catch (Exception e) {

            e.printStackTrace();

            CrashAnalytics.e(TAG, e);
        }
        finally {

            statementIdAula.clearBindings();

            statementIdAula.close();
        }
        return aula;
    }
    ///OK
    ArrayList<DiasLetivos> getDiasLetivos(Bimestre bimestre, List<Integer> listaDiaSemana) {

        ArrayList<DiasLetivos> listaDiasLetivos = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();

        StringBuffer query = new StringBuffer();

        banco.get().beginTransaction();

        cursor = banco.get().rawQuery(

                "SELECT turmasFrequencia_id FROM BIMESTRE WHERE id = " + bimestre.getId(), null
        );

        int turma_id = 0;

        if(cursor != null && cursor.moveToNext()) {

            turma_id = cursor.getInt(cursor.getColumnIndex("turmasFrequencia_id"));
        }

        if(cursor != null) {

            cursor.close();
        }

        cursor = banco.get().rawQuery(

                "SELECT id FROM BIMESTRE WHERE numero = " + (bimestre.getNumero() == 1 ? 4 : bimestre.getNumero() - 1) +
                        " AND turmasFrequencia_id = " +
                        turma_id + ";", null
        );

        int bimestre_id = 0;

        if(cursor != null && cursor.moveToNext()) {

            bimestre_id = cursor.getInt(cursor.getColumnIndex("id"));
        }

        query.append(" SELECT * FROM DIASLETIVOS ");

        query.append(" WHERE bimestre_id = ").append(bimestre.getId());

        query.append(" OR bimestre_id = ").append(bimestre_id);

        if(cursor != null) {

            cursor.close();
        }

        try {

            cursor = banco.get().rawQuery(query.toString(), null);

            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");

            while(cursor.moveToNext()) {

                String input_date = cursor.getString(cursor.getColumnIndex("dataAula"));

                Date data = format1.parse(input_date);

                calendar.setTime(data);

                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;

                if(listaDiaSemana.contains(dayOfWeek)) {

                    final DiasLetivos diasLetivos = new DiasLetivos();

                    diasLetivos.setId(cursor.getInt(cursor.getColumnIndex("id")));

                    diasLetivos.setDataAula(cursor.getString(cursor.getColumnIndex("dataAula")));

                    listaDiasLetivos.add(diasLetivos);
                }
            }
        }
        catch (Exception e) {

            e.printStackTrace();
        }
        finally {

            if (cursor != null) {

                cursor.close();
            }

            banco.get().setTransactionSuccessful();

            banco.get().endTransaction();

            calendar.clear();
        }
        return listaDiasLetivos;
    }
    ///OK
    public void getConflitosResolvidos() {

        Cursor cursor = null;

        try {

            banco.get().beginTransaction();

            cursor = banco.get().rawQuery(

                    "SELECT DC.id FROM DIASCONFLITO AS DC " +
                            "JOIN FALTASALUNOS AS FA " +
                            "ON DC.diaLetivo_id = FA.diasLetivos_id " +
                            "AND DC.aula_id = FA.aula_id " +
                            "WHERE FA.dataServidor IS NOT NULL", null

            );

            statementDeleteConflitosResolvidos = banco.get().compileStatement(queryDeleteConflitosResolvidos);

            while(cursor.moveToNext()) {

                int id = cursor.getInt(cursor.getColumnIndex("id"));

                statementDeleteConflitosResolvidos.bindLong(1, id);

                statementDeleteConflitosResolvidos.executeUpdateDelete();

                statementDeleteConflitosResolvidos.clearBindings();
            }
        }
        catch(Exception e) {

            e.printStackTrace();
        }
        finally {

            if(cursor != null) {

                cursor.close();
            }

            statementDeleteConflitosResolvidos.close();

            banco.get().setTransactionSuccessful();

            banco.get().endTransaction();
        }
    }
    ////Novo Aplicar Statement////
    boolean verificarSeExistemLancamentosParaHorario(int aulaEspecifica, int diaLetivo) {

        Cursor cursor = banco.get().rawQuery("SELECT id FROM FALTASALUNOS " +
                "WHERE diasLetivos_id IN " +
                "(SELECT id FROM DIASLETIVOS WHERE dataAula = (SELECT dataAula FROM DIASLETIVOS WHERE id = " + diaLetivo + ")) " +
                "AND aula_id IN " +
                "(SELECT id FROM AULAS WHERE inicioHora = (SELECT inicioHora FROM AULAS WHERE id = " + aulaEspecifica +")) " +
                "AND diasLetivos_id NOT IN (" + diaLetivo + ") AND aula_id NOT IN (" + aulaEspecifica + ")", null);

        int count = 0;

        boolean lancamentoFeito = false;

        if(cursor != null) {

            count = cursor.getCount();

            cursor.close();
        }

        if(count > 0) {

            lancamentoFeito = true;
        }

        return lancamentoFeito;
    }
    ////Novo Aplicar Statement////
    List<String> getHorariosComLancamento(int diaLetivo, int turmaId) {

        banco.get().beginTransaction();

        Cursor cursor2 = banco.get().rawQuery("SELECT id FROM ALUNOS WHERE turma_id = " + turmaId + " AND alunoAtivo = 1", null);

        Cursor cursor1 = banco.get().rawQuery("SELECT DISTINCT AU.id FROM FALTASALUNOS AS FA JOIN AULAS AS AU ON " +
                "FA.aula_id = AU.id WHERE FA.diasLetivos_id = " + diaLetivo, null);

        Cursor cursor5 = banco.get().rawQuery("SELECT dataAula FROM DIASLETIVOS WHERE id = " + diaLetivo, null);

        String dataAula = "";

        if(cursor5 != null) {

            cursor5.moveToFirst();

            dataAula = cursor5.getString(cursor5.getColumnIndex("dataAula"));

            cursor5.close();
        }

        String dia = dataAula.split("/")[0];

        String mes = dataAula.split("/")[1];

        String ano = dataAula.split("/")[2];

        Cursor cursor4 = banco.get().rawQuery("SELECT id FROM AULAS WHERE disciplina_id = (SELECT id FROM DISCIPLINA WHERE turmasFrequencia_id = " +
                "(SELECT id FROM TURMASFREQUENCIA WHERE turma_id = " + turmaId + ")) " +
                "AND inicioHora IN (SELECT horario FROM DIASCOMFREQUENCIA " +
                "WHERE turmasFrequencia_id  = " +
                "(SELECT id FROM TURMASFREQUENCIA WHERE turma_id = " + turmaId + ") AND dataFrequenciaDia = " + dia +
                " AND dataFrequenciaMes = " + mes + " AND dataFrequenciaAno = " + ano + ")", null);

        int totalAlunosAtivos = 0;

        List<String> listaHorariosComLancamento = new ArrayList<>();

        if(cursor2 != null) {

            totalAlunosAtivos = cursor2.getCount();

            cursor2.close();
        }

        List<Integer> listaAulaIds = new ArrayList<>();

        List<Integer> listaAulaIdsDiasComFrequencia = new ArrayList<>();

        if(cursor1 != null) {

            while(cursor1.moveToNext()) {

                int id = cursor1.getInt(cursor1.getColumnIndex("id"));

                listaAulaIds.add(id);
            }
            cursor1.close();
        }

        if(cursor4 != null) {

            while(cursor4.moveToNext()) {

                int id = cursor4.getInt(cursor4.getColumnIndex("id"));

                if(!listaAulaIds.contains(id)) {

                    listaAulaIdsDiasComFrequencia.add(id);
                }
            }
            cursor4.close();
        }

        if(listaAulaIds.size() > 0) {

            for(int i = 0; i < listaAulaIds.size(); i++) {

                Cursor cursor3 = banco.get().rawQuery("SELECT id FROM FALTASALUNOS WHERE diasLetivos_id = " + diaLetivo + " " +
                        "AND aula_id = " + listaAulaIds.get(i), null);

                if(cursor3 != null) {

                    int totalFaltasDiaHorario = cursor3.getCount();

                    cursor3.close();

                    if(totalFaltasDiaHorario == totalAlunosAtivos) {

                        Cursor cursor = banco.get().rawQuery("SELECT inicioHora, fimHora FROM AULAS " +
                                "WHERE id = " + listaAulaIds.get(i), null );

                        if(cursor != null) {

                            while(cursor.moveToNext()) {

                                String horario = cursor.getString(cursor.getColumnIndex("inicioHora")) + "/" + cursor.getString(cursor.getColumnIndex("fimHora"));

                                if(!listaHorariosComLancamento.contains(horario)) {

                                    listaHorariosComLancamento.add(horario);
                                }
                            }
                            cursor.close();
                        }
                    }
                }
            }
        }

        if(listaAulaIdsDiasComFrequencia.size() > 0) {

            for(int i = 0; i < listaAulaIdsDiasComFrequencia.size(); i++) {

                Cursor cursor = banco.get().rawQuery("SELECT inicioHora, fimHora FROM AULAS " +
                        "WHERE id = " + listaAulaIdsDiasComFrequencia.get(i), null );

                if(cursor != null) {

                    while(cursor.moveToNext()) {

                        String horario = cursor.getString(cursor.getColumnIndex("inicioHora")) + "/" + cursor.getString(cursor.getColumnIndex("fimHora"));

                        if(!listaHorariosComLancamento.contains(horario)) {

                            listaHorariosComLancamento.add(horario);
                        }
                    }
                    cursor.close();
                }
            }
        }

        banco.get().setTransactionSuccessful();

        banco.get().endTransaction();

        return listaHorariosComLancamento;
    }
    ////Novo////
    void getTurmaComLancamentoExistente(int aulaEspecifica, int diaLetivo) {

        //Cursor cursor = banco.get().rawQuery()
    }
    ////Novo Aplicar Statement////
    int getAulasPorSemana(int turmasFrequenciaId) {

        Cursor cursor = banco.get().rawQuery("SELECT aulasSemana FROM TURMASFREQUENCIA WHERE id = " + turmasFrequenciaId, null);

        int aulasPorSemana = 0;

        if(cursor != null) {

            cursor.moveToFirst();
            aulasPorSemana = cursor.getInt(cursor.getColumnIndex("aulasSemana"));

            cursor.close();
        }

        return aulasPorSemana;
    }
    ////Novo Aplicar Statement////
    int getTotalLancamentosNaSemana(int disciplina_id, int semanaMes, int mes1) {

        int totalLancamentosSemana = 0;

        Cursor cursor = banco.get().rawQuery("SELECT DISTINCT FA.aula_id, FA.diasLetivos_id FROM AULAS AS AU JOIN FALTASALUNOS AS FA, DIASLETIVOS AS DL " +
                                             "ON FA.diasLetivos_id = DL.id WHERE FA.aula_id IN " +
                "(SELECT id FROM AULAS WHERE disciplina_id = " + disciplina_id + ") AND DL.semanaMes = " + semanaMes + " AND DL.mes = " + mes1, null);

        if(cursor != null) {

            totalLancamentosSemana = cursor.getCount();

            cursor.close();
        }

        return totalLancamentosSemana;
    }

    public List<String> getDiasComFrequencia(int codigoTurma) {

        List<String> diasComFrequencia = new ArrayList<>();

        String query = "SELECT " +
                "dataFrequenciaDia AS dia, " +
                "dataFrequenciaMes AS mes, " +
                "dataFrequenciaAno AS ano " +
                "FROM DIASCOMFREQUENCIA " +
                "INNER JOIN TURMASFREQUENCIA " +
                "ON DIASCOMFREQUENCIA.turmasFrequencia_id = TURMASFREQUENCIA.id " +
                "WHERE TURMASFREQUENCIA.codigoTurma = ?";

        Cursor cursor = banco.get().rawQuery(query, new String[]{String.valueOf(codigoTurma)});

        if(cursor.moveToFirst()){

            do{
                String dia = cursor.getString(cursor.getColumnIndex("dia"));
                String mes = cursor.getString(cursor.getColumnIndex("mes"));
                String ano = cursor.getString(cursor.getColumnIndex("ano"));

                diasComFrequencia.add(dia + "/" + mes  + "/" + ano);
            }while (cursor.moveToNext());
        }

        cursor.close();

        return diasComFrequencia;
    }
}
