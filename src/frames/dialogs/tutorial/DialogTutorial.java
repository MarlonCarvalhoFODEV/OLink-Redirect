package frames.dialogs.tutorial;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.*;

import java.io.IOException;

public class DialogTutorial {

    private DialogTutorialController controller ;
    private JFXDialog dialog ;

    public DialogTutorial(StackPane stackPane){

        FXMLLoader loader = new FXMLLoader(getClass().getResource("dg_tutorial.fxml"));
        try {
            Parent body = loader.load();
            controller = loader.getController();

            Text txt = new Text("Tutorial");
            txt.setFont(Font.font("arial", FontWeight.NORMAL, FontPosture.REGULAR, 18));
            txt.setTextAlignment(TextAlignment.CENTER);

            JFXDialogLayout layout = new JFXDialogLayout();
            layout.setHeading(txt);
            layout.setBody(body);

            stackPane.setVisible(true);

            dialog = new JFXDialog(stackPane, layout, JFXDialog.DialogTransition.CENTER);
            dialog.setOnDialogClosed(new EventHandler() {
                @Override
                public void handle(Event event) {
                    stackPane.setVisible(false);
                    controller.stop();
                }
            });
            //layout.setActions(btnYes, btnDont);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void show(){
        dialog.show();
        System.out.println("SHOW DIALOG");
    }

    public void dismiss(){
        dialog.close();
    }

}
