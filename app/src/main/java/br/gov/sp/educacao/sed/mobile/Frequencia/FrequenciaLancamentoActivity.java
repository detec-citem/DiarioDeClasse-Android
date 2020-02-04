package br.gov.sp.educacao.sed.mobile.Frequencia;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.gov.sp.educacao.sed.mobile.Escola.Aula;
import br.gov.sp.educacao.sed.mobile.Escola.Bimestre;
import br.gov.sp.educacao.sed.mobile.Escola.DiasLetivos;
import br.gov.sp.educacao.sed.mobile.Fechamento.FechamentoData;
import br.gov.sp.educacao.sed.mobile.Login.UsuarioTO;
import br.gov.sp.educacao.sed.mobile.R;
import br.gov.sp.educacao.sed.mobile.RegistroDeAula.RegistroAulaActivity;
import br.gov.sp.educacao.sed.mobile.RegistroDeAula.RegistroAulaFundamentalActivity;
import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;
import br.gov.sp.educacao.sed.mobile.util.Analytics;
import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.CriarAcessoBanco;
import br.gov.sp.educacao.sed.mobile.util.DateUtils;

public class FrequenciaLancamentoActivity
        extends AppCompatActivity
         implements FrequenciaLancamentoViewMvc.Listener {

    private boolean alterouFrequencia = false;

    Parcelable state;

    private UsuarioTO usuario;

    private Bimestre bimestre;

    private int diasMes;
    private int anoAtual;
    private int faltasAnuais;
    private int faltasBimestre;
    private int mesSelecionado;
    private int aulasPorSemana;
    private int semanaMes;
    private int mes1;

    private boolean alcancouLimiteLancamentosSemana;
    private boolean alcancouLimiteSemanaCheckBox;

    private String horarioSelecionado;

    private int aulaEspecifica;

    private Date dataAtual;
    private Date dataSelecionada;

    private TurmaGrupo turmaGrupo;

    private int diaLetivo;
    private int totalLancamentosSemana;

    private String data;
    private String dataParaRegistroAula;
    private String siglaFaltou;
    private String horarioExcluir;
    private String siglaCompareceu;
    private String siglaNaoSeAplica;

    private List<Aluno> listaAlunos;
    private List<Aluno> alunosAtivos;

    private Bimestre bimestreAnterior;

    private Bundle savedInstanceState;

    private FechamentoData fechamentoData;

    private List<Integer> listaDiaSemana;

    private List<Aula> listaAula;
    //private List<Aula> listaAulaSelecionada;

    private List<String> listahorario;
    private List<String> listaDiasLetivosStr;
    private List<String> listaHorariosSelecionados;
    private List<String> listaHorariosComLancamentos;

    private Calendar calendar;

    private ArrayList<String> disableDateStrings;

    private CaldroidListener listener1;
    private CaldroidFragment dialogCaldroidFragment;

    private FrequenciaDBcrud frequenciaDBcrud;
    private FrequenciaDBgetters frequenciaDBgetters;
    private FrequenciaDBsetters frequenciaDBsetters;

    private String TAG = FrequenciaLancamentoActivity.class.getSimpleName();

    private FrequenciaLancamentoViewMvcImpl frequenciaLancamentoViewMvcImpl;

    private SQLiteDatabase database;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        frequenciaLancamentoViewMvcImpl = new FrequenciaLancamentoViewMvcImpl(

                LayoutInflater.from(this), getSupportFragmentManager(), null
        );

        this.savedInstanceState = savedInstanceState;

        inicializarGoogleAnalytics();

        inicializarSiglasFrequencia();

        dataAtual = new Date();

        horarioSelecionado = "";

        Bundle bundle = getIntent().getExtras();

        turmaGrupo = bundle.getParcelable(TurmaGrupo.BUNDLE_TURMA_GRUPO);

        frequenciaLancamentoViewMvcImpl.setarTurmaGrupo(turmaGrupo);

        anoAtual = turmaGrupo.getTurma().getAno();

        listaAlunos = turmaGrupo.getTurma().getAlunos();

        inicializarBanco();

        usuario = frequenciaDBgetters.getUsuarioAtivo();

        alunosAtivos = frequenciaDBgetters.getAlunosAtivos(listaAlunos);

        listaAula = frequenciaDBgetters.getAula(turmaGrupo.getDisciplina());

        listaHorariosSelecionados = new ArrayList<>();

        fechamentoData = frequenciaDBgetters.getFechamentoAberto();

        bimestreAnterior = frequenciaDBgetters.getBimestreAnterior(turmaGrupo.getTurmasFrequencia().getId());

        bimestre = frequenciaDBgetters.getBimestre(turmaGrupo.getTurmasFrequencia().getId());

        inicializarCalendario();

        listaDiaSemana = new ArrayList<>();
        listaDiaSemana.add(1);
        listaDiaSemana.add(2);
        listaDiaSemana.add(3);
        listaDiaSemana.add(4);
        listaDiaSemana.add(5);

        popularListaDiasLetivosString();

        ordenarAlunosPorNumeroChamada();

        calendar = Calendar.getInstance();

        aulasPorSemana = frequenciaDBgetters.getAulasPorSemana(turmaGrupo.getTurmasFrequencia().getId());

        totalLancamentosSemana = 0;

        alcancouLimiteLancamentosSemana = false;

        alcancouLimiteSemanaCheckBox = false;

        frequenciaLancamentoViewMvcImpl.exibirDadosAula(turmaGrupo.getTurma().getNomeTurma(), turmaGrupo.getTurma().getNomeTipoEnsino());

        setContentView(frequenciaLancamentoViewMvcImpl.getRootView());
    }

    public void usuarioQuerExcluirFrequencia(String horario) {

        frequenciaLancamentoViewMvcImpl.confirmarExclusaoDeFrequencia(data, horario);
    }

    public void excluirFrequencia(String horario) {

        frequenciaLancamentoViewMvcImpl.inicializaProgress();

        horarioExcluir = horario;

        if(faltasSincronizadasNaDataHorario() > 0) {

            excluirFrequenciasNaSed(horario);
        }
        else {

            excluirFrequenciaNoBancoLocal();
        }
    }

    private int faltasSincronizadasNaDataHorario() {

        return frequenciaDBgetters.getFaltasSincronizadas(diaLetivo, aulaEspecifica);
    }

    private void excluirFrequenciasNaSed(String horario) {

        try {

            FrequenciaEnvio frequenciaEnvio = new FrequenciaEnvio();

            frequenciaEnvio.codigoFrequencia = 0;

            String data1 = formataDataAvaliacao(data);

            frequenciaEnvio.setDataDaAula(data1);

            frequenciaEnvio.setCodigoDisciplina(turmaGrupo.getDisciplina().getCodigoDisciplina());

            frequenciaEnvio.setCodigoTurma(turmaGrupo.getTurma().getCodigoTurma());

            frequenciaEnvio.setHorarioInicioAula(horario.split("/")[0]);

            frequenciaEnvio.setHorarioFimAula(horario.split("/")[1]);

            frequenciaEnvio.setCodigoEscola(turmaGrupo.getTurma().getCodigoEscola());

            ExcluirFrequenciaTask excluirFrequenciaTask = new ExcluirFrequenciaTask(usuario.getToken());

            excluirFrequenciaTask.delegate = frequenciaLancamentoViewMvcImpl;

            excluirFrequenciaTask.execute(frequenciaEnvio);
        }
        catch(Exception e) {

            e.printStackTrace();
        }
    }

    String formataDataAvaliacao(String data) {

        String data1 = "0";

        if(data != null && !data.equals("") ) {

            String dia = (data.split("/")[0].length() < 2 ? "0" + data.split("/")[0] : data.split("/")[0]);

            String mes = (data.split("/")[1].length() < 2 ? "0" + data.split("/")[1] : data.split("/")[1]);

            String ano = data.split("/")[2];

            data1 =  ano + "-" + mes + "-" + dia;
        }

        return data1;
    }

    public void excluirFrequenciaNoBancoLocal() {

        int diaLetivoId = frequenciaDBgetters.getDiaLetivoIdPelosBimestres(bimestre.getId(), bimestreAnterior.getId(), data);

        int aulaId = frequenciaDBgetters.getAulaIdPeloHorario(horarioExcluir.split("/")[0], turmaGrupo.getDisciplina().getId());

        frequenciaDBcrud.excluirFrequencia(diaLetivoId, aulaId);

        for(Aluno aluno : listaAlunos) {

            aluno.setComparecimento("");
        }

        frequenciaLancamentoViewMvcImpl.recarregarLista();

        frequenciaLancamentoViewMvcImpl.usuarioAvisoSucesso();

        frequenciaLancamentoViewMvcImpl.desmarcarHorario(horarioExcluir);

        listaHorariosComLancamentos.remove(horarioExcluir);

        horarioExcluir = "";
    }

    @Override
    protected void onStop() {

        super.onStop();

        frequenciaLancamentoViewMvcImpl.unregisterListenerMenuNavegacao();

        frequenciaLancamentoViewMvcImpl.unregisterListener();
    }

    @Override
    protected void onResume() {

        super.onResume();

        frequenciaLancamentoViewMvcImpl.removerProgressBarVoador();
    }

    @Override
    protected void onStart() {

        super.onStart();

        frequenciaLancamentoViewMvcImpl.registerListenerMenuNavegacao();

        frequenciaLancamentoViewMvcImpl.registerListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listaDiasLetivosStr != null) {
            listaDiasLetivosStr.clear();
        }
        if (listaDiaSemana != null) {
            listaDiaSemana.clear();
        }
        if (disableDateStrings != null) {
            disableDateStrings.clear();
        }
        if (listaAula != null) {
            listaAula.clear();
        }

        if(listahorario != null) {
            listahorario.clear();
        }
        if(listaAlunos != null) {
            listaAlunos.clear();
        }
    }

    public void onBackPressed() {
        if (alterouFrequencia) {
            final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setTitle("Atenção");
            alertBuilder.setMessage("Você tem lançamentos de frequência não salvos! Você tem certeza que deseja continuar?");
            alertBuilder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    database.endTransaction();
                    dialog.dismiss();
                    finish();
                }
            });
            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);
            alertDialog.show();
        }
        else {
            finish();
        }
    }

    private void inicializarBanco() {
        Banco banco = CriarAcessoBanco.gerarBanco(this);
        database = banco.get();
        frequenciaDBgetters = new FrequenciaDBgetters(banco);
        frequenciaDBsetters = new FrequenciaDBsetters(banco);
        frequenciaDBcrud = new FrequenciaDBcrud(banco);
    }

    public void irParaInicioLista() {
        frequenciaLancamentoViewMvcImpl.irParaPrimeiroAluno();
    }

    @Override
    public void aplicarNA(Aluno aluno) {
        if (!aluno.getComparecimento().equals(siglaNaoSeAplica)) {
            alterouFrequencia = true;
            salvarFrequencia(siglaNaoSeAplica, aluno);
        }
    }

    @Override
    public void inicializarCalendario() {

        listener1 = configurarCalendarioDatas();

        dialogCaldroidFragment = new CaldroidFragment();

        dialogCaldroidFragment.setCancelable(true);

        dialogCaldroidFragment.setCaldroidListener(listener1);

        frequenciaLancamentoViewMvcImpl.mostrarFundoBranco(dialogCaldroidFragment);

        checarCalendarioAdicionarDados(savedInstanceState);
    }

    @Override
    public void usuarioClicouConfirmar() {
        int faltas = frequenciaDBgetters.getFaltas(diaLetivo, aulaEspecifica);
        if (alunosAtivos.size() == faltas) {
            alterouFrequencia = false;
            database.setTransactionSuccessful();
            database.endTransaction();
            frequenciaLancamentoViewMvcImpl.avisarUsuarioChamadaRealizada();
            frequenciaLancamentoViewMvcImpl.exibirProgressBarVoador();
            Intent intent;
            if (turmaGrupo.getTurma().getCodigoTipoEnsino() == 14) {
                intent = new Intent(this, RegistroAulaFundamentalActivity.class);
            }
            else {
                intent = new Intent(this, RegistroAulaActivity.class);
            }
            Bundle bundle = new Bundle();
            bundle.putParcelable(TurmaGrupo.BUNDLE_TURMA_GRUPO, turmaGrupo);
            bundle.putParcelableArrayList("listaHorariosSelecionados", (ArrayList) listaHorariosSelecionados);
            bundle.putString("data", dataParaRegistroAula);
            intent.putExtras(bundle);
            navegarPara(intent);
        }
        else {
            frequenciaLancamentoViewMvcImpl.avisarUsuarioChamadaIncompleta();
        }
    }

    @Override
    public void aplicarFalta(Aluno aluno) {
        if (!aluno.getComparecimento().equals(siglaFaltou)) {
            alterouFrequencia = true;
            salvarFrequencia(siglaFaltou, aluno);
        }
    }

    @Override
    public void navegarPara(Intent intent) {
        startActivity(intent);
    }

    public void salvarFrequencia(String frequencia, Aluno aluno) {
        int alunoId = aluno.getId();
        int disciplinaId = turmaGrupo.getDisciplina().getId();
        int usuarioId = usuario.getId();
        aluno.setComparecimento(frequencia);
        if (!listaHorariosSelecionados.isEmpty()) {
            int i;
            int numeroHorariosSelecionados = listaHorariosSelecionados.size();
            for(i = 0; i < numeroHorariosSelecionados; i++) {
                String horarioSelecionado = listaHorariosSelecionados.get(i);
                String[] componentesHorario = horarioSelecionado.split("/");
                if (componentesHorario.length == 2) {
                    String inicioHora = componentesHorario[0].trim();
                    String fimHora = componentesHorario[1].trim();
                    int aulaHorario = frequenciaDBgetters.getAula(inicioHora, fimHora, 0, disciplinaId);
                    frequenciaDBsetters.setComparecimento(usuarioId, diaLetivo, aulaHorario, alunoId, frequencia);
                    if (frequencia.equals(siglaFaltou)) {
                        adicionarFaltasBimestraisAnuais(aluno);
                    }
                    else {
                        subtrairFaltasBimestraisAnuais(aluno);
                    }
                    frequenciaDBsetters.setFaltasAluno(aluno, disciplinaId);
                }
            }
        }
        else {
            frequenciaDBsetters.setComparecimento(usuarioId, diaLetivo, aulaEspecifica, alunoId, frequencia);
            if (frequencia.equals(siglaFaltou)) {
                adicionarFaltasBimestraisAnuais(aluno);
            }
            else {
                subtrairFaltasBimestraisAnuais(aluno);
            }
            frequenciaDBsetters.setFaltasAluno(aluno, disciplinaId);
        }
    }

    @Override
    public void aplicarPresenca(Aluno aluno) {
        if (!aluno.getComparecimento().equals(siglaCompareceu)) {
            alterouFrequencia = true;
            salvarFrequencia(siglaCompareceu, aluno);
        }
    }

    private void inicializarGoogleAnalytics() {
        Analytics.setTela(this, this.getClass().toString());
    }

    private void inicializarSiglasFrequencia() {
        siglaCompareceu = getString(R.string.sigla_compareceu);
        siglaFaltou = getString(R.string.sigla_falta);
        siglaNaoSeAplica = getString(R.string.sigla_nao_se_aplica);
    }

    private void ordenarAlunosPorNumeroChamada() {

        Collections.sort(listaAlunos, new Comparator<Aluno>( ) {

            @Override
            public int compare(Aluno p1, Aluno p2) {

                return p1.getNumeroChamada() - p2.getNumeroChamada();
            }
        });
    }

    private void popularListaDiasLetivosString() {

        ArrayList<DiasLetivos> listaDiasLetivos;

        listaDiasLetivos = frequenciaDBgetters.getDiasLetivos(bimestre, listaDiaSemana);

        listaDiasLetivosStr = new ArrayList<>();

        for(DiasLetivos diasLetivos : listaDiasLetivos) {

            listaDiasLetivosStr.add(diasLetivos.getDataAula());
        }
    }

    @Override
    public void resgatarFaltasAluno(Aluno aluno) {

        frequenciaDBsetters.setFaltasAluno(

                aluno, turmaGrupo.getDisciplina().getId()
        );
    }

    public void irParaProximoAlunoAtivo(int posicao) {
        int i;
        int numeroAlunos = listaAlunos.size();
        for(i = posicao + 1; i < numeroAlunos; i++) {
            if (listaAlunos.get(i).getAlunoAtivo()) {
                posicao = i;
                break;
            }
        }
        frequenciaLancamentoViewMvcImpl.irParaProximoAlunoAtivo(posicao, 0);
    }

    @Override
    public void usuarioQuerReplicarChamada(String data) {

        int diaLetivoSelecionado = frequenciaDBgetters.getDiaLetivo(data);

        int numChamadas = frequenciaDBgetters.getFaltas(diaLetivoSelecionado, aulaEspecifica);

        if(numChamadas < alunosAtivos.size()) {

            frequenciaLancamentoViewMvcImpl.avisarUsuarioChamadaIncompleta();

            return;
        }

        if(alunosAtivos.size() == numChamadas && listahorario.size() > 1) {

            frequenciaLancamentoViewMvcImpl.exibirSelecaoHorariosAula();
        }
        else {

            frequenciaLancamentoViewMvcImpl.avisarUsuarioNaoPossuiHorarios();
        }
    }

    public CaldroidListener configurarCalendarioDatas() {

        final List<String> listaDiasMarcados = frequenciaDBgetters.getDiasComFrequencia(turmaGrupo.getTurma().getCodigoTurma());

        listener1 = new CaldroidListener() {

            @Override
            public void onSelectDate(Date data, View view) {

                int dia = DateUtils.getCurrentDay(data);

                int mes = DateUtils.getCurrentMonth(data);

                int ano = DateUtils.getCurrentYear(data);

                if (mes == mesSelecionado) {

                    FrequenciaLancamentoActivity.this.data = dia + "/" + mes + "/" + ano;

                    usuarioSelecionouDataCalendario(data);

                    irParaInicioLista();

                    dialogCaldroidFragment.dismiss();
                }
            }

            public void onChangeMonth(int month, int year) {

                int diasMesAnterior = DateUtils.totalDiasMes(month - 2, year);

                for(int i = 0; i < diasMesAnterior; i++) {

                    Calendar calendarAnterior = Calendar.getInstance();

                    calendarAnterior.set(year,month - 2,i + 1);

                    Date dataLetivoNext = calendarAnterior.getTime();

                    dialogCaldroidFragment.clearBackgroundResourceForDate(dataLetivoNext);

                    dialogCaldroidFragment.clearTextColorForDate(dataLetivoNext);
                }

                int diasMesNext = DateUtils.totalDiasMes(month, year);

                for(int i = 0; i < diasMesNext; i++) {

                    Calendar calendarNext = Calendar.getInstance();

                    calendarNext.set(year, month,i + 1);

                    Date dataLetivoNext = calendarNext.getTime();

                    dialogCaldroidFragment.clearBackgroundResourceForDate(dataLetivoNext);

                    dialogCaldroidFragment.clearTextColorForDate(dataLetivoNext);
                }

                diasMes = DateUtils.totalDiasMes(month - 1, year);

                Set<Integer> mapaDiasMes = new HashSet<>();

                for (int i = 0; i < diasMes; i++) {

                    mapaDiasMes.add(i + 1);
                }

                ArrayList<Integer> diasLetivosConfirmados = new ArrayList<>();

                Calendar calendar = Calendar.getInstance();

                int anoAtual = calendar.get(Calendar.YEAR);

                SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");

                Date date = null;

                String strDataAtual = sdf.format(calendar.getTime());

                try {

                    dataAtual = sdf.parse(strDataAtual);
                }
                catch(ParseException e) {

                    e.printStackTrace();
                }

                Date inicioBimestreAnterior = null;

                Date fimBimestreAnterior = null;

                try {

                    inicioBimestreAnterior = sdf.parse(bimestreAnterior.getInicio());

                    fimBimestreAnterior = sdf.parse(bimestreAnterior.getFim());
                }
                catch (Exception e) {

                }

                for(int i = 0; i < listaDiasLetivosStr.size(); i++) {

                    try {

                        date = sdf.parse(listaDiasLetivosStr.get(i));
                    }
                    catch (ParseException e) {

                        e.printStackTrace();
                    }

                    Calendar cal = Calendar.getInstance();

                    cal.setTime(date);

                    int diaLetivoMes = cal.get(Calendar.DAY_OF_MONTH);

                    if(month - 1 == cal.get(Calendar.MONTH)) {

                        calendar.set(year,month - 1, diaLetivoMes);

                        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;

                        try {

                            Date inicioBimestre = sdf.parse(bimestre.getInicio());

                            for(int diaDaSemana: listaDiaSemana) {

                                if(diaDaSemana == dayOfWeek) {

                                    String strDataLetivo = sdf.format(calendar.getTime());

                                    Date dataLetivo = sdf.parse(strDataLetivo);

                                    if(((dataLetivo.after(inicioBimestre)
                                            || dataLetivo.equals(inicioBimestre))
                                            && (dataLetivo.before(dataAtual)
                                            || dataLetivo.equals(dataAtual)))
                                            || (fechamentoData != null
                                            && Integer.valueOf(fechamentoData.getCodigoTipoFechamento()) - 4 == (bimestre.getNumero() - 1)
                                            && (dataLetivo.after(inicioBimestreAnterior) || dataLetivo.equals(inicioBimestreAnterior))
                                            && (dataLetivo.before(fimBimestreAnterior) || dataLetivo.equals(fimBimestreAnterior)))) {

                                        if(dataLetivo.equals(dataAtual)) {

                                            dialogCaldroidFragment.setBackgroundResourceForDate(

                                                    R.color.amarelo_dia_letivo, dataLetivo
                                            );

                                            dialogCaldroidFragment.setTextColorForDate(

                                                    R.color.amarelo_texto_dia_letivo, dataLetivo
                                            );
                                        }
                                        else {

                                            dialogCaldroidFragment.setBackgroundResourceForDate(

                                                    R.color.azul_dia_letivo, dataLetivo
                                            );

                                            dialogCaldroidFragment.setTextColorForDate(

                                                    R.color.azul_texto_dia_letivo, dataLetivo
                                            );

                                            Date date1 = sdf.parse(strDataLetivo);

                                            if(listaDiasMarcados.contains(strDataLetivo)) {

                                                dialogCaldroidFragment.setBackgroundResourceForDate(R.color.verde_dia_letivo, date1);

                                                dialogCaldroidFragment.setTextColorForDate(R.color.verde_texto_dia_letivo, date1);
                                            }
                                        }


                                        diasLetivosConfirmados.add(diaLetivoMes);
                                    }
                                }
                            }
                        }
                        catch(ParseException e) {

                            e.printStackTrace();
                        }
                    }
                }

                disableDateStrings = new ArrayList<>();

                for(int diaLetivo : mapaDiasMes) {

                    if(!diasLetivosConfirmados.contains(diaLetivo)) {

                        disableDateStrings.add(

                                DateUtils.parseDateNormal(diaLetivo, month, year)
                        );
                    }
                }

                dialogCaldroidFragment.setDisableDatesFromString(disableDateStrings);

                mesSelecionado = month;

                if (month == 1 && year > anoAtual) {

                    dialogCaldroidFragment.prevMonth();
                }
                if (month == 12 && year < anoAtual) {

                    dialogCaldroidFragment.nextMonth();
                }
            }

            @Override
            public void onCaldroidViewCreated() {

            }
        };

        return listener1;
    }

    @Override
    public void aplicarPresencaParaTodosAlunos(View view) {
    }

    @Override
    public void usuarioQuerFecharSelecaoCalendario() {

        frequenciaLancamentoViewMvcImpl.esconderFundoBranco(dialogCaldroidFragment);
    }

    public void usuarioSelecionouDataCalendario(Date date) {

        state = null;

        frequenciaLancamentoViewMvcImpl.esconderFundoBranco(dialogCaldroidFragment);

        listaHorariosSelecionados.clear();

        int mes = DateUtils.getCurrentMonth(date);

        int dia = DateUtils.getCurrentDay(date);

        int ano = DateUtils.getCurrentYear(date);

        calendar.clear();

        calendar.set(ano, mes - 1, dia);

        semanaMes = calendar.get(Calendar.WEEK_OF_MONTH);

        mes1 = calendar.get(Calendar.MONTH);

        anoAtual = ano;

        data = dia + "/" + mes + "/" + anoAtual;

        dataParaRegistroAula = (dia < 10 ? "0" + dia : dia) + "/" + (mes < 10 ? "0" + mes : mes) + "/" +  ano;

        diaLetivo = frequenciaDBgetters.getDiaLetivoIdPelosBimestres(bimestre.getId(), bimestreAnterior.getId(), data);

        dataSelecionada = date;

        frequenciaLancamentoViewMvcImpl.exibirData(data);

        frequenciaLancamentoViewMvcImpl.mostrarLista();

        frequenciaLancamentoViewMvcImpl.exibirOpcoesHorarios();

        listahorario = new ArrayList<>();

        if (listaAula != null && !listaAula.isEmpty()) {
            int i;
            int numeroAulas = listaAula.size();
            for(i = 0; i < numeroAulas; i++) {
                Aula aula = listaAula.get(i);
                listahorario.add(aula.getInicio() + "/" + aula.getFim());
            }
        }

        if (!listahorario.isEmpty()) {
            frequenciaLancamentoViewMvcImpl.setTelaMaisQueUmHorario(listahorario, listaHorariosSelecionados);
            aulaEspecifica = pegarAulaEspecifica(date, listahorario.get(0));
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(R.string.atencao).setMessage("Você não possui horários cadastrados").setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    dialog.dismiss();
                    finish();
                }
            });
            AlertDialog alerta = builder.create();
            alerta.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);
            alerta.show();
        }
    }

    private void subtrairFaltasBimestraisAnuais(Aluno aluno) {
        faltasBimestre = aluno.getFaltasBimestre();
        faltasAnuais = aluno.getFaltasAnuais();
        faltasBimestre = (faltasBimestre <= 0 ? (0) : (faltasBimestre - 1));
        int disciplinaId = turmaGrupo.getDisciplina().getId();
        faltasAnuais = (faltasAnuais <= 0 ? (0) : (faltasAnuais - 1));
        frequenciaDBsetters.salvarNumeroFaltasBimestre(disciplinaId, faltasBimestre, aluno, database);
        frequenciaDBsetters.salvarNumeroFaltasAnuais(disciplinaId, faltasAnuais, aluno, database);
    }

    @Override
    public void usuarioSelecionouHorario(String horarioAula) {

        aulaEspecifica = pegarAulaEspecifica(dataSelecionada, horarioAula);

        horarioSelecionado = horarioAula;

        irParaInicioLista();

        pegarTipoFrequenciaAlunosPorData(diaLetivo);

        boolean lancamentoExistenteDataHorario = frequenciaDBgetters.verificarSeExistemLancamentosParaHorario(aulaEspecifica, diaLetivo);

        if(lancamentoExistenteDataHorario) {

            verificarTurmaComLancamentoExistente(aulaEspecifica, diaLetivo);

            frequenciaLancamentoViewMvcImpl.usuarioAvisoLancamentoExistente();
        }

        totalLancamentosSemana = frequenciaDBgetters.getTotalLancamentosNaSemana(turmaGrupo.getDisciplina().getId(), semanaMes, mes1);

        alcancouLimiteLancamentosSemana = totalLancamentosSemana >= aulasPorSemana;

        if(!alcancouLimiteLancamentosSemana && !alcancouLimiteSemanaCheckBox) {
            database.beginTransaction();
            presencaParaTodos();
            frequenciaLancamentoViewMvcImpl.exibirListaAlunos(listaAlunos, turmaGrupo);
            frequenciaLancamentoViewMvcImpl.removerSelecaoHorarios();
        }
        else {

            frequenciaLancamentoViewMvcImpl.usuarioAvisoLimiteSemanalAlcancado();

            usuarioQuerFecharSelecaoHorarios();

            inicializarCalendario();
        }
    }

    private void presencaParaTodos() {
        int i;
        boolean temAlunosSemFrequencia = false;
        int numeroAlunos = listaAlunos.size();
        for (i = 0; i < numeroAlunos; i++) {
            Aluno aluno = listaAlunos.get(i);
            if (aluno.getComparecimento().isEmpty()) {
                temAlunosSemFrequencia = true;
                break;
            }
        }
        if (temAlunosSemFrequencia) {
            alterouFrequencia = true;
            for (i = 0; i < numeroAlunos; i++) {
                Aluno aluno = listaAlunos.get(i);
                salvarFrequencia(siglaCompareceu, aluno);
            }
        }
    }

    public void usuarioQuerFecharSelecaoHorarios() {

        horarioSelecionado = "";

        frequenciaLancamentoViewMvcImpl.esconderLista();

        frequenciaLancamentoViewMvcImpl.removerSelecaoHorarios();
    }

    @Override
    public void usuarioQuerAvancar() {
        if (!listaHorariosSelecionados.isEmpty()) {
            usuarioSelecionouHorario(listaHorariosSelecionados.get(0));
            frequenciaLancamentoViewMvcImpl.mostrarPrimeiroHorarioDosSelecionados(listaHorariosSelecionados.get(0));
        }
        else {
            frequenciaLancamentoViewMvcImpl.avisoUsuarioSelecioneHorario();
        }
    }

    private void verificarTurmaComLancamentoExistente(int aulaEspecifica, int diaLetivo) {

        frequenciaDBgetters.getTurmaComLancamentoExistente(aulaEspecifica, diaLetivo);
    }

    @Override
    public List<String> pegarHorariosComLancamentos() {

        listaHorariosComLancamentos = frequenciaDBgetters.getHorariosComLancamento(diaLetivo, turmaGrupo.getTurma().getId());

        return listaHorariosComLancamentos;
    }

    private void adicionarFaltasBimestraisAnuais(Aluno aluno) {
        faltasBimestre = aluno.getFaltasBimestre();
        faltasAnuais = aluno.getFaltasAnuais();
        faltasBimestre = faltasBimestre + 1;
        faltasAnuais = faltasAnuais + 1;
        int disciplinaId = turmaGrupo.getDisciplina().getId();
        frequenciaDBsetters.salvarNumeroFaltasBimestre(disciplinaId, faltasBimestre, aluno, database);
        frequenciaDBsetters.salvarNumeroFaltasAnuais(disciplinaId, faltasAnuais, aluno, database);
    }

    private void pegarTipoFrequenciaAlunosPorData(int diaLetivo) {
        int i;
        int numeroAlunos = listaAlunos.size();
        for (i = 0; i < numeroAlunos; i++) {
            Aluno aluno = listaAlunos.get(i);
            String frequencia = frequenciaDBgetters.getSiglaComparecimento(aluno.getId(), aulaEspecifica, diaLetivo);
            aluno.setComparecimento(frequencia);
        }
    }

    private int pegarAulaEspecifica(Date date, String horarioAula) {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        String inicioHora = horarioAula.split("/")[0].trim();

        String fimHora = horarioAula.split("/")[1].trim();

        return frequenciaDBgetters.getAula(

                inicioHora, fimHora, 0, turmaGrupo.getDisciplina().getId()
        );
    }

    public void usuarioChecouHorario(String horario) {

        totalLancamentosSemana = frequenciaDBgetters.getTotalLancamentosNaSemana(turmaGrupo.getDisciplina().getId(), semanaMes, mes1);

        if(aulasPorSemana == listaHorariosSelecionados.size() && !listaHorariosSelecionados.contains(horario)
                || (listaHorariosSelecionados.size() + listaHorariosComLancamentos.size() == aulasPorSemana && !listaHorariosSelecionados.contains(horario))
                || (totalLancamentosSemana + listaHorariosSelecionados.size() >= aulasPorSemana && !listaHorariosSelecionados.contains(horario))) {

            alcancouLimiteSemanaCheckBox = true;

            frequenciaLancamentoViewMvcImpl.usuarioAvisoLimiteSemanalAlcancado();

            frequenciaLancamentoViewMvcImpl.deschecarUltimoHorarioChecado(horario);
        }
        else {

            if(!listaHorariosSelecionados.contains(horario)) {

                listaHorariosSelecionados.add(horario);
            }
            else {

                listaHorariosSelecionados.remove(horario);
            }
        }
        alcancouLimiteSemanaCheckBox = false;
    }

    public void checarCalendarioAdicionarDados(Bundle savedInstanceState) {

        final String dialogTag = "CALDROID_DIALOG_FRAGMENT";

        if(savedInstanceState != null) {

            dialogCaldroidFragment.restoreDialogStatesFromKey(

                    getSupportFragmentManager(), savedInstanceState,"DIALOG_CALDROID_SAVED_STATE",
                    dialogTag
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
    }

    public void replicarChamadaMultiplosHorarios(String data, String horarioAula) {
        int i;
        int j;
        int disciplinaId = turmaGrupo.getDisciplina().getId();
        int numeroHorariosSelecionados = listaHorariosSelecionados.size();
        for (i = 0; i < numeroHorariosSelecionados; i++) {
            String horario = listaHorariosSelecionados.get(i);
            String[] horarios = horario.split("/");
            if (horarios.length == 2) {
                String inicioHora = horarios[0].trim();
                String fimHora = horarios[1].trim();
                int aulaHorario = frequenciaDBgetters.getAula(inicioHora, fimHora, 0, disciplinaId);
                int numeroAlunosAtivos = alunosAtivos.size();
                for (j = 0; j < numeroAlunosAtivos; j++) {
                    Aluno aluno = alunosAtivos.get(i);
                    int alunoId = aluno.getId();
                    String comparecimento = frequenciaDBgetters.getSiglaComparecimento(alunoId, aulaEspecifica, diaLetivo);
                    frequenciaDBsetters.setComparecimento(usuario.getId(), diaLetivo, aulaHorario, alunoId, comparecimento);
                    Aluno alunoDB = frequenciaDBgetters.getTotalFaltas(aluno, disciplinaId);
                    int faltasBimestre = alunoDB.getFaltasBimestre();
                    int faltasAnuais = alunoDB.getFaltasAnuais();
                    if (comparecimento.equals("F")) {
                        if (faltasBimestre > 0) {
                            frequenciaDBsetters.salvarNumeroFaltasBimestre(disciplinaId, faltasBimestre + 1, aluno, database);
                        }
                        if(faltasAnuais > 0) {
                            frequenciaDBsetters.salvarNumeroFaltasAnuais(disciplinaId,faltasAnuais + 1, aluno, database);
                        }
                    }
                }
            }
        }
    }
}