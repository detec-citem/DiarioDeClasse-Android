package br.gov.sp.educacao.sed.mobile.Menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import br.gov.sp.educacao.sed.mobile.R;
import br.gov.sp.educacao.sed.mobile.constants.EtapasSincronizacao;

public class SincronizacaoDialog implements OnClickListener, Runnable {

    private boolean terminouEtapasSemErro = true;
    private Context context;
    private AlertDialog dialog;
    private Button falhouButton;
    private Drawable erroDrawable;
    private Drawable esperaDrawable;
    private Drawable sucessoDrawable;
    private ImageView fecharImageView;
    private ImageView tokenImageView;
    private ImageView avaliacoesImageView;
    private ImageView frequenciasImageView;
    private ImageView registrosImageView;
    private ImageView dadosImageView;
    private ProgressBar tokenProgressBar;
    private ProgressBar avaliacoesProgressBar;
    private ProgressBar frequenciasProgressBar;
    private ProgressBar registrosProgressBar;
    private ProgressBar dadosProgressBar;
    private ViewGroup sincronizacaoViewGroup;
    private ViewGroup sucessoViewGroup;
    private SincronizacaoDialogListener listener;

    //Construtor
    SincronizacaoDialog(Context context, SincronizacaoDialogListener listener) {
        this.context = context;
        this.listener = listener;
    }

    void mostrarDialogSincronizacao() {

        if (dialog == null) {

            View view = LayoutInflater.from(context).inflate(R.layout.dialog_sincronizacao, null);

            falhouButton = view.findViewById(R.id.btn_falhou);
            fecharImageView = view.findViewById(R.id.iv_fechar);
            tokenImageView = view.findViewById(R.id.iv_token);
            avaliacoesImageView = view.findViewById(R.id.iv_avaliacoes);
            frequenciasImageView = view.findViewById(R.id.iv_frequencia);
            registrosImageView = view.findViewById(R.id.iv_registro);
            dadosImageView = view.findViewById(R.id.iv_dados);
            tokenProgressBar = view.findViewById(R.id.pb_token);
            avaliacoesProgressBar = view.findViewById(R.id.pb_avaliacoes);
            frequenciasProgressBar = view.findViewById(R.id.pb_frequencia);
            registrosProgressBar = view.findViewById(R.id.pb_registro);
            dadosProgressBar = view.findViewById(R.id.pb_dados);
            sincronizacaoViewGroup = view.findViewById(R.id.cl_sicronizacao);
            sucessoViewGroup = view.findViewById(R.id.cl_sucesso);

            falhouButton.setOnClickListener(this);
            fecharImageView.setOnClickListener(this);

            dialog = new AlertDialog.Builder(context).setView(view).create();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
        }

        resetarDialog();
        dialog.show();
    }

