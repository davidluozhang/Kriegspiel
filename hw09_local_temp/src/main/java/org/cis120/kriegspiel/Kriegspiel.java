package org.cis120.kriegspiel;

import java.util.*;


public class Kriegspiel {

    private Piece[][] board;
    private Set<Piece> activePieces;
    private Set<Piece> takenPieces;
    private boolean isWhiteTurn;
    private Piece lastMoved;
    private Piece whiteKing;
    private Piece blackKing;


    public Kriegspiel() {
        reset();
    }

    private Piece[][] resetBoard() {
        Piece.resetPieces();
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
        blackKing = board[0][4];
        board[0][5] = new Bishop(false, new Position(0, 5));
        board[0][6] = new Knight(false, new Position(0, 6));
        board[0][7] = new Rook(false, new Position(0, 7));

        board[7][0] = new Rook(true, new Position(7, 0));
        board[7][1] = new Knight(true, new Position(7, 1));
        board[7][2] = new Bishop(true, new Position(7, 2));
        board[7][3] = new Queen(true, new Position(7, 3));
        board[7][4] = new King(true, new Position(7, 4));
        whiteKing = board[7][4];
        board[7][5] = new Bishop(true, new Position(7, 5));
        board[7][6] = new Knight(true, new Position(7, 6));
        board[7][7] = new Rook(true, new Position(7, 7));
        return board;
    }

    public boolean playTurn(Position current, Position move) {
        if (checkWinner() == 1) {
            System.out.println("White already won!");
            return false;
        } else if (checkWinner() == -1) {
            System.out.println("Black already won!");
            return false;
        }

        System.out.println("White's Turn: " + isWhiteTurn);
        if (current == null || move == null) {
            return false;
        }

        if (!current.inBoundsPosition() || !move.inBoundsPosition()) {
            System.out.println("Either current or move out of bounds");
            return false;
        }

        Piece piece = getSpecificPiece(current);
        if (piece == null) {
            System.out.println("Piece is null");
            return false;
        }

        if (piece.isWhite() != isWhiteTurn) {
            System.out.println("Not your piece");
            return false;
        }

        if (piece.getName().equals("Pawn")
                && piece.isLegalMove(move, board, isWhiteTurn, lastMoved)
                && ((Pawn) piece).isEnPassant(move, board, lastMoved)) {
            System.out.println("En Passant Capture");
            enPassant(piece, move);

        } else if (piece.getName().equals("King")
                && ((King) piece).isCastle(move, board, isWhiteTurn)) {
            System.out.println("Castling");
            castle(piece, move);

        } else if (piece.isLegalMove(move, board, isWhiteTurn)) {
            int newRow = move.getRow();
            int newCol = move.getCol();
            Position end = piece.pathMove(move, board);
            int endRow = end.getRow();
            int endCol = end.getCol();
            Piece target = getSpecificPiece(end);

            if (target == null) {
                board[newRow][newCol] = piece;
                piece.setPosition(end);
                System.out.println("Moving to Empty");

            } else {
                double random = Math.random();
                double winProbability = .8 + (piece.getStrength() - target.getStrength()) / 4;
                System.out.print("Contest: ");

                if (random <= winProbability) {
                    activePieces.remove(target);
                    takenPieces.add(target);
                    board[endRow][endCol] = piece;
                    piece.setPosition(end);
                    target.setPosition(null);
                    System.out.println("Attacker wins");

                } else {
                    activePieces.remove(piece);
                    takenPieces.add(piece);
                    piece.setPosition(null);
                    System.out.println("Defender wins");
                }
            }
        } else {
            System.out.println("Not legal move");
            return false;
        }
        lastMoved = piece;
        if (piece.getName().equals("Pawn") && piece.getPosition() != null) {
            int row = piece.getPosition().getRow();
            int col = piece.getPosition().getCol();
            if ((row == 0 && piece.isWhite())
                    || (row == 7 && !piece.isWhite())) {
                Piece pawnPromoted = new Queen(piece.getId(), piece.isWhite(), piece.getPosition());
                board[row][col] = pawnPromoted;
                activePieces.add(pawnPromoted);
                activePieces.remove(piece);
                lastMoved = pawnPromoted;
            }
        }
        board[current.getRow()][current.getCol()] = null;
        isWhiteTurn = !isWhiteTurn;
        return true;
    }

