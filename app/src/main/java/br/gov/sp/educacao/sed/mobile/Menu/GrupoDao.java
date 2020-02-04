package br.gov.sp.educacao.sed.mobile.Menu;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import org.json.JSONException;
import org.json.JSONObject;

public class GrupoDao {
    //Variáveis
    private static SQLiteStatement statementInserirGrupo;

    //Métodos
    public static void inserirGrupo(int codigoGrupo, JSONObject grupoJson, SQLiteDatabase database) throws JSONException {
        if (grupoJson.has("AnoLetivo") && grupoJson.has("CodigoDisciplina") && grupoJson.has("CodigoTipoEnsino") && grupoJson.has("Serie") && !grupoJson.isNull("AnoLetivo") && !grupoJson.isNull("CodigoDisciplina") && !grupoJson.isNull("CodigoTipoEnsino") && !grupoJson.isNull("Serie")) {
            if (statementInserirGrupo == null) {
                statementInserirGrupo = database.compileStatement("INSERT OR REPLACE INTO NOVO_GRUPO (codigo, anoLetivo, codigoTipoEnsino, serie, codigoDisciplina) VALUES (?, ?, ?, ?, ?);");
            }
            int anoLetivo = grupoJson.getInt("AnoLetivo");
            int codigoDisciplina = grupoJson.getInt("CodigoDisciplina");
            int codigoTipoEnsino = grupoJson.getInt("CodigoTipoEnsino");
            int serie = grupoJson.getInt("Serie");
            statementInserirGrupo.bindLong(1, codigoGrupo);
            statementInserirGrupo.bindLong(2, anoLetivo);
            statementInserirGrupo.bindLong(3, codigoTipoEnsino);
            statementInserirGrupo.bindLong(4, serie);
            statementInserirGrupo.bindLong(5, codigoDisciplina);
            statementInserirGrupo.executeInsert();
            statementInserirGrupo.clearBindings();
        }
    }

    public static void fecharStatement() {
        if (statementInserirGrupo != null) {
            statementInserirGrupo.clearBindings();
            statementInserirGrupo.close();
        }
        statementInserirGrupo = null;
    }
}