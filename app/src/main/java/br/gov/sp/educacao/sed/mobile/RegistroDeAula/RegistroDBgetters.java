package br.gov.sp.educacao.sed.mobile.RegistroDeAula;

import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.gov.sp.educacao.sed.mobile.Escola.Aula;
import br.gov.sp.educacao.sed.mobile.Escola.Bimestre;
import br.gov.sp.educacao.sed.mobile.Escola.DiasLetivos;
import br.gov.sp.educacao.sed.mobile.Escola.Disciplina;

import br.gov.sp.educacao.sed.mobile.Fechamento.FechamentoData;

import br.gov.sp.educacao.sed.mobile.Login.UsuarioTO;

import br.gov.sp.educacao.sed.mobile.Turmas.TurmasFrequencia;

import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;

public class RegistroDBgetters {

    private static final String TAG = RegistroDBgetters.class.getSimpleName();

    private Banco banco;

    private RegistroDBcrud registroDBcrud;

    public RegistroDBgetters(Banco banco) {

        this.banco = banco;

        this.registroDBcrud = new RegistroDBcrud(banco);
    }

    FechamentoData getFechamentoAberto() {

        Cursor cursor = null;

        FechamentoData fechamentoData = new FechamentoData();

        try {

            //banco.open();

            cursor = banco.get().rawQuery("SELECT " +
                            "codigoTipoFechamento, " +
                            "nome, " +
                            "ano, " +
                            "inicio, " +
                            "fim " +
                            "FROM TIPO_FECHAMENTO " +
                            "WHERE date('now') >= inicio " +
                            "AND date('now') <= fim;",
                    null);
            if(cursor != null
                    && cursor.moveToNext()) {

                fechamentoData.setInicio(cursor.getString(cursor.getColumnIndex("inicio")));
                fechamentoData.setFim(cursor.getString(cursor.getColumnIndex("fim")));
                fechamentoData.setCodigoTipoFechamento(cursor.getString(cursor.getColumnIndex("codigoTipoFechamento")));
                fechamentoData.setAno(cursor.getString(cursor.getColumnIndex("ano")));
                fechamentoData.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            }
            else {

                fechamentoData = null;
            }
        }
        catch (Exception e) {

            CrashAnalytics.e(TAG, e);

            fechamentoData = null;
        }
        finally {

            if (cursor != null) {

                cursor.close();
            }
            //banco.close();
        }
        return fechamentoData;
    }

    Bimestre getBimestre(TurmasFrequencia turmasFrequencia) {

        Bimestre bimestre = new Bimestre();

        Cursor cursor = null;

        try {

            cursor = banco.get()
                    .rawQuery("SELECT * " +
                                    "FROM BIMESTRE " +
                                    "WHERE turmasFrequencia_id = " + turmasFrequencia.getId() +
                                    " AND bimestreAtual = 1;",
                            null);

            if(cursor.moveToNext()) {

                bimestre.setId(cursor.getInt(cursor.getColumnIndex("id")));

                bimestre.setNumero(cursor.getInt(cursor.getColumnIndex("numero")));

                bimestre.setInicio(cursor.getString(cursor.getColumnIndex("inicioBimestre")));

                bimestre.setFim(cursor.getString(cursor.getColumnIndex("fimBimestre")));
            }
        }
        catch(Exception e) {

            e.printStackTrace();

            CrashAnalytics.e(TAG, e);
        }
        finally {

            if(cursor != null) {

                cursor.close();
            }
        }
        return bimestre;
    }

    Bimestre getBimestreAnterior(int turmasFrequencia_id) {

        Cursor cursor = null;

        cursor = banco.get().rawQuery("SELECT numero," +
                        " inicioBimestre," +
                        " fimBimestre," +
                        " turmasFrequencia_id" +
                        " FROM BIMESTRE" +
                        " WHERE numero = ((SELECT numero FROM BIMESTRE WHERE turmasFrequencia_id = ? " +
                        " AND bimestreAtual = 1) - 1) AND turmasFrequencia_id = ? ;",
                new String [] {String.valueOf(turmasFrequencia_id),
                        String.valueOf(turmasFrequencia_id)});

        cursor.moveToFirst();

        Bimestre bimestre = new Bimestre();

        bimestre.setNumero(cursor.getInt(0));
        bimestre.setInicio(cursor.getString(1));
        bimestre.setFim(cursor.getString(2));
        bimestre.setId(cursor.getInt(3));

        cursor.close();

        return bimestre;
    }

