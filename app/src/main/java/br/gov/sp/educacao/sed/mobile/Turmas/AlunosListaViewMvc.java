package br.gov.sp.educacao.sed.mobile.Turmas;

import android.view.View;
import android.view.ViewGroup;

import android.content.Intent;
import android.support.annotation.Nullable;

import java.util.List;

interface AlunosListaViewMvc {

    interface Listener {

        void onBackPressed();

        void navegarPara(Intent intent);

        void onAlunoSelecionado(Aluno aluno);

        void usuarioSelecionouAluno(Aluno aluno);
    }

    View getRootView();

    void unregisterListener();

    void exibirNomeTurma(String turma);

    void exibirNomeUsuario(String nome);

    void registerListener(Listener listener);

    void exibirListaAlunos(List<Aluno> listaDeAlunos);

    ToolbarViewMvc getToolbarViewMvcImpl(@Nullable ViewGroup parent);
}
