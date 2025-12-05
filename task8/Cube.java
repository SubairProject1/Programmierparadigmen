import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Cube {
    private int x, y, z;

    public Cube(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Check if this cube is south-east of the given cube
     * @param cube the cube to compare with
     * @return true if this cube is south-east of the given cube
     */
    public boolean isSouthEastOf(Cube cube) {
        return Stream.of(x > cube.x, y > cube.y, z == cube.z).allMatch(Boolean::booleanValue);
    }

    /**
     * Check if this cube is south-west of the given cube
     * @param cube the cube to compare with
     * @return true if this cube is south-west of the given cube
     */
    public boolean isSouthWestOf(Cube cube) {
        return Stream.of(x < cube.x, y > cube.y, z == cube.z).allMatch(Boolean::booleanValue);
    }

    /**
     * Check if the south side of this cube is sunlit.
     * A south side is considered sunlit if there is no cube
     * further south (in the -y direction) within a distance less than 5 * (z + 1)
     * where the other cube's height is at least as high as the current cube.
     * @param cube the cube to compare with
     * @return true if this cube is sufficiently far south and lower in height.
     */
    public boolean isSouthOf(Cube cube) {
        return y > cube.y && (y - cube.y) > 5 * (z - cube.z + 1);
    }

    /**
     * Get the x-coordinate of the cube
     * @return the x-coordinate of the cube
     */
    public int getX() {
        return x;
    }

    /**
     * Get the y-coordinate of the cube
     * @return the y-coordinate of the cube
     */
    public int getY() {
        return y;
    }

    /**
     * Get the z-coordinate of the cube
     * @return the z-coordinate of the cube
     */
    public int getZ() {
        return z;
    }

    @Override
    public String toString() {
        return Stream.of(x, y, z)
                .map(Object::toString)
                .collect(Collectors.joining(", ", "(", ")"));
    }
}
