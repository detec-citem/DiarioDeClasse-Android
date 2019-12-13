package br.gov.sp.educacao.sed.mobile.Frequencia;

import java.util.List;

import android.database.Cursor;

import android.database.sqlite.SQLiteStatement;

import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;

import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.DateUtils;
import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;

class FrequenciaDBsetters {

    private Banco banco;

    private Cursor cursor;

    private String queryInserirFaltasAnuaisAluno;

    private String queryAtualizarFaltasAnuaisAluno;

    private String queryInserirFaltasBimestreAluno;

    private String queryInserirComparecimentoAluno;

    private String queryAtualizarFaltasBimestreAluno;

    private String queryAtualizarComparecimentoAluno;

    @SuppressWarnings("FieldCanBeLocal")
    private SQLiteStatement statementInserirFaltasAnuaisAluno;

    @SuppressWarnings("FieldCanBeLocal")
    private SQLiteStatement statementAtualizarFaltasAnuaisAluno;

    @SuppressWarnings("FieldCanBeLocal")
    private SQLiteStatement statementInserirComparecimentoAluno;

    private SQLiteStatement statementInserirFaltasBimestreAluno;

    private SQLiteStatement statementAtualizarFaltasBimestreAluno;

    @SuppressWarnings("FieldCanBeLocal")
    private SQLiteStatement statementAtualizarComparecimentoAluno;

    private final String TAG = FrequenciaDBsetters.class.getSimpleName() ;

