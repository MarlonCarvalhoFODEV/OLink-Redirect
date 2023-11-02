package Itens;

import java.io.Serializable;

public class ITPixelBackup implements Serializable {

    private int idUser;
    private String idPixel, pixelName, idAccountADS, accessToken ;

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getIdPixel() {
        return idPixel;
    }

    public void setIdPixel(String idPixel) {
        this.idPixel = idPixel;
    }

    public String getPixelName() {
        return pixelName;
    }

    public void setPixelName(String pixelName) {
        this.pixelName = pixelName;
    }

    public String getIdAccountADS() {
        return idAccountADS;
    }

    public void setIdAccountADS(String idAccountADS) {
        this.idAccountADS = idAccountADS;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
