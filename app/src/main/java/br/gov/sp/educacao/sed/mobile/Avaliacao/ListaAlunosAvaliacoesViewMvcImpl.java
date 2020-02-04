package br.gov.sp.educacao.sed.mobile.Avaliacao;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import br.gov.sp.educacao.sed.mobile.R;
import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;

class ListaAlunosAvaliacoesViewMvcImpl
        implements ListaAlunosAvaliacoesViewMvc,
        LancamentoAvaliacaoAdapter.OnAlunoAvaliacaoSelecionadoListener,
        MenuNavegacao.usuarioSelecionouMenuNavegacao {

    private Bundle bundle;

    @SuppressWarnings("FieldCanBeLocal")
    private TextView menu;

    private Intent intent;

    private View mRootView;

    private Toolbar toolbar;

    private TextView tvTurma;

    private Listener listener;

    @SuppressWarnings("FieldCanBeLocal")
    private Button btConfirmar;

    private boolean menuAberto;

    @SuppressWarnings("FieldCanBeLocal")
    private ListView lvLancamento;

    private FrameLayout frameLayout;

    private MenuNavegacao menuNavegacao;

    private ProgressBar progressBarVoador;

    private LayoutInflater layoutInflater;

    private FragmentManager fragmentManager;

    private LancamentoAvaliacaoAdapter adapter;

    private ToolbarViewMvcImpl toolbarViewMvcImpl;

    private Animation apenasRemoverMenuNavegacao, salvarNotasRemoverMenuNavegacao, mudarTelaRemoverMenuNavegacao, animacaoProgressBarVoador;

    ListaAlunosAvaliacoesViewMvcImpl(LayoutInflater inflater, FragmentManager fragmentManager, ViewGroup parent, List<Aluno> listaAlunos) {

        mRootView = inflater.inflate(R.layout.activity_lista_alunos_avaliacoes, parent, false);

        this.layoutInflater = inflater;

        this.fragmentManager = fragmentManager;

        toolbar = findViewById(R.id.toolbar);

        toolbarViewMvcImpl = getToolbarViewMvcImpl(toolbar);

        inicializarToolbar();

        menuNavegacao = new MenuNavegacao();

        bundle = new Bundle();

        menuAberto = false;

        frameLayout = findViewById(R.id.container);

        progressBarVoador = findViewById(R.id.progressBar);

        inicializarAnimacoes();

        intent = null;

        tvTurma = findViewById(R.id.tv_diretoria);

        lvLancamento = findViewById(R.id.lv_lancamento);

        adapter = new LancamentoAvaliacaoAdapter(getContext(), this);

        adapter.addAll(listaAlunos);

        lvLancamento.setAdapter(adapter);

        btConfirmar = findViewById(R.id.bt_confirmar_alunos);

        btConfirmar.setEnabled(true);

        btConfirmar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(menuAberto) {

                    removerMenuNavegacao(salvarNotasRemoverMenuNavegacao);
                }
                else {

                    exibirDialogNotasSalvas();
                }
            }
        });
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

        toolbarViewMvcImpl.setTitle("Notas");

        toolbar.setNavigationIcon(R.drawable.icone_voltar);

        inicializarMenuNavegacaoToolbar();

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

        salvarNotasRemoverMenuNavegacao = AnimationUtils.loadAnimation(getContext(), R.anim.remover_menu_navegacao);

        listenerAnimacaoSalvarNotaRemoverMenuNavegacao();
    }

    @Override
    public void alterarNota(Aluno aluno) {

        listener.alterarNota(aluno);
    }

    private void esconderMenuNavegacao() {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        frameLayout.setVisibility(View.INVISIBLE);

        fragmentTransaction.hide(menuNavegacao);

        fragmentTransaction.commit();
    }

    private void exibirDialogNotasSalvas() {

        AlertDialog notasSalvas;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Notas salvas!");

        builder.setMessage("As notas foram salvas no dispositivo. " +
                "Sincronize o aplicativo para que os dados sejam enviados para a SED.");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int arg1) {

                dialog.dismiss();
            }
        });

        notasSalvas = builder.create();

        notasSalvas.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);

        notasSalvas.show();
    }

    private void exibirProgressBarVoador() {

        progressBarVoador.setVisibility(View.VISIBLE);

        progressBarVoador.startAnimation(animacaoProgressBarVoador);
    }

    @Override
    public void recarregarListaAvaliacoes() {

        adapter.notifyDataSetChanged();
    }

    void setarTurmaGrupo(TurmaGrupo turmaGrupo) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(R.anim.move2, R.anim.slideback);

        int nivelTela = 3;

        bundle.putInt("NivelTela", nivelTela);

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

    private void listenerAnimacaoSalvarNotaRemoverMenuNavegacao() {

        salvarNotasRemoverMenuNavegacao.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

                exibirProgressBarVoador();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                removerProgressBarVoador();

                exibirDialogNotasSalvas();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void exibirNomeTurmaDisciplina(String nomeTurma, String disciplina) {

        String tvTurmaText = nomeTurma + " / " + disciplina;

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
