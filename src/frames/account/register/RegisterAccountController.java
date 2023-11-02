package frames.account.register;

import Itens.ITLogin;
import apis.encrypt.SHA256;
import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import org.json.JSONObject;
import system.OLinkConfiguration;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import urls.UrlsConnetionsWEB;
import webconnection.DialogNoConnection;

import java.net.*;
import java.util.ResourceBundle;


public class RegisterAccountController implements Initializable {

    @FXML
    private TextField etName, etEmail ;

    @FXML
    private PasswordField etPassword, etConfirmPassword ;

    @FXML
    private BorderPane bpLoading ;

    @FXML
    private Button btnRegister ;

    @FXML
    private StackPane stackPane ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        etEmail.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().equals(" ")) {
                change.setText("");
            }
            return change;
        }));
    }

    private void showLoading(boolean show){
        Platform.runLater(() -> {
            bpLoading.setVisible(show);
            btnRegister.setDisable(show);
        });
    
    }

    public void registerAccount(){
        if(verifyInformations()){
            showLoading(true);

            ITLogin login = new ITLogin();

            JSONObject send = new JSONObject();
            send.put("name", etName.getText());
            send.put("email", etEmail.getText());
            send.put("pass", SHA256.encryptBase64(etPassword.getText()));
            send.put("sid", login.getSec().getSystemId());
            send.put("mac", login.getSec().getMac());

            InternetRequisitionAPI requestServer = OLinkConfiguration.getApiRequisition();
            requestServer.setIdRequisition("registerAccount");
            requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
            requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.OTHER_RSA);
            requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
            requestServer.setDomain(UrlsConnetionsWEB.DOMAIN_CONNECTION);
            requestServer.setController(UrlsConnetionsWEB.UserManager.ACCOUNTS);
            requestServer.setFunction(UrlsConnetionsWEB.UserManager.Methods.REGISTER);
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

                        int result = response.getInt("result");

                        if (result > 0) {
                            Alert dialog = dialogInformation("STATUS DE REGISTRO", "Cadastro Realizado Com Sucesso!", Alert.AlertType.CONFIRMATION);
                            dialog.showAndWait();

                            showLoading(false);

                            Stage stage = (Stage) etName.getScene().getWindow();
                            stage.close();
                        } else if (result == -1) {

                            Alert dialog = dialogInformation("STATUS DE REGISTRO", "Já existe um usuário com esse EMAIL!", Alert.AlertType.ERROR);
                            dialog.showAndWait();
                        } else if(result == 2){
                            Alert dialog = dialogInformation("STATUS DE REGISTRO", "Você já possui uma conta, tente acessa-la", Alert.AlertType.ERROR);
                            dialog.showAndWait();
                        }else {
                            Alert dialog = dialogInformation("STATUS DE REGISTRO", "Erro Durante o Cadastro", Alert.AlertType.ERROR);
                            dialog.showAndWait();
                        }

                        showLoading(false);
                    }
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                    showLoading(false);
                }

                @Override
                public void notConnectionInternet() {
                    new DialogNoConnection(stackPane).show(true);
                }
            });
        }
    }

    private boolean verifyInformations(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro no preenchimento dos campos!");

        if(etName.getText() != null && !etName.getText().isEmpty()){

            if(etEmail.getText() != null && !etEmail.getText().isEmpty()){

                if(etPassword.getText() != null && !etPassword.getText().isEmpty()){

                    if(etConfirmPassword.getText() != null && !etConfirmPassword.getText().isEmpty()){

                        if(etConfirmPassword.getText().equals(etPassword.getText())){
                            return true ;
                        } else {
                            alert.setContentText("Senhas não são iguais");
                            alert.show();
                        }
                    } else {
                        alert.setContentText("Só faltou confirma sua senha");
                        alert.show();
                    }
                } else {
                    alert.setContentText("É importante uma senha para accessar o painel");
                    alert.show();
                }
            } else {
                alert.setContentText("O CAMPO EMAIL é necessário para o cadastro");
                alert.show();
            }
        } else {
            alert.setContentText("O CAMPO NOME não pode estar vazio!");
            alert.show();
        }

        return false ;
    }

    public static final Alert dialogInformation(String title, String contentText, String headerText, ImageView graphic, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(contentText);
        alert.setHeaderText(headerText);
        alert.setGraphic(graphic);
        return alert ;
    }

    public static final Alert dialogInformation(String title, String contentText, String headerText, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(contentText);
        alert.setHeaderText(headerText);
        return alert ;
    }

    public static final Alert dialogInformation(String title, String contentText, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(contentText);
        return alert ;
    }

    public void testGetMacAddress() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            NetworkInterface ni = NetworkInterface.getByInetAddress(address);
            byte[] mac = ni.getHardwareAddress();
            String macAddress = "";

            for (int i = 0; i < mac.length; i++) {
                String macTemporary = String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : "");
                macAddress += macTemporary;
                //System.out.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : "");
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
