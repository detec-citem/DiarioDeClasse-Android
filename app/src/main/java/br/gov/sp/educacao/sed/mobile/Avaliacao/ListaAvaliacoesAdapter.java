package br.gov.sp.educacao.sed.mobile.Avaliacao;

import java.util.List;
import java.util.ArrayList;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import br.gov.sp.educacao.sed.mobile.R;

import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.AnimationUtils;

import android.widget.ArrayAdapter;

import android.support.v4.view.animation.LinearOutSlowInInterpolator;

public class ListaAvaliacoesAdapter
        extends ArrayAdapter<Avaliacao>
         implements ListaAvaliacoesItemViewMvc.Listener {

    private List<View> lista;

    public String TAG = ListaAvaliacoesAdapter.class.getSimpleName();

    private final OnAvaliacaoSelecionadaListener mOnAvaliacaoSelecionadaListener;

    public interface OnAvaliacaoSelecionadaListener {

        void onAvaliacaoSelecionada(Avaliacao avaliacao);

        void usuarioQuerEditarAvaliacao(Avaliacao avaliacao);

        void usuarioQuerDeletarAvaliacao(Avaliacao avaliacao);
    }

    ListaAvaliacoesAdapter(Context context, OnAvaliacaoSelecionadaListener onAvaliacaoSelecionadaListener) {

        super(context, 0);

        lista = new ArrayList<>();

        mOnAvaliacaoSelecionadaListener = onAvaliacaoSelecionadaListener;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {

            ListaAvaliacoesItemViewMvcImpl viewMvc = new ListaAvaliacoesItemViewMvcImpl(LayoutInflater.from(getContext()), parent);

            viewMvc.registerListener(this);

            convertView = viewMvc.getRootView();

            convertView.setTag(viewMvc);
        }

        final Avaliacao avaliacao = getItem(position);

        final ListaAvaliacoesItemViewMvc viewMvc = (ListaAvaliacoesItemViewMvc) convertView.getTag();

        viewMvc.exibirInfoAvaliacao(avaliacao);

        Animation animation;

        animation = AnimationUtils.loadAnimation(getContext(), R.anim.move);

        Interpolator interpolator = new LinearOutSlowInInterpolator();

        animation.setInterpolator(interpolator);

        animation.setStartOffset(150);

        convertView.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

                //Mudar nome
                viewMvc.comecaai();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        lista.add(convertView);

        return convertView;
    }

    public void ativarBotoes() {

        for(int i = 0; i < lista.size(); i++) {

            lista.get(i).setClickable(true);
        }
    }

    @Override
    public void onAvaliacaoSelecionada(Avaliacao avaliacao) {

        mOnAvaliacaoSelecionadaListener.onAvaliacaoSelecionada(avaliacao);
    }

    @Override
    public void usuarioQuerEditarAvaliacao(Avaliacao avaliacao) {

        mOnAvaliacaoSelecionadaListener.usuarioQuerEditarAvaliacao(avaliacao);
    }

    @Override
    public void usuarioQuerDeletarAvaliacao(Avaliacao avaliacao) {

        mOnAvaliacaoSelecionadaListener.usuarioQuerDeletarAvaliacao(avaliacao);
    }
}