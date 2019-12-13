package br.gov.sp.educacao.sed.mobile.Frequencia;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.gov.sp.educacao.sed.mobile.R;

public class Calendario
        extends AppCompatActivity {

    private FragmentCalendario menuNavegacao;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        fragmentManager = this.getSupportFragmentManager();

        menuNavegacao = new FragmentCalendario();

        setContentView(R.layout.activity_calendario);
    }

    @Override
    protected void onResume() {

        super.onResume();

        setarTurmaGrupo();

        exibirCalendario();

    }


    void setarTurmaGrupo() {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.container, menuNavegacao, "FragmentCalendario");

        fragmentTransaction.commit();
    }

    private void exibirCalendario() {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.show(menuNavegacao);

        fragmentTransaction.commit();
    }

    public void mudarcor(View view) {

        view.setBackgroundColor(getResources().getColor(R.color.verde_dia_letivo));
    }
}
