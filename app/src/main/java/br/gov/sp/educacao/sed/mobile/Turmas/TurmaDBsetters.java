package br.gov.sp.educacao.sed.mobile.Turmas;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDoneException;
import android.database.sqlite.SQLiteStatement;
import android.util.SparseIntArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.gov.sp.educacao.sed.mobile.Avaliacao.Avaliacao;
import br.gov.sp.educacao.sed.mobile.Avaliacao.AvaliacaoDBgetters;
import br.gov.sp.educacao.sed.mobile.Avaliacao.AvaliacaoDBsetters;
import br.gov.sp.educacao.sed.mobile.Avaliacao.AvaliacoesTO;
import br.gov.sp.educacao.sed.mobile.Avaliacao.NotasAlunoTO;
import br.gov.sp.educacao.sed.mobile.Escola.AulasTO;
import br.gov.sp.educacao.sed.mobile.Escola.BimestreTO;
import br.gov.sp.educacao.sed.mobile.Escola.DiasLetivosTO;
import br.gov.sp.educacao.sed.mobile.Escola.DisciplinaTO;
import br.gov.sp.educacao.sed.mobile.Fechamento.FechamentoAlunoTO;
import br.gov.sp.educacao.sed.mobile.Fechamento.FechamentoTurmaTO;
import br.gov.sp.educacao.sed.mobile.Fechamento.TipoFechamentoTO;
import br.gov.sp.educacao.sed.mobile.Frequencia.TotalFaltasAlunosTO;
import br.gov.sp.educacao.sed.mobile.Login.UsuarioTO;
import br.gov.sp.educacao.sed.mobile.RegistroDeAula.HabilidadeConteudo;
import br.gov.sp.educacao.sed.mobile.RegistroDeAula.ObjetoConteudo;
import br.gov.sp.educacao.sed.mobile.RegistroDeAula.Registro;
import br.gov.sp.educacao.sed.mobile.RegistroDeAula.RegistroDBcrud;
import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.GenericsTable;
import br.gov.sp.educacao.sed.mobile.util.Utils;

public class TurmaDBsetters {

    private final String TAG = TurmaDBsetters.class.getSimpleName();

    private Banco banco;
    private RegistroDBcrud registroDBcrud;
    private AvaliacaoDBsetters avaliacaoDBsetters;
    private AvaliacaoDBgetters avaliacaoDBgetters;
    private SparseIntArray alunosSparseArray;
    private SparseIntArray turmasSparseArray;
    private SQLiteStatement statementDiasComFrequencia;
    private SQLiteStatement statementDiasLetivos;
    private SQLiteStatement statementDisciplinaId;
    private SQLiteStatement statementTotalFaltasAlunos;
    private SQLiteStatement statementInsertAulas;
    private SQLiteStatement statementInsertAvaliacao;
    private SQLiteStatement statementInsertDisciplina;
    private SQLiteStatement statementInsertNotas;
    private SQLiteStatement statementUpdateDiasComFrequencia;

    public TurmaDBsetters(Banco banco) {

        this.banco = banco;

        this.registroDBcrud = new RegistroDBcrud(banco);

        this.avaliacaoDBsetters = new AvaliacaoDBsetters(banco);

        this.avaliacaoDBgetters = new AvaliacaoDBgetters(banco);
    }

