package frames.login;

import Itens.ITLogin;
import Itens.ITUserIndentify;
import apis.encrypt.SHA256;
import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import frames.splashscreen.SplashController;
import org.json.JSONObject;
import system.OLinkConfiguration;
import frames.account.UserAccount;
import frames.account.register.RegisterAccountActivity;
import frames.account.register.RegisterAccountController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import frames.dashboard.DashboardActivity;
import urls.UrlsConnetionsWEB;
import util.*;
import webconnection.DialogNoConnection;
import webconnection.WebJson;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import static system.OLinkConfiguration.*;

public class LoginController implements Initializable {

    @FXML
    private TextField etEmail;

    @FXML
    private PasswordField etPassword;

    @FXML
    private JFXButton btnLogin, btnRegister;

    @FXML
    private BorderPane bpLoading;

    @FXML
    private ImageView ivLogo, ivBack;

    @FXML
    private StackPane stackPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ivLogo.setImage(new LKImageManager().getImageResource("/icon.png").getImage());
        ivBack.setImage(new LKImageManager().getImageResource("/back_bottom.png").getImage());
        etEmail.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().equals(" ")) {
                change.setText("");
            }
            return change;
        }));
    }

    private void showLoading(boolean show) {
        Platform.runLater(()->{
            bpLoading.setVisible(show);
            btnLogin.setDisable(show);
        });
    }

    public void requestPassword() {
        UtilDialogManager.showRequestPassword(stackPane);
    }

    private void requestLogin() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            ITLogin login = new ITLogin();

            JSONObject send = new JSONObject();
            send.put("email", etEmail.getText());
            send.put("pass", SHA256.encryptBase64(etPassword.getText()));
            send.put("sid", login.getSec().getSystemId());
            send.put("mac", login.getSec().getMac());

            InternetRequisitionAPI requestServer = OLinkConfiguration.getApiRequisition();
            requestServer.setIdRequisition("requestLogin");
            requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
            requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.OTHER_RSA);
            requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
            requestServer.setDomain(UrlsConnetionsWEB.DOMAIN_CONNECTION);
            requestServer.setController(UrlsConnetionsWEB.UserManager.ACCOUNTS);
            requestServer.setFunction(UrlsConnetionsWEB.UserManager.Methods.LOGIN);
            requestServer.setObjectTransport(send);
            requestServer.request(new InternetRequisitionResponse() {
                @Override
                public void onLoading(boolean loading) {
                }

                @Override
                public void onSucess(Object object) {
                    if (object != null) {
                        ExecutorService executor = Executors.newSingleThreadExecutor();
                        executor.submit(() -> {
                            JSONObject response = new JSONObject(object.toString());
                            response = response.getJSONObject("map");

                            UserAccount ac = new Gson().fromJson(response.getString("ac"), UserAccount.class);

                            if (ac != null && ac.isAccess()) {
                                if (ac.getContacts().getEmail() != null &&
                                        !ac.getContacts().getEmail().isEmpty()) {

                                    requestUserPlan(ac);
                                } else {
                                    showDialog("Erro de Login", "Usuário ou senha inválida!", false, true);
                                }
                            } else if (ac == null) {
                                showDialog("Erro de Login", "Usuário ou senha inválida!", false, true);

                            } else {
                                showLoading(false);
                                Stage stage = (Stage) stackPane.getScene().getWindow();
                                UtilDialogManager.showToast(stage, "Caso seja proprietário da conta, Entre em contato com o suporte", 2000, true);
                            }
                        });
                    }
                    executor.shutdown();
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                    executor.shutdown();
                    showLoading(false);
                }

                @Override
                public void notConnectionInternet() {
                    showLoading(false);
                    executor.shutdown();
                    new DialogNoConnection(stackPane).show(true);
                }
            });
        });
    }

    private void requestUserPlan(UserAccount ac) {
            StringBuilder sb = new StringBuilder();
            sb.append(UrlsConnetionsWEB.DOMAIN_CONNECTION);
            sb.append(UrlsConnetionsWEB.UserManager.ACCOUNTS);
            sb.append(UrlsConnetionsWEB.UserManager.Methods.TYPE_PLAN);

            ITLogin login = new ITLogin();

            JSONObject send = new JSONObject();
            send.put("id_user", ac.getId());
            send.put("email", ac.getContacts().getEmail());
            send.put("sid", login.getSec().getSystemId());
            send.put("mac", login.getSec().getMac());

            InternetRequisitionAPI requestServer = OLinkConfiguration.getApiRequisition();
            requestServer.setIdRequisition("request-order");
            requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
            requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
            requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
            requestServer.setDomain(UrlsConnetionsWEB.DOMAIN_CONNECTION);
            requestServer.setController(UrlsConnetionsWEB.UserManager.ACCOUNTS);
            requestServer.setFunction(UrlsConnetionsWEB.UserManager.Methods.TYPE_PLAN);
            requestServer.setObjectTransport(send);
            requestServer.request(new InternetRequisitionResponse() {
                @Override
                public void onLoading(boolean loading) {
                    showLoading(loading);
                }

                @Override
                public void onSucess(Object object) {
                    if (object != null) {
                        JSONObject response = new JSONObject(object.toString());
                        response = response.getJSONObject("map");

                        ITUserIndentify iden = new Gson().fromJson(response.getString("result"), ITUserIndentify.class);

                        setAccount(ac);
                        setPlans(iden.getServiceAccess());

                        Stage stage = (Stage) bpLoading.getScene().getWindow();
                        stage.close();
                        DashboardActivity dashboard = new DashboardActivity();
                        dashboard.show(false);
                    }
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }

                @Override
                public void notConnectionInternet() {
                    showLoading(false);
                    new DialogNoConnection(stackPane).show(true);
                }
            });
    }

    public void login() {
        showLoading(true);
        if (!btnLogin.isDisable()) {
            btnLogin.setDisable(true);

            if (verifyFields()) {

                requestLogin();
            }
        }
    }

    private void showDialog(String title, String contentText, boolean loading, boolean showDialog) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                showLoading(loading);

                if (showDialog) {
                    Alert a = RegisterAccountController.dialogInformation(title, contentText, Alert.AlertType.ERROR);
                    a.show();
                }
            }
        });
    }

    public void newAccount() throws IOException {
        Stage stage = (Stage) bpLoading.getScene().getWindow();
        RegisterAccountActivity reg = new RegisterAccountActivity(stage);
        reg.show(false);
    }

    private boolean verifyFields() {
        if (etEmail.getText() != null && !etEmail.getText().isEmpty() &&
                etPassword.getText() != null && !etPassword.getText().isEmpty() &&
                etEmail.getText().contains("@")) {
            return true;
        } else {
            Alert alert = RegisterAccountController.dialogInformation("Erro de Preenchimento", "Todos os campos devem ser preenchidos de forma correta", Alert.AlertType.ERROR);
            alert.show();
            showLoading(false);
            return false;
        }
    }
}
