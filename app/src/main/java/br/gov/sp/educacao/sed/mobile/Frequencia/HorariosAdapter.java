package br.gov.sp.educacao.sed.mobile.Frequencia;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class HorariosAdapter
        extends ArrayAdapter<String>
         implements HorariosItemViewMvc.Listener {

    private final OnHorarioSelecionadoListener mOnHorarioSelecionadoListener;

    private List<String> listaHorariosComLancamento;

    private List<String> listaHorariosSelecionados;

    public interface OnHorarioSelecionadoListener {

        void onHorarioSelecionado(String horario);

        void onHorarioChecado(String horario);

        void usuarioQuerEditarFrequencia(String horario);

        void usuarioQuerExcluirFrequencia(String horario);
    }

    HorariosAdapter(Context context, List<String> listaHorariosComLancamentos, List<String> listaHorariosSelecionados, OnHorarioSelecionadoListener onHorarioSelecionadoListener) {

        super(context, 0);

        this.listaHorariosComLancamento = listaHorariosComLancamentos;

        this.listaHorariosSelecionados = listaHorariosSelecionados;

        mOnHorarioSelecionadoListener = onHorarioSelecionadoListener;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null) {

            HorariosItemViewMvcImpl viewMvc = new HorariosItemViewMvcImpl(LayoutInflater.from(getContext()), parent);

            viewMvc.registerListener(this);

            convertView = viewMvc.getRootView();

            convertView.setTag(viewMvc);
        }

        final String horario = getItem(position);

        HorariosItemViewMvcImpl viewMvc = (HorariosItemViewMvcImpl) convertView.getTag();

        viewMvc.exibirHorario(horario);

        viewMvc.desmarcarHorarios();

        if(listaHorariosComLancamento.contains(horario)) {

            viewMvc.marcarHorarioComLancamento();
        }

        viewMvc.limparCheckBoxHorario();

        if(listaHorariosSelecionados.contains(horario)) {

            viewMvc.checarHorario();
        }

        return convertView;
    }

    public void deschecarHorario(int posicao) {

    }

    @Override
    public void onHorarioSelecionado(String horario) {

        mOnHorarioSelecionadoListener.onHorarioSelecionado(horario);
    }

    @Override
    public void onHorarioChecado(String horario) {

        mOnHorarioSelecionadoListener.onHorarioChecado(horario);
    }

    @Override
    public void usuarioQuerEditarFrequencia(String horario) {

        mOnHorarioSelecionadoListener.usuarioQuerEditarFrequencia(horario);
    }

    @Override
    public void usuarioQuerExcluirFrequencia(String horario) {

        mOnHorarioSelecionadoListener.usuarioQuerExcluirFrequencia(horario);
    }
}
