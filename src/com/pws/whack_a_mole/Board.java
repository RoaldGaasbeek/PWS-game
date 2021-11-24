package com.pws.whack_a_mole;

//import com.pws.Button;
//import com.pws.main_menu.Menu_Board;
//import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.lang.Math;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Objects;
import java.util.Random;


import static com.pws.Button.BUTTON_HEIGHT;
import static com.pws.Button.BUTTON_WIDTH;
import static com.pws.whack_a_mole.Mole.MOLE_HEIGHT;
import static com.pws.whack_a_mole.Mole.MOLE_WIDTH;
import static com.zetcode.SmallestInArray.getSmallest;


public class Board extends JPanel {
    private static final int BOARD_WIDTH = 600;
    private static final int BOARD_HEIGHT = 400;
    private final int FPS = 60;
    private final int PERIOD = 1000 / FPS;
    private final int MAXIMUM_MOLES = 3;
    private final Game game;
    private int molesOnScreen = 9;
    private int molesWhacked = 0;
    private HitEffect hitEffect;
    private Timer timer;
    private Timer inGameTimer;
    private boolean isRunning = true;
    private boolean isGameOver = false;
    private List<Mole> moles = new ArrayList<>();
//    private List<com.pws.Button> buttons = new ArrayList<>();
    private Random rand = new Random();
    private double SECONDS_PASSED;
    private double SECONDS_REMAINING;
    private double GAME_LENGTH = 60;
    private JButton replayButton;
    private JButton mainMenuButton;
    private int molesMissed = 0;
    private List<Double> lifespans = new ArrayList<>();

    Board(Game game) {
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
        inGameTimer = new Timer (100, new InGameTimer());
        timer = new Timer(PERIOD, new GameCycle());
        timer.start();
        inGameTimer.start();
    }

