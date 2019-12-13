package br.gov.sp.educacao.sed.mobile.Menu;

import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.support.v4.app.Fragment;

import br.gov.sp.educacao.sed.mobile.R;

public class MenuLateral extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.menu_lateral, container, false);

        return rootView;
    }
}