import java.util.Iterator;

public class Path<X> implements Admin<X, Path<X>>, Iterable<X> {
    private Node<X> head;

    /**
     * Adds an element to this Path if it is not already present.
     *
     * @param x the element to be addded
     * @return the updated path
     */
    @Override
    public Path<X> add(X x) {
        if(!contains(x)) {
            Node<X> node = new Node<>(x);
            node.next = head;
            head = node;
        }
        return this;
    }

    /**
     * Removes an element from the path if it is present. Otherwise, the path remains unchanged.
     *
     * @param x the element to be removed
     * @return the updated path
     */
    @Override
    public Path<X> remove(X x) {
        if (head == null) {
            return this;
        }
        if (head.value.equals(x)) {
            head = head.next;
            return this;
        }
        Node<X> current = head;
        while (current.next != null) {
            if (current.next.value.equals(x)) {
                current.next = current.next.next;
                return this;
            }
            current = current.next;
        }
        return this;
    }

    /**
     * Returns an iterator over the elements in this path, in the order they were added.
     *
     * @return an iterator over the elements in this path
     */
    @Override
    public Iterator<X> iterator() {
        return new PathIterator();
    }

    private boolean contains(X x) {
        for (X y : this) {
            if (x.equals(y)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (X x : this) {
            sb.append(x).append(", ");
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("]");
        return sb.toString();
    }

    private static class Node<X> {
        X value;
        Node<X> next;
        Node(X value) {
            this.value = value;
        }
    }

    private class PathIterator implements Iterator<X> {
        private Node<X> current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public X next() {
            if(!hasNext()) {
                throw new IllegalStateException("No more elements");
            }
            X value = current.value;
            current = current.next;
            return value;
        }
    }
}
