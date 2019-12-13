package br.gov.sp.educacao.sed.mobile.RegistroDeAula;

import android.view.View;

interface HabilidadeItemViewMvc {

    interface Listener {

        void onHabilidadeSelecionada(Habilidade habilidade);
    }

    View getRootView();

    void registerListener(Listener listener);

    void unregisterListener();

    void exibirInfoHabilidade(Habilidade habilidade);
}
