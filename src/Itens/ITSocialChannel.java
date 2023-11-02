package Itens;

import system.OLinkConfiguration;

import java.io.Serializable;

public class ITSocialChannel implements Serializable {

    private int id, idUser, specification, systemId = OLinkConfiguration.SystemConfigure.SYSTEM_ID;
    private String title;
    private String pathImage ;
    private String pixel, idPixelBackup;
    private boolean selected ;

    public ITSocialChannel(){}

    public ITSocialChannel(int id, String title, String pathImage){
        this.id = id ;
        this.title = title ;
        this.pathImage = pathImage ;
    }

    public ITSocialChannel(int id, String title, String pathImage, String pixel){
        this.id = id ;
        this.title = title ;
        this.pathImage = pathImage ;
        this.pixel = pixel ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getTitle() {
        return title;
    }

    public String getPixel() {
        return pixel;
    }

    public void setPixel(String pixel) {
        this.pixel = pixel;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSpecification() {
        return specification;
    }

    public void setSpecification(int specification) {
        this.specification = specification;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public int getSystemId() {
        return systemId;
    }

    public String getIdPixelBackup() {
        return idPixelBackup;
    }

    public void setIdPixelBackup(String idPixelBackup) {
        this.idPixelBackup = idPixelBackup;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return title;
    }

}