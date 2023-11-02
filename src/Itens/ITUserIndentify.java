/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Itens;

import system.OLinkConfiguration;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Marlon
 */
public class ITUserIndentify implements Serializable{
    
   private int idUser, systemId= OLinkConfiguration.SystemConfigure.SYSTEM_ID;;
    private int typePlan ;
    private String email ;
    private Map<Integer, Boolean> serviceAccess ;

    public ITUserIndentify(){
        this.serviceAccess = new HashMap<>();
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getTypePlan() {
        return typePlan;
    }

    public void setTypePlan(int typePlan) {
        this.typePlan = typePlan;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<Integer, Boolean> getServiceAccess() {
        return serviceAccess;
    }

    public void setServiceAccess(Map<Integer, Boolean> serviceAccess) {
        this.serviceAccess = serviceAccess;
    }

    public int getSystemId() {
        return systemId;
    }
}
