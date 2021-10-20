package com.pws.main_menu;

import com.pws.whack_a_mole.Board;
import com.pws.whack_a_mole.Game;
import com.pws.whack_a_mole.HitEffect;
import com.pws.main_menu.Main_menu_picture;
import com.pws.whack_a_mole.Mole;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.pws.main_menu.Main_menu_picture.PICTURE_HEIGHT;
import static com.pws.main_menu.Main_menu_picture.PICTURE_WIDTH;
import static com.pws.main_menu.Button.BUTTON_HEIGHT;
import static com.pws.main_menu.Button.BUTTON_WIDTH;
import static com.pws.whack_a_mole.Mole.MOLE_HEIGHT;
import static com.pws.whack_a_mole.Mole.MOLE_WIDTH;


public class Menu_Board extends JPanel {
    protected static final int BOARD_WIDTH = 600;
    protected static final int BOARD_HEIGHT = 400;
    private final int NUM_OF_BALLOONS = 20;
    private final int TARGET_WIDTH = 24;
    private final int PERIOD = 1000 / 60;
    private int molesHit = 0;
    private HitEffect hitEffect;
    private Timer timer;
    private boolean isRunning = true;
    private int lifespan;
    private List<Button> buttons = new ArrayList<>();

    public Menu_Board() {
        initBoard();
    }

    private void initBoard() {
        setPreferredSize(new Dimension(Menu_Board.BOARD_WIDTH, Menu_Board.BOARD_HEIGHT));


        addMouseMotionListener(new MAdapter());
        addMouseListener(new MAdapter2());
        setBackground(Color.CYAN);

        Point p = MouseInfo.getPointerInfo().getLocation();
        hitEffect = new HitEffect(p.getX(), p.getY());

        timer = new Timer(PERIOD, new GameCycle());
        timer.start();

    }
    private void createButtons() {
        Random rand = new Random();

//        moles.add(new Mole(rand.nextInt(BOARD_WIDTH - MOLE_WIDTH), rand.nextInt(BOARD_HEIGHT - MOLE_HEIGHT)));
        buttons.add(new Button(BOARD_WIDTH-BUTTON_WIDTH, BOARD_HEIGHT-BUTTON_HEIGHT, "whack-a-mole" ));
        buttons.add(new Button(BOARD_WIDTH-BUTTON_WIDTH, BOARD_HEIGHT-(2* BUTTON_HEIGHT), "memory" ));

    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // For now we only have one mole.
        //Mole mole = moles.get(0);

        for (Button button : buttons) {
            g2d.drawImage(button.getImage(), (int) button.getX(), (int) button.getY(), this);
        }

        if (isRunning) {
            doDrawing(g);
        } else {

        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        drawButtonText(g2d);

    }
    private void hit(int mx, int my) {
        for (int i = 0; i < buttons.size(); i++) {
            Button button = buttons.get(i);
            String butText = button.getText();
            Rectangle2D rectangle = new Rectangle2D.Double(button.getX(), button.getY(), BUTTON_WIDTH, BUTTON_HEIGHT);

            if (rectangle.contains(mx, my)) {
//                mole.setVisible(false);
                if (butText == "whack-a-mole") {
                    Random rand = new Random();
                    com.pws.whack_a_mole.Game game = new com.pws.whack_a_mole.Game();
                    game.setVisible(true);
                }



//                moles.add(new Mole(rand.nextInt(BOARD_WIDTH - MOLE_WIDTH), rand.nextInt(BOARD_HEIGHT - MOLE_HEIGHT)));
            }
        }
    }
    private void drawButtonText(Graphics2D g2d) {
        for (int i = 0; i < buttons.size(); i++) {
            Button button = buttons.get(i);
            g2d.setFont(new Font("Geneva", Font.BOLD, 12));
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            String label1 = String.format("play %d", button.getText());
            g2d.drawString(label1, (int) button.getX(), (int) button.getdY());
        }
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
