package br.gov.sp.educacao.sed.mobile.Avaliacao;

import android.os.Bundle;

import android.content.Intent;
import android.content.Context;

import android.widget.TextView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.LinearLayout;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.support.v7.widget.Toolbar;

import android.support.annotation.Nullable;

import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.AnimationUtils;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import br.gov.sp.educacao.sed.mobile.R;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;

import android.support.v4.view.ViewPager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;

class FragmentLAPViewMvcImpl
        implements FragmentLAPViewMvc,
        MenuNavegacao.usuarioSelecionouMenuNavegacao,
        MenuNavegacao.usuarioSelecionouListarAlunos {

    private Intent intent;

    private Bundle bundle;

    private Toolbar toolbar;

    private ViewPager mPager;

    private Listener listener;

    @SuppressWarnings("WeakerAccess")
    PagerAdapter mPagerAdapter;

    private boolean menuAberto;

    private final View mRootView;

    private FrameLayout frameLayout;

    private LayoutInflater inflater;

    @SuppressWarnings("FieldCanBeLocal")
    private LinearLayout layoutPager;

    @SuppressWarnings("FieldCanBeLocal")
    private FrameLayout listaLancamento;

    private MenuNavegacao menuNavegacao;

    private ProgressBar progressBarVoador;

    private int numeroPaginas, paginaAtual;

    private Animation animation, animation2;

    private FragmentManager fragmentManager;

    @SuppressWarnings("FieldCanBeLocal")
    private TextView tvAvaliacao, tvTurma, menu;

    private ToolbarViewMvcImpl toolbarViewMvcImpl;

    private Animation animacaoProgressBarVoador, listarAlunosRemoverMenuNavegacao;

    private Animation apenasRemoverMenuNavegacao, mudarAlunoRemoverMenuNavegacao, mudarTelaRemoverMenuNavegacao;

    FragmentLAPViewMvcImpl(LayoutInflater layoutInflater, FragmentManager fragmentManager, ViewGroup parent) {

        mRootView = layoutInflater.inflate(R.layout.fragment_avaliacao_lancamento_pager, parent, false);

        layoutPager = (LinearLayout) layoutInflater.inflate(R.layout.layout_pager_alunos, parent, false);

        this.inflater = layoutInflater;

        this.fragmentManager = fragmentManager;

        frameLayout = findViewById(R.id.container);

        toolbar = findViewById(R.id.toolbar);

        progressBarVoador = findViewById(R.id.progressBar);

        toolbarViewMvcImpl = getToolbarViewMvcImpl(toolbar);

        paginaAtual = 1;

        menuAberto = false;

        intent = null;

        tvTurma = findViewById(R.id.tv_turma);

        tvAvaliacao = findViewById(R.id.tv_avaliacao);

        listaLancamento = findViewById(R.id.lista_lancamento);

        listaLancamento.addView(layoutPager);

        mPager = layoutPager.findViewById(R.id.pager);

        menuNavegacao = new MenuNavegacao();

        bundle = new Bundle();

        inicializarToolbar();

        inicializarAnimacoes();

        inicializarListenerAnimacoes();
    }

    @Override
    public View getRootView() {

        return mRootView;
    }

    @Override
    public void listarAlunos() {

        removerMenuNavegacao(listarAlunosRemoverMenuNavegacao);
    }

    private Context getContext() {

        return getRootView().getContext();
    }

    void voltarVisibilidadeItem() {

        Animation animation2 = AnimationUtils.loadAnimation(getContext(), R.anim.move);

        mPager.startAnimation(animation2);
    }

    void removerProgressBarVoador() {

        progressBarVoador.setVisibility(View.GONE);
    }

    @Override
    public void unregisterListener() {

        this.listener = null;
    }

    private void inicializarToolbar() {

        toolbarViewMvcImpl.setTitle("Avaliação");

        toolbar.setNavigationIcon(R.drawable.icone_voltar);

        inicializarMenuNavegacaoToolbar();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                listener.botaoVoltar();
            }
        });

        toolbar.addView(toolbarViewMvcImpl.getRootView());
    }

    private void irParaProximoAluno() {

        if(mPager.getCurrentItem() < numeroPaginas - 1) {

            mPager.startAnimation(animation);
        }
        else if(paginaAtual == numeroPaginas) {

            inicializarListenerAnimacoesLista();

            mPager.startAnimation(animation);
        }
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

        animation = AnimationUtils.loadAnimation(getContext(), R.anim.moveback);

        Interpolator interpolator = new LinearOutSlowInInterpolator();

        animation.setInterpolator(interpolator);

        animation2 = AnimationUtils.loadAnimation(getContext(), R.anim.slide);

        Interpolator interpolator2 = PathInterpolatorCompat.create(0.080f, 0.960f, 0.770f, 0.910f);

        animation2.setInterpolator(interpolator2);

        inicializarListenerAnimacoesSlider();

        animacaoProgressBarVoador = AnimationUtils.loadAnimation(getContext(), R.anim.exibir_progress_voador);

        mudarTelaRemoverMenuNavegacao = AnimationUtils.loadAnimation(getContext(), R.anim.remover_menu_navegacao);

        apenasRemoverMenuNavegacao = AnimationUtils.loadAnimation(getContext(), R.anim.remover_menu_navegacao);

        mudarAlunoRemoverMenuNavegacao = AnimationUtils.loadAnimation(getContext(), R.anim.remover_menu_navegacao);

        listarAlunosRemoverMenuNavegacao = AnimationUtils.loadAnimation(getContext(), R.anim.remover_menu_navegacao);
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

    @Override
    public void exibirNomeTurma(String turma) {

        tvTurma.setText(turma);
    }

    public void registerListenerMenuNavegacao() {

        menuNavegacao.registerListenerMenuNavegacao(this);

        menuNavegacao.registerListenerListarAlunos(this);
    }

    private void inicializarListenerAnimacoes() {

        listenerAnimacaoApenasRemoverMenuNavegacao();

        listenerAnimacaoMudarAlunoRemoverMenuNavegacao();

        listenerAnimacaoMudarTelaRemoverMenuNavegacao();

        listenerAnimacaoListarAlunosRemoverMenuNavegacao();
    }

    void setarTurmaGrupo(TurmaGrupo turmaGrupo) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(R.anim.move2, R.anim.slideback);

        int nivelTela = 2;

        bundle.putInt("NivelTela", nivelTela);

        bundle.putParcelable(TurmaGrupo.BUNDLE_TURMA_GRUPO, turmaGrupo);

        menuNavegacao.setArguments(bundle);

        fragmentTransaction.add(R.id.container, menuNavegacao, "MenuNavegacao");

        fragmentTransaction.commit();
    }

    @Override
    public void navegarPara(final Intent intent) {

        this.intent = intent;

        removerMenuNavegacao(mudarTelaRemoverMenuNavegacao);
    }

    @Override
    public void usuarioClicouBotaoConfirmaNota() {

        if(menuAberto) {

            removerMenuNavegacao(mudarAlunoRemoverMenuNavegacao);
        }
        else {

            irParaProximoAluno();
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

    private void inicializarListenerAnimacoesLista() {

        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

                exibirProgressBarVoador();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                listener.navegarParaListaAlunos();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void exibirNomeAvaliacao(String avaliacao) {

        tvAvaliacao.setText(avaliacao);
    }

    private void inicializarListenerAnimacoesSlider() {

        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                mPager.setCurrentItem(mPager.getCurrentItem() + 1);

                paginaAtual = mPager.getCurrentItem() + 1;

                mPager.startAnimation(animation2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void definirNumeroPaginas(int numeroPaginas) {

        this.numeroPaginas = numeroPaginas;
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
    public void createPagerAdapter(PagerAdapter pagerAdapter) {

        this.mPagerAdapter = pagerAdapter;

        mPager.setAdapter(mPagerAdapter);
    }

    private void listenerAnimacaoMudarTelaRemoverMenuNavegacao() {

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

    private void listenerAnimacaoMudarAlunoRemoverMenuNavegacao() {

        mudarAlunoRemoverMenuNavegacao.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                esconderMenuNavegacao();

                irParaProximoAluno();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void listenerAnimacaoListarAlunosRemoverMenuNavegacao() {

        listarAlunosRemoverMenuNavegacao.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

                exibirProgressBarVoador();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                listener.navegarParaListaAlunos();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public ToolbarViewMvcImpl getToolbarViewMvcImpl(@Nullable ViewGroup parent) {

        return new ToolbarViewMvcImpl(inflater, parent);
    }
}
