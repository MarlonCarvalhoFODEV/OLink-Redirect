package frames.dashboard;

import Itens.*;
import adapters.AdapterSocialChannel;
import adapters.AdapterSocialChannelContent;
import adapters.AdapterSocialChannelv2;
import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import frames.account.UserAccount;
import frames.checkout.CheckoutController;
import frames.configuration.ConfigurationActivity;
import frames.dialogs.tutorial.DialogTutorial;
import frames.graphic.GraphicActivity;
import frames.login.LoginActivity;
import frames.newlink.NewLinkActivity;
import frames.splashscreen.SplashController;
import frames.tutorials.TutorialActivity;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.MapChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import frames.newcontent.NewContentActivity;
import frames.plans.PlansActivity;
import org.json.JSONObject;
import system.OLinkConfiguration;
import urls.UrlsConnetionsWEB;
import util.DecodeChannelID;
import util.Toast;
import util.UpdateApp;
import webconnection.DialogNoConnection;
import webconnection.WebJson;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import util.LKImageManager;

import static system.OLinkConfiguration.*;
import static webconnection.WebJson.haveConnection;


public class DashboardController implements Initializable {

    public static final int YOUTUBE = 48242540;
    public static final int FACEBOOK = 654655;
    public static final int WHATSAPP = 54334;
    public static final int TELEGRAM = 876866;
    public static final int INSTAGRAM = 7454899;
    public static final int PINTEREST = 4123755;
    public static final int TWITTER = 886544;
    public static final int SITE = 56792133;
    public static final int BLOG = 15247953;

    private int tempSize = 0;

    @FXML
    private ListView<ITSocialChannel> lvSocialChannel;

    @FXML
    private TreeView mTreeView;

    @FXML
    private JFXButton btnUpgrade;

    @FXML
    private Button btnEditGroup, btnDelGroup, btnAddGroup;

    @FXML
    private BorderPane bpLoading;

    @FXML
    private WebView wvRight, wvGhost;

    @FXML
    private ComboBox<String> cbCategoryDash;

    @FXML
    private TextField etSearch;

    @FXML
    private StackPane stackPane;

    private NewContentActivity activity;
    @FXML
    private ImageView ivLogo;
    @FXML
    private ImageView ivGraphic;

    @FXML
    private BorderPane bpPanelDouble;
    @FXML
    private AnchorPane apMenu;
    @FXML
    private HBox mSearch;
    @FXML
    private JFXButton addNewChannel;
    @FXML
    private BorderPane bpLoadingChannel;
    @FXML
    private AnchorPane apChannel;
    @FXML
    private ImageView ivReloadChannels, ivReloadContents;
    @FXML
    private Label lbRegisters, lbSubscribers, lbConversions, lbCancels, lbRevenue, lbRevenueMonthly, lbClicks;
    @FXML
    private BorderPane panelGeneral, panelPartner;

    private boolean started = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showLoading(true);
        checkIsPartner();

        unlockFunctions(haveAccess());

        stackPane.setVisible(false);

        ImageView imgAddGroup = new LKImageManager().getImageResource("/icon_plus.png");
        imgAddGroup.setFitWidth(20);
        imgAddGroup.setFitHeight(20);
        btnAddGroup.setGraphic(imgAddGroup);
        btnAddGroup.setStyle("-fx-background-color: transparent;");

