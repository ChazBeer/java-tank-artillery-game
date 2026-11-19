package com.artillery.model;

import java.awt.*;

public class Tank {

    private int x;
    private int y;
    private int width = 36;
    private int height = 16;
    private Color color;

    private double angleDegrees = 0;
    private static final double MIN_ANGLE = -5;
    private static final double MAX_ANGLE = 90;
    private static final double ANGLE_STEP = 1;
    private static final double BARREL_LENGTH = 22;

    public Tank (int startX, Terrain terrain, Color color) {

        this.x = startX;

        // Snap the bottom of the tank directly to the flat ground level
        this.y = terrain.getHeightAt(this.x) - height;
        this.color = color;
    }

    // */*/*/*/*
    // Movement
    // */*/*/*/*
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

        // Barrel rotation
        int pivotX = x;
        int pivotY = y - 5;

        double angleRad = Math.toRadians(angleDegrees);
        int endX = pivotX + (int) Math.round(BARREL_LENGTH * Math.cos(angleRad));
        int endY = pivotY - (int) Math.round(BARREL_LENGTH * Math.sin(angleRad));

        g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawLine(pivotX, pivotY, endX, endY);
        g2d.setStroke(new BasicStroke(1)); // Stroke reset

        // Draw tank turret
        g2d.setColor(color);
        g2d.fillRect(x - width / 4, y - 10, width / 2, height / 2);
        g2d.setColor(color.darker());
        g2d.drawRect(x - width / 4, y - 10, width / 2, height / 2);

        // Draw tank treads
        g2d.setColor(Color.GRAY);
        g2d.fillRect(x - width / 2, y + height * 3 / 4, width, height / 4);

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(
                1,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                10,
                new float[]{3, 2},
                0
                )
        );
        g2d.drawRect(x - width / 2, y + height * 3 / 4, width, height / 4);
        g2d.setStroke(new BasicStroke(1)); // Reset stroke
    }

    // */*/*/*/*
    // Turret rotation
    // */*/*/*/*
    public void aimUp() {
        angleDegrees = Math.min(MAX_ANGLE, angleDegrees + ANGLE_STEP);
    }

    public void aimDown() {
        angleDegrees = Math.max(MIN_ANGLE, angleDegrees - ANGLE_STEP);
    }

    public double getAngleDegrees() {
        return angleDegrees;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}