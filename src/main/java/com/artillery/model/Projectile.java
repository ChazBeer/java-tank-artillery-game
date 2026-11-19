package com.artillery.model;

import java.awt.*;

public class Projectile {

    private double x;
    private double y;
    private double vx;
    private double vy;
    private int radius = 4;
    private boolean active = true;

    private static final double GRAVITY = 0.25; // pixels/frame^2

    public Projectile (double startX, double startY, double angleDegrees, double speed) {

        this.x = startX;
        this.y = startY;

        double angleRad = Math.toRadians(angleDegrees);

        this.vx = speed * Math.cos(angleRad);
        this.vy = -speed * Math.sin(angleRad); // Negative Y is upward
    }

    public void update(Terrain terrain, int screenWidth, int screenHeight) {

        if (!active) {
            return;
        }

        x += vx;
        vy += GRAVITY;
        y += vy;

        int ix = (int) Math.round(x);
        if (ix < 0 || ix > screenWidth || y > screenHeight || y >= terrain.getHeightAt(ix)) {
            active = false;
        }
    }

    public void draw (Graphics g2d) {
        if (!active) {
            return;
        }

        int drawX = (int) Math.round(x) - radius;
        int drawY = (int) Math.round(y) - radius;

        g2d.setColor(Color.BLACK);
        g2d.fillOval(drawX, drawY, radius * 2, radius * 2);
    }

    public boolean isActive () {
        return active;
    }
}
