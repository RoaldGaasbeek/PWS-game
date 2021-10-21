package com.pws.whack_a_mole;

import com.pws.Button;
import com.pws.main_menu.Menu_Board;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.lang.Math;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;


import static com.pws.Button.BUTTON_HEIGHT;
import static com.pws.Button.BUTTON_WIDTH;
import static com.pws.whack_a_mole.Mole.MOLE_HEIGHT;
import static com.pws.whack_a_mole.Mole.MOLE_WIDTH;
import static com.zetcode.SmallestInArray.getSmallest;


public class Board extends JPanel {
    protected static final int BOARD_WIDTH = 600;
    protected static final int BOARD_HEIGHT = 400;
    private final int TARGET_WIDTH = 24;
    private final int FPS = 60;
    private final int PERIOD = 1000 / FPS;
    private final int MAXIMUM_MOLES = 2;
    private final Game game;
    public int moles_on_screen = 9;
    private int molesWhacked = 0;
    private HitEffect hitEffect;
    private Timer timer;
    private boolean isRunning = true;
    private List<Mole> moles = new ArrayList<>();
    private List<com.pws.Button> buttons = new ArrayList<>();
    private Random rand = new Random();
    public double SECONDS_PASSED;
    public double SECONDS_REMAINING;
    public double GAME_LENGTH = 60;
    private JButton replayButton;
    private JButton mainMenuButton;

    public Board(Game game) {
        this.game = game;
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
        setBackground(Color.gray);
        setLayout(null);

        Point p = MouseInfo.getPointerInfo().getLocation();
        hitEffect = new HitEffect(p.getX(), p.getY());

        replayButton = createReplayButton();
        replayButton.setVisible(false);
        mainMenuButton = createMainMenuButton();
        mainMenuButton.setVisible(false);

        add(replayButton);
        add(mainMenuButton);

        timer = new Timer(PERIOD, new GameCycle());
        timer.start();
    }

    private void createMoles() {
        SECONDS_PASSED = 0;
        SECONDS_REMAINING = GAME_LENGTH;
//        moles.add(new Mole(rand.nextInt(BOARD_WIDTH - MOLE_WIDTH), rand.nextInt(BOARD_HEIGHT - MOLE_HEIGHT)));

        for (int row = 1; row <= 3; row++) {
            for (int column = 1; column <= 3; column++) {
//                moles.add(new Mole(rand.nextInt(BOARD_WIDTH - MOLE_WIDTH), rand.nextInt(BOARD_HEIGHT - MOLE_HEIGHT)));
                moles.add(new Mole(20 + row * MOLE_WIDTH + row * 20, 20 + column * MOLE_HEIGHT + column * 20, rand.nextInt(251)));
            }
        }
    }

