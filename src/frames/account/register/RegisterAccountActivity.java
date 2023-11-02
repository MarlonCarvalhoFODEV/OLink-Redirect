package frames.account.register;


import system.OLinkConfiguration;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import util.LKImageManager;

import java.io.IOException;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

public class RegisterAccountActivity {

    private Stage stage ;
    private RegisterAccountController controller ;

    public RegisterAccountActivity(Stage owner) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("register_account.fxml"));

        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StackPane layout = new StackPane();
        layout.getChildren().add(root);

        Scene scene = new Scene(layout);
        stage = new Stage();
        stage.getIcons().add(new LKImageManager().getImageResource("icon_program.png").getImage());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initOwner(owner);
        stage.setTitle("OLink Redirect - NOVO REGISTRO");
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if(OLinkConfiguration.getThreadGroup().activeCount() > 0)OLinkConfiguration.getThreadGroup().interrupt();
            }
        });

        controller = loader.getController();
    }

    public void show(boolean wait){
        if(wait){
            stage.showAndWait();
        } else {
            stage.show();
        }
    }

    public RegisterAccountController getController() {
        return controller;
    }


}
