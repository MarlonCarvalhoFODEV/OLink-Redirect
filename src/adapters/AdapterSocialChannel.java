package adapters;

import Itens.ITSocialChannel;
import apis.internet.WebJson;
import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import util.LKImageManager;


public class AdapterSocialChannel extends ListCell<ITSocialChannel> {

    public static final int SITE = 10 ;
    public static final int SOCIAL_MEDIA = 11 ;
    public static final int BLOG = 12 ;
    public static final int YOUTUBE = 13 ;
    public static final int FACEBOOK = 14 ;
    public static final int FACEBOOK_GROUP = 15 ;
    public static final int FACEBOOK_PAGE = 16 ;
    public static final int TWITTER = 17 ;
    public static final int WHATSAPP = 18 ;
    public static final int TELEGRAM = 19 ;
    public static final int INSTAGRAM = 20 ;
    public static final int PINTEREST = 21 ;
    public static final int COMUNICATION = 22 ;
    public static final int SOFTWARE = 23 ;
    
    private final HBox rootPanel ;
    private final VBox vertical ;
    private final ImageView imageView ;
    private final Text title, specification;

    public AdapterSocialChannel(){

        imageView = new ImageView();
        imageView.setFitWidth(25);
        imageView.setFitHeight(25);
        imageView.setSmooth(true);
        imageView.setPreserveRatio(true);

        title = new Text();
        specification = new Text();
        specification.setFont(Font.font("arial",FontWeight.NORMAL,10));
        specification.setFill(Paint.valueOf("#999999"));

        vertical = new VBox(title, specification);
        vertical.setAlignment(Pos.CENTER_LEFT);

        rootPanel = new HBox(imageView, vertical);
        rootPanel.setAlignment(Pos.CENTER_LEFT);
        rootPanel.setSpacing(10);
        rootPanel.setPrefHeight(50);
        rootPanel.setMinHeight(50);
        rootPanel.setPadding(new Insets(0,0,0,8));
        rootPanel.setStyle("-fx-background-radius:50; -fx-background-color: white; -fx-border-color: gainsboro; -fx-border-radius: 50");
    }

    @Override
    protected void updateItem(ITSocialChannel item, boolean empty) {
        super.updateItem(item, empty);

        if(item != null && !empty){

            if(item.getPathImage()!= null){
                StringBuilder sb = new StringBuilder();
                sb.append("/");
                sb.append(item.getPathImage());

                imageView.setImage(new LKImageManager().getImageResource(sb.toString()).getImage());

            }

            title.setText(item.getTitle().length()<=13?item.getTitle():item.getTitle().substring(0,12)+"...");

            if(item.getSpecification() > 0){
                rootPanel.getChildren().remove(1);

                vertical.getChildren().clear();
                vertical.getChildren().add(0, title);
                vertical.getChildren().add(1, specification);
                rootPanel.getChildren().add(vertical);

                specification.setText(decodeSpecification(item.getSpecification()));
                imageView.setImage(decodeSpecificationImage(item.getSpecification()));
               // btnConfig.setGraphic(new LKImageManager().getImageResource("ic_config.png"));
            } else {
                rootPanel.getChildren().remove(1);
                rootPanel.getChildren().add(title);
            }

            setGraphic(rootPanel);
        } else {
            setGraphic(null);
        }
    }
    
    public static String decodeSpecification(int specification){
 
        switch(specification){
            case SITE: return "SITE" ;
            case SOCIAL_MEDIA: return "MEDIA SOCIAL" ;
            case BLOG: return "BLOG" ;
            case YOUTUBE: return "YOUTUBE" ;
            case FACEBOOK: return "FACEBOOK" ;
            case FACEBOOK_PAGE: return "PAGINA FACEBOOK" ;
            case FACEBOOK_GROUP: return "GRUPO FACEBOOK" ;
            case WHATSAPP: return "WHATSAPP" ;
            case TELEGRAM: return "TELEGRAM" ;
            case INSTAGRAM: return "INSTAGRAM" ;
            case PINTEREST: return "PINTEREST" ;
            case TWITTER: return "TWITTER" ;
            case COMUNICATION: return "COMUNICAÇÃO" ;
            case SOFTWARE: return "SOFTWARE" ;
            default: return "SEM ESPECIFICAÇÃO";
        }
    }

