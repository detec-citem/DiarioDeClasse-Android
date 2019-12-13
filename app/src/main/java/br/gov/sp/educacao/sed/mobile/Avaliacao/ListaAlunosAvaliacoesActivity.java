package br.gov.sp.educacao.sed.mobile.Avaliacao;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

import android.content.Intent;

import android.view.LayoutInflater;

import android.support.v7.app.AppCompatActivity;

import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;

import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.Analytics;
import br.gov.sp.educacao.sed.mobile.util.CriarAcessoBanco;

public class ListaAlunosAvaliacoesActivity
        extends AppCompatActivity
         implements ListaAlunosAvaliacoesViewMvc.Listener {

    private Banco banco;

    private Avaliacao avaliacao;

    private ArrayList<Aluno> listaAlunos;

    private CriarAcessoBanco criarAcessoBanco;

    private AvaliacaoDBgetters avaliacaoDBgetters;

    private AvaliacaoDBsetters avaliacaoDBsetters;

    private ListaAlunosAvaliacoesViewMvcImpl alunosAvaliacoesViewMvc;

    private String TAG = ListaAlunosAvaliacoesActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        inicializarGoogleAnalytics();

        Bundle bundle;

        bundle = getIntent().getExtras();

        TurmaGrupo turmaGrupo;

        turmaGrupo = bundle.getParcelable(TurmaGrupo.BUNDLE_TURMA_GRUPO);

        avaliacao = bundle.getParcelable(Avaliacao.BUNDLE_AVALIACAO);

        listaAlunos = turmaGrupo.getTurma().getAlunos();

        criarAcessoBanco = new CriarAcessoBanco();

        banco = criarAcessoBanco.gerarBanco(getApplicationContext());

        avaliacaoDBgetters = new AvaliacaoDBgetters(banco);

        avaliacaoDBsetters = new AvaliacaoDBsetters(banco);

        avaliacaoDBgetters.getAlunosNotas(avaliacao.getId(), listaAlunos);

        Collections.sort(turmaGrupo.getTurma().getAlunos(), new Comparator<Aluno>() {

            @Override
            public int compare(Aluno aluno1, Aluno aluno2) {

                return  aluno1.compareTo(aluno2);
            }
        });

        alunosAvaliacoesViewMvc = new ListaAlunosAvaliacoesViewMvcImpl(

                LayoutInflater.from(this), getSupportFragmentManager(), null, listaAlunos
        );

        alunosAvaliacoesViewMvc.exibirNomeTurmaDisciplina(

                turmaGrupo.getTurma().getNomeTurma(), turmaGrupo.getDisciplina().getNomeDisciplina()
        );

        alunosAvaliacoesViewMvc.setarTurmaGrupo(turmaGrupo);

        setContentView(alunosAvaliacoesViewMvc.getRootView());
    }

    @Override
    protected void onStop() {

        super.onStop();

        alunosAvaliacoesViewMvc.unregisterListenerMenuNavegacao();

        alunosAvaliacoesViewMvc.unregisterListener();
    }

    @Override
    protected void onStart() {

        super.onStart();

        alunosAvaliacoesViewMvc.registerListenerMenuNavegacao();

        alunosAvaliacoesViewMvc.registerListener(this);
    }

    @Override
    protected void onResume() {

        super.onResume();

        alunosAvaliacoesViewMvc.removerProgressBarVoador();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if(listaAlunos != null) {

            listaAlunos.clear();
        }
    }

    public void onBackPressed() {

        finish();
    }

    @Override
    public void alterarNota(Aluno aluno) {

        avaliacaoDBsetters.editarNotasAluno(aluno, avaliacao.getId());

        alunosAvaliacoesViewMvc.recarregarListaAvaliacoes();
    }

    @Override
    public void navegarPara(Intent intent) {

        startActivity(intent);
    }

    private void inicializarGoogleAnalytics() {

        Analytics.setTela(this, this.getClass().toString());
    }
}

