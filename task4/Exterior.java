import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Exterior represents an area outside of a building (exterior space).
 * It includes terraces, balconies, or loggias, as openings in the building
 * envelope are not closable, even though they are perceived as rooms
 * and integrated into the building.
 */
public class Exterior implements Space, Circulation {

    private Entity associatedEntity; // The entity (e.g., Building or Complex) to which this exterior space belongs
    private ArrayList<Space> connectedSpaces; // Spaces connected to this exterior space
    private Boolean isExit = false; // Whether this space serves as an exit

    /**
     * Constructs an Exterior space.
     */
    public Exterior() {
        this.connectedSpaces = new ArrayList<>();
    }

    /**
     * Constructs an Exterior space with exit status.
     *
     * @param isExit whether this space serves as an exit
     */
    public Exterior(Boolean isExit) {
        this.connectedSpaces = new ArrayList<>();
        assert this.isExit != null : "isExit must be set";
    }

    /**
     * Constructs an Exterior space.
     *
     * @param associatedEntity the entity (building or complex) this exterior space is part of
     */
    public Exterior(Entity associatedEntity) {
        // associatedEntity is not null
        assert associatedEntity != null : "associatedEntity cannot be null";
        this.associatedEntity = associatedEntity;
        this.connectedSpaces = new ArrayList<>();
    }

    /**
     * Constructs an Exterior space with exit status.
     *
     * @param associatedEntity the entity (building or complex) this exterior space is part of
     * @param isExit whether this space serves as an exit
     */
    public Exterior(Entity associatedEntity, Boolean isExit) {
        // associatedEntity is not null
        assert associatedEntity != null : "associatedEntity cannot be null";
        this.associatedEntity = associatedEntity;
        this.connectedSpaces = new ArrayList<>();
        this.isExit = isExit;
    }

    /**
     * Gets the entity this exterior space is part of.
     *
     * @return the associated entity
     */
    @Override
    public Entity entity() {
        // associatedEntity is not null
        assert this.associatedEntity != null : "associatedEntity should not be null";
        return associatedEntity;
    }

    /**
     * Gets the escape route for this exterior space.
     *
     * @return an Escape object representing the escape path
     */
    @Override
    public Escape escape() {
        return new Escape(this);
    }

    /**
     * Removes this exterior space and returns dependent spaces.
     *
     * @return a set of connected spaces
     */
    @Override
    public Set<Space> remove() {
        Set<Space> dependentSpaces = new HashSet<>(connectedSpaces);
        connectedSpaces.clear(); // Disconnect all connections
        // connectedSpaces is empty
        assert connectedSpaces.isEmpty() : "connectedSpaces should be empty after removal";
        return dependentSpaces;
    }

    /**
     * Connects another space to this exterior space.
     *
     * @param space the space to connect
     */
    public void addConnection(Space space) {
        // space is not null
        assert space != null : "space cannot be null";
        connectedSpaces.add(space);
        // connectedSpaces contains space
        assert connectedSpaces.contains(space) : "connectedSpaces should contain the newly added space";
    }

    /**
     * Gets the spaces connected to this exterior space.
     *
     * @return a list of connected spaces
     */
    public ArrayList<Space> getConnectedSpaces() {
        return connectedSpaces;
    }

    /**
     * Adds a connected space to this exterior space.
     *
     * @param space the space to add
     */
    @Override
    public void addConnectedSpace(Space space) {
        // space is not null
        assert space != null : "space cannot be null";
        connectedSpaces.add(space);
        // connectedSpaces contains space
        assert connectedSpaces.contains(space) : "connectedSpaces should contain the newly added space";
    }

    /**
     * Checks if this space is a circulation space.
     *
     * @return true if it is a circulation space
     */
    @Override
    public boolean isCirculation() {
        return true;
    }

    /**
     * Checks if this space is an exit.
     *
     * @return true if it is an exit
     */
    @Override
    public boolean isExit() {
        return isExit;
    }
}