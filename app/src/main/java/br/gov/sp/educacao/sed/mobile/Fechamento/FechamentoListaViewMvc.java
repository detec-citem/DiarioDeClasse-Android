package br.gov.sp.educacao.sed.mobile.Fechamento;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

interface FechamentoListaViewMvc {

    interface Listener {

        void botaoVoltar();

        void navegarPara(Intent intent);
    }

    View getRootView();

    void unregisterListener();

    void registerListener(Listener listener);

    ToolbarViewMvcImpl getToolbarViewMvcImpl(@Nullable ViewGroup parent);
}
