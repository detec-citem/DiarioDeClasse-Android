package br.gov.sp.educacao.sed.mobile.Menu;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import org.json.JSONException;
import org.json.JSONObject;

public class CurriculoFundamentalDao {
    //Variáveis
    private static SQLiteStatement inserirCurriculoFundamental;

    //Métodos
    public static void fecharStatement() {
        if (inserirCurriculoFundamental != null) {
            inserirCurriculoFundamental.close();
        }
        inserirCurriculoFundamental = null;
    }

    public static int inserirCurriculoFundamental(JSONObject curriculoFundamentalJson, SQLiteDatabase database) throws JSONException {
        if (curriculoFundamentalJson.has("Ano") && curriculoFundamentalJson.has("Disciplina") && curriculoFundamentalJson.has("Serie") && curriculoFundamentalJson.has("TipoEnsino") && !curriculoFundamentalJson.isNull("Ano") && !curriculoFundamentalJson.isNull("Disciplina") && !curriculoFundamentalJson.isNull("Serie") && !curriculoFundamentalJson.isNull("TipoEnsino")) {
            if (inserirCurriculoFundamental == null) {
                inserirCurriculoFundamental = database.compileStatement("INSERT OR REPLACE INTO CURRICULO_FUNDAMENTAL (ano, serie, tipoEnsino, disciplina) VALUES (?, ?, ?, ?)");
            }
            inserirCurriculoFundamental.bindLong(1, curriculoFundamentalJson.getInt("Ano"));
            inserirCurriculoFundamental.bindLong(2, curriculoFundamentalJson.getInt("Serie"));
            inserirCurriculoFundamental.bindString(3, curriculoFundamentalJson.getString("TipoEnsino"));
            inserirCurriculoFundamental.bindLong(4, curriculoFundamentalJson.getInt("Disciplina"));
            int curriculoId = (int) inserirCurriculoFundamental.executeInsert();
            inserirCurriculoFundamental.clearBindings();
            return curriculoId;
        }
        return -1;
    }

    public static CurriculoFundamental buscarCurriculo(int ano, int serie, int disciplina, SQLiteDatabase database) {
        SQLiteStatement selectCodigoCurriculo = database.compileStatement("SELECT codigoCurriculo FROM CURRICULO_FUNDAMENTAL WHERE ano = ? AND serie = ? AND disciplina = ?;");
        selectCodigoCurriculo.bindLong(1, ano);
        selectCodigoCurriculo.bindLong(2, serie);
        selectCodigoCurriculo.bindLong(3, disciplina);
        int codigoCurriculo = (int) selectCodigoCurriculo.simpleQueryForLong();
        selectCodigoCurriculo.close();
        return new CurriculoFundamental(codigoCurriculo, ano, serie, disciplina);
    }
}