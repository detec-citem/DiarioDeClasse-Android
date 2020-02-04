package br.gov.sp.educacao.sed.mobile.Login;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.gov.sp.educacao.sed.mobile.util.GenericsTable;

public class UsuarioTO
        implements GenericsTable, Parcelable {

    public static final String nomeTabela = "USUARIO";

    private Integer id;
    private Integer ativo;
    private String usuario;
    private String senha;
    private String nome;
    private String cpf;
    private String rg;
    private String digitoRG;
    private String dataUltimoAcesso;
    private String token;

    public UsuarioTO() {

    }

    public UsuarioTO(Cursor cursor) {

        setId(cursor.getInt(cursor.getColumnIndex("id")));
        setNome(cursor.getString(cursor.getColumnIndex("nome")));
        setUsuario(cursor.getString(cursor.getColumnIndex("usuario")));
        setSenha(cursor.getString(cursor.getColumnIndex("senha")));
        setCpf(cursor.getString(cursor.getColumnIndex("cpf")));
        setRg(cursor.getString(cursor.getColumnIndex("rg")));
        setDigitoRG(cursor.getString(cursor.getColumnIndex("digitoRG")));
        //setDataUltimoAcesso(cursor.getString(cursor.getColumnIndex("dataUltimoAcesso")));
        setAtivo(cursor.getInt(cursor.getColumnIndex("ativo")));
        setToken(cursor.getString(cursor.getColumnIndex("token")));
    }

    public void setUsuarioDB(Cursor cursor) {

        setId(cursor.getInt(cursor.getColumnIndex("id")));
        setNome(cursor.getString(cursor.getColumnIndex("nome")));
        setUsuario(cursor.getString(cursor.getColumnIndex("usuario")));
        setSenha(cursor.getString(cursor.getColumnIndex("senha")));
        setToken(cursor.getString(cursor.getColumnIndex("token")));
    }

    public UsuarioTO(JSONObject retorno) throws JSONException {

        setUsuario(retorno.getString("Usuario").trim());
        setSenha(retorno.getString("Senha").trim());
        setNome(retorno.getString("Nome").trim());
        setCpf(retorno.getString("NumeroCpf").trim());
        setRg(retorno.getString("numeroRg").trim());
        setDigitoRG(retorno.getString("digitoRg").trim());
        setAtivo(1);
        setToken(retorno.getString("Token"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.ROOT);
        setDataUltimoAcesso(dateFormat.format(new Date()));
    }

    private UsuarioTO(Parcel in) {

        id = in.readByte() == 0x00 ? null : in.readInt();
        usuario = in.readString();
        senha = in.readString();
        nome = in.readString();
        cpf = in.readString();
        rg = in.readString();
        digitoRG = in.readString();
        dataUltimoAcesso = in.readString();
        ativo = in.readByte() == 0x00 ? null : in.readInt();
        token = in.readString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getDigitoRG() {
        return digitoRG;
    }

    public void setDigitoRG(String digitoRG) {
        this.digitoRG = digitoRG;
    }

    public String getDataUltimoAcesso() {
        return dataUltimoAcesso;
    }

    public void setDataUltimoAcesso(String dataUltimoAcesso) {
        this.dataUltimoAcesso = dataUltimoAcesso;
    }

    public Integer getAtivo() {
        return ativo;
    }

    public void setAtivo(Integer ativo) {
        this.ativo = ativo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public ContentValues getContentValues() {

        ContentValues values = new ContentValues();

        values.put("usuario", getUsuario());
        values.put("senha", getSenha());
        values.put("nome", getNome());
        values.put("cpf", getCpf());
        values.put("rg", getRg());
        values.put("digitoRG", getDigitoRG());
        values.put("dataUltimoAcesso", getDataUltimoAcesso());
        values.put("ativo", getAtivo());
        values.put("token", getToken());

        return values;
    }

    @Override
    public String getCodigoUnico() {
        return "cpf = " + getCpf();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        if (id == null) {

            dest.writeByte((byte) (0x00));
        }
        else {

            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        dest.writeString(usuario);
        dest.writeString(senha);
        dest.writeString(nome);
        dest.writeString(cpf);
        dest.writeString(rg);
        dest.writeString(digitoRG);
        dest.writeString(dataUltimoAcesso);

        if (ativo == null) {

            dest.writeByte((byte) (0x00));
        }
        else {

            dest.writeByte((byte) (0x01));
            dest.writeInt(ativo);
        }
        dest.writeString(token);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<UsuarioTO> CREATOR = new Parcelable.Creator<UsuarioTO>() {
        @Override
        public UsuarioTO createFromParcel(Parcel in) {
            return new UsuarioTO(in);
        }

        @Override
        public UsuarioTO[] newArray(int size) {
            return new UsuarioTO[size];
        }
    };
}