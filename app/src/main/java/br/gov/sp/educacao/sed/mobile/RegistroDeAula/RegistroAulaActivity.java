package br.gov.sp.educacao.sed.mobile.RegistroDeAula;

import android.content.Intent;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.view.LayoutInflater;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.gov.sp.educacao.sed.mobile.Escola.Aula;
import br.gov.sp.educacao.sed.mobile.Escola.Bimestre;
import br.gov.sp.educacao.sed.mobile.Escola.DiasLetivos;

import br.gov.sp.educacao.sed.mobile.Fechamento.FechamentoData;

import br.gov.sp.educacao.sed.mobile.R;

import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;

import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.CriarAcessoBanco;
import br.gov.sp.educacao.sed.mobile.util.DateUtils;
import br.gov.sp.educacao.sed.mobile.util.ListenerCalendario.CaldroidFragmentListener;

public class RegistroAulaActivity
        extends AppCompatActivity
         implements RegistroAulaActivityViewMvc.Listener {

    private String turma;
    private String dataCriacao;

    private int disciplina, disciplinaSelecionada;
    private int bimestreSelecionado;
    private int serie;
    private int numeroBimestreAtual;

    public CaldroidFragment dialogCaldroidFragment;
    public CaldroidListener listener;

    private Bimestre bimestreAtual;
    private Bimestre bimestreAnterior;

    private List<DiasLetivos> listaDiasLetivos;
    private List<Aula> listaAula;

    private List<String> listaDiasLetivosStr;
    private List<String> listaDiasMarcados;
    private List<String> datasRegistros;
    private List<String> listaHorariosSelecionados;

    private Registro registroAtual, registroCriado;

    private GrupoCurriculoContHab grupoCurriculoContHab;

    private List<Habilidade> listaHabilidadesCheckadas;
    private List<Habilidade> listaHabilidadesParaExibir;

    private List<Integer> listaDiaSemana;
    private List<Integer> listaCodigosHabilidadesCheckadas;
    private List<Integer> listaCodigosConteudosCheckados = new ArrayList<>();

    private List<Conteudo> conteudosParaExibir;

    private TurmaGrupo turmaGrupo;

    private CriarAcessoBanco criarAcessoBanco;

    private Calendar calendar;

    private Banco banco;

    private RegistroDBgetters registroDBgetters;

    private RegistroDBcrud registroDBcrud;

    private RegistroAulaActivityViewMvcImpl registroAulaActivityViewMvcImpl;

    private boolean alterouRegistro = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        registroAulaActivityViewMvcImpl = new RegistroAulaActivityViewMvcImpl(LayoutInflater.from(this), getSupportFragmentManager(), null);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        turmaGrupo = extras.getParcelable(TurmaGrupo.BUNDLE_TURMA_GRUPO);

        turma = String.valueOf(turmaGrupo.getTurma().getCodigoTurma());

        registroAulaActivityViewMvcImpl.exibirNomeTurmaTipoEnsino(turmaGrupo.getTurma().getNomeTurma(), turmaGrupo.getTurma().getNomeTipoEnsino());

        registroAulaActivityViewMvcImpl.setarTurmaGrupo(turmaGrupo);

        disciplina = turmaGrupo.getDisciplina().getCodigoDisciplina();

        serie = turmaGrupo.getTurma().getSerie();

        setContentView(registroAulaActivityViewMvcImpl.getRootView());

        disciplinaSelecionada = 0;

        if (disciplina == 1000) {

            registroAulaActivityViewMvcImpl.selecaoDisciplinaAnosIniciais();

            //registroAulaActivityViewMvcImpl.exibirOpcoesAnosIniciais(getSupportFragmentManager());
        }

        listaHabilidadesCheckadas = new ArrayList<>();

        listaCodigosHabilidadesCheckadas = new ArrayList<>();

        listaHabilidadesParaExibir = new ArrayList<>();

        listaHorariosSelecionados = new ArrayList<>();

        criarAcessoBanco = new CriarAcessoBanco();

        calendar = Calendar.getInstance();

        banco = criarAcessoBanco.gerarBanco(getApplicationContext());

        registroDBgetters = new RegistroDBgetters(banco);

        registroDBcrud = new RegistroDBcrud(banco);

        bimestreAtual = registroDBgetters.getBimestre(turmaGrupo.getTurmasFrequencia());

        bimestreAnterior = registroDBgetters.getBimestreAnterior(turmaGrupo.getTurmasFrequencia().getId());

        if(extras.getString("data") != null) {

            usuarioSelecionouDataCalendario(extras.getString("data"), null);

            listaHorariosSelecionados = (ArrayList) extras.getParcelableArrayList("listaHorariosSelecionados");
        }
        else {

            iniciaCaldroid(savedInstanceState, turmaGrupo.getTurma().getCodigoTurma());
        }
    }

    @Override
    public void usuarioSelecionouConteudo(Conteudo conteudo) {

        Conteudo conteudoSelecionado = conteudo;

        listaCodigosHabilidadesCheckadas.clear();

        listaHabilidadesParaExibir.clear();

        pegarHabilidadesDosConteudos(conteudoSelecionado);

        for(int conteudoRegistro = 0; conteudoRegistro < registroAtual.getConteudos().size(); conteudoRegistro++) {

            if(registroAtual.getConteudos().get(conteudoRegistro).getCodigoConteudo() == conteudoSelecionado.getCodigo()) {

                listaCodigosHabilidadesCheckadas = registroAtual.getConteudos().get(conteudoRegistro).getCodigosHabilidades();

                break;
            }
        }

        for(int habilidade = 0; habilidade < listaHabilidadesParaExibir.size(); habilidade++) {

            for(int indiceCodigo = 0; indiceCodigo < listaCodigosHabilidadesCheckadas.size(); indiceCodigo++) {

                if(listaCodigosHabilidadesCheckadas.get(indiceCodigo) == listaHabilidadesParaExibir.get(habilidade).getCodigo()) {

                    boolean estaNaLista = false;

                    for(int habilidadeCheckada = 0; habilidadeCheckada < listaHabilidadesCheckadas.size(); habilidadeCheckada++) {

                        if(listaHabilidadesCheckadas.get(habilidadeCheckada).getCodigo() == listaHabilidadesParaExibir.get(habilidade).getCodigo()
                                && listaHabilidadesCheckadas.get(habilidadeCheckada).getCodigoConteudo() == listaHabilidadesParaExibir.get(habilidade).getCodigoConteudo()) {

                            estaNaLista = true;

                            listaHabilidadesParaExibir.get(habilidade).setChecked(true);

                            break;
                        }
                    }
                    if(!estaNaLista) {

                        listaHabilidadesParaExibir.get(habilidade).setChecked(true);

                        listaHabilidadesCheckadas.add(listaHabilidadesParaExibir.get(habilidade));
                    }
                }
            }
        }

        for(int habilidade = 0; habilidade < listaHabilidadesParaExibir.size(); habilidade++) {

            for(int habilidadeCheckada = 0; habilidadeCheckada < listaHabilidadesCheckadas.size(); habilidadeCheckada++) {

                if (listaHabilidadesCheckadas.get(habilidadeCheckada).getCodigo() == listaHabilidadesParaExibir.get(habilidade).getCodigo()
                        && listaHabilidadesCheckadas.get(habilidadeCheckada).getCodigoConteudo() == listaHabilidadesParaExibir.get(habilidade).getCodigoConteudo()) {

                    listaHabilidadesParaExibir.get(habilidade).setChecked(true);
                }
            }
        }
        registroAulaActivityViewMvcImpl.exibirHabilidades(listaHabilidadesParaExibir);
    }

    @Override
    public void usuarioSelecionouHabilidade(Habilidade habilidade) {

        if(!habilidade.isChecked()) {

            adicionarHabilidadeCheckada(habilidade);
        }
        else {

            removerHabilidadeCheckada(habilidade);
        }
        registroAulaActivityViewMvcImpl.recarregarListasExibidas();
    }

    private void removerHabilidadeCheckada(Habilidade habilidade) {

        for(int codigo = 0; codigo < listaCodigosHabilidadesCheckadas.size(); codigo++) {

            if(listaCodigosHabilidadesCheckadas.get(codigo) == habilidade.getCodigo()) {

                listaCodigosHabilidadesCheckadas.remove(codigo);
            }
        }

        for(int habilidadeCheckada = 0; habilidadeCheckada < listaHabilidadesCheckadas.size(); habilidadeCheckada++) {

            if(listaHabilidadesCheckadas.get(habilidadeCheckada).getCodigo() == habilidade.getCodigo()) {

                listaHabilidadesCheckadas.remove(habilidadeCheckada);
            }
        }

        for(int habilidadeNaLista = 0; habilidadeNaLista < listaHabilidadesParaExibir.size(); habilidadeNaLista++) {

            if(listaHabilidadesParaExibir.get(habilidadeNaLista).getCodigo() == habilidade.getCodigo()) {

                listaHabilidadesParaExibir.get(habilidadeNaLista).setChecked(false);
            }
        }

        boolean temHabilidadeCheckada = false;

        for(int habilidadeCheckada = 0; habilidadeCheckada < listaHabilidadesCheckadas.size(); habilidadeCheckada++) {

            if(listaHabilidadesCheckadas.get(habilidadeCheckada).getCodigoConteudo() != habilidade.getCodigoConteudo()) {

                temHabilidadeCheckada = true;

                for (int conteudo = 0; conteudo < conteudosParaExibir.size(); conteudo++ ) {

                    if(habilidade.getCodigoConteudo() == conteudosParaExibir.get(conteudo).getCodigo()) {

                        conteudosParaExibir.get(conteudo).setHabilidadeCheck(false);
                    }
                }
            }
        }

        if(!temHabilidadeCheckada) {

            for(int conteudo = 0; conteudo < conteudosParaExibir.size(); conteudo++ ) {

                if(habilidade.getCodigoConteudo() == conteudosParaExibir.get(conteudo).getCodigo()) {

                    conteudosParaExibir.get(conteudo).setHabilidadeCheck(false);
                }
            }
        }
    }

    private void adicionarHabilidadeCheckada(Habilidade habilidade) {

        listaCodigosHabilidadesCheckadas.add(habilidade.getCodigo());

        habilidade.setChecked(true);

        listaHabilidadesCheckadas.add(habilidade);

        listaHabilidadesParaExibir.add(habilidade);

        for(int conteudo = 0; conteudo < conteudosParaExibir.size(); conteudo++ ) {

            if(habilidade.getCodigoConteudo() == conteudosParaExibir.get(conteudo).getCodigo()) {

                conteudosParaExibir.get(conteudo).setHabilidadeCheck(true);

                break;
            }
        }
    }

    private void pegarHabilidadesDosConteudos(Conteudo conteudoSelecionado) {

        for(int habilidade = 0; habilidade < grupoCurriculoContHab.getHabilidades().size(); habilidade++) {

            if(grupoCurriculoContHab.getHabilidades().get(habilidade).getCodigoConteudo() == conteudoSelecionado.getCodigo()) {

                listaHabilidadesParaExibir.add(grupoCurriculoContHab.getHabilidades().get(habilidade));
            }
        }
    }

    @Override
    public void abrirOpcoesAnosIniciais() {

        registroAulaActivityViewMvcImpl.exibirOpcoesAnosIniciais(getSupportFragmentManager());
    }

    public void usuarioSelecionouMateriaAnosIniciais(int disciplina) {

        disciplinaSelecionada = disciplina;

        registroAulaActivityViewMvcImpl.removerOpcoesAnosIniciais();

        montarBlocoConteudos(numeroBimestreAtual);
    }

    @Override
    public void navegarPara(Intent intent) {

        startActivity(intent);
    }

    @Override
    public void usuarioQuerFecharSelecaoHorarios() {

        if(listaHorariosSelecionados.size() == 0) {

            registroAulaActivityViewMvcImpl.avisoUsuarioSelecioneUmOuMaisHorarios();
        }
        else {

            if(verificarDisciplina()) {

                montarBlocoConteudos(numeroBimestreAtual);
            }

            registroAulaActivityViewMvcImpl.removerSelecaoHorarios();
        }
    }

    @Override
    public void usuarioSelecionouHorario(String horario) {

        if(verificarDisciplina()) {

            verificarListaHorariosSelecionados(horario, true);

            montarBlocoConteudos(numeroBimestreAtual);
        }

        registroAulaActivityViewMvcImpl.removerSelecaoHorarios();

        if(disciplina == 1000) {

            registroAulaActivityViewMvcImpl.exibirOpcoesAnosIniciais(getSupportFragmentManager());
        }
    }

    @Override
    public void usuarioChecouHorario(String horario) {

        //Controle de limite de aulas por semana deverá ser implementado como melhoria

        /*totalLancamentosSemana = frequenciaDBgetters.getTotalLancamentosNaSemana(turmaGrupo.getDisciplina().getId(), semanaMes, mes1);

        if(aulasPorSemana == listaHorariosSelecionados.size() && !listaHorariosSelecionados.contains(horario)
                || (listaHorariosSelecionados.size() + listaHorariosComLancamentos.size() == aulasPorSemana && !listaHorariosSelecionados.contains(horario))
                || (totalLancamentosSemana + listaHorariosSelecionados.size() >= aulasPorSemana && !listaHorariosSelecionados.contains(horario))) {

            alcancouLimiteSemanaCheckBox = true;

            frequenciaLancamentoViewMvcImpl.usuarioAvisoLimiteSemanalAlcancado();

            frequenciaLancamentoViewMvcImpl.deschecarUltimoHorarioChecado(horario);
        }
        else {*/

            verificarListaHorariosSelecionados(horario, false);
        //}
        //alcancouLimiteSemanaCheckBox = false;
    }

    @Override
    public void usuarioQuerAvancar() {

        if(listaHorariosSelecionados.size() > 0) {

            usuarioSelecionouHorario(listaHorariosSelecionados.get(0));
        }
        else {

            registroAulaActivityViewMvcImpl.avisoUsuarioSelecioneUmOuMaisHorarios();
        }
    }

    private void verificarListaHorariosSelecionados(String horario, boolean selecaoHorario) {

        if(!listaHorariosSelecionados.contains(horario)) {

            listaHorariosSelecionados.add(horario);
        }
        else {

            if(!selecaoHorario) {

                listaHorariosSelecionados.remove(horario);
            }
        }
    }

    public void usuarioSelecionouBloco(int bloco) {

        if(verificarDisciplina()) {

            montarBlocoConteudos(bloco);
        }
        else {

            registroAulaActivityViewMvcImpl.usuarioAvisoSelecioneDisciplina();
        }
    }

    private boolean verificarDisciplina() {

        boolean verificado = false;

        if(disciplina == 1000) {

            if(disciplinaSelecionada != 0) {

                verificado = true;
            }
        }
        else {

            disciplinaSelecionada = disciplina;

            verificado = true;
        }
        return verificado;
    }

    public void salvarRegistro(String observacoes) {

        List<ObjetoConteudo> listaConteudos = new ArrayList<>();

        for(int habilidadeCheckada = 0; habilidadeCheckada < listaHabilidadesCheckadas.size(); habilidadeCheckada++) {

            Habilidade habilidade = listaHabilidadesCheckadas.get(habilidadeCheckada);

            int indiceConteudo = -1;

            for(int indice = 0; indice < listaConteudos.size(); indice++) {

                if(habilidade.getCodigoConteudo() == listaConteudos.get(indice).getCodigoConteudo()) {

                    indiceConteudo = indice;

                    break;
                }
            }

            if(indiceConteudo == -1) {

                ObjetoConteudo conteudo = new ObjetoConteudo();

                conteudo.setCodigoConteudo(habilidade.getCodigoConteudo());

                conteudo.adicionarCodigoHabilidade(habilidade.getCodigo());

                listaConteudos.add(conteudo);
            }
            else {

                listaConteudos.get(indiceConteudo).adicionarCodigoHabilidade(habilidade.getCodigo());
            }
        }

        for(int conteudo = 0; conteudo < listaConteudos.size(); conteudo++) {

            if(listaConteudos.get(conteudo).getCodigoConteudo() == 0) {

                listaConteudos.remove(conteudo);
            }
        }

        if(listaConteudos.isEmpty() && observacoes.equals("")) {

            registroAulaActivityViewMvcImpl.avisoUsuarioSelecioneConteudoObservacao();

            return;
        }
        else {

            criarNovoRegistro(listaConteudos, observacoes);
        }

        Registro registroAntigo = registroDBcrud.existeRegistro(

                registroCriado.getDataCriacao(), Integer.parseInt(registroCriado.getCodigoTurma())
        );

        for(int i = 0; i < listaHorariosSelecionados.size(); i++) {

            registroCriado.setHorariosFormatando(listaHorariosSelecionados.get(i));
        }

        if(registroAntigo == null) {

            registroDBcrud.salvarRegistroBanco(registroCriado);
        }
        else {

            registroDBcrud.atualizarRegistroBanco(registroCriado, registroAntigo);
        }

        registroAulaActivityViewMvcImpl.avisoUsuarioRegistroSalvo();

        listaConteudos.clear();
    }

    private void criarNovoRegistro(List<ObjetoConteudo> conteudos, String observacoes) {

        registroCriado = new Registro();

        registroCriado.setBimestre(registroDBgetters.getBimestreAtual(Integer.parseInt(turma)));
        registroCriado.setCodigoDisciplina(String.valueOf(disciplinaSelecionada));
        registroCriado.setCodigoGrupoCurriculo(String.valueOf(grupoCurriculoContHab.getCodigoGrupo()));
        registroCriado.setCodigoTurma(turma);
        registroCriado.setConteudos(conteudos);
        registroCriado.setDataCriacao(dataCriacao);
        registroCriado.setObservacoes(observacoes);
        registroCriado.setOcorrencias("");
    }

    public void onBackPressed() {

        if (registroAulaActivityViewMvcImpl.conteudosEmExibicao()) {

            if (alterouRegistro) {

                registroAulaActivityViewMvcImpl.avisoAlteracoesPendentes();
            } else {

                finish();
            }
        } else {

            registroAulaActivityViewMvcImpl.exibirConteudos();
        }
    }

    private void montarBlocoConteudos(int bloco) {

        if(dataCriacao == null) {

            registroAulaActivityViewMvcImpl.avisoUsuarioSelecioneData();

            return;
        }

        registroAulaActivityViewMvcImpl.configurarBlocos(bloco);

        bimestreSelecionado = bloco;

        grupoCurriculoContHab = registroDBgetters.getGrupoCurriculoContHab(

                serie, disciplinaSelecionada, bimestreSelecionado
        );

        if(grupoCurriculoContHab != null) {

            buscarRegistroPorData();

            if(registroAtual != null) {

                if(registroAtual.getObservacoes() != null) {

                    registroAulaActivityViewMvcImpl.exibirObservacoes(!registroAtual.getObservacoes().equals("null") ? registroAtual.getObservacoes() : "");
                }

                marcarConteudosCheckados();
            }

            if(listaHabilidadesCheckadas.size() > 0) {

                marcarConteudosCheckadas();
            }

            registroAulaActivityViewMvcImpl.exibirListaConteudos(conteudosParaExibir);

            pegarHabilidadesCheckadas();
        }
        else {

            conteudosParaExibir.clear();

            registroAulaActivityViewMvcImpl.avisoUsuarioNenhumConteudoParaExibir();

            registroAulaActivityViewMvcImpl.exibirListaConteudos(conteudosParaExibir);
        }
    }

    private void marcarConteudosCheckados() {

        for(ObjetoConteudo conteudo : registroAtual.getConteudos()) {

            if(conteudo.getCodigosHabilidades().size() > 0) {

                listaCodigosConteudosCheckados.add(conteudo.getCodigoConteudo());
            }
        }

        for(int conteudo = 0; conteudo < conteudosParaExibir.size(); conteudo++) {

            for(int indiceCodigo = 0; indiceCodigo < listaCodigosConteudosCheckados.size(); indiceCodigo++) {

                if(listaCodigosConteudosCheckados.get(indiceCodigo) == conteudosParaExibir.get(conteudo).getCodigo()) {

                    conteudosParaExibir.get(conteudo).setHabilidadeCheck(true);

                    break;
                }
            }
        }
    }

    private void marcarConteudosCheckadas() {

        for(int conteudo = 0; conteudo < conteudosParaExibir.size(); conteudo++) {

            for(Habilidade habilidadeCheckada : listaHabilidadesCheckadas) {

                if(habilidadeCheckada.getCodigoConteudo() == conteudosParaExibir.get(conteudo).getCodigo()) {

                    conteudosParaExibir.get(conteudo).setHabilidadeCheck(true);

                    break;
                }
            }
        }
    }

    private void buscarRegistroPorData() {

        conteudosParaExibir = new ArrayList<>();

        conteudosParaExibir = grupoCurriculoContHab.getConteudos();

        registroAtual = registroDBgetters.buscarRegistrosDataTurma(

                dataCriacao, turma, registroDBgetters.getBimestreAtual(Integer.parseInt(turma))
        );
    }

    private void pegarHabilidadesCheckadas() {

        int quantidadeCodigosHabilidade = 0;

        for(int conteudo = 0; conteudo < registroAtual.getConteudos().size(); conteudo++) {

            quantidadeCodigosHabilidade = registroAtual.getConteudos().get(conteudo).getCodigosHabilidades().size();

            for(int indiceCodigoHabilidade = 0; indiceCodigoHabilidade < quantidadeCodigosHabilidade; indiceCodigoHabilidade++) {

                Habilidade habilidade;

                habilidade = registroDBgetters.buscarNovoHabilidade(

                        registroAtual.getConteudos().get(conteudo).getCodigoConteudo(),
                        registroAtual.getConteudos().get(conteudo).getCodigosHabilidades().get(indiceCodigoHabilidade)
                );

                boolean existeNaLista = false;

                for(int habilidadeCheckada = 0; habilidadeCheckada < listaHabilidadesCheckadas.size(); habilidadeCheckada++) {

                    if(listaHabilidadesCheckadas.get(habilidadeCheckada).getCodigo() == habilidade.getCodigo()
                            && listaHabilidadesCheckadas.get(habilidadeCheckada).getCodigoConteudo() == habilidade.getCodigoConteudo()) {

                        existeNaLista = true;

                        break;
                    }
                }
                if(!existeNaLista) {

                    listaHabilidadesCheckadas.add(habilidade);
                }
            }
        }
    }

    public void usuarioSelecionouDataCalendario(String data, Date date) {

        int mes = 0;

        int dia = 0;

        int ano = 0;

        if(date != null) {

            mes = DateUtils.getCurrentMonth(date);

            dia = DateUtils.getCurrentDay(date);

            ano = DateUtils.getCurrentYear(date);
        }
        else {

            String[] dataSplit = data.split("/");

            dia = Integer.valueOf(dataSplit[0]);

            mes = Integer.valueOf(dataSplit[1]);

            ano = Integer.valueOf(dataSplit[2]);
        }

        calendar.clear();

        calendar.set(ano, mes - 1, dia);

        int diaSemana = calendar.get(Calendar.DAY_OF_WEEK);

        List<String> listahorario = new ArrayList<>();

        listaAula = registroDBgetters.getAula(turmaGrupo.getDisciplina());

        for(int i = 0; i < listaAula.size(); i++) {

            if(listaAula.get(i).getDiaSemana() + 1 == diaSemana) {

                Aula aula = listaAula.get(i);

                listahorario.add(aula.getInicio() + "/" + aula.getFim());
            }
        }

        dataCriacao = data;

        numeroBimestreAtual = bimestreAtual.getNumero();

        if(date != null) {

            registroAulaActivityViewMvcImpl.esconderFundoBranco(dialogCaldroidFragment);

            registroAulaActivityViewMvcImpl.exibirOpcoesHorarios(listahorario);
        }
        else {

            if(verificarDisciplina()) {

                montarBlocoConteudos(numeroBimestreAtual);
            }

            registroAulaActivityViewMvcImpl.removerSelecaoHorarios();

            if(disciplina == 1000) {

                registroAulaActivityViewMvcImpl.exibirOpcoesAnosIniciais(getSupportFragmentManager());
            }
        }
    }

    public void iniciaCaldroid(Bundle savedInstanceState, int id) {

        try {

            listaAula = registroDBgetters.getAula(turmaGrupo.getDisciplina());

            listaDiaSemana = new ArrayList<>();

            for(Aula aulaFor : listaAula) {

                if(!listaDiaSemana.contains(aulaFor.getDiaSemana())) {

                    listaDiaSemana.add(aulaFor.getDiaSemana());
                }
            }
        }
        catch(Exception e) {

            e.printStackTrace();
        }

        listaDiasLetivos = registroDBgetters.getDiasLetivos(bimestreAtual, listaDiaSemana);

        listaDiasLetivosStr = new ArrayList<>();

        listaDiasMarcados = new ArrayList<>();

        datasRegistros = new ArrayList<>();

        datasRegistros = registroDBgetters.buscarDataRegistros(id);

        if(!datasRegistros.isEmpty()) {

            for (String data : datasRegistros) {

                listaDiasMarcados.add(data.substring(0, 10));
            }
        }

        for(DiasLetivos diasLetivosFor : listaDiasLetivos) {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            try {

                Date date = sdf.parse(diasLetivosFor.getDataAula());

                listaDiasLetivosStr.add(sdf.format(date));

            }
            catch(ParseException e) {

                e.printStackTrace();
            }
        }

        dialogCaldroidFragment = new CaldroidFragment();

        getCaldroid(savedInstanceState);

        listener = getListenerCaldroid();

        getListenerCaldroid();

        dialogCaldroidFragment.setCaldroidListener(listener);

        registroAulaActivityViewMvcImpl.mostrarFundoBranco(dialogCaldroidFragment);
    }

    public void getCaldroid(Bundle savedInstanceState) {

        final String dialogTag = "CALDROID_DIALOG_FRAGMENT";

        if(savedInstanceState != null) {

            dialogCaldroidFragment.restoreDialogStatesFromKey(

                    getSupportFragmentManager(), savedInstanceState,
                    "DIALOG_CALDROID_SAVED_STATE", dialogTag
            );

            Bundle args = dialogCaldroidFragment.getArguments();

            if(args == null) {

                args = new Bundle();

                args.putInt(CaldroidFragment.THEME_RESOURCE, R.style.CalDroidTheme);

                dialogCaldroidFragment.setArguments(args);
            }
        }
        else {

            Bundle bundle = new Bundle();

            bundle.putInt(CaldroidFragment.THEME_RESOURCE, R.style.CalDroidTheme);

            dialogCaldroidFragment.setArguments(bundle);
        }

        //dialogCaldroidFragment.show(getSupportFragmentManager(), dialogTag);
    }

    public void setarListener(CaldroidFragmentListener listener) {

        dialogCaldroidFragment.setCaldroidListener(listener);
    }

    public CaldroidListener getListenerCaldroid() {

        FechamentoData fechamentoData = registroDBgetters.getFechamentoAberto();

        CaldroidFragmentListener listener = new CaldroidFragmentListener(

                dialogCaldroidFragment, bimestreAtual, bimestreAnterior, listaDiaSemana, listaDiasLetivos, listaDiasMarcados,
                turmaGrupo, fechamentoData, this
        );

        return listener;
    }

    @Override
    public void usuarioClicouSairRegistro() {

        finish();
    }

    @Override
    protected void onStart() {

        super.onStart();

        registroAulaActivityViewMvcImpl.registerListenerMenuNavegacao();

        registroAulaActivityViewMvcImpl.registerListener(this);
    }

    @Override
    protected void onResume() {

        super.onResume();

        registroAulaActivityViewMvcImpl.removerProgressBarVoador();
    }

    @Override
    protected void onPause() {

        super.onPause();

        registroAulaActivityViewMvcImpl.removerProgressBarVoador();
    }

    @Override
    protected void onStop() {

        super.onStop();

        registroAulaActivityViewMvcImpl.unregisterListenerMenuNavegacao();

        registroAulaActivityViewMvcImpl.unregisterListener();


    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if(listaCodigosHabilidadesCheckadas != null) {

            listaCodigosHabilidadesCheckadas.clear();
        }

        listaHabilidadesParaExibir.clear();

        listaHabilidadesCheckadas.clear();

        listaCodigosConteudosCheckados.clear();

        if(conteudosParaExibir != null) {

            conteudosParaExibir.clear();
        }
    }
}
