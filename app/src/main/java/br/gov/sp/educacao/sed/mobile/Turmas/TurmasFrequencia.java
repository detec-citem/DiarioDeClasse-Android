package br.gov.sp.educacao.sed.mobile.Turmas;

import android.os.Parcel;
import android.os.Parcelable;

public class TurmasFrequencia
        implements Parcelable {

    private int id;
    private int aulasBimestre;
    private int aulasAno;

    public TurmasFrequencia() {}

    private TurmasFrequencia(Parcel in) {

        id = in.readInt();
        aulasBimestre = in.readInt();
        aulasAno = in.readInt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAulasBimestre() {
        return aulasBimestre;
    }

    public void setAulasBimestre(int aulasBimestre) {
        this.aulasBimestre = aulasBimestre;
    }

    public int getAulasAno() {
        return aulasAno;
    }

    public void setAulasAno(int aulasAno) {
        this.aulasAno = aulasAno;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(aulasBimestre);
        dest.writeInt(aulasAno);
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