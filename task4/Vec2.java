/**
 * The Vec2 class represents a two-dimensional vector.
 * It includes basic operations to access the x and y coordinates.
 */
public class Vec2 {
    private double x, y;

    /**
     * Constructs a Vec2 object with the given x and y coordinates.
     *
     * @param x the x-coordinate of the vector
     * @param y the y-coordinate of the vector
     */
    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x-coordinate of the vector.
     *
     * @return the x-coordinate of the vector
     */
    public double x() { return x; }

    /**
     * Returns the y-coordinate of the vector.
     *
     * @return the y-coordinate of the vector
     */
    public double y() { return y; }
}

