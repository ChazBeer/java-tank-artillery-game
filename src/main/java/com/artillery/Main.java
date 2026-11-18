package com.artillery;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Tank Artillery Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            int windowWidth = 1000;
            int windowHeight = 700;

            GamePanel panel = new GamePanel(windowWidth, windowHeight);
            frame.add(panel);
            frame.pack();

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}