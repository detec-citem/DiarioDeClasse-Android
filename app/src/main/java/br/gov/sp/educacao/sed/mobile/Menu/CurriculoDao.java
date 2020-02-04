package br.gov.sp.educacao.sed.mobile.Menu;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import org.json.JSONException;
import org.json.JSONObject;

public class CurriculoDao {
    //Variáveis
    private static SQLiteStatement statementInserirCurriculo;

    //Métodos
    public static void inserirCurriculo(int codigoCurriculo, int codigoGrupo, JSONObject grupoCurriculoJson, SQLiteDatabase database) throws JSONException {
        if (grupoCurriculoJson.has("Bimestre") && !grupoCurriculoJson.isNull("Bimestre")) {
            if (statementInserirCurriculo == null) {
                statementInserirCurriculo = database.compileStatement("INSERT OR REPLACE INTO NOVO_CURRICULO (codigo, codigoGrupo, bimestre) VALUES (?, ?, ?);");
            }
            int bimestre = grupoCurriculoJson.getInt("Bimestre");
            statementInserirCurriculo.bindLong(1, codigoCurriculo);
            statementInserirCurriculo.bindLong(2, codigoGrupo);
            statementInserirCurriculo.bindLong(3, bimestre);
            statementInserirCurriculo.executeInsert();
            statementInserirCurriculo.clearBindings();
        }
    }

    public static void fecharStatement() {
        if (statementInserirCurriculo != null) {
            statementInserirCurriculo.clearBindings();
            statementInserirCurriculo.close();
        }
        statementInserirCurriculo = null;
    }
}