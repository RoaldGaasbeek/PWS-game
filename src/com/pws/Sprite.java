package com.pws;

import java.awt.Image;
import javax.swing.ImageIcon;

import static java.awt.Image.SCALE_DEFAULT;

public class Sprite {
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
        ImageIcon imageIcon = new ImageIcon(imageName);

        image = imageIcon.getImage();
    }

    protected void loadImage(String imageName, int width, int height) {
        ImageIcon imageIcon = new ImageIcon(imageName);

        this.width = width;
        this.height = width;

        image = imageIcon.getImage().getScaledInstance(width, height, SCALE_DEFAULT);
    }

    protected void getImageDimensions() {
        if (width == 0) {
            width = image.getWidth(null);
            height = image.getHeight(null);
        }
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