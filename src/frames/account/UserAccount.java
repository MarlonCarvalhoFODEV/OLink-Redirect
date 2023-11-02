package frames.account;

import Itens.ITSecurity;
import system.OLinkConfiguration;

import java.io.Serializable;

public class UserAccount implements Serializable {

    private int id, idPartner, systemId= OLinkConfiguration.SystemConfigure.SYSTEM_ID;
    private String name ;
    private String password ;
    private String partnerLink ;
    private UserContacts contacts ;
    private boolean access, partner;
    private ITSecurity sec ;

    public static final int BLOCKED_ACCESS = -500 ;

    public UserAccount(){
        this.contacts = new UserContacts();
        this.sec = new ITSecurity();
        this.access = true ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPartner() {
        return idPartner;
    }

    public void setIdPartner(int idPartner) {
        this.idPartner = idPartner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserContacts getContacts() {
        return contacts;
    }

    public void setContacts(UserContacts contacts) {
        this.contacts = contacts;
    }

    public boolean isAccess() {
        return access;
    }

    public void setAccess(boolean access) {
        this.access = access;
    }

    public boolean isPartner() {
        return partner;
    }

    public void setPartner(boolean partner) {
        this.partner = partner;
    }

    public String getPartnerLink() {
        return partnerLink;
    }

    public void setPartnerLink(String partnerLink) {
        this.partnerLink = partnerLink;
    }

    public ITSecurity getSec() {
        return sec;
    }

    public int getSystemId() {
        return systemId;
    }

    public String getSecret(){
        StringBuilder sb = new StringBuilder();
        sb.append(id);
        sb.append("-");
        sb.append(systemId);
        sb.append("-");
        sb.append(sec.getMac());
        sb.append(sec.getIpPublicFirst());

        return OLinkConfiguration.encryptExtended(sb.toString());
    }
}
