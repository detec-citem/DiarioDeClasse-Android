package br.gov.sp.educacao.sed.mobile.RegistroDeAula;

import android.content.ContentValues;

import android.database.Cursor;

import android.database.sqlite.SQLiteStatement;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;

public class RegistroDBcrud {

    private String TAG = RegistroDBcrud.class.getSimpleName();

    private Banco banco;

    private ContentValues valuesRegistro;

    private ContentValues valuesConteudo;

    private ContentValues valuesHabilidade;

    private Registro registroEncontrado;

    @SuppressWarnings("FieldCanBeLocal")
    private String queryDeletarHabilidades;

    private SQLiteStatement statementDeletarHabilidades;

    @SuppressWarnings("FieldCanBeLocal")
    private String queryDeletarConteudoRegistro;

    private SQLiteStatement statementDeletarConteudoRegistro;

    @SuppressWarnings("FieldCanBeLocal")
    private String queryDeletarRegistro;

    private SQLiteStatement statementDeletarRegistro;

    @SuppressWarnings("FieldCanBeLocal")
    private String queryInsertRegistro;

    private SQLiteStatement statementInsertRegistro;

    @SuppressWarnings("FieldCanBeLocal")
    private String queryInsertConteudoRegistro;

    private SQLiteStatement statementInsertConteudoRegistro;

    @SuppressWarnings("FieldCanBeLocal")
    private String queryInsertHabilidadeRegistro;

    private SQLiteStatement statementInsertHabilidadeRegistro;

    @SuppressWarnings("FieldCanBeLocal")
    private String queryTotalDeRegistros;

    private SQLiteStatement statementTotalDeRegistros;

    @SuppressWarnings("FieldCanBeLocal")
    private List<Integer> habilidadesDeletar;

    private int novoId;

    private int countConteudo;

    private int countHabilidades;

    public RegistroDBcrud(Banco banco) {

        this.banco = banco;

        valuesRegistro = new ContentValues();

        valuesConteudo = new ContentValues();

        valuesHabilidade = new ContentValues();

        queryDeletarHabilidades =

                "DELETE FROM NOVO_HABILIDADE_REGISTRO WHERE habilidade = ? AND codNovoConteudo = ? AND codNovoRegistro = ?;";

        statementDeletarHabilidades = banco.get().compileStatement(queryDeletarHabilidades);

        queryDeletarConteudoRegistro =

                "DELETE FROM NOVO_CONTEUDO_REGISTRO WHERE codigoConteudo = ? AND codNovoRegistro = ?;";

        statementDeletarConteudoRegistro = banco.get().compileStatement(queryDeletarConteudoRegistro);

        queryDeletarRegistro =

                "DELETE FROM NOVO_REGISTRO WHERE codNovoRegistro = ?;";

        statementDeletarRegistro = banco.get().compileStatement(queryDeletarRegistro);

        queryInsertRegistro =

                "INSERT INTO NOVO_REGISTRO (codNovoRegistro, bimestre, codDisciplina, codTurma, ocorrencias," +
                        " observacoes, codGrupoCurriculo, dataCriacao, horarios) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        statementInsertRegistro = banco.get().compileStatement(queryInsertRegistro);

        queryInsertConteudoRegistro =

                "INSERT INTO NOVO_CONTEUDO_REGISTRO (codNovoRegistro, codigoConteudo) VALUES (?, ?);";

        statementInsertConteudoRegistro = banco.get().compileStatement(queryInsertConteudoRegistro);

        queryInsertHabilidadeRegistro =

                "INSERT INTO NOVO_HABILIDADE_REGISTRO (codNovoConteudo, habilidade, codNovoRegistro) VALUES (?, ?, ?);";

        statementInsertHabilidadeRegistro = banco.get().compileStatement(queryInsertHabilidadeRegistro);

        queryTotalDeRegistros =

                "SELECT COUNT(codNovoRegistro) FROM NOVO_REGISTRO;";

        statementTotalDeRegistros = banco.get().compileStatement(queryTotalDeRegistros);

        novoId = 0;

        countConteudo = 0;

        countHabilidades = 0;
    }

