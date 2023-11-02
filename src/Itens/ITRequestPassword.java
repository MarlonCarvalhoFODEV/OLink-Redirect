/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Itens;

import system.OLinkConfiguration;

/**
 *
 * @author Marlon
 */
public class ITRequestPassword {
    
    private int idUser, systemId= OLinkConfiguration.SystemConfigure.SYSTEM_ID;
    private String email, verificationCode, password, ip ;

    public ITRequestPassword(){}

    public ITRequestPassword(String email, String verificationCode, String password, String ip){
        this.email = email ;
        this.verificationCode = verificationCode ;
        this.password = password ;
        this.ip = ip ;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public int getIdUser() {
        return idUser ;
    }

    public int getSystemId() {
        return systemId;
    }
}
