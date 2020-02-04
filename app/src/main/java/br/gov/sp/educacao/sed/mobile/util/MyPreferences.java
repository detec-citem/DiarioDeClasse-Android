package br.gov.sp.educacao.sed.mobile.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MyPreferences {
    //Constantes
    private final String PREF_NAME = "preferenciaDiarioClasse";
    private final String KEY_ATUALIZAR = "precisaAtualizar";
    private final String KEY_LOGIN_TESTE = "loginTeste";
    private final String KEY_VERSAO_APP = "versaoApp";
    public static final String KEY_TUTORIAL = "tutorial";

    //Variáveis
    private SharedPreferences preferences;
    private Editor editor;
    private Context context;

    //Construtor
    public MyPreferences(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    //Métodos
    public void salvarFlagAtualizar() {
        editor.putBoolean(KEY_ATUALIZAR, true);
        editor.commit();
    }

    public boolean isLoginTeste() {
        return preferences.getBoolean(KEY_LOGIN_TESTE, false);
    }

    public void setLoginTeste() {
        editor.putBoolean(KEY_LOGIN_TESTE, true);
        editor.commit();
    }

    public boolean precisaAtualizar() {
        return preferences.getBoolean(KEY_ATUALIZAR, false);
    }

    public void atualizou() {
        editor.putBoolean(KEY_ATUALIZAR, false);
        editor.commit();
    }

    public void salvarVersaoApp(int versaoApp) {
        editor.putInt(KEY_VERSAO_APP, versaoApp);
        editor.commit();
    }

    public void putBoolean(String key, boolean bol){

        editor.putBoolean(key, bol);
        editor.commit();
    }

    public boolean getBoolean(String key){

        return preferences.getBoolean(key, false);
    }

    public int versaoAtual() {
        return preferences.getInt(KEY_VERSAO_APP, 0);
    }
}