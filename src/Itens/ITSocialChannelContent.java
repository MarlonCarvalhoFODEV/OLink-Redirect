package Itens;

import system.OLinkConfiguration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ITSocialChannelContent implements Serializable {

    private int id, idUser, idChannel, specification, countClickTotal, countSale, index, systemId = OLinkConfiguration.SystemConfigure.SYSTEM_ID;
    ;
    private double totalBilling;
    private String title, pathImage, category;
    private List<ITLink> links;

    public ITSocialChannelContent() {
        this.links = new ArrayList<>();
    }

    public ITSocialChannelContent(String title) {
        this.title = title;
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

    public int getIdChannel() {
        return idChannel;
    }

    public void setIdChannel(int idChannel) {
        this.idChannel = idChannel;
    }

    public int getSpecification() {
        return specification;
    }

    public void setSpecification(int specification) {
        this.specification = specification;
    }

    public int getCountClickTotal() {
        countClickTotal = 0;
        links.forEach(link -> {
            countClickTotal += link.getCountClick();
            countClickTotal += link.getCountSale();
        });
        return countClickTotal;
    }

    public void setCountClickTotal(int countClickTotal) {
        this.countClickTotal = countClickTotal;
    }

    public int getCountSale() {
        return countSale;
    }

    public void setCountSale(int countSale) {
        this.countSale = countSale;
    }

    public double getTotalBilling() {
        return totalBilling;
    }

    public void setTotalBilling(double totalBilling) {
        this.totalBilling = totalBilling;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<ITLink> getLinks() {
        return links;
    }

    public void setLinks(List<ITLink> links) {
        this.links = links;
    }

    public int getSystemId() {
        return systemId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
