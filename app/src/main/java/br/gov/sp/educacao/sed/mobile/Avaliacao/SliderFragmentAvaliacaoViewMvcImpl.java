package br.gov.sp.educacao.sed.mobile.Avaliacao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import br.gov.sp.educacao.sed.mobile.R;
import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;

class SliderFragmentAvaliacaoViewMvcImpl
        implements SliderFragmentAvaliacaoViewMvc {

    private TextView tvNome;

    private TextView tvStatus;

    private Listener listener;

    private Button btConfirma;

    private final View mRootView;

    private NumberPicker numberOfPlayersPicker;

    private NumberPicker numberOfPlayersPickerDezena;

    private NumberPicker numberOfPlayersPickerCentena;

    private String TAG = SliderFragmentAvaliacaoViewMvcImpl.class.getSimpleName();

    SliderFragmentAvaliacaoViewMvcImpl(LayoutInflater layoutInflater, ViewGroup parent) {

        mRootView = layoutInflater.inflate(R.layout.fragment_screen_slide_lancamento_avaliacao, parent, false);

        tvStatus = findViewById(R.id.tv_status);

        tvNome = findViewById(R.id.tvNome);

        btConfirma = findViewById(R.id.bt_confirma);

        numberOfPlayersPicker = findViewById(R.id.numberOfPlayersPicker);

        numberOfPlayersPickerDezena = findViewById(R.id.numberOfPlayersPickerDezena);

        numberOfPlayersPickerCentena = findViewById(R.id.numberOfPlayersPickerCentena);

        inicializarSelecaoNotas();

        inicializarSelecaoNotasDezena();

        inicializarSelecaoNotasCentena();

        inicializarBotaoConfirma();
    }

    @Override
    public View getRootView() {

        return mRootView;
    }

    private void exibirNota() {

        if(numberOfPlayersPicker.getValue() == 0) {

            tvStatus.setText("Sem nota");
        }
        else {

            String tvStatusText = String.format(getContext().getString(R.string.notaAvaliacao),
                    (numberOfPlayersPicker.getValue() - 1),
                    numberOfPlayersPickerDezena.getValue(),
                    numberOfPlayersPickerCentena.getValue());

            tvStatus.setText(tvStatusText);
        }
    }

    private Context getContext() {

        return getRootView().getContext();
    }

    @Override
    public void unregisterListener() {

        this.listener = null;
    }

    private void inicializarSelecaoNotas() {

        numberOfPlayersPicker.setMaxValue(11);
        numberOfPlayersPicker.setMinValue(0);

        numberOfPlayersPicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                return;
            }
        });

        numberOfPlayersPicker.setDisplayedValues(new String[]{"S/N", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"});

        numberOfPlayersPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker,
                                      int oldVal,
                                      int newVal) {

                if(newVal == 0
                        || newVal == 11) {

                    numberOfPlayersPickerDezena.setValue(0);

                    numberOfPlayersPickerCentena.setValue(0);
                }

                exibirNota();
            }
        });
    }

    private void inicializarBotaoConfirma() {

        btConfirma.setEnabled(true);

        btConfirma.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String nota = (numberOfPlayersPicker.getValue() - 1) + "." + numberOfPlayersPickerDezena.getValue() +
                        numberOfPlayersPickerCentena.getValue();

                listener.usuarioClicouConfirmaNota(nota);
            }
        });
    }

    @Override
    public void configurarParaAlunoNaoAtivo() {

        tvStatus.setVisibility(View.VISIBLE);
        tvStatus.setText("Inativo");
        tvStatus.setTextColor(getContext().getResources().getColor(R.color.amarelo_texto_dia_letivo));

        numberOfPlayersPicker.setEnabled(false);
        numberOfPlayersPickerDezena.setEnabled(false);
        numberOfPlayersPickerCentena.setEnabled(false);

        btConfirma.setEnabled(true);
    }

    private void inicializarSelecaoNotasDezena() {

        numberOfPlayersPickerDezena.setMaxValue(9);
        numberOfPlayersPickerDezena.setMinValue(0);

        numberOfPlayersPickerDezena.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                return;
            }
        });

        numberOfPlayersPickerDezena.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker,
                                      int oldVal,
                                      int newVal){

                if(numberOfPlayersPicker.getValue() == 0
                        || numberOfPlayersPicker.getValue() == 11) {

                    numberOfPlayersPickerDezena.setValue(0);
                }

                exibirNota();
            }
        });
    }

    private void inicializarSelecaoNotasCentena() {

        numberOfPlayersPickerCentena.setMaxValue(9);
        numberOfPlayersPickerCentena.setMinValue(0);

        numberOfPlayersPickerCentena.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                return;
            }
        });

        numberOfPlayersPickerCentena.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker,
                                      int oldVal,
                                      int newVal) {

                if(numberOfPlayersPicker.getValue() == 0
                        || numberOfPlayersPicker.getValue() == 11) {

                    numberOfPlayersPickerCentena.setValue(0);
                }

                exibirNota();
            }
        });
    }

    private <T extends View> T findViewById(int id) {

        return getRootView().findViewById(id);
    }

    @Override
    public void registerListener(Listener listener) {

        this.listener = listener;
    }

    @Override
    public void configurarParaAlunoAtivo(String nota) {

        numberOfPlayersPicker.setEnabled(true);
        numberOfPlayersPickerDezena.setEnabled(true);
        numberOfPlayersPickerCentena.setEnabled(true);

        btConfirma.setEnabled(true);

        if(nota != null && !nota.equals("12")) {

            if(nota.equals("11.00") || nota.equals("11")) {

                tvStatus.setText("Sem nota");
            }
            else {

                String formatedNota = nota.replace(".", ",");

                if(nota.length() == 3) {

                    String tvStatusText1 = "Nota: " + formatedNota + "0";

                    tvStatus.setText(tvStatusText1);
                }
                else {

                    String tvStatusText2 = "Nota: " + formatedNota;

                    tvStatus.setText(tvStatusText2);
                }
                numberOfPlayersPicker.setValue(Integer.parseInt(formatedNota.split(",")[0]) + 1);

                numberOfPlayersPickerDezena.setValue(Integer.parseInt(nota.replace(".", ",")
                        .split(",")[1]
                        .substring(0, 1)));
                try {

                    String nota2 = nota.replace(".", ",").split(",")[1].substring(1, 2);

                    numberOfPlayersPickerCentena.setValue(Integer.parseInt(nota2));
                }
                catch(StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {

                    CrashAnalytics.e(TAG, e);

                    numberOfPlayersPickerCentena.setValue(Integer.parseInt("0"));
                }
            }
        }
    }

    @Override
    public void exibirNomeChamadaAluno(String dadosAluno) {

        tvNome.setText(dadosAluno);
    }
}
