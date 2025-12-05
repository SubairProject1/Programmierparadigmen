import java.util.ArrayList;
import java.util.List;

/**
 * An Ensemble is an Entity and a loose combination of multiple buildings or building complexes perceived as a unit, even though they have separate entrances.
 * For example, buildings along a street or around a square form an ensemble if the street or square is recognized as a distinct area by the buildings.
 * Scattered, neighboring buildings with common features also form an ensemble if they are recognized as a unit.
 */

public class Ensemble implements Entity {

    private List<Entity> entities;
    private boolean hasPermit = true;

    /**
     * Constructs an Ensemble with an empty list of entities. Entities are to be added using the addEntity method.
     */
    public Ensemble() {
        entities = new ArrayList<>();
    }

    /**
     * Returns the set of Buildings and/or Complexes that belong to the ensemble.
     *
     * @return the set of buildings or complexes in the ensemble
     */
    public List<Entity> entities() {
        return entities;
    }

    /**
     * Returns the Space enclosed by the ensemble, or null if it does not exist.
     *
     * @return the area enclosed by the ensemble, or null
     */
    public Space space() {
        return null;
    }

    /**
     * Adds an Entity to the Ensemble.
     * If the entity is a Building or Complex and has a permit, it is added to the list of entities in the Ensemble.
     *
     * Returns the Ensemble with the added entity.
     * Throws an IllegalArgumentException if the entity is not a Building or Complex or doesn't have a permit.
     *
     * @param entity the entity to be added to the Ensamble
     * @throws IllegalArgumentException if the entity is not a Building or Complex or doesn't have a permit
     * @return the Ensemble with the added entity
     */
    @Override
    public Entity addEntity(Entity entity) {

        if ((entity instanceof Building || entity instanceof Complex) && entity.hasPermit()){

            entities.add(entity);
            return this;
        }
        throw new IllegalArgumentException("Entity is not a Building or Complex or doesn't have a permit");
    }

    /**
     * Removes an Entity from the Ensemble, if the entity is a Building or Complex and is present in the Ensemble.
     * The entity is removed from the list of entities in the Ensemble and its permit is deleted.
     * If only one entity is left in the Ensemble after removal, the Ensemble is transformed into that entity.
     *
     * Returns the Ensemble with the removed entity, or the single remaining Entity if only one is left.
     * Throws an IllegalArgumentException if entity is not a Building or Complex or is not in the Ensemble.
     *
     * @param entity the entity to be removed from the Ensemble
     * @throws IllegalArgumentException if the entity is not a Building or Complex or is not in the Ensemble
     * @return the Ensemble with the removed entity, or the single remaining Entity if only one is left
     */
    @Override
    public Entity removeEntity(Entity entity) {
        if((entity instanceof Building || entity instanceof Complex) && entities.contains(entity)){

            entities.remove(entity);
            entity.deletePermit();
            if(entities.size() == 1){ // if only one entity left

                Entity myEntity = entities.get(0); // ensemble is transformed to Building or Complex
                return myEntity;
            }
            return this; // entity removed
        }
        throw new IllegalArgumentException("Entity is not a Building or Complex or is not in the Ensemble");
    }

    /**
     * Returns false as Ensembles do not support permits.
     *
     * @return false, as this implementation does not support permits
     */
    @Override
    public boolean hasPermit() {
        return false;
    }

    /**
     * Deletes the permit of the Ensemble.
     * This implementation does nothing as permits are not supported.
     */
    @Override
    public void deletePermit() {
        hasPermit = false;
    }

    /**
     * Gives a permit to the Ensemble.
     * This implementation does nothing as permits are not supported.
     */
    @Override
    public void givePermit() {
        hasPermit = true;
    }
}
