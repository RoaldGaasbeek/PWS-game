package com.pws.whack_a_mole;

import com.pws.Sprite;


public class Mole extends Sprite {
    public Mole(int x, int y) {
        super(x, y);

        initMole();
    }

    private void initMole() {
        loadImage("src/resources/mole1.png", 50, 50);
        getImageDimensions();
    }
}
