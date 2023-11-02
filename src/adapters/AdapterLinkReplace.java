package adapters;

import Itens.ITLink;
import Itens.ITLinkReplace;
import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import frames.account.UserAccount;
import frames.newcontent.NewContentController;
import frames.newlink.NewLinkActivity;
import frames.newlink.NewLinkController;
import itenview.ITLinkReplaceView;
import itenview.ITLinkView;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.json.JSONObject;
import system.OLinkConfiguration;
import urls.UrlsConnetionsWEB;
import util.LKImageManager;
import util.Toast;
import webconnection.DialogNoConnection;

import java.io.IOException;

import static system.OLinkConfiguration.getAccount;


public class AdapterLinkReplace extends ListCell<ITLinkReplace> {

    private NewLinkController controller ;

    public AdapterLinkReplace(ListView<ITLinkReplace> listView, NewLinkController controller) {
        this.controller = controller;
    }

    @Override
    protected void updateItem(ITLinkReplace item, boolean empty) {
        super.updateItem(item, empty);

        ITLinkReplaceView view = new ITLinkReplaceView(item);

        if (item != null && !empty) {

            view.getBtnRemove().setOnAction(event -> {
                controller.removerLinkReplace(getIndex());
            });

            view.getEtUrlReplace().setText(item.getCurrentURL());
            view.getEtUrlNew().setText(item.getSubstitutueURL());

            setGraphic(view.getRoot());
            //item.setPixel(null);
        } else {
            setGraphic(null);
        }
    }

}
