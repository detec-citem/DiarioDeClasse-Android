package br.gov.sp.educacao.sed.mobile.Carteirinha;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import br.gov.sp.educacao.sed.mobile.R;

public class ListaCarteirinhasViewMvcImpl
        implements ListaCarteirinhasViewMvc,
        CarteirinhaAdapter.OnCarteirinhaSelecionadaListener, RevalidarTokenInterface {

    @SuppressWarnings("FieldCanBeLocal")
    private Bitmap src;

    @SuppressWarnings("FieldCanBeLocal")
    private Bitmap src1;

    private View mRootView;

    private Toolbar toolbar;

    private Listener listener;

    @SuppressWarnings("FieldCanBeLocal")
    private AlertDialog dialogCarteirinhas;

    @SuppressWarnings("FieldCanBeLocal")
    private RecyclerView recyclerView;

    @SuppressWarnings("FieldCanBeLocal")
    private CarteirinhaAdapter adapter;

    private Resources resources;

    private List<DadosCarteirinha> listaCarteirinhas;

    private TextView buscarCarteirinhas;

    private LayoutInflater layoutInflater;

    private ToolbarViewMvcImpl toolbarViewMvcImpl;

    @SuppressWarnings("FieldCanBeLocal")
    private RoundedBitmapDrawable roundedBitmapDrawable;

    @SuppressWarnings("FieldCanBeLocal")
    private RoundedBitmapDrawable roundedBitmapDrawable1;

    ListaCarteirinhasViewMvcImpl(LayoutInflater layoutInflater, ViewGroup parent, Resources resources, List<DadosCarteirinha> listaCarteirinhas) {

        mRootView = layoutInflater.inflate(R.layout.activity_lista_carteirinhas, parent, false);

        toolbar = findViewById(R.id.toolbar);

        this.layoutInflater = layoutInflater;

        this.resources = resources;

        toolbarViewMvcImpl = getToolbarViewMvcImpl(toolbar);

        inicializarToolbar();

        buscarCarteirinhas = toolbarViewMvcImpl.getBuscarCarteirinhas();

        inicializarListenerBuscarCarteirinhas();

        this.listaCarteirinhas = listaCarteirinhas;

        recyclerView = findViewById(R.id.recycler);

        criarAdapter();
    }

    private void criarAdapter() {

        roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, getImgCarteirinha());

        roundedBitmapDrawable.setCornerRadius(30);

        src1 = BitmapFactory.decodeResource(resources, R.drawable.background);

        roundedBitmapDrawable1 = RoundedBitmapDrawableFactory.create(resources, src1);

        roundedBitmapDrawable1.setCornerRadius(10);

        adapter = new CarteirinhaAdapter(getContext(), this.listaCarteirinhas, roundedBitmapDrawable, roundedBitmapDrawable1, this);

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private Bitmap getImgCarteirinha() {

        Bitmap bitmapFoto = null;

        String fotoBase64 = "Aguardando Foto";

        if(listaCarteirinhas.size() > 0) {

            fotoBase64 = listaCarteirinhas.get(0).getFotoUsuario();
        }

        try {

            if(!fotoBase64.equals("Aguardando Foto")) {

                bitmapFoto = bitmapFromBase64(fotoBase64);
            }
            else {

                src = BitmapFactory.decodeResource(resources, R.drawable.carteirinhasemfoto);

                roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, src);

                bitmapFoto = roundedBitmapDrawable.getBitmap();
            }
        }
        catch(Exception e) {

            e.printStackTrace();
        }

       return bitmapFoto;
    }

    private Bitmap getQRCodeCarteirinha() {

        Bitmap bitmapQrCode = null;

        String qrCodeBase64 = listaCarteirinhas.get(0).getQrCodeUsuario();

        try {

            //fotoBase64 = String.valueOf(lerArquivoEstatico(R.raw.fotobase64));

            bitmapQrCode = bitmapFromBase64(qrCodeBase64);
        }
        catch(Exception e) {

            e.printStackTrace();
        }

        return bitmapQrCode;
    }

    private Bitmap bitmapFromBase64(String imgString){

        String[] base = imgString.split(",");

        String input = "";

        if(base.length > 1) {

            input = base[1];
        }
        else {

            input = imgString;
        }

        byte[] imageBytes;

        imageBytes = Base64.decode(input, Base64.DEFAULT);

        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    StringBuilder lerArquivoEstatico(int fileID) throws IOException {

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

    @Override
    public void revalidacaoDeToken(String token) {

        if (listener != null) {
            listener.atualizarToken(token);
        }

    }

    public void perfilSelecionado(boolean perfilOk) {
        if (listener != null) {
            listener.perfilSelecionado(perfilOk);
        }
    }

    void atualizarListaDeCarteirinhas() {

        adapter.notifyDataSetChanged();

        roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, getImgCarteirinha());

        roundedBitmapDrawable.setCornerRadius(30);

        new android.os.Handler().post(new Runnable() {

            @Override
            public void run() {

                for(int i = 0; i < listaCarteirinhas.size(); i++) {

                    RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(i);

                    if(viewHolder != null) {

                        View view = viewHolder.itemView;

                        ImageView imageView = view.findViewById(R.id.foto);

                        imageView.setImageDrawable(roundedBitmapDrawable);
                    }
                }
            }
        });

        finalizarProgress();
    }

    private void inicializarListenerBuscarCarteirinhas() {

        buscarCarteirinhas.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                inicializaProgress();

                if (listener != null) {
                    listener.usuarioQuerAtualizarCarteirinhas();
                }
            }
        });
    }

    @Override
    public View getRootView() {

        return mRootView;
    }

    private void finalizarProgress() {

        dialogCarteirinhas.dismiss();
    }

    void inicializaProgress() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(

                getContext(), R.style.ThemeOverlay_AppCompat_Dialog
        );

        View view = layoutInflater.inflate(R.layout.dialogcarteirinhas, null, false);

        builder.setView(view);

        dialogCarteirinhas = builder.create();

        dialogCarteirinhas.setCancelable(false);

        dialogCarteirinhas.setCanceledOnTouchOutside(false);

        dialogCarteirinhas.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);

        dialogCarteirinhas.show();
    }

    private Context getContext() {

        return getRootView().getContext();
    }

    @Override
    public void unregisterListener() {

        this.listener = null;
    }

    private void inicializarToolbar() {

        toolbarViewMvcImpl.setTitle("Selecione a Carteirinha");

        toolbar.setNavigationIcon(R.drawable.icone_voltar);

        toolbarViewMvcImpl.setBuscarCarteirinhasAtivo();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (listener != null) {
                    listener.onBackPressed();
                }

            }
        });

        toolbar.addView(toolbarViewMvcImpl.getRootView());
    }

    private <T extends View> T findViewById(int id) {

        return getRootView().findViewById(id);
    }

    @Override
    public void registerListener(Listener listener) {

        this.listener = listener;
    }

    void dadosRecebidosCarteirinhas(String respostaJsonCarteirinha) {
        if (listener != null) {
            listener.analisarRespostaCarteirinhas(respostaJsonCarteirinha);
        }
    }

    @Override
    public ToolbarViewMvcImpl getToolbarViewMvcImpl(@Nullable ViewGroup parent) {

        return new ToolbarViewMvcImpl(layoutInflater, parent);
    }

    @Override
    public void onCarteirinhaSelecionada(DadosCarteirinha dadosCarteirinha, View view1, View view2) {
        if (listener != null) {
            listener.onCarteirinhaSelecionada(dadosCarteirinha, view1, view2);
        }
    }

    void avisoUsuarioErroBuscarCarteirinhas() {

        finalizarProgress();

        Toast.makeText(getContext(), "Ocorreu um erro, tente novamente.", Toast.LENGTH_SHORT).show();
    }

    void avisoUsuarioCarteirinhaNaoAprovada() {

        Toast.makeText(getContext(), "Carteirinha Aguardando Aprovação", Toast.LENGTH_SHORT).show();
    }

    void avisoUsuarioServidorSemRS() {

        Toast.makeText(getContext(), "Aguardando geração de código RS", Toast.LENGTH_SHORT).show();
    }
}