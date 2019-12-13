package br.gov.sp.educacao.sed.mobile.Carteirinha;

import android.os.Build;

import android.util.Base64;

import android.widget.TextView;
import android.widget.ImageView;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.content.Context;
import android.content.res.Resources;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.BitmapFactory;

import br.gov.sp.educacao.sed.mobile.R;

import android.support.v4.content.res.ResourcesCompat;

class CarteirinhaViewMvcImpl
        implements CarteirinhaViewMvc {

    private TextView validade;

    private Listener listener;

    private TextView rgUsuario;

    private TextView rsUsuario;

    private Resources resources;

    private TextView nomeSocial;

    private final View mRootView;

    private TextView nomeUsuario;

    @SuppressWarnings("FieldCanBeLocal")
    private ImageView botaoVoltar;

    private ImageView fotoUsuario;

    private TextView cargoUsuario;

    private ImageView qrCodeUsuario;

    private TextView textoGovernoSP;

    private TextView textoSecretariaEducacao;

    private DadosCarteirinha dadosCarteirinha;

    CarteirinhaViewMvcImpl(LayoutInflater layoutInflater, int telaPequena, ViewGroup parent, Resources resources, DadosCarteirinha dadosCarteirinha) {

        this.dadosCarteirinha = dadosCarteirinha;

        this.resources = resources;

        if(telaPequena == 1) { //Tela maior que 4.3 e menor que 5

            mRootView = layoutInflater.inflate(R.layout.activity_carteirinha_tela_pequena, parent, false);
        }
        else if(telaPequena == 2) { //Tela menor que 4.3

            mRootView = layoutInflater.inflate(R.layout.activity_carteirinha_tela_muito_pequena, parent, false);
        }
        else {

            mRootView = layoutInflater.inflate(R.layout.activity_carteirinha, parent, false);
        }

        fotoUsuario = findViewById(R.id.foto);

        validade = findViewById(R.id.validade);

        rgUsuario = findViewById(R.id.rgUsuario);

        rsUsuario = findViewById(R.id.rsUsuario);

        qrCodeUsuario = findViewById(R.id.qrCode);

        nomeSocial = findViewById(R.id.nomeSocial);

        cargoUsuario = findViewById(R.id.nomeCargo);

        nomeUsuario = findViewById(R.id.nomeUsuario);

        botaoVoltar = findViewById(R.id.botaoVoltar);

        textoGovernoSP = findViewById(R.id.textoGovernoSP);

        textoSecretariaEducacao = findViewById(R.id.textoSecretariaEducacao);

        botaoVoltar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                listener.onBackPressed();
            }
        });

        exibirCarteirinha();
    }

    private void exibirCarteirinha() {

        if(Build.VERSION.SDK_INT >= 16) {

            Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.mavenproregular);

            rgUsuario.setTypeface(typeface);

            rsUsuario.setTypeface(typeface);

            cargoUsuario.setTypeface(typeface);

            validade.setTypeface(typeface);

            textoSecretariaEducacao.setTypeface(typeface);

            Typeface typeface1 = ResourcesCompat.getFont(getContext(), R.font.mavenprobold);

            textoGovernoSP.setTypeface(typeface1);

            nomeUsuario.setTypeface(typeface1);

            nomeSocial.setTypeface(typeface1);
        }

        nomeUsuario.setText(dadosCarteirinha.getNomeUsuario());

        nomeSocial.setText(!dadosCarteirinha.getNomeSocial().equals("null") ? dadosCarteirinha.getNomeSocial() : "");

        cargoUsuario.setText(dadosCarteirinha.getCargoUsuario());

        rgUsuario.setText(dadosCarteirinha.getRgUsuario());

        String textoRsUsuario = "RS: " + dadosCarteirinha.getRsUsuario();

        rsUsuario.setText(textoRsUsuario);

        String textoValidade = "válido até: " + (!dadosCarteirinha.getValidade().equals("null") ? dadosCarteirinha.getValidade() : "");

        validade.setText(textoValidade);

        fotoUsuario.setImageBitmap(getFotoCarteirinha());

        qrCodeUsuario.setImageBitmap(getQRCodeCarteirinha());
    }

    private Bitmap getFotoCarteirinha() {

        Bitmap bitmapFoto = null;

        String fotoBase64 = dadosCarteirinha.getFotoUsuario();

        try {

            //fotoBase64 = String.valueOf(lerArquivoEstatico(R.raw.fotobase64));

            bitmapFoto = bitmapFromBase64(fotoBase64);
        }
        catch(Exception e) {

            e.printStackTrace();
        }

        return bitmapFoto;
    }

    private Bitmap getQRCodeCarteirinha() {

        Bitmap bitmapQrCode = null;

        String qrCodeBase64 = dadosCarteirinha.getQrCodeUsuario();

        try {

            //qrCodeBase64 = String.valueOf(lerArquivoEstatico(R.raw.qrcodebase64));

            bitmapQrCode = bitmapFromBase64(qrCodeBase64);
        }
        catch(Exception e) {

            e.printStackTrace();
        }

        return bitmapQrCode;
    }

    private StringBuilder lerArquivoEstatico(int fileID) throws IOException {

        BufferedReader br = new BufferedReader(

                new InputStreamReader(resources.openRawResource(fileID), "UTF-8")
        );

        StringBuilder jsonString = new StringBuilder();

        String line;

        while((line = br.readLine()) != null) {

            line = line.trim();

            if(line.length() > 0) {

                jsonString.append(line);
            }
        }
        return jsonString;
    }

    private Bitmap bitmapFromBase64(String imgString){

        String[] base = imgString.split(",");

        String input = base[1];

        byte[] imageBytes;

        imageBytes = Base64.decode(input, Base64.DEFAULT);

        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    @Override
    public View getRootView() {

        return mRootView;
    }

    private Context getContext() {

        return getRootView().getContext();
    }

    private <T extends View> T findViewById(int id) {

        return getRootView().findViewById(id);
    }

    @Override
    public void registerListener(Listener listener) {

        this.listener = listener;
    }

    @Override
    public void unregisterListener() {

        this.listener = null;
    }
}
