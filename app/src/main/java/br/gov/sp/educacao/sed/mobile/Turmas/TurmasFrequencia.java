package br.gov.sp.educacao.sed.mobile.Turmas;

import android.os.Parcel;
import android.os.Parcelable;

public class TurmasFrequencia
        implements Parcelable {

    private int id;

    public TurmasFrequencia() {}

    private TurmasFrequencia(Parcel in) {

        id = in.readInt();
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
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TurmasFrequencia> CREATOR = new Parcelable.Creator<TurmasFrequencia>() {
        @Override
        public TurmasFrequencia createFromParcel(Parcel in) {
            return new TurmasFrequencia(in);
        }

        @Override
        public TurmasFrequencia[] newArray(int size) {
            return new TurmasFrequencia[size];
        }
    };
}