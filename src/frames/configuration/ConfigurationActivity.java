package frames.configuration;

import system.OLinkConfiguration;
import frames.dashboard.DashboardActivity;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import util.LKImageManager;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigurationActivity {

    private Stage stage ;
    private ConfigurationController controller ;

    public ConfigurationActivity(){
        try {
            FXMLLoader anchor = new FXMLLoader(getClass().getResource("config_activity.fxml"));

            Scene scene = new Scene(anchor.load());

            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("OLink Redirect");
            stage.getIcons().add(new LKImageManager().getImageResource("/icon_program.png").getImage());
            stage.setOnCloseRequest((WindowEvent event) -> {
                if(OLinkConfiguration.getThreadGroup().activeCount() > 0)OLinkConfiguration.getThreadGroup().interrupt();
            });

            controller = anchor.getController();
        } catch (
                IOException ex) {
            Logger.getLogger(DashboardActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void show(boolean showAndWait){
        if(showAndWait){
            stage.showAndWait();
        } else {
            stage.show();
        }
    }
}
