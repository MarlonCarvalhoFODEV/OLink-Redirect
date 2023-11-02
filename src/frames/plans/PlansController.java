/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames.plans;

import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.ResourceBundle;

import frames.checkout.CheckoutController;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import util.CryptoUtil;
import util.Toast;

import static system.OLinkConfiguration.getAccount;
import static system.OLinkConfiguration.getThreadGroup;
import static webconnection.WebJson.*;

/**
 *
 * @author Marlon
 */
public class PlansController implements Initializable{

    @FXML
    private BorderPane bpLoading;
    @FXML
    private WebView wvLoadPay;
    
    public static final String MENSAL_PLAN = "https://olink.me/plano-mensal";
    public static final String TRIMESTRAL_PLAN = "https://olink.me/plano-trimestral";
    public static final String ANUAL_PLAN = "https://olink.me/plano-anual";
    private static final String CONFIRMATION_BUY = "https://olink.me/obrigado-pela-compra";
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    @FXML
    public void startPlanFree() throws Exception{
        
    }
    
    @FXML
    public void startPlanTrimestral() throws IOException, URISyntaxException {
        trimestralPlan();
       
    }
    
    @FXML
    public void startPlanAnual() throws IOException, URISyntaxException {
        anualPlan();
    }
    
    @FXML
    public void startPlanMensal() throws IOException, URISyntaxException {
        mensalPlan();
    }
    
     private void initCheckout(String url) throws IOException {

        StringBuilder sb = new StringBuilder();
        sb.append(url);
        sb.append("?iduser=");
        sb.append(getAccount().getId());

        wvLoadPay.setVisible(true);
        wvLoadPay.setDisable(false);
        wvLoadPay.getEngine().setJavaScriptEnabled(true);
        wvLoadPay.getEngine().setUserAgent(CheckoutController.UserAgent.USER_AGENT);
        wvLoadPay.getEngine().load(sb.toString());

        wvLoadPay.getEngine().getLoadWorker().stateProperty().addListener((ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                showProgress(false);

                if(wvLoadPay.getEngine().locationProperty().getValue().equals(CONFIRMATION_BUY)){
                    if(haveConnection()){
                        Thread th = new Thread(getThreadGroup(), ()->{

                            Platform.runLater(()->{
                                Stage stage = (Stage) wvLoadPay.getScene().getWindow();

                                Toast.makeText(stage,"Pagamento Realizado com Sucesso!",200,500,5000);
                                startApplication();
                                stage.close();
                            });

                            try {
                                Thread.sleep(5000);

                                closeApplication();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        });
                        th.start();
                    } else {
                        Stage stage = (Stage) wvLoadPay.getScene().getWindow();
                        Toast.makeText(stage, "Sem Conexão com a internet",200,500,5000, true);
                    }
                }
            }
        });
    }

    private void mensalPlan() throws IOException, URISyntaxException {
       showProgress(true);
       //initCheckout(MENSAL_PLAN);
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI(MENSAL_PLAN));
        }
    }

    private void trimestralPlan() throws IOException, URISyntaxException {
       showProgress(true);
       //initCheckout(TRIMESTRAL_PLAN);
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI(TRIMESTRAL_PLAN));
        }
    }

    private void anualPlan() throws IOException, URISyntaxException {
        showProgress(true);
        //initCheckout(ANUAL_PLAN);
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI(ANUAL_PLAN));
        }
     }

    private void showProgress(boolean show){
        if(show){
            bpLoading.setVisible(show);
        } else {
            bpLoading.setVisible(false);
        }
    }
}
