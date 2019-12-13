package br.gov.sp.educacao.sed.mobile.Frequencia;

import android.view.View;

public interface HorariosItemViewMvc {

    interface Listener {

        void onHorarioSelecionado(String horario);

        void onHorarioChecado(String horario);

        void usuarioQuerEditarFrequencia(String horario);

        void usuarioQuerExcluirFrequencia(String horario);
    }

    View getRootView();

    void unregisterListener();

    void exibirHorario(String horario);

    void marcarHorarioComLancamento();

    void desmarcarHorarios();

    void checarHorario();

    void limparCheckBoxHorario();

    void registerListener(Listener listener);
}
