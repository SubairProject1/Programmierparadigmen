import java.util.ArrayList;

/**
 * Circulation represents the areas designed for movement within and outside a building,
 * such as hallways, staircases, and access paths. Every area intended for human occupancy,
 * whether short-term or long-term, must be accessible, meaning there must be a (not too long)
 * path through which this area can be entered.
 */
public interface Circulation extends Space {

    /**
     * Retrieves a list of rooms that are connected through this circulation space.
     * In a valid circulation space, there must be at least two connected rooms.
     *
     * @return an ArrayList of connected rooms
     * @throws IllegalStateException if the connected rooms have an illegal state depending on what the implemented room requires such as less than two rooms are connected or all rooms are on the same floor
     */
    ArrayList<Space> getConnectedSpaces();

    void addConnectedSpace(Space space);

    default Escape escape() {
        Escape escape = new Escape(this);
        if(escape.length() < 10) {
            return escape;
        }
        return null;
    }

    /**
     * Checks if the circulation space is a valid circulation space.
     * A valid circulation space must connect at least two spaces.
     *
     * @return true if the circulation space is valid, false otherwise
     */
    boolean isCirculation();


    boolean isExit();
}

