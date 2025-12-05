import java.util.ArrayList;
import java.util.List;

/**
 * A Complex is an Entity and a combination of connected buildings with shared entrances.
 * One can move between buildings through interior spaces.
 * Exterior spaces like courtyards, that are not part of any building,
 * can also be part of the complex.
 */
public class Complex implements Entity {

    private List<Building> buildings;
    private List<Exterior> exteriorSpaces;
    private boolean hasPermit = true;

    // Constructors
    public Complex() {
        buildings = new ArrayList<>();
        exteriorSpaces = new ArrayList<>();
    }
    public Complex(List<Building> buildings, List<Exterior> exteriorSpaces) {
        this.buildings = buildings;
        this.exteriorSpaces = exteriorSpaces;
    }

    /**
     * Returns the list of buildings in the complex.
     *
     * @return the list of buildings in the complex
     */
    public List<Building> buildings() {

        return buildings;
    }

    /**
     * Returns the list of exterior spaces of the complex not enclosed by any building.
     *
     * @return the list of exterior spaces in the Complex
     */
    public List<Exterior> spaces() {

        return exteriorSpaces;
    }

    /**
     * Adds a Building to this Complex.
     * If the entity is a Building and has a permit, it is added to the list of buildings in the Complex.
     * Throws an IllegalArgumentException if the entity is not a Building or doesn't have a permit.
     *
     * @param entity the Building to be added to the Complex
     * @throws IllegalArgumentException if the entity is not a Building or doesn't have a permit
     * @return the Complex with the added Building
     */

    @Override
    public Entity addEntity(Entity entity) {
        // if the entity is a building and the building has a permit
        if (entity instanceof Building && entity.hasPermit()){

            buildings.add((Building) entity);
            return this;
        }

        throw new IllegalArgumentException("Can only add a building with a permit to a complex.");

    }

    /**
     * Removes a building from the Complex if entity is a Building and is in this.
     * The building is removed from the list of buildings in the Complex and its permit is deleted.
     * If only one Building is left in the Complex, the Complex is transformed into a Building.
     *
     * Returns the Building with the removed building, or the single remaining Building if only one is left.
     * Throws an IllegalArgumentException if entity is not a Building or is not in the Complex.
     *
     * @param entity the building to be removed from the Complex
     * @return the building with the removed building, or the single remaining building if only one is left
     * @throws IllegalArgumentException if the entity is not a Building or is not in the Complex
     */
    @Override
    public Entity removeEntity(Entity entity) {
        // if the entity is a building and the building is in the complex
        if (entity instanceof Building && buildings.contains(entity)) {

            buildings.remove(entity);
            entity.deletePermit();
            if (buildings.size() == 1) { // if only one building left

                Building myBuilding = buildings.get(0);

                // add exterior spaces to the building
                for (Exterior exteriorSpace : exteriorSpaces) {
                    myBuilding.addSpace(exteriorSpace);
                }
                buildings.clear();
                exteriorSpaces.clear();

                return myBuilding; // complex is transformed in building, return building
            }
            return this; // building removed

        } //building not removed
        throw new IllegalArgumentException("Can only remove a building from this complex.");

    }

    /**
     * Checks if the Complex has a permit.
     *
     * @return true if the Complex has a permit, false otherwise
     */
    @Override
    public boolean hasPermit() {

        return hasPermit;
    }

    /**
     * Revokes the permit for the Complex.
     */
    @Override
    public void deletePermit() {

        hasPermit = false;
    }

    /**
     * Grants a permit to the Complex.
     */
    @Override
    public void givePermit() {

        hasPermit = true;
    }

}
