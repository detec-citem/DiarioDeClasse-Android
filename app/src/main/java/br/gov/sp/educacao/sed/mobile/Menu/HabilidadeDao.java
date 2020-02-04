package br.gov.sp.educacao.sed.mobile.Menu;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDoneException;
import android.database.sqlite.SQLiteStatement;

import org.json.JSONException;
import org.json.JSONObject;

public class HabilidadeDao {
    //Variáveis
    private static SQLiteStatement statementInserirHabilidade;
    private static SQLiteStatement statementInserirHabilidadeConteudo;
    private static SQLiteStatement statementTemHabilidade;

    //Métodos
    public static void inserirHabilidade(int codigoConteudo, JSONObject habilidadeJson, SQLiteDatabase database) throws JSONException {
        if (habilidadeJson.has("Codigo") && habilidadeJson.has("Descricao") && !habilidadeJson.isNull("Codigo") && !habilidadeJson.isNull("Descricao")) {
            int codigoHabilidade = habilidadeJson.getInt("Codigo");
            String descricao = habilidadeJson.getString("Descricao");
            if (statementTemHabilidade == null) {
                statementTemHabilidade = database.compileStatement("SELECT 1 FROM NOVO_HABILIDADE WHERE codigoHabilidade = ? AND codigoConteudo = ?;");
                statementInserirHabilidadeConteudo = database.compileStatement("INSERT OR REPLACE INTO NOVO_CONTEUDO_HABILIDADE (conteudo, habilidade) VALUES (?, ?);");
            }
            statementTemHabilidade.bindLong(1, codigoHabilidade);
            statementTemHabilidade.bindLong(2, codigoConteudo);
            try {
                statementTemHabilidade.simpleQueryForString();
                statementTemHabilidade.clearBindings();
            }
            catch (SQLiteDoneException e) {
                if (statementInserirHabilidade == null) {
                    statementInserirHabilidade = database.compileStatement("INSERT INTO NOVO_HABILIDADE (codigoHabilidade, codigoConteudo, descricao) VALUES (?, ?, ?);");
                }
                statementInserirHabilidade.bindLong(1, codigoHabilidade);
                statementInserirHabilidade.bindLong(2, codigoConteudo);
                statementInserirHabilidade.bindString(3, descricao);
                statementInserirHabilidade.executeInsert();
                statementInserirHabilidade.clearBindings();
            }
            statementInserirHabilidadeConteudo.bindLong(1, codigoConteudo);
            statementInserirHabilidadeConteudo.bindLong(2, codigoHabilidade);
            statementInserirHabilidadeConteudo.executeInsert();
            statementInserirHabilidadeConteudo.clearBindings();
        }
    }

    public static void fecharStatements() {
        if (statementInserirHabilidade != null) {
            statementInserirHabilidade.clearBindings();
            statementInserirHabilidade.close();
        }
        if (statementInserirHabilidadeConteudo != null) {
            statementInserirHabilidadeConteudo.clearBindings();
            statementInserirHabilidadeConteudo.close();
        }
        if (statementTemHabilidade != null) {
            statementTemHabilidade.clearBindings();
            statementTemHabilidade.close();
        }
        statementInserirHabilidade = null;
        statementInserirHabilidadeConteudo = null;
        statementTemHabilidade = null;
    }
}