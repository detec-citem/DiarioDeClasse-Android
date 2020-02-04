package br.gov.sp.educacao.sed.mobile.Frequencia;

import android.content.Context;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.TextView;

import br.gov.sp.educacao.sed.mobile.R;
import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;

class FrequenciaLancamentoItemViewMvcImpl
        implements FrequenciaLancamentoItemViewMvc {
    private int position;
    private Aluno aluno;

    private TextView tvAluno;
    private TextView tvAtivo;
    private TextView tvAluno2;
    private TextView tvAluno3;

    private Listener listener;

    private String CHAMADA;
    private String FALTA;
    private String PRESENCA;
    private String NAOSEAPLICA;

    private final View mRootView;

    private TurmaGrupo turmaGrupo;

    private String siglaFaltou;
    private String descTransferido;
    private String siglaCompareceu;
    private String siglaNaoSeAplica;

    private Button btFrequencia;
    private Button btNaoSeAplica;
    private Button btAplicarFalta;
    private Button btAplicarComparecimento;

    private Animation animation, animation2, animation3, animation4;

    FrequenciaLancamentoItemViewMvcImpl(LayoutInflater layoutInflater, ViewGroup parent) {

        mRootView = layoutInflater.inflate(R.layout.layout_frequencia_aluno, parent, false);

        inicializarViews();

        inicializarStrings();

        inicializarListeners();

        inicializarAnimacoes();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public View getRootView() {

        return mRootView;
    }

    private Context getContext() {

        return getRootView().getContext();
    }

    private void inicializarViews() {

        tvAluno = findViewById(R.id.tv_aluno);
        tvAluno2 = findViewById(R.id.tv_aluno2);
        tvAluno3 = findViewById(R.id.tv_aluno3);

        tvAtivo = findViewById(R.id.tv_ativo2);

        btFrequencia = findViewById(R.id.bt_frequencia);

        btNaoSeAplica = findViewById(R.id.bt_frequencia1);
        btAplicarComparecimento = findViewById(R.id.bt_frequencia2);
        btAplicarFalta = findViewById(R.id.bt_frequencia3);
    }

    @Override
    public void unregisterListener() {

        this.listener = null;
    }

    private void inicializarStrings() {

        FALTA = "FALTA";

        siglaFaltou = getContext().getResources().getString(R.string.sigla_falta);

        PRESENCA = "PRESENCA";

        siglaCompareceu = getContext().getResources().getString(R.string.sigla_compareceu);

        siglaNaoSeAplica = getContext().getResources().getString(R.string.sigla_nao_se_aplica);

        descTransferido = "Inativo";

        NAOSEAPLICA = "NAOSEAPLICA";
    }

    private void configurarItemComum() {

        tvAluno.setVisibility(View.VISIBLE);
        tvAluno2.setVisibility(View.VISIBLE);
        tvAluno3.setVisibility(View.VISIBLE);

        btNaoSeAplica.setText(siglaNaoSeAplica);
        btNaoSeAplica.setBackgroundResource(R.drawable.button_nao_aplica);

        btAplicarComparecimento.setText(siglaCompareceu);
        btAplicarComparecimento.setBackgroundResource(R.drawable.button_presenca);

        btAplicarFalta.setText(siglaFaltou);
        btAplicarFalta.setBackgroundResource(R.drawable.button_falta);

        exibirFaltasPercentual();

        String textAluno = aluno.getNumeroChamada() + " - " + aluno.getNomeAluno();

        tvAluno.setText(textAluno);
    }

    private void inicializarAnimacoes() {

        animation = AnimationUtils.loadAnimation(getContext(), R.anim.moveback);

        animation2 = AnimationUtils.loadAnimation(getContext(), R.anim.slide3);

        animation3 = AnimationUtils.loadAnimation(getContext(), R.anim.moveback3);

        animation4 = AnimationUtils.loadAnimation(getContext(), R.anim.moveback2);

        Interpolator interpolator = new LinearOutSlowInInterpolator();

        Interpolator interpolator2 = PathInterpolatorCompat.create(0.080f, 0.960f, 0.770f, 0.910f);

        animation.setInterpolator(interpolator);

        animation2.setInterpolator(interpolator2);

        animation3.setInterpolator(interpolator2);

        animation4.setInterpolator(interpolator2);

        animation3.setDuration(10);

        animation4.setDuration(10);

        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                btFrequencia.startAnimation(animation2);

                exibirStatusFrequenciaAtual();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animation2.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                new android.os.Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        listener.irParaProximoAlunoAtivo(position);
                    }
                }, 200);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void configurarItemBotoes() {

        String sigla = aluno.getComparecimento();

        if(aluno.getAlunoAtivo()) {

            switch (sigla) {

                case "Compareceu":
                case "C": {

                    btFrequencia.setText(siglaCompareceu);
                    aluno.setComparecimento(siglaCompareceu);
                    btFrequencia.setBackgroundResource(R.drawable.button_presenca);

                    break;
                }

                case "Faltou":
                case "F": {

                    btFrequencia.setText(siglaFaltou);
                    aluno.setComparecimento(siglaFaltou);
                    btFrequencia.setBackgroundResource(R.drawable.button_falta);

                    break;
                }

                case "Não se aplica":
                case "N": {

                    btFrequencia.setText(siglaNaoSeAplica);
                    aluno.setComparecimento(siglaNaoSeAplica);
                    btFrequencia.setBackgroundResource(R.drawable.button_nao_aplica);

                    break;
                }

                default: {

                    btFrequencia.setText("");
                    btFrequencia.setBackgroundColor(getContext().getResources().getColor(R.color.transparente));

                    break;
                }
            }
        }
    }

    private void inicializarListeners() {

        inicializarListenerBotaoNA();

        inicializarListenerBotaoCompareceu();

        inicializarListenerBotaoFalta();
    }

    private void exibirFaltasPercentual() {

        double faltasAnuais;
        double faltasBimestre;
        double percentualAnual;
        double percentualBimestre;

        faltasAnuais = aluno.getFaltasAnuais();

        faltasBimestre = aluno.getFaltasBimestre();

        //percentualAnual = ((int) faltasAnuais / turmaGrupo.getTurmasFrequencia().getAulasAno()) * 100;

        //percentualBimestre = ((int) faltasBimestre / turmaGrupo.getTurmasFrequencia().getAulasBimestre()) * 100;

        tvAluno3.setText("Faltas Ano: " + (int) faltasAnuais);// + " (" + String.format("%.1f", percentualAnual) + "%)");

        tvAluno2.setText("Faltas Bimestre: " + (int) faltasBimestre);// + " (" + String.format("%.1f", percentualBimestre) + "%)");
    }

    private void inicializarListenerBotaoNA() {

        btNaoSeAplica.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                listener.aplicarNA(aluno);

                exibirFaltasPercentual();

                CHAMADA = NAOSEAPLICA;

                btFrequencia.startAnimation(animation);
            }
        });
    }

    private void exibirStatusFrequenciaAtual() {

        switch (CHAMADA) {

            case "FALTA": {

                btFrequencia.setText(siglaFaltou);

                btFrequencia.setBackgroundResource(R.drawable.button_falta);

                break;
            }

            case "PRESENCA": {

                btFrequencia.setText(siglaCompareceu);

                btFrequencia.setBackgroundResource(R.drawable.button_presenca);

                break;
            }

            case "NAOSEAPLICA": {

                btFrequencia.setText(siglaNaoSeAplica);

                btFrequencia.setBackgroundResource(R.drawable.button_nao_aplica);

                break;
            }
        }
    }

    private void configurarItemParaAlunoAtivo() {

        btFrequencia.startAnimation(animation4);

        tvAtivo.setVisibility(View.GONE);

        btFrequencia.setVisibility(View.VISIBLE);
        btFrequencia.setBackgroundColor(getContext().getResources().getColor(R.color.transparente));

        btNaoSeAplica.setVisibility(View.VISIBLE);
        btAplicarFalta.setVisibility(View.VISIBLE);
        btAplicarComparecimento.setVisibility(View.VISIBLE);

        tvAluno.setTextColor(getContext().getResources().getColor(R.color.black));
    }

    private void inicializarListenerBotaoFalta() {

        btAplicarFalta.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                listener.aplicarFalta(aluno);

                exibirFaltasPercentual();

                CHAMADA = FALTA;

                btFrequencia.startAnimation(animation);
            }
        });
    }

    private void configurarItemParaAlunoInativo() {

        btFrequencia.startAnimation(animation3);

        btFrequencia.setVisibility(View.INVISIBLE);

        btNaoSeAplica.setVisibility(View.INVISIBLE);
        btAplicarComparecimento.setVisibility(View.INVISIBLE);
        btAplicarFalta.setVisibility(View.INVISIBLE);

        tvAluno2.setVisibility(View.INVISIBLE);
        tvAluno3.setVisibility(View.INVISIBLE);

        tvAtivo.setVisibility(View.VISIBLE);
        tvAtivo.setText(descTransferido);

        String textAluno = aluno.getNumeroChamada() + " - " + aluno.getNomeAluno();

        tvAluno.setText(textAluno);
    }

    @Override
    public void registerListener(Listener listener) {

        this.listener = listener;
    }

    private <T extends  View> T findViewById(int id) {

        return getRootView().findViewById(id);
    }

    private void inicializarListenerBotaoCompareceu() {

        btAplicarComparecimento.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                listener.aplicarPresenca(aluno);

                exibirFaltasPercentual();

                CHAMADA = PRESENCA;

                btFrequencia.startAnimation(animation);
            }
        });
    }

    @Override
    public void exibirInfoAluno(Aluno aluno, TurmaGrupo turmaGrupo) {

        this.aluno = aluno;

        this.turmaGrupo = turmaGrupo;

        if(aluno.getAlunoAtivo()) {

            configurarItemParaAlunoAtivo();

            configurarItemComum();

            configurarItemBotoes();
        }
        else {

            configurarItemParaAlunoInativo();
        }
    }
}
