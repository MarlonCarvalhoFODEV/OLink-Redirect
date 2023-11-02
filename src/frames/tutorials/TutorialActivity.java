package frames.tutorials;

import frames.checkout.CheckoutActivity;
import frames.graphic.GraphicActivity;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import system.OLinkConfiguration;
import util.LKImageManager;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TutorialActivity {

    private Stage stage ;
    private TutorialController controller ;

    public TutorialActivity() throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("tutorial_activity.fxml"));

            Parent root = loader.load();
            Scene scene = new Scene(root);

            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("OLink Redirect");
            //stage.initOwner(primaryStage);
            stage.getIcons().add(new LKImageManager().getImageResource("/icon_program.png").getImage());
            stage.setResizable(false);
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

    public void show(boolean andWait){
        if(andWait){
            stage.showAndWait();
        } else {
            stage.show();
        }
    }
}
