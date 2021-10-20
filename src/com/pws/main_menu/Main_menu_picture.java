package com.pws.main_menu;

import com.pws.Sprite;



public class Main_menu_picture extends Sprite {

        public static final int PICTURE_WIDTH = 70;
        public static final int PICTURE_HEIGHT = 75;

        public Main_menu_picture (int x, int y) {
            super(x, y);

            initMainMenuPicture();
        }

        private void initMainMenuPicture() {
            loadImage("src/resources/mole1.png", PICTURE_WIDTH, PICTURE_HEIGHT);
            getImageDimensions();
        }
}

