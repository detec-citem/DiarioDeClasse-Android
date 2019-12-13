package br.gov.sp.educacao.sed.mobile.Carteirinha;

import android.database.sqlite.SQLiteStatement;

import br.gov.sp.educacao.sed.mobile.util.Banco;

 class CarteirinhaDBcrud {

    private Banco banco;

    private String queryUpdateCarteirinhas;

    private String queryInsertCarteirinhas;

    private String queryDeletarCarteirinha;

    @SuppressWarnings("FieldCanBeLocal")
    private SQLiteStatement statementInsertCarteirinhas;

    @SuppressWarnings("FieldCanBeLocal")
    private SQLiteStatement statementUpdateCarteirinhas;

    @SuppressWarnings("FieldCanBeLocal")
    private SQLiteStatement statementDeletarCarteirinha;

    private final String TAG = CarteirinhaDBcrud.class.getSimpleName();

    CarteirinhaDBcrud(Banco banco) {

        this.banco = banco;

        queryInsertCarteirinhas =

                "INSERT INTO CARTEIRINHAS (idUsuario, nomeSocial, codigoCargo, cargoUsuario, rsUsuario, fotoUsuario, qrCodeUsuario, statusAprovacao, validade) " +
                        "VALUES (1, ?, ?, ?, ?, ?, ?, ?, ?);";

        queryUpdateCarteirinhas =

                "UPDATE CARTEIRINHAS SET cargoUsuario = ?, nomeSocial = ?, rsUsuario = ?, fotoUsuario = ?, qrCodeUsuario = ?, statusAprovacao = ?, validade = ? WHERE codigoCargo = ?;";

        queryDeletarCarteirinha =

                "DELETE FROM CARTEIRINHAS WHERE codigoCargo = ?;";
    }

    void insertCarteirinha(DadosCarteirinha dadosCarteirinha) {

         statementInsertCarteirinhas = this.banco.get().compileStatement(queryInsertCarteirinhas);

         try {

             statementInsertCarteirinhas.bindString(1, dadosCarteirinha.getNomeSocial());
             statementInsertCarteirinhas.bindString(2, dadosCarteirinha.getCodigoCargo());
             statementInsertCarteirinhas.bindString(3, dadosCarteirinha.getCargoUsuario());
             statementInsertCarteirinhas.bindString(4, dadosCarteirinha.getRsUsuario());
             statementInsertCarteirinhas.bindString(5, "data:image/jpeg;base64," + dadosCarteirinha.getFotoUsuario());
             statementInsertCarteirinhas.bindString(6, dadosCarteirinha.getQrCodeUsuario());
             statementInsertCarteirinhas.bindString(7, dadosCarteirinha.getStatus());
             statementInsertCarteirinhas.bindString(8, dadosCarteirinha.getValidade());

             statementInsertCarteirinhas.executeInsert();
         }
         catch(Exception e) {

             e.printStackTrace();
         }
         finally {

             statementInsertCarteirinhas.clearBindings();

             statementInsertCarteirinhas.close();
         }
     }

    void atualizarCarteirinha(DadosCarteirinha dadosCarteirinha) {

        statementUpdateCarteirinhas = this.banco.get().compileStatement(queryUpdateCarteirinhas);

        try {

               statementUpdateCarteirinhas.bindString(1, dadosCarteirinha.getCargoUsuario());
               statementUpdateCarteirinhas.bindString(2, dadosCarteirinha.getNomeSocial());
               statementUpdateCarteirinhas.bindString(3, dadosCarteirinha.getRsUsuario());
               statementUpdateCarteirinhas.bindString(4, "data:image/jpeg;base64," + dadosCarteirinha.getFotoUsuario());
               statementUpdateCarteirinhas.bindString(5, dadosCarteirinha.getQrCodeUsuario());
               statementUpdateCarteirinhas.bindString(6, dadosCarteirinha.getStatus());
               statementUpdateCarteirinhas.bindString(7, dadosCarteirinha.getValidade());
               statementUpdateCarteirinhas.bindString(8, dadosCarteirinha.getCodigoCargo());

               if(statementUpdateCarteirinhas.executeUpdateDelete() == 0) {

                   insertCarteirinha(dadosCarteirinha);
               }
        }
        catch(Exception e) {

            e.printStackTrace();
        }
        finally {

            statementUpdateCarteirinhas.clearBindings();

            statementUpdateCarteirinhas.close();
        }
    }

    void deletarCarteirinha(String codigoCargo) {

        statementDeletarCarteirinha = banco.get().compileStatement(queryDeletarCarteirinha);

        statementDeletarCarteirinha.bindString(1, codigoCargo);

        statementDeletarCarteirinha.executeUpdateDelete();

        statementDeletarCarteirinha.clearBindings();

        statementDeletarCarteirinha.close();
     }
 }
