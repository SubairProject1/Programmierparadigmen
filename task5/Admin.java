/**
 * A generic interface named Admin with type parameters X and T, providing methods whose results and the current instance
 * are of type T. These methods depend only on the current instance (this) and a parameter of type X, and
 * do not modify the instance or the parameter.
 *
 * @param <X> the type of the input parameter for the methods
 * @param <T> the type of the current instance and the return type of the methods
 */
public interface Admin<X, T> {
    /**
     * Adds an element to the collection.
     *
     * @param x the element to add
     * @return the collection
     */
    T add(X x);

    /**
     * Removes an element from the collection.
     *
     * @param x the element to remove
     * @return the collection
     */
    T remove(X x);
}
