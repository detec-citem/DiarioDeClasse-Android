package br.gov.sp.educacao.sed.mobile.Avaliacao;

import android.app.Dialog;

import android.widget.Button;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import android.text.Editable;
import android.text.TextWatcher;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.content.Context;
import android.content.res.Resources;
import android.content.DialogInterface;

import br.gov.sp.educacao.sed.mobile.R;

import android.support.v7.app.AlertDialog;

class DialogNovaAvaliacaoViewMvcImpl
        implements DialogNovaAvaliacaoViewMvc {

    private Spinner spTipo;

    private View mRootView;

    private CheckBox ckVale;

    private EditText mEdNome;

    private Listener listener;

    private Dialog mRootDialog;

    private final String SIMBOLO = "º";

    private TextView mTvDiaAlert, tvBimestreAtual;

    public final String TAG = "DialogNovaAvaliacao";

    DialogNovaAvaliacaoViewMvcImpl(LayoutInflater layoutInflater, ViewGroup parent, boolean editar) {

        mRootView = layoutInflater.inflate(R.layout.alert_avaliacao , parent, false);

        mRootDialog = inicializarDialogCriarEditarAvaliacao(mRootView, editar);

        tvBimestreAtual = findViewById(R.id.tv_bimestre_atual);

        spTipo = findViewById(R.id.spTipo);

        ckVale = findViewById(R.id.ckVale);

        mTvDiaAlert = findViewById(R.id.tv_dia_alert);

        mTvDiaAlert.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                listener.inicializarCalendario();
            }
        });

        mEdNome = findViewById(R.id.ed_nome);

        mEdNome.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s,
                                          int start,
                                          int count,
                                          int after) {}

            @Override
            public void onTextChanged(CharSequence s,
                                      int start,
                                      int before,
                                      int count) {

                mEdNome.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    //Mudar Nome
    private void ativarBotao() {

        listener.ativarBotao();
    }

    @Override
    public void removerDialog() {

        getRootView().dismiss();
    }

    @Override
    public Dialog getRootView() {

        /*Animation animation;

        animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide);

        Interpolator interpolator = new LinearOutSlowInInterpolator();

        animation.setInterpolator(interpolator);

        mRootView.startAnimation(animation);*/

        return mRootDialog;
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

    private boolean validarCamposDialogAvaliacao(boolean editar) {

        boolean validado = false;

        if(validarNomeAvaliacao(mEdNome)
                && validarTipoAtividadeAvaliacao(spTipo)
                && validarDataAvaliacao(mTvDiaAlert)) {

            validado = true;

            String nomeAvaliacao = mEdNome.getText().toString();

            String tipoAtividade = spTipo.getSelectedItem().toString();

            String dataAvaliacao = mTvDiaAlert.getText().toString();

            boolean selecionado = ckVale.isChecked();

            if(editar) {

                listener.usuarioQuerEditarAvaliacao(nomeAvaliacao, dataAvaliacao, tipoAtividade , selecionado);
            }
            else {

                listener.usuarioQuerSalvarAvaliacao(nomeAvaliacao, dataAvaliacao, tipoAtividade, selecionado);
            }
        }

        return validado;
    }

    private boolean validarNomeAvaliacao(EditText nome) {

        if (nome.getText().toString().length() > 0) {

            return true;
        }

        Toast.makeText(getContext(), getContext().getResources().getString(R.string.complete_campos), Toast.LENGTH_SHORT).show();

        nome.setError(getContext().getResources().getString(R.string.obrigatorio));

        return false;
    }

    private boolean validarDataAvaliacao(TextView dataAvaliacao) {

        if(!dataAvaliacao.getText().equals("") && !dataAvaliacao.getText().equals("DD/MM/AAAA")) {

            return true;
        }

        Toast.makeText(getContext(), getContext().getResources().getString(R.string.hintdata), Toast.LENGTH_SHORT).show();

        return false;
    }

    private boolean validarTipoAtividadeAvaliacao(Spinner tipoAtividade) {

        if (tipoAtividade.getSelectedItemPosition() > 0) {

            return true;
        }

        Toast.makeText(getContext(), getContext().getResources().getString(R.string.selecione_tipo), Toast.LENGTH_SHORT).show();

        return false;
    }

    @Override
    public void usuarioSelecionouData(String data, String bimestre) {

        if(mTvDiaAlert != null) {

            mTvDiaAlert.setText(data);

            String tvBimestreAtualTxt = bimestre + SIMBOLO;

            tvBimestreAtual.setText(tvBimestreAtualTxt);
        }
    }

    private AlertDialog inicializarDialogCriarEditarAvaliacao(View view, final boolean editar) {

        final AlertDialog dialogAvaliacao;

        Resources resources = getContext().getResources();

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AppTheme_Dialog_Alert);

        builder.setTitle(editar ? resources.getString(R.string.edit_ava) : resources.getString(R.string.nova_ava))
                .setView(view)
                .setPositiveButton(editar ? R.string.ok : R.string.cadastrar, null)
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {

                                ativarBotao();

                                dialog.cancel();
                            }
                        });

        dialogAvaliacao = builder.create();

        dialogAvaliacao.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = dialogAvaliacao.getButton(AlertDialog.BUTTON_POSITIVE);

                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        ativarBotao();

                        if(validarCamposDialogAvaliacao(editar)) {

                            dialogAvaliacao.dismiss();
                        }
                    }
                });
            }
        });

        dialogAvaliacao.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);

        dialogAvaliacao.setCanceledOnTouchOutside(false);

        dialogAvaliacao.show();

        return dialogAvaliacao;
    }

    @Override
    public void exibirDadosAvaliacao(String nomeAvaliacao, String dataAvaliacao, boolean valeNota, int tipoAtividade) {

        mEdNome.setText(nomeAvaliacao);

        mTvDiaAlert.setText(dataAvaliacao);

        ckVale.setChecked(valeNota);

        spTipo.setSelection(tipoAtividade);
    }
}
