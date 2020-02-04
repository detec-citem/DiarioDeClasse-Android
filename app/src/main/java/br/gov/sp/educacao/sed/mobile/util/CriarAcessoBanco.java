package br.gov.sp.educacao.sed.mobile.util;

import android.content.Context;

public class CriarAcessoBanco {
    //Variáveis
    private static Banco banco;

    //Singleton
    public static Banco gerarBanco(Context context) {
        if(banco == null) {
            banco = new Banco(new BdSED(context));
        }
        return banco;
    }
}