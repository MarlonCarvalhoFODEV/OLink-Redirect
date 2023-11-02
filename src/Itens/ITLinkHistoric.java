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
public class ITLinkHistoric extends HashMap<Integer, ITYear>{
    
    private int totalResult,systemId= OLinkConfiguration.SystemConfigure.SYSTEM_ID;
    
    public int getTotalResult(){
        totalResult = 0 ;
        values().forEach(year ->{
            System.out.println("VALUE YEAR CALC: "+year.getTotalResult());
            totalResult += year.getTotalResult();
        });
        return totalResult ;
    }
    
    public void addYear(ITYear year){
        put(year.getYear(), year);
    }
    
    public ITYear getYear(int year){
      return get(year);
    }
    
    public void addMonth(int year,ITMonth month){
        get(year).put(year, month);
    }
    
    public ITMonth getMonth(int year, int month){
        return get(year).get(month);
    }
    
    public void addDay(int year,int month, ITDay day){
        get(year).get(month).put(day.getDay(), day);
    }
    
    public ITDay getDay(int year, int month, int day){
        return get(year).get(month).get(day);
    }

    public int getSystemId() {
        return systemId;
    }

    public void addReceptorYear(ITReceptor rec) {
        ITYear y = new ITYear();
        y.setIdUser(rec.getIdUser());
        y.setIdLink(rec.getIdLink());
        y.setIdContent(rec.getIdContent());
        y.setIdChannel(rec.getIdChannel());
        y.setYear(rec.getYear());
        y.setTotalResult(rec.getTotalResult());
        put(y.getYear(), y);
    }
  
}
