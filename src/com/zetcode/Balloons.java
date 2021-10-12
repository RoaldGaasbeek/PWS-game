package com.zetcode;

import java.awt.EventQueue;
import javax.swing.JFrame;


/**
 * Java Balloons game
 *
 * Author: Jan Bodnar
 * Website: http://zetcode.com
 */

public class Balloons extends JFrame {

    public Balloons() {

        initUI();
    }

    private void initUI() {

        Board board = new Board();
        add(board);

        setResizable(false);

        pack();

        setTitle("Balloons");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            Balloons game = new Balloons();
            game.setVisible(true);
        });
    }
}
