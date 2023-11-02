package util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class UtilDate {

    public static LocalDate getCurrentDate(){
        return LocalDate.now();
    }

    public static Period getDurationLocalDate(LocalDate start, LocalDate end){
        return Period.between(start, end);
    }

    public static LocalDateTime getCurrentDateTime(){
        return LocalDateTime.now();
    }

    public static String getDateHourFullISO_8601(){
        return Instant.now().toString();
    }

    public static String getDurationInstantISO_8601(Instant start, Instant end){
        Duration duracao = Duration.between(start, end);
        return String.valueOf(duracao.toMillis());
    }

    public static String dateFormatter(String formatterDate, LocalDate localDate){
        return DateTimeFormatter.ofPattern(formatterDate).format(localDate);
    }

    public static String dateFormatter(String formatterDate, LocalDateTime localDateTime){
        return DateTimeFormatter.ofPattern(formatterDate).format(localDateTime);
    }


    public static String dateTimeFormatter(String dateTimeFormated, String formatIN, String formatOUT ){
        DateTimeFormatter formatterIn = DateTimeFormatter.ofPattern(formatIN);
        LocalDateTime dateTimeIn = LocalDateTime.parse(dateTimeFormated, formatterIn);
        //LocalDateTime dateTime = LocalDateTime.parse("05-06-2020"+" "+"14:20:00");
        return dateFormatter(formatOUT,dateTimeIn);
    }

    public static LocalDateTime dateTimeFormatter(String dateTimeFormated, String formatIN ){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatIN);
        return LocalDateTime.parse(dateTimeFormated, formatter);
    }




    public static String getDateFromLocal(String language, String county, LocalDateTime localDateTime){
        DateTimeFormatter formatador = DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.SHORT)
                .withLocale(new Locale(language, county));
        return localDateTime.format(formatador);
    }
}
