package com.genesys.challenge.client.ui;
import com.genesys.challenge.client.domain.Game;
import com.genesys.challenge.client.service.GameService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class JoinGameDialog {
    private JList list;
    private JOptionPane optionPane;
    private JButton okButton, cancelButton;
    private ActionListener okEvent, cancelEvent;
    private JDialog dialog;
    private GameService gameService;
    private Long playerId;
    List<Game> availableGames;

    public JoinGameDialog(Long playerId){
        gameService = new GameService();
        this.playerId = playerId;
        loadGameList();
        createAndDisplayOptionPane();
        dialog.setTitle("Please select game to join");
    }

    public void loadGameList(){
        availableGames = gameService.getGamesToJoin(playerId);
        DefaultListModel<String> listModel = new DefaultListModel<>();

        for(Game game:availableGames)
            listModel.addElement("Game id " + game.getId() + " with player " + game.getFirstPlayer().getUserName());
        //create the list
        list = new JList<>(listModel);
     }

    private void createAndDisplayOptionPane(){
        setupButtons();
        JPanel pane = layoutComponents();
        optionPane = new JOptionPane(pane);
        optionPane.setOptions(new Object[]{okButton, cancelButton});
        dialog = optionPane.createDialog("Select option");
    }

    private void setupButtons(){
        okButton = new JButton("Ok");
        okButton.addActionListener(e -> handleOkButtonClick(e));
        if(list.getModel().getSize()==0)
            okButton.setEnabled(false);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> handleCancelButtonClick(e));
    }

    private JPanel layoutComponents(){
        centerListElements();
        JPanel panel = new JPanel(new BorderLayout(5,5));
        panel.add(list, BorderLayout.CENTER);
        return panel;
    }

    private void centerListElements(){
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) list.getCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public void setOnOk(ActionListener event){ okEvent = event; }

    public void setOnClose(ActionListener event){
        cancelEvent  = event;
    }

    private void handleOkButtonClick(ActionEvent e){
        if(okEvent != null){ okEvent.actionPerformed(e); }
        dialog.dispose();
    }

    private void handleCancelButtonClick(ActionEvent e){
        if(cancelEvent != null){ cancelEvent.actionPerformed(e);}
        dialog.dispose();
    }

    public void show(){ dialog.setVisible(true); }

    public Game getSelectedItem(){
        return availableGames.get(list.getSelectedIndex());
    }

    public void dispose() {
        dialog.dispose();
    }
}