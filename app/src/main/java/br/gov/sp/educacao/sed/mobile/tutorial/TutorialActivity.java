package br.gov.sp.educacao.sed.mobile.tutorial;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import br.gov.sp.educacao.sed.mobile.Login.LoginActivity;
import br.gov.sp.educacao.sed.mobile.Menu.HomeActivity;

public class TutorialActivity extends AppCompatActivity implements TutorialActivityInterface.Listener {

    private TutorialActivityView tutorialActivityView;

    private String origem = "";
    private boolean temUsuarioLogado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tutorialActivityView = new TutorialActivityView(LayoutInflater.from(this), null);

        setContentView(tutorialActivityView.getRootView());

        origem = getIntent().getStringExtra("origem");

        temUsuarioLogado = getIntent().getBooleanExtra("temUsuario", false);
    }

    @Override
    public void terminouTutorial() {

        if(origem.equals("menu")){

            finish();
        }
        else if(origem.equals("splash")){

            if(temUsuarioLogado){

                startActivity(new Intent(this, HomeActivity.class));
            }
            else{

                startActivity(new Intent(this, LoginActivity.class));
            }

            finish();
        }
    }

    @Override
    public void navegarParaAtendimentoSed() {

        String url = "https://atendimento.educacao.sp.gov.br/";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));

        startActivity(intent);
    }

    public void clickAvancarTutorial(View v){

        tutorialActivityView.avancarImagem();
    }

    public void clickVoltarTutorial(View v){

        tutorialActivityView.voltarImagem();
    }

    @Override
    protected void onStart() {
        super.onStart();

        tutorialActivityView.registerListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        tutorialActivityView.unregisterListener();
    }
}
