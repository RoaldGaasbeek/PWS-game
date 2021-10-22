package com.pws.memory;


import javax.swing.*;
import java.awt.*;

public class memory_game extends JFrame {
    private final JLabel statusbar= new JLabel("");
    public memory_game() {

        initUI();
    }
    private void initUI() {
        memory_board mBoard = new memory_board(this, statusbar);
        add(mBoard);


        add(statusbar , BorderLayout.SOUTH);
        setResizable(false);
        pack();
        setTitle("Memory");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            memory_game game = new memory_game();
            game.setVisible(true);
        });
    }
}

