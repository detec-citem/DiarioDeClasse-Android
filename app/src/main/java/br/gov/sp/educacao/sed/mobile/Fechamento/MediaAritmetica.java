package br.gov.sp.educacao.sed.mobile.Fechamento;

import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;

import br.gov.sp.educacao.sed.mobile.Avaliacao.Avaliacao;

import br.gov.sp.educacao.sed.mobile.util.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.util.List;

public class MediaAritmetica {

    private int CASAS_DECIMAIS = 2;

    private final String ALUNO_ATIVO_SEM_NOTA = "11";

    private final String ALUNO_ATIVO_SEM_NOTA2 = "11.00";

    private final String ALUNO_INATIVO_SEM_NOTA = "12";

    public void calculaMedia(FechamentoDBgetters fechamentoDBgetters, List<Aluno> alunos, List<Avaliacao> avaliacoes) {

        /*for(Avaliacao avaliacao : avaliacoes) {

            for(Aluno aluno : alunos) {

                String nota = fechamentoDBgetters.getNotaAluno(avaliacao.getId(), aluno.getId());

                if(!nota.equals(ALUNO_ATIVO_SEM_NOTA)
                        && !nota.equals(ALUNO_INATIVO_SEM_NOTA)
                        && !nota.equals(ALUNO_ATIVO_SEM_NOTA2)){

                    aluno.setMedia(aluno.getMedia() + Double.valueOf(nota));
                }
            }
        }*/

        for(Aluno aluno : alunos) {

            int avaliacoesSemNota = 0;

            for(Avaliacao avaliacao : avaliacoes) {

                String nota = fechamentoDBgetters.getNotaAluno(avaliacao.getId(), aluno.getId());

                if(!nota.equals(ALUNO_ATIVO_SEM_NOTA)
                        && !nota.equals(ALUNO_INATIVO_SEM_NOTA)
                        && !nota.equals(ALUNO_ATIVO_SEM_NOTA2)){

                    aluno.setMedia(aluno.getMedia() + Double.valueOf(nota));
                }
                else {

                    avaliacoesSemNota++;
                }
            }
            if(avaliacoesSemNota == avaliacoes.size()) {

                aluno.setMedia(11);
            }
            else {

                aluno.setMedia(arredondar(aluno.getMedia() / avaliacoes.size(), CASAS_DECIMAIS));
            }
        }

        /*for(int i = 0; i < alunos.size(); i++) {

            alunos.get(i).setMedia(arredondar(alunos.get(i).getMedia() / avaliacoes.size(), CASAS_DECIMAIS));
        }*/
    }

    private double arredondar(double valor, int casasDecimais) {

        if (casasDecimais < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(valor);

        bd = bd.setScale(casasDecimais, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }
}