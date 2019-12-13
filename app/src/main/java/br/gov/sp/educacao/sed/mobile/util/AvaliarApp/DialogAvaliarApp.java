package br.gov.sp.educacao.sed.mobile.util.AvaliarApp;

import android.os.Bundle;

import android.app.Dialog;

import android.widget.Button;
import android.widget.RatingBar;

import android.view.View;
import android.view.Window;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import br.gov.sp.educacao.sed.mobile.R;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.DialogFragment;

public class DialogAvaliarApp
        extends DialogFragment
         implements RatingBar.OnRatingBarChangeListener,
                                       View.OnClickListener {

    public static final String KEY = "fragment_rate";

    private Button botaoNaoAvaliar;

    private Button botaoAvaliarDepois;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog( savedInstanceState );

        dialog.getWindow().requestFeature( Window.FEATURE_NO_TITLE );

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_avaliar_app, container);

        RatingBar notaEstrelas = view.findViewById(R.id.rb_stars);

        notaEstrelas.setOnRatingBarChangeListener(this);

        botaoNaoAvaliar = view.findViewById(R.id.naoAvaliar);

        botaoNaoAvaliar.setOnClickListener(this);

        botaoAvaliarDepois = view.findViewById(R.id.avaliarDepois);

        botaoAvaliarDepois.setOnClickListener( this );

        return view;
    }

    @Override
    public void onRatingChanged(RatingBar notasEstrelas, float nota, boolean fromUser) {

        if(nota >= 4) {

            DialogAvaliarManager.showRateDialogPlayStore(getActivity());

            GerenciadorAvaliarApp.naoPerguntarNovamente(getActivity());

            dismiss();
        }
        else if(nota > 0) {

            DialogAvaliarManager.showRateDialogFeedback(getActivity(), nota);

            GerenciadorAvaliarApp.atualizarTempo(getActivity());

            GerenciadorAvaliarApp.atualizarVezesInicioApp(getActivity());

            dismiss();
        }
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.avaliarDepois ) {

            GerenciadorAvaliarApp.atualizarTempo( getActivity() );

            GerenciadorAvaliarApp.atualizarVezesInicioApp( getActivity() );
        }
        else {

            GerenciadorAvaliarApp.naoPerguntarNovamente( getActivity() );
        }
        dismiss();
    }
}