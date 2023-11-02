/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import Itens.ITDay;
import Itens.ITHour;
import Itens.ITMonth;
import Itens.ITYear;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Marlon
 */
public class PackArrayTransport implements Serializable {

    private ArrayList<ITYear> yearList ;
    private ArrayList<ITMonth> monthList ;
    private ArrayList<ITDay> dayList ;
    private ArrayList<ITHour> hourList ;

    public PackArrayTransport(){}

    public ArrayList<ITYear> getYearList() {
        return yearList;
    }

    public void setYearList(ArrayList<ITYear> yearList) {
        this.yearList = yearList;
    }

    public ArrayList<ITMonth> getMonthList() {
        return monthList;
    }

    public void setMonthList(ArrayList<ITMonth> monthList) {
        this.monthList = monthList;
    }

    public ArrayList<ITDay> getDayList() {
        return dayList;
    }

    public void setDayList(ArrayList<ITDay> dayList) {
        this.dayList = dayList;
    }

    public ArrayList<ITHour> getHourList() {
        return hourList;
    }

    public void setHourList(ArrayList<ITHour> hourList) {
        this.hourList = hourList;
    }
}