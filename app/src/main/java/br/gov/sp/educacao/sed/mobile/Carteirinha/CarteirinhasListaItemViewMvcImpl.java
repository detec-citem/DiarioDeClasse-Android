package br.gov.sp.educacao.sed.mobile.Carteirinha;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import br.gov.sp.educacao.sed.mobile.R;

public class CarteirinhasListaItemViewMvcImpl
        implements  ListaCarteirinhasItemViewMvc {

    private ImageView foto;

    private ImageView background;

    private TextView nomeCargo;

    private View mRootView;

    private Listener listener;

    private DadosCarteirinha dadosCarteirinha;

    private RoundedBitmapDrawable roundedBitmapDrawable;

    private RoundedBitmapDrawable roundedBitmapDrawable1;

    private String TAG = CarteirinhasListaItemViewMvcImpl.class.getSimpleName();

    CarteirinhasListaItemViewMvcImpl(LayoutInflater layoutInflater, ViewGroup parent, RoundedBitmapDrawable rd1, RoundedBitmapDrawable rd2) {

        mRootView = layoutInflater.inflate(R.layout.layout_carteirinha, parent, false);

        foto = findViewById(R.id.foto);

        background = findViewById(R.id.imagem);

        nomeCargo = findViewById(R.id.nomeCargo);

        roundedBitmapDrawable = rd1;

        roundedBitmapDrawable1 = rd2;

        getRootView().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                listener.onCarteirinhaSelecionada(dadosCarteirinha, foto, nomeCargo);
            }
        });
    }

    @Override
    public void setarCarteirinha(DadosCarteirinha dadosCarteirinha, CarteirinhaAdapter.MyViewHolder holder) {

        if(Build.VERSION.SDK_INT >= 16) {

            Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.mavenproregular);

            nomeCargo.setTypeface(typeface);
        }

        nomeCargo.setText(dadosCarteirinha.getCargoUsuario());

        foto.setImageDrawable(roundedBitmapDrawable);

        background.setImageDrawable(roundedBitmapDrawable1);

        this.dadosCarteirinha = dadosCarteirinha;
    }

    @Override
    public View getRootView() {

        return mRootView;
    }

    private Context getContext() {

        return getRootView().getContext();
    }

    @Override
    public void unregisterListener() {

        this.listener = null;
    }

    @Override
    public void registerListener(Listener listener) {

        this.listener = listener;
    }

    private <T extends  View> T findViewById(int id) {

        return getRootView().findViewById(id);
    }
}
