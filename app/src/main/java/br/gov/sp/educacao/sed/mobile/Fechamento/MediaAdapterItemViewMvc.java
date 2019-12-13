package br.gov.sp.educacao.sed.mobile.Fechamento;

import android.view.View;

import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;

interface MediaAdapterItemViewMvc {

    interface Listener {

        void atualizarLista();
    }

    View getRootView();

    void unregisterListener();

    void exibirMedias(Aluno aluno);

    void registerListener(Listener listener);
}
