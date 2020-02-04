package br.gov.sp.educacao.sed.mobile.Turmas;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

interface TurmasListaViewMvc {

    interface Listener {

        void onBackPressed();

        void onTurmaSelecionada(TurmaGrupo turmaGrupo);

        void configurarEscolhaTelaFrequencia(boolean lancamento);
    }

    View getRootView();

    void exibirListaTurmas();

    void unregisterListener();

    void inicializarListaDeTurmas();

    void exibirNomeUsuario(String nome);

    void registerListener(Listener listener);

    ToolbarViewMvc getToolbarViewMvc(@Nullable ViewGroup parent);
}
