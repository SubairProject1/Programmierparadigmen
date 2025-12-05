import java.util.Set;

/**
 * A PublicRoad represents an area that is unrestrictedly accessible
 * and solely serves the purpose of movement (pedestrian and vehicular traffic).
 * Facilities such as parking lots, rows of trees, green strips, or benches
 * are not considered other uses but rather support certain forms of movement.
 * The escape() method always returns null.
 */
public class PublicRoad implements Space {

    public PublicRoad() {}

    /**
     * The public road isn't enclosed in any buildings unlike interior or exterior,
     * so it returns null instead of the smallest possible built unit in which the area is integrated.
     *
     * @return null as public roads are not enclosed in any buildings
     */
    @Override
    public Entity entity() {
        return null;
    }

    /**
     * Public roads do not require escape paths.
     *
     * @return null always, as public roads do not have escape paths
     */
    @Override
    public Escape escape() {
        return null;
    }

    /**
     * Removes the public road.
     *
     * @return an empty set as public roads cannot be removed
     */
    @Override
    public Set<Space> remove() {
        return Set.of();
    }
}
