package br.gov.sp.educacao.sed.mobile.Turmas;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;

import br.gov.sp.educacao.sed.mobile.Escola.Aula;
import br.gov.sp.educacao.sed.mobile.Escola.Bimestre;
import br.gov.sp.educacao.sed.mobile.Escola.Disciplina;
import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;

public class TurmaDBgetters {

    private Banco banco;

    private final String TAG = TurmaDBgetters.class.getSimpleName();

    public TurmaDBgetters(Banco banco) {

        this.banco = banco;
    }

    String getNomeUsuario() {

        String queryNomeUsuario =

                "SELECT nome FROM USUARIO WHERE ativo = 1;";

        SQLiteStatement statementNomeUsuario = banco.get().compileStatement(queryNomeUsuario);

        String nome = "";

        try {

            banco.get().beginTransaction();

            nome = statementNomeUsuario.simpleQueryForString();
        }
        catch (Exception e) {

            e.printStackTrace();

            CrashAnalytics.e(TAG, e);
        }
        finally {

            statementNomeUsuario.close();

            statementNomeUsuario = null;

            banco.get().setTransactionSuccessful();

            banco.get().endTransaction();
        }
        return nome;
    }

    private Aula getAula(Disciplina disciplina) {

        Cursor cursor = null;

        try {

            cursor = banco.get().rawQuery(

                    "SELECT * FROM AULAS WHERE disciplina_id = " + disciplina.getId(), null
            );

            if(cursor.getCount() > 0 && cursor.moveToNext()) {

                Aula aula = new Aula();

                aula.setFim(cursor.getString(cursor.getColumnIndex("fimHora")));
                aula.setInicio(cursor.getString(cursor.getColumnIndex("inicioHora")));

                return aula;
            }
        }
        catch(Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            if(cursor != null) {

                cursor.close();
            }
        }
        return null;
    }

    public Bimestre getBimestre(int turmasFrequenciaId) {

        Cursor cursor = null;

        Bimestre bimestre = new Bimestre();

        try {

            banco.get().beginTransaction();

            cursor = banco.get().rawQuery(

                    "SELECT * FROM BIMESTRE WHERE turmasFrequencia_id = " + turmasFrequenciaId + " AND bimestreAtual = 1;", null
            );

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

            banco.get().setTransactionSuccessful();

            banco.get().endTransaction();
        }
        return bimestre;
    }

