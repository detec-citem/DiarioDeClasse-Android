package br.gov.sp.educacao.sed.mobile.Fechamento;

import android.os.Bundle;

import java.util.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

import android.content.Intent;

import java.text.SimpleDateFormat;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.support.annotation.Nullable;

import br.gov.sp.educacao.sed.mobile.Login.LoginDBcrud;

import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.CriarAcessoBanco;

public class FechamentoPager
        extends Fragment
         implements IFechamentoTurmaListener, FechamentoPagerViewMvc.Listener {

    private Banco banco;

    private TurmaGrupo turmaGrupo;

    private boolean anosIniciais;

    private int numeroPaginas;
    private int alunosAtivosSize;
    private int mCountNaoEnviados;
    private int mCountConfirmados;
    private int tipoFechamentoAtual;

    private LoginDBcrud loginDBcrud;

    private ArrayList<Aluno> listaAlunos;

    private OnBotaoVoltarListener mListener;

    private CriarAcessoBanco criarAcessoBanco;

    private FechamentoDBcrud fechamentoDBcrud;

    public final int EMPTY = 100;
    public final int ERROR = 500;
    public final int NAO_DISPONIVEL = 503;
    public final int RETORNO_COM_SUCESSO = 200;

    private FechamentoDBgetters fechamentoDBgetters;

    private FechamentoPagerViewMvcImpl fechamentoPagerViewMvcImpl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        fechamentoPagerViewMvcImpl = new FechamentoPagerViewMvcImpl(LayoutInflater.from(getContext()), getFragmentManager(), null);

        Bundle bundle;

        bundle = getArguments();

        turmaGrupo = bundle.getParcelable(TurmaGrupo.BUNDLE_TURMA_GRUPO);

        tipoFechamentoAtual = bundle.getInt("tipoFechamentoAtual");

        alunosAtivosSize = bundle.getInt("alunosAtivosSize");

        fechamentoPagerViewMvcImpl.exibirNomeTurmaDisciplina(turmaGrupo.getTurma().getNomeTurma(), turmaGrupo.getDisciplina().getNomeDisciplina() );

        fechamentoPagerViewMvcImpl.setarTurmaGrupo(turmaGrupo);

        listaAlunos = new ArrayList<>();

        listaAlunos = turmaGrupo.getTurma().getAlunos();

        criarAcessoBanco = new CriarAcessoBanco();

        banco = criarAcessoBanco.gerarBanco(getActivity().getApplicationContext());

        fechamentoDBcrud = new FechamentoDBcrud(banco);

        fechamentoDBgetters = new FechamentoDBgetters(banco);

        loginDBcrud = new LoginDBcrud(banco);

        ordenarAlunosPorNumeroChamada();

        numeroPaginas = turmaGrupo.getTurma().getAlunos().size();

        fechamentoPagerViewMvcImpl.definirNumeroPaginas(numeroPaginas);

        inicializarPagerAdapter();

        anosIniciais = fechamentoDBgetters.isAnosIniciais(turmaGrupo.getTurma().getCodigoTurma());

        return fechamentoPagerViewMvcImpl.getRootView();
    }

    @Override
    public void onStop() {

        super.onStop();

        fechamentoPagerViewMvcImpl.unregisterListener();

        fechamentoPagerViewMvcImpl.unregisterListenerMenuNavegacao();
    }

    @Override
    public void onStart() {

        super.onStart();

        fechamentoPagerViewMvcImpl.registerListener(this);

        fechamentoPagerViewMvcImpl.registerListenerMenuNavegacao();

        fechamentoPagerViewMvcImpl.inicializarListenerAnimacaoAlunos();
    }

    @Override
    public void onResume() {

        super.onResume();

        fechamentoPagerViewMvcImpl.removerProgressBarVoador();

        fechamentoPagerViewMvcImpl.voltarVisibilidadeItem();
    }
    @Override
    public void onDestroy() {

        super.onDestroy();

        if(mListener != null) {

            unregisterOnBotaoVoltarListener();
        }
    }

    @Override
    public void botaoVoltar() {

        mListener.usuarioClicouBotaoVoltar();
    }

    public void proximoAluno() {

        fechamentoPagerViewMvcImpl.usuarioClicouBotaoConfirma();
    }

    @Override
    public void enviarFechamento() {

        Thread fechamentoTurmaThread = new Thread(new EnviarFechamentoTurma(

                getActivity(), FechamentoPager.this, turmaGrupo, anosIniciais, tipoFechamentoAtual,
                fechamentoDBcrud, fechamentoDBgetters, loginDBcrud)
        );

        fechamentoTurmaThread.start();
    }

    public void navegarParaListaAlunos() {

        Bundle bundle = new Bundle();

        bundle.putParcelableArrayList("listaAlunos", listaAlunos);

        bundle.putParcelable(TurmaGrupo.BUNDLE_TURMA_GRUPO, turmaGrupo);

        bundle.putInt("codigoTurma", turmaGrupo.getTurma().getCodigoTurma());
        bundle.putInt("codigoDisciplina", turmaGrupo.getDisciplina().getCodigoDisciplina());
        bundle.putInt("tipoFechamentoAtual", tipoFechamentoAtual);
        bundle.putInt("alunosAtivosSize", alunosAtivosSize);
        bundle.putInt("countAlunosNaoEnviados", mCountNaoEnviados);
        bundle.putInt("countConfirmados", mCountConfirmados);

        Intent intent = new Intent(getActivity(), FechamentoLista.class);

        intent.putExtras(bundle);

        startActivity(intent);
    }

    public void inicializarPagerAdapter() {

        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());

        fechamentoPagerViewMvcImpl.createPagerAdapter(mPagerAdapter);
    }

    @Override
    public void navegarPara(Intent intent) {

        startActivity(intent);
    }

    public interface OnBotaoVoltarListener {


        void usuarioClicouBotaoVoltar();

    }

    @Override
    public void usuarioQuerEnviarFechamento() {

        mCountNaoEnviados = fechamentoDBgetters.getTotalFechamentosAlunosNaoEnviados(

                turmaGrupo.getTurma().getCodigoTurma(), turmaGrupo.getDisciplina().getCodigoDisciplina(), tipoFechamentoAtual
        );

        mCountConfirmados = fechamentoDBgetters.getTotalFechamentoAlunoConfirmado(

                turmaGrupo.getTurma().getCodigoTurma(), turmaGrupo.getDisciplina().getCodigoDisciplina(), tipoFechamentoAtual
        );

        if(fechamentoDBgetters.verificarFaltasNaoSincronizadas(turmaGrupo.getTurma().getAlunos())) {

            fechamentoDBcrud.atualizarEstadoFechamentoAluno(turmaGrupo.getTurma().getAlunos());

            fechamentoPagerViewMvcImpl.avisoUsuarioNecessarioSincronizar();
        }

        else if((mCountNaoEnviados - (mCountNaoEnviados - alunosAtivosSize) == alunosAtivosSize)
                && mCountConfirmados == alunosAtivosSize) {

            fechamentoPagerViewMvcImpl.exibirOpcaoEnviarFechamento(turmaGrupo.getTurma().getNomeTurma());
        }
        else {

            fechamentoPagerViewMvcImpl.avisoUsuarioConfirmeNotasFaltasParaEnviar();
        }

        fechamentoPagerViewMvcImpl.ativarBotaoEnviar();
    }

    private void ordenarAlunosPorNumeroChamada() {

        Collections.sort(listaAlunos, new Comparator<Aluno>( ) {

            @Override
            public int compare(Aluno p1, Aluno p2) {

                return p1.getNumeroChamada() - p2.getNumeroChamada();
            }
        });
    }
    public void unregisterOnBotaoVoltarListener() {

        this.mListener = null;
    }

    @Override
    public void onFechamentoTurmaEnviado(int code) {

        fechamentoPagerViewMvcImpl.envioFinalizado();

        switch (code) {

            case RETORNO_COM_SUCESSO:

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

                fechamentoDBcrud.updateFechamentoTurmaServidor(

                        turmaGrupo.getTurma().getCodigoTurma(), turmaGrupo.getDisciplina().getCodigoDisciplina(),
                        tipoFechamentoAtual, dateFormat.format(new Date())
                );

                fechamentoPagerViewMvcImpl.avisoUsuarioFechamentosEnviados();

                break;

            case EMPTY:

                fechamentoPagerViewMvcImpl.avisoUsuarioNenhumFechamentoParaEnviar();

                break;

            case ERROR:

                fechamentoPagerViewMvcImpl.avisoUsuarioFechamentoNaoEnviado();

                break;

            case NAO_DISPONIVEL:

                fechamentoPagerViewMvcImpl.avisoUsuarioFechamentoNaoEnviado();

                break;

            default:

                fechamentoPagerViewMvcImpl.avisoUsuarioFechamentoNaoEnviado();

                break;
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {

            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Bundle bundle = new Bundle();

            FechamentoSlider fechamentoSlider;

            bundle.putParcelableArrayList("listaAlunos", listaAlunos);
            bundle.putParcelable(TurmaGrupo.BUNDLE_TURMA_GRUPO, turmaGrupo);

            bundle.putInt("codigoDisciplina", turmaGrupo.getDisciplina().getCodigoDisciplina());
            bundle.putInt("posicao", position);
            bundle.putInt("idDisciplina", turmaGrupo.getDisciplina().getId());
            bundle.putInt("tipoFechamentoAtual", tipoFechamentoAtual);
            bundle.putInt("alunosAtivosSize", alunosAtivosSize);

            fechamentoSlider = new FechamentoSlider();
            fechamentoSlider.fechamentoPager = FechamentoPager.this;
            fechamentoSlider.setArguments(bundle);

            return fechamentoSlider;
        }

        @Override
        public int getCount() {

            return numeroPaginas;
        }

    }

    public void registerOnBotaoVoltarListener(OnBotaoVoltarListener listener) {

        this.mListener = listener;
    }
}
