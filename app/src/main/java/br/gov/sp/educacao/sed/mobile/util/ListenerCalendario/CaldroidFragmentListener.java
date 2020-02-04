package br.gov.sp.educacao.sed.mobile.util.ListenerCalendario;

import android.app.Activity;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.gov.sp.educacao.sed.mobile.Avaliacao.ListaAvaliacoesActivity;
import br.gov.sp.educacao.sed.mobile.Escola.Bimestre;
import br.gov.sp.educacao.sed.mobile.Escola.DiasLetivos;
import br.gov.sp.educacao.sed.mobile.Fechamento.FechamentoData;
import br.gov.sp.educacao.sed.mobile.R;
import br.gov.sp.educacao.sed.mobile.RegistroDeAula.RegistroAulaActivity;
import br.gov.sp.educacao.sed.mobile.RegistroDeAula.RegistroAulaFundamentalActivity;
import br.gov.sp.educacao.sed.mobile.util.DateUtils;

public class CaldroidFragmentListener extends CaldroidListener {
    //Variáveis
    private int mesSelecionado;
    private Date dataAtual;
    private List<String> listaDiasMarcados;
    private List<String> listaDiasLetivosStr;
    private Bimestre bimestreAtual;
    private Bimestre bimestreAnterior;
    private Activity activity;
    private FechamentoData fechamentoData;
    private CaldroidFragment caldroidFragment;

    //Construtor
    public CaldroidFragmentListener(CaldroidFragment caldroidFragment, Bimestre bimestreAtual, Bimestre bimestreAnterior, List<DiasLetivos> diasLetivos, List<String> listaDiasMarcados, FechamentoData fechamentoData, Activity activity) {
        int i;
        this.caldroidFragment = caldroidFragment;
        this.bimestreAtual = bimestreAtual;
        this.bimestreAnterior = bimestreAnterior;
        this.listaDiasMarcados = listaDiasMarcados;
        this.fechamentoData = fechamentoData;
        this.activity = activity;
        int numeroDiasLetivos = diasLetivos.size();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        List<String> listaDiasLetivosStr = new ArrayList<>();
        try {
            for(i = 0; i < numeroDiasLetivos; i++) {
                DiasLetivos diaLetivo = diasLetivos.get(i);
                Date data = sdf.parse(diaLetivo.getDataAula());
                listaDiasLetivosStr.add(sdf.format(data));
            }
            this.listaDiasLetivosStr = listaDiasLetivosStr;
        }
        catch (ParseException e) {
            Crashlytics.logException(e);
        }
    }

    //Métodos
    public void onDateSelected(Date date) {
    }

