package br.gov.sp.educacao.sed.mobile.Frequencia;

import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import br.gov.sp.educacao.sed.mobile.R;

public class HorariosItemViewMvcImpl
        implements HorariosItemViewMvc {

    private Listener listener;

    private CheckBox checkBox;

    private TextView horario;

    private String horarioItem;

    private ImageView editarFrequencia;

    private ImageView excluirFrequencia;

    private ConstraintLayout constraintLayout;

    private final View mRootView;

    HorariosItemViewMvcImpl(LayoutInflater layoutInflater, ViewGroup parent) {

        mRootView = layoutInflater.inflate(R.layout.layout_horario, parent, false);

        checkBox = findViewById(R.id.checkBoxHorario);

        horario = findViewById(R.id.horario);

        constraintLayout = findViewById(R.id.constraintId);

        editarFrequencia = findViewById(R.id.btnEditarFrequencia);

        excluirFrequencia = findViewById(R.id.btnExcluirFrequencia);

        checkBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                listener.onHorarioChecado(horarioItem);
            }
        });

        horario.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                listener.onHorarioSelecionado(horarioItem);
            }
        });

        editarFrequencia.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                listener.usuarioQuerEditarFrequencia(horarioItem);
            }
        });

        excluirFrequencia.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                listener.usuarioQuerExcluirFrequencia(horarioItem);
            }
        });
    }

    @Override
    public View getRootView() {

        return mRootView;
    }

    @Override
    public void checarHorario() {

        checkBox.setChecked(true);
    }

    @Override
    public void limparCheckBoxHorario() {

        checkBox.setChecked(false);
    }

    @Override
    public void marcarHorarioComLancamento() {

        constraintLayout.setBackgroundColor(getRootView().getResources().getColor(R.color.verde_dia_letivo));
    }

    @Override
    public void desmarcarHorarios() {

        constraintLayout.setBackgroundColor(getRootView().getResources().getColor(R.color.transparente));
    }

    @Override
    public void unregisterListener() {

        this.listener = null;
    }

    @Override
    public void exibirHorario(String horario) {

        horarioItem = horario;

        this.horario.setText(horarioItem);
    }

    @Override
    public void registerListener(Listener listener) {

        this.listener = listener;
    }

    private <T extends  View> T findViewById(int id) {

        return getRootView().findViewById(id);
    }
}
