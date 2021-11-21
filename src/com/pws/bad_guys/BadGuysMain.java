package com.pws.bad_guys;

import javax.swing.*;
import java.awt.*;

import static java.awt.BorderLayout.CENTER;


public class BadGuysMain extends JFrame {

    public BadGuysMain() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        Board board = new Board();
        board.initBoard();
        add(CENTER, board);

        setTitle("Hit the bad guy");
        setSize(600, 600);

        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            BadGuysMain ex = new BadGuysMain();
            ex.setVisible(true);
        });
    }
}