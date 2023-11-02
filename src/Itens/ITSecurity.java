/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Itens;

import java.io.Serializable;

import system.OLinkConfiguration;
import util.UtilAddressConnection;

/**
 *
 * @author Marlon
 */
public class ITSecurity implements Serializable{

    private int systemId= OLinkConfiguration.SystemConfigure.SYSTEM_ID;
    private final String ipPublic, ipLocal, mac ;
    private String ipPublicFirst ;
    
    public ITSecurity(){
        this.ipPublic = UtilAddressConnection.getPublicIp();
        this.ipLocal = UtilAddressConnection.getLocalIp();
        this.mac = UtilAddressConnection.getMac();
    }

    public String getIpPublic() {
        return ipPublic;
    }

    public String getIpLocal() {
        return ipLocal;
    }

    public String getMac() {
        return mac;
    }

    public String getIpPublicFirst() {
        return ipPublicFirst;
    }

    public void setIpPublicFirst(String ipPublicFirst) {
        this.ipPublicFirst = ipPublicFirst;
    }

    public int getSystemId() {
        return systemId;
    }
}
