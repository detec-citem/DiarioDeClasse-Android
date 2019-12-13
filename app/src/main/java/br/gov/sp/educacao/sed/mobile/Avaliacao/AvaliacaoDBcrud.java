package br.gov.sp.educacao.sed.mobile.Avaliacao;

import java.util.List;
import java.util.ArrayList;

import org.json.JSONObject;

import android.database.Cursor;

import android.content.ContentValues;
import android.database.sqlite.SQLiteStatement;

import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;

public class AvaliacaoDBcrud {

    private Banco banco;

    private final String TAG = AvaliacaoDBcrud.class.getSimpleName();

    public AvaliacaoDBcrud(Banco banco) {

        this.banco = banco;
    }
    ///OK
    void insertAvaliacao(Avaliacao avaliacao) {

        /*("CREATE TABLE IF NOT EXISTS CARTEIRINHAS(" +
                " id INTEGER PRIMARY KEY NOT NULL, " +
                " nomeUsuario VARCHAR(255), " +
                " cargoUsuario VARCHAR(255), " +
                " rgUsuario VARCHAR(50), " +
                " rsUsuario VARCHAR(50), " +
                " fotoUsuario VARCHAR(255), " +
                " qrCodeUsuario VARCHAR(255))");*/

        ContentValues insertValues = new ContentValues();

        insertValues.put("nome", avaliacao.getNome());
        insertValues.put("data", avaliacao.getData());
        insertValues.put("turma_id", avaliacao.getTurmaId());
        insertValues.put("bimestre", avaliacao.getBimestre());
        insertValues.put("mobileId", avaliacao.getMobileId());
        insertValues.put("codigoTurma", avaliacao.getCodTurma());
        insertValues.put("codigoAvaliacao", avaliacao.getCodigo());
        insertValues.put("valeNota", avaliacao.isValeNota() ? 1 : 0);
        insertValues.put("dataCadastro", avaliacao.getDataCadastro());
        insertValues.put("disciplina_id", avaliacao.getDisciplinaId());
        insertValues.put("codigoDisciplina", avaliacao.getCodDisciplina());
        insertValues.put("codigoTipoAtividade", avaliacao.getTipoAtividade());

        try {

            banco.get().insert("AVALIACOES", null, insertValues);
        }
        catch (Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            insertValues.clear();
        }
    }
    ///OK
    public void deleteAvaliacao(int codigoAvaliacao) {

        String queryDeletarAvaliacao =

                "DELETE FROM AVALIACOES WHERE id = ?;";

        String queryDeletarNotasAvaliacao =

                "DELETE FROM NOTASALUNO WHERE avaliacao_id = ?;";


        SQLiteStatement statementDeletarAvaliacao = banco.get().compileStatement(queryDeletarAvaliacao);

        SQLiteStatement statementDeletarNotasAvaliacao = banco.get().compileStatement(queryDeletarNotasAvaliacao);

        try {

            banco.get().beginTransaction();

            List<Integer> listaIdAvaliacoes = getIdAvaliacoes(codigoAvaliacao);

            if(listaIdAvaliacoes.size() > 0) {

                for(int i = 0; i < listaIdAvaliacoes.size(); i++) {

                    statementDeletarNotasAvaliacao.bindLong(1, listaIdAvaliacoes.get(i));

                    statementDeletarNotasAvaliacao.executeUpdateDelete();

                    statementDeletarAvaliacao.bindLong(1, listaIdAvaliacoes.get(i));

                    statementDeletarAvaliacao.executeUpdateDelete();
                }
            }
        }
        catch (Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            statementDeletarAvaliacao.clearBindings();

            statementDeletarAvaliacao.close();

            statementDeletarNotasAvaliacao.clearBindings();

            statementDeletarNotasAvaliacao.close();

            banco.get().setTransactionSuccessful();

            banco.get().endTransaction();
        }
    }
    ///OK
    private List<Integer> getIdAvaliacoes(int codigoAvaliacao) {

        List<Integer> listaIdAvaliacoes = new ArrayList<>();

        Cursor cursor = null;

        try {

            cursor = banco.get().rawQuery(

                    "SELECT id FROM AVALIACOES WHERE codigoAvaliacao = " + codigoAvaliacao + " AND dataServidor = 'deletar'", null
            );

            if(cursor.getCount() > 0) {

                int idAvaliacao;

                while(cursor.moveToNext()) {

                    idAvaliacao = cursor.getInt(cursor.getColumnIndex("id"));

                    listaIdAvaliacoes.add(idAvaliacao);
                }
            }
        }
        catch(Exception e) {

            e.printStackTrace();
        }
        finally {

            if(cursor != null) {

                cursor.close();
            }
        }
        return  listaIdAvaliacoes;
    }
    ///OK
    void editarAvaliacao(Avaliacao avaliacao, boolean editarNaSed) {

        ContentValues insertValues = new ContentValues();

        insertValues.put("codigoAvaliacao", avaliacao.getCodigo());
        insertValues.put("dataServidor", avaliacao.getDataServidor());

        if(editarNaSed) {

            insertValues.put("nome", avaliacao.getNome());
            insertValues.put("data", avaliacao.getData());
            insertValues.put("bimestre", avaliacao.getBimestre());
            insertValues.put("mobileId", avaliacao.getMobileId());
            insertValues.put("codigoTurma", avaliacao.getCodTurma());
            insertValues.put("valeNota", avaliacao.isValeNota() ? 1 : 0);
            insertValues.put("dataCadastro", avaliacao.getDataCadastro());
            insertValues.put("codigoDisciplina", avaliacao.getCodDisciplina());
            insertValues.put("codigoTipoAtividade", avaliacao.getTipoAtividade());
        }

        try {

            banco.get().update("AVALIACOES", insertValues, "id = " + avaliacao.getId(), null);
        }
        catch(Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            insertValues.clear();
        }
    }
    ///OK
    public void atualizarAvaliacaoSincronizada(List<JSONObject> lista) {

        try {

            banco.get().beginTransaction();

            for (int i = 0; i < lista.size(); i++) {

                Avaliacao avaliacao = new Avaliacao();

                avaliacao.setId(lista.get(i).getInt("Id"));
                avaliacao.setCodigo(lista.get(i).getInt("Codigo"));
                avaliacao.setMobileId(lista.get(i).getInt("MobileId"));
                avaliacao.setDataServidor(lista.get(i).getString("DataServidor"));

                editarAvaliacao(avaliacao, false);
            }
        }
        catch (Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            banco.get().setTransactionSuccessful();

            banco.get().endTransaction();
        }
    }
}
