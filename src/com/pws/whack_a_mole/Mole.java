package com.pws.whack_a_mole;

import com.pws.Sprite;


public class Mole extends Sprite {

    public static final int MOLE_WIDTH = 70;
    public static final int MOLE_HEIGHT = 75;

    public Mole(int x, int y) {
        super(x, y);

        initMole();
    }

    private void initMole() {
        loadImage("src/resources/mole1.png", MOLE_WIDTH, MOLE_HEIGHT);
        getImageDimensions();
    }
}
