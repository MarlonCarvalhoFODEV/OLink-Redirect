package frames.tutorials;

import Itens.ITSocialChannel;
import Itens.ITTutorialVideo;
import adapters.AdapterSocialChannel;
import adapters.AdapterVideoTutorial;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Callback;
import urls.UrlsConnetionsWEB;
import util.Toast;
import webconnection.DialogNoConnection;
import webconnection.WebJson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static system.OLinkConfiguration.*;
import static webconnection.WebJson.*;

public class TutorialController implements Initializable {

    private MediaPlayer mediaPlayer ;
    private MediaView mediaView ;

    @FXML
    private WebView webview ;

    @FXML
    private ListView<ITTutorialVideo> listview ;

    @FXML
    private StackPane stackPane ;

    @FXML
    private BorderPane progress ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        listview.setCellFactory(new Callback<ListView<ITTutorialVideo>, ListCell<ITTutorialVideo>>() {
            @Override
            public ListCell<ITTutorialVideo> call(ListView<ITTutorialVideo> param) {
                return new AdapterVideoTutorial();
            }
        });
        listview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ITTutorialVideo>() {
            @Override
            public void changed(ObservableValue<? extends ITTutorialVideo> observable, ITTutorialVideo oldValue, ITTutorialVideo newValue) {
               loadVideoFromURL(newValue.getUrl(), webview.getWidth(), webview.getHeight());
            }
        });

        loadListTutorials();
    }

    private void showLoading(boolean show) {
        Platform.runLater(()->{
            progress.setVisible(show);
        });
    }

    private void loadListTutorials(){

        showLoading(true);

        if (haveConnection()) {
            listview.getItems().clear();

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

                            if (getSelectedChannel() != null) {
                                //getContentsFromChannel("", "");
                                listview.getItems().addAll(0, videos);
                                listview.getSelectionModel().selectFirst();
                            } else {

                            }

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


    private void loadVideoFromURL(String code, double width, double height){
        StringBuilder sb = new StringBuilder();
        sb.append("<iframe width=\"");
        sb.append(width);
        sb.append(" \"");
        sb.append("\"");
        sb.append(height);
        sb.append("\" src=\"http://www.youtube.com/embed/");
        sb.append(code);
        sb.append("\"");
        sb.append("frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" ");
        sb.append("allowfullscreen></iframe>");

        StringBuilder sbBody = new StringBuilder();
        sbBody.append("<html>");
        sbBody.append("<head>");
        sbBody.append("</head>");
        sbBody.append("<body>");
        sbBody.append(sb.toString());
        sbBody.append("<body>");
        sbBody.append("</html>");

        webview.getEngine().loadContent(sbBody.toString(), "text/html");
        webview.getEngine().getLoadWorker().stateProperty().addListener((ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                showLoading(false);

            }
        });
    }

    private void loadMedia(String webUrl) throws IOException {

        URL url = new URL(webUrl);

        Path video = Files.createTempFile("now-playing", ".mp4");

        try (InputStream stream = url.openStream()) {
            Files.copy(stream, video, StandardCopyOption.REPLACE_EXISTING);
        }

        Media media = new Media(video.toFile().toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaView = new MediaView(mediaPlayer);
        mediaView.setSmooth(true);
    }

    private void loadMedia(File file) throws MalformedURLException {
        Media media = new Media(file.toURI().toURL().toExternalForm());

        mediaPlayer = new MediaPlayer(media);
        mediaView = new MediaView(mediaPlayer);

    }

    private void play(){
        if(mediaPlayer != null){
            mediaPlayer.play();
        } else {

        }
    }

    private void stop(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
        } else {

        }
    }

    private void pause(){
        if(mediaPlayer != null){
            mediaPlayer.pause();
        } else {

        }
    }

}
