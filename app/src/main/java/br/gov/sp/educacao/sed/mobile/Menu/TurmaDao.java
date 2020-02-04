package br.gov.sp.educacao.sed.mobile.Menu;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class TurmaDao {
    //Métodos
    public static int buscarCodigoTurma(int turmaId, SQLiteDatabase database) {
        SQLiteStatement buscarCodigoTurma = database.compileStatement("SELECT codigoTurma FROM TURMAS WHERE id = ?;");
        buscarCodigoTurma.bindLong(1, turmaId);
        int codigoTurma = (int) buscarCodigoTurma.simpleQueryForLong();
        buscarCodigoTurma.close();
        return codigoTurma;
    }
}