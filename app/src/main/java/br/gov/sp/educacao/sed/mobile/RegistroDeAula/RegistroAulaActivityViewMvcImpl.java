package br.gov.sp.educacao.sed.mobile.RegistroDeAula;

import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;

import android.os.Bundle;

import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.gov.sp.educacao.sed.mobile.Frequencia.FragmentHorarios;
import br.gov.sp.educacao.sed.mobile.R;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;

class RegistroAulaActivityViewMvcImpl
        implements RegistroAulaActivityViewMvc,
        ConteudoRegistroAdapter.OnConteudoSelecionadoListener,
        HabilidadesRegistroAdapter.OnHabilidadeSelecionadaListener,
        DialogAnosIniciais.Listener,
        MenuNavegacao.usuarioSelecionouMenuNavegacao, FragmentHorarios.fragmentHorariosListener {

    private final View mRootView;

    private Listener listener;

    private LayoutInflater layoutInflater;

    private FragmentManager fragmentManager;

    private ListView listViewConteudo;
    private ListView listViewHabilidades;

    private TextView txtSelBimestre;
    private TextView txtContHab;
    private TextView tvTurma;

    private DialogAnosIniciais dialogAnosIniciais;

    private HabilidadesRegistroAdapter adapterHabilidade;

    private ConteudoRegistroAdapter conteudoRegistroAdapter;

    private Button btn_salvar_registro, editObservacoes, selecionarDisciplina;

    private TextView menu, bloco1, bloco2, bloco3, bloco4;

    private EditText etObservacoes;

    private Toolbar toolbar;

    private ToolbarViewMvcImpl toolbarViewMvcImpl;

    private String observacoes;

    private int blocoInicial;

    private FragmentHorarios selecaoHorarios;

    private Bundle bundle;

    private Intent intent;

    private ProgressBar progressBarVoador;

    private boolean menuAberto;

    private MenuNavegacao menuNavegacao;

    private FrameLayout frameLayout, frameLayout2, frameLayout3;

    private Animation apenasRemoverMenuNavegacao, mudarTelaRemoverMenuNavegacao, animacaoProgressBarVoador;

    public final String TAG = "DialogAnosIniciais";

    RegistroAulaActivityViewMvcImpl(LayoutInflater layoutInflater, FragmentManager fragmentManager, ViewGroup parent) {

        mRootView = layoutInflater.inflate(R.layout.activity_new_hab_comp2, parent, false);

        listViewConteudo = findViewById(R.id.listConteudo);

        listViewHabilidades = findViewById(R.id.listHabilidades);

        txtSelBimestre = findViewById(R.id.txt_seleciona_bimestre);

        bloco1 = findViewById(R.id.btn_1bim);
        bloco2 = findViewById(R.id.btn_2bim);
        bloco3 = findViewById(R.id.btn_3bim);
        bloco4 = findViewById(R.id.btn_4bim);

        txtContHab = findViewById(R.id.registro_txtContHab);

        tvTurma = findViewById(R.id.tv_turma);

        txtContHab.setText("Conteúdos");

        blocoInicial = 0;

        btn_salvar_registro = findViewById(R.id.btn_salvar_registro);

        selecionarDisciplina = findViewById(R.id.selecionarDisciplina);

        editObservacoes = findViewById(R.id.editTxt1);

        etObservacoes = findViewById(R.id.et_observacoes);

        toolbar = findViewById(R.id.toolbar);

        this.layoutInflater = layoutInflater;

        this.fragmentManager = fragmentManager;

        frameLayout = findViewById(R.id.container);

        frameLayout2 = findViewById(R.id.container1);

        frameLayout3 = findViewById(R.id.container2);

        progressBarVoador = findViewById(R.id.progressBar);

        menuAberto = false;

        menuNavegacao = new MenuNavegacao();

        selecaoHorarios = new FragmentHorarios();

        bundle = new Bundle();

        intent = null;

        toolbarViewMvcImpl = getToolbarViewMvcImpl(toolbar);

        inicializarToolbar();

        inicializarAnimacoes();

        inicializarListenersBlocos();

        inicializarListenerObservacoes();

        inicializarListenerSalvarRegistro();

        inicializarBotaoOpcoesAnosIniciais();

        registerFragmentHorariosListener();
    }

    private void inicializarAnimacoes() {

        animacaoProgressBarVoador = AnimationUtils.loadAnimation(getContext(), R.anim.exibir_progress_voador);

        mudarTelaRemoverMenuNavegacao = AnimationUtils.loadAnimation(getContext(), R.anim.remover_menu_navegacao);

        apenasRemoverMenuNavegacao = AnimationUtils.loadAnimation(getContext(), R.anim.remover_menu_navegacao);
    }

    void mostrarFundoBranco(Fragment calendario) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.container3, calendario, "FragmentCalendario");

        fragmentTransaction.show(calendario);

        fragmentTransaction.commit();

        frameLayout3.setVisibility(View.VISIBLE);
    }

    void esconderFundoBranco(Fragment calendario) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        frameLayout3.setVisibility(View.INVISIBLE);

        fragmentTransaction.hide(calendario);

        fragmentTransaction.commit();
    }

    private void registerFragmentHorariosListener() {

        selecaoHorarios.registerListener(this);
    }

    void exibirOpcoesHorarios(List<String> listaHorarios) {

        iniciarSelecaoHorarios(listaHorarios);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.show(selecaoHorarios);

        fragmentTransaction.commit();

        frameLayout2.setVisibility(View.VISIBLE);
    }

    void removerSelecaoHorarios() {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        frameLayout2.setVisibility(View.INVISIBLE);

        fragmentTransaction.hide(selecaoHorarios);

        fragmentTransaction.commit();
    }

    private void iniciarSelecaoHorarios(List<String> listaHorarios) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //List<String> listaHorariossComLancamento = listener.pegarHorariosComLancamentos();

        Bundle bundle = new Bundle();

        bundle.putStringArrayList("lista", (ArrayList) listaHorarios);

        bundle.putStringArrayList("lista2", new ArrayList<String>());

        selecaoHorarios.setArguments(bundle);

        fragmentTransaction.add(R.id.container1, selecaoHorarios, "FragmentHorarios");

        fragmentTransaction.commit();
    }

    private void inicializarListenerObservacoes() {

        editObservacoes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(menuAberto) {

                    removerMenuNavegacao(apenasRemoverMenuNavegacao);
                }

                if(etObservacoes.getVisibility() == View.GONE) {

                    txtContHab.setText("Observações");

                    txtSelBimestre.setVisibility(View.GONE);

                    etObservacoes.setVisibility(View.VISIBLE);

                    listViewConteudo.setVisibility(View.GONE);

                    listViewHabilidades.setVisibility(View.GONE);
                }
                else {

                    txtSelBimestre.setVisibility(View.VISIBLE);

                    configurarBlocos(blocoInicial);
                }
            }
        });
    }

    private void inicializarBotaoOpcoesAnosIniciais() {

        selecionarDisciplina.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                listener.abrirOpcoesAnosIniciais();
            }
        });
    }

    private void inicializarListenerSalvarRegistro() {

        btn_salvar_registro.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                usuarioClicouBotaoSalvarRegistro();
            }
        });
    }

    private void inicializarListenersBlocos() {

        bloco1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(menuAberto) {

                    removerMenuNavegacao(apenasRemoverMenuNavegacao);
                }

                bloco1.setBackgroundResource(R.drawable.button_pressed);
                bloco2.setBackgroundResource(R.drawable.button_enabled);
                bloco3.setBackgroundResource(R.drawable.button_enabled);
                bloco4.setBackgroundResource(R.drawable.button_enabled);

                listener.usuarioSelecionouBloco(1);
            }
        });

        bloco2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(menuAberto) {

                    removerMenuNavegacao(apenasRemoverMenuNavegacao);
                }

                bloco1.setBackgroundResource(R.drawable.button_enabled);
                bloco2.setBackgroundResource(R.drawable.button_pressed);
                bloco3.setBackgroundResource(R.drawable.button_enabled);
                bloco4.setBackgroundResource(R.drawable.button_enabled);

                listener.usuarioSelecionouBloco(2);
            }
        });

        bloco3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(menuAberto) {

                    removerMenuNavegacao(apenasRemoverMenuNavegacao);
                }

                bloco1.setBackgroundResource(R.drawable.button_enabled);
                bloco2.setBackgroundResource(R.drawable.button_enabled);
                bloco3.setBackgroundResource(R.drawable.button_pressed);
                bloco4.setBackgroundResource(R.drawable.button_enabled);

                listener.usuarioSelecionouBloco(3);
            }
        });

        bloco4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(menuAberto) {

                    removerMenuNavegacao(apenasRemoverMenuNavegacao);
                }

                bloco1.setBackgroundResource(R.drawable.button_enabled);
                bloco2.setBackgroundResource(R.drawable.button_enabled);
                bloco3.setBackgroundResource(R.drawable.button_enabled);
                bloco4.setBackgroundResource(R.drawable.button_pressed);

                listener.usuarioSelecionouBloco(4);
            }
        });
    }

    private void inicializarToolbar() {

        toolbarViewMvcImpl.setTitle("Registro de Aula");

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

    private void exibirProgressBarVoador() {

        progressBarVoador.setVisibility(View.VISIBLE);

        progressBarVoador.startAnimation(animacaoProgressBarVoador);
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

    private void removerMenuNavegacao(Animation animation) {

        menuAberto = false;

        menuNavegacao.desativarCliques();

        frameLayout.setVisibility(View.INVISIBLE);

        frameLayout.startAnimation(animation);
    }

    private void esconderMenuNavegacao() {

        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        frameLayout.setVisibility(View.INVISIBLE);

        fragmentTransaction.hide(menuNavegacao);

        fragmentTransaction.commit();
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

    void removerProgressBarVoador() {

        progressBarVoador.setVisibility(View.GONE);
    }

    @Override
    public void avisoAlteracoesPendentes() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getRootView().getContext());

        builder.setMessage("Há alterações que não foram salvas. Deseja mesmo sair da tela de registros?");

        builder.setPositiveButton("CANCELAR", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                dialog.dismiss();
            }
        });

        builder.setPositiveButton("SAIR", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                listener.usuarioClicouSairRegistro();
            }
        });

        android.app.AlertDialog alertDialog = builder.create();

        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);

        alertDialog.show();
    }

    @Override
    public void navegarPara(final Intent intent) {

        this.intent = intent;

        listenerAnimacaoMudarTelaRemoverMenuNavegacao(mudarTelaRemoverMenuNavegacao);

        removerMenuNavegacao(mudarTelaRemoverMenuNavegacao);
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

    private <T extends View> T findViewById(int id) {

        return getRootView().findViewById(id);
    }

    private Context getContext() {

        return getRootView().getContext();
    }

    @Override
    public ToolbarViewMvcImpl getToolbarViewMvcImpl(@Nullable ViewGroup parent) {

        return new ToolbarViewMvcImpl(layoutInflater, parent);
    }

    @Override
    public View getRootView() {

        return mRootView;
    }

    @Override
    public void registerListener(Listener listener) {

        this.listener = listener;
    }

    @Override
    public void unregisterListener() {

        this.listener = null;
    }

    public void registerListenerMenuNavegacao() {

        menuNavegacao.registerListenerMenuNavegacao(this);
    }

    public void unregisterListenerMenuNavegacao() {

        menuNavegacao.unregisterListener();
    }

    void selecaoDisciplinaAnosIniciais() {

        selecionarDisciplina.setVisibility(View.VISIBLE);

        txtSelBimestre.setText("Selecione a disciplina");
    }

    void exibirNomeTurmaTipoEnsino(String nomeTurma, String tipoEnsino) {

        String tvTurmaText = nomeTurma + " / " + tipoEnsino;

        tvTurma.setText(tvTurmaText);
    }

    void exibirHabilidades(List<Habilidade> listaHabilidades) {

        adapterHabilidade = new HabilidadesRegistroAdapter(getContext(), this);

        adapterHabilidade.addAll(listaHabilidades);

        listViewHabilidades.setAdapter(adapterHabilidade);
    }

    void usuarioAvisoSelecioneDisciplina() {

        Toast.makeText(getContext(), "Selecione a Disciplina", Toast.LENGTH_SHORT).show();
    }

    void configurarBlocos(int bloco) {

        blocoInicial = bloco;

        colorirBotaoBlocoInicial(bloco);

        txtSelBimestre.setVisibility(View.GONE);

        if(listViewHabilidades.getVisibility() == View.VISIBLE) {

            listViewHabilidades.setVisibility(View.GONE);
        }

        if(etObservacoes.getVisibility() == View.VISIBLE) {

            etObservacoes.setVisibility(View.GONE);

            observacoes = etObservacoes.getText().toString();
        }

        txtContHab.setText("Conteúdos");

        listViewConteudo.setVisibility(View.VISIBLE);
    }

    private void colorirBotaoBlocoInicial(int bloco) {

        bloco1.setBackgroundResource(R.drawable.button_enabled);
        bloco2.setBackgroundResource(R.drawable.button_enabled);
        bloco3.setBackgroundResource(R.drawable.button_enabled);
        bloco4.setBackgroundResource(R.drawable.button_enabled);

        switch(bloco) {

            case 1: {

                bloco1.setBackgroundResource(R.drawable.button_pressed);

                break;
            }
            case 2: {

                bloco2.setBackgroundResource(R.drawable.button_pressed);

                break;
            }
            case  3: {

                bloco3.setBackgroundResource(R.drawable.button_pressed);

                break;
            }
            case 4: {

                bloco4.setBackgroundResource(R.drawable.button_pressed);

                break;
            }
        }
    }

    void exibirListaConteudos(List<Conteudo> listaConteudos) {

        conteudoRegistroAdapter = new ConteudoRegistroAdapter(getContext(), this);

        conteudoRegistroAdapter.addAll(listaConteudos);

        listViewConteudo.setAdapter(conteudoRegistroAdapter);
    }

    public void avisoUsuarioNenhumConteudoParaExibir() {

        Toast.makeText(getContext(), "Sem conteúdo para este bloco", Toast.LENGTH_SHORT).show();
    }

    void avisoUsuarioRegistroSalvo() {

        AlertDialog alerta;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Registro salvo!");

        builder.setMessage("O registro de aula foi salvo no dispositivo. " +
                "Sincronize o aplicativo para que os dados sejam enviados para a SED.");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int arg1) {

                dialog.dismiss();
            }
        });

        alerta = builder.create();

        alerta.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);

        alerta.show();
    }

    public void usuarioClicouBotaoSalvarRegistro() {

        if(menuAberto) {

            removerMenuNavegacao(apenasRemoverMenuNavegacao);
        }

        String observacoes = etObservacoes.getText().toString();

        listener.salvarRegistro(observacoes);
    }

    void avisoUsuarioSelecioneConteudoObservacao() {

        Toast.makeText(getContext(), "Selecione um conteúdo ou escreva uma observação", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConteudoSelecionado(Conteudo conteudo) {

        if(menuAberto) {

            removerMenuNavegacao(apenasRemoverMenuNavegacao);
        }

        listViewConteudo.setVisibility(View.GONE);

        listViewHabilidades.setVisibility(View.VISIBLE);

        txtContHab.setText("Habilidades");

        listener.usuarioSelecionouConteudo(conteudo);
    }

    @Override
    public void onHabilidadeSelecionada(Habilidade habilidade) {

        if(menuAberto) {

            removerMenuNavegacao(apenasRemoverMenuNavegacao);
        }

        listener.usuarioSelecionouHabilidade(habilidade);
    }

    void recarregarListasExibidas() {

        conteudoRegistroAdapter.notifyDataSetChanged();

        adapterHabilidade.notifyDataSetChanged();
    }

    void exibirObservacoes(String observacoes) {

        String etObservacoesText = "";

        if(this.observacoes == null) {

            etObservacoesText = observacoes;
        }
        else {

            etObservacoesText = this.observacoes;
        }

        etObservacoes.setText(etObservacoesText);
    }

    void exibirOpcoesAnosIniciais(FragmentManager fragmentManager) {

        dialogAnosIniciais = new DialogAnosIniciais();

        dialogAnosIniciais.show(fragmentManager, TAG);

        dialogAnosIniciais.registerListener(this);
    }

    void removerOpcoesAnosIniciais() {

        dialogAnosIniciais.dismiss();
    }

    @Override
    public void selecionouMateria(int codigoMateria) {

        listener.usuarioSelecionouMateriaAnosIniciais(codigoMateria);
    }

    boolean conteudosEmExibicao() {

        boolean conteudoEmExibicao = false;

        if(listViewConteudo.getVisibility() == View.VISIBLE) {

            conteudoEmExibicao = true;
        }
        return conteudoEmExibicao;
    }

    void exibirConteudos() {

        txtContHab.setText("Conteúdos");

        listViewHabilidades.setVisibility(View.GONE);

        etObservacoes.setVisibility(View.GONE);

        listViewConteudo.setVisibility(View.VISIBLE);
    }

    void avisoUsuarioSelecioneData() {

        Toast.makeText(getContext(), "Selecione uma data", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void usuarioQuerFecharSelecaoHorarios() {

        listener.usuarioQuerFecharSelecaoHorarios();
    }

    @Override
    public void onHorarioSelecionado(String horario) {

        listener.usuarioSelecionouHorario(horario);
    }

    @Override
    public void onHorarioChecado(String horario) {

        listener.usuarioChecouHorario(horario);
    }

    @Override
    public void usuarioQuerEditarFrequencia(String horario) {

        //excluir esse método
    }

    @Override
    public void usuarioQuerExcluirFrequencia(String horario) {

        //Excluir Registro?
    }

    @Override
    public void usuarioQuerAvancar() {

        listener.usuarioQuerAvancar();
    }

    public void avisoUsuarioSelecioneUmOuMaisHorarios() {

        Toast.makeText(getContext(), "Selecione um ou mais horários de aula", Toast.LENGTH_SHORT).show();
    }
}
