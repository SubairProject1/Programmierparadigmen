import java.util.Iterator;

/**
 * A generic container class that implements the ApprovableSet interface. This container holds entries of type X
 * and criteria of type P, where X is a subtype of Approvable and T is a subtype of Admin. The entries and criteria
 * are stored in linked lists with dummy nodes (nil) to simplify list operations.
 *
 * @param <X> the type of the entries in the container, which must be a subtype of Approvable
 * @param <P> the type of the criteria used for approval
 * @param <T> the type of the approved results, which must be a subtype of Admin
 */
public class AdminSet<X extends Approvable<P, T>, P, T extends Admin<? super X, T>> implements ApprovableSet<X, P, T> {



    private class EntryNode {
        X value;
        EntryNode next;

        EntryNode(X value) {
            this.value = value;
        }
    }

    private class CriterionNode {
        P value;
        CriterionNode next;

        CriterionNode(P value) {
            this.value = value;
        }
    }

    // entry that is not in the list
    private EntryNode nilEntry = new EntryNode(null);
    // criterion that is not in the list
    private CriterionNode nilCriterion = new CriterionNode(null);

    /**
     * Adds an entry to the container if it is not already present.
     *
     * @param x the entry to add
     */
    @Override
    public void add(X x) {
        if (!containsEntry(x)) {
            EntryNode newNode = new EntryNode(x);
            EntryNode nextNode = nilEntry.next;
            nilEntry.next = newNode;
            newNode.next = nextNode;
        }
    }

    @Override
    public void remove(X x) {
        EntryNode current = nilEntry;
        while (current.next != null) {
            if (current.next.value.equals(x)) {
                current.next = current.next.next; // Bypass the node to remove it
                return; // Exit the method once the entry is removed
            }
            current = current.next;
        }
    }

