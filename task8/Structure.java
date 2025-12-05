import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Structure {
    private final Set<Cube> cubes;

    public Set<Cube> getCubes() {
        return cubes;
    }

    public Structure() {
        cubes = new HashSet<>();
    }

    public Structure(Set<Cube> cubes) {
        this.cubes = cubes;
    }

    /**
     * Returns the height of the structure at the given position using streams.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @return height of the structure at the given position
     */
    public int maxHeightAt(int x, int y) {
        return cubes.stream()  // **Create a stream** of cubes.
                .filter(c -> c.getX() == x && c.getY() == y)  // **Filter:** Keep cubes at `(x, y)`.
                .mapToInt(Cube::getZ)  // **Map to height:** Convert each cube to its `z` (height).
                .max()  // **Get max:** Find the maximum height.
                .orElse(0);  // **Default:** Return 0 if no cubes exist at `(x, y)`.
    }

    /**
     * Adds a cube to the structure at the given position.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void addCube(int x, int y) {
        cubes.add(new Cube(x, y, maxHeightAt(x, y) + 1));  // **Add new cube** with height `maxHeight + 1`.
    }

    /**
     * Checks if a cube exists at the specified coordinates.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @param z z-coordinate
     * @return `true` if a cube exists at the specified position, `false` otherwise
     */
    public boolean hasCubeAt(int x, int y, int z) {
        return cubes.stream()  // **Create a stream** of cubes.
                .anyMatch(c -> c.getX() == x && c.getY() == y && c.getZ() == z);  // **Check existence** of a cube at `(x, y, z)`.
    }

    /**
     * Evaluate the structure's thermal score.
     * - `mapToDouble`: Maps each cube to its thermal score.
     * - `sum()`: Sums the thermal scores of all cubes.
     *
     * @return thermal score of the structure
     */
    public double evaluateThermalScore() {
        return cubes.stream()  // **Stream of cubes** in the structure.
                .mapToDouble(this::thermalScore)  // **Map to thermal score** for each cube.
                .sum();  // **Sum:** Aggregate thermal scores.
    }

    /**
     * Evaluate the structure's visibility score.
     *
     * @return visibility score of the structure
     */
    public double evaluateVisibilityScore() {
        return cubes.stream()  // **Stream of cubes** in the structure.
                .flatMapToDouble(cube -> Arrays.stream(new int[][]{  // **Map to directions** for visibility score calculation.
                        {1, 0},  // East
                        {-1, 0}, // West
                        {0, 1},  // North
                        {0, -1}  // South
                }).mapToDouble(dir -> visibilityScore(cube, dir[0], dir[1])))  // **Map to visibility score** for each direction.
                .sum();  // **Sum:** Aggregate visibility scores.
    }

    /**
     * Evaluate the structure's catastrophe resistance score.
     *
     * @return catastrophe resistance score of the structure
     */
    public double evaluateCatastropheScore() {
        int totalGroundCubes = (int) cubes.stream()  // **Stream creation:** Create a stream of cubes.
                .filter(cube -> cube.getZ() == 1)  // **Filter:** Select only cubes at ground level (`z == 1`).
                .count();  // **Count:** Count the number of ground-level cubes.

        return cubes.stream()  // **Stream creation:** Create a stream of cubes.
                .mapToDouble(cube -> catastropheResistanceScore(cube, totalGroundCubes))  // **Map to double:** Map each cube to its catastrophe resistance score.
                .sum();  // **Sum:** Aggregate the catastrophe resistance scores of all cubes.
    }


    /**
     * Evaluate an individual cube's thermal score.
     *
     * @param cube Cube to evaluate
     * @return thermal score of the cube
     */
    public double thermalScore(Cube cube) {
        return Arrays.stream(new int[][]{  // **Directions for neighboring cubes.**
                {1, 0, 0},  // East
                {-1, 0, 0}, // West
                {0, 1, 0},  // North
                {0, -1, 0}, // South
                {0, 0, 1},  // Top
                {0, 0, -1}  // Bottom
        }).mapToDouble(dir -> {  // **Map direction to thermal score.**
            int nx = cube.getX() + dir[0];
            int ny = cube.getY() + dir[1];
            int nz = cube.getZ() + dir[2];
            if (hasCubeAt(nx, ny, nz)) {
                return 1.0;  // Neighbor present: return 1.0.
            } else if (dir[0] == 1 && isExposedEast(cube)) {
                return 0.2;  // Bonus for east exposure.
            } else if (dir[0] == -1 && isExposedWest(cube)) {
                return 0.1;  // Bonus for west exposure.
            } else if (dir[1] == -1 && isExposedSouth(cube)) {
                return 0.5;  // Bonus for south exposure.
            }
            return 0.0;  // No contribution.
        }).sum();  // **Sum:** Aggregate thermal scores.
    }

    /**
     * Calculate the visibility score for a specific direction.
     *
     * @param cube Cube to evaluate.
     * @param dx Change in x-coordinate.
     * @param dy Change in y-coordinate.
     * @return visibility score for the direction
     */
    private double visibilityScore(Cube cube, int dx, int dy) {
        if (hasCubeAt(cube.getX() + dx, cube.getY() + dy, cube.getZ())) {
            return 0.0;  // Immediate obstruction.
        }

        double distanceFactor = IntStream.range(1, 25)  // **Range:** Check distances up to 25 units.
                .filter(distance -> hasCubeAt(cube.getX() + dx * distance, cube.getY() + dy * distance, cube.getZ()))  // **Filter:** Check for obstruction.
                .mapToDouble(distance -> (double) distance / 25)  // **Map to score:** Convert distance to score.
                .findFirst()  // **Find first obstruction** distance.
                .orElse(1.0);  // **Default:** Return 1.0 if unobstructed.

        long restrictedEdges = Stream.of(  // **Stream of restrictions:**
                hasCubeAt(cube.getX(), cube.getY() - 1, cube.getZ()) || cube.getZ() == 1,  // Bottom
                hasCubeAt(cube.getX() - 1, cube.getY(), cube.getZ()),  // Left
                hasCubeAt(cube.getX() + 1, cube.getY(), cube.getZ())   // Right
        ).filter(Boolean::booleanValue).count();  // **Count restricted edges.**

        double restrictionFactor = switch ((int) restrictedEdges) {
            case 1 -> 0.5;  // One restriction.
            case 2 -> 0.25;  // Two restrictions.
            case 3 -> 0.125;  // All restricted.
            default -> 1.0;  // No restrictions.
        };

        return distanceFactor * restrictionFactor;  // Multiply distance by restriction factor.
    }

    /**
     * Evaluate a cube's catastrophe resistance score.
     *
     * @param cube Cube to evaluate
     * @param totalGroundCubes Total number of ground-level cubes
     * @return catastrophe resistance score for the cube
     */
    private double catastropheResistanceScore(Cube cube, int totalGroundCubes) {
        int z = cube.getZ();
        int groundLevel = 1;

        // **Ground contribution: +1 if cube is at ground level.**
        double groundScore = (z == groundLevel) ? 1.0 : 0.0;

        // **Stream over directions to compute surface contributions.**
        double surfaceScore = Arrays.stream(new int[][]{
                        {1, 0, 0},  // East
                        {-1, 0, 0}, // West
                        {0, 1, 0},  // North
                        {0, -1, 0}, // South
                        {0, 0, 1},  // Top
                        {0, 0, -1}  // Bottom
                })  // **Stream of neighboring directions.**
                .mapToDouble(dir -> {
                    int nx = cube.getX() + dir[0];  // Calculate neighboring x-coordinate.
                    int ny = cube.getY() + dir[1];  // Calculate neighboring y-coordinate.
                    int nz = cube.getZ() + dir[2];  // Calculate neighboring z-coordinate.

                    // **If the neighboring position has a cube: shared surface penalty.**
                    return hasCubeAt(nx, ny, nz)
                            ? 1.0 - Math.pow((double) z / totalGroundCubes, 2)
                            : 0.6 - Math.pow((double) z / totalGroundCubes, 2);  // **Otherwise: vulnerability penalty for outside surface.**
                })
                .sum();  // **Sum:** Aggregate the scores from all surfaces.

        // **Ground penalty:** Penalize for excessive ground usage.
        double groundPenalty = totalGroundCubes > 0 ? totalGroundCubes * 0.2 : 0.0;

        // **Final score: Combine ground score, surface score, and ground penalty.**
        return groundScore + surfaceScore - groundPenalty;
    }


    /**
     * Checks if the east side of the cube is exposed.
     * @param cube Cube to check
     * @return true if the east side is exposed
     */
    private boolean isExposedEast(Cube cube) {
        return cubes.stream().noneMatch(c -> c.isSouthEastOf(cube));
    }


    /**
     * Checks if the west side of the cube is exposed.
     * @param cube Cube to check
     * @return true if the west side is exposed
     */
    private boolean isExposedWest(Cube cube) {
        return cubes.stream().noneMatch(c -> c.isSouthWestOf(cube));
    }


    /**
     * Checks if the south side of the cube is exposed.
     * @param cube Cube to check
     * @return true if the south side is exposed
     */
    private boolean isExposedSouth(Cube cube) {
        return cubes.stream().noneMatch(c -> c.isSouthOf(cube));
    }

    /**
     * Checks if at least one lateral side of each cube in the structure is free.
     *
     * @return `true` if at least one lateral side of each cube is free, `false` otherwise
     */
    public boolean hasFreeLateralSide() {
        return cubes.stream().allMatch(cube ->  // **Check all cubes.**
                Stream.of(
                        !hasCubeAt(cube.getX() + 1, cube.getY(), cube.getZ()),  // East
                        !hasCubeAt(cube.getX() - 1, cube.getY(), cube.getZ()),  // West
                        !hasCubeAt(cube.getX(), cube.getY() + 1, cube.getZ()),  // North
                        !hasCubeAt(cube.getX(), cube.getY() - 1, cube.getZ())   // South
                ).anyMatch(Boolean::booleanValue)  // **At least one free side.**
        );
    }

    @Override
    public String toString() {
        int minX = cubes.stream().mapToInt(Cube::getX).min().orElse(0);
        int maxX = cubes.stream().mapToInt(Cube::getX).max().orElse(0);
        int minY = cubes.stream().mapToInt(Cube::getY).min().orElse(0);
        int maxY = cubes.stream().mapToInt(Cube::getY).max().orElse(0);

        return IntStream.rangeClosed(minY, maxY)  // **Iterate over y-coordinates (rows).**
                .mapToObj(y -> IntStream.rangeClosed(minX, maxX)  // **Iterate over x-coordinates (columns).**
                        .mapToObj(x -> {
                            int height = maxHeightAt(x, maxY - (y - minY));  // Get height at `(x, y)`.
                            return height == 0 ? " " : height <= 9 ? String.valueOf(height) : String.valueOf((char) ('A' + (height - 10)));
                        })
                        .collect(Collectors.joining()))  // **Join heights into a row.**
                .collect(Collectors.joining("\n"));  // **Join rows into a string with newlines.**
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Structure))
            return false;
        return this.toString().equals(obj.toString());
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}