    public void salvarRegistroBanco(Registro registro) {

        Registro registroAntigo = existeRegistro(registro.getDataCriacao(), Integer.parseInt(registro.getCodigoTurma()));

        if(registroAntigo == null) {

            try {

                novoId = (-1) * getCountRegistro();

                if (registro.getCodNovoRegistro() == 0) {

                    registro.setCodNovoRegistro(novoId - 1);
                }

                for (int o = 0; o < registro.getConteudos().size(); o++) {

                    habilidadesDeletar = registro.getConteudos().get(o).getCodigosHabilidades();

                    for (int z = 0; z < habilidadesDeletar.size(); z++) {

                        statementDeletarHabilidades.bindLong(1, habilidadesDeletar.get(z));
                        statementDeletarHabilidades.bindLong(2, registro.getConteudos().get(o).getCodigoConteudo());
                        statementDeletarHabilidades.bindLong(3, registro.getCodNovoRegistro());

                        statementDeletarHabilidades.executeUpdateDelete();
                    }

                    statementDeletarConteudoRegistro.bindLong(1, registro.getConteudos().get(o).getCodigoConteudo());
                    statementDeletarConteudoRegistro.bindLong(2, registro.getCodNovoRegistro());

                    statementDeletarConteudoRegistro.executeUpdateDelete();
                }

                statementDeletarRegistro.bindLong(1, registro.getCodNovoRegistro());

                statementDeletarRegistro.executeUpdateDelete();

                statementInsertRegistro.bindLong(  1, registro.getCodNovoRegistro());
                statementInsertRegistro.bindLong(  2, registro.getBimestre());
                statementInsertRegistro.bindLong(  3, Integer.valueOf(registro.getCodigoDisciplina()));
                statementInsertRegistro.bindLong(  4, Integer.valueOf(registro.getCodigoTurma()));
                statementInsertRegistro.bindString(5, (registro.getOcorrencias() != null ? registro.getOcorrencias() : "" ));
                statementInsertRegistro.bindString(6, (registro.getObservacoes() != null ? registro.getObservacoes() : "" ));
                statementInsertRegistro.bindLong(  7, Integer.valueOf(registro.getCodigoGrupoCurriculo()));
                statementInsertRegistro.bindString(8, registro.getDataCriacao());
                statementInsertRegistro.bindString(9, registro.getHorariosInsert());

                statementInsertRegistro.executeInsert();

                countConteudo = registro.getConteudos().size();

                for(int i = 0; i < countConteudo; i++) {

                    statementInsertConteudoRegistro.bindLong(1, registro.getCodNovoRegistro());
                    statementInsertConteudoRegistro.bindLong(2, registro.getConteudos().get(i).getCodigoConteudo());

                    statementInsertConteudoRegistro.executeInsert();

                    countHabilidades = registro.getConteudos().get(i).checarHabilidades();

                    for (int o = 0; o < countHabilidades; o++) {

                        statementInsertHabilidadeRegistro.bindLong(1, registro.getConteudos().get(i).getCodigoConteudo());
                        statementInsertHabilidadeRegistro.bindLong(2, registro.getConteudos().get(i).getHabSelecionadas(o));
                        statementInsertHabilidadeRegistro.bindLong(3, registro.getCodNovoRegistro());

                        statementInsertHabilidadeRegistro.executeInsert();
                    }
                }
            }
            catch(Exception e) {

                e.printStackTrace();
            }
            finally {

                valuesRegistro.clear();

                valuesConteudo.clear();

                valuesHabilidade.clear();
            }
        }
        else {

            atualizarRegistroBanco(registro, registroAntigo);
        }
    }

    private int getCountRegistro() {

        int totalDeRegistros = (int) statementTotalDeRegistros.simpleQueryForLong();

        return totalDeRegistros;

        /*String[] colunas = new String[]{"codNovoRegistro"};

        Cursor cursor = banco.get().query("NOVO_REGISTRO", colunas,
                        null, null, null, null, null);

        if(cursor.moveToFirst()) {

            cursor.close();

            return cursor.getCount();
        }
        else {

            cursor.close();

            return 0;
        }*/
    }

