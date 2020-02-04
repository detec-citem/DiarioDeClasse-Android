package br.gov.sp.educacao.sed.mobile.Turmas;

import android.os.Parcel;
import android.os.Parcelable;

import br.gov.sp.educacao.sed.mobile.Escola.Aula;
import br.gov.sp.educacao.sed.mobile.Escola.Disciplina;

public class TurmaGrupo
        implements Parcelable {

    public static final String BUNDLE_TURMA_GRUPO = "turma_grupo";

    private Aula aula;
    private Turma turma;
    private Disciplina disciplina;
    private TurmasFrequencia turmasFrequencia;


    public TurmaGrupo() {}

    public Turma getTurma() {

        return turma;
    }

    @Override
    public String toString() {

        return turma.getNomeTurma()+ " - " + disciplina.getNomeDisciplina();
    }

    @Override
    public int describeContents() {

        return 0;
    }

    private TurmaGrupo(Parcel in) {

        aula = in.readParcelable(Aula.class.getClassLoader());
        disciplina = in.readParcelable(Disciplina.class.getClassLoader());
        turma = in.readParcelable(Turma.class.getClassLoader());
        turmasFrequencia = in.readParcelable(TurmasFrequencia.class.getClassLoader());
    }

    public void setAula(Aula aula) {

        this.aula = aula;
    }

    public Aula getAula(){
        return aula;
    }

    public Disciplina getDisciplina() {

        return disciplina;
    }

    public void setTurma(Turma turma) {

        this.turma = turma;
    }

    public TurmasFrequencia getTurmasFrequencia() {

        return turmasFrequencia;
    }

    public void setDisciplina(Disciplina disciplina) {

        this.disciplina = disciplina;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeParcelable(aula, flags);
        dest.writeParcelable(disciplina, flags);
        dest.writeParcelable(turma, flags);
        dest.writeParcelable(turmasFrequencia, flags);
    }

    public void setTurmasFrequencia(TurmasFrequencia turmasFrequencia) {

        this.turmasFrequencia = turmasFrequencia;
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TurmaGrupo> CREATOR = new Parcelable.Creator<TurmaGrupo>() {

        @Override
        public TurmaGrupo createFromParcel(Parcel in) {

            return new TurmaGrupo(in);
        }

        @Override
        public TurmaGrupo[] newArray(int size) {

            return new TurmaGrupo[size];
        }
    };
}