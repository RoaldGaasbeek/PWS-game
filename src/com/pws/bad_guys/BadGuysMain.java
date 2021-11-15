package com.pws.bad_guys;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.LINE_START;
import static java.awt.BorderLayout.NORTH;
import static javax.swing.JSplitPane.TOP;

public class BadGuysMain extends JFrame {

    public BadGuysMain() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        Board board = new Board(getLayout(), getContentPane(), this);
        board.initBoard();
//        add(board);
        add(CENTER, board);

       // add(NORTH, new JButton("TOP"));

        setTitle("Jumping");
        setSize(500, 600);

        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = getContentPane();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            BadGuysMain ex = new BadGuysMain();
            ex.setVisible(true);
        });
    }
}