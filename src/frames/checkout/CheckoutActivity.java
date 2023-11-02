/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames.checkout;

import Itens.ITPlan;
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
public class CheckoutActivity {
    
    private Stage stage ;
    private CheckoutController controller ;
    
    public CheckoutActivity(Stage rootStage){
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("checkout_activity.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            
            stage = new Stage();
            stage.initOwner(rootStage);
            stage.setScene(scene);
            stage.setTitle("OLink Redirect");
            stage.setResizable(false);
            stage.getIcons().add(new LKImageManager().getImageResource("icon_program.png").getImage());
            stage.setFullScreen(false);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if(OLinkConfiguration.getThreadGroup().activeCount() > 0)OLinkConfiguration.getThreadGroup().interrupt();
            }
        });
            
            controller = loader.getController();
        } catch (IOException ex) {
            Logger.getLogger(CheckoutActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
    public CheckoutActivity(){
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("checkout_activity.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            
            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("OLink Redirect");
            stage.setResizable(false);
            stage.getIcons().add(new LKImageManager().getImageResource("icon_program.png").getImage());
            stage.setFullScreen(false);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if(OLinkConfiguration.getThreadGroup().activeCount() > 0)OLinkConfiguration.getThreadGroup().interrupt();
            }
        });
            
            controller = loader.getController();
        } catch (IOException ex) {
            Logger.getLogger(CheckoutActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void show(boolean andWait){
        if(andWait){
            stage.showAndWait();
        } else {
            stage.show();
        }
    }
    
    public void show(Stage owner, boolean andWait){
        if(andWait){
            stage.showAndWait();
        } else {
            stage.show();
        }
        
        owner.close();
    }

    public void setPlan(ITPlan responsePlan) {
        controller.setPlan(responsePlan);
    }
    
}
