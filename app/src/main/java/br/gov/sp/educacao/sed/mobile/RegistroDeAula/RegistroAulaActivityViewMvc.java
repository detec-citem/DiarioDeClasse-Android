package br.gov.sp.educacao.sed.mobile.RegistroDeAula;

import android.content.Intent;
import android.support.annotation.Nullable;

import android.view.View;
import android.view.ViewGroup;

interface RegistroAulaActivityViewMvc {

    interface Listener {

        void onBackPressed();

        void usuarioSelecionouConteudo(Conteudo conteudo);

        void usuarioSelecionouHabilidade(Habilidade habilidade);

        void usuarioSelecionouBloco(int bloco);

        void salvarRegistro(String observacoes);

        void abrirOpcoesAnosIniciais();

        void usuarioSelecionouMateriaAnosIniciais(int disciplina);

        void navegarPara(Intent intent);

        void usuarioQuerFecharSelecaoHorarios();

        void usuarioSelecionouHorario(String horario);

        void usuarioChecouHorario(String horario);

        void usuarioQuerAvancar();

        void usuarioClicouSairRegistro();
    }

    View getRootView();

    void registerListener(Listener listener);

    void unregisterListener();

    ToolbarViewMvcImpl getToolbarViewMvcImpl(@Nullable ViewGroup parent);

    void usuarioClicouBotaoSalvarRegistro();

    void avisoAlteracoesPendentes();

    void avisoUsuarioNenhumConteudoParaExibir();
}
