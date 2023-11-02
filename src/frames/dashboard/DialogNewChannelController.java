/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames.dashboard;

import Itens.ITPartner;
import Itens.ITSocialChannel;
import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;

import java.net.URL;
import java.util.ResourceBundle;

import frames.account.UserAccount;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.json.JSONObject;
import system.OLinkConfiguration;
import urls.UrlsConnetionsWEB;
import util.Toast;
import webconnection.DialogNoConnection;

import static adapters.AdapterSocialChannel.*;
import static system.OLinkConfiguration.getAccount;
import static system.OLinkConfiguration.getThreadGroup;
import static util.MaskedTextField.addTextLimiter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

import javafx.collections.FXCollections;

/**
 * @author Marlon
 */
public class DialogNewChannelController implements Initializable {

    @FXML
    private Label lbTitle;

    @FXML
    private TextField etName;
    @FXML
    private TextField etIdPixelCode, etIdPixelBackup;

    @FXML
    private JFXButton btnAddChannel;
    @FXML
    private BorderPane bpProgress;

    private JFXDialog dialog;

    @FXML
    private ComboBox<String> cbSpecification;

    private ITSocialChannel channel;
    private DashboardController dashController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setConfigureSpecifications();
        startModeUpdate();
        addTextLimiter(etName, 20);
    }

    private void startModeUpdate() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (channel != null) {
                        etName.setText(channel.getTitle());
                        etIdPixelCode.setText(channel.getPixel());
                        etIdPixelBackup.setText(channel.getIdPixelBackup());
                        String s = decodeSpecification(channel.getSpecification());
                        int index = cbSpecification.getItems().indexOf(s);
                        cbSpecification.getSelectionModel().select(index);

                        lbTitle.setText("Atualizar Canal");
                        btnAddChannel.setText("ATUALIZAR");
                        btnAddChannel.setOnAction(new UpdateChannelListener(channel));
                    }
                });
                timer.cancel();
            }
        }, 250);
    }

    private void setConfigureSpecifications() {
        ArrayList<String> value = new ArrayList<>();
        value.add("SITE");
        value.add("MEDIA SOCIAL");
        value.add("BLOG");
        value.add("YOUTUBE");
        value.add("FACEBOOK");
        value.add("PAGINA FACEBOOK");
        value.add("GRUPO FACEBOOK");
        value.add("WHATSAPP");
        value.add("TELEGRAM");
        value.add("INSTAGRAM");
        value.add("PINTEREST");
        value.add("TWITTER");
        value.add("COMUNICAÇÃO");
        value.add("SOFTWARE");

        cbSpecification.setItems(FXCollections.observableArrayList(value));
    }

    private ITSocialChannel createChannel() {
        ITSocialChannel c = null;

        if (etName.getText() != null && !etName.getText().isEmpty() && cbSpecification.getSelectionModel().getSelectedIndex() >= 0) {
            c = new ITSocialChannel();
            c.setTitle(etName.getText().toUpperCase());
            c.setSpecification(encodeSpecification(cbSpecification.getSelectionModel().getSelectedItem()));
            c.setIdUser(getAccount().getId());
            c.setPixel(etIdPixelCode.getText());
            c.setIdPixelBackup(etIdPixelBackup.getText());
        }

        return c;
    }

    private void saveOnWeb() {
        showProgress(true);
        ITSocialChannel channel = createChannel();

        UserAccount ac = getAccount();

        JSONObject send = new JSONObject();
        send.put("idu", ac.getId());
        send.put("oip", ac.getSec().getIpPublicFirst());
        send.put("mac", ac.getSec().getMac());
        send.put("sid", ac.getSec().getSystemId());
        send.put("tit", channel.getTitle());
        send.put("px", channel.getPixel());
        send.put("pxb", channel.getIdPixelBackup());
        send.put("spe", channel.getSpecification());

        InternetRequisitionAPI requestServer = OLinkConfiguration.getApiRequisition();
        requestServer.setIdRequisition("saveOnWeb");
        requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
        requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.OTHER_RSA);
        requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
        requestServer.setDomain(UrlsConnetionsWEB.DOMAIN_CONNECTION);
        requestServer.setController(UrlsConnetionsWEB.ChannelManager.CHANNEL);
        requestServer.setFunction(UrlsConnetionsWEB.ChannelManager.Methods.CREATE);
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

                    showProgress(false);
                    Stage stage = (Stage) dialog.getScene().getWindow();

                    int check = response.getInt("check");
                    boolean result = response.getBoolean("result");

                    if (check == 0) {

                        dialog.close();

                        dashController.reloadChannelPublic();
                        Toast.makeText(dashController.getStage(), "Criado com sucesso!", 0, 500, 2000);
                    } else if (check == 1) {
                        Toast.makeText(stage, "Plano Gratuito Até 2 Canais", 0, 500, 3000, true);
                    } else if (check == 2) {
                        Toast.makeText(stage, "Plano Mensal Até 5 Canais", 0, 500, 3000, true);
                    } else if (check == 3) {
                        Toast.makeText(stage, "Plano TRIMESTRAL Até 8 Canais", 0, 500, 3000, true);
                    } else {
                        Toast.makeText(stage, "Erro ao tentar criar canal!", 0, 500, 3000, true);
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }

            @Override
            public void notConnectionInternet() {
                new DialogNoConnection(dashController.getStackPane()).show(true);
            }
        });

 /*       channel = createChannel();

        if(channel != null){
            if (haveConnection()) {
                showProgress(true);

                StringBuilder sb = new StringBuilder();
                sb.append(UrlsConnetionsWEB.DOMAIN_CONNECTION);
                sb.append(UrlsConnetionsWEB.ChannelManager.CHANNEL);
                sb.append(UrlsConnetionsWEB.ChannelManager.Methods.CREATE);

                Thread th = new Thread(getThreadGroup(), () -> {

                    String jsonReturned = new WebJson().postJson(sb.toString(),channel);

                    Platform.runLater(() -> {
                        showProgress(false);
                        Stage stage = (Stage) dialog.getScene().getWindow();
                        if (jsonReturned != null && Integer.valueOf(jsonReturned) == 0) {

                            dialog.close();

                            dashController.reloadChannelPublic();
                            Toast.makeText(dashController.getStage(), "Criado com sucesso!", 0, 500, 2000);
                        } else if (jsonReturned != null && Integer.valueOf(jsonReturned) == 1) {
                            Toast.makeText(stage, "Plano Gratuito Até 2 Canais", 0, 500, 3000, true);
                        } else if (jsonReturned != null && Integer.valueOf(jsonReturned) == 2) {
                            Toast.makeText(stage, "Plano Mensal Até 5 Canais", 0, 500, 3000, true);
                        } else if (jsonReturned != null && Integer.valueOf(jsonReturned) == 3) {
                            Toast.makeText(stage, "Plano TRIMESTRAL Até 8 Canais", 0, 500, 3000, true);
                        } else {
                            Toast.makeText(stage, "Erro ao tentar criar canal!", 0, 500, 3000, true);
                        }
                    });
                });
                th.start();
            } else {
                new DialogNoConnection(dashController.getStackPane()).show(true);
            }
        } else {

        }*/


    }

    @FXML
    public void addChannel() {
        saveOnWeb();
    }

    private final void updateChannel() {
        showProgress(true);
        ITSocialChannel channel = createChannel();

        UserAccount ac = getAccount();

        JSONObject send = new JSONObject();
        send.put("idu", ac.getId());
        send.put("oip", ac.getSec().getIpPublicFirst());
        send.put("mac", ac.getSec().getMac());
        send.put("sid", ac.getSec().getSystemId());
        send.put("idc", this.channel.getId());
        send.put("tit", channel.getTitle());
        send.put("px", channel.getPixel());
        send.put("pxb", channel.getIdPixelBackup());
        send.put("spe",decodeSpecificationCode(cbSpecification.getItems().get(cbSpecification.getSelectionModel().getSelectedIndex()))) ;

        InternetRequisitionAPI requestServer = OLinkConfiguration.getApiRequisition();
        requestServer.setIdRequisition("updateChannel");
        requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
        requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.OTHER_RSA);
        requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
        requestServer.setDomain(UrlsConnetionsWEB.DOMAIN_CONNECTION);
        requestServer.setController(UrlsConnetionsWEB.ChannelManager.CHANNEL);
        requestServer.setFunction(UrlsConnetionsWEB.ChannelManager.Methods.UPDATE);
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

                    showProgress(false);
                    Stage stage = (Stage) dialog.getScene().getWindow();

                    int check = response.getInt("check");
                    boolean result = response.getBoolean("result");

                   if(result){
                       dialog.close();
                       dashController.reloadChannelPublic();
                       Toast.makeText(dashController.getStage(), "Atualizado com sucesso!", 0, 500, 2000);
                   }
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }

            @Override
            public void notConnectionInternet() {
                new DialogNoConnection(dashController.getStackPane()).show(true);
            }
        });
    }

    private void showProgress(boolean show) {
        bpProgress.setVisible(show);
        btnAddChannel.setDisable(show);
    }

    public void setDialog(JFXDialog dialog) {
        this.dialog = dialog;
    }

    public static int encodeSpecification(String specification) {
        switch (specification) {
            case "SITE":
                return SITE;
            case "MEDIA SOCIAL":
                return SOCIAL_MEDIA;
            case "BLOG":
                return BLOG;
            case "YOUTUBE":
                return YOUTUBE;
            case "FACEBOOK":
                return FACEBOOK;
            case "PAGINA FACEBOOK":
                return FACEBOOK_PAGE;
            case "GRUPO FACEBOOK":
                return FACEBOOK_GROUP;
            case "WHATSAPP":
                return WHATSAPP;
            case "TELEGRAM":
                return TELEGRAM;
            case "INSTAGRAM":
                return INSTAGRAM;
            case "PINTEREST":
                return PINTEREST;
            case "TWITTER":
                return TWITTER;
            case "COMUNICAÇÃO":
                return COMUNICATION;
            case "SOFTWARE":
                return SOFTWARE;
            default:
                return 0;
        }
    }

    public void setDashboardController(DashboardController dashboardController) {
        dashController = dashboardController;
    }

    public void setChannel(ITSocialChannel item) {
        channel = item;
    }

    public class UpdateChannelListener implements EventHandler<ActionEvent> {

        private final ITSocialChannel channel;

        public UpdateChannelListener(ITSocialChannel channel) {
            this.channel = channel;
        }

        @Override
        public void handle(ActionEvent event) {
            updateChannel();
        }
    }
}
