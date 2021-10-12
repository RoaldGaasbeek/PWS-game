package com.zetcode;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.Timer;
/**
 * Java Balloons game
 *
 * Author: Jan Bodnar
 * Website: http://zetcode.com
 */
public class Board<var> extends JPanel {
    protected static final int BOARD_WIDTH = 600;
    protected static final int BOARD_HEIGHT = 400;
    private final int NUM_OF_BALLOONS = 20;
    private final int BALLOON_WIDTH = 40;
    private final int BALLOON_HEIGHT = 60;
    private final int TARGET_WIDTH = 24;
    private List<Balloon > balloons;
    private final int FPS = 60;
    private final int PERIOD = 1000 / FPS;
    private int balloonsCracked = 0;
    private int balloonsMissed = 0;
    private Target target;
    private Timer timer;
    private boolean isRunning = true;
    public Board() {
        initBoard();
    }
    private void initBoard() {
        setPreferredSize(
                new Dimension(Board.BOARD_WIDTH , Board.BOARD_HEIGHT));
// hides cursor
        createBalloons();
        addMouseMotionListener(new MAdapter());
        addMouseListener(new MAdapter2());
        setBackground(Color.GRAY);
        Point p = MouseInfo.getPointerInfo().getLocation();
        timer = new Timer(PERIOD, new GameCycle());
        timer.start();
    }
    private void createBalloons() {
        balloons = new ArrayList <>();
        int startY = 1200;
        for (int i = 0; i < NUM_OF_BALLOONS; i++) {
            int x = new Random().nextInt(BOARD_WIDTH - BALLOON_WIDTH);
            int y = new Random().nextInt(startY) + BOARD_HEIGHT;
            var balloon = new Balloon(x, y);
            balloons.add(balloon);
        }
    }
    private void doGameCycle() {
        updateBalloons();
        repaint();
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isRunning) {
            doDrawing(g);
        } else {
            gameOver(g);
        }
        Toolkit.getDefaultToolkit().sync();
    }
    private void doDrawing(Graphics g) {
        var g2d = (Graphics2D) g;
        drawScore(g2d);
        for (Balloon balloon : balloons) {
            g2d.drawImage(balloon.getImage(), (int) balloon.getX(),
                    (int) balloon.getY(), this);
        }
        g2d.drawImage(target.getImage(), (int) target.getX(),
                (int) target.getY(), this);
    }
    private void updateBalloons() {
        for (int i = 0; i < balloons.size(); i++) {
            var balloon = balloons.get(i);
            double by = balloon.getY();
            if (by + BALLOON_HEIGHT < 0) {
                balloonsMissed++;
                balloon.setVisible(false);
            }
            if (balloon.isVisible()) {
                balloon.move();
            } else {
                balloons.remove(i);
                if (balloons.isEmpty()) {
                    timer.stop();
                    isRunning = false;
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        }
    }
    private void fire(int mx, int my) {
        for (int i = 0; i < balloons.size(); i++) {
            var balloon = balloons.get(i);
            double bx = balloon.getX();
            double by = balloon.getY();
            var ellipse = new Ellipse2D.Double(bx, by,
                    BALLOON_WIDTH , BALLOON_HEIGHT);
            if (ellipse.contains(mx, my)) {
                balloon.setVisible(false);
                balloonsCracked++;
            }
        }
    }
    private void drawScore(Graphics2D g2d) {
        g2d.setFont(new Font("Geneva", Font.BOLD, 12));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING ,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING ,
                RenderingHints.VALUE_RENDER_QUALITY);
        var label1 = String.format("Destroyed %d", balloonsCracked);
        g2d.drawString(label1, 5, BOARD_HEIGHT - 85);
        int nOfBalloons = balloons.size();
        var label2 = String.format("Left %d", nOfBalloons);
        g2d.drawString(label2, 5, BOARD_HEIGHT - 60);
        var label3 = String.format("Missed %d", balloonsMissed);
        g2d.drawString(label3, 5, BOARD_HEIGHT - 35);
    }
    private void gameOver(Graphics g) {
        var g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING ,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING ,
                RenderingHints.VALUE_RENDER_QUALITY);
        var msg = "Game Over";
        var msg2 = String.format("Cracked: %d missed: %d",
                balloonsCracked , balloonsMissed);
        var myFont = new Font("Geneva", Font.BOLD, 24);
        var fontMetrics = this.getFontMetrics(myFont);
        g.setFont(myFont);
        g.drawString(msg,
                (BOARD_WIDTH - fontMetrics.stringWidth(msg)) / 2,
                (BOARD_HEIGHT / 2) - fontMetrics.getHeight());
        g.drawString(msg2, (BOARD_WIDTH -
                        fontMetrics.stringWidth(msg2)) / 2,
                (BOARD_HEIGHT / 2) + fontMetrics.getHeight());
    }
    private class MAdapter extends MouseMotionAdapter {
        @Override
        public void mouseMoved(MouseEvent e) {
            target.mouseMoved(e);
        }
    }
    private class MAdapter2 extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            int mx = e.getX() + TARGET_WIDTH / 2;
            int my = e.getY() + TARGET_WIDTH / 2;
            fire(mx, my);
        }
    }
    private class GameCycle implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }
    }
}
