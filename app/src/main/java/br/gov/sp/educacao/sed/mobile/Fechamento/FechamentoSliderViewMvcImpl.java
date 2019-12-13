package br.gov.sp.educacao.sed.mobile.Fechamento;

import android.os.Handler;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.widget.Button;
import android.widget.TextView;
import android.widget.NumberPicker;

import br.gov.sp.educacao.sed.mobile.R;

import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;

class FechamentoSliderViewMvcImpl
        implements FechamentoSliderViewMvc {

    private TextView tvNome;

    private Listener listener;

    private Button btConfirma;

    private final View mRootView;

    private int faltasAcumuladas;

    private TextView tvConfirmado;

    private TextView tvTransferido;

    private NumberPicker numberNota, numberFaltas, numberAusenciasCompensadas, editFaltasAcumuladas;

    FechamentoSliderViewMvcImpl(LayoutInflater layoutInflater, ViewGroup parent) {

        mRootView = layoutInflater.inflate(R.layout.fragment_detalhe_aluno_fechamento, parent, false);

        tvNome = findViewById(R.id.tv_aluno);

        editFaltasAcumuladas = findViewById(R.id.edit_fa);

        btConfirma = findViewById(R.id.btn_confirma_fechamento);

        btConfirma.setClickable(true);

        tvConfirmado = findViewById(R.id.tv_confirmado);

        tvTransferido = findViewById(R.id.tv_transferido);

        numberNota = findViewById(R.id.np_nota);

        numberFaltas = findViewById(R.id.np_faltas);

        numberAusenciasCompensadas = findViewById(R.id.np_ac);

        inicializarListeners();
    }

    @Override
    public View getRootView() {

        return mRootView;
    }

    public Context getContext() {

        return getRootView().getContext();
    }

    void configurarAlunoInativo() {

        numberNota.setEnabled(false);

        numberFaltas.setEnabled(false);

        numberAusenciasCompensadas.setEnabled(false);

        btConfirma.setVisibility(View.VISIBLE);

        tvConfirmado.setVisibility(View.INVISIBLE);

        tvTransferido.setVisibility(View.VISIBLE);
    }

    void exibirFaltasAcumuladas() {

        final int totalFaltasAcumuladas = this.faltasAcumuladas - numberAusenciasCompensadas.getValue();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                editFaltasAcumuladas.setMinValue(totalFaltasAcumuladas);
                editFaltasAcumuladas.setValue(totalFaltasAcumuladas);
                editFaltasAcumuladas.setMaxValue(totalFaltasAcumuladas);
            }
        }, 100);
    }

    void setarValorMedia(int media) {

        if(media == 11) {

            numberNota.setDisplayedValues(new String[]{"S/N"});
        }
        else {

            numberNota.setValue(media);

            numberNota.setMaxValue(media);

            numberNota.setMinValue(media);
        }
    }

    @Override
    public void unregisterListener() {

        this.listener = null;
    }

    private void inicializarListeners() {

        numberNota.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                return;
            }
        });

        numberFaltas.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                return;
            }
        });

        numberAusenciasCompensadas.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                return;
            }
        });

        editFaltasAcumuladas.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                return;
            }
        });

        numberAusenciasCompensadas.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(final NumberPicker picker, int valorAntigo, int valorNovo) {

                tvConfirmado.setVisibility(View.INVISIBLE);

                btConfirma.setVisibility(View.VISIBLE);

                tvTransferido.setVisibility(View.INVISIBLE);

                final int totalFaltasAcumuladas = faltasAcumuladas - valorNovo;

                editFaltasAcumuladas.setMinValue(totalFaltasAcumuladas);
                editFaltasAcumuladas.setValue(totalFaltasAcumuladas);
                editFaltasAcumuladas.setMaxValue(totalFaltasAcumuladas);
            }
        });

        btConfirma.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                btConfirma.setClickable(false);

                if(tvTransferido.getVisibility() == View.INVISIBLE) {

                    listener.usuarioClicouConfirma(

                            numberNota.getValue(), numberFaltas.getValue(),
                            numberAusenciasCompensadas.getValue(), editFaltasAcumuladas.getValue()
                    );
                }
                else if(tvTransferido.getVisibility() == View.VISIBLE){

                    listener.usuarioClicouAlunoInativo();
                }
            }
        });

        tvTransferido.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                listener.usuarioClicouAlunoInativo();
            }
        });
    }

    void exibirNomeAlunoChamada(Aluno aluno) {

        tvNome.setText(aluno.getNumeroChamada() + " - " + aluno.getNomeAluno());
    }

    void exibirNumeroFaltas(int faltasBimestre) {

        numberFaltas.setValue(faltasBimestre);

        numberFaltas.setMaxValue(faltasBimestre);

        numberFaltas.setMinValue(faltasBimestre);
    }

    void setarFaltasAcumuladas(int faltasAnuais) {

        this.faltasAcumuladas = faltasAnuais;
    }

    private <T extends View> T findViewById(int id) {

        return getRootView().findViewById(id);
    }

    @Override
    public void registerListener(Listener listener) {

        this.listener = listener;
    }

    void exibirFechamentoConfirmado(boolean confirmado) {

        tvTransferido.setVisibility(View.INVISIBLE);

        if(confirmado) {

            btConfirma.setVisibility(View.INVISIBLE);

            tvConfirmado.setVisibility(View.VISIBLE);
        }
        else {

            btConfirma.setVisibility(View.VISIBLE);

            tvConfirmado.setVisibility(View.INVISIBLE);
        }
    }

    void exibirAusenciasCompensadas(int ausenciasCompensadas) {

        numberAusenciasCompensadas.setMaxValue(numberFaltas.getValue());

        numberAusenciasCompensadas.setValue(ausenciasCompensadas);

        numberAusenciasCompensadas.setMinValue(0);
    }
}
