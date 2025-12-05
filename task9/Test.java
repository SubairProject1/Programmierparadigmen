import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 * Subair: helped with Test by providing a structure, worked on Sammelpunkt class and worked on some methods in Simulation
 * Catalin: Implemented the logic of the Simulation and the printing to the output file
 * Andrei: Worked mainly on the Person class
 */
public class Test {
    public static final String CLASS_PATH = System.getProperty("java.class.path");
    public static final File FILE = new File("test.out");

    public static void main(String[] args) {
        // Clear the test.out file
        clearFile();

        // Shared pipeline for inter-process communication
        BlockingQueue<PersonData> pipeline = Constants.pipeline;

        // Start the Sammelpunkt process as a thread
        Sammelpunkt sammelpunkt = new Sammelpunkt(pipeline);
        Thread sammelpunktThread = new Thread(sammelpunkt);
        sammelpunktThread.start();

        try {
            // Launch Simulation processes
            launchSimulation("10", "grid1");

            // Notify Sammelpunkt to terminate
            pipeline.put(Sammelpunkt.END_MARKER);
            sammelpunktThread.join();
        } catch (IOException | InterruptedException e) {
            System.err.println("An exception occurred: " + e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Clears the contents of test.out.
     */
    public static void clearFile() {
        try {
            if (FILE.exists()) {
                FILE.delete();
            }
            if (FILE.createNewFile()) {
                System.out.println("File test.out cleared.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Launches a Simulation process with the given arguments.
     *
     * @param numberOfPeople The number of people in the simulation.
     * @param gridName       The name of the grid for the simulation.
     * @throws IOException If the process cannot be started.
     * @throws InterruptedException If the process is interrupted.
     */
    public static void launchSimulation(String numberOfPeople, String gridName) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "java", "-cp", CLASS_PATH, "Simulation", gridName, numberOfPeople
        );
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);

        Process simulationProcess = processBuilder.start();
        int exitCode = simulationProcess.waitFor();
        if (exitCode == 0) {
            System.out.println("Simulation (" + gridName + ", " + numberOfPeople + ") finished successfully.");
        } else {
            System.err.println("Simulation (" + gridName + ", " + numberOfPeople + ") failed with exit code " + exitCode);
        }
    }
}
