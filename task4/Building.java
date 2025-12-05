import java.util.ArrayList;
import java.util.List;

/**
 * A building is an Entity and a structure designed to enclose accessible spaces that allow for human occupancy (and may fulfill other functions).
 * Two structures are separate buildings if each can be removed and the other remains a complete building.
 * Otherwise, it is a single building composed of multiple structures.
 */

public class Building implements Entity {

    private List<Space> spaces;


    private boolean hasPermit = true;

    // Constructors

    /**
     * Default constructor for the Building class.
     * Initializes the spaces list as an empty ArrayList.
     */
    public Building() {
        spaces = new ArrayList<>();
    }

    /**
     * Constructor for the Building class with a list of spaces.
     * Initializes the spaces list with the provided list of spaces.
     *
     * @param spaces the list of spaces to initialize the building with
     */
    public Building(List<Space> spaces) {
        this.spaces = spaces;
    }

    /**
     * Adds a list of new spaces to the building.
     *
     * @param newSpaces the list of new spaces to be added
     */
    public void addSpace(List<Space> newSpaces) {
        spaces.addAll(newSpaces);
    }

    /**
     * Adds a single space to the building.
     *
     * @param space the space to be added
     */
    public void addSpace(Space space) {
        spaces.add(space);
    }

    /**
     * Returns the list of spaces (Interior and Exterior) contained in the building.
     *
     * @return spaces
     */
    public List<Space> spaces() {
        return spaces;
    }

    /**
     * Adds a Building to this Building and transforms it into a Complex.
     * Returns the new Complex with the two buildings.
     * Throws an IllegalArgumentException if the building doesn't have a permit or the entity to be added is not a Building.
     *
     * @param entity the Building to be added
     * @throws IllegalArgumentException if the entity is not a Building
     * @return new complex after the building is added
     */
    @Override
    public Entity addEntity(Entity entity) {

        // if the entity is a building and the building has a permit
        if(entity instanceof Building && entity.hasPermit()) {

            // create a new complex with the two buildings and no exterior spaces
            List<Building> buildings = new ArrayList<>();
            buildings.add(this);
            buildings.add((Building) entity);

            Complex complex = new Complex(new ArrayList<>(buildings), new ArrayList<>());

            return complex; // return the new complex
        }
        throw new IllegalArgumentException("Cannot add non-building or building without permit to a building.");

    }

    /**
     * Throws an UnsupportedOperationException because a building cannot be removed from another building.
     *
     * @param building the building to be removed
     * @throws UnsupportedOperationException always
     * @return nothing
     */
    @Override
    public Entity removeEntity(Entity building) {

        throw new UnsupportedOperationException("Cannot remove a building from a building.");
    }

    /**
     * Checks if the building has a permit.
     *
     * @return true if the building has a permit, false otherwise
     */
    @Override
    public boolean hasPermit() {
        return hasPermit;
    }

    /**
     * Revokes the permit for the building.
     */
    @Override
    public void deletePermit() {
        hasPermit = false;
    }

    /**
     * Grants a permit to the building.
     */
    @Override
    public void givePermit() {
        hasPermit = true;
    }
}
