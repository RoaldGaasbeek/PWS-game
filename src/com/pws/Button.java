package com.pws;

import com.pws.Sprite;

public class Button extends Sprite {

    public static final int BUTTON_WIDTH = 100;
    public static final int BUTTON_HEIGHT = 50;
    public String Text;

    public Button(int x, int y, String text) {
        super(x, y);
        initButton();
        Text = text;
    }

    public String getText() {
        return Text;
    }


    private void initButton() {
        loadImage("src/resources/green button.png", BUTTON_WIDTH, BUTTON_HEIGHT);
        getImageDimensions();
    }
}
