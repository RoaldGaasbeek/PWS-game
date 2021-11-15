package com.pws.bad_guys;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Board extends JPanel {

    private static final int TIMER_DELAY = 2000;
    private static final int TIMER_DELAY_SHORT = 200;

    private final Random random;
    private final LayoutManager layout;
    private final Container contentPane;
    private final BadGuysMain main;
    private Guy jumper;
    private JLabel thiefsLabel = new JLabel("Bad guys hit: ");
    private JLabel scoreLabel = new JLabel("0");
    private int score = 0;
    private JLabel resultLabel = new JLabel("");
    private JTextArea startupMessageTextArea;
    private boolean isRunning = false;
    private int counter = 0;
    private int inactiveCounter = 0;

    private JPanel northPanel = new JPanel();
    private SpeedScore speedScore = new SpeedScore();
    private JPanel centerPanel = new JPanel();


    public Board(LayoutManager layout, Container contentPane, BadGuysMain main) {
        this.main = main;
        this.layout = layout;
        this.contentPane = contentPane;

        setLayout(new BorderLayout());

        jumper = new Guy(50, 50, true);
        random = new Random();

        setupStartText();
    }

    private void setupStartText() {
        startupMessageTextArea = new JTextArea();
        startupMessageTextArea.setWrapStyleWord(true);
        startupMessageTextArea.setLineWrap(true);
        startupMessageTextArea.setAlignmentY(10);
        startupMessageTextArea.setTabSize(4);
        startupMessageTextArea.setSize(350, 100);
        startupMessageTextArea.setEditable(false);
        startupMessageTextArea.setText("\n\n" +
                "  Your challenge, if you choose to accept, is to\n" +
                "  hit as many bad guys as you can within 30 seconds.\n\n" +
                "  By the way, don't hit the good guys!\n\n" +
                "\tAccept mission (spacebar)\n" +
                "\tChicken out (any other key)\n" +
                "\n\n");


    }

    public void initBoard() {
        addKeyListener(new TAdapter());
        addMouseListener(new MyMouseListener());
        addMouseMotionListener(new MyMouseMotionListener());
        setBackground(Color.GREEN);
        setFocusable(true);

        setGameVisibility();

        northPanel.setLayout(new FlowLayout());
        northPanel.add(thiefsLabel);
        northPanel.add(scoreLabel);
        northPanel.add(resultLabel);

        add(BorderLayout.NORTH, northPanel);

        add(BorderLayout.EAST, speedScore);

        ImageIcon ii = new ImageIcon("src/resources/bad-guys/blue.png");
        ImageIcon iiPressed = new ImageIcon("src/resources/bad-guys/thief-40x40.png");

        JButton startButton = new JButton("Bad Guys", ii);
        startButton.setSize(ii.getIconWidth(), ii.getIconHeight());
        startButton.setPressedIcon(iiPressed);
        //startButton.setMaximumSize(new Dimension(150, 40));
        //startButton.setBackground(Color.green);
        //startButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        //startButton.setIconTextGap(20);
        //startButton.setBorder(BorderFactory.createLineBorder(Color.darkGray, 2));
//        startButton.setHorizontalAlignment(SwingConstants.LEADING);
//        startButton.setRolloverEnabled(true);
        startButton.setRolloverIcon(ii);
//        startButton.setBounds(100,60,120,50);
        startButton.setPreferredSize(new Dimension(ii.getIconWidth(), ii.getIconHeight()));
        startButton.setOpaque(false);
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        startButton.setHorizontalTextPosition(JButton.CENTER);
        startButton.setVerticalTextPosition(JButton.CENTER);


        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));
        centerPanel.add(startupMessageTextArea);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(startButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

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

    private void setGameVisibility() {
        jumper.setVisible(isRunning);
        northPanel.setVisible(isRunning);
        speedScore.setVisible(isRunning);
        centerPanel.setVisible(!isRunning);
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (isRunning) {
            if (jumper.isVisible()) {
                drawJumper(g);
            }
        }

        Toolkit.getDefaultToolkit().sync();
    }


    private void drawJumper(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(jumper.getImage(), jumper.getX(),
                jumper.getY(), this);
    }


    private class MyActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (!isRunning) {
                return;
            }

            if ("JUMPER_SLOW".equals(actionEvent.getActionCommand())) {
                setJumperPositionAndRepaint(true);
            } else if ("JUMPER_FAST".equals(actionEvent.getActionCommand())) {
                setJumperPositionAndRepaint(false);
            }

        }
    }

    private class MyShortActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (!isRunning) {
                return;
            }

            if ("RESULT LABEL".equals(actionEvent.getActionCommand())) {
                //resultLabel.setVisible(false);

                //setJumperPostionAndRepaint(false);
            }
        }
    }

    private void setJumperPositionAndRepaint(boolean forced) {
        boolean newBadGuy = forced;

        if (!forced) {
            counter++;

              if (!jumper.isVisible()) {
                if (counter > 10) {
                    jumper.setVisible(true);
                    counter = random.nextInt(7);

                    newBadGuy = true;
                }
            }
        }

        if (newBadGuy) {
            int newX = random.nextInt(400 - Guy.WIDTH);
            int newY = random.nextInt(400 - Guy.HEIGHT);
            jumper.setXY(newX, newY);
            jumper.setVisible(true);
            jumper.determineType();
            repaint();
        }
    }

    private void handleHitAction() {
        if (!isRunning) {
            return;
        }
        if (jumper.isVisible()) {
            jumper.setVisible(false);

            if (jumper.isBadGuy()) {
                score += 10;
                resultLabel.setText(" SCORE! Hitting took " + jumper.getTime() + " ms");
                speedScore.processTime(jumper.getTime());
            } else {
                score -= 50;
                resultLabel.setText(" DON't HIT THE GOOD GUY!");
            }
            scoreLabel.setText("" + score);

            jumper.determineType();




            counter = random.nextInt(3);
            repaint();
        } else {
            score -= 5;
            scoreLabel.setText("" + score);
            resultLabel.setText("MISSED!");
        }

        resultLabel.setVisible(true);

    }


    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (isRunning) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    handleHitAction();
                }
            } else {

                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    isRunning = true;
                    setGameVisibility();
                    revalidate();
                } else {
                    System.exit(0);
                }
            }
        }
    }

    private class MyMouseMotionListener extends MouseMotionAdapter {

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