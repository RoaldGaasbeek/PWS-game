package com.pws.bad_guys;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.*;

public class SpeedScore extends JPanel {
    private static int WIDTH = 145;
    private final JLabel labelAvg = new JLabel("");;
    private final JLabel labelFast = new JLabel("");;
    private final JLabel labelSlow = new JLabel("");;

    private int fastest;
    private int slowest;
    private int avg;
    private final List<Integer> hitTimes = new ArrayList<>();

    public SpeedScore() {
        init();

        setLayout(new FlowLayout());
        setSize(WIDTH, 100);
        setPreferredSize(new Dimension(WIDTH, 100));
        setBackground(Color.getHSBColor(70f, 10f, 100f));
        setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.black));

        add(labelFast);
        add(labelSlow);
        add(labelAvg);
        add(Box.createHorizontalStrut(WIDTH));
        add(Box.createRigidArea(new Dimension(10, 180)));
    }

    public void init() {
        labelFast.setText(" Fast: ---- ms");
        labelSlow.setText(" Slow: ---- ms");
        labelAvg.setText(" Avg:   ---- ms");

        fastest = 0;
        slowest = 0;
        avg = 0;
        hitTimes.clear();
    }

    public void processTime(int millis) {
        if (fastest == 0 || millis < fastest) {
            fastest = millis;
            labelFast.setText(" Fast: " + millis);
        }
        if (slowest == 0 || millis > slowest) {
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
