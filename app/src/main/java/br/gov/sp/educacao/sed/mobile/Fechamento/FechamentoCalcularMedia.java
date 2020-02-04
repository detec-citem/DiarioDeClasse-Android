package br.gov.sp.educacao.sed.mobile.Fechamento;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.SparseIntArray;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

import br.gov.sp.educacao.sed.mobile.Avaliacao.Avaliacao;
import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;
import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.CriarAcessoBanco;

public class FechamentoCalcularMedia
        extends AppCompatActivity
         implements FechamentoCalcularMediaViewMvc.Listener {

    private Banco banco;

    private int disciplina;

    private List<Aluno> alunos;

    private List<Double> medias;

    private SparseIntArray pesos;

    private TurmaGrupo turmaGrupo;

    private List<Avaliacao> avaliacoes;

    private final int TIPO_SOMA = 3;
    private final int TIPO_PONDERADA = 1;
    private final int TIPO_ARITMETICA = 2;

    private MediaSoma mediaSoma;
    private MediaPonderada mediaPonderada;
    private MediaAritmetica mediaAritmetica;

    private CriarAcessoBanco criarAcessoBanco;

    private FechamentoDBcrud fechamentoDBcrud;

    private FechamentoDBgetters fechamentoDBgetters;

    private FechamentoCalcularMediaViewMvcImpl fechamentoCalcularMediaViewMvcImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        fechamentoCalcularMediaViewMvcImpl = new FechamentoCalcularMediaViewMvcImpl(LayoutInflater.from(this), getSupportFragmentManager(), null);

        pesos = new SparseIntArray();

        medias = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();

        turmaGrupo = bundle.getParcelable(TurmaGrupo.BUNDLE_TURMA_GRUPO);

        fechamentoCalcularMediaViewMvcImpl.setarTurmaGrupo(turmaGrupo);

        disciplina = bundle.getInt("Disciplina");

        avaliacoes = bundle.getParcelableArrayList("totalAvaliacoesCorrigidas");



        banco = CriarAcessoBanco.gerarBanco(getApplicationContext());

        fechamentoDBcrud = new FechamentoDBcrud(banco);

        fechamentoDBgetters = new FechamentoDBgetters(banco);

        setupComponents();

        setContentView(fechamentoCalcularMediaViewMvcImpl.getRootView());
    }

    @Override
    protected void onStop() {

        super.onStop();

        fechamentoCalcularMediaViewMvcImpl.unregisterListenerMenuNavegacao();

        fechamentoCalcularMediaViewMvcImpl.unregisterListener();
    }

    @Override
    protected void onStart() {

        super.onStart();

        fechamentoCalcularMediaViewMvcImpl.registerListenerMenuNavegacao();

        fechamentoCalcularMediaViewMvcImpl.registerListener(this);
    }

    @Override
    protected void onResume() {

        super.onResume();

        fechamentoCalcularMediaViewMvcImpl.removerProgressBarVoador();
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    private void setupComponents() {

        fechamentoCalcularMediaViewMvcImpl.exibirNomeTurma(turmaGrupo.getTurma().getNomeTurma());

        switch (disciplina) {

            case 2700:

                fechamentoCalcularMediaViewMvcImpl.exibirNomeDisciplina("ANOS INICIAIS/ MATEMÁTICA");

                break;

            case 1100:

                fechamentoCalcularMediaViewMvcImpl.exibirNomeDisciplina("ANOS INICIAIS/ LÍNGUA PORTUGUESA");

                break;

            case 7245:

                fechamentoCalcularMediaViewMvcImpl.exibirNomeDisciplina("ANOS INICIAIS/ CIÊNCIAS HUMANAS/NATUREZA");

                break;

            default:

                fechamentoCalcularMediaViewMvcImpl.exibirNomeDisciplina(turmaGrupo.getDisciplina().getNomeDisciplina());
        }

        if (turmaGrupo != null) {

            alunos = Aluno.getAlunosAtivos(turmaGrupo.getTurma().getAlunos());
        }
    }

    @Override
    public void navegarPara(Intent intent) {

        startActivity(intent);
    }

    public void usuarioSelecionouSalvar(long selected) {

        //Limpar medias no onDestroy
        if(medias.size() > 0) {

            medias.clear();
        }

        for(int i = 0; i < alunos.size(); i++) {

            medias.add(alunos.get(i).getMedia());
        }

        boolean isArrayInteiro = medias != null ? verificaNotaInteira(medias) : true;

        if(selected > 0 && isArrayInteiro) {

            for(Aluno aluno : alunos) {

                MediaAluno media = new MediaAluno();

                if(aluno.getMedia() != 11) {

                    double notaMedia = aluno.getMedia();

                    Double nd = new Double(notaMedia);

                    int nota = nd.intValue();

                    media.setNotaMedia(nota);
                }
                else {

                    media.setNotaMedia(11);
                }

                media.setCodigoMatricula(aluno.getCodigoMatricula());

                media.setDisciplina(disciplina);

                media.setCodigoTurma(turmaGrupo.getTurma().getCodigoTurma());

                media.setBimestre(fechamentoDBgetters.getFechamentoAtual());

                fechamentoDBcrud.insertMediaAluno(media);
            }

            fechamentoCalcularMediaViewMvcImpl.avisoUsuarioMediasSalvas();
        }
        else if(!(selected > 0)) {

            fechamentoCalcularMediaViewMvcImpl.avisoUsuarioCalculeTodasMedias();
        }
        else if(!isArrayInteiro) {

            fechamentoCalcularMediaViewMvcImpl.avisoUsuarioArredondeTodasMedias();
        }

        fechamentoCalcularMediaViewMvcImpl.removerProgressBarVoador();

        /*new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {


            }
        },500);*/
    }

    public void usuarioSelecinouCalcular(long selected) {

        mediaPonderada = new MediaPonderada();

        if(selected == 1) {

            if (pesos.size() == avaliacoes.size()) {

                mediaPonderada.calculaMedia(

                        fechamentoDBgetters, alunos, avaliacoes, pesos
                );

                fechamentoCalcularMediaViewMvcImpl.configuracao1(alunos);
            }
            else {

                fechamentoCalcularMediaViewMvcImpl.avisoUsuarioSelecionePesos();
            }
        }
    }

    public void usuarioSelecionouTipoMedia(String selected) {

        for(int i = 0; i < alunos.size(); i++) {

            alunos.get(i).setMedia(0);
            alunos.get(i).setItemTipoArredondamento("empty");
        }

        mediaAritmetica = new MediaAritmetica();

        mediaSoma = new MediaSoma();

        int tipo = 0;

        if(selected.equals("Selecione")) {

            //listAlunosMedia.setAdapter(null);
        }
        else {

            switch(selected) {

                case "Ponderada":

                    tipo = TIPO_PONDERADA;
                    break;

                case "Aritmética":

                    tipo = TIPO_ARITMETICA;
                    break;

                case "Soma":

                    tipo = TIPO_SOMA;
                    break;
            }

            //listAlunosMedia.setAdapter(null);

            if(tipo == TIPO_PONDERADA) {

                fechamentoCalcularMediaViewMvcImpl.configuracao3(avaliacoes);
            }
            else {

                if(tipo == TIPO_ARITMETICA) {

                    mediaAritmetica.calculaMedia(

                            fechamentoDBgetters, alunos, avaliacoes
                    );
                }
                else {

                    mediaSoma.calculaMedia(

                            fechamentoDBgetters, alunos, avaliacoes
                    );
                }

                fechamentoCalcularMediaViewMvcImpl.configuracao2(alunos);
            }
        }
    }

    public boolean verificaNotaInteira(List<Double> mediaAlunos) {

        double notaMedia = 0d;

        int count = 0;

        if(mediaAlunos == null) {

            return false;
        }

        for(int i = 0; i < mediaAlunos.size(); i++) {

            notaMedia = mediaAlunos.get(i);

            if ((int)notaMedia == notaMedia) {

                count++;
            }
        }

        if(count != mediaAlunos.size()) {

            return false;
        }
        return true;
    }

    @Override
    public void adicionarPesoAvaliacao(Editable peso, Object tag) {

        if(!peso.toString().equals("")) {

            pesos.put((int) tag, Integer.parseInt(peso.toString()));
        }
        else {

            pesos.delete((int) tag);
        }
    }
}
