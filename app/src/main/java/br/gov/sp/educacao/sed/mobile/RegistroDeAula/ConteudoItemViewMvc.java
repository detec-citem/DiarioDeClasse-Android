package br.gov.sp.educacao.sed.mobile.RegistroDeAula;

import android.view.View;

interface ConteudoItemViewMvc {

    interface Listener {

        void onConteudoSelecionado(Conteudo conteudo);
    }

    View getRootView();

    void registerListener(Listener listener);

    void unregisterListener();

    void exibirInfoConteudo(Conteudo conteudo);
}
