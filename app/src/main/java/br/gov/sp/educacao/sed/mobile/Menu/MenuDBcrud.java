package br.gov.sp.educacao.sed.mobile.Menu;

import java.util.List;
import java.util.ArrayList;

import android.database.sqlite.SQLiteStatement;

import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;

public class MenuDBcrud {

    private Banco banco;

    private String queryAtualizarTokenUsuario;

    private SQLiteStatement statementAtualizarTokenUsuario;

    private final String TAG = MenuDBcrud.class.getSimpleName();

    public MenuDBcrud(Banco banco) {

        this.banco = banco;

        queryAtualizarTokenUsuario =

                "UPDATE USUARIO SET token = ? WHERE id = 1 AND ativo = 1;";


    }

    void limparTabelas() {

        List<String> tabelasParaApagar = new ArrayList<>(25);

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

        apagarTabelas(tabelasParaApagar);
    }

    public void atualizarTokenUsuario(String token) {

        statementAtualizarTokenUsuario = banco.get().compileStatement(queryAtualizarTokenUsuario);

        try {

            banco.get().beginTransaction();

            statementAtualizarTokenUsuario.bindString(1, token);

            statementAtualizarTokenUsuario.executeUpdateDelete();
        }
        catch(Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            banco.get().setTransactionSuccessful();

            banco.get().endTransaction();

            statementAtualizarTokenUsuario.clearBindings();

            statementAtualizarTokenUsuario.close();

            statementAtualizarTokenUsuario = null;
        }
    }

    private void apagarTabelas(List<String> tabelasParaApagar) {

        try {

            banco.get().beginTransaction();

            for(int i = 0; i < tabelasParaApagar.size(); i++) {

                banco.get().delete(tabelasParaApagar.get(i), null, null);
            }
        }
        catch (Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            banco.get().setTransactionSuccessful();

            banco.get().endTransaction();

            tabelasParaApagar.clear();

            tabelasParaApagar = null;
        }
    }
}
