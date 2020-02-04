package br.gov.sp.educacao.sed.mobile.Avaliacao;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import br.gov.sp.educacao.sed.mobile.Login.UsuarioTO;
import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;
import br.gov.sp.educacao.sed.mobile.Turmas.Turma;
import br.gov.sp.educacao.sed.mobile.util.Analytics;
import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.CriarAcessoBanco;

public class SliderFragmentAvaliacao
        extends Fragment
         implements SliderFragmentAvaliacaoViewMvcImpl.Listener {

    private Aluno aluno;

    private Banco banco;

    @SuppressWarnings("FieldCanBeLocal")
    private Turma turma;

    private UsuarioTO usuario;

    private Activity activity;

    private Avaliacao avaliacao;

    @SuppressWarnings("FieldCanBeLocal")
    private CriarAcessoBanco criarAcessoBanco;

    private AvaliacaoDBgetters avaliacaoDBgetters;

    private AvaliacaoDBsetters avaliacaoDBsetters;

    private String TAG = SliderFragmentAvaliacao.class.getSimpleName();

    public FragmentLancamentoAvaliacaoPager fragmentLancamentoAvaliacaoPager;

    private SliderFragmentAvaliacaoViewMvcImpl sliderFragmentAvaliacaoViewMvcImpl;

    public SliderFragmentAvaliacao(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        sliderFragmentAvaliacaoViewMvcImpl = new SliderFragmentAvaliacaoViewMvcImpl(

                LayoutInflater.from(getContext()), null
        );

        inicializarGoogleAnalytics();

        Bundle bundle = getArguments();

        int position = bundle.getInt("posicao");

        avaliacao = bundle.getParcelable(Avaliacao.BUNDLE_AVALIACAO);

        turma = bundle.getParcelable("turma");

        ordenarTurmaPorNumeroChamada(turma.getAlunos());

        aluno = turma.getAlunos().get(position);



        banco = CriarAcessoBanco.gerarBanco(activity.getApplicationContext());

        avaliacaoDBgetters = new AvaliacaoDBgetters(banco);

        avaliacaoDBsetters = new AvaliacaoDBsetters(banco);

        usuario = avaliacaoDBgetters.getUsuarioAtivo();

        exibirNomeChamadaAluno();

        configurarPorStatusAluno();

        return sliderFragmentAvaliacaoViewMvcImpl.getRootView();
    }

    @Override
    public void onStop() {

        super.onStop();

        sliderFragmentAvaliacaoViewMvcImpl.unregisterListener();
    }

    @Override
    public void onStart() {

        super.onStart();

        sliderFragmentAvaliacaoViewMvcImpl.registerListener(this);
    }

    private void salvarDadosAvaliacao() {

        NotasAluno notasAluno = new NotasAluno();

        notasAluno.setNota("11.00");
        notasAluno.setAluno_id(aluno.getId());
        notasAluno.setUsuario_id(usuario.getId());
        notasAluno.setAvaliacao_id(avaliacao.getId());
        notasAluno.setCodigoMatricula(aluno.getCodigoMatricula());
        notasAluno.setDataCadastro(new SimpleDateFormat("dd/MM/yyyy hh:mm").format(new Date()));

        avaliacaoDBsetters.setNotasAluno(notasAluno);
    }

    private String procurarNotaDoAluno() {

        NotasAluno notasAluno = new NotasAluno();

        notasAluno.setAluno_id(aluno.getId());

        notasAluno.setAvaliacao_id(avaliacao.getId());

        return avaliacaoDBgetters.getNotasAluno(notasAluno);
    }

    public void exibirNomeChamadaAluno() {

        String tvNomeText = aluno.getNumeroChamada() + " - " + aluno.getNomeAluno();

        sliderFragmentAvaliacaoViewMvcImpl.exibirNomeChamadaAluno(tvNomeText);
    }

    private void configurarPorStatusAluno() {

        if(aluno.getAlunoAtivo()) {

            String nota = procurarNotaDoAluno();

            configurarParaAlunoAtivo(nota);
        }
        else {

            configurarParaAlunoNaoAtivo();

            salvarDadosAvaliacao();
        }
    }

    private void inicializarGoogleAnalytics() {

        activity = getActivity();

        Analytics.setTela(activity, activity.getClass().toString());
    }

    private void configurarParaAlunoNaoAtivo() {

        sliderFragmentAvaliacaoViewMvcImpl.configurarParaAlunoNaoAtivo();
    }

    private void configurarParaAlunoAtivo(String nota) {

        sliderFragmentAvaliacaoViewMvcImpl.configurarParaAlunoAtivo(nota);
    }

    @Override
    public void usuarioClicouConfirmaNota(String nota) {

        if(aluno.getAlunoAtivo()) {

            NotasAluno notasAluno = new NotasAluno();

            notasAluno.setAluno_id(aluno.getId());
            notasAluno.setUsuario_id(usuario.getId());
            notasAluno.setAvaliacao_id(avaliacao.getId());
            notasAluno.setCodigoMatricula(aluno.getCodigoMatricula());
            notasAluno.setDataCadastro(new SimpleDateFormat("dd/MM/yyyy hh:mm").format(new Date()));

            banco.get().beginTransaction();

            if(avaliacao.getDataServidor() != null) {

                avaliacao.setDataServidor(null);

                avaliacaoDBsetters.setAvaliacaoDataServidorNull(avaliacao.getId());
            }

            if(nota.equals("-1.00")) {

                notasAluno.setNota("11.00");

                aluno.setNota("11.00");
            }
            else {

                notasAluno.setNota(nota);

                aluno.setNota(nota);
            }
            avaliacaoDBsetters.setNotasAluno(notasAluno);

            banco.get().setTransactionSuccessful();

            banco.get().endTransaction();
        }

        fragmentLancamentoAvaliacaoPager.proximoAluno();
    }

    private void ordenarTurmaPorNumeroChamada(ArrayList<Aluno> alunos) {

        Collections.sort(alunos, new Comparator<Aluno>() {

            @Override
            public int compare(Aluno aluno1, Aluno aluno2) {

                return  aluno1.compareTo(aluno2);
            }
        });
    }
}