    public void atualizarRegistroBanco(Registro registroNovo, Registro registroAntigo) {

        try {

            for (int o = 0; o < registroAntigo.getConteudos().size(); o++) {

                List<Integer> habilidadesDeletar = registroAntigo.getConteudos()
                        .get(o)
                        .getCodigosHabilidades();

                for (int z = 0; z < habilidadesDeletar.size(); z++) {

                    banco.get()
                            .delete("NOVO_HABILIDADE_REGISTRO",
                                    "habilidade = ? " +
                                            "AND codNovoRegistro = ?",
                                    new String[]{String.valueOf(habilidadesDeletar.get(z)),
                                            String.valueOf(registroAntigo.getCodNovoRegistro())});
                }
                banco.get()
                        .delete("NOVO_CONTEUDO_REGISTRO",
                                "codigoConteudo = ?",
                                new String[]{String.valueOf(registroAntigo.getConteudos()
                                        .get(o)
                                        .getCodigoConteudo())});
            }
            banco.get()
                    .delete("NOVO_REGISTRO",
                            "codNovoRegistro = ?",
                            new String[]{String.valueOf(registroAntigo.getCodNovoRegistro())});
        }
        catch (Exception e) {

            CrashAnalytics.e(TAG, e);
        }

        salvarRegistroBanco(registroNovo);
    }

    @Nullable
    public Registro existeRegistro(String data, int codigoTurma) {

        String[] colunas = new String[]{"codNovoRegistro", "bimestre",
                "codDisciplina", "codTurma",
                "ocorrencias", "observacoes",
                "codGrupoCurriculo", "dataCriacao"};

        Cursor cursor = banco.get().query("NOVO_REGISTRO", colunas, "dataCriacao = ? " + "AND codTurma = " + codigoTurma,
                        new String[]{data}, null, null, null);

        if (cursor.moveToFirst()) {

            registroEncontrado = new Registro();

            try {

                registroEncontrado.setCodNovoRegistro(cursor.getInt(0));
                registroEncontrado.setBimestre(cursor.getInt(1));
                registroEncontrado.setCodigoDisciplina(cursor.getString(2));
                registroEncontrado.setCodigoTurma(cursor.getString(3));
                registroEncontrado.setOcorrencias(cursor.getString(4));
                registroEncontrado.setObservacoes(cursor.getString(5));
                registroEncontrado.setCodigoGrupoCurriculo(cursor.getString(6));
                registroEncontrado.setDataCriacao(cursor.getString(7));
                registroEncontrado.setConteudos(buscarConteudos(registroEncontrado.getCodNovoRegistro()));

                cursor.close();
            }
            catch (Exception e) {

                e.printStackTrace();

                cursor.close();
            }
            finally {

                return registroEncontrado;
            }
        }
        else {

            if(cursor != null) {

                cursor.close();
            }
            return null;
        }
    }

    ArrayList<ObjetoConteudo> buscarConteudos(int codNovoRegistro) {

        ArrayList<ObjetoConteudo> conteudos = new ArrayList<>();

        String[] colunas = new String[]{"codigoConteudo"};

        Cursor cursor = banco.get()
                .query("NOVO_CONTEUDO_REGISTRO",
                        colunas,
                        "codNovoRegistro = ?",
                        new String[]{"" + codNovoRegistro},
                        null, null, null);
        if (cursor.getCount() > 0) {

            cursor.moveToFirst();

            do {

                ObjetoConteudo c = new ObjetoConteudo();

                c.setCodigoConteudo(cursor.getInt(0));

                c.setCodigosHabilidades(buscarHabilidades(cursor.getInt(0),
                        codNovoRegistro));
                conteudos.add(c);
            }

            while (cursor.moveToNext());
        }
        cursor.close();

        return conteudos;
    }

    private ArrayList<Integer> buscarHabilidades(int codNovoConteudo, int codNovoRegistro) {

        ArrayList<Integer> habilidades = new ArrayList<>();

        String[] colunas = new String[]{"habilidade"};

        Cursor cursor = banco.get()
                .query("NOVO_HABILIDADE_REGISTRO",
                        colunas,
                        "codNovoConteudo = ? " +
                                "AND codNovoRegistro = ?",
                        new String[]{String.valueOf(codNovoConteudo),
                                String.valueOf(codNovoRegistro)},
                        null, null, null);

        if (cursor.getCount() > 0) {

            cursor.moveToFirst();

            do {

                int h = 0;

                h = cursor.getInt(0);

                habilidades.add(h);
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return habilidades;
    }
}
