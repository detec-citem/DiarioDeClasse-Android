package br.gov.sp.educacao.sed.mobile.Fechamento;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import br.gov.sp.educacao.sed.mobile.R;

public class DialogEnviandoFechamento
        extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View view = inflater.inflate(R.layout.dialogenviandofechamento, null, false);

        return inicializaAlertDialog(view);
    }

    private AlertDialog inicializaAlertDialog(final View view) {

        AlertDialog mAlertDialog;

        final AlertDialog.Builder builder = new AlertDialog.Builder(

                getActivity(), R.style.ThemeOverlay_AppCompat_Dialog
        );

        builder.setView(view);

        mAlertDialog = builder.create();

        mAlertDialog.setCanceledOnTouchOutside(false);

        mAlertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);

        mAlertDialog.show();

        return mAlertDialog;
    }
}
