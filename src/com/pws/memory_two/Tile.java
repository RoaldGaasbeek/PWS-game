package com.ad.memory_two;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Tile extends JButton {
    public final static int WIDTH = 190;
    public final static int HEIGHT = 204;
    private final Board board;
    private final String imageNameBack;
    private boolean isShown = false;
    private int id;
    private String imageName;
    private ImageIcon backIcon;
    private ImageIcon frontIcon;


    public Tile(String imageName, String imageNameBack, int id, Board board) {
        this.id = id;
        this.imageName = imageName;
        this.imageNameBack = imageNameBack;

        this.board = board;

        init();
     }


    private void init() {
        frontIcon = new ImageIcon(imageName);
        backIcon = new ImageIcon(imageNameBack);

        setIcon(backIcon);
        setSize(backIcon.getIconWidth(), backIcon.getIconHeight());
        //setPressedIcon(frontIcon);
        //setRolloverIcon(backIcon);
        setPreferredSize(new Dimension(backIcon.getIconWidth(), backIcon.getIconHeight()));
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (isVisible() && !isShown) {
                    board.handleTileClick(Tile.this);
                }
            }
        });
    }

    public void doShow(boolean show) {
        this.isShown = show;

        if (show) {
            setIcon(frontIcon);
        } else {
            setIcon(backIcon);
        }
    }


    public boolean isShown() {
        return isShown;
    }

    public int getId() {
        return id;
    }
}