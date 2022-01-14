package org.cis120.kriegspiel;

public class Rook extends Piece {
    private boolean canCastle;
    private static String whiteRookFilePath = "files/WhiteRook.PNG";
    private static String blackRookFilePath = "files/BlackRook.PNG";

    public Rook(boolean isWhite, Position pos) {
        this.setWhite(isWhite);
        this.setPosition(pos);
        this.setName("Rook");
        this.setStrength(.9);
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
        if (currentRow != newRow && currentCol != newCol) {
            return false;
        }
        if (currentRow == newRow) {
            int end = Math.max(currentCol, newCol);
            int start = Math.min(currentCol, newCol);
            for (int c = start + 1; c < end; c++) {
                if (board[currentRow][c] != null &&
                        board[currentRow][c].isWhite() != isWhiteTurn) {
                    return true;
                }
                if (board[currentRow][c] != null &&
                        board[currentRow][c].isWhite() == isWhiteTurn) {
                    return false;
                }
            }
        } else {
            int end = Math.max(currentRow, newRow);
            int start = Math.min(currentRow, newRow);
            for (int r = start + 1; r < end; r++) {
                if (board[r][currentCol] != null &&
                        board[r][currentCol].isWhite() != isWhiteTurn) {
                    return true;
                }
                if (board[r][currentCol] != null &&
                        board[r][currentCol].isWhite() == isWhiteTurn) {
                    return false;
                }
            }
        }
        canCastle = false;
        return true;
    }

    @Override
    public Position pathMove(Position newPos, Piece[][] board) {
        int newCol = newPos.getCol();
        int newRow = newPos.getRow();
        int currentCol = getPosition().getCol();
        int currentRow = getPosition().getRow();
        int endRow = newRow;
        int endCol = newCol;
        if (currentRow == newRow) {
            if (currentCol < newCol) {
                for (int c = currentCol + 1; c < newCol; c++) {
                    if (board[currentRow][c] != null &&
                            board[currentRow][c].isWhite() != this.isWhite()) {
                        endCol = c;
                        break;
                    }
                }
            } else {
                for (int c = currentCol - 1; c > newCol; c--) {
                    if (board[currentRow][c] != null &&
                            board[currentRow][c].isWhite() != this.isWhite()) {
                        endCol = c;
                        break;
                    }
                }
            }
        } else {
            if (currentRow < newRow) {
                for (int r = currentRow + 1; r < newRow; r++) {
                    if (board[r][currentCol] != null &&
                            board[r][currentCol].isWhite() != this.isWhite()) {
                        endRow = r;
                        break;
                    }
                }
            } else {
                for (int r = currentRow - 1; r > newRow; r--) {
                    if (board[r][currentCol] != null &&
                            board[r][currentCol].isWhite() != this.isWhite()) {
                        endRow = r;
                        break;
                    }
                }
            }
        }
        return new Position(endRow, endCol);
    }

    public boolean canCastle() {
        return canCastle;
    }

    @Override
    public Piece copy() {
        Piece copy;
        if (getPosition() == null) {
            copy = new Rook(isWhite(), null);
        } else {
            copy = new Rook(isWhite(), getPosition().copy());
        }
        copy.setId(getId());
        return copy;
    }

    @Override
    public String getFilePath() {
        if (this.isWhite()) {
            return whiteRookFilePath;
        }
        return blackRookFilePath;
    }

}

