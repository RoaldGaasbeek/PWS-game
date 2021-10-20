package com.pws.main_menu;

import com.pws.whack_a_mole.Board;
import com.pws.whack_a_mole.HitEffect;
import com.pws.main_menu.Main_menu_picture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.pws.main_menu.Main_menu_picture.PICTURE_HEIGHT;
import static com.pws.main_menu.Main_menu_picture.PICTURE_WIDTH;


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

    public Board() {
        initBoard();
    }

    private void initBoard() {
        setPreferredSize(new Dimension(Menu_Board.BOARD_WIDTH, Menu_Board.BOARD_HEIGHT));


        addMouseMotionListener(new MAdapter());
        addMouseListener(new MAdapter2());
        setBackground(Color.GRAY);

        Point p = MouseInfo.getPointerInfo().getLocation();
        hitEffect = new HitEffect(p.getX(), p.getY());

        timer = new Timer(PERIOD, new com.pws.whack_a_mole.Board.GameCycle());
        timer.start();
    }
