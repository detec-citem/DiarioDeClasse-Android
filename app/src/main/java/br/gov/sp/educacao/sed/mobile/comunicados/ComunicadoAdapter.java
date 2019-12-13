package br.gov.sp.educacao.sed.mobile.comunicados;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.gov.sp.educacao.sed.mobile.R;

public class ComunicadoAdapter extends BaseAdapter {

    private final ArrayList<Comunicado> comunicados;

    private final Activity act;

    public ComunicadoAdapter(ArrayList<Comunicado> comunicados, Activity act) {

        this.comunicados = comunicados;
        this.act = act;
    }

    @Override
    public int getCount() {

        return comunicados.size();
    }

    @Override
    public Object getItem(int i) {

        return comunicados.get(i);
    }

    @Override
    public long getItemId(int i) {

        return comunicados.get(i).getCdComunicado();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = act.getLayoutInflater().inflate(R.layout.layout_comunicado, parent, false);

        Comunicado comunicado = comunicados.get(position);

        TextView txtTitulo = view.findViewById(R.id.item_comunicado_titulo);
        TextView txtComunicado = view.findViewById(R.id.item_comunicado_texto);
        TextView txtData = view.findViewById(R.id.item_comunicado_data);

        txtTitulo.setText(comunicado.getTitulo());
        txtComunicado.setText(comunicado.getComunicado());
        txtData.setText(comunicado.getData());

        return view;
    }
}
