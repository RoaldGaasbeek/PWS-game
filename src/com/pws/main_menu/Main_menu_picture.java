package com.pws.main_menu;


import com.pws.Sprite;


public class Main_menu_picture extends Sprite {

    public static final int PICTURE_WIDTH = 500;
    public static final int PICTURE_HEIGHT = 250;

    public Main_menu_picture(int x, int y) {
        super(x, y);

        initMainMenuPicture();
    }

    private void initMainMenuPicture() {
        loadImage("src/resources/main_menu_logo.png", PICTURE_WIDTH, PICTURE_HEIGHT);
        getImageDimensions();
    }
}