    FrequenciaDBsetters(Banco banco) {

        this.banco = banco;

        queryAtualizarFaltasAnuaisAluno =

                "UPDATE TOTALFALTASALUNOS SET faltasAnuais = ? WHERE aluno_id = ? AND disciplina_id = ?;";

        queryInserirFaltasAnuaisAluno =

                "INSERT INTO TOTALFALTASALUNOS (faltasAnuais, codigoMatricula, disciplina_id) VALUES (?, ?, ?);";

        queryAtualizarFaltasBimestreAluno =

                "UPDATE TOTALFALTASALUNOS SET faltasBimestre = ? WHERE aluno_id = ? AND disciplina_id = ?;";

        queryInserirFaltasBimestreAluno =

                "INSERT INTO TOTALFALTASALUNOS (faltasBimestre, codigoMatricula, disciplina_id) VALUES (?, ?, ?);";

        queryAtualizarComparecimentoAluno =

                "UPDATE FALTASALUNOS SET tipoFalta = ? WHERE aluno_id = ? AND diasLetivos_id = ? AND aula_id = ?;";

        queryInserirComparecimentoAluno =

                "INSERT INTO FALTASALUNOS (tipoFalta, aluno_id, dataCadastro, usuario_id, aula_id, diasLetivos_id) VALUES (?, ?, ?, ?, ?, ?);";
    }
    ///OK
    void setFaltasAluno(Aluno aluno, int idDisciplina) {

        try {

            cursor = banco.get().rawQuery(

                    "SELECT faltasAnuais, faltasBimestre, faltasSequenciais FROM TOTALFALTASALUNOS " +
                            "WHERE aluno_id = " + aluno.getId() +
                            " AND disciplina_id = " + idDisciplina, null
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
    }
    ///OK
    void setFaltasPorAluno(List<Aluno> alunos, int disciplinaId) {

        for(int i = 0; i < alunos.size(); i++) {

            try {

                cursor = banco.get().rawQuery(

                        "SELECT faltasAnuais, faltasBimestre, faltasSequenciais FROM TOTALFALTASALUNOS " +
                                "WHERE aluno_id = " + alunos.get(i).getId() +
                                " AND disciplina_id = " + disciplinaId,
                                null
                );

                if(cursor.getCount() > 0) {

                    cursor.moveToNext();

                    alunos.get(i).setFaltasAnuais(cursor.getInt(cursor.getColumnIndex("faltasAnuais")));

                    alunos.get(i).setFaltasBimestre(cursor.getInt(cursor.getColumnIndex("faltasBimestre")));

                    alunos.get(i).setFaltasSequenciais(cursor.getInt(cursor.getColumnIndex("faltasSequenciais")));
                }
            }
            catch(Exception e) {

                CrashAnalytics.e(TAG, e);
            }
            finally {

                if (cursor != null) {

                    cursor.close();
                }
            }
        }
    }
    ///OK
    void setNumeroFaltasAnuais(Aluno aluno, int disciplinaId, int faltasAnuais) {

        statementAtualizarFaltasAnuaisAluno = banco.get().compileStatement(queryAtualizarFaltasAnuaisAluno);

        statementInserirFaltasAnuaisAluno = banco.get().compileStatement(queryInserirFaltasAnuaisAluno);

        try {

            statementAtualizarFaltasAnuaisAluno.bindLong(1, faltasAnuais);
            statementAtualizarFaltasAnuaisAluno.bindLong(2, aluno.getId());
            statementAtualizarFaltasAnuaisAluno.bindLong(3, disciplinaId);

            if(statementAtualizarFaltasAnuaisAluno.executeUpdateDelete() == 0) {

                statementInserirFaltasAnuaisAluno.bindLong(1, faltasAnuais);
                statementInserirFaltasAnuaisAluno.bindString(2, aluno.getCodigoMatricula());
                statementInserirFaltasAnuaisAluno.bindLong(3, disciplinaId);

                statementInserirFaltasAnuaisAluno.executeInsert();
            }
        }
        catch (Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            statementAtualizarFaltasAnuaisAluno.clearBindings();

            statementAtualizarFaltasAnuaisAluno.close();

            statementInserirFaltasAnuaisAluno.clearBindings();

            statementInserirFaltasAnuaisAluno.close();
        }
    }
    ///OK
    void setNumeroFaltasBimestre(Aluno aluno, int disciplinaId, int faltasBimestre) {

        try {

            statementAtualizarFaltasBimestreAluno = banco.get().compileStatement(queryAtualizarFaltasBimestreAluno);

            statementInserirFaltasBimestreAluno = banco.get().compileStatement(queryInserirFaltasBimestreAluno);

            statementAtualizarFaltasBimestreAluno.bindLong(1, faltasBimestre);
            statementAtualizarFaltasBimestreAluno.bindLong(2, aluno.getId());
            statementAtualizarFaltasBimestreAluno.bindLong(3, disciplinaId);

            if(statementAtualizarFaltasBimestreAluno.executeUpdateDelete() == 0) {

                statementInserirFaltasBimestreAluno.bindLong(   1, faltasBimestre);
                statementInserirFaltasBimestreAluno.bindString( 2, aluno.getCodigoMatricula());
                statementInserirFaltasBimestreAluno.bindLong(   3, disciplinaId);
            }
        }
        catch (Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            statementAtualizarFaltasBimestreAluno.clearBindings();

            statementAtualizarFaltasBimestreAluno.close();

            statementInserirFaltasBimestreAluno.clearBindings();

            statementInserirFaltasBimestreAluno.close();
        }
    }
    ///OK
    void setComparecimento(int usuarioId, int diaLetivoId, int aulaId, int alunoId, String comparecimento) {

        String dataAtual = DateUtils.getCurrentDate();

        statementAtualizarComparecimentoAluno = banco.get().compileStatement(queryAtualizarComparecimentoAluno);

        statementInserirComparecimentoAluno = banco.get().compileStatement(queryInserirComparecimentoAluno);

        try {

            statementAtualizarComparecimentoAluno.bindString( 1, comparecimento);
            statementAtualizarComparecimentoAluno.bindLong(   2, alunoId);
            statementAtualizarComparecimentoAluno.bindLong(   3, diaLetivoId);
            statementAtualizarComparecimentoAluno.bindLong(   4, aulaId);

            if(statementAtualizarComparecimentoAluno.executeUpdateDelete() == 0) {

                statementInserirComparecimentoAluno.bindString( 1, comparecimento);
                statementInserirComparecimentoAluno.bindLong(   2, alunoId);
                statementInserirComparecimentoAluno.bindString( 3, dataAtual);
                statementInserirComparecimentoAluno.bindLong(   4, usuarioId);
                statementInserirComparecimentoAluno.bindLong(   5, aulaId);
                statementInserirComparecimentoAluno.bindLong(   6, diaLetivoId);

                statementInserirComparecimentoAluno.executeInsert();
            }
        }
        catch (Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            statementAtualizarComparecimentoAluno.clearBindings();

            statementAtualizarComparecimentoAluno.close();

            statementInserirComparecimentoAluno.clearBindings();

            statementInserirComparecimentoAluno.close();
        }
    }
}