    ArrayList<DiasLetivos> getDiasLetivos(Bimestre bimestre,
                                          List<Integer> listaDiaSemana) {

        ArrayList<DiasLetivos> listaDiasLetivos = new ArrayList<>();

        StringBuffer query = new StringBuffer();

        Cursor cursor2 = banco.get().rawQuery("SELECT turmasFrequencia_id " +
                        "FROM BIMESTRE " +
                        "WHERE id = " + bimestre.getId(),
                null);

        int turma_id = 0;

        if(cursor2 != null
                && cursor2.moveToNext()) {

            turma_id = cursor2.getInt(cursor2.getColumnIndex("turmasFrequencia_id"));
        }

        cursor2.close();

        cursor2 = banco.get().rawQuery("SELECT id FROM BIMESTRE WHERE numero = " +
                        (bimestre.getNumero() == 1 ? 4 : bimestre.getNumero() - 1) +
                        " AND turmasFrequencia_id = " +
                        turma_id + ";",
                null);

        int bimestre_id = 0;

        if(cursor2 != null
                && cursor2.moveToNext()) {

            bimestre_id = cursor2.getInt(cursor2.getColumnIndex("id"));
        }

        query.append(" SELECT * FROM DIASLETIVOS ");

        query.append(" WHERE bimestre_id = ").append(bimestre.getId());

        query.append(" OR bimestre_id = ").append(bimestre_id);

        cursor2.close();

        Cursor cursor = null;

        try {

            cursor = banco.get()
                    .rawQuery(query.toString(),
                            null);

            while(cursor.moveToNext()) {

                String input_date = cursor.getString(cursor.getColumnIndex("dataAula"));

                SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");

                Date data = format1.parse(input_date);

                Calendar c = Calendar.getInstance();

                c.setTime(data);

                int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;

                if(listaDiaSemana.contains(dayOfWeek)) {

                    final DiasLetivos diasLetivos = new DiasLetivos();

                    diasLetivos.setId(cursor.getInt(cursor.getColumnIndex("id")));

                    diasLetivos.setDataAula(cursor.getString(cursor.getColumnIndex("dataAula")));

                    listaDiasLetivos.add(diasLetivos);
                }
            }
        }
        catch (Exception e) {

            e.printStackTrace();
        }
        finally {

            if (cursor != null) {

                cursor.close();
            }
        }
        return listaDiasLetivos;
    }

