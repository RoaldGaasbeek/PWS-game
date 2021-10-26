package com.pws.memory_two;

public enum MemorySet {
    NATURE(".png", 8, 190, 204),
    POKEMON(".jpg", 24, 105, 90),
    POKEMON_GEN1(".png", 41, 128, 128);


    private final String fileExtension;
    private final int numberOfTiles;
    private final int width;
    private final int height;

    MemorySet(String fileExtension, int numberOfTiles, int width, int height) {
        this.fileExtension = fileExtension;
        this.numberOfTiles =numberOfTiles;
        this.width = width;
        this.height = height;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public int getNumberOfTiles() {
        return numberOfTiles;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
