package br.gov.sp.educacao.sed.mobile.comunicados;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import br.gov.sp.educacao.sed.mobile.R;
import br.gov.sp.educacao.sed.mobile.util.CriarAcessoBanco;

public class ComunicadosActivity extends AppCompatActivity {
    //Variáveis
    private ArrayList<Comunicado> comunicados;

    //Lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunicados);
        Toolbar toolbar = findViewById(R.id.comunicado_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        boolean semComunicados;
        if (savedInstanceState == null) {
            ComunicadoBdCrud comunicadoBdCrud = new ComunicadoBdCrud(CriarAcessoBanco.gerarBanco(this));
            comunicados = comunicadoBdCrud.getTodosComunicados();
            int i;
            int numeroComunicados = comunicados.size();
            semComunicados = comunicados.isEmpty();
            if (!semComunicados) {
                for (i = 0; i < numeroComunicados; i++) {
                    Comunicado comunicado = comunicados.get(i);
                    if (!comunicado.isVisualizado()) {
                        comunicadoBdCrud.atualizarComunicadoVisto(comunicado.getCdComunicado());
                        comunicado.setVisualizado(true);
                    }
                }
            }
        }
        else {
            comunicados = savedInstanceState.getParcelableArrayList("comunicados");
            semComunicados = comunicados.isEmpty();
        }
        TextView avisoTextView = findViewById(R.id.comunicado_txt_aviso);
        ListView comunicadosListView = findViewById(R.id.comunicado_lista);
        if (semComunicados) {
            avisoTextView.setVisibility(View.VISIBLE);
            comunicadosListView.setVisibility(View.GONE);
        }
        else {
            avisoTextView.setVisibility(View.GONE);
            comunicadosListView.setVisibility(View.VISIBLE);
            ComunicadoAdapter comunicadoAdapter = new ComunicadoAdapter(comunicados, this);
            comunicadosListView.setAdapter(comunicadoAdapter);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("comunicados", comunicados);
        super.onSaveInstanceState(outState);
    }
}