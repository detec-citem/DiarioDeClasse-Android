package br.gov.sp.educacao.sed.mobile.Avaliacao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.gov.sp.educacao.sed.mobile.Escola.Aula;
import br.gov.sp.educacao.sed.mobile.Escola.Bimestre;
import br.gov.sp.educacao.sed.mobile.Escola.DiasLetivos;
import br.gov.sp.educacao.sed.mobile.Escola.Disciplina;
import br.gov.sp.educacao.sed.mobile.Fechamento.FechamentoData;
import br.gov.sp.educacao.sed.mobile.Login.UsuarioTO;
import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmasFrequencia;
import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;

public class AvaliacaoDBgetters {

    private Banco banco;

    private Cursor cursor;

    private final String TAG = AvaliacaoDBgetters.class.getSimpleName();

    public AvaliacaoDBgetters(Banco banco) {

        this.banco = banco;
    }
    ///OK///Sem uso?
    boolean getFechamento() {

        cursor = banco.get().rawQuery(

                "SELECT id FROM TIPO_FECHAMENTO WHERE date('now') >= inicio AND date('now') <= fim;", null
        );

        if(cursor.moveToFirst()) {

            return true;
        }
        else {

            return false;
        }
    }
    ///Sem uso?
    int getFechamentoAtual() {

        int bimestre = 0;

        Cursor cursor = null;

        try {

            cursor = banco.get().rawQuery(

                    "SELECT nome FROM TIPO_FECHAMENTO WHERE date('now') >= inicio AND date('now') <= fim;", null
            );

            if (cursor.moveToFirst()) {

                switch (cursor.getString(0)) {

                    case "Conselho Primeiro Bimestre":

                        bimestre = 1;
                        break;

                    case "Conselho Segundo Bimestre":

                        bimestre = 2;
                        break;

                    case "Conselho Terceiro Bimestre":

                        bimestre = 3;
                        break;

                    case "Conselho Quarto Bimestre":

                        bimestre = 4;
                        break;
                }
            }
        }
        catch (Exception e) {

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
    int getUltimoIdAvaliacao() {

        int ultimoIdAvaliacao = 1;

        //Cursor cursor = null;

        String queryUltimoAvaliacaoId =

                "SELECT id FROM AVALIACOES ORDER BY id DESC LIMIT 1;";

        SQLiteStatement statementUltimoAvaliacaoId = banco.get().compileStatement(queryUltimoAvaliacaoId);

        try {

            ultimoIdAvaliacao = (int) statementUltimoAvaliacaoId.simpleQueryForLong() + 1;

            /*cursor = banco.get().rawQuery("SELECT id FROM AVALIACOES ORDER BY id DESC LIMIT 1", null);

            if(cursor.moveToNext()) {

                ultimoIdAvaliacao = cursor.getInt(cursor.getColumnIndex("id")) + 1;
            }*/
        }
        catch(Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            statementUltimoAvaliacaoId.close();

            /*if(cursor != null) {

                cursor.close();
            }*/
        }
        return ultimoIdAvaliacao;
    }
    ///OK
    UsuarioTO getUsuarioAtivo() {

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

        banco.get().beginTransaction();

        cursor = banco.get().rawQuery(query.toString(), null);

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
            banco.get().setTransactionSuccessful();

            banco.get().endTransaction();
        }
        return null;
    }
    ///OK
    FechamentoData getFechamentoAberto() {

        //Cursor cursor = null;

        FechamentoData fechamentoData = new FechamentoData();

        banco.get().beginTransaction();

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
        catch (Exception e) {

            CrashAnalytics.e(TAG, e);

            fechamentoData = null;
        }
        finally {

            if (cursor != null) {

                cursor.close();
            }

            banco.get().setTransactionSuccessful();

            banco.get().endTransaction();
        }
        return fechamentoData;
    }
    ///OK
    String getNotasAluno(NotasAluno notasAluno) {

        //Cursor cursor = null;

        String retorno = null;

        try {

            cursor = banco.get().rawQuery(

                    "SELECT * FROM NOTASALUNO WHERE avaliacao_id = " + notasAluno.getAvaliacao_id() +
                            " AND aluno_id=" + notasAluno.getAluno_id(), null
            );

            cursor.moveToNext();

            if(cursor.getCount() == 0) {

                retorno = "11.00";
            }
            else {

                retorno = cursor.getString(cursor.getColumnIndex("nota"));
            }
        }
        catch(Exception e){

            CrashAnalytics.e(TAG, e);
        }
        finally{

            if (cursor != null){

                cursor.close();
            }
        }
        return retorno;
    }
    ///OK
    int getBimestreAtual(int turmaFrequencia_id) {

        int bimestre = 1;

        //Cursor cursor = null;

        String queryBimestreAtual =

                "SELECT numero FROM BIMESTRE WHERE turmasFrequencia_id = ? AND bimestreAtual = 1;";

        SQLiteStatement statementBimestreAtual = banco.get().compileStatement(queryBimestreAtual);

        banco.get().beginTransaction();

        try {

            statementBimestreAtual.bindLong(1, turmaFrequencia_id);

            bimestre = (int) statementBimestreAtual.simpleQueryForLong();

            /*cursor = banco.get().rawQuery(

                    "SELECT numero FROM BIMESTRE WHERE turmasFrequencia_id = "
                            + turmaFrequencia_id + " AND bimestreAtual = 1", null
            );

            if (cursor.moveToFirst()) {

                bimestre = cursor.getInt(0);
            }*/
        }
        catch (Exception e) {

            e.printStackTrace();
        }
        finally {

            statementBimestreAtual.clearBindings();

            statementBimestreAtual.close();

            banco.get().setTransactionSuccessful();

            banco.get().endTransaction();

            /*if(cursor != null) {

                cursor.close();
            }*/
        }
        return bimestre;
    }
    ///OK
    ArrayList<Aula> getAula(Disciplina disciplina) {

        ArrayList<Aula> listaAula = new ArrayList<>();

        //Cursor cursor = null;

        try {

            cursor = banco.get().rawQuery(

                    "SELECT * FROM AULAS WHERE disciplina_id = " + disciplina.getId(), null
            );

            while(cursor.moveToNext()) {

                for(int i = 1; i <= 5; i++) {

                    final Aula aula = new Aula();

                    aula.setInicio(cursor.getString(cursor.getColumnIndex("inicioHora")));
                    aula.setFim(cursor.getString(cursor.getColumnIndex("fimHora")));

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
        }
        return listaAula;
    }
    ///OK
    public List<Integer> getAvaliacoesParaDeletar() {

        List<Integer> codigosAvaliacoesParaDeletar = new ArrayList<>(0);

        //Cursor cursor = null;

        banco.get().beginTransaction();

        try {

            cursor = banco.get().rawQuery(

                    "SELECT codigoAvaliacao FROM AVALIACOES WHERE dataServidor = 'deletar';", null
            );

            if(cursor.getCount() > 0) {

                int codigoAvaliacaoColumnIndex = cursor.getColumnIndex("codigoAvaliacao");

                while(cursor.moveToNext()) {

                    codigosAvaliacoesParaDeletar.add(cursor.getInt(codigoAvaliacaoColumnIndex));
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
        return codigosAvaliacoesParaDeletar;
    }
    ///Sem uso?
    int getTotalNotasAlunosAtivos(Avaliacao avaliacao) {

        Cursor cursor = null;

        int count = 0;

        try {

            cursor = banco.get().rawQuery(

                    "SELECT id FROM NOTASALUNO WHERE avaliacao_id = " + avaliacao.getId() + " AND nota != '11.00';", null
            );

            count = cursor.getCount();
        }
        catch(Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            if(cursor != null) {

                cursor.close();
            }
        }
        return count;
    }
    ///OK
    Bimestre getBimestreAnterior(int turmasFrequencia_id) {

        //Cursor cursor = null;

        cursor = banco.get().rawQuery(

                "SELECT numero, inicioBimestre, fimBimestre, turmasFrequencia_id FROM BIMESTRE " +
                        "WHERE numero = ((SELECT numero FROM BIMESTRE WHERE turmasFrequencia_id = ? " +
                        "AND bimestreAtual = 1) AND turmasFrequencia_id = ?);",
                new String [] {String.valueOf(turmasFrequencia_id),
                        String.valueOf(turmasFrequencia_id)}
        );

        cursor.moveToFirst();

        Bimestre bimestre = new Bimestre();

        bimestre.setNumero(cursor.getInt(0));
        bimestre.setInicio(cursor.getString(1));
        bimestre.setFim(cursor.getString(2));
        bimestre.setId(cursor.getInt(3));

        if(cursor != null) {

            cursor.close();
        }

        return bimestre;
    }
    ///OK
    public List<JSONObject> getAvaliacoesNaoSincronizadas() {

        List<JSONObject> avaliacoes = new ArrayList<>();

        Cursor cursor = null;

        banco.get().beginTransaction();

        try {

            cursor = banco.get().rawQuery(

                    "SELECT * FROM AVALIACOES WHERE dataServidor IS NULL", null
            );

            if (cursor.getCount() > 0) {

                int idColumnIndex = cursor.getColumnIndex("id");
                int nomeColumnIndex = cursor.getColumnIndex("nome");
                int dataColumnIndex = cursor.getColumnIndex("data");
                int turmaIdColumnIndex = cursor.getColumnIndex("turma_id");
                int bimestreColumnIndex = cursor.getColumnIndex("bimestre");
                int mobileIdColumnIndex = cursor.getColumnIndex("mobileId");
                int codigoTurmaColumnIndex = cursor.getColumnIndex("codigoTurma");
                int discipinaIdColumnIndex = cursor.getColumnIndex("disciplina_id");
                int dataCadastroColumnIndex = cursor.getColumnIndex("dataCadastro");
                int codigoAvaliacaoColumnIndex = cursor.getColumnIndex("codigoAvaliacao");
                int codigoDisciplinaColumnIndex = cursor.getColumnIndex("codigoDisciplina");
                int codigoTipoAtividadeColumnIndex = cursor.getColumnIndex("codigoTipoAtividade");

                while(cursor.moveToNext()) {

                    Avaliacao avaliacaoObj = new Avaliacao();

                    avaliacaoObj.setId(cursor.getInt(idColumnIndex));
                    avaliacaoObj.setNome(cursor.getString(nomeColumnIndex));
                    avaliacaoObj.setData(cursor.getString(dataColumnIndex));
                    avaliacaoObj.setTurmaId(cursor.getInt(turmaIdColumnIndex));
                    avaliacaoObj.setBimestre(cursor.getInt(bimestreColumnIndex));
                    avaliacaoObj.setMobileId(cursor.getInt(mobileIdColumnIndex));
                    avaliacaoObj.setCodTurma(cursor.getInt(codigoTurmaColumnIndex));
                    avaliacaoObj.setCodigo(cursor.getInt(codigoAvaliacaoColumnIndex));
                    avaliacaoObj.setDisciplinaId(cursor.getInt(discipinaIdColumnIndex));
                    avaliacaoObj.setDataCadastro(cursor.getString(dataCadastroColumnIndex));
                    avaliacaoObj.setCodDisciplina(cursor.getInt(codigoDisciplinaColumnIndex));
                    avaliacaoObj.setTipoAtividade(cursor.getInt(codigoTipoAtividadeColumnIndex));
                    avaliacaoObj.setValeNota(cursor.getInt(cursor.getColumnIndex("valeNota")) == 1);

                    avaliacoes.add(montarJSONEnvio(avaliacaoObj));
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
        return avaliacoes;
    }
    ///OK
    Bimestre getBimestre(TurmasFrequencia turmasFrequencia) {

        Bimestre bimestre = new Bimestre();

        //Cursor cursor = null;

        try {

            cursor = banco.get().rawQuery(

                    "SELECT * FROM BIMESTRE WHERE turmasFrequencia_id = "
                            + turmasFrequencia.getId() + " AND bimestreAtual = 1;", null
            );

            if(cursor.moveToNext()) {

                bimestre.setId(cursor.getInt(cursor.getColumnIndex("id")));
                bimestre.setNumero(cursor.getInt(cursor.getColumnIndex("numero")));
                bimestre.setFim(cursor.getString(cursor.getColumnIndex("fimBimestre")));
                bimestre.setInicio(cursor.getString(cursor.getColumnIndex("inicioBimestre")));
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
    ///Sem uso?
    List<String> getAlunosAtivosPorTurmaId(Integer turma_id) {

        List<String> codigosMatriculaAtivo = new ArrayList<>();

        Cursor cursor = null;

        try {

            cursor = banco.get().rawQuery(

                    "SELECT codigoMatricula FROM ALUNOS WHERE turma_id = " + turma_id + " AND alunoAtivo = '1' ", null
            );

            if(cursor.getCount() > 0) {

                int codigoMatricula = cursor.getColumnIndex("codigoMatricula");

                while(cursor.moveToNext()) {

                    codigosMatriculaAtivo.add(cursor.getString(codigoMatricula));
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
        }
        return codigosMatriculaAtivo;
    }
    ///OK ///
    public List<Integer> getTurmasFrequenciaIds(int codigoTurma) {

        //Cursor cursor = null;

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
    ///OK
    void getAlunosNotas(int avaliacaoId, List<Aluno> listaAlunos) {

        NotasAluno notasAluno = new NotasAluno();

        notasAluno.setAvaliacao_id(avaliacaoId);

        banco.get().beginTransaction();

        try {

            for(int i = 0; i < listaAlunos.size(); i++) {

                notasAluno.setAluno_id(listaAlunos.get(i).getId());

                listaAlunos.get(i).setNota(getNotasAluno(notasAluno));
            }
        }
        catch (Exception e) {

            Log.e(TAG, "Erro ao resgatar notas");
        }
        finally {

            banco.get().setTransactionSuccessful();

            banco.get().endTransaction();
        }
    }
    ///OK
    private JSONObject montarJSONEnvio(Avaliacao avaliacao) throws JSONException {

        JSONObject avaliacaoJson = new JSONObject();

        JSONArray notasJson = null;


        try {

            avaliacaoJson.put("Id", avaliacao.getId());
            avaliacaoJson.put("Nome", avaliacao.getNome());
            avaliacaoJson.put("Data", avaliacao.getData());
            avaliacaoJson.put("Codigo", avaliacao.getCodigo());
            avaliacaoJson.put("ValeNota", avaliacao.isValeNota());
            avaliacaoJson.put("Bimestre", avaliacao.getBimestre());
            avaliacaoJson.put("MobileId", avaliacao.getMobileId());
            avaliacaoJson.put("CodigoTurma", avaliacao.getCodTurma());
            avaliacaoJson.put("CodigoDisciplina", avaliacao.getCodDisciplina());
            avaliacaoJson.put("CodigoTipoAtividade", avaliacao.getTipoAtividade());

            notasJson = new JSONArray();

            banco.get().beginTransaction();

            cursor = banco.get().rawQuery(

                    "SELECT NOTASALUNO.codigoMatricula, NOTASALUNO.nota FROM ALUNOS, NOTASALUNO " +
                            "WHERE NOTASALUNO.aluno_id = ALUNOS.id " +
                            "AND ALUNOS.turma_id = " + avaliacao.getTurmaId() +
                            " AND NOTASALUNO.avaliacao_id = " + avaliacao.getId(),
                    null
            );

            int notaColumnIndex = cursor.getColumnIndex("nota");

            int codigoMatriculaColumnIndex = cursor.getColumnIndex("codigoMatricula");

            StringBuilder notaJsonStringBuilder = new StringBuilder();

            while(cursor.moveToNext()) {

                String nota = cursor.getString(notaColumnIndex);

                if(!nota.equals("11.00")) {

                    notaJsonStringBuilder.setLength(0);
                    notaJsonStringBuilder.append("{\"CodigoMatriculaAluno\":")
                            .append(cursor.getString(codigoMatriculaColumnIndex))
                            .append(",\"Nota\":\"")
                            .append(nota)
                            .append("\"}");

                    notasJson.put(new JSONObject(notaJsonStringBuilder.toString()));
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

        avaliacaoJson.put("Notas", notasJson);

        return avaliacaoJson;
    }
    ///OK
    List<Avaliacao> getAvaliacoes(int turmaId, int codigoDisciplina, int bimestre) {

        List<Avaliacao> avaliacoes = new ArrayList<>();

        //Cursor cursor = null;

        String disciplinas = String.valueOf(codigoDisciplina);

        if(codigoDisciplina == 1000) {

            disciplinas = "1100 OR codigoDisciplina = 2700 OR codigoDisciplina = 7245";
        }

        try {

            cursor = banco.get().rawQuery(

                    "SELECT * FROM AVALIACOES WHERE bimestre = " + bimestre +
                            " AND turma_id = " + turmaId +
                            " AND dataServidor IS NOT 'deletar' " +
                            " AND (codigoDisciplina = " + disciplinas + ")",
                    null
            );

            if(cursor.getCount() > 0) {

                while(cursor.moveToNext()) {

                    Avaliacao avaliacaoObj = new Avaliacao();

                    avaliacaoObj.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    avaliacaoObj.setNome(cursor.getString(cursor.getColumnIndex("nome")));
                    avaliacaoObj.setData(cursor.getString(cursor.getColumnIndex("data")));
                    avaliacaoObj.setTurmaId(cursor.getInt(cursor.getColumnIndex("turma_id")));
                    avaliacaoObj.setBimestre(cursor.getInt(cursor.getColumnIndex("bimestre")));
                    avaliacaoObj.setMobileId(cursor.getInt(cursor.getColumnIndex("mobileId")));
                    avaliacaoObj.setCodTurma(cursor.getInt(cursor.getColumnIndex("codigoTurma")));
                    avaliacaoObj.setCodigo(cursor.getInt(cursor.getColumnIndex("codigoAvaliacao")));
                    avaliacaoObj.setValeNota(cursor.getInt(cursor.getColumnIndex("valeNota")) == 1);
                    avaliacaoObj.setDisciplinaId(cursor.getInt(cursor.getColumnIndex("disciplina_id")));
                    avaliacaoObj.setCodDisciplina(cursor.getInt(cursor.getColumnIndex("codigoDisciplina")));
                    avaliacaoObj.setTipoAtividade(cursor.getInt(cursor.getColumnIndex("codigoTipoAtividade")));

                    avaliacoes.add(avaliacaoObj);
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
        }
        return avaliacoes;
    }
    ///OK ///
    public ArrayList<Avaliacao> getListaTotalAvaliacoes(int turma, String disciplina) {

        ArrayList<Avaliacao> arrayListAvaliacao = new ArrayList<>();

        //Cursor cursor = null;

        try {

            cursor = banco.get().rawQuery(

                    "SELECT * FROM AVALIACOES WHERE codigoTurma = " + turma + " AND dataServidor IS NOT NULL" +
                    " AND (codigoDisciplina = " +
                    (!disciplina.equals("1000") ? (disciplina + ")") : ("2700 OR codigoDisciplina = 1100 OR codigoDisciplina = 7245)")), null
            );

            while(cursor.moveToNext()) {

                final Avaliacao avaliacao = new Avaliacao();

                avaliacao.setId(cursor.getInt(cursor.getColumnIndex("id")));
                avaliacao.setNome(cursor.getString(cursor.getColumnIndex("nome")));
                avaliacao.setData(cursor.getString(cursor.getColumnIndex("data")));
                avaliacao.setTurmaId(cursor.getInt(cursor.getColumnIndex("turma_id")));
                avaliacao.setBimestre(cursor.getInt(cursor.getColumnIndex("bimestre")));
                avaliacao.setMobileId(cursor.getInt(cursor.getColumnIndex("mobileId")));
                avaliacao.setCodTurma(cursor.getInt(cursor.getColumnIndex("codigoTurma")));
                avaliacao.setCodigo(cursor.getInt(cursor.getColumnIndex("codigoAvaliacao")));
                avaliacao.setValeNota(cursor.getInt(cursor.getColumnIndex("valeNota")) == 1);
                avaliacao.setDisciplinaId(cursor.getInt(cursor.getColumnIndex("disciplina_id")));
                avaliacao.setCodDisciplina(cursor.getInt(cursor.getColumnIndex("codigoDisciplina")));
                avaliacao.setTipoAtividade(cursor.getInt(cursor.getColumnIndex("codigoTipoAtividade")));

                arrayListAvaliacao.add(avaliacao);
            }
        }
        catch(Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally{

            if(cursor != null) {

                cursor.close();
            }
        }
        return arrayListAvaliacao;
    }
    ///OK
    ArrayList<DiasLetivos> getDiasLetivos(Bimestre bimestre, List<Integer> listaDiaSemana) {

        StringBuffer query = new StringBuffer();

        ArrayList<DiasLetivos> listaDiasLetivos = new ArrayList<>();

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
                        " AND turmasFrequencia_id = " + turma_id + ";", null
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

        //Cursor cursor = null;

        try {

            cursor = banco.get().rawQuery(query.toString(), null);

            while(cursor.moveToNext()) {

                String input_date = cursor.getString(cursor.getColumnIndex("dataAula"));

                SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");

                Date data = format1.parse(input_date);

                Calendar c = Calendar.getInstance();

                c.setTime(data);

                int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;

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
        }
        return listaDiasLetivos;
    }
    ///Sem uso?
    List<Avaliacao> getAvaliacoesValeNota(TurmaGrupo turmaGrupo, int disciplina, int bimestre) {

        List<Avaliacao> avaliacoes = new ArrayList<>();

        Cursor cursor = null;

        try {

            cursor = banco.get().rawQuery(

                    "SELECT * FROM AVALIACOES WHERE bimestre = " + bimestre +
                            " AND turma_id = " + turmaGrupo.getTurma().getId() +
                            " AND disciplina_id = " + turmaGrupo.getDisciplina().getId() +
                            " AND valeNota = 1" +
                            " AND dataServidor IS NOT 'deletar'",
                    null
            );

            if(cursor.getCount() > 0) {

                while(cursor.moveToNext()) {

                    Avaliacao avaliacaoObj = new Avaliacao();

                    avaliacaoObj.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    avaliacaoObj.setNome(cursor.getString(cursor.getColumnIndex("nome")));
                    avaliacaoObj.setData(cursor.getString(cursor.getColumnIndex("data")));
                    avaliacaoObj.setTurmaId(cursor.getInt(cursor.getColumnIndex("turma_id")));
                    avaliacaoObj.setBimestre(cursor.getInt(cursor.getColumnIndex("bimestre")));
                    avaliacaoObj.setMobileId(cursor.getInt(cursor.getColumnIndex("mobileId")));
                    avaliacaoObj.setCodTurma(cursor.getInt(cursor.getColumnIndex("codigoTurma")));
                    avaliacaoObj.setCodigo(cursor.getInt(cursor.getColumnIndex("codigoAvaliacao")));
                    avaliacaoObj.setValeNota(cursor.getInt(cursor.getColumnIndex("valeNota")) == 1);
                    avaliacaoObj.setDisciplinaId(cursor.getInt(cursor.getColumnIndex("disciplina_id")));
                    avaliacaoObj.setCodDisciplina(cursor.getInt(cursor.getColumnIndex("codigoDisciplina")));
                    avaliacaoObj.setTipoAtividade(cursor.getInt(cursor.getColumnIndex("codigoTipoAtividade")));

                    avaliacoes.add(avaliacaoObj);
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
        }
        return avaliacoes;
    }
    ///OK
    List<Avaliacao> getAvaliacoesByFiltro(TurmaGrupo turmaGrupo, int tipoAtividade, int bimestre) {

        List<Avaliacao> avaliacoes = new ArrayList<>();

        //Cursor cursor = null;

        banco.get().beginTransaction();

        try {

            if(tipoAtividade == 0) {

                cursor = banco.get().rawQuery(

                        "SELECT * FROM AVALIACOES WHERE turma_id = " + turmaGrupo.getTurma().getId() +
                                " AND disciplina_id = " + turmaGrupo.getDisciplina().getId() +
                                " AND bimestre = " + bimestre +
                                " AND dataServidor IS NOT 'deletar' " +
                                " AND (codigoTipoAtividade = " + 11 +
                                " OR  codigoTipoAtividade = " + 12 +
                                " OR  codigoTipoAtividade = " + 13 +
                                " OR  codigoTipoAtividade = " + 14 + ")",
                        null
                );
            }
            else {

                cursor = banco.get().rawQuery(

                        "SELECT * FROM AVALIACOES WHERE turma_id = " + turmaGrupo.getTurma().getId() +
                                " AND disciplina_id = " + turmaGrupo.getDisciplina().getId() +
                                " AND codigoTipoAtividade = " + tipoAtividade +
                                " AND bimestre = " + bimestre +
                                " AND dataServidor IS NOT 'deletar' ",
                        null
                );
            }

            if(cursor.getCount() > 0) {

                while(cursor.moveToNext()) {

                    Avaliacao avaliacaoObj = new Avaliacao();

                    avaliacaoObj.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    avaliacaoObj.setNome(cursor.getString(cursor.getColumnIndex("nome")));
                    avaliacaoObj.setData(cursor.getString(cursor.getColumnIndex("data")));
                    avaliacaoObj.setTurmaId(cursor.getInt(cursor.getColumnIndex("turma_id")));
                    avaliacaoObj.setBimestre(cursor.getInt(cursor.getColumnIndex("bimestre")));
                    avaliacaoObj.setMobileId(cursor.getInt(cursor.getColumnIndex("mobileId")));
                    avaliacaoObj.setCodTurma(cursor.getInt(cursor.getColumnIndex("codigoTurma")));
                    avaliacaoObj.setCodigo(cursor.getInt(cursor.getColumnIndex("codigoAvaliacao")));
                    avaliacaoObj.setValeNota(cursor.getInt(cursor.getColumnIndex("valeNota")) == 1);
                    avaliacaoObj.setDisciplinaId(cursor.getInt(cursor.getColumnIndex("disciplina_id")));
                    avaliacaoObj.setDataServidor(cursor.getString(cursor.getColumnIndex("dataServidor")));
                    avaliacaoObj.setCodDisciplina(cursor.getInt(cursor.getColumnIndex("codigoDisciplina")));
                    avaliacaoObj.setTipoAtividade(cursor.getInt(cursor.getColumnIndex("codigoTipoAtividade")));

                    avaliacoes.add(avaliacaoObj);
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
        return avaliacoes;
    }
    ///OK
    List<Avaliacao> getAvaliacoesByFiltroAnosIniciais(TurmaGrupo turmaGrupo, int tipoAtividade, int bimestre, int disciplina) {

        List<Avaliacao> avaliacoes = new ArrayList<>();

        //Cursor cursor = null;

        banco.get().beginTransaction();

        try {

            if(tipoAtividade == 0) {

                cursor = banco.get().rawQuery(

                        "SELECT * FROM AVALIACOES WHERE turma_id = " + turmaGrupo.getTurma().getId() +
                                " AND codigoDisciplina = " + disciplina +
                                " AND bimestre = " + bimestre +
                                " AND dataServidor IS NOT 'deletar' " +
                                " AND (codigoTipoAtividade = " + 11 +
                                " OR  codigoTipoAtividade = " + 12 +
                                " OR  codigoTipoAtividade = " + 13 +
                                " OR  codigoTipoAtividade = " + 14 + ")",
                                null
                );
            }
            else {

                cursor = banco.get().rawQuery(

                        "SELECT * FROM AVALIACOES WHERE turma_id = " + turmaGrupo.getTurma().getId() +
                                " AND codigoDisciplina = " + disciplina +
                                " AND codigoTipoAtividade = " + tipoAtividade +
                                " AND bimestre = " + bimestre +
                                " AND dataServidor IS NOT 'deletar' ",
                                null
                );
            }

            if(cursor.getCount() > 0) {

                while(cursor.moveToNext()) {

                    Avaliacao avaliacaoObj = new Avaliacao();

                    avaliacaoObj.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    avaliacaoObj.setNome(cursor.getString(cursor.getColumnIndex("nome")));
                    avaliacaoObj.setData(cursor.getString(cursor.getColumnIndex("data")));
                    avaliacaoObj.setTurmaId(cursor.getInt(cursor.getColumnIndex("turma_id")));
                    avaliacaoObj.setBimestre(cursor.getInt(cursor.getColumnIndex("bimestre")));
                    avaliacaoObj.setMobileId(cursor.getInt(cursor.getColumnIndex("mobileId")));
                    avaliacaoObj.setCodTurma(cursor.getInt(cursor.getColumnIndex("codigoTurma")));
                    avaliacaoObj.setCodigo(cursor.getInt(cursor.getColumnIndex("codigoAvaliacao")));
                    avaliacaoObj.setValeNota(cursor.getInt(cursor.getColumnIndex("valeNota")) == 1);
                    avaliacaoObj.setDataServidor(cursor.getString(cursor.getColumnIndex("dataServidor")));
                    avaliacaoObj.setCodDisciplina(cursor.getInt(cursor.getColumnIndex("codigoDisciplina")));
                    avaliacaoObj.setTipoAtividade(cursor.getInt(cursor.getColumnIndex("codigoTipoAtividade")));

                    avaliacoes.add(avaliacaoObj);
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
        return avaliacoes;
    }
}
