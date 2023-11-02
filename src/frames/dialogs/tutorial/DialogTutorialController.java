package frames.dialogs.tutorial;

import Itens.ITTutorialVideo;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import urls.UrlsConnetionsWEB;
import webconnection.DialogNoConnection;
import webconnection.WebJson;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static system.OLinkConfiguration.getSelectedChannel;
import static system.OLinkConfiguration.getThreadGroup;
import static webconnection.WebJson.haveConnection;

public class DialogTutorialController implements Initializable {

    @FXML
    private WebView wvTutorial ;

    @FXML
    private BorderPane progress ;

    @FXML
    private StackPane stackPane ;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadListTutorials();
    }

    private void loadListTutorials(){

        showLoading(true);

        if (haveConnection()) {

            getThreadGroup().interrupt();
            Thread th = new Thread(getThreadGroup(), new Runnable() {
                @Override
                public void run() {
                    StringBuilder sb = new StringBuilder();
                    sb.append(UrlsConnetionsWEB.DOMAIN_CONNECTION);
                    sb.append(UrlsConnetionsWEB.VideoTutorial.TUTORIALS);
                    sb.append(UrlsConnetionsWEB.VideoTutorial.Method.GET_LIST_TUTORIALS);

                    String json = new WebJson().getJson(sb.toString());

                    ArrayList<ITTutorialVideo> videos = new ArrayList<>();
                    if (json != null) {

                        Gson gson = new Gson();
                        JsonArray array = gson.fromJson(json, JsonArray.class);

                        ITTutorialVideo channel = null;
                        for (JsonElement e : array) {
                            ITTutorialVideo c = gson.fromJson(e, ITTutorialVideo.class);

                            if (channel == null)
                                channel = c;
                            videos.add(c);
                        }


                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {

                            loadVideoFromCODE(videos.get(0).getUrl());


                            /*if (getSelectedChannel() != null) {

                                //getContentsFromChannel("", "");
                               // listview.getItems().addAll(0, videos);
                                //listview.getSelectionModel().selectFirst();
                            } else {

                            }*/

                        }
                    });
                }
            });

            th.start();
        } else {
            showLoading(false);
            new DialogNoConnection(stackPane).show(true);
        }

    }

    private void loadVideoFromCODE(String code){

        StringBuilder sb = new StringBuilder();
        sb.append("<iframe width=\"560\" height=\"340\"");
        sb.append("src=\"https://www.youtube.com/embed/");
        sb.append(code);
        sb.append("\" frameborder=\"0\";allow=\"accelerometer; autoplay; encrypted-media; gyroscope;");
        sb.append("picture-in-picture\" allowfullscreen></iframe>");

        StringBuilder sbBody = new StringBuilder();
        sbBody.append("<html>");
        sbBody.append("<head>");
        sbBody.append("</head>");
        sbBody.append("<body>");
        sbBody.append(sb.toString());
        sbBody.append("<body>");
        sbBody.append("</html>");

        wvTutorial.getEngine().loadContent(sbBody.toString(), "text/html");
        wvTutorial.getEngine().getLoadWorker().stateProperty().addListener((ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                showLoading(false);
            }
        });
    }

    private void showLoading(boolean show) {
        Platform.runLater(()->{
            progress.setVisible(show);
        });
    }

    public void stop(){
        wvTutorial.getEngine().loadContent("<html></html>", "text/html");
        wvTutorial.getEngine().getLoadWorker().stateProperty().addListener((ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                showLoading(false);
            }
        });
    }
}
