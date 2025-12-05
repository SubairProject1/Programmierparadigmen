import java.util.Collection;

/**
 * AUTHOR: Subair
 * STYLE: Procedural Programming
 * The PrintSimulation interface exemplifies procedural programming because it defines static methods that perform
 * actions directly, without encapsulating data or behavior in objects. It processes the data in resultList linearly
 * to print or analyze, focusing on functional logic rather than object-oriented principles.
 *
 * This interface defines the behavior for printing simulation results in various formats.
 * It provides methods to print all simulation results or the one closest to the average sustainability value.
 *
 * GOOD: The PrintSimulation interface maintains high cohesion by focusing solely
 * on printing functionalities for simulation results. Any additional functionality
 * would reduce cohesion. A variant with low cohesion might involve unrelated
 * operations, like file handling or direct calculation adjustments.
 * It demonstrates effective procedural programming by focusing on isolated,
 * linear data-processing methods that perform specific actions with minimal dependencies and straightforward logic.
 */

public interface PrintSimulation {

    /**
     * Prints sustainability of all simulation results from the provided collection.
     * Each result is printed on a new line, followed by an extra line for clarity.
     *
     * @param resultList a collection of simulation result strings to be printed
     *
     * Precondition: The resultList is not null and has valid simulation results that can be processed by extractSustainability().
     * Postcondition: The sustainability values of all simulation results are printed to the standard output.
     * Invariant: The resultList maintains the same order throughout the printing process.
     *
     * BAD: The printAllSimulationResult method is highly coupled to the exact format of result strings,
     * assuming they contain "sustainability: " followed by a numeric value (see extractSustainability(...)).
     * A more loosely-coupled design would allow the format to be specified or make this functionality
     * part of a separate class responsible for extracting sustainability values from strings.
     */
    static void printAllSimulationResult(Collection<String> resultList) {
        if(resultList == null) { throw new IllegalArgumentException("The resultList is null."); }
        for (String result : resultList) {
            double sustainability = extractSustainability(result);
            System.out.print(sustainability + " ");
        }
        System.out.println("\n");
    }

    /**
     * Prints the simulation result that has the sustainability value closest to the average
     * of all provided simulation results. The printed result has "SimulationResult" replaced
     * by "AverageSimulationResult".
     *
     * @param resultList a collection of simulation result strings to analyze
     *
     * Precondition: The resultList is not null and has valid simulation results that can be processed by extractSustainability().
     * Postcondition: The result with the sustainability value closest to the average is printed with "SimulationResult" replaced by "AverageSimulationResult or nothing if the list is empty.
     * Invariant: The resultList maintains the same order throughout this method.
     *
     * BAD: The printSimulationClosestToAverage method combines calculation of the average sustainability
     * with the printing of the result. This reduces cohesion, as a better design would
     * separate the calculation logic from the output logic, possibly into two methods.
     */
    static void printSimulationClosestToAverage(Collection<String> resultList) {

        if(resultList == null) { throw new IllegalArgumentException("The resultList is null."); }
        double totalSustainability = 0;
        for (String result : resultList) {
            totalSustainability += extractSustainability(result);
        }
        double averageSustainability = totalSustainability / resultList.size();

        String closestSimulation = null;
        double closestDifference = Double.MAX_VALUE;

        for (String result : resultList) {
            double sustainability = extractSustainability(result);
            double difference = Math.abs(sustainability - averageSustainability);

            if (difference < closestDifference) {
                closestDifference = difference;
                closestSimulation = result;
            }
        }

        if (closestSimulation != null) {
            closestSimulation = closestSimulation.replace("SimulationResult", "AverageSimulationResult");
        }

        System.out.println(closestSimulation);
    }

    /**
     * Extracts the sustainability value from a simulation result string.
     * This method assumes the format contains "sustainability: " followed by a numerical value.
     *
     * @param result the simulation result string containing the sustainability value
     * @return the extracted sustainability value as a double
     *
     * Precondition: The result string is not null and contains the keyword "sustainability: " followed by a numerical value.
     * Postcondition: Returns the sustainability value as a double extracted from the given result string.
     */
    private static double extractSustainability(String result) {
        String keyword = "sustainability: ";
        int index = result.indexOf(keyword);
        if (index == -1) { throw new IllegalArgumentException("Expected form for result is not maintained."); }
        String subStr = result.substring(index + keyword.length()).trim();
        String[] parts = subStr.split("\\s+");
        return Double.parseDouble(parts[0]);
    }
}
