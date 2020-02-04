package br.gov.sp.educacao.sed.mobile.Menu;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.gov.sp.educacao.sed.mobile.Avaliacao.AvaliacaoDBcrud;
import br.gov.sp.educacao.sed.mobile.Avaliacao.AvaliacaoDBgetters;
import br.gov.sp.educacao.sed.mobile.BuildConfig;
import br.gov.sp.educacao.sed.mobile.Carteirinha.ListaCarteirinhasActivity;
import br.gov.sp.educacao.sed.mobile.Fechamento.FechamentoActivity;
import br.gov.sp.educacao.sed.mobile.Fechamento.FechamentoData;
import br.gov.sp.educacao.sed.mobile.Frequencia.FrequenciaDBcrud;
import br.gov.sp.educacao.sed.mobile.Frequencia.FrequenciaDBgetters;
import br.gov.sp.educacao.sed.mobile.Frequencia.FrequenciaLancamentoActivity;
import br.gov.sp.educacao.sed.mobile.Login.LoginActivity;
import br.gov.sp.educacao.sed.mobile.Login.UsuarioTO;
import br.gov.sp.educacao.sed.mobile.R;
import br.gov.sp.educacao.sed.mobile.RegistroDeAula.RegistroAulaActivity;
import br.gov.sp.educacao.sed.mobile.RegistroDeAula.RegistroDBcrud;
import br.gov.sp.educacao.sed.mobile.RegistroDeAula.RegistroDBgetters;
import br.gov.sp.educacao.sed.mobile.Turmas.AlunosListaActivity;
import br.gov.sp.educacao.sed.mobile.Turmas.ResgatarTurmasAsyncTask;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmaDBgetters;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmaDBsetters;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmasListaActivity;
import br.gov.sp.educacao.sed.mobile.comunicados.Comunicado;
import br.gov.sp.educacao.sed.mobile.comunicados.ComunicadoAsyncTask;
import br.gov.sp.educacao.sed.mobile.comunicados.ComunicadoBdCrud;
import br.gov.sp.educacao.sed.mobile.comunicados.ComunicadoTO;
import br.gov.sp.educacao.sed.mobile.comunicados.ComunicadosActivity;
import br.gov.sp.educacao.sed.mobile.constants.EtapasSincronizacao;
import br.gov.sp.educacao.sed.mobile.tutorial.TutorialActivity;
import br.gov.sp.educacao.sed.mobile.util.ActivityTabelas;
import br.gov.sp.educacao.sed.mobile.util.AnalyticsApplication;
import br.gov.sp.educacao.sed.mobile.util.AvaliarApp.DialogAvaliarApp;
import br.gov.sp.educacao.sed.mobile.util.AvaliarApp.DialogAvaliarManager;
import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;
import br.gov.sp.educacao.sed.mobile.util.CriarAcessoBanco;
import br.gov.sp.educacao.sed.mobile.util.MyPreferences;
import br.gov.sp.educacao.sed.mobile.util.NetworkUtils;

public class HomeActivity extends AppCompatActivity implements HomeViewMvc.Listener {

    private AlertDialog dialogVersaoApp;

    public Banco banco;

    private Bundle bundle;

    private Intent intent;

    public String deviceId;

    public boolean buscouComunicado = false;

    public UsuarioTO usuario;

    private Intent selecaoTela;

    private ComunicadoBdCrud comunicadoBdCrud;

    private MenuDBcrud menuDBcrud;

    private HomeViewMvcImpl homeViewMvcImpl;

    private MenuDBgetters menuDBgetters;
    public TurmaDBgetters turmaDBgetters;
    public TurmaDBsetters turmaDBsetters;

    private FrequenciaDBcrud frequenciaDBcrud;
    public RegistroDBcrud registroDBcrud;
    public AvaliacaoDBcrud avaliacaoDBcrud;

    private FrequenciaDBgetters frequenciaDBgetters;
    public RegistroDBgetters registroDBgetters;
    public AvaliacaoDBgetters avaliacaoDBgetters;

    private MyPreferences myPreferences;

    private DeletarAvaliacoesTask deletarAvaliacoesTask;

    private Bundle savedInstanceState;

    private String TAG = HomeActivity.class.getSimpleName();

