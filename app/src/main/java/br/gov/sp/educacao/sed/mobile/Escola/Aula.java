package br.gov.sp.educacao.sed.mobile.Escola;

import android.os.Parcel;
import android.os.Parcelable;

public class Aula
        implements Parcelable {

    private int id;
    private String inicio;
    private String fim;

    public Aula() {}

    private Aula(Parcel in) {
        id = in.readInt();
        inicio = in.readString();
        fim = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
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