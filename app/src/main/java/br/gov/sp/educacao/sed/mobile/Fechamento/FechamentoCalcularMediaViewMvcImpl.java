package br.gov.sp.educacao.sed.mobile.Fechamento;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.gov.sp.educacao.sed.mobile.Avaliacao.Avaliacao;
import br.gov.sp.educacao.sed.mobile.R;
import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;

class FechamentoCalcularMediaViewMvcImpl
        implements FechamentoCalcularMediaViewMvc, MenuNavegacao.usuarioSelecionouMenuNavegacao {

    private Bundle bundle;

    private Intent intent;

    private Button btSalvar;

    private Toolbar toolbar;

    private Listener listener;

    private boolean menuAberto;

    private final View mRootView;

    private ScrollView scrollPesos;

    private FrameLayout frameLayout;

    private Spinner spinnerTipoMedia;

    private ListView listAlunosMedia;

    private MenuNavegacao menuNavegacao;

    private ProgressBar progressBarVoador;

    private LayoutInflater layoutInflater;

    private FragmentManager fragmentManager;

    private TextView tvTurma, tvDisciplina, menu;

    private ToolbarViewMvcImpl toolbarViewMvcImpl;

    private Animation apenasRemoverMenuNavegacao, salvarMediasRemoverMenuNavegacao, mudarTelaRemoverMenuNavegacao, animacaoProgressBarVoador;

    FechamentoCalcularMediaViewMvcImpl(LayoutInflater layoutInflater, FragmentManager fragmentManager, ViewGroup parent) {

        mRootView = layoutInflater.inflate(R.layout.activity_avaliacao_calcular_media, parent, false);

        this.layoutInflater = layoutInflater;

        this.fragmentManager = fragmentManager;

        toolbar = findViewById(R.id.toolbar);

        toolbarViewMvcImpl = getToolbarViewMvcImpl(toolbar);

        inicializarToolbar();

        menuNavegacao = new MenuNavegacao();

        bundle = new Bundle();

        menuAberto = false;

        frameLayout = findViewById(R.id.container);

        progressBarVoador = findViewById(R.id.progressBar);

        inicializarAnimacoes();

        intent = null;

        tvTurma = findViewById(R.id.tv_turma);

        tvDisciplina = findViewById(R.id.tv_disciplina);

        spinnerTipoMedia = findViewById(R.id.spinner_tipo_media);

        listAlunosMedia = findViewById(R.id.lista_aluno);

        scrollPesos = findViewById(R.id.scroll_pesos);

        btSalvar = findViewById(R.id.bt_salvar);

        inicializarListenerBotaoSalvar();

        inicializarListenerTipoMedia();
    }

    @Override
    public View getRootView() {

        return mRootView;
    }

    private Context getContext() {

        return getRootView().getContext();
    }

    private void checarTipoMedia() {

        if(btSalvar.getText().toString().toUpperCase().equals("SALVAR")) {

            exibirProgressBarVoador();

            new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                listener.usuarioSelecionouSalvar(spinnerTipoMedia.getSelectedItemId());
            }
        },450);


        }
        else if(btSalvar.getText().toString().toUpperCase().equals("CALCULAR")) {

            listener.usuarioSelecinouCalcular(spinnerTipoMedia.getSelectedItemId());
        }
    }

    void removerProgressBarVoador() {

        progressBarVoador.setVisibility(View.GONE);
    }

    void avisoUsuarioMediasSalvas() {

        Toast.makeText(getContext(),getContext().getResources().getString(R.string.medias_salvas), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void unregisterListener() {

        this.listener = null;
    }

    private void inicializarToolbar() {

        toolbarViewMvcImpl.setTitle("Alunos");

        inicializarMenuNavegacaoToolbar();

        toolbar.setNavigationIcon(R.drawable.icone_voltar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                listener.onBackPressed();
            }
        });
        toolbar.addView(toolbarViewMvcImpl.getRootView());
    }

    void avisoUsuarioSelecionePesos() {

        Toast.makeText(getContext(), getContext().getResources().getString(R.string.digite_todos_pesos), Toast.LENGTH_SHORT).show();
    }

    private void exibirMenuNavegacao() {

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.exibir_menu_navegacao);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.show(menuNavegacao);

        menuNavegacao.ativarCliques();

        fragmentTransaction.commit();

        menuAberto = true;

        frameLayout.setVisibility(View.VISIBLE);

        frameLayout.startAnimation(animation);
    }

    private void inicializarAnimacoes() {

        animacaoProgressBarVoador = AnimationUtils.loadAnimation(getContext(), R.anim.exibir_progress_voador);

        mudarTelaRemoverMenuNavegacao = AnimationUtils.loadAnimation(getContext(), R.anim.remover_menu_navegacao);

        apenasRemoverMenuNavegacao = AnimationUtils.loadAnimation(getContext(), R.anim.remover_menu_navegacao);

        salvarMediasRemoverMenuNavegacao = AnimationUtils.loadAnimation(getContext(), R.anim.remover_menu_navegacao);

        listenerAnimacaoSalvarMediasRemoverMenuNavegacao();
    }

    void exibirNomeDisciplina(String s) {

        tvDisciplina.setText(s);
    }

    private void esconderMenuNavegacao() {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        frameLayout.setVisibility(View.INVISIBLE);

        fragmentTransaction.hide(menuNavegacao);

        fragmentTransaction.commit();
    }

    void avisoUsuarioCalculeTodasMedias() {

        Toast.makeText(getContext(), getContext().getResources().getString(R.string.calcule_medias), Toast.LENGTH_SHORT).show();
    }

    void configuracao1(List<Aluno> alunos) {

        listAlunosMedia.setAdapter(null);

        listAlunosMedia.setVisibility(View.VISIBLE);

        scrollPesos.setVisibility(View.GONE);

        btSalvar.setText(R.string.salvar);

        MediaAvaliacaoAlunoAdapter adapter = new MediaAvaliacaoAlunoAdapter(getContext());

        adapter.addAll(alunos);

        listAlunosMedia.setAdapter(adapter);
    }

    void configuracao2(List<Aluno> alunos) {

        listAlunosMedia.setVisibility(View.VISIBLE);

        scrollPesos.setVisibility(View.GONE);

        btSalvar.setText(R.string.salvar);

        MediaAvaliacaoAlunoAdapter adapter = new MediaAvaliacaoAlunoAdapter(getContext());

        adapter.addAll(alunos);

        listAlunosMedia.setAdapter(adapter);
    }

    private void exibirProgressBarVoador() {

        progressBarVoador.setVisibility(View.VISIBLE);

        progressBarVoador.startAnimation(animacaoProgressBarVoador);
    }

    void avisoUsuarioArredondeTodasMedias() {

        Toast.makeText(getContext(), getContext().getResources().getString(R.string.notas_sem_arredondamento), Toast.LENGTH_SHORT).show();
    }

    public void registerListenerMenuNavegacao() {

        menuNavegacao.registerListenerMenuNavegacao(this);
    }

    private void inicializarListenerTipoMedia() {

        spinnerTipoMedia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                listener.usuarioSelecionouTipoMedia(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void setarTurmaGrupo(TurmaGrupo turmaGrupo) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(R.anim.move2, R.anim.slideback);

        int nivelTela = 1;

        bundle.putInt("NivelTela", nivelTela);

        bundle.putParcelable(TurmaGrupo.BUNDLE_TURMA_GRUPO, turmaGrupo);

        menuNavegacao.setArguments(bundle);

        fragmentTransaction.add(R.id.container, menuNavegacao, "MenuNavegacao");

        fragmentTransaction.commit();
    }

    @Override
    public void navegarPara(final Intent intent) {

        this.intent = intent;

        listenerAnimacaoMudarTelaRemoverMenuNavegacao(mudarTelaRemoverMenuNavegacao);

        removerMenuNavegacao(mudarTelaRemoverMenuNavegacao);
    }

    private void inicializarListenerBotaoSalvar() {

        btSalvar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(menuAberto) {

                    removerMenuNavegacao(salvarMediasRemoverMenuNavegacao);
                }
                else {

                    checarTipoMedia();
                }
            }
        });
    }

    public void unregisterListenerMenuNavegacao() {

        menuNavegacao.unregisterListener();
    }

    public void exibirNomeTurma(String nomeTurma) {

        tvTurma.setText(nomeTurma);
    }

    private void inicializarMenuNavegacaoToolbar() {

        menu = toolbarViewMvcImpl.getMenu();

        menu.setVisibility(View.VISIBLE);

        menu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(menuAberto) {

                    listenerAnimacaoApenasRemoverMenuNavegacao();

                    removerMenuNavegacao(apenasRemoverMenuNavegacao);
                }
                else {

                    exibirMenuNavegacao();
                }
            }
        });
    }

    void configuracao3(List<Avaliacao> avaliacoes) {

        btSalvar.setText(R.string.calcular);

        listAlunosMedia.setVisibility(View.GONE);

        scrollPesos.setVisibility(View.VISIBLE);

        List<EditText> editTexts = criaViewCalculoMediaPonderada(scrollPesos, avaliacoes);

        for(final EditText editText : editTexts) {

            editText.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s,
                                              int start,
                                              int count,
                                              int after) {
                }

                @Override
                public void onTextChanged(CharSequence s,
                                          int start,
                                          int before,
                                          int count) {
                }

                @Override
                public void afterTextChanged(Editable peso) {

                    listener.adicionarPesoAvaliacao(peso, editText.getTag());
                }
            });
        }
    }

    @Override
    public void registerListener(Listener listener) {

        this.listener = listener;
    }

    private <T extends View> T findViewById(int id) {

        return getRootView().findViewById(id);
    }

    private void removerMenuNavegacao(Animation animation) {

        menuAberto = false;

        menuNavegacao.desativarCliques();

        frameLayout.setVisibility(View.INVISIBLE);

        frameLayout.startAnimation(animation);
    }

    private void listenerAnimacaoApenasRemoverMenuNavegacao() {

        apenasRemoverMenuNavegacao.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                esconderMenuNavegacao();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void listenerAnimacaoSalvarMediasRemoverMenuNavegacao() {

        salvarMediasRemoverMenuNavegacao.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

                exibirProgressBarVoador();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                removerProgressBarVoador();

                checarTipoMedia();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public ToolbarViewMvcImpl getToolbarViewMvcImpl(@Nullable ViewGroup parent) {

        return new ToolbarViewMvcImpl(layoutInflater, parent);
    }

    private void listenerAnimacaoMudarTelaRemoverMenuNavegacao(Animation mudarTelaRemoverMenuNavegacao) {

        mudarTelaRemoverMenuNavegacao.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

                exibirProgressBarVoador();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                esconderMenuNavegacao();

                listener.navegarPara(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private List<EditText> criaViewCalculoMediaPonderada(ScrollView scrollPesos, List<Avaliacao> avaliacoes) {

        scrollPesos.removeAllViews();

        LinearLayout.LayoutParams paramsHorizontal = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams paramsVertical = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        LinearLayout linearVertical = new LinearLayout(getContext());

        linearVertical.setOrientation(LinearLayout.VERTICAL);

        linearVertical.setLayoutParams(paramsVertical);

        List<EditText> editTexts = new ArrayList<>();

        for(Avaliacao avaliacao : avaliacoes) {

            LinearLayout linearLayout = new LinearLayout(getContext());

            linearLayout.setOrientation(LinearLayout.HORIZONTAL);

            linearLayout.setLayoutParams(paramsHorizontal);

            TextView textView = new TextView(getContext());

            textView.setText(String.format(getContext().getResources().getString(R.string.label_nome_avaliacao), avaliacao.getNome()));

            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimensionPixelSize(R.dimen.texto_label_adapter));

            textView.setPadding(0, 10, 0, 0);

            linearLayout.addView(textView);

            EditText editText = new EditText(getContext());

            editText.setId(avaliacao.getId());
            editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimensionPixelSize(R.dimen.texto_label_adapter));
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setHint(getContext().getResources().getString(R.string.hint_digite_peso));
            editText.setTag(avaliacao.getId());

            linearLayout.addView(editText);

            editTexts.add(editText);

            linearVertical.addView(linearLayout);
        }

        scrollPesos.addView(linearVertical);

        return editTexts;
    }
}
