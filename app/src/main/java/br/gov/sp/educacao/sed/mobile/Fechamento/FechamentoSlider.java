package br.gov.sp.educacao.sed.mobile.Fechamento;

import android.os.Bundle;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.support.v4.app.Fragment;

import android.support.annotation.Nullable;

import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;
import br.gov.sp.educacao.sed.mobile.Turmas.Turma;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;

import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.Utils;
import br.gov.sp.educacao.sed.mobile.util.Analytics;
import br.gov.sp.educacao.sed.mobile.util.CriarAcessoBanco;

public class FechamentoSlider
        extends Fragment
         implements FechamentoSliderViewMvc.Listener {

    private Aluno aluno;

    private Turma turma;

    private Banco banco;

    private int position;

    private int bimestre;

    private int idDisciplina;

    private MediaAluno media;

    private int codigoDisciplina;

    private TurmaGrupo turmaGrupo;

    private int tipoFechamentoAtual;

    private ArrayList<Aluno> alunos;

    private FechamentoAluno mFechamento;

    private boolean fechamentoLancamento;

    public FechamentoPager fechamentoPager;

    private CriarAcessoBanco criarAcessoBanco;

    private FechamentoDBcrud fechamentoDBcrud;

    private FechamentoDBgetters fechamentoDBgetters;

    private FechamentoSliderViewMvcImpl fechamentoSliderViewMvcImpl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fechamentoSliderViewMvcImpl = new FechamentoSliderViewMvcImpl(LayoutInflater.from(getActivity()), null);

        inicializarGoogleAnalytics();

        Bundle bundle = getArguments();

        turmaGrupo = bundle.getParcelable(TurmaGrupo.BUNDLE_TURMA_GRUPO);

        alunos = bundle.getParcelableArrayList("listaAlunos");

        position = bundle.getInt("posicao");

        codigoDisciplina = bundle.getInt("codigoDisciplina");

        idDisciplina = bundle.getInt("idDisciplina");

        tipoFechamentoAtual = bundle.getInt("tipoFechamentoAtual");

        fechamentoLancamento = bundle.getBoolean("Lancamento");

        turma = turmaGrupo.getTurma();

        criarAcessoBanco = new CriarAcessoBanco();

        banco = criarAcessoBanco.gerarBanco(fechamentoSliderViewMvcImpl.getContext());

        fechamentoDBcrud = new FechamentoDBcrud(banco);

        fechamentoDBgetters = new FechamentoDBgetters(banco);

        bimestre = fechamentoDBgetters.getBimestreAtual(turmaGrupo.getTurmasFrequencia().getId());

        aluno = fechamentoDBgetters.getTotalFaltas(alunos.get(position), idDisciplina);

        mFechamento = fechamentoDBgetters.getFechamentoAluno(

                turma.getCodigoTurma(), aluno, codigoDisciplina, tipoFechamentoAtual
        );

        media = fechamentoDBgetters.getMediaAluno(

                turma.getCodigoTurma(), aluno.getCodigoMatricula(), codigoDisciplina
        );

        fechamentoSliderViewMvcImpl.exibirNomeAlunoChamada(aluno);

        configurarExibicaoFechamento();

        return fechamentoSliderViewMvcImpl.getRootView();
    }

    @Override
    public void onStop() {

        super.onStop();

        fechamentoSliderViewMvcImpl.unregisterListener();
    }

    @Override
    public void onStart() {

        super.onStart();

        fechamentoSliderViewMvcImpl.registerListener(this);
    }

    @Override
    public void usuarioClicouAlunoInativo() {

        fechamentoPager.proximoAluno();
    }

    private void inicializarGoogleAnalytics() {

        Analytics.setTela(fechamentoSliderViewMvcImpl.getContext(), fechamentoSliderViewMvcImpl.getContext().getClass().toString());
    }

    private void configurarExibicaoFechamento() {

        if(aluno.getAlunoAtivo()) {

            if(mFechamento.getCodigoFechamento() != 0) {

                configurarFechamentoJaSincronizado();
            }
            else if(mFechamento.getCodigoFechamento() == 0) {

                configurarFechamentoNaoSincronizado();
            }
        }
        else {

            fechamentoSliderViewMvcImpl.configurarAlunoInativo();
        }
    }

    private void configurarFechamentoJaSincronizado() {

        fechamentoSliderViewMvcImpl.setarFaltasAcumuladas(mFechamento.getFaltasAcumuladas());

        if(mFechamento.isConfirmado()) {

            fechamentoSliderViewMvcImpl.exibirFechamentoConfirmado(true);
        }
        else {

            fechamentoSliderViewMvcImpl.exibirFechamentoConfirmado(false);
        }

        fechamentoSliderViewMvcImpl.setarValorMedia(mFechamento.getNota());

        fechamentoSliderViewMvcImpl.exibirNumeroFaltas(mFechamento.getFaltas());

        fechamentoSliderViewMvcImpl.exibirAusenciasCompensadas(mFechamento.getAusenciasCompensadas());

        fechamentoSliderViewMvcImpl.exibirFaltasAcumuladas();
    }

    private void configurarFechamentoNaoSincronizado() {

        int faltasBimestre = 0;

        if(tipoFechamentoAtual - bimestre == 4) {

            faltasBimestre = aluno.getFaltasBimestre();
        }
        else {

            faltasBimestre = aluno.getFaltasBimestreAnterior();
        }

        fechamentoSliderViewMvcImpl.exibirNumeroFaltas(faltasBimestre);

        fechamentoSliderViewMvcImpl.exibirAusenciasCompensadas(mFechamento.getAusenciasCompensadas());

        fechamentoSliderViewMvcImpl.setarFaltasAcumuladas(aluno.getFaltasAnuais());

        fechamentoSliderViewMvcImpl.exibirFaltasAcumuladas();

        if(mFechamento.isConfirmado()) {

            fechamentoSliderViewMvcImpl.exibirFechamentoConfirmado(true);
        }
        else {

            fechamentoSliderViewMvcImpl.exibirFechamentoConfirmado(false);
        }

        if(media != null) {

            fechamentoSliderViewMvcImpl.setarValorMedia(media.getNotaMedia());
        }
        else {

            fechamentoSliderViewMvcImpl.setarValorMedia(11);
        }
    }

    @Override
    public void usuarioClicouConfirma(int nota, int faltas, int ausenciasCompensadas, int faltasAcumuladas) {

        final FechamentoAluno fechamento = new FechamentoAluno();

        if(aluno.getAlunoAtivo()) {

            if((nota - 1) == -1) {

                fechamento.setNota(Utils.ALUNO_ATIVO_SEM_NOTA);
            }
            else {

                fechamento.setNota(nota);
            }

            fechamento.setFaltas(faltas);

            fechamento.setAusenciasCompensadas(ausenciasCompensadas);

            fechamento.setFaltasAcumuladas(faltasAcumuladas);

            fechamento.setCodigoTipoFechamento(tipoFechamentoAtual);

            fechamento.setCodigoMatricula(aluno.getCodigoMatricula());

            fechamento.setCodigoTurma(turma.getCodigoTurma());

            fechamento.setCodigoDisciplina(codigoDisciplina);

            fechamento.setConfirmado(Utils.ALUNO_CONFIRMADO_PROFESSOR_FECHAMENTO);

            fechamentoSliderViewMvcImpl.exibirFechamentoConfirmado(true);
        }
        else {

            fechamento.setNota(Utils.ALUNO_INATIVO_SEM_NOTA);

            fechamento.setFaltas(0);

            fechamento.setAusenciasCompensadas(0);

            fechamento.setCodigoMatricula(aluno.getCodigoMatricula());

            fechamento.setCodigoTurma(turma.getCodigoTurma());

            fechamento.setCodigoDisciplina(codigoDisciplina);

            fechamento.setConfirmado(Utils.ALUNO_CONFIRMADO_PROFESSOR_FECHAMENTO);
        }

        fechamentoPager.proximoAluno();

        fechamentoDBcrud.insertFechamentoAluno(fechamento);
    }
}