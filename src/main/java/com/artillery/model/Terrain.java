package com.artillery.model;

import java.awt.*;

public class Terrain {

    private int width;
    private int height;
    private int baseGroundLevel;
    private int[] heights;

    public Terrain (int width, int height) {

        this.height = height;
        this.width = width;
        this.baseGroundLevel = (int) (height * 0.7); // Ground should sit 70% dow the screen
        this.heights = new int[Math.max(1, width)];

        generateTerrain();
    }

    private void generateTerrain () {

        int minGroundY = (int) (height * 0.35); // Keep hills from going too high
        int maxGroundY = height - 40; // Keep a strip of ground at the bottom

        for(int x = 0; x < heights.length; x++) {
            // Combine gentle wide hills with rolling waves
            double wave1 = Math.sin(x * 0.008) * 75.0;
            double wave2 = Math.sin(x * 0.022 + 1.2) * 30.0;

            // Minusing moves y up into hills for positive wave vals
            int y = (int) Math.round(baseGroundLevel - wave1 - wave2);

            // Clamp ensuring terrain stays inside playable area vertically
            heights[x] = Math.max(minGroundY, Math.min(maxGroundY, y));
        }
    }

    // Return Y coord of the ground for any given X
    public int getHeightAt (int x) {

        if (heights == null || heights.length == 0) {
            return baseGroundLevel;
        }
        int clampedX = Math.max(0, Math.min(heights.length - 1, x));
        return heights[clampedX];
    }

    // Flat green rectangle for ground
    public void draw (Graphics g) {
        g.setColor(new Color(34, 129, 34));

        Polygon terrainShape = new Polygon();

        // Terrain surface points
        for (int x = 0; x < heights.length; x++) {
            terrainShape.addPoint(x, heights[x]);
        }

        // Add bottom right corner
        terrainShape.addPoint(width - 1, height);

        // Add bottom left corner
        terrainShape.addPoint(0, height);

        // Fill polygon
        g.fillPolygon(terrainShape);
    }

    // Creates craters
    public void createCrater (int centerX, int radius) {

        if (heights == null || heights.length == 0 || radius <= 0) {
            return;
        }

        int maxGroundY = height - 25; // Keep solid floor near bottom of panel
        int startX = Math.max(0, centerX - radius);
        int endX = Math.min(heights.length - 1, centerX + radius);

        for (int x = startX; x <= endX; x++) {
            double dx = x - centerX;
            double insideSquared = (double) radius * radius - dx * dx;

            if (insideSquared > 0) {
                double carveDepth = Math.sqrt(insideSquared);
                int newHeight = (int) Math.round(heights[x] + carveDepth);
                heights[x] = Math.min(maxGroundY, newHeight);
            }
        }
    }
}
