package br.gov.sp.educacao.sed.mobile.RegistroDeAula;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import br.gov.sp.educacao.sed.mobile.R;

class HabilidadeItemViewMvcImpl
        implements HabilidadeItemViewMvc {

    private final View mRootView;

    private Listener listener;

    private Habilidade habilidade;

    private CheckBox check;

    private TextView txtHabilidade;

    HabilidadeItemViewMvcImpl(LayoutInflater layoutInflater, ViewGroup parent) {

        mRootView = layoutInflater.inflate(R.layout.layout_lista_habilidades, parent, false);

        check = findViewById(R.id.checkHabilidade);

        txtHabilidade = findViewById(R.id.txtHabilidade);

        mRootView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                listener.onHabilidadeSelecionada(habilidade);
            }
        });
    }

    private <T extends  View> T findViewById(int id) {

        return getRootView().findViewById(id);
    }

    @Override
    public View getRootView() {

        return mRootView;
    }

    @Override
    public void registerListener(Listener listener) {

        this.listener = listener;
    }

    @Override
    public void unregisterListener() {

        this.listener = null;
    }

    @Override
    public void exibirInfoHabilidade(Habilidade habilidade) {

        this.habilidade = habilidade;

        txtHabilidade.setText(habilidade.getDescricao());

        if(habilidade.isChecked()) {

            check.setChecked(true);
        }
        else {

            check.setChecked(false);
        }
    }
}
