package br.gov.sp.educacao.sed.mobile.Fechamento;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import br.gov.sp.educacao.sed.mobile.R;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;

class FechamentoViewMvcImpl
        implements FechamentoViewMvc, MenuNavegacao.usuarioSelecionouMenuNavegacao {

    private Bundle bundle;

    private Intent intent;

    private Toolbar toolbar;

    private Listener listener;

    private boolean menuAberto;

    private boolean anosIniciais;

    private final View mRootView;

    private FrameLayout frameLayout;

    private EditText etJustificativa;

    private EditText etAulasRealizadas;

    private EditText etAulasPlanejadas;

    private Button btConfirma, btMedia;

    private MenuNavegacao menuNavegacao;

    private LayoutInflater layoutInflater;

    private ProgressBar progressBarVoador;

    private FragmentManager fragmentManager;

    private ToolbarViewMvcImpl toolbarViewMvcImpl;

    private TextView tvTurma, tvTipoFechamento, menu;

    private Animation confirmarFechamentoRemoverMenuNavegacao, calcularMediaRemoverMenuNavegacao;

    private Animation apenasRemoverMenuNavegacao, mudarTelaRemoverMenuNavegacao, animacaoProgressBarVoador;

    FechamentoViewMvcImpl(LayoutInflater layoutInflater, FragmentManager fragmentManager, ViewGroup parent) {

        mRootView = layoutInflater.inflate(R.layout.activity_fechamento, parent, false);

        this.layoutInflater = layoutInflater;

        this.fragmentManager = fragmentManager;

        etJustificativa = findViewById(R.id.txt_justificativa);

        etAulasRealizadas = findViewById(R.id.txt_aulas_realizadas);

        etAulasPlanejadas = findViewById(R.id.txt_aulas_planejadas);

        btConfirma = findViewById(R.id.btn_confirma_fechamento);

        progressBarVoador = findViewById(R.id.progressBar);

        frameLayout = findViewById(R.id.container);

        menuNavegacao = new MenuNavegacao();

        menuAberto = false;

        intent = null;

        bundle = new Bundle();

        btMedia = findViewById(R.id.calcularMedia);

        tvTurma = findViewById(R.id.tv_diretoria);

        tvTipoFechamento = findViewById(R.id.tv_tipo_fechamento);

        toolbar = findViewById(R.id.toolbar);

        toolbarViewMvcImpl = getToolbarViewMvcImpl(toolbar);

        inicializarToolbar();

        inicializarAnimacoes();

        inicializarListenerBotaoConfirma();

        inicializarListenerBotaoCalcularMedia();
    }

    @Override
    public View getRootView() {

        return mRootView;
    }

    public void ativarBotao() {

        btConfirma.setClickable(true);

        btMedia.setClickable(true);
    }

    private Context getContext() {

        return mRootView.getContext();
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

        confirmarFechamentoRemoverMenuNavegacao = AnimationUtils.loadAnimation(getContext(), R.anim.remover_menu_navegacao);

        calcularMediaRemoverMenuNavegacao = AnimationUtils.loadAnimation(getContext(), R.anim.remover_menu_navegacao);

        listenerConfirmarFechamentoRemoverMenuNavegacao();

        listenerCalculcarMediaRemoverMenuNavegacao();
    }

    private void esconderMenuNavegacao() {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        frameLayout.setVisibility(View.INVISIBLE);

        fragmentTransaction.hide(menuNavegacao);

        fragmentTransaction.commit();
    }

    @Override
    public void esconderBtnCalcularMedia() {

        btMedia.setVisibility(View.INVISIBLE);
    }

    @Override
    public void exibirAvisoNenhumaProva() {

        ativarBotao();

        Toast.makeText(getContext(),"Você não tem provas cadastradas", Toast.LENGTH_LONG).show();
    }

    private void exibirProgressBarVoador() {

        progressBarVoador.setVisibility(View.VISIBLE);

        progressBarVoador.startAnimation(animacaoProgressBarVoador);
    }

    @Override
    public void exibirAvisoApenasUmaProva() {

        ativarBotao();

        Toast.makeText(getContext(), R.string.media_apenas_uma_prova, Toast.LENGTH_LONG).show();
    }

    private void verificarDadosFechamento() {

        Integer aulasRealizadas = 0;

        Integer aulasPlanejadas = 0;

        String justificativa = etJustificativa.getText().toString();

        if(etAulasRealizadas.getText().toString().equals("")) {

            avisoUsuarioInformarAulasRealizadas();
        }
        else if(etAulasPlanejadas.getText().toString().equals("")) {

            avisoUsuarioInformarAulasPlanejadas();
        }
        else {

            aulasRealizadas = Integer.parseInt(etAulasRealizadas.getText().toString());

            aulasPlanejadas = Integer.parseInt(etAulasPlanejadas.getText().toString());

            if((aulasRealizadas < aulasPlanejadas) && justificativa.equals("")) {

                avisoUsuarioEscrevaJustificativa();
            }
            else if(aulasRealizadas > aulasPlanejadas) {

                avisoUsuarioConflitoNumeroPlanejadasRealizadas();
            }
            else if(aulasRealizadas == 0 || aulasPlanejadas == 0) {

                avisoUsuarioCamposNaoPodemSerZero();
            }
            else {

                listener.usuarioSelecionouConfirmar(aulasPlanejadas, aulasRealizadas, justificativa);
            }
        }
    }

    void setarTurmaGrupo(TurmaGrupo turmaGrupo) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(R.anim.move2, R.anim.slideback);

        int nivelTela = 1;

        bundle.putInt("NivelTela", nivelTela);

        /*
        if(anosIniciais) {

            Disciplina disciplina = turmaGrupo.getDisciplina();

            disciplina.setNomeDisciplina("CLASSE ANOS INICIAIS ENSINO FUNDAMENTAL");

            disciplina.setCodigoDisciplina(1000);

            disciplina.setId(turmaGrupo.getDisciplina().getId());

            turmaGrupo.setDisciplina(disciplina);
        }
         */

        bundle.putParcelable(TurmaGrupo.BUNDLE_TURMA_GRUPO, turmaGrupo);

        menuNavegacao.setArguments(bundle);

        fragmentTransaction.add(R.id.container, menuNavegacao, "MenuNavegacao");

        fragmentTransaction.commit();
    }

    public void registerListenerMenuNavegacao() {

        menuNavegacao.registerListenerMenuNavegacao(this);
    }

    void setarAnosIniciais(boolean anosIniciais) {

        this.anosIniciais = anosIniciais;
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

    public void avisoUsuarioSemPeriodoFechamento() {

        Toast.makeText(getContext(), getContext().getResources().getString(R.string.sem_periodo_fechamento), Toast.LENGTH_SHORT).show();
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

    private void inicializarListenerBotaoConfirma() {

        btConfirma.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                btConfirma.setClickable(false);

                if(menuAberto) {

                    removerMenuNavegacao(confirmarFechamentoRemoverMenuNavegacao);
                }
                else {

                    verificarDadosFechamento();
                }
            }
        });
    }

    private void avisoUsuarioEscrevaJustificativa() {

        Toast.makeText(getContext(), getContext().getResources().getString(R.string.aulas_realizadas_planejadas_justificativa_warning), Toast.LENGTH_LONG).show();

        etJustificativa.requestFocus();
    }

    @Override
    public void registerListener(Listener listener) {

        this.listener = listener;
    }

    private void avisoUsuarioCamposNaoPodemSerZero() {

        Toast.makeText(getContext(), getContext().getResources().getString(R.string.aulas_fechamento_zeradas), Toast.LENGTH_LONG).show();

        ativarBotao();

        etAulasRealizadas.requestFocus();
    }

    private <T extends  View> T findViewById(int id) {

        return getRootView().findViewById(id);
    }

    private void avisoUsuarioInformarAulasPlanejadas() {

        Toast.makeText(getContext(), getContext().getResources().getString(R.string.inf_aulas_planejadas), Toast.LENGTH_SHORT).show();

        ativarBotao();

        etAulasPlanejadas.requestFocus();
    }

    private void avisoUsuarioInformarAulasRealizadas() {

        Toast.makeText(getContext(), getContext().getResources().getString(R.string.inf_aulas_realizadas), Toast.LENGTH_SHORT).show();

        ativarBotao();

        etAulasRealizadas.requestFocus();
    }

    private void inicializarListenerBotaoCalcularMedia() {

        btMedia.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                btMedia.setClickable(false);

                if(menuAberto) {

                    removerMenuNavegacao(calcularMediaRemoverMenuNavegacao);
                }
                else {

                    listener.usuarioClicouCalcularMedia();
                }
            }
        });
    }

    private void removerMenuNavegacao(Animation animation) {

        menuAberto = false;

        menuNavegacao.desativarCliques();

        frameLayout.setVisibility(View.INVISIBLE);

        frameLayout.startAnimation(animation);
    }

    public void exibirNomeFechamento(String nomeFechamento) {

        tvTipoFechamento.setText(nomeFechamento);
    }

    private void listenerCalculcarMediaRemoverMenuNavegacao() {

        calcularMediaRemoverMenuNavegacao.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

                exibirProgressBarVoador();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                listener.usuarioClicouCalcularMedia();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
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

    public void exibirDadosFechamento(FechamentoTurma fechamento) {

        etJustificativa.setText(!fechamento.getJustificativa().equals("null") ? fechamento.getJustificativa() : "");

        etAulasRealizadas.setText(String.valueOf(fechamento.getAulasRealizadas()));

        etAulasPlanejadas.setText(String.valueOf(fechamento.getAulasPlanejadas()));
    }

    private void avisoUsuarioConflitoNumeroPlanejadasRealizadas() {

        Toast.makeText(getContext(), getContext().getResources().getString(R.string.aulas_realizadas_planejadas_warning), Toast.LENGTH_SHORT).show();

        ativarBotao();

        etAulasRealizadas.requestFocus();
    }

    private void listenerConfirmarFechamentoRemoverMenuNavegacao() {

        confirmarFechamentoRemoverMenuNavegacao.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

                exibirProgressBarVoador();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                verificarDadosFechamento();
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
