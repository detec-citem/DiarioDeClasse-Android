package br.gov.sp.educacao.sed.mobile.util;

import android.content.Context;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import br.gov.sp.educacao.sed.mobile.util.CrashAnalytics.CrashAnalytics;

public abstract class BdManager
        extends SQLiteOpenHelper {

    protected Context context;

    public BdManager(Context context,
                     String nome,
                     int versao) {

        super(context, nome, null, versao);

        this.context = context;
    }

    public abstract void onCreate(SQLiteDatabase bd);

    public abstract void onUpgrade(SQLiteDatabase bd,
                                   int versaoAtual,
                                   int versaoNova);

    protected void byFile(int fileID,
                          SQLiteDatabase bd)
                          throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(context.getResources()
                                                                            .openRawResource(fileID), "UTF-8"));
        StringBuilder sql = new StringBuilder();

        String line;

        while ((line = br.readLine()) != null) {

            line = line.trim();

            if (line.length() > 0) {

                sql.append(line);

                if (line.endsWith(";")) {

                    bd.execSQL(sql.toString());

                    sql.delete(0, sql.length());
                }
            }
        }
    }

    public ArrayList<Cursor> getData(String Query) {

        SQLiteDatabase sqlDB = this.getWritableDatabase();

        String[] columns = new String[] { "mesage" };

        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);

        MatrixCursor Cursor2= new MatrixCursor(columns);

        alc.add(null);

        alc.add(null);

        try {

            String maxQuery = Query ;

            Cursor c = sqlDB.rawQuery(maxQuery, null);

            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);

            if (null != c && c.getCount() > 0) {

                alc.set(0,c);

                c.moveToFirst();

                return alc ;
            }
            return alc;
        }
        catch(Exception ex) {

            CrashAnalytics.e("BdManager", ex);

            Cursor2.addRow(new Object[] { ""+ex.getMessage() });

            alc.set(1,Cursor2);

            return alc;
        }
    }
}