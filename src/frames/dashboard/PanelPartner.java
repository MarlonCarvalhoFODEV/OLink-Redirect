package frames.dashboard;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PanelPartner {

    private Node node ;
    private PartnerController controller ;

    public PanelPartner() {
        try {
            FXMLLoader anchor = new FXMLLoader(getClass().getResource("panel_partner.fxml"));

            node = anchor.load();

            controller = anchor.getController();
        } catch (IOException ex) {
            Logger.getLogger(DashboardActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Node getNode() {
        return node;
    }

    public PartnerController getController() {
        return controller;
    }
}
