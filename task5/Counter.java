/**
 * A class whose objects count method calls for testing purposes. Counter implements Approvable, passing
 * its type parameter T to Approvable and replacing P with Counter (with the appropriate type for the type parameter).
 * Objects of Counter contain a count variable. A call to x.approved(y) increases x's count variable by 1000 and y's
 * count variable by 1. The value set via approve or the constructor is returned. Calls to approve behave as described
 * in Approvable.
 *
 * @param <T> the type parameter passed to Approvable
 */
public class Counter<T> implements Approvable<Counter<T>, T> {

    private int count = 0;
    private T value;

    /**
     * Constructs a Counter object with the specified value.
     *
     * @param value the value to set for this Counter
     */
    public Counter(T value) {
        this.value = value;
    }

    /**
     * Increases the count variable of this Counter by 1000 and the count variable of the specified Counter by 1.
     * Returns the value set via approve or the constructor.
     *
     * @param tCounter the Counter whose count variable is to be incremented
     * @return the value set via approve or the constructor
     */
    @Override
    public T approved(Counter<T> tCounter) {
        this.count += 1000;
        tCounter.count++;
        return value;
    }

    /**
     * Sets the value of this Counter to the specified value.
     *
     * @param p the Counter whose value is to be set
     * @param t the value to set
     */
    @Override
    public void approve(Counter<T> p, T t) {
        this.value = t;
    }

    /**
     * Returns the content of the count variable as a string.
     *
     * @return the content of the count variable as a string
     */
    @Override
    public String toString() {
        return Integer.toString(count);
    }
}

