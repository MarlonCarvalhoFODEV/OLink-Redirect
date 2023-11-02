/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames.splashscreen;

import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import org.json.JSONObject;
import system.OLinkConfiguration;
import frames.login.LoginActivity;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import urls.UrlsConnetionsWEB;
import util.UpdateApp;
import webconnection.DialogNoConnection;
import webconnection.WebJson;

/**
 * @author Marlon
 */
public class SplashController implements Initializable {

    @FXML
    private Label lbInfoUpdate;

    @FXML
    private StackPane stackPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        asUpdate();
    }

    private void asUpdate() {
        lbInfoUpdate.setText("Verificando atualizações disponíveis...");
        JSONObject version = new JSONObject();
        version.put("version", OLinkConfiguration.SystemConfigure.VERSION_APP);

        InternetRequisitionAPI requestServer = OLinkConfiguration.getApiRequisition();
        requestServer.setIdRequisition("request-order");
        requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
        requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.AES);
        requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
        requestServer.setDomain(UrlsConnetionsWEB.DOMAIN_CONNECTION);
        requestServer.setController(UrlsConnetionsWEB.UpdateControll.UPDATE_CONTROLL);
        requestServer.setFunction(UrlsConnetionsWEB.UpdateControll.Method.VERIFY_AS_UPDATE);
        requestServer.setObjectTransport(version);
        requestServer.request(new InternetRequisitionResponse() {
            @Override
            public void onLoading(boolean loading) {
            }

            @Override
            public void onSucess(Object object) {
                if (object != null) {

                    JSONObject response = new JSONObject(object.toString());
                    response = response.getJSONObject("map");

                    UpdateApp asUpdate = new Gson().fromJson(response.getString("as_update"), UpdateApp.class);

                    if (asUpdate == null) {

                        Stage stage = (Stage) lbInfoUpdate.getScene().getWindow();
                        stage.close();

                        try {
                            LoginActivity login = new LoginActivity();
                            login.show(false);
                        } catch (Exception ex) {
                            Logger.getLogger(SplashController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        showDialogDownloadUpdating("ATUALIZAÇÃO DISPONÍVEL version - " + asUpdate.getVersion());
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }

            @Override
            public void notConnectionInternet() {
                new DialogNoConnection(stackPane).show(true);
            }
        });
    }

    private void waiting(long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException ex) {
            Logger.getLogger(SplashController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private void showDialogDownloadUpdating(String title) {
        stackPane.setVisible(true);
        JFXDialogLayout content = new JFXDialogLayout();

        Label tit = new Label(title);
        tit.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        tit.setAlignment(Pos.CENTER);
        content.setHeading(tit);

        Label question = new Label("Deseja atualizar?");
        question.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        question.setAlignment(Pos.CENTER);

        ProgressBar progress = new ProgressBar();
        progress.setPrefSize(400, 18);

        VBox verticalLayout = new VBox(question, progress);
        content.setBody(verticalLayout);

        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);


        JFXButton btnCancel = new JFXButton("NÃO, Cancelar");
        btnCancel.setOnMouseClicked(event -> {
            dialog.close();
            ((Stage) stackPane.getScene().getWindow()).close();
        });
        JFXButton btnDownload = new JFXButton("SIM, Atualizar");
        btnDownload.setOnMouseClicked(event -> {
            btnCancel.setDisable(true);
            btnDownload.setDisable(true);
            startDownload(question, progress);
        });
        content.setActions(btnDownload, btnCancel);

        dialog.setOnDialogClosed((event) -> {
            stackPane.setVisible(false);
        });
        dialog.show();
    }

    private void startDownload(Label status, ProgressBar progressBar) {
        Thread th = new Thread(OLinkConfiguration.getThreadGroup(), () -> {
            try {
                String file = "https://rendalivre.com/SETUP-OLinkRedirect.exe";

                String home = System.getProperty("user.home");
                //File fi = new File(home+"/Downloads/");
                File fi = new File("C:\\Users\\Public\\Downloads\\");
                if (!fi.exists())
                    fi.mkdirs();

                WebJson.downloadFile(file, fi.getAbsolutePath(), status, progressBar);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        th.start();
    }
}
