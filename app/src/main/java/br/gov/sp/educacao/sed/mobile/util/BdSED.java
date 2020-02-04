package br.gov.sp.educacao.sed.mobile.util;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import br.gov.sp.educacao.sed.mobile.R;
import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;
import com.crashlytics.android.Crashlytics;
import java.io.IOException;
import java.util.ArrayList;

public class BdSED extends BdManager {
    //Constants
    private static final String NAME = "SEDDB";
    private static final String TAG = "SEDDB";
    private static final int VERSAO = 18;

    //Construtor
    public BdSED(Context context) {
        super(context, NAME, VERSAO);
    }

    //Lifecycle
    @Override
    public void onCreate(SQLiteDatabase bd) {
        criarBanco(bd);
    }

    @Override
    public void onUpgrade(SQLiteDatabase bd, int versaoAtual, int versaoNova) {
        try {
            bd.beginTransaction();
            bd.execSQL("DROP TABLE IF EXISTS FECHAMENTO_TURMA;");
            bd.execSQL("DROP TABLE IF EXISTS FECHAMENTO_ALUNO;");
            bd.execSQL("DROP TABLE IF EXISTS MEDIA_ALUNO;");
            bd.execSQL("DROP TABLE IF EXISTS TIPO_FECHAMENTO;");
            bd.execSQL("DROP TABLE IF EXISTS FALTASALUNOS;");
            bd.execSQL("DROP TABLE IF EXISTS DIASLETIVOS;");
            bd.execSQL("DROP TABLE IF EXISTS BIMESTRE;");
            bd.execSQL("DROP TABLE IF EXISTS NOTASALUNO;");
            bd.execSQL("DROP TABLE IF EXISTS AVALIACOES;");
            bd.execSQL("DROP TABLE IF EXISTS TOTALFALTASALUNOS;");
            bd.execSQL("DROP TABLE IF EXISTS AULAS;");
            bd.execSQL("DROP TABLE IF EXISTS DISCIPLINA;");
            bd.execSQL("DROP TABLE IF EXISTS TURMASFREQUENCIA;");
            bd.execSQL("DROP TABLE IF EXISTS ALUNOS;");
            bd.execSQL("DROP TABLE IF EXISTS TURMAS;");
            bd.execSQL("DROP TABLE IF EXISTS USUARIO;");
            bd.execSQL("DROP TABLE IF EXISTS NOVO_HABILIDADE_REGISTRO;");
            bd.execSQL("DROP TABLE IF EXISTS NOVO_GRUPO;");
            bd.execSQL("DROP TABLE IF EXISTS NOVO_CURRICULO;");
            bd.execSQL("DROP TABLE IF EXISTS NOVO_CONTEUDO;");
            bd.execSQL("DROP TABLE IF EXISTS NOVO_HABILIDADE;");
            bd.execSQL("DROP TABLE IF EXISTS NOVO_CONTEUDO_HABILIDADE;");
            bd.execSQL("DROP TABLE IF EXISTS NOVO_CONTEUDO_REGISTRO;");
            bd.execSQL("DROP TABLE IF EXISTS NOVO_REGISTRO;");
            bd.execSQL("DROP TABLE IF EXISTS DIASCOMFREQUENCIA;");
            bd.execSQL("DROP TABLE IF EXISTS DIASCONFLITO;");
            bd.execSQL("DROP TABLE IF EXISTS CARTEIRINHAS;");
            bd.execSQL("DROP TABLE IF EXISTS COMUNICADOS;");
            bd.execSQL("DROP TABLE IF EXISTS CURRICULO_FUNDAMENTAL;");
            bd.execSQL("DROP TABLE IF EXISTS CONTEUDO_FUNDAMENTAL;");
            bd.execSQL("DROP TABLE IF EXISTS REGISTRO_AULA_FUNDAMENTAL;");
            bd.execSQL("DROP TABLE IF EXISTS HABILIDADE_REGISTRO_FUNDAMENTAL;");
            bd.execSQL("DROP TABLE IF EXISTS REGISTRO_JSON;");
        }
        catch (Exception e) {
            Crashlytics.logException(e);
            CrashAnalytics.e(TAG, e);
        }
        finally {
            bd.setTransactionSuccessful();
            bd.endTransaction();
        }
        criarBanco(bd);
    }

    //Métodos
    private void criarBanco(SQLiteDatabase database) {
        try {
            byFile(R.raw.script, database);
        }
        catch (IOException e) {
            Crashlytics.logException(e);
            CrashAnalytics.e(TAG, e);
        }
    }

    public ArrayList<Cursor> getData(String Query) {
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        ArrayList<Cursor> alc = new ArrayList<>(2);
        MatrixCursor cursor2 = new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);
        try {
            String maxQuery = Query ;
            Cursor c = sqlDB.rawQuery(maxQuery, null);
            cursor2.addRow(new Object[] { "Success" });
            alc.set(1, cursor2);
            if (c != null && c.getCount() > 0) {
                alc.set(0, c);
                c.moveToFirst();
                return alc;
            }
            return alc;
        }
        catch (Exception ex) {
            CrashAnalytics.e(TAG, ex);
            cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1, cursor2);
            return alc;
        }
    }
}