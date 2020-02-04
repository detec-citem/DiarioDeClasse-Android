package br.gov.sp.educacao.sed.mobile.Fechamento;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.gov.sp.educacao.sed.mobile.R;

class ToolbarViewMvcImpl {

    private TextView menu;

    private View mRootView;

    private LayoutInflater layoutInflater;

    private TextView mTxtTitle, mTxtEnviar;

    public ToolbarViewMvcImpl(LayoutInflater layoutInflater, ViewGroup parent) {

        setRootView(layoutInflater.inflate(R.layout.layout_toolbar, parent, false));

        mTxtTitle = findViewById(R.id.txt_toolbar_title);

        mTxtEnviar = findViewById(R.id.txt_toolbar_enviar);

        menu = findViewById(R.id.menu);

        this.layoutInflater = layoutInflater;
    }

    public View getEnviar() {

        return mTxtEnviar;
    }

    public View getRootView() {

        return mRootView;
    }

    public TextView getMenu() {

        return menu;
    }

    void setVisibilidadeEnviar() {

        mTxtEnviar.setVisibility(View.VISIBLE);
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

    public ToolbarViewMvcImpl getToolbarViewMvc(@Nullable ViewGroup parent) {

        return new ToolbarViewMvcImpl(layoutInflater, parent);
    }
}
