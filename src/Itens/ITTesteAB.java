package Itens;

import java.io.Serializable;

public class ITTesteAB implements Serializable {

    private int timeDays ;
    private String destinyA, destinyB;

    public int getTimeDays() {
        return timeDays;
    }

    public void setTimeDays(int timeDays) {
        this.timeDays = timeDays;
    }

    public String getDestinyA() {
        return destinyA;
    }

    public void setDestinyA(String destinyA) {
        this.destinyA = destinyA;
    }

    public String getDestinyB() {
        return destinyB;
    }

    public void setDestinyB(String destinyB) {
        this.destinyB = destinyB;
    }
}
