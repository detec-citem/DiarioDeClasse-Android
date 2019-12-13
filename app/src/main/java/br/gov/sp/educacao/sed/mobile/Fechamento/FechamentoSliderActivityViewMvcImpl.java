package br.gov.sp.educacao.sed.mobile.Fechamento;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import br.gov.sp.educacao.sed.mobile.R;

class FechamentoSliderActivityViewMvcImpl
        implements FechamentoSliderActivityMvc {

    private Listener listener;

    private final View mRootView;

    private LayoutInflater layoutInflater;

    FechamentoSliderActivityViewMvcImpl(LayoutInflater layoutInflater, ViewGroup parent) {

        mRootView = layoutInflater.inflate(R.layout.activity_fechamento_slider, parent, false);

        this.layoutInflater = layoutInflater;
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
