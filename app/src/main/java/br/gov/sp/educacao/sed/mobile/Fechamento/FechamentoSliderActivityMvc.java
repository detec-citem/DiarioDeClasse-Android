package br.gov.sp.educacao.sed.mobile.Fechamento;

import android.view.View;

interface FechamentoSliderActivityMvc {

    interface Listener {

        void onBackPressed();
    }

    View getRootView();

    void unregisterListener();

    void registerListener(Listener listener);
}
