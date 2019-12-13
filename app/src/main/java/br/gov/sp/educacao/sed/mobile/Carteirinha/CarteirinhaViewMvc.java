package br.gov.sp.educacao.sed.mobile.Carteirinha;

import android.view.View;

interface CarteirinhaViewMvc {

    interface Listener {

        void onBackPressed();
    }

    View getRootView();

    void unregisterListener();

    void registerListener(Listener listener);
}
