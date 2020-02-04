package br.gov.sp.educacao.sed.mobile.RegistroDeAula;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.SimpleArrayMap;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import br.gov.sp.educacao.sed.mobile.Escola.Aula;
import br.gov.sp.educacao.sed.mobile.Escola.Bimestre;
import br.gov.sp.educacao.sed.mobile.Escola.DiasLetivos;
import br.gov.sp.educacao.sed.mobile.Escola.Disciplina;
import br.gov.sp.educacao.sed.mobile.Fechamento.FechamentoData;
import br.gov.sp.educacao.sed.mobile.Frequencia.FragmentHorarios;
import br.gov.sp.educacao.sed.mobile.Menu.ConteudoFundamental;
import br.gov.sp.educacao.sed.mobile.Menu.ConteudoFundamentalDao;
import br.gov.sp.educacao.sed.mobile.Menu.HomeActivity;
import br.gov.sp.educacao.sed.mobile.R;
import br.gov.sp.educacao.sed.mobile.Turmas.Turma;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;
import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.BdSED;
import br.gov.sp.educacao.sed.mobile.util.ListenerCalendario.CaldroidFragmentListener;
import com.crashlytics.android.Crashlytics;
import com.roomorama.caldroid.CaldroidFragment;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RegistroAulaFundamentalActivity extends AppCompatActivity implements AnimationListener, OnClickListener, ConteudoFundamentalAdapter.OnConteudoFundamentalSelecionadoListener, DialogAnosIniciais.Listener, FragmentHorarios.fragmentHorariosListener, MenuNavegacao.usuarioSelecionouMenuNavegacao, HabilidadeFundamentalAdapter.OnHabilidadeFundamentalSelecionadoListener {
    //Variáveis
    private boolean habilidades;
    private boolean menuAberto;
    private int bimestreSelecionado;
    private int numeroBimestreAtual;
    private int serie;
    private String dataCriacao;
    private String observacoes;
    private String objetoConhecimentoSelecionado;
    private Calendar calendar;
    private Bundle bundle;
    private EditText observacoesEditText;
    private FrameLayout frameLayout;
    private FrameLayout frameLayout2;
    private FrameLayout frameLayout3;
    private ListView conteudoListView;
    private TextView selecionarBimestreTextView;
    private TextView txtContHab;
    private TextView bloco1TextView;
    private TextView bloco2TextView;
    private TextView bloco3TextView;
    private TextView bloco4TextView;
    private Animation apenasRemoverMenuNavegacao;
    private Banco banco;
    private Bimestre bimestreAtual;
    private Disciplina disciplina;
    private Disciplina disciplinaSelecionada;
    private TurmaGrupo turmaGrupo;
    private RegistroAulaFundamental registroAtual;
    private SparseArrayCompat<List<String>> objetosDoConhecimento;
    private SparseArrayCompat<SimpleArrayMap<String, List<ConteudoFundamental>>> conteudos;
    private SparseArrayCompat<SimpleArrayMap<String, List<ConteudoFundamental>>> conteudosSelecionados;
    private List<String> listaHorariosSelecionados;
    private List<Aula> aulas;
    private RegistroDBgetters registroDBgetters;
    private CaldroidFragment dialogCaldroidFragment;
    private DialogAnosIniciais dialogAnosIniciais;
    private FragmentHorarios selecaoHorarios;
    private MenuNavegacao menuNavegacao;

    //Lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_hab_comp2);
        menuAberto = false;
        bundle = new Bundle();
        findViewById(R.id.fecharselecaocalendario).setOnClickListener(this);
        findViewById(R.id.listHabilidades).setVisibility(View.GONE);
        conteudoListView = findViewById(R.id.listConteudo);
        bloco1TextView = findViewById(R.id.btn_1bim);
        bloco2TextView = findViewById(R.id.btn_2bim);
        bloco3TextView = findViewById(R.id.btn_3bim);
        bloco4TextView = findViewById(R.id.btn_4bim);
        frameLayout = findViewById(R.id.container);
        frameLayout2 = findViewById(R.id.container1);
        frameLayout3 = findViewById(R.id.container2);
        observacoesEditText = findViewById(R.id.et_observacoes);
        Button observacoesButton = findViewById(R.id.editTxt1);
        Button selecionarDisciplinaButton = findViewById(R.id.selecionarDisciplina);
        Button salvarRegistroButton = findViewById(R.id.btn_salvar_registro);
        selecionarBimestreTextView = findViewById(R.id.txt_seleciona_bimestre);
        txtContHab = findViewById(R.id.registro_txtContHab);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView turmaTextView = findViewById(R.id.tv_turma);
        txtContHab.setText(R.string.conteudos);
        toolbar.setNavigationIcon(R.drawable.icone_voltar);
        bloco1TextView.setOnClickListener(this);
        bloco2TextView.setOnClickListener(this);
        bloco3TextView.setOnClickListener(this);
        bloco4TextView.setOnClickListener(this);
        observacoesButton.setOnClickListener(this);
        salvarRegistroButton.setOnClickListener(this);
        selecionarDisciplinaButton.setOnClickListener(this);
        menuNavegacao = new MenuNavegacao();
        selecaoHorarios = new FragmentHorarios();
        selecaoHorarios.registerListener(this);
        View toolbarView = LayoutInflater.from(this).inflate(R.layout.layout_toolbar, null, false);
        TextView tituloTextView = toolbarView.findViewById(R.id.txt_toolbar_title);
        tituloTextView.setText("Registro de Aula");
        TextView menu = toolbarView.findViewById(R.id.menu);
        menu.setVisibility(View.VISIBLE);
        menu.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(this);
        toolbar.addView(toolbarView);
        apenasRemoverMenuNavegacao = AnimationUtils.loadAnimation(this, R.anim.remover_menu_navegacao);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        turmaGrupo = extras.getParcelable(TurmaGrupo.BUNDLE_TURMA_GRUPO);
        Turma turma = turmaGrupo.getTurma();
        turmaTextView.setText(turma.getNomeTurma() + " / " + turma.getNomeTipoEnsino());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.move2, R.anim.slideback);
        bundle.putInt("NivelTela", 1);
        bundle.putParcelable(TurmaGrupo.BUNDLE_TURMA_GRUPO, turmaGrupo);
        menuNavegacao.setArguments(bundle);
        menuNavegacao.registerListenerMenuNavegacao(this);
        fragmentTransaction.add(R.id.container, menuNavegacao, "MenuNavegacao");
        fragmentTransaction.commit();
        disciplina = turmaGrupo.getDisciplina();
        serie = turmaGrupo.getTurma().getSerie();
        calendar = Calendar.getInstance();
        if (disciplina != null && disciplina.getCodigoDisciplina() == 1000) {
            selecionarDisciplinaButton.setVisibility(View.VISIBLE);
            selecionarBimestreTextView.setText("Selecione a disciplina");
        }
        int ano = calendar.get(Calendar.YEAR);
        listaHorariosSelecionados = new ArrayList<>();
        banco = new Banco(new BdSED(this));
        registroDBgetters = new RegistroDBgetters(banco);
        bimestreAtual = registroDBgetters.getBimestre(turmaGrupo.getTurmasFrequencia());
        Bimestre bimestreAnterior = registroDBgetters.getBimestreAnterior(turmaGrupo.getTurmasFrequencia().getId());
        if (extras.getString("data") != null) {
            usuarioSelecionouDataCalendario(extras.getString("data"), null);
            listaHorariosSelecionados = (ArrayList) extras.getParcelableArrayList("listaHorariosSelecionados");
        }
        else {
            int i;
            List<Integer> listaDiaSemana = new ArrayList<>();
            listaDiaSemana.add(1);
            listaDiaSemana.add(2);
            listaDiaSemana.add(3);
            listaDiaSemana.add(4);
            listaDiaSemana.add(5);
            aulas = registroDBgetters.getAula(turmaGrupo.getDisciplina());
            List<DiasLetivos> diasLetivos1 = registroDBgetters.getDiasLetivos(bimestreAtual, listaDiaSemana);
            List<String> diasLetivosString = new ArrayList<>();
            List<String> diasMarcados = new ArrayList<>();
            List<String> datasRegistros = registroDBgetters.buscarDataRegistros(turma.getCodigoTurma());
            if (!datasRegistros.isEmpty()) {
                int numeroDatas = datasRegistros.size();
                for (i = 0; i < numeroDatas; i++) {
                    String data = datasRegistros.get(i);
                    diasMarcados.add(data.substring(0, 10));
                }
            }
            int numeroDiasLetivo = diasLetivos1.size();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));
            for (i = 0; i < numeroDiasLetivo; i++) {
                DiasLetivos diasLetivos = diasLetivos1.get(i);
                try {
                    Date date = sdf.parse(diasLetivos.getDataAula());
                    diasLetivosString.add(sdf.format(date));
                }
                catch (ParseException e) {
                    Crashlytics.logException(e);
                }
            }
            dialogCaldroidFragment = new CaldroidFragment();
            String dialogTag = "CALDROID_DIALOG_FRAGMENT";
            if (savedInstanceState != null) {
                dialogCaldroidFragment.restoreDialogStatesFromKey(getSupportFragmentManager(), savedInstanceState, "DIALOG_CALDROID_SAVED_STATE", dialogTag);
                Bundle args = dialogCaldroidFragment.getArguments();
                if (args == null) {
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
            FechamentoData fechamentoData = registroDBgetters.getFechamentoAberto();
            CaldroidFragmentListener listener = new CaldroidFragmentListener(dialogCaldroidFragment, bimestreAtual, bimestreAnterior, diasLetivos1, diasMarcados, fechamentoData, this);
            dialogCaldroidFragment.setCaldroidListener(listener);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.container3, dialogCaldroidFragment, "FragmentCalendario");
            fragmentTransaction.show(dialogCaldroidFragment);
            fragmentTransaction.commit();
            frameLayout3.setVisibility(View.VISIBLE);
        }
    }

    public void usuarioSelecionouDataCalendario(String data, Date date) {
        int i;
        int dia = 0;
        int mes = 0;
        int ano = 0;
        dataCriacao = data;
        numeroBimestreAtual = bimestreAtual.getNumero();
        if(date != null) {
            calendar.setTime(date);
            dia = calendar.get(Calendar.DAY_OF_MONTH);
            mes = calendar.get(Calendar.MONTH) + 1;
            ano = calendar.get(Calendar.YEAR);
        }
        else {
            String[] dataSplit = data.split("/");
            if (dataSplit.length >= 3) {
                dia = Integer.valueOf(dataSplit[0]);
                mes = Integer.valueOf(dataSplit[1]);
                ano = Integer.valueOf(dataSplit[2]);
            }
        }
        calendar.clear();
        calendar.set(ano, mes - 1, dia);
        aulas = registroDBgetters.getAula(disciplina);
        int numeroAulas = aulas.size();
        ArrayList<String> horarios = new ArrayList<>(numeroAulas);
        for (i = 0; i < numeroAulas; i++) {
            Aula aula = aulas.get(i);
            horarios.add(aula.getInicio() + "/" + aula.getFim());
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (date != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            frameLayout3.setVisibility(View.INVISIBLE);
            fragmentTransaction.hide(dialogCaldroidFragment);
            fragmentTransaction.commit();
            fragmentTransaction = fragmentManager.beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("lista", horarios);
            bundle.putStringArrayList("lista2", new ArrayList<String>());
            selecaoHorarios.setArguments(bundle);
            fragmentTransaction.add(R.id.container1, selecaoHorarios, "FragmentHorarios");
            fragmentTransaction.commit();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.show(selecaoHorarios);
            fragmentTransaction.commit();
            frameLayout2.setVisibility(View.VISIBLE);
        }
        else {
            if(verificarDisciplina()) {
                montarBlocoConteudos(numeroBimestreAtual);
            }
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            frameLayout2.setVisibility(View.INVISIBLE);
            fragmentTransaction.hide(selecaoHorarios);
            fragmentTransaction.commit();
            if (disciplina != null && disciplina.getCodigoDisciplina() == 1000) {
                mostrarAnosIniciais();
            }
        }
    }

    private void mostrarAnosIniciais() {
        dialogAnosIniciais = new DialogAnosIniciais();
        dialogAnosIniciais.show(getSupportFragmentManager(), "DialogAnosIniciais");
        dialogAnosIniciais.registerListener(this);
    }

    private void montarBlocoConteudos(int bloco) {
        if (dataCriacao == null) {
            Toast.makeText(this, "Selecione uma data", Toast.LENGTH_SHORT).show();
            return;
        }
        configurarBlocos(bloco);
        bimestreSelecionado = bloco;
        ConteudoFundamentalAdapter conteudoFundamentalAdapter;
        if (disciplinaSelecionada != null) {
            int codigoDisciplina = disciplinaSelecionada.getCodigoDisciplina();
            if (codigoDisciplina <= 8400) {
                SQLiteDatabase database = banco.get();
                Turma turma = turmaGrupo.getTurma();
                registroAtual = RegistroAulaFundamentalDao.buscarRegistroAulaFundamental(bimestreAtual.getNumero(), codigoDisciplina, turma.getId(), dataCriacao, database);
                if (registroAtual != null) {
                    if(registroAtual.getObservacoes() != null) {
                        String observacoes = registroAtual.getObservacoes();
                        if (!registroAtual.getObservacoes().equals("null")) {
                            observacoes = registroAtual.getObservacoes();
                        }
                        else {
                            observacoes = "";
                        }
                        String textoObservacoes;
                        if(this.observacoes == null) {
                            textoObservacoes = observacoes;
                        }
                        else {
                            textoObservacoes = this.observacoes;
                        }
                        observacoesEditText.setText(textoObservacoes);
                    }
                }
                int i;
                int j;
                int k;
                if (conteudos == null) {
                    calendar = Calendar.getInstance();
                    int ano = calendar.get(Calendar.YEAR);
                    List<ConteudoFundamental> conteudosBimestre1 = ConteudoFundamentalDao.buscarConteudosFundamental(ano, 1, serie, codigoDisciplina, database);
                    conteudos = new SparseArrayCompat<>();
                    objetosDoConhecimento = new SparseArrayCompat<>();
                    if (conteudosBimestre1 != null) {
                        int numeroConteudosBimestre1 = conteudosBimestre1.size();
                        List<String> objetosConhecimento = new ArrayList<>();
                        SimpleArrayMap<String,List<ConteudoFundamental>> habilidadesDoConhecimento = new SimpleArrayMap<>();
                        for (i = 0; i < numeroConteudosBimestre1; i++) {
                            ConteudoFundamental conteudo = conteudosBimestre1.get(i);
                            String objetoConhecimento = conteudo.getObjetosConhecimento();
                            if (!objetosConhecimento.contains(objetoConhecimento)) {
                                objetosConhecimento.add(objetoConhecimento);
                            }
                            List<ConteudoFundamental> habilidadesBimestre = habilidadesDoConhecimento.get(objetoConhecimento);
                            if (habilidadesBimestre == null) {
                                habilidadesBimestre = new ArrayList<>();
                                habilidadesDoConhecimento.put(objetoConhecimento, habilidadesBimestre);
                            }
                            habilidadesBimestre.add(conteudo);
                        }
                        objetosDoConhecimento.append(1, objetosConhecimento);
                        conteudos.append(1, habilidadesDoConhecimento);
                    }
                    List<ConteudoFundamental> conteudosBimestre2 = ConteudoFundamentalDao.buscarConteudosFundamental(ano, 2, serie, codigoDisciplina, database);
                    if (conteudosBimestre2 != null) {
                        int numeroConteudosBimestre2 = conteudosBimestre2.size();
                        List<String> objetosConhecimento = new ArrayList<>();
                        SimpleArrayMap<String,List<ConteudoFundamental>> habilidadesDoConhecimento = new SimpleArrayMap<>();
                        for (i = 0; i < numeroConteudosBimestre2; i++) {
                            ConteudoFundamental conteudo = conteudosBimestre2.get(i);
                            String objetoConhecimento = conteudo.getObjetosConhecimento();
                            if (!objetosConhecimento.contains(objetoConhecimento)) {
                                objetosConhecimento.add(objetoConhecimento);
                            }
                            List<ConteudoFundamental> habilidadesBimestre = habilidadesDoConhecimento.get(objetoConhecimento);
                            if (habilidadesBimestre == null) {
                                habilidadesBimestre = new ArrayList<>();
                                habilidadesDoConhecimento.put(objetoConhecimento, habilidadesBimestre);
                            }
                            habilidadesBimestre.add(conteudo);
                        }
                        objetosDoConhecimento.append(2, objetosConhecimento);
                        conteudos.append(2, habilidadesDoConhecimento);
                    }
                    List<ConteudoFundamental> conteudosBimestre3 = ConteudoFundamentalDao.buscarConteudosFundamental(ano, 3, serie, codigoDisciplina, database);
                    if (conteudosBimestre3 != null) {
                        int numeroConteudosBimestre3 = conteudosBimestre3.size();
                        List<String> objetosConhecimento = new ArrayList<>();
                        SimpleArrayMap<String,List<ConteudoFundamental>> habilidadesDoConhecimento = new SimpleArrayMap<>();
                        for (i = 0; i < numeroConteudosBimestre3; i++) {
                            ConteudoFundamental conteudo = conteudosBimestre3.get(i);
                            String objetoConhecimento = conteudo.getObjetosConhecimento();
                            if (!objetosConhecimento.contains(objetoConhecimento)) {
                                objetosConhecimento.add(objetoConhecimento);
                            }
                            List<ConteudoFundamental> habilidadesBimestre = habilidadesDoConhecimento.get(objetoConhecimento);
                            if (habilidadesBimestre == null) {
                                habilidadesBimestre = new ArrayList<>();
                                habilidadesDoConhecimento.put(objetoConhecimento, habilidadesBimestre);
                            }
                            habilidadesBimestre.add(conteudo);
                        }
                        objetosDoConhecimento.append(3, objetosConhecimento);
                        conteudos.append(3, habilidadesDoConhecimento);
                    }
                    List<ConteudoFundamental> conteudosBimestre4 = ConteudoFundamentalDao.buscarConteudosFundamental(ano, 4, serie, codigoDisciplina, database);
                    if (conteudosBimestre4 != null) {
                        int numeroConteudosBimestre4 = conteudosBimestre4.size();
                        List<String> objetosConhecimento = new ArrayList<>();
                        SimpleArrayMap<String,List<ConteudoFundamental>> habilidadesDoConhecimento = new SimpleArrayMap<>();
                        for (i = 0; i < numeroConteudosBimestre4; i++) {
                            ConteudoFundamental conteudo = conteudosBimestre4.get(i);
                            String objetoConhecimento = conteudo.getObjetosConhecimento();
                            if (!objetosConhecimento.contains(objetoConhecimento)) {
                                objetosConhecimento.add(objetoConhecimento);
                            }
                            List<ConteudoFundamental> habilidadesBimestre = habilidadesDoConhecimento.get(objetoConhecimento);
                            if (habilidadesBimestre == null) {
                                habilidadesBimestre = new ArrayList<>();
                                habilidadesDoConhecimento.put(objetoConhecimento, habilidadesBimestre);
                            }
                            habilidadesBimestre.add(conteudo);
                        }
                        objetosDoConhecimento.append(4, objetosConhecimento);
                        conteudos.append(4, habilidadesDoConhecimento);
                    }
                    if (registroAtual != null) {
                        int codigoRegistroAtual = registroAtual.getCodigoRegistroAula();
                        List<Integer> conteudosRegistro = HabilidadeRegistroFundamentalDao.buscarHabilidadesRegistroFundamental(codigoRegistroAtual, database);
                        if (conteudosRegistro != null) {
                            int numeroConteudosCurriculo = conteudosRegistro.size();
                            for(i = 0; i < numeroConteudosCurriculo; i++) {
                                boolean encontrou = false;
                                int codigoConteudo = conteudosRegistro.get(i);
                                int bimestre = ConteudoFundamentalDao.buscarBimestreDoConteudoFundamental(codigoConteudo, database);
                                SimpleArrayMap<String,List<ConteudoFundamental>> habilidadesDoConhecimento = conteudos.get(bimestre);
                                if (habilidadesDoConhecimento != null) {
                                    int numero = habilidadesDoConhecimento.size();
                                    for (j = 0; j < numero; j++) {
                                        String objetoConhecimento = habilidadesDoConhecimento.keyAt(j);
                                        List<ConteudoFundamental> habilidades = habilidadesDoConhecimento.get(objetoConhecimento);
                                        if (habilidades != null) {
                                            int numerohabilidades = habilidades.size();
                                            for (k = 0; k < numerohabilidades; k++) {
                                                ConteudoFundamental conteudoFundamental = habilidades.get(k);
                                                if (conteudoFundamental != null) {
                                                    if (conteudoFundamental.getCodigoConteudo() == codigoConteudo) {
                                                        encontrou = true;
                                                        conteudoFundamental.setChecado(true);
                                                        if (conteudosSelecionados == null) {
                                                            conteudosSelecionados = new SparseArrayCompat<>();
                                                        }
                                                        SimpleArrayMap<String, List<ConteudoFundamental>> a = conteudosSelecionados.get(bimestre);
                                                        if (a == null) {
                                                            a = new SimpleArrayMap<>();
                                                            conteudosSelecionados.append(bimestre, a);
                                                        }
                                                        List<ConteudoFundamental> conteudoFundamentals = a.get(objetoConhecimento);
                                                        if (conteudoFundamentals == null) {
                                                            conteudoFundamentals = new ArrayList<>();
                                                            a.put(objetoConhecimento, conteudoFundamentals);
                                                        }
                                                        conteudoFundamentals.add(conteudoFundamental);
                                                        conteudosSelecionados.put(bimestre, a);
                                                        break;
                                                    }
                                                }
                                            }
                                            if (encontrou) {
                                                break;
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
                List<String> objetosConhecimentoDoBimestre = objetosDoConhecimento.get(bimestreSelecionado);
                if (objetosConhecimentoDoBimestre != null) {
                    conteudoFundamentalAdapter = new ConteudoFundamentalAdapter(this, this);
                    conteudoFundamentalAdapter.addAll(objetosConhecimentoDoBimestre);
                    conteudoListView.setAdapter(conteudoFundamentalAdapter);
                }
                else {
                    Toast.makeText(this, "Sem conteúdo para este bloco", Toast.LENGTH_SHORT).show();
                    conteudoFundamentalAdapter = new ConteudoFundamentalAdapter(this, this);
                    List<String> listaVazia = new ArrayList<>();
                    conteudoFundamentalAdapter.addAll(listaVazia);
                    conteudoListView.setAdapter(conteudoFundamentalAdapter);
                }
            }
            else {
                Toast.makeText(this, "Sem conteúdo para disciplina eletiva", Toast.LENGTH_SHORT).show();
                conteudoFundamentalAdapter = new ConteudoFundamentalAdapter(this, this);
                List<String> listaVazia = new ArrayList<>();
                conteudoFundamentalAdapter.addAll(listaVazia);
                conteudoListView.setAdapter(conteudoFundamentalAdapter);
            }
        }
        else {
            Toast.makeText(this, "Sem conteúdo para este bloco", Toast.LENGTH_SHORT).show();
            conteudoFundamentalAdapter = new ConteudoFundamentalAdapter(this, this);
            List<String> listaVazia = new ArrayList<>();
            conteudoFundamentalAdapter.addAll(listaVazia);
            conteudoListView.setAdapter(conteudoFundamentalAdapter);
        }
    }

    private void configurarBlocos(int bloco) {
        if (habilidades) {
            habilidades = false;
        }
        bloco1TextView.setBackgroundResource(R.drawable.button_enabled);
        bloco2TextView.setBackgroundResource(R.drawable.button_enabled);
        bloco3TextView.setBackgroundResource(R.drawable.button_enabled);
        bloco4TextView.setBackgroundResource(R.drawable.button_enabled);
        switch(bloco) {
            case 1: {
                bloco1TextView.setBackgroundResource(R.drawable.button_pressed);
                break;
            }
            case 2: {
                bloco2TextView.setBackgroundResource(R.drawable.button_pressed);
                break;
            }
            case  3: {
                bloco3TextView.setBackgroundResource(R.drawable.button_pressed);
                break;
            }
            case 4: {
                bloco4TextView.setBackgroundResource(R.drawable.button_pressed);
                break;
            }
        }
        selecionarBimestreTextView.setVisibility(View.GONE);
        if(observacoesEditText.getVisibility() == View.VISIBLE) {
            observacoesEditText.setVisibility(View.GONE);
            observacoes = observacoesEditText.getText().toString();
        }
        txtContHab.setText(R.string.conteudos);
        conteudoListView.setVisibility(View.VISIBLE);
    }

    private void removerMenuNavegacao(Animation animation) {
        menuAberto = false;
        menuNavegacao.desativarCliques();
        frameLayout.setVisibility(View.INVISIBLE);
        frameLayout.startAnimation(animation);
    }

    private void removerSelecaoHorarios() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        frameLayout2.setVisibility(View.INVISIBLE);
        fragmentTransaction.hide(selecaoHorarios);
        fragmentTransaction.commit();
    }

    private void usuarioSelecionouBloco(int bloco) {
        if (verificarDisciplina()) {
            montarBlocoConteudos(bloco);
        }
        else {
            Toast.makeText(this, "Selecione a Disciplina", Toast.LENGTH_SHORT).show();
        }
    }

    private void usuarioSelecionouHorario(String horario) {
        if (verificarDisciplina()) {
            verificarHorariosSelecionados(horario, true);
            montarBlocoConteudos(numeroBimestreAtual);
        }
        removerSelecaoHorarios();
        if (disciplina != null && disciplina.getCodigoDisciplina() == 1000) {
            mostrarAnosIniciais();
        }
    }

    private boolean verificarDisciplina() {
        boolean verificado = false;
        if (disciplina != null) {
            if (disciplina.getCodigoDisciplina() == 1000) {
                if(disciplinaSelecionada != null && disciplinaSelecionada.getCodigoDisciplina() != 0) {
                    verificado = true;
                }
            }
            else {
                disciplinaSelecionada = disciplina;
                verificado = true;
            }
        }
        return verificado;
    }

    private void verificarHorariosSelecionados(String horario, boolean selecaoHorario) {
        if (!listaHorariosSelecionados.contains(horario)) {
            listaHorariosSelecionados.add(horario);
        }
        else if(!selecaoHorario) {
            listaHorariosSelecionados.remove(horario);
        }
    }

    //
    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        frameLayout.setVisibility(View.INVISIBLE);
        fragmentTransaction.hide(menuNavegacao);
        fragmentTransaction.commit();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    //OnClickListener
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_1bim) {
            if(menuAberto) {
                removerMenuNavegacao(apenasRemoverMenuNavegacao);
            }
            bloco1TextView.setBackgroundResource(R.drawable.button_pressed);
            bloco2TextView.setBackgroundResource(R.drawable.button_enabled);
            bloco3TextView.setBackgroundResource(R.drawable.button_enabled);
            bloco4TextView.setBackgroundResource(R.drawable.button_enabled);
            usuarioSelecionouBloco(1);
        }
        else if (id == R.id.btn_2bim) {
            if(menuAberto) {
                removerMenuNavegacao(apenasRemoverMenuNavegacao);
            }
            bloco1TextView.setBackgroundResource(R.drawable.button_enabled);
            bloco2TextView.setBackgroundResource(R.drawable.button_pressed);
            bloco3TextView.setBackgroundResource(R.drawable.button_enabled);
            bloco4TextView.setBackgroundResource(R.drawable.button_enabled);
            usuarioSelecionouBloco(2);
        }
        else if (id == R.id.btn_3bim) {
            if(menuAberto) {
                removerMenuNavegacao(apenasRemoverMenuNavegacao);
            }
            bloco1TextView.setBackgroundResource(R.drawable.button_enabled);
            bloco2TextView.setBackgroundResource(R.drawable.button_enabled);
            bloco3TextView.setBackgroundResource(R.drawable.button_pressed);
            bloco4TextView.setBackgroundResource(R.drawable.button_enabled);
            usuarioSelecionouBloco(3);
        }
        else if (id == R.id.btn_4bim) {
            if(menuAberto) {
                removerMenuNavegacao(apenasRemoverMenuNavegacao);
            }
            bloco1TextView.setBackgroundResource(R.drawable.button_enabled);
            bloco2TextView.setBackgroundResource(R.drawable.button_enabled);
            bloco3TextView.setBackgroundResource(R.drawable.button_enabled);
            bloco4TextView.setBackgroundResource(R.drawable.button_pressed);
            usuarioSelecionouBloco(4);
        }
        else if (id == R.id.menu) {
            if (menuAberto) {
                apenasRemoverMenuNavegacao.setAnimationListener(this);
                removerMenuNavegacao(apenasRemoverMenuNavegacao);
            }
            else {
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.exibir_menu_navegacao);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.show(menuNavegacao);
                menuNavegacao.ativarCliques();
                fragmentTransaction.commit();
                menuAberto = true;
                frameLayout.setVisibility(View.VISIBLE);
                frameLayout.startAnimation(animation);
            }
        }
        else if (id == R.id.selecionarDisciplina) {
            mostrarAnosIniciais();
        }
        else if (id == R.id.editTxt1) {
            if (menuAberto) {
                removerMenuNavegacao(apenasRemoverMenuNavegacao);
            }
            if (observacoesEditText.getVisibility() == View.GONE) {
                txtContHab.setText("Observações");
                selecionarBimestreTextView.setVisibility(View.GONE);
                observacoesEditText.setVisibility(View.VISIBLE);
                conteudoListView.setVisibility(View.GONE);
            }
            else {
                observacoesEditText.setVisibility(View.GONE);
                selecionarBimestreTextView.setVisibility(View.VISIBLE);
                configurarBlocos(numeroBimestreAtual);
            }
        }
        else if (id == R.id.btn_salvar_registro) {
            int i;
            observacoes = observacoesEditText.getText().toString();
            StringBuilder horariosString = new StringBuilder();
            int numeroHorarios = listaHorariosSelecionados.size();
            for (i = 0; i < numeroHorarios; i++) {
                String horario = listaHorariosSelecionados.get(i);
                horariosString.append(horario).append("-");
            }
            if (horariosString.length() > 0) {
                horariosString.deleteCharAt(horariosString.length() - 1);
            }
            String horarios = horariosString.toString();
            SQLiteDatabase database = banco.get();
            if (registroAtual != null) {
                registroAtual.setSincronizado(false);
                registroAtual.setDataCriacao(dataCriacao);
                registroAtual.setObservacoes(observacoes);
                registroAtual.setHorarios(horarios);
            }
            else {
                int numeroDeRegistros = RegistroAulaFundamentalDao.numeroDeRegistros(database);
                int codigoRegistroAula = -numeroDeRegistros - 1;
                if (disciplinaSelecionada != null) {
                    registroAtual = new RegistroAulaFundamental(codigoRegistroAula, false, dataCriacao, observacoes, horarios, bimestreAtual.getNumero(), disciplinaSelecionada.getCodigoDisciplina(), turmaGrupo.getTurma().getId());
                }
            }
            if (registroAtual != null) {
                database.beginTransaction();
                int codigoRegistroAula = RegistroAulaFundamentalDao.inserirRegistroAulaFundamental(registroAtual, database);
                if (conteudosSelecionados != null) {
                    List<ConteudoFundamental> conteudosSelecionadosTodosBimestres = new ArrayList<>();
                    SimpleArrayMap<String, List<ConteudoFundamental>> a = conteudosSelecionados.get(1);
                    if (a != null) {
                        int numero = a.size();
                        for(i = 0; i < numero; i++) {
                            List<ConteudoFundamental> conteudos = a.valueAt(i);
                            if (conteudos != null && !conteudos.isEmpty()) {
                                conteudosSelecionadosTodosBimestres.addAll(conteudos);
                            }
                        }
                    }
                    a = conteudosSelecionados.get(2);
                    if (a != null) {
                        int numero = a.size();
                        for(i = 0; i < numero; i++) {
                            List<ConteudoFundamental> conteudos = a.valueAt(i);
                            if (conteudos != null && !conteudos.isEmpty()) {
                                conteudosSelecionadosTodosBimestres.addAll(conteudos);
                            }
                        }
                    }
                    a = conteudosSelecionados.get(3);
                    if (a != null) {
                        int numero = a.size();
                        for(i = 0; i < numero; i++) {
                            List<ConteudoFundamental> conteudos = a.valueAt(i);
                            if (conteudos != null && !conteudos.isEmpty()) {
                                conteudosSelecionadosTodosBimestres.addAll(conteudos);
                            }
                        }
                    }
                    a = conteudosSelecionados.get(4);
                    if (a != null) {
                        int numero = a.size();
                        for(i = 0; i < numero; i++) {
                            List<ConteudoFundamental> conteudos = a.valueAt(i);
                            if (conteudos != null && !conteudos.isEmpty()) {
                                conteudosSelecionadosTodosBimestres.addAll(conteudos);
                            }
                        }
                    }
                    int numeroConteudosSelecionados = conteudosSelecionadosTodosBimestres.size();
                    HabilidadeRegistroFundamentalDao.deletarHabilidadeRegistroFundamental(codigoRegistroAula, database);
                    for (i = 0; i < numeroConteudosSelecionados; i++) {
                        ConteudoFundamental conteudoFundamental = conteudosSelecionadosTodosBimestres.get(i);
                        HabilidadeRegistroFundamentalDao.inserirHabilidadeRegistroFundamental(codigoRegistroAula, conteudoFundamental.getCodigoConteudo(), database);
                    }
                }
                database.setTransactionSuccessful();
                database.endTransaction();
                AlertDialog alerta;
                AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("Registro salvo!").setMessage("O registro de aula foi salvo no dispositivo. Sincronize o aplicativo para que os dados sejam enviados para a SED.").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                        Intent intent = new Intent(menuNavegacao.getContext(), HomeActivity.class);
                        navegarPara(intent);
                    }
                });
                alerta = builder.create();
                alerta.getWindow().setBackgroundDrawableResource(R.drawable.dialogarredondado);
                alerta.show();
            }
        }
        else if (id == R.id.fecharselecaocalendario) {
            finish();
        }
        else if (habilidades) {
            habilidades = false;
            montarBlocoConteudos(bimestreSelecionado);
        }
        else {
            finish();
        }
    }

    @Override
    public void onConteudoSelecionado(String conteudo) {
        habilidades = true;
        objetoConhecimentoSelecionado = conteudo;
        List<ConteudoFundamental> habilidades = this.conteudos.get(bimestreSelecionado).get(conteudo);
        HabilidadeFundamentalAdapter habilidadeFundamentalAdapter = new HabilidadeFundamentalAdapter(this, this);
        habilidadeFundamentalAdapter.addAll(habilidades);
        conteudoListView.setAdapter(habilidadeFundamentalAdapter);
    }

    //fragmentHorariosListener
    @Override
    public void usuarioQuerFecharSelecaoHorarios() {
        finish();
    }

    @Override
    public void onHorarioSelecionado(String horario) {
        usuarioSelecionouHorario(horario);
    }

    @Override
    public void onHorarioChecado(String horario) {
        verificarHorariosSelecionados(horario, false);
    }

    @Override
    public void usuarioQuerEditarFrequencia(String horario) {
    }

    @Override
    public void usuarioQuerExcluirFrequencia(String horario) {
    }

    @Override
    public void usuarioQuerAvancar() {
        if (listaHorariosSelecionados.isEmpty()) {
            Toast.makeText(this, "Selecione um ou mais horários de aula", Toast.LENGTH_SHORT).show();
        }
        else {
            usuarioSelecionouHorario(listaHorariosSelecionados.get(0));
        }
    }

    //DialogAnosIniciais.Listener
    @Override
    public void selecionouMateria(int codigoMateria) {
        disciplinaSelecionada = disciplina;
        dialogAnosIniciais.dismiss();
        montarBlocoConteudos(numeroBimestreAtual);
    }

    //MenuNavagacao
    @Override
    public void navegarPara(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void onHabilidadeSelecionada(ConteudoFundamental habilidade) {
        if (conteudosSelecionados == null) {
            conteudosSelecionados = new SparseArrayCompat<>();
        }
        SimpleArrayMap<String, List<ConteudoFundamental>> a = conteudosSelecionados.get(bimestreSelecionado);
        if (a == null) {
            a = new SimpleArrayMap<>();
            conteudosSelecionados.append(bimestreSelecionado, a);
        }
        List<ConteudoFundamental> conteudosFundamentais = a.get(objetoConhecimentoSelecionado);
        if (conteudosFundamentais == null) {
            conteudosFundamentais = new ArrayList<>();
            a.put(objetoConhecimentoSelecionado, conteudosFundamentais);
        }
        habilidade.setChecado(!habilidade.getChecado());
        if (habilidade.getChecado()) {
            conteudosFundamentais.add(habilidade);
        }
        else {
            conteudosFundamentais.remove(habilidade);
        }
        conteudosSelecionados.put(bimestreSelecionado, a);
        HabilidadeFundamentalAdapter habilidadeFundamentalAdapter = (HabilidadeFundamentalAdapter) conteudoListView.getAdapter();
        habilidadeFundamentalAdapter.notifyDataSetChanged();
    }
}