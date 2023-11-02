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
public class ITDay extends HashMap<Integer, ITHour> {

    private int month ;
    private int idUser,idLink, idContent, idChannel,day, totalResult, systemId= OLinkConfiguration.SystemConfigure.SYSTEM_ID;
    
    public ITDay(){}

    public ITDay(int day){
        this.day = day ;
    }
    
    public ITDay(int month, int totalResult){
        this.day = month ;
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

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(int countResult) {
        this.totalResult = countResult;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getMonth() {
        return month;
    }

    public synchronized void addReceptorHour(ITReceptor rec) {
        ITHour y = new ITHour();
        y.setIdUser(rec.getIdUser());
        y.setIdLink(rec.getIdLink());
        y.setIdContent(rec.getIdContent());
        y.setIdChannel(rec.getIdChannel());
        y.setHour(rec.getHour());
        y.setTotalResult(rec.getTotalResult());
        totalResult += y.getTotalResult();
        
        put(y.getHour(), y);
    }

    public int getSystemId() {
        return systemId;
    }
}
