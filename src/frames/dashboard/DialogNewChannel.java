/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames.dashboard;

import Itens.ITSocialChannel;
import com.jfoenix.controls.JFXDialog;
import system.OLinkConfiguration;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Marlon
 */
public class DialogNewChannel {
    
    private JFXDialog dialog ;
    private DialogNewChannelController controller ;
    
    public DialogNewChannel(StackPane stackPane){
        stackPane.setVisible(true);
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dg_new_channel.fxml"));
            AnchorPane root = loader.load();
            
            dialog = new JFXDialog(stackPane, root, JFXDialog.DialogTransition.CENTER);
            dialog.setOnDialogClosed((Event event) -> {
                stackPane.setVisible(false);
                if(OLinkConfiguration.getThreadGroup().activeCount() > 0)OLinkConfiguration.getThreadGroup().interrupt();
            });
            
            FXMLLoader lc = (FXMLLoader) stackPane.getParent().getUserData();
            
            controller = loader.getController();
            controller.setDialog(dialog);
            controller.setDashboardController((DashboardController) getController(stackPane));
        } catch (IOException ex) {
            Logger.getLogger(DialogNewChannel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public DialogNewChannel(StackPane stackPane, ITSocialChannel item){
        stackPane.setVisible(true);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dg_new_channel.fxml"));
            AnchorPane root = loader.load();

            dialog = new JFXDialog(stackPane, root, JFXDialog.DialogTransition.CENTER);
            dialog.setOnDialogClosed((Event event) -> {
                stackPane.setVisible(false);
                if(OLinkConfiguration.getThreadGroup().activeCount() > 0)OLinkConfiguration.getThreadGroup().interrupt();
            });

            FXMLLoader lc = (FXMLLoader) stackPane.getParent().getUserData();

            controller = loader.getController();
            controller.setDialog(dialog);
            controller.setChannel(item);
            controller.setDashboardController((DashboardController) getController(stackPane));
        } catch (IOException ex) {
            Logger.getLogger(DialogNewChannel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void show(){
        dialog.show();
    }
    
    public static Object getController(Node node) {
        Object controller = null;
        do {
            controller = node.getUserData();
            node = node.getParent();
        } while (controller == null && node != null);
        return controller;
    }
}
