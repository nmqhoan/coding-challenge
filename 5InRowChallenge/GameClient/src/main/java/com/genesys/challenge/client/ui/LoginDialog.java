package com.genesys.challenge.client.ui;

import com.genesys.challenge.client.domain.Player;
import com.genesys.challenge.client.service.PlayerService;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class LoginDialog extends JDialog {

    private JTextField tfUsername;
    private JLabel lbUsername;
    private JButton btnLogin;
    private JButton btnCancel;
    private boolean succeeded;
    private Player currentLoginPlayer;
    private PlayerService playerService;

    public LoginDialog(Frame parent) {
        super(parent, "Login", true);

        playerService = new PlayerService();
        //
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();

        cs.fill = GridBagConstraints.HORIZONTAL;

        lbUsername = new JLabel("Username: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbUsername, cs);

        tfUsername = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(tfUsername, cs);

        panel.setBorder(new LineBorder(Color.GRAY));

        tfUsername.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
              if(ke.getKeyCode()==KeyEvent.VK_ENTER){
                  currentLoginPlayer = playerService.authenticate(getUsername());
                  if(currentLoginPlayer!=null){
                      JOptionPane.showMessageDialog(LoginDialog.this,
                              "Hi " + getUsername() + "! You have successfully logged in.",
                              "Login",
                              JOptionPane.INFORMATION_MESSAGE);
                      succeeded = true;
                      dispose();
                  } else {
                      JOptionPane.showMessageDialog(LoginDialog.this,
                              "Invalid username",
                              "Login",
                              JOptionPane.ERROR_MESSAGE);
                      // reset username and password
                      tfUsername.setText("");
                      succeeded = false;
                  }
             }
        }});

        btnLogin = new JButton("Login");

        btnLogin.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                currentLoginPlayer = playerService.authenticate(getUsername());
                if(currentLoginPlayer!=null){
                    JOptionPane.showMessageDialog(LoginDialog.this,
                            "Hi " + getUsername() + "! You have successfully logged in.",
                            "Login",
                            JOptionPane.INFORMATION_MESSAGE);
                    succeeded = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginDialog.this,
                            "Invalid username",
                            "Login",
                            JOptionPane.ERROR_MESSAGE);
                    // reset username and password
                    tfUsername.setText("");
                    succeeded = false;
                }
            }
        });
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        JPanel bp = new JPanel();
        bp.add(btnLogin);
        bp.add(btnCancel);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    public String getUsername() {
        return tfUsername.getText().trim();
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public Player getCurrentLoginPlayer() {
        return currentLoginPlayer;
    }

    public void setCurrentLoginPlayer(Player currentLoginPlayer) {
        this.currentLoginPlayer = currentLoginPlayer;
    }
}
