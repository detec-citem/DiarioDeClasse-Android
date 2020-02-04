package br.gov.sp.educacao.sed.mobile.Frequencia;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

interface FrequenciaConsultaViewMvc {

    interface Listener {

        void onBackPressed();

        void inicializarListaAlunos();

        void navegarPara(Intent intent);
    }

    View getRootView();

    void unregisterListener();

    void registerListener(Listener listener);

    void exibirNomeTurmaTipoEnsino(String nomeTurma, String tipoEnsino);

    ToolbarViewMvcImpl getToolbarViewMvcImpl(@Nullable ViewGroup parent);
}