    ArrayList<String> buscarDataRegistros(int id) {

        ArrayList<String> datas = new ArrayList<>();

        String[] cod = new String[]{String.valueOf(id)};

        Cursor cursor = banco.get()
                .rawQuery("SELECT dataCriacao " +
                                "FROM NOVO_REGISTRO " +
                                "WHERE codTurma = ?;",
                        cod);
        if (cursor.getCount() > 0) {

            cursor.moveToFirst();

            do {

                if(!datas.contains(cursor.getString(0))) {

                    datas.add(cursor.getString(0));
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return datas;
    }

    Habilidade buscarNovoHabilidade(int codNovoConteudo,
                                    int codHabilidade) {

        String[] colunas = new String[]{"codigoHabilidade", "codigoConteudo", "descricao"};

        Cursor cursor = null;

        Habilidade habilidade = new Habilidade();

        try {

            cursor = banco.get()
                    .query("NOVO_HABILIDADE",
                            colunas,
                            "codigoConteudo = ? " +
                                    "AND codigoHabilidade = ?",
                            new String[]{String.valueOf(codNovoConteudo),
                                    String.valueOf(codHabilidade)},
                            null, null, null);

            if (cursor.moveToFirst()) {

                habilidade.setCodigo(cursor.getInt(0));
                habilidade.setCodigoConteudo(cursor.getInt(1));
                habilidade.setDescricao(cursor.getString(2));
            }
        }
        catch (Exception e) {


        }
        finally {

            if(cursor != null) {

                cursor.close();
            }
        }
        return habilidade;
    }

    GrupoCurriculoContHab getGrupoCurriculoContHab(int serie, int codigoDisciplina, int bimestre) {

        GrupoCurriculoContHab gcch = new GrupoCurriculoContHab();

        String[] colunas = new String[] {

                "codigo", "anoLetivo", "codigoTipoEnsino", "serie", "codigoDisciplina"
        };

        Cursor cursor = banco.get().query(

                "NOVO_GRUPO", colunas, "serie = ? " + "AND codigoDisciplina = ?",

                        new String[] {

                                String.valueOf(serie), String.valueOf(codigoDisciplina)

                        },null, null, null);

        if(cursor.moveToFirst()) {

            gcch.setCodigoGrupo(cursor.getInt(0));
            gcch.setAnoLetivo(cursor.getInt(1));
            gcch.setCodigoTipoEnsino(cursor.getInt(2));
            gcch.setSerie(cursor.getInt(3));
            gcch.setCodigoDisciplina(cursor.getInt(4));

            String[] colunas2 = new String[] {"codigo", "codigoGrupo", "bimestre"};

            Cursor cursor2 = banco.get().query(

                    "NOVO_CURRICULO", colunas2, "codigoGrupo = ? " + "AND bimestre = ?",

                            new String[] {

                                    String.valueOf(gcch.getCodigoGrupo()), String.valueOf(bimestre)

                            }, null, null, null);

            if(cursor2.getCount() == 1) {

                Curriculo curriculo = new Curriculo();

                cursor2.moveToFirst();

                curriculo.setCodigoCurriculo(cursor2.getInt(0));
                curriculo.setCodigoGrupo(cursor2.getInt(1));
                curriculo.setBimestre(cursor2.getInt(2));

                gcch.setCurriculo(curriculo);
            }
            else {

                cursor2.close();

                List<Conteudo> conteudos = new ArrayList<>();

                gcch.setConteudos(conteudos);

                return gcch;
            }

            String[] colunas3 = new String[] {"codigoConteudo", "codigoCurriculo", "descricao"};

            Cursor cursor3 = banco.get().query(

                    "NOVO_CONTEUDO", colunas3, "codigoCurriculo = ?",

                            new String[] {

                                    String.valueOf(gcch.getCurriculo().getCodigoCurriculo())

                            }, null, null, null);

            List<Conteudo> conteudos = new ArrayList<>();

            List<Habilidade> habilidades = new ArrayList<>();

            if(cursor3.getCount() > 0) {

                cursor3.moveToFirst();

                do {

                    Conteudo conteudo = new Conteudo();

                    conteudo.setCodigo(cursor3.getInt(0));
                    conteudo.setCodigoCurriculo(cursor3.getInt(1));
                    conteudo.setDescricao(cursor3.getString(2));

                    conteudos.add(conteudo);

                    String[] colunas4 = new String[] {"codigoHabilidade", "codigoConteudo", "descricao"};

                    Cursor cursor4 = banco.get().query(

                            "NOVO_HABILIDADE", colunas4, "codigoConteudo = ?",

                                    new String[] {

                                            String.valueOf(conteudo.getCodigo())

                                    }, null, null, null);

                    if(cursor4.getCount() > 0) {

                        cursor4.moveToFirst();

                        do {

                            Habilidade habilidade = new Habilidade();

                            habilidade.setCodigo(cursor4.getInt(0));
                            habilidade.setCodigoConteudo(cursor4.getInt(1));
                            habilidade.setDescricao(cursor4.getString(2));

                            habilidades.add(habilidade);

                        }
                        while (cursor4.moveToNext());
                    }
                    else {

                        cursor.close();

                        return null;
                    }
                }
                while (cursor3.moveToNext());

                gcch.setConteudos(conteudos);

                gcch.setHabilidades(habilidades);
            }
            else {

                cursor.close();

                cursor2.close();

                cursor3.close();

                return null;
            }
        }
        cursor.close();

        return gcch;
    }

    int getBimestreAtual(int codTurma) {

        String[] colunas = new String[]{"id"};

        int id = -1;

        //banco.open();

        Cursor cursor = banco.get()
                .query("TURMASFREQUENCIA",
                        colunas,
                        "codigoTurma = ?",
                        new String[]{String.valueOf(codTurma)},
                        null, null, null);
        if(cursor.getCount() > 0) {

            cursor.moveToFirst();

            id = cursor.getInt(0);
        }
        cursor.close();

        int bimestre = 1;

        colunas = new String[]{"numero"};

        cursor = banco.get()
                .query("BIMESTRE",
                        colunas,
                        "turmasFrequencia_id = ? AND bimestreAtual = 1",
                        new String[]{String.valueOf(id)},
                        null, null, null);

        if(cursor.getCount() > 0) {

            cursor.moveToFirst();

            bimestre = cursor.getInt(0);
        }
        cursor.close();

        //banco.close();

        return bimestre;
    }

    Registro buscarRegistrosDataTurma(String dataCriacao,
                                      String turma,
                                      int bimestre) {

        String[] colunas = new String[]{"codNovoRegistro",
                "bimestre",
                "codDisciplina",
                "codTurma",
                "ocorrencias",
                "observacoes",
                "codGrupoCurriculo",
                "dataCriacao"};

        Cursor cursor = null;

        Registro registro = new Registro();

        //banco.open();

        cursor = banco.get()
                .query("NOVO_REGISTRO",
                        colunas,
                        "bimestre = ? " +
                                "AND dataCriacao = ? " +
                                "AND codTurma = ?",
                        new String[]{String.valueOf(bimestre),
                                dataCriacao,
                                turma},
                        null, null, null);

        if(cursor.moveToFirst()){

            registro.setCodNovoRegistro(cursor.getInt(0));
            registro.setBimestre(cursor.getInt(1));
            registro.setCodigoDisciplina(cursor.getString(2));
            registro.setCodigoTurma(cursor.getString(3));
            registro.setOcorrencias(cursor.getString(4));
            registro.setObservacoes(cursor.getString(5));
            registro.setCodigoGrupoCurriculo(cursor.getString(6));
            registro.setDataCriacao(cursor.getString(7));
            registro.setConteudos(registroDBcrud.buscarConteudos(registro.getCodNovoRegistro()));

        }
        if(cursor != null) {

            cursor.close();
        }

        //banco.close();

        return registro;
    }

    public List<Registro> buscarRegistrosNaoSincronizados() {

        String[] colunas = new String[]{"codNovoRegistro",
                "bimestre",
                "codDisciplina",
                "codTurma",
                "ocorrencias",
                "observacoes",
                "codGrupoCurriculo",
                "dataCriacao",
                "horarios"};

        Cursor cursor = null;

        //banco.open();

        cursor = banco.get()
                .query("NOVO_REGISTRO",
                        colunas,
                        "codNovoRegistro < 0",
                        null, null, null, null);

        List<Registro> registros = new ArrayList<>();

        if(cursor.getCount() > 0) {//cursor != null) {

            cursor.moveToFirst();

            do {

                Registro r = new Registro();

                r.setCodNovoRegistro(cursor.getInt(0));
                r.setBimestre(cursor.getInt(1));
                r.setCodigoDisciplina(cursor.getString(2));
                r.setCodigoTurma(cursor.getString(3));
                r.setOcorrencias(cursor.getString(4));
                r.setObservacoes(cursor.getString(5));
                r.setCodigoGrupoCurriculo(cursor.getString(6));
                r.setDataCriacao(cursor.getString(7));
                r.setConteudos(registroDBcrud.buscarConteudos(r.getCodNovoRegistro()));

                String[] horarios = cursor.getString(8).split("-");

                for(int i = 0; i < horarios.length; i++) {

                    r.setHorarios(horarios[i]);
                }

                registros.add(r);

            }
            while (cursor.moveToNext());
        }
        cursor.close();

        //banco.close();

        return registros;
    }

    ArrayList<Aula> getAula(Disciplina disciplina) {

        ArrayList<Aula> listaAula = new ArrayList<>();

        Cursor cursor = null;

        try {

            //banco.open();

            cursor = banco.get()
                    .rawQuery("SELECT * " +
                                    "FROM AULAS " +
                                    "WHERE disciplina_id = " + disciplina.getId(),
                            null);
            while(cursor.moveToNext()) {

                for(int i = 1; i <= 5; i++) {

                    final Aula aula = new Aula();

                    aula.setInicio(cursor.getString(cursor.getColumnIndex("inicioHora")));
                    aula.setFim(cursor.getString(cursor.getColumnIndex("fimHora")));
                    aula.setDiaSemana(i);

                    listaAula.add(aula);
                }
            }
        }
        catch(Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            if(cursor != null) {

                cursor.close();
            }
            //banco.close();
        }
        return listaAula;
    }

    UsuarioTO getUsuarioAtivo() {

        StringBuffer query = new StringBuffer();

        query.append("SELECT " +
                "us.id, " +
                "us.usuario, " +
                "us.senha, " +
                "us.nome, " +
                "us.cpf, " +
                "us.rg, " +
                "us.digitoRG, " +
                "us.dataUltimoAcesso, " +
                "us.ativo, " +
                "us.token  ");

        query.append(" FROM USUARIO as us ");
        query.append(" WHERE us.ativo = 1 ");

        //banco.open();

        Cursor cursor = banco.get()
                .rawQuery(query.toString(),
                        null);
        try {

            if (cursor.moveToFirst()) {

                return new UsuarioTO(cursor);
            }
        }
        catch (Exception e) {

            e.printStackTrace();

            CrashAnalytics.e(TAG, e);
        }
        finally {

            cursor.close();

            //banco.close();
        }
        return null;
    }

    public JSONObject montarJSONEnvio(Registro registro) {

        JSONObject jsonObject = null;

        JSONArray conteudos = null;

        try {

            jsonObject = new JSONObject();

            jsonObject.put("bimestre", registro.getBimestre());
            jsonObject.put("codNovoRegistro", registro.getCodNovoRegistro());
            jsonObject.put("codigoDisciplina", registro.getCodigoDisciplina());
            jsonObject.put("codigoGrupoCurriculo", registro.getCodigoGrupoCurriculo());
            jsonObject.put("codigoTurma", registro.getCodigoTurma());

            conteudos = new JSONArray();

            for(int i = 0; i < registro.getConteudos().size(); i++) {

                JSONObject conteudoInterior = new JSONObject();

                conteudoInterior.put("codigoConteudo", registro.getConteudo(i).getCodigoConteudo());

                JSONArray habilidades = new JSONArray();

                for(int j = 0; j < registro.getConteudo(i).getCodigosHabilidades().size(); j++) {

                    habilidades.put(registro.getConteudo(i).getHabSelecionadas(j));
                }
                conteudoInterior.put("habilidades", habilidades);
                conteudos.put(conteudoInterior);
            }
            jsonObject.put("conteudos", conteudos);
            jsonObject.put("dataCriacao", registro.getDataCriacao());
            jsonObject.put("observacoes", registro.getObservacoes());
            jsonObject.put("ocorrencias", registro.getOcorrencias());

            JSONArray horarios = new JSONArray();

            for(int i = 0; i < registro.getHorarios().size(); i++) {

                horarios.put(registro.getHorarios().get(i));
            }

            jsonObject.put("horarios", horarios);
        }
        catch (Exception e) {

            e.printStackTrace();
        }
        return jsonObject;
    }
}
