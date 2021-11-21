package com.pws.bad_guys;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.*;

public class SpeedScore extends JPanel {

    private final JLabel labelAvg;
    private final JLabel labelFast;
    private final JLabel labelSlow;

    private int fastest;
    private int slowest;
    private int avg;
    private final List<Integer> hitTimes = new ArrayList<>();

    public SpeedScore() {
        labelFast = new JLabel("");
        labelFast.setSize(150, 20);
        labelSlow = new JLabel("");
        labelSlow.setSize(100, 20);
        labelAvg = new JLabel("");
        labelAvg.setSize(100, 20);

        init();

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setPreferredSize(new Dimension(100, this.getHeight()));
        setBackground(Color.getHSBColor(70f, 10f, 100f));
        setBorder(BorderFactory.createLineBorder(Color.black));

        add(labelFast);
        add(labelSlow);
        add(labelAvg);
    }

    public void init() {
        labelFast.setText(" Fast: ---- ms");
        labelSlow.setText(" Slow: ---- ms");
        labelAvg.setText(" Avg:  ---- ms");
        hitTimes.clear();

    }

    public void processTime(int millis) {
        if (fastest == 0 || millis < fastest) {
            fastest = millis;
            labelFast.setText(" Fast: " + millis);
        } else if (slowest == 0 || millis > slowest) {
            slowest = millis;
            labelSlow.setText(" SLow: " + millis);
        }

        hitTimes.add(millis);
        Optional<Integer> total = hitTimes.stream().reduce(Integer::sum);

        if (total.isPresent()) {
            avg = total.get() / hitTimes.size();
            labelAvg.setText(" Avg:  " + avg);
        }
    }

}
