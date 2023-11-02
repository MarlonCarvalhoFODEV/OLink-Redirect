package frames.dashboard;

import Itens.ITPartner;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import urls.UrlsConnetionsWEB;
import util.LKImageManager;
import webconnection.DialogNoConnection;
import webconnection.WebJson;

import java.net.URL;
import java.util.ResourceBundle;

import static system.OLinkConfiguration.getAccount;
import static system.OLinkConfiguration.getThreadGroup;
import static webconnection.WebJson.haveConnection;

public class PartnerController implements Initializable {

    @FXML
    private ImageView ivLogoDashboard ;
    @FXML
    private StackPane spDashboard ;

    @FXML
    private Label lbRegisters, lbSubscribers, lbConversions, lbCancels, lbRevenue, lbRevenueMonthly;
    @FXML
    private BorderPane panelGeneral, panelPartner ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        checkIsPartner();
    }

    public void checkIsPartner() {
        if(getAccount().isPartner()){

            ivLogoDashboard.setImage(new LKImageManager().getImageResource("partner.png").getImage());
            panelGeneral.setBottom(panelPartner);

            if (haveConnection()) {
                Thread th = new Thread(getThreadGroup(),()->{
                    StringBuilder sb = new StringBuilder();
                    sb.append(UrlsConnetionsWEB.DOMAIN_CONNECTION);
                    sb.append(UrlsConnetionsWEB.PartnerManager.PARTNER);
                    sb.append(UrlsConnetionsWEB.PartnerManager.Methods.GET_PANEL_INF);
                    sb.append(getAccount().getId());

                    String json = new WebJson().getJson(sb.toString());

                    ITPartner partner = new Gson().fromJson(json, ITPartner.class);

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {

                            if (partner != null) {

                                lbRegisters.setText("Registros ("+partner.getRegisters()+")");
                                lbSubscribers.setText("Assinantes ("+partner.getSubscribers()+")");
                                lbConversions.setText("Conversão ("+partner.getConversions()+"% )");
                                lbCancels.setText("Cancelados ("+partner.getCancels()+")");
                                lbRevenue.setText("Receita Gerada R$ "+partner.getRevenue()+"");
                                lbRevenueMonthly.setText("Receita Prevista/mês R$ "+partner.getRevenueMonthly()+"");                    }

                        }
                    });
                });
                th.start();
            } else {
                new DialogNoConnection(spDashboard).show(true);
            }
        } else {
            panelGeneral.getChildren().remove(panelPartner);
        }
    }

    public ImageView getIvLogoDashboard() {
        return ivLogoDashboard;
    }

    public StackPane getSpDashboard() {
        return spDashboard;
    }
}
