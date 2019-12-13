package br.gov.sp.educacao.sed.mobile.comunicados;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import br.gov.sp.educacao.sed.mobile.util.Banco;

public class ComunicadoBdCrud {

    private Banco banco;

    public ComunicadoBdCrud(Banco banco) {

        this.banco = banco;
    }

    public void salvarComunicadosBanco(ArrayList<Comunicado> comunicados){

        ContentValues valores = new ContentValues();

        for(Comunicado comunicado : comunicados){

            valores.clear();

            valores.put("cd_comunicado", comunicado.getCdComunicado());
            valores.put("titulo", comunicado.getTitulo());
            valores.put("comunicado", comunicado.getComunicado());
            valores.put("data", comunicado.getData());
            valores.put("visualizado", comunicado.isVisualizado());

            banco.get().insertWithOnConflict("COMUNICADOS", null, valores, SQLiteDatabase.CONFLICT_IGNORE);
        }
    }

    public Comunicado getUltimoComunicadoNaoVisto(){

        Cursor cursor = null;

        Comunicado comunicado = null;

        try{

            cursor = banco.get().rawQuery("SELECT * FROM COMUNICADOS WHERE visualizado = 0 ORDER BY data DESC LIMIT 1", null);

            if(cursor.moveToFirst()){

                comunicado = new Comunicado();

                comunicado.setCdComunicado(cursor.getInt(cursor.getColumnIndex("cd_comunicado")));
                comunicado.setTitulo(cursor.getString(cursor.getColumnIndex("titulo")));
                comunicado.setComunicado(cursor.getString(cursor.getColumnIndex("comunicado")));
                comunicado.setData(cursor.getString(cursor.getColumnIndex("data")));
                comunicado.setVisualizado((cursor.getInt(cursor.getColumnIndex("visualizado"))) == 1);
            }
        }
        catch (Exception e){

            e.printStackTrace();
        }
        finally {

            if(cursor != null){

                cursor.close();
            }
        }

        return comunicado;
    }

    public ArrayList<Comunicado> getTodosComunicados(){

        Cursor cursor = null;

        ArrayList<Comunicado> comunicados = new ArrayList<>();

        try{

            cursor = banco.get().rawQuery("SELECT * FROM COMUNICADOS ORDER BY data DESC", null);

            if(cursor.moveToFirst()){

                Comunicado comunicado;

                do{
                    comunicado = new Comunicado();

                    comunicado.setCdComunicado(cursor.getInt(cursor.getColumnIndex("cd_comunicado")));
                    comunicado.setTitulo(cursor.getString(cursor.getColumnIndex("titulo")));
                    comunicado.setComunicado(cursor.getString(cursor.getColumnIndex("comunicado")));
                    comunicado.setData(cursor.getString(cursor.getColumnIndex("data")));
                    comunicado.setVisualizado((cursor.getInt(cursor.getColumnIndex("visualizado"))) == 1);

                    comunicados.add(comunicado);

                }while (cursor.moveToNext());

            }
        }
        catch (Exception e){

            e.printStackTrace();
        }
        finally {

            if(cursor != null){

                cursor.close();
            }
        }

        return comunicados;
    }

    public void atualizarComunicadoVisto(int cdComunicado){

        banco.get().execSQL("UPDATE COMUNICADOS SET visualizado = 1 WHERE cd_comunicado = ?", new String[]{String.valueOf(cdComunicado)});
    }

    public boolean temComunicadoBanco(){

        Cursor cursor = null;

        boolean encontrouRegistro = false;

        try{

            cursor = banco.get().rawQuery("SELECT * FROM COMUNICADOS WHERE visualizado = 0 ORDER BY data DESC LIMIT 1", null);

            if(cursor.moveToFirst()){

                encontrouRegistro = true;
            }
            else{

                encontrouRegistro = false;
            }
        }
        catch (Exception e){

            e.printStackTrace();
        }
        finally {

            if(cursor != null){

                cursor.close();
            }
        }

        return encontrouRegistro;
    }
}
