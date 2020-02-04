package br.gov.sp.educacao.sed.mobile.RegistroDeAula;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

public class RegistroAulaFundamentalDao {
    //Métodos
    public static void atualizarRegistro(int codigoAntigo, RegistroAulaFundamental registroAula, SQLiteDatabase database) {
        SQLiteStatement atualizarRegistroStatement = database.compileStatement("UPDATE REGISTRO_AULA_FUNDAMENTAL SET codigoRegistroAula = ?, dataCriacao = ?, observacoes = ?, sincronizado = ?, horarios = ?, bimestre = ?, disciplina = ?, turma = ? WHERE codigoRegistroAula = ?;");
        atualizarRegistroStatement.bindLong(1, registroAula.getCodigoRegistroAula());
        atualizarRegistroStatement.bindString(2, registroAula.getDataCriacao());
        atualizarRegistroStatement.bindString(3, registroAula.getObservacoes());
        atualizarRegistroStatement.bindLong(4, 1);
        atualizarRegistroStatement.bindString(5, registroAula.getHorarios());
        atualizarRegistroStatement.bindLong(6, registroAula.getBimestre());
        atualizarRegistroStatement.bindLong(7, registroAula.getDisciplina());
        atualizarRegistroStatement.bindLong(8, registroAula.getTurma());
        atualizarRegistroStatement.bindLong(9, codigoAntigo);
        atualizarRegistroStatement.executeUpdateDelete();
        atualizarRegistroStatement.close();
    }

    public static RegistroAulaFundamental buscarRegistroAulaFundamental(int bimestre, int disciplina, int turma, String dataCriacao, SQLiteDatabase database) {
        RegistroAulaFundamental registroAulaFundamental = null;
        Cursor cursor = database.rawQuery("SELECT codigoRegistroAula, sincronizado, observacoes, horarios FROM REGISTRO_AULA_FUNDAMENTAL WHERE bimestre = " + bimestre + " AND disciplina = " + disciplina + " AND turma = " + turma + " AND dataCriacao = '" + dataCriacao + "';", null);
        if (cursor.moveToFirst()) {
            int codigoRegistroAula = cursor.getInt(0);
            int sincronizado = cursor.getInt(1);
            String observacoes = cursor.getString(2);
            String horarios = cursor.getString(3);
            registroAulaFundamental = new RegistroAulaFundamental(codigoRegistroAula, sincronizado == 1, dataCriacao, observacoes, horarios, bimestre, disciplina, turma);
        }
        cursor.close();
        return registroAulaFundamental;
    }

    public static int inserirRegistroAulaFundamental(RegistroAulaFundamental registroAulaFundamental, SQLiteDatabase database) {
        SQLiteStatement inserirRegistroAulaStatement = database.compileStatement("INSERT OR REPLACE INTO REGISTRO_AULA_FUNDAMENTAL (codigoRegistroAula, dataCriacao, observacoes, sincronizado, horarios, bimestre, disciplina, turma) VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
        int codigoRegistroAula = registroAulaFundamental.getCodigoRegistroAula();
        boolean sincronizado = registroAulaFundamental.getSincronizado();
        String dataCriacao = registroAulaFundamental.getDataCriacao();
        String observacoes = registroAulaFundamental.getObservacoes();
        String horarios = registroAulaFundamental.getHorarios();
        int bimestre = registroAulaFundamental.getBimestre();
        int disciplina = registroAulaFundamental.getDisciplina();
        int turma = registroAulaFundamental.getTurma();
        inserirRegistroAulaStatement.bindLong(1, codigoRegistroAula);
        inserirRegistroAulaStatement.bindString(2, dataCriacao);
        inserirRegistroAulaStatement.bindString(3, observacoes);
        inserirRegistroAulaStatement.bindString(5, horarios);
        inserirRegistroAulaStatement.bindLong(6, bimestre);
        inserirRegistroAulaStatement.bindLong(7, disciplina);
        inserirRegistroAulaStatement.bindLong(8, turma);
        if (sincronizado) {
            inserirRegistroAulaStatement.bindLong(4, 1);
        }
        else {
            inserirRegistroAulaStatement.bindLong(4, 0);
        }
        codigoRegistroAula = (int) inserirRegistroAulaStatement.executeInsert();
        inserirRegistroAulaStatement.clearBindings();
        inserirRegistroAulaStatement.close();
        return codigoRegistroAula;
    }

    public static int numeroDeRegistros(SQLiteDatabase database) {
        SQLiteStatement inserirRegistroAulaStatement = database.compileStatement("SELECT COUNT(codigoRegistroAula) FROM REGISTRO_AULA_FUNDAMENTAL;");
        int numeroDeRegistros = (int) inserirRegistroAulaStatement.simpleQueryForLong();
        inserirRegistroAulaStatement.close();
        return numeroDeRegistros;
    }

    public static List<RegistroAulaFundamental> buscarRegistrosAulaNaoSincronizados(SQLiteDatabase database) {
        List<RegistroAulaFundamental> registrosAula = null;
        Cursor cursor = database.rawQuery("SELECT codigoRegistroAula, dataCriacao, observacoes, horarios, bimestre, disciplina, turma FROM REGISTRO_AULA_FUNDAMENTAL WHERE sincronizado = 0;", null);
        int numeroRegistrosAula = cursor.getCount();
        if (numeroRegistrosAula > 0) {
            registrosAula = new ArrayList<>(numeroRegistrosAula);
            while (cursor.moveToNext()) {
                int codigoRegistroAula = cursor.getInt(0);
                String dataCriacao = cursor.getString(1);
                String observacoes = cursor.getString(2);
                String horarios = cursor.getString(3);
                int bimestre = cursor.getInt(4);
                int disciplina = cursor.getInt(5);
                int turma = cursor.getInt(6);
                RegistroAulaFundamental registroAula = new RegistroAulaFundamental(codigoRegistroAula, false, dataCriacao, observacoes, horarios, bimestre, disciplina, turma);
                registrosAula.add(registroAula);
            }
        }
        cursor.close();
        return registrosAula;
    }
}