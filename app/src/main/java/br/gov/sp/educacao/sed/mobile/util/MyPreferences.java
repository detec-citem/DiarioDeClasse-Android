package br.gov.sp.educacao.sed.mobile.util;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferences {

    private String PREF_NAME = "preferenciaDiarioClasse";

    private String KEY_ATUALIZAR = "precisaAtualizar";

    private String KEY_LOGIN_TESTE = "loginTeste";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context ctx;

    public MyPreferences(Context ctx) {

        this.ctx = ctx;

        preferences = ctx.getSharedPreferences(PREF_NAME, ctx.MODE_PRIVATE);

        this.editor = preferences.edit();
    }

    public void salvarFlagAtualizar() {

        this.editor.putBoolean(KEY_ATUALIZAR, true);

        editor.commit();
    }

    public boolean isLoginTeste(){

        return this.preferences.getBoolean(KEY_LOGIN_TESTE, false);
    }

    public void setLoginTeste(){

        this.editor.putBoolean(KEY_LOGIN_TESTE, true);

        this.editor.commit();
    }

    public boolean precisaAtualizar() {

        return this.preferences.getBoolean(KEY_ATUALIZAR, false);
    }

    public void atualizou() {

        this.editor.putBoolean(KEY_ATUALIZAR, false);

        editor.commit();
    }
}
