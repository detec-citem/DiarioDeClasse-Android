package br.gov.sp.educacao.sed.mobile.Fechamento;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

interface FechamentoPagerViewMvc {

    interface Listener {

        void botaoVoltar();

        void enviarFechamento();

        void navegarParaListaAlunos();

        void navegarPara(Intent intent);

        void usuarioQuerEnviarFechamento();
    }

    View getRootView();

    void unregisterListener();

    void usuarioClicouBotaoConfirma();

    void registerListener(Listener listener);

    void definirNumeroPaginas(int numeroPaginas);

    void createPagerAdapter(PagerAdapter pagerAdapter);

    ToolbarViewMvcImpl getToolbarViewMvcImpl(@Nullable ViewGroup parent);
}
