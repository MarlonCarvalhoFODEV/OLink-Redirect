/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Itens;

import system.OLinkConfiguration;

import java.io.Serializable;

/**
 *
 * @author Marlon
 */
public class ITLogin implements Serializable{

    private int systemId=OLinkConfiguration.SystemConfigure.SYSTEM_ID;
    private String e, p;
    private ITSecurity sec ;

    public ITLogin(){
        this.sec = new ITSecurity();
    }
    
    public ITLogin(String email, String pass){
        this.e = email ;
        this.p = pass ;
        this.sec = new ITSecurity();
    }
    
    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public ITSecurity getSec() {
        return sec;
    }

    public int getSystemId() {
        return systemId;
    }
}