    public UsuarioTO getUsuarioAtivo(SQLiteDatabase database) {
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
        UsuarioTO usuarioTO = null;
        Cursor cursor = database.rawQuery(query.toString(), null);
        if (cursor.moveToFirst()) {
            usuarioTO = new UsuarioTO(cursor);
        }
        return usuarioTO;
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

    void updateTurmas(JSONObject retorno) throws JSONException {
        if (retorno.has("TurmasTurmas") && !retorno.isNull("TurmasTurmas")) {
            JSONArray turmasJsonArray = retorno.getJSONArray("TurmasTurmas");
            int numeroTurmas = turmasJsonArray.length();
            if (numeroTurmas > 0) {
                int i;
                int j;
                int k;
                int ano = retorno.getInt("Ano");
                alunosSparseArray = new SparseIntArray();
                turmasSparseArray = new SparseIntArray();
                SQLiteDatabase database = banco.get();
                AlunosTO alunosTO = new AlunosTO();
                TurmasTO turmasTO = new TurmasTO();
                SQLiteStatement alunosStatement = database.compileStatement("SELECT id FROM " + AlunosTO.nomeTabela + " WHERE codigoMatricula = ?");
                SQLiteStatement turmasStatement = database.compileStatement("SELECT id FROM " + TurmasTO.nomeTabela + " WHERE codigoTurma = ?;");
                database.beginTransaction();
                for(i = 0; i < numeroTurmas; i++) {
                    JSONObject jsonTurma = turmasJsonArray.getJSONObject(i);
                    turmasTO.setJSON(jsonTurma, ano);
                    int codigoTurma = turmasTO.getCodigoTurma();
                    turmasStatement.bindLong(1, codigoTurma);
                    int turmaId = setDadosDataBase2(TurmasTO.nomeTabela, turmasTO, database, turmasStatement);
                    turmasSparseArray.put(codigoTurma, turmaId);
                    if (turmaId != -1) {
                        JSONArray jsonArrayAlunos = jsonTurma.getJSONArray("Alunos");
                        int numeroAlunos = jsonArrayAlunos.length();
                        for(j = 0; j < numeroAlunos; j++) {
                            JSONObject jsonAluno = jsonArrayAlunos.getJSONObject(j);
                            alunosTO.setJSON(jsonAluno, turmaId);
                            String codigoMatricula = alunosTO.getCodigoMatricula();
                            alunosStatement.bindString(1, codigoMatricula);
                            int alunoId = setDadosDataBase2(AlunosTO.nomeTabela, alunosTO, database, alunosStatement);
                            alunosSparseArray.put(codigoMatricula.hashCode(), alunoId);
                        }
                    }
                }
                alunosStatement.close();
                turmasStatement.close();
                if (retorno.has("FechamentoParametrizacao") && !retorno.isNull("FechamentoParametrizacao")) {
                    JSONArray tiposFechamentosJson = retorno.getJSONArray("FechamentoParametrizacao");
                    int numeroFechamentos = tiposFechamentosJson.length();
                    if (numeroFechamentos > 0) {
                        TipoFechamentoTO tipoFechamentoTO = new TipoFechamentoTO();
                        SQLiteStatement tipoFechamentoId = database.compileStatement("SELECT id FROM " + TipoFechamentoTO.nomeTabela + " WHERE ?");
                        for (i = 0; i < numeroFechamentos; i++) {
                            JSONObject tipoFechamentoJson = tiposFechamentosJson.getJSONObject(i);
                            tipoFechamentoTO.setJSON(tipoFechamentoJson);
                            tipoFechamentoId.bindString(1, tipoFechamentoTO.getCodigoUnico());
                            setDadosDataBase2(TipoFechamentoTO.nomeTabela, tipoFechamentoTO, database, tipoFechamentoId);
                        }
                        tipoFechamentoId.close();
                    }
                }
                if (retorno.has("TurmasFrequencia") && !retorno.isNull("TurmasFrequencia")) {
                    JSONArray turmasFrequenciaJsonArray = retorno.getJSONArray("TurmasFrequencia");
                    int numeroFrequencias = turmasFrequenciaJsonArray.length();
                    if (numeroFrequencias > 0) {
                        TurmasFrequenciaTO turmasFrequenciaTO = new TurmasFrequenciaTO();
                        SQLiteStatement statementAtualizarBimestre = database.compileStatement("UPDATE BIMESTRE SET bimestreAtual = 0 WHERE turmasFrequencia_id = ? AND numero = ?;");
                        SQLiteStatement statementAtualizarBimestreAtual = database.compileStatement("UPDATE BIMESTRE SET bimestreAtual = 1 WHERE turmasFrequencia_id = ? AND numero = ? AND inicioBimestre = ? AND fimBimestre = ?;");
                        SQLiteStatement statementBimestresCalendario = database.compileStatement("SELECT id FROM " + BimestreTO.nomeTabela + " WHERE ?");
                        SQLiteStatement statementBimestreId = database.compileStatement("SELECT id FROM BIMESTRE WHERE numero = ? AND turmasFrequencia_id = ?;");
                        SQLiteStatement statementBimestreId2 = database.compileStatement("SELECT id FROM BIMESTRE WHERE bimestreAtual = 1 AND turmasFrequencia_id = ?;");
                        SQLiteStatement statementInsertBimestre = database.compileStatement("INSERT OR REPLACE INTO BIMESTRE (bimestreAtual, numero, inicioBimestre, fimBimestre, turmasFrequencia_id) VALUES (?, ?, ?, ?, ?);");
                        SQLiteStatement statementInserirTurmaFrequencia = database.compileStatement("INSERT OR REPLACE INTO " + TurmasFrequenciaTO.nomeTabela + "(codigoTurma, codigoDiretoria, codigoEscola, codigoTipoEnsino, aulasBimestre, aulasAno, turma_id, aulasSemana) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                        SQLiteStatement statementTemBimestreBanco = database.compileStatement("SELECT id FROM BIMESTRE WHERE numero = ? AND turmasFrequencia_id = ?;");
                        SQLiteStatement statementTurmaFrequenciaId = database.compileStatement("SELECT id FROM " + TurmasFrequenciaTO.nomeTabela + " WHERE turma_id = ?;");
                        SQLiteStatement selecionarTurmaId = database.compileStatement("SELECT id FROM TURMAS WHERE codigoTurma = ?");
                        statementInsertAulas = database.compileStatement("SELECT id FROM " + AulasTO.nomeTabela + " WHERE ?");
                        statementDiasComFrequencia = database.compileStatement("INSERT INTO DIASCOMFREQUENCIA (dataFrequenciaDia, dataFrequenciaMes, dataFrequenciaAno, turmasFrequencia_id, horario) VALUES (?, ? , ? , ?, ?);");
                        statementInsertDisciplina = database.compileStatement("SELECT id FROM " + DisciplinaTO.nomeTabela + " WHERE ?");
                        statementTotalFaltasAlunos = database.compileStatement("SELECT id FROM " + TotalFaltasAlunosTO.nomeTabela + " WHERE ?");
                        statementUpdateDiasComFrequencia = database.compileStatement("UPDATE DIASCOMFREQUENCIA SET dataFrequenciaDia = ?, dataFrequenciaMes = ?, dataFrequenciaAno = ?, turmasFrequencia_id = ?, horario = ? WHERE dataFrequenciaDia = ? AND dataFrequenciaMes = ? AND dataFrequenciaAno = ? AND turmasFrequencia_id = ? AND horario = ?;");
                        statementDiasLetivos = database.compileStatement("SELECT id FROM " + DiasLetivosTO.nomeTabela + " WHERE ?");
                        statementDisciplinaId = database.compileStatement("SELECT id FROM DISCIPLINA WHERE codigoDisciplina = ? AND turmasFrequencia_id = ?");
                        statementInsertAvaliacao = database.compileStatement("SELECT id FROM AVALIACOES WHERE codigoAvaliacao = ? AND disciplina_id = ?");
                        statementInsertNotas = database.compileStatement("SELECT id FROM " + NotasAlunoTO.nomeTabela + " WHERE ?");
                        for(i = 0; i < numeroFrequencias; i++) {
                            JSONObject frequenciaJson = turmasFrequenciaJsonArray.getJSONObject(i);
                            int codigoTurma = frequenciaJson.getInt("CodigoTurma");
                            int turmaId = turmasSparseArray.get(codigoTurma);
                            turmasFrequenciaTO.setJSON(frequenciaJson, turmaId);
                            statementTurmaFrequenciaId.bindLong(1, turmaId);
                            int turmaFrequenciaId = -1;
                            try {
                                turmaFrequenciaId = (int) statementTurmaFrequenciaId.simpleQueryForLong();
                            }
                            catch (SQLiteDoneException e) {
                            }
                            if (turmaFrequenciaId == -1) {
                                statementInserirTurmaFrequencia.bindLong(1, codigoTurma);
                                statementInserirTurmaFrequencia.bindLong(2, turmasFrequenciaTO.getCodigoDiretoria());
                                statementInserirTurmaFrequencia.bindLong(3, turmasFrequenciaTO.getCodigoEscola());
                                statementInserirTurmaFrequencia.bindLong(4, turmasFrequenciaTO.getCodigoTipoEnsino());
                                statementInserirTurmaFrequencia.bindLong(5, 0);
                                statementInserirTurmaFrequencia.bindLong(6, 0);
                                statementInserirTurmaFrequencia.bindLong(7, turmaId);
                                statementInserirTurmaFrequencia.bindLong(8, turmasFrequenciaTO.getAulasSemana());
                                turmaFrequenciaId = (int) statementInserirTurmaFrequencia.executeInsert();
                            }
                            if (turmaFrequenciaId != -1) {
                                JSONObject disciplinaJson = frequenciaJson.getJSONObject("Disciplina");
                                int codigoDisciplina = Integer.parseInt(disciplinaJson.getString("CodigoDisciplina"));
                                convertJSONArrayForTurmasDisciplina(codigoDisciplina, turmaFrequenciaId, disciplinaJson, database);
                                int[] turmasFrequenciaIds = null;
                                Cursor cursor = database.rawQuery("SELECT id FROM TURMASFREQUENCIA WHERE codigoTurma = " + codigoTurma, null);
                                int numero = cursor.getCount();
                                if (numero > 0) {
                                    j = 0;
                                    turmasFrequenciaIds = new int[numero];
                                    while (cursor.moveToNext()) {
                                        turmasFrequenciaIds[j] = cursor.getInt(0);
                                        j++;
                                    }
                                }
                                cursor.close();
                                if (turmasFrequenciaIds != null) {
                                    if(numero > 1) {
                                        for(j = 0; j < numero; j++) {
                                            atualizarFaltas(codigoDisciplina, turmasFrequenciaIds[j], disciplinaJson, database);
                                        }
                                    }
                                    else {
                                        atualizarFaltas(codigoDisciplina, turmaFrequenciaId, disciplinaJson, database);
                                    }
                                }
                                JSONArray diasComFrequenciaJson = frequenciaJson.getJSONArray("DiasComFrequencia");
                                int numeroDatas = diasComFrequenciaJson.length();
                                List<String> listaDiasFrequenciaJson = new ArrayList<>(numeroDatas);
                                for(j = 0; j < numeroDatas; j++) {
                                    String data = diasComFrequenciaJson.getString(j);
                                    String[] componentesData = data.split("T");
                                    String dataFormatada = componentesData[0];
                                    String horario = componentesData[1];
                                    String[] dataSeparada = dataFormatada.split("[-]");
                                    String mes = dataSeparada[1];
                                    if (Integer.parseInt(mes) < 10) {
                                        mes = mes.substring(1);
                                    }
                                    String dia = dataSeparada[2];
                                    if (Integer.parseInt(dia) < 10) {
                                        dia = dia.substring(1);
                                    }
                                    String diaComFrequencia = ano + "-" + mes + "-" + dia + "T" + horario;// +":00";
                                    listaDiasFrequenciaJson.add(diaComFrequencia);
                                    setDiasComFrequencia(data, turmaFrequenciaId);
                                }
                                List<String> listaDiasFrequenciaBanco = new ArrayList<>();
                                cursor = database.rawQuery("SELECT COUNT(id) FROM DIASCOMFREQUENCIA WHERE turmasFrequencia_id = " + turmaFrequenciaId, null);
                                if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                                    int totalNumeroDatasNoBanco = cursor.getInt(0);
                                    if (totalNumeroDatasNoBanco > numeroDatas) {
                                        Cursor cursor1 = database.rawQuery("SELECT dataFrequenciaAno, dataFrequenciaMes, dataFrequenciaDia, horario FROM DIASCOMFREQUENCIA WHERE turmasFrequencia_id = " + turmaFrequenciaId, null);
                                        if (cursor1.getCount() > 0) {
                                            while (cursor1.moveToNext()) {
                                                String diaBanco = cursor1.getString(cursor1.getColumnIndex("dataFrequenciaAno")) + "-"
                                                        + cursor1.getString(cursor1.getColumnIndex("dataFrequenciaMes")) + "-"
                                                        + cursor1.getString(cursor1.getColumnIndex("dataFrequenciaDia")) + "T"
                                                        + cursor1.getString(cursor1.getColumnIndex("horario"));// + ":00";
                                                listaDiasFrequenciaBanco.add(diaBanco);
                                            }
                                            cursor1.close();
                                        }
                                    }
                                }
                                cursor.close();
                                int numeroDiasFrequenciaBanco = listaDiasFrequenciaBanco.size();
                                for(j = 0; j < numeroDiasFrequenciaBanco; j++) {
                                    String diaFrequenciaBanco = listaDiasFrequenciaBanco.get(j);
                                    if(!listaDiasFrequenciaJson.contains(diaFrequenciaBanco)) {
                                        deletarDiasComFrequenciaBancoLocal(turmaFrequenciaId, diaFrequenciaBanco, database);
                                    }
                                }
                                convertJSONArrayForTurmasAvaliacoes(codigoDisciplina, codigoTurma, frequenciaJson.getJSONArray("Avaliacoes"), database);
                                JSONArray bimestreCalendarioJson = frequenciaJson.getJSONArray("BimestreCalendario");
                                int bimestres = bimestreCalendarioJson.length();
                                BimestreTO bimestreTO = new BimestreTO();
                                for(j = 0; j < bimestres; j++) {
                                    bimestreTO.setJSON(bimestreCalendarioJson.getJSONObject(j), turmaFrequenciaId);
                                    if(!getBimestreBanco(bimestreTO, statementTemBimestreBanco)) {
                                        setDadosDataBase2(BimestreTO.nomeTabela, bimestreTO, database, statementBimestresCalendario);
                                    }
                                }
                                JSONObject bimestreAtualJson = frequenciaJson.getJSONObject("BimestreAtual");
                                bimestreTO.setJSON(bimestreAtualJson, turmaFrequenciaId);
                                statementAtualizarBimestreAtual.bindLong(   1, bimestreTO.getTurmasFrequencia_id());
                                statementAtualizarBimestreAtual.bindLong(   2, bimestreTO.getNumero());
                                statementAtualizarBimestreAtual.bindString( 3, bimestreTO.getInicioBimestre());
                                statementAtualizarBimestreAtual.bindString( 4, bimestreTO.getFimBimestre());
                                int bimestreAtualId = -1;
                                int rowsUpdate = statementAtualizarBimestreAtual.executeUpdateDelete();
                                if (rowsUpdate == 0) {
                                    statementInsertBimestre.bindLong(1,1);
                                    statementInsertBimestre.bindLong(2, bimestreTO.getNumero());
                                    statementInsertBimestre.bindString(3, bimestreTO.getInicioBimestre());
                                    statementInsertBimestre.bindString(4, bimestreTO.getFimBimestre());
                                    statementInsertBimestre.bindLong(5, bimestreTO.getTurmasFrequencia_id());
                                    bimestreAtualId = (int) statementInsertBimestre.executeInsert();
                                }
                                else {
                                    statementBimestreId2.bindLong(1, turmaFrequenciaId);
                                    try {
                                        bimestreAtualId = (int) statementBimestreId2.simpleQueryForLong();
                                    }
                                    catch (SQLiteDoneException e) {
                                    }
                                }
                                if (bimestreAtualId != -1) {
                                    convertJSONArrayForTurmasCalendario(ano, bimestreAtualId, frequenciaJson.getJSONArray("CalendarioBimestreAtual"), database);
                                }
                                JSONObject bimestreAnteriorJson = frequenciaJson.getJSONObject("BimestreAnterior");
                                bimestreTO.setJSON(bimestreAnteriorJson, turmaFrequenciaId);
                                int bimestreAnteriorId = -1;
                                try {
                                    statementBimestreId.bindLong(1, bimestreTO.getNumero());
                                    statementBimestreId.bindLong(2, bimestreTO.getTurmasFrequencia_id());
                                    bimestreAnteriorId = (int) statementBimestreId.simpleQueryForLong();
                                }
                                catch(SQLiteDoneException e) {
                                }
                                if (bimestreAnteriorId != -1) {
                                    convertJSONArrayForTurmasCalendario(ano, bimestreAnteriorId, frequenciaJson.getJSONArray("CalendarioBimestreAnterior"), database);
                                }
                            }
                        }
                        statementAtualizarBimestre.close();
                        statementAtualizarBimestreAtual.close();
                        statementBimestresCalendario.close();
                        statementBimestreId.close();
                        statementBimestreId2.close();
                        statementTemBimestreBanco.close();
                        selecionarTurmaId.close();
                        statementInsertAulas.close();
                        statementDiasComFrequencia.close();
                        statementInsertDisciplina.close();
                        statementTotalFaltasAlunos.close();
                        statementUpdateDiasComFrequencia.close();
                        statementDiasLetivos.close();
                        statementDisciplinaId.close();
                        statementInsertAvaliacao.close();
                        statementInsertNotas.close();
                    }
                }
                if (retorno.has("Fechamentos") && !retorno.isNull("Fechamentos")) {
                    JSONArray fechamentoJsonArray = retorno.getJSONArray("Fechamentos");
                    int numeroFechamentos = fechamentoJsonArray.length();
                    if (fechamentoJsonArray.length() > 0) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                        String data = dateFormat.format(new Date());
                        FechamentoAlunoTO fechamentoAlunoTO = new FechamentoAlunoTO();
                        FechamentoTurmaTO fechamentoTurmaTO = new FechamentoTurmaTO();
                        alunosStatement = database.compileStatement("SELECT id FROM " + FechamentoAlunoTO.nomeTabela + " WHERE ?");
                        turmasStatement = database.compileStatement("SELECT id FROM " + FechamentoTurmaTO.nomeTabela + " WHERE ?");
                        for(i = 0; i < numeroFechamentos; i++) {
                            JSONObject fechamentoJson = fechamentoJsonArray.getJSONObject(i);
                            int aulasPlanejadas = fechamentoJson.getInt("AulasPlanejadas");
                            int aulasRealizadas = fechamentoJson.getInt("AulasRealizadas");
                            int codigoDisciplina = fechamentoJson.getInt("CodigoDisciplina");
                            int codigoTipoFechamento = fechamentoJson.getInt("CodigoTipoFechamento");
                            int codigoTurma = fechamentoJson.getInt("CodigoTurma");
                            String justificativa = fechamentoJson.getString("Justificativa");
                            fechamentoTurmaTO.setCodigoTurma(codigoTurma);
                            fechamentoTurmaTO.setCodigoTipoFechamento(codigoTipoFechamento);
                            fechamentoTurmaTO.setCodigoDisciplina(codigoDisciplina);
                            fechamentoTurmaTO.setAulasRealizadas(aulasRealizadas);
                            fechamentoTurmaTO.setAulasPlanejadas(aulasPlanejadas);
                            fechamentoTurmaTO.setJustificativa(justificativa);
                            fechamentoTurmaTO.setDataServidor(data);
                            turmasStatement.bindString(1, fechamentoTurmaTO.getCodigoUnico());
                            setDadosDataBase2(FechamentoTurmaTO.nomeTabela, fechamentoTurmaTO, database, turmasStatement);
                            JSONArray fechamentosAlunosJson = fechamentoJson.getJSONArray("Fechamentos");
                            int numeroFechamentosAluno = fechamentosAlunosJson.length();
                            for(j = 0; j < numeroFechamentosAluno; j++) {
                                JSONObject fechamentoAlunoJson = fechamentosAlunosJson.getJSONObject(j);
                                fechamentoAlunoTO.setCodigoTurma(codigoTurma);
                                fechamentoAlunoTO.setCodigoDisciplina(codigoDisciplina);
                                fechamentoAlunoTO.setCodigoTipoFechamento(codigoTipoFechamento);
                                fechamentoAlunoTO.setCodigoFechamento(fechamentoAlunoJson.getInt("Codigo"));
                                fechamentoAlunoTO.setFaltas(fechamentoAlunoJson.getInt("Faltas"));
                                fechamentoAlunoTO.setAusenciasCompensadas(fechamentoAlunoJson.getInt("FaltasCompensadas"));
                                fechamentoAlunoTO.setFaltasAcumuladas(fechamentoAlunoJson.getInt("FaltasAcumuladas"));
                                if (aulasPlanejadas != 0 && aulasRealizadas != 0 && justificativa != null && !fechamentoAlunoJson.isNull("Nota")) {
                                    fechamentoAlunoTO.setDataServidor(data);
                                }
                                else {
                                    fechamentoAlunoTO.setConfirmado(Utils.ALUNO_NAO_CONFIRMADO_FECHAMENTO);
                                }
                                if(!fechamentoAlunoJson.isNull("Justificativa")) {
                                    fechamentoAlunoTO.setJustificativa(fechamentoAlunoJson.getString("Justificativa"));
                                }
                                else {
                                    fechamentoAlunoTO.setJustificativa("");
                                }
                                String fechamentoAlunoString = fechamentoAlunoJson.toString();
                                char[] caracteres = fechamentoAlunoString.toCharArray();
                                int numeroCaracteres = caracteres.length;
                                StringBuilder matriculaStringBuilder = new StringBuilder();
                                for(k = fechamentoAlunoString.indexOf("\"Aluno\":") + 8; k < numeroCaracteres; k++) {
                                    char c = caracteres[k];
                                    if (c != ',' && c != '}') {
                                        matriculaStringBuilder.append(c);
                                    }
                                    else {
                                        break;
                                    }
                                }
                                fechamentoAlunoTO.setCodigoMatricula(matriculaStringBuilder.toString());
                                if(!fechamentoAlunoJson.isNull("Nota")) {
                                    fechamentoAlunoTO.setNota(fechamentoAlunoJson.getInt("Nota"));
                                }
                                else {
                                    Cursor cursor = database.rawQuery("SELECT * FROM alunos WHERE codigoMatricula = " + fechamentoAlunoJson.getInt("Aluno"), null);
                                    if (cursor.getCount() > 0 && cursor.moveToNext() && cursor.getInt(cursor.getColumnIndex("alunoAtivo")) == 1) {
                                        fechamentoAlunoTO.setNota(Utils.ALUNO_ATIVO_SEM_NOTA);
                                    }
                                    else {
                                        fechamentoAlunoTO.setNota(Utils.ALUNO_ATIVO_SEM_NOTA);
                                    }
                                    cursor.close();
                                }
                                alunosStatement.bindString(1, fechamentoAlunoTO.getCodigoUnico());
                                setDadosDataBase2(FechamentoAlunoTO.nomeTabela, fechamentoAlunoTO, database, alunosStatement);
                            }
                        }
                        alunosStatement.close();
                    }
                }
                if (retorno.has("RegistrosAula") && !retorno.isNull("RegistrosAula")) {
                    JSONArray registrosAulaJsonArray = retorno.getJSONArray("RegistrosAula");
                    int numeroRegistroAula = registrosAulaJsonArray.length();
                    if (numeroRegistroAula > 0) {
                        SQLiteStatement statementDisciplinaCurriculo = database.compileStatement("SELECT codigoDisciplina FROM NOVO_GRUPO WHERE codigo = ?;");
                        List<HabilidadeConteudo> habilidadeConteudo = new ArrayList<>();
                        List<Integer> habilidades = new ArrayList<>();
                        for(i = 0; i < numeroRegistroAula; i++) {
                            JSONObject registroJson = registrosAulaJsonArray.getJSONObject(i);
                            int codigoNovoRegistro = registroJson.getInt("Codigo");
                            int bimestre = registroJson.getInt("Bimestre");
                            int codigoGrupoCurriculo = registroJson.getInt("CodigoGrupoCurriculo");
                            String codigoTurma = registroJson.getString("CodigoTurma");
                            String observacoes = registroJson.getString("Observacoes");
                            String dataCriacao = registroJson.getString("DataCriacao");
                            Registro registro = new Registro();
                            registro.setCodNovoRegistro(codigoNovoRegistro);
                            registro.setBimestre(bimestre);
                            registro.setCodigoTurma(codigoTurma);
                            registro.setObservacoes(observacoes);
                            registro.setCodigoGrupoCurriculo(String.valueOf(codigoGrupoCurriculo));
                            registro.setDataCriacao(dataCriacao.substring(0, 10));
                            int codigoDisciplina = 0;
                            statementDisciplinaCurriculo.bindLong(1, codigoGrupoCurriculo);
                            try {
                                codigoDisciplina = (int) statementDisciplinaCurriculo.simpleQueryForLong();
                            }
                            catch(SQLiteDoneException e) {
                            }
                            registro.setCodigoDisciplina(String.valueOf(codigoDisciplina));
                            JSONArray habilidadesJson = registroJson.getJSONArray("Habilidades");
                            int numeroHabilidades = habilidadesJson.length();
                            habilidadeConteudo.clear();
                            for(j = 0; j < numeroHabilidades; j++) {
                                HabilidadeConteudo habcon = new HabilidadeConteudo();
                                JSONObject habilidadeConteudoJson = habilidadesJson.getJSONObject(j);
                                int codigoConteudo = habilidadeConteudoJson.getInt("CodigoConteudo");
                                int codigoHabilidade = habilidadeConteudoJson.getInt("CodigoHabilidade");
                                habcon.setCodigoConteudo(codigoConteudo);
                                habcon.setCodigoHabilidade(codigoHabilidade);
                                habilidadeConteudo.add(habcon);
                            }
                            int numeroHabilidadesConteudo = habilidadeConteudo.size();
                            List<ObjetoConteudo> conteudos = new ArrayList<>();
                            for(j = 0; j < numeroHabilidadesConteudo; j++) {
                                HabilidadeConteudo habcon = new HabilidadeConteudo();
                                habcon = habilidadeConteudo.get(j);
                                int codigoConteudo = habcon.getCodigoConteudo();
                                habilidades.clear();
                                ObjetoConteudo objcont = new ObjetoConteudo();
                                objcont.setCodigoConteudo(codigoConteudo);
                                for(k = 0; k < numeroHabilidadesConteudo; k++) {
                                    HabilidadeConteudo habilidadeConteudo1 = habilidadeConteudo.get(k);
                                    if(habilidadeConteudo1.getCodigoConteudo() == codigoConteudo) {
                                        habilidades.add(habilidadeConteudo1.getCodigoHabilidade());
                                    }
                                }
                                objcont.setCodigosHabilidades(habilidades);
                                conteudos.add(objcont);
                            }
                            registro.setConteudos(conteudos);
                            registroDBcrud.salvarRegistroBanco(registro);
                        }
                        statementDisciplinaCurriculo.close();
                    }
                }
                database.setTransactionSuccessful();
                database.endTransaction();
                alunosSparseArray.clear();
                turmasSparseArray.clear();
            }
        }
    }