        ImageView imgDelGroup = new LKImageManager().getImageResource("/icon_any.png");
        imgDelGroup.setFitWidth(20);
        imgDelGroup.setFitHeight(20);
        btnDelGroup.setGraphic(imgDelGroup);
        btnDelGroup.setStyle("-fx-background-color: transparent;");
        btnDelGroup.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                showDialogConfirm();
            }
        });

        ImageView imgEditGroup = new LKImageManager().getImageResource("/icon_edit.png");
        imgEditGroup.setFitWidth(20);
        imgEditGroup.setFitHeight(20);
        btnEditGroup.setGraphic(imgEditGroup);
        btnEditGroup.setStyle("-fx-background-color: transparent;");

        lvSocialChannel.setCellFactory(new Callback<ListView<ITSocialChannel>, ListCell<ITSocialChannel>>() {
            @Override
            public ListCell<ITSocialChannel> call(ListView<ITSocialChannel> param) {
                try {
                    return new AdapterSocialChannelv2(stackPane);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        lvSocialChannel.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ITSocialChannel>() {

            private boolean started = false;

            @Override
            public void changed(ObservableValue<? extends ITSocialChannel> observable, ITSocialChannel oldValue, ITSocialChannel newValue) {
                mTreeView.getRoot().getChildren().clear();
                setSelectedChannel(lvSocialChannel.getSelectionModel().getSelectedItem());
                getSavedsCategories(getSelectedChannel());
                getContentsFromChannel("", "");
            }
        });
        cbCategoryDash.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                if (ov.getValue() != null && t1 != null && t != null && !t.equals(t1)) {
                    mTreeView.getRoot().getChildren().clear();
                    getContentsFromChannel(etSearch.getText(), cbCategoryDash.getSelectionModel().getSelectedItem());
                }
            }
        });

        mTreeView.setCellFactory(new Callback<TreeView, TreeCell>() {
            @Override
            public TreeCell call(TreeView param) {
                return new AdapterSocialChannelContent();
            }
        });
        mTreeView.setRoot(new TreeItem());
        mTreeView.setShowRoot(false);

        //wvRight.getEngine().setJavaScriptEnabled(false);
        wvRight.getEngine().setUserAgent(CheckoutController.UserAgent.USER_AGENT);

        wvGhost.setDisable(false);
        //wvGhost.getEngine().setJavaScriptEnabled(true);
        wvGhost.getEngine().setUserAgent(CheckoutController.UserAgent.USER_AGENT);
        wvGhost.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                wvGhost.setVisible(false);
            }
        });

        loadChannels();

        /*TutorialActivity tutorial = null;
        try {
            tutorial = new TutorialActivity();

            tutorial.show(false);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    private void loadTutorial() {
        if (!started) {
            started = true;

            DialogTutorial dialog = new DialogTutorial(stackPane);
            dialog.show();
        }
    }

    private synchronized void loadChannels() {
        if (lvSocialChannel.getItems() != null && lvSocialChannel.getItems().size() > 0)
            lvSocialChannel.getItems().clear();

        UserAccount ac = getAccount();

        JSONObject send = new JSONObject();
        send.put("idu", ac.getId());
        send.put("oip", ac.getSec().getIpPublicFirst());
        send.put("mac", ac.getSec().getMac());
        send.put("sid", ac.getSec().getSystemId());

        InternetRequisitionAPI requestServer = OLinkConfiguration.getApiRequisition();
        requestServer.setIdRequisition("loadChannels");
        requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
        requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
        requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
        requestServer.setDomain(UrlsConnetionsWEB.DOMAIN_CONNECTION);
        requestServer.setController(UrlsConnetionsWEB.ChannelManager.CHANNEL);
        requestServer.setFunction(UrlsConnetionsWEB.ChannelManager.Methods.GET);
        requestServer.setObjectTransport(send);
        requestServer.request(new InternetRequisitionResponse() {
            @Override
            public void onLoading(boolean loading) {
                showLoading(loading);
            }

            @Override
            public void onSucess(Object object) {
                if (object != null) {

                    Gson gson = new Gson();
                    JsonArray array = gson.fromJson(object.toString(), JsonArray.class);

                    ArrayList<ITSocialChannel> channels = new ArrayList<>();
                    if (array != null) {

                        ITSocialChannel channel = null;
                        for (JsonElement e : array) {
                            ITSocialChannel c = gson.fromJson(e, ITSocialChannel.class);

                            if (channel == null)
                                channel = c;
                            channels.add(c);
                        }

                        if (channel != null) {
                            getSavedsCategories(channel);
                            setSelectedChannel(channel);
                        }

                        loadTutorial();
                        if (getSelectedChannel() != null) {
                            //getContentsFromChannel("", "");
                            lvSocialChannel.getItems().addAll(0, channels);
                            lvSocialChannel.getSelectionModel().selectFirst();
                        } else {
                            showLoading(false);
                        }
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                showLoading(false);
                e.printStackTrace();
            }

            @Override
            public void notConnectionInternet() {
                showLoading(false);
                new DialogNoConnection(stackPane).show(true);
            }
        });
    }

    @FXML
    public void onEnter(KeyEvent ae) {
        if (ae != null && ae.getCode() == KeyCode.ENTER) {
            mTreeView.getRoot().getChildren().clear();
            getContentsFromChannel(etSearch.getText(), cbCategoryDash.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    public void clickedMouse() {
        ArrayList<ITSocialChannelContent> cont = new ArrayList<>();

        ObservableList<TreeItem> contents = mTreeView.getRoot().getChildren();
        contents.forEach(treeitem -> {
            if (treeitem.getValue().getClass().getSimpleName().equals(ITSocialChannelContent.class.getSimpleName())) {

                cont.add((ITSocialChannelContent) treeitem.getValue());
            }
        });

        Stage stage = (Stage) ivLogo.getScene().getWindow();
        GraphicActivity ac = new GraphicActivity(stage);
        ac.getController().setContents(cont);
        ac.show(false);
    }

    @FXML
    public void exitedMouse() {
        ivGraphic.setImage(new LKImageManager().getImageResource("/icon_graphic_pizza.png").getImage());
    }

    @FXML
    public void enteredMouse() {
        ivGraphic.setImage(new LKImageManager().getImageResource("/icon_graphic_pizza_over.png").getImage());
    }

    private synchronized void getSavedsCategories(ITSocialChannel channel) {

        UserAccount ac = getAccount();

        JSONObject send = new JSONObject();
        send.put("idu", ac.getId());
        send.put("oip", ac.getSec().getIpPublicFirst());
        send.put("mac", ac.getSec().getMac());
        send.put("sid", ac.getSec().getSystemId());
        send.put("idc", channel.getId());
        send.put("ilt", channel.getId());

        InternetRequisitionAPI requestServer = OLinkConfiguration.getApiRequisition();
        requestServer.setIdRequisition("loadCategory");
        requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
        requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
        requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
        requestServer.setDomain(UrlsConnetionsWEB.DOMAIN_CONNECTION);
        requestServer.setController(UrlsConnetionsWEB.ContentManager.CONTENTS);
        requestServer.setFunction(UrlsConnetionsWEB.ContentManager.Method.GET_SAVEDS_CATEGORIES);
        requestServer.setObjectTransport(send);
        requestServer.request(new InternetRequisitionResponse() {
            @Override
            public void onLoading(boolean loading) {
            }

            @Override
            public void onSucess(Object object) {
                if (object != null) {

                    Gson gson = new Gson();
                    JsonArray array = gson.fromJson(object.toString(), JsonArray.class);

                    List<String> cats = new ArrayList<>();
                    cats.add("Todas as Categorias");
                    for (JsonElement e : array) {
                        String s = e.getAsString();

                        if (s != null && !s.isEmpty()) {
                            cats.add(e.getAsString());
                        }
                    }

                    cbCategoryDash.setItems(FXCollections.observableArrayList(cats));
                    cbCategoryDash.getSelectionModel().select(0);
                }
            }

            @Override
            public void onError(Exception e) {
                showLoading(false);
                e.printStackTrace();
            }

            @Override
            public void notConnectionInternet() {
                showLoading(false);
                new DialogNoConnection(stackPane).show(true);
            }
        });

    /*    if (haveConnection()) {
            getThreadGroup().interrupt();
            Thread th = new Thread(getThreadGroup(), new Runnable() {
                @Override
                public void run() {
                    StringBuilder url = new StringBuilder();
                    url.append(UrlsConnetionsWEB.DOMAIN_CONNECTION);
                    url.append(UrlsConnetionsWEB.ContentManager.CONTENTS);
                    url.append(UrlsConnetionsWEB.ContentManager.Method.GET_ALL_CATEGORIES);
                    url.append("?iduser=");
                    url.append(getAccount().getId());
                    url.append("&idchannel=");
                    url.append(channel.getId());

                    String jsonResult = new WebJson().getJson(url.toString());
                    JsonArray array = new Gson().fromJson(jsonResult, JsonArray.class);

                    List<String> cats = new ArrayList<>();
                    cats.add("Todas as Categorias");
                    for (JsonElement e : array) {
                        String s = e.getAsString();

                        if (s != null && !s.isEmpty()) {
                            cats.add(e.getAsString());
                        }
                    }

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            cbCategoryDash.setItems(FXCollections.observableArrayList(cats));
                            cbCategoryDash.getSelectionModel().select(0);
                        }
                    });
                }
            });
            th.start();
        } else {
            new DialogNoConnection(stackPane).show(true);
        }*/
    }

    private synchronized void getContentsFromChannel(String searchin, String categorie) {

        mTreeView.getRoot().getChildren().clear();

        UserAccount ac = getAccount();

        JSONObject send = new JSONObject();
        send.put("idu", ac.getId());
        send.put("oip", ac.getSec().getIpPublicFirst());
        send.put("mac", ac.getSec().getMac());
        send.put("sid", ac.getSec().getSystemId());
        send.put("idc", getSelectedChannel().getId());
        send.put("src", searchin);
        send.put("cat", (categorie == null || categorie.equals("Todas as Categorias")) ? "" : categorie);

        InternetRequisitionAPI requestServer = OLinkConfiguration.getApiRequisition();
        requestServer.setIdRequisition("getContentsFromChannel");
        requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
        requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.OTHER_RSA);
        requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
        requestServer.setDomain(UrlsConnetionsWEB.DOMAIN_CONNECTION);
        requestServer.setController(UrlsConnetionsWEB.ContentManager.CONTENTS);
        requestServer.setFunction(UrlsConnetionsWEB.ContentManager.Method.GET);
        requestServer.setObjectTransport(send);
        requestServer.request(new InternetRequisitionResponse() {
            @Override
            public void onLoading(boolean loading) {
                showLoading(loading);
            }

            @Override
            public void onSucess(Object object) {
                if (object != null) {

                    Gson gson = new Gson();
                    JsonArray array = gson.fromJson(object.toString(), JsonArray.class);

                    ITSocialChannelContent item;
                    for (JsonElement jsonE : array) {

                        item = gson.fromJson(jsonE, ITSocialChannelContent.class);
                        item.setSpecification(lvSocialChannel.getSelectionModel().getSelectedItem().getSpecification());
                        item.setPathImage(DecodeChannelID.decodeIconFromIDChannel(item.getIdChannel()));

                        addContentFromChannel(item);
                    }

                }
            }

            @Override
            public void onError(Exception e) {
                showLoading(false);
                e.printStackTrace();
            }

            @Override
            public void notConnectionInternet() {
                showLoading(false);
                new DialogNoConnection(stackPane).show(true);
            }
        });

       /* if (haveConnection()) {

            mTreeView.getRoot().getChildren().clear();

            Thread th = new Thread(getThreadGroup(), new Runnable() {
                @Override
                public void run() {

                    StringBuilder sb = new StringBuilder();
                    sb.append(UrlsConnetionsWEB.DOMAIN_CONNECTION);
                    sb.append(UrlsConnetionsWEB.ContentManager.CONTENTS);
                    sb.append(UrlsConnetionsWEB.ContentManager.Method.GET);

                    ITSearch search = new ITSearch();
                    search.setIdUser(getAccount().getId());
                    search.setSearch(searchin);
                    search.setCategory((categorie == null || categorie.equals("Todas as Categorias")) ? "" : categorie);

                    search.setIdChannel(getSelectedChannel().getId());

                    String json = new WebJson().jsonRequest("POST", sb.toString(), search);

                    Gson gson = new Gson();
                    JsonArray array = gson.fromJson(json, JsonArray.class);

                    ITSocialChannelContent item;
                    for (JsonElement jsonE : array) {

                        item = gson.fromJson(jsonE, ITSocialChannelContent.class);
                        item.setSpecification(lvSocialChannel.getSelectionModel().getSelectedItem().getSpecification());
                        item.setPathImage(DecodeChannelID.decodeIconFromIDChannel(item.getIdChannel()));


                        addContentFromChannel(item);
                    }

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            showLoading(false);
                        }
                    });
                }
            });
            th.start();
        } else {
            showLoading(false);
            new DialogNoConnection(stackPane).show(true);
        }*/
    }

    public synchronized void addContentFromChannel(ITSocialChannelContent item) {
        if (item != null) {
            item.setIdChannel(getSelectedChannel().getId());
            item.setPathImage(DecodeChannelID.decodeIconFromIDChannel(getSelectedChannel().getId()));
            TreeItem<Object> group = new TreeItem<>(item);

            for (ITLink link : item.getLinks()) {
                group.getChildren().add(new TreeItem<>(link));
            }

            //mTreeView.getRoot().getChildren().clear();
            mTreeView.getRoot().getChildren().add(group);
        }
    }

    @FXML
    public void addNewContent() throws Exception {
        ITSocialChannel item = getSelectedChannel();
        item.setPathImage(DecodeChannelID.decodeIconFromIDChannel(getSelectedChannel().getId()));

        if (activity == null && item != null) {

            setSelectedChannel(item);

            Stage stage = (Stage) lvSocialChannel.getScene().getWindow();
            activity = new NewContentActivity(stage);
            activity.getController().setSocialChannel(item);
            activity.show();

            ITSocialChannelContent itemContent = activity.getController().getContent();
            item.setSpecification(item.getSpecification());

            if (item != null) {

                //TreeItem c = (TreeItem) mTreeView.getSelectionModel().getSelectedItem();
                //c.getChildren().clear();
                addContentFromChannel(itemContent);
            }

            mTreeView.refresh();
        } else {
            Alert dialog = new Alert(Alert.AlertType.INFORMATION);
            dialog.setTitle("Menssagem Informativa");
            dialog.setHeaderText("Selecione Um Canal Antes de Adicionar Um Conteúdo");
            dialog.show();
        }
        activity = null;
    }

    private void removeItem(TreeItem c, JFXDialog dialog) {

        showLoading(true);
        UserAccount ac = getAccount();

        JSONObject send = new JSONObject();
        send.put("idu", ac.getId());
        send.put("oip", ac.getSec().getIpPublicFirst());
        send.put("mac", ac.getSec().getMac());
        send.put("sid", ac.getSec().getSystemId());

        if (c != null) {
            Object object = c.getValue();

            if (object.getClass().getSimpleName().equals(ITSocialChannelContent.class.getSimpleName())) {

                ITSocialChannelContent item = (ITSocialChannelContent) c.getValue();

                send.put("idc", item.getIdChannel());
                send.put("idt", item.getId());

                InternetRequisitionAPI requestServer = OLinkConfiguration.getApiRequisition();
                requestServer.setIdRequisition("getContentsFromChannel");
                requestServer.setTypeRequest(InternetRequisition.TypeRequest.DELETE);
                requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.OTHER_RSA);
                requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
                requestServer.setDomain(UrlsConnetionsWEB.DOMAIN_CONNECTION);
                requestServer.setController(UrlsConnetionsWEB.ContentManager.CONTENTS);
                requestServer.setFunction(UrlsConnetionsWEB.ContentManager.Method.DELETE_CONTENT);
                requestServer.setObjectTransport(send);
                requestServer.request(new InternetRequisitionResponse() {
                    @Override
                    public void onLoading(boolean loading) {
                        showLoading(loading);
                    }

                    @Override
                    public void onSucess(Object object) {
                        showLoading(false);
                        if (object != null) {

                            JSONObject response = new JSONObject(object.toString());
                            response = response.getJSONObject("map");

                            boolean resultResponse = response.getBoolean("result");
                            if (resultResponse) {
                                c.getParent().getChildren().remove(c);

                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Erro ao deletar");
                                alert.setHeaderText("Remoção NÃO realizada");
                                alert.show();
                            }

                            dialog.close();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        showLoading(false);
                        e.printStackTrace();
                    }

                    @Override
                    public void notConnectionInternet() {
                        showLoading(false);
                        new DialogNoConnection(stackPane).show(true);
                    }
                });
            } else {
                ITLink item = (ITLink) c.getValue();

                ITSocialChannelContent content = (ITSocialChannelContent) c.getParent().getValue();

                send.put("idc", item.getIdChannel());
                send.put("idt", content.getId());
                send.put("idl", item.getId());

                content.getLinks().remove(item);

                InternetRequisitionAPI requestServer = OLinkConfiguration.getApiRequisition();
                requestServer.setIdRequisition("deleteLink");
                requestServer.setTypeRequest(InternetRequisition.TypeRequest.DELETE);
                requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.OTHER_RSA);
                requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
                requestServer.setDomain(UrlsConnetionsWEB.DOMAIN_CONNECTION);
                requestServer.setController(UrlsConnetionsWEB.ContentManager.Link.LINKS);
                requestServer.setFunction(UrlsConnetionsWEB.ContentManager.Link.Method.DELETE_LINK);
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

                            boolean resultResponse = response.getBoolean("result");

                            if (resultResponse) {
                                c.getParent().getChildren().remove(c);
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Erro ao deletar");
                                alert.setHeaderText("Remoção NÃO realizada");
                                alert.show();
                            }
                            dialog.close();
                            showLoading(false);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        showLoading(false);
                        e.printStackTrace();
                    }

                    @Override
                    public void notConnectionInternet() {
                        showLoading(false);
                        new DialogNoConnection(stackPane).show(true);
                    }
                });
            }
        }



