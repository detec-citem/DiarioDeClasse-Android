package br.gov.sp.educacao.sed.mobile.Fechamento;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.widget.ArrayAdapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class FechamentoListaAdapter
        extends ArrayAdapter<FechamentoAluno> {

    public FechamentoListaAdapter(Context context) {

        super(context, 0);
    }

    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null) {

            FechamentoListaItemViewMvcImpl viewMvc = new FechamentoListaItemViewMvcImpl(LayoutInflater.from(getContext()), parent);

            convertView = viewMvc.getRootView();

            convertView.setTag(viewMvc);
        }

        final FechamentoAluno fechamentoAluno = getItem(position);

        FechamentoListaItemViewMvcImpl viewMvc = (FechamentoListaItemViewMvcImpl) convertView.getTag();

        viewMvc.exibirFechamentoAluno(fechamentoAluno);

        return convertView;
    }
}