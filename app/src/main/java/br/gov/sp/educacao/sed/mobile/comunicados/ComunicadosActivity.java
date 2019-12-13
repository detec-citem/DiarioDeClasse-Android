package br.gov.sp.educacao.sed.mobile.comunicados;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;

import br.gov.sp.educacao.sed.mobile.R;
import br.gov.sp.educacao.sed.mobile.util.CriarAcessoBanco;

public class ComunicadosActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private ComunicadoBdCrud comunicadoBdCrud;

    private CriarAcessoBanco criarAcessoBanco;

    private ArrayList<Comunicado> comunicados;

    private ComunicadoAdapter comunicadoAdapter;

    private ListView listaComunicados;

    private TextView txtAviso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunicados);

        toolbar = findViewById(R.id.comunicado_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listaComunicados = findViewById(R.id.comunicado_lista);

        txtAviso = findViewById(R.id.comunicado_txt_aviso);

        txtAviso.setVisibility(View.GONE);

        listaComunicados.setVisibility(View.GONE);

        iniciarBanco();

        carregarComunicados();
    }

    private void iniciarBanco() {

        criarAcessoBanco = new CriarAcessoBanco();

        comunicadoBdCrud = new ComunicadoBdCrud(criarAcessoBanco.gerarBanco(this));
    }


    private void carregarComunicados() {

        comunicados = comunicadoBdCrud.getTodosComunicados();

        //comunicados = new ArrayList<>();
        //Comunicado comunicado1 = new Comunicado(1,"Teste 1","teste teste teste", "02-03-2018", true);
        //Comunicado comunicado2 = new Comunicado(1,"Teste 2","teste teste teste teste teste teste", "25-09-2018", true);
        //Comunicado comunicado3 = new Comunicado(1,"Teste 3","teste teste teste teste teste teste teste teste teste", "19-10-2018", true);
        //Comunicado comunicado4 = new Comunicado(1,"Teste 4","teste teste teste teste teste teste teste teste teste teste teste teste", "26-10-2018", true);
        //Comunicado comunicado5 = new Comunicado(1,"Teste 5","teste teste teste teste teste teste teste teste teste teste teste teste teste teste teste", "30-10-2018", true);
        //Comunicado comunicado6 = new Comunicado(1,"Teste 6","teste teste teste teste teste teste teste teste teste teste teste teste teste teste teste teste teste teste", "29-11-2018", true);
        //Comunicado comunicado7 = new Comunicado(1,"Teste 7","teste teste teste teste teste teste teste teste teste teste teste teste teste teste teste teste teste teste teste teste teste ", "05-12-2018", true);
        //comunicados.add(comunicado1);
        //comunicados.add(comunicado2);
        //comunicados.add(comunicado3);
        //comunicados.add(comunicado4);
        //comunicados.add(comunicado5);
        //comunicados.add(comunicado6);
        //comunicados.add(comunicado7);

        if(comunicados.size() == 0){

            txtAviso.setVisibility(View.VISIBLE);
        }
        else{

            listaComunicados.setVisibility(View.VISIBLE);

            comunicadoAdapter = new ComunicadoAdapter(comunicados, this);

            listaComunicados.setAdapter(comunicadoAdapter);

            atualizarComunicadosVistos();
        }
    }

    private void atualizarComunicadosVistos() {

        for(int i = 0; i < comunicados.size(); i++){

            if(!comunicados.get(i).isVisualizado()){

                comunicadoBdCrud.atualizarComunicadoVisto(comunicados.get(i).getCdComunicado());

                comunicados.get(i).setVisualizado(true);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return true;
    }
}
