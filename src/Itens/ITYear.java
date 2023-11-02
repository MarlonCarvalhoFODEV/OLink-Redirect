/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Itens;

import system.OLinkConfiguration;

import java.util.HashMap;


/**
 *
 * @author Marlon
 */
public class ITYear extends HashMap<Integer, ITMonth> {
    private int idUser,idLink, idContent, idChannel, year, totalResult,systemId= OLinkConfiguration.SystemConfigure.SYSTEM_ID;;

    public static final int JAN = 1 ;
    public static final int FEV = 2 ;
    public static final int MAR = 3 ;
    public static final int ABR = 4 ;
    public static final int MAI = 5 ;
    public static final int JUN = 6 ;
    public static final int JUL = 7 ;
    public static final int AGO = 8 ;
    public static final int SET = 9 ;
    public static final int OUT = 10 ;
    public static final int NOV = 11 ;
    public static final int DEZ = 12 ;

    public ITYear(){}
    
    public ITYear(int year){
        this.year = year ;
    }
    
    public ITYear(int year, int totalResult){
        this.year = year ;
        this.totalResult = totalResult ;
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
    
    public void setYear(int year) {
        this.year = year;
    }
    
    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
    }

    public int getYear() {
        return year;
    }

    public int getTotalResult() {
        return totalResult ;
    }
    
    public ITMonth getMonth(int month){
        return get(month);
    }
    
    public ITDay getDay(int month, int dayMax31){
        return get(month).get(dayMax31);
    }

    public int getSystemId() {
        return systemId;
    }

    public synchronized void addReceptorMonth(ITReceptor rec) {
        ITMonth y = new ITMonth();
        y.setIdUser(rec.getIdUser());
        y.setIdLink(rec.getIdLink());
        y.setIdContent(rec.getIdContent());
        y.setIdChannel(rec.getIdChannel());
        y.setMonthName(rec.getMonthName());
        y.setTotalResult(rec.getTotalResult());
        y.setMonth(rec.getMonth());
        totalResult += y.getTotalResult();
        put(y.getMonth(), y);
    }
}
