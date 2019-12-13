package br.gov.sp.educacao.sed.mobile.Avaliacao;

import android.view.View;
import android.view.ViewGroup;

import android.content.Intent;

import android.support.annotation.Nullable;

import android.support.v4.view.PagerAdapter;

interface FragmentLAPViewMvc {

    interface Listener {

        void botaoVoltar();

        void navegarParaListaAlunos();

        void navegarPara(Intent intent);
    }

    View getRootView();

    void unregisterListener();

    void exibirNomeTurma(String turma);

    void usuarioClicouBotaoConfirmaNota();

    void definirNumeroPaginas(int numPages);

    void registerListener(Listener listener);

    void exibirNomeAvaliacao(String avaliacao);

    void createPagerAdapter(PagerAdapter pagerAdapter);

    ToolbarViewMvcImpl getToolbarViewMvcImpl(@Nullable ViewGroup parent);
}
