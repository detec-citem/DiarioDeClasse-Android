package br.gov.sp.educacao.sed.mobile.Avaliacao;

import android.view.View;

interface SliderFragmentAvaliacaoViewMvc {

    interface Listener {

        void usuarioClicouConfirmaNota(String nota);
    }

    View getRootView();

    void unregisterListener();

    void configurarParaAlunoNaoAtivo();

    void registerListener(Listener listener);

    void configurarParaAlunoAtivo(String nota);

    void exibirNomeChamadaAluno(String dadosAluno);
}
