package br.gov.sp.educacao.sed.mobile.Turmas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.Calendar;

import br.gov.sp.educacao.sed.mobile.Frequencia.FrequenciaConsultaActivity;
import br.gov.sp.educacao.sed.mobile.Frequencia.FrequenciaLancamentoActivity;
import br.gov.sp.educacao.sed.mobile.RegistroDeAula.RegistroAulaActivity;
import br.gov.sp.educacao.sed.mobile.RegistroDeAula.RegistroAulaFundamentalActivity;
import br.gov.sp.educacao.sed.mobile.util.Analytics;
import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.CriarAcessoBanco;

public class TurmasListaActivity extends AppCompatActivity implements TurmasListaViewMvc.Listener {
    //Variáveis
    private boolean fechamento;
    private boolean telaConsultaFrequencia;
    private boolean selecaoFrequencia;
    private boolean selecaoRegistro;
    private String usuario;
    private ArrayList<TurmaGrupo> turmaGrupos;
    private Intent intent;
    private TurmasListaViewMvcImpl turmasViewMvcImpl;

    //Lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Analytics.setTela(this, this.getClass().toString());
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            intent = extras.getParcelable("Menu");
            fechamento = extras.getBoolean("Fechamento", false);
            Calendar calendar = Calendar.getInstance();
            int ano = calendar.get(Calendar.YEAR);

            Banco banco = CriarAcessoBanco.gerarBanco(getApplicationContext());
            TurmaDBgetters turmaDBgetters = new TurmaDBgetters(banco);
            usuario = turmaDBgetters.getNomeUsuario();
            turmaGrupos = turmaDBgetters.getTurmas(ano, fechamento);
            if(fechamento) {
                int i;
                int numeroTurmas = turmaGrupos.size();
                for (i = 0; i < numeroTurmas; i++) {
                    TurmaGrupo turmaGrupo = turmaGrupos.get(i);
                    if(turmaGrupo.getDisciplina().getCodigoDisciplina() == 7245){
                        turmaGrupos.remove(turmaGrupo);
                        break;
                    }
                }
            }
            String nomeTela = intent.getComponent().getClassName();
            if(nomeTela.equals(FrequenciaLancamentoActivity.class.getName())) {
                selecaoFrequencia = true;
            }
            else if (nomeTela.equals(RegistroAulaActivity.class.getName())) {
                selecaoRegistro = true;
            }
        }
        else {
            fechamento = savedInstanceState.getBoolean("Fechamento");
            selecaoFrequencia = savedInstanceState.getBoolean("selecaoFrequencia");
            selecaoRegistro = savedInstanceState.getBoolean("selecaoRegistro");
            telaConsultaFrequencia = savedInstanceState.getBoolean("telaConsultaFrequencia");
            intent = savedInstanceState.getParcelable("Menu");
            turmaGrupos = savedInstanceState.getParcelableArrayList("turmaGrupos");
        }
        turmasViewMvcImpl = new TurmasListaViewMvcImpl(LayoutInflater.from(this), null, turmaGrupos);
        turmasViewMvcImpl.exibirNomeUsuario(usuario);
        if (selecaoFrequencia) {
            turmasViewMvcImpl.criarTabLancamentoConsulta();
        }
        setContentView(turmasViewMvcImpl.getRootView());
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
    protected void onStop() {
        super.onStop();
        turmasViewMvcImpl.unregisterListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        turmasViewMvcImpl.limparLista();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("Fechamento", fechamento);
        outState.putBoolean("selecaoFrequencia", selecaoFrequencia);
        outState.putBoolean("selecaoRegistro", selecaoRegistro);
        outState.putBoolean("telaConsultaFrequencia", telaConsultaFrequencia);
        outState.putParcelable("Menu", intent);
        outState.putParcelableArrayList("turmaGrupos", turmaGrupos);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.intent = intent.getExtras().getParcelable("Menu");
    }

    //TurmasListaViewMvc.Listener
    @Override
    public void onTurmaSelecionada(TurmaGrupo turmaGrupo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(TurmaGrupo.BUNDLE_TURMA_GRUPO, turmaGrupo);
        intent.putExtras(bundle);
        if (selecaoFrequencia) {
            if (!telaConsultaFrequencia) {
                intent.setClassName(this, FrequenciaLancamentoActivity.class.getName());
            }
            else {
                intent.setClassName(this, FrequenciaConsultaActivity.class.getName());
            }
        }
        else if (selecaoRegistro) {
            if (turmaGrupo.getTurma().getCodigoTipoEnsino() == 14) {
                intent.setClassName(this, RegistroAulaFundamentalActivity.class.getName());
            }
            else {
                intent.setClassName(this, RegistroAulaActivity.class.getName());
            }
        }
        else {
            intent.setClassName(this, intent.getComponent().getClassName());
        }
        startActivity(intent);
    }

    @Override
    public void configurarEscolhaTelaFrequencia(boolean telaConsulta) {
        telaConsultaFrequencia = telaConsulta;
    }
}