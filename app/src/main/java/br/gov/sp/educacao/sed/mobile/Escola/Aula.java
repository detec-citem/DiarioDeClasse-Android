package br.gov.sp.educacao.sed.mobile.Escola;

import android.os.Parcel;
import android.os.Parcelable;

public class Aula
        implements Parcelable {

    private int id;
    private int diaSemana;
    private String inicio;
    private String fim;

    public Aula() {}

    private Aula(Parcel in) {

        id = in.readInt();
        diaSemana = in.readInt();
        inicio = in.readString();
        fim = in.readString();
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFim() {
        return fim;
    }

    public void setFim(String fim) {
        this.fim = fim;
    }

    public int getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(int diaSemana) {
        this.diaSemana = diaSemana;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeInt(diaSemana);
        dest.writeString(inicio);
        dest.writeString(fim);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Aula> CREATOR = new Parcelable.Creator<Aula>() {

        @Override
        public Aula createFromParcel(Parcel in) {
            return new Aula(in);
        }

        @Override
        public Aula[] newArray(int size) {
            return new Aula[size];
        }
    };
}