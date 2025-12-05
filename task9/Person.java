import java.util.Random;

/**
 * Represents a person in the evacuation simulation.
 * Each person is modeled as a Runnable entity that moves on a shared grid
 * toward a Sammelpunkt (assembly point). The person can move across adjacent field pairs
 * on the grid or wait if no valid moves are available. Movement and waiting steps are tracked,
 * and the grid is notified when a person reaches the Sammelpunkt.
 *
 * Responsibilities of this class include:
 * - Managing the person's movement across adjacent field pairs on the grid.
 * - Simulating random waits after making a move and when no valid moves are available.
 * - Tracking the number of movement and waiting steps.
 * - Notifying the grid when the Sammelpunkt is reached.
 *
 * The Person class uses thread-safe interactions with the grid to ensure
 * no two persons occupy the same field at the same time.
 */
public class Person implements Runnable {

    private PositionPair positionPair;
    private final Simulation simulation;
    private final boolean isCaptain;

    private final int id;
    private static int idCounter = 0;

    private int waitCounter = 0;
    private int moveCounter = 0;
    private boolean running = true;

    /**
     * Returns the current position of the left foot.
     *
     * @return The current position of the left foot.
     */
    public Position getCurrentLeft() {
        return positionPair.getLeft();
    }

    /**
     * Returns the current position of the right foot.
     *
     * @return The current position of the right foot.
     */
    public Position getCurrentRight() {
        return positionPair.getRight();
    }

    /**
     * Returns the current position pair (left and right feet).
     *
     * @return The current position pair.
     */
    public PositionPair getPositionPair() {
        return positionPair;
    }

    /**
     * Returns the number of waiting steps performed by this person.
     *
     * @return The number of waiting steps.
     */
    public int getWaitCounter() {
        return waitCounter;
    }

    /**
     * Returns the number of movement steps performed by this person.
     *
     * @return The number of movement steps.
     */
    public int getMoveCounter() {
        return moveCounter;
    }

    /**
     * Returns the unique identification number of this person.
     *
     * @return The unique ID of this person.
     */
    public int getId() {
        return id;
    }

    /**
     * Constructs a person with a given initial position and simulation context.
     *
     * @param pos The initial position pair of the person's feet.
     * @param simulation The simulation context in which this person exists.
     */
    public Person(PositionPair pos, Simulation simulation, boolean isCaptain) {
        this.positionPair = pos;
        this.simulation = simulation;
        this.isCaptain = isCaptain;

        synchronized (this) {
            id = ++idCounter;
        }
    }

    /**
     * The run method is executed when the thread starts. It manages the movement and waiting logic for the person.
     * If a valid next position is found, the person moves, then waits for a random time.
     * If no valid move is available, the person simply waits.
     * If the wait counter exceeds 64, or if the person reaches the Sammelpunkt, appropriate notifications are made.
     */
    @Override
    public void run() {
        while (running) {
            PositionPair nextPositionPair = simulation.generateNextPositionPairs(this);

            if (nextPositionPair == null) {
                waitCounter++;
                if (isCaptain) {
                    simulation.printToFile();
                }
                if (waitCounter >= 64) {
                    simulation.notifySammelpunkt(this);
                    running = false; // Stop this thread
                    break;
                }
                waitRandomTime();
            } else {
                positionPair = nextPositionPair;
                System.out.println("Person " + id + " moved to " + nextPositionPair);
                moveCounter++;
                if (isCaptain) {
                    simulation.printToFile();
                }
                waitRandomTime();
            }

            if (simulation.isSammelpunkt(getCurrentLeft()) || simulation.isSammelpunkt(getCurrentRight())) {
                simulation.notifySammelpunkt(this);
                running = false; // Stop this thread
                break;
            }
        }
        System.out.println("Person " + id + " terminated.");
    }


    /**
     * Terminates the person's movement by setting the running flag to false.
     */
    public void terminate() {
        running = false;
    }

    /**
     * Causes the person to wait for a random amount of time between 5 and 50 milliseconds.
     * This simulates random pauses in the person's movement.
     */
    private void waitRandomTime() {
        try {
            Random rand = new Random();
            int waitTime = rand.nextInt(46) + 5; // Wait between 5 and 50 ms
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public String toString() {
        return "Person ID: " + id + "PositionPair: " + positionPair;
    }
}
