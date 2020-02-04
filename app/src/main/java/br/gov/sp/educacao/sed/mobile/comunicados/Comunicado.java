package br.gov.sp.educacao.sed.mobile.comunicados;

import android.os.Parcel;
import android.os.Parcelable;

public class Comunicado implements Parcelable {
    //Variáveis
    private int cdComunicado;
    private String titulo;
    private String comunicado;
    private String data;
    private boolean visualizado;

    //Construtores
    public Comunicado() {
    }

    public Comunicado(int cdComunicado, String titulo, String comunicado, String data, boolean visualizado) {
        this.cdComunicado = cdComunicado;
        this.titulo = titulo;
        this.comunicado = comunicado;
        this.data = data;
        this.visualizado = visualizado;
    }

    private Comunicado(Parcel in) {
        cdComunicado = in.readInt();
        titulo = in.readString();
        comunicado = in.readString();
        data = in.readString();
        visualizado = in.readByte() != 0;
    }

    //Métodos
    public int getCdComunicado() {
        return cdComunicado;
    }

    public void setCdComunicado(int cdComunicado) {
        this.cdComunicado = cdComunicado;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getComunicado() {
        return comunicado;
    }

    public void setComunicado(String comunicado) {
        this.comunicado = comunicado;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isVisualizado() {
        return visualizado;
    }

    public void setVisualizado(boolean visualizado) {
        this.visualizado = visualizado;
    }

    //Parcelable
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cdComunicado);
        dest.writeString(titulo);
        dest.writeString(comunicado);
        dest.writeString(data);
        byte visualizado;
        if (this.visualizado) {
            visualizado = 1;
        }
        else {
            visualizado = 0;
        }
        dest.writeByte(visualizado);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Comunicado> CREATOR = new Creator<Comunicado>() {
        @Override
        public Comunicado createFromParcel(Parcel in) {
            return new Comunicado(in);
        }

        @Override
        public Comunicado[] newArray(int size) {
            return new Comunicado[size];
        }
    };
}