package frames.login;

import system.OLinkConfiguration;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import util.LKImageManager;

/**
 *
 * @author Marlon
 */
public class LoginActivity {

    private Stage stage ;
   
    public LoginActivity(){
        try {
            FXMLLoader anchor = new FXMLLoader(getClass().getResource("login_activity.fxml"));

            Scene scene = new Scene(anchor.load());

            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("OLink Redirect");
            stage.getIcons().add(new LKImageManager().getImageResource("/icon_program.png").getImage());
            stage.setMaximized(false);
            stage.setResizable(false);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                   if(OLinkConfiguration.getThreadGroup().activeCount() > 0)OLinkConfiguration.getThreadGroup().interrupt();
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void show(boolean wait){
        if(wait){
            stage.showAndWait();
        } else {
            stage.show();
        }
    }
 
}
