package br.gov.sp.educacao.sed.mobile.Escola;

import android.os.Parcel;

public class DiasLetivos {

    private int id;
    private String dataAula;

    public DiasLetivos() {
    }

    private DiasLetivos(Parcel in) {
        id = in.readInt();
        dataAula = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDataAula() {
        return dataAula;
    }

    public void setDataAula(String dataAula) {
        this.dataAula = dataAula;
    }
}
