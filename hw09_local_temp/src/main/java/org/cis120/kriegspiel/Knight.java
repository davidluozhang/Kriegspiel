package org.cis120.kriegspiel;

public class Knight extends Piece {
    private static String whiteKnightFilePath = "files/WhiteKnight.PNG";
    private static String blackKnightFilePath = "files/BlackKnight.PNG";

    public Knight(boolean isWhite, Position pos) {
        this.setWhite(isWhite);
        this.setPosition(pos);
        this.setName("Knight");
        this.setStrength(.8);
    }

    @Override
    public boolean isLegalMove(Position newPos, Piece[][] board, boolean isWhiteTurn) {
        if (!super.isLegalMove(newPos, board, isWhiteTurn)) {
            return false;
        }
        int newCol = newPos.getCol();
        int newRow = newPos.getRow();
        int currentCol = getPosition().getCol();
        int currentRow = getPosition().getRow();
        if ((Math.abs(newCol - currentCol) == 1) && (Math.abs(newRow - currentRow) == 2)) {
            return true;
        }
        if ((Math.abs(newCol - currentCol) == 2) && (Math.abs(newRow - currentRow) == 1)) {
            return true;
        }
        return false;
    }

    @Override
    public Piece copy() {
        Piece copy;
        if (getPosition() == null) {
            copy = new Knight(isWhite(), null);
        } else {
            copy = new Knight(isWhite(), getPosition().copy());
        }
        copy.setId(getId());
        return copy;
    }

    @Override
    public String getFilePath() {
        if (this.isWhite()) {
            return whiteKnightFilePath;
        }
        return blackKnightFilePath;
    }
}

