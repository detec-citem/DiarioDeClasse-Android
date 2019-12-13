package br.gov.sp.educacao.sed.mobile.Menu;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.gov.sp.educacao.sed.mobile.Avaliacao.AvaliacaoDBcrud;
import br.gov.sp.educacao.sed.mobile.Avaliacao.AvaliacaoDBgetters;
import br.gov.sp.educacao.sed.mobile.Avaliacao.ListaAvaliacoesActivity;
import br.gov.sp.educacao.sed.mobile.Carteirinha.ListaCarteirinhasActivity;
import br.gov.sp.educacao.sed.mobile.Fechamento.FechamentoActivity;
import br.gov.sp.educacao.sed.mobile.Fechamento.FechamentoData;
import br.gov.sp.educacao.sed.mobile.Frequencia.Calendario;
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
import br.gov.sp.educacao.sed.mobile.util.ActivityTabelas;
import br.gov.sp.educacao.sed.mobile.util.AnalyticsApplication;
import br.gov.sp.educacao.sed.mobile.util.AvaliarApp.DialogAvaliarApp;
import br.gov.sp.educacao.sed.mobile.util.AvaliarApp.DialogAvaliarManager;
import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.CriarAcessoBanco;
import br.gov.sp.educacao.sed.mobile.util.MyPreferences;
import br.gov.sp.educacao.sed.mobile.util.NetworkUtils;

