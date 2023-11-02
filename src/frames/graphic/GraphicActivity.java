/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames.graphic;

import system.OLinkConfiguration;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import util.LKImageManager;

/**
 *
 * @author Marlon
 */
public class GraphicActivity {
    
    private Stage stage ; 
    private GraphicController controller ;
    
    
    public GraphicActivity(Stage ownerStage){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("graphic_activity.fxml"));
            
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("OLink Redirect");
            stage.initOwner(ownerStage);
            stage.getIcons().add(new LKImageManager().getImageResource("/icon_program.png").getImage());
            stage.setResizable(true);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    if(OLinkConfiguration.getThreadGroup().activeCount() > 0){
                        OLinkConfiguration.getThreadGroup().interrupt();
                    }
                }
            });
            
            controller = loader.getController();
        } catch (IOException ex) {
            Logger.getLogger(GraphicActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public GraphicController getController() {
        return controller;
    }
    
    public void show(boolean showAndWait){
        if(showAndWait){
            stage.showAndWait();
        } else {
            stage.show();
        }
    }
}
