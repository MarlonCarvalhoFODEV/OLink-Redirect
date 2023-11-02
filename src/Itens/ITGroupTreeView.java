package Itens;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class ITGroupTreeView {

    private ImageView icon ;
    private Text title, totalClick, totalSale, totalRevenue;
    private VBox root ;
    private Node cell ;

    public ITGroupTreeView(){
        icon = new ImageView();
        icon.setFitWidth(45);
        icon.setFitHeight(45);
        icon.setSmooth(true);
        icon.setPreserveRatio(true);

        title = new Text();
        title.setFont(Font.font(null,FontWeight.BOLD, 14));

        totalClick = new Text();
        totalClick.setFont(Font.font(null, FontWeight.BOLD,14));

        totalSale = new Text();
        totalSale.setFont(Font.font(null, FontWeight.BOLD,14));

        totalRevenue = new Text();
        totalRevenue.setFont(Font.font(null, FontWeight.BOLD,14));

        Text textClick = new Text("cliques totais");
        textClick.setFont(Font.font(null,FontWeight.NORMAL, 12));
        VBox totalClicks = new VBox(totalClick, textClick);
        totalClicks.setAlignment(Pos.CENTER);

        Text textSales = new Text("vendas totais");
        textSales.setFont(Font.font(null,FontWeight.NORMAL, 12));
        VBox totalSale = new VBox(this.totalSale, textSales);
        totalSale.setAlignment(Pos.CENTER);

        Text textRevenue = new Text("receita total");
        textRevenue.setFont(Font.font(null,FontWeight.BOLD, 12));
        VBox totalRevenue = new VBox(this.totalRevenue, textRevenue);
        totalRevenue.setAlignment(Pos.CENTER);

        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        HBox line = new HBox(icon, title, region,newSeaparator(Orientation.VERTICAL), totalClicks, newSeaparator(Orientation.VERTICAL), totalSale, newSeaparator(Orientation.VERTICAL), totalRevenue);
        line.setAlignment(Pos.CENTER_LEFT);
        line.setSpacing(5);

        root = new VBox(newSeaparator(Orientation.HORIZONTAL),line,newSeaparator(Orientation.HORIZONTAL));
        root.setAlignment(Pos.CENTER_LEFT);

    }

    public ImageView getIcon() {
        return icon;
    }

    public Text getTitle() {
        return title;
    }

    public Text getTotalClick() {
        return totalClick;
    }

    public Text getTotalSale() {
        return totalSale;
    }

    public Text getTotalRevenue() {
        return totalRevenue;
    }

    private Separator newSeaparator(Orientation orientation){
        return new Separator(orientation);
    }

    public VBox getRoot() {
        return root;
    }

}
