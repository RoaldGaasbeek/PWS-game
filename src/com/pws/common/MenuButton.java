package com.pws.common;

import javax.swing.*;
import java.awt.*;


public class MenuButton {

    public static JButton createMenuButton(String caption, boolean visible) {
        ImageIcon ii = new ImageIcon("src/resources/menu-button.png");

        JButton newButton = new JButton(caption, ii);
        newButton.setSize(ii.getIconWidth(), ii.getIconHeight());
        newButton.setRolloverEnabled(true);
        newButton.setRolloverIcon(ii);
        newButton.setContentAreaFilled(false);
        newButton.setBorderPainted(false);
        newButton.setHorizontalTextPosition(JButton.CENTER);
        newButton.setVerticalTextPosition(JButton.CENTER);
        newButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newButton.setFocusPainted(false);
        newButton.setVisible(visible);
        newButton.setAlignmentY(JButton.BOTTOM_ALIGNMENT);
        return newButton;
    }
}
