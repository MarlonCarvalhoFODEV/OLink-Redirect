package frames.newlink;

import Itens.ITLink;
import system.OLinkConfiguration;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import util.LKImageManager;

import java.io.IOException;

public class NewLinkActivity {

    private final Stage stage ;
    private NewLinkController controller ;

    public NewLinkActivity(Stage parentStage) throws IOException {
        controller = new NewLinkController();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("link2.fxml"));
        loader.setController(controller);

        Parent root = loader.load();

        StackPane layout = new StackPane();
        layout.getChildren().add(root);
 
        Scene scene = new Scene(layout);
        
        stage = new Stage();
        stage.initOwner(parentStage);
        stage.setScene(scene);
        stage.getIcons().add(new LKImageManager().getImageResource("icon_program.png").getImage());
        stage.setTitle("Novo Link");
        stage.setResizable(false);
        stage.setOnCloseRequest(event -> {
            if (OLinkConfiguration.getThreadGroup().activeCount() > 0)
                OLinkConfiguration.getThreadGroup().interrupt();
        });

        controller = loader.getController();
    }

    public NewLinkActivity(Stage parentStage, ITLink link) throws IOException {

        controller = new NewLinkController(link);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("link2.fxml"));
        loader.setController(controller);

        Parent root = loader.load();

        StackPane layout = new StackPane();
        layout.getChildren().add(root);

        Scene scene = new Scene(layout);

        stage = new Stage();
        stage.initOwner(parentStage);
        stage.setScene(scene);
        stage.getIcons().add(new LKImageManager().getImageResource("icon_program.png").getImage());
        stage.setTitle("Novo Link");
        stage.setResizable(false);
        stage.setOnCloseRequest(event -> {
            if (OLinkConfiguration.getThreadGroup().activeCount() > 0)
                OLinkConfiguration.getThreadGroup().interrupt();
        });

    }

    public void show(){
        stage.showAndWait();
    }



    public NewLinkController getController() {
        return controller;
    }
}
