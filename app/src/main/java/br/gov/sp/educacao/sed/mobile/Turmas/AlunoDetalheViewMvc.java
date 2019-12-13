package br.gov.sp.educacao.sed.mobile.Turmas;

import android.view.View;
import android.view.ViewGroup;

import android.content.Intent;

import android.support.annotation.Nullable;


interface AlunoDetalheViewMvc {

    interface Listener {

        void onBackPressed();

        void navegarPara(Intent intent);
    }

    View getRootView();

    void unregisterListener();

    void registerListener(Listener listener);

    ToolbarViewMvc getToolbarViewMvc(@Nullable ViewGroup parent);
}
