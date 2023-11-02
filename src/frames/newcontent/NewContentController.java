package frames.newcontent;

import Itens.ITLink;
import Itens.ITPartner;
import Itens.ITSocialChannel;
import Itens.ITSocialChannelContent;
import adapters.AdapterNewLink;
import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.jfoenix.controls.JFXButton;
import frames.account.UserAccount;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import frames.newlink.NewLinkActivity;
import org.json.JSONObject;
import system.OLinkConfiguration;
import urls.UrlsConnetionsWEB;
import util.DecodeChannelID;
import webconnection.DialogNoConnection;
import webconnection.WebJson;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import util.LKImageManager;
import util.UtilDialogManager;

import static system.OLinkConfiguration.*;
import static util.MaskedTextField.addTextLimiter;
import static webconnection.WebJson.haveConnection;

public class NewContentController implements Initializable {

    @FXML
    private TextField etTitleContent, etCatContent ;

    @FXML
    private ListView<ITLink> lvUrls ;

    @FXML
    private Button btnFinish ;

    @FXML
    private BorderPane bpLoading ;

    @FXML
    private ComboBox<String> cbCategory ;
    
    @FXML
    private JFXButton btnAddLink ;
    
    @FXML
    private AnchorPane rootContent ;
    
    @FXML
    private StackPane stackPaneContent ;

    private ITSocialChannel socialChannel ;
    private ITSocialChannelContent item ;
    private int itemPositionEdit = -1;

    public NewContentController(){}

    public NewContentController(ITSocialChannelContent content){
        item = content ;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startEdit();

        lvUrls.setCellFactory(param -> new AdapterNewLink(this));
        btnAddLink.setGraphic(new LKImageManager().getImageResource("/icon_plus_color.png",30));

        //loadCategory();
        getSavedsCategories(socialChannel);
        addTextLimiter(etCatContent,20);
        addTextLimiter(etTitleContent,50);
    }

    public void comboAction(ActionEvent event) {
        etCatContent.setText(cbCategory.getSelectionModel().getSelectedItem());
    }
    
    private void startEdit(){
        if(item != null){
            Platform.runLater(()->{
                etTitleContent.setText(item.getTitle());
                lvUrls.getItems().addAll(item.getLinks());
                btnFinish.setText("Atualizar");
                etCatContent.setText(item.getCategory());
            });
        }
    }
/*

    private void loadCategory(){
        if(haveConnection()){
            Thread th = new Thread(getThreadGroup(), () -> {
                StringBuilder url = new StringBuilder();
                url.append(UrlsConnetionsWEB.DOMAIN_CONNECTION);
                url.append(UrlsConnetionsWEB.ContentManager.CONTENTS);
                url.append(UrlsConnetionsWEB.ContentManager.Method.GET_SAVEDS_CATEGORIES);
                url.append("?iduser=");
                url.append(getAccount().getId());
                url.append("&idchannel=");
                url.append(getSelectedChannel().getId());

                String jsonResult = new WebJson().getJson(url.toString());
                JsonArray array = new Gson().fromJson(jsonResult, JsonArray.class);

                List<String> cats = new ArrayList<>();
                for(JsonElement e : array){
                    cats.add(e.getAsString());
                }

                Platform.runLater(() -> cbCategory.setItems(FXCollections.observableArrayList(cats)));
            });
            th.start();
        } else {
            new DialogNoConnection(stackPaneContent).show(true);
        }
    }
*/

