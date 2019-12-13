package br.gov.sp.educacao.sed.mobile.comunicados;

public class Comunicado {

    private int cdComunicado;
    private String titulo;
    private String comunicado;
    private String data;
    private boolean visualizado;

    public Comunicado(int cdComunicado, String titulo, String comunicado, String data, boolean visualizado) {
        this.cdComunicado = cdComunicado;
        this.titulo = titulo;
        this.comunicado = comunicado;
        this.data = data;
        this.visualizado = visualizado;
    }

    public Comunicado() {
    }

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
}
