package br.gov.sp.educacao.sed.mobile.Frequencia;

import java.util.List;

import android.os.Bundle;

import android.content.Context;
import android.content.Intent;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import br.gov.sp.educacao.sed.mobile.R;

import android.support.v7.widget.Toolbar;

import android.support.annotation.Nullable;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;

class FrequenciaConsultaViewMvcImpl
        implements FrequenciaConsultaViewMvc,
        MenuNavegacao.usuarioSelecionouMenuNavegacao {

    private Bundle bundle;

    @SuppressWarnings("FieldCanBeLocal")
    private TextView menu;

    private Intent intent;

    private Toolbar toolbar;

    private TextView tvTurma;

    private Listener listener;

    private boolean menuAberto;

    private ListView lvConsulta;

    private final View mRootView;

    private FrameLayout frameLayout;

    private MenuNavegacao menuNavegacao;

    private LayoutInflater layoutInflater;

    private ProgressBar progressBarVoador;

    private FragmentManager fragmentManager;

    @SuppressWarnings("FieldCanBeLocal")
    private ConsultaFrequenciaAdapter adapter;

    private ToolbarViewMvcImpl toolbarViewMvcImpl;

    private Animation apenasRemoverMenuNavegacao, mudarTelaRemoverMenuNavegacao, animacaoProgressBarVoador;

    FrequenciaConsultaViewMvcImpl(LayoutInflater layoutInflater, FragmentManager fragmentManager, ViewGroup parent) {

        mRootView = layoutInflater.inflate(R.layout.activity_frequencia_consulta, parent, false);

        this.layoutInflater = layoutInflater;

        this.fragmentManager = fragmentManager;

        toolbar = findViewById(R.id.toolbar);

        tvTurma = findViewById(R.id.tv_turma);

        frameLayout = findViewById(R.id.container);

        lvConsulta = findViewById(R.id.lv_consulta);

        progressBarVoador = findViewById(R.id.progressBar);

        toolbarViewMvcImpl = getToolbarViewMvcImpl(toolbar);

        inicializarToolbar();

        inicializarAnimacoes();

        menuNavegacao = new MenuNavegacao();

        bundle = new Bundle();

        menuAberto = false;
    }

    @Override
    public View getRootView() {

        return mRootView;
    }

    private Context getContext() {

        return getRootView().getContext();
    }

    void removerProgressBarVoador() {

        progressBarVoador.setVisibility(View.GONE);
    }

    @Override
    public void unregisterListener() {

        this.listener = null;
    }

    private void inicializarToolbar() {

        toolbarViewMvcImpl.setTitle("Frequência");

        inicializarMenuNavegacaoToolbar();

        toolbar.setNavigationIcon(R.drawable.icone_voltar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                listener.onBackPressed();
            }
        });
        toolbar.addView(toolbarViewMvcImpl.getRootView());
    }

    private void exibirMenuNavegacao() {

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.exibir_menu_navegacao);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.show(menuNavegacao);

        menuNavegacao.ativarCliques();

        fragmentTransaction.commit();

        menuAberto = true;

        frameLayout.setVisibility(View.VISIBLE);

        frameLayout.startAnimation(animation);
    }

    private void inicializarAnimacoes() {

        animacaoProgressBarVoador = AnimationUtils.loadAnimation(getContext(), R.anim.exibir_progress_voador);

        mudarTelaRemoverMenuNavegacao = AnimationUtils.loadAnimation(getContext(), R.anim.remover_menu_navegacao);

        apenasRemoverMenuNavegacao = AnimationUtils.loadAnimation(getContext(), R.anim.remover_menu_navegacao);
    }

    private void esconderMenuNavegacao() {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        frameLayout.setVisibility(View.INVISIBLE);

        fragmentTransaction.hide(menuNavegacao);

        fragmentTransaction.commit();
    }

    private void exibirProgressBarVoador() {

        progressBarVoador.setVisibility(View.VISIBLE);

        progressBarVoador.startAnimation(animacaoProgressBarVoador);
    }

    public void registerListenerMenuNavegacao() {

        menuNavegacao.registerListenerMenuNavegacao(this);
    }

    void setarTurmaGrupo(TurmaGrupo turmaGrupo) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(R.anim.move2, R.anim.slideback);

        int nivelTela = 1;

        bundle.putInt("NivelTela", nivelTela);

        bundle.putParcelable(TurmaGrupo.BUNDLE_TURMA_GRUPO, turmaGrupo);

        menuNavegacao.setArguments(bundle);

        fragmentTransaction.add(R.id.container, menuNavegacao, "MenuNavegacao");

        fragmentTransaction.commit();
    }

    @Override
    public void navegarPara(final Intent intent) {

        this.intent = intent;

        listenerAnimacaoMudarTelaRemoverMenuNavegacao(mudarTelaRemoverMenuNavegacao);

        removerMenuNavegacao(mudarTelaRemoverMenuNavegacao);
    }

    public void unregisterListenerMenuNavegacao() {

        menuNavegacao.unregisterListener();
    }

    private void inicializarMenuNavegacaoToolbar() {

        menu = toolbarViewMvcImpl.getMenu();

        menu.setVisibility(View.VISIBLE);

        menu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(menuAberto) {

                    listenerAnimacaoApenasRemoverMenuNavegacao();

                    removerMenuNavegacao(apenasRemoverMenuNavegacao);
                }
                else {

                    exibirMenuNavegacao();
                }
            }
        });
    }

    void inicializarListaAlunos(List<Aluno> alunos) {

        adapter = new ConsultaFrequenciaAdapter(getContext());

        adapter.addAll(alunos);

        lvConsulta.setAdapter(adapter);
    }

    @Override
    public void registerListener(Listener listener) {

        this.listener = listener;
    }

    private <T extends  View> T findViewById(int id) {

        return getRootView().findViewById(id);
    }

    private void removerMenuNavegacao(Animation animation) {

        menuAberto = false;

        menuNavegacao.desativarCliques();

        frameLayout.setVisibility(View.INVISIBLE);

        frameLayout.startAnimation(animation);
    }

    private void listenerAnimacaoApenasRemoverMenuNavegacao() {

        apenasRemoverMenuNavegacao.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                esconderMenuNavegacao();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void exibirNomeTurmaTipoEnsino(String nomeTurma, String tipoEnsino) {

        String tvTurmaText = nomeTurma + " / " + tipoEnsino;

        tvTurma.setText(tvTurmaText);
    }

    @Override
    public ToolbarViewMvcImpl getToolbarViewMvcImpl(@Nullable ViewGroup parent) {

        return new ToolbarViewMvcImpl(layoutInflater, parent);
    }

    private void listenerAnimacaoMudarTelaRemoverMenuNavegacao(Animation mudarTelaRemoverMenuNavegacao) {

        mudarTelaRemoverMenuNavegacao.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

                exibirProgressBarVoador();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                esconderMenuNavegacao();

                listener.navegarPara(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
