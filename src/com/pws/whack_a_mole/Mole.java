package com.pws.whack_a_mole;

import com.pws.Sprite;


class Mole extends Sprite {

    static final int MOLE_WIDTH = 70;
    static final int MOLE_HEIGHT = 70;
    int lifespan;

    Mole(int x, int y, int Lifespan) {
        super(x, y);
        initMole();
        lifespan = Lifespan;
    }

    private void initMole() {
        loadImage("src/resources/mole1.png", MOLE_WIDTH, MOLE_HEIGHT);
        getImageDimensions();
    }
}
