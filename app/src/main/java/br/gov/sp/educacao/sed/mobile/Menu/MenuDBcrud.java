package br.gov.sp.educacao.sed.mobile.Menu;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;

public class MenuDBcrud {
    //Constantes
    private final String TAG = "MenuDBcrud";

    //Variáveis
    private Banco banco;
    private SQLiteStatement statementAtualizarTokenUsuario;

    //Construtor
    public MenuDBcrud(Banco banco) {
        this.banco = banco;
    }

    //Métodos
    public void atualizarTokenUsuario(String token) {
        SQLiteDatabase database = banco.get();
        if (statementAtualizarTokenUsuario == null) {
            statementAtualizarTokenUsuario = database.compileStatement("UPDATE USUARIO SET token = ? WHERE id = 1 AND ativo = 1;");
        }
        if (database.inTransaction()) {
            database.endTransaction();
        }
        try {
            database.beginTransaction();
            statementAtualizarTokenUsuario.bindString(1, token);
            statementAtualizarTokenUsuario.executeUpdateDelete();
        }
        catch(Exception e) {
            Crashlytics.logException(e);
            CrashAnalytics.e(TAG, e);
        }
        finally {
            database.setTransactionSuccessful();
            database.endTransaction();
        }
    }

    void limparTabelas() {
        List<String> tabelasParaApagar = new ArrayList<>(32);
        tabelasParaApagar.add("FECHAMENTO_TURMA");
        tabelasParaApagar.add("FECHAMENTO_ALUNO");
        tabelasParaApagar.add("MEDIA_ALUNO");
        tabelasParaApagar.add("TIPO_FECHAMENTO");
        tabelasParaApagar.add("FALTASALUNOS");
        tabelasParaApagar.add("DIASLETIVOS");
        tabelasParaApagar.add("BIMESTRE");
        tabelasParaApagar.add("NOTASALUNO");
        tabelasParaApagar.add("AVALIACOES");
        tabelasParaApagar.add("TOTALFALTASALUNOS");
        tabelasParaApagar.add("AULAS");
        tabelasParaApagar.add("DISCIPLINA");
        tabelasParaApagar.add("TURMASFREQUENCIA");
        tabelasParaApagar.add("ALUNOS");
        tabelasParaApagar.add("TURMAS");
        tabelasParaApagar.add("USUARIO");
        tabelasParaApagar.add("NOVO_HABILIDADE_REGISTRO");
        tabelasParaApagar.add("NOVO_GRUPO");
        tabelasParaApagar.add("NOVO_CURRICULO");
        tabelasParaApagar.add("NOVO_CONTEUDO");
        tabelasParaApagar.add("NOVO_HABILIDADE");
        tabelasParaApagar.add("NOVO_CONTEUDO_HABILIDADE");
        tabelasParaApagar.add("NOVO_CONTEUDO_REGISTRO");
        tabelasParaApagar.add("NOVO_REGISTRO");
        tabelasParaApagar.add("DIASCOMFREQUENCIA");
        tabelasParaApagar.add("CARTEIRINHAS");
        tabelasParaApagar.add("DIASCONFLITO");
        tabelasParaApagar.add("COMUNICADOS");
        tabelasParaApagar.add("CURRICULO_FUNDAMENTAL");
        tabelasParaApagar.add("CONTEUDO_FUNDAMENTAL");
        tabelasParaApagar.add("REGISTRO_AULA_FUNDAMENTAL");
        tabelasParaApagar.add("HABILIDADE_REGISTRO_FUNDAMENTAL");
        apagarTabelas(tabelasParaApagar);
    }

    private void apagarTabelas(List<String> tabelasParaApagar) {
        SQLiteDatabase database = banco.get();
        database.beginTransaction();
        for(int i = 0; i < 32; i++) {
            try {
                database.delete(tabelasParaApagar.get(i), null, null);
            }
            catch (Exception e) {
                Crashlytics.logException(e);
                CrashAnalytics.e(TAG, e);
            }
        }
        database.setTransactionSuccessful();
        database.endTransaction();
        tabelasParaApagar.clear();
    }
}