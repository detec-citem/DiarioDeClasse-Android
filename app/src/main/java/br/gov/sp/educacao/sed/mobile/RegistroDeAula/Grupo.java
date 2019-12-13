package br.gov.sp.educacao.sed.mobile.RegistroDeAula;

public class Grupo {

    private int codigoGrupo;
    private int anoLetivo;
    private int codigoTipoEnsino;
    private int serie;
    private int codigoDisciplina;

    public Grupo(int codigoGrupo, int anoLetivo, int codigoTipoEnsino, int serie, int codigoDisciplina) {

        this.codigoGrupo = codigoGrupo;
        this.anoLetivo = anoLetivo;
        this.codigoTipoEnsino = codigoTipoEnsino;
        this.serie = serie;
        this.codigoDisciplina = codigoDisciplina;
    }

    public Grupo() {}

    public void setGrupo(int codigoGrupo, int anoLetivo, int codigoTipoEnsino, int serie, int codigoDisciplina) {

        this.codigoGrupo = codigoGrupo;
        this.anoLetivo = anoLetivo;
        this.codigoTipoEnsino = codigoTipoEnsino;
        this.serie = serie;
        this.codigoDisciplina = codigoDisciplina;
    }

    public int getCodigoGrupo() {
        return codigoGrupo;
    }

    public void setCodigoGrupo(int codigoGrupo) {
        this.codigoGrupo = codigoGrupo;
    }

    public int getAnoLetivo() {
        return anoLetivo;
    }

    public void setAnoLetivo(int ano) {
        this.anoLetivo = ano;
    }

    public int getCodigoTipoEnsino() {
        return codigoTipoEnsino;
    }

    public void setCodigoTipoEnsino(int codigoTipoEnsino) {

        this.codigoTipoEnsino = codigoTipoEnsino;
    }

    public int getSerie() {
        return serie;
    }

    public void setSerie(int serie) {
        this.serie = serie;
    }

    public int getCodigoDisciplina() {
        return codigoDisciplina;
    }

    public void setCodigoDisciplina(int codigoDisciplina) {
        this.codigoDisciplina = codigoDisciplina;
    }

}
