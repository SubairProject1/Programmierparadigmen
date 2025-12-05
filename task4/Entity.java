/**
 * An Entity is a built unit such as a Building, Complex, Ensemble or the whole City.
 * Objects of Entity are not of Type Space, however they can contain Spaces where people live.
 *
 * The Types of Entities can change after adding or removing other entities.
 * An Entity has a permit that can be given or deleted (for example after removal from an Ensemble).
 */

public interface Entity {

    /**
     * Adds an entity to this.
     * For example add a building to a complex, add a complex to an ensemble, etc.
     * The Entity added has to be of the correct type and have a permit.
     * The type of this can be changed after adding an entity. If a building is added to another Building, the building
     * is transformed into a Complex.
     *
     * Returns the Entity with the added entity or the new entity if the type is changed after adding.
     * Throws an IllegalArgumentException if the entity is not of the correct type or doesn't have a permit. (See implementation for details.
     *
     * @param entity the entity to be added
     * @throws IllegalArgumentException if the entity is not of the correct type or doesn't have a permit
     * @return the entity that was added or the entity that was created as a result of adding entity
     */
    Entity addEntity(Entity entity);

    /**
     * Removes an entity from this.
     * For example remove a building from a complex, remove a complex from an ensemble, etc.
     * The Entity removed has to be of the correct type and be in this.
     * The type of this can be changed after removing an entity. If only one Entity remains in a Complex or an Ensemble after removal, it is changed into the remaining Entity.
     * The permit of the removed entity is deleted.
     *
     * Returns the Entity with the removed entity or the new entity if the type is changed after removal.
     * Throws an IllegalArgumentException if the entity is not of the correct type or is not in this. (See implementation for details)
     *
     * @param entity the entity to be removed
     * @return the entity that was removed or a new entity if the type is changed after removal, or null if entity is not in this
     */
    Entity removeEntity(Entity entity);

    /**
     * Checks if the entity has a permit.
     *
     * @return true if the entity has a permit, false otherwise
     */
    boolean hasPermit();

    /**
     * Revokes the permit for the entity.
     */
    void deletePermit();

    /**
     * Grants a permit to the entity.
     */
    void givePermit();


}
