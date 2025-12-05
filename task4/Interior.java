/**
 * Represents an interior room within a building that is specifically designed for human occupancy and includes additional functionality beyond a basic room.
 * This interface extends the Room interface, adding methods to check for alternative escape routes and connection to lifts.
 *
 * Unlike basic rooms, which are simply enclosed spaces with recognizable boundaries and primarily accessible through doors, interior rooms can additionally have windows.
 * These windows provide natural lighting and ventilation, enhancing the living conditions within the space.
 * Lifts, although considered interior rooms, typically do not have windows but are still essential components for vertical movement within buildings.
 */
public interface Interior extends Room {

    /**
     * Returns the total area (in square meters) of alternative escape routes, such as sufficiently sized openable windows that can be used in emergencies.
     * By default, this method returns 0.0.
     *
     * @return the sum of the areas of alternative escape routes in square meters
     */
    default double alternativeEscape() {
        return 0.0d;
    }

    /**
     * Checks if the interior room is connected to a lift.
     *
     * @return true if the room is connected to a lift, false otherwise
     */
    Boolean isConnectedToLift();
}

