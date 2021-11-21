package com.pws.bad_guys;

import java.awt.*;
import java.util.Random;

import javax.swing.*;

public class Guy {
    public static final Random random = new Random();
    public static final int WIDTH = 60;
    public static final int HEIGHT = 60;
    private boolean isBadGuy;

    private int x;
    private int y;
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