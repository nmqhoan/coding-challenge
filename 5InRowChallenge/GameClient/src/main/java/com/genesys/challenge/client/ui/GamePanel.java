package com.genesys.challenge.client.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genesys.challenge.client.domain.Game;
import com.genesys.challenge.client.domain.GameBoard;
import com.genesys.challenge.client.domain.Move;
import com.genesys.challenge.client.dto.MoveDTO;
import com.genesys.challenge.client.enummeration.GameCode;
import com.genesys.challenge.client.enummeration.GameStatus;
import com.genesys.challenge.client.domain.Player;
import com.genesys.challenge.client.enummeration.PlayerStatus;
import com.genesys.challenge.client.service.GameBoardService;
import com.genesys.challenge.client.service.GameService;
import com.genesys.challenge.client.service.MoveService;
import com.genesys.challenge.client.service.PlayerService;
import com.genesys.challenge.client.socket.Message;
import com.genesys.challenge.client.socket.SocketClient;
import com.genesys.challenge.client.util.GameUtils;
import com.genesys.challenge.client.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.*;
import java.time.Instant;
import javax.swing.*;
import javax.swing.text.*;

public class GamePanel extends JFrame
        implements ActionListener {
    private final Logger log = LoggerFactory.getLogger(GamePanel.class);

    private JFrame frame;
    private JPanel panel;
    private JTextArea textGameArea;
    private JTextField textColInput;
    private JButton btnJoinGame, btnLogin, btnNewGame, btnExit ;
    private JLabel lblMoveInput;

    private GameBoard gameBoard;
    private Game currentGame;
    private Player currentPlayer;
    private GameCode currentPlayerCode;
    private boolean isFirstPlayer;
    private SocketClient socketClient;

    private GameService gameService;
    private PlayerService playerService;
    private MoveService moveService;
    private GameBoardService gameBoardService;

    public GamePanel(String title) {
        this.setTitle(title);
        gameService = new GameService();
        playerService = new PlayerService();
        moveService = new MoveService();
        gameBoardService = new GameBoardService();
        socketClient = new SocketClient(this);
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */

    private void initComponents() {
//        frame = new JFrame("Five In Row");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(true);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        textGameArea = new JTextArea(25, 70);
        textGameArea.setText("Please login to start!!!");
        textGameArea.setWrapStyleWord(true);
        textGameArea.setEditable(false);
        textGameArea.setFont(Font.getFont(Font.SANS_SERIF));
        JScrollPane scroller = new JScrollPane(textGameArea);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        JPanel inputpanel = new JPanel();
        inputpanel.setLayout(new FlowLayout());

        lblMoveInput = new JLabel();
        lblMoveInput.setText("Please insert column number (1-9)");

        textColInput = new JTextField(4);
        textColInput.setEnabled(false);
        addActionListenerForPlayerMoveInput();;

        btnLogin = new JButton("Login");
        btnLogin.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        LoginDialog loginDlg = new LoginDialog(frame);
                        loginDlg.setVisible(true);
                        // if logon successfully
                        if(loginDlg.isSucceeded()){
                            btnLogin.setText("Hi " + loginDlg.getUsername() + "!");
                            currentPlayer = loginDlg.getCurrentLoginPlayer();
                            btnJoinGame.setEnabled(true);
                            btnNewGame.setEnabled(true);
                            textGameArea.setText("Please join or create a new game!");
                         }
                    }
                });

        btnJoinGame = new JButton("Join Game");
        btnJoinGame.setEnabled(false);
        btnJoinGame.addActionListener(e -> {
            JoinGameDialog listDialog = new JoinGameDialog(this.currentPlayer.getId());
            listDialog.setOnOk(i -> {
                Game game = listDialog.getSelectedItem();
                listDialog.dispose();
                if(game!=null){
                    currentGame = listDialog.getSelectedItem();
                    log.debug("Chosen game: " + currentGame);
                    loadJoinGame();

                    Message message = new Message();
                    message.setPlayerStatus(PlayerStatus.NEXT_TURN);
                    message.setFromId(currentPlayer.getId());
                    message.setToId(currentGame.getFirstPlayer().getId());
                    message.setGameId(currentGame.getId());

                    sendMessage(message);

                }
            });
            listDialog.show();
        });


        btnNewGame = new JButton("New Game");
        btnNewGame.setEnabled(false);
        btnNewGame.addActionListener(e->{
            newGame();
        });

        btnExit = new JButton("Exit");
        btnExit.addActionListener(e->{
            exitGame();
            dispose();
        });

        DefaultCaret caret = (DefaultCaret) textGameArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        panel.add(scroller);
        inputpanel.add(lblMoveInput);
        inputpanel.add(textColInput);
        inputpanel.add(btnLogin);
        inputpanel.add(btnJoinGame);
        inputpanel.add(btnNewGame);
        inputpanel.add(btnExit);
        panel.add(inputpanel);
        this.getContentPane().add(BorderLayout.CENTER, panel);
        this.pack();
        this.setLocationByPlatform(true);
        this.setVisible(true);
        this.setResizable(false);
    }

    private void newGame() {
        gameBoard = new GameBoard(GameUtils.row_size,GameUtils.col_size);
        textGameArea.setText(StringUtil.newGame().toString());
        textGameArea.append(StringUtil.drawBoard(gameBoard).toString());
        currentGame = gameService.createGame(currentPlayer.getId());
        isFirstPlayer = true;
        updateCurrentPlayerGameCode();
        if(currentGame.getGameStatus().equals(GameStatus.WAITS_FOR_PLAYER)) {
            textGameArea.append(StringUtil.GAME_CODE + " " + currentPlayerCode + "\n");
            textGameArea.append(StringUtil.WAIT_FOR_PLAYER_JOINT);
        }
    }

    private void addActionListenerForPlayerMoveInput(){
        textColInput.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                String value = textColInput.getText();
                int l = value.length();
                if (ke.getKeyChar() >= '1' && ke.getKeyChar() <= '9') {
                    if(value.length()<1)
                        textColInput.setEditable(true);
                    else
                        textColInput.setEditable(false);
                } else {
                    if(ke.getExtendedKeyCode()==KeyEvent.VK_BACK_SPACE || ke.getExtendedKeyCode()==KeyEvent.VK_DELETE)
                        textColInput.setEditable(true);
                    else if(ke.getKeyCode()==KeyEvent.VK_ENTER){
                        log.info("Move " + value);
                        makeMove(Integer.valueOf(value));
                        textColInput.setText("");
                    }else
                        textColInput.setEditable(false);
                }
            }
        });
    }

    private void makeMove(int col) {
        //dop a move on gameBoard
        int accordRow = gameBoardService.createMove(gameBoard,col,currentPlayerCode);
        log.info("Find accroding row of move {}",accordRow);
        if(accordRow==-1) {
            JOptionPane.showMessageDialog(new JFrame(), "That column is already full.", "Dialog",
                    JOptionPane.ERROR_MESSAGE);
        }else {
            textGameArea.append("\nYour select col: " + col);
            textGameArea.append(StringUtil.drawBoard(gameBoard).toString());

            //save a move
            MoveDTO moveDTO = new MoveDTO();
            moveDTO.setGridColumn(col);
            moveDTO.setGridRow(accordRow);
            moveDTO.setMovedTime(Instant.now());
            moveDTO.setPlayerGameCode(currentPlayerCode);
            moveDTO.setGameId(currentGame.getId());
            moveDTO.setPlayerId(currentPlayer.getId());
            Move move = moveService.create(moveDTO);

            //load latest update game
            updateLatestGameStatus();
//            gameBoardService.checkGameLogic(gameBoard);

            //send notification to other player
            Message message = new Message();
            message.setGameId(currentGame.getId());
            message.setFromId(currentPlayer.getId());
            message.setToId(isFirstPlayer?currentGame.getSecondPlayer().getId():currentGame.getFirstPlayer().getId());
            if(currentGame.getGameStatus()==GameStatus.IN_PROGRESS) {
                message.setPlayerStatus(PlayerStatus.NEXT_TURN);
                String nextTurnUserName =  isFirstPlayer?currentGame.getSecondPlayer().getUserName()
                        :currentGame.getFirstPlayer().getUserName();
                textGameArea.append(StringUtil.WAIT_FOR_PLAYER_TURN + ": " + nextTurnUserName);
                textColInput.setEnabled(false);
            }
            sendMessage(message);
        }
    }

    private void exitGame(){
        textGameArea.append("\n");
        textGameArea.append("EXIT GAME");

        if(currentGame==null)
            return;

        //update game status
        if(currentGame.getGameStatus()==GameStatus.IN_PROGRESS
                || currentGame.getGameStatus()==GameStatus.WAITS_FOR_PLAYER) {
            if (currentGame.getFirstPlayer().getId() == currentPlayer.getId())
                currentGame.setGameStatus(GameStatus.SECOND_PLAYER_WON);
            else currentGame.setGameStatus(GameStatus.FIRST_PLAYER_WON);
            currentGame = gameService.update(currentGame);
        }

        //send notification
        Message message = new Message();
        message.setPlayerStatus(PlayerStatus.EXIT);
        message.setFromId(currentPlayer.getId());
        message.setToId(currentPlayer.getId()==currentGame.getFirstPlayer().getId()?
                        currentGame.getSecondPlayer().getId():currentGame.getFirstPlayer().getId());
        message.setGameId(currentGame.getId());
        sendMessage(message);
    }

    private void loadJoinGame() {
        gameBoard = new GameBoard();
        isFirstPlayer = false;
        updateCurrentPlayerGameCode();

        textGameArea.setText(StringUtil.newGame().toString());
        textGameArea.append(StringUtil.drawBoard(gameBoard).toString());
        textGameArea.append(StringUtil.GAME_CODE + " " + currentPlayerCode + "\n");
        textGameArea.append(StringUtil.WAIT_FOR_PLAYER_TURN + ": " + currentGame.getFirstPlayer().getUserName());

        //update game status
        currentGame.setSecondPlayer(currentPlayer);
        currentGame.setGameStatus(GameStatus.IN_PROGRESS);
        currentGame = gameService.update(currentGame);

    }

    private void updateCurrentPlayerGameCode() {
        if(currentGame.getFirstPlayer().getId() == currentPlayer.getId())
            currentPlayerCode = currentGame.getFirstPlayerGameCode();
        else
            currentPlayerCode = currentGame.getFirstPlayerGameCode()==GameCode.O?GameCode.X:GameCode.O;
    }

    private void sendMessage(Message message){
        ObjectMapper Obj = new ObjectMapper();
        try {
            String jsonStr = Obj.writeValueAsString(message);
            socketClient.sendMessage(jsonStr);
        } catch (JsonProcessingException jsonProcessingException) {
            jsonProcessingException.printStackTrace();
        }
    }


    public void onMessage(String msg){
        try {
            Message gameMessage = new ObjectMapper().readValue(msg, Message.class);
            updateLatestGameStatus();
            log.info("current game {}", currentGame);
            switch (currentGame.getGameStatus()){
                case FIRST_PLAYER_WON:
                    textGameArea.append("\nGame over. Winner is " + currentGame.getFirstPlayer().getUserName()+".");
                    textColInput.setEnabled(false);
                    return;
                case SECOND_PLAYER_WON:
                    textGameArea.append("\nGame over. Winner is " + currentGame.getSecondPlayer().getUserName()+".");
                    textColInput.setEnabled(false);
                    return;
                case TIE:
                    textGameArea.append("\nGame over. Game is tie");
                    textColInput.setEnabled(false);
                    return;
            }
            if(currentPlayer.getId()==gameMessage.getToId()
                    && currentGame.getId()==gameMessage.getGameId()) {
                textGameArea.append("\n");
                switch (gameMessage.getPlayerStatus()){
                    case EXIT:
                        textGameArea.append(gameMessage.getPlayerStatus().toString());
                        if(currentPlayer.getId()==currentGame.getFirstPlayer().getId()) {
                            textGameArea.append("Game over. Winner is " + currentPlayer.getUserName() + ".");
                            textColInput.setEnabled(false);
                        }
                        break;
                    case NEXT_TURN:
                        textGameArea.append(StringUtil.drawBoard(gameBoard).toString());
                        textGameArea.append("It your turn " + currentPlayer.getUserName()
                                            +", please enter column (1-9)");
                        textColInput.setEnabled(true);
                        break;
                }

            }
        }catch (JsonProcessingException e){
            log.error(e.getMessage());
        }
    }

    private void updateLatestGameStatus(){
        try {
            currentGame = gameService.getGame(currentGame.getId());
            gameBoard = gameBoardService.update(currentGame, gameBoard);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

}