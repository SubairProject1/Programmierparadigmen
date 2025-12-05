/**
 * Represents an interior space, implementing the Approvable interface with a type parameter P.
 * The type parameter T of Approvable is replaced by Path<Space<P>>.
 * This class describes areas inside buildings, with meanings dependent on the given values of type P.
 * The approved and approve methods follow the descriptions in Approvable.
 * The area method returns the size of the interior space in square meters.
 *
 * @param <P> the type parameter representing the approval criterion
 */
public class Interior<P> extends Space<P> {

    private final double area;

    /**
     * Constructs an Interior object with the specified description and area.
     *
     * @param description a textual description of the interior space
     * @param area the size of the interior space in square meters
     */
    public Interior(String description, double area) {
        super(description); // Pass the description to the parent Space class
        this.area = area;
    }

    /**
     * Returns the size of the interior space in square meters.
     *
     * @return the size of the interior space in square meters
     */
    public double area() {
        return area;
    }
}

