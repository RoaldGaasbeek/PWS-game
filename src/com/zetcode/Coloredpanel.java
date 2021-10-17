package com.zetcode;

import com.pws.Sprite;

import java.util.Random;

public class Coloredpanel extends Sprite {
    private final int NUMBER_OF_BALLOON_TYPES = 5;
    private int speed = 2;

    public Coloredpanel(int x, int y) {
        super(x, y);
        initBalloon();
    }

    private void initBalloon() {
        int r = new Random().nextInt(NUMBER_OF_BALLOON_TYPES) + 1;
        speed = Math.max(2, r);
        loadImage(String.format("src/resources/yellowBlock.png"));
        getImageDimensions();
    }

    public void move() {
        y -= speed;
    }
}