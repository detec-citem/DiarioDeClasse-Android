package br.gov.sp.educacao.sed.mobile.Turmas;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Turma
        implements Parcelable {

    private int id;
    private int codigoTurma;
    private int codigoTipoEnsino;
    private int ano;
    private int serie;
    private int codigoEscola;
    private String nomeEscola;
    private String nomeTurma;
    private String nomeDiretoria;
    private String nomeTipoEnsino;
    private ArrayList<Aluno> alunoArrayList;

    private volatile boolean header, footer;

    public Turma() {
        alunoArrayList = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public boolean isHeader() {
        return header;
    }

    public void setHeader(boolean header) {
        this.header = header;
    }

    public boolean isFooter() {
        return footer;
    }

    public void setFooter(boolean footer) {
        this.footer = footer;
    }

    public ArrayList<Aluno> getAlunos(){
        return alunoArrayList;
    }

    public Aluno getAluno(int position) {
        return alunoArrayList.get(position);
    }

    public int arrayAlunoSize() {
        return alunoArrayList.size();
    }

    public void addAluno(Aluno aluno) {
        alunoArrayList.add(aluno);
    }

    public String getNomeEscola() {
        return nomeEscola;
    }

    public int getSerie() {
        return serie;
    }

    public void setSerie(int serie) {
        this.serie = serie;
    }

    public void setNomeEscola(String nomeEscola) {
        this.nomeEscola = nomeEscola;
    }

    public void setCodigoTurma(int codigoTurma) {
        this.codigoTurma = codigoTurma;
    }

    public int getCodigoTurma() {
        return codigoTurma;
    }

    public String getNomeTurma() {
        return nomeTurma;
    }

    public void setNomeTurma(String nomeTurma) {
        this.nomeTurma = nomeTurma;
    }

    public String getNomeDiretoria() {
        return nomeDiretoria;
    }

    public void setNomeDiretoria(String nomeDiretoria) {
        this.nomeDiretoria = nomeDiretoria;
    }

    public int getCodigoEscola() {
        return codigoEscola;
    }

    public void setCodigoEscola(int codigoEscola) {
        this.codigoEscola = codigoEscola;
    }

    public int getCodigoTipoEnsino() {
        return codigoTipoEnsino;
    }

    public void setCodigoTipoEnsino(int codigoTipoEnsino) {
        this.codigoTipoEnsino = codigoTipoEnsino;
    }

    public String getNomeTipoEnsino() {
        return nomeTipoEnsino;
    }

    public void setNomeTipoEnsino(String nomeTipoEnsino) {
        this.nomeTipoEnsino = nomeTipoEnsino;
    }

    private Turma(Parcel in) {

        id = in.readInt();
        codigoTurma = in.readInt();
        codigoTipoEnsino = in.readInt();
        codigoEscola = in.readInt();
        ano = in.readInt();
        serie = in.readInt();
        nomeEscola = in.readString();
        nomeTurma = in.readString();
        nomeDiretoria = in.readString();
        nomeTipoEnsino = in.readString();
        if (in.readByte() == 0x01) {

            alunoArrayList = new ArrayList<>();
            in.readList(alunoArrayList, Aluno.class.getClassLoader());
        }
        else {

            alunoArrayList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeInt(codigoTurma);
        dest.writeInt(codigoTipoEnsino);
        dest.writeInt(codigoEscola);
        dest.writeInt(ano);
        dest.writeInt(serie);
        dest.writeString(nomeEscola);
        dest.writeString(nomeTurma);
        dest.writeString(nomeDiretoria);
        dest.writeString(nomeTipoEnsino);
        if (alunoArrayList == null) {

            dest.writeByte((byte) (0x00));
        }
        else {

            dest.writeByte((byte) (0x01));
            dest.writeList(alunoArrayList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Turma> CREATOR = new Parcelable.Creator<Turma>() {
        @Override
        public Turma createFromParcel(Parcel in) {
            return new Turma(in);
        }

        @Override
        public Turma[] newArray(int size) {
            return new Turma[size];
        }
    };
}