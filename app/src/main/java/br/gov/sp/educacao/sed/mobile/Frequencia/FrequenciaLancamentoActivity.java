package br.gov.sp.educacao.sed.mobile.Frequencia;

import java.util.Set;
import java.util.Date;
import java.util.List;
import java.util.HashSet;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;

import android.content.Intent;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.LayoutInflater;

import br.gov.sp.educacao.sed.mobile.R;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import br.gov.sp.educacao.sed.mobile.RegistroDeAula.RegistroAulaActivity;
import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;

import br.gov.sp.educacao.sed.mobile.Escola.Bimestre;

import br.gov.sp.educacao.sed.mobile.Login.UsuarioTO;

import br.gov.sp.educacao.sed.mobile.Escola.Aula;
import br.gov.sp.educacao.sed.mobile.Escola.DiasLetivos;

import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.DateUtils;
import br.gov.sp.educacao.sed.mobile.util.Analytics;
import br.gov.sp.educacao.sed.mobile.util.CriarAcessoBanco;

import br.gov.sp.educacao.sed.mobile.Fechamento.FechamentoData;

public class FrequenciaLancamentoActivity
        extends AppCompatActivity
         implements FrequenciaLancamentoViewMvc.Listener {

    Parcelable state;

    private UsuarioTO usuario;

    private Bimestre bimestre;

    private int diasMes;
    private int anoAtual;
    private int faltasAnuais;
    private int faltasBimestre;
    private int mesSelecionado;
    private int aulasPorSemana;
    private int diaSemana;
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

    private Banco banco;

    @SuppressWarnings("FieldCanBeLocal")
    private CriarAcessoBanco criarAcessoBanco;

    private Calendar calendar;

    private ArrayList<String> disableDateStrings;

    private CaldroidListener listener1;
    private CaldroidFragment dialogCaldroidFragment;

    private FrequenciaDBcrud frequenciaDBcrud;
    private FrequenciaDBgetters frequenciaDBgetters;
    private FrequenciaDBsetters frequenciaDBsetters;

    private String TAG = FrequenciaLancamentoActivity.class.getSimpleName();

    private FrequenciaLancamentoViewMvcImpl frequenciaLancamentoViewMvcImpl;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        frequenciaLancamentoViewMvcImpl = new FrequenciaLancamentoViewMvcImpl(

                LayoutInflater.from(this), getSupportFragmentManager(), checarTamanhoTela(), null
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

        popularListaDiasSemana();

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

    private int checarTamanhoTela() {

        int width  = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        int dpi    = Resources.getSystem().getDisplayMetrics().densityDpi;

        int telaPequena = 0;

        WindowManager windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);

        final Display display = windowManager.getDefaultDisplay();

        Point outPoint = new Point();

        if (Build.VERSION.SDK_INT >= 19) {

            display.getRealSize(outPoint);
        }
        else {

            display.getSize(outPoint);
        }

        if(height >= 880 && height < 1000) {

            telaPequena = 1; //Tela maior que 4.3 e menor que 5
        }
        else if(height < 880) {

            telaPequena = 2; //Tela menor que 4.3
        }

        Log.e(TAG, "Medidas - " + "Width: " + width + " Height: " + height + " Dpi: " + dpi);
        Log.e(TAG, "Medidas - " + "Height: " + outPoint.y);

        return telaPequena;
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

        listaAlunos.clear();
        listaAula.clear();
        listaDiasLetivosStr.clear();
        listaDiaSemana.clear();
        disableDateStrings.clear();

        if(listahorario != null) {

            listahorario.clear();
        }

        if(listaAlunos != null) {

            listaAlunos.clear();
        }

        /*if(listaAulaSelecionada != null) {

            listaAulaSelecionada.clear();
        }*/
    }

    public void onBackPressed() {

        finish();
    }

    private void inicializarBanco() {

        criarAcessoBanco = new CriarAcessoBanco();

        banco = criarAcessoBanco.gerarBanco(getApplicationContext());

        frequenciaDBgetters = new FrequenciaDBgetters(banco);

        frequenciaDBsetters = new FrequenciaDBsetters(banco);

        frequenciaDBcrud = new FrequenciaDBcrud(banco);
    }

    public void irParaInicioLista() {

        frequenciaLancamentoViewMvcImpl.irParaPrimeiroAluno();
    }

    @Override
    public void aplicarNA(Aluno aluno) {

        if(!aluno.getComparecimento().equals("N")) {

            salvarNaoAplica(aluno);
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

        List<Aluno> alunos = frequenciaDBgetters.getAlunosAtivos(listaAlunos);

        if(alunos.size() == faltas) {

            frequenciaLancamentoViewMvcImpl.avisarUsuarioChamadaRealizada();

            frequenciaLancamentoViewMvcImpl.exibirProgressBarVoador();

            Intent intent = new Intent(this, RegistroAulaActivity.class);

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

    public void salvarFalta(Aluno aluno) {

        banco.get().beginTransaction();

        if(listaHorariosSelecionados.size() > 0) {

            for(int i = 0; i < listaHorariosSelecionados.size(); i++) {

                String inicioHora = listaHorariosSelecionados.get(i).split("/")[0].trim();

                String fimHora = listaHorariosSelecionados.get(i).split("/")[1].trim();

                int aulaHorario = frequenciaDBgetters.getAula(

                        inicioHora, fimHora, 0, turmaGrupo.getDisciplina().getId()
                );

                frequenciaDBsetters.setComparecimento(

                        usuario.getId(), diaLetivo, aulaHorario, aluno.getId(), "F"
                );

                if(!aluno.getComparecimento().equals("F")) {

                    adicionarFaltasBimestraisAnuais(aluno);
                }

                aluno.setComparecimento(siglaFaltou);

                frequenciaDBsetters.setFaltasAluno(

                        aluno, turmaGrupo.getDisciplina().getId()
                );
            }
        }

        else {

            frequenciaDBsetters.setComparecimento(

                    usuario.getId(), diaLetivo, aulaEspecifica, aluno.getId(), "F"
            );

            if(!aluno.getComparecimento().equals("F")) {

                adicionarFaltasBimestraisAnuais(aluno);
            }

            aluno.setComparecimento(siglaFaltou);

            frequenciaDBsetters.setFaltasAluno(

                    aluno, turmaGrupo.getDisciplina().getId()
            );
        }

        banco.get().setTransactionSuccessful();

        banco.get().endTransaction();
    }

    private void popularListaDiasSemana() {

        listaDiaSemana = new ArrayList<>();

        for(Aula aulaFor : listaAula) {

            if (!listaDiaSemana.contains(aulaFor.getDiaSemana())) {

                listaDiaSemana.add(aulaFor.getDiaSemana());
            }
        }
    }

    @Override
    public void aplicarFalta(Aluno aluno) {

        if(!aluno.getComparecimento().equals("F")) {

            salvarFalta(aluno);
        }
    }

    @Override
    public void navegarPara(Intent intent) {

        startActivity(intent);
    }

    public void salvarPresenca(Aluno aluno) {

        banco.get().beginTransaction();

        if(listaHorariosSelecionados.size() > 0) {

            for(int i = 0; i < listaHorariosSelecionados.size(); i++) {

                String inicioHora = listaHorariosSelecionados.get(i).split("/")[0].trim();

                String fimHora = listaHorariosSelecionados.get(i).split("/")[1].trim();

                int aulaHorario = frequenciaDBgetters.getAula(

                        inicioHora, fimHora, 0, turmaGrupo.getDisciplina().getId()
                );

                frequenciaDBsetters.setComparecimento(

                        usuario.getId(), diaLetivo, aulaHorario, aluno.getId(), siglaCompareceu
                );

                if(aluno.getComparecimento().equals("F")) {

                    subtrairFaltasBimestraisAnuais(aluno);
                }

                aluno.setComparecimento(siglaCompareceu);

                frequenciaDBsetters.setFaltasAluno(

                        aluno, turmaGrupo.getDisciplina().getId()
                );
            }
        }
        else {

            frequenciaDBsetters.setComparecimento(

                    usuario.getId(), diaLetivo, aulaEspecifica, aluno.getId(), siglaCompareceu
            );

            if(aluno.getComparecimento().equals("F")) {

                subtrairFaltasBimestraisAnuais(aluno);
            }

            aluno.setComparecimento(siglaCompareceu);

            frequenciaDBsetters.setFaltasAluno(

                    aluno, turmaGrupo.getDisciplina().getId()
            );
        }

        banco.get().setTransactionSuccessful();

        banco.get().endTransaction();
    }

    public void salvarPresencaTodosAlunos() {

        banco.get().beginTransaction();

        for(int i = 0; i <= listaAlunos.size() - 1; i++) {

            if(listaAlunos.get(i).getAlunoAtivo()) {

                salvarPresenca(listaAlunos.get(i));
            }
        }

        banco.get().setTransactionSuccessful();

        banco.get().endTransaction();

        frequenciaLancamentoViewMvcImpl.recarregarLista();
    }

    public void salvarNaoAplica(Aluno aluno) {

        banco.get().beginTransaction();

        frequenciaDBsetters.setFaltasAluno(aluno, turmaGrupo.getDisciplina().getId());

        faltasBimestre = aluno.getFaltasBimestre();

        faltasAnuais = aluno.getFaltasAnuais();

        frequenciaDBsetters.setComparecimento(

                usuario.getId(), diaLetivo, aulaEspecifica, aluno.getId(), siglaNaoSeAplica
        );

        if(aluno.getComparecimento().equals("F")) {

            subtrairFaltasBimestraisAnuais(aluno);
        }

        aluno.setComparecimento(siglaNaoSeAplica);

        frequenciaDBsetters.setFaltasAluno(

                aluno, turmaGrupo.getDisciplina().getId()
        );

        banco.get().setTransactionSuccessful();

        banco.get().endTransaction();
    }

    @Override
    public void aplicarPresenca(Aluno aluno) {

        banco.get().beginTransaction();

        if(!aluno.getComparecimento().equals("C")) {

            salvarPresenca(aluno);
        }

        banco.get().setTransactionSuccessful();

        banco.get().endTransaction();
    }

    private void inicializarGoogleAnalytics() {

        Analytics.setTela(this, this.getClass().toString());
    }

    private void inicializarSiglasFrequencia() {

        siglaCompareceu = getResources().getString(R.string.sigla_compareceu);

        siglaFaltou = getResources().getString(R.string.sigla_falta);

        siglaNaoSeAplica = getResources().getString(R.string.sigla_nao_se_aplica);
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

    public void irParaProximoAlunoAtivo(Aluno aluno) {

        int index = aluno.getNumeroChamada() - 1;

        int count = 1;

        for(int i = index + 1; i < listaAlunos.size(); i++) {

            if(listaAlunos.get(i).getAlunoAtivo()) {

                index = i;

                break;
            }
            else {

                count++;
            }
        }
        frequenciaLancamentoViewMvcImpl.irParaProximoAlunoAtivo(index + 1, count);
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

                banco.get().beginTransaction();

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

                banco.get().setTransactionSuccessful();

                banco.get().endTransaction();

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

        if(diaLetivo == 0 || horarioSelecionado.equals("")) {

            frequenciaLancamentoViewMvcImpl.avisarUsuarioSelecionarData();
        }
        else {

            salvarPresencaTodosAlunos();
        }
    }

    @Override
    public void usuarioQuerFecharSelecaoCalendario() {

        frequenciaLancamentoViewMvcImpl.esconderFundoBranco(dialogCaldroidFragment);
    }

    public void usuarioSelecionouDataCalendario(Date date) {

        frequenciaLancamentoViewMvcImpl.esconderFundoBranco(dialogCaldroidFragment);

        listaHorariosSelecionados.clear();

        int mes = DateUtils.getCurrentMonth(date);

        int dia = DateUtils.getCurrentDay(date);

        int ano = DateUtils.getCurrentYear(date);

        calendar.clear();

        calendar.set(ano, mes - 1, dia);

        diaSemana = calendar.get(Calendar.DAY_OF_WEEK);

        semanaMes = calendar.get(Calendar.WEEK_OF_MONTH);

        mes1 = calendar.get(Calendar.MONTH);

        listahorario = new ArrayList<>();

        for(int i = 0; i < listaAula.size(); i++) {

            if(listaAula.get(i).getDiaSemana() + 1 == diaSemana) {

                Aula aula = listaAula.get(i);

                listahorario.add(aula.getInicio() + "/" + aula.getFim());
            }
        }

        anoAtual = ano;

        data = dia + "/" + mes + "/" + anoAtual;

        dataParaRegistroAula = (dia < 10 ? "0" + dia : dia) + "/" + (mes < 10 ? "0" + mes : mes) + "/" +  ano;

        diaLetivo = frequenciaDBgetters.getDiaLetivoIdPelosBimestres(bimestre.getId(), bimestreAnterior.getId(), data);

        frequenciaLancamentoViewMvcImpl.setTelaMaisQueUmHorario(listahorario, listaHorariosSelecionados);

        aulaEspecifica = pegarAulaEspecifica(date, listahorario.get(0));

        dataSelecionada = date;

        frequenciaLancamentoViewMvcImpl.exibirData(data);

        frequenciaLancamentoViewMvcImpl.mostrarLista();

        frequenciaLancamentoViewMvcImpl.exibirOpcoesHorarios();

        state = null;
    }

    private void subtrairFaltasBimestraisAnuais(Aluno aluno) {

        faltasBimestre = aluno.getFaltasBimestre();

        faltasAnuais = aluno.getFaltasAnuais();

        faltasBimestre = (faltasBimestre < 0 ? (0) : (faltasBimestre - 1));

        banco.get().beginTransaction();

        frequenciaDBsetters.setNumeroFaltasBimestre(

                aluno, turmaGrupo.getDisciplina().getId(), faltasBimestre
        );

        faltasAnuais = (faltasAnuais < 0 ? (0) : (faltasAnuais - 1));

        frequenciaDBsetters.setNumeroFaltasAnuais(

                aluno, turmaGrupo.getDisciplina().getId(), faltasAnuais
        );

        banco.get().setTransactionSuccessful();

        banco.get().endTransaction();
    }

    @Override
    public void usuarioSelecionouHorario(String horarioAula) {

        aulaEspecifica = pegarAulaEspecifica(dataSelecionada, horarioAula);

        horarioSelecionado = horarioAula;

        irParaInicioLista();

        pegarTipoFrequenciaAlunosPorData(diaLetivo);

        boolean lancamentoExistenteDataHorario = false;

        lancamentoExistenteDataHorario = frequenciaDBgetters.verificarSeExistemLancamentosParaHorario(aulaEspecifica, diaLetivo);

        if(lancamentoExistenteDataHorario) {

            verificarTurmaComLancamentoExistente(aulaEspecifica, diaLetivo);

            frequenciaLancamentoViewMvcImpl.usuarioAvisoLancamentoExistente();
        }

        totalLancamentosSemana = frequenciaDBgetters.getTotalLancamentosNaSemana(turmaGrupo.getDisciplina().getId(), semanaMes, mes1);

        alcancouLimiteLancamentosSemana = totalLancamentosSemana >= aulasPorSemana;

        if(!alcancouLimiteLancamentosSemana && !alcancouLimiteSemanaCheckBox) {

            frequenciaLancamentoViewMvcImpl.exibirListaAlunos(listaAlunos, turmaGrupo);

            frequenciaLancamentoViewMvcImpl.removerSelecaoHorarios();
        }
        else {

            frequenciaLancamentoViewMvcImpl.usuarioAvisoLimiteSemanalAlcancado();

            usuarioQuerFecharSelecaoHorarios();

            inicializarCalendario();
        }
    }

    public void usuarioQuerFecharSelecaoHorarios() {

        horarioSelecionado = "";

        frequenciaLancamentoViewMvcImpl.esconderLista();

        frequenciaLancamentoViewMvcImpl.removerSelecaoHorarios();
    }

    @Override
    public void usuarioQuerAvancar() {

        if(listaHorariosSelecionados.size() > 0) {

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

        faltasBimestre = (faltasBimestre < 0 ? (0) : (faltasBimestre + 1));

        banco.get().beginTransaction();

        frequenciaDBsetters.setNumeroFaltasBimestre(

                aluno, turmaGrupo.getDisciplina().getId(), (faltasBimestre)
        );

        faltasAnuais = (faltasAnuais < 0 ? (0) : (faltasAnuais + 1));

        frequenciaDBsetters.setNumeroFaltasAnuais(

                aluno, turmaGrupo.getDisciplina().getId(), (faltasAnuais)
        );

        banco.get().setTransactionSuccessful();

        banco.get().endTransaction();
    }

    public void pegarTipoFrequenciaAlunosPorData(int diaLetivo) {

        banco.get().beginTransaction();

        for(Aluno aluno : listaAlunos) {

            aluno.setComparecimento(frequenciaDBgetters.getSiglaComparecimento(

                    aluno.getId(), aulaEspecifica, diaLetivo)
            );
        }

        banco.get().setTransactionSuccessful();

        banco.get().endTransaction();
    }

    private int pegarAulaEspecifica(Date date, String horarioAula) {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        int diaSemana = 0;//calendar.get(Calendar.DAY_OF_WEEK) - 1;

        String inicioHora = horarioAula.split("/")[0].trim();

        String fimHora = horarioAula.split("/")[1].trim();

        return frequenciaDBgetters.getAula(

                inicioHora, fimHora, diaSemana, turmaGrupo.getDisciplina().getId()
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

        //DiasLetivos diaLetivoSelecionado = frequenciaDBgetters.getDiaLetivo(data);

        //int diaLetivoSelecionado = frequenciaDBgetters.getDiaLetivo(data);

        banco.get().beginTransaction();

        for(String horario : listaHorariosSelecionados) {

            //if(!horario.equals(horarioAula)) {

                //Calendar calendar = Calendar.getInstance();

                //calendar.setTime(dataSelecionada);

                //int diaSemana = calendar.get(Calendar.DAY_OF_WEEK) - 1;

                String inicioHora = horario.split("/")[0].trim();

                String fimHora = horario.split("/")[1].trim();

                int aulaHorario = frequenciaDBgetters.getAula(

                        inicioHora, fimHora, 0, turmaGrupo.getDisciplina().getId()
                );

                for(Aluno aluno : alunosAtivos) {

                    String comparecimento = frequenciaDBgetters.getSiglaComparecimento(

                            aluno.getId(), aulaEspecifica, diaLetivo
                    );

                    frequenciaDBsetters.setComparecimento(

                            usuario.getId(), diaLetivo, aulaHorario, aluno.getId(), comparecimento
                    );

                    Aluno alunoDB = frequenciaDBgetters.getTotalFaltas(

                            aluno, turmaGrupo.getDisciplina().getId()
                    );

                    int faltasBimestre = alunoDB.getFaltasBimestre();

                    int faltasAnuais = alunoDB.getFaltasAnuais();

                    if(comparecimento.equals("F")) {

                        if(faltasBimestre > 0) {

                            frequenciaDBsetters.setNumeroFaltasBimestre(

                                    aluno, turmaGrupo.getDisciplina().getId(),faltasBimestre + 1
                            );
                        }

                        if(faltasAnuais > 0) {

                            frequenciaDBsetters.setNumeroFaltasAnuais(

                                    aluno, turmaGrupo.getDisciplina().getId(),faltasAnuais + 1
                            );
                        }
                    }
                }
           // }
        }

        banco.get().setTransactionSuccessful();

        banco.get().endTransaction();
    }
}