    private RevalidarTokenAsyncTask revalidarTokenAsyncTask;
    private ResgatarTurmasAsyncTask resgatarTurmasAsyncTask;
    private EnviarRegistrosAsyncTask enviarRegistrosAsyncTask;
    private SelecionarPerfilAsyncTask selecionarPerfilAsyncTask;
    private EnviarAvaliacoesAsyncTask enviarAvaliacoesAsyncTask;
    private EnviarFrequenciasAsyncTask enviarFrequenciasAsyncTask;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        homeViewMvcImpl = new HomeViewMvcImpl(LayoutInflater.from(this), getSupportFragmentManager(), checarTamanhoTela(), null, this);
        setContentView(homeViewMvcImpl.getRootView());

        myPreferences = new MyPreferences(this);

        inicializarBanco();

        bundle = new Bundle();
        intent = new Intent();
        selecaoTela = new Intent();

        Tracker tracker = ((AnalyticsApplication) getApplication()).getTracker(AnalyticsApplication.TrackerName.APP_TRACKER);
        tracker.enableAutoActivityTracking(true);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {

                deviceId = instanceIdResult.getToken();
            }
        });

        usuario = menuDBgetters.getUsuarioAtivo();

        if (usuario != null) {

            Crashlytics.setUserIdentifier(usuario.getUsuario());
        }
        else {

            Crashlytics.setUserIdentifier("null");
        }

        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);

        if (turmaDBgetters.getTurmas(ano, false).isEmpty()) {

            conferirConexao();
        }
        else {

            if(NetworkUtils.isNetworkAvailable(this)){

                new VersaoAppAsyncTask(this,this).execute(usuario.getToken());
            }
            else{

                int versaoAtual = myPreferences.versaoAtual();

                if (versaoAtual != 0) {

                    terminouRequisicaoVersaoDoApp(versaoAtual);
                }
                else {

                    try {
                        DialogAvaliarManager.showRateDialog(HomeActivity.this, savedInstanceState);
                    }
                    catch (Exception e) {
                        Crashlytics.logException(e);
                        CrashAnalytics.e(TAG, e);
                    }
                }
            }
        }

        try{

            homeViewMvcImpl.desabilitarBotao("avaliacao");

            if(true){ //!verificarFechamentoDisponivel()

                homeViewMvcImpl.desabilitarBotao("fechamento");
            }
        }
        catch (Exception e){

            e.printStackTrace();
        }
    }

    private boolean checarTamanhoTela() {

        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        int dpi = Resources.getSystem().getDisplayMetrics().densityDpi;

        boolean telaPequena = false;

        WindowManager windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);

        final Display display = windowManager.getDefaultDisplay();

        Point outPoint = new Point();

        if (Build.VERSION.SDK_INT >= 19) {

            display.getRealSize(outPoint);
        } else {

            display.getSize(outPoint);
        }

        if (height < 1000) {

            telaPequena = true;
        }

        Log.e(TAG, "Medidas - " + "Width: " + width + " Height: " + height + " Dpi: " + dpi);
        Log.e(TAG, "Medidas - " + "Height: " + outPoint.y);

        return telaPequena;
    }

    private void logOut() {

        menuDBcrud.limparTabelas();

        limparIntent(intent);

        intent.setClass(this, LoginActivity.class);

        startActivity(intent);

        finish();
    }

    @Override
    public void terminouRequisicaoRecado(JSONArray jsonRetorno) {

        ComunicadoTO comunicadoTO = new ComunicadoTO(jsonRetorno);

        ArrayList<Comunicado> comunicados;

        if (comunicadoTO.getQtd() > 0) {

            buscouComunicado = true;

            comunicados = comunicadoTO.getComunicadosFromJson();

            comunicadoBdCrud.salvarComunicadosBanco(comunicados);

            Comunicado comunicadoParaExibir = comunicadoBdCrud.getUltimoComunicadoNaoVisto();

            if (comunicadoParaExibir != null) {

                exibirComunicadoNaoVisto(comunicadoParaExibir);

                for (int i = 0; i < comunicados.size(); i++) {

                    comunicadoBdCrud.atualizarComunicadoVisto(comunicados.get(i).getCdComunicado());
                }
            }
            else {
                try {
                    DialogAvaliarManager.showRateDialog(HomeActivity.this, savedInstanceState);
                }
                catch (Exception e) {
                    Crashlytics.logException(e);
                    CrashAnalytics.e(TAG, e);
                }
            }
        }
        else {
            try {
                DialogAvaliarManager.showRateDialog(HomeActivity.this, savedInstanceState);
            }
            catch (Exception e) {
                Crashlytics.logException(e);
                CrashAnalytics.e(TAG, e);
            }
        }
    }

    private void exibirComunicadoNaoVisto(Comunicado comunicado) {
        if (!isFinishing()) {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

            LayoutInflater inflater = this.getLayoutInflater();

            View dialogView = inflater.inflate(R.layout.dialog_comunicado, null);

            dialogBuilder.setView(dialogView);

            TextView txtTitulo = dialogView.findViewById(R.id.dialog_comunicado_titulo);
            TextView txtTexto = dialogView.findViewById(R.id.dialog_comunicado_texto);
            TextView txtData = dialogView.findViewById(R.id.dialog_comunicado_data);

            txtTitulo.setText(comunicado.getTitulo());
            txtTexto.setText(comunicado.getComunicado());
            txtData.setText(comunicado.getData());

            dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    try {
                        DialogAvaliarManager.showRateDialog(HomeActivity.this, savedInstanceState);
                    }
                    catch (Exception e) {
                        Crashlytics.logException(e);
                        CrashAnalytics.e(TAG, e);
                    }
                }
            });

            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);
            alertDialog.show();
        }
    }

    private void verificarRecadoNovo() {

        ComunicadoAsyncTask comunicadoAsyncTask = new ComunicadoAsyncTask();

        comunicadoAsyncTask.delegate = this;

        comunicadoAsyncTask.execute(usuario.getToken());
    }

    @Override
    protected void onStart() {

        super.onStart();

        inicializarBanco();
    }

    @Override
    protected void onResume() {

        super.onResume();

        homeViewMvcImpl.ativarBotoes();

        //inicializarAcessoBanco(banco);

        if (usuario == null) {

            usuario = menuDBgetters.getUsuarioAtivo();

            homeViewMvcImpl.exibirNomeUsuario(usuario.getNome());
        } else {

            homeViewMvcImpl.exibirNomeUsuario(usuario.getNome());
        }

        homeViewMvcImpl.registerListener(this);

        if (!buscouComunicado) {

            verificarRecadoNovo();
        }

    }

    @Override
    protected void onPause() {

        super.onPause();

        //terminarObjetosAcessoBanco();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        homeViewMvcImpl.unregisterListener();
    }

    @Override
    public void onBackPressed() {

        return;
    }

    private void sobreAplicativo() {

        try {

            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

            homeViewMvcImpl.sobreAplicativo(packageInfo);
        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
        }
    }

    private void inicializarBanco() {
        if (banco == null) {
            banco = CriarAcessoBanco.gerarBanco(getApplicationContext());
        }
        inicializarAcessoBanco(banco);
    }

    private void exibirCarteirinha() {

        limparIntent(intent);

        bundle.clear();

        bundle.putParcelable("usuario", usuario);

        //intent.setClass(this, CarteirinhaActivity.class);

        intent.setClass(this, ListaCarteirinhasActivity.class);

        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public void salvarFrequenciaComConflito(String data, String horario, String turma, String disciplina) {

        int turmaFrequencia = turmaDBgetters.getTurmaFrequencia(turma);

        frequenciaDBgetters.inserirFrequenciaConflito(data, horario, turmaFrequencia, disciplina);
    }

    @Override
    public void alterarFrequencias() {

        frequenciaDBcrud.updateFrequencia();
    }

    @Override
    public void resolverConflitos() {

        frequenciaDBgetters.getConflitosResolvidos();
    }

    private void avaliarAplicativo() {

        DialogAvaliarApp dialog = new DialogAvaliarApp();

        dialog.show(getSupportFragmentManager(), "DialogFragmentAvaliarAplicativo");
    }

    private void navegarPara(Object tela) {

        selecaoTela.setClass(this, (Class) tela);

        intent.setClass(this, TurmasListaActivity.class);

        bundle.clear();

        bundle.putParcelable("usuario", usuario);

        bundle.putParcelable("Menu", selecaoTela);

        if (tela == FechamentoActivity.class) {

            bundle.putBoolean("Fechamento", true);
        }

        intent.replaceExtras(bundle);

        startActivity(intent);
    }

    private void compartilharAplicativo() {

        Intent sendIntent = new Intent();

        sendIntent.setAction(Intent.ACTION_SEND);

        sendIntent.putExtra(

                Intent.EXTRA_TEXT, getString(R.string.menu_share) + " " + getString(R.string.menu_share_link_play)
        );

        sendIntent.setType("text/plain");

        startActivity(sendIntent);
    }

    private void limparIntent(Intent intent) {

        if (intent.hasExtra("usuario")) {

            intent.removeExtra("usuario");
        }

        if (intent.hasExtra("Menu")) {

            intent.removeExtra("Menu");
        }

        if (intent.hasExtra("Fechamento")) {

            intent.removeExtra("Fechamento");
        }
    }

    private boolean fechamentoEstaDisponivel() {
        boolean fechamentoDisponivel = false;
        FechamentoData fechamentoData = menuDBgetters.getDataFechamento();
        if (fechamentoData != null) {
            String status = fechamentoData.getStatus();
            if (status != null) {
                if (status.equals("aberto")) {
                    fechamentoDisponivel = true;
                }
                else {
                    aguardeDataFechamento(fechamentoData);
                }
            }
        }
        return fechamentoDisponivel;
    }

    private boolean verificarFechamentoDisponivel(){

        final FechamentoData fechamentoData = menuDBgetters.getDataFechamento();

        boolean fechamentoDisponivel = false;

        if (fechamentoData != null) {

            if (fechamentoData.getStatus().equals("aberto")) {

                fechamentoDisponivel = true;
            }
        }
        return fechamentoDisponivel;

    }
    
    @Override
    public void usuarioQuerSairSemSincronizar() {

        logOut();
    }

    @Override
    public void usuarioAceitaUsarPlanoDeDados() {
        homeViewMvcImpl.mostrarViewSinc();
        iniciarRevalidacaoToken();
    }

    public void onClickMenuLateral(View menuItem) {

        homeViewMvcImpl.selecaoMenuLateral(menuItem);
    }

    public void onClickMenuPrincipal(View modulo) {

        homeViewMvcImpl.selecaoMenuPrincipal(modulo);
    }

    private void inicializarAcessoBanco(Banco banco) {

        frequenciaDBgetters = new FrequenciaDBgetters(banco);
        avaliacaoDBgetters = new AvaliacaoDBgetters(banco);
        registroDBgetters = new RegistroDBgetters(banco);
        comunicadoBdCrud = new ComunicadoBdCrud(banco);
        frequenciaDBcrud = new FrequenciaDBcrud(banco);
        avaliacaoDBcrud = new AvaliacaoDBcrud(banco);
        turmaDBgetters = new TurmaDBgetters(banco);
        turmaDBsetters = new TurmaDBsetters(banco);
        registroDBcrud = new RegistroDBcrud(banco);
        menuDBgetters = new MenuDBgetters(banco);
        menuDBcrud = new MenuDBcrud(banco);
    }

    private List<JSONObject> procurarFrequenciasParaEnviar() {
        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);

        ArrayList<TurmaGrupo> turmas = turmaDBgetters.getTurmas(ano, false);
        return frequenciaDBgetters.montarJSONEnvio(turmas);
    }

    private void checarSeExistemDadosNaoSincronizados() {

        if (!menuDBgetters.temDadosParaSincronizar()) {

            logOut();
        } else {

            homeViewMvcImpl.usuarioAvisoDadosNaoSincronizados();
        }
    }

    @Override
    public void alterarAvaliacoes(List<JSONObject> lista) {

        avaliacaoDBcrud.atualizarAvaliacaoSincronizada(lista);
    }

    private ActivityManager.MemoryInfo getAvailableMemory() {

        ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        return memoryInfo;
    }

    @Override
    public void usuarioSelecionouMenuLateral(String selecao) {

        switch (selecao) {

            case "COMPARTILHAR":

                compartilharAplicativo();

                break;

            case "AVALIAR":

                avaliarAplicativo();

                break;

            case "SINCRONIZAR":

                if (myPreferences.isLoginTeste()) {

                    Toast.makeText(this, "Sincronizado!", Toast.LENGTH_SHORT).show();
                } else {

                    conferirConexao();
                }


                break;

            case "SOBRE":

                sobreAplicativo();

                //startActivity(new Intent(this, Calendario.class));

                break;

            case "TUTORIAL":

                Intent intentTutorial = new Intent(HomeActivity.this, TutorialActivity.class);

                intentTutorial.putExtra("origem", "menu");

                myPreferences.putBoolean(MyPreferences.KEY_TUTORIAL, true);

                startActivity(intentTutorial);

                break;

            case "LOGOUT":

                checarSeExistemDadosNaoSincronizados();

                break;

            case "TABELAS":

                Intent intent = new Intent(this, ActivityTabelas.class);

                startActivity(intent);

                break;


        }
    }

    @Override
    public void usuarioSelecionouMenuPrincipal(String selecao) {

        switch (selecao) {

            case "CARTEIRINHA":

                if (myPreferences.isLoginTeste()) {

                    Toast.makeText(this, "Login de testes não possuí carteirinha!", Toast.LENGTH_SHORT).show();
                } else {


                    exibirCarteirinha();
                }

                break;

            case "TURMAS":

                navegarPara(AlunosListaActivity.class);

                break;

            case "REGISTROAULA":

                navegarPara(RegistroAulaActivity.class);

                break;

            case "FREQUENCIA":

                navegarPara(FrequenciaLancamentoActivity.class);

                break;

            case "AVALIACAO":

                //navegarPara(ListaAvaliacoesActivity.class);

                Toast.makeText(this, "Estamos trabalhando no módulo \"Avaliações\" e este estará disponível em breve.", Toast.LENGTH_SHORT).show();

                break;

            case "FECHAMENTO":

                /*
                if (!myPreferences.isLoginTeste()) {

                    if (fechamentoEstaDisponivel()) {

                        navegarPara(FechamentoActivity.class);
                    }
                }
                 */

                Toast.makeText(this, "Aguarde o período do lançamento de fechamento.", Toast.LENGTH_LONG).show();

                break;

            case "COMUNICADOS":

                startActivity(new Intent(this, ComunicadosActivity.class));

                break;
        }
    }

    @Override
    public void deletarAvaliacaoNoBancoLocal(int codigoAvaliacao) {

        avaliacaoDBcrud.deleteAvaliacao(codigoAvaliacao);
    }

    private void aguardeDataFechamento(FechamentoData fechamentoData) {

        LocalDate aguardeData = LocalDate.parse(

                fechamentoData.getInicio(), DateTimeFormat.forPattern("yyyy-MM-dd")
        );

        homeViewMvcImpl.usuarioAvisoFechamentoIndisponivel(aguardeData);
    }

    @Override
    public void usuarioClicouBotaoSincronizar() {

        if (myPreferences.isLoginTeste()) {

            Toast.makeText(this, "Sincronizado!", Toast.LENGTH_SHORT).show();
            return;
        }

        conferirConexao();
    }

    private void conferirConexao() {

        if (!NetworkUtils.isWifi(this)) {

            homeViewMvcImpl.usuarioAvisoSemWiFi();
        }
        else {

            iniciarSincronizacao();
        }
    }

    public void iniciarSincronizacao() {

        if (myPreferences.isLoginTeste()) {

            return;
        }

        homeViewMvcImpl.mostrarViewSinc();

        avancarSincronizacao(EtapasSincronizacao.ETAPA_INICIO);

    }

    private void avancarSincronizacao(String etapa) {

        switch (etapa){

            case EtapasSincronizacao.ETAPA_INICIO:

                iniciarRevalidacaoToken();
                break;

            case EtapasSincronizacao.ETAPA_TOKEN :

                iniciarEtapaAvaliacao();
                break;

            case EtapasSincronizacao.ETAPA_AVAL :

                iniciarEtapaFaltas();
                break;

            case EtapasSincronizacao.ETAPA_FALTAS :

                iniciarEtapaRegistro();
                break;

            case EtapasSincronizacao.ETAPA_REGISTRO :

                sincronizarComDadosAtualizadosDaSED();
                break;

            case EtapasSincronizacao.ETAPA_DADOS:

                terminouSincronizacaoDados();
                break;
        }
    }

    public void iniciarRevalidacaoToken() {
        if (usuario != null) {
            revalidarTokenAsyncTask = new RevalidarTokenAsyncTask();
            revalidarTokenAsyncTask.delegate = homeViewMvcImpl;
            revalidarTokenAsyncTask.execute(usuario.getUsuario(), usuario.getSenha());
        }
    }

    @Override
    public void atualizarToken(String token) {

        if (token != null && !token.isEmpty()) {

            menuDBcrud.atualizarTokenUsuario(token);

            usuario.setToken(token);

            selecionarPerfil(token);
        }
    }

    @Override
    public void selecionarPerfil(String token) {

        selecionarPerfilAsyncTask = new SelecionarPerfilAsyncTask();

        selecionarPerfilAsyncTask.delegate = homeViewMvcImpl;

        selecionarPerfilAsyncTask.execute(token);
    }

    @Override
    public void perfilSelecionado(boolean perfilOK) {

        completouEtapaSincronizacao(EtapasSincronizacao.ETAPA_TOKEN, perfilOK);
    }

    private void iniciarEtapaAvaliacao(){

        List<Integer> avaliacoesParaDeletar = avaliacaoDBgetters.getAvaliacoesParaDeletar();

        if (!avaliacoesParaDeletar.isEmpty()) {

            deletarAvaliacoesTask = new DeletarAvaliacoesTask(usuario.getToken());
            deletarAvaliacoesTask.delegate = homeViewMvcImpl;
            deletarAvaliacoesTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, avaliacoesParaDeletar);
        }

        List<JSONObject> avaliacoesNaoSincronizadas = avaliacaoDBgetters.getAvaliacoesNaoSincronizadas();

        if (!avaliacoesNaoSincronizadas.isEmpty()) {

            enviarAvaliacoesAsyncTask = new EnviarAvaliacoesAsyncTask(usuario.getToken());
            enviarAvaliacoesAsyncTask.delegate = this;
            enviarAvaliacoesAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, avaliacoesNaoSincronizadas);
        }
        else{

            completouEtapaSincronizacao(EtapasSincronizacao.ETAPA_AVAL, true);
        }
    }

    private void iniciarEtapaFaltas(){

        List<JSONObject> listaFrequenciasParaEnviar = procurarFrequenciasParaEnviar();

        if (!listaFrequenciasParaEnviar.isEmpty()) {

            enviarFrequenciasAsyncTask = new EnviarFrequenciasAsyncTask(usuario.getToken());
            enviarFrequenciasAsyncTask.delegate = this;
            enviarFrequenciasAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, listaFrequenciasParaEnviar);
        }
        else {

            completouEtapaSincronizacao(EtapasSincronizacao.ETAPA_FALTAS, true);
        }
    }

    public void frequenciasResultadoSincronizacao(List<JSONObject> listaResultadoFrequencia) {

        alterarFrequencias();

        for (int i = 0; i < listaResultadoFrequencia.size(); i++) {

            try {

                if (listaResultadoFrequencia.get(i).getInt("Status") == 409) {

                    for (int j = 0; j < listaResultadoFrequencia.get(i).getJSONArray("DiasComConflito").length(); j++) {

                        if (listaResultadoFrequencia.get(i).getJSONArray("DiasComConflito")
                                .getJSONObject(j).getJSONArray("HorariosComConflito").length() > 0) {

                            for (int k = 0; k < listaResultadoFrequencia.get(i).getJSONArray("DiasComConflito").getJSONObject(j).getJSONArray("HorariosComConflito").length(); k++) {

                                String dia =
                                        listaResultadoFrequencia.get(i)
                                                .getJSONArray("DiasComConflito")
                                                .getJSONObject(j)
                                                .getString("Dia");

                                String horario = listaResultadoFrequencia.get(i)
                                        .getJSONArray("DiasComConflito")
                                        .getJSONObject(j)
                                        .getJSONArray("HorariosComConflito")
                                        .getJSONObject(k)
                                        .getString("Horario");

                                String turma = String.valueOf(listaResultadoFrequencia.get(i).getInt("Turma"));

                                String disciplina = String.valueOf(listaResultadoFrequencia.get(i).getInt("Disciplina"));

                                salvarFrequenciaComConflito(dia, horario, turma, disciplina);
                            }
                        }
                    }
                }
            }
            catch (Exception e) {

                e.printStackTrace();
            }
        }

        resolverConflitos();
    }

    private void iniciarEtapaRegistro() {
        enviarRegistrosAsyncTask = new EnviarRegistrosAsyncTask(this, registroDBgetters, registroDBcrud, banco.get());
        enviarRegistrosAsyncTask.delegate = this;
        enviarRegistrosAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, usuario.getToken());
    }

    @Override
    public synchronized void completouEtapaSincronizacao(String etapa, boolean sucesso) {

        homeViewMvcImpl.completouEtapa(etapa, sucesso);
        avancarSincronizacao(etapa);
    }

    private void sincronizarComDadosAtualizadosDaSED() {

        Log.e(TAG, "JSON: Iniciado / Memória: " + getAvailableMemory().availMem);

        resgatarTurmasAsyncTask = new ResgatarTurmasAsyncTask(turmaDBsetters);

        resgatarTurmasAsyncTask.delegate = homeViewMvcImpl;

        try {

            resgatarTurmasAsyncTask.execute(usuario.getToken());
        }
        catch (Exception e) {

            e.printStackTrace();
        }
        finally {

            enviarFrequenciasAsyncTask = null;
            enviarAvaliacoesAsyncTask = null;
            enviarRegistrosAsyncTask = null;
            revalidarTokenAsyncTask = null;
            resgatarTurmasAsyncTask = null;
        }
    }

    @Override
    public void terminouSincronizacaoTurma(boolean sucesso) {

        if(sucesso){

            iniciarEtapaCurriculos();
        }
        else{

            completouEtapaSincronizacao(EtapasSincronizacao.ETAPA_DADOS, sucesso);
        }

    }

    private void iniciarEtapaCurriculos(){

        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);

        List<TurmaGrupo> turmas = turmaDBgetters.getTurmas(ano, false);

        if (!turmas.isEmpty()) {

            new CurriculosAsyncTask(banco, turmas, this, this).execute(usuario.getToken());
        }
        else {

            completouEtapaSincronizacao(EtapasSincronizacao.ETAPA_DADOS, false);
            homeViewMvcImpl.exibirDadosIncompletosNoServidor();
        }
    }

    public void terminouSincronizacaoDados() {

        if (NetworkUtils.isNetworkAvailable(this)) {

            dialogVersaoApp = new Builder(this, R.style.ThemeOverlay_AppCompat_Dialog)
                    .setCancelable(false)
                    .setView(R.layout.dialog_versao_app)
                    .create();

            dialogVersaoApp.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);
            dialogVersaoApp.show();

            new VersaoAppAsyncTask(this,this).execute(usuario.getToken());
        }
        else {
            int versaoAtual = myPreferences.versaoAtual();
            if (versaoAtual != 0) {
                terminouRequisicaoVersaoDoApp(versaoAtual);
            }
            else {
                try {
                    DialogAvaliarManager.showRateDialog(HomeActivity.this, savedInstanceState);
                }
                catch (Exception e) {
                    Crashlytics.logException(e);
                    CrashAnalytics.e(TAG, e);
                }
            }
        }


        try{

            homeViewMvcImpl.desabilitarBotao("avaliacao");

            if(!verificarFechamentoDisponivel()){

                homeViewMvcImpl.desabilitarBotao("fechamento");
            }
        }
        catch (Exception e){

            e.printStackTrace();
        }

    }

    @Override
    public void terminouRequisicaoVersaoDoApp(int versaoApp) {

        if (dialogVersaoApp != null) {
            dialogVersaoApp.dismiss();
            dialogVersaoApp = null;
        }

        if(versaoApp == -1){

            return;
        }

        myPreferences.salvarVersaoApp(versaoApp);

        if (versaoApp != BuildConfig.VERSION_CODE) {

            AlertDialog alertDialog = new Builder(this).setTitle("Atenção").setMessage("Existe uma nova versão do alicativo Diário de Classe na Google Play! Baixe a nova versão para continuar utilizando o Diário de Classe!").setCancelable(false).setNeutralButton(R.string.ir_para_loja, null).create();
            alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);
            alertDialog.show();
            alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=br.gov.sp.educacao.sed.mobile")));
                    }
                    catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=br.gov.sp.educacao.sed.mobile")));
                    }
                }
            });
        }
        else {
            List<String> lista = frequenciaDBgetters.getHorariosComConflito();
            if (!lista.isEmpty()) {
                homeViewMvcImpl.avisoUsuarioHorariosConflito(lista);
            }
            else {
                verificarRecadoNovo();
            }
        }
    }
}