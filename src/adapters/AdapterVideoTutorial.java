package adapters;

import Itens.ITTutorialVideo;
import itenview.ITLinkView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import util.LKImageManager;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdapterVideoTutorial extends ListCell<ITTutorialVideo> {

    private Parent root ;
    private Label videoName ;
    //private  ImageView icon ;

    public AdapterVideoTutorial() {

        try {
            root = FXMLLoader.load(ITLinkView.class.getResource("it_video_tutorial.fxml"));
            videoName = (Label) root.lookup("#lbNameVideo");
        } catch (IOException ex) {
            Logger.getLogger(AdapterNewLink.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    @Override
    protected void updateItem(ITTutorialVideo item, boolean empty) {
        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.

        if(item != null){

            videoName.setText(item.getVideoName());
            //icon.setImage(new LKImageManager().getImageResource("link.png",35).getImage());

            setGraphic(root);
        }
        else {
            setGraphic(null);
        }
    }

}
