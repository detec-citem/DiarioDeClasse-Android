package br.gov.sp.educacao.sed.mobile.Turmas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.gov.sp.educacao.sed.mobile.R;

class TurmasListaItemViewMvcImpl
        implements TurmasListaItemViewMvc {

    private TextView txtTipo;

    private Listener listener;

    private TextView txtTurma;

    private final View mRootView;

    private TurmaGrupo turmaGrupo;

    private TextView txtDiretoria;

    private TextView txtDisciplina;

    private LinearLayout layDiretoria;

    TurmasListaItemViewMvcImpl(LayoutInflater layoutInflater, ViewGroup parent) {

        mRootView = layoutInflater.inflate(R.layout.layout_turma_list_item, parent, false);

        txtDiretoria = findViewById(R.id.tv_diretoria);

        txtTurma = findViewById(R.id.tv_turma);

        txtDisciplina = findViewById(R.id.tv_disciplina);

        txtTipo = findViewById(R.id.tv_tipo);

        layDiretoria = findViewById(R.id.lay_diretoria);

        getRootView().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                getRootView().setClickable(false);

                listener.onTurmaSelecionada(turmaGrupo);
            }
        });
    }

    @Override
    public View getRootView() {

        return mRootView;
    }

    public void ativarBotao() {

        getRootView().setClickable(true);
    }

    @Override
    public void unregisterListener() {

        this.listener = null;
    }

    @Override
    public void exibirInfoTurma(TurmaGrupo turma) {

        this.turmaGrupo = turma;

        txtTurma.setText(turma.getTurma().getNomeTurma());

        txtTipo.setText(turma.getTurma().getNomeTipoEnsino());

        if(turma.getTurma().isHeader()) {

            layDiretoria.setVisibility(View.VISIBLE);
        }
        else {

            layDiretoria.setVisibility(View.GONE);
        }

        txtDisciplina.setText(turma.getDisciplina().getNomeDisciplina());

        txtDiretoria.setText(turma.getTurma().getNomeDiretoria() + " / " + turma.getTurma().getNomeEscola());
    }

    @Override
    public void registerListener(Listener listener) {

        this.listener = listener;
    }

    private <T extends  View> T findViewById(int id) {

        return getRootView().findViewById(id);
    }
}
