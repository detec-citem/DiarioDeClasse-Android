package br.gov.sp.educacao.sed.mobile.RegistroDeAula;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;

public class ConteudoRegistroAdapter
        extends ArrayAdapter<Conteudo>
         implements ConteudoItemViewMvc.Listener {

    private final OnConteudoSelecionadoListener mOnConteudoSelecionadoListener;

    public interface OnConteudoSelecionadoListener {

        void onConteudoSelecionado(Conteudo conteudo);
    }

    public ConteudoRegistroAdapter(Context context, OnConteudoSelecionadoListener onConteudoSelecionadoListener) {

        super(context, 0);

        mOnConteudoSelecionadoListener = onConteudoSelecionadoListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null) {

            ConteudoItemViewMvcImpl viewMvc = new ConteudoItemViewMvcImpl(LayoutInflater.from(getContext()), parent);

            viewMvc.registerListener(this);

            convertView = viewMvc.getRootView();

            convertView.setTag(viewMvc);
        }

        Conteudo conteudo = getItem(position);

        ConteudoItemViewMvcImpl viewMvc = (ConteudoItemViewMvcImpl) convertView.getTag();

        viewMvc.exibirInfoConteudo(conteudo);

        return convertView;
    }

    @Override
    public void onConteudoSelecionado(Conteudo conteudo) {

        mOnConteudoSelecionadoListener.onConteudoSelecionado(conteudo);
    }
}
