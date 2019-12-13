package br.gov.sp.educacao.sed.mobile.Fechamento;

import java.util.ArrayList;

import android.database.Cursor;

import android.content.ContentValues;

import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;

import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;

class FechamentoDBcrud {

    private final String TAG = FechamentoDBcrud.class.getSimpleName();

    private Banco banco;

    FechamentoDBcrud(Banco banco) {

        this.banco = banco;
    }

    void insertMediaAluno(MediaAluno media) {

        ContentValues insertValues = new ContentValues();

        Cursor cursor1 = null;

        insertValues.put("codigoTurma", media.getCodigoTurma());
        insertValues.put("codigoDisciplina", media.getDisciplina());
        insertValues.put("codigoMatricula", media.getCodigoMatricula());
        insertValues.put("nota_media", media.getNotaMedia());
        insertValues.put("bimestre", media.getBimestre());

        int rowsUpdate = 0;

        try {

            rowsUpdate = banco.get().update(

                    "MEDIA_ALUNO", insertValues, "codigoMatricula = " + media.getCodigoMatricula() +
                            " AND codigoTurma = " + media.getCodigoTurma() +
                            " AND codigoDisciplina = " + media.getDisciplina(),
                    null
            );

            if (rowsUpdate == 0) {

                banco.get().insert("MEDIA_ALUNO", null, insertValues);
            }

            insertValues.clear();

            cursor1 = banco.get().rawQuery(

                    "UPDATE FECHAMENTO_ALUNO SET confirmado = 0 WHERE codigoTurma = " +
                            media.getCodigoTurma() + " AND codigoDisciplina = " +
                            media.getDisciplina() + " AND codigoMatricula = " +
                            media.getCodigoMatricula(), null
            );

            cursor1.moveToNext();
        }
        catch (Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            if(cursor1 != null) {

                cursor1.close();
            }

            insertValues.clear();
        }
    }

    void insertFechamentoTurma(FechamentoTurma fechamento) {

        ContentValues insertValues = new ContentValues();

        insertValues.put("codigoTurma", fechamento.getCodigoTurma());
        insertValues.put("codigoDisciplina", fechamento.getCodigoDisciplina());
        insertValues.put("codigoTipoFechamento", fechamento.getCodigoTipoFechamento());
        insertValues.put("aulasPlanejadas", fechamento.getAulasPlanejadas());
        insertValues.put("aulasRealizadas", fechamento.getAulasRealizadas());
        insertValues.put("justificativa", fechamento.getJustificativa());
        insertValues.put("dataServidor", fechamento.getDataServidor());

        int rowsUpdate = 0;

        try {

            rowsUpdate = banco.get().update(

                    "FECHAMENTO_TURMA", insertValues, " codigoTurma = " + fechamento.getCodigoTurma() +
                            " AND codigoDisciplina = " + fechamento.getCodigoDisciplina() +
                            " AND codigoTipoFechamento = " + fechamento.getCodigoTipoFechamento(),
                            null
            );

            if(rowsUpdate == 0) {

                banco.get().insert(

                        "FECHAMENTO_TURMA", null, insertValues
                );
            }
            else {

                setFechamentoTurmaDataServidorNull(

                        fechamento.getCodigoTurma(), fechamento.getCodigoDisciplina(), fechamento.getCodigoTipoFechamento()
                );
            }
        }
        catch(Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            insertValues.clear();
        }
    }

    void insertFechamentoAluno(FechamentoAluno fechamento) {

        ContentValues insertValues = new ContentValues();

        insertValues.put("codigoTurma", fechamento.getCodigoTurma());
        insertValues.put("codigoMatricula", fechamento.getCodigoMatricula());
        insertValues.put("codigoDisciplina", fechamento.getCodigoDisciplina());
        insertValues.put("codigoTipoFechamento", fechamento.getCodigoTipoFechamento());
        insertValues.put("nota", fechamento.getNota());
        insertValues.put("faltas", fechamento.getFaltas());
        insertValues.put("ausenciasCompensadas", fechamento.getAusenciasCompensadas());
        insertValues.put("faltasAcumuladas", fechamento.getFaltasAcumuladas());
        insertValues.put("confirmado", fechamento.isConfirmado() ? 1 : 0);

        int rowsUpdate = 0;

        try {

            rowsUpdate = banco.get().update(

                    "FECHAMENTO_ALUNO", insertValues, " codigoMatricula = " + fechamento.getCodigoMatricula() +
                            " AND codigoTurma = " + fechamento.getCodigoTurma() +
                            " AND codigoDisciplina = " + fechamento.getCodigoDisciplina() +
                            " AND codigoTipoFechamento = " + fechamento.getCodigoTipoFechamento(), null
            );

            if (rowsUpdate == 0) {

                banco.get().insert("FECHAMENTO_ALUNO", null, insertValues);
            }
            else {

                setFechamentoTurmaDataServidorNull(

                        fechamento.getCodigoTurma(), fechamento.getCodigoDisciplina(), fechamento.getCodigoTipoFechamento())
                ;

                setFechamentosAlunosDataServidorNull(

                        fechamento.getCodigoTurma(), fechamento.getCodigoDisciplina(), fechamento.getCodigoTipoFechamento()
                );
            }
        }
        catch (Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            insertValues.clear();
        }
    }

