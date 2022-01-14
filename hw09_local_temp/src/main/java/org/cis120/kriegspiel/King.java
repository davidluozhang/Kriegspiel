package org.cis120.kriegspiel;

public class King extends Piece {
    private boolean canCastle;
    private static String whiteKingFilePath = "files/WhiteKing.PNG";
    private static String blackKingFilePath = "files/BlackKing.PNG";

    public King(boolean isWhite, Position pos) {
        this.setWhite(isWhite);
        this.setName("King");
        this.setStrength(.5);
        this.setPosition(pos);
        canCastle = true;
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
        int rowDist = Math.abs(newRow - currentRow);
        int colDist = Math.abs(newCol - currentCol);
        if (colDist > 1 || rowDist > 1) {
            if (rowDist == 0 && (colDist == 2 || colDist == 3)) {
                if (!isCastle(newPos, board, isWhiteTurn)) {
                    return false;
                }
            } else {
                return false;
            }
        }
        canCastle = false;
        return true;
    }

    public boolean isCastle(Position newPos, Piece[][] board, boolean isWhiteTurn) {
        if (!super.isLegalMove(newPos, board, isWhiteTurn)) {
            return false;
        }
        if (!canCastle) {
            return false;
        }
        int kingCol = newPos.getCol();
        int kingRow = newPos.getRow();
        Piece castleRook;
        if (kingCol == 6) {
            castleRook = board[kingRow][7];
            if (castleRook == null || !castleRook.getName().equals("Rook")) {
                return false;
            }
            Rook rook = (Rook) castleRook;
            if (!rook.canCastle()) {
                return false;
            }
            if (board[kingRow][5] == null && board[kingRow][6] == null) {
                canCastle = false;
                return true;
            }
            return false;
        } else if (kingCol == 2) {
            castleRook = board[kingRow][0];
            if (castleRook == null || !castleRook.getName().equals("Rook")) {
                return false;
            }
            Rook rook = (Rook) castleRook;
            if (!rook.canCastle()) {
                return false;
            }
            if (board[kingRow][1] == null && board[kingRow][2] == null
                    && board[kingRow][3] == null) {
                canCastle = false;
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public Piece copy() {
        King copy;
        if (getPosition() == null) {
            copy = new King(isWhite(), null);
        } else {
            copy = new King(isWhite(), getPosition().copy());
        }
        copy.canCastle = canCastle;
        copy.setId(getId());
        return copy;
    }

    @Override
    public String getFilePath() {
        if (this.isWhite()) {
            return whiteKingFilePath;
        }
        return blackKingFilePath;
    }
}
