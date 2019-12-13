package br.gov.sp.educacao.sed.mobile.Turmas;

import android.os.Bundle;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import android.content.Intent;

import android.view.LayoutInflater;

import android.support.v7.app.AppCompatActivity;

import br.gov.sp.educacao.sed.mobile.Escola.Bimestre;

import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.Analytics;
import br.gov.sp.educacao.sed.mobile.util.DateUtils;
import br.gov.sp.educacao.sed.mobile.util.CriarAcessoBanco;

public class TurmasListaActivity
        extends AppCompatActivity
         implements TurmasListaViewMvc.Listener{

    private ArrayList<TurmaGrupo> turmaArrayList;

    private int ano;

    private Banco banco;

    private Intent intent;

    private String usuario;

    private Bimestre bimestre;

    private TurmaDBgetters turmaDBgetters;

    private CriarAcessoBanco criarAcessoBanco;

    private TurmasListaViewMvcImpl turmasViewMvcImpl;

    private boolean telaConsultaFrequencia, selecaoFrequencia, fechamento;

    private final String TELA_FREQUENCIA_CONSULTA = "br.gov.sp.educacao.sed.mobile.Frequencia.FrequenciaConsultaActivity";

    private final String TELA_FREQUENCIA_LANCAMENTO = "br.gov.sp.educacao.sed.mobile.Frequencia.FrequenciaLancamentoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        inicializarGoogleAnalytics();

        criarAcessoBanco = new CriarAcessoBanco();

        banco = criarAcessoBanco.gerarBanco(getApplicationContext());

        turmaDBgetters = new TurmaDBgetters(banco);

        intent = getIntent().getExtras().getParcelable("Menu");

        fechamento = getIntent().getExtras().getBoolean("Fechamento", false);

        ano = DateUtils.getCurrentYear(new Date());

        turmasViewMvcImpl = new TurmasListaViewMvcImpl(LayoutInflater.from(this), null, listarTurmaOrg());

        verificarTelaDestino();

        usuario = turmaDBgetters.getNomeUsuario();

        turmasViewMvcImpl.exibirNomeUsuario(usuario);

        setContentView(turmasViewMvcImpl.getRootView());
    }

    @Override
    protected void onStop() {

        super.onStop();

        turmasViewMvcImpl.unregisterListener();
    }

    @Override
    protected void onStart() {

        super.onStart();

        turmasViewMvcImpl.registerListener(this);
    }

    @Override
    protected void onResume() {

        super.onResume();

        if(turmasViewMvcImpl != null) {

            turmasViewMvcImpl.ativarBotao();
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if(turmaArrayList != null) {

            turmaArrayList.clear();
        }

        turmasViewMvcImpl.limparLista();
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    private void verificarTelaDestino() {

        if(intent.getComponent().getClassName().equals(TELA_FREQUENCIA_LANCAMENTO)) {

            selecaoFrequencia = true;

            criarTabParaTelaFrequencia();
        }
    }

    private void configurarTelaDestino() {

        if(selecaoFrequencia) {

            if(!telaConsultaFrequencia) {

                intent.setClassName(this, TELA_FREQUENCIA_LANCAMENTO);
            }
            else {

                intent.setClassName(this, TELA_FREQUENCIA_CONSULTA);
            }
        }
        else {

            intent.setClassName(this, intent.getComponent().getClassName());
        }
    }

    private void criarTabParaTelaFrequencia() {

        turmasViewMvcImpl.criarTabLancamentoConsulta();
    }

    private void inicializarGoogleAnalytics() {

        Analytics.setTela(this, this.getClass().toString());
    }

    private List<TurmaGrupo> listarTurmaOrg() {

        List<TurmaGrupo> turmaGruposList = turmaDBgetters.getTurmas(ano, fechamento);

        if(fechamento){

            for (TurmaGrupo turmaGrupo : turmaGruposList) {

                if(turmaGrupo.getDisciplina().getCodigoDisciplina() == 7245){

                    turmaGruposList.remove(turmaGrupo);

                    break;
                }
            }
        }

        return turmaGruposList;
    }

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);

        this.intent = intent.getExtras().getParcelable("Menu");
    }

    private void navegarParaModulo(TurmaGrupo turmaGrupo) {

        configurarTelaDestino();

        Bundle bundle = new Bundle();

        bundle.putParcelable("turma_grupo", turmaGrupo);

        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public void onTurmaSelecionada(TurmaGrupo turmaGrupo) {

        usuarioSelecionouTurma(turmaGrupo);
    }

    private void usuarioSelecionouTurma(TurmaGrupo turmaGrupo) {

        bimestre = turmaDBgetters.getBimestre(turmaGrupo.getTurmasFrequencia().getId());

        if(bimestre.getNumero() == 0) {

            turmasViewMvcImpl.avisoUsuarioCalendarioNaoHomologado();
        }
        else {

            navegarParaModulo(turmaGrupo);
        }
    }

    @Override
    public void configurarEscolhaTelaFrequencia(boolean telaConsulta) {

        this.telaConsultaFrequencia = telaConsulta;
    }
}
