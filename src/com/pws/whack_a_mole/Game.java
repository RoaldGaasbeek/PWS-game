package com.pws.whack_a_mole;


import javax.swing.*;
import java.awt.*;


public class Game extends JFrame {

    public Game() {

        initUI();
    }

    private void initUI() {

        Board board = new Board(this);
        add(board);

        setResizable(false);

        pack();

        setTitle("Whac-a-mole");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }


    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            Game game = new Game();
            game.setVisible(true);
        });
    }
}
