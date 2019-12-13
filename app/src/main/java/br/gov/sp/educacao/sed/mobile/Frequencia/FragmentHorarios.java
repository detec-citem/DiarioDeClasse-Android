package br.gov.sp.educacao.sed.mobile.Frequencia;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.gov.sp.educacao.sed.mobile.R;

public class FragmentHorarios
        extends Fragment
         implements HorariosAdapter.OnHorarioSelecionadoListener {

    private View mRootView;

    private ImageView fecharSelecaoHorarios, botaoAvancar;

    private fragmentHorariosListener listener;

    private HorariosAdapter horariosAdapter;

    private List<String> listaHorarios;

    private ListView listaDeHorarios;

    private List<String> listaHorariosSelecionados;

    private ArrayList<String> listaHorariosComLancamento;

    public interface fragmentHorariosListener {

        void usuarioQuerFecharSelecaoHorarios();

        void onHorarioSelecionado(String horario);

        void onHorarioChecado(String horario);

        void usuarioQuerEditarFrequencia(String horario);

        void usuarioQuerExcluirFrequencia(String horario);

        void usuarioQuerAvancar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.selecaohorarios2, container, false);

        fecharSelecaoHorarios = mRootView.findViewById(R.id.fecharselecaohorarios);

        botaoAvancar = mRootView.findViewById(R.id.avancar);

        listaDeHorarios = mRootView.findViewById(R.id.listaHorarios);

        Bundle bundle = getArguments();

        listaHorarios = bundle.getStringArrayList("lista");

        listaHorariosComLancamento = bundle.getStringArrayList("lista2");

        listaHorariosSelecionados = new ArrayList<>();

        horariosAdapter = new HorariosAdapter(getContext(), listaHorariosComLancamento, listaHorariosSelecionados, this);

        horariosAdapter.addAll(listaHorarios);

        listaDeHorarios.setAdapter(horariosAdapter);

        fecharSelecaoHorarios();

        inicializarListenerAvancar();

        return mRootView;
    }

    private void inicializarListenerAvancar() {

        botaoAvancar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                listener.usuarioQuerAvancar();
            }
        });
    }

    public void deschecarUltimoHorarioChecado(String horario) {

        int id = 0;

        for(int i = 0; i < listaHorarios.size(); i++) {

            if(listaHorarios.get(i).equals(horario)) {

                id = i;

                break;
            }
        }

        View view = null;

        view = listaDeHorarios.getChildAt(id);

        CheckBox checkBox = view.findViewById(R.id.checkBoxHorario);

        checkBox.setChecked(false);
    }

    public void desmarcarHorario(String horario) {

        int id = 0;

        for(int i = 0; i < listaHorarios.size(); i++) {

            if(listaHorarios.get(i).equals(horario)) {

                id = i;

                break;
            }
        }

        View view = null;

        view = listaDeHorarios.getChildAt(id);

        ConstraintLayout constraintLayout = view.findViewById(R.id.constraintId);

        constraintLayout.setBackgroundColor(getResources().getColor(R.color.transparente));
    }

    public void atualizarListaHorariosComLancamento(List<String> listaHorariosComLancamento) {

        this.listaHorariosComLancamento.clear();

        this.listaHorariosComLancamento.addAll(listaHorariosComLancamento);

        horariosAdapter.notifyDataSetChanged();
    }

    public void atualizarListHorariosSelecionados(List<String> listaHorariosSelecionados) {

        this.listaHorariosSelecionados.clear();

        this.listaHorariosSelecionados.addAll(listaHorariosSelecionados);

        horariosAdapter.notifyDataSetChanged();
    }

    public void registerListener(fragmentHorariosListener listener) {

        this.listener = listener;
    }

    public void unregisterListener() {

        this.listener = null;
    }

    public void fecharSelecaoHorarios() {

        fecharSelecaoHorarios.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                listener.usuarioQuerFecharSelecaoHorarios();
            }
        });
    }

    public void usuarioQuerAvancar() {

        listener.usuarioQuerAvancar();
    }

    public void usuarioQuerExcluirFrequencia(String horario) {

        listener.usuarioQuerExcluirFrequencia(horario);
    }

    public void onHorarioChecado(String horario) {

        listener.onHorarioChecado(horario);
    }

    public void onHorarioSelecionado(String horario) {

        listener.onHorarioSelecionado(horario);
    }

    public void usuarioQuerEditarFrequencia(String horario) {


    }
}