    //Métodos
    void terminouEtapa(String etapa, boolean sucesso) {

        switch (etapa){

            case EtapasSincronizacao.ETAPA_TOKEN :

                tokenImageView.setVisibility(View.VISIBLE);
                tokenProgressBar.setVisibility(View.INVISIBLE);

                if (sucesso) {

                    criarIconeSucesso();
                    tokenImageView.setImageDrawable(sucessoDrawable);
                }
                else {

                    terminouEtapasSemErro = false;
                    criarIconeErro();
                    tokenImageView.setImageDrawable(erroDrawable);
                }

                avaliacoesProgressBar.setVisibility(View.VISIBLE);
                avaliacoesImageView.setVisibility(View.INVISIBLE);
                break;

            case EtapasSincronizacao.ETAPA_AVAL :

                avaliacoesProgressBar.setVisibility(View.INVISIBLE);
                avaliacoesImageView.setVisibility(View.VISIBLE);

                if (sucesso) {

                    criarIconeSucesso();
                    avaliacoesImageView.setImageDrawable(sucessoDrawable);
                }
                else {

                    terminouEtapasSemErro = false;
                    criarIconeErro();
                    avaliacoesImageView.setImageDrawable(erroDrawable);
                }

                frequenciasProgressBar.setVisibility(View.VISIBLE);
                frequenciasImageView.setVisibility(View.INVISIBLE);
                break;

            case EtapasSincronizacao.ETAPA_FALTAS :

                frequenciasProgressBar.setVisibility(View.INVISIBLE);
                frequenciasImageView.setVisibility(View.VISIBLE);

                if (sucesso) {

                    criarIconeSucesso();
                    frequenciasImageView.setImageDrawable(sucessoDrawable);
                }
                else {

                    terminouEtapasSemErro = false;
                    criarIconeErro();
                    frequenciasImageView.setImageDrawable(erroDrawable);
                }

                registrosImageView.setVisibility(View.INVISIBLE);
                registrosProgressBar.setVisibility(View.VISIBLE);
                break;

            case EtapasSincronizacao.ETAPA_REGISTRO :

                registrosProgressBar.setVisibility(View.INVISIBLE);
                registrosImageView.setVisibility(View.VISIBLE);

                if (sucesso) {
                    criarIconeSucesso();
                    registrosImageView.setImageDrawable(sucessoDrawable);
                }
                else {

                    terminouEtapasSemErro = false;
                    criarIconeErro();
                    registrosImageView.setImageDrawable(erroDrawable);
                }

                dadosProgressBar.setVisibility(View.VISIBLE);
                dadosImageView.setVisibility(View.INVISIBLE);
                break;

            case EtapasSincronizacao.ETAPA_DADOS:

                dadosProgressBar.setVisibility(View.INVISIBLE);
                dadosImageView.setVisibility(View.VISIBLE);

                if (sucesso) {

                    criarIconeSucesso();
                    dadosImageView.setImageDrawable(sucessoDrawable);
                }
                else {

                    terminouEtapasSemErro = false;
                    criarIconeErro();
                    dadosImageView.setImageDrawable(erroDrawable);
                }

                terminouSincronizacao();
                break;

        }
    }

    private void terminouSincronizacao(){

        if (terminouEtapasSemErro) {

            sincronizacaoViewGroup.setVisibility(View.GONE);
            sucessoViewGroup.setVisibility(View.VISIBLE);
            sucessoViewGroup.postDelayed(this, 2500);
        }
        else {

            fecharImageView.setVisibility(View.VISIBLE);
            falhouButton.setVisibility(View.VISIBLE);
        }
    }

    private void criarIconeErro() {

        if (erroDrawable == null) {

            erroDrawable = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_sinc_falha, null);
        }
    }

    private void criarIconeSucesso() {

        if (sucessoDrawable == null) {

            sucessoDrawable = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_sinc_sucess, null);
        }
    }

    private void resetarDialog() {

        falhouButton.setVisibility(View.INVISIBLE);
        fecharImageView.setVisibility(View.GONE);
        sincronizacaoViewGroup.setVisibility(View.VISIBLE);
        sucessoViewGroup.setVisibility(View.GONE);
        avaliacoesProgressBar.setVisibility(View.GONE);
        frequenciasProgressBar.setVisibility(View.GONE);
        registrosProgressBar.setVisibility(View.GONE);
        tokenProgressBar.setVisibility(View.VISIBLE);

        if (esperaDrawable == null) {

            esperaDrawable = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_sinc_espera, null);
        }

        avaliacoesImageView.setImageDrawable(esperaDrawable);
        frequenciasImageView.setImageDrawable(esperaDrawable);
        registrosImageView.setImageDrawable(esperaDrawable);
        dadosImageView.setImageDrawable(esperaDrawable);
        tokenImageView.setImageDrawable(esperaDrawable);
    }

    //OnClickListener
    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_falhou) {

            resetarDialog();
            listener.tentarNovamente();
        }
        else {

            dialog.dismiss();
        }
    }

    //Runnable
    @Override
    public void run() {

        dialog.dismiss();
        listener.terminouSincronizacao();
    }
}