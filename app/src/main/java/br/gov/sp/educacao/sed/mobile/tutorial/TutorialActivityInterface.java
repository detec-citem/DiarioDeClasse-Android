package br.gov.sp.educacao.sed.mobile.tutorial;

import android.view.View;

public interface TutorialActivityInterface {

    interface Listener{

        void terminouTutorial();
        void navegarParaAtendimentoSed();
    }

    View getRootView();
    void unregisterListener();
    void registerListener(Listener listener);

    void avancarImagem();
    void voltarImagem();
}
