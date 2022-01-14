package org.cis120.kriegspiel;

public class Piece {

    private boolean isWhite;
    private String name;
    private double strength;
    private Position position;
    private int id;
    private static int counter = 0;

    public Piece() {
        counter++;
        this.id = counter;
    }

    public Piece(int id) {
        this.id = id;
    }

    public boolean isLegalMove(Position newPos, Piece[][] board, boolean isWhiteTurn) {
        if (newPos == null) {
            return false;
        }
        if (!newPos.inBoundsPosition()) {
            return false;
        }
        if (this.isWhite != isWhiteTurn) {
            return false;
        }
        Piece target = board[newPos.getRow()][newPos.getCol()];
        if (target == null) {
            return true;
        }
        if (target.isWhite() == isWhite) {
            return false;
        }
        return true;
    }

    public boolean isLegalMove(Position newPos, Piece[][] board,
                               boolean isWhiteTurn, Piece lastMoved) {
        if (newPos == null) {
            return false;
        }
        if (!newPos.inBoundsPosition()) {
            return false;
        }
        if (this.isWhite != isWhiteTurn) {
            return false;
        }
        Piece target = board[newPos.getRow()][newPos.getCol()];
        if (target == null) {
            return true;
        }
        if (target.isWhite() == isWhite) {
            return false;
        }
        return true;
    }

    public Position pathMove(Position newPos, Piece[][] board) {
        return newPos;
    };

    public String getName() {
        return name;
    }

    public double getStrength() {
        return strength;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public int getId() {
        return id;
    }

    protected void setWhite(boolean isWhite) {
        this.isWhite = isWhite;
    }

    protected void setId(int id) {
        this.id = id;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setStrength(double strength) {
        this.strength = strength;
    }

    public void setPosition(Position position) {
        this.position = position;
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
        Piece piece = (Piece) o;
        return this.id == piece.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public static void resetPieces() {
        counter = 0;
    }

    public Piece copy() {
        return null;
    }

    public String getFilePath() {
        return "Not Overridden";
    }

}

