package com.artillery.model;

import java.awt.*;

public class Tank {
    private int x;
    private int y;
    private int width = 36;
    private int height = 16;
    private Color color;

    public Tank (int startX, Terrain terrain, Color color) {

        this.x = startX;

        // Snap the bottom of the tank directly to the flat ground level
        this.y = terrain.getHeightAt(this.x) - height;
        this.color = color;
    }

    public void moveLeft (Terrain terrain, int screenWidth) {

        if (x > 30) {
            x -= 3;
            updatePosition(terrain);
        }
    }

    public void moveRight (Terrain terrain, int screenWidth) {

        if (x < screenWidth - 30) {
            x += 3;
            updatePosition(terrain);
        }
    }

    public void updatePosition (Terrain terrain) {

        this.y = terrain.getHeightAt(this.x) - height;
    }

    public void draw (Graphics2D g2d) {

        // Draw Tank Body
        g2d.setColor(color);
        g2d.fillRect(x - width / 2, y, width, height);
        g2d.setColor(color.darker());
        g2d.drawRect(x - width / 2, y, width, height);

        // Draw static barrel pointing forward
        g2d.setStroke(new BasicStroke(4));
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawLine(x, y + height / 2, x + 20, y + height / 2);
        g2d.setStroke(new BasicStroke(1));
    }

    public int getX() { return x; }
    public int getY() { return y; }
}