public class HomeActivity
        extends AppCompatActivity
         implements HomeViewMvc.Listener {

    public Banco banco;

    private int threads;

    private Bundle bundle;

    private Intent intent;

    public String deviceId;

    public boolean buscouComunicado = false;

    public UsuarioTO usuario;

    private boolean completou;

    private Intent selecaoTela;

    private ComunicadoBdCrud comunicadoBdCrud;

    private MenuDBcrud menuDBcrud;

    private MenuDBgetters menuDBgetters;

    public TurmaDBgetters turmaDBgetters;

    public TurmaDBsetters turmaDBsetters;

    public RegistroDBcrud registroDBcrud;

    public AvaliacaoDBcrud avaliacaoDBcrud;

    private HomeViewMvcImpl homeViewMvcImpl;

    private FrequenciaDBcrud frequenciaDBcrud;

    public RegistroDBgetters registroDBgetters;

    public AvaliacaoDBgetters avaliacaoDBgetters;

    private FrequenciaDBgetters frequenciaDBgetters;

    private MyPreferences myPreferences;

    private DeletarAvaliacoesTask deletarAvaliacoesTask;

    private String TAG = HomeActivity.class.getSimpleName();

    private RevalidarTokenAsyncTask revalidarTokenAsyncTask;

    private ResgatarTurmasAsyncTask resgatarTurmasAsyncTask;

    private EnviarRegistrosAsyncTask enviarRegistrosAsyncTask;

    private SelecionarPerfilAsyncTask selecionarPerfilAsyncTask;

    private EnviarAvaliacoesAsyncTask enviarAvaliacoesAsyncTask;

    private EnviarFrequenciasAsyncTask enviarFrequenciasAsyncTask;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        homeViewMvcImpl = new HomeViewMvcImpl(LayoutInflater.from(this), getSupportFragmentManager(), checarTamanhoTela(), null);

        setContentView(homeViewMvcImpl.getRootView());

        myPreferences = new MyPreferences(this);

        inicializarBanco();

        bundle = new Bundle();

        intent = new Intent();

        selecaoTela = new Intent();

        inicializarGoogleAnalytics();

        usuario = menuDBgetters.getUsuarioAtivo();

        if(usuario != null) {

            Crashlytics.setUserIdentifier(usuario.getUsuario());
        }
        else {

            Crashlytics.setUserIdentifier("null");
        }

        if(turmaDBgetters.getTurmas(Calendar.getInstance().get(Calendar.YEAR), false).size() == 0) {

            conferirConexao();
        }
        else {

            DialogAvaliarManager.showRateDialog(this, savedInstanceState);
        }
    }

    private boolean checarTamanhoTela() {

        int width  = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        int dpi    = Resources.getSystem().getDisplayMetrics().densityDpi;

        boolean telaPequena = false;

        WindowManager windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);

        final Display display = windowManager.getDefaultDisplay();

        Point outPoint = new Point();

        if(Build.VERSION.SDK_INT >= 19) {

            display.getRealSize(outPoint);
        }
        else {

            display.getSize(outPoint);
        }

        if(height < 1000) {

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

        if(comunicadoTO.getQtd() > 0){

            buscouComunicado = true;

            comunicados = comunicadoTO.getComunicadosFromJson();

            comunicadoBdCrud.salvarComunicadosBanco(comunicados);

            Comunicado comunicadoParaExibir = comunicadoBdCrud.getUltimoComunicadoNaoVisto();

            if(comunicadoParaExibir != null){

                exibirComunicadoNaoVisto(comunicadoParaExibir);

                for(int i = 0; i < comunicados.size(); i++){

                    comunicadoBdCrud.atualizarComunicadoVisto(comunicados.get(i).getCdComunicado());
                }
            }
        }
    }

    private void exibirComunicadoNaoVisto(Comunicado comunicado) {

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
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();

        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);

        alertDialog.show();
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

        if(usuario == null) {

            usuario = menuDBgetters.getUsuarioAtivo();

            homeViewMvcImpl.exibirNomeUsuario(usuario.getNome());
        }
        else {

            homeViewMvcImpl.exibirNomeUsuario(usuario.getNome());
        }

        homeViewMvcImpl.registerListener(this);

        if(!buscouComunicado || !myPreferences.isLoginTeste()){

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

    private void revalidarToken() {

        homeViewMvcImpl.inicializaProgressoSincronizar();

        revalidarTokenAsyncTask = new RevalidarTokenAsyncTask();

        revalidarTokenAsyncTask.delegate = homeViewMvcImpl;

        revalidarTokenAsyncTask.execute(usuario.getUsuario(), usuario.getSenha());
    }

    private void conferirConexao() {

        if (!NetworkUtils.isWifi(this)) {

            homeViewMvcImpl.usuarioAvisoSemWiFi();
        } else {

            if (myPreferences.isLoginTeste()) {

                iniciarSincronizacao();
            } else {

                revalidarToken();
            }
        }
    }

    private void sobreAplicativo() {

        try {

            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

            homeViewMvcImpl.sobreAplicativo(packageInfo);
        }
        catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
        }
    }

    private void inicializarBanco() {

        if(banco == null) {

            CriarAcessoBanco criarAcessoBanco = new CriarAcessoBanco();

            banco = criarAcessoBanco.gerarBanco(getApplicationContext());
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
    public void verificarSeExistemHorariosComConflito() {

        List<String> lista = frequenciaDBgetters.getHorariosComConflito();

        if(lista.size() > 0) {

            homeViewMvcImpl.avisoUsuarioHorariosConflito(lista);
        }
        else{

            if(!myPreferences.isLoginTeste()){

                verificarRecadoNovo();
            }
        }
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

    private void iniciarSincronizacao() {

        threads = 0;

        completou = false;

        if(!myPreferences.isLoginTeste()){

            sincronizarAvaliacoesDeletadas();

            sincronizarAvaliacoesCriadas();

            sincronizarFrequencias();

            enviarRegistrosAsyncTask = new EnviarRegistrosAsyncTask(registroDBgetters, registroDBcrud);

            enviarRegistrosAsyncTask.delegate = homeViewMvcImpl;

            enviarRegistrosAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, usuario.getToken());
        }
        else{

            completouEtapaSincronizacao();
            completouEtapaSincronizacao();
            completouEtapaSincronizacao();
            completouEtapaSincronizacao();
        }

    }

    @Override
    public void completouSincronizacao() {

        Log.e(TAG, "JSON: Finalizado inicial");

        if(!completou) {

            completou = true;

            homeViewMvcImpl.finalizaProgressoSincronizar();

            Calendar today = Calendar.getInstance();

            if(turmaDBgetters.getTurmas(today.get(Calendar.YEAR), false).size() > 0) {

                homeViewMvcImpl.exibirSincronizacaoRealizada();
            }
            else {

                homeViewMvcImpl.exibirDadosIncompletosNoServidor();
            }
        }
        Log.e(TAG, "JSON: Finalizado inicial / Memória: " + getAvailableMemory().availMem);
    }

    private void navegarPara(Object tela) {

        selecaoTela.setClass(this, (Class) tela);

        intent.setClass(this, TurmasListaActivity.class);

        bundle.clear();

        bundle.putParcelable("usuario", usuario);

        bundle.putParcelable("Menu", selecaoTela);

        if(tela == FechamentoActivity.class) {

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

    private void sincronizarFrequencias() {

        List<JSONObject> listaFrequenciasParaEnvirar = procurarFrequenciasParaEnviar();

        if(listaFrequenciasParaEnvirar.size() > 0) {

            enviarFrequenciasAsyncTask = new EnviarFrequenciasAsyncTask(usuario.getToken());

            enviarFrequenciasAsyncTask.delegate = homeViewMvcImpl;

            enviarFrequenciasAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, listaFrequenciasParaEnvirar);
        }
        else {

            completouEtapaSincronizacao();
        }
    }

    private void limparIntent(Intent intent) {

        if(intent.hasExtra("usuario")) {

            intent.removeExtra("usuario");
        }

        if(intent.hasExtra("Menu")) {

            intent.removeExtra("Menu");
        }

        if(intent.hasExtra("Fechamento")) {

            intent.removeExtra("Fechamento");
        }
    }

    @Override
    public void atualizarToken(String token) {

        if(token != null && !token.isEmpty()) {

            menuDBcrud.atualizarTokenUsuario(token);

            usuario.setToken(token);

            selecionarPerfil(token);
        }
    }

    private void inicializarGoogleAnalytics() {

        Tracker tracker = (

                (AnalyticsApplication) this.getApplication()).getTracker(AnalyticsApplication.TrackerName.APP_TRACKER
        );

        tracker.enableAutoActivityTracking(true);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {

                deviceId = instanceIdResult.getToken();

                Log.e(TAG, "Token: " + deviceId);
            }
        });
    }

    @Override
    public void selecionarPerfil(String token) {

        selecionarPerfilAsyncTask = new SelecionarPerfilAsyncTask();

        selecionarPerfilAsyncTask.delegate = homeViewMvcImpl;

        selecionarPerfilAsyncTask.execute(token);
    }

    private boolean fechamentoEstaDisponivel() {

        final FechamentoData fechamentoData = menuDBgetters.getDataFechamento();

        boolean fechamentoDisponivel = false;

        if(fechamentoData != null) {

            if(fechamentoData.getStatus().equals("aberto")) {

                fechamentoDisponivel = true;
            }
            else {

                aguardeDataFechamento(fechamentoData);
            }
        }
        return fechamentoDisponivel;
    }

    @Override
    public void usuarioQuerSairSemSincronizar() {

        logOut();
    }

    @SuppressWarnings("unchecked")
    private void sincronizarAvaliacoesCriadas() {

        if(procurarAvaliacoesNaoSincronizadas().size() > 0) {

            enviarAvaliacoesAsyncTask = new EnviarAvaliacoesAsyncTask(usuario.getToken());

            enviarAvaliacoesAsyncTask.delegate = homeViewMvcImpl;

            enviarAvaliacoesAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, procurarAvaliacoesNaoSincronizadas());
        }
        else {

            completouEtapaSincronizacao();
        }
    }

    @Override
    public void usuarioClicouBotaoSincronizar() {

        //procurarFrequenciasParaEnviar();

        //if(myPreferences.isLoginTeste()){
//
        //    Toast.makeText(this, "Sincronizado!", Toast.LENGTH_SHORT).show();
        //    return;
        //}

        conferirConexao();
    }

    @Override
    public void usuarioAceitaUsarPlanoDeDados() {

        revalidarToken();
    }

    public void onClickMenuLateral(View menuItem) {

        homeViewMvcImpl.selecaoMenuLateral(menuItem);
    }

    @SuppressWarnings("unchecked")
    private void sincronizarAvaliacoesDeletadas() {

        if(procurarAvaliacoesParaDeletar().size() > 0) {

            deletarAvaliacoesTask = new DeletarAvaliacoesTask(usuario.getToken());

            deletarAvaliacoesTask.delegate = homeViewMvcImpl;

            deletarAvaliacoesTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, procurarAvaliacoesParaDeletar());
        }
        else {

            completouEtapaSincronizacao();
        }
    }

    public void onClickMenuPrincipal(View modulo) {

        homeViewMvcImpl.selecaoMenuPrincipal(modulo);
    }

    @Override
    public void perfilSelecionado(boolean perfilOK) {

        if(perfilOK) {

            iniciarSincronizacao();
        }
        else {

            terminouSincronizacao(perfilOK);
        }
    }

    private void inicializarAcessoBanco(Banco banco) {

        turmaDBgetters = new TurmaDBgetters(banco);

        turmaDBsetters = new TurmaDBsetters(banco);

        menuDBgetters = new MenuDBgetters(banco);

        menuDBcrud = new MenuDBcrud(banco);

        frequenciaDBcrud = new FrequenciaDBcrud(banco);

        frequenciaDBgetters = new FrequenciaDBgetters(banco);

        registroDBgetters = new RegistroDBgetters(banco);

        registroDBcrud = new RegistroDBcrud(banco);

        avaliacaoDBgetters = new AvaliacaoDBgetters(banco);

        avaliacaoDBcrud = new AvaliacaoDBcrud(banco);

        comunicadoBdCrud = new ComunicadoBdCrud(banco);
    }

    private List<JSONObject> procurarFrequenciasParaEnviar() {

        ArrayList<TurmaGrupo> turmas = turmaDBgetters.getTurmas(2019, false);

        return frequenciaDBgetters.montarJSONEnvio(turmas);
    }

    private void sincronizarComDadosAtualizadosDaSED() {

        Log.e(TAG, "JSON: Iniciado / Memória: " + getAvailableMemory().availMem);

        resgatarTurmasAsyncTask = new ResgatarTurmasAsyncTask(turmaDBsetters);

        resgatarTurmasAsyncTask.delegate = homeViewMvcImpl;

        try {

            //String json1 = String.valueOf(lerArquivoEstatico(R.raw.json2));

            //resgatarTurmasAsyncTask.execute(usuario.getToken(), json1);

            resgatarTurmasAsyncTask.execute(usuario.getToken());
        }
        catch (Exception e) {

            e.printStackTrace();
        }
        finally {

            revalidarTokenAsyncTask = null;

            enviarRegistrosAsyncTask = null;

            enviarAvaliacoesAsyncTask = null;

            enviarFrequenciasAsyncTask = null;

            resgatarTurmasAsyncTask = null;
        }
    }

    @Override
    public void terminouSincronizacao(boolean sucesso) {

        Log.e(TAG, "JSON: Finalizado progress");

        resgatarTurmasAsyncTask = null;

        revalidarTokenAsyncTask = null;

        enviarRegistrosAsyncTask = null;

        enviarAvaliacoesAsyncTask = null;

        selecionarPerfilAsyncTask = null;

        enviarFrequenciasAsyncTask = null;

        if(sucesso) {

            homeViewMvcImpl.atualizarProgressoSincronizar(2);
        }
        else {

            if(myPreferences.isLoginTeste()){

                Toast.makeText(this, "Sincronizado!", Toast.LENGTH_SHORT).show();
                return;
            }

            homeViewMvcImpl.finalizaProgressoSincronizar();

            homeViewMvcImpl.exibirFalhaNaSincronizacao();
        }
        Log.e(TAG, "JSON: Finalizado progress / Memória: " + getAvailableMemory().availMem);
    }

    private void checarSeExistemDadosNaoSincronizados() {

        if(!menuDBgetters.temDadosParaSincronizar()) {

            logOut();
        }
        else {

            homeViewMvcImpl.usuarioAvisoDadosNaoSincronizados();
        }
    }

    private List<Integer> procurarAvaliacoesParaDeletar() {

        return avaliacaoDBgetters.getAvaliacoesParaDeletar();
    }

    @Override
    public void alterarAvaliacoes(List<JSONObject> lista) {

        avaliacaoDBcrud.atualizarAvaliacaoSincronizada(lista);
    }

    @Override
    public synchronized void completouEtapaSincronizacao() {

        threads++;

        if(threads == 4) {

            sincronizarComDadosAtualizadosDaSED();
        }
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

                //if(myPreferences.isLoginTeste()){
//
                //    Toast.makeText(this, "Sincronizado!", Toast.LENGTH_SHORT).show();
                //}
                //else{
//
                //
                //}

                conferirConexao();

                break;

            case "SOBRE":

                sobreAplicativo();

                //startActivity(new Intent(this, Calendario.class));

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

        switch(selecao) {

            case "CARTEIRINHA":

                if(myPreferences.isLoginTeste()){

                    Toast.makeText(this, "Login de testes não possuí carteirinha!", Toast.LENGTH_SHORT).show();
                }
                else{


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

                navegarPara(ListaAvaliacoesActivity.class);

                break;

            case "FECHAMENTO":

                if(!myPreferences.isLoginTeste()){

                    if(fechamentoEstaDisponivel()) {

                        navegarPara(FechamentoActivity.class);
                    }
                }
                
                break;

            case "COMUNICADOS":

                startActivity(new Intent(this, ComunicadosActivity.class));

                break;
        }
    }

    private List<JSONObject> procurarAvaliacoesNaoSincronizadas() {

        return avaliacaoDBgetters.getAvaliacoesNaoSincronizadas();
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

    protected StringBuilder lerArquivoEstatico(int fileID) throws IOException {

        BufferedReader br = new BufferedReader(

                new InputStreamReader(getApplicationContext().getResources().openRawResource(fileID), "UTF-8")
        );

        StringBuilder jsonString = new StringBuilder();

        String line;

        while((line = br.readLine()) != null) {

            line = line.trim();

            if(line.length() > 0) {

                jsonString.append(line);
            }
        }
        return jsonString;
    }
}