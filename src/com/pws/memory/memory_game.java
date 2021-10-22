package com.pws.memory;
//Jip is a poopiehead

import javax.swing.*;
import java.awt.*;

public class memory_game extends JFrame {
    private JLabel statusbar;
    public memory_game() {
        initUI();
    }
    private void initUI() {
        statusbar = new JLabel("");
        add(statusbar , BorderLayout.SOUTH);
        add(new memory_board(statusbar));
        setResizable(false);
        pack();
        setTitle("Minesweeper");
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

