import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A graphical user interface modelling a tic-tac-toe (noughts and crosses, Xs and Os) game.
 * 
 * @author Lynn Marshall
 * @version November 8, 2012
 * 
 * @author Riya Arora 101190033
 * @version April 02, 2022
 */

public class TicTacToe extends JFrame implements ActionListener, MouseListener 
{
    public static final String PLAYER_X = "X"; // player using "X"
    public static final String PLAYER_O = "O"; // player using "O"
    public static final String EMPTY = " ";  // empty cell
    public static final String TIE = "T"; // game ended in a tie

    private String player;   // current player (PLAYER_X or PLAYER_O)

    private String winner;   // winner: PLAYER_X, PLAYER_O, TIE, EMPTY = in progress

    private int numFreeSquares; // number of squares still free

    JButton[][] board; // 2D array of buttons to replicate the game
    JLabel status; // Label to display current status of game

    private int xGamesWon = 0, oGamesWon = 0, numTies = 0; //variables to keep track of statistics
    private JLabel xWon, oWon, ties; //labels to display statistics

    private JFrame boardF; //frame for the board

    private JMenuBar menuBar;
    private JMenu game, color, xColor, oColor; 
    private JMenuItem newGame, quit, resetStats, changePlayer; 

    private JMenuItem redX, redO, blueX, blueO, pinkX, pinkO, orangeX, orangeO, blackX, blackO; 

    private Color playerXCol, playerOCol; //initialize and set default colors for the players

    /** 
     * Constructs a new Tic-Tac-Toe board.
     */
    public TicTacToe()
    {        
        boardF = new JFrame();
        boardF.setSize(350, 350);
        Container contentPane = boardF.getContentPane();

        //initialize statistics JLabels
        JLabel statistics = new JLabel("Statistics: ");
        xWon = new JLabel("   X wins: ");
        oWon = new JLabel("   O wins: ");
        ties = new JLabel("   Ties: ");
        resetStats();

        //set initial color for each player
        playerXCol = Color.black;
        playerOCol = Color.black;

        //create the main options menu
        createMenu();

        //create the player color menu
        createColorMenu();

        JPanel panelBoard = new JPanel(new GridLayout(3, 3));
        JPanel panelStatus = new JPanel();
        JPanel panelStats = new JPanel();

        //set up the status panel
        status = new JLabel();
        panelStatus.add(status);
        panelBoard.setSize(300, 300);

        //set up the statistics panel
        panelStats.add(statistics);
        panelStats.add(xWon);
        panelStats.add(oWon);
        panelStats.add(ties);

        //set and initialize the board layout with the buttons
        board = new JButton[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = new JButton(EMPTY);
                board[i][j].addActionListener(this);
                board[i][j].setFont(new Font("Arial", Font.BOLD, 36));
                panelBoard.add(board[i][j]);
            }
        }

        //empty panels to make the board look nice
        JPanel empty = new JPanel();
        JPanel empty2 = new JPanel();

        //add panels to content pane
        contentPane.add(panelBoard, BorderLayout.CENTER);
        contentPane.add(panelStatus, BorderLayout.SOUTH);
        contentPane.add(panelStats, BorderLayout.PAGE_START);
        contentPane.add(empty, BorderLayout.WEST);
        contentPane.add(empty2, BorderLayout.EAST);

