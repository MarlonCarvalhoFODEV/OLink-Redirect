/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Itens;

import system.OLinkConfiguration;

import static Itens.ITYear.*;

import java.util.HashMap;

/**
 *
 * @author Marlon
 */
public class ITMonth extends HashMap<Integer, ITDay>{
    private int idUser,idLink, idContent, idChannel,month, totalResult, systemId= OLinkConfiguration.SystemConfigure.SYSTEM_ID; ;
    private String monthName ;

    public ITMonth(){}
    
    public ITMonth(int month){
        this.month = month ;
        this.monthName = getMonthName(month, false);
        this.totalResult = calculateMonthResult();
    }
    
    public ITMonth(int month, int totalResult){
        this.month = month ;
        this.monthName = getMonthName(month, false);
        this.totalResult = totalResult ;
    }

    private int calculateMonthResult(){
        totalResult = 0 ;
        values().forEach((day) -> {
            totalResult += day.getTotalResult();
        });
        return totalResult ;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdLink() {
        return idLink;
    }

    public void setIdLink(int idLink) {
        this.idLink = idLink;
    }

    public int getIdContent() {
        return idContent;
    }

    public void setIdContent(int idContent) {
        this.idContent = idContent;
    }

    public int getIdChannel() {
        return idChannel;
    }

    public void setIdChannel(int idChannel) {
        this.idChannel = idChannel;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public synchronized void addReceptorDay(ITReceptor rec) {
        ITDay y = new ITDay();
        y.setIdUser(rec.getIdUser());
        y.setIdLink(rec.getIdLink());
        y.setIdContent(rec.getIdContent());
        y.setIdChannel(rec.getIdChannel());
        y.setDay(rec.getDay());
        y.setTotalResult(rec.getTotalResult());
        totalResult += y.getTotalResult();
        put(y.getDay(), y);
    }

    public int getSystemId() {
        return systemId;
    }


    public static String getMonthName(int month, boolean fullName){
        switch(month){
            case JAN: return !fullName?"Jan":"Janeiro";
            case FEV: return !fullName?"Fev":"Fevereiro";
            case MAR: return !fullName?"Mar":"Março";
            case ABR: return !fullName?"Abr":"Abril";
            case MAI: return !fullName?"Mai":"Maio";
            case JUN: return !fullName?"Jun":"Junho";
            case JUL: return !fullName?"Jul":"Julho";
            case AGO: return !fullName?"Ago":"Agosto";
            case SET: return !fullName?"Set":"Setembro";
            case OUT: return !fullName?"Out":"Outubro";
            case NOV: return !fullName?"Nov":"Novembro";
            case DEZ: return !fullName?"Dez":"Dezembro";
            default:return null ;
        }
    }
}
