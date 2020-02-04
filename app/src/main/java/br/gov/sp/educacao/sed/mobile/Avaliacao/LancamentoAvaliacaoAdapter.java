package br.gov.sp.educacao.sed.mobile.Avaliacao;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;

public class LancamentoAvaliacaoAdapter
        extends ArrayAdapter<Aluno>
         implements  ListaAlunosAvaliacoesItemViewMvc.Listener {

    private final OnAlunoAvaliacaoSelecionadoListener mOnAlunoAvaliacaoSelecionadoListener;

    public interface OnAlunoAvaliacaoSelecionadoListener {

        void alterarNota(Aluno aluno);
    }

    LancamentoAvaliacaoAdapter(Context context, OnAlunoAvaliacaoSelecionadoListener onAlunoAvaliacaoSelecionadoListener) {

        super(context, 0);

        mOnAlunoAvaliacaoSelecionadoListener = onAlunoAvaliacaoSelecionadoListener;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {

            ListaAlunosAvaliacoesItemViewMvcImpl alunosAvaliacoesItemViewMvc = new ListaAlunosAvaliacoesItemViewMvcImpl(LayoutInflater.from(getContext()), parent);

            alunosAvaliacoesItemViewMvc.registerListener(this);

            convertView = alunosAvaliacoesItemViewMvc.getRootView();

            convertView.setTag(alunosAvaliacoesItemViewMvc);
        }

        final Aluno aluno = getItem(position);

        ListaAlunosAvaliacoesItemViewMvc alunosAvaliacoesItemViewMvc = (ListaAlunosAvaliacoesItemViewMvc) convertView.getTag();

        alunosAvaliacoesItemViewMvc.exibirInfoAlunoAvaliacao(aluno);

        return convertView;
    }

    @Override
    public void alterarNota(Aluno aluno) {

        mOnAlunoAvaliacaoSelecionadoListener.alterarNota(aluno);
    }
}