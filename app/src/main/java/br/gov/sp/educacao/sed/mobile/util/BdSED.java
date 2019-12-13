package br.gov.sp.educacao.sed.mobile.util;

import android.content.Context;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import br.gov.sp.educacao.sed.mobile.R;
import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;

public class BdSED
        extends BdManager {

    private static final String NAME = "SEDDB";
    private static final String TAG_LOG = "SEDDB";
    private static final int VERSAO = 16;

    private MyPreferences myPref;

    public BdSED(Context context) {

        super(context, NAME, VERSAO);

        myPref = new MyPreferences(context);
    }

    @Override
    public void onCreate(SQLiteDatabase bd) {

        criaTabelas(bd);
    }

    @Override
    public void onUpgrade(SQLiteDatabase bd, int versaoAtual, int versaoNova) {

        try {

            if(versaoNova == 2) {

                bd.execSQL("CREATE TABLE \"BIMESTRE\" (\n" +
                           "    \"id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                           "    \"numero\" int,\n" +
                           "    \"inicio\" VARCHAR,\n" +
                           "    \"fim\" VARCHAR,\n" +
                           "    \"CodigoTurma\" int,\n" +
                           "    \"CodigoDisciplina\" int,\n" +
                           "    \"tipo\" int,\n" +
                           "    \"ano\" int,\n" +
                           "    \"UsuarioId\" int\n" +
                           ");");

                bd.execSQL("ALTER TABLE \"CURRICULO\"" +
                           "ADD COLUMN codigo_curriculo INT");
            }

            if(versaoNova == 3) {

                bd.execSQL("ALTER TABLE \"AVALIACAO\"" +
                           "ADD COLUMN tipoAtividade INT DEFAULT NULL");

                bd.execSQL("ALTER TABLE \"AVALIACAO\"" +
                           "ADD COLUMN valeNota INT DEFAULT NULL");
            }

            if(versaoNova == 4) {

                bd.execSQL("ALTER TABLE \"AVALIACAO\"" +
                           "ADD COLUMN codigo INT DEFAULT NULL");
            }

            if(versaoNova == 5) {

                bd.execSQL("DROP INDEX index_avaliacoes_codigoAvaliacao");
                bd.execSQL("CREATE UNIQUE INDEX index_avaliacoes_id on AVALIACOES (id)");
            }

            if(versaoNova == 6) {

                bd.execSQL("CREATE TABLE \"FECHAMENTO_TURMA\" (\n" +
                           "    \"id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                           "    \"codigoTurma\" INT,\n" +
                           "    \"codigoDisciplina\" INT,\n" +
                           "    \"codigoTipoFechamento\" INT,\n" +
                           "    \"aulasRealizadas\" INT,\n" +
                           "    \"aulasPlanejadas\" INT,\n" +
                           "    \"justificativa\" VARCHAR,\n" +
                           "    \"dataServidor\" VARCHAR(20)\n" +
                           ");");

                bd.execSQL("CREATE TABLE \"FECHAMENTO_ALUNO\" (\n" +
                           "    \"id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                           "    \"codigoFechamento\" INT,\n" +
                           "    \"codigoTurma\" INT,\n" +
                           "    \"codigoMatricula\" INT,\n" +
                           "    \"codigoDisciplina\" INT,\n" +
                           "    \"codigoTipoFechamento\" INT,\n" +
                           "    \"nota\" INT,\n" +
                           "    \"faltas\" INT,\n" +
                           "    \"ausenciasCompensadas\" INT,\n" +
                           "    \"confirmado\" INT,\n" +
                           "    \"faltasAcumuladas\" INT,\n" +
                           "    \"justificativa\" VARCHAR,\n" +
                           "    \"dataServidor\" VARCHAR(20)\n" +
                           ");");

                bd.execSQL("CREATE TABLE \"MEDIA_ALUNO\" (\n" +
                           "    \"id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                           "    \"codigoTurma\" INT,\n" +
                           "    \"codigoDisciplina\" INT,\n" +
                           "    \"codigoMatricula\" INT,\n" +
                           "    \"bimestre\" INT,\n" +
                           "    \"nota_media\" INT\n" +
                           ");");

                bd.execSQL("CREATE TABLE \"TIPO_FECHAMENTO\" (\n" +
                           "    \"id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                           "    \"codigoTipoFechamento\" INT,\n" +
                           "    \"nome\" TEXT,\n" +
                           "    \"ano\" INT,\n" +
                           "    \"inicio\" VARCHAR(20),\n" +
                           "    \"fim\" VARCHAR(20)\n" +
                           ");");
            }

            if(versaoNova == 7) {

                bd.execSQL("CREATE TABLE IF NOT EXISTS \"REGISTROAULA\" (\n" +
                           "    \"id\" INTEGER PRIMARY KEY NOT NULL,\n" +
                           "    \"dataCriacao\" VARCHAR(20),\n" +
                           "    \"observacoes\" VARCHAR(60),\n" +
                           "    \"bimestre\" INTEGER,\n" +
                           "    \"cod_curr\" INTEGER,\n" +
                           "    \"cod_turma\" INTEGER,\n" +
                           "    \"cod_cont\" INTEGER,\n" +
                           "    \"cod_hab\" INTEGER\n" +
                           ");");
            }

            if(versaoNova == 8) {

                bd.execSQL("CREATE TABLE NOVO_REGISTRO(" +
                           "codNovoRegistro INTEGER PRIMARY KEY," +
                           " bimestre INTEGER NOT NULL," +
                           " codDisciplina TEXT," +
                           " codTurma TEXT NOT NULL," +
                           " ocorrencias TEXT," +
                           " observacoes TEXT NOT NULL," +
                           " codGrupoCurriculo TEXT NOT NULL," +
                           " dataCriacao TEXT NOT NULL);");

                bd.execSQL("CREATE TABLE NOVO_CONTEUDO_REGISTRO(" +
                           "codNovoConteudo INTEGER PRIMARY KEY AUTOINCREMENT," +
                           " codNovoRegistro INTEGER NOT NULL," +
                           " codigoConteudo INTEGER NOT NULL," +
                           " FOREIGN KEY(codNovoRegistro) REFERENCES NOVO_REGISTRO(codNovoRegistro));");

                bd.execSQL("CREATE TABLE NOVO_HABILIDADE_REGISTRO(" +
                           "codNovoHabilidade INTEGER PRIMARY KEY AUTOINCREMENT," +
                           " codNovoConteudo INTEGER NOT NULL," +
                           " codNovoRegistro INTEGER NOT NULL," +
                           " habilidade INTEGER NOT NULL," +
                           " codNovoRegistro INTEGER NOT NULL," +
                           " FOREIGN KEY(codNovoConteudo) REFERENCES NOVO_CONTEUDO_REGISTRO(codNovoConteudo)," +
                           " FOREIGN KEY(codNovoRegistro) REFERENCES NOVO_REGISTRO(codNovoRegistro)" +
                           ");");

                bd.execSQL("CREATE TABLE NOVO_GRUPO(" +
                           "    codigo INTEGER PRIMARY KEY NOT NULL," +
                           "    anoLetivo INTEGER NOT NULL," +
                           "    codigoTipoEnsino INTEGER NOT NULL," +
                           "    serie INTEGER NOT NULL," +
                           "    codigoDisciplina INTEGER" +
                           ");");

                bd.execSQL("CREATE TABLE NOVO_CURRICULO(" +
                           "    codigo INTEGER PRIMARY KEY NOT NULL," +
                           "    codigoGrupo INTEGER NOT NULL," +
                           "    bimestre INTEGER NOT NULL," +
                           "    FOREIGN KEY(codigoGrupo) REFERENCES NOVO_GRUPO(codigo)" +
                           ");");

                bd.execSQL("CREATE TABLE IF NOT EXISTS NOVO_CONTEUDO(" +
                           "    codigoConteudo INTEGER PRIMARY KEY NOT NULL," +
                           "    codigoCurriculo INTEGER NOT NULL," +
                           "    descricao TEXT NOT NULL," +
                           "    FOREIGN KEY(codigoCurriculo) REFERENCES NOVO_CURRICULO(codigo)" +
                           ");");

                bd.execSQL("CREATE TABLE NOVO_HABILIDADE(" +
                           " \"id\" INTEGER PRIMARY KEY NOT NULL,"+
                           " \"codigoHabilidade\" INTEGER NOT NULL," +
                           " \"codigoConteudo\" INTEGER NOT NULL," +
                           " \"descricao\" TEXT NOT NULL," +
                           " FOREIGN KEY(codigoConteudo) REFERENCES NOVO_CONTEUDO(codigoConteudo)" +
                           ");");

                bd.execSQL("CREATE TABLE NOVO_CONTEUDO_HABILIDADE(" +
                           "    conteudo INTEGER, " +
                           "    habilidade INTEGER, " +
                           "    FOREIGN KEY(conteudo) REFERENCES NOVO_CONTEUDO(codigoConteudo), " +
                           "    FOREIGN KEY(habilidade) REFERENCES NOVO_HABILIDADE(codigoHabilidade), " +
                           "    PRIMARY KEY(conteudo, habilidade));");
            }

            if(versaoNova == 9) {

                bd.execSQL("CREATE TABLE IF NOT EXISTS NOVO_REGISTRO(" +
                           "codNovoRegistro INTEGER PRIMARY KEY," +
                           " bimestre INTEGER NOT NULL," +
                           " codDisciplina TEXT," +
                           " codTurma TEXT NOT NULL," +
                           " ocorrencias TEXT," +
                           " observacoes TEXT NOT NULL," +
                           " codGrupoCurriculo TEXT NOT NULL," +
                           " dataCriacao TEXT NOT NULL);");

                bd.execSQL("CREATE TABLE IF NOT EXISTS NOVO_CONTEUDO_REGISTRO(" +
                           " codNovoConteudo INTEGER PRIMARY KEY AUTOINCREMENT," +
                           " codNovoRegistro INTEGER NOT NULL," +
                           " codigoConteudo INTEGER NOT NULL," +
                           " FOREIGN KEY(codNovoRegistro) REFERENCES NOVO_REGISTRO(codNovoRegistro));");

                bd.execSQL("CREATE TABLE IF NOT EXISTS NOVO_HABILIDADE_REGISTRO(" +
                           " codNovoHabilidade INTEGER PRIMARY KEY AUTOINCREMENT," +
                           " codNovoConteudo INTEGER NOT NULL," +
                           " codNovoRegistro INTEGER NOT NULL," +
                           " habilidade INTEGER NOT NULL," +
                           " FOREIGN KEY(codNovoConteudo) REFERENCES NOVO_CONTEUDO_REGISTRO(codNovoConteudo)," +
                           " FOREIGN KEY(codNovoRegistro) REFERENCES NOVO_REGISTRO(codNovoRegistro)" +
                           ");");

                bd.execSQL("CREATE TABLE IF NOT EXISTS NOVO_GRUPO(" +
                           "    codigo INTEGER PRIMARY KEY NOT NULL," +
                           "    anoLetivo INTEGER NOT NULL," +
                           "    codigoTipoEnsino INTEGER NOT NULL," +
                           "    serie INTEGER NOT NULL," +
                           "    codigoDisciplina INTEGER" +
                           ");");

                bd.execSQL("CREATE TABLE IF NOT EXISTS NOVO_CURRICULO(" +
                           "    codigo INTEGER PRIMARY KEY NOT NULL," +
                           "    codigoGrupo INTEGER NOT NULL," +
                           "    bimestre INTEGER NOT NULL," +
                           "    FOREIGN KEY(codigoGrupo) REFERENCES NOVO_GRUPO(codigo)" +
                           ");");

                bd.execSQL("CREATE TABLE IF NOT EXISTS NOVO_CONTEUDO(" +
                           "    codigoConteudo INTEGER PRIMARY KEY NOT NULL," +
                           "    codigoCurriculo INTEGER NOT NULL," +
                           "    descricao TEXT NOT NULL," +
                           "    FOREIGN KEY(codigoCurriculo) REFERENCES NOVO_CURRICULO(codigo)" +
                           ");");

                bd.execSQL("CREATE TABLE IF NOT EXISTS NOVO_HABILIDADE(" +
                           " \"id\" INTEGER PRIMARY KEY NOT NULL, "+
                           " \"codigoHabilidade\" INTEGER NOT NULL," +
                           " \"codigoConteudo\" INTEGER NOT NULL," +
                           " \"descricao\" TEXT NOT NULL," +
                           " FOREIGN KEY(codigoConteudo) REFERENCES NOVO_CONTEUDO(codigoConteudo)" +
                           ");");

                bd.execSQL("CREATE TABLE IF NOT EXISTS NOVO_CONTEUDO_HABILIDADE(" +
                           "    conteudo INTEGER, " +
                           "    habilidade INTEGER, " +
                           "    FOREIGN KEY(conteudo) REFERENCES NOVO_CONTEUDO(codigoConteudo), " +
                           "    FOREIGN KEY(habilidade) REFERENCES NOVO_HABILIDADE(codigoHabilidade), " +
                           "    PRIMARY KEY(conteudo, habilidade));");
            }

            if(versaoNova == 10) {

                bd.execSQL("ALTER TABLE ALUNOS" +
                           " ADD COLUMN nascimento TEXT");

                bd.execSQL("ALTER TABLE ALUNOS" +
                           " ADD COLUMN necessidadesEspeciais TEXT");

                myPref.salvarFlagAtualizar();
            }

            if(versaoNova == 11) {

                bd.execSQL("CREATE TABLE IF NOT EXISTS DIASCOMFREQUENCIA(" +
                           " id INTEGER PRIMARY KEY NOT NULL, "+
                           " dataFrequenciaDia VARCHAR(4), "+
                           " dataFrequenciaMes VARCHAR(4), "+
                           " dataFrequenciaAno VARCHAR(4), "+
                           "FOREIGN KEY(turmasFrequencia_id) REFERENCES TURMASFREQUENCIA(id));");
            }

            if(versaoNova == 12) {

                /*bd.execSQL("ALTER TABLE FALTASALUNOS" +
                           " ADD COLUMN turma_id INTEGER");*/

                bd.execSQL("DROP TABLE IF EXISTS GRUPOS");

                bd.execSQL("DROP TABLE IF EXISTS CONTEUDO");

                bd.execSQL("DROP TABLE IF EXISTS HABILIDADES");

                bd.execSQL("DROP TABLE IF EXISTS REGISTROAULA");

                bd.execSQL("DROP TABLE IF EXISTS HABILIDADESABORDADAS");
            }

            if(versaoNova == 13) {

                bd.execSQL("ALTER TABLE \"TOTALFALTASALUNOS\"" +
                        "ADD COLUMN faltasBimestreAnterior INT DEFAULT NULL");
            }

            if(versaoNova == 15) {

                bd.execSQL("CREATE TABLE IF NOT EXISTS CARTEIRINHAS(" +
                           " id INTEGER PRIMARY KEY NOT NULL, " +
                           " idUsuario INTEGER, " +
                           " nomeUsuario VARCHAR(255), " +
                           " nomeSocial varchar(255), " +
                           " codigoCargo VARCHAR(20), " +
                           " cargoUsuario VARCHAR(100), " +
                           " rgUsuario VARCHAR(15), " +
                           " rsUsuario VARCHAR(30), " +
                           " fotoUsuario TEXT, " +
                           " qrCodeUsuario TEXT," +
                           " statusAprovacao TEXT," +
                           " validade TEXT," +
                           " FOREIGN KEY(idUsuario) REFERENCES USUARIO(id));");

                bd.execSQL("CREATE TABLE IF NOT EXISTS DIASCONFLITO(" +
                        "id INTEGER PRIMARY KEY NOT NULL, " +
                        "diaLetivo_id INTEGER, " +
                        "aula_id INTEGER, " +
                        "turmasFrequencia_id INTEGER, " +
                        "disciplina_id INTEGER, " +
                        "FOREIGN KEY(diaLetivo_id) REFERENCES DIASLETIVOS(id), " +
                        "FOREIGN KEY(aula_id) REFERENCES AULAS(id), " +
                        "FOREIGN KEY(turmasFrequencia_id) REFERENCES TURMASFREQUENCIA(id), " +
                        "FOREIGN KEY(disciplina_id) REFERENCES DISCIPLINA(id)" +
                        ");");

                bd.execSQL("ALTER TABLE TURMASFREQUENCIA " +
                            "ADD COLUMN aulasSemana INTEGER DEFAULT NULL");

                bd.execSQL("ALTER TABLE DIASLETIVOS " +
                            "ADD COLUMN diaSemana INTEGER DEFAULT NULL");

                bd.execSQL("ALTER TABLE DIASLETIVOS " +
                            "ADD COLUMN semanaMes INTEGER DEFAULT NULL");

                bd.execSQL("ALTER TABLE DIASLETIVOS " +
                            "ADD COLUMN mes INTEGER DEFAULT NULL");

                bd.execSQL("ALTER TABLE DIASCOMFREQUENCIA " +
                            "ADD COLUMN horario INTEGER DEFAULT NULL");

                bd.execSQL("ALTER TABLE NOVO_REGISTRO " +
                            "ADD COLUMN horarios TEXT DEFAULT NULL");

            }
            if(versaoNova == 16){

                bd.execSQL("CREATE TABLE IF NOT EXISTS COMUNICADOS(" +
                        "cd_comunicado INTEGER PRIMARY KEY NOT NULL, " +
                        "titulo TEXT, " +
                        "comunicado TEXT, " +
                        "data TEXT, " +
                        "visualizado INTEGER);");
            }
        }
        catch (Exception e) {

            CrashAnalytics.e(TAG_LOG, e);
        }
    }

    private void criaTabelas(SQLiteDatabase bd) {

        try {

            byFile(R.raw.script, bd);
        }
        catch (Exception e) {

            CrashAnalytics.e(TAG_LOG, e);
        }
    }

    public ArrayList<Cursor> getData(String Query) {

        SQLiteDatabase sqlDB = this.getWritableDatabase();

        String[] columns = new String[] { "mesage" };

        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);

        MatrixCursor Cursor2= new MatrixCursor(columns);

        alc.add(null);

        alc.add(null);

        try {

            String maxQuery = Query ;

            Cursor c = c = sqlDB.rawQuery(maxQuery, null);

            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);

            if (null != c
                    && c.getCount() > 0) {

                alc.set(0,c);

                c.moveToFirst();

                return alc ;
            }
            return alc;

        }
        catch(Exception ex) {

            CrashAnalytics.e(TAG_LOG, ex);

            Cursor2.addRow(new Object[] { ""+ex.getMessage() });

            alc.set(1, Cursor2);

            return alc;
        }
    }
}