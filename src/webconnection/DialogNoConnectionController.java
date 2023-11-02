package webconnection;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DialogNoConnectionController implements Initializable {

    @FXML
    private JFXButton btnFinish ;

    private JFXDialog dialog ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnFinish.setOnMouseClicked((event)->{
            finish();
        });
    }

    private void finish(){
        ((Stage)dialog.getScene().getWindow()).close();
    }

    public void setDialog(JFXDialog dialog) {
        this.dialog = dialog ;
    }
}
