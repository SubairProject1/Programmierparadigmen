import java.util.ArrayList;
import java.util.Set;

/**
 * Represents a servant space designed for auxiliary tasks and short-term stays,
 * such as bathrooms, kitchens, utility rooms, etc. These spaces do not need to
 * meet the same quality standards as served spaces.
 */
public class ServantSpace implements Interior {

    private int floor;
    private Vec2 windowSize;
    private ArrayList<Space> connectedSpaces;

    private Boolean emergencyArisen; // not fixed

    /**
     * Constructs a ServantSpace with specified quality parameters.
     *
     * @param floor the floor number where the space is located
     * @param windowSize the window size dimensions
     * @throws IllegalArgumentException if the window size is too small for an alternative escape route
     */
    public ServantSpace(int floor, Vec2 windowSize) {
        // windowSize > 1.1
        if(windowSize.x() * windowSize.y() < 1.1d) {
            throw new IllegalArgumentException("No alternative escape route possible due to window size being too small. It should at least be 1.1m^2.");
        }

        this.floor = floor;
        this.windowSize = windowSize;
        emergencyArisen = false;
        connectedSpaces = new ArrayList<>();
    }

    /**
     * Indicates that a servant space cannot be connected to a lift.
     *
     * @return false always, as servant spaces cannot be connected to lifts
     */
    @Override
    public Boolean isConnectedToLift() {
        return false;
    }

    /**
     * Gets the spaces connected to this servant space.
     *
     * @return a list of connected spaces
     */
    @Override
    public ArrayList<Space> getConnectedSpaces() {
        return connectedSpaces;
    }

    /**
     * Adds a space to the list of connected spaces.
     *
     * @param space the space to connect
     */
    @Override
    public void addConnectedSpace(Space space) {
        connectedSpaces.add(space);
    }

    /**
     * Checks if the space functions as circulation.
     *
     * @return true if the space functions as circulation, false otherwise
     */
    @Override
    public boolean isCirculation() {
        // connectedSpaces.size() > 1
        return connectedSpaces.size() > 1;
    }

    /**
     * Gets the floor number where the space is located.
     *
     * @return the floor number
     */
    @Override
    public int getFloor() {
        return floor;
    }

    /**
     * Gets the associated entity of the space.
     *
     * @return the associated entity (currently returns null)
     */
    @Override
    public Entity entity() {
        return null;
    }

    /**
     * Provides an escape route if an emergency has arisen.
     *
     * @return an Escape object representing the escape path, or null if no emergency
     */
    @Override
    public Escape escape() {
        return null;
    }

    /**
     * Removes the space and returns an empty set of connected spaces.
     *
     * @return an empty set of spaces
     */
    @Override
    public Set<Space> remove() {
        return Set.of();
    }

    /**
     * Calculates the alternative escape route area.
     *
     * @return the area of the window size
     */
    @Override
    public double alternativeEscape() {
        // windowSize
        return windowSize.x() * windowSize.y();
    }

    /**
     * Checks if the space serves as an exit.
     *
     * @return true if the space is an exit, false otherwise
     */
    @Override
    public boolean isExit() {
        if(connectedSpaces == null)
            return false;
        for(Space space : connectedSpaces) {
            if(space instanceof PublicRoad) {
                return true;
            }
        }
        return false;
    }
}
