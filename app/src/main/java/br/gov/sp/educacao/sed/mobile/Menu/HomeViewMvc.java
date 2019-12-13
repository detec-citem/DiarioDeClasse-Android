package br.gov.sp.educacao.sed.mobile.Menu;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.view.View;
import android.view.ViewGroup;

import org.joda.time.LocalDate;

import android.content.pm.PackageInfo;

import android.support.annotation.Nullable;

interface HomeViewMvc {

    interface Listener {

        void alterarFrequencias();

        void completouSincronizacao();

        void atualizarToken(String token);

        void completouEtapaSincronizacao();

        void terminouRequisicaoRecado(JSONArray jsonRetorno);

        void selecionarPerfil(String token);

        void usuarioClicouBotaoSincronizar();

        void usuarioQuerSairSemSincronizar();

        void usuarioAceitaUsarPlanoDeDados();

        void resolverConflitos();

        void perfilSelecionado(boolean perfilOK);

        void terminouSincronizacao(boolean sucess);

        void alterarAvaliacoes(List<JSONObject> lista);

        void usuarioSelecionouMenuLateral(String selecao);

        void usuarioSelecionouMenuPrincipal(String selecao);

        void deletarAvaliacaoNoBancoLocal(int codigoAvaliacao);

        void salvarFrequenciaComConflito(String dia, String horario, String turma, String disciplina);

        void verificarSeExistemHorariosComConflito();
    }

    View getRootView();

    void unregisterListener();

    void usuarioAvisoSemWiFi();

    void exibirFalhaNaSincronizacao();

    void completouEtapaSincronizacao();

    void finalizaProgressoSincronizar();

    void exibirSincronizacaoRealizada();

    void revalidacaoDeToken(String token);

    void inicializaProgressoSincronizar();

    void selecaoMenuPrincipal(View modulo);

    void selecaoMenuLateral(View menuItem);

    void exibirDadosIncompletosNoServidor();

    void registerListener(Listener listener);

    void frequenciasResultadoSincronizacao(List<JSONObject> listaResultadoFrequencia);

    void usuarioAvisoDadosNaoSincronizados();

    void terminouSincronizacao(boolean sucess);

    void sobreAplicativo(PackageInfo packageInfo);

    void deletarAvaliacaoNoBancoLocal(int codigoAvaliacao);

    ToolbarViewMvc getToolbarViewMvc(@Nullable ViewGroup parent);

    void usuarioAvisoFechamentoIndisponivel(LocalDate aguardeData);

    void avaliacoesResultadoSincronizacao(List<JSONObject> lista);
}
