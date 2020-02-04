package br.gov.sp.educacao.sed.mobile.Fechamento;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;

import br.gov.sp.educacao.sed.mobile.R;
import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;

class MediaAdapterItemViewMvcImpl
        implements MediaAdapterItemViewMvc {

    private double media;

    private TextView tvAluno;
    private TextView tvMedia;

    private Listener listener;

    private ImageView imgUp;
    private ImageView imgDown;
    private ImageView imgReplay;

    private final View mRootView;

    private LayoutInflater layoutInflater;

    private final String TIPO_ARREDONDAMENTO_MAIS = "up";

    private final String TIPO_ARREDONDAMENTO_MENOS = "down";

    private final String TIPO_ARREDONDAMENTO_VAZIO = "empty";

    private SparseArray<Double> notasReplay = new SparseArray<>();

    MediaAdapterItemViewMvcImpl(LayoutInflater layoutInflater, ViewGroup parent) {

        mRootView = layoutInflater.inflate(R.layout.item_avaliacao_aluno_media, parent, false);

        this.layoutInflater = layoutInflater;

        imgUp = findViewById(R.id.img_up);
        tvAluno = findViewById(R.id.tv_aluno);
        tvMedia = findViewById(R.id.tv_media);
        imgDown = findViewById(R.id.img_down);
        imgReplay = findViewById(R.id.img_replay);
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

    public void exibirMedias(final Aluno aluno) {

        getArrowEnable(aluno.getItemTipoArredondamento());

        String tvAlunoText = aluno.getNumeroChamada() + " - " + aluno.getNomeAluno();

        tvAluno.setText(tvAlunoText);

        if(aluno.getMedia() != 11) {

            media = aluno.getMedia();

            tvMedia.setText(String.valueOf(media));
        }
        else {

            tvMedia.setText("S/N");
            imgUp.setEnabled(false);
            imgDown.setEnabled(false);
            imgReplay.setEnabled(false);
        }

        imgUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int matricula = aluno.getCodigoAluno();

                media = aluno.getMedia();

                notasReplay.put(matricula, media);

                BigDecimal bd = new BigDecimal(media);

                int valorFinal;

                if(media <= 10) {

                    if(bd.remainder(BigDecimal.ONE).doubleValue() <= 0.5) {

                        double result = Math.ceil(bd.doubleValue());

                        valorFinal = (int)result;

                        aluno.setMedia(result);
                    }
                    else {

                        bd = bd.setScale(0, BigDecimal.ROUND_UP);

                        valorFinal = bd.intValue();

                        aluno.setMedia((double)valorFinal);
                    }
                }
                else {

                    valorFinal = 10;

                    aluno.setMedia(10.0);
                }

                tvMedia.setText(String.valueOf(valorFinal));

                aluno.setItemTipoArredondamento(TIPO_ARREDONDAMENTO_MAIS);

                getArrowEnable(aluno.getItemTipoArredondamento());

                listener.atualizarLista();
            }
        });

        imgDown.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int matricula = aluno.getCodigoAluno();

                media = aluno.getMedia();

                notasReplay.put(matricula, media);

                BigDecimal bd = new BigDecimal(media);

                int valorFinal;

                if(media <= 10) {

                    if(bd.remainder(BigDecimal.ONE).doubleValue() >= 0.5) {

                        double result = Math.floor(bd.doubleValue());

                        valorFinal = (int)result;

                        aluno.setMedia(result);
                    }
                    else {

                        bd = bd.setScale(0, BigDecimal.ROUND_DOWN);

                        valorFinal = bd.intValue();

                        aluno.setMedia((double)valorFinal);
                    }
                }
                else {

                    valorFinal = 10;

                    aluno.setMedia(10.0);
                }
                tvMedia.setText("Sem media");

                tvMedia.setText(String.valueOf(valorFinal));

                aluno.setItemTipoArredondamento(TIPO_ARREDONDAMENTO_MENOS);

                getArrowEnable(aluno.getItemTipoArredondamento());

                listener.atualizarLista();
            }
        });

        imgReplay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                double notaAnt = 0D;

                if(notasReplay.get(aluno.getCodigoAluno()) != null) {

                    notaAnt = notasReplay.get(aluno.getCodigoAluno());

                    aluno.setMedia(notaAnt);
                }

                imgUp.setEnabled(true);
                imgDown.setEnabled(true);
                imgUp.setImageResource(R.drawable.icon_arrow_up);
                imgDown.setImageResource(R.drawable.icon_arrow_down);

                tvMedia.setText(String.valueOf(notaAnt));

                aluno.setItemTipoArredondamento(TIPO_ARREDONDAMENTO_VAZIO);

                getArrowEnable(aluno.getItemTipoArredondamento());

                listener.atualizarLista();
            }
        });
    }

    @Override
    public void registerListener(Listener listener) {

        this.listener = listener;
    }

    private <T extends  View> T findViewById(int id) {

        return getRootView().findViewById(id);
    }

    private void getArrowEnable(String arredondamento) {

        if(arredondamento.equals(TIPO_ARREDONDAMENTO_MENOS)) {

            imgUp.setEnabled(false);
            imgReplay.setEnabled(true);
            imgDown.setEnabled(false);
            imgUp.setImageResource(R.drawable.icon_arrow_up2);
            imgDown.setImageResource(R.drawable.icon_arrow_down);
        }
        else if(arredondamento.equals(TIPO_ARREDONDAMENTO_MAIS)) {

            imgDown.setEnabled(false);
            imgReplay.setEnabled(true);
            imgUp.setEnabled(false);
            imgDown.setImageResource(R.drawable.icon_arrow_down2);
            imgUp.setImageResource(R.drawable.icon_arrow_up);
        }
        else if(arredondamento.equals(TIPO_ARREDONDAMENTO_VAZIO)) {

            imgUp.setEnabled(true);
            imgUp.setImageResource(R.drawable.icon_arrow_up);
            imgDown.setEnabled(true);
            imgDown.setImageResource(R.drawable.icon_arrow_down);
        }
    }
}
