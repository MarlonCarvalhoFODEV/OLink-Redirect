package Itens.biolink;

import frames.account.UserAccount;

import java.util.ArrayList;

public class ITBioLink {

    private String secret ;
    private String urlPhoto ;
    private String profileName ;
    private String description ;
    private ITBioConfig config ;
    private ArrayList<ITButtonLink> links ;

    public ITBioLink(String secret) {
        this.secret = secret;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ITBioConfig getConfig() {
        return config;
    }

    public void setConfig(ITBioConfig config) {
        this.config = config;
    }

    public ArrayList<ITButtonLink> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<ITButtonLink> links) {
        this.links = links;
    }
}
