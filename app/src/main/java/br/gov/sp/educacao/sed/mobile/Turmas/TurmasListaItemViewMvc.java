package br.gov.sp.educacao.sed.mobile.Turmas;

import android.view.View;

interface TurmasListaItemViewMvc {

    interface Listener {

        void onTurmaSelecionada(TurmaGrupo turmaGrupo);
    }

    View getRootView();

    void unregisterListener();

    void exibirInfoTurma(TurmaGrupo turma);

    void registerListener(Listener listener);
}
