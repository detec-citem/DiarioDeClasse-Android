package br.gov.sp.educacao.sed.mobile.util;

import android.database.sqlite.SQLiteDatabase;

public class Banco {
    //Variáveis
    private SQLiteDatabase database;

    //Constructor
    public Banco(BdManager bancoManager) {
        database = bancoManager.getWritableDatabase();
    }

    public SQLiteDatabase get() {
        return database;
    }
}