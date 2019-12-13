package br.gov.sp.educacao.sed.mobile.Turmas;

import android.os.Bundle;

import android.content.Intent;
import android.content.Context;

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


class AlunoDetalheViewMvcImpl
        implements AlunoDetalheViewMvc,
        MenuNavegacao.usuarioSelecionouMenuNavegacao {

    private TextView menu;
    private TextView tvNome;
    private TextView tvAluno;
    private TextView tvTurma;
    private TextView tvStatus;
    private TextView tvNomePai;
    private TextView tvNomeMae;
    private TextView tvMatricula;
    private TextView tvNascimento;
    private TextView tvNecessidades;

    private Intent intent;

    private Bundle bundle;

    private Toolbar toolbar;

    private Listener listener;

    private boolean menuAberto;

    private final View mRootView;

    private FrameLayout frameLayout;

    private MenuNavegacao menuNavegacao;

    private ProgressBar progressBarVoador;

    private LayoutInflater layoutInflater;

    private FragmentManager fragmentManager;

    private ToolbarViewMvc toolbarViewMvcImpl;

    private Animation apenasRemoverMenuNavegacao, mudarTelaRemoverMenuNavegacao, animacaoProgressBarVoador;

    AlunoDetalheViewMvcImpl(LayoutInflater layoutInflater, FragmentManager fragmentManager, ViewGroup parent) {

        mRootView = layoutInflater.inflate(R.layout.activity_turma_aluno_detalhe, parent, false);

        this.layoutInflater = layoutInflater;

        this.fragmentManager = fragmentManager;

        progressBarVoador = findViewById(R.id.progressBar);

        frameLayout = findViewById(R.id.container);

        menuNavegacao = new MenuNavegacao();

        bundle = new Bundle();

        toolbar = findViewById(R.id.toolbar);

        toolbarViewMvcImpl = getToolbarViewMvc(toolbar);

        tvTurma = findViewById(R.id.tv_turma);
        tvAluno = findViewById(R.id.tv_aluno);
        tvStatus = findViewById(R.id.tv_status);
        tvNome = findViewById(R.id.tv_user_name);
        tvNomeMae = findViewById(R.id.tv_nome_mae);
        tvNomePai = findViewById(R.id.tv_nome_pai);
        tvMatricula = findViewById(R.id.tv_matricula);
        tvNascimento = findViewById(R.id.tv_nascimento);
        tvNecessidades = findViewById(R.id.tv_necessidades);

        inicializarAnimacoes();

        inicializarToolbar();
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

        toolbarViewMvcImpl.setTitle("Aluno");

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

        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        frameLayout.setVisibility(View.INVISIBLE);

        fragmentTransaction.hide(menuNavegacao);

        fragmentTransaction.commit();
    }

    private void exibirProgressBarVoador() {

        progressBarVoador.setVisibility(View.VISIBLE);

        progressBarVoador.startAnimation(animacaoProgressBarVoador);
    }

    void setarTurmaGrupo(TurmaGrupo turmaGrupo) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(R.anim.move2, R.anim.slideback);

        //int nivelTela = 1;

        //bundle.putInt("NivelTela", nivelTela);

        bundle.putParcelable(TurmaGrupo.BUNDLE_TURMA_GRUPO, turmaGrupo);

        menuNavegacao.setArguments(bundle);

        fragmentTransaction.add(R.id.container, menuNavegacao, "MenuNavegacao");

        fragmentTransaction.commit();
    }

    public void registerListenerMenuNavegacao() {

        menuNavegacao.registerListenerMenuNavegacao(this);
    }

    @Override
    public void navegarPara(final Intent intent) {

        this.intent = intent;

        listenerAnimacaoMudarTelaRemoverMenuNavegacao(mudarTelaRemoverMenuNavegacao);

        removerMenuNavegacao(mudarTelaRemoverMenuNavegacao);
    }

    private String convertStatus(boolean status) {

        if(status) {

            return "Ativo";
        }
        else {

            return "Inativo";
        }
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
    public ToolbarViewMvc getToolbarViewMvc(@Nullable ViewGroup parent) {

        return new ToolbarViewMvc(layoutInflater, parent);
    }

    void exibirDetalhesAluno(Aluno aluno, String usuario, String nomeTurma) {

        String LABEL_TURMA = getContext().getString(R.string.desc_turma);
        String LABEL_MATRICULA = getContext().getString(R.string.desc_matricula);
        String LABEL_STATUS = getContext().getString(R.string.desc_status);
        String LABEL_NOME_PAI = getContext().getString(R.string.desc_nome_pai);
        String LABEL_NOME_MAE = getContext().getString(R.string.desc_nome_mae);
        String LABEL_NASCIMENTO = "Data Nascimento: ";
        String LABEL_NECESSIDADES = "Necessidades Especiais: ";
        String IDX_SEPARATOR = ": ";

        tvNome.setText(usuario);

        tvAluno.setText(aluno.getNomeAluno());

        tvTurma.setText(LABEL_TURMA + IDX_SEPARATOR + nomeTurma);

        if(aluno.getDigitoRa() == null) {

            tvMatricula.setText(LABEL_MATRICULA + IDX_SEPARATOR + aluno.getNumeroRa());
        }
        else {

            tvMatricula.setText(LABEL_MATRICULA + IDX_SEPARATOR + aluno.getNumeroRa() + "-" + aluno.getDigitoRa());
        }

        tvStatus.setText(LABEL_STATUS + IDX_SEPARATOR + convertStatus(aluno.getAlunoAtivo()));

        String nomePaiFormatado = aluno.getPai();

        if(nomePaiFormatado == null) {

            nomePaiFormatado = getContext().getString(R.string.desc_info_indisp);
        }

        String nomeMaeFormatado = aluno.getMae();

        if(nomeMaeFormatado == null) {

            nomeMaeFormatado = getContext().getString(R.string.desc_info_indisp);
        }

        tvNomePai.setText(LABEL_NOME_PAI + IDX_SEPARATOR + nomePaiFormatado);

        tvNomeMae.setText(LABEL_NOME_MAE + IDX_SEPARATOR + nomeMaeFormatado);

        if(aluno.getNascimento() != null) {

            tvNascimento.setText(LABEL_NASCIMENTO + aluno.getNascimento());
        }

        if(aluno.getNecessidadesEspeciais() != null) {

            tvNecessidades.setText(LABEL_NECESSIDADES + aluno.getNecessidadesEspeciais());
        }
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
