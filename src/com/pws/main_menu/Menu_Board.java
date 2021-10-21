package com.pws.main_menu;

import com.pws.Button;
import com.pws.whack_a_mole.HitEffect;

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
    private List<com.pws.Button> buttons = new ArrayList<>();
    public Main_menu_picture main_menu_pic = new Main_menu_picture(20, 20);
    public Menu_Board() {

        initBoard();
    }

    private void initBoard() {
        setPreferredSize(new Dimension(Menu_Board.BOARD_WIDTH, Menu_Board.BOARD_HEIGHT));
        createButtons((ArrayList<Button>) buttons, 2, "play Whac-a-Mole", "play Memory", " ");

        addMouseMotionListener(new MAdapter());
        addMouseListener(new MAdapter2());
        setBackground(Color.CYAN);

        Point p = MouseInfo.getPointerInfo().getLocation();
        hitEffect = new HitEffect(p.getX(), p.getY());

        timer = new Timer(PERIOD, new GameCycle());
        timer.start();

    }
    public static void createButtons(ArrayList<com.pws.Button> list,int amount, String text1, String text2, String text3) {
        Random rand = new Random();
        for (int i = 0; i < amount; i++ ) {
            if (i == 0) {
//            buttons.add(new Button(BOARD_WIDTH - BUTTON_WIDTH, BOARD_HEIGHT - BUTTON_HEIGHT, "whac-a-mole"));
//            buttons.add(new Button(BOARD_WIDTH - BUTTON_WIDTH, BOARD_HEIGHT - (2 * BUTTON_HEIGHT), "memory"));
                list.add(new com.pws.Button(BOARD_WIDTH - BUTTON_WIDTH, BOARD_HEIGHT - ((i + 1) * BUTTON_HEIGHT), text1));
            } else if (i == 1){
                list.add(new com.pws.Button(BOARD_WIDTH - BUTTON_WIDTH, BOARD_HEIGHT - ((i + 1) * BUTTON_HEIGHT), text2));
            } else if (i ==2){
                list.add(new com.pws.Button(BOARD_WIDTH - BUTTON_WIDTH, BOARD_HEIGHT - ((i + 1) * BUTTON_HEIGHT), text3));
            } else{
                list.add(new com.pws.Button(BOARD_WIDTH - BUTTON_WIDTH, BOARD_HEIGHT - ((i + 1) * BUTTON_HEIGHT), "..."));
            }
        }
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for (com.pws.Button button : buttons) {
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
        drawButtonText((ArrayList<Button>) buttons, g2d);
        g2d.drawImage(main_menu_pic.getImage(), (int) main_menu_pic.getX(), (int) main_menu_pic.getY(), this);
    }
    private void hit(int mx, int my) {
        for (int i = 0; i < buttons.size(); i++) {
            com.pws.Button button = buttons.get(i);
            String butText = button.getText();
            Rectangle2D rectangle = new Rectangle2D.Double(button.getX(), button.getY(), BUTTON_WIDTH, BUTTON_HEIGHT);

            if (rectangle.contains(mx, my)) {
                if (butText == "play Whac-a-Mole") {
                    com.pws.whack_a_mole.Game game = new com.pws.whack_a_mole.Game();
                    game.setVisible(true);
                }
            }
        }
    }
    public static void drawButtonText(ArrayList<com.pws.Button> list, Graphics2D g2d) {
        for (int i = 0; i < list.size(); i++) {
            Button button = list.get(i);
            g2d.setFont(new Font("Geneva", Font.BOLD, 12));
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            String label1 = String.format( button.getText());
            g2d.drawString(label1, (int) button.getX(), (int) button.getY() + 25);

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
