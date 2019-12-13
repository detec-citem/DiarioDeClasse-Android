package br.gov.sp.educacao.sed.mobile.Menu;

import android.widget.TextView;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import br.gov.sp.educacao.sed.mobile.R;

import android.support.annotation.Nullable;

class ToolbarViewMvc {

    private TextView menu;

    private View mRootView;

    private TextView mTxtTitle;

    private LayoutInflater layoutInflater;

    public ToolbarViewMvc(LayoutInflater layoutInflater, ViewGroup parent) {

        setRootView(layoutInflater.inflate(R.layout.layout_toolbar, parent, false));

        mTxtTitle = findViewById(R.id.txt_toolbar_title);

        menu = findViewById(R.id.menu);

        this.layoutInflater = layoutInflater;
    }

    public TextView getMenu() {

        return menu;
    }

    public View getRootView() {

        return mRootView;
    }

    public void setTitle(String title) {

        mTxtTitle.setText(title);
    }

    private void setRootView(View rootView) {

        mRootView = rootView;
    }

    private <T extends View> T findViewById(int id) {

        return getRootView().findViewById(id);
    }

    public ToolbarViewMvc getToolbarViewMvc(@Nullable ViewGroup parent) {

        return new ToolbarViewMvc(layoutInflater, parent);
    }
}
