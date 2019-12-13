package br.gov.sp.educacao.sed.mobile.Avaliacao;

import android.view.View;

interface AvaliacoesSliderViewMvc {

    interface Listener {

        void onBackPressed();
    }

    View getRootView();

    void unregisterListener();

    void registerListener(Listener listener);
}
