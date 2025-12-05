import java.io.Serializable;

/**
 * Represents the data of a person in the evacuation simulation.
 * The data includes a unique identification number, the number of movement steps
 * performed on the escape route, and the number of waiting steps performed.
 * This data is used to track the person's progress and performance during the simulation.
 */
public class PersonData implements Serializable {

    private final int id;
    private final int moveCounter;
    private final int waitCounter;

    /**
     * Constructs a new PersonData object with the specified ID, movement steps, and waiting steps.
     *
     * @param id The unique identification number of the person.
     * @param moveCounter The number of movement steps performed by the person.
     * @param waitCounter The number of waiting steps performed by the person.
     */
    public PersonData(int id, int moveCounter, int waitCounter) {
        this.id = id;
        this.moveCounter = moveCounter;
        this.waitCounter = waitCounter;
    }

    /**
     * Returns a string representation of the PersonData object, including the ID, movement steps, and waiting steps.
     *
     * @return A string representation of the PersonData object.
     */
    @Override
    public String toString() {
        return "Person ID: " + id + ", Moves: " + moveCounter + ", Waits: " + waitCounter;
    }
}
