/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adapters;

import Itens.ITLink;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import urls.UrlsConnetionsWEB;
import util.LKImageManager;

/**
 *
 * @author Marlon
 */
public class AdapterGraphicLink extends ListCell<ITLink>{
    
    private final Parent root ;
    private final Label text ;
    private final ImageView icon ;
    
    public AdapterGraphicLink() throws IOException{
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("it_link_graphic.fxml"));
        root = loader.load();
        
        text = (Label) root.lookup("#lbLinkName");
        icon = (ImageView) root.lookup("#ivIconLink");
    }
    
    @Override
    protected void updateItem(ITLink item, boolean empty) {
        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
        
        if(item != null){
            
            text.setText(UrlsConnetionsWEB.DOMAIN_CONNECTION+item.getFromLink());
            icon.setImage(new LKImageManager().getImageResource("link.png",35).getImage());
            
            setGraphic(root);
        }
        else {
            setGraphic(null);
        }
    }
    
    
    
}
