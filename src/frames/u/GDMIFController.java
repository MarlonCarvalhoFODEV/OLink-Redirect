/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames.u;

import Itens.ITLogin;
import Itens.ITRequestPassword;
import Itens.ITUpdatePass;
import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import com.google.gson.Gson;
import javafx.scene.paint.Paint;
import org.json.JSONObject;
import system.OLinkConfiguration;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import urls.UrlsConnetionsWEB;
import util.CryptoUtil;
import util.Toast;
import webconnection.DialogNoConnection;
import webconnection.WebJson;

/**
 *
 * @author Marlon
 */
public class GDMIFController implements Initializable{

    private Stage ownerStage ;
    private ITRequestPassword item ;
    
    @FXML
    private TextField etNewPassword ;
    @FXML
    private TextField etNewPasswordConfirm ;
    @FXML
    private BorderPane bpLoading ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initVariableStage();
    }
    
    private void initVariableStage(){
        Thread th = new Thread(OLinkConfiguration.getThreadGroup(), ()-> {
            sleeping(500);
            ownerStage = (Stage) etNewPassword.getScene().getWindow();
        });
        th.start();
    }
    
    @FXML
    public void updatePassword(){
        sendNewPassword();
    }
    
    private void sendNewPassword(){
        int idUser = item.getIdUser();
        String email = item.getEmail();
        String password = CryptoUtil.encryptMD5(etNewPassword.getText().getBytes());
        String passwordRepeat = CryptoUtil.encryptMD5(etNewPasswordConfirm.getText().getBytes());

        if(validateFields(email, password, passwordRepeat)) {
            showLoading(true);

            ITLogin login = new ITLogin();

            JSONObject send = new JSONObject();
            send.put("id_user", idUser);
            send.put("email", email);
            send.put("pass", password);
            send.put("sid", login.getSec().getSystemId());
            send.put("mac", login.getSec().getMac());

            InternetRequisitionAPI requestServer = OLinkConfiguration.getApiRequisition();
            requestServer.setIdRequisition("sendNewPassword");
            requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
            requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
            requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
            requestServer.setDomain(UrlsConnetionsWEB.DOMAIN_CONNECTION);
            requestServer.setController(UrlsConnetionsWEB.UserManager.ACCOUNTS);
            requestServer.setFunction(UrlsConnetionsWEB.UserManager.Methods.UPDATE_PASSWORD);
            requestServer.setObjectTransport(send);
            requestServer.request(new InternetRequisitionResponse() {
                @Override
                public void onLoading(boolean loading) { }

                @Override
                public void onSucess(Object object) {
                    if (object != null) {
                        JSONObject response = new JSONObject(object.toString());
                        response = response.getJSONObject("map");

                        boolean result = new Gson().fromJson(response.getString("result"), Boolean.class);
                        showLoading(false);

                        if (Boolean.valueOf(result)) {
                            Toast.makeText(ownerStage, "Atualizado com sucesso", 0, 200, 2000);

                            ((Stage) etNewPassword.getScene().getWindow()).close();
                        } else {
                            Toast.makeText(ownerStage, "Não tem permissão para atualizar", 0, 200, 2000, true);
                        }
                    }
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                    //showLoading(false);
                }

                @Override
                public void notConnectionInternet() {
                    //new DialogNoConnection(stackPane).show(true);
                }
            });
        }
    }
    
    private boolean validateFields(String email, String pass, String confirmPass){
        boolean mail = (email != null);
        boolean pas = pass.equals(confirmPass);
        boolean validadation = (mail && pas);
        if(!mail){
            Toast.makeText(ownerStage, "Email Nulo", 0, 1000, 5000, true);
        } 
        
        if(!pas){
            Toast.makeText(ownerStage, "Senhas são diferentes", 0, 1000, 5000, true);
        }
        
        return validadation ;
    }
    
    private void showLoading(boolean show){
        if(show){
            bpLoading.setVisible(true);
        } else {
            bpLoading.setVisible(false);
        }
    }
    
    private void sleeping(int timeMills){
        try {
            Thread.sleep(timeMills);
        } catch (InterruptedException ex) {
            Logger.getLogger(GDMIFController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setResponse(ITRequestPassword response) {
        item = response ;
    }
}
