package br.gov.sp.educacao.sed.mobile.Fechamento;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.ArrayList;

import android.database.Cursor;

import br.gov.sp.educacao.sed.mobile.Login.UsuarioTO;

import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;

import br.gov.sp.educacao.sed.mobile.Avaliacao.Avaliacao;

import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;

class FechamentoDBgetters {

    private Banco banco;

    private final String TAG = FechamentoDBgetters.class.getSimpleName();

    FechamentoDBgetters(Banco banco) {

        this.banco = banco;
    }

    int getFechamentoAtual() {

        int bimestre = 0;

        Cursor cursor = null;

        try {

            cursor = banco.get().rawQuery(

                    "SELECT nome FROM TIPO_FECHAMENTO WHERE date('now') >= inicio " +
                            "AND date('now') <= fim;",
                    null
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

        Cursor cursor = banco.get().rawQuery(query.toString(), null);

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

            cursor.close();
        }
        return null;
    }

    int getTipoFechamentoAtual() {

        Cursor cursor = null;

        try {

            cursor = banco.get().rawQuery(

                    "SELECT codigoTipoFechamento FROM TIPO_FECHAMENTO WHERE inicio <= date('now') " +
                            "AND fim >= date('now')",
                    null
            );

            if(cursor != null && cursor.moveToNext()) {

                return cursor.getInt(cursor.getColumnIndex("codigoTipoFechamento"));
            }
        }
        catch (Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            if (cursor != null) {

                cursor.close();
            }
        }
        return 0;
    }

    boolean isAnosIniciais(int codigoTurma) {

        Cursor cursor = null;

        int disciplina = 0;

        boolean anosIniciais = false;

        try {

            cursor = banco.get().rawQuery(

                    "SELECT c.codigoDisciplina FROM TURMASFREQUENCIA a INNER JOIN TURMAS b " +
                            "ON a.turma_id = b.id " +
                            "INNER JOIN DISCIPLINA c " +
                            "ON c.turmasFrequencia_id = a.id " +
                            "AND a.codigoTurma = " + codigoTurma,
                    null
            );

            cursor.moveToFirst();

            disciplina = cursor.getInt(0);

            if(disciplina == 1000) {

                anosIniciais = true;
            }
        }
        catch (Exception e) {

            return anosIniciais;
        }
        finally {

            if(cursor != null) {

                cursor.close();
            }
        }
        return anosIniciais;
    }

    private Cursor getSelect(StringBuffer query) {

        Cursor cursor = null;

        cursor = banco.get().rawQuery(query.toString(), null);

        if(cursor != null) {

            cursor.moveToFirst();
        }

        return cursor;
    }

    int getBimestreAtual(int turmaFrequencia_id) {

        int bimestre = 1;

        Cursor cursor = null;

        try {

            cursor = banco.get().rawQuery(

                    "SELECT numero FROM BIMESTRE WHERE turmasFrequencia_id = " + turmaFrequencia_id +
                            " AND bimestreAtual = 1",
                    null
            );

            if (cursor.moveToFirst() && cursor != null) {

                bimestre = cursor.getInt(0);
            }
        }
        catch (Exception e) {

            e.printStackTrace();
        }
        finally {

            if(cursor != null) {

                cursor.close();
            }
        }
        return bimestre;
    }

    String getNotaAluno(int avaliacaoId, int alunoId) {

        Cursor cursor = null;

        String retorno = null;

        try {

            cursor = banco.get().rawQuery(

                    "SELECT * FROM NOTASALUNO WHERE avaliacao_id= " + avaliacaoId +
                            " AND aluno_id=" + alunoId,
                    null
            );

            cursor.moveToNext();

            if(cursor.getCount() == 0) {

                retorno = "12";
            }
            else {

                retorno = cursor.getString(cursor.getColumnIndex("nota"));
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
        return retorno;
    }

    Aluno getTotalFaltas(Aluno aluno, int idDisciplina) {

        Cursor cursor = null;

        try {

            cursor = banco.get().rawQuery(

                    "SELECT faltasAnuais, faltasBimestre, faltasSequenciais, faltasBimestreAnterior " +
                            "FROM TOTALFALTASALUNOS " +
                            "WHERE aluno_id = " + aluno.getId() +
                            " AND disciplina_id = " + idDisciplina,
                    null
            );

            if (cursor.getCount() > 0) {

                cursor.moveToNext();

                aluno.setFaltasAnuais(cursor.getInt(cursor.getColumnIndex("faltasAnuais")));

                aluno.setFaltasBimestre(cursor.getInt(cursor.getColumnIndex("faltasBimestre")));

                aluno.setFaltasSequenciais(cursor.getInt(cursor.getColumnIndex("faltasSequenciais")));

                aluno.setFaltasBimestreAnterior(cursor.getInt(cursor.getColumnIndex("faltasBimestreAnterior")));
            }
        }
        catch (Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            if (cursor != null) {

                cursor.close();
            }
        }
        return aluno;
    }

    boolean verificarFaltasNaoSincronizadas(List<Aluno> alunos) {

        Cursor cursor = null;

        boolean faltasNaoSincronizadas = false;

        try {

            for(int i = 0; i < alunos.size(); i++) {

                cursor = banco.get().rawQuery(

                        "SELECT " + "dataServidor " + "FROM FALTASALUNOS " + "WHERE aluno_id = " + alunos.get(i).getId(), null
                );

                if(cursor.getCount() > 0) {

                    for(int j = 0; j < cursor.getCount(); j++) {

                        cursor.moveToNext();

                        String dataServidor = cursor.getString(cursor.getColumnIndex("dataServidor"));

                        if(dataServidor == null) {

                            faltasNaoSincronizadas = true;

                            break;
                        }
                    }
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
        return faltasNaoSincronizadas;
    }

    boolean isFechamentoTurmaAlterada(FechamentoTurma fechamentoTurma) {

        Cursor cursor = null;

        try {

            cursor = banco.get().rawQuery(

                    "SELECT id FROM FECHAMENTO_TURMA WHERE codigoTurma = " + fechamentoTurma.getCodigoTurma() +
                            " AND codigoDisciplina = " + fechamentoTurma.getCodigoDisciplina() +
                            " AND codigoTipoFechamento = " + fechamentoTurma.getCodigoTipoFechamento() +
                            " AND aulasRealizadas = " + fechamentoTurma.getAulasRealizadas() +
                            " AND aulasPlanejadas = " + fechamentoTurma.getAulasPlanejadas() +
                            " AND justificativa = '" + fechamentoTurma.getJustificativa() + "'",
                    null
            );

            if(cursor != null && cursor.getCount() > 0) {

                return false;
            }
        }
        catch (Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            if (cursor != null) {

                cursor.close();
            }
        }
        return true;
    }

    List<Avaliacao> getAvaliacoesValeNota(TurmaGrupo turmaGrupo, int bimestre, int codigoDisciplina) {

        List<Avaliacao> avaliacoes = new ArrayList<>();

        Cursor cursor = null;

        try {

            cursor = banco.get().rawQuery(

                    "SELECT * FROM AVALIACOES WHERE bimestre = " + bimestre +
                            " AND turma_id = " + turmaGrupo.getTurma().getId() +
                            " AND disciplina_id = " + turmaGrupo.getDisciplina().getId() +
                            " AND codigoDisciplina = " + codigoDisciplina +
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
                    avaliacaoObj.setValeNota(cursor.getInt(cursor.getColumnIndex("valeNota")) == 1);
                    avaliacaoObj.setCodigo(cursor.getInt(cursor.getColumnIndex("codigoAvaliacao")));
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

    MediaAluno getMediaAluno(int codigoTurma, String codigoMatricula, int codigoDisciplina) {

        Cursor cursor = null;

        MediaAluno media = null;

        try {

            cursor = banco.get().rawQuery(

                    "SELECT * FROM MEDIA_ALUNO WHERE codigoTurma = " + codigoTurma +
                            " AND codigoMatricula = " + codigoMatricula +
                            " AND codigoDisciplina = " + codigoDisciplina,
                            null
            );

            if (cursor.getCount() > 0 && cursor.moveToFirst()) {

                media = new MediaAluno();

                media.setBimestre(cursor.getInt(cursor.getColumnIndex("bimestre")));
                media.setNotaMedia(cursor.getInt(cursor.getColumnIndex("nota_media")));
                media.setCodigoTurma(cursor.getInt(cursor.getColumnIndex("codigoTurma")));
                media.setDisciplina(cursor.getInt(cursor.getColumnIndex("codigoDisciplina")));
                media.setCodigoMatricula(cursor.getString(cursor.getColumnIndex("codigoMatricula")));
            }
        }
        catch (Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            if (cursor != null) {

                cursor.close();
            }
        }
        return media;
    }

    FechamentoTurma getFechamentoTurma(int codigoTurma, int codigoDisciplina, int codigoTipoFechamento) {

        Cursor cursor = null;

        FechamentoTurma fechamento = null;

        try {

            cursor = banco.get().rawQuery(

                    "SELECT * FROM FECHAMENTO_TURMA WHERE codigoTurma = " + codigoTurma +
                            " AND codigoDisciplina = " + codigoDisciplina +
                            " AND codigoTipoFechamento = " + codigoTipoFechamento,
                            null
            );

            if (cursor.getCount() > 0) {

                if(cursor.moveToNext()) {

                    fechamento = new FechamentoTurma();

                    fechamento.setCodigoTurma(cursor.getInt(cursor.getColumnIndex("codigoTurma")));
                    fechamento.setJustificativa(cursor.getString(cursor.getColumnIndex("justificativa")));
                    fechamento.setAulasPlanejadas(cursor.getInt(cursor.getColumnIndex("aulasPlanejadas")));
                    fechamento.setAulasRealizadas(cursor.getInt(cursor.getColumnIndex("aulasRealizadas")));
                    fechamento.setCodigoDisciplina(cursor.getInt(cursor.getColumnIndex("codigoDisciplina")));
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
        return fechamento;
    }

    int getTotalFechamentoAlunoConfirmado(int codigoTurma, int codigoDisciplina, int codigoTipoFechamento) {

        Cursor cursor = null;

        try {

            cursor = banco.get().rawQuery(

                    "SELECT id FROM FECHAMENTO_ALUNO WHERE codigoTurma = " + codigoTurma +
                            " AND codigoDisciplina = " + codigoDisciplina +
                            " AND codigoTipoFechamento = " + codigoTipoFechamento +
                            " AND confirmado <> 0",
                    null
            );

            if(cursor != null && cursor.getCount() > 0) {

                return cursor.getCount();
            }
        }
        catch (Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            if (cursor != null) {

                cursor.close();
            }
        }
        return 0;
    }

    int getTotalFechamentosAlunosNaoEnviados(int codigoTurma, int codigoDisciplina, int codigoTipoFechamento) {

        Cursor cursor = null;

        try {

            cursor = banco.get().rawQuery(

                    "SELECT id FROM FECHAMENTO_ALUNO WHERE codigoTurma = " + codigoTurma +
                            " AND codigoDisciplina = " + codigoDisciplina +
                            " AND codigoTipoFechamento = " + codigoTipoFechamento +
                            " AND dataServidor = ''", null
            );

            if(cursor != null && cursor.getCount() > 0) {

                return cursor.getCount();
            }
        }
        catch (Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            if (cursor != null) {

                cursor.close();
            }
        }
        return 0;
    }

    JSONObject montarJSONEnvio(int codigoTurma, int disciplina, boolean anosIniciais, int tipoFechamentoAtual) {

        JSONObject jsonEnvio = new JSONObject();

        StringBuffer query = new StringBuffer();

        query.append("SELECT  ft.codigoTurma, " +
                "ft.codigoDisciplina, " +
                "ft.codigoTipoFechamento, " +
                "ft.aulasPlanejadas, " +
                "ft.aulasRealizadas, " +
                "ft.justificativa, " +
                "fa.codigoFechamento, " +
                "fa.nota, " +
                "fa.faltas, " +
                "fa.ausenciasCompensadas, " +
                "fa.faltasAcumuladas, " +
                "fa.codigoMatricula");

        query.append(" FROM FECHAMENTO_TURMA as ft, ");
        query.append("FECHAMENTO_ALUNO as fa, ");
        query.append("ALUNOS a");
        query.append(" WHERE ft.codigoTurma = ").append(codigoTurma);
        query.append(" AND ft.codigoTipoFechamento = ").append(tipoFechamentoAtual);
        query.append(" AND ft.codigoTurma = fa.codigoTurma");
        query.append(" AND ft.codigoTipoFechamento = fa.codigoTipoFechamento");
        query.append(" AND ft.codigoDisciplina = ").append(disciplina);
        query.append(" AND fa.codigoDisciplina = ").append(disciplina);
        query.append(" AND fa.codigoMatricula = a.codigoMatricula");

        Cursor cursor = getSelect(query);

        if (cursor != null) {

            JSONObject jsonObjFechamento;
            JSONArray jsonArrayFechamentos = new JSONArray();

            List<String> listAluno = new ArrayList<>();
            List<Integer> listTurma = new ArrayList<>();

            try {

                do {

                    if (!listTurma.contains(codigoTurma)) {

                        jsonEnvio = new JSONObject();

                        jsonArrayFechamentos = new JSONArray();

                        listTurma.add(codigoTurma);

                        jsonEnvio.put("CodigoTurma", codigoTurma);
                        jsonEnvio.put("CodigoDisciplina", disciplina);
                        jsonEnvio.put("Justificativa", cursor.getString(cursor.getColumnIndex("justificativa")));
                        jsonEnvio.put("AulasPlanejadas", cursor.getInt(cursor.getColumnIndex("aulasPlanejadas")));
                        jsonEnvio.put("AulasRealizadas", cursor.getInt(cursor.getColumnIndex("aulasRealizadas")));
                        jsonEnvio.put("CodigoTipoFechamento", cursor.getInt(cursor.getColumnIndex("codigoTipoFechamento")));
                        jsonEnvio.put("CodigoEventoCalendario", cursor.getInt(cursor.getColumnIndex("codigoTipoFechamento")));

                        jsonEnvio.put("Fechamentos", jsonArrayFechamentos);
                    }

                    if (!listAluno.contains(cursor.getString(cursor.getColumnIndex("codigoMatricula")))) {

                        listAluno.add(cursor.getString(cursor.getColumnIndex("codigoMatricula")));

                        jsonObjFechamento = new JSONObject();

                        jsonObjFechamento.put("Codigo", cursor.getInt(cursor.getColumnIndex("codigoFechamento")));

                        if (cursor.getInt(cursor.getColumnIndex("nota")) > 10 || cursor.getInt(cursor.getColumnIndex("nota")) < 0) {

                            jsonObjFechamento.put("Nota", JSONObject.NULL);
                        }
                        else {

                            jsonObjFechamento.put("Nota", cursor.getInt(cursor.getColumnIndex("nota")));
                        }
                        if (anosIniciais
                                && disciplina != 1000) {

                            jsonObjFechamento.put("Faltas", JSONObject.NULL);
                            jsonObjFechamento.put("FaltasCompensadas", JSONObject.NULL);
                            jsonObjFechamento.put("FaltasAcumuladas", JSONObject.NULL);
                        }
                        else {

                            jsonObjFechamento.put("Faltas", cursor.getInt(cursor.getColumnIndex("faltas")));
                            jsonObjFechamento.put("FaltasCompensadas", cursor.getInt(cursor.getColumnIndex("ausenciasCompensadas")));
                            jsonObjFechamento.put("FaltasAcumuladas", cursor.getInt(cursor.getColumnIndex("faltasAcumuladas")));
                        }
                        jsonObjFechamento.put("Aluno", cursor.getString(cursor.getColumnIndex("codigoMatricula")));

                        jsonArrayFechamentos.put(jsonObjFechamento);
                    }
                }
                while (cursor.moveToNext());
            }
            catch (Exception e){

            }
            finally {

                cursor.close();
            }
        }

        Log.e(TAG, jsonEnvio.toString());

        Log.e("F", "Fechamento: " + jsonEnvio);

        return jsonEnvio;
    }

    FechamentoAluno getFechamentoAluno(int codigoTurma, Aluno aluno, int codigoDisciplina, int codigoTipoFechamento) {

        Cursor cursor = null;

        FechamentoAluno fechamento = null;

        try {

            cursor = banco.get().rawQuery(

                    "SELECT * FROM FECHAMENTO_ALUNO WHERE codigoTurma = " + codigoTurma +
                            " AND codigoMatricula = " + aluno.getCodigoMatricula()  +
                            " AND codigoDisciplina = " + codigoDisciplina +
                            " AND codigoTipoFechamento = " + codigoTipoFechamento,
                            null
            );

            if(cursor.getCount() > 0) {

                if(cursor.moveToNext()) {

                    fechamento = new FechamentoAluno();

                    fechamento.setNomeAluno(aluno.getNomeAluno());
                    fechamento.setNota(cursor.getInt(cursor.getColumnIndex("nota")));
                    int confirmado = cursor.getInt(cursor.getColumnIndex("confirmado"));
                    fechamento.setFaltas(cursor.getInt(cursor.getColumnIndex("faltas")));
                    fechamento.setCodigoTurma(cursor.getInt(cursor.getColumnIndex("codigoTurma")));
                    fechamento.setCodigoFechamento(cursor.getInt(cursor.getColumnIndex("codigoFechamento")));
                    fechamento.setCodigoDisciplina(cursor.getInt(cursor.getColumnIndex("codigoDisciplina")));
                    fechamento.setFaltasAcumuladas(cursor.getInt(cursor.getColumnIndex("faltasAcumuladas")));
                    fechamento.setCodigoMatricula(cursor.getString(cursor.getColumnIndex("codigoMatricula")));
                    fechamento.setAusenciasCompensadas(cursor.getInt(cursor.getColumnIndex("ausenciasCompensadas")));
                    fechamento.setCodigoTipoFechamento(cursor.getInt(cursor.getColumnIndex("codigoTipoFechamento")));

                    if(confirmado == 0) {

                        fechamento.setConfirmado(false);
                    }
                    else {

                        fechamento.setConfirmado(true);
                    }
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
        return fechamento;
    }
}