    private boolean containsEntry(X x) {
        EntryNode current = nilEntry.next;
        while (current != null) {
            if (current.value.equals(x)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Adds a criterion to the container if it is not already present.
     *
     * @param p the criterion to add
     */
    @Override
    public void addCriterion(P p) {
        if (!containsCriterion(p)) {
            CriterionNode newNode = new CriterionNode(p);
            CriterionNode nextNode = nilCriterion.next;
            nilCriterion.next = newNode;
            newNode.next = nextNode;
        }
    }

    private boolean containsCriterion(P p) {
        CriterionNode current = nilCriterion.next;
        while (current != null) {
            if (current.value.equals(p)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Extends each approved object by adding the entry to its approved result.
     * For each criterion returned by the criterions iterator and each entry returned by the iterator(p) iterator,
     * this method performs the operation: x.approve(p, x.approved(p).add(x));
     */
    public void extend() {
        this.criterions().forEachRemaining(p ->
                this.iterator(p).forEachRemaining(x ->
                        x.approve(p, x.approved(p).add(x))
                )
        );
    }

    /**
     * Shortens each approved object by removing the entry from its approved result.
     * For each criterion returned by the criterions iterator and each entry returned by the iterator(p) iterator,
     * this method performs the operation: x.approve(p, x.approved(p).remove(x));
     */
    public void shorten() {
        this.criterions().forEachRemaining(p ->
                this.iterator(p).forEachRemaining(x ->
                        x.approve(p, x.approved(p).remove(x))
                )
        );
    }

    /**
     * Returns an iterator over all entries in the container, iterating in any order.
     * The entries included in the iteration are those that were added using the add method.
     *
     * @return an Iterator over all entries in the container
     */
    @Override
    public Iterator<X> iteratorAll() {
        return new EntryIterator(nilEntry);
    }

    /**
     * Returns an iterator over all entries in the container that match the given criterion.
     * The entries included in the iteration are those that were added using the add method and for which
     * x.approved(p) returns a non-null result.
     *
     * @param p the criterion used to filter entries
     * @return an Iterator over all entries in the container that match the criterion
     */
    @Override
    public Iterator<X> iterator(P p) {
        return new FilteredEntryIterator(nilEntry.next, x -> x.approved(p) != null);
    }

    /**
     * Returns an iterator over all entries in the container that do not match the given criterion.
     * The entries included in the iteration are those that were added using the add method and for which
     * x.approved(p) returns null.
     *
     * @param p the criterion used to filter entries
     * @return an Iterator over all entries in the container that do not match the criterion
     */
    @Override
    public Iterator<X> iteratorNot(P p) {
        return new FilteredEntryIterator(nilEntry.next, x -> x.approved(p) == null);
    }

    /**
     * Returns an iterator over all entries in the container that match all criteria.
     * The entries included in the iteration are those that were added using the add method and for which
     * x.approved(p) returns a non-null result for every criterion added via addCriterion.
     *
     * @return an Iterator over all entries in the container that match all criteria
     */
    @Override
    public Iterator<X> iterator() {
        return new FilteredEntryIterator(nilEntry.next, x -> {
            CriterionNode current = nilCriterion.next;
            while (current != null) {
                if (x.approved(current.value) == null) {
                    return false;
                }
                current = current.next;
            }
            return true;
        });
    }

    /**
     * Returns an iterator over all criteria in the container, iterating in any order.
     * The criteria included in the iteration are those that were added using the addCriterion method.
     *
     * @return an Iterator over all criteria in the container
     */
    @Override
    public Iterator<P> criterions() {
        return new CriterionIterator(nilCriterion);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Entries:\n");
        for (X x : this) {
            sb.append(x).append("\n");
        }
        sb.append("Criteria:\n");
        for (Iterator<P> it = this.criterions(); it.hasNext(); ) {
            P p = it.next();
            sb.append(p).append("\n");
        }
        return sb.toString();
    }

    private class EntryIterator implements Iterator<X> {
        private EntryNode nil;
        private EntryNode previous;
        private EntryNode lastReturned;
        private EntryNode current;

        EntryIterator(EntryNode nil) {
            this.nil = nil;
            this.previous = null;
            this.lastReturned = null;
            this.current = nil.next;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public X next() {
            if (!hasNext()) {
                throw new IllegalStateException("No more elements");
            }
            previous = lastReturned;
            lastReturned = current;
            current = current.next;
            return lastReturned.value;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException("No element to remove");
            }

            if (previous == null) {
                nil.next = current;
            } else {
                previous.next = current;
            }

            lastReturned = null;
        }
    }

    private class CriterionIterator implements Iterator<P> {
        private CriterionNode nil;
        private CriterionNode previous;
        private CriterionNode lastReturned;
        private CriterionNode current;

        CriterionIterator(CriterionNode nil) {
            this.nil = nil;
            this.previous = null;
            this.lastReturned = null;
            this.current = nil.next;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public P next() {
            if (!hasNext()) {
                throw new IllegalStateException("No more elements");
            }
            previous = lastReturned;
            lastReturned = current;
            current = current.next;
            return lastReturned.value;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException("No element to remove");
            }

            if (previous == null) {
                nil.next = current;
            } else {
                previous.next = current;
            }

            lastReturned = null;
        }
    }

    private class FilteredEntryIterator implements Iterator<X> {
        private EntryNode nil;
        private EntryNode previous;
        private EntryNode lastReturned;
        private EntryNode current;
        private final java.util.function.Predicate<X> filter;

        FilteredEntryIterator(EntryNode nil, java.util.function.Predicate<X> filter) {
            this.nil = nil;
            this.previous = null;
            this.lastReturned = null;
            this.current = nil.next;
            this.filter = filter;
            moveToNextValid();
        }

        private void moveToNextValid() {
            while (current != null && !filter.test(current.value)) {
                current = current.next;
            }
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public X next() {
            if (!hasNext()) {
                throw new IllegalStateException("No more elements");
            }
            previous = lastReturned;
            lastReturned = current;
            current = current.next;
            moveToNextValid();
            return lastReturned.value;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException("No element to remove");
            }

            if (previous == null) {
                nil.next = current;
            } else {
                previous.next = current;
            }

            lastReturned = null;
        }
    }
}
