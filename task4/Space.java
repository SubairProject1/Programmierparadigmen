import java.util.Set;

/**
 * Represents an area that, when integrated into a built unit, allows for the occupancy of people.
 * It can be a room but doesn't have to be. Often referred to as Space to include areas where transitions
 * are too diffuse to speak of room boundaries and rooms. Different objects of Space do not overlap;
 * they can touch at defined edges if they lie next to or on top of each other.
 *
 * The entity() method returns the smallest possible built unit the space is integrated into, or null
 * if it is not integrated anywhere and therefore unusable.
 * The escape() method returns an Escape object describing the shortest paths, or null if the space
 * is not integrated or is a public road.
 * The remove() method removes the space from its environment (making it unusable) and also all other
 * spaces that are no longer sufficiently well-connected afterward. It returns the set of removed spaces.
 * Removing a space can also change the shortest paths of other spaces. Methods for integrating spaces
 * into built units are provided in Entity. These methods can also change paths.
 */
public interface Space {

    /**
     * Returns the smallest possible built unit the space is integrated into.
     *
     * @return the enclosed entity, or null if not integrated
     */
    Entity entity();

    /**
     * Removes the space from its environment and returns the dependent spaces.
     *
     * @return a set of dependent spaces that are removed
     */
    Set<Space> remove();

    /**
     * Returns an Escape object describing the shortest paths from this space.
     *
     * @return the escape path, or null if not integrated or a public road
     */
    Escape escape();
}