    public ArrayList<TurmaGrupo> getTurmas(int ano, boolean fechamento) {

        ArrayList<TurmaGrupo> listaTurmaGrupo = new ArrayList<TurmaGrupo>();

        String queryTurmas =

                "SELECT T.id AS idTurma, T.anoLetivo AS anoLetivoTurma, T.codigoTurma AS codigoTurma, T.nomeTurma AS nomeTurma, T.serieTurma AS serieTurma, " +
                        "T.codigoDiretoria AS codigoDiretoria, T.nomeDiretoria AS nomeDiretoria, T.codigoEscola AS codigoEscola, T.nomeEscola AS nomeEscola, " +
                        "T.codigoTipoEnsino AS codigoTipoEnsino, T.nomeTipoEnsino AS nomeTipoEnsino, " +
                        "TF.id AS idTurmasFrequencia, TF.aulasBimestre AS aulasBimestre, TF.aulasAno AS aulasAno " +
                        "FROM TURMAS AS T, " +
                        "TURMASFREQUENCIA AS TF " +
                        "WHERE TF.turma_id = T.id " +
                        "AND T.anoLetivo = " + ano +
                        " ORDER BY nomeTurma;";

        Cursor cursor = null;

        try {

            banco.get().beginTransaction();

            cursor = banco.get().rawQuery(queryTurmas, null);

            int idTurmaIndex = cursor.getColumnIndex("idTurma");
            int anoLetivoTurmaIndex = cursor.getColumnIndex("anoLetivoTurma");
            int codigoTurmaIndex = cursor.getColumnIndex("codigoTurma");
            int nomeTurmaIndex = cursor.getColumnIndex("nomeTurma");
            int serieTurmaIndex = cursor.getColumnIndex("serieTurma");
            int nomeEscolaIndex = cursor.getColumnIndex("nomeEscola");
            int nomeDiretoriaIndex = cursor.getColumnIndex("nomeDiretoria");
            int codigoTipoEnsinoIndex = cursor.getColumnIndex("codigoTipoEnsino");
            int nomeTipoEnsinoIndex = cursor.getColumnIndex("nomeTipoEnsino");
            int idTurmasFrequenciaIndex = cursor.getColumnIndex("idTurmasFrequencia");
            int codigodEscolaIndex = cursor.getColumnIndex("codigoEscola");
            int aulasBimestreIndex = cursor.getColumnIndex("aulasBimestre");
            int aulasAnoIndex = cursor.getColumnIndex("aulasAno");

            while(cursor.moveToNext()) {

                Turma turma = new Turma();

                turma.setId(cursor.getInt(idTurmaIndex));
                turma.setAno(cursor.getInt(anoLetivoTurmaIndex));
                turma.setCodigoTurma(cursor.getInt(codigoTurmaIndex));
                turma.setCodigoEscola(cursor.getInt(codigodEscolaIndex));
                turma.setNomeTurma(cursor.getString(nomeTurmaIndex));
                turma.setSerie(cursor.getInt(serieTurmaIndex));
                turma.setNomeEscola(cursor.getString(nomeEscolaIndex));
                turma.setNomeDiretoria(cursor.getString(nomeDiretoriaIndex));
                turma.setCodigoTipoEnsino(cursor.getInt(codigoTipoEnsinoIndex));
                turma.setNomeTipoEnsino(cursor.getString(nomeTipoEnsinoIndex));

                TurmasFrequencia turmasFrequencia = new TurmasFrequencia();

                turmasFrequencia.setId(cursor.getInt(idTurmasFrequenciaIndex));

                ArrayList<Disciplina> disciplinas = getDisciplinas(turmasFrequencia);

                Cursor cursorAluno = banco.get().rawQuery("SELECT * FROM ALUNOS WHERE turma_id = " + turma.getId(), null);

                int alunoIdIndex = cursorAluno.getColumnIndex("id");
                int codigoAlunoIndex = cursorAluno.getColumnIndex("codigoAluno");
                int codigoMatriculaIndex = cursorAluno.getColumnIndex("codigoMatricula");
                int alunoAtivoIndex = cursorAluno.getColumnIndex("alunoAtivo");
                int numeroChamadaIndex = cursorAluno.getColumnIndex("numeroChamada");
                int nomeAlunoIndex = cursorAluno.getColumnIndex("nomeAluno");
                int numeroRaIndex = cursorAluno.getColumnIndex("numeroRa");
                int digitoRaIndex = cursorAluno.getColumnIndex("digitoRa");
                int paiIndex = cursorAluno.getColumnIndex("pai");
                int maeIndex = cursorAluno.getColumnIndex("mae");
                int nascimentoIndex = cursorAluno.getColumnIndex("nascimento");
                int necessidadesEspeciaisIndex = cursorAluno.getColumnIndex("necessidadesEspeciais");

                for(Disciplina disciplina : disciplinas){

                    TurmaGrupo turmaGrupo = new TurmaGrupo();

                    turmaGrupo.setTurma(turma);
                    turmaGrupo.setTurmasFrequencia(turmasFrequencia);
                    turmaGrupo.setDisciplina(disciplina);
                    turmaGrupo.setAula(getAula(disciplina));
                    listaTurmaGrupo.add(turmaGrupo);

                    if(fechamento && disciplina.getCodigoDisciplina() == 1000) {

                        int numeroSerie = cursor.getInt(serieTurmaIndex);

                        int disciplinasAnosIniciais = 3;

                        int[] codigoDisciplina = new int[]{2700, 1100, 7245};

                        String[] nomeDisciplina = new String[]{"MATEMÁTICA", "LÍNGUA PORTUGUESA", "CIÊNCIAS DA NATUREZA/CIÊNCIAS HUMANAS"};

                        for(int i = 0; i < disciplinasAnosIniciais; i++) {

                            Turma ttg1 = new Turma();

                            ttg1.setId(cursor.getInt(idTurmaIndex));
                            ttg1.setAno(cursor.getInt(anoLetivoTurmaIndex));
                            ttg1.setCodigoTurma(cursor.getInt(codigoTurmaIndex));
                            ttg1.setNomeTurma(cursor.getString(nomeTurmaIndex));
                            ttg1.setSerie(cursor.getInt(serieTurmaIndex));
                            ttg1.setNomeEscola(cursor.getString(nomeEscolaIndex));
                            ttg1.setNomeDiretoria(cursor.getString(nomeDiretoriaIndex));
                            ttg1.setCodigoTipoEnsino(cursor.getInt(codigoTipoEnsinoIndex));
                            ttg1.setNomeTipoEnsino(cursor.getString(nomeTipoEnsinoIndex));

                            while (cursorAluno.moveToNext()) {

                                Aluno aluno = new Aluno();

                                aluno.setId(cursorAluno.getInt(alunoIdIndex));
                                aluno.setCodigoAluno(cursorAluno.getInt(codigoAlunoIndex));
                                aluno.setCodigoMatricula(cursorAluno.getString(codigoMatriculaIndex));
                                aluno.setNumeroChamada((byte) cursorAluno.getInt(numeroChamadaIndex));
                                aluno.setNomeAluno(cursorAluno.getString(nomeAlunoIndex));
                                aluno.setNumeroRa(cursorAluno.getInt(numeroRaIndex));
                                aluno.setDigitoRa(cursorAluno.getString(digitoRaIndex));
                                aluno.setPai(cursorAluno.getString(paiIndex));
                                aluno.setMae(cursorAluno.getString(maeIndex));
                                aluno.setNascimento(cursorAluno.getString(nascimentoIndex));
                                aluno.setNecessidadesEspeciais(cursorAluno.getString(necessidadesEspeciaisIndex));

                                int alunoAtivoColuna = cursorAluno.getInt(alunoAtivoIndex);

                                if(alunoAtivoColuna == 0) {

                                    aluno.setAlunoAtivo(false);
                                }
                                else {

                                    aluno.setAlunoAtivo(true);
                                }
                                ttg1.addAluno(aluno);
                            }
                            cursorAluno.moveToFirst();

                            Disciplina dc1 = new Disciplina();

                            dc1.setCodigoDisciplina(codigoDisciplina[i]);

                            dc1.setNomeDisciplina(nomeDisciplina[i]);

                            dc1.setId(disciplina.getId());

                            TurmaGrupo turmaGrupo1 = new TurmaGrupo();

                            turmaGrupo1.setTurma(ttg1);
                            turmaGrupo1.setTurmasFrequencia(turmasFrequencia);
                            turmaGrupo1.setDisciplina(dc1);
                            turmaGrupo1.setAula(getAula(disciplina));

                            listaTurmaGrupo.add(turmaGrupo1);
                        }
                    }
                }

                while(cursorAluno.moveToNext()) {

                    Aluno aluno = new Aluno();

                    aluno.setId(cursorAluno.getInt(alunoIdIndex));
                    aluno.setCodigoAluno(cursorAluno.getInt(codigoAlunoIndex));
                    aluno.setCodigoMatricula(cursorAluno.getString(codigoMatriculaIndex));
                    aluno.setNumeroChamada((byte)cursorAluno.getInt(numeroChamadaIndex));
                    aluno.setNomeAluno(cursorAluno.getString(nomeAlunoIndex));
                    aluno.setNumeroRa(cursorAluno.getInt(numeroRaIndex));
                    aluno.setDigitoRa(cursorAluno.getString(digitoRaIndex));
                    aluno.setPai(cursorAluno.getString(paiIndex));
                    aluno.setMae(cursorAluno.getString(maeIndex));
                    aluno.setNascimento(cursorAluno.getString(nascimentoIndex));
                    aluno.setNecessidadesEspeciais(cursorAluno.getString(necessidadesEspeciaisIndex));

                    int alunoAtivoColuna = cursorAluno.getInt(alunoAtivoIndex);

                    if(alunoAtivoColuna == 0) {

                        aluno.setAlunoAtivo(false);
                    }
                    else {

                        aluno.setAlunoAtivo(true);
                    }
                    turma.addAluno(aluno);
                }

                cursorAluno.close();

            }
        }
        catch(Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            if (cursor != null) {

                cursor.close();
            }

            banco.get().setTransactionSuccessful();

            banco.get().endTransaction();
        }
        return listaTurmaGrupo;
    }