    private void setDiasComFrequencia(String data, int turmaFrequenciaId) {
        String[] componenetesData = data.split("T");
        String dataFormatada = componenetesData[0];
        String horario = componenetesData[1];
        String[] dataSeparada = dataFormatada.split("[-]");
        String dia = Integer.valueOf(dataSeparada[2]) < 10 ? dataSeparada[2].substring(1) : dataSeparada[2];
        String mes = Integer.valueOf(dataSeparada[1]) < 10 ? dataSeparada[1].substring(1) : dataSeparada[1];
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
        int colunasAtualizadas = statementUpdateDiasComFrequencia.executeUpdateDelete();
        if(colunasAtualizadas == 0) {
            statementDiasComFrequencia.bindString(1, dia);
            statementDiasComFrequencia.bindString(2, mes);
            statementDiasComFrequencia.bindString(3, dataSeparada[0]);
            statementDiasComFrequencia.bindLong(  4, turmaFrequenciaId);
            statementDiasComFrequencia.bindString(5, horario);
            statementDiasComFrequencia.executeInsert();
        }
        statementDiasComFrequencia.clearBindings();
        statementUpdateDiasComFrequencia.clearBindings();
    }

    private Integer setDadosDataBase2(String nameTable, GenericsTable genericsTable, SQLiteDatabase database, SQLiteStatement statement) {
        long id = -1;
        try {
            if (genericsTable.getCodigoUnico() != null) {
                int rowsUpdate = database.update(nameTable, genericsTable.getContentValues(), genericsTable.getCodigoUnico(), null);
                if(rowsUpdate == 0) {
                    id = database.insertWithOnConflict(nameTable, null, genericsTable.getContentValues(), SQLiteDatabase.CONFLICT_REPLACE);
                }
                else {
                    id = statement.simpleQueryForLong();
                }
            }
            else {
                id = database.insert(nameTable, null, genericsTable.getContentValues());
            }
        }
        catch (SQLiteDoneException e) {
        }
        return (int) id;
    }

