package Itens;

import system.OLinkConfiguration;
import urls.UrlsConnetionsWEB;

import java.io.Serializable;

public class ITLink implements Serializable {

    private int id, idUser, idChannel, idContent, countClick,countViews, countSale, redirectType, index, systemId= OLinkConfiguration.SystemConfigure.SYSTEM_ID;
    private double value ;
    private String domain, linkName, fromLink, toLink, page;
    private boolean statusResult, testAB, launching;

    private ITPixel pixel ;
    private ITTesteAB testeAB ;
    private ITLaunch launch ;

    public ITLink(){}

    public ITLink(int id, int idUser, int idChannel, int idContent, String linkName, String fromLink, String toLink, int countClick, int countView, double value, ITLaunch la, ITTesteAB teAB){
        this.id = id ;
        this.idUser = idUser ;
        this.idChannel = idChannel ;
        this.idContent = idContent ;
        this.linkName = linkName ;
        this.fromLink = fromLink ;
        this.toLink = toLink ;
        this.countClick = countClick ;
        this.countViews = countView ;
        this.value = value ;
        this.launch = la ;
        this.testeAB = teAB ;
    }

    public ITLink(int id, int idUser, int idChannel, int idContent, String linkName, String fromLink, String toLink, ITPixel pixel,int redirectType, int countClick, int countView, int countSale, double value, ITLaunch la, ITTesteAB teAB){
        this.id = id ;
        this.idUser = idUser ;
        this.idChannel = idChannel ;
        this.idContent = idContent ;
        this.linkName = linkName ;
        this.fromLink = fromLink ;
        this.toLink = toLink ;
        this.pixel = pixel ;
        this.countClick = countClick ;
        this.countViews = countView ;
        this.countSale = countSale ;
        this.value = value ;
        this.redirectType = redirectType ;
        if(la.getDateStart() != null && !la.getDateStart().isEmpty())this.launch = la ;
        if(teAB.getTimeDays()>0)this.testeAB = teAB ;
    }

    public ITLink(int id, int idUser, int idChannel, int idContent, String linkName, String fromLink, String toLink, int countClick, int countView, ITLaunch la, ITTesteAB teAB){
        this.id = id ;
        this.idUser = idUser ;
        this.idChannel = idChannel ;
        this.idContent = idContent ;
        this.linkName = linkName ;
        this.fromLink = fromLink ;
        this.toLink = toLink ;
        this.countClick = countClick ;
        this.countViews = countView ;
        this.launch = la ;
        this.testeAB = teAB ;
    }

    public ITLink(int id, int idUser, int idChannel, int idContent, String linkName, String fromLink, String toLink, ITPixel pixel, int countClick, int countView, ITLaunch la, ITTesteAB teAB){
        this.id = id ;
        this.idUser = idUser ;
        this.idChannel = idChannel ;
        this.idContent = idContent ;
        this.linkName = linkName ;
        this.fromLink = fromLink ;
        this.toLink = toLink ;
        this.pixel = pixel ;
        this.countClick = countClick ;
        this.countViews = countView ;
        this.launch = la ;
        this.testeAB = teAB ;
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

    public int getIdContent() {
        return idContent;
    }

    public void setIdContent(int idContent) {
        this.idContent = idContent;
    }

    public int getCountClick() {
        return countClick;
    }

    public void setCountClick(int countClick) {
        this.countClick = countClick;
    }

    public int getCountViews() {
        return countViews;
    }

    public void setCountViews(int countViews) {
        this.countViews = countViews;
    }

    public int getCountSale() {
        return countSale;
    }

    public void setCountSale(int countSale) {
        this.countSale = countSale;
    }

    public int getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(int redirectType) {
        this.redirectType = redirectType;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getFromLink() {
        return fromLink;
    }

    public void setFromLink(String fromLink) {
        this.fromLink = fromLink;
    }

    public String getToLink() {
        return toLink;
    }

    public void setToLink(String toLink) {
        this.toLink = toLink;
    }

    public ITPixel getPixel() {
        return pixel;
    }

    public void setPixel(ITPixel pixel) {
        this.pixel = pixel;
    }

    public boolean isStatusResult() {
        return statusResult;
    }

    public void setStatusResult(boolean statusResult) {
        this.statusResult = statusResult;
    }

    public int getSystemId() {
        return systemId;
    }

    public boolean isTestAB() {
        boolean check = (testeAB!=null) && (testeAB.getTimeDays()>0) && testeAB.getDestinyA() != null;
        return testAB=check;
    }

    public boolean isLaunching() {
        boolean check = (launch!=null) && (launch.getDateStart() != null) && !launch.getDateStart().isEmpty()
                && launch.getDateEnd() != null && !launch.getDateEnd().isEmpty()
                && launch.getUrlBefore() != null && !launch.getUrlBefore().isEmpty()
                && launch.getUrlDuring() != null && !launch.getUrlDuring().isEmpty()
                && launch.getUrlAfter() != null && !launch.getUrlAfter().isEmpty();
        return launching=check;
    }

    public boolean isLaunchingAndTesteAB(){
        return isTestAB() && isLaunching() ;
    }

    public void setLaunching(boolean launching) {
        this.launching = launching;
    }

    public ITTesteAB getTesteAB() {
        return testeAB;
    }

    public void setTesteAB(ITTesteAB testeAB) {
        this.testeAB = testeAB;
    }

    public ITLaunch getLaunch() {
        return launch;
    }

    public void setLaunch(ITLaunch launch) {
        this.launch = launch;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
