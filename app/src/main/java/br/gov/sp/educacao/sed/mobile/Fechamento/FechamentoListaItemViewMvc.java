package br.gov.sp.educacao.sed.mobile.Fechamento;

import android.view.View;

interface FechamentoListaItemViewMvc {

    interface Listener {

    }

    View getRootView();

    void unregisterListener();

    void registerListener(Listener listener);

    void exibirFechamentoAluno(FechamentoAluno fechamentoAluno);
}
