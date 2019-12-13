package br.gov.sp.educacao.sed.mobile.Avaliacao;

import android.app.AlertDialog;

import android.graphics.Typeface;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import android.content.Context;
import android.content.DialogInterface;

import br.gov.sp.educacao.sed.mobile.R;

import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;

class ListaAlunosAvaliacoesItemViewMvcImpl
        implements ListaAlunosAvaliacoesItemViewMvc {

    private ViewGroup p1;

    private View mRootView;

    private TextView tvAtivo;

    private Listener listener;

    private TextView tvMatriculaAluno;

    private AlertDialog dialogSelecaoNotas;

    private NumberPicker numberOfPlayersPicker;

    private NumberPicker numberOfPlayersPickerDezena;

    private NumberPicker numberOfPlayersPickerCentena;

    private TextView tvAluno, tvNota, tvNota2, tvNome2;

    ListaAlunosAvaliacoesItemViewMvcImpl(LayoutInflater inflater, ViewGroup parent) {

        mRootView = inflater.inflate(R.layout.layout_avaliacao_aluno, parent, false);

        p1 = parent;

        tvAluno = findViewById(R.id.tv_aluno);

        tvMatriculaAluno = findViewById(R.id.tv_matricula_aluno);

        tvAtivo = findViewById(R.id.tv_ativo);

        tvNota = findViewById(R.id.tv_nota);
    }

    @Override
    public View getRootView() {

        return mRootView;
    }

    private Context getContext() {

        return getRootView().getContext();
    }

    @Override
    public void unregisterListener() {

        this.listener = null;
    }

    private void exibirDadosAluno(Aluno aluno) {

        String textAluno = aluno.getNumeroChamada() + " - " + aluno.getNomeAluno();

        String nota = aluno.getNota();

        if (aluno.getAlunoAtivo()) {

            configurarDadosTelaAlunoAtivo(nota);
        }
        else {

            configurarDadosTelaAlunoInativo();
        }

        tvAluno.setText(textAluno);
    }

    private void configurarDadosTelaAlunoInativo() {

        tvNota.setVisibility(View.INVISIBLE);

        tvMatriculaAluno.setVisibility(View.INVISIBLE);

        tvAtivo.setVisibility(View.VISIBLE);

        tvAtivo.setText("Inativo");
    }

    private void inicializarSelecaoNotas(View view) {

        numberOfPlayersPicker = view.findViewById(R.id.numberOfPlayersPicker);

        numberOfPlayersPicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                return;
            }
        });

        numberOfPlayersPicker.setMaxValue(11);

        numberOfPlayersPicker.setMinValue(0);

        numberOfPlayersPicker.setDisplayedValues(new String[]{"S/N", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"});

        numberOfPlayersPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker,
                                      int oldVal,
                                      int newVal) {

                if (newVal == 11 | newVal == 0) {

                    numberOfPlayersPickerDezena.setValue(0);
                    numberOfPlayersPickerCentena.setValue(0);
                }
                if (newVal == 0) {

                    tvNota2.setText("Sem nota");
                    tvNota.setText("S/N");
                }
                else {

                    String tvNota2txt = "Nota " + String.valueOf(newVal - 1) + "," +
                            numberOfPlayersPickerDezena.getValue() + numberOfPlayersPickerCentena.getValue();

                    tvNota2.setText(tvNota2txt);
                }
            }
        });
    }

    @Override
    public void registerListener(Listener listener) {

        this.listener = listener;
    }

    private <T extends View> T findViewById(int id) {

        return getRootView().findViewById(id);
    }

    @Override
    public void exibirInfoAlunoAvaliacao(Aluno aluno) {

        exibirDadosAluno(aluno);

        configurarClickNota(aluno);
    }

    private void configurarClickNota(final Aluno aluno) {

        tvNota.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final LinearLayout.LayoutParams paramslayoutmain = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                paramslayoutmain.setMargins(20, 20, 20, 20);

                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View view = inflater.inflate(R.layout.alert_nota, p1, false);

                tvNota2 = view.findViewById(R.id.tv_nota);

                tvNome2 = view.findViewById(R.id.horario);

                String nota = aluno.getNota();

                inicializarDialogNota(aluno, view);

                inicializarSelecaoNotas(view);

                inicializarSelecaoNotasDezena(view);

                inicializarSelecaoNotasCentena(view);

                if (nota != null
                        && !nota.equals("12")) {

                    if (nota.equals("11") || nota.equals("11.00")) {

                        tvNota2.setText("Sem nota");
                    }
                    else {

                        configurarValoresSelecaoNota(nota);
                    }
                }
            }
        });
    }

    private void inicializarSelecaoNotasDezena(View view) {

        numberOfPlayersPickerDezena = view.findViewById(R.id.numberOfPlayersPickerDezena);

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
                                      int newVal) {

                if (numberOfPlayersPicker.getValue() == 0 | numberOfPlayersPicker.getValue() == 11) {

                    numberOfPlayersPickerDezena.setValue(0);
                    numberOfPlayersPickerCentena.setValue(0);
                }
                if (numberOfPlayersPicker.getValue() == 0) {

                    tvNota2.setText("Sem nota");
                    tvNota.setText("S/N");
                }
                else {

                    String tvNota2txt = "Nota " + (numberOfPlayersPicker.getValue() - 1) + "," +
                            numberOfPlayersPickerDezena.getValue() + numberOfPlayersPickerCentena.getValue();

                    tvNota2.setText(tvNota2txt);
                }
            }
        });
    }

    private void inicializarSelecaoNotasCentena(View view) {

        numberOfPlayersPickerCentena = view.findViewById(R.id.numberOfPlayersPickerCentena);

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

                if (numberOfPlayersPicker.getValue() == 0 | numberOfPlayersPicker.getValue() == 11) {

                    numberOfPlayersPickerDezena.setValue(0);
                    numberOfPlayersPickerCentena.setValue(0);
                }
                if (numberOfPlayersPicker.getValue() == 0) {

                    tvNota2.setText("Sem nota");
                    tvNota.setText("S/N");
                }
                else {

                    String tvNota2txt = "Nota " + (numberOfPlayersPicker.getValue() - 1) + "," +
                            numberOfPlayersPickerDezena.getValue() + numberOfPlayersPickerCentena.getValue();

                    tvNota2.setText(tvNota2txt);
                }
            }
        });
    }

    private void configurarValoresSelecaoNota(String nota) {

        tvNota2.setText("Nota " + nota);

        numberOfPlayersPicker.setValue(Integer.parseInt(nota.replace(".", ",")
                .split(",")[0]) + 1);

        numberOfPlayersPickerDezena.setValue(Integer.parseInt(nota.replace(".", ",")
                .split(",")[1].substring(0, 1)));

        if(nota.length() >= 3) {

            String nota2 = nota.replace(".", ",");
            String nota3 = nota2.split(",")[1];
            String nota4 = nota3.substring(1, 2);

            numberOfPlayersPickerCentena.setValue(Integer.parseInt(nota4));
        }
        else {

            numberOfPlayersPickerCentena.setValue(Integer.parseInt("0"));
        }
    }

    private void configurarDadosTelaAlunoAtivo(String nota) {

        tvAtivo.setVisibility(View.INVISIBLE);

        tvNota.setVisibility(View.VISIBLE);

        tvMatriculaAluno.setVisibility(View.VISIBLE);

        if (nota != null
                && !nota.equals("12")) {

            if (nota.equals("11.00") || nota.equals("11")) {

                tvNota.setText("S/N");
            }
            else {

                tvNota.setText(String.valueOf(nota));
            }
        }
        else {

            Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),"icomoon.ttf");

            tvNota.setTypeface(typeface);

            tvNota.setText(String.valueOf((char) 0xe600));
        }
    }

    private void inicializarDialogNota(final Aluno aluno, View view) {

        AlertDialog.Builder alertbuilder = new AlertDialog.Builder(getContext());

        alertbuilder.setTitle(getContext().getResources().getString(R.string.nota));

        alertbuilder.setCancelable(false);

        alertbuilder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                final int nota = numberOfPlayersPicker.getValue();

                if (nota == 0) {

                    aluno.setNota("11.00");

                    listener.alterarNota(aluno);

                    tvNota.setText("S/N");
                }
                else {

                    String tvNotaText = nota - 1 +
                            "." +
                            numberOfPlayersPickerDezena.getValue() +
                            numberOfPlayersPickerCentena.getValue();

                    tvNota.setText(tvNotaText);

                    aluno.setNota(tvNotaText);

                    listener.alterarNota(aluno);
                }
                dialogSelecaoNotas.dismiss();
            }
        });

        alertbuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogSelecaoNotas.dismiss();
            }
        });

        alertbuilder.setView(view);

        String dadosAluno = aluno.getNumeroChamada() + " - " + aluno.getNomeAluno();

        tvNome2.setText(dadosAluno);

        dialogSelecaoNotas = alertbuilder.create();

        dialogSelecaoNotas.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);

        dialogSelecaoNotas.show();
    }
}


