package com.pws.memory;


import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class memory_board extends JPanel {
    public final memory_game game;
    public final int NUM_IMAGES = 11;
    public final int CELL_SIZE = 150;
    public final int COVER_FOR_CELL = 10;
    public final int MARK_FOR_CELL = 10;
    public final int EMPTY_CELL = 0;
    public final int MINE_CELL = 12;
    public final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;
    public final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL;
    public final int N_ROWS = 5;
    public final int N_COLS = 4;
    public final int BOARD_WIDTH = N_ROWS * CELL_SIZE + 1;
    public final int BOARD_HEIGHT = N_COLS * CELL_SIZE + 1;
    public int[] field;
    public boolean inGame;
    public int picturesLeft;
    public Image[] img;
    public int allCells;
    public final JLabel statusbar;
    boolean isSecondPress = false;
    public ArrayList<Integer> fieldsLeft = new ArrayList<Integer>();
    public memory_board(memory_game mGame,JLabel statusbar) {
        this.game = mGame;
        this.statusbar = statusbar;
        initBoard();
    }

    public void initBoard() {
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        img = new Image[NUM_IMAGES];
        for (int i = 0; i < NUM_IMAGES; i++) {
            String path = "src/resources/shapes/" + i + ".png";
            Image image = (new ImageIcon(path)).getImage();
            Image realImage = image.getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_DEFAULT);
            img[i] = realImage;
        }
        addMouseListener(new TileAdapter());
        newGame();
        setBackground(Color.orange);
        setLayout(null);
    }

    public void newGame() {
        int cell;
        Random random = new Random();
        inGame = true;
        picturesLeft = NUM_IMAGES-1;
        allCells = N_ROWS * N_COLS;
        field = new int[allCells];

        for (int i = 0; i < allCells; i++) {
            fieldsLeft.add(i);
        }
       statusbar.setText(Integer.toString(picturesLeft));
            for (int j = 0; j < NUM_IMAGES; j++) {
                for (int k = 0; k < 2; k++) {
                    int l = random.nextInt(fieldsLeft.size());

                    int position = fieldsLeft.get(l);
                            field[position] = COVER_FOR_CELL + j;
                            fieldsLeft.remove(position);
                        }
                    }
                }




//    public void find_empty_cells(int j) {
//        int current_col = j % N_COLS;
//        int cell;
//        if (current_col > 0) {
//            cell = j - N_COLS - 1;
//            if (cell >= 0) {
//                if (field[cell] > MINE_CELL) {
//                    field[cell] -= COVER_FOR_CELL;
//                    if (field[cell] == EMPTY_CELL) {
//                        find_empty_cells(cell);
//                    }
//                }
//            }
//            cell = j - 1;
//            if (cell >= 0) {
//                if (field[cell] > MINE_CELL) {
//                    field[cell] -= COVER_FOR_CELL;
//                    if (field[cell] == EMPTY_CELL) {
//                        find_empty_cells(cell);
//                    }
//                }
//            }
//            cell = j + N_COLS - 1;
//            if (cell < allCells) {
//                if (field[cell] > MINE_CELL) {
//                    field[cell] -= COVER_FOR_CELL;
//                    if (field[cell] == EMPTY_CELL) {
//                        find_empty_cells(cell);
//                    }
//                }
//            }
//        }
//        cell = j - N_COLS;
//        if (cell >= 0) {
//            if (field[cell] > MINE_CELL) {
//                field[cell] -= COVER_FOR_CELL;
//                if (field[cell] == EMPTY_CELL) {
//                    find_empty_cells(cell);
//                }
//            }
//        }
//        cell = j + N_COLS;
//        if (cell < allCells) {
//            if (field[cell] > MINE_CELL) {
//                field[cell] -= COVER_FOR_CELL;
//                if (field[cell] == EMPTY_CELL) {
//                    find_empty_cells(cell);
//                }
//            }
//        }
//        if (current_col < (N_COLS - 1)) {
//            cell = j - N_COLS + 1;
//            if (cell >= 0) {
//                if (field[cell] > MINE_CELL) {
//                    field[cell] -= COVER_FOR_CELL;
//                    if (field[cell] == EMPTY_CELL) {
//                        find_empty_cells(cell);
//                    }
//                }
//            }
//            cell = j + N_COLS + 1;
//            if (cell < allCells) {
//                if (field[cell] > MINE_CELL) {
//                    field[cell] -= COVER_FOR_CELL;
//                    if (field[cell] == EMPTY_CELL) {
//                        find_empty_cells(cell);
//                    }
//                }
//            }
//            cell = j + 1;
//            if (cell < allCells) {
//                if (field[cell] > MINE_CELL) {
//                    field[cell] -= COVER_FOR_CELL;
//                    if (field[cell] == EMPTY_CELL) {
//                        find_empty_cells(cell);
//                    }
//                }
//            }
//        }
//    }


    @Override
    public void paintComponent(Graphics g) {
        int uncover = 0;
        for (int i = 0; i < N_COLS; i++) {
            for (int j = 0; j < N_ROWS; j++) {
                int cell = field[(i * N_COLS) + j];
                int cellValue = cell;
                if (cell > COVER_FOR_CELL) {
                    cellValue = 10;
                }
                g.drawImage(img[cellValue], (j * CELL_SIZE),
                        (i * CELL_SIZE), this);
            }
        }
        for (int i=0; i< field.length; i++){
            int cell = field [i];
            if (cell > COVER_FOR_CELL){
                uncover ++;
            }
        }
        if (uncover == 0 && inGame) {
            inGame = false;
            statusbar.setText("Game won");
        } else if (!inGame) {
            statusbar.setText("Game lost");
        }
    }

    public class TileAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {

            int x = e.getX();
            int y = e.getY();
            int cCol = y / CELL_SIZE;
            int cRow = x / CELL_SIZE;
            boolean doRepaint = false;
            if (!inGame) {
                newGame();
                repaint();
            }
            if ((x < N_ROWS * CELL_SIZE) && (y < N_COLS * CELL_SIZE)) {
//                    if (field[(cRow * N_COLS) + cCol]
//                            > COVERED_MINE_CELL) {
//                        return;
//                    }
                    if ((field[(cCol * N_COLS) + cRow] >= COVER_FOR_CELL)) {
                        field[(cCol * N_COLS) + cRow] -= COVER_FOR_CELL;
                        doRepaint = true;
                        if (!isSecondPress){

                        }
//                        if (field[(cRow * N_COLS) + cCol] == EMPTY_CELL) {
//                            find_empty_cells((cRow * N_COLS) + cCol);
//                        }
                    }
                }
                if (doRepaint) {
                    repaint();
                }
            }
        }
    }