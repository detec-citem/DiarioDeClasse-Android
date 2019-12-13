package br.gov.sp.educacao.sed.mobile.Turmas;

import android.view.View;

interface AlunosListaItemViewMvc {

    interface Listener {

        void onAlunoSelecionado(Aluno aluno);
    }

    View getRootView();

    void unregisterListener();

    void registerListener(Listener listener);

    void exibirInfoAluno(Aluno aluno, int totalAlunos);
}
