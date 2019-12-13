package br.gov.sp.educacao.sed.mobile.Avaliacao;


public class NotasAluno {

    private int aluno_id;
    private int usuario_id;
    private int avaliacao_id;
    private String codigoMatricula;
    private String nota;
    private String dataCadastro;
    private String dataServidor;

    public int getAluno_id() {
        return aluno_id;
    }

    public void setAluno_id(int aluno_id) {
        this.aluno_id = aluno_id;
    }

    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }

    public int getAvaliacao_id() {
        return avaliacao_id;
    }

    public void setAvaliacao_id(int avaliacao_id) {
        this.avaliacao_id = avaliacao_id;
    }

    public String getCodigoMatricula() {
        return codigoMatricula;
    }

    public void setCodigoMatricula(String codigoMatricula) {
        this.codigoMatricula = codigoMatricula;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getDataServidor() {
        return dataServidor;
    }

    public void setDataServidor(String dataServidor) {
        this.dataServidor = dataServidor;
    }
}