    void atualizarEstadoFechamentoAluno(ArrayList<Aluno> alunos) {

        Cursor cursor1 = null;

        try {
            for (int i = 0; i < alunos.size(); i++) {

                cursor1 = banco.get().rawQuery(

                        "UPDATE FECHAMENTO_ALUNO SET confirmado = 0 WHERE codigoMatricula = " + alunos.get(i).getCodigoMatricula(), null
                );

                cursor1.moveToNext();
            }
        }
        catch(Exception e) {

            e.printStackTrace();
        }
        finally {

            if(cursor1 != null) {

                cursor1.close();
            }
        }
    }

    void updateFechamentoTurma(String Data, int tipoFechamentoAtual) {

        ContentValues values = new ContentValues();

        values.put("dataServidor", Data);

        try {

            banco.get().update(

                    "FECHAMENTO_TURMA", values, "codigoTipoFechamento = ? " + "AND dataServidor = ''",
                    new String[]{String.valueOf(tipoFechamentoAtual)}
            );

            banco.get().update(

                    "FECHAMENTO_ALUNO", values, "codigoTipoFechamento = ? " + "AND dataServidor = ''" +
                            "AND confirmado = 0",
                    new String[]{String.valueOf(tipoFechamentoAtual)}
            );
        }
        catch (Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            values.clear();
        }
    }

    private void setFechamentoTurmaDataServidorNull(int codigoTurma, int codigoDisciplina, int codigoTipoFechamento) {

        ContentValues values = new ContentValues();

        values.put("dataServidor", "");

        try {

            banco.get().update(

                    "FECHAMENTO_TURMA", values, "codigoTurma = ? " + "AND codigoDisciplina = ? " +
                            "AND codigoTipoFechamento = ?",
                    new String[]{String.valueOf(codigoTurma),
                            String.valueOf(codigoDisciplina),
                            String.valueOf(codigoTipoFechamento)}
            );
        }
        catch (Exception e) {

            e.printStackTrace();
        }
        finally {

            values.clear();
        }
    }

    void updateFechamentoTurmaServidor(int codigoTurma, int codigoDisciplina, int codigoTipoFechamento, String data) {

        ContentValues values = new ContentValues();

        values.put("dataServidor", data);

        try {

            banco.get().update(

                    "FECHAMENTO_TURMA", values, "codigoTurma = ? " + "AND codigoDisciplina = ? " +
                            "AND codigoTipoFechamento = ?",
                            new String[]{String.valueOf(codigoTurma),
                                    String.valueOf(codigoDisciplina),
                                    String.valueOf(codigoTipoFechamento)}
                                    );
        }
        catch (Exception e) {

            e.printStackTrace();
        }
        finally {

            values.clear();
        }
    }

    private void setFechamentosAlunosDataServidorNull(int codigoTurma, int codigoDisciplina, int codigoTipoFechamento) {

        ContentValues values = new ContentValues();

        values.put("dataServidor", "");

        try {

            banco.get().update(

                    "FECHAMENTO_ALUNO", values, "codigoTurma = ? " + "AND codigoDisciplina = ? " +
                            "AND codigoTipoFechamento = ? ",
                            new String[]{String.valueOf(codigoTurma),
                                    String.valueOf(codigoDisciplina),
                                    String.valueOf(codigoTipoFechamento)}
                                    );
        }
        catch (Exception e) {

            e.printStackTrace();
        }
        finally {

            values.clear();
        }
    }
}
