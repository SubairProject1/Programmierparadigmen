import java.util.ArrayList;
import java.util.Set;

/**
 * PureCirculation represents an area that serves solely for circulation (i.e., movement)
 * and does not permit any other use. Pure circulation areas are present where many people
 * are on the move and may need to flee if necessary, without being hindered by other uses of the space.
 */
public class PureCirculation implements Circulation {
    ArrayList<Space> connectedSpaces = new ArrayList<Space>();

    /**
     * Constructs a PureCirculation with specified connected spaces.
     *
     * @param connectedSpaces the spaces connected to this circulation space
     */
    public PureCirculation(ArrayList<Space> connectedSpaces){
        this.connectedSpaces = connectedSpaces;
    }

    /**
     * Gets the spaces connected to this circulation space.
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
     * Indicates that this space is not an exit.
     *
     * @return false always, as pure circulation spaces are not exits
     */
    @Override
    public boolean isExit() {
        return false;
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
     * Provides an escape route for this circulation space.
     *
     * @return null as pure circulation spaces do not provide escape routes
     */
    @Override
    public Escape escape() {
        return null;
    }

    /**
     * Removes the space and returns a null set of connected spaces.
     *
     * @return null as removal is not applicable for pure circulation
     */
    @Override
    public Set<Space> remove() {
        return null;
    }
}

