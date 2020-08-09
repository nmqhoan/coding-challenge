package com.genesys.challenge.client;

import com.genesys.challenge.client.ui.GamePanel;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.swing.*;

@EnableAutoConfiguration
public class FiveInRowClientApplication {
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                new GamePanel("Five in Row - Player 1").setVisible(true);
                new GamePanel("Five in Row - Player 2").setVisible(true);
            }
        });
    }
}