package br.gov.sp.educacao.sed.mobile.Fechamento;

import android.text.Editable;

import android.view.View;
import android.view.ViewGroup;

import android.content.Intent;

import android.support.annotation.Nullable;

interface FechamentoCalcularMediaViewMvc {

    interface Listener {

        void onBackPressed();

        void navegarPara(Intent intent);

        void usuarioSelecionouSalvar(long id);

        void usuarioSelecinouCalcular(long selected);

        void usuarioSelecionouTipoMedia(String selected);

        void adicionarPesoAvaliacao(Editable peso, Object tag);
    }

    View getRootView();

    void unregisterListener();

    void registerListener(Listener listener);

    ToolbarViewMvcImpl getToolbarViewMvcImpl(@Nullable ViewGroup parent);
}
