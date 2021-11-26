package com.pws.main_menu;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.pws.bad_guys.BadGuysMain;
import com.pws.common.MenuButton;
import com.pws.memory_two.MemoryMain;
import com.pws.whack_a_mole.Game;
import com.pws.whack_a_mole.HitEffect;


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

        JButton badGuysButton = MenuButton.createMenuButton("Bad guys", true);
        badGuysButton.setLocation(BOARD_WIDTH - badGuysButton.getWidth() - 2, BOARD_HEIGHT -  3 * badGuysButton.getHeight() - 10);
        badGuysButton.addActionListener(e -> {
            EventQueue.invokeLater(() -> {
                BadGuysMain ex = new BadGuysMain();
                ex.setVisible(true);
            });

        });

        JButton whacAMoleButton = MenuButton.createMenuButton("Whac-a-Mole", true);
        whacAMoleButton.setLocation(BOARD_WIDTH - whacAMoleButton.getWidth() - 2, BOARD_HEIGHT - 2 * whacAMoleButton.getHeight() - 6);
        whacAMoleButton.addActionListener(e -> {
            Game game = new Game();
            game.setVisible(true);
        });

        JButton memoryButton = MenuButton.createMenuButton("Memory", true);
        memoryButton.setLocation(BOARD_WIDTH - memoryButton.getWidth() - 2, BOARD_HEIGHT - memoryButton.getHeight() - 2);
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
