package com.pws.memory;
import com.pws.memory.memory_board.*;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MinesAdapter extends MouseAdapter {
    private final int N_ROWS = 16;
    private final int N_COLS = 16;
    private final int MINE_CELL = 9;
    private final int CELL_SIZE = 15;
    private final int COVER_FOR_CELL = 10;
    private final int MARK_FOR_CELL = 10;
    private final int EMPTY_CELL = 0;
    private final int COVERED_MINE_CELL = MINE_CELL
            + COVER_FOR_CELL;
    private final int MARKED_MINE_CELL = COVERED_MINE_CELL
            + MARK_FOR_CELL;
    private int minesLeft;
    private int[] field;
    private boolean inGame;



    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int cCol = x / CELL_SIZE;
        int cRow = y / CELL_SIZE;
        boolean doRepaint = false;
        if (!inGame) {
            com.pws.memory.memory_board.newGame();
            repaint();
        }
        if ((x < N_COLS * CELL_SIZE) && (y < N_ROWS * CELL_SIZE)) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                if (field[(cRow * N_COLS) + cCol] > MINE_CELL) {
                    doRepaint = true;
                    Label statusbar = new Label() ;
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
    public class Minesweeper extends JFrame {
        private JLabel statusbar;
        public Minesweeper() {
            initUI();
        }
        private void initUI() {
            statusbar = new JLabel("");
            add(statusbar , BorderLayout.SOUTH);
            add(new Board(statusbar));
            setResizable(false);
            pack();
            setTitle("Minesweeper");
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        public static void main(String[] args) {
            EventQueue.invokeLater(() -> {
                var game = new Minesweeper();
                game.setVisible(true);
            });
        }
    }
}
