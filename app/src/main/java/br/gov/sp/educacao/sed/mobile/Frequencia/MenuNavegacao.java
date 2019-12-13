package br.gov.sp.educacao.sed.mobile.Frequencia;

import android.os.Bundle;

import android.content.Intent;

import android.widget.TextView;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.support.v4.app.Fragment;

import br.gov.sp.educacao.sed.mobile.R;

import br.gov.sp.educacao.sed.mobile.Menu.HomeActivity;

import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmasListaActivity;

import br.gov.sp.educacao.sed.mobile.Fechamento.FechamentoActivity;

import br.gov.sp.educacao.sed.mobile.Avaliacao.ListaAvaliacoesActivity;
import br.gov.sp.educacao.sed.mobile.RegistroDeAula.RegistroAulaActivity;

public class MenuNavegacao
        extends Fragment {

    private Bundle bundle;

    private int nivelTela;

    private View mRootView;

    private TextView turmaAtual;

    private TextView frequencia;

    private TextView fechamento;

    private TextView avaliacoes;

    private TextView mudarTurma;

    private TurmaGrupo turmaGrupo;

    private TextView registroAulas;

    private TextView menuPrincipal;

    private usuarioSelecionouMenuNavegacao listener;

    interface usuarioSelecionouMenuNavegacao {

        void navegarPara(Intent intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.menu_navegacao_avaliacao, container, false);

        receberIntent();

        inicializarViews();

        configurarNivelMenu();

        exibirNomeTurma();

        configurarBundle();

        inicializarListeners();

        return mRootView;
    }

    public void ativarCliques() {

        turmaAtual.setClickable(true);

        frequencia.setClickable(true);

        registroAulas.setClickable(true);

        fechamento.setClickable(true);

        avaliacoes.setClickable(true);

        mudarTurma.setClickable(true);

        menuPrincipal.setClickable(true);
    }

    private void receberIntent() {

        if(getArguments() == null) {

            nivelTela = 0;
        }
        else {

            turmaGrupo = getArguments().getParcelable(TurmaGrupo.BUNDLE_TURMA_GRUPO);

            nivelTela = getArguments().getInt("NivelTela");
        }
    }

    public void desativarCliques() {

        turmaAtual.setClickable(false);

        frequencia.setClickable(false);

        registroAulas.setClickable(false);

        fechamento.setClickable(false);

        avaliacoes.setClickable(false);

        mudarTurma.setClickable(false);

        menuPrincipal.setClickable(false);
    }

    private void exibirNomeTurma() {

        turmaAtual.setText(turmaGrupo.getTurma().getNomeTurma());
    }

    private void inicializarViews() {

        turmaAtual = mRootView.findViewById(R.id.turma_atual);

        mudarTurma = mRootView.findViewById(R.id.mudarTurma);

        menuPrincipal = mRootView.findViewById(R.id.menuPrincipal);

        frequencia = mRootView.findViewById(R.id.frequencia);

        avaliacoes = mRootView.findViewById(R.id.avaliacoes);

        registroAulas = mRootView.findViewById(R.id.registro_aulas);

        fechamento = mRootView.findViewById(R.id.fechamento);
    }

    private void configurarBundle() {

        bundle = new Bundle();

        bundle.putParcelable(TurmaGrupo.BUNDLE_TURMA_GRUPO, turmaGrupo);
    }

    public void unregisterListener() {

        this.listener = null;
    }

    private void configurarNivelMenu() {

        switch(nivelTela) {

            case 1: {

                frequencia.setVisibility(View.GONE);
            }
        }
    }

    private void inicializarListeners() {

        inicializarListenerMudarTurma();

        inicializarListenerMenuPrincipal();

        inicializarListenerFrequencia();

        inicializarListenerAvaliacoes();

        inicializarListenerRegistroAulas();

        inicializarListenerFechamento();
    }

    private void inicializarListenerFrequencia() {

        frequencia.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(getActivity(), FrequenciaLancamentoActivity.class);

                intent1.putExtras(bundle);

                listener.navegarPara(intent1);
            }
        });
    }

    private void inicializarListenerAvaliacoes() {

        avaliacoes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(getActivity(), ListaAvaliacoesActivity.class);

                intent1.putExtras(bundle);

                listener.navegarPara(intent1);
            }
        });
    }

    private void inicializarListenerMudarTurma() {

        mudarTurma.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(getActivity(), TurmasListaActivity.class);

                Intent intent = new Intent(getActivity(), FrequenciaLancamentoActivity.class);

                Bundle bundle = new Bundle();

                bundle.putParcelable("Menu", intent);

                intent1.putExtras(bundle);

                listener.navegarPara(intent1);
            }
        });
    }

    private void inicializarListenerFechamento() {

        fechamento.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(getActivity(), FechamentoActivity.class);

                intent1.putExtras(bundle);

                listener.navegarPara(intent1);
            }
        });
    }

    private void inicializarListenerMenuPrincipal() {

        menuPrincipal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), HomeActivity.class);

                listener.navegarPara(intent);
            }
        });
    }

    private void inicializarListenerRegistroAulas() {

        registroAulas.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(getActivity(), RegistroAulaActivity.class);

                intent1.putExtras(bundle);

                listener.navegarPara(intent1);
            }
        });
    }

    public void registerListenerMenuNavegacao(usuarioSelecionouMenuNavegacao listener) {

        this.listener = listener;
    }
}