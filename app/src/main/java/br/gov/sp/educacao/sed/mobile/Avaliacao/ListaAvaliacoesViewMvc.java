package br.gov.sp.educacao.sed.mobile.Avaliacao;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

interface ListaAvaliacoesViewMvc {

    interface Listener {

        void onBackPressed();

        void deletarAvaliacao();

        void navegarPara(Intent intent);

        void usuarioQuerCriarAvaliacao();

        void usuarioSelecionouDisciplina(int i);

        void onAvaliacaoSelecionada(Avaliacao avaliacao);

        void usuarioQuerDeletarAvaliacao(Avaliacao avaliacao);

        void usuarioSelecionouBimestre(int bimestreSelecionado);

        void configurarDialogEditarAvaliacao(Avaliacao avaliacao);

        void usuarioSelecionouTipoAtividade(String tipoAtividade);
    }

    View getRootView();

    void unregisterListener();

    void exibirAvisoNenhumaProva();

    void exibirAvisoApenasUmaProva();

    void exibirDialogDeletarAvaliacao();

    void inicializarBotaoNovaAvaliacao();

    void registerListener(Listener listener);

    void exibirSelecaoDisciplinaAnosIniciais(int serie);

    ToolbarViewMvcImpl getToolbarViewMvcImpl(@Nullable ViewGroup parent);
}
