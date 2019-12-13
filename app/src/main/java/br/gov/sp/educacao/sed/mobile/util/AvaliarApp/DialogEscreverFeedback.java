package br.gov.sp.educacao.sed.mobile.util.AvaliarApp;

import android.net.Uri;

import android.os.Bundle;

import android.content.Intent;

import android.widget.Toast;
import android.widget.EditText;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.support.annotation.Nullable;

import br.gov.sp.educacao.sed.mobile.R;

import br.gov.sp.educacao.sed.mobile.util.Banco;

import br.gov.sp.educacao.sed.mobile.Login.UsuarioTO;
import br.gov.sp.educacao.sed.mobile.Login.LoginDBcrud;

import br.gov.sp.educacao.sed.mobile.util.CriarAcessoBanco;

public class DialogEscreverFeedback
        extends DialogAvaliarApp {

    private Banco banco;

    private float nota;

    private String usuario;

    private EditText etFeedback;

    private LoginDBcrud loginDBcrud;

    private CriarAcessoBanco criarAcessoBanco;

    private static final String RATING_KEY = "nota";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rate_dialog_feedback, container);

        etFeedback = view.findViewById(R.id.et_feedback);

        criarAcessoBanco = new CriarAcessoBanco();

        banco = criarAcessoBanco.gerarBanco(getActivity().getApplicationContext());

        loginDBcrud = new LoginDBcrud(banco);

        usuario = loginDBcrud.getLoginUsuario();

        View bt = view.findViewById(R.id.bt_no);

        bt.setOnClickListener(this);

        bt = view.findViewById(R.id.bt_send);

        bt.setOnClickListener(this);

        if (savedInstanceState != null) {

            nota = savedInstanceState.getFloat(RATING_KEY);
        }

        return view;
    }

    public void darNota(float nota) {

        this.nota = nota;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putFloat(RATING_KEY, nota);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {

        String feedback = etFeedback.getText().toString();

        if(view.getId() == R.id.bt_send && feedback.length() > 0) {

            Intent intent = new Intent(Intent.ACTION_SENDTO);

            intent.setDataAndType(Uri.parse("mailto:"), "text/plain");

            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"seesp.mobile@gmail.com"});

            intent.putExtra(Intent.EXTRA_SUBJECT, "Avaliação do aplicativo Di@rio de Classe");

            intent.putExtra(Intent.EXTRA_TEXT, "Estrelas: " + nota + "\n\n Avaliação: " + feedback + "\n\n Login: " + usuario);

            getActivity().startActivity(Intent.createChooser(intent, "Enviar e-mail"));
        }
        else if(view.getId() == R.id.bt_send) {

            Toast.makeText(getActivity(), "Entre com o feedback", Toast.LENGTH_SHORT).show();

            return;
        }
        dismiss();
    }
}
