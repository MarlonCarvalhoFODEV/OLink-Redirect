package util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LKImageManager {
    
    
    public synchronized ImageView getImageResource(String imageName){
        StringBuilder imgName = new StringBuilder();
        if(!imageName.contains("/"))imgName.append("/");
        imgName.append(imageName);

        ImageView img = new ImageView(new Image(getClass().getResource(imgName.toString()).toString()));
        
        return img ;
    }
    
    public synchronized ImageView getImageResource(String imageName, int width, int height){
        
        StringBuilder imgName = new StringBuilder();
        if(!imageName.contains("/"))imgName.append("/");
        imgName.append(imageName);
        
        ImageView img = new ImageView(new Image(getClass().getResource(imgName.toString()).toString()));
        img.setFitHeight(height);
        img.setFitWidth(width);
        return img ;
    }
    
    public synchronized ImageView getImageResource(String imageName, int widthAndHeight){
        StringBuilder imgName = new StringBuilder();
        if(!imageName.contains("/"))imgName.append("/");
        imgName.append(imageName);
        
        ImageView img = new ImageView(new Image(getClass().getResource(imgName.toString()).toString()));
        img.setFitHeight(widthAndHeight);
        img.setFitWidth(widthAndHeight);
        return img ;
    }

}
