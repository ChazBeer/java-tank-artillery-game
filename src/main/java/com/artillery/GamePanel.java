package com.artillery;

import com.artillery.model.Projectile;
import com.artillery.model.Tank;
import com.artillery.model.Terrain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private static final int TARGET_FPS = 60;
    private static final int DELAY = 1000 / TARGET_FPS;

    private final Timer gameTimer;
    private final Terrain terrain;
    private final Tank playerTank;

    // Projectile variables
    private Projectile activeProjectile;
    private boolean fireKeyWasPressed = false;

    // Track key states for smooth movement
    private Map<Integer, Boolean> keyStates = new HashMap<>();

    public GamePanel (int width, int height) {

        setPreferredSize(new Dimension(width, height));
        setBackground(new Color(135, 206, 235)); // Sky blue
        setFocusable(true);
        addKeyListener(this);

        terrain = new Terrain(width, height);
        playerTank = new Tank(200, terrain, new Color(70, 130, 180));

        gameTimer = new Timer(DELAY, this);
        gameTimer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        updateGame();
        repaint();
    }

    private void updateGame() {

        int width = getWidth();

        // Handle movement via A/D or Left/Right
        if (keyStates.getOrDefault(KeyEvent.VK_A, false) || keyStates.getOrDefault(KeyEvent.VK_LEFT, false)) {
            playerTank.moveLeft(terrain, width);
        }
        if (keyStates.getOrDefault(KeyEvent.VK_D, false) || keyStates.getOrDefault(KeyEvent.VK_RIGHT, false)) {
            playerTank.moveRight(terrain, width);
        }

        // Handle movement via W/S or Up/Down
        if (keyStates.getOrDefault(KeyEvent.VK_W, false) || keyStates.getOrDefault(KeyEvent.VK_UP, false)) {
            playerTank.aimUp();
        }
        if (keyStates.getOrDefault(KeyEvent.VK_S, false) || keyStates.getOrDefault(KeyEvent.VK_DOWN, false)) {
            playerTank.aimDown();
        }

        // Handle Firing via SPACE
        boolean spacePressed = keyStates.getOrDefault(KeyEvent.VK_SPACE, false);

        // Only allow new shot when no projectile in existence
        if (spacePressed && !fireKeyWasPressed && activeProjectile == null) {
            activeProjectile = playerTank.fire();
        }

        fireKeyWasPressed = spacePressed;

        if (activeProjectile != null) {
            activeProjectile.update(terrain, getWidth(), getHeight());

            // Clear ref once impacts ground
            if (!activeProjectile.isActive()) {
                activeProjectile = null;
            }
        }

    }

    @Override
    protected void paintComponent (Graphics g) {

        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw world
        terrain.draw(g2D);;
        playerTank.draw(g2D);

        // Simple instruction text
        g2D.setColor(Color.black);
        g2D.setFont(new Font("Arial", Font.BOLD, 14));
        g2D.drawString("Use A/D or Left/Right to move", 20, 30);

        // Draw projectile
        if (activeProjectile != null) {
            activeProjectile.draw(g2D);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyStates.put(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyStates.put(e.getKeyCode(), false);
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
