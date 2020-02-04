package br.gov.sp.educacao.sed.mobile.Fechamento;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

import br.gov.sp.educacao.sed.mobile.Avaliacao.Avaliacao;
import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;
import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.CriarAcessoBanco;

public class FechamentoActivity
        extends AppCompatActivity
         implements FechamentoViewMvc.Listener {

    private Banco banco;

    private Bundle bundle;

    private int disciplina;

    private int alunosAtivos;

    private boolean anosIniciais;

    private TurmaGrupo turmaGrupo;

    private int tipoFechamentoAtual;

    private FechamentoTurma fechamento;

    private List<Avaliacao> avaliacoes;

    private CriarAcessoBanco criarAcessoBanco;

    private FechamentoDBcrud fechamentoDBcrud;

    private FechamentoDBgetters fechamentoDBgetters;

    private FechamentoViewMvcImpl fechamentoViewMvcImpl;

    public String TAG = FechamentoActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        fechamentoViewMvcImpl = new FechamentoViewMvcImpl(LayoutInflater.from(this), getSupportFragmentManager(), null);



        banco = CriarAcessoBanco.gerarBanco(this);

        fechamentoDBcrud = new FechamentoDBcrud(banco);

        fechamentoDBgetters = new FechamentoDBgetters(banco);

        bundle = getIntent().getExtras();

        turmaGrupo = bundle.getParcelable(TurmaGrupo.BUNDLE_TURMA_GRUPO);

        anosIniciais = fechamentoDBgetters.isAnosIniciais(turmaGrupo.getTurma().getCodigoTurma());

        fechamentoViewMvcImpl.setarAnosIniciais(anosIniciais);

        fechamentoViewMvcImpl.setarTurmaGrupo(turmaGrupo);

        disciplina = turmaGrupo.getDisciplina().getCodigoDisciplina();

        exibirNomeTurmaDisciplina();

        tipoFechamentoAtual = fechamentoDBgetters.getTipoFechamentoAtual();

        exibirNomeFechamento();

        fechamento = fechamentoDBgetters.getFechamentoTurma(

                turmaGrupo.getTurma().getCodigoTurma(), turmaGrupo.getDisciplina().getCodigoDisciplina(),
                tipoFechamentoAtual
        );

        if(fechamento != null) {

            fechamentoViewMvcImpl.exibirDadosFechamento(fechamento);
        }

        alunosAtivos = getAlunosAtivosSize();

        setContentView(fechamentoViewMvcImpl.getRootView());
    }

    @Override
    protected void onStop() {

        super.onStop();

        fechamentoViewMvcImpl.unregisterListenerMenuNavegacao();

        fechamentoViewMvcImpl.unregisterListener();
    }

    @Override
    protected void onStart() {

        super.onStart();

        fechamentoViewMvcImpl.registerListenerMenuNavegacao();

        fechamentoViewMvcImpl.registerListener(this);

        if(turmaGrupo.getDisciplina().getCodigoDisciplina() == 1000){

            fechamentoViewMvcImpl.esconderBtnCalcularMedia();
        }
    }

    @Override
    protected void onResume() {

        super.onResume();

        fechamentoViewMvcImpl.removerProgressBarVoador();

        fechamentoViewMvcImpl.ativarBotao();
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    private int getAlunosAtivosSize() {

        List<Aluno> alunos = turmaGrupo.getTurma().getAlunos();

        int alunosAtivos = 0;

        int alunosSize = alunos.size();

        for(int i = 0; i < alunosSize; i++) {

            Aluno aluno = alunos.get(i);

            if(aluno.getAlunoAtivo()) {

                alunosAtivos++;
            }
        }
        return alunosAtivos;
    }

    private void exibirNomeFechamento() {

        String nomeFechamento = "";

        switch(tipoFechamentoAtual) {

            case 5:

                nomeFechamento = "Conselho Primeiro Bimestre";

                break;

            case 6:

                nomeFechamento = "Conselho Segundo Bimestre";

                break;

            case 7:

                nomeFechamento = "Conselho Terceiro Bimestre";

                break;

            case 8:

                nomeFechamento = "Conselho Quarto Bimestre";

                break;

            default:

                nomeFechamento = "Fechamento";

                break;
        }

        fechamentoViewMvcImpl.exibirNomeFechamento(nomeFechamento);
    }

    @Override
    public void navegarPara(Intent intent) {

        startActivity(intent);
    }

    private void navegarParaCalculoMedia() {

        Bundle dadosAvaliacoes = new Bundle();

        dadosAvaliacoes.putParcelable(TurmaGrupo.BUNDLE_TURMA_GRUPO, turmaGrupo);

        dadosAvaliacoes.putParcelableArrayList("totalAvaliacoesCorrigidas", (ArrayList) avaliacoes);

        dadosAvaliacoes.putInt("Disciplina", disciplina);

        Intent intent = new Intent(FechamentoActivity.this, FechamentoCalcularMedia.class);

        intent.putExtras(dadosAvaliacoes);

        startActivity(intent);
    }

    @Override
    public void usuarioClicouCalcularMedia() {

        avaliacoes = fechamentoDBgetters.getAvaliacoesValeNota(

                turmaGrupo, fechamentoDBgetters.getFechamentoAtual(), turmaGrupo.getDisciplina().getCodigoDisciplina()
        );

        if(avaliacoes.size() == 1) {

            fechamentoViewMvcImpl.exibirAvisoApenasUmaProva();
        }
        else if(avaliacoes.size() == 0) {

            fechamentoViewMvcImpl.exibirAvisoNenhumaProva();
        }
        else if(avaliacoes.size() > 1) {

            navegarParaCalculoMedia();
        }
    }

    private void exibirNomeTurmaDisciplina() {

        fechamentoViewMvcImpl.exibirNomeTurmaDisciplina(

                turmaGrupo.getTurma().getNomeTurma(), turmaGrupo.getDisciplina().getNomeDisciplina()
        );
    }

    @Override
    public void usuarioSelecionouConfirmar(int aulasPlanejadas, int aulasRealizadas, String justificativa) {

        if(tipoFechamentoAtual == 5 || tipoFechamentoAtual == 6
                || tipoFechamentoAtual == 7 || tipoFechamentoAtual == 8) {

            final FechamentoTurma fechamentoTurma = new FechamentoTurma();

            fechamentoTurma.setCodigoTurma(turmaGrupo.getTurma().getCodigoTurma());

            fechamentoTurma.setCodigoDisciplina(turmaGrupo.getDisciplina().getCodigoDisciplina());

            fechamentoTurma.setCodigoTipoFechamento(tipoFechamentoAtual);

            fechamentoTurma.setAulasPlanejadas(aulasPlanejadas);

            fechamentoTurma.setAulasRealizadas(aulasRealizadas);

            fechamentoTurma.setJustificativa(justificativa);

            fechamentoTurma.setDataServidor(null);

            if(fechamento == null) {

                fechamentoDBcrud.insertFechamentoTurma(fechamentoTurma);
            }
            else if(fechamentoDBgetters.isFechamentoTurmaAlterada(fechamentoTurma)) {

                fechamentoDBcrud.insertFechamentoTurma(fechamentoTurma);
            }

            Bundle bundle = new Bundle();

            bundle.putInt("alunosAtivosSize", alunosAtivos);

            bundle.putInt("tipoFechamentoAtual", tipoFechamentoAtual);

            bundle.putParcelable(TurmaGrupo.BUNDLE_TURMA_GRUPO, turmaGrupo);

            Intent intent = new Intent(this, FechamentoSliderActivity.class);

            intent.putExtras(bundle);

            startActivity(intent);
        }
        else {

            fechamentoViewMvcImpl.avisoUsuarioSemPeriodoFechamento();
        }
    }
}