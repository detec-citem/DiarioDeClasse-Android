package br.gov.sp.educacao.sed.mobile.RegistroDeAula;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;

public class HabilidadesRegistroAdapter
        extends ArrayAdapter<Habilidade>
         implements HabilidadeItemViewMvc.Listener {

    private final OnHabilidadeSelecionadaListener mOnHabilidadeSelecionadaListener;

    public interface OnHabilidadeSelecionadaListener {

        void onHabilidadeSelecionada(Habilidade habilidade);
    }

    public HabilidadesRegistroAdapter(Context context, OnHabilidadeSelecionadaListener onHabilidadeSelecionadaListener) {

        super(context, 0);

        mOnHabilidadeSelecionadaListener = onHabilidadeSelecionadaListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null) {

            HabilidadeItemViewMvcImpl viewMvc = new HabilidadeItemViewMvcImpl(LayoutInflater.from(getContext()), parent);

            viewMvc.registerListener(this);

            convertView = viewMvc.getRootView();

            convertView.setTag(viewMvc);
        }

        final Habilidade habilidade = getItem(position);

        HabilidadeItemViewMvcImpl viewMvc = (HabilidadeItemViewMvcImpl) convertView.getTag();

        viewMvc.exibirInfoHabilidade(habilidade);

        return convertView;
    }

    @Override
    public void onHabilidadeSelecionada(Habilidade habilidade) {

        mOnHabilidadeSelecionadaListener.onHabilidadeSelecionada(habilidade);
    }
}
