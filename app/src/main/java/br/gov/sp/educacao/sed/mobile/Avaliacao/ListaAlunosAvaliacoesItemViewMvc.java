package br.gov.sp.educacao.sed.mobile.Avaliacao;

import android.view.View;

import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;

interface ListaAlunosAvaliacoesItemViewMvc {

    interface Listener {

        void alterarNota(Aluno aluno);
    }

    View getRootView();

    void unregisterListener();

    void registerListener(Listener listener);

    void exibirInfoAlunoAvaliacao(Aluno aluno);
}
