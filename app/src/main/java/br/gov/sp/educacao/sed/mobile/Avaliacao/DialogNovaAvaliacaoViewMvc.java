package br.gov.sp.educacao.sed.mobile.Avaliacao;

import android.app.Dialog;

interface DialogNovaAvaliacaoViewMvc {

    interface Listener {

        void ativarBotao();

        void inicializarCalendario();

        boolean mediaCalculada();

        void usuarioQuerEditarAvaliacao(String nomeAvaliacao, String dataAvaliacao, String tipoAtividade, boolean valeNota);

        void usuarioQuerSalvarAvaliacao(String nomeAvaliacao, String dataAvaliacao, String tipoAtividade, boolean valeNota);
    }

    Dialog getRootView();

    void removerDialog();

    void unregisterListener();

    void registerListener(Listener listener);

    void usuarioSelecionouData(String data, String bimestre);

    void exibirDadosAvaliacao(String nomeAvaliacao, String dataAvaliacao, boolean valeNota, int tipoAtividade);
}
