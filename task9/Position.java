public class Position {
    private final int x;
    private final int y;

    /**
     * Constructs a position with the given x and y coordinates.
     *
     * @param x The x-coordinate of this position.
     * @param y The y-coordinate of this position.
     */
    public Position(int x, int y) {

        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x-coordinate of this position.
     *
     * @return The x-coordinate of this position.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of this position.
     *
     * @return The y-coordinate of this position.
     */
    public int getY() {
        return y;
    }

    /**
     * Returns whether this position is adjacent to another position.
     * @param other the other position to check adjacency with
     * @return true if the positions are adjacent, false otherwise
     */
    public boolean isAdjacent(Position other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y) == 1;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return x == position.x && y == position.y;
    }

    @Override
    public String toString() {
        return "X: " + x + ", Y: " + y;
    }

}
