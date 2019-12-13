package br.gov.sp.educacao.sed.mobile.Turmas;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

import android.content.Intent;

import android.view.LayoutInflater;

import android.support.v7.app.AppCompatActivity;

import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.CriarAcessoBanco;

public class AlunosListaActivity
        extends AppCompatActivity
         implements AlunosListaViewMvc.Listener {

    private Banco banco;

    private String usuario;

    private TurmaGrupo turmaGrupo;

    private TurmaDBgetters turmaDBgetters;

    private ArrayList<Aluno> alunoArrayList;

    private CriarAcessoBanco criarAcessoBanco;

    private AlunosListaViewMvcImpl turmasAlunosViewMvcImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        turmasAlunosViewMvcImpl = new AlunosListaViewMvcImpl(LayoutInflater.from(this), getSupportFragmentManager(), null);

        criarAcessoBanco = new CriarAcessoBanco();

        banco = criarAcessoBanco.gerarBanco(getApplicationContext());

        turmaDBgetters = new TurmaDBgetters(banco);

        Bundle bundle = getIntent().getExtras();

        turmaGrupo = bundle.getParcelable("turma_grupo");

        turmasAlunosViewMvcImpl.setarTurmaGrupo(turmaGrupo);

        Turma turma = turmaGrupo.getTurma();

        usuario = turmaDBgetters.getNomeUsuario();

        turmasAlunosViewMvcImpl.exibirNomeUsuario(usuario);

        turmasAlunosViewMvcImpl.exibirNomeTurma(turma.getNomeTurma() + " / " + turma.getNomeTipoEnsino());

        alunoArrayList = turma.getAlunos();

        if (alunoArrayList != null) {

            Collections.sort(alunoArrayList, new Comparator<Aluno>() {

                        @Override
                        public int compare(Aluno p1, Aluno p2) {

                            return p1.getNumeroChamada() - p2.getNumeroChamada();
                        }
                    });

            turmasAlunosViewMvcImpl.exibirListaAlunos(alunoArrayList);
        }

        setContentView(turmasAlunosViewMvcImpl.getRootView());
    }

    @Override
    protected void onStop() {

        super.onStop();

        turmasAlunosViewMvcImpl.unregisterListenerMenuNavegacao();

        turmasAlunosViewMvcImpl.unregisterListener();
    }

    @Override
    protected void onStart() {

        super.onStart();

        turmasAlunosViewMvcImpl.registerListenerMenuNavegacao();

        turmasAlunosViewMvcImpl.registerListener(this);
    }

    @Override
    protected void onResume() {

        super.onResume();

        turmasAlunosViewMvcImpl.removerProgressBarVoador();

        turmasAlunosViewMvcImpl.ativarBotao();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        turmasAlunosViewMvcImpl.limparLista();
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    @Override
    public void navegarPara(Intent intent) {

        startActivity(intent);
    }

    @Override
    public void onAlunoSelecionado(Aluno aluno) {

        usuarioSelecionouAluno(aluno);
    }

    @Override
    public void usuarioSelecionouAluno(Aluno aluno) {

        Bundle bundle = new Bundle();

        bundle.putParcelable("Aluno", aluno);

        bundle.putString("Usuario", usuario);

        bundle.putParcelable(TurmaGrupo.BUNDLE_TURMA_GRUPO, turmaGrupo);

        Intent intent = new Intent(this, AlunoDetalheActivity.class);

        intent.putExtras(bundle);

        startActivity(intent);
    }
}
