package br.gov.sp.educacao.sed.mobile.Fechamento;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

interface FechamentoViewMvc {

    interface Listener {

        void onBackPressed();

        void navegarPara(Intent intent);

        void usuarioClicouCalcularMedia();

        void usuarioSelecionouConfirmar(int aulasPlanejadas, int aulasRealizadas, String justificativa);
    }

    View getRootView();

    void unregisterListener();

    void exibirAvisoNenhumaProva();

    void esconderBtnCalcularMedia();

    void exibirAvisoApenasUmaProva();

    void avisoUsuarioSemPeriodoFechamento();

    void registerListener(Listener listener);

    void exibirNomeFechamento(String nomeFechamento);

    void exibirDadosFechamento(FechamentoTurma fechamento);

    void exibirNomeTurmaDisciplina(String nomeTurma, String nomeDisciplina);

    ToolbarViewMvcImpl getToolbarViewMvcImpl(@Nullable ViewGroup parent);
}
