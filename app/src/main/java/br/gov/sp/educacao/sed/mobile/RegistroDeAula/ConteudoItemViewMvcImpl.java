package br.gov.sp.educacao.sed.mobile.RegistroDeAula;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CheckBox;
import android.widget.TextView;

import br.gov.sp.educacao.sed.mobile.R;

class ConteudoItemViewMvcImpl
        implements ConteudoItemViewMvc {

    private final View mRootView;

    private Conteudo conteudo;

    private Listener listener;

    private TextView txtConteudo;

    private CheckBox chckHabSelec;

    ConteudoItemViewMvcImpl(LayoutInflater layoutInflater, ViewGroup parent) {

        mRootView = layoutInflater.inflate(R.layout.layout_lista_conteudo , parent, false);

        txtConteudo = findViewById(R.id.txt_layout_conteudo);

        chckHabSelec = findViewById(R.id.check_layout_conteudo);

        mRootView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                listener.onConteudoSelecionado(conteudo);
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
    public void exibirInfoConteudo(Conteudo conteudo) {

        this.conteudo = conteudo;

        chckHabSelec.setChecked(conteudo.isHabilidadeCheck());

        txtConteudo.setText(conteudo.getDescricao());
    }
}
