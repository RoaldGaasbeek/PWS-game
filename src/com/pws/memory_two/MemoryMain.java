package com.pws.memory_two;

import javax.swing.*;
import java.awt.*;

import static java.awt.BorderLayout.CENTER;

public class MemoryMain extends JFrame {

    public MemoryMain() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        Board board = new Board(this);
        add(CENTER, board);

        // initBoard needs to be AFTER the board is added to the frame to resize properly.
        board.initBoard();

        setTitle("Memory");
        setSize(400, 400);

        setLocationRelativeTo(null);
//        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            MemoryMain ex = new MemoryMain();
            ex.setVisible(true);
        });
    }
}