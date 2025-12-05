import java.util.Iterator;

/**
 * A generic interface with three type parameters, X, P, and T, extending java.lang.Iterable<X>.
 * An instance of this interface is a container with entries of types X and P, where X is a subtype of Approvable.
 * This ensures that for every entry x of type X and every entry p of type P, the method x.approved(p) can be invoked,
 * returning a result of type T (or null).
 *
 * @param <X> the type of the entries in the container, which must be a subtype of Approvable
 * @param <P> the type of the criteria used for approval
 * @param <T> the type of the approved results
 */
public interface ApprovableSet<X extends Approvable<P, T>, P, T> extends Iterable<X> {
    /**
     * Adds an element to the collection unless it's already been added by this method.
     *
     * @param x the element to add
     */
    void add(X x);

    /**
     * Removes an element from the collection.
     *
     * @param x the element to remove
     */
    void remove(X x);

    /**
     * Adds a criterion to the collection unless it's already been added by this method.
     *
     * @param p the criterion to add
     */
    void addCriterion(P p);

    /**
     * Returns an iterator over all entries in the container which have been added via add(), iterating in any order.
     *
     * @return an Iterator over all entries in the container
     */
    Iterator<X> iteratorAll();

    /**
     * Returns an iterator over all entries in the container which have been added via add() and for which
     * x.approved(p) returns a non-null result, iterating in any order.
     *
     * @param p the criterion used to filter entries
     * @return an Iterator over all entries in the container that match the criterion
     */
    Iterator<X> iterator(P p);

    /**
     * Returns an iterator over all entries in the container which have been added via add() and for which
     * x.approved(p) returns null, iterating in any order.
     *
     * @param p the criterion used to filter entries
     * @return an Iterator over all entries in the container that do not match the criterion
     */
    Iterator<X> iteratorNot(P p);

    /**
     * Returns an iterator over all entries in the container which have been added via add() and for which
     * x.approved(p) returns a non-null result for every entry p added via addCriterion, iterating in any order.
     *
     * @return an Iterator over all entries in the container that match the criteria for every entry added via addCriterion
     */
    @Override
    Iterator<X> iterator();

    /**
     * Returns an iterator over all entries in the container which have been added via addCriterion(), iterating in any order.
     *
     * @return an Iterator over all entries added via addCriterion()
     */
    Iterator<P> criterions();

    //void remove(X x);
}
