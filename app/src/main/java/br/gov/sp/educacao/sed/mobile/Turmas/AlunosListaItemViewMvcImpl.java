package br.gov.sp.educacao.sed.mobile.Turmas;

import android.widget.TextView;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import br.gov.sp.educacao.sed.mobile.R;

class AlunosListaItemViewMvcImpl
        implements AlunosListaItemViewMvc {

    private Aluno aluno;

    private TextView tvAluno;

    private Listener listener;

    private final View mRootView;

    private TextView tvStatusAluno;

    private TextView tvPosicaoAlunoTurma;

    private String TAG = AlunosListaItemViewMvcImpl.class.getSimpleName();

    AlunosListaItemViewMvcImpl(LayoutInflater layoutInflater, ViewGroup parent) {

        mRootView = layoutInflater.inflate(R.layout.layout_aluno, parent, false);

        tvAluno = findViewById(R.id.tv_aluno);

        tvPosicaoAlunoTurma = findViewById(R.id.tv_posicao_aluno_turma);

        tvStatusAluno = findViewById(R.id.tv_status_aluno);

        getRootView().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                getRootView().setClickable(false);

                listener.onAlunoSelecionado(aluno);
            }
        });
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
    public void registerListener(Listener listener) {

        this.listener = listener;
    }

    private <T extends  View> T findViewById(int id) {

        return getRootView().findViewById(id);
    }

    @Override
    public void exibirInfoAluno(Aluno aluno, int totalAlunos) {

        this.aluno = aluno;

        tvAluno.setText(aluno.getNomeAluno());

        String numeroChamada = ("N° Chamada: " + String.valueOf(aluno.getNumeroChamada()));

        tvPosicaoAlunoTurma.setText(numeroChamada);

        String statusAluno;

        if(aluno.getAlunoAtivo()) {

            statusAluno = "Status: Ativo";

            tvStatusAluno.setText(statusAluno);
        }
        else {

            statusAluno = "Status: Inativo";

            tvStatusAluno.setText(statusAluno);
        }
    }
}
