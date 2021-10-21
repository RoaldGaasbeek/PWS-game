package com.pws;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Button extends Sprite {

    public static final int BUTTON_WIDTH = 140;
    public static final int BUTTON_HEIGHT = 50;
    public String Text;

    public Button(int x, int y, String text) {
        super(x, y);
        initButton();
        Text = text;
    }

    public String getText() {
        return Text;
    }

    public static void createButtons(ArrayList<Button> list, int amount, String text1, String text2, String text3, int boardWidth, int boardHeight) {
        Random rand = new Random();
        for (int i = 0; i < amount; i++) {
            if (i == 0) {
//            buttons.add(new Button(BOARD_WIDTH - BUTTON_WIDTH, BOARD_HEIGHT - BUTTON_HEIGHT, "whac-a-mole"));
//            buttons.add(new Button(BOARD_WIDTH - BUTTON_WIDTH, BOARD_HEIGHT - (2 * BUTTON_HEIGHT), "memory"));
                list.add(new com.pws.Button(boardWidth - BUTTON_WIDTH, boardHeight - ((i + 1) * BUTTON_HEIGHT), text1));
            } else if (i == 1) {
                list.add(new com.pws.Button(boardWidth - BUTTON_WIDTH, boardHeight - ((i + 1) * BUTTON_HEIGHT), text2));
            } else if (i == 2) {
                list.add(new com.pws.Button(boardWidth - BUTTON_WIDTH, boardHeight - ((i + 1) * BUTTON_HEIGHT), text3));
            } else {
                list.add(new com.pws.Button(boardWidth - BUTTON_WIDTH, boardHeight - ((i + 1) * BUTTON_HEIGHT), "..."));
            }
        }
    }

    public static void drawButtonText(ArrayList<com.pws.Button> list, Graphics2D g2d) {
        for (int i = 0; i < list.size(); i++) {
            Button button = list.get(i);
            g2d.setFont(new Font("Geneva", Font.BOLD, 12));
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            String label1 = String.format(button.getText());
            g2d.drawString(label1, (int) button.getX(), (int) button.getY() + 25);

        }
    }

    private void initButton() {
        loadImage("src/resources/green button.png", BUTTON_WIDTH, BUTTON_HEIGHT);
        getImageDimensions();
    }
}
