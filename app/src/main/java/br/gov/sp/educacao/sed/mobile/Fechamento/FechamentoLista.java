package br.gov.sp.educacao.sed.mobile.Fechamento;

import android.os.Bundle;

import java.util.List;
import java.util.ArrayList;

import android.content.Intent;

import android.util.SparseArray;

import android.view.LayoutInflater;

import android.support.v7.app.AppCompatActivity;

import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;

import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.Analytics;
import br.gov.sp.educacao.sed.mobile.util.CriarAcessoBanco;

public class FechamentoLista
        extends AppCompatActivity
         implements FechamentoListaViewMvc.Listener {

    private Banco banco;

    private List<Aluno> alunos;

    private TurmaGrupo turmaGrupo;

    private List<FechamentoAluno> fechamentos;

    private CriarAcessoBanco criarAcessoBanco;

    private FechamentoDBgetters fechamentoDBgetters;

    private FechamentoListaViewMvcImpl fechamentoListaViewMvcImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        fechamentoListaViewMvcImpl = new FechamentoListaViewMvcImpl(LayoutInflater.from(this), getSupportFragmentManager(), null);

        Bundle bundle = getIntent().getExtras();

        turmaGrupo = bundle.getParcelable(TurmaGrupo.BUNDLE_TURMA_GRUPO);

        fechamentoListaViewMvcImpl.setarTurmaGrupo(turmaGrupo);

        exibirNomeTurmaDisciplina();

        int codigoTurma = bundle.getInt("codigoTurma");

        int codigoDisciplina = bundle.getInt("codigoDisciplina");

        int tipoFechamentoAtual = bundle.getInt("tipoFechamentoAtual");

        alunos = bundle.getParcelableArrayList("listaAlunos");

        int alunosSize = alunos.size();

        criarAcessoBanco = new CriarAcessoBanco();

        banco = criarAcessoBanco.gerarBanco(this);

        fechamentoDBgetters = new FechamentoDBgetters(banco);

        SparseArray<MediaAluno> medias = new SparseArray<>();

        fechamentos = new ArrayList<>();

        for (int i = 0; i < alunosSize; i++) {

            Aluno aluno = alunos.get(i);

            medias.put(aluno.getCodigoAluno(), fechamentoDBgetters.getMediaAluno(codigoTurma, aluno.getCodigoMatricula(),
                            codigoDisciplina));

            fechamentos.add(fechamentoDBgetters.getFechamentoAluno(

                    codigoTurma, aluno, codigoDisciplina, tipoFechamentoAtual)
            );
        }

        inicializarGoogleAnalytics();

        setContentView(fechamentoListaViewMvcImpl.getRootView());
    }

    @Override
    protected void onStop() {

        super.onStop();

        fechamentoListaViewMvcImpl.unregisterListenerMenuNavegacao();

        fechamentoListaViewMvcImpl.unregisterListener();
    }

    @Override
    protected void onStart() {

        super.onStart();

        fechamentoListaViewMvcImpl.registerListenerMenuNavegacao();

        fechamentoListaViewMvcImpl.registerListener(this);

        fechamentoListaViewMvcImpl.criarAdapter(fechamentos);
    }

    @Override
    protected void onResume() {

        super.onResume();

        fechamentoListaViewMvcImpl.removerProgressBarVoador();
    }

    @Override
    public void botaoVoltar() {

        super.onBackPressed();
    }

    @Override
    public void navegarPara(Intent intent) {

        startActivity(intent);
    }

    private void exibirNomeTurmaDisciplina() {

        fechamentoListaViewMvcImpl.exibirNomeTurmaDisciplina(

                turmaGrupo.getTurma().getNomeTurma(), turmaGrupo.getDisciplina().getNomeDisciplina()
        );
    }

    private void inicializarGoogleAnalytics() {

        Analytics.setTela(this, this.getClass().toString());
    }
}
