import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * The Sammelpunkt class collects and stores the data of people who reach the meeting point.
 * The data includes a unique identification number, the number of movement steps on the escape route,
 * and the number of waiting steps performed. The Sammelpunkt process is connected to the Simulation process via a pipeline.
 * When the Simulation process terminates, the PersonData objects are written to the file `test.out`.
 */
public class Sammelpunkt implements Runnable {
    private final BlockingQueue<PersonData> pipeline;
    public static final PersonData END_MARKER = new PersonData(-1, 0, 0);
    private final List<PersonData> personDataList = new ArrayList<>();

    /**
     * Constructs a Sammelpunkt process with the given pipeline.
     *
     * @param pipeline The pipeline for receiving person data
     */
    public Sammelpunkt(BlockingQueue<PersonData> pipeline) {
        this.pipeline = pipeline;
    }

    /**
     * Runs the Sammelpunkt process, collecting person data from the pipeline
     * until the END_MARKER is received. Then writes the collected data to a file.
     */
    @Override
    public void run() {
        try (FileWriter writer = new FileWriter("test.out", true)) {
            while (true) {
                PersonData data = pipeline.take(); // Deserialize data
                if (data == END_MARKER) {
                    System.out.println("Sammelpunkt received END_MARKER. Exiting.");
                    break;
                }
                personDataList.add(data);
                System.out.println("Received: " + data);
            }

            // Write all person data to file
            writer.write("Person Data Collected:\n");
            for (PersonData data : personDataList) {
                writer.write(data + "\n");
            }
            System.out.println("Data written to test.out.");
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}