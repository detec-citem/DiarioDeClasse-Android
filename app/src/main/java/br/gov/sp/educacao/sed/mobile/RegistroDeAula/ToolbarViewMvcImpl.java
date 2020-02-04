package br.gov.sp.educacao.sed.mobile.RegistroDeAula;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.gov.sp.educacao.sed.mobile.R;

class ToolbarViewMvcImpl {

    private View mRootView;

    private LayoutInflater layoutInflater;

    private TextView mTxtTitle;

    private TextView menu;

    public ToolbarViewMvcImpl(LayoutInflater layoutInflater, ViewGroup parent) {

        setRootView(layoutInflater.inflate(R.layout.layout_toolbar, parent, false));

        mTxtTitle = findViewById(R.id.txt_toolbar_title);

        menu = findViewById(R.id.menu);

        this.layoutInflater = layoutInflater;
    }

    public void setRootView(View rootView) {

        mRootView = rootView;
    }

    public void setTitle(String title) {

        mTxtTitle.setText(title);
    }

    public TextView getMenu() {

        return menu;
    }

    private <T extends View> T findViewById(int id) {

        return getRootView().findViewById(id);
    }

    public View getRootView() {

        return mRootView;
    }

    public ToolbarViewMvcImpl getToolbarViewMvc(@Nullable ViewGroup parent) {

        return new ToolbarViewMvcImpl(layoutInflater, parent);
    }
}
