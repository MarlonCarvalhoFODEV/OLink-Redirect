/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames.checkout;

import Itens.ITPlan;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.css.converter.URLConverter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import system.OLinkConfiguration;
import urls.UrlsConnetionsWEB;
import webconnection.DialogNoConnection;

import static frames.plans.PlansController.*;
import static webconnection.WebJson.haveConnection;

/**
 *
 * @author Marlon
 */
public class CheckoutController implements Initializable{

    @FXML
    private WebView wvLoadPay;
    @FXML
    private BorderPane bpProgress;
    @FXML
    private StackPane stackPane ;
    
    private ITPlan plan ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCheckout();
    }

    private void initCheckout(){
        if(haveConnection()){
            String url = null ;
            switch(plan.getId()){
                case 94741290: url = MENSAL_PLAN;break;
                case 9476455: url = TRIMESTRAL_PLAN; break;
                case 94741289: url = ANUAL_PLAN; break;
                default: url = "https://rendalivre.com"; break;
            }

            wvLoadPay.setVisible(true);
            wvLoadPay.setDisable(false);
            wvLoadPay.getEngine().setJavaScriptEnabled(true);
            wvLoadPay.getEngine().setUserAgent(UserAgent.USER_AGENT);
            wvLoadPay.getEngine().load(url);
            wvLoadPay.getEngine().getLoadWorker().stateProperty().addListener((ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) -> {
                if (newValue == Worker.State.SUCCEEDED) {
                    showProgress(false);
                    System.out.println("URL: "+wvLoadPay.getEngine().locationProperty());


                }
            });


            String c = (String) wvLoadPay.getEngine().executeScript("return document.cookie");
            CookieManager cookieManager = new CookieManager();
            CookieHandler.setDefault(cookieManager);
            cookieManager.getCookieStore().getCookies().forEach(cookie -> {
                String name = cookie.getName();
                String value = cookie.getValue();
                String domain = cookie.getDomain();
                long maxAge = cookie.getMaxAge(); // seconds
                boolean secure = cookie.getSecure();

                System.out.println("NAME: "+name);
                System.out.println("VALUE: "+value);
                System.out.println("DOMAIN: "+domain);
                System.out.println("MAX_AGE: "+maxAge);
                System.out.println("SECURE: "+secure);
                System.out.println("***************************");
            });
        } else {
            new DialogNoConnection(stackPane).show(true);
        }
    }
    
    public void setPlan(ITPlan responsePlan) {
        this.plan = responsePlan ;
    }
   
    private void showProgress(boolean show){
        if(show){
            bpProgress.setVisible(show);
        } else {
            bpProgress.setVisible(false);
        }
    }

    public static class UserAgent{
        public static final String USER_AGENT = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Mobile Safari/537.36";
    }
}
