package com.pws.whack_a_mole;

import com.pws.Sprite;

import java.awt.event.MouseEvent;

public class HitEffect extends Sprite {

    public HitEffect(double x, double y) {
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
