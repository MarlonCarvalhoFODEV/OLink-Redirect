package adapters;

import Itens.ITSocialChannelContent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.control.TreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import util.LKPaths;

import java.io.File;
import java.io.IOException;
import java.util.Map;


public class AdapterSocialChannelContent1 extends TreeCell<Object> {

    private Node cell ;

    private ImageView imageView ;
    private Text title, txTotalSale, txTotalRevenue, txTotalClickSum;


    private Separator[] separators = {new Separator(Orientation.VERTICAL),new Separator(Orientation.VERTICAL),new Separator(Orientation.VERTICAL),new Separator(Orientation.VERTICAL),};

    public AdapterSocialChannelContent1(){
        imageView = new ImageView();
        title = new Text();
        title.setStyle("-fx-font-weight: bold;");


        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/itcontent.fxml"));
            loader.setLocation(getClass().getResource("/fxml/itcontent.fxml"));
            Node cell = loader.load();
            Map<String, Object> namespace = loader.getNamespace();

            //imageView = (ImageView) namespace.get("itContentIcon");
            //txTotalClickSum = (Text) cell.lookup("#itContentTotalClick");
            title = (Text) namespace.get("itContentTitle");
            //txTotalSale = (Text) cell.lookup("#itContentTotalSale");
//            txTotalRevenue = (Text) cell.lookup("#itContentTotalRevenue");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void updateItem(Object object, boolean empty) {
        super.updateItem(object, empty);

        if(object != null && !empty){

                ITSocialChannelContent it = (ITSocialChannelContent) object;

                StringBuilder sb = new StringBuilder();
                sb.append(LKPaths.PATH_RESOURCES);
                sb.append(it.getPathImage());

                File file = new File(sb.toString());
                Image image = new Image(file.toURI().toString());

                imageView.setImage(image);
                imageView.setFitWidth(40);
                imageView.setFitHeight(40);
                imageView.setImage(image);

                title.setText(it.getTitle());
                //txTotalClickSum.setText(String.valueOf(it.getCountClickTotal()));
                //txTotalRevenue.setText(String.valueOf(it.getTotalBilling()));
                //txTotalSale.setText("R$ "+it.getCountSale());

                for(Separator sep : separators){
                    sep.setVisible(false);
                }
            setGraphic(cell);
        } else {
            setGraphic(null);
        }
    }

    @Override
    public void updateSelected(boolean selected) {
        super.updateSelected(selected);
    }

}
