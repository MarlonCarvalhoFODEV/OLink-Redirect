/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames.graphic;

import com.jfoenix.controls.JFXDialog;
import frames.dashboard.DialogNewChannel;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Marlon
 */
public class DialogGraphic {
    
    private JFXDialog dialog ;
    
    public DialogGraphic(StackPane stackPane, int idChannel){
        
        try {
            stackPane.setVisible(true);
            stackPane.setBackground(Background.EMPTY);
            
            FXMLLoader loader = new FXMLLoader(GraphicActivity.class.getResource("dg_graphic.fxml"));

            dialog = new JFXDialog(stackPane,loader.load(), JFXDialog.DialogTransition.CENTER);
            dialog.setPadding(new Insets(10,10,10,10));
            dialog.setOnDialogClosed((Event event) -> {
                stackPane.setVisible(false);
            });
            
            DialogGraphicController controller = loader.getController();
            controller.setGraphicController((GraphicController) DialogNewChannel.getController(stackPane));
            controller.setDialog(dialog);
            controller.setIdChannel(idChannel);
            
        } catch (IOException ex) {
            Logger.getLogger(DialogGraphic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void show(){
        dialog.show();
    }
}
