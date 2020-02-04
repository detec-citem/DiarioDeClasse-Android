package br.gov.sp.educacao.sed.mobile.Menu;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import br.gov.sp.educacao.sed.mobile.Fechamento.FechamentoData;
import br.gov.sp.educacao.sed.mobile.Login.UsuarioTO;
import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;

public class MenuDBgetters {

    private Banco banco;

    private FechamentoData fechamentoData;

    private String queryAvaliacoesParaSincronizar;

    private String queryFechamentosParaSincronizar;

    private String queryFrequenciasParaSincronizar;

    private SQLiteStatement statementAvaliacoesParaSincronizar;

    private SQLiteStatement statementFrequenciaParaSincronizar;

    private SQLiteStatement statementFechamentosParaSincronizar;

    private final String TAG = MenuDBgetters.class.getSimpleName();

    public MenuDBgetters(Banco banco) {

        this.banco = banco;

        this.fechamentoData = new FechamentoData();

        queryAvaliacoesParaSincronizar =

                "SELECT COUNT(*) FROM AVALIACOES AS avaliacoes, NOTASALUNO AS notasAluno " +
                        "WHERE avaliacoes.id = notasAluno.avaliacao_id AND notasAluno.dataServidor IS NULL AND notasAluno.nota IS NOT '11.00';";

        statementAvaliacoesParaSincronizar = banco.get().compileStatement(queryAvaliacoesParaSincronizar);

        queryFechamentosParaSincronizar =

                "SELECT COUNT(*) FROM FECHAMENTO_TURMA WHERE codigoTipoFechamento = " +
                        "(SELECT (CASE WHEN ((date('now') >= inicio) AND (date('now') <= fim )) " +
                        "THEN codigoTipoFechamento ELSE 0 END) codigoTipoFechamento FROM TIPO_FECHAMENTO ORDER BY codigoTipoFechamento DESC LIMIT 1) " +
                        "AND dataServidor = '';";

        statementFechamentosParaSincronizar = banco.get().compileStatement(queryFechamentosParaSincronizar);

        queryFrequenciasParaSincronizar =

                "SELECT COUNT(*) " +
                        "FROM FALTASALUNOS AS fa, ALUNOS AS al, DIASLETIVOS AS di, AULAS AS au, DISCIPLINA AS dc, TURMASFREQUENCIA AS tf " +
                        "WHERE al.id = fa.aluno_id AND di.id = fa.diasLetivos_id AND au.id = fa.aula_id AND dc.id = au.disciplina_id " +
                        "AND tf.id = dc.turmasFrequencia_id AND fa.dataServidor IS NULL;";

        statementFrequenciaParaSincronizar = banco.get().compileStatement(queryFrequenciasParaSincronizar);
    }

    boolean temDadosParaSincronizar() {

        banco.get().beginTransaction();

        long count = statementAvaliacoesParaSincronizar.simpleQueryForLong();

        boolean dadosNaoSincronizados = false;

        if(count > 0) {

            dadosNaoSincronizados = true;
        }

        count = statementFechamentosParaSincronizar.simpleQueryForLong();

        if(count > 0) {

            dadosNaoSincronizados = true;
        }

        count = statementFrequenciaParaSincronizar.simpleQueryForLong();

        if(count > 0) {

            dadosNaoSincronizados = true;
        }

        banco.get().setTransactionSuccessful();

        banco.get().endTransaction();

        return dadosNaoSincronizados;
    }

    FechamentoData getDataFechamento() {

        Cursor cursor = null;

        banco.get().beginTransaction();

        try {

            cursor = banco.get().rawQuery(

                    "SELECT codigoTipoFechamento, inicio, fim," +
                            "CASE " +
                            "WHEN ((date('now') >= inicio) AND (date('now') <= fim )) " +
                            "THEN 'aberto' " +
                            "ELSE 'fechado' " +
                            "END AS status " +
                            "FROM TIPO_FECHAMENTO WHERE date('now') <= fim", null
            );

            cursor.moveToFirst();

            fechamentoData.setCodigoTipoFechamento(String.valueOf(cursor.getInt(0)));

            fechamentoData.setInicio(cursor.getString(1));

            fechamentoData.setFim(cursor.getString(2));

            fechamentoData.setStatus(cursor.getString(3));
        }
        catch(Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            if(cursor != null) {

                cursor.close();

                cursor = null;
            }

            banco.get().setTransactionSuccessful();

            banco.get().endTransaction();
        }
        return fechamentoData;
    }

    public UsuarioTO getUsuarioAtivo() {

        String getUsuarioAtivo =

                "SELECT id, usuario, senha, nome, cpf, rg, digitoRG, dataUltimoAcesso, ativo, token FROM USUARIO WHERE ativo = 1;";

        UsuarioTO usuarioTO = null;

        Cursor cursor = null;

        try {

            banco.get().beginTransaction();

            cursor = banco.get().rawQuery(getUsuarioAtivo, null);

            if(cursor.moveToFirst()) {

                usuarioTO = new UsuarioTO(cursor);
            }
        }
        catch(Exception e) {

            e.printStackTrace();

            CrashAnalytics.e(TAG, e);
        }
        finally {

            if(cursor != null) {

                cursor.close();

                cursor = null;
            }

            banco.get().setTransactionSuccessful();

            banco.get().endTransaction();
        }
        return usuarioTO;
    }
}