    private void createMoles() {
        SECONDS_PASSED = 0;
        SECONDS_REMAINING = GAME_LENGTH;

        for (int row = 1; row <= 3; row++) {
            for (int column = 1; column <= 3; column++) {
//                moles.add(new Mole(rand.nextInt(BOARD_WIDTH - MOLE_WIDTH), rand.nextInt(BOARD_HEIGHT - MOLE_HEIGHT)));
                moles.add(new Mole(20 + row * MOLE_WIDTH + row * 20, 20 + column * MOLE_HEIGHT + column * 20, rand.nextInt(400)));
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
                mole.lifespan = rand.nextInt( 200);
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

//        Graphics2D g2d = (Graphics2D) g;



        if (isRunning) {
            doDrawing(g);
        } else if (!isGameOver) {
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
        if (SECONDS_REMAINING <= 0) {
            isRunning = false;
            setCursor(Cursor.getDefaultCursor());
        }
        for (Mole mole : moles) {
            mole.lifespan++;

            if (mole.isVisible()) {

                if (mole.lifespan > 120) {
                    mole.setVisible(false);
                    giveCoordinates(mole);
                    mole.lifespan = 0;
                    molesOnScreen--;
                    molesMissed++;
                    repaint();
                }
            } else {
                if (mole.lifespan > 240) {
                    if (molesOnScreen <= MAXIMUM_MOLES) {
                        mole.setVisible(true);
                        mole.lifespan = 0;
                        molesOnScreen++;
                        repaint();
                    }else {
                        mole.lifespan = rand.nextInt(200);
                    }

                }
            }
        }
    }

    private void giveCoordinates(Mole mole) {

        boolean overlaps = true;
        int randX = 0;
        int randY = 0;

        while (overlaps) {
            randX = rand.nextInt(BOARD_WIDTH - MOLE_WIDTH - 80) + 40;
            randY = rand.nextInt(BOARD_HEIGHT - MOLE_HEIGHT - 80) + 40;

            overlaps = false;
            for (Mole mole1 : moles) {
                if (( mole1.getX() - MOLE_WIDTH < randX && randX < mole1.getX() + MOLE_WIDTH ) &&
                        ( mole1.getY() - MOLE_HEIGHT < randY && randY < mole1.getY() + MOLE_HEIGHT )) {

                    overlaps = true;
                    break;
                }

            }
        }

        mole.setXY(randX, randY);
    }


    private void whack(int mx, int my) {
        for (Mole mole : moles) {
            Ellipse2D ellipse = new Ellipse2D.Double(mole.getX(), mole.getY(), MOLE_WIDTH, MOLE_HEIGHT);

            if (ellipse.contains(mx, my) && mole.isVisible()) {
                molesWhacked++;
                lifespans.add((double) mole.lifespan);
                molesOnScreen--;
                giveCoordinates(mole);
                mole.setVisible(false);
            }
        }
    }

    private void gameOver(Graphics g) {

        isGameOver = true;

        showMoles(false);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        String msg = "Game Over";
        String msg2 = String.format("Moles whacked: %d", molesWhacked);

        double molesWhackedPerSecond = molesWhacked / GAME_LENGTH;
        String roundedWhacksPerSecond = String.format("%.2f", molesWhackedPerSecond);
        String msg3 = ("Moles per second: " + roundedWhacksPerSecond);
        String msg4 = ("moles missed: " + molesMissed);

        double totalLifespans = 0;
        for (Double d : lifespans) {
            totalLifespans += d;
        }

        double averageLifespan = totalLifespans/molesWhacked;
        averageLifespan = averageLifespan * 16 / 1000;  // 1000/FPS = 16 and divide by 1000 to get seconds
        String roundedAverageLifespan = String.format("%.2f", averageLifespan);
        String msg5 = ("average reaction speed: " + roundedAverageLifespan );

//        misclicks = totalClicks - molesMissed;

        writeResultsToFile(roundedWhacksPerSecond, roundedAverageLifespan);

        Font myFont = new Font("Geneva", Font.BOLD, 24);
        FontMetrics fontMetrics = this.getFontMetrics(myFont);
        g.setFont(myFont);
        drawMessage(g, msg, fontMetrics,-3);
        drawMessage(g, msg2, fontMetrics,-1);
        drawMessage(g, msg3, fontMetrics,0);
        drawMessage(g, msg4, fontMetrics,1);
        drawMessage(g, msg5, fontMetrics,2);

        mainMenuButton.setVisible(true);
        replayButton.setVisible(true);

    }

    private static final String RESULTS_FILENAME = "Whac-a-mole-results.csv";

    private void writeResultsToFile(String roundedWhacksPerSecond, String roundedAverageLifespan) {
        PrintWriter writer = null;
        File file = new File(RESULTS_FILENAME);
        try {
            if (file.createNewFile()) {
                writer = new PrintWriter(new FileWriter(RESULTS_FILENAME));
                // Write the header
                writer.println("Game time;hits;missed;Moles per second;average reaction speed"); //misclicks;
            } else {
                writer = new PrintWriter(new FileWriter(RESULTS_FILENAME, true));
            }
            writer.print(GAME_LENGTH + ";");
            writer.print(molesWhacked + ";");
            writer.print(molesMissed + ";");
//            writer.print(misclicks + ";");
            writer.print(roundedWhacksPerSecond + ";");
            writer.print(roundedAverageLifespan);
            writer.println();
        } catch (IOException e) {
            // Ignore error, just log the stacktrace
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }


    private void drawMessage(Graphics g, String msg, FontMetrics fontMetrics, int i) {
        g.drawString(msg,
                (BOARD_WIDTH - fontMetrics.stringWidth(msg)) / 2,
                (BOARD_HEIGHT / 2) + i * fontMetrics.getHeight());
    }

    private void showMoles(boolean show) {
        for (Mole mole : moles) {
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
            molesOnScreen = 9;
            isRunning = true;
            isGameOver = false;

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

    private void drawScore(Graphics2D g2d) {
        g2d.setFont(new Font("Geneva", Font.BOLD, 12));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        String label1 = String.format("Score %d", molesWhacked);
        g2d.drawString(label1, 10, 15);
    }
    private void drawTimer(Graphics2D g2d) {
        g2d.setFont(new Font("Geneva", Font.BOLD, 12));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        // String.format("Time left: %d", (int) SECONDS_REMAINING);
        String label1 = ("Time left: "+ (int) SECONDS_REMAINING);
        g2d.drawString( label1, 10, 30);
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

    private class InGameTimer implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SECONDS_PASSED = SECONDS_PASSED+ 0.1;
            SECONDS_REMAINING = (GAME_LENGTH - SECONDS_PASSED);

        }
    }

    private class GameCycle implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (isRunning) {
                showMole();
//                updateInGameTimer();
            }
        }
    }
}
