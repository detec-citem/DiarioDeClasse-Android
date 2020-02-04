package br.gov.sp.educacao.sed.mobile.Frequencia;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;

public class LancamentoAlunoAdapter
        extends ArrayAdapter<Aluno>
         implements FrequenciaLancamentoItemViewMvcImpl.Listener {

    private TurmaGrupo turmaGrupo;

    private final String TAG = "Frequência: ";

    private final OnFrequenciaSelecionadaListener mOnFrequenciaSelecionadaListener;

    public interface OnFrequenciaSelecionadaListener {

        void aplicarNA(Aluno aluno);

        void aplicarFalta(Aluno aluno);

        void aplicarPresenca(Aluno aluno);

        void resgatarFaltasAluno(Aluno aluno);

        void irParaProximoAlunoAtivo(int posicao);
    }

    LancamentoAlunoAdapter(Context context, OnFrequenciaSelecionadaListener onFrequenciaSelecionadaListener, TurmaGrupo turmaGrupo){

        super(context, 0);

        this.turmaGrupo = turmaGrupo;

        mOnFrequenciaSelecionadaListener = onFrequenciaSelecionadaListener;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {

        if(convertView == null) {

            FrequenciaLancamentoItemViewMvcImpl viewMvc = new FrequenciaLancamentoItemViewMvcImpl(LayoutInflater.from(getContext()), parent);

            viewMvc.registerListener(this);

            convertView = viewMvc.getRootView();

            convertView.setTag(viewMvc);
        }

        final Aluno aluno = getItem(position);

        FrequenciaLancamentoItemViewMvcImpl viewMvc = (FrequenciaLancamentoItemViewMvcImpl) convertView.getTag();

        viewMvc.setPosition(position);

        mOnFrequenciaSelecionadaListener.resgatarFaltasAluno(aluno);

        viewMvc.exibirInfoAluno(aluno, turmaGrupo);

        return convertView;
    }

    @Override
    public void aplicarNA(Aluno aluno) {

        mOnFrequenciaSelecionadaListener.aplicarNA(aluno);
    }

    @Override
    public void aplicarFalta(Aluno aluno) {

        mOnFrequenciaSelecionadaListener.aplicarFalta(aluno);
    }

    @Override
    public void aplicarPresenca(Aluno aluno) {

        mOnFrequenciaSelecionadaListener.aplicarPresenca(aluno);
    }

    @Override
    public void irParaProximoAlunoAtivo(int posicao) {

        mOnFrequenciaSelecionadaListener.irParaProximoAlunoAtivo(posicao);
    }
}