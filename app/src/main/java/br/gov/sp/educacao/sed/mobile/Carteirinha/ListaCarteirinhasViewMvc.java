package br.gov.sp.educacao.sed.mobile.Carteirinha;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

interface ListaCarteirinhasViewMvc {

    interface Listener {

        void onBackPressed();

        void revalidarToken();

        void buscarCarteirinhas();

        void atualizarToken(String token);

        void selecionarPerfil(String token);

        void usuarioQuerAtualizarCarteirinhas();

        void perfilSelecionado(boolean perfilOK);

        void analisarRespostaCarteirinhas(String respostaJsonCarteirinha);

        void onCarteirinhaSelecionada(DadosCarteirinha dadosCarteirinha, View view1, View view2);
    }

    View getRootView();

    void registerListener(Listener listener);

    void unregisterListener();

    ToolbarViewMvcImpl getToolbarViewMvcImpl(@Nullable ViewGroup parent);
}
