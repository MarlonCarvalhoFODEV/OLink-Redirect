/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames.splashscreen;

import system.OLinkConfiguration;
import javafx.application.Application;
import static javafx.application.Application.launch;
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
public class SplashActivity extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception{
            Parent root = FXMLLoader.load(getClass().getResource("splash_activity_v2.fxml"));
            primaryStage.setTitle("OLink Redirect");
            primaryStage.getIcons().add(new LKImageManager().getImageResource("/icon_program.png").getImage());
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.show();
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override 
            public void handle(WindowEvent event) {
                if(OLinkConfiguration.getThreadGroup().activeCount() > 0)OLinkConfiguration.getThreadGroup().interrupt();
            }
        });
    }

    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
