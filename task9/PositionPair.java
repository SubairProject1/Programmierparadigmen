public class PositionPair {

    private final Position left;
    private final Position right;

    /**
     * @return The position of the left foot.
     */
    public Position getLeft() {
        return left;
    }

    /**
     * @return The position of the right foot.
     */
    public Position getRight() {
        return right;
    }

    /**
     * Constructs a PositionPair with the given left and right positions.
     *
     * @param left  The position of the left foot.
     * @param right The position of the right foot.
     */
    public PositionPair(Position left, Position right) {

        this.left = left;
        this.right = right;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PositionPair pair2 = (PositionPair) obj;
        return left.equals(pair2.left) && right.equals(pair2.right);
    }

    @Override
    public String toString() {
        return "Left: " + left + ", Right: " + right;
    }
}
