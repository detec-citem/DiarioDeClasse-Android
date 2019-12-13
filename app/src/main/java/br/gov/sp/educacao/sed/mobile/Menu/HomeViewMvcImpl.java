package br.gov.sp.educacao.sed.mobile.Menu;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import org.joda.time.LocalDate;

import android.widget.Toast;
import android.widget.Button;
import android.widget.TextView;
import android.widget.FrameLayout;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.DialogInterface;

import br.gov.sp.educacao.sed.mobile.Carteirinha.RevalidarTokenInterface;
import br.gov.sp.educacao.sed.mobile.R;

import android.support.v7.widget.Toolbar;
import android.support.v7.app.AlertDialog;

import android.support.annotation.Nullable;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.constraint.ConstraintLayout;

public class HomeViewMvcImpl
        implements HomeViewMvc, RevalidarTokenInterface {

    private TextView menu;

    private Toolbar toolbar;

    private Listener listener;

    private boolean menuAberto;

    private TextView statusView;

    private final View mRootView;

    private TextView nomeUsuario;

    private MenuLateral menuLateral;

    private FrameLayout frameLayout;

    private Button botaoSincronizar;

    private LayoutInflater layoutInflater;

    private ToolbarViewMvc toolbarViewMvc;

    private AlertDialog avisoSincronizando;

    private ConstraintLayout homeContainer;

    private FragmentManager fragmentManager;

    private TextView fechamento, avaliacao, turmas, registroDeAula, frequencia, carteirinha, comunicado;

    HomeViewMvcImpl(LayoutInflater layoutInflater, final FragmentManager fragmentManager, boolean telaPequena, ViewGroup parent) {

        if(telaPequena) {

            mRootView = layoutInflater.inflate(R.layout.activity_home_tela_pequena, parent, false);
        }
        else {

            mRootView = layoutInflater.inflate(R.layout.activity_home, parent, false);
        }

        toolbar = findViewById(R.id.toolbar);

        nomeUsuario = findViewById(R.id.txt_nomeUsuario);

        this.layoutInflater = layoutInflater;

        this.fragmentManager = fragmentManager;

        homeContainer = findViewById(R.id.homeContainer);

        toolbarViewMvc = getToolbarViewMvc(toolbar);

        initToolbar();

        menu = toolbarViewMvc.getMenu();

        menu.setVisibility(View.VISIBLE);

        menuAberto = false;

        frameLayout = findViewById(R.id.container);

        fechamento = findViewById(R.id.tv_fechamento);

        avaliacao = findViewById(R.id.tv_avaliacao);

        turmas = findViewById(R.id.tv_turmas);

        carteirinha = findViewById(R.id.tv_carteirinha);

        registroDeAula = findViewById(R.id.tv_registroAula);

        frequencia = findViewById(R.id.tv_frequencia);

        comunicado = findViewById(R.id.tv_comunicados);

        botaoSincronizar = findViewById(R.id.bt_sincronizar);

        inicializarListener();
    }

    void ativarBotoes() {

        turmas.setClickable(true);

        carteirinha.setClickable(true);

        registroDeAula.setClickable(true);

        frequencia.setClickable(true);

        avaliacao.setClickable(true);

        fechamento.setClickable(true);
    }

    @Override
    public View getRootView() {

        return mRootView;
    }

    private void initToolbar() {

        toolbarViewMvc.setTitle("Di@rio de Classe");

        toolbar.addView(toolbarViewMvc.getRootView());
    }

    private Context getContext() {

        return getRootView().getContext();
    }

    private void exibirMenuLateral() {

        if(menuLateral == null) {

            menuLateral = new MenuLateral();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.setCustomAnimations(R.anim.move2, R.anim.slideback);

            fragmentTransaction.add(R.id.container, menuLateral);

            fragmentTransaction.commit();
        }
        else  {

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.setCustomAnimations(R.anim.move2, R.anim.slideback);

            fragmentTransaction.show(menuLateral);

            fragmentTransaction.commit();
        }
    }

    @Override
    public void unregisterListener() {

        this.listener = null;
    }

    private void removerMenuLateral() {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.hide(menuLateral);

        fragmentTransaction.commit();
    }

    @Override
    public void usuarioAvisoSemWiFi() {

        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle("Acesso à Internet")
                .setMessage("O aparelho não está conectado a uma rede sem fio (Wi-Fi). " +
                        "Aceita utilizar sua internet móvel (3G, 4G)? \n" +
                        "Importante: ao clicar em SIM, ACEITO, declara estar ciente " +
                        "de que a utilização poderá acarretar em cobranças adicionais de sua operadora de telefonia móvel.")

                .setPositiveButton("SIM, ACEITO", new DialogInterface.OnClickListener() {

                    @Override

                    public void onClick(DialogInterface dialog, int which) {

                        listener.usuarioAceitaUsarPlanoDeDados();
                    }
                })
                .setNeutralButton("NÃO, VOLTAR", new DialogInterface.OnClickListener() {

                    @Override

                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(getContext(), (R.string.conecte_wifi), Toast.LENGTH_SHORT).show();
                    }
                })
                .setCancelable(false)
                .create();

        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);

        alertDialog.show();
    }

    private void inicializarListener() {

        homeContainer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(frameLayout.getVisibility() == View.VISIBLE) {

                    frameLayout.setVisibility(View.INVISIBLE);

                    removerMenuLateral();

                    menuAberto = false;
                }
            }
        });

        botaoSincronizar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                listener.usuarioClicouBotaoSincronizar();
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(menuAberto) {

                    frameLayout.setVisibility(View.INVISIBLE);

                    removerMenuLateral();

                    menuAberto = false;
                }
                else {

                    frameLayout.setVisibility(View.VISIBLE);

                    exibirMenuLateral();

                    menuAberto = true;
                }
            }
        });
    }

    void exibirNomeUsuario(String nome) {

        nomeUsuario.setText(nome);
    }

    @Override
    public void exibirFalhaNaSincronizacao() {

        criarDialogDeAlerta(R.string.erro_turmas, "Não foi possível sincronizar");
    }

    @Override
    public void completouEtapaSincronizacao() {

        listener.completouEtapaSincronizacao();
    }

    @Override
    public void finalizaProgressoSincronizar() {

        if(avisoSincronizando != null){

            avisoSincronizando.dismiss();
        }
    }

    @Override
    public void exibirSincronizacaoRealizada() {

        criarDialogDeAlerta(R.string.sincronizacao_realizada, "Sucesso");
    }

    @Override
    public void revalidacaoDeToken(String token) {

        listener.atualizarToken(token);
    }

    @Override
    public void inicializaProgressoSincronizar() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(

                getContext(), R.style.ThemeOverlay_AppCompat_Dialog
        );

        View view = layoutInflater.inflate(R.layout.dialogsincronizar, null, false);

        builder.setView(view);

        statusView = view.getRootView().findViewById(R.id.status);

        avisoSincronizando = builder.create();

        avisoSincronizando.setCancelable(false);

        avisoSincronizando.setCanceledOnTouchOutside(false);

        avisoSincronizando.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);

        avisoSincronizando.show();

        atualizarProgressoSincronizar(1);
    }

    @Override
    public void selecaoMenuLateral(View menuItem) {

        int id = menuItem.getId();

        switch (id) {

            case R.id.compartilhar:

                listener.usuarioSelecionouMenuLateral("COMPARTILHAR");

                break;

            case R.id.avaliar:

                listener.usuarioSelecionouMenuLateral("AVALIAR");

                break;

            case R.id.sincronizar:

                listener.usuarioSelecionouMenuLateral("SINCRONIZAR");

                break;

            case R.id.sobre:

                listener.usuarioSelecionouMenuLateral("SOBRE");

                break;

            case R.id.sair:

                listener.usuarioSelecionouMenuLateral("LOGOUT");

                break;


            case R.id.tabelas:

                listener.usuarioSelecionouMenuLateral("TABELAS");

                break;

        }
    }

    @Override
    public void selecaoMenuPrincipal(View modulo) {

        if(menuLateral != null && menuLateral.isVisible()) {

            removerMenuLateral();
        }

        switch (modulo.getId()) {

            case R.id.tv_carteirinha:

                carteirinha.setClickable(false);

                listener.usuarioSelecionouMenuPrincipal("CARTEIRINHA");

                break;

            case R.id.tv_turmas:

                turmas.setClickable(false);

                listener.usuarioSelecionouMenuPrincipal("TURMAS");

                break;

            case R.id.tv_registroAula:

                registroDeAula.setClickable(false);

                listener.usuarioSelecionouMenuPrincipal("REGISTROAULA");

                break;

            case R.id.tv_frequencia:

                frequencia.setClickable(false);

                listener.usuarioSelecionouMenuPrincipal("FREQUENCIA");

                break;

            case R.id.tv_avaliacao:

                avaliacao.setClickable(false);

                listener.usuarioSelecionouMenuPrincipal("AVALIACAO");

                break;

            case R.id.tv_fechamento:

                //fechamento.setClickable(false);

                listener.usuarioSelecionouMenuPrincipal("FECHAMENTO");

                break;

            case R.id.tv_comunicados:

                //comunicado.setClickable(false);

                listener.usuarioSelecionouMenuPrincipal("COMUNICADOS");

                break;
        }
    }

    @Override
    public void exibirDadosIncompletosNoServidor() {

        criarDialogDeAlertaNaoCancelavel(R.string.dados_incompletos_no_servidor);
    }

    private <T extends View> T findViewById(int id) {

        return getRootView().findViewById(id);
    }

    public void perfilSelecionado(boolean perfilOk) {

        listener.perfilSelecionado(perfilOk);
    }

    @Override
    public void usuarioAvisoDadosNaoSincronizados() {

        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.atencao)
                .setMessage(R.string.logoff_with_unsaved_data_warning_msg)
                .setPositiveButton("Sair mesmo assim", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        listener.usuarioQuerSairSemSincronizar();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(false)
                .create();

        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);

        alertDialog.show();
    }

    @Override
    public void registerListener(Listener listener) {

        this.listener = listener;
    }

    @Override
    public void frequenciasResultadoSincronizacao(List<JSONObject> listaResultadoFrequencia) {

        listener.alterarFrequencias();

        for(int i = 0; i < listaResultadoFrequencia.size(); i++) {

            try {

                if(listaResultadoFrequencia.get(i).getInt("Status") == 409) {

                    for(int j = 0; j < listaResultadoFrequencia.get(i).getJSONArray("DiasComConflito").length(); j++) {

                        if(listaResultadoFrequencia.get(i).getJSONArray("DiasComConflito").getJSONObject(j).getJSONArray("HorariosComConflito").length() > 0) {

                            for(int k = 0; k < listaResultadoFrequencia.get(i).getJSONArray("DiasComConflito").getJSONObject(j).getJSONArray("HorariosComConflito").length(); k++) {

                                String dia = listaResultadoFrequencia.get(i).getJSONArray("DiasComConflito").getJSONObject(j).getString("Dia");

                                String horario = listaResultadoFrequencia.get(i).getJSONArray("DiasComConflito").getJSONObject(j).getJSONArray("HorariosComConflito").getJSONObject(k).getString("Horario");

                                String turma = String.valueOf(listaResultadoFrequencia.get(i).getInt("Turma"));

                                String disciplina = String.valueOf(listaResultadoFrequencia.get(i).getInt("Disciplina"));

                                listener.salvarFrequenciaComConflito(dia, horario, turma, disciplina);
                            }
                        }
                    }
                }
            }
            catch(Exception e) {

                e.printStackTrace();
            }
        }

        listener.resolverConflitos();
    }

    void atualizarProgressoSincronizar(int progresso) {

        int delay = 200;

        if(progresso == 1) {

            for(int i = 0; i <= 80 + 1; i++) {

                final int porcentagem = i;

                new android.os.Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        statusView.setText(String.valueOf(porcentagem) + " %");
                    }
                }, (delay * (i * 2) ));
            }
        }
        else {

            for(int i = 81; i <= 101 + 1; i++) {

                final int porcentagem = i;

                delay = 300;

                new android.os.Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        statusView.setText(String.valueOf(porcentagem) + " %");

                        if(porcentagem > 100) {

                            listener.completouSincronizacao();
                        }
                    }
                }, (delay * i));
            }
        }
    }

    @Override
    public void terminouSincronizacao(boolean success) {

        listener.terminouSincronizacao(success);
    }

    @Override
    public void sobreAplicativo(PackageInfo packageInfo) {

        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle("Sobre")
                .setMessage("Di@rio de Classe versão " + packageInfo.versionName)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                })
                .create();

        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);

        alertDialog.show();
    }

    @Override
    public void deletarAvaliacaoNoBancoLocal(int codigoAvaliacao) {

        listener.deletarAvaliacaoNoBancoLocal(codigoAvaliacao);
    }

    @Override
    public void usuarioAvisoFechamentoIndisponivel(LocalDate data) {

        fechamento.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_fechamento_disabled,0,0);

        Toast.makeText(getContext(),"Aguarde " + data.toString("dd/MM/yyyy"), Toast.LENGTH_SHORT).show();
    }

    private void criarDialogDeAlertaNaoCancelavel(int messageStringId) {

        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.atencao)
                .setMessage(messageStringId)
                .setNeutralButton(R.string.tentar_novamente, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(false)
                .create();

        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);

        alertDialog.show();
    }

    @Override
    public ToolbarViewMvc getToolbarViewMvc(@Nullable ViewGroup parent) {

        return new ToolbarViewMvc(layoutInflater, parent);
    }

    private void criarDialogDeAlerta(int messageStringId, String title) {

        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setCancelable(false)
                .setTitle(title)
                .setMessage(messageStringId)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                        listener.verificarSeExistemHorariosComConflito();
                    }
                }).create();

        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);

        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.show();
    }

    public void avisoUsuarioHorariosConflito(List<String> listaMensagens) {

        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                })
                .setTitle("Horários com conflito").create();

        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < listaMensagens.size(); i++) {

            stringBuilder.append(listaMensagens.get(i) + "\n" + "\n");
        }

        alertDialog.setMessage(stringBuilder);

        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);

        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.show();
    }

    @Override
    public void avaliacoesResultadoSincronizacao(List<JSONObject> lista) {

        listener.alterarAvaliacoes(lista);
    }
}
