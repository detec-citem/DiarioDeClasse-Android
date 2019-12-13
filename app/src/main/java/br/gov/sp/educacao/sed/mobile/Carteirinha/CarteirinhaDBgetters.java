package br.gov.sp.educacao.sed.mobile.Carteirinha;

import java.util.List;
import java.util.ArrayList;

import android.database.Cursor;

import br.gov.sp.educacao.sed.mobile.util.Banco;

public class CarteirinhaDBgetters {

    private Banco banco;

    private Cursor cursor;

    private final String TAG = CarteirinhaDBgetters.class.getSimpleName();

    public CarteirinhaDBgetters(Banco banco) {

        this.banco = banco;

        this.cursor = null;
    }

    List<DadosCarteirinha> getCarteirinhas() {

        List<DadosCarteirinha> listaCarteirinhas = new ArrayList<>();

        try {

            cursor = banco.get().rawQuery("SELECT * FROM CARTEIRINHAS", null);

            if(cursor.getCount() > 0) {

                while(cursor.moveToNext()) {

                    DadosCarteirinha dadosCarteirinha = new DadosCarteirinha();

                    dadosCarteirinha.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    dadosCarteirinha.setNomeUsuario(cursor.getString(cursor.getColumnIndex("nomeUsuario")));
                    dadosCarteirinha.setCargoUsuario(cursor.getString(cursor.getColumnIndex("cargoUsuario")));
                    dadosCarteirinha.setRgUsuario(cursor.getString(cursor.getColumnIndex("rgUsuario")));
                    dadosCarteirinha.setRsUsuario(cursor.getString(cursor.getColumnIndex("rsUsuario")));
                    dadosCarteirinha.setFotoUsuario(cursor.getString(cursor.getColumnIndex("fotoUsuario")));
                    dadosCarteirinha.setQrCodeUsuario(cursor.getString(cursor.getColumnIndex("qrCodeUsuario")));
                    dadosCarteirinha.setStatus(cursor.getString(cursor.getColumnIndex("statusAprovacao")));
                    dadosCarteirinha.setCodigoCargo(cursor.getString(cursor.getColumnIndex("codigoCargo")));

                    listaCarteirinhas.add(dadosCarteirinha);
                }
            }
        }
        catch(Exception e) {

            e.printStackTrace();
        }
        finally {

            if(cursor != null) {

                cursor.close();
            }
        }

        return listaCarteirinhas;
    }

    DadosCarteirinha getCarteirinha(String codigoCargo) {

        DadosCarteirinha dadosCarteirinha = new DadosCarteirinha();

        try {

            cursor = banco.get().rawQuery(

                    "SELECT CARTEIRINHAS.id, CARTEIRINHAS.cargoUsuario, CARTEIRINHAS.rsUsuario, " +
                            "CARTEIRINHAS.fotoUsuario, CARTEIRINHAS.qrCodeUsuario, CARTEIRINHAS.statusAprovacao, " +
                            "CARTEIRINHAS.codigoCargo, CARTEIRINHAS.validade, CARTEIRINHAS.nomeSocial, " +
                            "USUARIO.nome AS nomeUsuario, USUARIO.rg AS rgUsuario, USUARIO.digitoRG AS digitoRG " +
                            "FROM CARTEIRINHAS " +
                            "INNER JOIN USUARIO ON USUARIO.id = CARTEIRINHAS.idUsuario " +
                            "WHERE codigoCargo = " + codigoCargo, null);

            cursor.moveToFirst();

            dadosCarteirinha.setId(cursor.getInt(cursor.getColumnIndex("id")));
            dadosCarteirinha.setNomeUsuario(cursor.getString(cursor.getColumnIndex("nomeUsuario")));
            dadosCarteirinha.setNomeSocial(cursor.getString(cursor.getColumnIndex("nomeSocial")));
            dadosCarteirinha.setCargoUsuario(cursor.getString(cursor.getColumnIndex("cargoUsuario")));

            dadosCarteirinha.setRgUsuario(cursor.getString(
                    cursor.getColumnIndex("rgUsuario")) + "-" +cursor.getString(cursor.getColumnIndex("digitoRG"))
            );

            dadosCarteirinha.setRsUsuario(cursor.getString(cursor.getColumnIndex("rsUsuario")));
            dadosCarteirinha.setFotoUsuario(cursor.getString(cursor.getColumnIndex("fotoUsuario")));
            dadosCarteirinha.setQrCodeUsuario(cursor.getString(cursor.getColumnIndex("qrCodeUsuario")));
            dadosCarteirinha.setStatus(cursor.getString(cursor.getColumnIndex("statusAprovacao")));
            dadosCarteirinha.setCodigoCargo(cursor.getString(cursor.getColumnIndex("codigoCargo")));
            dadosCarteirinha.setValidade(cursor.getString(cursor.getColumnIndex("validade")));
        }
        catch(Exception e) {

            e.printStackTrace();
        }
        finally {

            if(cursor != null) {

                cursor.close();
            }
        }

        return dadosCarteirinha;
    }
}
