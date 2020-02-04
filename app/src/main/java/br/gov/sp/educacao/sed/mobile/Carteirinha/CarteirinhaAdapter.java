package br.gov.sp.educacao.sed.mobile.Carteirinha;

import android.content.Context;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class CarteirinhaAdapter
        extends RecyclerView.Adapter<CarteirinhaAdapter.MyViewHolder>
         implements ListaCarteirinhasItemViewMvc.Listener {

    private LayoutInflater layoutInflater;

    private List<DadosCarteirinha> listaCarteirinhas;

    private RoundedBitmapDrawable roundedBitmapDrawable;

    private RoundedBitmapDrawable roundedBitmapDrawable1;

    private OnCarteirinhaSelecionadaListener onCarteirinhaSelecionada;

    public interface OnCarteirinhaSelecionadaListener {

        void onCarteirinhaSelecionada(DadosCarteirinha dadosCarteirinha, View view1, View view2);
    }

    public CarteirinhaAdapter(

            Context context, List<DadosCarteirinha> listaCarteirinhas, RoundedBitmapDrawable r1, RoundedBitmapDrawable r2, OnCarteirinhaSelecionadaListener onCarteirinhaSelecionada
    ) {

        layoutInflater = LayoutInflater.from(context);

        this.listaCarteirinhas = listaCarteirinhas;

        roundedBitmapDrawable = r1;

        roundedBitmapDrawable1 = r2;

        this.onCarteirinhaSelecionada = onCarteirinhaSelecionada;
    }

    @Override
    public CarteirinhaAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ListaCarteirinhasItemViewMvc viewMvc = new CarteirinhasListaItemViewMvcImpl(layoutInflater, parent, roundedBitmapDrawable, roundedBitmapDrawable1);

        viewMvc.registerListener(this);

        return new MyViewHolder(viewMvc);
    }

    @Override
    public void onBindViewHolder(CarteirinhaAdapter.MyViewHolder holder, int position) {

        holder.mViewMvc.setarCarteirinha(listaCarteirinhas.get(position), holder);
    }

    @Override
    public int getItemCount() {

        return listaCarteirinhas.size();
    }

    @Override
    public void onCarteirinhaSelecionada(DadosCarteirinha dadosCarteirinha, View view1, View view2) {

        onCarteirinhaSelecionada.onCarteirinhaSelecionada(dadosCarteirinha, view1, view2);
    }

   class MyViewHolder
           extends RecyclerView.ViewHolder {

        private ListaCarteirinhasItemViewMvc mViewMvc;

        public MyViewHolder(ListaCarteirinhasItemViewMvc itemView) {

            super(itemView.getRootView());

            mViewMvc = itemView;
        }
   }
}
