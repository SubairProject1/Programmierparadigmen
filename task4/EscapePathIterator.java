import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class EscapePathIterator implements Iterator<Space> {
    private int index = 0;
    private ArrayList<Space> escapePath;

    public EscapePathIterator(ArrayList<Space> escapePath) {
        this.escapePath = escapePath;
    }

    /**
     * Returns true if the iteration has more elements.
     * @return true if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return index < escapePath.size();
    }

    /**
     * Returns the next element in the iteration.
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public Space next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return escapePath.get(index++);
    }
}
