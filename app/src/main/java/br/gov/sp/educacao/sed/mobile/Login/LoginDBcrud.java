package br.gov.sp.educacao.sed.mobile.Login;

import android.database.sqlite.SQLiteStatement;

import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;

public class LoginDBcrud {

    private Banco banco;

    private String token;

    private String login;

    private String queryTokenUsuario;

    private String queryLoginUsuario;

    private String queryAtualizarUsuario;

    private SQLiteStatement statementTokenUsuario;

    private SQLiteStatement statementLoginUsuario;

    private SQLiteStatement statementAtualizarUsuario;

    private final String TAG = LoginDBcrud.class.getSimpleName();

    public LoginDBcrud(Banco banco) {

        this.banco = banco;

        queryTokenUsuario =

                "SELECT token FROM USUARIO WHERE id = 1 AND ativo = 1;";

        queryLoginUsuario =

                "SELECT usuario FROM USUARIO WHERE id = 1 AND ativo = 1;";

        queryAtualizarUsuario =

                "UPDATE USUARIO SET usuario = ?, senha = ?, nome = ?, cpf = ?, rg = ?, digitoRG = ?, dataUltimoAcesso = ?, ativo = ?, token = ?;";

        statementAtualizarUsuario = banco.get().compileStatement(queryAtualizarUsuario);
    }

    public String getTokenUsuario() {

        token = "";

        statementTokenUsuario = banco.get().compileStatement(queryTokenUsuario);

        try {

            token = statementTokenUsuario.simpleQueryForString();
        }
        catch(Exception e) {

            e.printStackTrace();

            CrashAnalytics.e(TAG, e);
        }
        finally {

            statementTokenUsuario = null;
        }
        return token;
    }

    public String getLoginUsuario() {

        login = "";

        statementLoginUsuario = banco.get().compileStatement(queryLoginUsuario);

        try {

            login = statementLoginUsuario.simpleQueryForString();
        }
        catch(Exception e) {

            e.printStackTrace();

            CrashAnalytics.e(TAG, e);
        }
        finally {

            statementLoginUsuario = null;
        }
        return login;
    }

    void inserirUsuarioNoBanco(UsuarioTO usuarioTO) {

        String queryInserirUsuarioNoBanco =

                "INSERT INTO USUARIO (usuario, senha, nome, cpf, rg, digitoRG, dataUltimoAcesso, ativo, token) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        SQLiteStatement statementInserirUsuarioNoBanco = banco.get().compileStatement(queryInserirUsuarioNoBanco);

        statementInserirUsuarioNoBanco.bindString(1, usuarioTO.getUsuario());
        statementInserirUsuarioNoBanco.bindString(2, usuarioTO.getSenha());
        statementInserirUsuarioNoBanco.bindString(3, usuarioTO.getNome());
        statementInserirUsuarioNoBanco.bindString(4, usuarioTO.getCpf());
        statementInserirUsuarioNoBanco.bindString(5, usuarioTO.getRg());
        statementInserirUsuarioNoBanco.bindString(6, usuarioTO.getDigitoRG());
        statementInserirUsuarioNoBanco.bindString(7, usuarioTO.getDataUltimoAcesso());
        statementInserirUsuarioNoBanco.bindLong(  8, 1);
        statementInserirUsuarioNoBanco.bindString(9, usuarioTO.getToken());

        try {

            banco.get().beginTransaction();

            statementInserirUsuarioNoBanco.executeInsert();
        }
        catch(Exception e) {

            e.printStackTrace();
        }
        finally {

            banco.get().setTransactionSuccessful();

            banco.get().endTransaction();

            statementInserirUsuarioNoBanco.clearBindings();

            statementInserirUsuarioNoBanco = null;

            queryInserirUsuarioNoBanco = null;
        }
    }

    public void atualizarUsuario(UsuarioTO usuarioTO) {

        statementAtualizarUsuario.bindString(1, usuarioTO.getUsuario());
        statementAtualizarUsuario.bindString(2, usuarioTO.getSenha());
        statementAtualizarUsuario.bindString(3, usuarioTO.getNome());
        statementAtualizarUsuario.bindString(4, usuarioTO.getCpf());
        statementAtualizarUsuario.bindString(5, usuarioTO.getRg());
        statementAtualizarUsuario.bindString(6, usuarioTO.getDigitoRG());
        statementAtualizarUsuario.bindString(7, usuarioTO.getDataUltimoAcesso());
        statementAtualizarUsuario.bindLong(  8, 1);
        statementAtualizarUsuario.bindString(9, usuarioTO.getToken());

        try {

            banco.get().beginTransaction();

            statementAtualizarUsuario.executeUpdateDelete();
        }
        catch(Exception e) {

            e.printStackTrace();

            CrashAnalytics.e(TAG, e);
        }
        finally {

            banco.get().setTransactionSuccessful();

            banco.get().endTransaction();

            statementAtualizarUsuario.clearBindings();
        }
    }
}
