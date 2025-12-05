import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 *  Catalin: Provided the overall structure of task 8 and worked on parts of the algorithm.
 *           Wrote Parts of Test.
 *  Andrei:  Worked on generateNewStructures() in functional style, wrote the third evaluation function
 *  Subair   Worked on optimize() in functional style, wrote the third evaluation function and finished Test.
 */
public class Test {
    public static void main(String[] args) {

        int n = 45;     // Number of cubes
        int m = 6;     // Maximum height of the structure
        int k = 5;      // Number of best solutions to return;

        double diversityThreshold = 0.1;

        Function<Structure, Double> scoringFunction1 = Structure::evaluateVisibilityScore;
        Function<Structure, Double> scoringFunction2 = Structure::evaluateThermalScore;
        Function<Structure, Double> scoringFunction3 = Structure::evaluateCatastropheScore;

        // Create an instance of CubeOptimize with the scoring functions
        CubeOptimizer cubeOptimizer1 = new CubeOptimizer(scoringFunction1, diversityThreshold);
        CubeOptimizer cubeOptimizer2 = new CubeOptimizer(scoringFunction2, diversityThreshold);
        CubeOptimizer cubeOptimizer3 = new CubeOptimizer(scoringFunction3, diversityThreshold);

        // Optimize the cubes using the cubeOptimizer instance and print results
        System.out.println("Visibility score: ");
        Set<Structure> optimizedCubes = cubeOptimizer1.optimize(n, m, k);
        List<Structure> results = new ArrayList<>(optimizedCubes);
        printResults(scoringFunction1, results, k);
        System.out.println();

        System.out.println("Thermal score: ");
        optimizedCubes = cubeOptimizer2.optimize(n, m, k);
        results = new ArrayList<>(optimizedCubes);
        printResults(scoringFunction2, results, k);
        System.out.println();

        System.out.println("Catastrophe score: ");
        optimizedCubes = cubeOptimizer3.optimize(n, m, k);
        results = new ArrayList<>(optimizedCubes);
        printResults(scoringFunction3, results, k);
        System.out.println();
    }

    /**
     * Print the k best structures along with their scores
     * @param scoringFunc the scoring function to evaluate the structure
     * @param results list of structures
     * @param k number of solutions to print
     */
    static void printResults(Function<Structure, Double> scoringFunc, List<Structure> results, int k) {
        System.out.println("Structures:");
        IntStream.range(0, results.size()).forEach(i -> {
            Structure structure = results.get(i);
            System.out.println("Solution " + (i + 1) + ":\n" + structure);
            System.out.println("Score (using scoring function): " + scoringFunc.apply(structure) + "\n");
        });
    }
}

