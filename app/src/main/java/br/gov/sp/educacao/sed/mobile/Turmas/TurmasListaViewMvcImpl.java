package br.gov.sp.educacao.sed.mobile.Turmas;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.gov.sp.educacao.sed.mobile.R;

class TurmasListaViewMvcImpl
        implements TurmasListaViewMvc, TurmasListaAdapter.OnTurmaSelecionadaListener {

    //private TextView tvNome;

    private Toolbar toolbar;

    private ListView lvValor;

    private TextView tvTurma;

    private Listener listener;

    private final View mRootView;

    private TextView text1, text2;

    private LinearLayout layTurma;

    private List<TurmaGrupo> listaTurmas;

    private LayoutInflater layoutInflater;

    private ToolbarViewMvc toolbarViewMvc;

    private TurmasListaAdapter turmaAdapter;

    private ConstraintLayout constraintLayout;

    TurmasListaViewMvcImpl(LayoutInflater inflater, ViewGroup parent, List<TurmaGrupo> listaTurmas) {

        mRootView = inflater.inflate(R.layout.activity_lista_turmas_avaliacoes, parent, false);

        this.listaTurmas = listaTurmas;

        lvValor = findViewById(R.id.lv_valor);

        layTurma = findViewById(R.id.lay_turma);

        tvTurma = findViewById(R.id.tv_turma);

        text1 = findViewById(R.id.text1);

        text2 = findViewById(R.id.text2);

        //tvNome = findViewById(R.id.tv_user_name);

        toolbar = findViewById(R.id.toolbar);

        constraintLayout = findViewById(R.id.lay_cons);

        this.layoutInflater = inflater;

        toolbarViewMvc = getToolbarViewMvc(toolbar);

        inicializarToolbar();

        inicializarListaDeTurmas();

        exibirListaTurmas();

        inicializarListenersTextView();

        turmaAdapter = new TurmasListaAdapter(getContext(), this);

        turmaAdapter.addAll(listaTurmas);

        lvValor.setAdapter(turmaAdapter);
    }

    void limparLista() {

        turmaAdapter.limparLista();
    }

    @Override
    public View getRootView() {

        return mRootView;
    }

    public void ativarBotao() {

        turmaAdapter.ativarBotao();
    }

    private Context getContext() {

        return getRootView().getContext();
    }

    private void mudaCor(View view) {

        boolean consultaFrequencia;

        switch (view.getId()) {

            case R.id.text1:

                consultaFrequencia = false;

                text2.setTextColor(getContext().getResources().getColor(R.color.white));

                text1.setTextColor(getContext().getResources().getColor(R.color.amarelo_selecao_aba));

                listener.configurarEscolhaTelaFrequencia(consultaFrequencia);

                break;

            case R.id.text2:

                consultaFrequencia = true;

                text1.setTextColor(getContext().getResources().getColor(R.color.white));

                text2.setTextColor(getContext().getResources().getColor(R.color.amarelo_selecao_aba));

                listener.configurarEscolhaTelaFrequencia(consultaFrequencia);

                break;
        }
    }

    @Override
    public void exibirListaTurmas() {

        Collections.sort(listaTurmas, new Comparator<TurmaGrupo>() {

            @Override
            public int compare(TurmaGrupo p1, TurmaGrupo p2) {

                return p1.getTurma().getNomeEscola()
                        .compareToIgnoreCase(p2.getTurma().getNomeEscola());
            }
        });

        setarNomeEscolaDiretoria(listaTurmas);
    }

    @Override
    public void unregisterListener() {

        this.listener = null;
    }

    void criarTabLancamentoConsulta(){

        constraintLayout.setVisibility(View.VISIBLE);
    }

    private void inicializarToolbar() {

        toolbarViewMvc.setTitle("Turmas");

        toolbar.setNavigationIcon(R.drawable.icone_voltar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                listener.onBackPressed();
            }
        });
        toolbar.addView(toolbarViewMvc.getRootView());
    }

    @Override
    public void inicializarListaDeTurmas() {

        lvValor.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view,
                                             int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int primeiroItemVisivel,
                                 int visibleItemCount, int totalItemCount) {

                if (listaTurmas.size() > 0) {

                    if (listaTurmas.get(primeiroItemVisivel).getTurma().isHeader()) {

                        setarPrimeiroItem(primeiroItemVisivel);
                    }
                    else if (listaTurmas.get(primeiroItemVisivel).getTurma().isFooter()) {

                        setarPrimeiroItem(primeiroItemVisivel);
                    }
                    layTurma.setVisibility(View.VISIBLE);
                }
                else {

                    layTurma.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void exibirNomeUsuario(String nome) {

        //tvNome.setText(nome);
    }

    void avisoUsuarioCalendarioNaoHomologado() {

        final android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(getContext());

        alertDialog.setTitle("Calendário não homologado")
                .setMessage("É necessário que o calendário escolar esteja homologado." +
                        "\n\nVerifique com o GOE/AOE da escola para que você possa " +
                        "acessar esta turma no aplicativo Di@rio de Classe.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                        ativarBotao();
                    }
                })
                .create().show();
    }

    private void inicializarListenersTextView() {

        text1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                mudaCor(view);
            }
        });

        text2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                mudaCor(view);
            }
        });
    }

    private void setarPrimeiroItem(int posicao) {

        final Turma turma = listaTurmas.get(posicao).getTurma();

        String exibirTexto = turma.getNomeDiretoria() + " / " + turma.getNomeEscola();

        tvTurma.setText(exibirTexto);
    }

    @Override
    public void registerListener(Listener listener) {

        this.listener = listener;
    }

    private <T extends  View> T findViewById(int id) {

        return getRootView().findViewById(id);
    }

    @Override
    public void onTurmaSelecionada(TurmaGrupo turmaGrupo) {

        listener.onTurmaSelecionada(turmaGrupo);
    }

    @Override
    public ToolbarViewMvc getToolbarViewMvc(@Nullable ViewGroup parent) {

        return new ToolbarViewMvc(layoutInflater, parent);
    }

    private void setarNomeEscolaDiretoria(List<TurmaGrupo> listaTurmas) {

        String nome = "";

        for(int i = 0; i < listaTurmas.size(); i++) {

            final Turma turma = listaTurmas.get(i).getTurma();

            String diretoriaEscola = turma.getNomeDiretoria() + " / " + turma.getNomeEscola();

            listaTurmas.get(i).getTurma().setHeader(false);

            listaTurmas.get(i).getTurma().setFooter(false);

            if(!nome.equals(diretoriaEscola)) {

                listaTurmas.get(i).getTurma().setHeader(true);

                nome = diretoriaEscola;

                if(i > 0) {

                    listaTurmas.get(i - 1).getTurma().setFooter(true);
                }
            }
        }
    }
}