    private void initialiseMoles() {
        SECONDS_PASSED = 0;
        SECONDS_REMAINING = GAME_LENGTH;


        int index = 0;
        for (int row = 1; row <= 3; row++) {
            for (int column = 1; column <= 3; column++) {
                Mole mole = moles.get(index);
                mole.setXY(20 + row * MOLE_WIDTH + row * 20, 20 + column * MOLE_HEIGHT + column * 20);
                mole.lifespan = rand.nextInt( 251);
                index++;
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
        drawTimer(g2d);
        for (Mole mole : moles) {
            if (mole.isVisible()) {
                g2d.drawImage(mole.getImage(), (int) mole.getX(), (int) mole.getY(), this);
            }
        }
        g2d.drawImage(hitEffect.getImage(), (int) hitEffect.getX(), (int) hitEffect.getY(), this);
    }

    private void updateMoles() {
        for (int i = 0; i < moles.size(); i++) {
            Mole mole = moles.get(i);
            mole.lifespan++;

            if (mole.isVisible()) {

                if (mole.lifespan > 120) {
                    mole.setVisible(false);
                    mole.setXY(giveCoordinates("x"), giveCoordinates("y"));
                    mole.lifespan = 0;
                    moles_on_screen--;
                    repaint();
                }
            } else {
                if (mole.lifespan > 250) {
                    if (moles_on_screen <= MAXIMUM_MOLES) {
                        mole.setVisible(true);
                        mole.lifespan = 0;
                        moles_on_screen++;
                        repaint();
                    }
                    mole.lifespan = 0;
                }
            }
        }
    }

    private void updateInGameTimer() {
        SECONDS_PASSED = (SECONDS_PASSED + ((1 / (double) FPS)));
        SECONDS_REMAINING = (GAME_LENGTH - SECONDS_PASSED);
        if (SECONDS_REMAINING <= 0) {
            isRunning = false;
            setCursor(Cursor.getDefaultCursor());
        }
    }

    private int giveCoordinates(String xory) {
        int randX = 200;
        int randY = 200;
        int dx = 1000;
        int dy = 1000;
        ArrayList<Integer> dxs = new ArrayList<Integer>();
        ArrayList<Integer> dys = new ArrayList<Integer>();
        while ((((int) Math.sqrt((dx * dx) + (dy * dy))) > (MOLE_WIDTH + MOLE_HEIGHT)) || (randX < 30 && randY < 60)) {
            dxs.clear();
            dys.clear();
            randX = rand.nextInt(BOARD_WIDTH - MOLE_WIDTH);
            randY = rand.nextInt(BOARD_HEIGHT - MOLE_HEIGHT);
            for (int a = 0; a < moles.size(); a++) {
                Mole mole1 = moles.get(a);
                dxs.add((int) Math.abs((randX - mole1.getX())));
                dys.add(Math.abs((int) (randY - mole1.getY())));
            }
            dx = (getSmallest(dxs, 9));
            dy = (getSmallest(dys, 9));
        }
        if (xory == "x") {
            return (randX);
        } else {
            return (randY);
        }
    }

    private void whack(int mx, int my) {
        for (int i = 0; i < moles.size(); i++) {
            Mole mole = moles.get(i);

            Ellipse2D ellipse = new Ellipse2D.Double(mole.getX(), mole.getY(), MOLE_WIDTH, MOLE_HEIGHT);

            if (ellipse.contains(mx, my) && mole.isVisible()) {
                molesWhacked++;
                moles_on_screen--;
                mole.setXY(giveCoordinates("x"), giveCoordinates("y"));
//                moles.add(new Mole(rand.nextInt(BOARD_WIDTH - MOLE_WIDTH), rand.nextInt(BOARD_HEIGHT - MOLE_HEIGHT)));
                mole.setVisible(false);
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
        showMoles(false);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        String msg = "Game Over";
        String msg2 = String.format("Moles hit: %d", molesWhacked);
        Font myFont = new Font("Geneva", Font.BOLD, 24);
        FontMetrics fontMetrics = this.getFontMetrics(myFont);
        g.setFont(myFont);
        g.drawString(msg,
                (BOARD_WIDTH - fontMetrics.stringWidth(msg)) / 2,
                (BOARD_HEIGHT / 2) - fontMetrics.getHeight());
        g.drawString(msg2, (BOARD_WIDTH -
                        fontMetrics.stringWidth(msg2)) / 2,
                (BOARD_HEIGHT / 2) + fontMetrics.getHeight());
        mainMenuButton.setVisible(true);
        replayButton.setVisible(true);
    }

    private void showMoles(boolean show) {
        for (int i = 0; i < moles.size(); i++) {
            Mole mole = moles.get(i);
            mole.setVisible(show);
        }
    }

    private JButton createReplayButton() {
        JButton replayButton = new JButton("play again");
        replayButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        replayButton.setLocation(BOARD_WIDTH - (BUTTON_WIDTH), BOARD_HEIGHT - 2 * BUTTON_HEIGHT);
        replayButton.addActionListener(e -> {
            molesWhacked = 0;
            setCursor(getToolkit().createCustomCursor(
                    new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
                    new Point(0, 0),
                    null));
            moles_on_screen = 9;
            isRunning = true;

            initialiseMoles();

            showMoles(true);


            replayButton.setVisible(false);
            mainMenuButton.setVisible(false);

        });
        return replayButton;
    }
    private JButton createMainMenuButton() {
        JButton mainMenuButton = new JButton("back to menu");
        mainMenuButton.setSize((BUTTON_WIDTH), BUTTON_HEIGHT);
        mainMenuButton.setLocation(BOARD_WIDTH - (BUTTON_WIDTH ), BOARD_HEIGHT - BUTTON_HEIGHT);
        mainMenuButton.addActionListener(e -> {
            game.setVisible(false);


            mainMenuButton.setVisible(false);

        });
        return mainMenuButton;
    }

    private void drawTimer(Graphics2D g2d) {
        g2d.setFont(new Font("Geneva", Font.BOLD, 12));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        String label1 = String.format("Time left: %d", (int) SECONDS_REMAINING);
        g2d.drawString(label1, 10, 30);
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
            if (isRunning) {
                showMole();
                updateInGameTimer();
            }
        }
    }
}