    public static int decodeSpecificationCode(String specification){

        switch(specification){
            case "SITE": return SITE ;
            case "MEDIA SOCIAL": return SOCIAL_MEDIA ;
            case "BLOG": return BLOG ;
            case "YOUTUBE": return YOUTUBE ;
            case "FACEBOOK": return FACEBOOK ;
            case "PAGINA FACEBOOK": return FACEBOOK_PAGE ;
            case "GRUPO FACEBOOK": return FACEBOOK_GROUP ;
            case "WHATSAPP": return WHATSAPP ;
            case "TELEGRAM": return TELEGRAM ;
            case "INSTAGRAM": return INSTAGRAM ;
            case "PINTEREST": return PINTEREST ;
            case "TWITTER": return TWITTER ;
            case "COMUNICAÇÃO": return COMUNICATION ;
            case "SOFTWARE": return SOFTWARE ;
            default: return 0;
        }
    }
    
    public static Image decodeSpecificationImage(int specification){
 
        switch(specification){
            case SITE: return new LKImageManager().getImageResource("site.png").getImage() ;
            case SOCIAL_MEDIA: return new LKImageManager().getImageResource("social_media.png").getImage() ;
            case BLOG: return new LKImageManager().getImageResource("blog.png").getImage() ;
            case YOUTUBE: return new LKImageManager().getImageResource("youtube.png").getImage() ;
            case FACEBOOK: return new LKImageManager().getImageResource("facebook.png").getImage() ;
            case FACEBOOK_PAGE: return new LKImageManager().getImageResource("facebook_page.png").getImage() ;
            case FACEBOOK_GROUP: return new LKImageManager().getImageResource("facebook_group.png").getImage() ;
            case WHATSAPP: return new LKImageManager().getImageResource("whatsapp.png").getImage() ;
            case TELEGRAM: return new LKImageManager().getImageResource("telegram.png").getImage() ;
            case INSTAGRAM: return new LKImageManager().getImageResource("instagram.png").getImage() ;
            case PINTEREST: return new LKImageManager().getImageResource("pinterest.png").getImage() ;
            case COMUNICATION: return new LKImageManager().getImageResource("comunication.png").getImage() ;
            case TWITTER: return new LKImageManager().getImageResource("twitter.png").getImage() ;
            case SOFTWARE: return new LKImageManager().getImageResource("software.png").getImage() ;
            default: return new LKImageManager().getImageResource("coringa.png").getImage();
        }
    } 
    
    public static Image decodeSpecificationImageGray(int specification){
 
        switch(specification){
            case SITE: return new LKImageManager().getImageResource("site_gray.png").getImage() ;
            case SOCIAL_MEDIA: return new LKImageManager().getImageResource("social_media_gray.png").getImage() ;
            case BLOG: return new LKImageManager().getImageResource("blog_gray.png").getImage() ;
            case YOUTUBE: return new LKImageManager().getImageResource("youtube_gray.png").getImage() ;
            case FACEBOOK: return new LKImageManager().getImageResource("facebook_gray.png").getImage() ;
            case FACEBOOK_PAGE: return new LKImageManager().getImageResource("facebook_page_gray.png").getImage() ;
            case FACEBOOK_GROUP: return new LKImageManager().getImageResource("facebook_group_gray.png").getImage() ;
            case WHATSAPP: return new LKImageManager().getImageResource("whatsapp_gray.png").getImage() ;
            case TELEGRAM: return new LKImageManager().getImageResource("telegram_gray.png").getImage() ;
            case INSTAGRAM: return new LKImageManager().getImageResource("instagram_gray.png").getImage() ;
            case PINTEREST: return new LKImageManager().getImageResource("pinterest_gray.png").getImage() ;
            case TWITTER: return new LKImageManager().getImageResource("twitter_gray.png").getImage() ;
            case COMUNICATION: return new LKImageManager().getImageResource("comunication_gray.png").getImage() ;
            case SOFTWARE: return new LKImageManager().getImageResource("software_gray.png").getImage() ;
            default: return new LKImageManager().getImageResource("coringa_gray.png").getImage();
        }
    }
}
