package br.gov.sp.educacao.sed.mobile.comunicados;

import org.json.JSONArray;

import java.util.ArrayList;

public class ComunicadoTO {

    private JSONArray jsonComunicados;

    public ComunicadoTO(JSONArray jsonComunicados) {

        this.jsonComunicados = jsonComunicados;
    }

    public ArrayList<Comunicado> getComunicadosFromJson(){

        ArrayList<Comunicado> comunicados = new ArrayList<>();

        try{

            for(int i = 0; i < jsonComunicados.length(); i++){

                Comunicado comunicado = new Comunicado();

                comunicado.setCdComunicado(jsonComunicados.getJSONObject(i).getInt("CodigoMensagem"));
                comunicado.setTitulo(jsonComunicados.getJSONObject(i).getString("TituloMensagen"));
                comunicado.setComunicado(jsonComunicados.getJSONObject(i).getString("DescricaoMensagem"));
                comunicado.setData(jsonComunicados.getJSONObject(i).getString("DataInclusao"));
                comunicado.setVisualizado(false);

                comunicados.add(comunicado);
            }
        }
        catch (Exception e){

            e.printStackTrace();
        }

        return comunicados;
    }

    public int getQtd(){

        if(jsonComunicados != null){

            if(!jsonComunicados.toString().contains("Sem permissão ou token expirado.")){

                return jsonComunicados.length();
            }
            else{

                return -1;
            }
        }
        else{

            return 0;
        }
    }
}
