package br.gov.sp.educacao.sed.mobile.RegistroDeAula;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ConteudoFundamentalAdapter extends ArrayAdapter<String> implements OnClickListener {
    //Interfaces
    public interface OnConteudoFundamentalSelecionadoListener {
        void onConteudoSelecionado(String conteudo);
    }

    //Variáveis
    private LayoutInflater layoutInflater;
    private OnConteudoFundamentalSelecionadoListener listener;

    //Construtor
    public ConteudoFundamentalAdapter(Context context, OnConteudoFundamentalSelecionadoListener onConteudoSelecionadoListener) {
        super(context, 0);
        layoutInflater = LayoutInflater.from(context);
        listener = onConteudoSelecionadoListener;
    }

    //ArrayAdapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ConteudoFundamentalViewHolder conteudoFundamentalViewHolder = null;
        if(convertView == null) {
            conteudoFundamentalViewHolder = new ConteudoFundamentalViewHolder(layoutInflater, parent);
            view = conteudoFundamentalViewHolder.getView();
            convertView = view;
            convertView.setTag(conteudoFundamentalViewHolder);
        }
        if (conteudoFundamentalViewHolder == null) {
            conteudoFundamentalViewHolder = (ConteudoFundamentalViewHolder) convertView.getTag();
            view = conteudoFundamentalViewHolder.getView();
        }
        String conteudo = getItem(position);
        conteudoFundamentalViewHolder.configurarCelula(false, position, conteudo);
        view.setOnClickListener(this);
        return convertView;
    }

    //OnClickListener
    @Override
    public void onClick(View view) {
        ConteudoFundamentalViewHolder conteudoFundamentalViewHolder = (ConteudoFundamentalViewHolder) view.getTag();
        int posicao = conteudoFundamentalViewHolder.getPosicao();
        listener.onConteudoSelecionado(getItem(posicao));
    }
}