package br.gov.sp.educacao.sed.mobile.Avaliacao;

import android.view.View;

interface ListaAvaliacoesItemViewMvc {

    void comecaai();

    interface Listener {

        void onAvaliacaoSelecionada(Avaliacao avaliacao);

        void usuarioQuerEditarAvaliacao(Avaliacao avaliacao);

        void usuarioQuerDeletarAvaliacao(Avaliacao avaliacao);
    }

    View getRootView();

    void unregisterListener();

    void registerListener(Listener listener);

    void exibirInfoAvaliacao(Avaliacao avaliacao);
}
