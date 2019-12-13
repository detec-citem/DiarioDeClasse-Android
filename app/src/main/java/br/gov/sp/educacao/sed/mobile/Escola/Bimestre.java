package br.gov.sp.educacao.sed.mobile.Escola;

import android.os.Parcel;
import android.os.Parcelable;

public class Bimestre
        implements Parcelable {

    private int id;
    private int numero;
    private String inicio;
    private String fim;

    public Bimestre() {
    }

    private Bimestre(Parcel in) {

        id = in.readInt();
        numero = in.readInt();
        inicio = in.readString();
        fim = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
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
        dest.writeInt(numero);
        dest.writeString(inicio);
        dest.writeString(fim);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Bimestre> CREATOR = new Parcelable.Creator<Bimestre>() {
        @Override
        public Bimestre createFromParcel(Parcel in) {
            return new Bimestre(in);
        }

        @Override
        public Bimestre[] newArray(int size) {
            return new Bimestre[size];
        }
    };
}