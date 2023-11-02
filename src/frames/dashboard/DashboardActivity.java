package frames.dashboard;

import system.OLinkConfiguration;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import util.LKImageManager;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import static webconnection.WebJson.closeApplication;

public class DashboardActivity {

    private Stage stage ;
    private DashboardController controller ;
    
    public DashboardActivity() {
        try {
            FXMLLoader anchor = new FXMLLoader(getClass().getResource("dashboard.fxml"));
            
            Scene scene = new Scene(anchor.load());
            
            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("OLink Redirect");
            stage.getIcons().add(new LKImageManager().getImageResource("/icon_program.png").getImage());
            stage.setMaximized(false);
            stage.setOnCloseRequest((WindowEvent event) -> {
                if(OLinkConfiguration.getThreadGroup().activeCount() > 0)OLinkConfiguration.getThreadGroup().interrupt();
                closeApplication();
            });
            
            controller = anchor.getController();
        } catch (IOException ex) {
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
