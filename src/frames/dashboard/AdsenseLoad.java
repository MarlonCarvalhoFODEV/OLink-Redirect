package frames.dashboard;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.util.Timer;
import java.util.TimerTask;

public class AdsenseLoad extends TimerTask {

    private WebView wvRight, wvGhost;
    private Timer timer = new Timer();
    private int timeMills, delayMills=1;

    private String pageUrl ;
    private WebEngine weRight, weGhost;

    public AdsenseLoad(WebView wvRight, WebView wvGhost) {
        this.wvRight = wvRight ;
        this.wvGhost = wvGhost ;
        this.weRight = wvRight.getEngine();
        this.weGhost = wvGhost.getEngine();

        weRight.getLoadWorker().stateProperty().addListener(new AdsenseListener(wvRight));
        weGhost.getLoadWorker().stateProperty().addListener(new AdsenseListener(wvGhost));
    }

    public void setDelayInSeconds(int seconds){
        delayMills = (seconds * 1000);
    }

    public void setIntervalRepeatInSeconds(int seconds){
        timeMills = (seconds * 1000);
    }

    public void stop(){
        timer.cancel();
    }

    public void start(){
        timer.schedule(this, delayMills, timeMills);
    }

    public void setPageURL(String pageUrl){
        this.pageUrl = pageUrl ;
    }

    @Override
    public void run() {
        Platform.runLater(()->{
            weRight.load("about:blank");
            weGhost.load("about:blank");

            weGhost.load(pageUrl);
        });
    }

    public class AdsenseListener implements ChangeListener<Worker.State> {

        private WebView adsenseView ;

        public AdsenseListener(WebView adsenseRight) {
            this.adsenseView = adsenseRight;
        }

        @Override
        public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
            if (newValue == Worker.State.SUCCEEDED) {
                if(adsenseView.getId().equals(wvGhost.getId())) {
                    adsenseView.setVisible(true);
                    weRight.load(pageUrl);
                }
            }
        }
    }
}
