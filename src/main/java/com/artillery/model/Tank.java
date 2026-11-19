package com.artillery.model;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Tank {

    private int x;
    private int y;
    private int width = 36;
    private int height = 16;
    private Color color;

    private double slopeAngleRad = 0.0;

    private double angleDegrees = 0;
    private static final double MIN_ANGLE = -5;
    private static final double MAX_ANGLE = 90;
    private static final double ANGLE_STEP = 1.0;
    private static final double BARREL_LENGTH = 22;
    private static final double DEFAULT_MUZZLE_SPEED = 11.0;

    public Tank (int startX, Terrain terrain, Color color) {

        this.x = startX;
        updatePosition(terrain); // Snap the bottom of the tank directly to the flat ground level
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

        // Snaps tank to ground under tank
        this.y = terrain.getHeightAt(this.x) - height;

        // Measure incline across the full width of the tank (dx = width)
        int halfWidth = width / 2;
        int leftGroundY = terrain.getHeightAt(this.x - halfWidth);
        int rightGroundY = terrain.getHeightAt(this.x + halfWidth);

        double dy = rightGroundY - leftGroundY;
        double dx = width; // Known horizontal span betwix left and right edges

        this.slopeAngleRad = Math.atan2(dy, dx);
    }

    public void draw (Graphics2D g2d) {

        AffineTransform oldTransform = g2d.getTransform();

        try{
            // Rotate about bottom center of tank so treads stay on ground
            double rotPivotX = x;
            double rotPivotY = y + height;

            g2d.translate(rotPivotX, rotPivotY);
            g2d.rotate(slopeAngleRad);
            g2d.translate(-rotPivotX, -rotPivotY);

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
        } finally {
            g2d.setTransform(oldTransform);
        }
    }

    // */*/*/*/*
    // Barrel elevation and firing
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

    public Projectile fire () {

        // Barrel tip (non-rotated)
        double barrelRad = Math.toRadians(angleDegrees);
        double localTipX = x + BARREL_LENGTH * Math.cos(barrelRad);
        double localTipY = (y - 5) - BARREL_LENGTH * Math.sin(barrelRad);

        // Rotate the tip about tanks pivot by slopeAngleRad
        double rotPivotX = x;
        double rotPivotY = y + height;

        double dx = localTipX - rotPivotX;
        double dy = localTipY - rotPivotY;

        double cosSlope = Math.cos(slopeAngleRad);
        double sinSlope = Math.sin(slopeAngleRad);

        double muzzleX = rotPivotX + dx * cosSlope - dy * sinSlope;
        double muzzleY = rotPivotY + dx * sinSlope + dy * cosSlope;

        // Combine tank tilt with barrel elevation for global fire angle
        double globalFireAngleDegrees = angleDegrees - Math.toDegrees(slopeAngleRad);

        return new Projectile(muzzleX, muzzleY, globalFireAngleDegrees, DEFAULT_MUZZLE_SPEED);
    }

    // */*/*/*/*
    // Getters
    // */*/*/*/*

    public int getX() { return x; }
    public int getY() { return y; }
}