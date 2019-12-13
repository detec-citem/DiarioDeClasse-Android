package br.gov.sp.educacao.sed.mobile.Frequencia;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.content.Intent;
import android.content.Context;
import android.content.DialogInterface;

import br.gov.sp.educacao.sed.mobile.R;

import android.support.v7.widget.Toolbar;
import android.support.v7.app.AlertDialog;

import android.support.annotation.Nullable;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;

class FrequenciaLancamentoViewMvcImpl
        implements FrequenciaLancamentoViewMvc,
        LancamentoAlunoAdapter.OnFrequenciaSelecionadaListener,
        MenuNavegacao.usuarioSelecionouMenuNavegacao,
        FragmentHorarios.fragmentHorariosListener{

    private Bundle bundle;

    private Intent intent;

    private Toolbar toolbar;

    private Listener listener;

    private boolean menuAberto;

    private boolean selecaoHorariosAberto;

    private boolean listaEscondida;

    private TextView menu;
    private TextView tvDia;
    private TextView tvTurma;
    private TextView tvHorario;

    private ImageView fecharSelecaoCalendario;

    private int telaPequena;

    private final View mRootView;

    private ListView lvLancamento;

    private FrameLayout frameLayout;

    private AlertDialog dialogExcluirFrequencia;

    private FrameLayout frameLayout2;

    private FrameLayout frameLayout3;

    //private FrameLayout frameLayout4;

    private FragmentHorarios selecaoHorarios;

    private MenuNavegacao menuNavegacao;

    private LayoutInflater layoutInflater;

    private ProgressBar progressBarVoador;

    private LancamentoAlunoAdapter adapter;

    private FragmentManager fragmentManager;

    private Button btnReplica;
    private Button btConfirmar;
    private Button btnExcluirFrequencia;
    private Button btnAplicarPresencaTodosAlunos;

    private ToolbarViewMvcImpl toolbarViewMvcImpl;

    private Animation selecionarDataRemoverMenuNavegacao;

    private Animation apenasRemoverMenuNavegacao, mudarTelaRemoverMenuNavegacao, animacaoProgressBarVoador;

    FrequenciaLancamentoViewMvcImpl(LayoutInflater layoutInflater, FragmentManager fragmentManager, int tamanhoTela, ViewGroup parent) {

        this.telaPequena = tamanhoTela;

        if(telaPequena == 2) {

            mRootView = layoutInflater.inflate(R.layout.activity_frequencia_lancamento_tela_muito_pequena, parent, false);
        }
        else {

            mRootView = layoutInflater.inflate(R.layout.activity_frequencia_lancamento, parent, false);
        }

        inicializarViews();

        bundle = new Bundle();

        this.layoutInflater = layoutInflater;

        this.fragmentManager = fragmentManager;

        toolbar = findViewById(R.id.toolbar);

        menuNavegacao = new MenuNavegacao();

        selecaoHorarios = new FragmentHorarios();

        progressBarVoador = findViewById(R.id.progressBar);

        toolbarViewMvcImpl = getToolbarViewMvcImpl(toolbar);

        frameLayout = findViewById(R.id.container);

        frameLayout2 = findViewById(R.id.container1);

        frameLayout3 = findViewById(R.id.container2);

        //frameLayout4 = findViewById(R.id.container3);

        fecharSelecaoCalendario = findViewById(R.id.fecharselecaocalendario);

        menuAberto = false;

        listaEscondida = false;

        inicializarToolbar();

        inicializarAnimacoes();

        inicializarListeners();

        registerFragmentHorariosListener();

        exibirData("   --/--/--   ");
    }

    @Override
    public void usuarioQuerExcluirFrequencia(String horario) {

        listener.usuarioQuerExcluirFrequencia(horario);
    }

    @Override
    public void usuarioQuerAvancar() {

        listener.usuarioQuerAvancar();
    }

    @Override
    public void onHorarioChecado(String horario) {

        listener.usuarioChecouHorario(horario);
    }

    @Override
    public void onHorarioSelecionado(String horario) {

        tvHorario.setText(horario);

        listener.usuarioSelecionouHorario(horario);
    }

    @Override
    public void usuarioQuerEditarFrequencia(String horario) {


    }

    @Override
    public void usuarioQuerFecharSelecaoHorarios() {

        tvHorario.setText("-- / --");

        listener.usuarioQuerFecharSelecaoHorarios();
    }

    void excluirFrequenciaNoBancoLocal() {

        listener.excluirFrequenciaNoBancoLocal();
    }

    void confirmarExclusaoDeFrequencia(String data, final String horario) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Excluir Frequência")
                .setMessage("Deseja excluir a frequência do dia " + data + " " + " no horário " + horario + "?")
                .setPositiveButton(R.string.confirma, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        listener.excluirFrequencia(horario);

                        dialog.dismiss();
                    }
                });

        builder.setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();

        dialog.setCanceledOnTouchOutside(false);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);

        dialog.show();
    }

    void usuarioAvisoFalha() {

        finalizaProgress();

        Toast.makeText(getContext(), "Não foi possível excluir a frequência", Toast.LENGTH_LONG).show();
    }

    void usuarioAvisoSucesso() {

        finalizaProgress();

        Toast.makeText(getContext(), "Frequência excluída com sucesso", Toast.LENGTH_LONG).show();
    }

    private void finalizaProgress() {

        dialogExcluirFrequencia.dismiss();
    }

    void inicializaProgress() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(

                getContext(), R.style.ThemeOverlay_AppCompat_Dialog
        );

        View view = layoutInflater.inflate(R.layout.dialog_excluir_frequencia, null, false);

        builder.setView(view);

        dialogExcluirFrequencia = builder.create();

        dialogExcluirFrequencia.setCancelable(false);

        dialogExcluirFrequencia.setCanceledOnTouchOutside(false);

        dialogExcluirFrequencia.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);

        dialogExcluirFrequencia.show();
    }

    private void registerFragmentHorariosListener() {

        selecaoHorarios.registerListener(this);
    }

    void recarregarLista() {

        if(adapter != null) {

            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public View getRootView() {

        return mRootView;
    }

    void irParaPrimeiroAluno() {

        lvLancamento.smoothScrollToPosition(0);
    }

    private Context getContext() {

        return getRootView().getContext();
    }

    void exibirData(String data) {

        tvDia.setText(data);
    }

    private void inicializarViews() {

        tvHorario = findViewById(R.id.tv_horario);

        tvDia = findViewById(R.id.tv_dia);

        btConfirmar = findViewById(R.id.bt_confirmar_alunos);

        btnExcluirFrequencia = findViewById(R.id.bt_excluir_frequencia);

        btnAplicarPresencaTodosAlunos = findViewById(R.id.btnMarcaPresencaTodos);

        tvTurma = findViewById(R.id.tv_diretoria);

        btnReplica = findViewById(R.id.btnReplica);

        lvLancamento = findViewById(R.id.lv_lancamento);
    }

    void removerProgressBarVoador() {

        progressBarVoador.setVisibility(View.GONE);
    }

    @Override
    public void unregisterListener() {

        this.listener = null;
    }

    void exibirSelecaoHorariosAula() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Replicar aula").setMessage(R.string.dialog_replicar_chamada)
                .setPositiveButton(R.string.confirma, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        listener.replicarChamadaMultiplosHorarios(tvDia.getText().toString(), tvHorario.getText().toString());

                        adapter.notifyDataSetChanged();

                        Toast.makeText(getContext(), getContext().getString(R.string.chamada_replicada), Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    }
                });

        builder.setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();

        dialog.setCanceledOnTouchOutside(false);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);

        dialog.show();
    }

    private void inicializarToolbar() {

        toolbarViewMvcImpl.setTitle(getContext().getResources().getString(R.string.frequencia));

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

    void avisarUsuarioSelecionarData() {

        Toast.makeText(getContext(), "Selecione uma data e horário de aula", Toast.LENGTH_LONG).show();

    }

    @Override
    public void aplicarNA(Aluno aluno) {

        listener.aplicarNA(aluno);
    }

    private void inicializarAnimacoes() {

        animacaoProgressBarVoador = AnimationUtils.loadAnimation(getContext(), R.anim.exibir_progress_voador);

        mudarTelaRemoverMenuNavegacao = AnimationUtils.loadAnimation(getContext(), R.anim.remover_menu_navegacao);

        apenasRemoverMenuNavegacao = AnimationUtils.loadAnimation(getContext(), R.anim.remover_menu_navegacao);

        selecionarDataRemoverMenuNavegacao = AnimationUtils.loadAnimation(getContext(), R.anim.remover_menu_navegacao);

        listenerAnimacaoSelecionarDataRemoverMenuNavegacao();
    }

    private void inicializarListeners() {

        btConfirmar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                listener.usuarioClicouConfirmar();
            }
        });

        fecharSelecaoCalendario.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                listener.usuarioQuerFecharSelecaoCalendario();
            }
        });

        tvDia.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(menuAberto) {

                    removerMenuNavegacao(selecionarDataRemoverMenuNavegacao);
                }
                else {

                    listener.inicializarCalendario();
                }

            }
        });

        btnAplicarPresencaTodosAlunos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                listener.aplicarPresencaParaTodosAlunos(view);
            }
        });

        btnReplica.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                listener.replicarChamadaMultiplosHorarios("", "");
            }
        });
    }

    private void esconderMenuNavegacao() {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        frameLayout.setVisibility(View.INVISIBLE);

        fragmentTransaction.hide(menuNavegacao);

        fragmentTransaction.commit();
    }

    @Override
    public void aplicarFalta(Aluno aluno) {

        listener.aplicarFalta(aluno);
    }

    void avisarUsuarioNaoPossuiHorarios() {

        Toast.makeText(getContext(), "Você não tem horários para duplicar", Toast.LENGTH_SHORT).show();
    }

    public void exibirProgressBarVoador() {

        progressBarVoador.setVisibility(View.VISIBLE);

        progressBarVoador.startAnimation(animacaoProgressBarVoador);
    }

    @Override
    public void aplicarPresenca(Aluno aluno) {

        listener.aplicarPresenca(aluno);
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

    public void registerListenerMenuNavegacao() {

        menuNavegacao.registerListenerMenuNavegacao(this);
    }

    @Override
    public void avisarUsuarioChamadaRealizada() {

        Toast.makeText(getContext(),getContext().getResources().getString(R.string.frequencia_todos_alunos), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void avisarUsuarioChamadaIncompleta() {

        Toast.makeText(getContext(),"É necessário lançar frequência para todos alunos", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void resgatarFaltasAluno(Aluno aluno) {

        listener.resgatarFaltasAluno(aluno);
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

    @Override
    public void irParaProximoAlunoAtivo(Aluno aluno) {

        listener.irParaProximoAlunoAtivo(aluno);
    }

    private void removerMenuNavegacao(Animation animation) {

        menuAberto = false;

        menuNavegacao.desativarCliques();

        frameLayout.setVisibility(View.INVISIBLE);

        frameLayout.startAnimation(animation);
    }

    void irParaProximoAlunoAtivo(int index, final int count) {

        if(telaPequena == 1) { //Tela maior que 880px e menor que 1000px de altura

            lvLancamento.smoothScrollBy(300 * (count), 475);
        }
        else if(telaPequena == 2) { //Tela menor que 880px de altura

            lvLancamento.smoothScrollByOffset(count);
        }
        else { //Tela maior que 1000 px de altura

            lvLancamento.smoothScrollToPosition(index);
        }

        lvLancamento.startLayoutAnimation();
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

    void exibirDadosAula(String nomeTurma, String nomeTipoEnsino) {

        String tvTurmaText = nomeTurma + " / " + nomeTipoEnsino;

        tvTurma.setText(tvTurmaText);
    }

    void setTelaMaisQueUmHorario(final List<String> listaHorario, final List<String> listaHorariosSelecionados) {

        tvHorario.setText("-- / --");

        tvHorario.setBackgroundResource(R.drawable.button_blue);

        if(!selecaoHorarios.isAdded()) {

            iniciarSelecaoHorarios(listaHorario);
        }
        else {

            selecaoHorarios.atualizarListaHorariosComLancamento(listener.pegarHorariosComLancamentos());

            selecaoHorarios.atualizarListHorariosSelecionados(listaHorariosSelecionados);
        }

        tvHorario.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(!selecaoHorariosAberto) {

                    selecaoHorariosAberto = true;

                    buscarListaHorariosComFrequenciaLancada();

                    exibirOpcoesHorarios();
                }
                else {

                    selecaoHorariosAberto = false;

                    removerSelecaoHorarios();
                }

                selecaoHorarios.atualizarListHorariosSelecionados(listaHorariosSelecionados);
            }
        });
    }

    void exibirOpcoesHorarios() {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.show(selecaoHorarios);

        fragmentTransaction.commit();

        frameLayout2.setVisibility(View.VISIBLE);
    }

    void removerSelecaoHorarios() {

        selecaoHorariosAberto = false;

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        frameLayout2.setVisibility(View.INVISIBLE);

        fragmentTransaction.hide(selecaoHorarios);

        fragmentTransaction.commit();
    }

    private void listenerAnimacaoSelecionarDataRemoverMenuNavegacao() {

        selecionarDataRemoverMenuNavegacao.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

                exibirProgressBarVoador();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                removerProgressBarVoador();

                listener.inicializarCalendario();
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

    @Override
    public void exibirListaAlunos(List<Aluno> listaAlunos, TurmaGrupo turmaGrupo) {

        if(adapter == null) {

            adapter = new LancamentoAlunoAdapter(getContext(), this, turmaGrupo);

            adapter.addAll(listaAlunos);

            lvLancamento.setAdapter(adapter);

            lvLancamento.setItemsCanFocus(false);

            btConfirmar.setVisibility(View.VISIBLE);
        }
        else {

            recarregarLista();
        }

        if(listaEscondida) {

            mostrarLista();
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

    private void iniciarSelecaoHorarios(List<String> listaHorarios) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        List<String> listaHorariossComLancamento = listener.pegarHorariosComLancamentos();

        Bundle bundle = new Bundle();

        bundle.putStringArrayList("lista", (ArrayList) listaHorarios);

        bundle.putStringArrayList("lista2", (ArrayList) listaHorariossComLancamento);

        selecaoHorarios.setArguments(bundle);

        fragmentTransaction.add(R.id.container1, selecaoHorarios, "FragmentHorarios");

        fragmentTransaction.commit();
    }

    void usuarioAvisoLancamentoExistente() {

        Toast.makeText(getContext(), "Você já possui um lançamento para esta data e horário em outra turma", Toast.LENGTH_LONG).show();
    }

    private void buscarListaHorariosComFrequenciaLancada() {

        selecaoHorarios.atualizarListaHorariosComLancamento(listener.pegarHorariosComLancamentos());
    }

    void esconderLista() {

        lvLancamento.setVisibility(View.INVISIBLE);

        listaEscondida = true;
    }

    void mostrarLista() {

        lvLancamento.setVisibility(View.VISIBLE);

        listaEscondida = false;
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

    void usuarioAvisoLimiteSemanalAlcancado() {

        tvHorario.setText("-- / --");

        Toast.makeText(

                getContext(), "Você alcançou o limite de lançamentos na semana, exclua uma frequência desta semana para continuar", Toast.LENGTH_LONG

        ).show();
    }

    void desmarcarHorario(String horario) {

        selecaoHorarios.desmarcarHorario(horario);
    }

    void deschecarUltimoHorarioChecado(String horario) {

        selecaoHorarios.deschecarUltimoHorarioChecado(horario);
    }

    public void avisoUsuarioSelecioneHorario() {

        Toast.makeText(getContext(), "Selecione ao menos um horário", Toast.LENGTH_SHORT).show();
    }

    public void mostrarPrimeiroHorarioDosSelecionados(String horario) {

        tvHorario.setText(horario);
    }
}

