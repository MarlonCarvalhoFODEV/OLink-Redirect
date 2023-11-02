package frames.newcontent;

import Itens.ITSocialChannelContent;
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

public class NewContentActivity {

    private Stage stage ;
    private NewContentController controller ;

    public NewContentActivity(Stage parentStage) throws IOException {

        controller = new NewContentController();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("newcontent.fxml"));
        loader.setController(controller);

        Parent root = loader.load();

        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(root);

        Scene scene = new Scene(secondaryLayout, 488, 500);

        stage = new Stage();
        stage.setTitle("Novo Conteudo");
        stage.getIcons().add(new LKImageManager().getImageResource("icon_program.png").getImage());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initOwner(parentStage);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if(OLinkConfiguration.getThreadGroup().activeCount() > 0)OLinkConfiguration.getThreadGroup().interrupt();
            }
        });


    }

    public NewContentActivity(Stage parentStage, ITSocialChannelContent content) throws IOException {

        controller = new NewContentController(content);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("newcontent.fxml"));
        loader.setController(controller);

        Parent root = loader.load();

        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(root);

        Scene scene = new Scene(secondaryLayout, 488, 500);

        stage = new Stage();
        stage.setTitle("Novo Conteudo");
        stage.getIcons().add(new LKImageManager().getImageResource("icon_program.png").getImage());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initOwner(parentStage);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if(OLinkConfiguration.getThreadGroup().activeCount() > 0)OLinkConfiguration.getThreadGroup().interrupt();
            }
        });

    }

    public NewContentActivity(ITSocialChannelContent item) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("newcontent.fxml"));

        Parent root = loader.load();

        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(root);

        Scene scene = new Scene(secondaryLayout, 400, 537);

        stage = new Stage();
        stage.setTitle("Novo Conteudo");
        stage.setScene(scene);

        controller = loader.getController();
        controller.setContent(item);
    }

    public void show(){
        stage.showAndWait();
    }

    public NewContentController getController() {
        return controller;
    }
}
