package Itens;

import system.OLinkConfiguration;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ITChildrenTreeView {

    private int systemId=OLinkConfiguration.SystemConfigure.SYSTEM_ID;
    private final ImageView icon ;
    private final Text title, linkRedirect, clicks, sales, views, conversion ;
    private final Button btnCopy ;
    
    private Separator[] seps = {newSeaparator(Orientation.VERTICAL),newSeaparator(Orientation.VERTICAL),newSeaparator(Orientation.VERTICAL)};
    private final VBox childClick, childSale, childConversion, childView ;
    private HBox line ;

    private final VBox root ;

    public ITChildrenTreeView(){
        icon = new ImageView();

        title = new Text();
        title.setFont(Font.font(null, FontWeight.BOLD, 12));

        btnCopy = new Button();

        linkRedirect = new Text();
        linkRedirect.setFont(Font.font(null, FontWeight.NORMAL,12));

        clicks = new Text();
        clicks.setFont(Font.font(null, FontWeight.BOLD,12));

        sales = new Text();
        sales.setFont(Font.font(null, FontWeight.BOLD,12));

        views = new Text();
        views.setFont(Font.font(null, FontWeight.BOLD,12));

        conversion = new Text();
        conversion.setFont(Font.font(null, FontWeight.BOLD,12));

        Text textClick = new Text("cliques");
        textClick.setFont(Font.font(null,FontWeight.NORMAL, 12));
        childClick = new VBox(clicks, textClick);
        childClick.setAlignment(Pos.CENTER);

        Text textSales = new Text("vendas");
        textSales.setFont(Font.font(null,FontWeight.NORMAL, 12));
        childSale = new VBox(sales, textSales);
        childSale.setAlignment(Pos.CENTER);

        Text textViews = new Text("views");
        textViews.setFont(Font.font(null,FontWeight.BOLD, 12));
        childView = new VBox(views, textViews);
        childView.setAlignment(Pos.CENTER);

        Text textConversion = new Text("conversão");
        textConversion.setFont(Font.font(null,FontWeight.NORMAL, 12));
        childConversion = new VBox(conversion, textConversion);
        childConversion.setAlignment(Pos.CENTER);

        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        line = new HBox(icon, title, linkRedirect, btnCopy, region, seps[0],childClick, seps[1], childSale, seps[2]);
        line.setSpacing(10);
        line.setAlignment(Pos.CENTER_LEFT);

        root = new VBox(line,newSeaparator(Orientation.HORIZONTAL));
        root.setAlignment(Pos.CENTER_LEFT);
    }

    public ImageView getIcon() {
        return icon;
    }

    public Text getTitle() {
        return title;
    }

    public Text getLinkRedirect() {
        return linkRedirect;
    }

    public Text getClicks() {
        return clicks;
    }

    public Text getSales() {
        return sales;
    }

    public Text getViews() {
        return views;
    }

    public Text getConversion() {
        return conversion;
    }

    public Button getBtnCopy() {
        return btnCopy;
    }

    private Separator newSeaparator(Orientation orientation){
        return new Separator(orientation);
    }

    public VBox getRoot() {
        return root;
    }
   
    public synchronized void addSaleChild(){
        if(!line.getChildren().contains(childSale)){
            line.getChildren().add(7,childSale);
            line.getChildren().add(8,seps[2]);
        }
    }
    
    public synchronized void removeSaleChild(){
        if(line.getChildren().contains(childSale)){
            line.getChildren().remove(childSale);
            line.getChildren().remove(seps[2]);
        }
    }
    
    public synchronized void addClickChild(){
        if(!line.getChildren().contains(childClick)){
            line.getChildren().add(5,childClick);
            line.getChildren().add(6,seps[1]);
        }
    }
    
    public synchronized void removeClickChild(){
        if(line.getChildren().contains(childClick)){
            line.getChildren().remove(childClick);
            line.getChildren().remove(seps[1]);
        }
    }
    
    public synchronized void addCConversionChild(){
        if(!line.getChildren().contains(childConversion)){
            line.getChildren().add(8,childConversion);
        }
    }
    
    public synchronized void removeConversionChild(){
        if(line.getChildren().contains(childConversion)){
            line.getChildren().remove(8);
        }
    }

    public int getSystemId() {
        return systemId;
    }
}
