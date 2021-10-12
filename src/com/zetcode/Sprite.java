package com.zetcode;
import java.awt.Image;
import javax.swing.ImageIcon;
public class Sprite<var> {
    protected double x;
    protected double y;
    protected int width;
    protected int height;
    protected boolean visible;
    protected Image image;
    public Sprite(double x, double y) {
        this.x = x;
        this.y = y;
        visible = true;
    }
    protected void loadImage(String imageName) {
        var ii = new ImageIcon(imageName);
        image = ((ImageIcon) ii).getImage();
    }
    protected void getImageDimensions() {
        width = image.getWidth(null);
        height = image.getHeight(null);
    }
    public Image getImage() {
        return image;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public boolean isVisible() {
        return visible;
    }
    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
}