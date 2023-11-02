package frames.newlink;

import Itens.*;
import adapters.AdapterLinkReplace;
import adapters.AdapterNewLink;
import apis.encrypt.RSA;
import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import apis.jsoup.APIJsoup;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.jfoenix.controls.*;
import frames.account.UserAccount;
import frames.plans.PlansActivity;

import java.lang.reflect.Field;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONObject;
import system.OLinkConfiguration;
import urls.UrlsConnetionsWEB;
import util.MaskedTextField;
import util.Toast;
import util.UtilDate;
import webconnection.DialogNoConnection;
import webconnection.WebJson;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import static system.OLinkConfiguration.*;
import static util.MaskedTextField.addTextLimiter;
import static webconnection.WebJson.haveConnection;

public class NewLinkController implements Initializable {


    @FXML
    private TextField etTitle, etLinkDestiny, etValueConversion, etNewLink, etNewEvent, etDestinyA, etDestinyB,
            etDestinyBefore, etDestinyDuring, etDestinyAfter;
    @FXML
    private MaskedTextField etTimeDayAB;
    @FXML
    private JFXDatePicker dateStart, dateEnd;
    @FXML
    private JFXTimePicker hourStart, hourEnd;
    @FXML
    private JFXButton btnSave, btnUpgrade, btnAddLinkReplace;
    @FXML
    private ComboBox<String> cbRedirectTypes, cbEvents;
    @FXML
    private JFXRadioButton rbLinkConversion, rbApplyPixel;
    @FXML
    private Label lbPRO, domain;
    @FXML
    private BorderPane bpLoading;
    @FXML
    private StackPane stackPane;
    @FXML
    private TitledPane tpAB;
    @FXML
    private TitledPane tpLaunch;

    @FXML
    private AnchorPane sbPanelLinkReplace ;

    @FXML
    private ListView<ITLinkReplace> lvUrlReplace ;

    private ITLink link;
    private ITSocialChannel socialChannel;
    private ITSocialChannelContent socialChannelContent;

    private static Tooltip toolTip = new Tooltip("Nome não disponível.");
    private static boolean IS_AVAILABLE = false;
    private boolean usUpdate = false;

    private ArrayList<String> urlsCreated = new ArrayList<>();

    public NewLinkController() {
    }

