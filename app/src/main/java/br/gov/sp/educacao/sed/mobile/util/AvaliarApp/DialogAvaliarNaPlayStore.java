package br.gov.sp.educacao.sed.mobile.util.AvaliarApp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.gov.sp.educacao.sed.mobile.R;

public class DialogAvaliarNaPlayStore
        extends DialogAvaliarApp {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState ) {

        View view = inflater.inflate(R.layout.fragment_avaliar_playstore, container);

        View botaoNaoObrigado = view.findViewById(R.id.naoAvaliarPlayStore);

        botaoNaoObrigado.setOnClickListener( this );

        View botaoConfirmar = view.findViewById(R.id.avaliarPlayStore);

        botaoConfirmar.setOnClickListener( this );

        return view;
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.avaliarPlayStore) {

            String packageName = getActivity().getPackageName();

            Intent intent;

            try {

                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));

                startActivity(intent);
            }
            catch(android.content.ActivityNotFoundException anfe) {

                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));

                startActivity(intent);
            }
        }
        dismiss();
    }
}