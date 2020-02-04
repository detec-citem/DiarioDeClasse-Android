package br.gov.sp.educacao.sed.mobile.RegistroDeAula;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import br.gov.sp.educacao.sed.mobile.Menu.ConteudoFundamental;

public class HabilidadeFundamentalAdapter extends ArrayAdapter<ConteudoFundamental> implements OnClickListener {
    //Interfaces
    public interface OnHabilidadeFundamentalSelecionadoListener {
        void onHabilidadeSelecionada(ConteudoFundamental habilidade);
    }

    //Variáveis
    private LayoutInflater layoutInflater;
    private OnHabilidadeFundamentalSelecionadoListener listener;

    //Construtor
    public HabilidadeFundamentalAdapter(Context context, OnHabilidadeFundamentalSelecionadoListener onHabilidadeFundamentalSelecionadoListener) {
        super(context, 0);
        layoutInflater = LayoutInflater.from(context);
        listener = onHabilidadeFundamentalSelecionadoListener;
    }

    //ArrayAdapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        HabilidadeFundamentalViewHolder habilidadeFundamentalViewHolder = null;
        if(convertView == null) {
            habilidadeFundamentalViewHolder = new HabilidadeFundamentalViewHolder(layoutInflater, parent);
            view = habilidadeFundamentalViewHolder.getView();
            convertView = view;
            convertView.setTag(habilidadeFundamentalViewHolder);
        }
        if (habilidadeFundamentalViewHolder == null) {
            habilidadeFundamentalViewHolder = (HabilidadeFundamentalViewHolder) convertView.getTag();
            view = habilidadeFundamentalViewHolder.getView();
        }
        ConteudoFundamental habilidade = getItem(position);
        habilidadeFundamentalViewHolder.configurarCelula(position, habilidade);
        view.setOnClickListener(this);
        return convertView;
    }

    //OnClickListener
    @Override
    public void onClick(View view) {
        HabilidadeFundamentalViewHolder habilidadeFundamentalViewHolder = (HabilidadeFundamentalViewHolder) view.getTag();
        int posicao = habilidadeFundamentalViewHolder.getPosicao();
        ConteudoFundamental habilidade = getItem(posicao);
        listener.onHabilidadeSelecionada(habilidade);
    }
}