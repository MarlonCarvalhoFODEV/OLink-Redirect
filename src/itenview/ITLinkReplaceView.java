package itenview;

import Itens.ITLinkReplace;
import adapters.AdapterNewLink;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ITLinkReplaceView {

    private Node root ;
    private TextField etUrlReplace, etUrlNew ;
    private Button btnRemove ;
    private ITLinkReplace item ;

    public ITLinkReplaceView(ITLinkReplace item){
        this.item = item ;
        try {
            root = FXMLLoader.load(getClass().getResource("it_rep_url.fxml"));

            this.etUrlReplace = (TextField) root.lookup("#etUrlReplace");
            this.etUrlNew = (TextField) root.lookup("#etUrlNew");
            this.btnRemove = (Button) root.lookup("#btnRemove");

            etUrlReplace.textProperty().addListener((observable, oldValue, newValue) -> {
                item.setCurrentURL(newValue);
            });

            etUrlNew.textProperty().addListener((observable, oldValue, newValue) -> {
                item.setSubstitutueURL(newValue);
            });
        } catch (IOException ex) {
            Logger.getLogger(AdapterNewLink.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public TextField getEtUrlReplace() {
        return etUrlReplace;
    }

    public void setEtUrlReplace(TextField etUrlReplace) {
        this.etUrlReplace = etUrlReplace;
    }

    public TextField getEtUrlNew() {
        return etUrlNew;
    }

    public void setEtUrlNew(TextField etUrlNew) {
        this.etUrlNew = etUrlNew;
    }

    public Button getBtnRemove() {
        return btnRemove;
    }

    public void setBtnRemove(Button btnRemove) {
        this.btnRemove = btnRemove;
    }

    public ITLinkReplace getItem() {
        return item;
    }
}
