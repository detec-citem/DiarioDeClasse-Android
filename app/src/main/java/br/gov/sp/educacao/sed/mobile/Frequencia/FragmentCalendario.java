package br.gov.sp.educacao.sed.mobile.Frequencia;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.gov.sp.educacao.sed.mobile.R;

public class FragmentCalendario
        extends Fragment {

    private View mRootView;

    private List<LinearLayout> listaLinear;

    private List<String> diasMarcadosUteis;

    private List<String> diasSabados;

    private List<String> diasDomingos;

    private LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4, linearLayout5, linearLayout6;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragmentcalendario, container, false);

        linearLayout1 = mRootView.findViewById(R.id.linear1);

        linearLayout2 = mRootView.findViewById(R.id.linear2);

        linearLayout3 = mRootView.findViewById(R.id.linear3);

        linearLayout4 = mRootView.findViewById(R.id.linear4);

        linearLayout5 = mRootView.findViewById(R.id.linear5);

        linearLayout6 = mRootView.findViewById(R.id.linear6);

        diasSabados = new ArrayList<>();

        diasDomingos = new ArrayList<>();

        diasMarcadosUteis = new ArrayList<>();

        listaLinear = new ArrayList<>(5);

        listaLinear.add(linearLayout1);
        listaLinear.add(linearLayout2);
        listaLinear.add(linearLayout3);
        listaLinear.add(linearLayout4);
        listaLinear.add(linearLayout5);

        /*dias();

        System.out.println("Dias: " + linearLayout1.getChildCount());

        for(int i = 0; i < listaLinear.size(); i++) {

            for(int j = 0; j < listaLinear.get(i).getChildCount(); j++) {

                if(j == 0 || j == 6) {

                    ((TextView) listaLinear.get(i).getChildAt(j)).setBackgroundColor(getResources().getColor(R.color.cinza_indice));
                }
            }
        }

        int diaPrimeiroSabado = Integer.valueOf(diasSabados.get(0));

        int diaUltimo = Integer.valueOf(diasMarcadosUteis.get(diasMarcadosUteis.size() - 1));

        if(diaPrimeiroSabado > 2) {

            linearLayout6.setVisibility(View.INVISIBLE);
        }

        for(int i = 0; i <= 6; i++) {

            if(diaPrimeiroSabado - i > 0) {

                ((TextView) linearLayout1.getChildAt(6 - i)).setText((String.valueOf(diaPrimeiroSabado - i)));
            }
            else {

                ((TextView) linearLayout1.getChildAt(6 - i)).setBackgroundColor(getResources().getColor(R.color.azul_claro_color));

                ((TextView) linearLayout1.getChildAt(6 - i)).setText((""));
            }

            ((TextView) linearLayout2.getChildAt(i)).setText((String.valueOf((diaPrimeiroSabado + 1) + i)));

            ((TextView) linearLayout3.getChildAt(i)).setText((String.valueOf((diaPrimeiroSabado + 8) + i)));

            ((TextView) linearLayout4.getChildAt(i)).setText((String.valueOf((diaPrimeiroSabado + 15) + i)));

            if(diaPrimeiroSabado + 22 + i <= diaUltimo) {

                ((TextView) linearLayout5.getChildAt(i)).setText((String.valueOf((diaPrimeiroSabado + 22) + i)));
            }
            else {

                ((TextView) linearLayout5.getChildAt(i)).setBackgroundColor(getResources().getColor(R.color.azul_claro_color));

                ((TextView) linearLayout5.getChildAt(i)).setText((""));
            }
        }*/

        return mRootView;
    }

    private void dias() {

        Calendar cal = Calendar.getInstance();

        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        SimpleDateFormat df = new SimpleDateFormat("d/M/yyyy");

        System.out.println(df.format(cal.getTime()));

        for(int j = 0; j < 1; j++) {

            cal.set(Calendar.MONTH, j);

            for(int i = 1; i <= maxDay; i++) {

                cal.set(Calendar.DAY_OF_MONTH, i);

                diasMarcadosUteis.add(String.valueOf(i));

                if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){

                    diasSabados.add(String.valueOf(i));
                }
                else if(cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {

                    diasDomingos.add(String.valueOf(i));
                }
            }
        }
    }
}
