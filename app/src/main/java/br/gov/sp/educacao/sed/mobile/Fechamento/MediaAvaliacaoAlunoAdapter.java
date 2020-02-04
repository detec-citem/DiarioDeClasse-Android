package br.gov.sp.educacao.sed.mobile.Fechamento;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;

public class MediaAvaliacaoAlunoAdapter
        extends ArrayAdapter<Aluno>
         implements MediaAdapterItemViewMvcImpl.Listener {

    MediaAvaliacaoAlunoAdapter(Context context) {

        super(context, 0);
    }

    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null) {

            MediaAdapterItemViewMvcImpl viewMvc = new MediaAdapterItemViewMvcImpl(LayoutInflater.from(getContext()), null);

            viewMvc.registerListener(this);

            convertView = viewMvc.getRootView();

            convertView.setTag(viewMvc);
        }

        final Aluno aluno = getItem(position);

        MediaAdapterItemViewMvcImpl viewMvc = (MediaAdapterItemViewMvcImpl) convertView.getTag();

        viewMvc.exibirMedias(aluno);

        return convertView;
    }

    @Override
    public void atualizarLista() {

        this.notifyDataSetChanged();
    }
}