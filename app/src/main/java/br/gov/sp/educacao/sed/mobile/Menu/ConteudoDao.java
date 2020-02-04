package br.gov.sp.educacao.sed.mobile.Menu;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import org.json.JSONException;
import org.json.JSONObject;

public class ConteudoDao {
    //Variáveis
    private static SQLiteStatement statementInserirConteudo;

    //Métodos
    public static void inserirConteudo(int codigoConteudo, int codigoCurriculo, JSONObject conteudoJson, SQLiteDatabase database) throws JSONException {
        if (conteudoJson.has("Descricao")  && !conteudoJson.isNull("Descricao")) {
            if (statementInserirConteudo == null) {
                statementInserirConteudo = database.compileStatement("INSERT OR REPLACE INTO NOVO_CONTEUDO (codigoConteudo, codigoCurriculo, descricao) VALUES (?, ?, ?);");
            }
            String descricao = conteudoJson.getString("Descricao");
            statementInserirConteudo.bindLong(1, codigoConteudo);
            statementInserirConteudo.bindLong(2, codigoCurriculo);
            statementInserirConteudo.bindString(3, descricao);
            statementInserirConteudo.executeInsert();
            statementInserirConteudo.clearBindings();
        }
    }

    public static void fecharStatement() {
        if (statementInserirConteudo != null) {
            statementInserirConteudo.clearBindings();
            statementInserirConteudo.close();
        }
        statementInserirConteudo = null;
    }
}