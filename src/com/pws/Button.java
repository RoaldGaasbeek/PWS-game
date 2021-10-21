package com.pws;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Button extends Sprite {

    public static final int BUTTON_WIDTH = 140;
    public static final int BUTTON_HEIGHT = 50;
    public String Text;
    public static List<Button> buttons = new ArrayList<Button>();
    public Button(int x, int y, String text) {
        super(x, y);
        initButton();
        Text = text;
    }

    public String getText() {
        return Text;
    }

    public static List<Integer> createButtons(int amount, String text1, String text2, String text3, int boardWidth, int boardHeight) {
        List<Integer> indexNumbers = new ArrayList<Integer>();
        Random rand = new Random();
        int currentSize = buttons.size();
        for (int i = 0; i < amount; i++) {
            if (i == 0) {
//            buttons.add(new Button(BOARD_WIDTH - BUTTON_WIDTH, BOARD_HEIGHT - BUTTON_HEIGHT, "whac-a-mole"));
//            buttons.add(new Button(BOARD_WIDTH - BUTTON_WIDTH, BOARD_HEIGHT - (2 * BUTTON_HEIGHT), "memory"));
                buttons.add(new com.pws.Button(boardWidth - BUTTON_WIDTH, boardHeight - ((i + 1) * BUTTON_HEIGHT), text1));
                indexNumbers.add(currentSize);
            } else if (i == 1) {
                buttons.add(new com.pws.Button(boardWidth - BUTTON_WIDTH, boardHeight - ((i + 1) * BUTTON_HEIGHT), text2));
                indexNumbers.add(currentSize+1);
            } else if (i == 2) {
                buttons.add(new com.pws.Button(boardWidth - BUTTON_WIDTH, boardHeight - ((i + 1) * BUTTON_HEIGHT), text3));
                indexNumbers.add(currentSize+2);
            } else {
                buttons.add(new com.pws.Button(boardWidth - BUTTON_WIDTH, boardHeight - ((i + 1) * BUTTON_HEIGHT), "..."));
            }
        }
        return indexNumbers;

    }

    public static void drawButtonText(List<Integer> buttonlist, Graphics2D g2d) {
        for (int i = buttonlist.get(0); i < buttonlist.get(buttonlist.size()-1); i++) {
            Button button = buttons.get(i);
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
