/**
 * A generic interface with two type parameters, P and T, providing methods to approve objects based on a given criterion
 * and retrieve the approved object for the given criterion. The results and behavior of these methods depend on the
 * criterion provided and the state of the instance, without modifying them.
 *
 * @param <P> the type of the input parameter for the methods, representing the criterion
 * @param <T> the type of the approved object and the return type of the methods
 */
public interface Approvable<P, T> {
    /**
     * Returns the approved object for the given criterion.
     *
     * @param p the criterion
     * @return the approved object or null if no such object exists
     */
    T approved(P p);

    /**
     * Approves the object for the given criterion.
     *
     * @param p the criterion, must not be null
     * @param t the object to approve, can be null
     */
    void approve(P p, T t);
}