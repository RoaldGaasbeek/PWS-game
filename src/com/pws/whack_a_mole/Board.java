package com.pws.whack_a_mole;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;


public class Board extends JPanel {
    protected static final int BOARD_WIDTH = 600;
    protected static final int BOARD_HEIGHT = 400;
    private final int NUM_OF_BALLOONS = 20;
    private final int BALLOON_WIDTH = 40;
    private final int BALLOON_HEIGHT = 60;
    private final int TARGET_WIDTH = 24;
    private final int PERIOD = 2000;
    private int molesHit = 0;
    private HitEffect hitEffect;
    private Timer timer;
    private boolean isRunning = true;
    private List<Mole> moles = new ArrayList<>();

    public Board() {
        initBoard();
    }

    private void initBoard() {
        setPreferredSize(
                new Dimension(Board.BOARD_WIDTH, Board.BOARD_HEIGHT));
        moles.add(new Mole(50, 50));
// hides cursor
//        setCursor(getToolkit().createCustomCursor(
//                new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
//                new Point(0, 0),
//                null));


        addMouseMotionListener(new MAdapter());
        addMouseListener(new MAdapter2());
        setBackground(Color.GRAY);

        Point p = MouseInfo.getPointerInfo().getLocation();
        hitEffect = new HitEffect(p.getX(), p.getY());

        timer = new Timer(PERIOD, new GameCycle());
        timer.start();
    }


    private void showMole() {
//        updateBalloons();



        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        Mole mole = moles.get(0);
        g2d.drawImage(mole.getImage(), (int) mole.getX(),
                (int) mole.getY(), this);

//        if (isRunning) {
//            doDrawing(g);
//        } else {
//            gameOver(g);
//        }
//        Toolkit.getDefaultToolkit().sync();
    }

    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        drawScore(g2d);

        g2d.drawImage(hitEffect.getImage(), (int) hitEffect.getX(),
                (int) hitEffect.getY(), this);
    }


    private void hit(int mx, int my) {
        for (int i = 0; i < moles.size(); i++) {
            Mole mole = moles.get(i);
            double bx = mole.getX();
            double by = mole.getY();
            Ellipse2D ellipse = new Ellipse2D.Double(bx, by,
                    BALLOON_WIDTH, BALLOON_HEIGHT);
            if (ellipse.contains(mx, my)) {
                mole.setVisible(false);
                molesHit++;
            }
        }
    }

    private void drawScore(Graphics2D g2d) {
    }

    private void gameOver(Graphics g) {
    }

    private class MAdapter extends MouseMotionAdapter {
        @Override
        public void mouseMoved(MouseEvent e) {
            hitEffect.mouseMoved(e);
        }
    }

    private class MAdapter2 extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            int mx = e.getX() + TARGET_WIDTH / 2;
            int my = e.getY() + TARGET_WIDTH / 2;
            hit(mx, my);
        }
    }

    private class GameCycle implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showMole();
        }
    }
}
