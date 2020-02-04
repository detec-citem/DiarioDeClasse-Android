package br.gov.sp.educacao.sed.mobile.Fechamento;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import br.gov.sp.educacao.sed.mobile.R;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;

class FechamentoPagerViewMvcImpl
        implements FechamentoPagerViewMvc,
        MenuNavegacao.usuarioSelecionouMenuNavegacao,
        MenuNavegacao.usuarioSelecionouListarAlunos {

    private Intent intent;

    private TextView menu;

    private Bundle bundle;

    private TextView enviar;

    private Toolbar toolbar;

    private ViewPager mPager;

    private TextView tvTurma;

    private Listener listener;

    private int numeroPaginas;

    PagerAdapter mPagerAdapter;

    private boolean menuAberto;

    private final View mRootView;

    private FrameLayout frameLayout;

    private MenuNavegacao menuNavegacao;

    private LayoutInflater layoutInflater;

    private ProgressBar progressBarVoador;

    private FragmentManager fragmentManager;

    private ToolbarViewMvcImpl toolbarViewMvcImpl;

    private LinearLayout listaLancamento, layoutPager;

    private DialogEnviandoFechamento dialogEnviandoFechamento;

    private Animation fadeOutIrProximoAluno, slideInIrProximoAluno;

    private Animation apenasRemoverMenuNavegacao, listarAlunosRemoverMenuNavegacao, mudarTelaRemoverMenuNavegacao, animacaoProgressBarVoador;

    FechamentoPagerViewMvcImpl(LayoutInflater layoutInflater, FragmentManager fragmentManager, ViewGroup parent) {

        mRootView = layoutInflater.inflate(R.layout.fragment_fechamento_pager, parent, false);

        layoutPager = (LinearLayout) layoutInflater.inflate(R.layout.layout_pager_alunos, parent, false);

        mPager = layoutPager.findViewById(R.id.pager);

        this.layoutInflater = layoutInflater;

        this.fragmentManager = fragmentManager;

        progressBarVoador = findViewById(R.id.progressBar);

        frameLayout = findViewById(R.id.container);

        menuNavegacao = new MenuNavegacao();

        bundle = new Bundle();

        menuAberto = false;

        frameLayout = findViewById(R.id.container);

        intent = null;

        listaLancamento = findViewById(R.id.lista_lancamento_fechamento);

        listaLancamento.addView(layoutPager);

        tvTurma = findViewById(R.id.tv_turma);

        toolbar = findViewById(R.id.toolbar);

        toolbarViewMvcImpl = getToolbarViewMvcImpl(toolbar);

        inicializarToolbar();

        inicializarAnimacoes();
    }

    void envioFinalizado() {

    }

    @Override
    public View getRootView() {

        return mRootView;
    }

    private Context getContext() {

        return getRootView().getContext();
    }

    void voltarVisibilidadeItem() {

        Animation animation2 = AnimationUtils.loadAnimation(getContext(), R.anim.move);

        animation2.setStartOffset(250);

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

        toolbarViewMvcImpl.setTitle("Fechamento");

        inicializarMenuNavegacaoToolbar();

        toolbarViewMvcImpl.setVisibilidadeEnviar();

        toolbar.setNavigationIcon(R.drawable.icone_voltar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                listener.botaoVoltar();
            }
        });

        toolbar.addView(toolbarViewMvcImpl.getRootView());

        enviar = (TextView) toolbarViewMvcImpl.getEnviar();

        enviar.setVisibility(View.VISIBLE);

        enviar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                enviar.setClickable(false);

                listener.usuarioQuerEnviarFechamento();
            }
        });
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

        fadeOutIrProximoAluno = AnimationUtils.loadAnimation(getContext(), R.anim.moveback);

        Interpolator interpolatorDoFadeOut = new LinearOutSlowInInterpolator();

        fadeOutIrProximoAluno.setInterpolator(interpolatorDoFadeOut);

        fadeOutIrProximoAluno.setStartOffset(210);

        slideInIrProximoAluno = AnimationUtils.loadAnimation(getContext(), R.anim.slide);

        Interpolator interpolatorDoSlideIn = PathInterpolatorCompat.create(0.080f, 0.960f, 0.770f, 0.910f);

        slideInIrProximoAluno.setInterpolator(interpolatorDoSlideIn);

        inicializarListenerAnimacoesSlider();

        animacaoProgressBarVoador = AnimationUtils.loadAnimation(getContext(), R.anim.exibir_progress_voador);

        mudarTelaRemoverMenuNavegacao = AnimationUtils.loadAnimation(getContext(), R.anim.remover_menu_navegacao);

        apenasRemoverMenuNavegacao = AnimationUtils.loadAnimation(getContext(), R.anim.remover_menu_navegacao);

        listarAlunosRemoverMenuNavegacao = AnimationUtils.loadAnimation(getContext(), R.anim.remover_menu_navegacao);
    }

    private void esconderMenuNavegacao() {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        frameLayout.setVisibility(View.INVISIBLE);

        fragmentTransaction.hide(menuNavegacao);

        fragmentTransaction.commit();
    }

    void avisoUsuarioFechamentosEnviados() {

         finalizaProgressEnviandoFechamento();

        Toast.makeText(getContext(), "O fechamento da turma foi enviado", Toast.LENGTH_SHORT).show();
    }

    private void exibirProgressBarVoador() {

        progressBarVoador.setVisibility(View.VISIBLE);

        progressBarVoador.startAnimation(animacaoProgressBarVoador);
    }

    void avisoUsuarioFechamentoNaoEnviado() {

        Toast.makeText(getContext(), getContext().getResources().getString(R.string.erro_servidor), Toast.LENGTH_SHORT).show();
    }

    void inicializarListenerAnimacaoAlunos() {

        inicializarListenerAnimacoesSlider();
    }

    @Override
    public void usuarioClicouBotaoConfirma() {

        if(mPager.getCurrentItem() < numeroPaginas - 1) {

            mPager.startAnimation(fadeOutIrProximoAluno);
        }
    }

    void avisoUsuarioNecessarioSincronizar() {

        final AlertDialog.Builder alertbuilder = new AlertDialog.Builder(getContext());

        alertbuilder.setTitle("Atenção");

        alertbuilder.setMessage("Existem dados não sincronizados, é necessário sincronizar antes de enviar o fechamento");

        alertbuilder.setPositiveButton(getContext().getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                listener.botaoVoltar();
            }
        });

        AlertDialog alertDialog = alertbuilder.create();

        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);

        alertDialog.show();
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
    public void usuarioSelecionouListarAlunos() {

        listenerAnimacaoListarAlunosRemoverMenuNavegacao();

        removerMenuNavegacao(listarAlunosRemoverMenuNavegacao);
    }

    public void registerListenerMenuNavegacao() {

        menuNavegacao.registerListenerListarAlunos(this);

        menuNavegacao.registerListenerMenuNavegacao(this);
    }

    @Override
    public void navegarPara(final Intent intent) {

        this.intent = intent;

        listenerAnimacaoMudarTelaRemoverMenuNavegacao(mudarTelaRemoverMenuNavegacao);

        removerMenuNavegacao(mudarTelaRemoverMenuNavegacao);
    }

    void avisoUsuarioNenhumFechamentoParaEnviar() {

        Toast.makeText(getContext(), "Não há nenhum fechamento para ser enviado", Toast.LENGTH_SHORT).show();
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

    void avisoUsuarioConfirmeNotasFaltasParaEnviar() {

        Toast.makeText(getContext(), "É necessário confirmar as notas e faltas antes de enviar o fechamento", Toast.LENGTH_SHORT).show();
    }

    private <T extends  View> T findViewById(int id) {

        return getRootView().findViewById(id);
    }

    private void inicializarListenerAnimacoesSlider() {

        fadeOutIrProximoAluno.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                mPager.setCurrentItem(mPager.getCurrentItem() + 1);

                slideInIrProximoAluno.setStartOffset(85);

                mPager.startAnimation(slideInIrProximoAluno);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    void exibirOpcaoEnviarFechamento(String nomeTurma) {

        final AlertDialog.Builder alertbuilder = new AlertDialog.Builder(getContext());

        alertbuilder.setTitle(getContext().getResources().getString(R.string.enviar_fechamento_turma, nomeTurma));

        alertbuilder.setCancelable(false);

        alertbuilder.setPositiveButton(getContext().getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                enviar.setClickable(true);

                dialog.dismiss();

                inicializaProgressEnviandoFechamento();

                listener.enviarFechamento();
            }
        });

        alertbuilder.setNegativeButton(getContext().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                enviar.setClickable(true);

                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertbuilder.create();

        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);

        alertDialog.show();
    }

    private void inicializaProgressEnviandoFechamento() {

        if(dialogEnviandoFechamento == null) {

            dialogEnviandoFechamento = new DialogEnviandoFechamento();

            dialogEnviandoFechamento.show(fragmentManager, null);
        }
        else {

            dialogEnviandoFechamento.show(fragmentManager, null);
        }
    }

    private void finalizaProgressEnviandoFechamento() {

        dialogEnviandoFechamento.dismiss();
    }

    @Override
    public void definirNumeroPaginas(int numeroPaginas) {

        this.numeroPaginas = numeroPaginas;
    }

    public void ativarBotaoEnviar() {

        enviar.setClickable(true);
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

        return new ToolbarViewMvcImpl(layoutInflater, parent);
    }

    public void exibirNomeTurmaDisciplina(String nomeTurma, String nomeDisciplina) {

        String tvTurmaText = nomeTurma + " / " + nomeDisciplina;

        tvTurma.setText(tvTurmaText);
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
