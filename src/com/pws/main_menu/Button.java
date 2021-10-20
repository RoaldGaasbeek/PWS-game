package com.pws.main_menu;

import com.pws.Sprite;

public class Button extends Sprite {

    public static final int BUTTON_WIDTH = 70;
    public static final int BUTTON_HEIGHT = 75;
    public String Text;
    public Button (int x, int y, String text) {
        super(x, y);
        initButton();
        Text = text;
    }

    public String getText(){
        return Text;
    }


    private void initButton() {
        loadImage("src/resources/mole1.png", BUTTON_WIDTH, BUTTON_HEIGHT);
        getImageDimensions();
    }
}
