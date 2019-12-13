package br.gov.sp.educacao.sed.mobile.Frequencia;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.widget.ArrayAdapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;

public class ConsultaFrequenciaAdapter
        extends ArrayAdapter<Aluno> {

    ConsultaFrequenciaAdapter(Context context) {

        super(context,0);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null) {

            FrequenciaConsultaItemViewMvcImpl viewMvc = new FrequenciaConsultaItemViewMvcImpl(LayoutInflater.from(getContext()), parent);

            convertView = viewMvc.getRootView();

            convertView.setTag(viewMvc);
        }

        final Aluno aluno = getItem(position);

        FrequenciaConsultaItemViewMvcImpl viewMvc = (FrequenciaConsultaItemViewMvcImpl) convertView.getTag();

        viewMvc.exibirFaltasAluno(aluno);

        return convertView;
    }
}