import java.util.ArrayList;
import java.util.List;

/**
 * A city is an Entity and a large human settlement with Public roads and Entities. It doesn't need a permit to exist,
 * thus it can't be removed.
 */

public class City implements Entity {

    private List<Entity> entities;
    private List<PublicRoad> roads;

    public City() {
        entities = new ArrayList<>();
    }

    public List<Entity> entities() {
        return entities;
    }

    /**
     * Adds a public road to the City.
     *
     * @param road the public road to be added
     * @return the City with the added road
     */
    public Entity addRoad(PublicRoad road) {

        roads.add(road);
        return this;
    }

    /**
     * Adds an entity to the City.
     * If the entity is a Building, Complex, Ensemble and has a permit, it is added to the list of entities in the City.
     *
     * Returns the City with the added entity.
     * Throws an IllegalArgumentException if the entity is not a Building, Complex, Ensemble or doesn't have a permit.
     *
     * @param entity the entity to be added
     * @return
     */
    @Override
    public Entity addEntity(Entity entity) {

        if (entity instanceof City) throw new IllegalArgumentException("Cannot add a city to a city.");
        if (entity.hasPermit()) {

            entities.add(entity);
            return this;
        }
        throw new IllegalArgumentException("Cannot add an entity without a permit to a city.");
    }

    /**
     * Removes an entity from the City and removes its permit, if it is present in the City.
     * Otherwise, throws an IllegalArgumentException.
     *
     * Returns the City with the removed entity.
     *
     * @param entity the entity to be removed
     * @throws IllegalArgumentException if the entity is not in the city
     * @return the City with the removed entity
     */
    @Override
    public Entity removeEntity(Entity entity) {
        if (entities.contains(entity)) {

            entities.remove(entity);
            entity.deletePermit();
            return this;
        }
        throw new IllegalArgumentException("Cannot remove an entity that is not in the city.");
    }

    /**
     * A city doesn't need a permit to exit, thus return true.
     * @return true
     */
    @Override
    public boolean hasPermit() {

        return true;
    }

    /**
     * A city doesn't need a permit to exit, thus does nothing.
     */
    @Override
    public void deletePermit() {
        return;
    }

    /**
     * A city doesn't need a permit to exit, thus does nothing.
     */
    @Override
    public void givePermit() {

    }
}