    private void deletarDiasComFrequenciaBancoLocal(int turmaFrequenciaId, String data, SQLiteDatabase database) {
        String[] componentesData = data.split("-");
        String anoFrequencia = componentesData[0];
        String mesFrequencia = componentesData[1];
        String diaFrequencia = componentesData[2].split("T")[0];
        String horario = data.split("T")[1].substring(0, 5);
        Cursor cursor = database.rawQuery("DELETE FROM DIASCOMFREQUENCIA WHERE dataFrequenciaDia = " + diaFrequencia + " AND dataFrequenciaMes = " + mesFrequencia + " AND dataFrequenciaAno = " + anoFrequencia + " AND horario = '" + horario + "' AND turmasFrequencia_id = " + turmaFrequenciaId, null);
        cursor.moveToFirst();
        cursor.close();
    }

    private void convertJSONArrayForTurmasDisciplina(int codigoDisciplina, int turmasFrequenciaId, JSONObject disciplinaJson, SQLiteDatabase database) throws JSONException {
        int i;
        DisciplinaTO disciplinaTO = new DisciplinaTO();
        disciplinaTO.setJSON(disciplinaJson, turmasFrequenciaId);
        statementInsertDisciplina.bindString(1, disciplinaTO.getCodigoUnico());
        int disciplina_id_disc = setDadosDataBase2(DisciplinaTO.nomeTabela, disciplinaTO, database, statementInsertDisciplina);
        if (disciplina_id_disc != -1) {
            AulasTO aulasTO = new AulasTO();
            JSONArray aulasJson = disciplinaJson.getJSONArray("Aulas");
            int numeroAulas = aulasJson.length();
            for(i = 0; i < numeroAulas; i++) {
                JSONObject aulaJson = aulasJson.getJSONObject(i);
                aulasTO.setJSON(aulaJson, disciplina_id_disc);
                statementInsertAulas.bindString(1, aulasTO.getCodigoUnico());
                setDadosDataBase2(AulasTO.nomeTabela, aulasTO, database, statementInsertAulas);
            }
            atualizarFaltas(codigoDisciplina, turmasFrequenciaId, disciplinaJson, database);
        }
    }