    private synchronized void getSavedsCategories(ITSocialChannel channel) {

        UserAccount ac = getAccount();

        JSONObject send = new JSONObject();
        send.put("idu", ac.getId());
        send.put("oip", ac.getSec().getIpPublicFirst());
        send.put("mac", ac.getSec().getMac());
        send.put("sid", ac.getSec().getSystemId());
        send.put("idc", getSelectedChannel().getId());
        send.put("ilt", 0);

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
                    //cats.add("Todas as Categorias");
                    for (JsonElement e : array) {
                        String s = e.getAsString();

                        if (s != null && !s.isEmpty()) {
                            cats.add(e.getAsString());
                        }
                    }

                    cbCategory.setItems(FXCollections.observableArrayList(cats));
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
                new DialogNoConnection(stackPaneContent).show(true);
            }
        });
    }

    public void crateLink(NewLinkActivity activity){
        ITLink item = activity.getController().getLink();
        if(item != null && !activity.getController().isUsUpdate()){
            item.setDomain(UrlsConnetionsWEB.DOMAIN_CONNECTION);
            item.setIdUser(getAccount().getId());
            item.setIdChannel(socialChannel.getId());
            item.setIdContent(item.getId());

            lvUrls.getItems().add(item);
            lvUrls.refresh();

            ITSocialChannelContent content = getContent();

            if(content != null)
                content.getLinks().add(item);
        } else if(!lvUrls.getItems().isEmpty() && itemPositionEdit != -1 && lvUrls.getItems().get(itemPositionEdit)!= null){
            ITLink i = lvUrls.getItems().get(itemPositionEdit);
            i.setId(item.getId());
            i.setIdChannel(socialChannel.getId());
            i.setIdContent(item.getIdContent());
            i.setIdUser(item.getIdUser());
            i.setDomain(item.getDomain());
            i.setRedirectType(item.getRedirectType());
            i.setFromLink(item.getFromLink());
            i.setLinkName(item.getLinkName());
            i.setPixel(item.getPixel());
            i.setTesteAB(item.getTesteAB());
            i.setLaunch(item.getLaunch());

            i.setToLink(item.getToLink());
            i.setValue(item.getValue());
            i.setPage(item.getPage());

            getContent().getLinks().remove(i);
            getContent().getLinks().add(i);
            lvUrls.refresh();
        }
    }

    public void setItemPositionEdit(int itemPositionEdit) {
        this.itemPositionEdit = itemPositionEdit;
    }

    public void startActivityLink(){
        try {
            Stage stage = (Stage) lvUrls.getScene().getWindow();

            NewLinkActivity activity = new NewLinkActivity(stage);
            activity.getController().setSocialChannel(socialChannel);
            activity.getController().setSocialContenr(item);

            for(ITLink link : lvUrls.getItems()){
                activity.getController().addUrlsCreated(link.getFromLink());
            }

            activity.show();
            crateLink(activity);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setContent(ITSocialChannelContent item) {
        this.item = item;
    }

    public ITSocialChannelContent getContent() {
        return item;
    }

    @FXML
    public void concluir(){
        if((etCatContent.getText() != null && !etCatContent.getText().isEmpty())
                && (etTitleContent.getText() != null
                && !etTitleContent.getText().isEmpty())) {

            showLoading(true);
            UserAccount ac = getAccount();

            JSONObject secretData = new JSONObject();
            secretData.put("idu", ac.getId());
            secretData.put("oip", ac.getSec().getIpPublicFirst());
            secretData.put("mac", ac.getSec().getMac());
            secretData.put("sid", ac.getSec().getSystemId());
            secretData.put("idc",socialChannel.getId());
            secretData.put("ctt",etTitleContent.getText());
            secretData.put("cct",etCatContent.getText());
            secretData.put("idt",item!=null?item.getId():0);
            secretData.put("csp",socialChannel.getSpecification());

            JSONObject send = new JSONObject();
            send.put("secret", OLinkConfiguration.encryptExtended(secretData));
            send.put("links", new ArrayList<>(lvUrls.getItems()));

            InternetRequisitionAPI requestServer = OLinkConfiguration.getApiRequisition();
            requestServer.setIdRequisition("concluir");
            requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.AES);
            requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
            requestServer.setDomain(UrlsConnetionsWEB.DOMAIN_CONNECTION);
            requestServer.setController(UrlsConnetionsWEB.ContentManager.CONTENTS);

            boolean newContent = false ;

            if(item == null){
                newContent = true ;

                item = new ITSocialChannelContent();
                item.setTitle(etTitleContent.getText());
                item.getLinks().addAll(lvUrls.getItems());
                item.setIdUser(getAccount().getId());
                item.setIdChannel(socialChannel.getId());
                item.setPathImage(DecodeChannelID.decodeIconFromIDChannel(socialChannel.getId()));
                item.setCategory(etCatContent.getText());
                item.setSpecification(socialChannel.getSpecification());

                requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
                requestServer.setFunction(UrlsConnetionsWEB.ContentManager.Method.NEW_CONTENT);
            } else {
                newContent = false ;

                requestServer.setTypeRequest(InternetRequisition.TypeRequest.PUT);
                requestServer.setFunction(UrlsConnetionsWEB.ContentManager.Method.UPDATE_CONTENT);

                item.getLinks().clear();
                item.setTitle(etTitleContent.getText());
                item.getLinks().addAll(lvUrls.getItems());
                item.setIdUser(getAccount().getId());
                item.setIdChannel(socialChannel.getId());
                item.setPathImage(DecodeChannelID.decodeIconFromIDChannel(socialChannel.getId()));
                item.setSpecification(socialChannel.getSpecification());
            }

            boolean finalNewContent = newContent;

            requestServer.setObjectTransport(send);
            requestServer.request(new InternetRequisitionResponse() {
                @Override
                public void onLoading(boolean loading) { }

                @Override
                public void onSucess(Object object) {
                    if (object != null) {

                        JSONObject response = new JSONObject(object.toString());
                        response = response.getJSONObject("map");

                        boolean result = response.getBoolean("result");

                        String title = finalNewContent ?"STATUS DE INSERÇÃO":"STATUS DE ATUALIZAÇÃO";
                        String contentText = finalNewContent ?"inserido com sucesso!!":"atualizado com sucesso!!";
                        String contentTextError = finalNewContent ?"Erro durante inserção, tente novamente":"Erro durante atualização, tente novamente";

                        if(result){
                            Alert d = new Alert(Alert.AlertType.INFORMATION);
                            d.setTitle(title);
                            d.setContentText(contentText);
                            d.showAndWait();

                            Stage stage = (Stage) etTitleContent.getScene().getWindow();
                            stage.close();
                        } else if(!result) {
                            Alert d = new Alert(Alert.AlertType.ERROR);
                            d.setTitle(title);
                            d.setContentText(contentTextError);
                            d.showAndWait();
                        }
                    }
                    showLoading(false);
                }

                @Override
                public void onError(Exception e) {
                    showLoading(false);
                    e.printStackTrace();
                }

                @Override
                public void notConnectionInternet() {
                    showLoading(false);
                    new DialogNoConnection(stackPaneContent).show(true);
                }
            });

        }
      /*  if(haveConnection()){
            if((etCatContent.getText() != null && !etCatContent.getText().isEmpty())
                    && (etTitleContent.getText() != null
                     && !etTitleContent.getText().isEmpty())){
                showLoading(true);
                Thread th = new Thread(getThreadGroup(), new Runnable() {
                    @Override
                    public void run() {

                        StringBuilder sb = new StringBuilder();
                        sb.append(UrlsConnetionsWEB.DOMAIN_CONNECTION);
                        sb.append(UrlsConnetionsWEB.ContentManager.CONTENTS);

                        if(item == null){
                            item = new ITSocialChannelContent();
                            item.setTitle(etTitleContent.getText());
                            item.getLinks().addAll(lvUrls.getItems());
                            item.setIdUser(getAccount().getId());
                            item.setIdChannel(socialChannel.getId());
                            item.setPathImage(DecodeChannelID.decodeIconFromIDChannel(socialChannel.getId()));
                            item.setCategory(etCatContent.getText());
                            item.setSpecification(socialChannel.getSpecification());


                            sb.append(UrlsConnetionsWEB.ContentManager.Method.NEW_CONTENT);
                            String response = new WebJson().postJson(sb.toString(), item);

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {

                                    showLoading(false);

                                    boolean result = Boolean.valueOf(response);
                                    if(result){
                                        Alert d = new Alert(Alert.AlertType.CONFIRMATION);
                                        d.setTitle("STATUS DE INSERÇÃO");
                                        d.setContentText("Conteudo inserido com sucesso");
                                        d.showAndWait();

                                        Stage stage = (Stage) etTitleContent.getScene().getWindow();
                                        stage.close();
                                    } else {
                                        Alert d = new Alert(Alert.AlertType.CONFIRMATION);
                                        d.setTitle("STATUS DE INSERÇÃO");
                                        d.setContentText("Erro durante inserção, tente novamente");
                                        d.showAndWait();
                                    }
                                }
                            });
                        } else {
                            sb.append(UrlsConnetionsWEB.ContentManager.Method.UPDATE_CONTENT);

                            item.getLinks().clear();
                            item.setTitle(etTitleContent.getText());
                            item.getLinks().addAll(lvUrls.getItems());
                            item.setIdUser(getAccount().getId());
                            item.setIdChannel(socialChannel.getId());
                            item.setPathImage(DecodeChannelID.decodeIconFromIDChannel(socialChannel.getId()));
                            item.setSpecification(socialChannel.getSpecification());

                            //item.getLinks().forEach((link)->{
                            //    link.setPixel(null);
                            //});

                            String response = new WebJson().jsonRequest("PUT",sb.toString(), item);

                            boolean result = Boolean.valueOf(response);

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {

                                    showLoading(false);
                                    if(result){
                                        Alert d = new Alert(Alert.AlertType.CONFIRMATION);
                                        d.setTitle("STATUS DE ATUALIZAÇÃO");
                                        d.setContentText("Conteudo atualizado com sucesso");
                                        d.showAndWait();

                                        Stage stage = (Stage) etTitleContent.getScene().getWindow();
                                        stage.close();
                                    } else {
                                        Alert d = new Alert(Alert.AlertType.CONFIRMATION);
                                        d.setTitle("STATUS DE ATUALIZAÇÃO");
                                        d.setContentText("Erro durante atualização, tente novamente");
                                        d.showAndWait();
                                    }
                                }
                            });
                        }
                    }
                });
                th.start();
            } else {
                // Message
                UtilDialogManager.showInfoDialog("Informativo", "Necessário preencher preencher os campos!", rootContent.getChildren(), stackPaneContent);
            }
        } else {
            new DialogNoConnection(stackPaneContent).show(true);
        }*/
    }

    public void exit(){
        Stage stage = (Stage) etTitleContent.getScene().getWindow();
        stage.close();
    }

    public void setSocialChannel(ITSocialChannel socialChannel) {
        this.socialChannel = socialChannel;
    }

    public void showLoading(boolean show){
        bpLoading.setVisible(show);
        btnFinish.setDisable(show);
    }


    public ITSocialChannel getSocialChannel() {
        return socialChannel;
    }

    public StackPane getStackPane() {
        return stackPaneContent;
    }

    public void removeLink(int index) {
        item.getLinks().remove(index);
        lvUrls.getItems().remove(index);
    }
}
