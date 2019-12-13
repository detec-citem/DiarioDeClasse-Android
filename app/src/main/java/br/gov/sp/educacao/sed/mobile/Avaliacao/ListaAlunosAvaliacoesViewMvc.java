package br.gov.sp.educacao.sed.mobile.Avaliacao;

import android.view.View;
import android.view.ViewGroup;

import android.content.Intent;

import android.support.annotation.Nullable;

import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;

interface ListaAlunosAvaliacoesViewMvc {

    interface Listener {

        void onBackPressed();

        void alterarNota(Aluno aluno);

        void navegarPara(Intent intent);
    }

    View getRootView();

    void unregisterListener();

    void recarregarListaAvaliacoes();

    void registerListener(Listener listener);

    void exibirNomeTurmaDisciplina(String nomeTurma, String disciplina);

    ToolbarViewMvcImpl getToolbarViewMvcImpl(@Nullable ViewGroup parent);
}
