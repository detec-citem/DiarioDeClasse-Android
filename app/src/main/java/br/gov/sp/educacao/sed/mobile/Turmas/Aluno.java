package br.gov.sp.educacao.sed.mobile.Turmas;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Aluno
        implements Parcelable,
                   Comparable<Aluno> {

    private int id;
    private boolean alunoAtivo;
    private int numeroChamada;
    private int faltasAnuais;
    private int faltasBimestre;
    private int faltasSequenciais;
    private int faltasBimestreAnterior;
    private int codigoAluno;
    private int numeroRa;
    private int selecionado;
    private double media;

    private String codigoMatricula;
    private String digitoRa;
    private String ufRa;
    private String comparecimento;
    private String nomeAluno;
    private String pai;
    private String mae;
    private String nascimento;
    private String necessidadesEspeciais;
    private String nota;
    private String itemTipoArredondamento = "empty";

    public double getMedia() {
        return media;
    }

    public void setMedia(double media) {
        this.media = media;
    }

    public String getItemTipoArredondamento() {
        return itemTipoArredondamento;
    }

    public void setItemTipoArredondamento(String itemTipoArredondamento) {
        this.itemTipoArredondamento = itemTipoArredondamento;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public Aluno() {

        this.selecionado = 0;
    }

    private Aluno(Parcel in) {

        id = in.readInt();
        alunoAtivo = in.readByte() != 0x00;
        numeroChamada = in.readInt();
        faltasAnuais = in.readInt();
        faltasBimestre = in.readInt();
        faltasBimestreAnterior = in.readInt();
        faltasSequenciais = in.readInt();
        codigoAluno = in.readInt();
        numeroRa = in.readInt();
        codigoMatricula = in.readString();
        digitoRa = in.readString();
        ufRa = in.readString();
        comparecimento = in.readString();
        nomeAluno = in.readString();
        pai = in.readString();
        mae = in.readString();
        nascimento = in.readString();
        necessidadesEspeciais = in.readString();
        nota = in.readString();
        selecionado = in.readInt();
        itemTipoArredondamento = in.readString();
        media = in.readDouble();
    }

    public static List<Aluno> getAlunosAtivos(List<Aluno> alunos) {

        int i;

        int alunosSize = alunos.size();

        List<Aluno> alunosAtivos = new ArrayList<>();

        for (i = 0; i < alunosSize; i++) {

            Aluno aluno = alunos.get(i);

            if (aluno.getAlunoAtivo()) {

                alunosAtivos.add(aluno);
            }
        }
        Collections.sort(alunosAtivos);
        return alunosAtivos;
    }

    public int getId() {
        return id;
    }

    public String getNascimento() {
        return nascimento;
    }

    public void setNascimento(String nascimento) {
        this.nascimento = nascimento;
    }

    public String getNecessidadesEspeciais() {
        return necessidadesEspeciais;
    }

    public void setNecessidadesEspeciais(String necessidadesEspeciais) {
        this.necessidadesEspeciais = necessidadesEspeciais;
    }

    public int getSelecionado() {
        return selecionado;
    }

    public void setSelecionado(int selecionado) {
        this.selecionado = selecionado;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getAlunoAtivo() {
        return alunoAtivo;
    }

    public void setAlunoAtivo(boolean alunoAtivo) {
        this.alunoAtivo = alunoAtivo;
    }

    public int getNumeroChamada() {
        return numeroChamada;
    }

    public void setNumeroChamada(int numeroChamada) {
        this.numeroChamada = numeroChamada;
    }

    public int getFaltasAnuais() {
        return faltasAnuais;
    }

    public void setFaltasAnuais(int faltasAnuais) {
        this.faltasAnuais = faltasAnuais;
    }

    public int getFaltasBimestre() {
        return faltasBimestre;
    }

    public void setFaltasBimestre(int faltasBimestre) {
        this.faltasBimestre = faltasBimestre;
    }

    public int getFaltasBimestreAnterior() {
        return faltasBimestreAnterior;
    }

    public void setFaltasBimestreAnterior(int faltasBimestreAnterior) {
        this.faltasBimestreAnterior = faltasBimestreAnterior;
    }

    public int getFaltasSequenciais() {
        return faltasSequenciais;
    }

    public void setFaltasSequenciais(int faltasSequenciais) {
        this.faltasSequenciais = faltasSequenciais;
    }

    public int getCodigoAluno() {
        return codigoAluno;
    }

    public void setCodigoAluno(int codigoAluno) {
        this.codigoAluno = codigoAluno;
    }

    public int getNumeroRa() {
        return numeroRa;
    }

    public void setNumeroRa(int numeroRa) {
        this.numeroRa = numeroRa;
    }

    public String getCodigoMatricula() {
        return codigoMatricula;
    }

    public void setCodigoMatricula(String codigoMatricula) {
        this.codigoMatricula = codigoMatricula;
    }

    public String getDigitoRa() {
        return digitoRa;
    }

    public void setDigitoRa(String digitoRa) {
        this.digitoRa = digitoRa;
    }

    public String getComparecimento() {
        return comparecimento;
    }

    public void setComparecimento(String comparecimento) {
        this.comparecimento = comparecimento;
    }

    public String getNomeAluno() {
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public String getPai() {
        return pai;
    }

    public void setPai(String pai) {
        this.pai = pai;
    }

    public String getMae() {
        return mae;
    }

    public void setMae(String mae) {
        this.mae = mae;
    }

    @Override
    public int compareTo(Aluno outroAluno) {

        return this.getNumeroChamada() - outroAluno.getNumeroChamada();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void setMedia(Double media) {
        this.media = media;
    }

    @Override
    public void writeToParcel(Parcel dest,
                              int flags) {

        dest.writeInt(id);
        dest.writeByte((byte) (alunoAtivo ? 0x01 : 0x00));
        dest.writeInt(numeroChamada);
        dest.writeInt(faltasAnuais);
        dest.writeInt(faltasBimestre);
        dest.writeInt(faltasBimestreAnterior);
        dest.writeInt(faltasSequenciais);
        dest.writeInt(codigoAluno);
        dest.writeInt(numeroRa);
        dest.writeString(codigoMatricula);
        dest.writeString(digitoRa);
        dest.writeString(ufRa);
        dest.writeString(comparecimento);
        dest.writeString(nomeAluno);
        dest.writeString(pai);
        dest.writeString(mae);
        dest.writeString(nascimento);
        dest.writeString(necessidadesEspeciais);
        dest.writeString(nota);
        dest.writeInt(selecionado);
        dest.writeString(itemTipoArredondamento);
        dest.writeDouble(media);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Aluno> CREATOR = new Parcelable.Creator<Aluno>() {

        @Override
        public Aluno createFromParcel(Parcel in) {

            return new Aluno(in);
        }

        @Override
        public Aluno[] newArray(int size) {

            return new Aluno[size];
        }
    };
}