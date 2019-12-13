package br.gov.sp.educacao.sed.mobile.Escola;

import android.os.Parcelable;
import android.os.Parcel;

public class Disciplina
        implements Parcelable {

    private int id;
    private int codigoDisciplina;
    private String nomeDisciplina;

    public Disciplina() {
    }

    private Disciplina(Parcel in) {

        id = in.readInt();
        codigoDisciplina = in.readInt();
        nomeDisciplina = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCodigoDisciplina() {
        return codigoDisciplina;
    }

    public void setCodigoDisciplina(int codigoDisciplina) {
        this.codigoDisciplina = codigoDisciplina;
    }

    public String getNomeDisciplina() {
        return nomeDisciplina;
    }

    public void setNomeDisciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(codigoDisciplina);
        dest.writeString(nomeDisciplina);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Disciplina> CREATOR = new Parcelable.Creator<Disciplina>() {
        @Override
        public Disciplina createFromParcel(Parcel in) {
            return new Disciplina(in);
        }

        @Override
        public Disciplina[] newArray(int size) {
            return new Disciplina[size];
        }
    };
}
