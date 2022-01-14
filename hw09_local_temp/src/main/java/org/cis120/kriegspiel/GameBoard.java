package org.cis120.kriegspiel;

/**
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.LinkedList;

/**
 * This class instantiates a TicTacToe object, which is the model for the game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 * 
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * In a Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with
 * its paintComponent method and the status JLabel).
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private Kriegspiel ks; // model for the game
    private JLabel status; // current status text
    private boolean isPieceChosen;
    private Position chosen;
    private boolean switchingPlayers;
    //private int GameState;
    //private GameMode mode;
    private LinkedList<Kriegspiel> previousStates;


    // Game constants
    public static final int BOARD_WIDTH = 800;
    public static final int BOARD_HEIGHT = 800;

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        ks = new Kriegspiel(); // initializes model for the game
        status = statusInit; // initializes the status JLabel
        isPieceChosen = false;
        chosen = null;
        switchingPlayers = true;
        previousStates = new LinkedList<>();
        //previousStates.add(this.copy);

        /*
         * Listens for mouse clicks. Updates the model, then updates the game
         * board based off of the updated model.
         */


        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (switchingPlayers) {
                    repaint();
                    switchingPlayers = false;
                } else if (!isPieceChosen) {
                    repaint();
                    Point currentClick = e.getPoint();
                    Position temp = new Position(currentClick.y / 100,
                            currentClick.x / 100);
                    //System.out.println(ks.getBoard()[][]);
                    Piece chosenPiece = ks.getSpecificPiece(temp);
                    System.out.println("Chosen piece: " + chosenPiece);
                    if (!(chosenPiece == null || chosenPiece.isWhite() != ks.isWhiteTurn())) {
                        chosen = temp;
                        isPieceChosen = true;
                        repaint();
                        updateStatus(); // updates the status JLabel
                        //repaint();
                    }
                } else {
                    Kriegspiel current = ks.copy();
                    Point moveToClick = e.getPoint();
                    Position move = new Position(moveToClick.y / 100, moveToClick.x / 100);
                    if (move.equals(chosen)) {
                        isPieceChosen = false;
                        chosen = null;
                        updateStatus();
                        repaint();
                    }
                    if (ks.playTurn(chosen, move)) {
                        switchingPlayers = true;
                        previousStates.addFirst(current);
                        updateStatus(); // updates the status JLabel
                        repaint();
                    }
                    chosen = null;
                    isPieceChosen = false;
                    // repaints the game board
                }
            }
        });
    }

    //public void hideBoard() {


    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        ks.reset();
        status.setText("White's Turn || Piece Not Selected");
        isPieceChosen = false;
        chosen = null;
        switchingPlayers = false;
        previousStates = new LinkedList<>();
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    public void undo() {
        if (previousStates.size() == 0) {
            reset();
        } else {
            Kriegspiel previous = previousStates.remove();
            ks = previous;
            isPieceChosen = false;
            chosen = null;
        }
        updateStatus();
        repaint();

        requestFocusInWindow();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        if (ks.isWhiteTurn()) {
            //status.setText("White's Turn");

            if (!isPieceChosen || ks.getSpecificPiece(chosen) == null) {
                status.setText("White's Turn || Piece Not Selected");
            } else {
                status.setText("White's Turn || Piece Selected: "
                        + ks.getSpecificPiece(chosen).getName() + " on "
                        + Position.notationFromPosition(chosen));
            }
        } else {

            if (!isPieceChosen || ks.getSpecificPiece(chosen) == null) {
                status.setText("Black's Turn || Piece Not Selected");
            } else {
                status.setText("Black's Turn || Piece Selected: "
                        + ks.getSpecificPiece(chosen).getName() + " on "
                        + Position.notationFromPosition(chosen));
            }
        }

        int winner = ks.checkWinner();
        if (winner == 1) {
            status.setText("White Wins!");
            switchingPlayers = false;
        } else if (winner == -1) {
            status.setText("Black Wins!");
            switchingPlayers = false;
        }

    }

    /**
     * Draws the game board.
     * <p>
     * There are many ways to draw a game board. This approach
     * will not be sufficient for most games, because it is not
     * modular. All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper
     * methods. Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (switchingPlayers && ks.checkWinner() == 0) {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, 800, 800);
            if (ks.isWhiteTurn()) {
                g.setColor(Color.BLACK);
                g.drawString("Switching Player: White's Turn -- " +
                        "Click anywhere to begin your turn.", 185, 400);
            } else {
                g.setColor(Color.BLACK);
                g.drawString("Switching Player: Black's Turn -- " +
                        "Click anywhere to begin your turn.", 185, 400);
            }
            switchingPlayers = false;
        } else {
            // Draws board grid
            for (int i = 0; i <= 8; i++) {
                g.drawLine(100 * i, 0, 100 * i, 800);
                g.drawLine(0, 100 * i, 800, 100 * i);
            }

            // Draws X's and O's
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (i % 2 != j % 2) {
                        g.setColor(new Color(196, 164, 132));
                        g.fillRect(100 * j, 100 * i, 100, 100);
                    } else {
                        g.setColor(new Color(255, 244, 194));
                        g.fillRect(100 * j, 100 * i, 100, 100);
                    }
                }
            }
            if (isPieceChosen) {
                g.setColor(new Color(255, 255, 120));
                g.fillRect(100 * chosen.getCol(), 100 * chosen.getRow(), 100, 100);
            }
            for (int i = 0; i < 8; i++) {
                g.setColor(Color.BLACK);
                g.drawString("" + (1 + i), 5, (8 - i) * 100 - 45);
                String files = "ABCDEFGH";
                for (int j = 0; j < 8; j++) {
                    g.drawString(files.substring(j, j + 1), 100 * j + 45, 795);
                }
            }


            Set<Piece> activePieces = ks.getActivePieces();
            for (Piece piece : activePieces) {
                g.setColor(Color.BLACK);
                int row = piece.getPosition().getRow();
                int col = piece.getPosition().getCol();
                try {
                    if (piece.isWhite() == ks.isWhiteTurn()) {
                        BufferedImage img = ImageIO.read(new File(piece.getFilePath()));
                        g.drawImage(img, 100 * col + 10, 100 * row + 10, 80, 80,null);
                        for (int r = row - 2; r <= row + 2; r++) {
                            for (int c = col - 2; c <= col + 2; c++) {
                                Piece other = ks.getSpecificPiece(new Position(r, c));
                                if (other != null && other.isWhite() != ks.isWhiteTurn()) {
                                    BufferedImage otherImg = ImageIO.read(
                                            new File(other.getFilePath()));
                                    g.drawImage(otherImg, 100 * c + 10, 100 * r + 10, 80, 80,null);
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Internal Error:" + e.getMessage());
                }
            }
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}