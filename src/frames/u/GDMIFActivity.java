/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames.u;

import Itens.ITRequestPassword;
import system.OLinkConfiguration;
import frames.dashboard.DashboardActivity;
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
public class GDMIFActivity {
    
    private GDMIFController controller ;
    private Stage stage ;
    
    public GDMIFActivity(Stage parentStage){
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("password_update.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            
            stage = new Stage();
            stage.setTitle("Redefinição de Senha");
            stage.getIcons().add(new LKImageManager().getImageResource("/icon_program.png").getImage());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initOwner(parentStage);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override 
                public void handle(WindowEvent event) {
                    if(OLinkConfiguration.getThreadGroup().activeCount() > 0)OLinkConfiguration.getThreadGroup().interrupt();
                }
            });   
            
            controller = loader.getController();
        } catch (IOException ex) {
            Logger.getLogger(DashboardActivity.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
    public void show(boolean andWait){
        if(andWait){
            stage.showAndWait();
        } else {
            stage.show();
        }
    }

    public void setResponseRequest(ITRequestPassword response) {
        controller.setResponse(response);
    }
}
