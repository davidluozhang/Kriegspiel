package org.cis120.kriegspiel;

public class Position {
    private int row;
    private int col;


    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public static Position positionFromNotation(String file, int rank) {
        int col = -1;
        String[] files = {"a", "b", "c", "d", "e", "f", "g", "h"};
        for (int i = 0; i < files.length; i++) {
            if (file.equalsIgnoreCase(files[i])) {
                col = i;
                break;
            }
        }
        if (col == -1) {
            return null;
        }
        int row = 8 - rank;
        Position pos = new Position(row, col);
        return pos;
    }

    public static String notationFromPosition(Position pos) {
        String files = "ABCDEFGH";
        String file = files.substring(pos.col, pos.col + 1); //add an edge case test
        int rank = 8 - pos.row;
        return file + rank;
    }

    public boolean inBoundsPosition() {
        return (row >= 0 && row < 8 && col >= 0 && col < 8);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (!(getClass() == o.getClass())) {
            return false;
        }
        Position position = (Position) o;
        return (this.row == position.row && this.col == position.col);
    }

    public Position copy() {
        return new Position(row, col);
    }
}

