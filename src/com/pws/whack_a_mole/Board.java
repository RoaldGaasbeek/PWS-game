package com.pws.whack_a_mole;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.pws.whack_a_mole.Mole.MOLE_HEIGHT;
import static com.pws.whack_a_mole.Mole.MOLE_WIDTH;


public class Board extends JPanel {
    protected static final int BOARD_WIDTH = 600;
    protected static final int BOARD_HEIGHT = 400;
//    private final int NUM_OF_BALLOONS = 20;
//    private final int TARGET_WIDTH = 24;
    private final int PERIOD = 1000 / 60;
    private int molesWhacked = 0;
    private HitEffect hitEffect;
    private Timer timer;
    private boolean isRunning = true;
    private final List<Mole> moles = new ArrayList<>();
    private int lifespan;

    public Board() {
        initBoard();
    }

    private void initBoard() {
        setPreferredSize(new Dimension(Board.BOARD_WIDTH, Board.BOARD_HEIGHT));

        createMoles();


        // hides cursor
        setCursor(getToolkit().createCustomCursor(
                new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
                new Point(0, 0),
                null));


        addMouseMotionListener(new MAdapter());
        addMouseListener(new MAdapter2());
        setBackground(Color.GRAY);

        Point p = MouseInfo.getPointerInfo().getLocation();
        hitEffect = new HitEffect(p.getX(), p.getY());

        timer = new Timer(PERIOD, new GameCycle() );
        timer.start();
    }

    private void createMoles() {
        new Random();

//        moles.add(new Mole(rand.nextInt(BOARD_WIDTH - MOLE_WIDTH), rand.nextInt(BOARD_HEIGHT - MOLE_HEIGHT)));

        for (int row = 1; row <= 3; row++) {
            for (int column = 1; column <= 3; column++) {
//                moles.add(new Mole(rand.nextInt(BOARD_WIDTH - MOLE_WIDTH), rand.nextInt(BOARD_HEIGHT - MOLE_HEIGHT)));
                moles.add(new Mole(20 + row * MOLE_WIDTH + row * 20, 20 + column * MOLE_HEIGHT + column * 20 ));
            }
        }
    }


    private void showMole() {
        updateMoles();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // For now we only have one mole.
        //Mole mole = moles.get(0);

        for (Mole mole : moles) {
            g2d.drawImage(mole.getImage(), (int) mole.getX(), (int) mole.getY(), this);
        }

        if (isRunning) {
            doDrawing(g);
        } else {
            gameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        drawScore(g2d);

        g2d.drawImage(hitEffect.getImage(), (int) hitEffect.getX(), (int) hitEffect.getY(), this);
    }

    private void updateMoles() {
        for (int i = 0; i < moles.size(); i++) {
            Mole mole = moles.get(i);

            if (mole.isVisible()) {
                lifespan++;
                if (lifespan > PERIOD * 200) {
                    mole.setVisible(false);
                    lifespan = 0;
                }
            } else {
                if (moles.isEmpty()) {
                    timer.stop();
                    isRunning = false;
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        }
    }

    private void whack(int mx, int my) {
        for (Mole mole : moles) {
            Ellipse2D ellipse = new Ellipse2D.Double(mole.getX(), mole.getY(), MOLE_WIDTH, MOLE_HEIGHT);

            if (ellipse.contains(mx, my)) {
//                mole.setVisible(false);
                molesWhacked++;
                Random rand = new Random();
                mole.setXY(rand.nextInt(BOARD_WIDTH - MOLE_WIDTH), rand.nextInt(BOARD_HEIGHT - MOLE_HEIGHT));
//                moles.add(new Mole(rand.nextInt(BOARD_WIDTH - MOLE_WIDTH), rand.nextInt(BOARD_HEIGHT - MOLE_HEIGHT)));
                showMole();
            }
        }
    }

    private void drawScore(Graphics2D g2d) {
        g2d.setFont(new Font("Geneva", Font.BOLD, 12));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        String label1 = String.format("Score %d", molesWhacked);
        g2d.drawString(label1, 10, 15);
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
            whack(e.getX(), e.getY());
        }
    }

    private class GameCycle implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showMole();
        }
    }
}
