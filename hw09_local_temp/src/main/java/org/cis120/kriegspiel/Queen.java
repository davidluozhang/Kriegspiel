package org.cis120.kriegspiel;

public class Queen extends Piece {
    private static String whiteQueenFilePath = "files/WhiteQueen.PNG";
    private static String blackQueenFilePath = "files/BlackQueen.PNG";

    public Queen(boolean isWhite, Position pos) {
        this.setWhite(isWhite);
        this.setPosition(pos);
        this.setName("Queen");
        this.setStrength(1);
    }

    //For pawn promotion
    public Queen(int id, boolean isWhite, Position pos) {
        this.setId(id);
        this.setWhite(isWhite);
        this.setPosition(pos);
        this.setName("Queen");
        this.setStrength(1);
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
            return true;
        } else if (currentCol == newCol) {
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
            return true;
        }
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
                if (c == newCol) {
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
        int endRow = newRow;
        int endCol = newCol;
        if (newCol != currentCol && newRow != currentRow) {
            int c = currentCol;
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
        } else {
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
            } else if (currentCol == newCol) {
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
        }
        return new Position(endRow, endCol);
    }

    @Override
    public Piece copy() {
        Piece copy;
        if (getPosition() == null) {
            copy = new Queen(isWhite(), null);
        } else {
            copy = new Queen(isWhite(), getPosition().copy());
        }
        copy.setId(getId());
        return copy;
    }

    @Override
    public String getFilePath() {
        if (this.isWhite()) {
            return whiteQueenFilePath;
        }
        return blackQueenFilePath;
    }

}

