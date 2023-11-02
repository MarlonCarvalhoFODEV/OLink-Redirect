package itenview;

import adapters.AdapterNewLink;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import util.LKImageManager;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ITLinkView {

    private Node root ;
    private ImageView icon, ivConversion, ivPixel, ivPixelEvent, ivAB, ivLC;
    private Label title, fromLink, toLink ;
    private Button btnTrash, btnEdit ;

    public ITLinkView(){

        try {
            root = FXMLLoader.load(getClass().getResource("it_link.fxml"));

            this.icon = (ImageView) root.lookup("#ivIcon");
            this.ivConversion = (ImageView) root.lookup("#ivConversion");
            this.ivPixel = (ImageView) root.lookup("#ivPixel");
            this.ivPixelEvent = (ImageView) root.lookup("#ivPixelEvent");
            this.ivAB = (ImageView) root.lookup("#ivAB");
            this.ivLC = (ImageView) root.lookup("#ivLC");

            this.icon.setImage(new LKImageManager().getImageResource("link.png", 25).getImage());
            this.ivConversion.setImage(new LKImageManager().getImageResource("icon_close.png", 15).getImage());
            this.ivPixel.setImage(new LKImageManager().getImageResource("icon_close.png", 15).getImage());
            this.ivPixelEvent.setImage(new LKImageManager().getImageResource("icon_close.png", 15).getImage());

            this.title = (Label) root.lookup("#txLinkName");
            this.fromLink = (Label) root.lookup("#txLinkFrom");
            this.toLink = (Label) root.lookup("#txLinkTo");
            this.btnTrash = (Button) root.lookup("#btnDelLink");
            this.btnEdit = (Button) root.lookup("#btnEditLink");
            this.btnEdit.setGraphic(new LKImageManager().getImageResource("icon_edit_degrade.png", 25));
            this.btnTrash.setGraphic(new LKImageManager().getImageResource("icon_crash_degrade.png", 25));

        } catch (IOException ex) {
            Logger.getLogger(AdapterNewLink.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public ImageView getIcon() {
        return icon;
    }

    public void setIcon(ImageView icon) {
        this.icon = icon;
    }

    public ImageView getIvConversion() {
        return ivConversion;
    }

    public void setIvConversion(ImageView ivConversion) {
        this.ivConversion = ivConversion;
    }

    public ImageView getIvPixel() {
        return ivPixel;
    }

    public void setIvPixel(ImageView ivPixel) {
        this.ivPixel = ivPixel;
    }

    public ImageView getIvPixelEvent() {
        return ivPixelEvent;
    }

    public void setIvPixelEvent(ImageView ivPixelEvent) {
        this.ivPixelEvent = ivPixelEvent;
    }

    public ImageView getIvAB() {
        return ivAB;
    }

    public void setIvAB(ImageView ivAB) {
        this.ivAB = ivAB;
    }

    public ImageView getIvLC() {
        return ivLC;
    }

    public void setIvLC(ImageView ivLC) {
        this.ivLC = ivLC;
    }

    public Label getTitle() {
        return title;
    }

    public void setTitle(Label title) {
        this.title = title;
    }

    public Label getFromLink() {
        return fromLink;
    }

    public void setFromLink(Label fromLink) {
        this.fromLink = fromLink;
    }

    public Label getToLink() {
        return toLink;
    }

    public void setToLink(Label toLink) {
        this.toLink = toLink;
    }

    public Button getBtnTrash() {
        return btnTrash;
    }

    public void setBtnTrash(Button btnTrash) {
        this.btnTrash = btnTrash;
    }

    public Button getBtnEdit() {
        return btnEdit;
    }

    public void setBtnEdit(Button btnEdit) {
        this.btnEdit = btnEdit;
    }
}