    private void convertJSONArrayForTurmasAvaliacoes(int codigoDisciplina, int codigoTurma, JSONArray avaliacoesJson, SQLiteDatabase database) throws JSONException {
        int i;
        int j;
        int k;
        int l;
        int numeroAvaliacoes = avaliacoesJson.length();
        int[] avaliacaoIds = new int[numeroAvaliacoes];
        for (i = 0; i < numeroAvaliacoes; i++) {
            avaliacaoIds[i] = avaliacoesJson.getJSONObject(i).getInt("Codigo");
        }
        List<Avaliacao> listaAvaliacaoBanco = avaliacaoDBgetters.getListaTotalAvaliacoes(codigoTurma, String.valueOf(codigoDisciplina));
        int numeroAvaliacoesBanco = listaAvaliacaoBanco.size();
        for (i = 0; i < numeroAvaliacoesBanco; i++) {
            boolean encontrou = false;
            Avaliacao avaliacao = listaAvaliacaoBanco.get(i);
            int codigoAvaliacao = avaliacao.getCodigo();
            for (j = 0; j < numeroAvaliacoes; j++) {
                if (avaliacaoIds[j] == codigoAvaliacao) {
                    encontrou = true;
                    break;
                }
            }
            if (!encontrou) {
                avaliacaoDBsetters.setAvaliacaoParaDeletar(avaliacao.getId());
            }
        }
        int usuarioId = getUsuarioAtivo(database).getId();
        AvaliacoesTO avaliacoesTO = new AvaliacoesTO();
        NotasAlunoTO notasAlunoTO = new NotasAlunoTO();
        for(i = 0; i < numeroAvaliacoes; i++) {
            JSONObject avaliacaoJson = avaliacoesJson.getJSONObject(i);
            int codigoTurma1 = avaliacaoJson.getInt("CodigoTurma");
            List<Integer> turmasFrequenciaIds = avaliacaoDBgetters.getTurmasFrequenciaIds(codigoTurma1);
            int numeroTurmasFrequencia = turmasFrequenciaIds.size();
            int codigoDisciplinaJson = avaliacaoJson.getInt("CodigoDisciplina");
            if(codigoDisciplina == 1000) {
                codigoDisciplinaJson = codigoDisciplina;
            }
            for (j = 0; j < numeroTurmasFrequencia; j++) {
                int turmaFrequenciaId = turmasFrequenciaIds.get(j);
                int turmaId = turmasSparseArray.get(codigoTurma1);
                statementDisciplinaId.bindLong(1, codigoDisciplinaJson);
                statementDisciplinaId.bindLong(2, turmaFrequenciaId);
                int avaliacaoDisciplinaId = -1;
                try {
                    avaliacaoDisciplinaId = (int) statementDisciplinaId.simpleQueryForLong();
                }
                catch (SQLiteDoneException e) {
                }
                if(avaliacaoDisciplinaId != -1 && turmaId != -1) {
                    avaliacoesTO.setJSON(avaliacaoJson, turmaId, avaliacaoDisciplinaId);
                    statementInsertAvaliacao.bindLong(1, avaliacoesTO.getCodigoAvaliacao());
                    statementInsertAvaliacao.bindLong(2, avaliacoesTO.getDisciplina_id());
                    int avaliacaoId = setDadosDataBase2(AvaliacoesTO.nomeTabela, avaliacoesTO, database, statementInsertAvaliacao);
                    if (avaliacaoId != -1 && avaliacaoJson.has("Notas")) {
                        JSONArray notasJson = avaliacaoJson.getJSONArray("Notas");
                        int numeroNotas = notasJson.length();
                        if (numeroNotas > 0) {
                            for(k = 0; k < numeroNotas; k++) {
                                JSONObject notaJson = notasJson.getJSONObject(k);
                                String notaJsonString = notaJson.toString();
                                char[] caracteres = notaJsonString.toCharArray();
                                int numeroCaracteres = caracteres.length;
                                StringBuilder matriculaStringBuilder = new StringBuilder();
                                for(l = notaJsonString.indexOf("\"CodigoMatriculaAluno\":") + 23; l < numeroCaracteres; l++) {
                                    char c = caracteres[l];
                                    if (c != ',' && c != '}') {
                                        matriculaStringBuilder.append(c);
                                    }
                                    else {
                                        break;
                                    }
                                }
                                int alunoId = alunosSparseArray.get(matriculaStringBuilder.toString().hashCode());
                                notasAlunoTO.setJSON(notaJson, alunoId, usuarioId, avaliacaoId);
                                statementInsertNotas.bindString(1, notasAlunoTO.getCodigoUnico());
                                setDadosDataBase2(NotasAlunoTO.nomeTabela, notasAlunoTO, database, statementInsertNotas);
                            }
                            Cursor cursor = database.rawQuery("SELECT ALUNOS.id, ALUNOS.codigoMatricula FROM ALUNOS LEFT JOIN NOTASALUNO ON ALUNOS.id = NOTASALUNO.aluno_id LEFT JOIN AVALIACOES ON AVALIACOES.turma_id = ALUNOS.turma_id AND ALUNOS.turma_id = " + turmaId, null);
                            if (cursor.getCount() > 0) {
                                while (cursor.moveToNext()) {
                                    int idIndice = cursor.getColumnIndex("id");
                                    int codigoMatriculaIndice = cursor.getColumnIndex("codigoMatricula");
                                    JSONObject notaJson = new JSONObject("{\"CodigoMatriculaAluno\":" + cursor.getString(codigoMatriculaIndice) + ",\"Nota\":\"11.0\"}");
                                    notasAlunoTO.setJSON(notaJson, cursor.getInt(idIndice), usuarioId, avaliacaoId);
                                    statementInsertNotas.bindString(1, notasAlunoTO.getCodigoUnico());
                                    setDadosDataBase2(NotasAlunoTO.nomeTabela, notasAlunoTO, database, statementInsertNotas);
                                }
                            }
                            cursor.close();
                        }
                    }
                }
            }
        }
    }

