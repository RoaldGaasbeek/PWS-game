package com.pws.memory;


import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.*;


public class memory_board extends JPanel {
    public final int NUM_IMAGES = 13;
    public final int CELL_SIZE = 40;
    public final int COVER_FOR_CELL = 10;
    public final int MARK_FOR_CELL = 10;
    public final int EMPTY_CELL = 0;
    public final int MINE_CELL = 9;
    public final int COVERED_MINE_CELL = MINE_CELL
            + COVER_FOR_CELL;
    public final int MARKED_MINE_CELL = COVERED_MINE_CELL
            + MARK_FOR_CELL;
    public final int DRAW_MINE = 9;
    public final int DRAW_COVER = 10;
    public final int DRAW_MARK = 11;
    public final int DRAW_WRONG_MARK = 12;
    public final int N_MINES = 40;
    public final int N_ROWS = 4;
    public final int N_COLS = 5;
    public final int BOARD_WIDTH = N_ROWS * CELL_SIZE + 1;
    public final int BOARD_HEIGHT = N_COLS * CELL_SIZE + 1;
    public int[] field;
    public boolean inGame;
    public int minesLeft;
    public Image[] img;
    public int allCells;
    public final JLabel statusbar;
    public final static boolean AmandoIsABigBrain = true;
    public final static boolean JipIsADummyDumbDumb = true;
//was all private in the minesweeper example changed to public for MinesAdapter class

    public memory_board(JLabel statusbar) {
        this.statusbar = statusbar;
        initBoard();
    }

    public void initBoard() {
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        img = new Image[NUM_IMAGES];
        for (int i = 0; i < NUM_IMAGES; i++) {
            var path = "src/resources/shapes" + i + ".png";
            img[i] = (new ImageIcon(path)).getImage();
        }
        addMouseListener(new MinesAdapter());
        newGame();
    }

    public void newGame() {
        int cell;
        var random = new Random();
        inGame = true;
        minesLeft = N_MINES;
        allCells = N_ROWS * N_COLS;
        field = new int[allCells];
        for (int i = 0; i < allCells; i++) {
            field[i] = COVER_FOR_CELL;
        }
        statusbar.setText(Integer.toString(minesLeft));
        int i = 0;
        while (i < allCells) {
            for (int j = 0; j < img.length; j++) {
                for (int k = 0; k < 2; k++) {
                    int position = random.nextInt(allCells + 1);
                    if ((position < allCells)
                            && (field[position] < COVER_FOR_CELL)) {
                        int current_col = position % N_COLS;
                        field[position] = COVER_FOR_CELL + j;
                        i++;
                    }
                }
            }
        }
    }

    public void find_empty_cells(int j) {
        int current_col = j % N_COLS;
        int cell;
        if (current_col > 0) {
            cell = j - N_COLS - 1;
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }
            cell = j - 1;
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }
            cell = j + N_COLS - 1;
            if (cell < allCells) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }
        }
        cell = j - N_COLS;
        if (cell >= 0) {
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL) {
                    find_empty_cells(cell);
                }
            }
        }
        cell = j + N_COLS;
        if (cell < allCells) {
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL) {
                    find_empty_cells(cell);
                }
            }
        }
        if (current_col < (N_COLS - 1)) {
            cell = j - N_COLS + 1;
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }
            cell = j + N_COLS + 1;
            if (cell < allCells) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }
            cell = j + 1;
            if (cell < allCells) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        int uncover = 0;
        for (int i = 0; i < N_ROWS; i++) {
            for (int j = 0; j < N_COLS; j++) {
                int cell = field[(i * N_COLS) + j];
                if (inGame && cell == MINE_CELL) {
                    inGame = false;
                }
                if (!inGame) {
                    if (cell == COVERED_MINE_CELL) {
                        cell = DRAW_MINE;
                    } else if (cell == MARKED_MINE_CELL)
                        cell = DRAW_MARK;
                } else if (cell > COVERED_MINE_CELL) {
                    cell = DRAW_WRONG_MARK;
                } else if (cell > MINE_CELL) {
                    cell = DRAW_COVER;
                } else {
                    if (cell > COVERED_MINE_CELL) {
                        cell = DRAW_MARK;
                    } else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                        uncover++;
                    }
                }
                g.drawImage(img[cell], (j * CELL_SIZE),
                        (i * CELL_SIZE), this);
            }
        }
        if (uncover == 0 && inGame) {
            inGame = false;
            statusbar.setText("Game won");
        } else if (!inGame) {
            statusbar.setText("Game lost");
        }
    }

    public class MinesAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            int cCol = x / CELL_SIZE;
            int cRow = y / CELL_SIZE;
            boolean doRepaint = false;
            if (!inGame) {
                newGame();
                repaint();
            }
            if ((x < N_COLS * CELL_SIZE) && (y < N_ROWS * CELL_SIZE)) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (field[(cRow * N_COLS) + cCol] > MINE_CELL) {
                        doRepaint = true;
                        Label statusbar = new Label();
                        if (field[(cRow * N_COLS) + cCol]
                                <= COVERED_MINE_CELL) {
                            if (minesLeft > 0) {
                                field[(cRow * N_COLS) + cCol]
                                        += MARK_FOR_CELL;
                                minesLeft--;
                                var msg = Integer.toString(minesLeft);
                                statusbar.setText(msg);
                            } else {
                                statusbar.setText("No marks left");
                            }
                        } else {
                            field[(cRow * N_COLS) + cCol]
                                    -= MARK_FOR_CELL;
                            minesLeft++;
                            var msg = Integer.toString(minesLeft);
                            statusbar.setText(msg);
                        }
                    }
                } else {
                    if (field[(cRow * N_COLS) + cCol]
                            > COVERED_MINE_CELL) {
                        return;
                    }
                    if ((field[(cRow * N_COLS) + cCol] > MINE_CELL)
                            && (field[(cRow * N_COLS) + cCol]
                            < MARKED_MINE_CELL)) {
                        field[(cRow * N_COLS) + cCol]
                                -= COVER_FOR_CELL;
                        doRepaint = true;
                        if (field[(cRow * N_COLS) + cCol] == MINE_CELL) {
                            inGame = false;
                        }
                        if (field[(cRow * N_COLS) + cCol] == EMPTY_CELL) {
                            find_empty_cells((cRow * N_COLS) + cCol);
                        }
                    }
                }
                if (doRepaint) {
                    repaint();

                }
            }
        }


        private void find_empty_cells(int i) {
        }

        private void repaint() {
        }

    }
}