/*
        if (haveConnection()) {
            showLoading(true);
            Thread th = new Thread(getThreadGroup(), () -> {
                String response;

                if (c != null) {
                    Object object = c.getValue();

                    if (object.getClass().getSimpleName().equals(ITSocialChannelContent.class.getSimpleName())) {

                        ITSocialChannelContent item = (ITSocialChannelContent) c.getValue();

                        StringBuilder url = new StringBuilder();
                        url.append(UrlsConnetionsWEB.DOMAIN_CONNECTION);
                        url.append(UrlsConnetionsWEB.ContentManager.CONTENTS);
                        url.append(UrlsConnetionsWEB.ContentManager.Method.DELETE_CONTENT);
                        url.append(getAccount().getId());
                        url.append("/");
                        url.append(item.getId());
                        url.append("/");
                        url.append(item.getIdChannel());

                        response = new WebJson().jsonRequest("DELETE", url.toString());

                        boolean resultResponse = Boolean.valueOf(response);
                        if (resultResponse) {
                            Platform.runLater(() -> {
                                c.getParent().getChildren().remove(c);

                            });
                        } else {
                            Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Erro ao deletar");
                                alert.setHeaderText("Remoção NÃO realizada");
                                alert.show();
                            });
                        }

                        dialog.close();
                        showLoading(false);
                    } else {
                        ITLink item = (ITLink) c.getValue();

                        ITSocialChannelContent content = (ITSocialChannelContent) c.getParent().getValue();

                        content.getLinks().remove(item);

                        StringBuilder url = new StringBuilder();
                        url.append(UrlsConnetionsWEB.DOMAIN_CONNECTION);
                        url.append(UrlsConnetionsWEB.ContentManager.Link.LINKS);
                        url.append(UrlsConnetionsWEB.ContentManager.Link.Method.DELETE_LINK);

                        response = new WebJson().jsonRequest("DELETE", url.toString(), item);
                        boolean resultResponse = Boolean.valueOf(response);

                        Platform.runLater(() -> {
                            if (resultResponse) {
                                c.getParent().getChildren().remove(c);
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Erro ao deletar");
                                alert.setHeaderText("Remoção NÃO realizada");
                                alert.show();
                            }
                            dialog.close();
                            showLoading(false);
                        });
                    }
                }
            });
            th.start();
        } else {
            new DialogNoConnection(stackPane).show(true);
        }*/

    }

    public void showDialogConfirm() {
        TreeItem c = (TreeItem) mTreeView.getSelectionModel().getSelectedItem();

        if (c != null) {

            Text tx = new Text("Deseja excluir este item ?");
            tx.setFont(Font.font("Arial", FontWeight.BOLD, 16));

            JFXDialogLayout layout = new JFXDialogLayout();
            layout.setHeading(new Text("Informação"));
            layout.setBody(tx);

            stackPane.setVisible(true);

            JFXDialog dialog = new JFXDialog(stackPane, layout, JFXDialog.DialogTransition.CENTER);
            dialog.setOnDialogClosed(new EventHandler() {
                @Override
                public void handle(Event event) {
                    stackPane.setVisible(false);
                }
            });

            JFXButton btnYes = new JFXButton("Sim");
            btnYes.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
            btnYes.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    removeItem(c, dialog);

                }
            });

            JFXButton btnDont = new JFXButton("Não");
            btnDont.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
            btnDont.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    dialog.close();
                }
            });

            layout.setActions(btnYes, btnDont);
            dialog.show();
        }
    }

    @FXML
    public void editContent() {
        TreeItem c = (TreeItem) mTreeView.getSelectionModel().getSelectedItem();

        if (c != null) {
            Object object = c.getValue();
            if (object.getClass().getSimpleName().equals(ITSocialChannelContent.class.getSimpleName())) {
                try {
                    ITSocialChannelContent content = (ITSocialChannelContent) c.getValue();

                    Stage stage = (Stage) lvSocialChannel.getScene().getWindow();

                    NewContentActivity contentEdit = new NewContentActivity(stage, content);
                    contentEdit.getController().setContent(content);
                    contentEdit.getController().setSocialChannel(lvSocialChannel.getSelectionModel().getSelectedItem());

                    contentEdit.show();

                    ITSocialChannelContent itemContent = contentEdit.getController().getContent();
                    itemContent.setSpecification(itemContent.getSpecification());

                    if (itemContent != null) {

                        c.getChildren().clear();

                        for (ITLink link : itemContent.getLinks())
                            c.getChildren().add(new TreeItem<>(link));

                        Platform.runLater(() -> {
                            mTreeView.refresh();
                        });

                    }

                    mTreeView.refresh();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    ITSocialChannelContent content = (ITSocialChannelContent) c.getParent().getValue();
                    ITLink link = (ITLink) c.getValue();

                    Stage stage = (Stage) lvSocialChannel.getScene().getWindow();
                    NewLinkActivity linkEdit = new NewLinkActivity(stage, link);
                    linkEdit.getController().setSocialChannel(lvSocialChannel.getSelectionModel().getSelectedItem());
                    linkEdit.getController().setSocialContenr(content);
                    linkEdit.show();

                    mTreeView.refresh();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro de Seleção");
            alert.setHeaderText("Necessário selecionar canal, após isso, selecione um conteúdo para edição");
            alert.show();
        }
    }

    @FXML
    public void upgradePro() {
        Stage stage = (Stage) bpPanelDouble.getScene().getWindow();

        PlansActivity activity = new PlansActivity(stage);
        activity.show(false);
    }

    private void showLoading(boolean show) {
        Platform.runLater(() -> {
            bpLoading.setVisible(show);
        });
    }


    private void unlockFunctions(boolean access) {
        if (access) {
            bpPanelDouble.getChildren().remove(bpPanelDouble.getRight());
            btnUpgrade.setVisible(!access);
            btnUpgrade.setDisable(!access);
            //addNewChannel.setDisable(!access);
        } else {
            apMenu.getChildren().remove(ivGraphic);
            btnUpgrade.setVisible(!access);
            btnUpgrade.setDisable(access);

            AdsenseLoad adsenseLoad = new AdsenseLoad(wvRight, wvGhost);
            adsenseLoad.setPageURL("https://rendalivre.com/demo");
            adsenseLoad.setIntervalRepeatInSeconds((int) (60.0 * 2.5));
            adsenseLoad.start();
            //addNewChannel.setDisable(!access);
            //addNewChannel.setVisible(!access);
            //apChannel.getChildren().remove(addNewChannel);
            //ivReloadChannels.setDisable(!access);
            //apChannel.getChildren().remove(ivReloadChannels);
        }
    }

    public Stage getStage() {
        return (Stage) lvSocialChannel.getScene().getWindow();
    }

    @FXML
    public void reloadChannelPublic() {
        loadChannels();
    }

    @FXML
    public void reloadContents() {
        getContentsFromChannel("", "");
    }

    @FXML
    public void copyPartnerLink() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(getAccount().getPartnerLink());
        clipboard.setContent(content);

        Stage stage = (Stage) btnAddGroup.getScene().getWindow();

        Toast.makeText(stage, "Link de Parceria Copiado!!", 0, 500, 2000, false);
    }

    public void checkIsPartner(boolean preDefined) {
        if (!preDefined)
            getAccount().setPartner(preDefined);
        if (getAccount().isPartner()) {

            ivLogo.setImage(new LKImageManager().getImageResource("partner.png").getImage());
            panelGeneral.setBottom(panelPartner);

            if (haveConnection()) {
                Thread th = new Thread(getThreadGroup(), () -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append(UrlsConnetionsWEB.DOMAIN_CONNECTION);
                    sb.append(UrlsConnetionsWEB.PartnerManager.PARTNER);
                    sb.append(UrlsConnetionsWEB.PartnerManager.Methods.GET_PANEL_INF);
                    sb.append(getAccount().getId());

                    String json = new WebJson().getJson(sb.toString());

                    ITPartner partner = new Gson().fromJson(json, ITPartner.class);

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (partner != null) {
                                lbRegisters.setText("Registros (" + partner.getRegisters() + ")");
                                lbSubscribers.setText("Assinantes (" + partner.getSubscribers() + ")");
                                lbConversions.setText("Conversão ( " + partner.getConversions() + "% )");
                                lbCancels.setText("Cancelados (" + partner.getCancels() + ")");
                                lbRevenue.setText("Receita Gerada R$ " + partner.getRevenue() + "");
                                lbRevenueMonthly.setText("Receita/mês R$ " + partner.getRevenueMonthly() + "");
                            }
                        }
                    });
                });
                th.start();
            } else {
                new DialogNoConnection(stackPane).show(true);
            }
        } else {
            panelGeneral.getChildren().remove(panelPartner);
        }
    }

    public void checkIsPartner() {
        UserAccount ac = getAccount();

        if (ac.isPartner()) {

            ivLogo.setImage(new LKImageManager().getImageResource("partner.png").getImage());
            panelGeneral.setBottom(panelPartner);

            JSONObject send = new JSONObject();
            send.put("idu", ac.getId());
            send.put("oip", ac.getSec().getIpPublicFirst());
            send.put("mac", ac.getSec().getMac());
            send.put("sid", ac.getSec().getSystemId());

            InternetRequisitionAPI requestServer = OLinkConfiguration.getApiRequisition();
            requestServer.setIdRequisition("checkIsPartner");
            requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
            requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
            requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
            requestServer.setDomain(UrlsConnetionsWEB.DOMAIN_CONNECTION);
            requestServer.setController(UrlsConnetionsWEB.PartnerManager.PARTNER);
            requestServer.setFunction(UrlsConnetionsWEB.PartnerManager.Methods.GET_PANEL_INF);
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

                        ITPartner partner = new Gson().fromJson(response.getString("result"), ITPartner.class);

                        if (partner != null) {
                            lbClicks.setText("Cliques (" + partner.getClicks() + ")");
                            lbRegisters.setText("Registros (" + partner.getRegisters() + ")");
                            lbSubscribers.setText("Assinantes (" + partner.getSubscribers() + ")");
                            lbConversions.setText("Conversão ( " + partner.getConversions() + "% )");
                            lbCancels.setText("Cancelados (" + partner.getCancels() + ")");
                            lbRevenue.setText("Receita Gerada R$ " + partner.getRevenue() + "");
                            lbRevenueMonthly.setText("Receita/mês R$ " + partner.getRevenueMonthly() + "");
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
        } else {
            panelGeneral.getChildren().remove(panelPartner);
        }
    }

    @FXML
    private void addNewChannel() {
        DialogNewChannel dialog = new DialogNewChannel(stackPane);
        dialog.show();
    }

    @FXML
    private void reloadChannels(MouseEvent event) {
        loadChannels();
    }

    public StackPane getStackPane() {
        return stackPane;
    }
}
