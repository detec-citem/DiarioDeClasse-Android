package br.gov.sp.educacao.sed.mobile.Turmas;

import java.util.List;

import android.os.Bundle;

import android.content.Intent;
import android.content.Context;

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

class AlunosListaViewMvcImpl
        implements AlunosListaViewMvc,
        AlunoAdapter.OnAlunoSelecionadoListener, MenuNavegacao.usuarioSelecionouMenuNavegacao {

    private TextView menu;

    private Bundle bundle;

    private Intent intent;

    //private TextView tvUser;

    //private TextView tvNome;

    private Toolbar toolbar;

    private TextView tvTurma;

    private ListView lvValor;

    private Listener listener;

    private boolean menuAberto;

    private final View mRootView;

    private FrameLayout frameLayout;

    private AlunoAdapter alunoAdapter;

    private MenuNavegacao menuNavegacao;

    private LayoutInflater layoutInflater;

    private ProgressBar progressBarVoador;

    private FragmentManager fragmentManager;

    private ToolbarViewMvc toolbarViewMvcImpl;

    private Animation selecionouAlunoRemoverMenuNavegacao, apenasRemoverMenuNavegacao, mudarTelaRemoverMenuNavegacao, animacaoProgressBarVoador;

    AlunosListaViewMvcImpl(LayoutInflater layoutInflater, FragmentManager fragmentManager, ViewGroup parent) {

        mRootView = layoutInflater.inflate(R.layout.activity_turmas_alunos, parent, false);

        //tvUser = findViewById(R.id.tv_user);

        //tvNome = findViewById(R.id.tv_user_name);

        lvValor = findViewById(R.id.lv_valor);

        tvTurma = findViewById(R.id.tv_diretoria);

        this.layoutInflater = layoutInflater;

        this.fragmentManager = fragmentManager;

        toolbar = findViewById(R.id.toolbar);

        frameLayout = findViewById(R.id.container);

        progressBarVoador = findViewById(R.id.progressBar);

        menuAberto = false;

        intent = null;

        menuNavegacao = new MenuNavegacao();

        bundle = new Bundle();

        toolbarViewMvcImpl = getToolbarViewMvcImpl(toolbar);

        inicializarToolbar();

        inicializarAnimacoes();
    }

    //Mudar Nome
    void limparLista() {

        alunoAdapter.limparLista();
    }

    @Override
    public View getRootView() {

        return mRootView;
    }

    //Mudar Nome
    public void ativarBotao() {

        alunoAdapter.ativarBotao();
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

        toolbarViewMvcImpl.setTitle("Alunos");

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

        selecionouAlunoRemoverMenuNavegacao = AnimationUtils.loadAnimation(getContext(), R.anim.remover_menu_navegacao);

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

    @Override
    public void exibirNomeTurma(String turma) {

        tvTurma.setText(turma);
    }

    @Override
    public void exibirNomeUsuario(String nome) {

        //tvNome.setText(nome);
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
    public void onAlunoSelecionado(Aluno aluno) {

        listenerAnimacaoAvaliacaoSelecionadaRemoverMenu(aluno);

        if(menuAberto) {

            removerMenuNavegacao(selecionouAlunoRemoverMenuNavegacao);
        }
        else {

            listener.onAlunoSelecionado(aluno);
        }
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

    private <T extends View> T findViewById(int id) {

        return getRootView().findViewById(id);
    }

    @Override
    public void registerListener(Listener listener) {

        this.listener = listener;
    }

    private void removerMenuNavegacao(Animation animation) {

        menuAberto = false;

        menuNavegacao.desativarCliques();

        frameLayout.setVisibility(View.INVISIBLE);

        frameLayout.startAnimation(animation);
    }

    @Override
    public void exibirListaAlunos(List<Aluno> listaDeAlunos) {

        alunoAdapter = new AlunoAdapter(getContext(), this);

        alunoAdapter.addAll(listaDeAlunos);

        lvValor.setAdapter(alunoAdapter);
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
    public ToolbarViewMvc getToolbarViewMvcImpl(@Nullable ViewGroup parent) {

        return new ToolbarViewMvc(layoutInflater, parent);

    }

    private void listenerAnimacaoAvaliacaoSelecionadaRemoverMenu(final Aluno aluno) {

        selecionouAlunoRemoverMenuNavegacao.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

                exibirProgressBarVoador();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                esconderMenuNavegacao();

                listener.onAlunoSelecionado(aluno);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
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
