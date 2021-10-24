package com.pws.bad_guys;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Guy {
    public static final Random random = new Random();
    public final static int WIDTH = 40;
    public final static int HEIGHT = 40;
    private boolean isBadGuy;

    private int dx;
    private int dy;
    private int x;
    private int y;
    private int w;
    private int h;
    private Image image;
    private boolean isVisible;
    private long start;
    private long end;

    public Guy(int x, int y, boolean isBadGuy) {
        this.x = x;
        this.y = y;
        this.isBadGuy = isBadGuy;

        loadImage();
    }

    private void loadImage() {
        ImageIcon ii;

        if (isBadGuy) {
            ii = new ImageIcon("src/resources/bad-guys/thief.png");
        } else {
            ii = new ImageIcon("src/resources/bad-guys/good_guy.png");
        }
        image = ii.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT);

        w = WIDTH;//image.getWidth(null);
        h = HEIGHT;//image.getHeight(null);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Image getImage() {
        return image;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setVisible(boolean isVisible) {
        if (isVisible != this.isVisible) {
            if (isVisible) {
                start = System.currentTimeMillis();
                end = 0;
            } else {
                end = System.currentTimeMillis();
            }
        } else {
            start = System.currentTimeMillis();
            end = 0;
        }
        this.isVisible = isVisible;
    }

    private long getShowTime() {
        if (!isVisible) {
            return end - start;
        } else {
            return System.currentTimeMillis() - start;
        }

    }

    public boolean isVisible() {
        return isVisible;
    }

    public int getTime() {
        return (int)(end - start);
    }

    public void determineType() {
        int counter = random.nextInt(3);
        isBadGuy = counter!=1;

        loadImage();
    }

    public boolean isBadGuy() {
        return isBadGuy;
    }
}