package com.pws.bad_guys;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class Board extends JPanel {

    private static final int TIMER_DELAY = 2000;
    private static final int TIMER_DELAY_SHORT = 200;
    private static final int FINISH_SCORE = 100;
    private static final int GAME_DURATION = 30;
    private static final int GAME_DURATION_COUNT = GAME_DURATION / (TIMER_DELAY / 1000);
    private static final int SCORE_GOOD_GUY = 10;
    private static final int SCORE_BAD_GUY = 20;

    private final Random random;
    private final Guy guy;
    private final JLabel thievesLabel = new JLabel("");
    private final JLabel scoreLabel = new JLabel("");
    private int score = 0;
    private int badGuysHit = 0;
    private final JLabel resultLabel = new JLabel("");
    private boolean isRunning = false;
    private int counter = 0;
    private int gameCount = 0;

    private final  JPanel northPanel = new JPanel();
    private final SpeedScore speedScore = new SpeedScore();
    private final JPanel centerPanel = new JPanel();


    public Board() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 400));

        guy = new Guy(50, 50, true);
        random = new Random();
    }

    private JTextPane setupStartText() {
        JTextPane textPane = new JTextPane();
        textPane.setBackground(Color.getHSBColor(70f, 10f, 100f));
        textPane.setBorder(BorderFactory.createLineBorder(Color.black));

        SimpleAttributeSet attribs = new SimpleAttributeSet();
        StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_CENTER);
        textPane.setParagraphAttributes(attribs, true);
        textPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        textPane.setSize(350, 100);
        textPane.setEditable(false);
        textPane.setText("\n\n" +
            "  Your challenge, if you choose to accept, is to\n" +
            "  hit as many bad guys as you can within 30 seconds.\n\n" +
            "  By the way, don't hit the good guys!\n\n" +
            "\tAccept mission (spacebar)\n" +
            "\tChicken out (any other key)\n" +
            "\n\n");

        return textPane;
    }

    public void initBoard() {
        addKeyListener(new TAdapter());
        addMouseListener(new MyMouseListener());
        setBackground(Color.getHSBColor(70f, 10f, 100f));
        setFocusable(true);

        setGameVisibility();

        initNorthPanel();
        initCenterPanel();

        add(BorderLayout.NORTH, northPanel);
        add(BorderLayout.EAST, speedScore);
        add(BorderLayout.CENTER, centerPanel);

        setGameVisibility();
        revalidate();

        Timer timer = new Timer(TIMER_DELAY, new MyActionListener());
        timer.setActionCommand("JUMPER_SLOW");
        timer.start();

        Timer timer2 = new Timer(TIMER_DELAY_SHORT, new MyActionListener());
        timer2.setActionCommand("JUMPER_FAST");
        timer2.start();
    }

    private void initCenterPanel() {
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(setupStartText());
        centerPanel.add(createStartButton());
    }

    private void initNorthPanel() {
        northPanel.setLayout(new FlowLayout());
        northPanel.setBackground(Color.getHSBColor(70f, 10f, 100f));
        northPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        northPanel.add(thievesLabel);
        northPanel.add(scoreLabel);
        northPanel.add(resultLabel);
    }

    private JButton createStartButton() {
        ImageIcon ii = new ImageIcon("src/resources/bad-guys/blue-button.png");

        JButton startButton = new JButton("Bad Guys", ii);
        startButton.setSize(ii.getIconWidth(), ii.getIconHeight());
        startButton.setHorizontalAlignment(SwingConstants.LEADING);
        startButton.setRolloverEnabled(true);
        startButton.setRolloverIcon(ii);
        startButton.setPreferredSize(new Dimension(ii.getIconWidth(), ii.getIconHeight()));
        startButton.setOpaque(false);
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        startButton.setHorizontalTextPosition(JButton.CENTER);
        startButton.setVerticalTextPosition(JButton.CENTER);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                startGame();
            }
        });
        return startButton;
    }

    private void setGameVisibility() {
        guy.setVisible(isRunning);
        northPanel.setVisible(isRunning);
        speedScore.setVisible(isRunning);
        centerPanel.setVisible(!isRunning);
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (isRunning && guy.isVisible()) {
            drawJumper(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }


    private void drawJumper(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(guy.getImage(), guy.getX(),
            guy.getY(), this);
    }


    private class MyActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (!isRunning) {
                return;
            }

            if ("JUMPER_SLOW".equals(actionEvent.getActionCommand())) {
                gameCount++;
                if (gameCount > GAME_DURATION_COUNT) {
                    gameOver();
                }
                setJumperPositionAndRepaint(true);
            } else if ("JUMPER_FAST".equals(actionEvent.getActionCommand())) {
                setJumperPositionAndRepaint(false);
            }

        }
    }

    private void setJumperPositionAndRepaint(boolean forced) {
        boolean newBadGuy = forced;

        if (!forced) {
            counter++;

            if (!guy.isVisible() && counter > 10) {
                guy.setVisible(true);
                counter = random.nextInt(7);
                newBadGuy = true;
            }
        }

        if (newBadGuy) {
            int xOffset = speedScore.getWidth();
            int yOffset = northPanel.getHeight();
            int newX = random.nextInt(400 - Guy.WIDTH - xOffset);
            int newY = random.nextInt(400 - Guy.HEIGHT - yOffset);
            guy.setXY(newX, newY + yOffset);
            guy.setVisible(true);
            guy.determineType();
            repaint();
        }
    }

    private void handleHitAction() {
        if (!isRunning) {
            return;
        }
        if (guy.isVisible()) {
            guy.setVisible(false);

            if (guy.isBadGuy()) {
                score += SCORE_GOOD_GUY;
                badGuysHit++;
                updateThieves();
                updateResult(" |  SCORE! Hitting took " + guy.getTime() + " ms");
                speedScore.processTime(guy.getTime());
            } else {
                score -= SCORE_BAD_GUY;
                updateResult(" |  DON't HIT THE GOOD GUY!");
            }
            updateScore();

            guy.determineType();
            counter = random.nextInt(3);
            repaint();
        } else {
            score -= 5;
            updateScore();
            updateResult(" |  MISSED!");
        }

        resultLabel.setVisible(true);

        if (score >= FINISH_SCORE) {
            gameOver();
        }
    }

    private void updateResult(String text) {
        resultLabel.setText(text);
    }

    private void updateScore() {
        scoreLabel.setText(" |  Score: " + score);
    }

    private void gameOver() {
        isRunning = false;

        if (score >= FINISH_SCORE) {
            updateResult(" |  Game over. You reached the score in " + gameCount * 2 + " seconds.");
        } else {
            updateResult(" |  Game over. Time is up!");
        }
    }


    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if (isRunning) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    handleHitAction();
                }
            } else {

                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    startGame();
                } else {
                    System.exit(0);
                }
            }
        }
    }

    private void startGame() {
        isRunning = true;
        gameCount = 0;
        score = 0;
        badGuysHit = 0;
        updateThieves();
        updateScore();
        updateResult("");

        speedScore.init();

        setGameVisibility();
        revalidate();
    }

    private void updateThieves() {
        thievesLabel.setText("Bad guys hit: " + badGuysHit);
    }

    private class MyMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            if (!isRunning) {
                return;
            }

            handleHitAction();
        }

    }
}