package com.zetcode;

import java.awt.event.MouseEvent;

public class Target extends Sprite {

    public Target(double x, double y) {
        super(x, y);

        initTarget();
    }

    private void initTarget() {

        loadImage("src/resources/target.png");
        getImageDimensions();
    }


    public void mouseMoved(MouseEvent e) {

        x = e.getX();
        y = e.getY();
    }
}
