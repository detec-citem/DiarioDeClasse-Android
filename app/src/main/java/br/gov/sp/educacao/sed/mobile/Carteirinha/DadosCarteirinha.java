package br.gov.sp.educacao.sed.mobile.Carteirinha;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class DadosCarteirinha
        implements Parcelable {

    private int id;

    private String nomeUsuario;

    private String nomeSocial;

    private String cargoUsuario;

    private String rgUsuario;

    private String rsUsuario;

    private String fotoUsuario;

    private String qrCodeUsuario;

    private String validade;

    private String codigoCargo;

    private String status;

    public DadosCarteirinha() {

    }

    public void setJSON(JSONObject retorno) throws JSONException {

        setRsUsuario(retorno.getString("RS"));
        setValidade(retorno.getString("Validade"));
        setCargoUsuario(retorno.getString("Cargo"));
        setCodigoCargo(retorno.getString("CodigoCargo"));
        setFotoUsuario(retorno.getString("ImagemBase64"));
        setNomeSocial(retorno.getString("NomePessoaSocial"));
        setQrCodeUsuario(retorno.getString("ImagemQrCodeBase64"));
        setStatus(retorno.getString("StatusAprovacaoDesc"));
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    public String getCodigoCargo() {
        return codigoCargo;
    }

    public void setCodigoCargo(String codigoCargo) {
        this.codigoCargo = codigoCargo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private DadosCarteirinha(Parcel in) {

        id = in.readInt();
        nomeUsuario = in.readString();
        nomeSocial = in.readString();
        cargoUsuario = in.readString();
        rgUsuario = in.readString();
        rsUsuario = in.readString();
        fotoUsuario = in.readString();
        qrCodeUsuario = in.readString();
        status = in.readString();
        codigoCargo = in.readString();
        validade = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeSocial() {
        return nomeSocial;
    }

    public void setNomeSocial(String nomeSocial) {
        this.nomeSocial = nomeSocial;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getCargoUsuario() {
        return cargoUsuario;
    }

    public void setCargoUsuario(String cargoUsuario) {
        this.cargoUsuario = cargoUsuario;
    }

    public String getRgUsuario() {
        return "RG: " + rgUsuario;
    }

    public void setRgUsuario(String rgUsuario) {
        this.rgUsuario = rgUsuario;
    }

    public String getRsUsuario() {
        return rsUsuario;
    }

    public void setRsUsuario(String rsUsuario) {
        this.rsUsuario = rsUsuario;
    }

    public String getFotoUsuario() {
        return fotoUsuario;
    }

    public void setFotoUsuario(String fotoUsuario) {
        this.fotoUsuario = fotoUsuario;
    }

    public String getQrCodeUsuario() {
        return qrCodeUsuario;
    }

    public void setQrCodeUsuario(String qrCodeUsuario) {
        this.qrCodeUsuario = qrCodeUsuario;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(nomeUsuario);
        dest.writeString(nomeSocial);
        dest.writeString(cargoUsuario);
        dest.writeString(rgUsuario);
        dest.writeString(rsUsuario);
        dest.writeString(fotoUsuario);
        dest.writeString(qrCodeUsuario);
        dest.writeString(status);
        dest.writeString(codigoCargo);
        dest.writeString(validade);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DadosCarteirinha> CREATOR = new Parcelable.Creator<DadosCarteirinha>() {

        @Override
        public DadosCarteirinha createFromParcel(Parcel in) {
            return new DadosCarteirinha(in);
        }

        @Override
        public DadosCarteirinha[] newArray(int size) {

            return new DadosCarteirinha[size];
        }
    };
}