    //CaldroidListener
    @Override
    public void onSelectDate(Date date, View view) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(date);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        int mes = calendario.get(Calendar.MONTH);
        int ano = calendario.get(Calendar.YEAR);
        if (mesSelecionado == mes + 1) {
            String diaString;
            if (dia < 10) {
                diaString = "0" + dia;
            }
            else {
                diaString = String.valueOf(dia);
            }
            String mesString;
            if (mesSelecionado < 10) {
                mesString = "0" + mesSelecionado;
            }
            else {
                mesString = String.valueOf(mesSelecionado);
            }
            String data = diaString + "/" + mesString + "/" +  ano;
            if (activity instanceof RegistroAulaActivity) {
                ((RegistroAulaActivity) activity).usuarioSelecionouDataCalendario(data, date);
            }
            else if (activity instanceof RegistroAulaFundamentalActivity) {
                ((RegistroAulaFundamentalActivity) activity).usuarioSelecionouDataCalendario(data, date);
            }
            caldroidFragment.dismiss();
            onDateSelected(date);
        }
    }

    @Override
    public void onChangeMonth(int month, int year) {
        int i;
        int j;
        int mesAtual = month - 1;
        int mesAnterior = month - 2;
        Date inicioBimestre = null;
        Date inicioBimestreAnterior = null;
        Date fimBimestre = null;
        Date fimBimestreAnterior = null;
        Date data = null;
        Calendar calendar = new GregorianCalendar(year, mesAnterior, 1);
        int diasMesAnterior = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar = Calendar.getInstance();
        for(i = 0; i < diasMesAnterior; i++) {
            calendar.set(year, mesAnterior, i + 1);
            Date dataLetivo = calendar.getTime();
            caldroidFragment.clearBackgroundResourceForDate(dataLetivo);
            caldroidFragment.clearTextColorForDate(dataLetivo);
        }
        calendar = new GregorianCalendar(year, month, 1);
        int diasProximoMes = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar = Calendar.getInstance();
        for(i = 0; i < diasProximoMes; i++) {
            calendar.set(year, month, i + 1);
            Date dataLetivo = calendar.getTime();
            caldroidFragment.clearBackgroundResourceForDate(dataLetivo);
            caldroidFragment.clearTextColorForDate(dataLetivo);
        }
        calendar = new GregorianCalendar(year, month, 1);
        int diasMesAtual = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Set<Integer> mapaDiasMes = new HashSet<>();
        for(i = 0; i < diasMesAtual; i++) {
            mapaDiasMes.add(i + 1);
        }
        ArrayList<Integer> diasLetivosConfirmados = new ArrayList<>();
        calendar = Calendar.getInstance();
        int anoAtual = calendar.get(Calendar.YEAR);
        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");
        String strDataAtual = sdf.format(new Date());
        try {
            dataAtual = sdf.parse(strDataAtual);
            inicioBimestreAnterior = sdf.parse(bimestreAnterior.getInicio());
            fimBimestreAnterior = sdf.parse(bimestreAnterior.getFim());
            inicioBimestre = sdf.parse(bimestreAtual.getInicio());
            fimBimestre = sdf.parse(bimestreAtual.getFim());
        }
        catch(ParseException e) {
            Crashlytics.logException(e);
        }
        int numeroDiasLetivos = listaDiasLetivosStr.size();
        for(i = 0; i < numeroDiasLetivos; i++) {
            String diaLetivoString = listaDiasLetivosStr.get(i);
            try {
                data = sdf.parse(diaLetivoString);
            }
            catch(ParseException e) {
                Crashlytics.logException(e);
            }
            calendar = Calendar.getInstance();
            calendar.setTime(data);
            if(mesAtual == calendar.get(Calendar.MONTH)) {
                int diaDiaLetivo = calendar.get(Calendar.DAY_OF_MONTH);
                calendar.set(year, mesAtual, diaDiaLetivo);
                String dataLetivoString = sdf.format(calendar.getTime());
                Date dataLetivo = null;
                try {
                    dataLetivo = sdf.parse(dataLetivoString);
                }
                catch(ParseException e) {
                    Crashlytics.logException(e);
                }
                String[] dataLetivoFormatado = dataLetivoString.split("/");
                if (dataLetivoFormatado.length >= 3) {
                    if(dataLetivoFormatado[0].length() < 2) {
                        dataLetivoFormatado[0] = "0" + dataLetivoFormatado[0];
                    }
                    if(dataLetivoFormatado[1].length() < 2) {
                        dataLetivoFormatado[1] = "0" + dataLetivoFormatado[1];
                    }
                    String dataMarcada = dataLetivoFormatado[0] + "/" + dataLetivoFormatado[1] + "/" + dataLetivoFormatado[2];
                    if(activity instanceof RegistroAulaActivity || activity instanceof RegistroAulaFundamentalActivity) {
                        if ((dataLetivo.after(inicioBimestre) || dataLetivo.equals(inicioBimestre)) && (dataLetivo.before(dataAtual) || dataLetivo.equals(dataAtual))) {
                            if (dataLetivo.equals(dataAtual)) {
                                caldroidFragment.setBackgroundResourceForDate(R.color.amarelo_dia_letivo, dataLetivo);
                                caldroidFragment.setTextColorForDate(R.color.amarelo_texto_dia_letivo, dataLetivo);
                            }
                            else {
                                caldroidFragment.setBackgroundResourceForDate(R.color.azul_dia_letivo, dataLetivo);
                                caldroidFragment.setTextColorForDate(R.color.azul_texto_dia_letivo, dataLetivo);
                            }
                            if (listaDiasMarcados.contains(dataMarcada)) {
                                caldroidFragment.setBackgroundResourceForDate(R.color.verde_dia_letivo, dataLetivo);
                                caldroidFragment.setTextColorForDate(R.color.verde_texto_dia_letivo, dataLetivo);
                            }
                            diasLetivosConfirmados.add(diaDiaLetivo);
                        }
                    }
                    else if(activity instanceof ListaAvaliacoesActivity) {
                        if (((dataLetivo.after(inicioBimestre) || dataLetivo.equals(inicioBimestre)) && (dataLetivo.before(fimBimestre) || dataLetivo.equals(fimBimestre))) || (fechamentoData != null && Integer.valueOf(fechamentoData.getCodigoTipoFechamento()) - 4 == (bimestreAtual.getNumero() - 1) && (dataLetivo.after(inicioBimestreAnterior) || dataLetivo.equals(inicioBimestreAnterior)) && (dataLetivo.before(fimBimestreAnterior) || dataLetivo.equals(fimBimestreAnterior)))) {
                            if (dataLetivo.equals(dataAtual)) {
                                caldroidFragment.setBackgroundResourceForDate(R.color.amarelo_dia_letivo, dataLetivo);
                                caldroidFragment.setTextColorForDate(R.color.amarelo_texto_dia_letivo, dataLetivo);
                            }
                            else {
                                caldroidFragment.setBackgroundResourceForDate(R.color.azul_dia_letivo, dataLetivo);
                                caldroidFragment.setTextColorForDate(R.color.azul_texto_dia_letivo, dataLetivo);
                            }
                            if(listaDiasMarcados.contains(dataMarcada)) {
                                caldroidFragment.setBackgroundResourceForDate(R.color.verde_dia_letivo, dataLetivo);
                                caldroidFragment.setTextColorForDate(R.color.verde_texto_dia_letivo, dataLetivo);
                            }
                            diasLetivosConfirmados.add(diaDiaLetivo);
                        }
                    }
                }
            }
        }
        ArrayList<String> datasDesabilitadas = new ArrayList<>();
        for(int diaLetivo : mapaDiasMes) {
            if (!diasLetivosConfirmados.contains(diaLetivo)) {
                datasDesabilitadas.add(DateUtils.parseDateNormal(diaLetivo, month, year));
            }
        }
        caldroidFragment.setDisableDatesFromString(datasDesabilitadas);
        mesSelecionado = month;
        if(month == 1 && year > anoAtual) {
            caldroidFragment.prevMonth();
        }
        if(month == 12 && year < anoAtual) {
            caldroidFragment.nextMonth();
        }
    }
}