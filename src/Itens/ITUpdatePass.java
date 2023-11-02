/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Itens;
import java.io.Serializable;

import system.OLinkConfiguration;

/**
 *
 * @author Marlon
 */
public class ITUpdatePass implements Serializable{
    private int idUser, systemId= OLinkConfiguration.SystemConfigure.SYSTEM_ID;
    private String email, password, ip;
    private ITSecurity sec ;

    public ITUpdatePass(){
        this.sec = new ITSecurity();
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getSystemId() {
        return systemId;
    }

    public ITSecurity getSec() {
        return sec;
    }

    public void setPassword(String password) {
        this.password = password ;
    }
}
