package com.pws.main_menu;

import com.pws.Sprite;

public class Button extends Sprite {

    public static final int BUTTON_WIDTH = 100;
    public static final int BUTTON_HEIGHT = 50;
    public double dX;
    public double dY;
    public String Text;

    public Button (int x, int y, String text) {
        super(x, y);
        initButton();
        Text = text;
        dX = x;
        dY = y;
    }

    public String getText(){
        return Text;
    }
    public double getdX(){
        return dX;
    }
    public double getdY(){
        return dY;
    }



    private void initButton() {
        loadImage("src/resources/button red.png", BUTTON_WIDTH, BUTTON_HEIGHT);
        getImageDimensions();
    }
}