package org.cis120.kriegspiel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * You can use this file (and others) to test your
 * implementation.
 */

public class GameTest {

    @Test
    public void testStartingState() {
        Piece[][] board = new Piece [8][];
        for (int row = 0; row < 8; row++) {
            board[row] = new Piece[8];
        }

        for (int col = 0; col < 8; col++) {
            board[1][col] = new Pawn(false, new Position(1, col));
            board[6][col] = new Pawn(true, new Position(6, col));
        }

        board[0][0] = new Rook(false, new Position(0, 0));
        board[0][1] = new Knight(false, new Position(0, 1));
        board[0][2] = new Bishop(false, new Position(0, 2));
        board[0][3] = new Queen(false, new Position(0, 3));
        board[0][4] = new King(false, new Position(0, 4));
        board[0][5] = new Bishop(false, new Position(0, 5));
        board[0][6] = new Knight(false, new Position(0, 6));
        board[0][7] = new Rook(false, new Position(0, 7));

        board[7][0] = new Rook(true, new Position(7, 0));
        board[7][1] = new Knight(true, new Position(7, 1));
        board[7][2] = new Bishop(true, new Position(7, 2));
        board[7][3] = new Queen(true, new Position(7, 3));
        board[7][4] = new King(true, new Position(7, 4));
        board[7][5] = new Bishop(true, new Position(7, 5));
        board[7][6] = new Knight(true, new Position(7, 6));
        board[7][7] = new Rook(true, new Position(7, 7));


        Kriegspiel ks = new Kriegspiel();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = ks.getBoard()[i][j];
                if (piece != null) {
                    assertEquals(piece.getName(), board[i][j].getName());
                    assertEquals(Position.notationFromPosition(piece.getPosition()),
                            Position.notationFromPosition(board[i][j].getPosition()));
                    System.out.println(piece.getName() + " " + piece.getId());
                    assertEquals(piece.isWhite(), board[i][j].isWhite());
                } else {
                    System.out.println(piece);
                }
            }
            System.out.println();
        }
        assertEquals(32, ks.getActivePieces().size());
        assertEquals(0, ks.getTakenPieces().size());

    }

    @Test
    public void testPlayMoveToEmpty() {
        Kriegspiel ks = new Kriegspiel();
        assertTrue(ks.isWhiteTurn());
        assertEquals("Pawn", ks.getBoard()[6][5].getName());
        assertNull(ks.getBoard()[5][5]);
        Position current = new Position(6, 5);
        Position move = new Position(5, 5);
        ks.playTurn(current, move);
        assertEquals("Pawn", ks.getBoard()[5][5].getName());
        assertNull(ks.getBoard()[6][5]);
        assertEquals("Pawn", ks.getBoard()[5][5].getName());
        assertFalse(ks.isWhiteTurn());
    }

    @Test
    public void testPlayManyTurns() {
        Kriegspiel ks = new Kriegspiel();
        Piece whitePawn = ks.getSpecificPiece(new Position(6, 4));
        Piece blackPawn = ks.getSpecificPiece(new Position(1, 3));

        assertTrue(ks.isWhiteTurn());
        Position current = new Position(6, 4);
        Position move = new Position(5, 4);
        ks.playTurn(current, move);

        assertFalse(ks.isWhiteTurn());
        current = new Position(1, 3);
        move = new Position(2, 3);
        ks.playTurn(current, move);

        assertTrue(ks.isWhiteTurn());
        current = new Position(5, 4);
        move = new Position(4, 4);
        ks.playTurn(current, move);

        assertFalse(ks.isWhiteTurn());
        current = new Position(2, 3);
        move = new Position(3, 3);
        ks.playTurn(current, move);

        assertEquals(blackPawn, ks.getBoard()[3][3]);
        assertNull(ks.getBoard()[1][3]);
        assertNull(ks.getBoard()[2][3]);
        assertEquals(whitePawn, ks.getBoard()[4][4]);
        assertNull(ks.getBoard()[6][4]);
        assertNull(ks.getBoard()[5][4]);
    }

    @Test
    public void testPlayManyTurnFight() {
        Kriegspiel ks = new Kriegspiel();
        Piece whitePawn = ks.getSpecificPiece(new Position(6, 4));
        Piece blackPawn = ks.getSpecificPiece(new Position(1, 3));

        assertTrue(ks.isWhiteTurn());
        Position current = new Position(6, 4);
        Position move = new Position(5, 4);
        ks.playTurn(current, move);

        assertFalse(ks.isWhiteTurn());
        current = new Position(1, 3);
        move = new Position(2, 3);
        ks.playTurn(current, move);

        assertTrue(ks.isWhiteTurn());
        current = new Position(5, 4);
        move = new Position(4, 4);
        ks.playTurn(current, move);

        assertFalse(ks.isWhiteTurn());
        current = new Position(2, 3);
        move = new Position(3, 3);
        ks.playTurn(current, move);

        assertTrue(ks.isWhiteTurn());
        current = new Position(4, 4);
        move = new Position(3, 3);
        ks.playTurn(current, move);

        assertNull(ks.getBoard()[1][3]);
        assertNull(ks.getBoard()[2][3]);
        assertNull(ks.getBoard()[6][4]);
        assertNull(ks.getBoard()[5][4]);
        assertNotEquals(whitePawn.getPosition(), blackPawn.getPosition());
        boolean test = false;
        if (whitePawn.getPosition() == null || blackPawn.getPosition() == null) {
            test = true;
        }
        assertTrue(test);

        if (whitePawn.getPosition() == null) {
            assertTrue(ks.getTakenPieces().contains(whitePawn));
            assertFalse(ks.getActivePieces().contains(whitePawn));
        } else {
            assertTrue(ks.getTakenPieces().contains(blackPawn));
            assertFalse(ks.getActivePieces().contains(blackPawn));
        }
    }

    @Test
    public void testIsLegalMove() {
        Kriegspiel ks = new Kriegspiel();
        Piece piece = ks.getSpecificPiece(new Position(7,7));
        assertFalse(piece.isLegalMove(new Position(8, 7), ks.getBoard(), ks.isWhiteTurn()));
        assertFalse(piece.isLegalMove(new Position(6, 7), ks.getBoard(), ks.isWhiteTurn()));
        assertFalse(piece.isLegalMove(new Position(7, 8), ks.getBoard(), ks.isWhiteTurn()));
        assertFalse(piece.isLegalMove(new Position(7, 6), ks.getBoard(), ks.isWhiteTurn()));
        assertFalse(piece.isLegalMove(new Position(6, 6), ks.getBoard(), ks.isWhiteTurn()));
        assertFalse(piece.isLegalMove(new Position(8, 8), ks.getBoard(), ks.isWhiteTurn()));
        assertFalse(piece.isLegalMove(new Position(8, 6), ks.getBoard(), ks.isWhiteTurn()));
        assertFalse(piece.isLegalMove(new Position(6, 8), ks.getBoard(), ks.isWhiteTurn()));

        Piece whitePawn = ks.getSpecificPiece(new Position(6, 4));
        assertTrue(whitePawn.isLegalMove(new Position(5, 4), ks.getBoard(), ks.isWhiteTurn()));
        assertFalse(whitePawn.isLegalMove(new Position(5, 4), ks.getBoard(), !ks.isWhiteTurn()));
    }

    @Test
    public void testIsLegalMoveRook() {
        Piece[][] testBoard = new Piece[8][];
        for (int r = 0; r < 8; r++) {
            testBoard[r] = new Piece[8];
        }
        Piece testRook = new Rook(true, new Position(4, 4));
        testBoard[4][4] = testRook;
        assertTrue(testRook.isLegalMove(new Position(6, 4), testBoard, true));
        assertTrue(testRook.isLegalMove(new Position(2, 4), testBoard, true));
        assertTrue(testRook.isLegalMove(new Position(4, 6), testBoard, true));
        assertTrue(testRook.isLegalMove(new Position(4, 2), testBoard, true));
        assertFalse(testRook.isLegalMove(new Position(6, 6), testBoard, true));
        Piece whitePiece1 = new Pawn(true, new Position(4, 5));
        testBoard[4][5] = whitePiece1;
        Piece whitePiece2 = new Pawn(true, new Position(6, 4));
        testBoard[6][4] = whitePiece2;
        Piece blackPiece1 = new Pawn(false, new Position(4, 3));
        testBoard[4][3] = blackPiece1;
        Piece blackPiece2 = new Pawn(false, new Position(2, 4));
        testBoard[2][4] = blackPiece2;
        assertFalse(testRook.isLegalMove(new Position(4, 6), testBoard, true));
        assertFalse(testRook.isLegalMove(new Position(7, 4), testBoard, true));
        assertTrue(testRook.isLegalMove(new Position(4, 2), testBoard, true));
        assertTrue(testRook.isLegalMove(new Position(0, 4), testBoard, true));
    }


    @Test
    public void testIsLegalMoveBishop() {
        Piece[][] testBoard = new Piece[8][];
        for (int r = 0; r < 8; r++) {
            testBoard[r] = new Piece[8];
        }
        Piece testBishop = new Bishop(true, new Position(4, 4));
        testBoard[4][4] = testBishop;
        assertTrue(testBishop.isLegalMove(new Position(5, 5), testBoard, true));
        assertTrue(testBishop.isLegalMove(new Position(3, 3), testBoard, true));
        assertTrue(testBishop.isLegalMove(new Position(5, 3), testBoard, true));
        assertTrue(testBishop.isLegalMove(new Position(3, 5), testBoard, true));
        assertFalse(testBishop.isLegalMove(new Position(5, 6), testBoard, true));
        Piece whitePiece1 = new Pawn(true, new Position(5, 5));
        testBoard[5][5] = whitePiece1;
        Piece whitePiece2 = new Pawn(true, new Position(2, 6));
        testBoard[2][6] = whitePiece2;
        Piece blackPiece1 = new Pawn(false, new Position(4, 3));
        testBoard[4][4] = blackPiece1;
        Piece blackPiece2 = new Pawn(false, new Position(2, 4));
        testBoard[3][3] = blackPiece2;
        assertFalse(testBishop.isLegalMove(new Position(6, 6), testBoard, true));
        assertFalse(testBishop.isLegalMove(new Position(1, 7), testBoard, true));
        assertTrue(testBishop.isLegalMove(new Position(3, 3), testBoard, true));
        assertTrue(testBishop.isLegalMove(new Position(2, 2), testBoard, true));
    }

    @Test
    public void testIsLegalMoveKnight() {
        Piece[][] testBoard = new Piece[8][];
        for (int r = 0; r < 8; r++) {
            testBoard[r] = new Piece[8];
        }
        Piece testKnight = new Knight(true, new Position(4, 3));
        testBoard[4][3] = testKnight;
        assertTrue(testKnight.isLegalMove(new Position(3, 5), testBoard, true));
        assertTrue(testKnight.isLegalMove(new Position(2, 4), testBoard, true));
        assertTrue(testKnight.isLegalMove(new Position(3, 1), testBoard, true));
        assertTrue(testKnight.isLegalMove(new Position(2, 2), testBoard, true));
        assertTrue(testKnight.isLegalMove(new Position(6, 2), testBoard, true));
        assertTrue(testKnight.isLegalMove(new Position(5, 1), testBoard, true));
        Piece whitePiece1 = new Pawn(true, new Position(5, 4));
        testBoard[5][4] = whitePiece1;
        assertTrue(testKnight.isLegalMove(new Position(5, 5), testBoard, true));
        Piece whitePiece2 = new Pawn(true, new Position(5, 5));
        testBoard[5][5] = whitePiece2;
        assertFalse(testKnight.isLegalMove(new Position(5, 5), testBoard, true));
        assertTrue(testKnight.isLegalMove(new Position(6, 4), testBoard, true));
        assertFalse(testKnight.isLegalMove(new Position(5, 6), testBoard, true));
    }

    @Test
    public void testIsLegalMoveQueen() {
        Piece[][] testBoard = new Piece[8][];
        for (int r = 0; r < 8; r++) {
            testBoard[r] = new Piece[8];
        }
        Piece testQueen = new Queen(true, new Position(4, 4));
        testBoard[4][4] = testQueen;
        assertTrue(testQueen.isLegalMove(new Position(2, 6), testBoard, true));
        assertTrue(testQueen.isLegalMove(new Position(2, 4), testBoard, true));
        assertTrue(testQueen.isLegalMove(new Position(2, 2), testBoard, true));
        Piece whitePiece1 = new Pawn(true, new Position(3, 3));
        testBoard[3][3] = whitePiece1;
        assertFalse(testQueen.isLegalMove(new Position(2, 2), testBoard, true));
        Piece blackPiece1 = new Pawn(false, new Position(5, 3));
        testBoard[5][3] = blackPiece1;
        System.out.println("Looking after here");
        assertTrue(testQueen.isLegalMove(new Position(6, 2), testBoard, true));
        assertFalse(testQueen.isLegalMove(new Position(5, 6), testBoard, true));
    }

    @Test
    public void testIsLegalMovePawn() {
        Piece[][] testBoard = new Piece[8][];
        for (int r = 0; r < 8; r++) {
            testBoard[r] = new Piece[8];
        }
        Piece testPawnWhite = new Pawn(true, new Position(6, 4));
        testBoard[6][4] = testPawnWhite;
        Piece testPawnBlack = new Pawn(false, new Position(2, 1));
        testBoard[2][1] = testPawnBlack;
        assertTrue(testPawnBlack.isLegalMove(new Position(3, 1), testBoard, false, testPawnWhite));
        assertTrue(testPawnWhite.isLegalMove(new Position(4, 4), testBoard, true, testPawnBlack));
        assertEquals(((Pawn) testPawnWhite).getPreviousPos(), new Position(6, 4));
        testBoard[4][4] = testPawnWhite;
        testPawnWhite.setPosition(new Position(5, 4));
        assertFalse(testPawnWhite.isLegalMove(new Position(5, 4), testBoard, true, testPawnBlack));
        assertFalse(testPawnWhite.isLegalMove(new Position(5, 5), testBoard, true, testPawnBlack));
        Piece testPawnBlack2 = new Pawn(false, new Position(4, 5));
        testBoard[4][5] = testPawnBlack2;
        assertTrue(testPawnBlack2.isLegalMove(new Position(5, 4), testBoard, false, testPawnWhite));
        Piece testPawnWhite2 = new Pawn(true, new Position(1, 1));
        assertFalse(testPawnBlack2.isLegalMove(new Position(5, 4),
                testBoard, false, testPawnWhite2));
    }

    @Test
    public void testBishopPath() {
        Piece[][] testBoard = new Piece[8][];
        for (int r = 0; r < 8; r++) {
            testBoard[r] = new Piece[8];
        }
        Piece testBishopWhite = new Bishop(true, new Position(2, 5));
        testBoard[2][5] = testBishopWhite;
        Position end = new Position(5, 2);
        Position middle = new Position(4, 3);
        Piece testBlack1 = new Rook(false, middle);
        testBoard[4][3] = testBlack1;
        Piece testBlack2 = new Rook(false, end);
        testBoard[5][2] = testBlack2;
        assertTrue(testBishopWhite.isLegalMove(end, testBoard, true));
        assertNotNull(testBoard[5][2]);
        Position actual = testBishopWhite.pathMove(end, testBoard);
        assertEquals(actual, middle);
    }

    @Test
    public void testRookPath() {
        Piece[][] testBoard = new Piece[8][];
        for (int r = 0; r < 8; r++) {
            testBoard[r] = new Piece[8];
        }
        Piece testRookWhite = new Rook(true, new Position(2, 5));
        testBoard[2][5] = testRookWhite;
        Position end = new Position(6, 5);
        Position middle = new Position(4, 5);
        Piece testBlack1 = new Rook(false, middle);
        testBoard[6][5] = testBlack1;
        Piece testBlack2 = new Rook(false, end);
        testBoard[4][5] = testBlack2;
        assertTrue(testRookWhite.isLegalMove(end, testBoard, true));
        assertNotNull(testBoard[4][5]);
        Position actual = testRookWhite.pathMove(end, testBoard);
        assertEquals(actual, middle);
    }

    @Test
    public void testQueenPath() {
        Piece[][] testBoard = new Piece[8][];
        for (int r = 0; r < 8; r++) {
            testBoard[r] = new Piece[8];
        }
        Piece testQueenWhite = new Queen(true, new Position(3, 4));
        testBoard[3][4] = testQueenWhite;

        Position middle = new Position(3, 6);
        Position end = new Position(3, 7);
        Piece testPieceBlack1 = new Rook(false, middle);
        testBoard[3][7] = testPieceBlack1;
        Piece testPieceBlack2 = new Rook(false, end);
        testBoard[3][6] = testPieceBlack2;
        assertTrue(testQueenWhite.isLegalMove(end, testBoard, true));
        assertTrue(testQueenWhite.isLegalMove(middle, testBoard, true));
        Position actual = testQueenWhite.pathMove(end, testBoard);
        assertEquals(actual, middle);

        middle = new Position(1, 4);
        end = new Position(0, 4);
        Piece testPieceBlack3 = new Rook(false, middle);
        testBoard[1][4] = testPieceBlack3;
        Piece testPieceBlack4 = new Rook(false, end);
        testBoard[0][4] = testPieceBlack4;
        assertTrue(testQueenWhite.isLegalMove(end, testBoard, true));
        assertTrue(testQueenWhite.isLegalMove(middle, testBoard, true));
        actual = testQueenWhite.pathMove(end, testBoard);
        assertEquals(actual, middle);

        middle = new Position(3, 1);
        end = new Position(3, 0);
        Piece testPieceBlack5 = new Rook(false, middle);
        testBoard[3][1] = testPieceBlack5;
        Piece testPieceBlack6 = new Rook(false, end);
        testBoard[3][0] = testPieceBlack6;
        assertTrue(testQueenWhite.isLegalMove(end, testBoard, true));
        assertTrue(testQueenWhite.isLegalMove(middle, testBoard, true));
        actual = testQueenWhite.pathMove(end, testBoard);
        assertEquals(actual, middle);

        middle = new Position(5, 4);
        end = new Position(6, 4);
        Piece testPieceBlack7 = new Rook(false, middle);
        testBoard[5][4] = testPieceBlack7;
        Piece testPieceBlack8 = new Rook(false, end);
        testBoard[6][4] = testPieceBlack8;
        assertTrue(testQueenWhite.isLegalMove(end, testBoard, true));
        assertTrue(testQueenWhite.isLegalMove(middle, testBoard, true));
        actual = testQueenWhite.pathMove(end, testBoard);
        assertEquals(actual, middle);

        middle = new Position(1, 6);
        end = new Position(0, 7);
        Piece testPieceBlack9 = new Rook(false, middle);
        testBoard[1][6] = testPieceBlack9;
        Piece testPieceBlack10 = new Rook(false, end);
        testBoard[0][7] = testPieceBlack10;
        assertTrue(testQueenWhite.isLegalMove(end, testBoard, true));
        assertTrue(testQueenWhite.isLegalMove(middle, testBoard, true));
        actual = testQueenWhite.pathMove(end, testBoard);
        assertEquals(actual, middle);

        middle = new Position(1, 2);
        end = new Position(0, 1);
        Piece testPieceBlack11 = new Rook(false, middle);
        testBoard[1][2] = testPieceBlack11;
        Piece testPieceBlack12 = new Rook(false, end);
        testBoard[0][1] = testPieceBlack12;
        assertTrue(testQueenWhite.isLegalMove(end, testBoard, true));
        assertTrue(testQueenWhite.isLegalMove(middle, testBoard, true));
        actual = testQueenWhite.pathMove(end, testBoard);
        assertEquals(actual, middle);

        middle = new Position(5, 2);
        end = new Position(6, 1);
        Piece testPieceBlack13 = new Rook(false, middle);
        testBoard[5][2] = testPieceBlack13;
        Piece testPieceBlack14 = new Rook(false, end);
        testBoard[6][1] = testPieceBlack14;
        assertTrue(testQueenWhite.isLegalMove(end, testBoard, true));
        assertTrue(testQueenWhite.isLegalMove(middle, testBoard, true));
        actual = testQueenWhite.pathMove(end, testBoard);
        assertEquals(actual, middle);

        middle = new Position(5, 6);
        end = new Position(6, 7);
        Piece testPieceBlack15 = new Rook(false, middle);
        testBoard[6][7] = testPieceBlack15;
        Piece testPieceBlack16 = new Rook(false, end);
        testBoard[5][6] = testPieceBlack16;
        assertTrue(testQueenWhite.isLegalMove(end, testBoard, true));
        assertTrue(testQueenWhite.isLegalMove(middle, testBoard, true));
        actual = testQueenWhite.pathMove(end, testBoard);
        assertEquals(actual, middle);
    }

    @Test
    public void testGameOverAndRandomAttack() {
        boolean[] outcomes = new boolean[1000];
        for (int i = 0; i < 999; i++) {
            Kriegspiel ks = new Kriegspiel();
            Position start = Position.positionFromNotation("f",2);
            Position end = Position.positionFromNotation("f",3);
            assertTrue(ks.playTurn(start, end));
            start = Position.positionFromNotation("e", 7);
            end = Position.positionFromNotation("e", 6);
            assertTrue(ks.playTurn(start, end));
            start = Position.positionFromNotation("g", 2);
            end = Position.positionFromNotation("g", 4);
            assertTrue(ks.playTurn(start, end));
            start = Position.positionFromNotation("d", 8);
            end = Position.positionFromNotation("h", 4);
            assertTrue(ks.playTurn(start, end));
            start = Position.positionFromNotation("a", 2);
            end = Position.positionFromNotation("a", 3);
            assertTrue(ks.playTurn(start, end));
            start = Position.positionFromNotation("h", 4);
            end = Position.positionFromNotation("e", 1);
            assertTrue(ks.playTurn(start, end));
            outcomes[i] = ks.checkWinner() == -1;
        }
        int count = 0;
        for (int i = 0; i < 1000; i++) {
            if (outcomes[i]) {
                count++;
            }
        }
        System.out.println(count);
        assertEquals(1.0 * count / 1000, .925, .05);
        //Simulate 1000 Queen attacks on King to win game.
        //Queen attack king has a 92.5% chance of succeeding.
    }

    @Test
    public void testCastle() {
        Piece[][] testBoard = new Piece[8][];
        for (int r = 0; r < 8; r++) {
            testBoard[r] = new Piece[8];
        }
        Piece rook = new Rook(true, new Position(7, 7));
        testBoard[7][7] = rook;
        Piece king = new King(true, new Position(7, 4));
        testBoard[7][4] = king;
        Position castlePos = new Position(7, 6);
        assertTrue(((King) king).isCastle(castlePos, testBoard, true));
        assertFalse(king.isLegalMove(new Position(7, 6), testBoard, true));
    }

}