        boardF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        clearBoard(); //set up initial state of game
        boardF.setVisible(true);
    }

    /**
     * Create the game menu in the game which includes 4 options: New Game, Quit, Change First Player and Reset Statictics
     * This method initializes all related elements and adds menu to the JFrame.
     */
    private void createMenu() {
        menuBar = new JMenuBar();
        boardF.setJMenuBar(menuBar);

        game = new JMenu("Game");
        game.addMouseListener(this);
        menuBar.add(game);

        newGame = new JMenuItem("New Game");
        newGame.addActionListener(this);
        game.add(newGame);

        quit = new JMenuItem("Quit");
        quit.addActionListener(this);
        game.add(quit);

        resetStats = new JMenuItem("Reset Statistics");
        resetStats.addActionListener(this);
        game.add(resetStats);

        changePlayer = new JMenuItem("Change First Player");
        changePlayer.addActionListener(this);
        game.add(changePlayer);

        //add keyboard shortcuts
        final int SHORTCUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,SHORTCUT_MASK));
        quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,SHORTCUT_MASK));
        resetStats.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,SHORTCUT_MASK));
        changePlayer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,SHORTCUT_MASK));

    }

    /**
     * Create the color menu in the game which includes 5 color options for each player.
     * This method initializes all related elements and adds menu to the JFrame.
     */
    private void createColorMenu() {
        //initialize color menus
        color = new JMenu("Choose Player Color");   
        color.addMouseListener(this);
        oColor = new JMenu("Player O");
        xColor = new JMenu("Player X");

        //initialize menuItems 
        blackX = new JMenuItem("Black");
        xColor.add(blackX);
        blackX.addActionListener(this);

        blackO = new JMenuItem("Black");
        oColor.add(blackO);
        blackO.addActionListener(this);

        redX = new JMenuItem("Red");
        xColor.add(redX);
        redX.addActionListener(this);

        redO = new JMenuItem("Red");
        oColor.add(redO);
        redO.addActionListener(this);

        blueX = new JMenuItem("Blue");
        xColor.add(blueX);
        blueX.addActionListener(this);

        blueO = new JMenuItem("Blue");
        oColor.add(blueO);
        blueO.addActionListener(this);

        pinkX = new JMenuItem("Pink");
        xColor.add(pinkX);
        pinkX.addActionListener(this);

        pinkO = new JMenuItem("Pink");
        oColor.add(pinkO);
        pinkO.addActionListener(this);

        orangeX = new JMenuItem("Orange");
        xColor.add(orangeX);
        orangeX.addActionListener(this);

        orangeO = new JMenuItem("Orange");
        oColor.add(orangeO);
        orangeO.addActionListener(this);

        game.add(color);
        color.add(oColor);
        color.add(xColor);

    }

    /**
     * Set the color of the player based on their choice
     */
    private void setColor(ActionEvent e) {
        Object o = e.getSource();
        //only change colors at beginning of game
        if (numFreeSquares == 9) {
            if (o == redX) {
                playerXCol = Color.red;
            } else if (o == blueX) {
                playerXCol = Color.blue;
            } else if (o == pinkX) {
                playerXCol = Color.magenta;
            } else if (o == orangeX) {
                playerXCol = Color.orange;
            } else if (o == redO) {
                playerOCol = Color.red;
            } else if (o == blueO) {
                playerOCol = Color.blue;
            } else if (o == pinkO) {
                playerOCol = Color.magenta;
            } else if (o == orangeO) {
                playerOCol = Color.orange;
            }
        } else {
            if (!(o == resetStats || o == newGame || o == quit || o == resetStats || o == changePlayer))
                status.setText("Change colors at the start of the next game!");
        }
    }

    /**
     * Updates the JLabels that represent the current statistics of the game
     */
    private void updateStats() {
        xWon.setText("   X wins: " + xGamesWon);
        oWon.setText("   O wins: " + oGamesWon);
        ties.setText("   Ties: " + numTies);
    }

    /**
     * Resets the current statistics of the game and update the JLabels
     */
    private void resetStats() {
        xGamesWon = 0;
        oGamesWon = 0;
        numTies = 0;
        updateStats();
    }

    /**
     * Sets the buttons to enabled or disabled based on the current status of the game.
     * 
     * @param enable True if the buttons should be enabled, false otherwise
     */
    private void setEnableButton(boolean enable) {
        for (int k = 0; k < 3; k++) {
            for (int p = 0; p < 3; p++) {
                board[k][p].setEnabled(enable);
            }
        }
    }

    /**
     * Sets everything up for a new game.  Marks all squares (JButtons) in the Tic Tac Toe board as empty and resets the 
     * color of the buttons,and indicates no winner yet, 9 free squares and the current player is player X, 
     * enables all the buttons and sets the current status of the game.
     */
    private void clearBoard()
    {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j].setText(EMPTY);
                board[i][j].setBackground(Color.white);
            }
        }
        setEnableButton(true);
        winner = EMPTY;
        numFreeSquares = 9;
        player = PLAYER_X;     // Player X always has the first turn.
        status.setText("Player " + player + "'s turn");
    }

    /**
     * This action listener is called when the user clicks on n a button or when a menu item is selected.
     * 
     * @param e The action event that triggered this method
     */
    public void actionPerformed (ActionEvent e) 
    {   
        setColor(e);
        //reset the current game's statistics
        if (e.getSource() == resetStats) {
            resetStats();
        }

        //quit the current game
        if (e.getSource() == quit) {
            boardF.dispose();            
        }

        //start a new game
        if (e.getSource() == newGame) {
            clearBoard();
        }

        //change players
        if (e.getSource() == changePlayer) {
            if (numFreeSquares == 9) {
                if (player == PLAYER_X) 
                    player = PLAYER_O;
                else 
                    player = PLAYER_X;
                status.setText("Player " + player + "'s turn");
            } else 
                status.setText("You can't change players in the middle of a game.");
        }

        //game logic
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (e.getSource() == board[i][j]){
                    if (board[i][j].getText().equals(EMPTY)) {
                        status.setText(player.equals(PLAYER_X) ? "Player " + PLAYER_O + "'s turn" : "Player " + PLAYER_X + "'s turn");
                        if (player==PLAYER_X) 
                            board[i][j].setForeground(playerXCol);
                        else 
                            board[i][j].setForeground(playerOCol);
                        board[i][j].setText(player);

                        numFreeSquares--;
                        if (haveWinner(i, j)) {
                            winner = player; // must be the player who just went
                            if (player==PLAYER_X) {
                                xGamesWon++;
                                updateStats();
                            } else {
                                oGamesWon++;
                                updateStats();
                            }
                            status.setText("Player " + winner + " wins!");
                            setEnableButton(false);
                        }
                        else if (numFreeSquares==0) {
                            winner = TIE; // board is full so it's a tie
                            numTies++;
                            updateStats();
                            status.setText("It's a tie");
                            setEnableButton(false);
                        }

                        if (player==PLAYER_X) 
                            player=PLAYER_O;
                        else 
                            player=PLAYER_X;

                    } else {
                        status.setText("That spot is already taken.");
                    }
                }
            }
        }
    }

    /**
     * Detects when the mouse enters the component.  We are only "listening" to the
     * JMenu.  We highlight the menu name when the mouse goes into that component.
     * 
     * @param e The mouse event triggered when the mouse was moved into the component
     */
    public void mouseEntered(MouseEvent e) {
        JMenu item = (JMenu) e.getSource();
        item.setSelected(true); // highlight the menu name
    }

    /**
     * Detects when the mouse exits the component.  We are only "listening" to the
     * JMenu.  We stop highlighting the menu name when the mouse exits  that component.
     * 
     * @param e The mouse event triggered when the mouse was moved out of the component
     */
    public void mouseExited(MouseEvent e) {
        JMenu item = (JMenu) e.getSource();
        item.setSelected(false); // stop highlighting the menu name
    }

    /**
     * Not used.
     * 
     * @param e The mouse event triggered when the mouse was released
     */
    public void mouseReleased(MouseEvent e) {}

    /**
     * Not used.
     * 
     * @param e The mouse event triggered when the mouse pressed the component
     */
    public void mousePressed(MouseEvent e) {}

    /**
     * Not used.
     * 
     * @param e The mouse event triggered when the mouse clicked on the component
     */
    public void mouseClicked(MouseEvent e) {}

    /**
     * Returns true if filling the given square gives us a winner, and false
     * otherwise.
     * 
     * Changes the background of the winning buttons to green.
     *
     * @param int row of square just set
     * @param int col of square just set
     * 
     * @return true if we have a winner, false otherwise
     */
    private boolean haveWinner(int row, int col) 
    {
        // unless at least 5 squares have been filled, we don't need to go any further
        // (the earliest we can have a winner is after player X's 3rd move).

        if (numFreeSquares>4) return false;

        // Note: We don't need to check all rows, columns, and diagonals, only those
        // that contain the latest filled square.  We know that we have a winner 
        // if all 3 squares are the same, as they can't all be blank (as the latest
        // filled square is one of them).

        // check row "row"
        if ( board[row][0].getText().equals(board[row][1].getText()) &&
        board[row][0].getText().equals(board[row][2].getText()) ) {
            board[row][0].setBackground(Color.green);
            board[row][1].setBackground(Color.green);
            board[row][2].setBackground(Color.green);
            return true;
        }

        // check column "col"
        if ( board[0][col].getText().equals(board[1][col].getText()) &&
        board[0][col].getText().equals(board[2][col].getText()) ) {
            board[0][col].setBackground(Color.green);
            board[1][col].setBackground(Color.green);
            board[2][col].setBackground(Color.green);
            return true;
        }

        // if row=col check one diagonal
        if (row==col) {
            if ( board[0][0].getText().equals(board[1][1].getText()) &&
            board[0][0].getText().equals(board[2][2].getText()) ) {
                board[0][0].setBackground(Color.green);
                board[1][1].setBackground(Color.green);
                board[2][2].setBackground(Color.green);
                return true;            
            }
        }

        // if row=2-col check other diagonal
        if (row==2-col) {
            if ( board[0][2].getText().equals(board[1][1].getText()) &&
            board[0][2].getText().equals(board[2][0].getText()) ) {
                board[0][2].setBackground(Color.green);
                board[1][1].setBackground(Color.green);
                board[2][0].setBackground(Color.green);
                return true;
            }
        }
        // no winner yet
        return false;
    }

}
