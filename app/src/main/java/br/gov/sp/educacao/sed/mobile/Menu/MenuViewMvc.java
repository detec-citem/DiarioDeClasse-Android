package br.gov.sp.educacao.sed.mobile.Menu;

import android.view.Menu;
import android.view.MenuInflater;

public class MenuViewMvc {

    private Menu mRootView;

    private MenuInflater menuInflater;

    public MenuViewMvc(MenuInflater menuInflater) {

        this.menuInflater = menuInflater;
    }

    public MenuViewMvc MenuViewMvc(MenuInflater menuInflater) {

        return new MenuViewMvc(menuInflater);
    }
}
