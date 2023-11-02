package webconnection;

import apis.date.DateAPI;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Testing {

    public static void main(String[] args) {
        try {
            String hour = DateAPI.getDateManager().format("HH:mm:ss","HH:mm","08:05:00");
            System.out.println(hour);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void setAttribute(String code, String attr){
        String codeInitial = code.substring(0, code.indexOf(">")+1);
        String codeMiddle = code.substring(code.indexOf(">")+1, code.indexOf("</"));
        String codeFinal = code.substring(code.indexOf("</"));

        StringBuilder sb = new StringBuilder();
        sb.append(codeInitial.replace(">",attr));
        sb.append(" >");
        sb.append(codeMiddle);
        sb.append(codeFinal);

        code = sb.toString();
    }

    public static String getAttribute(String code, String attr){
        String codeInitial = code.substring(0, code.indexOf(">")+1);
        String[] attrs = codeInitial.split(" ");

        for(int i=0; i < attrs.length; i++){
            if(attrs[i].contains(attr)){
                return attrs[i].split("\"")[1];
            }
        }
        return "" ;
    }

    public static String getAttributes(String code, String attr){
        String codeInitial = code.substring(0, code.indexOf(">")+1);

        String value = codeInitial.substring(codeInitial.indexOf(attr)+attr.length()+1);
        value = value.split("\"")[0];
        return value ;
    }
}
