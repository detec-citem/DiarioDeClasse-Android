package br.gov.sp.educacao.sed.mobile.Turmas;

import java.util.List;
import java.util.ArrayList;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.widget.ArrayAdapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class AlunoAdapter
        extends ArrayAdapter<Aluno>
         implements AlunosListaItemViewMvc.Listener{

    private final OnAlunoSelecionadoListener mOnAlunoSelecionadoListener;

    private List<View> lista;

    public interface OnAlunoSelecionadoListener {

        void onAlunoSelecionado(Aluno aluno);
    }

    AlunoAdapter(Context context, OnAlunoSelecionadoListener onAlunoSelecionadoListener) {

        super(context, 0);

        lista = new ArrayList<>();

        mOnAlunoSelecionadoListener = onAlunoSelecionadoListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        int totalAlunos = this.getCount();

        if(convertView == null) {

            AlunosListaItemViewMvc viewMvc = new AlunosListaItemViewMvcImpl(LayoutInflater.from(getContext()), parent);

            viewMvc.registerListener(this);

            convertView = viewMvc.getRootView();

            convertView.setTag(viewMvc);
        }

        final Aluno aluno = getItem(position);

        AlunosListaItemViewMvc viewMvc = (AlunosListaItemViewMvc) convertView.getTag();

        viewMvc.exibirInfoAluno(aluno, totalAlunos);

        lista.add(convertView);

        return convertView;
    }

    @Override
    public void onAlunoSelecionado(Aluno aluno) {

        mOnAlunoSelecionadoListener.onAlunoSelecionado(aluno);
    }

    //Mudar Nome
    public void ativarBotao() {

        for(int i = 0; i < lista.size(); i++) {

            lista.get(i).setClickable(true);
        }
    }

    //Mudar Nome
    public void limparLista() {

        lista.clear();
    }
}