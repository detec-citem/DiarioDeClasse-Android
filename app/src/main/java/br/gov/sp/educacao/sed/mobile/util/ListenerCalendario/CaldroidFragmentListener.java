package br.gov.sp.educacao.sed.mobile.util.ListenerCalendario;

import android.view.View;

import java.util.Set;
import java.util.Date;
import java.util.List;
import java.util.HashSet;
import java.util.Calendar;
import java.util.ArrayList;

import android.app.Activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import br.gov.sp.educacao.sed.mobile.R;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import br.gov.sp.educacao.sed.mobile.util.DateUtils;

import br.gov.sp.educacao.sed.mobile.Turmas.TurmaGrupo;

import br.gov.sp.educacao.sed.mobile.Escola.Bimestre;
import br.gov.sp.educacao.sed.mobile.Escola.DiasLetivos;

import br.gov.sp.educacao.sed.mobile.Fechamento.FechamentoData;

import br.gov.sp.educacao.sed.mobile.Avaliacao.ListaAvaliacoesActivity;

import br.gov.sp.educacao.sed.mobile.RegistroDeAula.RegistroAulaActivity;

public class CaldroidFragmentListener
        extends CaldroidListener {

    private Date dataAtual;

    private Activity activity;

    private int mesSelecionado;

    private TurmaGrupo turmaGrupo;

    private Bimestre bimestreAtual;

    private Bimestre bimestreAnterior;

    private FechamentoData fechamentoData;

    private List<String> listaDiasMarcados;

    private List<Integer> listaDiasDaSemana;

    private List<String> listaDiasLetivosStr;

    private CaldroidFragment caldroidFragment;

    private final String FORMAT_DATE_DMYYYY = "d/M/yyyy";

    private final String FORMAT_DATE_DDMMYYYY = "dd/MM/yyyy";

    public CaldroidFragmentListener(CaldroidFragment caldroidFragment,
                                    Bimestre bimestreAtual,
                                    Bimestre bimestreAnterior,
                                    List<Integer> listaDiasDaSemana,
                                    List<DiasLetivos> diasLetivos,
                                    List<String> listaDiasMarcados,
                                    TurmaGrupo turmaGrupo,
                                    FechamentoData fechamentoData,
                                    Activity activity1){

        this.caldroidFragment = caldroidFragment;

        this.bimestreAtual = bimestreAtual;

        this.bimestreAnterior = bimestreAnterior;

        this.listaDiasDaSemana = listaDiasDaSemana;

        this.listaDiasLetivosStr = converteListaDiasLetivosParaListaString(diasLetivos);

        this.listaDiasMarcados = listaDiasMarcados;

        this.turmaGrupo = turmaGrupo;

        this.fechamentoData = fechamentoData;

        this.activity = activity1;
    }

    @Override
    public void onSelectDate(Date date, View view) {

        String data;

        int dia = DateUtils.getCurrentDay(date);

        int mes = DateUtils.getCurrentMonth(date);

        int ano = DateUtils.getCurrentYear(date);

        if(mes == mesSelecionado) {

            onDateSelected(date);

            if(this.activity instanceof RegistroAulaActivity) {

                data = (dia < 10 ? "0" + dia : dia) + "/" + (mes < 10 ? "0" + mes : mes) + "/" +  ano;

                ((RegistroAulaActivity) activity).usuarioSelecionouDataCalendario(data, date);
            }
            caldroidFragment.dismiss();
        }
    }

    public void onDateSelected(Date date) {}

    @Override
    public void onChangeMonth(int month, int year) {

        int diasMesAnterior = DateUtils.totalDiasMes(month - 2, year);

        int diasMes;

        for(int i = 0; i < diasMesAnterior; i++) {

            Calendar calendarAnterior = Calendar.getInstance();

            calendarAnterior.set(year, month - 2, i + 1);

            Date dataLetivoNext = calendarAnterior.getTime();

            caldroidFragment.clearBackgroundResourceForDate(dataLetivoNext);

            caldroidFragment.clearTextColorForDate(dataLetivoNext);
        }

        int diasMesNext = DateUtils.totalDiasMes(month, year);

        for(int i = 0; i < diasMesNext; i++){

            Calendar calendarNext = Calendar.getInstance();

            calendarNext.set(year, month, i + 1);

            Date dataLetivoNext = calendarNext.getTime();

            caldroidFragment.clearBackgroundResourceForDate(dataLetivoNext);

            caldroidFragment.clearTextColorForDate(dataLetivoNext);
        }
        diasMes = DateUtils.totalDiasMes(month - 1, year);

        //ArrayList<Integer> totalDiasMes = new ArrayList<>();

        Set<Integer> mapaDiasMes = new HashSet<>();

        for(int i = 0; i < diasMes; i++) {

            mapaDiasMes.add(i + 1);
        }

        ArrayList<Integer> diasLetivosConfirmados = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();

        int anoAtual = calendar.get(Calendar.YEAR);

        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE_DMYYYY);

        String strDataAtual = sdf.format(calendar.getTime());

        try {

            dataAtual = sdf.parse(strDataAtual);
        }
        catch(ParseException e) {

            e.printStackTrace();
        }

        Date date = null;

        Date inicioBimestreAnterior = null;

        Date fimBimestreAnterior = null;

        try {

            inicioBimestreAnterior = sdf.parse(bimestreAnterior.getInicio());

            fimBimestreAnterior = sdf.parse(bimestreAnterior.getFim());
        }
        catch (Exception e) {

        }

        for(String diaLetivoString: listaDiasLetivosStr) {

            try {

                date = sdf.parse(diaLetivoString);
            }
            catch(ParseException e) {

                e.printStackTrace();
            }

            Calendar cal = Calendar.getInstance();

            cal.setTime(date);

            int diaLetivoMes = cal.get(Calendar.DAY_OF_MONTH);

            if(month - 1 == cal.get(Calendar.MONTH)) {

                calendar.set(year, month - 1, diaLetivoMes);

                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                try {

                    Date inicioBimestre = sdf.parse(bimestreAtual.getInicio());

                    Date fimBimestre = sdf.parse(bimestreAtual.getFim());

                    for (int j = 0; j < listaDiasDaSemana.size(); j++) {

                        if (listaDiasDaSemana.get(j) + 1 == dayOfWeek) {

                            String strDataLetivo = sdf.format(calendar.getTime());

                            Date dataLetivo = sdf.parse(strDataLetivo);

                            String strInicioBimestre = sdf.format(inicioBimestre);
                            Date dataInicioBimestre = sdf.parse(strInicioBimestre);

                            String strFimBimestre = sdf.format(fimBimestre);
                            Date dataFimBimestre = sdf.parse(strFimBimestre);

                            String[] strDataLetivoFormatado = strDataLetivo.split("/");

                            if(strDataLetivoFormatado[0].length() < 2) {

                                strDataLetivoFormatado[0] = "0" + strDataLetivoFormatado[0];
                            }
                            if(strDataLetivoFormatado[1].length() < 2) {

                                strDataLetivoFormatado[1] = "0" + strDataLetivoFormatado[1];
                            }

                            String dataMarcada = strDataLetivoFormatado[0] + "/" + strDataLetivoFormatado[1] + "/"
                                    + strDataLetivoFormatado[2];

                            if(activity instanceof RegistroAulaActivity) {

                                    if (((dataLetivo.after(inicioBimestre)
                                            || dataLetivo.equals(inicioBimestre))
                                            && (dataLetivo.before(dataAtual)
                                            || dataLetivo.equals(dataAtual)))) {

                                        if (dataLetivo.equals(dataAtual)) {


                                            caldroidFragment.setBackgroundResourceForDate(R.color.amarelo_dia_letivo, dataLetivo);

                                            caldroidFragment.setTextColorForDate(R.color.amarelo_texto_dia_letivo, dataLetivo);
                                        }
                                        else {

                                            caldroidFragment.setBackgroundResourceForDate(R.color.azul_dia_letivo, dataLetivo);

                                            caldroidFragment.setTextColorForDate(R.color.azul_texto_dia_letivo, dataLetivo);
                                        }

                                        try {

                                            Date date1 = sdf.parse(strDataLetivo);

                                            if(listaDiasMarcados.contains(dataMarcada)) {

                                                caldroidFragment.setBackgroundResourceForDate(R.color.verde_dia_letivo, date1);

                                                caldroidFragment.setTextColorForDate(R.color.verde_texto_dia_letivo, date1);
                                            }
                                        }
                                        catch(Exception e) {

                                        }
                                        diasLetivosConfirmados.add(diaLetivoMes);
                                    }
                            }
                            else if(activity instanceof ListaAvaliacoesActivity) {

                                if(((dataLetivo.after(inicioBimestre)
                                        || dataLetivo.equals(inicioBimestre))
                                        && (dataLetivo.before(fimBimestre)
                                        || dataLetivo.equals(fimBimestre)))
                                        || (fechamentoData != null
                                        && Integer.valueOf(fechamentoData.getCodigoTipoFechamento()) - 4 == (bimestreAtual.getNumero() - 1)
                                        && (dataLetivo.after(inicioBimestreAnterior) || dataLetivo.equals(inicioBimestreAnterior))
                                        && (dataLetivo.before(fimBimestreAnterior) || dataLetivo.equals(fimBimestreAnterior)))) {

                                    if(dataLetivo.equals(dataAtual)) {

                                        caldroidFragment.setBackgroundResourceForDate(R.color.amarelo_dia_letivo, dataLetivo);

                                        caldroidFragment.setTextColorForDate(R.color.amarelo_texto_dia_letivo, dataLetivo);
                                    }
                                    else {

                                        caldroidFragment.setBackgroundResourceForDate(R.color.azul_dia_letivo, dataLetivo);

                                        caldroidFragment.setTextColorForDate(R.color.azul_texto_dia_letivo, dataLetivo);
                                    }
                                    try {

                                        Date date1 = sdf.parse(strDataLetivo);

                                        if(listaDiasMarcados.contains(dataMarcada)) {

                                            caldroidFragment.setBackgroundResourceForDate(R.color.verde_dia_letivo, date1);

                                            caldroidFragment.setTextColorForDate(R.color.verde_texto_dia_letivo, date1);
                                        }
                                    }
                                    catch(Exception e) {

                                    }
                                    diasLetivosConfirmados.add(diaLetivoMes);
                                }
                            }
                        }
                    }
                }
                catch(ParseException e) {

                    e.printStackTrace();
                }
            }
        }

        //ArrayList<Integer> totalDiasMesCopia = new ArrayList<>();

        ArrayList<String> disableDateStrings = new ArrayList<>();

        //totalDiasMesCopia.addAll(totalDiasMes);

        for(int diaLetivo : mapaDiasMes) {

            if(!diasLetivosConfirmados.contains(diaLetivo)) {

                disableDateStrings.add(DateUtils.parseDateNormal(diaLetivo, month, year));
            }
        }

        caldroidFragment.setDisableDatesFromString(disableDateStrings);

        mesSelecionado = month;

        if(month == 1 && year > anoAtual) {

            caldroidFragment.prevMonth();
        }

        if(month == 12 && year < anoAtual) {

            caldroidFragment.nextMonth();
        }
    }

    private List<String> converteListaDiasLetivosParaListaString(List<DiasLetivos> diasLetivos) {

        if(diasLetivos != null) {

            List<String> listaDiasLetivosStr = new ArrayList<>();

            try {

                for(DiasLetivos diaLetivo : diasLetivos) {

                    SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE_DDMMYYYY);

                    Date date = sdf.parse(diaLetivo.getDataAula());

                    listaDiasLetivosStr.add(sdf.format(date));
                }
                return listaDiasLetivosStr;
            }
            catch(ParseException e) {

                e.printStackTrace();
            }
        }
        return null;
    }
}