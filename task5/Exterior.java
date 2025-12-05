/**
 * Represents an exterior area, implementing the Approvable interface with a type parameter P.
 * The type parameter T of Approvable is replaced by Path<Space<P>>.
 * This class describes areas outside of buildings, with meanings dependent on the given values of type P.
 * The approved and approve methods follow the descriptions in Approvable.
 *
 * @param <P> the type parameter representing the approval criterion
 */
public class Exterior<P> extends Space<P> {

    private boolean isPublic;

    /**
     * Constructs an Exterior object with the specified description and public status.
     *
     * @param description a textual description of the exterior area
     * @param isPublic a boolean indicating whether the area is public
     */
    public Exterior(String description, boolean isPublic) {
        super(description);
        this.isPublic = isPublic;
    }

    /**
     * Returns whether the exterior area is public.
     *
     * @return true if the area is public, false otherwise
     */
    public boolean isPublic() {
        return isPublic;
    }

    @Override
    public String toString() {
        return super.toString() + (isPublic ? " (public)" : " (private)");
    }
}
