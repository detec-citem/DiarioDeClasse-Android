package br.gov.sp.educacao.sed.mobile.RegistroDeAula;

import android.app.Dialog;

import android.graphics.Color;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import android.support.v7.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;

import android.widget.TextView;

import br.gov.sp.educacao.sed.mobile.R;

public class DialogAnosIniciais
        extends DialogFragment {

    interface Listener {

        void selecionouMateria(int codigoMateria);
    }

    private AlertDialog mAlertDialog;

    private Listener listener;

    private TextView txtMatematica, txtPortugues, txtCiencias;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View view = inflater.inflate(R.layout.fragment_dialog_anos_iniciais, null, false);

        txtMatematica = view.findViewById(R.id.txtMatematica);
        txtPortugues = view.findViewById(R.id.txtPortugues);
        txtCiencias = view.findViewById(R.id.txtCiencias);

        return inicializaAlertDialog(view);
    }

    private AlertDialog inicializaAlertDialog(final View view) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(

                getActivity(), R.style.AppTheme_Dialog_Alert
        );

        builder.setTitle("Selecione a Disciplina").setView(view);

        mAlertDialog = builder.create();

        inicializarListenersOpcoesAnosIniciais();

        mAlertDialog.setCanceledOnTouchOutside(false);

        mAlertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);

        mAlertDialog.show();

        return mAlertDialog;
    }

    private void inicializarListenersOpcoesAnosIniciais() {

        txtMatematica.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                listener.selecionouMateria(2700);
            }
        });

        txtPortugues.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                listener.selecionouMateria(1100);
            }
        });

        txtCiencias.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                listener.selecionouMateria(7245);
            }
        });
    }

    public void registerListener(Listener listener) {

        this.listener = listener;
    }

    public void unregisterListener() {

        this.listener = null;
    }
}
