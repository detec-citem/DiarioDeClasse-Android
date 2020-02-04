package br.gov.sp.educacao.sed.mobile.RegistroDeAula;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import br.gov.sp.educacao.sed.mobile.Menu.ConteudoFundamental;
import br.gov.sp.educacao.sed.mobile.R;

public class HabilidadeFundamentalViewHolder {
    //Variáveis
    private int posicao;
    private CheckBox checkBox;
    private TextView conteudoTextView;
    private View view;

    //Construtor
    public HabilidadeFundamentalViewHolder(LayoutInflater layoutInflater, ViewGroup parent) {
        view = layoutInflater.inflate(R.layout.layout_lista_conteudo, parent, false);
        conteudoTextView = view.findViewById(R.id.txt_layout_conteudo);
        checkBox = view.findViewById(R.id.check_layout_conteudo);
    }

    //Métodos
    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public View getView() {
        return view;
    }

    public void configurarCelula(int posicao, ConteudoFundamental habilidade) {
        this.posicao = posicao;
        checkBox.setChecked(habilidade.getChecado());
        conteudoTextView.setText(habilidade.getDescricao());
    }
}