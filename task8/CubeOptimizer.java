import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CubeOptimizer {

    private final Function<Structure, Double> scoringFunction;
    private final double diversityThreshold;

    /**
     * Constructor to pass a custom scoring function for comparing structures.
     *
     * @param scoringFunction the function to be used for scoring the structures
     */
    public CubeOptimizer(Function<Structure, Double> scoringFunction, double diversityThreshold) {
        this.scoringFunction = scoringFunction;
        this.diversityThreshold = diversityThreshold;
    }

    /**
     * Optimization function to find k diverse structures for a given number of cubes and maximum height.
     * Ensures that returned solutions have diverse evaluation scores.
     *
     * @param n total number of cubes
     * @param maxHeight maximum height of the structure
     * @param k maximum number of returned solutions
     * @return set of the top `k` best structures (1 best, k-1 diverse)
     */
    public Set<Structure> optimize(int n, int maxHeight, int k) {
        Set<Structure> solutions = new HashSet<>();  // Stores all possible structures

        // Base case: if only one cube, create the initial structure at (0, 0, 0)
        if (n == 1) {
            Structure initialStructure = new Structure();  // Create an empty structure
            initialStructure.addCube(0, 0);  // Add the first cube at (0, 0)
            solutions.add(initialStructure);  // Add the structure to the solution set
            return solutions;  // Return the singleton solution set
        }

        // Recursive call: get solutions for (n - 1) cubes
        Set<Structure> previousSolutions = optimize(n - 1, maxHeight, k);

        // **FlatMap and map pipeline:** Generate all possible structures by adding one cube to existing structures
        previousSolutions.stream()  // Stream of previous structures
                .flatMap(prevStructure -> generateNewStructures(maxHeight, previousSolutions).stream())  // Generate new structures by adding cubes
                .forEach(solutions::add);  // Collect new structures into the solution set

        // If the number of solutions is less than or equal to `k`, return all sorted solutions
        if (solutions.size() <= k) {
            return sortDescendingByScore(solutions);  // Sort solutions by descending score and return
        }

        // **Sorting:** Sort the solutions by score in descending order
        List<Structure> sortedSolutions = solutions.stream()
                .sorted(Comparator.comparing(scoringFunction).reversed())  // Sort using the scoring function in descending order
                .collect(Collectors.toList());  // Collect to a list

        // **Selecting the best structure:** First element in sorted list is the highest-scoring structure
        Structure bestStructure = sortedSolutions.get(0);

        // Get remaining structures after the best one
        List<Structure> remainingStructures = sortedSolutions.subList(1, sortedSolutions.size());

        // Shuffle remaining structures to introduce randomness in the selection process
        Collections.shuffle(remainingStructures);  // Shuffle the list to vary the selection order

        // **Final selection set:** Add the best structure and select additional diverse structures
        Set<Structure> finalSolutions = new LinkedHashSet<>();
        finalSolutions.add(bestStructure);  // Add the highest-scoring structure to the final solutions

        // **Filtering for diversity:** Add structures only if they differ significantly in score from the selected ones
        remainingStructures.stream()  // Stream of remaining structures
                .filter(structure -> finalSolutions.stream()  // For each remaining structure, compare against already selected solutions
                        .allMatch(selected -> Math.abs(scoringFunction.apply(selected) - scoringFunction.apply(structure))  // Compute the score difference
                                > diversityThreshold * scoringFunction.apply(selected)))  // Ensure score difference exceeds the diversity threshold
                .limit(k - 1)  // Limit to `k - 1` additional diverse structures
                .forEach(finalSolutions::add);  // Add the filtered structures to the final solution set

        return sortDescendingByScore(finalSolutions);  // Return the sorted final solution set
    }

    /**
     * Sorts a set of structures in descending order by their score and returns a LinkedHashSet (to preserve order).
     *
     * @param structures the set of structures to sort
     * @return a sorted LinkedHashSet of structures
     */
    private Set<Structure> sortDescendingByScore(Set<Structure> structures) {
        return structures.stream()  // Create a stream of structures
                .sorted(Comparator.comparing(scoringFunction).reversed())  // Sort by score in descending order
                .collect(Collectors.toCollection(LinkedHashSet::new));  // Collect into a LinkedHashSet to preserve order
    }

    /**
     * Generate possible new structures by adding one cube to valid positions in the given structure.
     *
     * @param maxHeight          maximum height allowed for the structure
     * @param previousStructures set of previous structures
     * @return set of new valid structures
     */
    private Set<Structure> generateNewStructures(int maxHeight, Set<Structure> previousStructures) {
        return previousStructures.stream()  // Stream of previous structures
                .flatMap(structure -> structure.getCubes().stream()  // Stream of cubes in the structure
                        .flatMap(existingCube -> Stream.of(  // Generate adjacent positions for each cube
                                new int[]{1, 0},  // East
                                new int[]{-1, 0}, // West
                                new int[]{0, 1},  // North
                                new int[]{0, -1}  // South
                        ).map(dir -> {  // Map each direction to a new cube position
                            int newX = existingCube.getX() + dir[0];  // Compute new x-coordinate
                            int newY = existingCube.getY() + dir[1];  // Compute new y-coordinate
                            int newZ = structure.maxHeightAt(newX, newY) + 1;  // Compute new height (`z`)

                            // Check if the new cube position is valid
                            if (newZ <= maxHeight && !structure.hasCubeAt(newX, newY, newZ)) {  // Ensure height constraint and no cube at new position
                                Structure newStructure = new Structure(new HashSet<>(structure.getCubes()));  // Create a copy of the current structure
                                newStructure.addCube(newX, newY);  // Add the new cube to the structure
                                return newStructure.hasFreeLateralSide() ? newStructure : null;  // Return structure if it has a free lateral side
                            }
                            return null;  // Return null if the position is invalid
                        }))
                        .filter(Objects::nonNull))  // **Filter:** Remove null values (invalid structures)
                .collect(Collectors.toSet());  // Collect valid structures into a set
    }
}