    private void atualizarFaltas(int codigoDisciplina, int turmasFrequenciaId, JSONObject jsonObjectDisciplina, SQLiteDatabase database) throws JSONException {
        int i;
        int j;
        JSONArray faltasAlunosJson = jsonObjectDisciplina.getJSONArray("FaltasAlunos");
        int numeroFaltasAlunos = faltasAlunosJson.length();
        Cursor cursor = database.rawQuery("SELECT id FROM DISCIPLINA WHERE codigoDisciplina = " + codigoDisciplina + " AND turmasFrequencia_id = " + turmasFrequenciaId , null);
        int disciplina_id = 0;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            disciplina_id = cursor.getInt(0);
        }
        cursor.close();
        TotalFaltasAlunosTO totalFaltasAlunosTO = new TotalFaltasAlunosTO();
        for(i = 0; i < numeroFaltasAlunos; i++) {
            JSONObject faltaAlunoJson = faltasAlunosJson.getJSONObject(i);
            String faltaAlunoString = faltaAlunoJson.toString();
            char[] caracteres = faltaAlunoString.toCharArray();
            int numeroCaracteres = caracteres.length;
            StringBuilder matriculaStringBuilder = new StringBuilder();
            for(j = faltaAlunoString.indexOf("\"CodigoMatricula\":") + 18; j < numeroCaracteres; j++) {
                char c = caracteres[j];
                if (c != ',' && c != '}') {
                    matriculaStringBuilder.append(c);
                }
                else {
                    break;
                }
            }
            int alunoId = -1;
            cursor = database.rawQuery("SELECT id FROM ALUNOS WHERE codigoMatricula = " + matriculaStringBuilder.toString(), null);
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                alunoId = cursor.getInt(cursor.getColumnIndex("id"));
            }
            cursor.close();
            if (alunoId != -1) {
                totalFaltasAlunosTO.setJSON(faltaAlunoJson, disciplina_id, alunoId);
                statementTotalFaltasAlunos.bindString(1, totalFaltasAlunosTO.getCodigoUnico());
                setDadosDataBase2(TotalFaltasAlunosTO.nomeTabela, totalFaltasAlunosTO, database, statementTotalFaltasAlunos);
            }
        }
    }

    private void convertJSONArrayForTurmasCalendario(int anoLetivo, int bimestreId, JSONArray calendarioJsonArray, SQLiteDatabase database) throws JSONException {
        int i;
        int j;
        int numeroElementosCalendario = calendarioJsonArray.length();
        Calendar calendar = Calendar.getInstance();
        DiasLetivosTO diasLetivosTO = new DiasLetivosTO();
        for(i = 0; i < numeroElementosCalendario; i++) {
            JSONObject calendarioJson = calendarioJsonArray.getJSONObject(i);
            int mes = calendarioJson.getInt("Mes");
            JSONArray diasLetivosJson = calendarioJson.getJSONArray("DiasLetivos");
            int numeroDiasLetivos = diasLetivosJson.length();
            for(j = 0; j < numeroDiasLetivos; j++) {
                int dia = diasLetivosJson.getInt(j);
                calendar.set(anoLetivo, mes - 1, dia);
                int diaSemana = calendar.get(Calendar.DAY_OF_WEEK);
                int semanaMes = calendar.get(Calendar.WEEK_OF_MONTH);
                int mesCalendario = calendar.get(Calendar.MONTH);
                diasLetivosTO.setJSON(bimestreId, dia, mes, anoLetivo, diaSemana, semanaMes, mesCalendario);
                setDadosDataBase2(DiasLetivosTO.nomeTabela, diasLetivosTO, database, statementDiasLetivos);
            }
        }
    }
}