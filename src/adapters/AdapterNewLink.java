package adapters;

import Itens.ITLink;
import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import frames.account.UserAccount;
import itenview.ITLinkView;
import org.json.JSONObject;
import system.OLinkConfiguration;
import frames.newcontent.NewContentController;
import frames.newlink.NewLinkActivity;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import urls.UrlsConnetionsWEB;
import util.LKImageManager;
import util.Toast;
import webconnection.DialogNoConnection;
import webconnection.WebJson;

import static system.OLinkConfiguration.getAccount;


public class AdapterNewLink extends ListCell<ITLink> {

    /*private Node root ;
    private ImageView icon, ivConversion, ivPixel, ivPixelEvent, ivAB, ivLC;
    private Label title, fromLink, toLink ;
    private Button btnTrash, btnEdit ;*/

    private Stage stage;
    private NewContentController controller;

    /*
        public AdapterNewLink(){

            try {
                root = FXMLLoader.load(getClass().getResource("it_link.fxml"));

                this.icon = (ImageView) root.lookup("#ivIcon");
                this.ivConversion = (ImageView) root.lookup("#ivConversion");
                this.ivPixel = (ImageView) root.lookup("#ivPixel");
                this.ivPixelEvent = (ImageView) root.lookup("#ivPixelEvent");
                this.ivAB = (ImageView) root.lookup("#ivAB");
                this.ivLC = (ImageView) root.lookup("#ivLC");

                this.icon.setImage(new LKImageManager().getImageResource("link.png", 25).getImage());
                this.ivConversion.setImage(new LKImageManager().getImageResource("icon_close.png", 15).getImage());
                this.ivPixel.setImage(new LKImageManager().getImageResource("icon_close.png", 15).getImage());
                this.ivPixelEvent.setImage(new LKImageManager().getImageResource("icon_close.png", 15).getImage());

                this.title = (Label) root.lookup("#txLinkName");
                this.fromLink = (Label) root.lookup("#txLinkFrom");
                this.toLink = (Label) root.lookup("#txLinkTo");
                this.btnTrash = (Button) root.lookup("#btnDelLink");
                this.btnEdit = (Button) root.lookup("#btnEditLink");
                this.btnEdit.setGraphic(new LKImageManager().getImageResource("icon_edit_degrade.png", 25));
                this.btnTrash.setGraphic(new LKImageManager().getImageResource("icon_crash_degrade.png", 25));

            } catch (IOException ex) {
                Logger.getLogger(AdapterNewLink.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    */
    public AdapterNewLink(NewContentController controller) {
        this.controller = controller;
        if(controller != null)
        this.stage = (Stage) controller.getStackPane().getScene().getWindow();
/*
        try {
            root = FXMLLoader.load(getClass().getResource("it_link.fxml"));

            this.icon = (ImageView) root.lookup("#ivIcon");
            this.ivConversion = (ImageView) root.lookup("#ivConversion");
            this.ivPixel = (ImageView) root.lookup("#ivPixel");
            this.ivPixelEvent = (ImageView) root.lookup("#ivPixelEvent");
            this.ivAB = (ImageView) root.lookup("#ivAB");
            this.ivLC = (ImageView) root.lookup("#ivLC");

            this.icon.setImage(new LKImageManager().getImageResource("link.png", 25).getImage());
            this.ivConversion.setImage(new LKImageManager().getImageResource("icon_close.png", 15).getImage());
            this.ivPixel.setImage(new LKImageManager().getImageResource("icon_close.png", 15).getImage());
            this.ivPixelEvent.setImage(new LKImageManager().getImageResource("icon_close.png", 15).getImage());

            this.title = (Label) root.lookup("#txLinkName");
            this.fromLink = (Label) root.lookup("#txLinkFrom");
            this.toLink = (Label) root.lookup("#txLinkTo");
            this.btnTrash = (Button) root.lookup("#btnDelLink");
            this.btnEdit = (Button) root.lookup("#btnEditLink");
            this.btnEdit.setGraphic(new LKImageManager().getImageResource("icon_edit_degrade.png", 25));
            this.btnTrash.setGraphic(new LKImageManager().getImageResource("icon_crash_degrade.png", 25));

        } catch (IOException ex) {
            Logger.getLogger(AdapterNewLink.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

    @Override
    protected ObservableList<Node> getChildren() {
        return super.getChildren();
    }

    @Override
    protected void updateItem(ITLink item, boolean empty) {
        super.updateItem(item, empty);

        ITLinkView view = new ITLinkView();

        if (item != null && !empty) {

            view.getIcon().setImage(new LKImageManager().getImageResource("/link.png").getImage());
            view.getIcon().setFitWidth(30);
            view.getIcon().setFitHeight(30);

            if (item.getValue() > 0)
                view.getIvConversion().setImage(new LKImageManager().getImageResource("icon_check.png", 15).getImage());
            if (item.getPixel() != null && item.getPixel().getCode() != null && !item.getPixel().getCode().isEmpty() || item.getPixel() != null && item.getPixel().getEvent() != null)
                view.getIvPixel().setImage(new LKImageManager().getImageResource("icon_check.png", 15).getImage());
            if (item.getPixel() != null && item.getPixel().getEvent() != null && !item.getPixel().getEvent().isEmpty())
                view.getIvPixelEvent().setImage(new LKImageManager().getImageResource("icon_check.png", 15).getImage());
            if (item.getTesteAB() != null)
                view.getIvAB().setImage(new LKImageManager().getImageResource("icon_check.png", 15).getImage());
            if (item.getLaunch() != null)
                view.getIvLC().setImage(new LKImageManager().getImageResource("icon_check.png", 15).getImage());

            view.getTitle().setText(item.getLinkName());
            view.getFromLink().setText("DE : " + UrlsConnetionsWEB.DOMAIN_CONNECTION + item.getFromLink());

            if (item.getTesteAB() != null) {
                view.getToLink().setText("PARA : " + item.getTesteAB() != null ? "2 Destinos (url)" : item.getToLink());
            } else if (item.getLaunch() != null) {
                view.getToLink().setText("PARA : " + item.getLaunch() != null ? "3 Destinos (url)" : item.getToLink());
            } else if (item.getLaunch() != null && item.getTesteAB() != null) {
                view.getToLink().setText("PARA : " + item.getLaunch() != null && item.getTesteAB() != null ? "A/B + Lançamento" : item.getToLink());
            } else {
                view.getToLink().setText("PARA : " + item.getToLink());
            }

            view.getBtnTrash().setOnAction(event -> {
                showConfirmDialog();
            });

            view.getBtnEdit().setOnAction(event -> {
                updateLink();
            });

            setGraphic(view.getRoot());

            //item.setPixel(null);
        } else {
            setGraphic(null);
        }
    }

    private void removeLink() {
        controller.removeLink(getIndex());

        ITLink link =  getListView().getItems().get(getIndex());

        if(link. getId() > 0){
            controller.showLoading(true);
            UserAccount ac = getAccount();

            JSONObject secretData = new JSONObject();
            secretData.put("idu", ac.getId());
            secretData.put("oip", ac.getSec().getIpPublicFirst());
            secretData.put("mac", ac.getSec().getMac());
            secretData.put("sid", ac.getSec().getSystemId());
            secretData.put("idc", link.getIdChannel());
            secretData.put("idt", link.getIdContent());
            secretData.put("idl", link.getId());

            InternetRequisitionAPI requestServer = OLinkConfiguration.getApiRequisition();
            requestServer.setIdRequisition("removeLink");
            requestServer.setTypeRequest(InternetRequisition.TypeRequest.DELETE);
            requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.OTHER_RSA);
            requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
            requestServer.setDomain(UrlsConnetionsWEB.DOMAIN_CONNECTION);
            requestServer.setController(UrlsConnetionsWEB.ContentManager.Link.LINKS);
            requestServer.setFunction(UrlsConnetionsWEB.ContentManager.Link.Method.DELETE_LINK);
            requestServer.setObjectTransport(secretData);
            requestServer.request(new InternetRequisitionResponse() {
                @Override
                public void onLoading(boolean loading) {
                    controller.showLoading(loading);
                }

                @Override
                public void onSucess(Object object) {
                    if (object != null) {

                        JSONObject response = new JSONObject(object.toString());
                        response = response.getJSONObject("map");

                        boolean result = response.getBoolean("result");

                        if (result) {
                            controller.showLoading(false);
                            Toast.makeText(stage, "Excluído com sucesso!", 0, 500, 2000);
                            getListView().getItems().remove(getIndex());
                        }
                    }
                }

                @Override
                public void onError(Exception e) {

                    e.printStackTrace();
                }

                @Override
                public void notConnectionInternet() {
                    new DialogNoConnection(controller.getStackPane()).show(true);
                }
            });
        }

/*
        Thread th = new Thread(OLinkConfiguration.getThreadGroup(), () -> {
            StringBuilder sb = new StringBuilder();
            sb.append(UrlsConnetionsWEB.DOMAIN_CONNECTION);
            sb.append(UrlsConnetionsWEB.ContentManager.Link.LINKS);
            sb.append(UrlsConnetionsWEB.ContentManager.Link.Method.DELETE_LINK);

            //if(haveConnection()){
            String json = new WebJson().jsonRequest("DELETE", sb.toString(), getListView().getItems().get(getIndex()));
            boolean sucess = new Gson().fromJson(json, Boolean.class);
            if (sucess) {
                Platform.runLater(() -> {
                    controller.showLoading(false);
                    Toast.makeText(stage, "Excluído com sucesso!", 0, 500, 2000);
                    getListView().getItems().remove(getIndex());
                });
            }
            //} else {
            //   new DialogNoConnection(stackPane).show(true);
            //}
        });
        th.start();*/
    }

    private void updateLink() {
        try {
            ITLink item = getListView().getItems().get(getIndex());
            controller.setItemPositionEdit(getIndex());

            NewLinkActivity activity = new NewLinkActivity(stage, item);
            activity.getController().setSocialChannel(controller.getSocialChannel());
            activity.getController().setSocialContenr(controller.getContent());
            activity.show();


            controller.crateLink(activity);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showConfirmDialog() {
        controller.getStackPane().setVisible(true);
        JFXDialogLayout layout = new JFXDialogLayout();

        Label label = new Label("Deseja excluir este Link?");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        layout.setBody(label);

        JFXDialog dialog = new JFXDialog(controller.getStackPane(), layout, JFXDialog.DialogTransition.CENTER);
        dialog.setOnDialogClosed((event -> {
            controller.getStackPane().setVisible(false);
        }));

        JFXButton confirm = new JFXButton("SIM, EXCLUIR");
        confirm.setOnMouseClicked(event -> {
            dialog.close();
            removeLink();
        });
        JFXButton cancel = new JFXButton("CANCEL");
        cancel.setOnMouseClicked(event -> {
            dialog.close();
        });
        layout.setActions(confirm, cancel);

        dialog.show();
    }
}