    public ArrayList<Disciplina> getDisciplinas(TurmasFrequencia turmasFrequencia) {

        Cursor cursor = null;

        ArrayList<Disciplina> disciplinas = new ArrayList<>();

        try {

            cursor = banco.get().rawQuery(

                    "SELECT * FROM DISCIPLINA WHERE turmasFrequencia_id = " + turmasFrequencia.getId(), null
            );

            if(cursor.moveToFirst()) {

                do{

                    Disciplina disciplina = new Disciplina();

                    disciplina.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    disciplina.setNomeDisciplina(cursor.getString(cursor.getColumnIndex("nomeDisciplina")));
                    disciplina.setCodigoDisciplina(cursor.getInt(cursor.getColumnIndex("codigoDisciplina")));

                    disciplinas.add(disciplina);
                }while (cursor.moveToNext());

                return  disciplinas;
            }
        }
        catch(Exception e) {

            CrashAnalytics.e(TAG, e);
        }
        finally {

            if(cursor != null) {

                cursor.close();
            }
        }
        return null;
    }

    public int getTurmaFrequencia(String turma) {

        int turmaFrequenciaId = 0;

        try {

            Cursor cursor = banco.get().rawQuery("SELECT TF.id FROM TURMASFREQUENCIA AS TF JOIN TURMAS AS T ON TF.turma_id = T.id WHERE T.codigoTurma = " + turma, null);

            if(cursor != null) {

                cursor.moveToFirst();

                turmaFrequenciaId = cursor.getInt(cursor.getColumnIndex("id"));
            }
        }
        catch(Exception e) {

            e.printStackTrace();
        }

        return turmaFrequenciaId;
    }
}
