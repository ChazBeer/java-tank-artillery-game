package com.artillery.model;

import java.awt.*;

public class Terrain {

    private int width;
    private int height;
    private int groundLevel;

    public Terrain (int width, int height) {

        this.height = height;
        this.width = width;
        this.groundLevel = (int) (height * 0.7); // Ground should sit 70% dow the screen
    }

    // Return Y coord of the ground for any given X
    public int getHeightAt (int x) {
        return groundLevel;
    }

    // Flat green rectangle for ground
    public void draw (Graphics g) {
        g.setColor(new Color(34, 129, 34));
        g.fillRect(0, groundLevel, width, height - groundLevel);
    }
}
