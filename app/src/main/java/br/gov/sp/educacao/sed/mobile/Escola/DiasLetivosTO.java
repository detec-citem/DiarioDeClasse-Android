package br.gov.sp.educacao.sed.mobile.Escola;

import android.content.ContentValues;

import org.json.JSONException;

import br.gov.sp.educacao.sed.mobile.util.GenericsTable;

public class DiasLetivosTO
        implements GenericsTable {

    public static final String nomeTabela = "DIASLETIVOS";

    private int bimestre_id;

    private int semanaMes;

    private int diaSemana;

    private int mes;

    private String dataAula;

    public DiasLetivosTO() {

    }

    public void setJSON(int bimestre_id, int dia, int mes, int anoletivo, int diaSemana, int semanaMes, int mes1) throws JSONException {

        this.bimestre_id = bimestre_id;

        this.semanaMes = semanaMes;

        this.diaSemana = diaSemana;

        this.mes = mes1;

        dataAula = dia + "/" + mes + "/" + anoletivo;
    }

    public DiasLetivosTO(int bimestre_id,
                         int dia,
                         int mes,
                         int anoletivo) throws JSONException {

        this.bimestre_id = bimestre_id;

        dataAula = dia + "/" + mes + "/" + anoletivo;
    }

    @Override
    public ContentValues getContentValues() {

        ContentValues values = new ContentValues();

        values.put("dataAula", dataAula);
        values.put("bimestre_id", bimestre_id);
        values.put("diaSemana", diaSemana);
        values.put("semanaMes", semanaMes);
        values.put("mes", mes);

        return values;
    }

    @Override
    public String getCodigoUnico() {

        return "dataAula = '" + dataAula + "' and bimestre_id = " + bimestre_id;
    }
}