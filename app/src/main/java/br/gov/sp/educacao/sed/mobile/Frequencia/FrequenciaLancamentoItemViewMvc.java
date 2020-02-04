package br.gov.sp.educacao.sed.mobile.Frequencia;

import android.view.View;

import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;

interface FrequenciaLancamentoItemViewMvc {

    interface Listener {

        void aplicarNA(Aluno aluno);

        void aplicarFalta(Aluno aluno);

        void aplicarPresenca(Aluno aluno);

        void irParaProximoAlunoAtivo(int posicao);
    }

    View getRootView();

    void unregisterListener();

    void registerListener(Listener listener);

    void exibirInfoAluno(Aluno aluno, TurmaGrupo turmaGrupo);
}
