package br.gov.sp.educacao.sed.mobile.Avaliacao;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;

public class FragmentLancamentoAvaliacaoPager
        extends Fragment
         implements FragmentLAPViewMvc.Listener {

    private int numeroPaginas;

    private Avaliacao avaliacao;

    private TurmaGrupo turmaGrupo;

    private OnBotaoVoltarListener mListener;

    FragmentLAPViewMvcImpl fragmentLAPViewMvcImpl;

    private String TAG = FragmentLancamentoAvaliacaoPager.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentLAPViewMvcImpl = new FragmentLAPViewMvcImpl(LayoutInflater.from(getContext()), getFragmentManager(), null);

        setHasOptionsMenu(true);

        Bundle bundle;

        bundle = getArguments();

        turmaGrupo = bundle.getParcelable(TurmaGrupo.BUNDLE_TURMA_GRUPO);

        fragmentLAPViewMvcImpl.setarTurmaGrupo(turmaGrupo);

        avaliacao = bundle.getParcelable(Avaliacao.BUNDLE_AVALIACAO);

        numeroPaginas = turmaGrupo.getTurma().getAlunos().size();

        fragmentLAPViewMvcImpl.definirNumeroPaginas(numeroPaginas);

        inicializarPagerAdapter();

        exibirNomeAvaliacao();

        exibirNomeTurma();

        return fragmentLAPViewMvcImpl.getRootView();
    }

    @Override
    public void botaoVoltar() {

        mListener.usuarioClicouBotaoVoltar();
    }

    @Override
    public void navegarPara(Intent intent) {

        startActivity(intent);
    }

    public interface OnBotaoVoltarListener {

        void usuarioClicouBotaoVoltar();
    }

    public void registerOnBotaoVoltarListener(OnBotaoVoltarListener listener) {

        this.mListener = listener;
    }

    public void unregisterOnBotaoVoltarListener() {

        this.mListener = null;
    }

    @Override
    public void onStop() {

        super.onStop();

        fragmentLAPViewMvcImpl.unregisterListenerMenuNavegacao();

        fragmentLAPViewMvcImpl.unregisterListener();
    }

    @Override
    public void onStart() {

        super.onStart();

        fragmentLAPViewMvcImpl.registerListenerMenuNavegacao();

        fragmentLAPViewMvcImpl.registerListener(this);
    }

    @Override
    public void onResume() {

        super.onResume();

        fragmentLAPViewMvcImpl.removerProgressBarVoador();

        fragmentLAPViewMvcImpl.voltarVisibilidadeItem();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        if(mListener != null) {

            unregisterOnBotaoVoltarListener();
        }
    }

    public void proximoAluno() {

        fragmentLAPViewMvcImpl.usuarioClicouBotaoConfirmaNota();
    }

    public void exibirNomeTurma() {

        String nomeTurma = turmaGrupo.getTurma().getNomeTurma() + " / " + turmaGrupo.getDisciplina().getNomeDisciplina();

        fragmentLAPViewMvcImpl.exibirNomeTurma(nomeTurma);
    }

    public void exibirNomeAvaliacao() {

        String nomeAvaliacao = avaliacao.getNome() + " - " + avaliacao.getData();

        fragmentLAPViewMvcImpl.exibirNomeAvaliacao(nomeAvaliacao);
    }

    public void navegarParaListaAlunos() {

        Bundle bundle = new Bundle();

        bundle.putParcelable(Avaliacao.BUNDLE_AVALIACAO, avaliacao);

        bundle.putParcelable(TurmaGrupo.BUNDLE_TURMA_GRUPO, turmaGrupo);

        Intent intent = new Intent(getActivity(), ListaAlunosAvaliacoesActivity.class);

        intent.putExtras(bundle);

        startActivity(intent);
    }

    public void inicializarPagerAdapter() {

        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());

        fragmentLAPViewMvcImpl.createPagerAdapter(mPagerAdapter);
    }

    private class ScreenSlidePagerAdapter
            extends FragmentStatePagerAdapter {

        ScreenSlidePagerAdapter(FragmentManager fm) {

            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Bundle bundle = new Bundle();
            bundle.putInt("posicao", position);

            bundle.putParcelable(Avaliacao.BUNDLE_AVALIACAO, avaliacao);
            bundle.putParcelable("turma", turmaGrupo.getTurma());

            SliderFragmentAvaliacao sliderFragmentAvaliacao;

            sliderFragmentAvaliacao = new SliderFragmentAvaliacao();
            sliderFragmentAvaliacao.fragmentLancamentoAvaliacaoPager = FragmentLancamentoAvaliacaoPager.this;
            sliderFragmentAvaliacao.setArguments(bundle);

            return sliderFragmentAvaliacao;
        }

        @Override
        public int getCount() {

            return numeroPaginas;
        }
    }
}
