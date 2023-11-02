package adapters;

import Itens.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import system.OLinkConfiguration;
import urls.UrlsConnetionsWEB;
import util.LKImageManager;

import java.awt.datatransfer.StringSelection;
import java.text.DecimalFormat;
import javafx.stage.Stage;
import util.UtilDialogManager;


public class AdapterSocialChannelContent extends TreeCell<Object> {

    private final ITGroupTreeView group ;

    public AdapterSocialChannelContent(){
        group = new ITGroupTreeView();
    }

    @Override
    protected void updateItem(Object object, boolean empty) {
        super.updateItem(object, empty);

        if(object != null && !empty){

            if(object.getClass().getSimpleName().equals(ITSocialChannelContent.class.getSimpleName())){

                ImageView img = new ImageView();
                VBox box = new VBox(img);
                box.setPrefSize(20,42);
                box.setAlignment(Pos.CENTER);
                setDisclosureNode(box);

                if(getTreeItem().isExpanded()){
                    img.setImage(new LKImageManager().getImageResource("/icon_indicator_expanded.png",10,10).getImage());
                } else {
                    img.setImage(new LKImageManager().getImageResource("/icon_indicator.png",10,10).getImage());
                }

                img.setFitWidth(10);
                img.setFitHeight(10);
                img.setSmooth(true);

                ITSocialChannelContent it = (ITSocialChannelContent) object;
                it.setIndex(getIndex());

                if(it.getPathImage() != null && it.getSpecification() == 0){
                    group.getIcon().setImage(new LKImageManager().getImageResource(it.getPathImage()).getImage());
                } else {
                    group.getIcon().setImage(AdapterSocialChannel.decodeSpecificationImageGray(it.getSpecification()));
                }
                group.getIcon().setFitWidth(35);
                group.getIcon().setFitHeight(35);

                group.getTitle().setText(it.getTitle());
                group.getTotalClick().setText(String.valueOf(sumClick(it)+0));
                group.getTotalSale().setText(String.valueOf(sumSales(it)));
                group.getTotalRevenue().setText("R$ "+String.valueOf(sumRevenue(it)));

                setGraphic(group.getRoot());
            
            } else if(object.getClass().getSimpleName().equals(ITLink.class.getSimpleName())){

                ITLink item = (ITLink) object;
                ITChildrenTreeView children = new ITChildrenTreeView();
                item.setIndex(getIndex());

                children.getIcon().setFitHeight(18);
                children.getIcon().setFitWidth(18);
                children.getIcon().setImage(new LKImageManager().getImageResource("/link.png").getImage());

                children.getTitle().setText(item.getLinkName());
                children.getLinkRedirect().setText(UrlsConnetionsWEB.DOMAIN_CONNECTION+item.getFromLink());
                children.removeConversionChild();
                
                if(item.getValue() > 0){
                    
                    children.addSaleChild();
                    children.getSales().setText(String.valueOf(item.getCountSale()+0));
                    children.removeClickChild();
                    
                } else {
                    children.addClickChild();
                    children.getClicks().setText(String.valueOf(item.getCountClick()+0));
                    children.removeSaleChild();
                }

                children.getBtnCopy().setGraphic(new LKImageManager().getImageResource("/icon_copy.png",20));
                children.getBtnCopy().setPrefSize(20,20);
                children.getBtnCopy().setMinSize(20,20);
                children.getBtnCopy().setMaxHeight(20);
                children.getBtnCopy().setMaxWidth(20);
                children.getBtnCopy().setStyle(" -fx-background-color: transparent;");
                children.getBtnCopy().setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        copy(UrlsConnetionsWEB.DOMAIN_CONNECTION+item.getFromLink());
                        Stage p = (Stage) children.getBtnCopy().getScene().getWindow();
                        UtilDialogManager.showToast(p, "Link copiado com sucesso", 2000);
                    }
                });
                setGraphic(children.getRoot());
            }
        } else {
            setGraphic(null);
        }
    }

    private void copy(String text){
        Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(text);
        clipboard.setContent(content);
    }

    @Override
    public void updateSelected(boolean selected) {
        super.updateSelected(selected);
    }

    private int sumClick(ITSocialChannelContent content){
        int sum = 0 ;
        for (ITLink item : content.getLinks()){
            sum += item.getCountClick();
        }

        return sum ;
    }

    private int sumViews(ITSocialChannelContent content){
        int sum = 0 ;
        for (ITLink item : content.getLinks()){
            sum += item.getCountViews();
        }

        return sum ;
    }
    
    private int sumSales(ITSocialChannelContent content){
        int sum = 0 ;
        for (ITLink item : content.getLinks()){
            sum += item.getCountSale();
        }

        return sum ;
    }
    
    private double sumRevenue(ITSocialChannelContent content){
        double sum = 0 ;
        
        for(ITLink l : content.getLinks()){
            sum += l.getCountSale()*l.getValue();
        }
        
        return sum ;
    }

    private String percentualConversion(ITLink item){
        double totalClick = item.getCountClick();
        double totalSale = item.getCountSale();

        DecimalFormat format = new DecimalFormat("#0.00");

        return format.format(((totalSale*100.0)/totalClick));
    }


}
