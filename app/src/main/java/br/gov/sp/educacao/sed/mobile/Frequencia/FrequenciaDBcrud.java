package br.gov.sp.educacao.sed.mobile.Frequencia;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;

public class FrequenciaDBcrud {

    private Banco banco;

    private String queryAtualizarDataServidor;

    @SuppressWarnings("FieldCanBeLocal")
    private SQLiteStatement statementAtualizarDataServidor;

    private final String TAG = FrequenciaDBcrud.class.getSimpleName();

    public FrequenciaDBcrud(Banco banco) {

        this.banco = banco;

        queryAtualizarDataServidor = "UPDATE FALTASALUNOS SET dataServidor = ? WHERE dataServidor IS NULL;";
    }
    ///OK
    public void updateFrequencia() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

        statementAtualizarDataServidor = banco.get().compileStatement(queryAtualizarDataServidor);

        banco.get().beginTransaction();

        try {

            statementAtualizarDataServidor.bindString(1, dateFormat.format(new Date()));

            statementAtualizarDataServidor.executeUpdateDelete();
        }
        catch (Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            statementAtualizarDataServidor.clearBindings();

            statementAtualizarDataServidor.close();

            banco.get().setTransactionSuccessful();

            banco.get().endTransaction();
        }
    }

    ////Aplicar Statement////
    public void excluirFrequencia(int diaLetivoId, int aulaId) {

        Cursor cursor = banco.get().rawQuery("DELETE FROM FALTASALUNOS " +
                "WHERE diasLetivos_id = " + diaLetivoId + " " +
                " AND aula_id = " + aulaId, null);

        cursor.moveToFirst();
    }
}
