package br.gov.sp.educacao.sed.mobile.Avaliacao;

import android.content.Context;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.gov.sp.educacao.sed.mobile.R;

public class ListaAvaliacoesItemViewMvcImpl
        implements ListaAvaliacoesItemViewMvc {

    private TextView tvData;

    private TextView tvTipo;

    private TextView tvNome;

    private Listener listener;

    private ImageView ivEditar;

    private Avaliacao avaliacao;

    private final View mRootView;

    public ImageView btnDeletarAval;

    @SuppressWarnings("FieldCanBeLocal")
    private LinearLayout itemAvaliacao;

    private String TAG = ListaAvaliacoesItemViewMvcImpl.class.getSimpleName();

    ListaAvaliacoesItemViewMvcImpl(LayoutInflater layoutInflater, ViewGroup parent) {

        mRootView = layoutInflater.inflate(R.layout.layout_avaliacao, parent, false);

        tvNome = findViewById(R.id.horario);

        tvTipo = findViewById(R.id.tv_tipo);

        tvData = findViewById(R.id.tv_data);

        ivEditar = findViewById(R.id.btnEditarFrequencia);

        itemAvaliacao = findViewById(R.id.lay_avaliacao);

        ivEditar.setVisibility(View.INVISIBLE);

        btnDeletarAval = findViewById(R.id.btnExcluirFrequencia);

        btnDeletarAval.setVisibility(View.INVISIBLE);

        getRootView().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                getRootView().setClickable(false);

                getRootView().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                       getRootView().setClickable(true);

                    }
                }, 300);

                listener.onAvaliacaoSelecionada(avaliacao);
            }
        });

        ivEditar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                listener.usuarioQuerEditarAvaliacao(avaliacao);
            }
        });

        btnDeletarAval.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                listener.usuarioQuerDeletarAvaliacao(avaliacao);
            }
        });
    }

    @Override
    public void comecaai() {

        Animation animation;

        animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide);

        final Interpolator interpolator = new LinearOutSlowInInterpolator();

        animation.setInterpolator(interpolator);

        Animation animation1;

        animation1 = AnimationUtils.loadAnimation(getContext(), R.anim.slide);

        final Interpolator interpolator1 = new LinearOutSlowInInterpolator();

        animation1.setInterpolator(interpolator1);

        animation.setStartOffset(450);

        animation1.setStartOffset(220);

        ivEditar.startAnimation(animation);

        btnDeletarAval.startAnimation(animation1);

        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

                ivEditar.setVisibility(View.VISIBLE);

                btnDeletarAval.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public View getRootView() {

        return mRootView;
    }

    private Context getContext() {

        return mRootView.getContext();
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
    public void exibirInfoAvaliacao(Avaliacao avaliacao) {

        this.avaliacao = avaliacao;

        tvNome.setText(avaliacao.getNome());

        tvTipo.setText(converterCodigoTipoAtividade(avaliacao.getTipoAtividade()));

        tvData.setText(avaliacao.getData());
    }

    private String converterCodigoTipoAtividade(int tipoAtividade) {

        switch (tipoAtividade) {

            case Avaliacao.CODIGO_TIPO_AVALIACAO:
                return Avaliacao.TIPO_AVALIACAO;

            case Avaliacao.CODIGO_TIPO_ATIVIDADE:
                return Avaliacao.TIPO_ATIVIDADE;

            case Avaliacao.CODIGO_TIPO_TRABALHO:
                return Avaliacao.TIPO_TRABALHO;

            case Avaliacao.CODIGO_TIPO_OUTROS:
                return Avaliacao.TIPO_OUTROS;

            default:
                return "";
        }
    }
}
