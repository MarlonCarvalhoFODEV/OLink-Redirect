/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames.plans;

import system.OLinkConfiguration;
import frames.newlink.NewLinkController;
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
public class PlansActivity {
    
    private Stage stage ;
    private PlansController controller ;
    
    public PlansActivity(Stage stageParent){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("plans.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            
            stage = new Stage();
            stage.initOwner(stageParent);
            stage.setScene(scene);
            stage.setTitle("Planos");
            stage.setFullScreen(false);
            stage.setResizable(false);
            stage.getIcons().add(new LKImageManager().getImageResource("icon_program.png").getImage());
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if(OLinkConfiguration.getThreadGroup().activeCount() > 0)OLinkConfiguration.getThreadGroup().interrupt();
                }
            });

            controller = loader.getController();
        } catch (IOException ex) {
            Logger.getLogger(PlansActivity.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    public PlansActivity(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("plans.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            
            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Planos");
            stage.setFullScreen(false);
            stage.setResizable(false);
            stage.getIcons().add(new LKImageManager().getImageResource("icon_program.png").getImage());
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if(OLinkConfiguration.getThreadGroup().activeCount() > 0)OLinkConfiguration.getThreadGroup().interrupt();
                }
            });

            controller = loader.getController();
        } catch (IOException ex) {
            Logger.getLogger(PlansActivity.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    public void show(boolean showAndWait){
        if(showAndWait){
            stage.showAndWait();
        } else {
            stage.show();
        }
    }
    
    public void show(Stage ownerStage, boolean showAndWait){
        if(showAndWait){
            stage.showAndWait();
        } else {
            stage.show();
        }
        ownerStage.close();
    }
    
    private void sleeping(int timeMills){
        try {
            Thread.sleep(timeMills);
        } catch (InterruptedException ex) {
            Logger.getLogger(NewLinkController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
