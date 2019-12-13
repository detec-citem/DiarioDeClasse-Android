package br.gov.sp.educacao.sed.mobile.Avaliacao;

import android.os.Bundle;

import java.util.List;
import java.util.Date;
import java.util.ArrayList;

import android.app.Dialog;

import android.view.LayoutInflater;

import br.gov.sp.educacao.sed.mobile.R;

import android.support.annotation.NonNull;

import android.support.v4.app.DialogFragment;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import br.gov.sp.educacao.sed.mobile.util.DateUtils;

import br.gov.sp.educacao.sed.mobile.Turmas.Turma;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;

import br.gov.sp.educacao.sed.mobile.Escola.Aula;
import br.gov.sp.educacao.sed.mobile.Escola.Bimestre;
import br.gov.sp.educacao.sed.mobile.Escola.DiasLetivos;

import br.gov.sp.educacao.sed.mobile.Fechamento.FechamentoData;

import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.CriarAcessoBanco;
import br.gov.sp.educacao.sed.mobile.util.ListenerCalendario.CaldroidFragmentListener;

public class DialogNovaAvaliacao
        extends DialogFragment
         implements DialogNovaAvaliacaoViewMvc.Listener {

    private Banco banco;

    private boolean editar;

    private int disciplina;

    private int mTipoDialog;

    private Avaliacao mAvaliacao;

    private TurmaGrupo mTurmaGrupo;

    private Bimestre bimestreAtual;

    private Bimestre bimestreAnterior;

    private Bundle mSavedInstanceState;

    private FechamentoData fechamentoData;

    private List<Integer> mListaDiasSemana;

    private List<String> mListDiasMarcados;

    private AvaliacaoDBcrud avaliacaoDBcrud;

    @SuppressWarnings("FieldCanBeLocal")
    private CriarAcessoBanco criarAcessoBanco;

    private List<DiasLetivos> mListaDiasLetivos;

    public final int DIALOG_EDITAR_AVALIACAO = 1;

    private AvaliacaoDBgetters avaliacaoDBgetters;

    private AvaliacaoDBsetters avaliacaoDBsetters;

    public final String TAG = "DialogNovaAvaliacao";

    private OnAvaliacaoCadastradaListener mListener;

    private CaldroidFragment mDialogCaldroidFragment;

    private DialogNovaAvaliacaoViewMvcImpl dialogNovaAvaliacaoViewMvcImpl;

    @SuppressWarnings("FieldCanBeLocal")
    private final String CALDROID_DIALOG_FRAGMENT = "CALDROID_DIALOG_FRAGMENT";

    @SuppressWarnings("FieldCanBeLocal")
    private final String DIALOG_CALDROID_SAVED_STATE = "DIALOG_CALDROID_SAVED_STATE";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        this.mSavedInstanceState = savedInstanceState;

        checarTipoDialog();

        dialogNovaAvaliacaoViewMvcImpl = new DialogNovaAvaliacaoViewMvcImpl(LayoutInflater.from(getContext()), null, editar);

        mAvaliacao = getArguments().getParcelable(Avaliacao.BUNDLE_AVALIACAO);

        dadosAvaliacaoEditar();

        List<Aula> mListaAulas;

        List<Avaliacao> mListaAvaliacaoes;

        mTurmaGrupo = getArguments().getParcelable(TurmaGrupo.BUNDLE_TURMA_GRUPO);

        criarAcessoBanco = new CriarAcessoBanco();

        banco = criarAcessoBanco.gerarBanco(getActivity().getApplicationContext());

        avaliacaoDBcrud = new AvaliacaoDBcrud(banco);

        avaliacaoDBgetters = new AvaliacaoDBgetters(banco);

        avaliacaoDBsetters = new AvaliacaoDBsetters(banco);

        fechamentoData = avaliacaoDBgetters.getFechamentoAberto();

        disciplina = getArguments().getInt("Disciplina");

        Turma turma = mTurmaGrupo.getTurma();

        if(turma != null) {

            banco.get().beginTransaction();

            bimestreAtual = avaliacaoDBgetters.getBimestre(mTurmaGrupo.getTurmasFrequencia());

            bimestreAnterior = avaliacaoDBgetters.getBimestreAnterior(mTurmaGrupo.getTurmasFrequencia().getId());

            mListaAulas = avaliacaoDBgetters.getAula(mTurmaGrupo.getDisciplina());

            mListaDiasSemana = getDiasDaSemana(mListaAulas);

            mListaAvaliacaoes = avaliacaoDBgetters.getAvaliacoes(

                    turma.getId(), mTurmaGrupo.getDisciplina().getCodigoDisciplina(),
                    bimestreAtual.getNumero()
            );

            if(fechamentoData != null) {

                mListaAvaliacaoes.addAll(avaliacaoDBgetters.getAvaliacoes(

                        turma.getId(), mTurmaGrupo.getDisciplina().getCodigoDisciplina(),
                        bimestreAtual.getNumero() == 1 ? 4 : bimestreAtual.getNumero() - 1)
                );
            }
            mListDiasMarcados = new ArrayList<>();

            for(int i = 0; i < mListaAvaliacaoes.size(); i++) {

                mListDiasMarcados.add(mListaAvaliacaoes.get(i).getData());
            }

            mListaDiasLetivos = avaliacaoDBgetters.getDiasLetivos(bimestreAtual, mListaDiasSemana);
        }

        banco.get().setTransactionSuccessful();

        banco.get().endTransaction();

        return dialogNovaAvaliacaoViewMvcImpl.getRootView();
    }

    @Override
    public void onStop() {

        super.onStop();

        dialogNovaAvaliacaoViewMvcImpl.unregisterListener();
    }

    @Override
    public void onStart() {

        super.onStart();

        dialogNovaAvaliacaoViewMvcImpl.registerListener(this);
    }

    @Override
    public void ativarBotao() {

        mListener.ativarBotao();
    }

    private void checarTipoDialog() {

        if(mTipoDialog == DIALOG_EDITAR_AVALIACAO) {

            editar = true;
        }
    }

    private void dadosAvaliacaoEditar() {

        if(mAvaliacao != null && editar) {

            dialogNovaAvaliacaoViewMvcImpl.exibirDadosAvaliacao(

                    mAvaliacao.getNome(), mAvaliacao.getData(), mAvaliacao.isValeNota(), converterIndexTipoAtividade(mAvaliacao.getTipoAtividade()));
        }
    }

    @Override
    public void inicializarCalendario() {

        mDialogCaldroidFragment = new CaldroidFragment();

        exibirCalendario(mSavedInstanceState);

        mDialogCaldroidFragment.setCaldroidListener(getListenerCaldroid());
    }

    public CaldroidListener getListenerCaldroid() {

        CaldroidFragmentListener listener = new CaldroidFragmentListener(

                mDialogCaldroidFragment, bimestreAtual, bimestreAnterior, mListaDiasSemana, mListaDiasLetivos,
                mListDiasMarcados, mTurmaGrupo, fechamentoData, getActivity()
        ) {

            @Override
            public void onDateSelected(Date date) {

                String data = DateUtils.convertDateToString(date, DateUtils.FORMAT_DATE_DDMMYYYY);

                String bimestre = String.valueOf(avaliacaoDBsetters.setBimestreAvaliacao(data, bimestreAtual));

                dialogNovaAvaliacaoViewMvcImpl.usuarioSelecionouData(data, bimestre);
            }
        };
        return listener;
    }

    public interface OnAvaliacaoCadastradaListener {

        //Mudar Nome
        void ativarBotao();

        void adicionarAvaliacaoCadastrada();

        void avaliacaoEditadaRenovarLista();
    }

    public void escolherTipoDialog(int tipoDialog) {

        this.mTipoDialog = tipoDialog;
    }

    private List<Integer> getDiasDaSemana(List<Aula> aulas) {

        List<Integer> diasDaSemana = new ArrayList<>();

        for(Aula aula : aulas) {

            if(!diasDaSemana.contains(aula.getDiaSemana())) {

                diasDaSemana.add(aula.getDiaSemana());
            }
        }
        return diasDaSemana;
    }

    private int converterTipoAtividade(String tipoAtividade) {

        switch (tipoAtividade) {

            case Avaliacao.TIPO_AVALIACAO:
                return Avaliacao.CODIGO_TIPO_AVALIACAO;

            case Avaliacao.TIPO_ATIVIDADE:
                return Avaliacao.CODIGO_TIPO_ATIVIDADE;

            case Avaliacao.TIPO_TRABALHO:
                return Avaliacao.CODIGO_TIPO_TRABALHO;

            case Avaliacao.TIPO_OUTROS:
                return Avaliacao.CODIGO_TIPO_OUTROS;

            default:
                return 0;
        }
    }

    private void exibirCalendario(Bundle savedInstanceState) {

        if(savedInstanceState != null) {

            mDialogCaldroidFragment.restoreDialogStatesFromKey(

                    getFragmentManager(), savedInstanceState, DIALOG_CALDROID_SAVED_STATE, CALDROID_DIALOG_FRAGMENT
            );

            Bundle bundle = mDialogCaldroidFragment.getArguments();

            if(bundle == null){

                bundle = new Bundle();

                bundle.putInt(CaldroidFragment.THEME_RESOURCE, R.style.CalDroidTheme);

                mDialogCaldroidFragment.setArguments(bundle);
            }
        }
        else {

            Bundle bundle = new Bundle();

            bundle.putInt(CaldroidFragment.THEME_RESOURCE, R.style.CalDroidTheme);

            mDialogCaldroidFragment.setArguments(bundle);
        }

        mDialogCaldroidFragment.show(getFragmentManager(), CALDROID_DIALOG_FRAGMENT);
    }

    private int converterIndexTipoAtividade(int tipoAtividade) {

        switch (tipoAtividade) {

            case Avaliacao.CODIGO_TIPO_AVALIACAO:
                return 1;

            case Avaliacao.CODIGO_TIPO_ATIVIDADE:
                return 2;

            case Avaliacao.CODIGO_TIPO_TRABALHO:
                return 3;

            case Avaliacao.CODIGO_TIPO_OUTROS:
                return 4;

            default:
                return 0;
        }
    }

    public void setOnAvaliacaoCadastradaListener(OnAvaliacaoCadastradaListener listener) {

        this.mListener = listener;
    }

    @Override
    public void usuarioQuerEditarAvaliacao(String nomeAvaliacao, String dataAvaliacao, String tipoAtividade, boolean valeNota) {

        mAvaliacao.setNome(nomeAvaliacao);

        mAvaliacao.setTipoAtividade(converterTipoAtividade(tipoAtividade));

        mAvaliacao.setData(dataAvaliacao);

        mAvaliacao.setValeNota(valeNota);

        mAvaliacao.setDataServidor(null);

        avaliacaoDBcrud.editarAvaliacao(mAvaliacao, true);

        if(mListener != null) {

            mListener.avaliacaoEditadaRenovarLista();
        }

        dialogNovaAvaliacaoViewMvcImpl.removerDialog();
    }

    @Override
    public void usuarioQuerSalvarAvaliacao(String nomeAvaliacao, String dataAvaliacao, String tipoAtividade, boolean valeNota) {

        final Avaliacao avaliacao = new Avaliacao();

        avaliacao.setCodTurma(mTurmaGrupo.getTurma().getCodigoTurma());

        avaliacao.setCodDisciplina(

                disciplina != 0 ? disciplina : mTurmaGrupo.getDisciplina().getCodigoDisciplina()
        );

        avaliacao.setNome(nomeAvaliacao);

        avaliacao.setData(dataAvaliacao);

        avaliacao.setDataCadastro(DateUtils.convertDateToString(

                new Date(), DateUtils.FORMAT_DATE_DDMMYYYY_HH_MM)
        );

        banco.get().beginTransaction();

        avaliacao.setBimestre(avaliacaoDBsetters.setBimestreAvaliacao(dataAvaliacao, bimestreAtual));

        avaliacao.setTipoAtividade(converterTipoAtividade(tipoAtividade));

        avaliacao.setValeNota(valeNota);

        avaliacao.setTurmaId(mTurmaGrupo.getTurma().getId());

        avaliacao.setDisciplinaId(mTurmaGrupo.getDisciplina().getId());

        avaliacao.setCodigo(0);

        int idAvaliacaoBanco = avaliacaoDBgetters.getUltimoIdAvaliacao();

        avaliacao.setMobileId(idAvaliacaoBanco);

        avaliacao.setId(idAvaliacaoBanco);

        avaliacaoDBcrud.insertAvaliacao(avaliacao);

        banco.get().setTransactionSuccessful();

        banco.get().endTransaction();

        if(mListener != null) {

            mListener.adicionarAvaliacaoCadastrada();
        }

        dialogNovaAvaliacaoViewMvcImpl.removerDialog();
    }
}