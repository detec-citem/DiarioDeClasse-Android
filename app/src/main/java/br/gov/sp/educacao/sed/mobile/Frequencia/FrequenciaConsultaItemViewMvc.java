package br.gov.sp.educacao.sed.mobile.Frequencia;

import android.view.View;

import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;

interface FrequenciaConsultaItemViewMvc {

    interface Listener {


    }

    View getRootView();

    void unregisterListener();

    void exibirFaltasAluno(Aluno aluno);

    void registerListener(Listener listener);
}
