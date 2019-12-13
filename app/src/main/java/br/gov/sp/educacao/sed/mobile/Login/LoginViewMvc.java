package br.gov.sp.educacao.sed.mobile.Login;

import android.view.View;

interface LoginViewMvc {

    interface Listener {

        void resultadoLogin(UsuarioTO usuarioTO);

        void executaLogin(String login, String senha);
    }

    View getRootView();

    void efetuarLogin();

    void conferirConexao();

    void finalizaProgress();

    void usuarioAvisoFalha();

    void unregisterListener();

    void resultadoLogin(UsuarioTO usuarioTO);

    void registerListener(Listener listener);
}
