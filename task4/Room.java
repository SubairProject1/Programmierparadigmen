/**
 * Represents a room that is an enclosed space within a larger structure.
 * This interface extends the Space interface and provides methods specific to rooms.
 *
 * A room is defined as an area enclosed by walls or other barriers, except for necessary openings like doors or windows.
 * The size of these openings does not determine whether an area is considered a room, but rather the clear boundaries that form its perimeter.
 *
 * @see Space
 */
public interface Room extends Circulation {

    /**
     * Returns the floor number of the room.
     *
     * @return the floor number
     */
    int getFloor();


    /**
     * Checks whether the room is an exit or not.
     * An exit is a room that directly leads out of the building to the public road.
     *
     * @return true if the room is an exit, false otherwise
     */
    boolean isExit();
}

