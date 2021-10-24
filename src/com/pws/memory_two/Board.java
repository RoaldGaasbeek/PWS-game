package com.pws.memory_two;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

public class Board extends JPanel {

    private List<Tile> tiles = new ArrayList<>();

    private boolean isRunning = true;
    private int tilesShown = 0;
    private int timerCounter = 40;
    private boolean matchFound = false;
    private MemorySet memorySet = MemorySet.POKEMON;
    private int numberOfTiles = 8;
    private Map<Integer, Integer> tilesNumberToColumns = new HashMap<>();
    private int numberOfTilesFound = 0;

    public Board() {
        setLayout(new GridLayout(2, 4));

        tilesNumberToColumns.put(8, 4);
        tilesNumberToColumns.put(16, 4);
        tilesNumberToColumns.put(24, 6);
        tilesNumberToColumns.put(30, 6);
        tilesNumberToColumns.put(36, 6);
    }


    public void initBoard() {
        setFocusable(true);

        createMenu();

        Timer timer = new Timer(50, new TimerActionListener());
        timer.start();
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();

        //Build the first menu.
        JMenu menu = new JMenu("Game");
        menu.setMnemonic(KeyEvent.VK_G);
        menu.getAccessibleContext().setAccessibleDescription("The game menu.");
        menuBar.add(menu);

        JMenu subMenu = createSubmenu(MemorySet.POKEMON, 36);
        menu.add(subMenu);

        subMenu = createSubmenu(MemorySet.NATURE, 16);
        menu.add(subMenu);


        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        topFrame.setJMenuBar(menuBar);
    }

    private JMenu createSubmenu(MemorySet memorySet, int maxTiles) {
        JMenu submenu = new JMenu(memorySet.name().toLowerCase());
        submenu.setActionCommand(memorySet.name());

        createTilesSizeSelectionMenu(submenu, maxTiles);

        return submenu;
    }


    private void createTilesSizeSelectionMenu(JMenu menu, int maxTiles) {
        ActionListener tilesActionListener = createNumberOfTilesActionListener();

        JMenuItem rbMenuItem;

        rbMenuItem = new JMenuItem("4x2");
        rbMenuItem.setActionCommand("8");
        rbMenuItem.setSelected(true);
        rbMenuItem.addActionListener(tilesActionListener);
        menu.add(rbMenuItem);

        if (16 > maxTiles) {
            return;
        }
        rbMenuItem = new JMenuItem("4x4");
        rbMenuItem.setActionCommand("16");
        rbMenuItem.addActionListener(tilesActionListener);
        menu.add(rbMenuItem);

        if (24 > maxTiles) {
            return;
        }
        rbMenuItem = new JMenuItem("6x4");
        rbMenuItem.setActionCommand("24");
        rbMenuItem.addActionListener(tilesActionListener);
        menu.add(rbMenuItem);

        if (30 > maxTiles) {
            return;
        }
        rbMenuItem = new JMenuItem("6x5");
        rbMenuItem.setActionCommand("30");
        rbMenuItem.addActionListener(tilesActionListener);
        menu.add(rbMenuItem);

        if (36 > maxTiles) {
            return;
        }
        rbMenuItem = new JMenuItem("6x6");
        rbMenuItem.setActionCommand("36");
        rbMenuItem.addActionListener(tilesActionListener);
        menu.add(rbMenuItem);
    }


    private ActionListener createNumberOfTilesActionListener() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    numberOfTiles = Integer.valueOf(e.getActionCommand());
                    JMenuItem menuItem = (JMenuItem) e.getSource();

                    JPopupMenu popupMenu = (JPopupMenu) menuItem.getParent();
                    JMenu parent = (JMenu) popupMenu.getInvoker();
                    memorySet = MemorySet.valueOf(parent.getActionCommand());

                    createTiles();
                    revalidate();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
    }


    private void createTiles() {
        removeAll();
        numberOfTilesFound = 0;

        List<Tile> myTiles = new ArrayList(numberOfTiles);

        if (MemorySet.POKEMON.equals(memorySet)) {
            addTiles(myTiles, ".jpg");
        } else if (MemorySet.NATURE.equals(memorySet)) {
            addTiles(myTiles, ".png");
        }

        int index;
        Random random = new Random();

        while (!myTiles.isEmpty()) {
            index = random.nextInt(myTiles.size());
            add(myTiles.get(index));
            myTiles.remove(index);
        }

        int columns = tilesNumberToColumns.get(numberOfTiles);
        int rows = numberOfTiles / columns;

        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

        if (MemorySet.POKEMON.equals(memorySet)) {
            topFrame.setSize((8 + 105) * columns, (38 + 90) * rows);
        } else if (MemorySet.NATURE.equals(memorySet)) {
            topFrame.setSize((8 + 200) * columns, (38 + 210) * rows);
        }

        GridLayout layout = (GridLayout) getLayout();
        layout.setColumns(columns);
        layout.setRows(rows);

        revalidate();
    }

    private void addTiles(List<Tile> myTiles, String fileType) {
        String setName = memorySet.name().toLowerCase();
        String path = "src/resources/memory/memory-" + setName + "/";
        String imageNameBack = path + "back.png";

        for (int i = 1; i <= (numberOfTiles / 2); i++) {
            String imageName = path + setName + "-" + String.valueOf(i) + fileType;

            myTiles.add(createTile(imageName, imageNameBack, i));
            myTiles.add(createTile(imageName, imageNameBack, i));
        }
    }

    private Tile createTile(String imageName, String imageNameBack, int number) {
        Tile tile = new Tile(imageName, imageNameBack, number, this);
        tiles.add(tile);

        return tile;
    }


    public void handleTileClick(Tile tile) {
        if (tilesShown < 2) {
            tile.doShow(true);

            if (tilesShown == 1) {
                handleMatchingTiles(tile);
            }

            timerCounter = 40;
            tilesShown++;
        }
    }

    private void handleMatchingTiles(Tile tile) {
        int id = tile.getId();
        for (Tile myTile : tiles) {
            if (myTile.isShown() && !myTile.equals(tile) && id == myTile.getId()) {
                matchFound = true;
                numberOfTilesFound += 2;

                break;
            }
        }
    }

    private void gameOver() {
        for (Tile tile : tiles) {
            tile.setVisible(true);
        }
    }

    private class TimerActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (tilesShown == 2) {
                timerCounter--;

                if (timerCounter <= 0) {
                    handleTileSelectionVisibility();
                    if (numberOfTilesFound == numberOfTiles) {
                        gameOver();
                    }
                }
            }
        }
    }

    private void handleTileSelectionVisibility() {
        for (Tile tile : tiles) {
            if (tile.isVisible() && tile.isShown()) {
                if (matchFound) {
                    tile.setVisible(false);
                } else {
                    tile.doShow(false);
                }
            }
        }

        matchFound = false;
        tilesShown = 0;
    }
}