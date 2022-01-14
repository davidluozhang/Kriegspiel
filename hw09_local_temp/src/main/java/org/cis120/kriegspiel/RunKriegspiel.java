package org.cis120.kriegspiel;

/**
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 * 
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * In a Model-View-Controller framework, Game initializes the view,
 * implements a bit of controller functionality through the reset
 * button, and then instantiates a GameBoard. The GameBoard will
 * handle the rest of the game's view and controller functionality, and
 * it will instantiate a TicTacToe object to serve as the game's model.
 */
public class RunKriegspiel implements Runnable {

    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Kriegspiel");
        frame.setLocation(800, 800);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        // Game board
        final GameBoard board = new GameBoard(status);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.reset();
            }
        });
        control_panel.add(reset);

        final JButton undo = new JButton("Undo");
        undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.undo();
            }
        });
        control_panel.add(undo);

        final JFrame instructionsFrame = new JFrame("Kriegspiel");
        instructionsFrame.setLocation(150, 400);

        final JTextArea instructions = new JTextArea("Instructions");
        instructions.setText("\n  Welcome to Kriegspiel! " +
                "\n \n  This is a chess variant " +
                "modeled after a 19th century Prussian training war" +
                "game that was" +
                "\n  designed to mimic actual battle. To my" +
                " knowledge, this is the first digital recreation of " +
                "\n  Kriegspiel that goes beyond blind chess " +
                "to capture the chaos and randomness of war. " +
                "\n \n  I have also made my own tweaks to make the " +
                "game more enjoyable." +
                "\n \n \n  Rules:" +
                "\n \n  You can only see your own pieces and enemy pieces " +
                "within a 2 square radius of yours." +
                "\n \n  Attacks are not guaranteed to succeed. The opponent's" +
                " piece will fight back and has a" +
                "\n  chance of taking your piece based on their piece's " +
                "strength relative to your's." +
                "\n \n  To win, you must take the opponent's king. The " +
                "king, of course, will fight back." +
                "\n \n  To mimic real battle, there are no guardrails " +
                "preventing you from risking your king," +
                "\n  whether by courage or blunder. " +
                "Traditional rules about check do not apply." +
                "\n \n  You are allowed to make moves that go through " +
                "enemy pieces, hidden or not. " +
                "\n  You will simply be intercepted. You will stop and fight" +
                " the first piece in your way. " +
                "\n  Knights are the exception, as they can jump over pieces" +
                " like in normal chess." +
                "\n \n  Pawns that make it to the other side are " +
                "promoted to Queen." +
                "\n \n  Traditional chess rules surrounding en passant and " +
                "castling still apply." +
                "\n \n \n  Player Instructions:" +
                "\n \n  The current player is displayed at the bottom." +
                "\n \n  Click once on the piece you wish to move. Your " +
                "chosen piece will be highlighted " +
                "\n  in yellow and displayed at the bottom. Then, click " +
                "on the square you wish to move to. " +
                "\n \n  To change which piece you want to move," +
                " click gain the piece or another piece of " +
                "\n  yours to clear your selection. Now, choose another " +
                "piece. " +
                "\n \n  The game will wait until you make a legal move." +
                "\n \n  A fog of war will set in as players switch turns. " +
                "The new player will click anywhere to  " +
                "\n  begin their turn. " +
                "\n \n  Some mices and trackpads struggle to register " +
                "clicks. Make sure not to click too " +
                "\n  quickly or softly. When in doubt, click again!" +
                "\n \n  Have fun! I know I did making this :) " +
                "\n \n \n                                               " +
                "                                                  " +
                "(c) David Zhi LuoZhang 2021   " +
                "\n");
        instructionsFrame.add(instructions);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Put instructions frame on the screen
        instructionsFrame.pack();
        instructionsFrame.setVisible(true);
        // Start the game
        board.reset();
    }
}