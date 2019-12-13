package br.gov.sp.educacao.sed.mobile.Avaliacao;

import android.os.Bundle;

import android.view.LayoutInflater;

import android.support.v4.app.Fragment;

import br.gov.sp.educacao.sed.mobile.R;

import android.support.v7.app.AppCompatActivity;

public class AvaliacoesSliderActivity
        extends AppCompatActivity
         implements AvaliacoesSliderViewMvc.Listener, FragmentLancamentoAvaliacaoPager.OnBotaoVoltarListener {

    private AvaliacoesSliderViewMvcImpl avaliacoesSliderViewMvcImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        avaliacoesSliderViewMvcImpl = new AvaliacoesSliderViewMvcImpl(LayoutInflater.from(this), null);

        iniciarLancamentoNotas();

        setContentView(avaliacoesSliderViewMvcImpl.getRootView());
    }

    @Override
    protected void onStop() {

        super.onStop();

        avaliacoesSliderViewMvcImpl.unregisterListener();
    }

    @Override
    protected void onStart() {

        super.onStart();

        avaliacoesSliderViewMvcImpl.registerListener(this);
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    private void iniciarLancamentoNotas() {

        Bundle bundle;

        bundle = getIntent().getExtras();

        Fragment fragment = new FragmentLancamentoAvaliacaoPager();

        ((FragmentLancamentoAvaliacaoPager) fragment).registerOnBotaoVoltarListener(this);

        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().add(

                  R.id.pager1, fragment, "FragAvaliacaoLancamentoPager"

        ).addToBackStack("FragAvaliacaoLancamentoPager").commit();
    }

    @Override
    public void usuarioClicouBotaoVoltar() {

        onBackPressed();
    }
}
