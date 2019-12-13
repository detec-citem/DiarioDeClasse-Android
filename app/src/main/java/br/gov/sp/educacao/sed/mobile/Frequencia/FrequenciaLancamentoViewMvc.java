package br.gov.sp.educacao.sed.mobile.Frequencia;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;

import android.content.Intent;

import android.support.annotation.Nullable;

import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;

interface FrequenciaLancamentoViewMvc {

    interface Listener {

        void onBackPressed();

        void aplicarNA(Aluno aluno);

        void inicializarCalendario();

        void usuarioClicouConfirmar();

        void aplicarFalta(Aluno aluno);

        void usuarioQuerExcluirFrequencia(String horario);

        void excluirFrequencia(String horario);

        //void fecharSelecaoHorarios();

        void excluirFrequenciaNoBancoLocal();

        void navegarPara(Intent intent);

        void aplicarPresenca(Aluno aluno);

        void resgatarFaltasAluno(Aluno aluno);

        void irParaProximoAlunoAtivo(Aluno aluno);

        void usuarioQuerReplicarChamada(String data);

        void aplicarPresencaParaTodosAlunos(View view);

        void usuarioSelecionouHorario(String horarioAula);

        void replicarChamadaMultiplosHorarios(String data, String horarioAula);

        List<String> pegarHorariosComLancamentos();

        void usuarioChecouHorario(String horario);

        void usuarioQuerFecharSelecaoCalendario();

        void usuarioQuerFecharSelecaoHorarios();

        void usuarioQuerAvancar();
    }

    View getRootView();

    void unregisterListener();

    void avisarUsuarioChamadaRealizada();

    void avisarUsuarioChamadaIncompleta();

    void registerListener(Listener listener);

    ToolbarViewMvcImpl getToolbarViewMvcImpl(@Nullable ViewGroup parent);

    void exibirListaAlunos(List<Aluno> listaAlunos, TurmaGrupo turmaGrupo);
}
