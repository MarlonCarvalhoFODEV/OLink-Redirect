package webconnection;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import system.OLinkConfiguration;
import urls.UrlsConnetionsWEB;

import java.io.IOException;

public class DialogNoConnection {

    private JFXDialog dialog ;
    private DialogNoConnectionController controller;

    public DialogNoConnection(StackPane stackPane){
        stackPane.setVisible(true);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dg_no_connection.fxml"));
            AnchorPane root = loader.load();
            dialog = new JFXDialog(stackPane,root, JFXDialog.DialogTransition.CENTER);
            dialog.setOnDialogClosed((event)->{
                stackPane.setVisible(false);
            });
            dialog.show();


            controller = loader.getController();
            controller.setDialog(dialog);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void show(boolean show){
        if(show){
            dialog.show();
        } else {
            dialog.close();
        }
    }

    public DialogNoConnectionController getController() {
        return controller;
    }
}
