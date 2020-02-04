package br.gov.sp.educacao.sed.mobile.Menu;

import android.content.pm.PackageInfo;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

interface HomeViewMvc {

    interface Listener {

        void alterarFrequencias();

        void atualizarToken(String token);

        void completouEtapaSincronizacao(String etapa, boolean sucesso);

        void terminouRequisicaoRecado(JSONArray jsonRetorno);

        void selecionarPerfil(String token);

        void usuarioClicouBotaoSincronizar();

        void usuarioQuerSairSemSincronizar();

        void usuarioAceitaUsarPlanoDeDados();

        void resolverConflitos();

        void perfilSelecionado(boolean perfilOK);

        void terminouSincronizacaoTurma(boolean sucess);

        void alterarAvaliacoes(List<JSONObject> lista);

        void usuarioSelecionouMenuLateral(String selecao);

        void usuarioSelecionouMenuPrincipal(String selecao);

        void deletarAvaliacaoNoBancoLocal(int codigoAvaliacao);

        void salvarFrequenciaComConflito(String dia, String horario, String turma, String disciplina);

        void frequenciasResultadoSincronizacao(List<JSONObject> listaResultadoFrequencia);

        void terminouRequisicaoVersaoDoApp(int versaoApp);
    }

    View getRootView();

    void unregisterListener();

    void usuarioAvisoSemWiFi();

    void selecaoMenuPrincipal(View modulo);

    void selecaoMenuLateral(View menuItem);

    void exibirDadosIncompletosNoServidor();

    void registerListener(Listener listener);

    void usuarioAvisoDadosNaoSincronizados();

    void terminouSincronizacaoTurmas(boolean sucess);

    void sobreAplicativo(PackageInfo packageInfo);

    void desabilitarBotao(String modulo);

    void deletarAvaliacaoNoBancoLocal(int codigoAvaliacao);

    ToolbarViewMvc getToolbarViewMvc(@Nullable ViewGroup parent);

    void usuarioAvisoFechamentoIndisponivel(LocalDate aguardeData);

    void completouEtapa(String etapa, boolean sucesso);

    void mostrarViewSinc();

}