package br.gov.sp.educacao.sed.mobile.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtils {

    public static final String FORMAT_DATE_DDMMYYYY = "dd/MM/yyyy";

    public static final String FORMAT_DATE_DDMMYYYY_HH_MM = "dd/MM/yyyy hh:mm";

    public static Calendar asLastHourOfDay(Calendar dataAtual) {

        Calendar result = new GregorianCalendar();

        result.setTime(asLastHourOfDay(new Date(dataAtual.getTimeInMillis())));

        return result;
    }

    public static Date asLastHourOfDay(Date dataVigencia) {

        Calendar calendar = GregorianCalendar.getInstance();

        calendar.setTime(dataVigencia);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        return new Date(calendar.getTimeInMillis());
    }

    public static int getCurrentDay(Date date) {

        Calendar cal = Calendar.getInstance();

        cal.setTime(date);

        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static int getCurrentMonth(Date date) {

        Calendar cal = Calendar.getInstance();

        cal.setTime(date);

        return cal.get(Calendar.MONTH) + 1;
    }

    public static int getCurrentYear(Date date) {

        Calendar cal = Calendar.getInstance();

        cal.setTime(date);

        return cal.get(Calendar.YEAR);
    }

    public static int totalDiasMes(int mes,
                                   int ano) {

        Calendar calDias = new GregorianCalendar(ano, mes, 1);

        return calDias.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static String parseDateNormal(int dia,
                                         int mes,
                                         int ano) {

        return ano + "-" + (mes < 10 ? "0" + mes : mes) + "-" + (dia < 10 ? "0" + dia : dia);
    }

    public static Calendar convertStringToDate(String data) {

        Calendar dataConvertida = null;

        if(data != null) {

            String[] dataSeparada = data.split("/");

            String dia = dataSeparada[0];
            String mes = dataSeparada[1];
            String ano = dataSeparada[2];

            dataConvertida = Calendar.getInstance();

            dataConvertida.set(Calendar.DATE, Integer.parseInt(dia));
            dataConvertida.set(Calendar.MONTH, Integer.parseInt(mes) - 1);
            dataConvertida.set(Calendar.YEAR, Integer.parseInt(ano));
            dataConvertida.set(Calendar.HOUR,0);
            dataConvertida.set(Calendar.MINUTE,0);
            dataConvertida.set(Calendar.SECOND,0);
            dataConvertida.set(Calendar.MILLISECOND,0);

        }
        else {

            Log.e("Conversão da data","[DateUtils.convertStringToDate] Falha na conversão de data [null]");
        }
        return dataConvertida;
    }

    public static String convertBarraParaTraco(String data)
            throws ParseException {

        SimpleDateFormat sdfTraco = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ROOT);

        Date dateToBeParsed = sdf.parse(data);

        return sdfTraco.format(dateToBeParsed);
    }

    public static String getCurrentDate() {

        Calendar cal = Calendar.getInstance();

        Date currentDate = cal.getTime();

        DateFormat date = new SimpleDateFormat(FORMAT_DATE_DDMMYYYY);

        return date.format(currentDate);
    }

    public static String convertDateToString(Date date,
                                             String dateFormat) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

        return simpleDateFormat.format(date);
    }
}