    public NewLinkController(ITLink link) {
        this.link = link;
        this.usUpdate = true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeRedirectTypes();
        initializeEvents();

        startEdit();
        setActionButton();
        unlockAccess(haveAccess());

        addTextLimiter(etTitle, 45);
        addTextLimiter(etLinkDestiny, 100);
        addTextLimiter(etNewLink, 30);
        addTextLimiter(etNewEvent, 20);

        etNewEvent.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().equals(" ") || change.getText().equals("-") || change.getText().equals("\"")
                    || change.getText().equals("!") || change.getText().equals("@") || change.getText().equals("#")
                    || change.getText().equals("$") || change.getText().equals("%") || change.getText().equals("¨¨")
                    || change.getText().equals("&") || change.getText().equals("*") || change.getText().equals("(")
                    || change.getText().equals(")") || change.getText().equals("-")
                    || change.getText().equals("+") || change.getText().equals("=") || change.getText().equals("<")
                    || change.getText().equals(">") || change.getText().equals(":") || change.getText().equals(";")
                    || change.getText().equals(":") || change.getText().equals(";") || change.getText().equals("?")
                    || change.getText().equals("/") || change.getText().equals("*") || change.getText().equals("")) {
                change.setText("");
            }
            return change;
        }));
        etLinkDestiny.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().equals(" ")) {
                change.setText("");
            }
            return change;
        }));

        etNewLink.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().equals(" ") || change.getText().equals(".") || change.getText().equals("\"")
                    || change.getText().equals("!") || change.getText().equals("@") || change.getText().equals("#")
                    || change.getText().equals("$") || change.getText().equals("%") || change.getText().equals("¨")
                    || change.getText().equals("&") || change.getText().equals("*") || change.getText().equals("(")
                    || change.getText().equals(")") || change.getText().equals(",") || change.getText().equals("|")
                    || change.getText().equals("+") || change.getText().equals("=") || change.getText().equals("<")
                    || change.getText().equals(">") || change.getText().equals("\"") || change.getText().equals(";")
                    || change.getText().equals(":") || change.getText().equals(";") || change.getText().equals("?")
                    || change.getText().equals("/") || change.getText().equals("*") || change.getText().equals("")
                    || change.getText().equals("'")) {
                change.setText("");
            }
            /*if (change.getText().equals(" ")) {
                change.setText("");
            }*/
            return change;
        }));
        etNewLink.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {

            } else {
                if (!etNewLink.getText().isEmpty() && link == null) {
                    verifyNewLinkAvailable(etNewLink.getText());
                }
            }
        });
        etValueConversion.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().equals(" ")) {
                change.setText("");
            }
            return change;
        }));
        etValueConversion.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?") && oldValue.length() <= 10) {
                etValueConversion.setText(oldValue);
            }
        });

        etLinkDestiny.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().equals(" ")) {
                change.setText("");
            }
            return change;
        }));
        /*etLinkDestiny.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!etNewLink.focusedProperty().getValue() && !etNewLink.getText().isEmpty() && link == null) {
                verifyNewLinkAvailable(etNewLink.getText());
            }
        });
        etTitle.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!etNewLink.focusedProperty().getValue() && !etNewLink.getText().isEmpty() && link == null) {
                verifyNewLinkAvailable(etNewLink.getText());
            }
        });*/

        rbLinkConversion.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(link.getValue() == 0) {
                etValueConversion.setDisable(!newValue);
            }
            else {
                rbLinkConversion.setSelected(true);
                rbLinkConversion.setDisable(true);
            }
        });
        rbApplyPixel.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (socialChannel.getPixel() != null && !socialChannel.getPixel().isEmpty()) {
                etNewEvent.setDisable(!newValue);
                cbEvents.setDisable(!newValue);
            } else {
                etNewEvent.setDisable(false);
                cbEvents.setDisable(false);
                rbApplyPixel.setDisable(false);

                Stage stage = (Stage) rbApplyPixel.getScene().getWindow();

                Toast.makeText(stage, "O Canal " + socialChannel.getTitle() + " Não possui\n pixel aplicado!!!", 300, 500, 1500, true);
            }
        });

        domain.setText(UrlsConnetionsWEB.DOMAIN_CONNECTION);
        toolTip.setStyle("-fx-background-color: linear-gradient(#FF6B6B , #FFA6A6 );"
                + " -fx-font-weight: bold;");

        dateEnd.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (dateStart.getValue() != null) {
                boolean isSmaller = (dateStart.getValue() != null && newValue.isBefore(dateStart.getValue()));
                if (isSmaller) {
                    dateEnd.setValue(null);

                    Stage stage = (Stage) dateEnd.getScene().getWindow();
                    Toast.makeText(stage, "Data Final deve ser Maior que a inicial", 0, 500, 3000, true);
                }
            }
        });

        dateStart.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (dateEnd.getValue() != null) {
                boolean isBigger = (dateEnd.getValue() != null && newValue.isAfter(dateEnd.getValue()));
                if (isBigger) {
                    dateStart.setValue(null);

                    Stage stage = (Stage) dateStart.getScene().getWindow();
                    Toast.makeText(stage, "Data Inicial deve ser MENOR que a final", 0, 500, 3000, true);
                }
            }
        });
        tpAB.expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    tpLaunch.setDisable(newValue);
                    dateStart.setDisable(newValue);
                    dateEnd.setDisable(newValue);
                    hourStart.setDisable(newValue);
                    hourEnd.setDisable(newValue);
                    etDestinyBefore.setDisable(newValue);
                    etDestinyDuring.setDisable(newValue);
                    etDestinyAfter.setDisable(newValue);

                    dateStart.setValue(null);
                    dateEnd.setValue(null);
                    hourStart.setValue(null);
                    hourEnd.setValue(null);
                    etDestinyBefore.setText(null);
                    etDestinyDuring.setText(null);
                    etDestinyAfter.setText(null);

                } else {
                    tpLaunch.setDisable(newValue);
                    dateStart.setDisable(newValue);
                    dateEnd.setDisable(newValue);
                    hourStart.setDisable(newValue);
                    hourEnd.setDisable(newValue);
                    etDestinyBefore.setDisable(newValue);
                    etDestinyDuring.setDisable(newValue);
                    etDestinyAfter.setDisable(newValue);

                    dateStart.setValue(null);
                    dateEnd.setValue(null);
                    hourStart.setValue(null);
                    hourEnd.setValue(null);
                    etDestinyBefore.setText(null);
                    etDestinyDuring.setText(null);
                    etDestinyAfter.setText(null);
                }
            }
        });
        tpLaunch.expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                tpAB.setDisable(newValue);
                etTimeDayAB.setText(null);
                etDestinyA.setText(null);
                etDestinyB.setText(null);

                etTimeDayAB.setDisable(newValue);
                etDestinyA.setDisable(newValue);
                etDestinyB.setDisable(newValue);
            }
        });

        sbPanelLinkReplace.setVisible(false);
        sbPanelLinkReplace.setPrefHeight(0.0);

        lvUrlReplace.setCellFactory(param -> new AdapterLinkReplace(lvUrlReplace, this));
        lvUrlReplace.getItems().addAll(new ITLinkReplace());
        lvUrlReplace.refresh();

        btnAddLinkReplace.setOnAction(event -> {
            addLinkReplace();
        });

        //hackTooltipStartTiming(toolTip);
    }

    public final void addLinkReplace(){
        lvUrlReplace.getItems().add(new ITLinkReplace());
        lvUrlReplace.refresh();
        sbPanelLinkReplace.setPrefHeight(lvUrlReplace.getItems().size() * 48);
    }

    public final void removerLinkReplace(int index){
        lvUrlReplace.getItems().remove(index);
        sbPanelLinkReplace.setPrefHeight(((lvUrlReplace.getItems().size()) * 48));
    }

    private void setActionButton() {
        btnSave.setOnMouseClicked((event) -> {
            createLink();
        });
    }

    private void initializeRedirectTypes() {
        String[] redirectTypes = new String[]{"Redirecionamento", "Manter URL"};

        cbRedirectTypes.setItems(FXCollections.observableArrayList(redirectTypes));
        cbRedirectTypes.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (link != null) {
                    link.setRedirectType(decodeRedirectType(newValue));

                    String selectedType = newValue ;
                    if(selectedType.equals("CLONAR DESTINO")){
                        sbPanelLinkReplace.setVisible(true);
                        sbPanelLinkReplace.setPrefHeight(48.0);
                    } else {
                        sbPanelLinkReplace.setVisible(false);
                        sbPanelLinkReplace.setPrefHeight(0.0);
                        lvUrlReplace.getItems().clear();
                    }
                }
                else{
                    String selectedType = newValue ;
                    if(selectedType.equals("CLONAR DESTINO")){
                        sbPanelLinkReplace.setVisible(true);
                        sbPanelLinkReplace.setPrefHeight(48.0);
                    } else {
                        sbPanelLinkReplace.setVisible(false);
                        sbPanelLinkReplace.setPrefHeight(0.0);
                    }
                }
            }
        });
    }

    private void initializeEvents() {
        UserAccount ac = getAccount();

        JSONObject send = new JSONObject();
        send.put("idu", ac.getId());
        send.put("oip", ac.getSec().getIpPublicFirst());
        send.put("mac", ac.getSec().getMac());
        send.put("sid", ac.getSec().getSystemId());

        InternetRequisitionAPI requestServer = OLinkConfiguration.getApiRequisition();
        requestServer.setIdRequisition("initializeEvents");
        requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
        requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
        requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
        requestServer.setDomain(UrlsConnetionsWEB.DOMAIN_CONNECTION);
        requestServer.setController(UrlsConnetionsWEB.ChannelManager.CHANNEL);
        requestServer.setFunction(UrlsConnetionsWEB.ChannelManager.Methods.GET_EVENTS_PIXELS);
        requestServer.setObjectTransport(send);
        requestServer.request(new InternetRequisitionResponse() {
            @Override
            public void onLoading(boolean loading) {
            }

            @Override
            public void onSucess(Object object) {
                if (object != null) {

                    JsonArray array = new Gson().fromJson(object.toString(), JsonArray.class);

                    List<String> events = new ArrayList<>();
                    Gson gson = new Gson();
                    array.forEach((e) -> {
                        events.add(gson.fromJson(e, String.class));
                    });

                    cbEvents.setItems(FXCollections.observableArrayList(events));
                    cbEvents.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                            etNewEvent.setText(newValue);
                        }
                    });
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
        /*
        if (haveConnection()) {
            Thread th = new Thread(getThreadGroup(), () -> {

                StringBuilder sb = new StringBuilder();
                sb.append(UrlsConnetionsWEB.DOMAIN_CONNECTION);
                sb.append(UrlsConnetionsWEB.ChannelManager.CHANNEL);
                sb.append(UrlsConnetionsWEB.ChannelManager.Methods.GET_EVENTS_PIXELS);

                ITSocialChannel item = new ITSocialChannel();
                item.setIdUser(getAccount().getId());
                item.setId(getSelectedChannel().getId());

                String json = new WebJson().postJson(sb.toString(), item);

                JsonArray array = new Gson().fromJson(json, JsonArray.class);

                List<String> events = new ArrayList<>();
                Gson gson = new Gson();
                array.forEach((e) -> {
                    events.add(gson.fromJson(e, String.class));
                });

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        cbEvents.setItems(FXCollections.observableArrayList(events));
                        cbEvents.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                                etNewEvent.setText(newValue);
                            }
                        });
                    }
                });
            });
            th.start();
        } else {
            new DialogNoConnection(stackPane).show(true);
        }*/
    }

    public int decodeRedirectType(String redirectype) {
        switch (redirectype) {
            case "Redirecionamento Padrão":
                return 0;
            case "Manter URL":
                return 4751369;
            case "CLONAR DESTINO":
                return 745;
            default:
                return 0;
        }
    }

    public String encodeRedirectType(int redirectype) {
        switch (redirectype) {
            case 0:
                return "Redirecionamento Padrão";
            case 4751369:
                return "Manter URL";
            case 745:
                return "CLONAR DESTINO";
            default:
                return "Redirecionamento Padrão";
        }
    }


    private void createLink() {

        boolean linkName = etTitle.getText() != null && !etTitle.getText().isEmpty();
        boolean linkCustom = etNewLink.getText() != null && !etNewLink.getText().isEmpty();
        //boolean linkRedirect = etLinkDestiny.getText() != null && !etLinkDestiny.getText().isEmpty();
        boolean validRedirect = etLinkDestiny.getText().contains("http://") || etLinkDestiny.getText().contains("https://");
            /*boolean validRedirectA = etDestinyA.getText().contains("http://") || etDestinyA.getText().contains("https://");
            boolean validRedirectB = etDestinyB.getText().contains("http://") || etDestinyB.getText().contains("https://");
            boolean validRedirectBef = etDestinyBefore.getText().contains("http://") || etDestinyBefore.getText().contains("https://");
            boolean validRedirectDur = etDestinyDuring.getText().contains("http://") || etDestinyDuring.getText().contains("https://");
            boolean validRedirectAft = etDestinyAfter.getText().contains("http://") || etDestinyAfter.getText().contains("https://");
*/
        boolean validLaunch = dateStart.getValue() != null && dateEnd.getValue() != null &&
                hourStart.getValue() != null && hourEnd.getValue() != null &&
                etDestinyBefore.getText() != null && !etDestinyBefore.getText().isEmpty() &&
                etDestinyDuring.getText() != null && !etDestinyDuring.getText().isEmpty() &&
                etDestinyAfter.getText() != null && !etDestinyAfter.getText().isEmpty();

        boolean isLaunch = dateStart.getValue() != null || dateEnd.getValue() != null ||
                hourStart.getValue() != null || hourEnd.getValue() != null ||
                etDestinyBefore.getText() != null && !etDestinyBefore.getText().isEmpty() ||
                etDestinyDuring.getText() != null && !etDestinyDuring.getText().isEmpty() ||
                etDestinyAfter.getText() != null && !etDestinyAfter.getText().isEmpty();

        boolean validTesteAB = etTimeDayAB.getText() != null && (!etTimeDayAB.getText().equals("__")) &&
                etDestinyA.getText() != null && !etDestinyA.getText().isEmpty() &&
                etDestinyB.getText() != null && !etDestinyB.getText().isEmpty();

        boolean isTesteAB = etTimeDayAB.getText() != null && (!etTimeDayAB.getText().equals("__")) ||
                etDestinyA.getText() != null && !etDestinyA.getText().isEmpty() ||
                etDestinyB.getText() != null && !etDestinyB.getText().isEmpty();

        if (validRedirect || isTesteAB || isLaunch) {

            if (IS_AVAILABLE || (link != null && link.getId() != 0)) {

                if (linkName) {

                    if (linkCustom) {
                        if (link != null) {

                            link.setDomain(UrlsConnetionsWEB.DOMAIN_CONNECTION);
                            link.setLinkName(etTitle.getText());
                            link.setFromLink(etNewLink.getText());
                            link.setToLink(etLinkDestiny.getText());
                            link.setIdChannel(socialChannel.getId());
                            link.setIdContent(socialChannelContent.getId());
                            link.setIdUser(getAccount().getId());

                            if(cbRedirectTypes.getSelectionModel().getSelectedItem().equals("CLONAR DESTINO"))
                            link.setPage(APIJsoup.cloneWebSiteAndReplaceURLS(etLinkDestiny.getText(),lvUrlReplace.getItems()));

                            ITTesteAB testeAB;
                            if (isTesteAB && validTesteAB) {
                                testeAB = new ITTesteAB();
                                testeAB.setTimeDays(Integer.valueOf(etTimeDayAB.getText()));
                                testeAB.setDestinyA(etDestinyA.getText());
                                testeAB.setDestinyB(etDestinyB.getText());

                                link.setTesteAB(testeAB);
                            }

                            ITLaunch launch;
                            if (isLaunch && validLaunch) {
                                String pattern = "yyyy-MM-dd";
                                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

                                String patternHour = "HH:mm:ss";
                                DateTimeFormatter dateFormatterHour = DateTimeFormatter.ofPattern(patternHour);

                                launch = new ITLaunch();
                                launch.setDateStart(dateStart.getValue().format(dateFormatter));
                                launch.setHourStart(hourStart.getValue().format(dateFormatterHour));
                                launch.setDateEnd(dateEnd.getValue().format(dateFormatter));
                                launch.setHourEnd(hourEnd.getValue().format(dateFormatterHour));
                                launch.setUrlBefore(etDestinyBefore.getText());
                                launch.setUrlDuring(etDestinyDuring.getText());
                                launch.setUrlAfter(etDestinyAfter.getText());

                                link.setLaunch(launch);
                            }


                            if (isLaunch && validLaunch && isTesteAB && validTesteAB) {
                                updateLink();
                            } else if (isLaunch && !validLaunch && isTesteAB && validTesteAB ||
                                    isLaunch && validLaunch && isTesteAB && !validTesteAB) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Teste A/B ou Lançamento Inválido");
                                alert.setContentText("Verifique se os campos estão completos e corretos");
                                alert.show();
                            } else if (isTesteAB && validTesteAB) {
                                updateLink();
                            } else if (isTesteAB && !validTesteAB) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Teste A/B Inválido");
                                alert.setContentText("Verifique se os campos estão completos e corretos");
                                alert.show();
                            } else if (isLaunch && validLaunch) {
                                updateLink();
                            } else if (isLaunch && !validLaunch) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Lançamento Inválido");
                                alert.setContentText("Verifique se os campos estão completos e corretos");
                                alert.show();
                            } else if (!isLaunch && !validLaunch && !isTesteAB && !validTesteAB) {
                                updateLink();
                            }
                        } else {
                            link = new ITLink();
                            link.setDomain(UrlsConnetionsWEB.DOMAIN_CONNECTION);
                            link.setLinkName(etTitle.getText());
                            link.setFromLink(etNewLink.getText());
                            link.setToLink(etLinkDestiny.getText());
                            link.setIdChannel(socialChannel.getId());

                            if(cbRedirectTypes.getSelectionModel().getSelectedItem() != null && cbRedirectTypes.getSelectionModel()
                                    .getSelectedItem().equals("CLONAR DESTINO"))
                            link.setPage(APIJsoup.cloneWebSiteAndReplaceURLS(etLinkDestiny.getText(),lvUrlReplace.getItems()));

                            if (socialChannelContent != null)
                                link.setIdContent(socialChannelContent.getId());
                            link.setIdUser(getAccount().getId());

                            ITTesteAB testeAB;
                            if (isTesteAB && validTesteAB) {
                                testeAB = new ITTesteAB();
                                testeAB.setTimeDays(Integer.valueOf(etTimeDayAB.getText()));
                                testeAB.setDestinyA(etDestinyA.getText());
                                testeAB.setDestinyB(etDestinyB.getText());

                                link.setTesteAB(testeAB);
                            }

                            ITLaunch launch;
                            if (isLaunch && validLaunch) {
                                String pattern = "yyyy-MM-dd";
                                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

                                String patternHour = "HH:mm:ss";
                                DateTimeFormatter dateFormatterHour = DateTimeFormatter.ofPattern(patternHour);

                                launch = new ITLaunch();
                                launch.setDateStart(dateStart.getValue().format(dateFormatter));
                                launch.setHourStart(hourStart.getValue().format(dateFormatterHour));
                                launch.setDateEnd(dateEnd.getValue().format(dateFormatter));
                                launch.setHourEnd(hourEnd.getValue().format(dateFormatterHour));
                                launch.setUrlBefore(etDestinyBefore.getText());
                                launch.setUrlDuring(etDestinyDuring.getText());
                                launch.setUrlAfter(etDestinyAfter.getText());

                                link.setLaunch(launch);
                            }

                            if ((isLaunch && validLaunch) && (isTesteAB && validTesteAB)) {
                                saveLink();
                            } else if (isLaunch && !validLaunch && isTesteAB && validTesteAB ||
                                    isLaunch && validLaunch && isTesteAB && !validTesteAB) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Teste A/B ou Lançamento Inválido");
                                alert.setContentText("Verifique se os campos estão completos e corretos");
                                alert.show();
                                link = null;
                            } else if (isTesteAB && validTesteAB) {
                                saveLink();
                            } else if (isTesteAB && !validTesteAB) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Teste A/B Inválido");
                                alert.setContentText("Verifique se os campos estão completos e corretos");
                                alert.show();
                                link = null;
                            } else if (isLaunch && validLaunch) {
                                saveLink();
                            } else if (isLaunch && !validLaunch) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Lançamento Inválido");
                                alert.setContentText("Verifique se os campos estão completos e corretos \nincluindo data e hora");
                                alert.show();
                                link = null;
                            } else if (!isLaunch && !validLaunch && !isTesteAB && !validTesteAB) {
                                saveLink();
                            }
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("LINK INVÁLIDO");
                        alert.setContentText("Link customizado inválido, Não pode ser NULO ");
                        alert.show();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("DESCRIÇÃO");
                    alert.setContentText("Digite um descrição/nome para o link ");
                    alert.show();
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ERRO NA URL ENCURTADA");
                alert.setContentText("A url especificada não está disponível: " + etNewLink.getText());
                alert.show();
            }
        } else {

            toolTip.setText("Necessário HTTP:// ou HTTPS://");
            addToolTipAndBorderColor(etLinkDestiny, toolTip);
            //hackTooltipStartTiming(toolTip);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("NECESSIDADES DE PREENCHIMENTO");
            alert.setContentText("Copie e Cole o Link com HTTP:// ou HTTPS://");
            alert.show();
        }

    }

    private void updateLink() {
        showLoading(true);

        String rType = cbRedirectTypes.getSelectionModel().getSelectedItem();
        String rTypeDefault = "Redirecionamento Padrão";
        String value = etValueConversion.getText();

        link.setValue(Double.valueOf(value.isEmpty() ? "0" : value));

        String event = etNewEvent.getText();

        if (cbEvents.getSelectionModel().getSelectedItem() != null && !event.equals("Meus Eventos") ||
                event != null && !event.isEmpty()) {

            ITPixel pixel = new ITPixel();
            pixel.setEvent(event);

            link.setPixel(pixel);
        }

        ITTesteAB ab = new ITTesteAB();
        if (etTimeDayAB.getText() != null && !etTimeDayAB.getText().equals("__")){
            ab.setTimeDays(Integer.valueOf(etTimeDayAB.getText()));
            ab.setDestinyA(etDestinyA.getText());
            ab.setDestinyB(etDestinyB.getText());
            link.setTesteAB(ab);
        }

        link.setRedirectType(decodeRedirectType(rType == null ? rTypeDefault : rType));

        UserAccount ac = getAccount();

        JSONObject sendUserData = new JSONObject();
        sendUserData.put("idu", ac.getId());
        sendUserData.put("idc", socialChannel.getId());
        sendUserData.put("ict", socialChannelContent.getId());
        sendUserData.put("oip", ac.getSec().getIpPublicFirst());
        sendUserData.put("mac", ac.getSec().getMac());
        sendUserData.put("sid", ac.getSec().getSystemId());

        sendUserData.put("lkn", link.getLinkName());
        sendUserData.put("ltl", link.getToLink());
        sendUserData.put("lrt", link.getRedirectType());
        sendUserData.put("lva", link.getValue());
        sendUserData.put("lid", link.getId());
        sendUserData.put("nn","nn");

        JSONObject send = new JSONObject();
        send.put("secret", OLinkConfiguration.encryptExtended(sendUserData));
        send.put("pg", link.getPage());

        if (link.getPixel() != null) {
            send.put("lpe", link.getPixel().getEvent());
            send.put("pid", link.getPixel().getIdPixel());
            send.put("lip", link.getPixel().getId());
            send.put("pep", link.getPixel().getEventPurchase());
        }

        if (link.getTesteAB() != null) {
            send.put("abd", link.getTesteAB().getTimeDays());
            send.put("aba", link.getTesteAB().getDestinyA());
            send.put("abb", link.getTesteAB().getDestinyB());
        }

        if (link.getLaunch() != null) {
            send.put("lds", link.getLaunch().getDateStart());
            send.put("lde", link.getLaunch().getDateEnd());
            send.put("lhs", link.getLaunch().getHourStart());
            send.put("lhe", link.getLaunch().getHourEnd());
            send.put("lua", link.getLaunch().getUrlAfter());
            send.put("lub", link.getLaunch().getUrlBefore());
            send.put("lud", link.getLaunch().getUrlDuring());
        }
        //send.put("link", new Gson().toJson(link));

        InternetRequisitionAPI requestServer = OLinkConfiguration.getApiRequisition();
        requestServer.setIdRequisition("updateLink");
        requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
        requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.AES);
        requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
        requestServer.setDomain(UrlsConnetionsWEB.DOMAIN_CONNECTION);
        requestServer.setController(UrlsConnetionsWEB.ContentManager.Link.LINKS);
        requestServer.setFunction(UrlsConnetionsWEB.ContentManager.Link.Method.UPDATE_LINK);
        requestServer.setObjectTransport(send);
        requestServer.request(new InternetRequisitionResponse() {
            @Override
            public void onLoading(boolean loading) {
            }

            @Override
            public void onSucess(Object object) {
                if (object != null) {
                    showLoading(false);

                    JSONObject response = new JSONObject(object.toString());
                    response = response.getJSONObject("map");

                    boolean result = response.getBoolean("result");

                    if (result) {

                        closeFrame();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("STATUS DE ATUALIZAÇÃO");
                        alert.setContentText("Erro ao tentar atualizar");
                        alert.show();
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

        /*Thread th = new Thread(getThreadGroup(), () -> {

            StringBuilder url = new StringBuilder();
            url.append(UrlsConnetionsWEB.DOMAIN_CONNECTION);
            url.append(UrlsConnetionsWEB.ContentManager.Link.LINKS);
            url.append(UrlsConnetionsWEB.ContentManager.Link.Method.UPDATE_LINK);

            String jsonResult = new WebJson().postJson(url.toString(), link);
            boolean result = Boolean.valueOf(jsonResult);

            Platform.runLater(() -> {
                if (result) {
                    showLoading(false);
                    closeFrame();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("STATUS DE ATUALIZAÇÃO");
                    alert.setContentText("Erro ao tentar atualizar");
                    alert.show();
                }
            });
        });
        th.start();*/
    }

    private void saveLink() {
        showLoading(true);

        String rType = cbRedirectTypes.getSelectionModel().getSelectedItem();
        String rTypeDefault = "Redirecionamento Padrão";
        String value = etValueConversion.getText();

        link.setValue(Double.valueOf(value.isEmpty() ? "0" : value));

        String event = etNewEvent.getText();

        if(cbEvents.getSelectionModel().getSelectedItem() != null &&
                event != null && !event.equals("Meus Eventos") ||
                event != null && !event.isEmpty()) {

            ITPixel pixel = new ITPixel();
            pixel.setEvent(event);
            pixel.setIdPixel(socialChannel.getPixel());

            link.setPixel(pixel);
        }

        link.setRedirectType(decodeRedirectType(rType == null ? rTypeDefault : rType));

        if (socialChannelContent != null)
            link.setIdContent(socialChannelContent.getId());

        closeFrame();
    }

    private void startEdit() {
        if (link != null) {
            usUpdate = true;
            etTitle.setText(link.getLinkName());
            etNewLink.setText(link.getFromLink());
            etNewLink.setDisable(true);
            etLinkDestiny.setText(link.getToLink());

            if (link.getPixel() != null)
                etNewEvent.setText(link.getPixel().getEvent());

            if (link.getTesteAB() != null) {
                etTimeDayAB.setText(String.valueOf(link.getTesteAB().getTimeDays()));
                etDestinyA.setText(link.getTesteAB().getDestinyA());
                etDestinyB.setText(link.getTesteAB().getDestinyB());
                tpAB.setExpanded(true);
            }

            if (link.getLaunch() != null && link.getLaunch().getDateStart() != null &&
                link.getLaunch().getHourStart() != null) {

                String dateStartB = UtilDate.dateTimeFormatter(link.getLaunch().getDateStart(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd");
                String dateEndB = UtilDate.dateTimeFormatter(link.getLaunch().getDateEnd(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd");

                LocalDate dateStart = LocalDate.parse(dateStartB);
                LocalDate dateEnd = LocalDate.parse(dateEndB);

              /*  String hourStartB = UtilDate.dateTimeFormatter(link.getLaunch().getHourStart(), "HH:mm:ss", "HH:mm:ss");
                String hourEndB = UtilDate.dateTimeFormatter(link.getLaunch().getHourEnd(), "HH:mm:ss", "HH:mm:ss");
*/
                LocalTime hourStart = LocalTime.parse(link.getLaunch().getHourStart());
                LocalTime hourEnd = LocalTime.parse(link.getLaunch().getHourEnd());

                this.dateStart.setValue(dateStart);
                this.dateEnd.setValue(dateEnd);

                this.hourStart.setValue(hourStart);
                this.hourEnd.setValue(hourEnd);

                etDestinyBefore.setText(link.getLaunch().getUrlBefore());
                etDestinyDuring.setText(link.getLaunch().getUrlDuring());
                etDestinyAfter.setText(link.getLaunch().getUrlAfter());

                tpLaunch.setExpanded(true);
            }

            //etEmbedCodeEvent.setText(link.getPixelEvent());
            etValueConversion.setText(String.valueOf(link.getValue()));
            btnSave.setText("Atualizar");
            cbRedirectTypes.getSelectionModel().select(encodeRedirectType(link.getRedirectType()));

            if (link.getPixel() != null)
                cbEvents.getSelectionModel().select(link.getPixel().getEvent());

            if (link != null && link.getPixel() != null && link.getPixel().getEvent() != null)
                rbApplyPixel.setSelected(true);

            if (link != null && link.getValue() > 0) {
                rbLinkConversion.setDisable(true);
                rbLinkConversion.setSelected(true);
            }

        }
    }

    public ITLink getLink() {
        return link;
    }

    public void setSocialChannel(ITSocialChannel item) {
        this.socialChannel = item;
    }

    public void closeFrame() {
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }

    public void setSocialContenr(ITSocialChannelContent item) {
        this.socialChannelContent = item;
    }

    private void showLoading(boolean show) {
        bpLoading.setVisible(show);
    }

    private static void addToolTipAndBorderColor(Node n, Tooltip t) {
        String STILE_BORDER_VALIDATION = "-fx-border-color: #FF0000; -fx-background-radius: 15; -fx-border-radius: 15; -fx-border-width: 2";

        Tooltip.install(n, t);
        n.setStyle(STILE_BORDER_VALIDATION);
    }

    private static void removeToolTipAndBorderColor(Node n, Tooltip t) {
        String STILE_BORDER_VALIDATION = "-fx-background-radius: 15; -fx-border-radius: 15";
        Tooltip.uninstall(n, t);
        n.setStyle(STILE_BORDER_VALIDATION);
    }

    @FXML
    public void onEnter(KeyEvent ae) {
        if ((ae != null && ae.getCode() == KeyCode.ENTER) || (ae != null && ae.getCode() == KeyCode.TAB) && (link != null && link.getId() != 0)) {
            verifyNewLinkAvailable(etNewLink.getText());
        }
    }

    public static void hackTooltipStartTimin(Tooltip tooltip) {
        try {
            Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            Object objBehavior = fieldBehavior.get(tooltip);

            Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(5)));
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private synchronized void verifyNewLinkAvailable(String linkName) {

        InternetRequisitionAPI requestServer = OLinkConfiguration.getApiRequisition();
        //requestServer.setIdRequisition("verifyNewLinkAvailable");
        requestServer.setTypeRequest(InternetRequisition.TypeRequest.GET);
        requestServer.setDomain(UrlsConnetionsWEB.DOMAIN_CONNECTION);
        //requestServer.setController(null);
        requestServer.setFunction(UrlsConnetionsWEB.ContentManager.Link.Method.AS_AVAILABLE_LINK);
        //requestServer.setObjectTransport(send);
        requestServer.addUrlParam("link", linkName);
        requestServer.request(new InternetRequisitionResponse() {
            @Override
            public void onLoading(boolean loading) {
            }

            @Override
            public void onSucess(Object object) {
                if (object != null) {

                    boolean rest = Boolean.valueOf(object.toString());
                    IS_AVAILABLE = rest;

                    if (!etNewLink.focusedProperty().getValue() && !etNewLink.getText().isEmpty() && (!IS_AVAILABLE || checkNewLinkAsCreated())) {
                        if (IS_AVAILABLE)
                            IS_AVAILABLE = false;
                        addToolTipAndBorderColor(etNewLink, toolTip);
                    } else {
                        removeToolTipAndBorderColor(etNewLink, toolTip);
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
/*
        if (haveConnection()) {
            Thread th = new Thread(getThreadGroup(), new Runnable() {
                @Override
                public void run() {
                    StringBuilder sb = new StringBuilder();
                    sb.append(UrlsConnetionsWEB.DOMAIN_CONNECTION);
                    sb.append(UrlsConnetionsWEB.ContentManager.Link.Method.AS_AVAILABLE_LINK);
                    sb.append(text);

                    boolean rest = Boolean.valueOf(new WebJson().getJson(sb.toString()));
                    IS_AVAILABLE = rest;

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (!etNewLink.focusedProperty().getValue() && !etNewLink.getText().isEmpty() && (!IS_AVAILABLE || checkNewLinkAsCreated())) {
                                if(IS_AVAILABLE)
                                    IS_AVAILABLE=false;
                                addToolTipAndBorderColor(etNewLink, toolTip);
                            } else {
                                removeToolTipAndBorderColor(etNewLink, toolTip);
                            }
                        }
                    });
                }
            });
            th.start();
        } else {
            new DialogNoConnection(stackPane).show(true);
        }*/
    }

    private synchronized boolean checkNewLinkAsCreated() {
        return urlsCreated.contains(etNewLink.getText());
    }

    private final void unlockAccess(boolean access) {
        if (access) {

            etValueConversion.setDisable(true);

            rbLinkConversion.setDisable(false);
            cbRedirectTypes.setDisable(false);
            etNewEvent.setDisable(false);
            cbEvents.setDisable(false);
            lbPRO.setDisable(false);
            lbPRO.setVisible(false);
            etNewEvent.setDisable(true);
            cbEvents.setDisable(true);
            rbApplyPixel.setDisable(false);
            tpAB.setDisable(false);
            tpLaunch.setDisable(false);
            etTimeDayAB.setDisable(false);
            etDestinyA.setDisable(false);
            etDestinyB.setDisable(false);
            etDestinyBefore.setDisable(false);
            etDestinyDuring.setDisable(false);
            etDestinyAfter.setDisable(false);

            //lbRastrer.setDisable(false);
            btnUpgrade.setVisible(false);
            btnUpgrade.setDisable(false);

            if(link != null && link.getValue() > 0)
                rbLinkConversion.setDisable(true);

            if(OLinkConfiguration.haveAccess(OLinkConfiguration.getSERVICE_ANUAL())){
                String[] redirectTypes = new String[]{"Redirecionamento", "Manter URL", "CLONAR DESTINO"};
                cbRedirectTypes.setItems(FXCollections.observableArrayList(redirectTypes));
                cbRedirectTypes.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        if (link != null) {
                            link.setRedirectType(decodeRedirectType(newValue));

                            String selectedType = newValue ;
                            if(selectedType.equals("CLONAR DESTINO")){
                                sbPanelLinkReplace.setVisible(true);
                                sbPanelLinkReplace.setPrefHeight(48.0);
                            } else {
                                sbPanelLinkReplace.setVisible(false);
                                sbPanelLinkReplace.setPrefHeight(0.0);
                                lvUrlReplace.getItems().clear();
                            }
                        }
                        else{
                            String selectedType = newValue ;
                            if(selectedType.equals("CLONAR DESTINO")){
                                sbPanelLinkReplace.setVisible(true);
                                sbPanelLinkReplace.setPrefHeight(48.0);
                            } else {
                                sbPanelLinkReplace.setVisible(false);
                                sbPanelLinkReplace.setPrefHeight(0.0);
                            }
                        }
                    }
                });
            }
        } else {
            rbLinkConversion.setDisable(true);
            cbRedirectTypes.setDisable(true);
            etValueConversion.setDisable(false);
            cbEvents.setDisable(true);
            //etNewEvent.setDisable(false);
            lbPRO.setVisible(true);
            rbApplyPixel.setDisable(true);
            //lbRastrer.setDisable(true);
            btnUpgrade.setVisible(true);
            btnUpgrade.setDisable(false);

            tpAB.setDisable(true);
            tpLaunch.setDisable(true);
            etTimeDayAB.setDisable(true);
            etDestinyA.setDisable(true);
            etDestinyB.setDisable(true);
            etDestinyBefore.setDisable(true);
            etDestinyDuring.setDisable(true);
            etDestinyAfter.setDisable(true);
        }
    }

    public boolean isUsUpdate() {
        return usUpdate;
    }

    public void startPlans() {
        Stage stage = (Stage) bpLoading.getScene().getWindow();
        stage.close();

        PlansActivity plans = new PlansActivity(stage);
        plans.show(false);
    }

    public void addUrlsCreated(String url) {
        urlsCreated.add(url);
    }

}
