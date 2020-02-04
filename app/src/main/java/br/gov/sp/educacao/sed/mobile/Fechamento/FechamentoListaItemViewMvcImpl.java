package br.gov.sp.educacao.sed.mobile.Fechamento;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import br.gov.sp.educacao.sed.mobile.R;
import br.gov.sp.educacao.sed.mobile.util.Utils;

class FechamentoListaItemViewMvcImpl
        implements FechamentoListaItemViewMvc {

    private TextView tvNota;

    private TextView tvAluno;

    private TextView tvFalta;

    private Listener listener;

    private final View mRootView;

    private ImageView imgConfirmado;

    private ImageView imgDesabilitado;

    private TextView tvFaltasAcumuladas;

    private TextView tvAusenciasCompensadas;

    FechamentoListaItemViewMvcImpl(LayoutInflater layoutInflater, ViewGroup parent) {

        mRootView = layoutInflater.inflate(R.layout.item_aluno_fechamento, parent, false);

        tvAluno = findViewById(R.id.tv_aluno);

        tvNota = findViewById(R.id.textView5);

        tvFalta = findViewById(R.id.textView8);

        tvAusenciasCompensadas = findViewById(R.id.textView6);

        tvFaltasAcumuladas = findViewById(R.id.textView7);

        imgConfirmado = findViewById(R.id.img_confirmado);

        imgDesabilitado = findViewById(R.id.img_desabilitado);
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

    @Override
    public void exibirFechamentoAluno(FechamentoAluno fechamentoAluno) {

        tvAluno.setText(fechamentoAluno.getNomeAluno());

        if(fechamentoAluno != null) {

            if (fechamentoAluno.getNota() == Utils.ALUNO_INATIVO_SEM_NOTA
                    || fechamentoAluno.getNota() == Utils.ALUNO_ATIVO_SEM_NOTA) {

                tvNota.setText("Nota: S/N");
            }
            else {

                tvNota.setText(String.valueOf(fechamentoAluno.getNota()));

                tvNota.setText("Nota: " + String.valueOf(fechamentoAluno.getNota()));
            }

            tvAusenciasCompensadas.setText("A/C: " + fechamentoAluno.getAusenciasCompensadas());

            tvFaltasAcumuladas.setText("F/A: " + fechamentoAluno.getFaltasAcumuladas());

            tvFalta.setText("Faltas: " + fechamentoAluno.getFaltas());

            if(fechamentoAluno.isConfirmado()) {

                imgConfirmado.setVisibility(View.VISIBLE);
                imgDesabilitado.setVisibility(View.GONE);
            }
            else {

                imgConfirmado.setVisibility(View.GONE);
                imgDesabilitado.setVisibility(View.VISIBLE);
            }
        }
        else {

            if(fechamentoAluno == null) {

                tvNota.setText("Nota: S/N");

                tvAusenciasCompensadas.setText("A/C: --");

                tvFalta.setText("Faltas: --");

                tvFaltasAcumuladas.setText("F/A: --");

                imgConfirmado.setVisibility(View.GONE);
                imgDesabilitado.setVisibility(View.GONE);
            }
            else {

                if(fechamentoAluno.getNota() == Utils.ALUNO_ATIVO_SEM_NOTA) {

                    tvNota.setText("Nota: S/N");
                }
                else {

                    tvNota.setText(String.valueOf(fechamentoAluno.getNota()));

                    tvNota.setText("Nota: " + String.valueOf(fechamentoAluno.getNota()));
                }

                imgConfirmado.setVisibility(View.GONE);
                imgDesabilitado.setVisibility(View.VISIBLE);
            }
        }
    }
}
