package br.gov.sp.educacao.sed.mobile.Carteirinha;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.gov.sp.educacao.sed.mobile.R;

class ToolbarViewMvcImpl {

    private TextView buscarCarteirinhas;

    private View mRootView;

    private TextView mTxtTitle;

    private LayoutInflater layoutInflater;

    public ToolbarViewMvcImpl(LayoutInflater layoutInflater, ViewGroup parent) {

        setRootView(layoutInflater.inflate(R.layout.layout_toolbar, parent, false));

        mTxtTitle = findViewById(R.id.txt_toolbar_title);

        buscarCarteirinhas = findViewById(R.id.buscarCarteirinhas);

        this.layoutInflater = layoutInflater;
    }

    void setBuscarCarteirinhasAtivo() {

        buscarCarteirinhas.setVisibility(View.VISIBLE);
    }

    TextView getBuscarCarteirinhas() {

        return buscarCarteirinhas;
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

    public ToolbarViewMvcImpl getToolbarViewMvc(@Nullable ViewGroup parent) {

        return new ToolbarViewMvcImpl(layoutInflater, parent);
    }
}
