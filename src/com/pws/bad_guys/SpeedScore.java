package com.pws.bad_guys;

import javax.swing.*;

public class SpeedScore extends JPanel {

    private final JLabel labelAvg;
    private final JLabel labelFast;
    private final JLabel labelSlow;

    private int fastest;
    private int slowest;
    private int avg;

    public SpeedScore() {
        labelFast = new JLabel("Fast: ");
        labelFast.setSize(150, 20);
        labelSlow = new JLabel("Slow: ");
        labelSlow.setSize(100, 20);
        labelAvg = new JLabel("Avg: ");
        labelAvg.setSize(100, 20);

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        add(labelFast);
        add(labelSlow);
        add(labelAvg);
    }

    public void processTime(int millis) {
        if (fastest == 0 || millis < fastest) {
            fastest = millis;
            labelFast.setText("Fast: " + millis);
        } else if (slowest == 0 || millis > slowest) {
            slowest = millis;
            labelSlow.setText("SLow: " + millis);
        }
    }

}
