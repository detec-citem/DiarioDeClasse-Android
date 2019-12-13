package br.gov.sp.educacao.sed.mobile.Carteirinha;

import android.os.Build;
import android.os.Bundle;

import org.json.JSONArray;

import java.util.List;
import java.util.ArrayList;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.app.ActivityOptions;

import android.view.View;
import android.view.Window;
import android.view.LayoutInflater;

import android.content.Intent;
import android.content.res.Resources;

import android.support.v7.app.AppCompatActivity;

import br.gov.sp.educacao.sed.mobile.Login.UsuarioTO;

import br.gov.sp.educacao.sed.mobile.R;
import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.CriarAcessoBanco;

import br.gov.sp.educacao.sed.mobile.Menu.RevalidarTokenAsyncTask;
import br.gov.sp.educacao.sed.mobile.Menu.SelecionarPerfilAsyncTask;

public class ListaCarteirinhasActivity
        extends AppCompatActivity
         implements ListaCarteirinhasViewMvc.Listener, ListaCarteirinhasItemViewMvc.Listener {

    private Banco banco;

    private UsuarioTO usuario;

    private boolean atualizarCarteirinhas;

    @SuppressWarnings("FieldCanBeLocal")
    private CriarAcessoBanco criarAcessoBanco;

    private CarteirinhaDBcrud carteirinhaDBCrud;

    private List<String> codigoCargoCarteirinhas;

    private List<DadosCarteirinha> carteirinhasNoJson;

    @SuppressWarnings("FieldCanBeLocal")
    private CarteirinhaAsyncTask carteirinhaAsyncTask;

    @SuppressWarnings("FieldCanBeLocal")
    private CarteirinhaDBgetters carteirinhaDBgetters;

    @SuppressWarnings("FieldCanBeLocal")
    private List<DadosCarteirinha> carteirinhasNoBancoLocal;

    @SuppressWarnings("FieldCanBeLocal")
    private  RevalidarTokenAsyncTask revalidarTokenAsyncTask;

    @SuppressWarnings("FieldCanBeLocal")
    private SelecionarPerfilAsyncTask selecionarPerfilAsyncTask;

    private ListaCarteirinhasViewMvcImpl listaCarteirinhasViewMvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        atualizarCarteirinhas = false;

        usuario = getIntent().getParcelableExtra("usuario");

        criarAcessoBanco = new CriarAcessoBanco();

        banco = criarAcessoBanco.gerarBanco(this);

        carteirinhaDBCrud = new CarteirinhaDBcrud(banco);

        carteirinhaDBgetters = new CarteirinhaDBgetters(banco);

        carteirinhasNoBancoLocal = carteirinhaDBgetters.getCarteirinhas();

        carteirinhasNoJson = new ArrayList<>();

        codigoCargoCarteirinhas = new ArrayList<>();

        Resources resources = getResources();

        listaCarteirinhasViewMvc = new ListaCarteirinhasViewMvcImpl(LayoutInflater.from(this), null, resources, carteirinhasNoBancoLocal);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){

            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }

        setContentView(listaCarteirinhasViewMvc.getRootView());
    }

    @Override
    protected void onStop() {

        super.onStop();

        listaCarteirinhasViewMvc.unregisterListener();
    }

    @Override
    protected void onStart() {

        super.onStart();

        if(carteirinhasNoBancoLocal.size() == 0) {

            listaCarteirinhasViewMvc.inicializaProgress();

            revalidarToken();
        }
    }

    @Override
    protected void onResume() {

        super.onResume();

        listaCarteirinhasViewMvc.registerListener(this);
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    public void revalidarToken() {

        revalidarTokenAsyncTask = new RevalidarTokenAsyncTask();

        revalidarTokenAsyncTask.delegate = listaCarteirinhasViewMvc;

        revalidarTokenAsyncTask.execute(usuario.getUsuario(), usuario.getSenha());
    }

    @Override
    public void buscarCarteirinhas() {

        carteirinhaAsyncTask = new CarteirinhaAsyncTask();

        carteirinhaAsyncTask.delegate = listaCarteirinhasViewMvc;

        carteirinhaAsyncTask.execute(usuario.getCpf(), usuario.getToken());
    }

    public void salvarCarteirinhasBanco() {

        try {

            banco.get().beginTransaction();

            for(int i = 0; i < carteirinhasNoJson.size(); i++) {

                carteirinhaDBCrud.insertCarteirinha(carteirinhasNoJson.get(i));
            }
        }
        catch(Exception e) {

            e.printStackTrace();
        }
        finally {

            banco.get().setTransactionSuccessful();

            banco.get().endTransaction();
        }
    }

    private void erroRequestCarteirinhas() {

        listaCarteirinhasViewMvc.avisoUsuarioErroBuscarCarteirinhas();
    }

    public void atualizarToken(String token) {

        if(token != null && !token.isEmpty()) {

            usuario.setToken(token);

            selecionarPerfil(token);
        }
    }

    public void atualizarCarteirinhasBanco() {

        try {

            banco.get().beginTransaction();

            for(int i = 0; i < carteirinhasNoJson.size(); i++) {

                carteirinhaDBCrud.atualizarCarteirinha(carteirinhasNoJson.get(i));

                codigoCargoCarteirinhas.add(carteirinhasNoJson.get(i).getCodigoCargo());
            }
        }
        catch(Exception e) {

            e.printStackTrace();
        }
        finally {

            banco.get().setTransactionSuccessful();

            banco.get().endTransaction();
        }
    }

    public void atualizarListaDeCarteirinhas() {

        carteirinhasNoBancoLocal.clear();

        carteirinhasNoBancoLocal.addAll(carteirinhasNoJson);

        carteirinhasNoJson.clear();

        listaCarteirinhasViewMvc.atualizarListaDeCarteirinhas();
    }

    @Override
    public void selecionarPerfil(String token) {

        selecionarPerfilAsyncTask = new SelecionarPerfilAsyncTask();

        selecionarPerfilAsyncTask.delegate = listaCarteirinhasViewMvc;

        selecionarPerfilAsyncTask.execute(token);
    }

    public void usuarioQuerAtualizarCarteirinhas() {

        atualizarCarteirinhas = true;

        revalidarToken();
    }

    @Override
    public void perfilSelecionado(boolean perfilOK) {

        if(perfilOK) {

            buscarCarteirinhas();
        }
        else {

            listaCarteirinhasViewMvc.avisoUsuarioErroBuscarCarteirinhas();
        }
    }

    private void deletarCarteirinhasQueNaoEstaoNoJson() {

        try {

            banco.get().beginTransaction();

            for(int i = 0; i < carteirinhasNoBancoLocal.size(); i++) {

                if(!codigoCargoCarteirinhas.contains(carteirinhasNoBancoLocal.get(i).getCodigoCargo())) {

                    carteirinhaDBCrud.deletarCarteirinha(carteirinhasNoBancoLocal.get(i).getCodigoCargo());
                }
            }
        }
        catch(Exception e) {

            e.printStackTrace();
        }
        finally {

            codigoCargoCarteirinhas.clear();

            codigoCargoCarteirinhas = null;

            banco.get().setTransactionSuccessful();

            banco.get().endTransaction();
        }
    }

    private void exibirCarteirinha(View view1, String codigoCargo) {

        Intent intent = new Intent(this, CarteirinhaActivity.class);

        intent.putExtra("codigoCargo", codigoCargo);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, view1, "foto");

            startActivity(intent, options.toBundle());
        }
        else {

            startActivity(intent);
        }
    }

    public void pegarCarteirinhasJson(String respostaJsonCarteirinha) {

        JSONArray jsonArray;

        try {

            jsonArray = new JSONArray(respostaJsonCarteirinha);

            for(int i = 0; i < jsonArray.length(); i++) {

                DadosCarteirinha dadosCarteirinha = new DadosCarteirinha();

                dadosCarteirinha.setJSON(jsonArray.getJSONObject(i));

                carteirinhasNoJson.add(dadosCarteirinha);
            }
        }
        catch(Exception e) {

            e.printStackTrace();
        }
        finally {

            jsonArray = null;
        }
    }

    @Override
    public void analisarRespostaCarteirinhas(String respostaJsonCarteirinha) {

        /*String respostaJsonCarteirinha1 = "";


        try {


            respostaJsonCarteirinha1 = String.valueOf(lerArquivoEstatico(R.raw.jsoncarteirinha));
        }
        catch (IOException e) {

            e.printStackTrace();
        }
        processarRespostaCarteirinhas(respostaJsonCarteirinha1);*/

        if(!respostaJsonCarteirinha.equals("")) {

            processarRespostaCarteirinhas(respostaJsonCarteirinha);
        }
        else {

            erroRequestCarteirinhas();
        }
    }

    protected StringBuilder lerArquivoEstatico(int fileID) throws IOException {

        BufferedReader br = new BufferedReader(

                new InputStreamReader(getApplicationContext().getResources().openRawResource(fileID), "UTF-8")
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

    private void processarRespostaCarteirinhas(String respostaJsonCarteirinha) {

        pegarCarteirinhasJson(respostaJsonCarteirinha);

        if(atualizarCarteirinhas) {

            atualizarCarteirinhasBanco();

            if(carteirinhasNoJson.size() != carteirinhasNoBancoLocal.size()) {

                deletarCarteirinhasQueNaoEstaoNoJson();
            }
        }
        else {

            salvarCarteirinhasBanco();
        }
        atualizarListaDeCarteirinhas();
    }

    @Override
    public void onCarteirinhaSelecionada(DadosCarteirinha dadosCarteirinha, View view1, View view2) {

        if(dadosCarteirinha.getStatus().equals("Aguardando aprovação")) {

            listaCarteirinhasViewMvc.avisoUsuarioCarteirinhaNaoAprovada();
        }
        else if(dadosCarteirinha.getRsUsuario().equals("null")) {

            listaCarteirinhasViewMvc.avisoUsuarioServidorSemRS();
        }
        else {

            exibirCarteirinha(view1, dadosCarteirinha.getCodigoCargo());
        }
    }
}
