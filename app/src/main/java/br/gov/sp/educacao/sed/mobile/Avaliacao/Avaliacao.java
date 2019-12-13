package br.gov.sp.educacao.sed.mobile.Avaliacao;

import android.os.Parcel;
import android.os.Parcelable;

public class Avaliacao
        implements Parcelable {

    static final String BUNDLE_AVALIACAO = "avaliacao";
    static final String TIPO_AVALIACAO = "Avaliação";
    static final String TIPO_ATIVIDADE = "Atividade";
    static final String TIPO_TRABALHO = "Trabalho";
    static final String TIPO_OUTROS = "Outros";
    static final String TODOS = "Todos";

    static final int CODIGO_TIPO_AVALIACAO = 11;
    static final int CODIGO_TIPO_ATIVIDADE = 12;
    static final int CODIGO_TIPO_TRABALHO = 13;
    static final int CODIGO_TIPO_OUTROS = 14;
    static final int CODIGO_TODOS = 0;

    private boolean valeNota;
    private int codigo;
    private int bimestre;
    private int codTurma;
    private int codDisciplina;
    private int id;
    private int tipoAtividade;
    private int mobileId;
    private String nome;
    private String data;
    private String dataCadastro;
    private String dataServidor;

    private int turmaId;
    private int disciplinaId;

    public Avaliacao() {
    }

    private Avaliacao(Parcel in) {

        nome = in.readString();
        data = in.readString();
        dataCadastro = in.readString();
        dataServidor = in.readString();
        codigo = in.readInt();
        bimestre = in.readInt();
        codTurma = in.readInt();
        codDisciplina = in.readInt();
        id = in.readInt();
        tipoAtividade = in.readInt();
        mobileId = in.readInt();
        valeNota = in.readByte() != 0;
        turmaId = in.readInt();
        disciplinaId = in.readInt();
    }

    public static final Creator<Avaliacao> CREATOR = new Creator<Avaliacao>() {

        @Override
        public Avaliacao createFromParcel(Parcel in) {

            return new Avaliacao(in);
        }

        @Override
        public Avaliacao[] newArray(int size) {

            return new Avaliacao[size];
        }
    };

    public boolean isValeNota() {
        return valeNota;
    }

    public void setValeNota(boolean valeNota) {
        this.valeNota = valeNota;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getBimestre() {
        return bimestre;
    }

    public void setBimestre(int bimestre) {
        this.bimestre = bimestre;
    }

    public int getCodTurma() {
        return codTurma;
    }

    public void setCodTurma(int codTurma) {
        this.codTurma = codTurma;
    }

    public int getCodDisciplina() {
        return codDisciplina;
    }

    public void setCodDisciplina(int codDisciplina) {
        this.codDisciplina = codDisciplina;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTipoAtividade() {
        return tipoAtividade;
    }

    public void setTipoAtividade(int tipoAtividade) {
        this.tipoAtividade = tipoAtividade;
    }

    public int getMobileId() {
        return mobileId;
    }

    public void setMobileId(int mobileId) {
        this.mobileId = mobileId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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

    public int getTurmaId() {
        return turmaId;
    }

    public void setTurmaId(int turmaId) {
        this.turmaId = turmaId;
    }

    public int getDisciplinaId() {
        return disciplinaId;
    }

    public void setDisciplinaId(int disciplinaId) {
        this.disciplinaId = disciplinaId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        return ((Avaliacao)o).getId() == id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(nome);
        dest.writeString(data);
        dest.writeString(dataCadastro);
        dest.writeString(dataServidor);
        dest.writeInt(codigo);
        dest.writeInt(bimestre);
        dest.writeInt(codTurma);
        dest.writeInt(codDisciplina);
        dest.writeInt(id);
        dest.writeInt(tipoAtividade);
        dest.writeInt(mobileId);
        dest.writeByte((byte) (valeNota ? 1 : 0));
        dest.writeInt(turmaId);
        dest.writeInt(disciplinaId);
    }
}
