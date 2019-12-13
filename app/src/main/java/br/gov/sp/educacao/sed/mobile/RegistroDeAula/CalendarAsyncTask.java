package br.gov.sp.educacao.sed.mobile.RegistroDeAula;

import android.app.Activity;

import android.os.AsyncTask;

import com.roomorama.caldroid.CaldroidFragment;

import java.util.ArrayList;

import br.gov.sp.educacao.sed.mobile.Escola.Bimestre;
import br.gov.sp.educacao.sed.mobile.Escola.DiasLetivos;
import br.gov.sp.educacao.sed.mobile.Fechamento.FechamentoData;
import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;
import br.gov.sp.educacao.sed.mobile.util.ListenerCalendario.CaldroidFragmentListener;

public class CalendarAsyncTask
        extends AsyncTask<Object, Void, CaldroidFragmentListener> {

    RegistroAulaActivity delegate = null;

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
    }

    @Override
    protected CaldroidFragmentListener doInBackground(Object... params) {

        CaldroidFragmentListener listener = new CaldroidFragmentListener(

                (CaldroidFragment) params[0],
                (Bimestre) params[1],
                (Bimestre) params[2],
                (ArrayList<Integer>)params[3],
                (ArrayList<DiasLetivos>)params[4],
                (ArrayList<String>)params[5],
                (TurmaGrupo)params[6],
                (FechamentoData)params[7],
                (Activity)params[8]);

        return listener;
    }

    @Override
    protected void onPostExecute(CaldroidFragmentListener result) {

        super.onPostExecute(result);

        delegate.setarListener(result);
    }
}