    public Piece getSpecificPiece(Position position) {
        if (!position.inBoundsPosition()) {
            return null;
        }
        int row = position.getRow();
        int col = position.getCol();
        return board[row][col];
    }

    private Piece[][] copyBoard() {
        Piece[][] boardCopy = new Piece[8][];
        for (int row = 0; row < 8; row ++) {
            boardCopy[row] = new Piece[8];
            for (int col = 0; col < 8; col ++) {
                Piece piece = board[row][col];
                if (piece != null) {
                    boardCopy[row][col] = piece.copy();
                }
            }
        }
        return boardCopy;
    }

    public Piece[][] getBoard() {
        return copyBoard();
    }

    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }

    public Set<Piece> getActivePieces() {
        Set<Piece> copy = new HashSet<>();
        for (Piece piece : activePieces) {
            copy.add(piece.copy());
        }
        return copy;
    }

    public Set<Piece> getTakenPieces() {
        Set<Piece> copy = new HashSet<>();
        for (Piece piece : takenPieces) {
            copy.add(piece.copy());
        }
        return copy;
    }

    public void enPassant(Piece pawn, Position newPos) {
        int curCol = pawn.getPosition().getCol();
        int curRow = pawn.getPosition().getRow();
        int newCol = newPos.getCol();
        int newRow = newPos.getRow();
        double random = Math.random();
        double winProbability = .8 + (pawn.getStrength() - lastMoved.getStrength()) / 4;
        System.out.print("Contest: ");
        if (random <= winProbability) {
            board[newRow][newCol] = pawn;
            pawn.setPosition(newPos);
            board[curRow][curCol] = null;
            int capCol = lastMoved.getPosition().getCol();
            int capRow = lastMoved.getPosition().getRow();
            board[capRow][capCol] = null;
            lastMoved.setPosition(null);
            activePieces.remove(lastMoved);
            takenPieces.add(lastMoved);
            System.out.println("Attacker wins");
        } else {
            activePieces.remove(pawn);
            takenPieces.add(pawn);
            System.out.println("Defender wins");
        }
    }

    public void castle(Piece king, Position newPos) {
        int kingCol = newPos.getCol();
        int row = newPos.getRow();
        if (kingCol == 6) {
            Rook rook = (Rook) board[row][7];
            board[row][kingCol] = king;
            king.setPosition(newPos);
            board[row][5] = rook;
            board[row][7] = null;
            rook.setPosition(new Position(row, 5));
        } else if (kingCol == 2)  {
            Rook rook = (Rook) board[row][0];
            board[row][kingCol] = king;
            king.setPosition(newPos);
            board[row][3] = rook;
            board[row][0] = null;
            rook.setPosition(new Position(row, 3));
        }
        board[row][4] = null;
    }

    public void reset() {
        board = resetBoard();
        activePieces = new HashSet<>();
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 8; col ++) {
                if (board[row][col] != null) {
                    activePieces.add(board[row][col]);
                }
            }
        }
        for (int row = 6; row < 8; row++) {
            for (int col = 0; col < 8; col ++) {
                if (board[row][col] != null) {
                    activePieces.add(board[row][col]);

                }
            }
        }
        takenPieces = new HashSet<>();
        isWhiteTurn = true;
        lastMoved = null;
    }

    public int checkWinner() {
        if (takenPieces.contains(whiteKing)) {
            System.out.println("Black won!");
            return -1;
        } else if (takenPieces.contains(blackKing)) {
            System.out.println("White won!");
            return 1;
        } else {
            return 0;

        }
    }

    public Kriegspiel copy() {
        Kriegspiel copy = new Kriegspiel();
        copy.isWhiteTurn = this.isWhiteTurn;
        copy.board = copyBoard();
        copy.activePieces = new HashSet<>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col ++) {
                if (copy.board[row][col] != null) {
                    copy.activePieces.add(copy.board[row][col]);
                }
            }
        }
        if (this.lastMoved == null) {
            copy.lastMoved = null;
        } else {
            copy.lastMoved = this.lastMoved.copy();
        }
        copy.takenPieces = this.getTakenPieces();
        return copy;
    }

    public Piece getLastMoved() {
        return lastMoved;
    }

}

