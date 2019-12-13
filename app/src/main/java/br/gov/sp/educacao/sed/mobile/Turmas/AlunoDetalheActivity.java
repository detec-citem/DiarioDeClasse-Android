package br.gov.sp.educacao.sed.mobile.Turmas;

import android.os.Bundle;

import android.content.Intent;

import android.view.LayoutInflater;

import android.support.v7.app.AppCompatActivity;

public class AlunoDetalheActivity
        extends AppCompatActivity
         implements AlunoDetalheViewMvc.Listener{

    private AlunoDetalheViewMvcImpl detalheViewMvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        detalheViewMvc = new AlunoDetalheViewMvcImpl(LayoutInflater.from(this), getSupportFragmentManager(), null);

        String usuario;

        usuario = getIntent().getExtras().getString("Usuario");

        TurmaGrupo turma = getIntent().getExtras().getParcelable(TurmaGrupo.BUNDLE_TURMA_GRUPO);

        detalheViewMvc.setarTurmaGrupo(turma);

        Aluno aluno = getIntent().getExtras().getParcelable("Aluno");

        String nomeTurma = turma.getTurma().getNomeTurma();

        detalheViewMvc.exibirDetalhesAluno(aluno, usuario, nomeTurma);

        setContentView(detalheViewMvc.getRootView());
    }

    @Override
    public void navegarPara(Intent intent) {

        startActivity(intent);
    }

    @Override
    protected void onStart() {

        super.onStart();

        detalheViewMvc.registerListenerMenuNavegacao();

        detalheViewMvc.registerListener(this);
    }

    @Override
    protected void onResume() {

        super.onResume();

        detalheViewMvc.removerProgressBarVoador();
    }

    @Override
    protected void onStop() {

        super.onStop();

        detalheViewMvc.unregisterListenerMenuNavegacao();

        detalheViewMvc.unregisterListener();
    }

    @Override
    public void onBackPressed() {

        finish();
    }
}
