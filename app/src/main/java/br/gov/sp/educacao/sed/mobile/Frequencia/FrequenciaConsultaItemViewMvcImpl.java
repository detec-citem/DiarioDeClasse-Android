package br.gov.sp.educacao.sed.mobile.Frequencia;

import android.widget.TextView;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import br.gov.sp.educacao.sed.mobile.R;

import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;

class FrequenciaConsultaItemViewMvcImpl
        implements FrequenciaConsultaItemViewMvc {

    private TextView tvAluno;

    private Listener listener;

    private TextView faltasAno;

    private final View mRootView;

    private TextView faltasBimestre;

    FrequenciaConsultaItemViewMvcImpl(LayoutInflater layoutInflater, ViewGroup parent) {

        mRootView = layoutInflater.inflate(R.layout.layout_consulta_frequencia, parent, false);

        tvAluno = findViewById(R.id.tv_aluno);

        faltasAno = findViewById(R.id.faltasAno);

        faltasBimestre = findViewById(R.id.faltasBimestre);
    }

    @Override
    public View getRootView() {

        return mRootView;
    }

    @Override
    public void unregisterListener() {

        this.listener = null;
    }

    @Override
    public void exibirFaltasAluno(Aluno aluno) {

        String textAluno = aluno.getNumeroChamada() + " - " + aluno.getNomeAluno();

        tvAluno.setText(textAluno);

        String faltasBimestre = "Faltas Bimestre: " + aluno.getFaltasBimestre();

        this.faltasBimestre.setText(faltasBimestre);

        String faltasAno = "Faltas Ano: " + aluno.getFaltasAnuais();

        this.faltasAno.setText(faltasAno);
    }

    @Override
    public void registerListener(Listener listener) {

        this.listener = listener;
    }

    private <T extends  View> T findViewById(int id) {

        return getRootView().findViewById(id);
    }
}
