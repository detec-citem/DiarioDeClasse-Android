package br.gov.sp.educacao.sed.mobile.Avaliacao;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.gov.sp.educacao.sed.mobile.R;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;

class ListaAvaliacoesViewMvcImpl
        implements
        ListaAvaliacoesViewMvc,
        ListaAvaliacoesAdapter.OnAvaliacaoSelecionadaListener,
        MenuNavegacao.usuarioSelecionouMenuNavegacao {

    private Bundle bundle;

    private Button btNova;

    private Intent intent;

    private View mRootView;

    private Toolbar toolbar;

    @SuppressWarnings("WeakerAccess")
    int bimestreSelecionado;

    private TextView tvTurma;

    private Listener listener;

    private boolean menuAberto;

    private ListView lvConsulta;

    private FrameLayout frameLayout;

    ListaAvaliacoesAdapter adapter;

    private MenuNavegacao menuNavegacao;

    private ProgressBar progressBarVoador;

    private LayoutInflater layoutInflater;

    @SuppressWarnings("FieldCanBeLocal")
    private List<Avaliacao> listaAvaliacoes;

    private FragmentManager fragmentManager;

    private ToolbarViewMvcImpl toolbarViewMvcImpl;

    @SuppressWarnings("FieldCanBeLocal")
    private TextView tvPeriodo, tvAtividade, tvDisciplina, menu;

    private final CharSequence[] sequenciaBimestres = {"1° Bimestre", "2° Bimestre", "3° Bimestre", "4° Bimestre"};

    private Animation selecionouAvaliacaoRemoverMenuNavegacao, apenasRemoverMenuNavegacao, mudarTelaRemoverMenuNavegacao, animacaoProgressBarVoador;

    ListaAvaliacoesViewMvcImpl(LayoutInflater inflater, FragmentManager fragmentManager, ViewGroup parent, List<Avaliacao> listaAvaliacoes) {

        mRootView = inflater.inflate(R.layout.activity_lista_avaliacoes, parent, false);

        toolbar = findViewById(R.id.toolbar);

        this.layoutInflater = inflater;

        this.fragmentManager = fragmentManager;

        toolbarViewMvcImpl = getToolbarViewMvcImpl(toolbar);

        inicializarToolbar();

        lvConsulta = findViewById(R.id.lv_consulta);

        tvTurma = findViewById(R.id.tv_diretoria);

        tvPeriodo = findViewById(R.id.periodo);

        tvAtividade = findViewById(R.id.atividade);

        tvDisciplina = findViewById(R.id.disciplina);

        btNova = findViewById(R.id.bt_nova);

        frameLayout = findViewById(R.id.container);

        progressBarVoador = findViewById(R.id.progressBar);

        menuAberto = false;

        intent = null;

        bimestreSelecionado = 1;

        adapter = new ListaAvaliacoesAdapter(getContext(), this);

        this.listaAvaliacoes = listaAvaliacoes;

        adapter.addAll(this.listaAvaliacoes);

        menuNavegacao = new MenuNavegacao();

        bundle = new Bundle();

        inicializarAnimacoes();

        Animation animation;

        animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide2);

        Interpolator interpolator = new LinearOutSlowInInterpolator();

        animation.setInterpolator(interpolator);

        animation.setStartOffset(750);

        btNova.startAnimation(animation);

        lvConsulta.setAdapter(adapter);
    }

    @Override
    public View getRootView() {

        return mRootView;
    }

    private Context getContext() {

        return getRootView().getContext();
    }

    void notasAvaliacoesFuturas() {

        Toast.makeText(getContext(), "Não é possível lançar notas para avaliações futuras", Toast.LENGTH_LONG).show();
    }

    void removerProgressBarVoador() {

        progressBarVoador.setVisibility(View.GONE);
    }

    @Override
    public void unregisterListener() {

        this.listener = null;
    }

    private void inicializarToolbar() {

        toolbarViewMvcImpl.setTitle("Avaliações");

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

    void exibirSelecaoTipoAtividade() {

        final String[] tiposAtividade = {"Atividade", "Avaliação", "Trabalho", "Outros", "Todos"};

        tvAtividade.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Tipo");

                builder.setItems(tiposAtividade, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int bimestre) {

                        CharSequence tipoFiltro = tiposAtividade[bimestre];

                        tvAtividade.setText(tipoFiltro);

                        listener.usuarioSelecionouTipoAtividade(String.valueOf(tipoFiltro));
                    }
                });

                AlertDialog dialogSelecionarBimestre = builder.create();

                dialogSelecionarBimestre.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);

                dialogSelecionarBimestre.show();
            }
        });
    }

    void exibirBimestre(int bimestre) {

        tvPeriodo.setText(sequenciaBimestres[bimestre - 1]);
    }

    void reativarBotaoNovaAvaliacao() {

        btNova.setClickable(true);
    }

    void exibirDialogSelecaoBimestre() {

        tvPeriodo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle(R.string.periodo);

                builder.setItems(sequenciaBimestres, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int bimestre) {

                        CharSequence bimestreFiltro = sequenciaBimestres[bimestre];

                        bimestreSelecionado = bimestre + 1;

                        tvPeriodo.setText(bimestreFiltro);

                        listener.usuarioSelecionouBimestre(bimestre + 1);
                    }
                });

                AlertDialog dialogSelecionarBimestre = builder.create();

                dialogSelecionarBimestre.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);

                dialogSelecionarBimestre.show();
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

        animacaoProgressBarVoador = AnimationUtils.loadAnimation(getContext(), R.anim.exibir_progress_voador);

        selecionouAvaliacaoRemoverMenuNavegacao = AnimationUtils.loadAnimation(getContext(), R.anim.remover_menu_navegacao);

        mudarTelaRemoverMenuNavegacao = AnimationUtils.loadAnimation(getContext(), R.anim.remover_menu_navegacao);

        apenasRemoverMenuNavegacao = AnimationUtils.loadAnimation(getContext(), R.anim.remover_menu_navegacao);
    }

    void nenhumaDisciplinaSelecionada() {

        Toast.makeText(getContext(), "Selecione a disciplina", Toast.LENGTH_SHORT).show();

        reativarBotaoNovaAvaliacao();
    }

    private void esconderMenuNavegacao() {

        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        frameLayout.setVisibility(View.INVISIBLE);

        fragmentTransaction.hide(menuNavegacao);

        fragmentTransaction.commit();
    }

    @Override
    public void exibirAvisoNenhumaProva() {

        Toast.makeText(getContext(),"Você não tem provas cadastradas", Toast.LENGTH_LONG).show();
    }

    void lancarNotasAvaliacaoNaoValeNota() {

        Toast.makeText(getContext(), R.string.lancar_avaliacao_sem_nota, Toast.LENGTH_SHORT).show();
    }

    private void exibirProgressBarVoador() {

        progressBarVoador.setVisibility(View.VISIBLE);

        progressBarVoador.startAnimation(animacaoProgressBarVoador);
    }

    @Override
    public void exibirAvisoApenasUmaProva() {

        Toast.makeText(getContext(), R.string.media_apenas_uma_prova, Toast.LENGTH_LONG).show();
    }

    @Override
    public void exibirDialogDeletarAvaliacao() {

        AlertDialog alertDialog = new AlertDialog.Builder(getContext(), R.style.AppTheme_Dialog_Alert)
                .setTitle("Excluir Avaliação")
                .setCancelable(false)
                .setMessage("Deseja excluir a avaliação selecionada?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        listener.deletarAvaliacao();
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                }).create();

        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);

        alertDialog.show();
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
    public void inicializarBotaoNovaAvaliacao() {

        btNova.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                btNova.setClickable(false);

                listener.usuarioQuerCriarAvaliacao();
            }
        });
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

    @Override
    public void exibirSelecaoDisciplinaAnosIniciais(int serie) {

        tvDisciplina.setVisibility(View.VISIBLE);

        final String[] disciplinas = {"Matemática", "Língua Portuguesa", "Ciências da Natureza/Humanas"};

        tvDisciplina.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Disciplina");

                builder.setItems(disciplinas, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int bimestre) {

                        CharSequence disciplinaFiltro = disciplinas[bimestre];

                        tvDisciplina.setText(disciplinaFiltro);

                        listener.usuarioSelecionouDisciplina(bimestre);
                    }
                });

                AlertDialog dialogSelecionarBimestre = builder.create();

                dialogSelecionarBimestre.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);

                dialogSelecionarBimestre.show();
            }
        });
    }

    @Override
    public void usuarioQuerEditarAvaliacao(Avaliacao avaliacao) {

        listener.configurarDialogEditarAvaliacao(avaliacao);
    }

    @Override
    public void usuarioQuerDeletarAvaliacao(Avaliacao avaliacao) {

        listener.usuarioQuerDeletarAvaliacao(avaliacao);
    }

    @Override
    public void onAvaliacaoSelecionada(final Avaliacao avaliacao) {

        listenerAnimacaoAvaliacaoSelecionadaRemoverMenu(avaliacao);

        if(menuAberto) {

            removerMenuNavegacao(selecionouAvaliacaoRemoverMenuNavegacao);
        }
        else {

            listener.onAvaliacaoSelecionada(avaliacao);
        }
    }

    void inicializarListaAvaliacoes(final List<Avaliacao> avaliacoes) {

        if(avaliacoes != null) {

            adapter.clear();

            adapter.addAll(avaliacoes);

            adapter.notifyDataSetChanged();
        }
        else {

            lvConsulta.setAdapter(null);
        }
    }

    void exibirNomeTurmaDisciplina(String nomeTurma, String disciplina) {

        String tvTurmaText = nomeTurma + " / " + disciplina;

        tvTurma.setText(tvTurmaText);
    }

    @Override
    public ToolbarViewMvcImpl getToolbarViewMvcImpl(@Nullable ViewGroup parent) {

        return new ToolbarViewMvcImpl(layoutInflater, parent);
    }

    private void listenerAnimacaoAvaliacaoSelecionadaRemoverMenu(final Avaliacao avaliacao) {

        selecionouAvaliacaoRemoverMenuNavegacao.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

                exibirProgressBarVoador();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                esconderMenuNavegacao();

                listener.onAvaliacaoSelecionada(avaliacao);
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

