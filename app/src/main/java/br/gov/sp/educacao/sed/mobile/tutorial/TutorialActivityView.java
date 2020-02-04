package br.gov.sp.educacao.sed.mobile.tutorial;

import android.content.res.Resources;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import br.gov.sp.educacao.sed.mobile.R;

public class TutorialActivityView implements TutorialActivityInterface {
    //Variáveis
    private int posicaoTutorial = 1;
    private Button avancarButton;
    private Button voltarButton;
    private ImageView tutorialImageView;
    private ImageView ponto1, ponto2, ponto3, ponto4, ponto5, ponto6, ponto7, ponto8, ponto9, ponto10, ponto11, ponto12, ponto13, ponto14, ponto15, ponto16, ponto17;
    private Listener listener;
    private TextView linkTextView;
    private VectorDrawableCompat pontoAtivoDrawable;
    private VectorDrawableCompat pontoDefaultDrawable;
    private View rootView;

    //Construtor
    public TutorialActivityView(LayoutInflater layoutInflater, ViewGroup parent) {
        rootView = layoutInflater.inflate(R.layout.activity_tutorial, parent, false);
        tutorialImageView = findViewById(R.id.tutorial_img_fundo);
        ponto1 = findViewById(R.id.tutorial_img_ponto_1);
        ponto2 = findViewById(R.id.tutorial_img_ponto_2);
        ponto3 = findViewById(R.id.tutorial_img_ponto_3);
        ponto4 = findViewById(R.id.tutorial_img_ponto_4);
        ponto5 = findViewById(R.id.tutorial_img_ponto_5);
        ponto6 = findViewById(R.id.tutorial_img_ponto_6);
        ponto7 = findViewById(R.id.tutorial_img_ponto_7);
        ponto8 = findViewById(R.id.tutorial_img_ponto_8);
        ponto9 = findViewById(R.id.tutorial_img_ponto_9);
        ponto10 = findViewById(R.id.tutorial_img_ponto_10);
        ponto11 = findViewById(R.id.tutorial_img_ponto_11);
        ponto12 = findViewById(R.id.tutorial_img_ponto_12);
        ponto13 = findViewById(R.id.tutorial_img_ponto_13);
        ponto14 = findViewById(R.id.tutorial_img_ponto_14);
        ponto15 = findViewById(R.id.tutorial_img_ponto_15);
        ponto16 = findViewById(R.id.tutorial_img_ponto_16);
        ponto17 = findViewById(R.id.tutorial_img_ponto_17);
        avancarButton = findViewById(R.id.tutorial_btn_avancar);
        voltarButton = findViewById(R.id.tutorial_btn_voltar);
        linkTextView = findViewById(R.id.tutorial_txt_link);
        Resources resources = layoutInflater.getContext().getResources();
        pontoAtivoDrawable = VectorDrawableCompat.create(resources, R.drawable.tutorial_ponto_ativo, null);
        pontoDefaultDrawable = VectorDrawableCompat.create(resources, R.drawable.tutorial_ponto_default, null);
        linkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.navegarParaAtendimentoSed();
                }
            }
        });
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    @Override
    public void unregisterListener() {
        listener = null;
    }

    @Override
    public void registerListener(Listener listener) {
        this.listener = listener;
    }

    private <T extends View> T findViewById(int id) {
        return rootView.findViewById(id);
    }

    @Override
    public void avancarImagem() {

        switch (posicaoTutorial) {
            case 1:
                tutorialImageView.setBackgroundResource(R.drawable.tutorial_img_02);
                ponto1.setImageDrawable(pontoDefaultDrawable);
                ponto2.setImageDrawable(pontoAtivoDrawable);
                voltarButton.setBackgroundResource(R.drawable.tutorial_btn_voltar);
                break;

            case 2:

                tutorialImageView.setBackgroundResource(R.drawable.tutorial_img_03);
                ponto2.setImageDrawable(pontoDefaultDrawable);
                ponto3.setImageDrawable(pontoAtivoDrawable);
                break;

            case 3:

                tutorialImageView.setBackgroundResource(R.drawable.tutorial_img_04);
                ponto3.setImageDrawable(pontoDefaultDrawable);
                ponto4.setImageDrawable(pontoAtivoDrawable);
                break;

            case 4:

                tutorialImageView.setBackgroundResource(R.drawable.tutorial_img_05);
                ponto4.setImageDrawable(pontoDefaultDrawable);
                ponto5.setImageDrawable(pontoAtivoDrawable);
                break;

            case 5:

                tutorialImageView.setBackgroundResource(R.drawable.tutorial_img_06);
                ponto5.setImageDrawable(pontoDefaultDrawable);
                ponto6.setImageDrawable(pontoAtivoDrawable);
                break;

            case 6:

                tutorialImageView.setBackgroundResource(R.drawable.tutorial_img_07);
                ponto6.setImageDrawable(pontoDefaultDrawable);
                ponto7.setImageDrawable(pontoAtivoDrawable);
                break;

            case 7:

                tutorialImageView.setBackgroundResource(R.drawable.tutorial_img_08);
                ponto7.setImageDrawable(pontoDefaultDrawable);
                ponto8.setImageDrawable(pontoAtivoDrawable);
                break;

            case 8:

                tutorialImageView.setBackgroundResource(R.drawable.tutorial_img_09);
                ponto8.setImageDrawable(pontoDefaultDrawable);
                ponto9.setImageDrawable(pontoAtivoDrawable);
                break;

            case 9:

                tutorialImageView.setBackgroundResource(R.drawable.tutorial_img_10);
                ponto9.setImageDrawable(pontoDefaultDrawable);
                ponto10.setImageDrawable(pontoAtivoDrawable);
                break;

            case 10:

                tutorialImageView.setBackgroundResource(R.drawable.tutorial_img_11);
                ponto10.setImageDrawable(pontoDefaultDrawable);
                ponto11.setImageDrawable(pontoAtivoDrawable);
                break;

            case 11:

                tutorialImageView.setBackgroundResource(R.drawable.tutorial_img_12);
                ponto11.setImageDrawable(pontoDefaultDrawable);
                ponto12.setImageDrawable(pontoAtivoDrawable);
                break;

            case 12:

                tutorialImageView.setBackgroundResource(R.drawable.tutorial_img_13);
                ponto12.setImageDrawable(pontoDefaultDrawable);
                ponto13.setImageDrawable(pontoAtivoDrawable);
                break;

            case 13:

                tutorialImageView.setBackgroundResource(R.drawable.tutorial_img_14);
                ponto13.setImageDrawable(pontoDefaultDrawable);
                ponto14.setImageDrawable(pontoAtivoDrawable);
                break;

            case 14:

                tutorialImageView.setBackgroundResource(R.drawable.tutorial_img_15);
                ponto14.setImageDrawable(pontoDefaultDrawable);
                ponto15.setImageDrawable(pontoAtivoDrawable);
                break;

            case 15:

                tutorialImageView.setBackgroundResource(R.drawable.tutorial_img_16);
                ponto15.setImageDrawable(pontoDefaultDrawable);
                ponto16.setImageDrawable(pontoAtivoDrawable);
                linkTextView.setVisibility(View.VISIBLE);
                break;

            case 16:

                tutorialImageView.setBackgroundResource(R.drawable.tutorial_img_17);
                ponto16.setImageDrawable(pontoDefaultDrawable);
                ponto17.setImageDrawable(pontoAtivoDrawable);
                avancarButton.setBackgroundResource(R.drawable.tutorial_btn_finalizar);
                linkTextView.setVisibility(View.INVISIBLE);
                break;

            case 17:

                listener.terminouTutorial();
                break;

        }

        posicaoTutorial++;
    }

    @Override
    public void voltarImagem() {

        switch (posicaoTutorial) {

            case 1:

                listener.terminouTutorial();
                break;

            case 2:

                tutorialImageView.setBackgroundResource(R.drawable.tutorial_img_01);
                ponto2.setImageDrawable(pontoDefaultDrawable);
                ponto1.setImageDrawable(pontoAtivoDrawable);
                voltarButton.setBackgroundResource(R.drawable.tutorial_btn_pular);
                break;

            case 3:

                tutorialImageView.setBackgroundResource(R.drawable.tutorial_img_02);
                ponto3.setImageDrawable(pontoDefaultDrawable);
                ponto2.setImageDrawable(pontoAtivoDrawable);
                break;

            case 4:

                tutorialImageView.setBackgroundResource(R.drawable.tutorial_img_03);
                ponto4.setImageDrawable(pontoDefaultDrawable);
                ponto3.setImageDrawable(pontoAtivoDrawable);
                break;

            case 5:

                tutorialImageView.setBackgroundResource(R.drawable.tutorial_img_04);
                ponto5.setImageDrawable(pontoDefaultDrawable);
                ponto4.setImageDrawable(pontoAtivoDrawable);
                break;

            case 6:

                tutorialImageView.setBackgroundResource(R.drawable.tutorial_img_05);
                ponto6.setImageDrawable(pontoDefaultDrawable);
                ponto5.setImageDrawable(pontoAtivoDrawable);
                break;

            case 7:

                tutorialImageView.setBackgroundResource(R.drawable.tutorial_img_06);
                ponto7.setImageDrawable(pontoDefaultDrawable);
                ponto6.setImageDrawable(pontoAtivoDrawable);
                break;

            case 8:

                tutorialImageView.setBackgroundResource(R.drawable.tutorial_img_07);
                ponto8.setImageDrawable(pontoDefaultDrawable);
                ponto7.setImageDrawable(pontoAtivoDrawable);
                break;

            case 9:

                tutorialImageView.setBackgroundResource(R.drawable.tutorial_img_08);
                ponto9.setImageDrawable(pontoDefaultDrawable);
                ponto8.setImageDrawable(pontoAtivoDrawable);
                break;

            case 10:

                tutorialImageView.setBackgroundResource(R.drawable.tutorial_img_09);
                ponto10.setImageDrawable(pontoDefaultDrawable);
                ponto9.setImageDrawable(pontoAtivoDrawable);
                break;

            case 11:

                tutorialImageView.setBackgroundResource(R.drawable.tutorial_img_10);
                ponto11.setImageDrawable(pontoDefaultDrawable);
                ponto10.setImageDrawable(pontoAtivoDrawable);
                break;

            case 12:

                tutorialImageView.setBackgroundResource(R.drawable.tutorial_img_11);
                ponto12.setImageDrawable(pontoDefaultDrawable);
                ponto11.setImageDrawable(pontoAtivoDrawable);
                break;

            case 13:

                tutorialImageView.setBackgroundResource(R.drawable.tutorial_img_12);
                ponto13.setImageDrawable(pontoDefaultDrawable);
                ponto12.setImageDrawable(pontoAtivoDrawable);
                break;

            case 14:

                tutorialImageView.setBackgroundResource(R.drawable.tutorial_img_13);
                ponto14.setImageDrawable(pontoDefaultDrawable);
                ponto13.setImageDrawable(pontoAtivoDrawable);
                break;

            case 15:

                tutorialImageView.setBackgroundResource(R.drawable.tutorial_img_14);
                ponto15.setImageDrawable(pontoDefaultDrawable);
                ponto14.setImageDrawable(pontoAtivoDrawable);
                break;

            case 16:

                tutorialImageView.setBackgroundResource(R.drawable.tutorial_img_15);
                ponto16.setImageDrawable(pontoDefaultDrawable);
                ponto15.setImageDrawable(pontoAtivoDrawable);
                linkTextView.setVisibility(View.INVISIBLE);
                break;

            case 17:

                tutorialImageView.setBackgroundResource(R.drawable.tutorial_img_16);
                ponto17.setImageDrawable(pontoDefaultDrawable);
                ponto16.setImageDrawable(pontoAtivoDrawable);
                avancarButton.setBackgroundResource(R.drawable.tutorial_btn_avancar);
                linkTextView.setVisibility(View.VISIBLE);
                break;

        }

        posicaoTutorial--;
    }
}