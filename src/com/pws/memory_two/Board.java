package com.pws.memory_two;

import static com.pws.Button.BUTTON_HEIGHT;
import static com.pws.Button.BUTTON_WIDTH;
import static java.util.stream.Collectors.toList;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

import javax.swing.*;
import javax.swing.Timer;

public class Board extends JPanel {

    private final static int HGAP = 10;
    private final static int VGAP = 10;
    private final static int VMARGIN = 80;

    private List<Tile> tiles = new ArrayList<>();

    private int tilesShown = 0;
    private int timerCounter = 40;
    private boolean matchFound = false;
    private int numberOfTiles = 8;
    private Map<Integer, Integer> tilesNumberToColumns = new HashMap<>();
    private int numberOfTilesFound = 0;
    private int numberOfAttempts = 0;
    private Instant startTime;

    private final Random random = new Random();

    public Board() {
        setPreferredSize(new Dimension(600, 400));

        GridLayout layout = new GridLayout(2, 4);
        layout.setHgap(10);
        layout.setVgap(10);
        setLayout(layout);

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

        JMenu subMenu = createSubmenu(MemorySet.POKEMON);
        menu.add(subMenu);

        subMenu = createSubmenu(MemorySet.NATURE);
        menu.add(subMenu);

        subMenu = createSubmenu(MemorySet.POKEMON_GEN1);
        menu.add(subMenu);

        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        topFrame.setJMenuBar(menuBar);
    }

    private JMenu createSubmenu(MemorySet memorySet) {
        JMenu submenu = new JMenu(memorySet.name().toLowerCase());
        submenu.setActionCommand(memorySet.name());

        createTilesSizeSelectionMenu(submenu, memorySet);

        return submenu;
    }


    private void createTilesSizeSelectionMenu(JMenu menu, MemorySet memorySet) {
        ActionListener tilesActionListener = createNumberOfTilesActionListener();

        JMenuItem rbMenuItem;

        rbMenuItem = new JMenuItem("4x2");
        rbMenuItem.setActionCommand("8");
        rbMenuItem.setSelected(true);
        rbMenuItem.addActionListener(tilesActionListener);
        menu.add(rbMenuItem);

        if (8 > memorySet.getNumberOfTiles()) {
            return;
        }
        rbMenuItem = new JMenuItem("4x4");
        rbMenuItem.setActionCommand("16");
        rbMenuItem.addActionListener(tilesActionListener);
        menu.add(rbMenuItem);

        if (12 > memorySet.getNumberOfTiles()) {
            return;
        }
        rbMenuItem = new JMenuItem("6x4");
        rbMenuItem.setActionCommand("24");
        rbMenuItem.addActionListener(tilesActionListener);
        menu.add(rbMenuItem);

        if (15 > memorySet.getNumberOfTiles()) {
            return;
        }
        rbMenuItem = new JMenuItem("6x5");
        rbMenuItem.setActionCommand("30");
        rbMenuItem.addActionListener(tilesActionListener);
        menu.add(rbMenuItem);

        if (18 > memorySet.getNumberOfTiles()) {
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
                    MemorySet memorySet = MemorySet.valueOf(parent.getActionCommand());

                    createTiles(memorySet);
                    revalidate();
                    initialiseGameStats();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
    }


    private void createTiles(MemorySet memorySet) {
        removeAll();
        tiles.clear();

        List<Tile> selectedTiles = selectTiles(memorySet);
        placeTiles(selectedTiles);

        int columns = tilesNumberToColumns.get(numberOfTiles);
        int rows = numberOfTiles / columns;

        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

        topFrame.setSize((HGAP + memorySet.getWidth()) * columns, (VGAP + memorySet.getHeight()) * rows + VMARGIN);

        GridLayout layout = (GridLayout) getLayout();
        layout.setColumns(columns);
        layout.setRows(rows);
    }

    private List<Tile> selectTiles(MemorySet memorySet) {
        int tilesSelected = 0;
        int index;
        List<Tile> selectedTiles = new ArrayList<>(numberOfTiles);
        // Get a list of all tile numbers
        List<Integer> allTiles = IntStream.range(1, memorySet.getNumberOfTiles() + 1).boxed().collect(toList());

        String setName = memorySet.name().toLowerCase();
        String path = "src/resources/memory/" + setName + "/";
        String imageNameBack = path + "back.png";

        // select tiles until we have enough
        while (tilesSelected < numberOfTiles/2) {
            // select an index randomly from all possibilities
            // this way, the starting tiles will be different each time
            index = random.nextInt(allTiles.size());
            int id = allTiles.get(index);
            String imageName = path + id + memorySet.getFileExtension();

            // add the tile twice
            selectedTiles.add(createTile(imageName, imageNameBack, id));
            selectedTiles.add(createTile(imageName, imageNameBack, id));

            // remove this tile number from the list, it must not be used again
            allTiles.remove(index);
            tilesSelected++;
        }

        return selectedTiles;
    }

    private void placeTiles(List<Tile> selectedTiles) {
        int index;
        while (!selectedTiles.isEmpty()) {
            // randomly select the next tile
            index = random.nextInt(selectedTiles.size());
            add(selectedTiles.get(index));
            tiles.add(selectedTiles.get(index));
            selectedTiles.remove(index);
        }
    }


    private Tile createTile(String imageName, String imageNameBack, int id) {
        return new Tile(imageName, imageNameBack, id, this);
    }


    private void initialiseGameStats() {
        numberOfTilesFound = 0;
        numberOfAttempts = 0;
        startTime = Instant.now();
    }

    public void handleTileClick(Tile tile) {
        if (tilesShown < 2) {
            tile.doShow(true);

            if (tilesShown == 1) {
                numberOfAttempts++;
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

        // calculate results
        Instant endTime = Instant.now();
        Duration duration = Duration.between(startTime, endTime);
        long millis = duration.toMillis();
        long secsWithWaitingTime = (millis + 500) / 1000;

        long minutes = duration.toMinutes();
        long seconds = secsWithWaitingTime - minutes*60;
        String timeString = String.format("%02d:%02d", minutes, seconds);

        Duration waitDuration = Duration.of(numberOfAttempts * 2, ChronoUnit.SECONDS);
        minutes = waitDuration.toMinutes();
        seconds = numberOfAttempts * 2 - minutes*60;
        String timeStringWaiting = String.format("%02d:%02d", minutes, seconds);

        // show the results
        JOptionPane.showMessageDialog(
                SwingUtilities.getWindowAncestor(this),
                "You needed " + numberOfAttempts + " turns to complete the game.\n" +
                "It took you " + timeString + " of which " +
                timeStringWaiting + " was waiting time.",
                "Game finished", JOptionPane.INFORMATION_MESSAGE);
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