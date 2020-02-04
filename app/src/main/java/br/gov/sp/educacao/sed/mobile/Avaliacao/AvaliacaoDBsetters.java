package br.gov.sp.educacao.sed.mobile.Avaliacao;

import android.content.ContentValues;
import android.database.Cursor;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import br.gov.sp.educacao.sed.mobile.Escola.Bimestre;
import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;
import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;

public class AvaliacaoDBsetters {

    private Banco banco;

    private Cursor cursor;

    private final String TAG = AvaliacaoDBsetters.class.getSimpleName();

    public AvaliacaoDBsetters(Banco banco) {

        this.banco = banco;
    }
    ///OK
    String formataDataAvaliacao(String data) {

        String data1 = "";

        if(data != null && !data.equals("") ) {

            data1 = data.split("/")[2] + "-" + data.split("/")[1] + "-" + data.split("/")[0];
        }

        return data1;
    }
    ///OK
    void setNotasAluno(NotasAluno notasAluno) {

        ContentValues insertValues = new ContentValues();

        insertValues.put("nota", notasAluno.getNota());
        insertValues.put("aluno_id", notasAluno.getAluno_id());
        insertValues.put("usuario_id", notasAluno.getUsuario_id());
        insertValues.put("avaliacao_id", notasAluno.getAvaliacao_id());
        insertValues.put("dataCadastro", notasAluno.getDataCadastro());
        insertValues.put("dataServidor", notasAluno.getDataServidor());
        insertValues.put("codigoMatricula", notasAluno.getCodigoMatricula());

        try {

           int updates = banco.get().update (

                    "NOTASALUNO", insertValues,"aluno_id=" + notasAluno.getAluno_id() +
                            " AND avaliacao_id=" + notasAluno.getAvaliacao_id(),null
           );

           if(updates == 0) {

               banco.get().insert("NOTASALUNO", null, insertValues);
           }
        }
        catch(Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            insertValues.clear();
        }
    }
    ///OK
    void setAvaliacaoDataServidorNull(int avaliacaoId) {

        cursor = null;

        try {

            cursor = banco.get().rawQuery(

                    "UPDATE AVALIACOES SET dataServidor = null WHERE id = " + avaliacaoId, null
            );
        }
        catch(Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            if(cursor != null) {

                cursor.moveToFirst();

                cursor.close();
            }
        }
    }
    ///OK
    void editarNotasAluno(Aluno aluno, int avaliacaoId) {

        ContentValues insertValues = new ContentValues();

        insertValues.put("nota", aluno.getNota());

        banco.get().beginTransaction();

        try {

            banco.get().update (

                    "NOTASALUNO", insertValues,"aluno_id=" + aluno.getId() +
                            " AND avaliacao_id=" + avaliacaoId,null);
        }
        catch(Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            insertValues.clear();

            banco.get().setTransactionSuccessful();

            banco.get().endTransaction();
        }
    }
    ///OK
    public void setAvaliacaoParaDeletar(int avaliacaoId) {

        cursor = null;

        try {

            cursor = banco.get().rawQuery(

                    "UPDATE AVALIACOES SET dataServidor = 'deletar' WHERE id = " + avaliacaoId, null
            );
        }
        catch(Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            if(cursor != null) {

                cursor.moveToFirst();

                cursor.close();
            }
        }
    }
    ///OK
    int setBimestreAvaliacao(String data, Bimestre bimestre) {

        String dataAvaliacao = formataDataAvaliacao(data);

        LocalDate dataAvalicaoFormatada = LocalDate.parse(

                dataAvaliacao, DateTimeFormat.forPattern("yyyy-MM-dd")
        );

        String dataInicioBimestre = formataDataAvaliacao(bimestre.getInicio());

        LocalDate dataInicioBimestreFormatada = LocalDate.parse(

                dataInicioBimestre, DateTimeFormat.forPattern("yyyy-MM-dd")
        );

        int numeroBimestre = (bimestre.getNumero() == 1 ? 4 : bimestre.getNumero() - 1);

        return (dataAvalicaoFormatada.isBefore(dataInicioBimestreFormatada) ? numeroBimestre : bimestre.getNumero());
    }
}
