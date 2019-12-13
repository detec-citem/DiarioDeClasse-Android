package br.gov.sp.educacao.sed.mobile.util;

import android.content.Context;

public class CriarAcessoBanco {

    private static final String TAG = "CriarAcessoBanco";

    private static Banco banco;

    public Banco gerarBanco(Context context) {

        if(banco == null) {

            banco = new Banco(new BdSED(context));

            banco.open();
        }

        return banco;
    }
}
