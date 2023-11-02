/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import Itens.ITLogin;
import Itens.ITRequestPassword;
import Itens.ITUpdatePass;
import apis.encrypt.SHA256;
import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import frames.account.UserAccount;
import org.json.JSONObject;
import system.OLinkConfiguration;
import frames.graphic.GraphicActivity;
import frames.u.GDMIFActivity;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import urls.UrlsConnetionsWEB;
import webconnection.DialogNoConnection;
import webconnection.WebJson;

import static system.OLinkConfiguration.getAccount;

/**
 *
 * @author Marlon
 */
public class UtilDialogManager {
    
    private static String code = null ;
    
    public static void showInfoDialog(String header,String message,ObservableList<Node> root, StackPane stackPane) {
        stackPane.setVisible(true);
        
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(header));
        content.setBody(new Text(message));
        JFXDialog dialog = new JFXDialog(stackPane,content, JFXDialog.DialogTransition.CENTER);
        dialog.setOnDialogClosed((Event event) -> {
            stackPane.setVisible(false);
        });
        dialog.setMinHeight(400);
        dialog.setMinWidth(400);

        dialog.show();
    }
    
    public static PieChart showPopupPieChart(StackPane stackPane, Node nodeAnchor) throws IOException {
        stackPane.setVisible(true);
        stackPane.setBackground(Background.EMPTY);
        
        FXMLLoader loader = new FXMLLoader(GraphicActivity.class.getResource("popchart.fxml"));
        AnchorPane content = loader.load();
     
        PieChart chart = (PieChart) content.lookup("#chart");
        
        JFXDialog dialog = new JFXDialog(stackPane,content, JFXDialog.DialogTransition.CENTER);
        dialog.setPadding(new Insets(10,10,10,10));
        dialog.setOnDialogClosed((Event event) -> {
            stackPane.setVisible(false);
        });
       
        dialog.show();
        
        return chart ;
    }
    
    public static void showRequestPassword(StackPane stackPane) {
        try {
            stackPane.setVisible(true);
            
            FXMLLoader loader = new FXMLLoader(CryptoUtil.class.getResource("dg_request_password.fxml"));
            
            AnchorPane root = loader.load();
            
            TextField etEmail = (TextField) root.lookup("#etEmailRequest");
            MaskedTextField etVerificaopmCode = new MaskedTextField("AAAAAA");
            etVerificaopmCode.setAlignment(Pos.CENTER);
            etVerificaopmCode.setPrefHeight(40);
            etVerificaopmCode.setPrefWidth(328);
            etVerificaopmCode.setPromptText("Insira o código aqui");
            etVerificaopmCode.setStyle("-fx-background-radius: 20; -fx-border-radius: 20");

            ProgressIndicator loading = (ProgressIndicator) root.lookup("#bpProgress");
            JFXButton btnSendEmail = (JFXButton) root.lookup("#btnSendEmail");
            Label textConfir = (Label) root.lookup("#txEmailSucess");
            
            VBox vb = (VBox) root.lookup("#vbox");
            vb.getChildren().remove(textConfir);
            VBox.setMargin(etVerificaopmCode, new Insets(0, 50, 20, 50));
                 
            ITRequestPassword requestI = new ITRequestPassword() ;
            btnSendEmail.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    loading.setVisible(true);
                    btnSendEmail.setDisable(true);

                    Platform.runLater(()->{vb.getChildren().remove(textConfir);});

                    ITUpdatePass request = new ITUpdatePass();
                    request.setEmail(etEmail.getText());

                    ITLogin login = new ITLogin();

                    JSONObject send = new JSONObject();
                    send.put("email", etEmail.getText());
                    send.put("sid", login.getSec().getSystemId());
                    send.put("mac", login.getSec().getMac());

                    InternetRequisitionAPI requestServer = OLinkConfiguration.getApiRequisition();
                    requestServer.setIdRequisition("showRequestPassword");
                    requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
                    requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
                    requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
                    requestServer.setDomain(UrlsConnetionsWEB.DOMAIN_CONNECTION);
                    requestServer.setController(UrlsConnetionsWEB.UserManager.ACCOUNTS);
                    requestServer.setFunction(UrlsConnetionsWEB.UserManager.Methods.REQUEST_PASSWORD);
                    requestServer.setObjectTransport(send);
                    requestServer.request(new InternetRequisitionResponse() {
                        @Override
                        public void onLoading(boolean loading) {
                        }

                        @Override
                        public void onSucess(Object object) {
                            if (object != null) {
                                JSONObject response = new JSONObject(object.toString());
                                response = response.getJSONObject("map");

                                ITRequestPassword request = new Gson().fromJson(response.getString("result"), ITRequestPassword.class);
                                if(request != null && request.getEmail() != null){

                                    requestI.setEmail(request.getEmail());
                                    requestI.setVerificationCode(request.getVerificationCode());
                                    requestI.setIp(request.getIp());
                                    code = request.getVerificationCode();

                                    etEmail.setDisable(true);
                                    textConfir.setVisible(true);
                                    textConfir.setText("Código enviado para o seu email");
                                    textConfir.setTextFill(Paint.valueOf("#00B200"));
                                    btnSendEmail.setDisable(true);
                                    loading.setVisible(false);
                                    etVerificaopmCode.setDisable(false);

                                    vb.getChildren().add(2,textConfir);
                                    vb.getChildren().add(etVerificaopmCode);
                                } else {
                                    loading.setVisible(false);
                                    btnSendEmail.setDisable(false);
                                    textConfir.setVisible(true);
                                    textConfir.setText("Email não encontrado");
                                    textConfir.setTextFill(Paint.valueOf("#FF0000"));

                                    vb.getChildren().add(2,textConfir);
                                }
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();
                            loading.setVisible(false);
                            btnSendEmail.setDisable(false);
                            textConfir.setVisible(true);
                            textConfir.setText("Email não encontrado");
                            textConfir.setTextFill(Paint.valueOf("#FF0000"));

                            vb.getChildren().add(2,textConfir);
                        }

                        @Override
                        public void notConnectionInternet() {
                            new DialogNoConnection(stackPane).show(true);
                        }
                    });
                }
            });
            
            JFXDialog dialog = new JFXDialog(stackPane,root, JFXDialog.DialogTransition.CENTER);
            dialog.setMinHeight(0);
            dialog.setMinWidth(0);
            dialog.prefWidth(root.getPrefWidth());
            dialog.setOnDialogClosed((Event event) -> {
                stackPane.setVisible(false);
            });
            dialog.setMinWidth(371);



            etVerificaopmCode.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                    String codeDigit = etVerificaopmCode.getText() ;
                    if(newValue.length() == 6 && !newValue.contains("_")){
                        codeDigit = SHA256.encryptBase64(codeDigit);

                        System.out.println("CODE: "+code);
                        System.out.println("CODE DIGHIT: "+codeDigit);

                        if(code.equals(codeDigit)){
                            loading.setVisible(true);
                            textConfir.setText("Validado com sucesso!");
                            textConfir.setTextFill(Paint.valueOf("#00B200"));
                            dialog.close();

                            Stage owner = (Stage) root.getScene().getWindow();
                            GDMIFActivity up = new GDMIFActivity(owner);
                            up.setResponseRequest(requestI);
                            up.show(false);
                        }
                    } else if(newValue.length() == 6 && newValue.substring(newValue.length()-1).matches("[0-9]||[A-z]") && !code.equalsIgnoreCase(etVerificaopmCode.getText())){
                        textConfir.setText("Código inválido!");
                        textConfir.setTextFill(Paint.valueOf("#FF0000"));
                    } else {
                        loading.setVisible(false);
                    }
                }
            });
                        
            dialog.show();
        } catch (IOException ex) {
            Logger.getLogger(UtilDialogManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void sleeping(int timeMills){
        try {
            Thread.sleep(timeMills);
        } catch (InterruptedException ex) {
            Logger.getLogger(UtilDialogManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static synchronized void showToast(Stage primarystage, String message, int timeinmills) {
        int fadeInTime = 500; //0.5 seconds
        int fadeOutTime= 500; //0.5 seconds
        Toast.makeText(primarystage, message, timeinmills, fadeInTime, fadeOutTime);
    }
    
    public static synchronized void showToast(Stage primarystage, String message, int timeinmills, boolean erro) {
        int fadeInTime = 500; //0.5 seconds
        int fadeOutTime= 500; //0.5 seconds
        
        Toast.makeText(primarystage, message, timeinmills, fadeInTime, fadeOutTime, erro);
    }
}
