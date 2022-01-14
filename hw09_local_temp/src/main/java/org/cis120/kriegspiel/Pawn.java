package org.cis120.kriegspiel;

public class Pawn extends Piece {
    private Position previousPos;
    private static String whitePawnFilePath = "files/WhitePawn.PNG";
    private static String blackPawnFilePath = "files/BlackPawn.PNG";

    public Pawn(boolean isWhite, Position pos) {
        this.setWhite(isWhite);
        this.setPosition(pos);
        this.setStrength(.5);
        this.setName("Pawn");
        previousPos = null;
    }

    @Override //Doesn't check for en passant
    public boolean isLegalMove(Position newPos, Piece[][] board,
                               boolean isWhiteTurn) {
        if (!super.isLegalMove(newPos, board, isWhiteTurn)) {
            return false;
        }
        int newCol = newPos.getCol();
        int newRow = newPos.getRow();
        int currentCol = getPosition().getCol();
        int currentRow = getPosition().getRow();
        if ((newRow > currentRow && isWhite()) || (newRow < currentRow && !isWhite())) {
            return false;
        }
        Piece target = board[newRow][newCol];
        if (Math.abs(currentRow - newRow) == 1
                && currentCol == newCol
                && target != null) {
            return false;
        }
        if (Math.abs(newRow - currentRow) != 1) {
            if (Math.abs(newRow - currentRow) == 2
                    && board[newRow][newCol] == null
                    && (currentRow == 6 && isWhite()
                    || currentRow == 1 && !isWhite())) {
                previousPos = getPosition();
                return true;
            }
            return false;
        }
        if (Math.abs(currentCol - newCol) > 1) {
            return false;
        }
        if (Math.abs(currentCol - newCol) == 1) {
            if (target == null) {
                return false;
            }
        }
        previousPos = getPosition();
        return true;
    }

    @Override
    public boolean isLegalMove(Position newPos, Piece[][] board,
                               boolean isWhiteTurn, Piece lastMoved) {
        if (!super.isLegalMove(newPos, board, isWhiteTurn)) {
            return false;
        }
        int newCol = newPos.getCol();
        int newRow = newPos.getRow();
        int currentCol = getPosition().getCol();
        int currentRow = getPosition().getRow();
        if ((newRow > currentRow && isWhite()) || (newRow < currentRow && !isWhite())) {
            return false;
        }
        if (Math.abs(newRow - currentRow) != 1) {
            if (Math.abs(newRow - currentRow) == 2
                    && board[newRow][newCol] == null
                    && (currentRow == 6 && isWhite()
                    || currentRow == 1 && !isWhite())) {
                previousPos = getPosition();
                return true;
            }
            return false;
        }
        if (Math.abs(currentRow - newRow) == 1 && board[newRow][newCol] != null) {
            return false;
        }
        if (Math.abs(currentCol - newCol) > 1) {
            return false;
        }
        if (Math.abs(currentCol - newCol) == 1) {
            Piece target = (board[newRow][newCol]);
            if (target != null && !isWhiteTurn) {
                return true;
            }
            if (!isEnPassant(newPos, board, lastMoved)) {
                return false;
            }
        }
        previousPos = getPosition();
        return true;
    }

    public boolean isEnPassant(Position newPos, Piece[][] board, Piece lastMoved) {
        int newCol = newPos.getCol();
        int newRow = newPos.getRow();
        if (newRow != 2 && isWhite() || newRow != 5 && !isWhite()) {
            return false;
        }
        int r;
        if (isWhite()) {
            r = newRow + 1;
        } else {
            r = newRow - 1;
        }
        Piece theirPiece = (board[r][newCol]);
        if (theirPiece == null || !theirPiece.getName().equals("Pawn")) {
            return false;
        }
        int prevRow = ((Pawn) theirPiece).previousPos.getRow();
        if (!(theirPiece.isWhite() && prevRow == 6) && !(!theirPiece.isWhite() && prevRow == 1)) {
            return false;
        }
        if (!theirPiece.equals(lastMoved)) {
            return false;
        }
        return true;
    }

    @Override
    public Position pathMove(Position newPos, Piece[][] board) {
        int curRow = getPosition().getRow();
        int newRow = newPos.getRow();
        int col = newPos.getCol();
        if (Math.abs(curRow - newRow) == 2) {
            if (board[(curRow + newRow) / 2][col] != null &&
                    board[(curRow + newRow) / 2][col].isWhite() != this.isWhite()) {
                return new Position((curRow + newRow) / 2, col);
            }
        }
        return super.pathMove(newPos, board);
    }

    public Position getPreviousPos() {
        return previousPos;
    }

    @Override
    public Piece copy() {
        Pawn copy;// = new Pawn(isWhite, position);

        if (getPosition() == null) {
            copy = new Pawn(isWhite(), null);
        } else {
            copy = new Pawn(isWhite(), getPosition().copy());
        }
        if (previousPos == null) {
            copy.previousPos = null;
        } else {
            copy.previousPos = previousPos.copy();
        }
        copy.setId(getId());
        return copy;
    }

    @Override
    public String getFilePath() {
        if (this.isWhite()) {
            return whitePawnFilePath;
        }
        return blackPawnFilePath;
    }
}
