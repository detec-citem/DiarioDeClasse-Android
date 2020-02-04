package br.gov.sp.educacao.sed.mobile.Turmas;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class TurmasListaAdapter
        extends ArrayAdapter<TurmaGrupo>
         implements TurmasListaItemViewMvc.Listener {

    private List<View> lista;

    private final OnTurmaSelecionadaListener mOnTurmaSelecionadaListener;

    public interface OnTurmaSelecionadaListener {

        void onTurmaSelecionada(TurmaGrupo turmaGrupo);
    }

    TurmasListaAdapter(Context context, OnTurmaSelecionadaListener onTurmaSelecionadaListener) {

        super(context, 0);

        lista = new ArrayList<>();

        mOnTurmaSelecionadaListener = onTurmaSelecionadaListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null) {

            TurmasListaItemViewMvc viewMvc = new TurmasListaItemViewMvcImpl(LayoutInflater.from(getContext()), parent);

            viewMvc.registerListener(this);

            convertView = viewMvc.getRootView();

            convertView.setTag(viewMvc);
        }

        final TurmaGrupo turmaGrupo = getItem(position);

        TurmasListaItemViewMvc viewMvc = (TurmasListaItemViewMvc) convertView.getTag();

        viewMvc.exibirInfoTurma(turmaGrupo);

        lista.add(convertView);

        return convertView;
    }

    //Mudar nome
    public void ativarBotao() {

        for(int i = 0; i < lista.size(); i++) {

            lista.get(i).setClickable(true);
        }
    }
    //MudarNome
    public void limparLista() {

        lista.clear();
    }

    @Override
    public void onTurmaSelecionada(TurmaGrupo turmaGrupo) {

        mOnTurmaSelecionadaListener.onTurmaSelecionada(turmaGrupo);
    }
}
