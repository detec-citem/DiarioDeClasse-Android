package br.gov.sp.educacao.sed.mobile.Fechamento;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import br.gov.sp.educacao.sed.mobile.R;

public class FechamentoSliderActivity
        extends AppCompatActivity
         implements FechamentoSliderActivityMvc.Listener, FechamentoPager.OnBotaoVoltarListener {

    private FechamentoSliderActivityViewMvcImpl fechamentoSliderMvcImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        fechamentoSliderMvcImpl = new FechamentoSliderActivityViewMvcImpl(LayoutInflater.from(this), null);

        iniciarFechamento();

        setContentView(fechamentoSliderMvcImpl.getRootView());
    }

    @Override
    protected void onStop() {

        super.onStop();

        fechamentoSliderMvcImpl.unregisterListener();
    }

    @Override
    protected void onStart() {

        super.onStart();

        fechamentoSliderMvcImpl.registerListener(this);
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    private void iniciarFechamento() {

        Bundle bundle;

        bundle = getIntent().getExtras();

        Fragment fragmentFechamentoPager = new FechamentoPager();

        ((FechamentoPager) fragmentFechamentoPager).registerOnBotaoVoltarListener(this);

        fragmentFechamentoPager.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.pager1, fragmentFechamentoPager, "FragFechamentoPager");

        fragmentTransaction.addToBackStack("FragFechamentoPager");

        fragmentTransaction.commit();
    }

    @Override
    public void usuarioClicouBotaoVoltar() {

        onBackPressed();
    }
}
