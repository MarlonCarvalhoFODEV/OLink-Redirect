package adapters;

import Itens.ITSocialChannel;
import com.jfoenix.controls.JFXButton;
import frames.dashboard.DialogNewChannel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import util.LKImageManager;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Handler;



public class AdapterSocialChannelv2 extends ListCell<ITSocialChannel> {

    public static final int SITE = 10;
    public static final int SOCIAL_MEDIA = 11;
    public static final int BLOG = 12;
    public static final int YOUTUBE = 13;
    public static final int FACEBOOK = 14;
    public static final int FACEBOOK_GROUP = 15;
    public static final int FACEBOOK_PAGE = 16;
    public static final int TWITTER = 17;
    public static final int WHATSAPP = 18;
    public static final int TELEGRAM = 19;
    public static final int INSTAGRAM = 20;
    public static final int PINTEREST = 21;
    public static final int COMUNICATION = 22;
    public static final int SOFTWARE = 23;

    private Parent root;
    private final ImageView imageView;
    private final JFXButton btnConfig;
    private final Label title, specification;

    private final StackPane stackPane;

    public AdapterSocialChannelv2(StackPane exStackPane) throws IOException {
        stackPane = exStackPane;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/it_channel.fxml"));
        root = loader.load();

        imageView = (ImageView) root.lookup("#icon_it_channel");
        title = (Label) root.lookup("#name_it_channel");
        specification = (Label) root.lookup("#specifc_it_channel");
        btnConfig = (JFXButton) root.lookup("#config_it_channel");

    }

    @Override
    protected void updateItem(ITSocialChannel item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null && !empty) {


            specification.setText(decodeSpecification(item.getSpecification()));
            imageView.setImage(decodeSpecificationImage(item.getSpecification()));
            title.setText(item.getTitle());
            btnConfig.setOnAction(new OnClickconfig(item));

            setGraphic(root);
        } else {
            setGraphic(null);
        }

    }


    public class OnClickconfig implements EventHandler<ActionEvent> {

        private ITSocialChannel channel;

        public OnClickconfig(ITSocialChannel item) {
            channel = item;
        }

        @Override
        public void handle(ActionEvent event) {
            DialogNewChannel dialog = new DialogNewChannel(stackPane, channel);
            dialog.show();
        }
    }

    public static String decodeSpecification(int specification) {

        switch (specification) {
            case SITE:
                return "SITE";
            case SOCIAL_MEDIA:
                return "MEDIA SOCIAL";
            case BLOG:
                return "BLOG";
            case YOUTUBE:
                return "YOUTUBE";
            case FACEBOOK:
                return "FACEBOOK";
            case FACEBOOK_PAGE:
                return "PAGINA FACEBOOK";
            case FACEBOOK_GROUP:
                return "GRUPO FACEBOOK";
            case WHATSAPP:
                return "WHATSAPP";
            case TELEGRAM:
                return "TELEGRAM";
            case INSTAGRAM:
                return "INSTAGRAM";
            case PINTEREST:
                return "PINTEREST";
            case TWITTER:
                return "TWITTER";
            case COMUNICATION:
                return "COMUNICAÇÃO";
            case SOFTWARE:
                return "SOFTWARE";
            default:
                return "SEM ESPECIFICAÇÃO";
        }
    }

    public static Image decodeSpecificationImage(int specification) {

        switch (specification) {
            case SITE:
                return new LKImageManager().getImageResource("site.png").getImage();
            case SOCIAL_MEDIA:
                return new LKImageManager().getImageResource("social_media.png").getImage();
            case BLOG:
                return new LKImageManager().getImageResource("blog.png").getImage();
            case YOUTUBE:
                return new LKImageManager().getImageResource("youtube.png").getImage();
            case FACEBOOK:
                return new LKImageManager().getImageResource("facebook.png").getImage();
            case FACEBOOK_PAGE:
                return new LKImageManager().getImageResource("facebook_page.png").getImage();
            case FACEBOOK_GROUP:
                return new LKImageManager().getImageResource("facebook_group.png").getImage();
            case WHATSAPP:
                return new LKImageManager().getImageResource("whatsapp.png").getImage();
            case TELEGRAM:
                return new LKImageManager().getImageResource("telegram.png").getImage();
            case INSTAGRAM:
                return new LKImageManager().getImageResource("instagram.png").getImage();
            case PINTEREST:
                return new LKImageManager().getImageResource("pinterest.png").getImage();
            case COMUNICATION:
                return new LKImageManager().getImageResource("comunication.png").getImage();
            case TWITTER:
                return new LKImageManager().getImageResource("twitter.png").getImage();
            case SOFTWARE:
                return new LKImageManager().getImageResource("software.png").getImage();
            default:
                return new LKImageManager().getImageResource("coringa.png").getImage();
        }
    }

    public static Image decodeSpecificationImageGray(int specification) {

        switch (specification) {
            case SITE:
                return new LKImageManager().getImageResource("site_gray.png").getImage();
            case SOCIAL_MEDIA:
                return new LKImageManager().getImageResource("social_media_gray.png").getImage();
            case BLOG:
                return new LKImageManager().getImageResource("blog_gray.png").getImage();
            case YOUTUBE:
                return new LKImageManager().getImageResource("youtube_gray.png").getImage();
            case FACEBOOK:
                return new LKImageManager().getImageResource("facebook_gray.png").getImage();
            case FACEBOOK_PAGE:
                return new LKImageManager().getImageResource("facebook_page_gray.png").getImage();
            case FACEBOOK_GROUP:
                return new LKImageManager().getImageResource("facebook_group_gray.png").getImage();
            case WHATSAPP:
                return new LKImageManager().getImageResource("whatsapp_gray.png").getImage();
            case TELEGRAM:
                return new LKImageManager().getImageResource("telegram_gray.png").getImage();
            case INSTAGRAM:
                return new LKImageManager().getImageResource("instagram_gray.png").getImage();
            case PINTEREST:
                return new LKImageManager().getImageResource("pinterest_gray.png").getImage();
            case TWITTER:
                return new LKImageManager().getImageResource("twitter_gray.png").getImage();
            case COMUNICATION:
                return new LKImageManager().getImageResource("comunication_gray.png").getImage();
            case SOFTWARE:
                return new LKImageManager().getImageResource("software_gray.png").getImage();
            default:
                return new LKImageManager().getImageResource("coringa_gray.png").getImage();
        }
    }
}
