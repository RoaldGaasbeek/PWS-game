package com.pws.main_menu;

import com.pws.bad_guys.BadGuysMain;
import com.pws.memory_two.MemoryMain;
import com.pws.Button;
import com.pws.whack_a_mole.Game;
import com.pws.whack_a_mole.HitEffect;
import com.pws.memory.memory_game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.pws.Button.BUTTON_HEIGHT;
import static com.pws.Button.BUTTON_WIDTH;


public class Menu_Board extends JPanel {
    protected static final int BOARD_WIDTH = 600;
    protected static final int BOARD_HEIGHT = 400;
    private final int FPS = 60;
    private final int PERIOD = 1000 / FPS;
    private HitEffect hitEffect;
    private Timer timer;
    private boolean isRunning = true;
    public Main_menu_picture main_menu_pic = new Main_menu_picture(20, 20);

    public Menu_Board() {
        initBoard();
    }

    private void initBoard() {
        setPreferredSize(new Dimension(Menu_Board.BOARD_WIDTH, Menu_Board.BOARD_HEIGHT));

        addMouseMotionListener(new MAdapter());
        addMouseListener(new MAdapter2());
        setBackground(Color.CYAN);
        setLayout(null);

        JButton badGuysButton = new JButton("play Bad guys");
        badGuysButton.setSize((BUTTON_WIDTH), BUTTON_HEIGHT);
        badGuysButton.setLocation(BOARD_WIDTH - (BUTTON_WIDTH ), BOARD_HEIGHT -  3* BUTTON_HEIGHT);
        badGuysButton.addActionListener(e -> {
            BadGuysMain game = new BadGuysMain();
            game.setVisible(true);
        });

        JButton whacAMoleButton = new JButton("play Whac-a-Mole");
        whacAMoleButton.setSize((BUTTON_WIDTH), BUTTON_HEIGHT);
        whacAMoleButton.setLocation(BOARD_WIDTH - (BUTTON_WIDTH), BOARD_HEIGHT - 2 * BUTTON_HEIGHT);
        whacAMoleButton.addActionListener(e -> {
            Game game = new Game();
            game.setVisible(true);
        });

        JButton memoryButton = new JButton("play Memory");
        memoryButton.setSize((BUTTON_WIDTH), BUTTON_HEIGHT);
        memoryButton.setLocation(BOARD_WIDTH - (BUTTON_WIDTH ), BOARD_HEIGHT -  BUTTON_HEIGHT);
        memoryButton.addActionListener(e -> {
            MemoryMain memory = new MemoryMain();
            memory.setVisible(true);
        });

        add(badGuysButton);
        add(memoryButton);
        add(whacAMoleButton);

        Point p = MouseInfo.getPointerInfo().getLocation();
        hitEffect = new HitEffect(p.getX(), p.getY());

        timer = new Timer(PERIOD, new GameCycle());
        timer.start();

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (isRunning) {
            doDrawing(g);
        } else {

        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(main_menu_pic.getImage(), (int) main_menu_pic.getX(), (int) main_menu_pic.getY(), this);
    }

    private void hit(int mx, int my) {

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
            hit(e.getX(), e.getY());
        }
    }

    private class GameCycle implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {


        }
    }
}
