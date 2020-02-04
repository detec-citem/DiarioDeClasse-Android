package br.gov.sp.educacao.sed.mobile.RegistroDeAula;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

public class HabilidadeRegistroFundamentalDao {
    //Métodos
    public static void atualizarHabilidadesRegistro(int codigoAntigo, int codigoNovo, SQLiteDatabase database) {
        SQLiteStatement atualizarHabilidadeRegistro = database.compileStatement("UPDATE HABILIDADE_REGISTRO_FUNDAMENTAL SET registroAula = ? WHERE registroAula = ?;");
        atualizarHabilidadeRegistro.bindLong(1, codigoNovo);
        atualizarHabilidadeRegistro.bindLong(2, codigoAntigo);
        atualizarHabilidadeRegistro.executeUpdateDelete();
    }

    public static List<Integer> buscarHabilidadesRegistroFundamental(int codigoRegistroAula, SQLiteDatabase database) {
        List<Integer> habilidadesRegistro = null;
        Cursor cursor = database.rawQuery("SELECT habilidade FROM HABILIDADE_REGISTRO_FUNDAMENTAL WHERE registroAula = " + codigoRegistroAula, null);
        int numeroHabilidadesRegistro = cursor.getCount();
        if (numeroHabilidadesRegistro > 0) {
            habilidadesRegistro = new ArrayList<>(numeroHabilidadesRegistro);
            while (cursor.moveToNext()) {
                int habilidade = cursor.getInt(0);
                habilidadesRegistro.add(habilidade);
            }
        }
        cursor.close();
        return habilidadesRegistro;
    }

    public static void deletarHabilidadeRegistroFundamental(int codigoRegistroAula, SQLiteDatabase database) {
        SQLiteStatement deletarHabilidadeRegistro = database.compileStatement("DELETE FROM HABILIDADE_REGISTRO_FUNDAMENTAL WHERE registroAula = ?;");
        deletarHabilidadeRegistro.bindLong(1, codigoRegistroAula);
        deletarHabilidadeRegistro.executeUpdateDelete();
        deletarHabilidadeRegistro.close();
    }

    public static void inserirHabilidadeRegistroFundamental(int codigoRegistroAula, int habilidade, SQLiteDatabase database) {
        SQLiteStatement inserirHabilidadeRegistro = database.compileStatement("INSERT INTO HABILIDADE_REGISTRO_FUNDAMENTAL (registroAula, habilidade) VALUES (?, ?);");
        inserirHabilidadeRegistro.bindLong(1, codigoRegistroAula);
        inserirHabilidadeRegistro.bindLong(2, habilidade);
        inserirHabilidadeRegistro.executeInsert();
        inserirHabilidadeRegistro.clearBindings();
        inserirHabilidadeRegistro.close();
    }
}