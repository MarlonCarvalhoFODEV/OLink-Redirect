package Itens;

import java.io.Serializable;

public class ITLinkReplace implements Serializable {

    private String currentURL ;
    private String substitutueURL ;

    public ITLinkReplace() {
    }

    public ITLinkReplace(String currentURL, String substitutueURL) {
        this.currentURL = currentURL;
        this.substitutueURL = substitutueURL;
    }

    public String getCurrentURL() {
        return currentURL;
    }

    public void setCurrentURL(String currentURL) {
        this.currentURL = currentURL;
    }

    public String getSubstitutueURL() {
        return substitutueURL;
    }

    public void setSubstitutueURL(String substitutueURL) {
        this.substitutueURL = substitutueURL;
    }
}
