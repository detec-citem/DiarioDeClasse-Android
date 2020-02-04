package br.gov.sp.educacao.sed.mobile.Frequencia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import java.util.Collections;
import java.util.Comparator;

import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;
import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.CriarAcessoBanco;

public class FrequenciaConsultaActivity
        extends AppCompatActivity
         implements FrequenciaConsultaViewMvc.Listener {

    private Banco banco;

    private Bundle bundle;

    private TurmaGrupo turmaGrupo;

    private CriarAcessoBanco criarAcessoBanco;

    private FrequenciaDBsetters frequenciaDBsetters;

    private FrequenciaConsultaViewMvcImpl frequenciaConsultaViewMvcImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        frequenciaConsultaViewMvcImpl = new FrequenciaConsultaViewMvcImpl(LayoutInflater.from(this), getSupportFragmentManager(), null);

        bundle = getIntent().getExtras();

        turmaGrupo = bundle.getParcelable(TurmaGrupo.BUNDLE_TURMA_GRUPO);



        banco = CriarAcessoBanco.gerarBanco(this);

        frequenciaDBsetters = new FrequenciaDBsetters(banco);

        frequenciaConsultaViewMvcImpl.exibirNomeTurmaTipoEnsino(turmaGrupo.getTurma().getNomeTurma(), turmaGrupo.getTurma().getNomeTipoEnsino());

        frequenciaConsultaViewMvcImpl.setarTurmaGrupo(turmaGrupo);

        setContentView(frequenciaConsultaViewMvcImpl.getRootView());
    }

    @Override
    protected void onStop() {

        super.onStop();

        frequenciaConsultaViewMvcImpl.unregisterListenerMenuNavegacao();

        frequenciaConsultaViewMvcImpl.unregisterListener();
    }

    @Override
    protected void onStart() {

        super.onStart();

        frequenciaConsultaViewMvcImpl.registerListenerMenuNavegacao();

        frequenciaConsultaViewMvcImpl.registerListener(this);

        inicializarListaAlunos();
    }

    @Override
    protected void onResume() {

        super.onResume();

        frequenciaConsultaViewMvcImpl.removerProgressBarVoador();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        banco = null;

        if(bundle != null) {

            bundle = null;
        }

        turmaGrupo = null;

        criarAcessoBanco = null;

        frequenciaDBsetters = null;

        frequenciaConsultaViewMvcImpl = null;
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    @Override
    public void inicializarListaAlunos() {

        ordenarAlunosPorNumeroChamada();

        frequenciaDBsetters.setFaltasPorAluno(turmaGrupo.getTurma().getAlunos(), turmaGrupo.getDisciplina().getId());

        frequenciaConsultaViewMvcImpl.inicializarListaAlunos(turmaGrupo.getTurma().getAlunos());
    }

    @Override
    public void navegarPara(Intent intent) {

        startActivity(intent);
    }

    private void ordenarAlunosPorNumeroChamada() {

        Collections.sort(turmaGrupo.getTurma().getAlunos(), new Comparator<Aluno>(){
            @Override
            public int compare(Aluno p1,
                               Aluno p2){

                return p1.getNumeroChamada() - p2.getNumeroChamada();
            }
        });
    }
}