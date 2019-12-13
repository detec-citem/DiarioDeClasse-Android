package br.gov.sp.educacao.sed.mobile.Avaliacao;

import java.util.List;

import android.os.Bundle;

import android.content.Intent;

import android.view.LayoutInflater;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import android.support.v7.app.AppCompatActivity;

import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;

import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.CriarAcessoBanco;

public class ListaAvaliacoesActivity
        extends AppCompatActivity
         implements ListaAvaliacoesViewMvc.Listener,
                    DialogNovaAvaliacao.OnAvaliacaoCadastradaListener {

    @SuppressWarnings("FieldCanBeLocal")
    private Banco banco;

    private int bimestre;

    private Bundle bundle;

    private int disciplina;

    private int tipoAtividade;

    private TurmaGrupo turmaGrupo;

    private Avaliacao avaliacaoParaDeletar;

    @SuppressWarnings("FieldCanBeLocal")
    private CriarAcessoBanco criarAcessoBanco;

    @SuppressWarnings("FieldCanBeLocal")
    private List<Avaliacao> avaliacaoArrayList;

    public final int DIALOG_NOVA_AVALIACAO = 0;

    public final int DIALOG_EDITAR_AVALIACAO = 1;

    private AvaliacaoDBgetters avaliacaoDBgetters;
    private AvaliacaoDBsetters avaliacaoDBsetters;

    @SuppressWarnings("FieldCanBeLocal")
    private final String TAGDIALOG = "DialogNovaAvaliacao";

    private ListaAvaliacoesViewMvcImpl listaAvaliacoesViewMvc;

    private String TAG = ListaAvaliacoesActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        criarAcessoBanco = new CriarAcessoBanco();

        banco = criarAcessoBanco.gerarBanco(getApplicationContext());

        avaliacaoDBgetters = new AvaliacaoDBgetters(banco);

        avaliacaoDBsetters = new AvaliacaoDBsetters(banco);

        Intent intent = getIntent();

        bundle = intent.getExtras();

        turmaGrupo = bundle.getParcelable(TurmaGrupo.BUNDLE_TURMA_GRUPO);

        bimestre = avaliacaoDBgetters.getBimestreAtual(turmaGrupo.getTurmasFrequencia().getId());

        tipoAtividade = 0;

        avaliacaoArrayList = avaliacaoDBgetters.getAvaliacoes(

                turmaGrupo.getTurma().getId(), turmaGrupo.getDisciplina().getId(), bimestre
        );

        disciplina = 0;

        listaAvaliacoesViewMvc = new ListaAvaliacoesViewMvcImpl(

                LayoutInflater.from(this), getSupportFragmentManager(), null, avaliacaoArrayList
        );

        inicializarComponentesTela();

        verificarDisciplina();

        exibirAvaliacoesComFiltro(tipoAtividade, avaliacaoDBgetters.getBimestreAtual(

                turmaGrupo.getTurmasFrequencia().getId())
        );

        listaAvaliacoesViewMvc.exibirNomeTurmaDisciplina(

                turmaGrupo.getTurma().getNomeTurma(), turmaGrupo.getDisciplina().getNomeDisciplina()
        );

        listaAvaliacoesViewMvc.setarTurmaGrupo(turmaGrupo);

        listaAvaliacoesViewMvc.exibirBimestre(bimestre);

        listaAvaliacoesViewMvc.exibirDialogSelecaoBimestre();

        listaAvaliacoesViewMvc.exibirSelecaoTipoAtividade();

        setContentView(listaAvaliacoesViewMvc.getRootView());
    }

    @Override
    protected void onStop() {

        super.onStop();

        listaAvaliacoesViewMvc.unregisterListenerMenuNavegacao();

        listaAvaliacoesViewMvc.unregisterListener();
    }

    @Override
    protected void onStart() {

        super.onStart();

        listaAvaliacoesViewMvc.registerListenerMenuNavegacao();

        listaAvaliacoesViewMvc.registerListener(this);
    }

    @Override
    protected void onResume() {

        super.onResume();

        listaAvaliacoesViewMvc.removerProgressBarVoador();
    }

    //Mudar nome
    @Override
    public void ativarBotao() {

        listaAvaliacoesViewMvc.reativarBotaoNovaAvaliacao();
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    public void deletarAvaliacao() {

        avaliacaoDBsetters.setAvaliacaoParaDeletar(avaliacaoParaDeletar.getId());

        listaAvaliacoesViewMvc.adapter.remove(avaliacaoParaDeletar);

        listaAvaliacoesViewMvc.adapter.notifyDataSetChanged();

        avaliacaoParaDeletar = null;
    }

    private void verificarDisciplina() {

        if(turmaGrupo != null && turmaGrupo.getDisciplina().getCodigoDisciplina() != 1000) {

            disciplina = turmaGrupo.getDisciplina().getCodigoDisciplina();
        }
    }

    @Override
    public void navegarPara(Intent intent) {

        startActivity(intent);
    }

    @Override
    public void usuarioQuerCriarAvaliacao() {

        if(disciplina != 0 && disciplina != 1000) {

            bundle.putInt("Disciplina", disciplina);

            exibirDialogCriarEditarAvaliacao(bundle, DIALOG_NOVA_AVALIACAO);
        }
        else {

            listaAvaliacoesViewMvc.nenhumaDisciplinaSelecionada();
        }
    }

    private void inicializarComponentesTela() {

        if(turmaGrupo.getDisciplina().getCodigoDisciplina() == 1000) {

            listaAvaliacoesViewMvc.exibirSelecaoDisciplinaAnosIniciais(turmaGrupo.getTurma().getSerie());
        }

        listaAvaliacoesViewMvc.inicializarBotaoNovaAvaliacao();
    }

    @Override
    public void adicionarAvaliacaoCadastrada() {

        exibirAvaliacoesComFiltro(tipoAtividade, bimestre);
    }

    @Override
    public void avaliacaoEditadaRenovarLista() {

        exibirAvaliacoesComFiltro(tipoAtividade, bimestre);
    }

    @Override
    public void usuarioSelecionouBimestre(int bimestre) {

        this.bimestre = bimestre;

        exibirAvaliacoesComFiltro(tipoAtividade, bimestre);
    }

    @Override
    public void usuarioSelecionouDisciplina(int disciplina) {

        this.disciplina = converterDisciplinaParaCodigoInt(disciplina);

        exibirAvaliacoesComFiltro(tipoAtividade, bimestre);
    }

    @Override
    public void onAvaliacaoSelecionada(Avaliacao avaliacao) {

        usuarioSelecionouAvaliacao(avaliacao);
    }

    public void usuarioSelecionouAvaliacao(Avaliacao avaliacao) {

        if(avaliacaoTemDataFutura(avaliacao)) {

            listaAvaliacoesViewMvc.notasAvaliacoesFuturas();

            listaAvaliacoesViewMvc.removerProgressBarVoador();
        }
        else {

            if(avaliacao.isValeNota()) {

                navegarAplicarNotasAvaliacao(avaliacao);
            }
            else {

                listaAvaliacoesViewMvc.lancarNotasAvaliacaoNaoValeNota();

                listaAvaliacoesViewMvc.removerProgressBarVoador();
            }
        }
    }

    private boolean avaliacaoTemDataFutura(Avaliacao avaliacao) {

        boolean dataFutura = false;

        final LocalDate dataAtual = LocalDate.now();

        String dataAvaliacao = avaliacaoDBsetters.formataDataAvaliacao(avaliacao.getData());

        LocalDate dataAvalicaoFormatada = LocalDate.parse(

                dataAvaliacao, DateTimeFormat.forPattern("yyyy-MM-dd")
        );

        if(dataAvalicaoFormatada == null || dataAvalicaoFormatada.isAfter(dataAtual)) {

            dataFutura = true;
        }
        return dataFutura;
    }

    private int converterDisciplinaParaCodigoInt(int disciplina) {

        switch(disciplina) {

            case 0: //"Matemática"

                return 2700;

            case 1: //"Língua Portuguesa"

            return 1100;


            case 2: //"Ciências Natureza/Humanas"

                return 7245;

            default:

                return 0;
        }
    }

    @Override
    public void usuarioQuerDeletarAvaliacao(Avaliacao avaliacao) {

        this.avaliacaoParaDeletar = avaliacao;

        listaAvaliacoesViewMvc.exibirDialogDeletarAvaliacao();
    }

    private void navegarAplicarNotasAvaliacao(Avaliacao avaliacao) {

        bundle.putParcelable(TurmaGrupo.BUNDLE_TURMA_GRUPO, turmaGrupo);

        bundle.putParcelable(Avaliacao.BUNDLE_AVALIACAO, avaliacao);

        Intent intent = new Intent(ListaAvaliacoesActivity.this, AvaliacoesSliderActivity.class);

        intent.putExtras(bundle);

        startActivity(intent);
    }

    private void exibirAvaliacoesComFiltro(int tipo, int bimestre) {

        List<Avaliacao> avaliacoes;

        if(turmaGrupo.getDisciplina().getCodigoDisciplina() == 1000) {

            avaliacoes = avaliacaoDBgetters.getAvaliacoesByFiltroAnosIniciais(

                    turmaGrupo, tipo, bimestre, disciplina
            );
        }
        else {

            avaliacoes = avaliacaoDBgetters.getAvaliacoesByFiltro(

                    turmaGrupo, tipo, bimestre
            );
        }
        listaAvaliacoesViewMvc.inicializarListaAvaliacoes(avaliacoes);
    }

    @Override
    public void configurarDialogEditarAvaliacao(Avaliacao avaliacao) {

        Bundle bundle = new Bundle();

        bundle.putParcelable(TurmaGrupo.BUNDLE_TURMA_GRUPO, turmaGrupo);

        bundle.putParcelable(Avaliacao.BUNDLE_AVALIACAO, avaliacao);

        exibirDialogCriarEditarAvaliacao(bundle, DIALOG_EDITAR_AVALIACAO);
    }

    @Override
    public void usuarioSelecionouTipoAtividade(String tipoAtividade) {

        this.tipoAtividade = converterTipoAtividadeParaCodigoInt(tipoAtividade);

        exibirAvaliacoesComFiltro(this.tipoAtividade, bimestre);
    }

    private int converterTipoAtividadeParaCodigoInt(String tipoAtividade) {

        switch(tipoAtividade) {

            case Avaliacao.TIPO_AVALIACAO:

                return Avaliacao.CODIGO_TIPO_AVALIACAO;

            case Avaliacao.TIPO_ATIVIDADE:

                return Avaliacao.CODIGO_TIPO_ATIVIDADE;

            case Avaliacao.TIPO_TRABALHO:

                return Avaliacao.CODIGO_TIPO_TRABALHO;

            case Avaliacao.TIPO_OUTROS:

                return Avaliacao.CODIGO_TIPO_OUTROS;

            case Avaliacao.TODOS:

                return Avaliacao.CODIGO_TODOS;

            default:

                return 0;
        }
    }

    public void exibirDialogCriarEditarAvaliacao(Bundle bundle, int tipoDialog) {

        DialogNovaAvaliacao dialogNovaAvaliacao = new DialogNovaAvaliacao();

        dialogNovaAvaliacao.escolherTipoDialog(tipoDialog);

        dialogNovaAvaliacao.setArguments(bundle);

        dialogNovaAvaliacao.setCancelable(false);

        dialogNovaAvaliacao.setOnAvaliacaoCadastradaListener(this);

        dialogNovaAvaliacao.show(getSupportFragmentManager(), TAGDIALOG);
    }
}
