package br.gov.sp.educacao.sed.mobile.Avaliacao;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import br.gov.sp.educacao.sed.mobile.R;

class AvaliacoesSliderViewMvcImpl
        implements AvaliacoesSliderViewMvc {

    private Listener listener;

    private final View mRootView;

    private LayoutInflater inflater;

    AvaliacoesSliderViewMvcImpl(LayoutInflater inflater, ViewGroup parent) {

        mRootView = inflater.inflate(R.layout.activity_avaliacoes_slider, parent, false);

        this.inflater = inflater;
    }

    @Override
    public View getRootView() {

        return mRootView;
    }

    private Context getContext() {

        return getRootView().getContext();
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
}
