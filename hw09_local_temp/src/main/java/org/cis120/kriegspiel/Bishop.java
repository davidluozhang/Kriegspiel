package org.cis120.kriegspiel;

public class Bishop extends Piece {
    private static String whiteBishopFilePath = "files/WhiteBishop.PNG";
    private static String blackBishopFilePath = "files/BlackBishop.PNG";

    public Bishop(boolean isWhite, Position pos) {
        this.setWhite(isWhite);
        this.setPosition(pos);
        this.setName("Bishop");
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
        if (Math.abs(newCol - currentCol) != Math.abs(newRow - currentRow)) {
            return false;
        }
        int c = currentCol;
        if (newCol > currentCol && newRow > currentRow) {
            for (int r = currentRow + 1; r < newRow; r++) {
                c++;
                if (c >= newCol) {
                    break;
                }
                if (board[r][c] != null && board[r][c].isWhite() != isWhiteTurn) {
                    return true;
                }
                if (board[r][c] != null && board[r][c].isWhite() == isWhiteTurn) {
                    return false;
                }
            }
        } else if (newCol < currentCol && newRow < currentRow) {
            for (int r = currentRow - 1; r > newRow; r--) {
                c--;
                if (c < newCol) {
                    break;
                }
                if (board[r][c] != null && board[r][c].isWhite() != isWhiteTurn) {
                    return true;
                }
                if (board[r][c] != null && board[r][c].isWhite() == isWhiteTurn) {
                    return false;
                }
            }
        } else if (newCol > currentCol && newRow < currentRow) {
            for (int r = currentRow - 1; r > newRow; r--) {
                c++;
                if (c >= newCol) {
                    break;
                }
                if (board[r][c] != null && board[r][c].isWhite() != isWhiteTurn) {
                    return true;
                }
                if (board[r][c] != null && board[r][c].isWhite() == isWhiteTurn) {
                    return false;
                }
            }
        } else if (newCol < currentCol && newRow > currentRow) {
            for (int r = currentRow + 1; r < newRow; r++) {
                c--;
                if (c < newCol) {
                    break;
                }
                if (board[r][c] != null && board[r][c].isWhite() != isWhiteTurn) {
                    return true;
                }
                if (board[r][c] != null && board[r][c].isWhite() == isWhiteTurn) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Position pathMove(Position newPos, Piece[][] board) {
        int currentRow = getPosition().getRow();
        int currentCol = getPosition().getCol();
        int newCol = newPos.getCol();
        int newRow = newPos.getRow();
        int c = currentCol;
        int endRow = newRow;
        int endCol = newCol;
        if (newCol > currentCol && newRow > currentRow) {
            for (int r = currentRow + 1; r < newRow; r++) {
                c++;
                if (c >= newCol) {
                    break;
                }
                if (board[r][c] != null && board[r][c].isWhite() != this.isWhite()) {
                    endRow = r;
                    endCol = c;
                    break;
                }
            }
        } else if (newCol < currentCol && newRow < currentRow) {
            for (int r = currentRow - 1; r > newRow; r--) {
                c--;
                if (c < newCol) {
                    break;
                }
                if (board[r][c] != null && board[r][c].isWhite() != this.isWhite()) {
                    endRow = r;
                    endCol = c;
                    break;
                }
            }
        } else if (newCol > currentCol && newRow < currentRow) {
            for (int r = currentRow - 1; r > newRow; r--) {
                c++;
                if (c >= newCol) {
                    break;
                }
                if (board[r][c] != null && board[r][c].isWhite() != this.isWhite()) {
                    endRow = r;
                    endCol = c;
                    break;
                }
            }
        } else if (newCol < currentCol && newRow > currentRow) {
            for (int r = currentRow + 1; r < newRow; r++) {
                c--;
                if (c < newCol) {
                    break;
                }
                if (board[r][c] != null && board[r][c].isWhite() != this.isWhite()) {
                    endRow = r;
                    endCol = c;
                    break;
                }
            }
        }
        return new Position(endRow, endCol);
    }

    @Override
    public Piece copy() {
        Bishop copy;
        if (getPosition() == null) {
            copy = new Bishop(isWhite(), null);
        } else {
            copy = new Bishop(isWhite(), getPosition().copy());
        }
        copy.setId(getId());
        return copy;
    }

    @Override
    public String getFilePath() {
        if (this.isWhite()) {
            return whiteBishopFilePath;
        }
        return blackBishopFilePath;
    }

}

