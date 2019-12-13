package br.gov.sp.educacao.sed.mobile.Fechamento;

import android.util.SparseArray;
import android.util.SparseIntArray;

import br.gov.sp.educacao.sed.mobile.Turmas.Aluno;
import br.gov.sp.educacao.sed.mobile.Avaliacao.Avaliacao;

import br.gov.sp.educacao.sed.mobile.util.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class MediaPonderada {

    private int CASAS_DECIMAIS = 2;

    private final int ALUNO_ATIVO_SEM_NOTA = 11;

    private final int ALUNO_INATIVO_SEM_NOTA = 12;

    public void calculaMedia(FechamentoDBgetters fechamentoDBgetters, List<Aluno> alunos,
                              List<Avaliacao> avaliacoes, SparseIntArray... peso) {

        SparseIntArray pesos = peso[0];

        int pesoTotal = 0;

        for (Avaliacao avaliacao : avaliacoes) {

            int pesoAvaliacao = pesos.get(avaliacao.getId());

            pesoTotal += pesoAvaliacao;

            for (Aluno aluno : alunos) {

                String nota = fechamentoDBgetters.getNotaAluno(avaliacao.getId(), aluno.getId());

                double notaComPeso = Double.parseDouble(nota) * pesoAvaliacao;

                if (!nota.equals(String.valueOf(ALUNO_ATIVO_SEM_NOTA)) && !nota.equals(String.valueOf(ALUNO_INATIVO_SEM_NOTA))) {

                    aluno.setMedia(aluno.getMedia() + notaComPeso);
                }
            }
        }

        for (int i = 0; i < alunos.size(); i++) {

            alunos.get(i).setMedia(arredondar(alunos.get(i).getMedia() / pesoTotal, CASAS_DECIMAIS));
        }
    }
    private double arredondar ( double valor, int casasDecimais){

        if (casasDecimais < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(valor);

        bd = bd.setScale(casasDecimais, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }
}
