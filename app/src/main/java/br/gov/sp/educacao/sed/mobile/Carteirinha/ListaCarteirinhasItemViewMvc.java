package br.gov.sp.educacao.sed.mobile.Carteirinha;

import android.content.res.Resources;
import android.view.View;

public interface ListaCarteirinhasItemViewMvc {

    interface Listener {

        void onCarteirinhaSelecionada(DadosCarteirinha dadosCarteirinha, View view1, View view2);
    }

    View getRootView();

    void unregisterListener();

    void registerListener(Listener listener);

    void setarCarteirinha(DadosCarteirinha dadosCarteirinha, CarteirinhaAdapter.MyViewHolder holder);
}
