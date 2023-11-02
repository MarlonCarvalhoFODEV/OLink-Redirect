package Itens;

import java.io.Serializable;

public class ITLaunch implements Serializable {

    private String dateStart, dateEnd, hourStart, hourEnd;
    private String urlBefore, urlDuring, urlAfter ;

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getHourStart() {
        return hourStart;
    }

    public void setHourStart(String hourStart) {
        this.hourStart = hourStart;
    }

    public String getHourEnd() {
        return hourEnd;
    }

    public void setHourEnd(String hourEnd) {
        this.hourEnd = hourEnd;
    }

    public String getUrlBefore() {
        return urlBefore;
    }

    public void setUrlBefore(String urlBefore) {
        this.urlBefore = urlBefore;
    }

    public String getUrlDuring() {
        return urlDuring;
    }

    public void setUrlDuring(String urlDuring) {
        this.urlDuring = urlDuring;
    }

    public String getUrlAfter() {
        return urlAfter;
    }

    public void setUrlAfter(String urlAfter) {
        this.urlAfter = urlAfter;
    }
}
