package com.pws.main_menu;

import javax.swing.*;
import java.awt.*;

public class Activate_menu extends JFrame {

    public Activate_menu() {

        initUI();
    }

    private void initUI() {

        Menu_Board menu_board = new Menu_Board();
        add(menu_board);

        setResizable(false);

        pack();

        setTitle("PWS-Games");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            Activate_menu activate_menu = new Activate_menu();
            activate_menu.setVisible(true);
        });
    }
}