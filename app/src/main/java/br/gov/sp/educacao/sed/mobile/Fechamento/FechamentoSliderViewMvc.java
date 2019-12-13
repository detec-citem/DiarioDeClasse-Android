package br.gov.sp.educacao.sed.mobile.Fechamento;

import android.view.View;

interface FechamentoSliderViewMvc {

    interface Listener {

        void usuarioClicouAlunoInativo();

        void usuarioClicouConfirma(int nota, int faltas, int ausenciasCompensadas, int faltasAcumuladas);
    }

    View getRootView();

    void unregisterListener();

    void registerListener(Listener listener);
}
