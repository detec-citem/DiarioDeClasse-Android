package br.gov.sp.educacao.sed.mobile.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Utils {

    public static final boolean ALUNO_NAO_CONFIRMADO_FECHAMENTO = false;

    public static final boolean ALUNO_CONFIRMADO_PROFESSOR_FECHAMENTO = true;

    public static final String ALUNO_ATIVO_SEM_NOTA2 = "11.00";

    public static final int ALUNO_ATIVO_SEM_NOTA = 11;

    public static final int ALUNO_INATIVO_SEM_NOTA = 12;


    public static StringBuilder lerArquivoEstatico(int fileID, Context context){

        try{

            BufferedReader br = new BufferedReader(

                    new InputStreamReader(context.getResources().openRawResource(fileID), "UTF-8")
            );

            StringBuilder jsonString = new StringBuilder();

            String line;

            while((line = br.readLine()) != null) {

                line = line.trim();

                if(line.length() > 0) {

                    jsonString.append(line);
                }
            }
            return jsonString;
        }
        catch (Exception e){

            e.printStackTrace();

            return null;
        }
    }
}