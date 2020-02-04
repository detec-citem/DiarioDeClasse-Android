package br.gov.sp.educacao.sed.mobile.Menu;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConteudoFundamentalDao {
    //Variáveis
    private static SQLiteStatement inserirConteudoFundamental;

    //Métodos
    public static void fecharStatement() {
        if (inserirConteudoFundamental != null) {
            inserirConteudoFundamental.close();
        }
        inserirConteudoFundamental = null;
    }

    public static int buscarBimestreDoConteudoFundamental(int codigoConteudo, SQLiteDatabase database) {
        SQLiteStatement bimestreStatement = database.compileStatement("SELECT bimestre FROM CONTEUDO_FUNDAMENTAL WHERE codigoConteudo = ?;");
        bimestreStatement.bindLong(1, codigoConteudo);
        int bimestre = (int) bimestreStatement.simpleQueryForLong();
        bimestreStatement.close();
        return bimestre;
    }

    public static List<ConteudoFundamental> buscarConteudosFundamental(int ano, int bimestre, int serie, int disciplina, SQLiteDatabase database) {
        ArrayList<ConteudoFundamental> conteudoFundamentais = null;
        Cursor cursor = database.rawQuery("SELECT A.codigoConteudo, A.campoDeAtuacao, A.descricao, A.praticaLinguagem, A.objetosConhecimento, B.codigoCurriculo FROM CONTEUDO_FUNDAMENTAL AS A INNER JOIN CURRICULO_FUNDAMENTAL AS B ON A.curriculoFundamental = B.codigoCurriculo WHERE A.bimestre = " + bimestre + " AND B.ano = " + ano + " AND B.serie = " + serie + " AND B.disciplina = " + disciplina, null);
        int numeroConteudos = cursor.getCount();
        if (numeroConteudos > 0) {
            conteudoFundamentais = new ArrayList<>(numeroConteudos);
            while (cursor.moveToNext()) {
                int codigoConteudo = cursor.getInt(0);
                String campoDeAtuacao = cursor.getString(1);
                String descricao = cursor.getString(2);
                String praticaLinguagem = cursor.getString(3);
                String objetosConhecimento = cursor.getString(4);
                int codigoCurriculo = cursor.getInt(5);
                ConteudoFundamental conteudoFundamental = new ConteudoFundamental(false, codigoConteudo, codigoCurriculo, bimestre, campoDeAtuacao, descricao, praticaLinguagem, objetosConhecimento);
                conteudoFundamentais.add(conteudoFundamental);
            }
        }
        cursor.close();
        return conteudoFundamentais;
    }

    public static void inserirConteudoFundamental(int curriculo, JSONObject conteudoFundamentalJson, SQLiteDatabase database) throws JSONException {
        if (conteudoFundamentalJson.has("Codigo") && conteudoFundamentalJson.has("CampoDeAtuacao") && conteudoFundamentalJson.has("Descricao") && conteudoFundamentalJson.has("PraticaLinguagem") && conteudoFundamentalJson.has("ObjetosConhecimento") && conteudoFundamentalJson.has("Bimestre") && !conteudoFundamentalJson.isNull("Codigo") && !conteudoFundamentalJson.isNull("Descricao") && !conteudoFundamentalJson.isNull("ObjetosConhecimento") && !conteudoFundamentalJson.isNull("Bimestre")) {
            if (inserirConteudoFundamental == null) {
                inserirConteudoFundamental = database.compileStatement("INSERT OR REPLACE INTO CONTEUDO_FUNDAMENTAL (codigoConteudo, campoDeAtuacao, descricao, praticaLinguagem, objetosConhecimento, bimestre, curriculoFundamental) VALUES (?, ?, ?, ?, ?, ?, ?)");
            }
            inserirConteudoFundamental.bindLong(1, conteudoFundamentalJson.getInt("Codigo"));
            inserirConteudoFundamental.bindString(2, conteudoFundamentalJson.getString("CampoDeAtuacao"));
            inserirConteudoFundamental.bindString(3, conteudoFundamentalJson.getString("Descricao"));
            inserirConteudoFundamental.bindString(4, conteudoFundamentalJson.getString("PraticaLinguagem"));
            inserirConteudoFundamental.bindString(5, conteudoFundamentalJson.getString("ObjetosConhecimento"));
            inserirConteudoFundamental.bindLong(6, conteudoFundamentalJson.getInt("Bimestre"));
            inserirConteudoFundamental.bindLong(7, curriculo);
            inserirConteudoFundamental.executeInsert();
            inserirConteudoFundamental.clearBindings();
        }
    }
}