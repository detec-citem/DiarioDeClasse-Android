package br.gov.sp.educacao.sed.mobile.Fechamento;

import java.util.List;

import br.gov.sp.educacao.sed.mobile.Avaliacao.Avaliacao;
import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;

public class MediaSoma {

    private final int ALUNO_ATIVO_SEM_NOTA = 11;

    private final int ALUNO_INATIVO_SEM_NOTA = 12;

    public void calculaMedia(FechamentoDBgetters fechamentoDBgetters, List<Aluno> alunos, List<Avaliacao> avaliacoes) {

        for(Avaliacao avaliacao : avaliacoes) {

            for(Aluno aluno : alunos) {

                String nota = fechamentoDBgetters.getNotaAluno(avaliacao.getId(), aluno.getId());

                if(!nota.equals(String.valueOf(ALUNO_ATIVO_SEM_NOTA)) && !nota.equals(String.valueOf(ALUNO_INATIVO_SEM_NOTA))) {

                    aluno.setMedia(aluno.getMedia() + Double.valueOf(nota));
                }
            }
        }
    }
}