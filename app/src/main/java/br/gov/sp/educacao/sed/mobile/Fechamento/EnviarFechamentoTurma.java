package br.gov.sp.educacao.sed.mobile.Fechamento;

import android.app.Activity;
import android.util.Log;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import br.gov.sp.educacao.sed.mobile.Login.LoginDBcrud;
import br.gov.sp.educacao.sed.mobile.Login.LoginRequest;
import br.gov.sp.educacao.sed.mobile.Login.UsuarioTO;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;
import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;
import br.gov.sp.educacao.sed.mobile.util.Servidor.ConnectHandler;
import br.gov.sp.educacao.sed.mobile.util.Servidor.ParametroJson;
import br.gov.sp.educacao.sed.mobile.util.Servidor.RetornoJson;
import br.gov.sp.educacao.sed.mobile.util.Servidor.UrlServidor;

public class EnviarFechamentoTurma
        implements Runnable {

    private String TAG = EnviarFechamentoTurma.class.getSimpleName();

    private Activity activity;
    private IFechamentoTurmaListener listener;
    private int codigoTurma;
    private int disciplina;
    private boolean anosIniciais;
    private int tipoFechamentoAtual;
    private FechamentoDBcrud fechamentoDBcrud;
    private FechamentoDBgetters fechamentoDBgetters;
    private LoginDBcrud loginDBcrud;

    EnviarFechamentoTurma(Activity activity,
                          IFechamentoTurmaListener listener,
                          TurmaGrupo turmaGrupo,
                          boolean anosIniciais,
                          int tipoFechamentoAtual,
                          FechamentoDBcrud fechamentoDBcrud,
                          FechamentoDBgetters fechamentoDBgetters,
                          LoginDBcrud loginDBcrud) {

        this.activity = activity;
        this.listener = listener;
        this.codigoTurma = turmaGrupo.getTurma().getCodigoTurma();
        this.disciplina = turmaGrupo.getDisciplina().getCodigoDisciplina();
        this.anosIniciais = anosIniciais;
        this.tipoFechamentoAtual = tipoFechamentoAtual;
        this.fechamentoDBcrud = fechamentoDBcrud;
        this.fechamentoDBgetters = fechamentoDBgetters;
        this.loginDBcrud = loginDBcrud;
    }

    private void executar() {

        ParametroJson parametroJson = new ParametroJson("POST");

        try {

            JSONObject jsonObject = fechamentoDBgetters.montarJSONEnvio(codigoTurma, disciplina, anosIniciais, tipoFechamentoAtual);

            parametroJson.setContext(activity);

            parametroJson.setUrlServico(UrlServidor.URL_SALVAR_FECHAMENTO);

            parametroJson.setJsonObject(jsonObject.toString());

            if (!ParametroJson.JSON_OBJECT_VAZIO.equals(parametroJson.getJsonObject())) {

                Log.e(TAG, parametroJson.toString());

                ConnectHandler connectHandler = new ConnectHandler();

                connectHandler.execute(parametroJson, loginDBcrud);

                final RetornoJson retorno = connectHandler.get();

                if (retorno == null) return;

                if (RetornoJson.RETORNO_COM_SUCESSO == retorno.getStatusRetorno()
                        || RetornoJson.RETORNO_COM_SUCESSO_SEM_RESPOSTA == retorno.getStatusRetorno()) {

                    activity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            listener.onFechamentoTurmaEnviado(RetornoJson.RETORNO_COM_SUCESSO);
                        }
                    });

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

                    fechamentoDBcrud.updateFechamentoTurma(dateFormat.format(new Date()), tipoFechamentoAtual);
                }
                else if (RetornoJson.TOKEN_EXPIRADO == retorno.getStatusRetorno()) {

                    UsuarioTO usuario = fechamentoDBgetters.getUsuarioAtivo();

                    LoginRequest loginRequest = new LoginRequest();

                    UsuarioTO usuario1 = loginRequest.executeRequest(usuario.getUsuario(), usuario.getSenha());

                    loginDBcrud.atualizarUsuario(usuario1);

                    executar();
                }
            }
            else {

                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        listener.onFechamentoTurmaEnviado(RetornoJson.EMPTY);
                    }
                });
            }
        }
        catch (InterruptedException | ExecutionException e) {

            e.printStackTrace();

            CrashAnalytics.e("EnviarFechamentoTurma", e);

            Log.e(TAG, parametroJson.toString());
        }
    }

    @Override
    public void run() {

        executar();
    }
}