/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Itens;

import system.OLinkConfiguration;

import java.util.ArrayList;

/**
 *
 * @author Marlon
 */
public class ITHour extends ArrayList<Integer> implements Comparable<ITHour>{
    
    private int idUser,idLink, idContent, idChannel, hour,systemId= OLinkConfiguration.SystemConfigure.SYSTEM_ID ;
    private int totalResult ;

    public ITHour(){}
    
    public ITHour(int day){
        this.hour = day ;
    }
    
    public ITHour(int hour, int totalResult){
        this.hour = hour ;
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

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
    }

    public int getSystemId() {
        return systemId;
    }

    @Override
    public int compareTo(ITHour o) {
        if(hour==o.getHour())  
            return 0;  
        else if(hour>o.getHour())  
            return 1;  
        else  
            return -1;  
    }


}
