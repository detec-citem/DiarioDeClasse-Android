package br.gov.sp.educacao.sed.mobile.util;

import android.database.sqlite.SQLiteDatabase;

public class Banco {

    private BdManager bancoManager;

    private SQLiteDatabase sqld;

    public Banco(BdManager bancoManager) {

        this.bancoManager = bancoManager;
    }

    public synchronized void open() {

        sqld = bancoManager.getWritableDatabase();
    }

    public synchronized void close() {

        sqld = bancoManager.getWritableDatabase();

        if(sqld.isOpen()) {

            sqld.close();
        }
    }

    public synchronized SQLiteDatabase get() {

        if (sqld != null
                && sqld.isOpen()) {

            return sqld;
        }

        return null;
    }

    public synchronized boolean isOpen() {

        return sqld != null
                && sqld.isOpen();
    }
}