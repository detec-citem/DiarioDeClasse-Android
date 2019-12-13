package br.gov.sp.educacao.sed.mobile.util.Servidor;

public class UrlServidor {

    public static String URL_SERVIDOR = "https://desenvolvimentosed.educacao.sp.gov.br/SedApi/Api";

    public static final String URL_LOGIN = URL_SERVIDOR + "/Login";

    public static final String URL_SELECIONAR_PERFIL = URL_SERVIDOR + "/Login/SelecionarPerfil";

    public static final String URL_DADOS_OFF_LINE = URL_SERVIDOR + "/Offline3/Professor";

    public static final String URL_FREQUENCIA = URL_SERVIDOR + "/Frequencia/NovaFrequencia";

    public static final String URL_FREQUENCIA_EXCLUIR = URL_SERVIDOR + "/Frequencia/ExcluirFrequencia";

    public static final String URL_REGISTRO_AULAS = URL_SERVIDOR + "/RegistroAula";

    public static final String URL_AVALIACAO = URL_SERVIDOR + "/AvaliacaoNova/Salvar";

    public static final String URL_AVALIACAO_DELETAR = URL_SERVIDOR + "/AvaliacaoNova/Excluir";

    public static final String URL_SALVAR_FECHAMENTO = URL_SERVIDOR + "/FechamentoNovo/Salvar";

    public static final String URL_CARTEIRINHA = URL_SERVIDOR + "/CarteirinhaServidor/DadosCarteirinhaServidor";

    public static final String URL_COMUNICADOS = URL_SERVIDOR + "/Comunicado/Buscar";

    public static String URL_TRACKER = "https://sed.educacao.sp.gov.br/SedApi/Api";
}